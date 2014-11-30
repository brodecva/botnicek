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
package cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.listeners;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serializable;

import javax.swing.event.MouseInputListener;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.DefaultEventManager;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.EventManager;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.Components;

/**
 * Posluchač pro tažení komponenty myší.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class DragListener extends MouseAdapter implements
        MouseInputListener, Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Vytvoří posluchače pro komponentu.
     * 
     * @param component
     *            komponenta
     * @return posluchač
     */
    public static DragListener create(final Component component) {
        return create(component, DefaultEventManager.create());
    }

    /**
     * Vytvoří posluchače pro tažení komponenty myší.
     * 
     * @param component
     *            komponenta
     * @param eventManager
     *            správce událostí
     * @return posluchač
     */
    public static DragListener create(final Component component,
            final EventManager eventManager) {
        Preconditions.checkNotNull(component);
        Preconditions.checkNotNull(eventManager);

        return new DragListener(component, eventManager);
    }

    private final Component draggedComponent;
    private final EventManager eventManager;
    private volatile int screenX = 0;
    private volatile int screenY = 0;
    private volatile int componentX = 0;

    private volatile int componentY = 0;

    private volatile boolean dragged = false;

    private DragListener(final Component component,
            final EventManager eventManager) {
        this.draggedComponent = component;
        this.eventManager = eventManager;
    }

    /**
     * Přidá posluchače ukončení tahu.
     * 
     * @param listener
     *            posluchač
     */
    public void addDragFinishedListener(final DragFinishedListener listener) {
        Preconditions.checkNotNull(listener);

        this.eventManager.addListener(DragFinishedEvent.class, listener);
    }

    private int computeDeltaX(final MouseEvent e) {
        return e.getXOnScreen() - this.screenX;
    }

    private int computeDeltaY(final MouseEvent e) {
        return e.getYOnScreen() - this.screenY;
    }

    private boolean hasBeenDragged() {
        return this.dragged;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.MouseAdapter#mouseDragged(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseDragged(final MouseEvent e) {
        turnOnDrag();

        final int deltaX = computeDeltaX(e);
        final int deltaY = computeDeltaY(e);

        moveComponent(deltaX, deltaY);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.MouseAdapter#mousePressed(java.awt.event.MouseEvent)
     */
    @Override
    public void mousePressed(final MouseEvent e) {
        placeDraggedOnTopInParent();

        storeScreenPosition(e);
        storeComponentPosition();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.MouseAdapter#mouseReleased(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseReleased(final MouseEvent e) {
        final boolean dragFinished = hasBeenDragged();
        turnOffDrag();

        if (dragFinished) {
            this.eventManager.fire(DragFinishedEvent.create());
        }
    }

    private void moveComponent(final int deltaX, final int deltaY) {
        this.draggedComponent.setLocation(this.componentX + deltaX,
                this.componentY + deltaY);
    }

    private void placeDraggedOnTopInParent() {
        final Container parent = this.draggedComponent.getParent();
        Preconditions.checkState(Components.hasParent(parent));

        parent.setComponentZOrder(this.draggedComponent, 0);
    }

    /**
     * Odebere posluchače ukončení tahu.
     * 
     * @param listener
     *            posluchač
     */
    public void removeDragFinishedListener(final DragFinishedListener listener) {
        Preconditions.checkNotNull(listener);

        this.eventManager.removeListener(DragFinishedEvent.class, listener);
    }

    private void storeComponentPosition() {
        this.componentX = this.draggedComponent.getX();
        this.componentY = this.draggedComponent.getY();
    }

    private void storeScreenPosition(final MouseEvent e) {
        this.screenX = e.getXOnScreen();
        this.screenY = e.getYOnScreen();
    }

    private void turnOffDrag() {
        this.dragged = false;
    }

    private void turnOnDrag() {
        this.dragged = true;
    }
}
