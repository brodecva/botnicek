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
package cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.event.MouseInputListener;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.events.DragFinishedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.events.DragFinishedListener;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.DefaultEventManager;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.EventManager;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public final class DragListener extends MouseAdapter implements MouseInputListener {
    
    private final Component draggedComponent;
    private final EventManager eventManager;
    
    private volatile int screenX = 0;
    private volatile int screenY = 0;
    private volatile int x = 0;
    private volatile int y = 0;
    private volatile boolean dragged = false;
    
    public static DragListener create(final Component component) {
        return new DragListener(component);
    }
    
    private DragListener(final Component component) {
        this(component, DefaultEventManager.create());
    }
    
    private DragListener(final Component component, final EventManager eventManager) {
        Preconditions.checkNotNull(component);
        Preconditions.checkNotNull(eventManager);
        
        this.draggedComponent = component;
        this.eventManager = eventManager;
    }
    
    public void addDragFinishedListener(final DragFinishedListener listener) {
        Preconditions.checkNotNull(listener);
        
        this.eventManager.addListener(DragFinishedEvent.class, listener);
    }
    
    public void removeDragFinishedListener(final DragFinishedListener listener) {
        Preconditions.checkNotNull(listener);
        
        this.eventManager.removeListener(DragFinishedEvent.class, listener);
    }
    
    @Override
    public void mousePressed(final MouseEvent e) {
        placeOnTop();
        
        DragListener.this.screenX = e.getXOnScreen();
        DragListener.this.screenY = e.getYOnScreen();

        DragListener.this.x = this.draggedComponent.getX();
        DragListener.this.y = this.draggedComponent.getY();
    }

    /**
     * 
     */
    private void placeOnTop() {
        final Container parent = this.draggedComponent.getParent();
        Preconditions.checkState(parent != null);
                
        parent.setComponentZOrder(this.draggedComponent, 0);
    }

    @Override
    public void mouseReleased(final MouseEvent e) {
        final boolean finished = DragListener.this.dragged;
        DragListener.this.dragged = false;

        if (finished) {
            this.eventManager.fire(DragFinishedEvent.create());
        }
    }

    @Override
    public void mouseDragged(final MouseEvent e) {
        DragListener.this.dragged = true;

        final int deltaX = e.getXOnScreen() - DragListener.this.screenX;
        final int deltaY = e.getYOnScreen() - DragListener.this.screenY;

        this.draggedComponent.setLocation(DragListener.this.x + deltaX, DragListener.this.y + deltaY);
    }
}
