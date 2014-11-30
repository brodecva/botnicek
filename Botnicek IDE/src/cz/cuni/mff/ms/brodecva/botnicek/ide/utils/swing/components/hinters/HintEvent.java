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
package cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.components.hinters;

import java.io.Serializable;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.AbstractEvent;

/**
 * Událost nápovědy prvku.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
final class HintEvent extends AbstractEvent<HintListener> implements
        Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Vytvoří událost.
     * 
     * @param hintedItem
     *            nabízený prvek
     * @return událost
     */
    public static HintEvent create(final Object hintedItem) {
        return new HintEvent(hintedItem);
    }

    private final Object hintedItem;

    private HintEvent(final Object hintedItem) {
        Preconditions.checkNotNull(hintedItem);

        this.hintedItem = hintedItem;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.Event#dispatchTo(java
     * .lang.Object)
     */
    @Override
    public void dispatchTo(final HintListener listener) {
        Preconditions.checkNotNull(listener);

        listener.hint(this.hintedItem);
    }

    /**
     * Vrátí nabídnutý prvek
     * 
     * @return vrátí nabídnutý prvek
     */
    public Object getHintedItem() {
        return this.hintedItem;
    }
}