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
package cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.components.hinters;

import java.io.Serializable;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.AbstractEvent;

/**
 * Událost změny hranic výběrového intervalu v textu.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
final class SelectionChangedEvent extends AbstractEvent<SelectionChangedListener> implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private final int start;
    private final int end;
    
    /**
     * Vytvoří událost.
     * 
     * @param start počátek
     * @param end konec
     * @return událost
     */
    public static <E> SelectionChangedEvent create(final int start, final int end) {
        return new SelectionChangedEvent(start, end);
    }
    
    private SelectionChangedEvent(final int start, final int end) {
        Preconditions.checkArgument(start >= 0);
        Preconditions.checkArgument(end >= start);
        
        this.start = start;
        this.end = end;
    }
    
    /**
     * Vrátí počátek.
     * 
     * @return počátek
     */
    public int getStart() {
        return this.start;
    }
    
    /**
     * Vrátí konec.
     * 
     * @return konec
     */
    public int getEnd() {
        return this.end;
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.Event#dispatchTo(java.lang.Object)
     */
    @Override
    public void dispatchTo(final SelectionChangedListener listener) {
        Preconditions.checkNotNull(listener);
        
        listener.changeSelection(this.start, this.end);
    }
}