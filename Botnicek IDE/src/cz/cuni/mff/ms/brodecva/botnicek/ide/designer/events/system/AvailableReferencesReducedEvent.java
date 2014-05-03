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
package cz.cuni.mff.ms.brodecva.botnicek.ide.designer.events.system;

import java.util.Set;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;

import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.nodes.EnterNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.Event;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public class AvailableReferencesReducedEvent implements Event<AvailableReferencesReducedListener> {
    
    private final Set<EnterNode> removedReferences;
    
    public static AvailableReferencesReducedEvent create(final Set<EnterNode> removedReferences) {
        return new AvailableReferencesReducedEvent(removedReferences);
    }
    
    private AvailableReferencesReducedEvent(final Set<EnterNode> removedReferences) {
        Preconditions.checkNotNull(removedReferences);
        
        this.removedReferences = ImmutableSet.copyOf(removedReferences);
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.utils.Event#dispatchTo(java.lang.Object)
     */
    @Override
    public void dispatchTo(final AvailableReferencesReducedListener listener) {
        listener.referencesReduced(this.removedReferences);
    }

}
 