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
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model;

import java.util.Set;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.Visitor;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.utils.Direction;



/**
 * @author Václav Brodec
 * @version 1.0
 */
public abstract class AbstractNode implements Node {
    private final NormalWord name;
    private final Network parent;
    private final int x;
    private final int y;
        
    /**
     * @param name
     * @param y 
     * @param x 
     */
    protected AbstractNode(final NormalWord name, final Network parent, final int x, final int y) {
        Preconditions.checkNotNull(name);
        Preconditions.checkNotNull(parent);        
        Preconditions.checkArgument(x >= 0);
        Preconditions.checkArgument(y >= 0);
        
        this.name = name;
        this.parent = parent;
        
        this.x = x;
        this.y = y;
    }
    
    protected AbstractNode(final Node original) {
        this.name = original.getName();
        this.parent = original.getNetwork();
        
        this.x = original.getX();
        this.y = original.getY();
    }
    
    public final NormalWord getName() {
        return this.name;
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
            arc.accept(visitor);
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
    
    public Network getNetwork() {
        return this.parent;
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node#adjoins(cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node, cz.cuni.mff.ms.brodecva.botnicek.ide.design.utils.Direction)
     */
    @Override
    public boolean adjoins(Node neighbour, Direction direction) {
        return this.parent.adjoins(this, neighbour, direction);
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node#directsAt(cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node)
     */
    @Override
    public boolean pointsTo(final Node node) {
        return adjoins(node, Direction.OUT);
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node#isDirectedAtBy(cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node)
     */
    @Override
    public boolean isPointedAtBy(final Node node) {
        return adjoins(node, Direction.IN);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + name.hashCode();
        result = prime * result + parent.hashCode();
        result = prime * result + x;
        result = prime * result + y;
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        
        if (obj == null) {
            return false;
        }
        
        if (getClass() != obj.getClass()) {
            return false;
        }
        
        AbstractNode other = (AbstractNode) obj;
        if (!name.equals(other.name)) {
            return false;
        }
        if (!parent.equals(other.parent)) {
            return false;
        }
        if (x != other.x) {
            return false;
        }
        if (y != other.y) {
            return false;
        }
        return true;
    }
}
