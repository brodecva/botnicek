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

import java.util.List;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

/**
 * Model umožňující správně vykreslování a aktualizaci napovídajícího combo
 * boxu.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @param <E>
 *            typ obsažených položek
 */
final class IgnoreFireContentsChangedComboBoxModel<E> extends
        DefaultComboBoxModel<E> {

    private static final long serialVersionUID = 1L;

    /**
     * Vytvoří model.
     * 
     * @param list
     *            seznam prvků
     * @return model
     */
    public static <E> IgnoreFireContentsChangedComboBoxModel<E> create(
            final List<? extends E> list) {
        Preconditions.checkNotNull(list);

        return new IgnoreFireContentsChangedComboBoxModel<E>(
                ImmutableList.copyOf(list));
    }

    private boolean ignored = false;

    private IgnoreFireContentsChangedComboBoxModel(final List<E> list) {
        super(new Vector<E>(list));
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.AbstractListModel#fireContentsChanged(java.lang.Object,
     * int, int)
     */
    @Override
    protected void fireContentsChanged(final Object source, final int index0,
            final int index1) {
        if (isIgnored()) {
            return;
        }

        super.fireContentsChanged(source, index0, index1);
    }

    /**
     * indikuje-zda-li je ignorováno volání
     * {@link #fireContentsChanged(Object, int, int)}.
     * 
     * @return zda-li model ignoruje
     */
    public boolean isIgnored() {
        return this.ignored;
    }

    /**
     * Nastaví ignorování volání {@link #fireContentsChanged(Object, int, int)}.
     * 
     * @param value
     *            hodnota
     */
    public void setIgnored(final boolean value) {
        this.ignored = value;
    }
}