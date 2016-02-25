/**
 * Copyright Václav Brodec 2014.
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
package cz.cuni.mff.ms.brodecva.botnicek.ide.utils.mvc;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.common.base.Preconditions;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.Sets;
import com.google.common.collect.Table;

import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Callback;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.Presence;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.Event;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.EventManager;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.MappedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.logging.LocalizedLogger;

/**
 * Pomocná třída, která poskytuje dědícím třídám základní prostředky pro správu
 * pohledů a interakci s událostmi zasílanými modelem.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @param <V>
 *            typ pohledu
 */
public abstract class AbstractController<V> implements Controller<V> {
    
    private static final Logger LOGGER = LocalizedLogger.getLogger(AbstractController.class);
    
    private final EventManager eventManager;
    private final Set<V> views = new HashSet<>();
    private final Table<Class<? extends MappedEvent<?, ?>>, Object, Set<?>> eventsAndKeysToStrongListenerRefs =
            HashBasedTable.create();
    private final SetMultimap<Class<? extends Event<?>>, ?> eventsToStrongListenerRefs =
            HashMultimap.create();

    /**
     * Postaví řadič na základě dodaného správce událostí, od kterého bude
     * přijímat a do kterého bude zasílat zprávy o událostech.
     * 
     * @param eventManager
     *            správce událostí
     */
    protected AbstractController(final EventManager eventManager) {
        Preconditions.checkNotNull(eventManager);

        this.eventManager = eventManager;
    }

    /**
     * Přidá posluchače na správci událostí a u sebe podrží silnou referenci,
     * která zamezí okamžitému zapomenutí správcem, který nedostupné posluchače
     * maže.
     * 
     * @param type
     *            typ události
     * @param listener
     *            posluchač
     */
    protected final <L> void addListener(final Class<? extends Event<L>> type,
            final L listener) {
        Preconditions.checkNotNull(type);
        Preconditions.checkNotNull(listener);

        this.eventManager.addListener(type, listener);
        addListenerStrongReference(type, listener);
    }

    /**
     * Zaregistruje posluchače pro daný klíč na správci událostí a u sebe podrží
     * silnou referenci, která zamezí okamžitému zapomenutí správcem, který
     * nedostupné posluchače maže.
     * 
     * @param type
     *            typ události
     * @param key
     *            klíč
     * @param listener
     *            posluchač
     */
    protected final <K, L> void addListener(
            final Class<? extends MappedEvent<K, L>> type, final K key,
            final L listener) {
        Preconditions.checkNotNull(type);
        Preconditions.checkNotNull(key);
        Preconditions.checkNotNull(listener);

        this.eventManager.addListener(type, key, listener);
        addListenerStrongReference(type, key, listener);
    }

    private <L> void addListenerStrongReference(
            final Class<? extends Event<L>> type, final L listener) {
        @SuppressWarnings("unchecked")
        final Set<L> listeners =
                (Set<L>) this.eventsToStrongListenerRefs.get(type);
        final boolean fresh = listeners.add(listener);
        Preconditions.checkArgument(fresh);
    }

