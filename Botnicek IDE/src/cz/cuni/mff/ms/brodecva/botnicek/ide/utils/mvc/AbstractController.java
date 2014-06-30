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
package cz.cuni.mff.ms.brodecva.botnicek.ide.utils.mvc;

import java.util.HashSet;
import java.util.Set;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.design.utils.Callback;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.DefaultEventManager;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.MappedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.EventManager;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.Event;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public abstract class AbstractController<V> implements Controller<V> {
    private final EventManager eventManager;
    private final Set<V> views = new HashSet<>();
    
    /*protected AbstractController() {
        this(DefaultEventManager.create());
    }*/
    
    protected AbstractController(final EventManager eventManager) {
        Preconditions.checkNotNull(eventManager);
        
        this.eventManager = eventManager;
    }
    
    protected EventManager getEventManager() {
        return this.eventManager;
    }
    
    public void addView(final V view) {
        Preconditions.checkNotNull(view);
        
        this.views.add(view);
    }
    
    public void removeView(final V view) {
        Preconditions.checkNotNull(view);
        
        this.views.remove(view);
    }
    
    protected void callViews(final Callback<V> callback) {
        for (final V view : this.views) {
            try {
                callback.call(view);
            } catch (final RuntimeException e) {
                throw e;
            }
        }
    }
    
    protected final void fire(final MappedEvent<?, ?> event) {
        Preconditions.checkNotNull(event);
        
        this.eventManager.fire(event);
    }
    
    protected final <K, L> void addListener(final Class<? extends MappedEvent<K, L>> type, final K key, final L listener) {
        Preconditions.checkNotNull(type);
        Preconditions.checkNotNull(key);
        Preconditions.checkNotNull(listener);
        
        this.eventManager.addListener(type, key, listener);
    }
    
    protected final <K, L> void removeListener(Class<? extends MappedEvent<K, L>> type, K key, L listener) {
        Preconditions.checkNotNull(type);
        Preconditions.checkNotNull(key);
        Preconditions.checkNotNull(listener);
        
        this.eventManager.removeListener(type, key, listener);
    }
    
    protected final void fire(final Event<?> event) {
        Preconditions.checkNotNull(event);
        
        this.eventManager.fire(event);
    }
    
    protected final <L> void addListener(final Class<? extends Event<L>> type, final L listener) {
        Preconditions.checkNotNull(type);
        Preconditions.checkNotNull(listener);
        
        this.eventManager.addListener(type, listener);
    }
    
    protected final <L> void removeListener(final Class<? extends Event<L>> type, final L listener) {
        Preconditions.checkNotNull(type);
        Preconditions.checkNotNull(listener);
        
        this.eventManager.removeListener(type, listener);
    }
    
    protected final <K, L> void removeAllListeners(final Class<? extends MappedEvent<K, L>> type, final K key) {
        Preconditions.checkNotNull(type);
        Preconditions.checkNotNull(key);
        
        this.eventManager.removeAllListeners(type, key);
    }
    
    protected final <L> void removeAllListeners(final Class<? extends Event<L>> type) {
        Preconditions.checkNotNull(type);
        
        this.eventManager.removeAllListeners(type);
    }
    
    public void fill(final V view) {
    };
}
