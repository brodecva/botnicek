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

/**
 * <p>
 * Rozhraní řadiče modifikovaného MVC.
 * </p>
 * <p>
 * Oproti kanonickému MVC se pohled neregistruje na modelu, aby získával
 * aktualizace, ale je to řadič, který naslouchá událostem a podle změn v modelu
 * volá vhodné metody na pohledu.
 * </p>
 * 
 * @author Václav Brodec
 * @version 1.0
 * @param <V>
 *            typ pohledu
 */
public interface Controller<V> {
    /**
     * Přidá pohled.
     * 
     * @param view
     *            pohled
     */
    void addView(V view);

    /**
     * Zavolá aktualizační metody na pohledu.
     * 
     * @param view
     *            pohled
     */
    void fill(V view);

    /**
     * <p>
     * Odebere pohled.
     * </p>
     * 
     * @param view
     *            pohled
     */
    void removeView(V view);
}
