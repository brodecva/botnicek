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

/**
 * Událost změny dostupné množiny vstupních uzlů sítí (do kterých je možné se zanořit).
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public class AvailableReferencesChangedEvent extends AbstractMappedEvent<System, AvailableReferencesChangedListener> {
    
    private final Set<EnterNode> references;
    
    /**
     * Vytvoří událost.
     * 
     * @param system rodičovský systém
     * @param references aktuální množina dostupných uzlů
     * @return událost
     */
    public static AvailableReferencesChangedEvent create(final System system, final Set<EnterNode> references) {
        return new AvailableReferencesChangedEvent(system, references);
    }
    
    private AvailableReferencesChangedEvent(final System system, final Set<EnterNode> references) {
        super(system);
        
        Preconditions.checkNotNull(references);
        
        this.references = ImmutableSet.copyOf(references);
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.utils.Event#dispatchTo(java.lang.Object)
     */
    @Override
    public void dispatchTo(final AvailableReferencesChangedListener listener) {
        listener.referencesChanged(this.references);
    }
}
 