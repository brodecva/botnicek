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

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.events.ArcRemovedListener;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.AbstractMappedEvent;

/**
 * Událost odebrání hran ze systému.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public class ArcRemovedEvent extends AbstractMappedEvent<System, ArcRemovedListener> {
    
    private final Arc arc;
    
    /**
     * Vytvoří událost.
     * 
     * @param system rodičovský systém
     * @param arc odebraná hrana
     * @return událost
     */
    public static ArcRemovedEvent create(final System system, final Arc arc) {
        return new ArcRemovedEvent(system, arc);
    }
    
    private ArcRemovedEvent(final System network, final Arc arc) {
        super(network);
        
        Preconditions.checkNotNull(arc);
        
        this.arc = arc;
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.utils.Event#dispatchTo(java.lang.Object)
     */
    @Override
    public void dispatchTo(final ArcRemovedListener listener) {
        listener.arcRemoved(this.arc);
    }
}
 