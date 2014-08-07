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

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.Presence;

/**
 * <p>Výchozí implementace správce událostí.</p>
 * <p>Kromě toho, že realizuje odesílání zpráv o události tak, aby klient nemusel rozlišovat, jaký typ odesílá, tak dovoluje v případě potřeby neodebírat posluchač, aniž by docházelo k unikání paměti.</p>
 * <p>Přestože jsou instance této třídy serializovatelné, nelze toto garantovat pro vložené objekty.</p>
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class DefaultEventManager implements EventManager, Serializable {
    
    /**
     * <p>Návštěvník pro rozlišení typu události při vypravování posluchačům.</p>
     * <p>Díky němu je událost podle svého typu rozesílána správným posluchačům.</p>
     * <p>Při průchodu posluchači bere v potaz vlastnosti {@link WeakHashMap} při iteraci přes členy.</p>
     */
    private final class MappedEventVisitor implements Visitor {
        
        private <K, L> void visitMapped(final MappedEvent<K, L> event) {
            @SuppressWarnings("unchecked")
            final Map<K, Set<?>> keysTolisteners = (Map<K, Set<?>>) eventsAndKeysToListeners.get(event.getClass());
            if (Presence.isAbsent(keysTolisteners)) {
                return;
            }
            
            @SuppressWarnings("unchecked")
            final Set<L> mappedListeners = (Set<L>) keysTolisteners.get(event.getKey());
            if (Presence.isAbsent(mappedListeners)) {
                return;
            }
            
            final Set<L> listenersSnapshot = ImmutableSet.copyOf(mappedListeners);
            for (final L listener : listenersSnapshot) {
                event.dispatchTo(listener);
            }
        }
        
        private <L> void visitCommon(final Event<L> event) {
            @SuppressWarnings("unchecked")
            final Set<L> listeners = (Set<L>) eventsToListeners.get(event.getClass());
            if (Presence.isAbsent(listeners)) {
                return;
            }
            
            final Set<L> listenersSnapshot = ImmutableSet.copyOf(listeners);
            for (final L listener : listenersSnapshot) {
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
    
    private static final long serialVersionUID = 1L;

    private final Map<Class<? extends MappedEvent<?, ?>>, Map<?, Set<?>>> eventsAndKeysToListeners = new HashMap<>();
    private final Map<Class<? extends Event<?>>, Set<?>> eventsToListeners = new HashMap<>();
    
    /**
     * Vytvoří manažer událostí.
     * 
     * @return manažer
     */
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
        final Map<K, Set<?>> keysTolisteners = (Map<K, Set<?>>) this.eventsAndKeysToListeners.get(type);
        final Map<K, Set<?>> usedKeysToListeners;
        if (Presence.isAbsent(keysTolisteners)) {
            usedKeysToListeners = new WeakHashMap<>();
            this.eventsAndKeysToListeners.put(type, usedKeysToListeners);
        } else {
            usedKeysToListeners = keysTolisteners;
        }
        
        @SuppressWarnings("unchecked")
        final Set<L> listeners = (Set<L>) usedKeysToListeners.get(key);
        final Set<L> usedListeners;
        if (Presence.isAbsent(listeners)) {
            usedListeners = Collections.newSetFromMap(new WeakHashMap<L, Boolean>());
            usedKeysToListeners.put(key, usedListeners);
        } else {
            usedListeners = listeners;
        }
        
        final boolean fresh = usedListeners.add(listener);
        Preconditions.checkArgument(fresh);
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
        final Map<K, Set<?>> keysTolisteners = (Map<K, Set<?>>) this.eventsAndKeysToListeners.get(type);
        Preconditions.checkArgument(Presence.isPresent(keysTolisteners));
        
        @SuppressWarnings("unchecked")
        final Set<L> listeners = (Set<L>) keysTolisteners.get(key);
        Preconditions.checkArgument(Presence.isPresent(listeners));
        
        final boolean contained = listeners.remove(listener);
        Preconditions.checkArgument(contained);
        
        if (listeners.isEmpty()) {
            keysTolisteners.remove(key);
        }
        if (keysTolisteners.isEmpty()) {
            this.eventsAndKeysToListeners.remove(type);
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
        final Set<L> usedListeners;
        if (Presence.isAbsent(listeners)) {
            usedListeners = Sets.newSetFromMap(new WeakHashMap<L, Boolean>());
            this.eventsToListeners.put(type, usedListeners);
        } else {
            usedListeners = listeners;
        }
        
        final boolean fresh = usedListeners.add(listener);
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
        
        @SuppressWarnings("unchecked")
        final Set<L> listeners = (Set<L>) this.eventsToListeners.get(type);
        Preconditions.checkArgument(Presence.isPresent(listeners));
        
        final boolean contained = listeners.remove(listener);
        Preconditions.checkArgument(contained);
        
        if (listeners.isEmpty()) {
            this.eventsToListeners.remove(listeners);
        }
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
        
        Preconditions.checkNotNull(type);
        Preconditions.checkNotNull(key);
        
        @SuppressWarnings("unchecked")
        final Map<K, Set<?>> keysTolisteners = (Map<K, Set<?>>) this.eventsAndKeysToListeners.get(type);
        Preconditions.checkArgument(Presence.isPresent(keysTolisteners));
        
        keysTolisteners.remove(key);
        
        if (keysTolisteners.isEmpty()) {
            this.eventsAndKeysToListeners.remove(type);
        }
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.EventRegister#removeAllListeners(java.lang.Class)
     */
    @Override
    public <K, L> void removeAllListeners(final Class<? extends Event<L>> type) {
        Preconditions.checkNotNull(type);
        
        Preconditions.checkNotNull(type);
        
        this.eventsToListeners.remove(type);
    }
}
