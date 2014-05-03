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

import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.utils.Callback;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.Event;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.EventManager;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public class AbstractController<V> implements Controller<V> {
    private final EventManager eventManager;
    private final Set<V> views = new HashSet<>();
    
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
            callback.call(view);
        }
    }
    
    protected final void fire(final Event<?> event) {
        Preconditions.checkNotNull(event);
        
        this.eventManager.fire(event);
    }
    
    protected final <L> void addEventListener(Class<? extends Event<L>> type, L listener) {
        Preconditions.checkNotNull(type);
        Preconditions.checkNotNull(listener);
        
        this.eventManager.addEventListener(type, listener);
    }
    
    protected final <L> void removeEventListener(Class<? extends Event<L>> type, L listener) {
        Preconditions.checkNotNull(type);
        Preconditions.checkNotNull(listener);
        
        this.eventManager.removeEventListener(type, listener);
    }
}
