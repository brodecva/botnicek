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
package cz.cuni.mff.ms.brodecva.botnicek.ide.designer.events.network;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.networks.NetworkInternalFrame;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.Event;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public class NodeMovedEvent implements Event<NodeMovedListener> {
    
    private final String name;
    private final int x;
    private final int y;
    
    public static NodeMovedEvent create(final String name, final int x, final int y) {
        return new NodeMovedEvent(name, x, y);
    }
    
    private NodeMovedEvent(final String name, final int x, final int y) {
        Preconditions.checkNotNull(name);
        Preconditions.checkArgument(!name.isEmpty());
        Preconditions.checkArgument(x > 0);
        Preconditions.checkArgument(y > 0);
        
        this.name = name;
        this.x = x;
        this.y = y;
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.utils.Event#dispatchTo(java.lang.Object)
     */
    @Override
    public void dispatchTo(final NodeMovedListener listener) {
        listener.nodeMoved(this.name, this.x, this.y);
    }

}
 