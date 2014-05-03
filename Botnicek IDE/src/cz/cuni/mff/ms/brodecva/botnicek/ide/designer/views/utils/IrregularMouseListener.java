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
package cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.utils;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;

import javax.swing.event.MouseInputListener;

import com.google.common.base.Preconditions;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public final class IrregularMouseListener implements MouseInputListener {
    
    private static class DummyMouseAdapter extends MouseAdapter { };
    private static class DummyMouseMotionAdapter extends MouseMotionAdapter { };
    
    private final Component component;
    private final MouseListener decorated;
    private final MouseMotionListener decoratedMotion;
    
    public static IrregularMouseListener decorate(final Component component, final MouseListener decorated) {
        return new IrregularMouseListener(component, decorated, new DummyMouseMotionAdapter());
    }
    
    public static IrregularMouseListener decorate(final Component component, final MouseMotionListener decoratedMotion) {
        return new IrregularMouseListener(component, new DummyMouseAdapter(), decoratedMotion);
    }
    
    public static IrregularMouseListener decorate(final Component component, final MouseInputListener decoratedInput) {
        return new IrregularMouseListener(component, decoratedInput, decoratedInput);
    }
    
    public static IrregularMouseListener decorate(final Component component, final MouseAdapter decoratedAdapter) {
        return new IrregularMouseListener(component, decoratedAdapter, decoratedAdapter);
    }
    
    private IrregularMouseListener(final Component component, final MouseListener decorated, final MouseMotionListener decoratedMotion) {
        Preconditions.checkNotNull(component);
        Preconditions.checkNotNull(decorated);
        Preconditions.checkNotNull(decoratedMotion);
        
        this.component = component;
        this.decorated = decorated;
        this.decoratedMotion = decoratedMotion;
    }
    
    /* (non-Javadoc)
     * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseClicked(final MouseEvent e) {
        if (isTarget(e)) {
            this.decorated.mouseClicked(e);
        } else {
            pass(e);
        }
    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseEntered(final MouseEvent e) {
        if (isTarget(e)) {
            this.decorated.mouseEntered(e);
        } else {
            pass(e);
        }
    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseExited(final MouseEvent e) {
        if (isTarget(e)) {
            this.decorated.mouseExited(e);
        } else {
            pass(e);
        }
    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    @Override
    public void mousePressed(final MouseEvent e) {
        if (isTarget(e)) {
            this.decorated.mousePressed(e);
        } else {
            pass(e);
        }
    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseReleased(final MouseEvent e) {
        if (isTarget(e)) {
            this.decorated.mouseReleased(e);
        } else {
            pass(e);
        }
    }
    
    /* (non-Javadoc)
     * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseDragged(final MouseEvent e) {
        if (isTarget(e)) {
            this.decoratedMotion.mouseDragged(e);
        } else {
            pass(e);
        }
    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseMoved(final MouseEvent e) {
        if (isTarget(e)) {
            this.decoratedMotion.mouseMoved(e);
        } else {
            pass(e);
        }
    }
    
    private boolean isTarget(final MouseEvent e) {
        return this.component.contains(e.getX(), e.getY());
    }
    
    private void pass(final MouseEvent e) {
        final int parentalX = this.component.getX() + e.getX();
        final int parentalY = this.component.getY() + e.getY();
        
        final Container parent = this.component.getParent();
        if (parent == null) {
            return;
        }
        
        final Component actualSource = parent.getComponentAt(parentalX, parentalY);
        if (actualSource == null) {
            return;
        }
        
        final int actualX = parentalX - actualSource.getX();
        final int actualY = parentalY - actualSource.getY();
        
        final MouseEvent actualMouseEvent = new MouseEvent(actualSource, e.getID(), e.getWhen(), e.getModifiers(), actualX, actualY, e.getClickCount(), e.isPopupTrigger()); 
        actualSource.dispatchEvent(actualMouseEvent);
    }
}