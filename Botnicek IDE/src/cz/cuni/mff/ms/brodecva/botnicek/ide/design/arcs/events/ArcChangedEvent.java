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
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.events;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.AbstractMappedEvent;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public class ArcChangedEvent extends AbstractMappedEvent<Arc, ArcChangedListener> {
    
    private final Arc newArc;
    
    public static ArcChangedEvent create(final Arc arc, final Arc newArc) {
        return new ArcChangedEvent(arc, newArc);
    }
    
    private ArcChangedEvent(final Arc arc, final Arc newArc) {
        super(arc);
        
        Preconditions.checkNotNull(newArc);
        
        this.newArc = newArc;
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.utils.Event#dispatchTo(java.lang.Object)
     */
    @Override
    public void dispatchTo(final ArcChangedListener listener) {
        listener.arcChanged(this.newArc);
    }
}
 