    private <K, L> void addListenerStrongReference(
            final Class<? extends MappedEvent<K, L>> type, final K key,
            final L listener) {
        @SuppressWarnings("unchecked")
        final Set<L> listeners =
                (Set<L>) this.eventsAndKeysToStrongListenerRefs.get(type, key);
        if (Presence.isAbsent(listeners)) {
            @SuppressWarnings("unchecked")
            final Set<L> newListeners = Sets.newHashSet(listener);
            this.eventsAndKeysToStrongListenerRefs.put(type, key, newListeners);
        } else {
            final boolean fresh = listeners.add(listener);
            Preconditions.checkArgument(fresh);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.utils.mvc.Controller#addView(java
     * .lang.Object)
     */
    @Override
    public final void addView(final V view) {
        Preconditions.checkNotNull(view);

        final boolean fresh = this.views.add(view);
        Preconditions.checkArgument(fresh);
    }

    /**
     * Provede kód na všech registrovaných pohledech.
     * 
     * @param callback
     *            instrukce k provedení na pohledu
     */
    protected final void callViews(final Callback<? super V> callback) {
        final Set<V> viewsSnapshot = ImmutableSet.copyOf(this.views);

        for (final V view : viewsSnapshot) {
            try {
                callback.call(view);
            } catch (final RuntimeException e) {
                LOGGER.log(Level.WARNING, "ViewCallException", new Object[] {view, e.getMessage()});
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.utils.mvc.Controller#fill(java.lang
     * .Object)
     */
    @Override
    public void fill(final V view) {
    }

    /**
     * Předá správci požadavek na odpálení události.
     * 
     * @param event
     *            událost
     */
    protected final void fire(final Event<?> event) {
        Preconditions.checkNotNull(event);

        this.eventManager.fire(event);
    }

    /**
     * Vrátí užitý správce událostí.
     * 
     * @return užitý správce událostí
     */
    protected final EventManager getEventManager() {
        return this.eventManager;
    }

    /**
     * Provede odstranění posluchačů pro všechny posluchače registrované přes
     * tento řadič.
     * 
     * @param type
     *            typ události
     */
    protected final <L> void removeAllListeners(
            final Class<? extends Event<L>> type) {
        Preconditions.checkNotNull(type);

        @SuppressWarnings("unchecked")
        final Set<L> listeners =
                (Set<L>) this.eventsToStrongListenerRefs.get(type);
        Preconditions.checkArgument(Presence.isPresent(listeners));

        final Iterator<L> listenerIterator = listeners.iterator();
        while (listenerIterator.hasNext()) {
            this.eventManager.removeListener(type, listenerIterator.next());
            listenerIterator.remove();
        }
    }

    /**
     * Provede odstranění posluchačů pro všechny posluchače registrované přes
     * tento řadič.
     * 
     * @param type
     *            typ události
     * @param key
     *            klíč
     */
    protected final <K, L> void removeAllListeners(
            final Class<? extends MappedEvent<K, L>> type, final K key) {
        Preconditions.checkNotNull(type);
        Preconditions.checkNotNull(key);

        @SuppressWarnings("unchecked")
        final Set<L> listeners =
                (Set<L>) this.eventsAndKeysToStrongListenerRefs.get(type, key);
        Preconditions.checkArgument(Presence.isPresent(listeners));

        final Iterator<L> listenerIterator = listeners.iterator();
        while (listenerIterator.hasNext()) {
            this.eventManager
                    .removeListener(type, key, listenerIterator.next());
            listenerIterator.remove();
        }
    }

    /**
     * Odebere posluchače na správci událostí a u sebe smaže silnou referenci,
     * která by jinak zamezila úklidu paměti.
     * 
     * @param type
     *            typ události
     * @param listener
     *            posluchač
     */
    protected final <L> void removeListener(
            final Class<? extends Event<L>> type, final L listener) {
        Preconditions.checkNotNull(type);
        Preconditions.checkNotNull(listener);

        removeListenerStrongReference(type, listener);
        try {
            this.eventManager.removeListener(type, listener);
        } catch (final Exception e) {
            addListenerStrongReference(type, listener);
            throw e;
        }
    }

    /**
     * Odebere posluchače pro daný klíč na správci událostí a u sebe smaže
     * silnou referenci, která by jinak zamezila úklidu paměti.
     * 
     * @param type
     *            typ události
     * @param key
     *            klíč
     * @param listener
     *            posluchač
     */
    protected final <K, L> void removeListener(
            final Class<? extends MappedEvent<K, L>> type, final K key,
            final L listener) {
        Preconditions.checkNotNull(type);
        Preconditions.checkNotNull(key);
        Preconditions.checkNotNull(listener);

        removeListenerStrongReference(type, key, listener);
        try {
            this.eventManager.removeListener(type, key, listener);
        } catch (final Exception e) {
            addListenerStrongReference(type, key, listener);
            throw e;
        }
    }

    private <L> void removeListenerStrongReference(
            final Class<? extends Event<L>> type, final L listener) {
        @SuppressWarnings("unchecked")
        final Set<L> listeners =
                (Set<L>) this.eventsToStrongListenerRefs.get(type);

        final boolean contained = listeners.remove(listener);
        Preconditions.checkArgument(contained);
    }

    private <K, L> void removeListenerStrongReference(
            final Class<? extends MappedEvent<K, L>> type, final K key,
            final L listener) {
        @SuppressWarnings("unchecked")
        final Set<L> listeners =
                (Set<L>) this.eventsAndKeysToStrongListenerRefs.get(type, key);
        Preconditions.checkArgument(Presence.isPresent(listeners));

        final boolean contained = listeners.remove(listener);
        Preconditions.checkArgument(contained);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.utils.mvc.Controller#removeView(
     * java.lang.Object)
     */
    @Override
    public final void removeView(final V view) {
        Preconditions.checkNotNull(view);

        final boolean contained = this.views.remove(view);
        Preconditions.checkArgument(contained);
    };
}
