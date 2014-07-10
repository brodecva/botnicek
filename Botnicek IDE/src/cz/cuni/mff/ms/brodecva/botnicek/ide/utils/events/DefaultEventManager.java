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

import java.util.Set;

import com.google.common.base.Preconditions;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.Sets;
import com.google.common.collect.Table;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public class DefaultEventManager implements EventManager {
    
    private final class MappedEventVisitor implements Visitor {
        
        private <K, L> void visitMapped(final MappedEvent<K, L> event) {
            @SuppressWarnings("unchecked")
            final Set<L> mappedListeners = (Set<L>) eventsAndKeysToListeners.get(event.getClass(), event.getKey());
            if (mappedListeners == null) {
                return;
            }
            
            for (final L listener : mappedListeners) {
                event.dispatchTo(listener);
            }
        }
        
        private <L> void visitCommon(final Event<L> event) {
            @SuppressWarnings("unchecked")
            final Class<? extends Event<L>> type = (Class<? extends Event<L>>) event.getClass();
            
            @SuppressWarnings("unchecked")
            final Set<L> listeners = (Set<L>) eventsToListeners.get(type);
            
            for (final L listener : listeners) {
                event.dispatchTo(listener);
            }
        }
        
        @Override
        public <L> void visit(final Event<L> event) {
            visitCommon(event);
        }

        @Override
        public <K, L> void visit(final MappedEvent<K, L> event) {
            visitMapped(event);
            visitCommon(event);
        }
    }

    private final Table<Class<? extends MappedEvent<?, ?>>, Object, Set<?>> eventsAndKeysToListeners = HashBasedTable.create();
    private final SetMultimap<Class<? extends Event<?>>, ?> eventsToListeners = HashMultimap.create();
    
    public static DefaultEventManager create() {
        return new DefaultEventManager();
    }
    
    private DefaultEventManager() {
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.utils.EventRegister#addEventListener(java.lang.Class, java.lang.Object)
     */
    @Override
    public <K, L> void
            addListener(final Class<? extends MappedEvent<K, L>> type, final K key, final L listener) {
        Preconditions.checkNotNull(type);
        Preconditions.checkNotNull(key);
        Preconditions.checkNotNull(listener);
        
        @SuppressWarnings("unchecked")
        final Set<L> listeners = (Set<L>) this.eventsAndKeysToListeners.get(type, key);
        if (listeners == null) {
            @SuppressWarnings("unchecked")
            final Set<L> newListeners = Sets.newHashSet(listener);
            this.eventsAndKeysToListeners.put(type, key, newListeners);
        } else {
            final boolean fresh = listeners.add(listener);
            Preconditions.checkArgument(fresh);
        }
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.utils.EventRegister#removeEventListener(java.lang.Class, java.lang.Object)
     */
    @Override
    public <K, L> void removeListener(final Class<? extends MappedEvent<K, L>> type, final K key,
            final L listener) {
        Preconditions.checkNotNull(type);
        Preconditions.checkNotNull(key);
        Preconditions.checkNotNull(listener);
        
        @SuppressWarnings("unchecked")
        final Set<L> listeners = (Set<L>) this.eventsAndKeysToListeners.get(type, key);
        Preconditions.checkArgument(listeners != null);
        
        final boolean contained = listeners.remove(listener);
        Preconditions.checkArgument(contained);
        
        if (listeners.isEmpty()) {
            this.eventsAndKeysToListeners.remove(type, key);
        }
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.EventRegister#addEventListener(java.lang.Class, java.lang.Object)
     */
    @Override
    public <L> void addListener(final Class<? extends Event<L>> type,
            final L listener) {
        Preconditions.checkNotNull(type);
        Preconditions.checkNotNull(listener);
        
        @SuppressWarnings("unchecked")
        final Set<L> listeners = (Set<L>) this.eventsToListeners.get(type);
        final boolean fresh = listeners.add(listener);
        Preconditions.checkArgument(fresh);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.EventRegister#removeEventListener(java.lang.Class, java.lang.Object)
     */
    @Override
    public <L> void removeListener(final Class<? extends Event<L>> type,
            final L listener) {
        Preconditions.checkNotNull(type);
        Preconditions.checkNotNull(listener);
        
        final boolean contained = this.eventsToListeners.remove(type, listener);
        Preconditions.checkArgument(contained);
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.Dispatcher#fire(cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.SimpleEvent)
     */
    @Override
    public <L> void fire(final Event<L> event) {
        Preconditions.checkNotNull(event);
        
        event.accept(new MappedEventVisitor());
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.EventRegister#removeAllListeners(java.lang.Class, java.lang.Object)
     */
    @Override
    public <K, L> void removeAllListeners(
            final Class<? extends MappedEvent<K, L>> type, final K key) {
        Preconditions.checkNotNull(type);
        Preconditions.checkNotNull(key);
        
        final Set<?> removed = this.eventsAndKeysToListeners.remove(type, key);
        Preconditions.checkArgument(removed != null);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.EventRegister#removeAllListeners(java.lang.Class)
     */
    @Override
    public <K, L> void removeAllListeners(final Class<? extends Event<L>> type) {
        Preconditions.checkNotNull(type);
        
        final Set<?> listeners = this.eventsToListeners.removeAll(type);
        Preconditions.checkArgument(!listeners.isEmpty());
    }
}
