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
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.Visitor;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.utils.Direction;


/**
 * @author Václav Brodec
 * @version 1.0
 */
public abstract class AbstractArc implements Arc {
    protected final static int DEFAULT_PRIORITY = 1;
    
    private final Network parent;
    private final NormalWord name;
    private final int priority;
    
    protected AbstractArc(final Network parent, final NormalWord name, final int priority) {
        Preconditions.checkNotNull(parent);
        Preconditions.checkNotNull(name);
        
        Preconditions.checkArgument(priority >= 0);
        
        this.parent = parent;
        this.name = name;
        this.priority = priority;
    }
    
    public final NormalWord getName() {
        return this.name;
    }
    
    public final boolean isFrom(final Node node) {
        return isAttached(node, Direction.OUT);
    }
    
    public final boolean isTo(final Node node) {
        return isAttached(node, Direction.IN);
    }
    
    public final boolean isAttached(final Node node, final Direction direction) {
        return getAttached(direction).equals(node);
    }
    
    public final Node getFrom() {
        return getAttached(Direction.OUT);
    }
    
    public final Node getTo() {
        return getAttached(Direction.IN);
    }
    
    public final Node getAttached(final Direction direction) {
        Preconditions.checkNotNull(direction);
        
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
    
    public final Network getNetwork() {
        return this.parent;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((parent == null) ? 0 : parent.hashCode());
        result = prime * result + priority;
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
        
        final AbstractArc other = (AbstractArc) obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        if (parent == null) {
            if (other.parent != null) {
                return false;
            }
        } else if (!parent.equals(other.parent)) {
            return false;
        }
        if (priority != other.priority) {
            return false;
        }
        return true;
    }
}
