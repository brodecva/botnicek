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
package cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.components.draglist;

import java.awt.datatransfer.StringSelection;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;

import com.google.common.base.Preconditions;

/**
 * <p>Posluchačů události tahu prvku seznamu.</p>
 * <p>Po rozpoznání tahu vezme index právě vybraného prvku a převede jej kvůli přenositelnosti na řetězec.</p>
 * 
 * @author Václav Brodec
 * @version 1.0
 * @param <E> typ taženého obsahu
 */
final class DragListener<E> implements DragSourceListener,
        DragGestureListener {
    
    private final DragOrderableList<E> list;
    private final DragSource dragSource;
    
    /**
     * Vytvoří posluchač tahu prvku seznamu.
     * 
     * @param list naslouchaný seznamu
     * @return posluchač tahu prvku seznamu
     */
    public static <E> DragListener<E> create(final DragOrderableList<E> list) {            
        Preconditions.checkNotNull(list);
        
        final DragSource dragSource = new DragSource(); 
        final DragListener<E> newInstance = new DragListener<E>(list, dragSource);
        
        dragSource.createDefaultDragGestureRecognizer(list,
                DnDConstants.ACTION_MOVE, newInstance);
        
        return newInstance;
    }
    
    private DragListener(final DragOrderableList<E> list, final DragSource dragSource) {
        assert list != null;
        assert dragSource != null;
        
        this.list = list;
        this.dragSource = dragSource;
    }

    /* (non-Javadoc)
     * @see java.awt.dnd.DragGestureListener#dragGestureRecognized(java.awt.dnd.DragGestureEvent)
     */
    @Override
    public void dragGestureRecognized(final DragGestureEvent e) {
        final int selectionIndex = this.list.getSelectedIndex();
        final String transferredSelectionIndex = Integer.toString(selectionIndex);
        
        final StringSelection transferable =
                new StringSelection(transferredSelectionIndex);
        
        this.dragSource.startDrag(e, DragSource.DefaultCopyDrop,
                transferable, this);
    }

    /* (non-Javadoc)
     * @see java.awt.dnd.DragSourceListener#dragEnter(java.awt.dnd.DragSourceDragEvent)
     */
    @Override
    public void dragEnter(DragSourceDragEvent dsde) {
    }

    /* (non-Javadoc)
     * @see java.awt.dnd.DragSourceListener#dragExit(java.awt.dnd.DragSourceEvent)
     */
    @Override
    public void dragExit(DragSourceEvent dse) {
    }

    /* (non-Javadoc)
     * @see java.awt.dnd.DragSourceListener#dragOver(java.awt.dnd.DragSourceDragEvent)
     */
    @Override
    public void dragOver(DragSourceDragEvent dsde) {
    }

    /* (non-Javadoc)
     * @see java.awt.dnd.DragSourceListener#dragDropEnd(java.awt.dnd.DragSourceDropEvent)
     */
    @Override
    public void dragDropEnd(DragSourceDropEvent dsde) {
    }

    /* (non-Javadoc)
     * @see java.awt.dnd.DragSourceListener#dropActionChanged(java.awt.dnd.DragSourceDragEvent)
     */
    @Override
    public void dropActionChanged(DragSourceDragEvent dsde) {
    }
}