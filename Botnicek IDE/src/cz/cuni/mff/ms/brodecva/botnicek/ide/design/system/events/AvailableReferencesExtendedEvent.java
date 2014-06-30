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
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.events;

import java.util.Set;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;

import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.EnterNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.AbstractMappedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.MappedEvent;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public class AvailableReferencesExtendedEvent extends AbstractMappedEvent<System, AvailableReferencesExtendedListener> {
    
    private final Set<EnterNode> additionalReferences;
    
    public static AvailableReferencesExtendedEvent create(final System system, final Set<EnterNode> additionalReferences) {
        return new AvailableReferencesExtendedEvent(system, additionalReferences);
    }
    
    private AvailableReferencesExtendedEvent(final System system, final Set<EnterNode> additionalReferences) {
        super(system);
        
        Preconditions.checkNotNull(additionalReferences);
        
        this.additionalReferences = ImmutableSet.copyOf(additionalReferences);
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.utils.Event#dispatchTo(java.lang.Object)
     */
    @Override
    public void dispatchTo(final AvailableReferencesExtendedListener listener) {
        listener.referencesExtended(this.additionalReferences);
    }
}
 