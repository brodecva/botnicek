/**
 * Copyright Václav Brodec 2013.
 * 
 * This file is part of Botníček.
 * 
 * Botníček is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Botníček is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Botníček.  If not, see <http://www.gnu.org/licenses/>.
 */
package cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.nodes;

import java.util.Set;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.Direction;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.Network;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.NetworkInfo;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.api.Visitor;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.arcs.Arc;



/**
 * @author Václav Brodec
 * @version 1.0
 */
public abstract class AbstractNode implements Node {
    private final String name;
    private final Network parent;
    private final int x;
    private final int y;
        
    /**
     * @param name
     * @param y 
     * @param x 
     */
    protected AbstractNode(final String name, final Network parent, final int x, final int y) {
        Preconditions.checkNotNull(name);
        Preconditions.checkNotNull(parent);        
        Preconditions.checkArgument(!name.isEmpty());
        Preconditions.checkArgument(x > 0);
        Preconditions.checkArgument(y > 0);
        
        this.name = name;
        this.parent = parent;
        
        this.x = x;
        this.y = y;
    }
    
    protected AbstractNode(final Node original) {
        this.name = original.getName();
        this.parent = original.getParent();
        
        this.x = original.getX();
        this.y = original.getY();
    }
    
    public final String getName() {
        return this.name;
    }
    
    public final Network getParent() {
        return this.parent;
    }
    
    public final int getX() {
        return this.x;
    }
    
    public final int getY() {
        return this.y;
    }
    
    public final void accept(final Visitor visitor) {
        visitor.visitEnter(this);
        
        final Set<Arc> outs = this.parent.getOuts(this);
        for (final Arc arc : outs) {
            if (!visitor.visited(arc.getTo())) {
                arc.accept(visitor);
            }
        }
        
        visitor.visitExit(this);
    }
    
    public final Set<Arc> getOuts() {
        return this.parent.getOuts(this);
    }
    
    public final Set<Arc> getIns() {
        return this.parent.getIns(this);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.nodes.Node#getConnections(cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.Direction)
     */
    @Override
    public Set<Arc> getConnections(Direction direction) {
        return this.parent.getConnections(this, direction);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.nodes.Node#getDegree(cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.Direction)
     */
    @Override
    public int getDegree(final Direction direction) {
        return this.parent.getConnections(this, direction).size();
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.nodes.Node#getOutDegree()
     */
    @Override
    public int getOutDegree() {
        return getDegree(Direction.OUT);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.nodes.Node#getInDegree()
     */
    @Override
    public int getInDegree() {
        return getDegree(Direction.IN);
    }
    
    public NetworkInfo getNetwork() {
        return this.parent;
    }
}
