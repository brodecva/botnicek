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

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import javax.swing.JList;
import javax.swing.TransferHandler;

import com.google.common.base.Preconditions;

/**
 * <p>Obsluhuje přesunutí prvku v seznamu vybraného pomocí tahu.</p>
 * <p>Jeho index získá z přesouvaného řetězcového obsahu.</p>
 * 
 * @author Václav Brodec
 * @version 1.0
 * @param <E> typ přesouvaného prvku
 * @see DragListener pro původce přesouvaného obsahu
 */
final class DropHandler<E> extends TransferHandler {
    
    private static final long serialVersionUID = 1L;
    
    private final DragOrderableList<E> moved;
    
    /**
     * Vytvořený obslužný objekt pro přesunutí prvku v rámci seznamu.
     * 
     * @param moved obsluhovaný seznam
     * @return obslužný objekt
     */
    public static <E> DropHandler<E> create(final DragOrderableList<E> moved) {
        Preconditions.checkNotNull(moved);
        
        return new DropHandler<E>(moved);
    }
    
    private DropHandler(final DragOrderableList<E> moved) {
        this.moved = moved;
    }
    
    /* (non-Javadoc)
     * @see javax.swing.TransferHandler#canImport(javax.swing.TransferHandler.TransferSupport)
     */
    @Override
    public boolean canImport(final TransferHandler.TransferSupport support) {
        if (!support.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            return false;
        }
        
        final JList.DropLocation dropLocation =
                (JList.DropLocation) support.getDropLocation();
        
        return dropLocation.getIndex() != -1;
    }

    /* (non-Javadoc)
     * @see javax.swing.TransferHandler#importData(javax.swing.TransferHandler.TransferSupport)
     */
    @Override
    public boolean importData(final TransferHandler.TransferSupport support) {
        if (!canImport(support)) {
            return false;
        }

        final Transferable transferable = support.getTransferable();
        final String indexString;
        try {
            indexString =
                    (String) transferable
                            .getTransferData(DataFlavor.stringFlavor);
        } catch (final UnsupportedFlavorException | IOException e) {
            return false;
        }
        
        final int sourceIndex;
        try {
            sourceIndex = Integer.parseInt(indexString);
        } catch (final NumberFormatException e) {
            return false;
        }
        
        final JList.DropLocation dropLocation =
                (JList.DropLocation) support.getDropLocation();
        final int targetIndex = dropLocation.getIndex();

        try {
            this.moved.moveToIndex(sourceIndex, targetIndex);
        } catch (final IllegalStateException e) {
            return false;
        }
        
        return true;
    }
}