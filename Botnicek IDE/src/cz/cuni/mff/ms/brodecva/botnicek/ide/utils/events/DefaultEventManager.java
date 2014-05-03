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
package cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public class DefaultEventManager implements EventManager {
    
    private final Map<Class<? extends Event<?>>, Set<?>> eventToListeners = new HashMap<>();
    
    public static DefaultEventManager create() {
        return new DefaultEventManager();
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.utils.Dispatcher#fire(cz.cuni.mff.ms.brodecva.botnicek.ide.utils.Event)
     */
    @Override
    public <L> void fire(Event<L> event) {
        Preconditions.checkNotNull(event);
        
        @SuppressWarnings("unchecked")
        final Set<L> listeners = (Set<L>) this.eventToListeners.get(event.getClass());
        
        if (listeners == null) {
            return;
        }
        
        for (final L listener : listeners) {
            event.dispatchTo(listener);
        }
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.utils.EventRegister#addEventListener(java.lang.Class, java.lang.Object)
     */
    @Override
    public <L> void
            addEventListener(Class<? extends Event<L>> type, L listener) {
        Preconditions.checkNotNull(type);
        Preconditions.checkNotNull(listener);
        
        @SuppressWarnings("unchecked")
        Set<L> listeners = (Set<L>) this.eventToListeners.get(type);
        
        if (listeners == null) {
            listeners = new HashSet<>();
            this.eventToListeners.put(type, listeners);
        }
        
        listeners.add(listener);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.utils.EventRegister#removeEventListener(java.lang.Class, java.lang.Object)
     */
    @Override
    public <L> void removeEventListener(final Class<? extends Event<L>> type,
            L listener) {
        Preconditions.checkNotNull(type);
        Preconditions.checkNotNull(listener);
        
        @SuppressWarnings("unchecked")
        final Set<L> listeners = (Set<L>) this.eventToListeners.get(type);
        
        if (listeners == null) {
            return;
        }
        
        listeners.remove(listener);
    }

}
