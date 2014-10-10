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

import javax.swing.JOptionPane;

import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Intended;

/**
 * Pomocné metody pro práci s komponentami.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see Component
 */
public final class Components {

    /**
     * Indikuje, zda-li komponenta má rodiče.
     * 
     * @param getParentResult
     *            výsledek volání metody {@link Component#getParent()}
     * @return zda-li komponenta má rodiče
     * @see Component
     */
    public static boolean hasParent(final Container getParentResult) {
        return getParentResult != Intended.nullReference();
    }

    /**
     * Indikuje, zda-li uživatel zrušil zadávání vstupu do vstupního dialogu.
     * 
     * @param showDialogResult
     *            výsledek volání metody
     *            {@link JOptionPane#showInputDialog(Object)} či jejích
     *            přetížení
     * @return zda-li uživatel zrušil zadávání vstupu do vstupního dialogu
     * @see JOptionPane
     */
    public static boolean hasUserCanceledInput(final Object showDialogResult) {
        return showDialogResult == Intended.nullReference();
    }

    private Components() {
    }

}
