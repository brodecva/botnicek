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
package cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.arcs;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.Direction;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.Network;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.NetworkInfo;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.api.Visitor;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.nodes.Node;


/**
 * @author Václav Brodec
 * @version 1.0
 */
public abstract class AbstractArc implements Arc {
    protected final static int DEFAULT_PRIORITY = 1;
    
    private final Network parent;
    private final String name;
    private final int priority;
    
    protected AbstractArc(final Network parent, final String name, final int priority) {
        Preconditions.checkNotNull(parent);
        Preconditions.checkNotNull(name);
        
        Preconditions.checkArgument(priority >= 0);
        
        this.parent = parent;
        this.name = name;
        this.priority = priority;
    }
    
    public final String getName() {
        return this.name;
    }
    
    public final boolean isFrom(final Node node) {
        return isAttached(node, Direction.OUT);
    }
    
    public final boolean isTo(final Node node) {
        return isAttached(node, Direction.IN);
    }
    
    public final boolean isAttached(final Node node, final Direction direction) {
        return getAttached(direction) == node;
    }
    
    public final Node getFrom() {
        return getAttached(Direction.OUT);
    }
    
    public final Node getTo() {
        return getAttached(Direction.IN);
    }
    
    public final Node getAttached(final Direction direction) {
        if (direction == null) {
            throw new NullPointerException();
        }
        
        return this.parent.getAttached(this, direction);
    }
    
    public final int getPriority() {
        return this.priority;
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.Visitable#accept(cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.Visitor)
     */
    @Override
    public final void accept(final Visitor visitor) {
        getTo().accept(visitor);
    }
    
    public final NetworkInfo getNetwork() {
        return this.parent;
    }
}
