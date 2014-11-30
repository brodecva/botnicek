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
package cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data;

import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Intended;

/**
 * Pomocné metody pro práci s návratovými hodnotami či argumenty metod, jež
 * vrací {@code null} jako indikátor nepřítomnosti výsledku či argumentu.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class Presence {

    /**
     * Indikuje nepřítomnou hodnotu podle toho, zda-li je vstup {@code null}.
     * 
     * @param nullForAbsent
     *            výsledek, který je {@code null}, pokud nebyla požadovaná
     *            hodnota přítomna
     * @return zda-li je je hodnota nepřítomna
     */
    public static boolean isAbsent(final Object nullForAbsent) {
        return !isPresent(nullForAbsent);
    }

    /**
     * Indikuje přítomnou hodnotu podle toho, zda-li je vstup {@code null}.
     * 
     * @param nullForAbsent
     *            výsledek, který je {@code null}, pokud nebyla požadovaná
     *            hodnota přítomna
     * @return zda-li je je hodnota přítomna
     */
    public static boolean isPresent(final Object nullForAbsent) {
        return nullForAbsent != Intended.nullReference();
    }

    private Presence() {
    }
}
