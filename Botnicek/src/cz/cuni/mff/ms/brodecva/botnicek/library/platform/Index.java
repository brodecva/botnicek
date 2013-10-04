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
package cz.cuni.mff.ms.brodecva.botnicek.library.platform;

/**
 * Jednorozměrný index.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public interface Index {

    /**
     * Nejnižší možná hodnota.
     */
    int LOWER_BOUND = 1;

    /**
     * Výchozí hodnota.
     */
    int DEFAULT_VALUE = LOWER_BOUND;

    /**
     * Vrátí hodnotu indexu.
     * 
     * @return hodnota indexu
     */
    int getValue();

    /**
     * Vrátí hash z indexu.
     * 
     * @return hash z indexu
     */
    @Override
    int hashCode();

    /**
     * Porovná objekt s indexem.
     * 
     * @param obj
     *            objekt
     * @return true, pokud je objekt index stejného typu a hodnoty
     */
    @Override
    boolean equals(final Object obj);

    /**
     * Vrátí textovou reprezentaci indexu.
     * 
     * @return textová reprezentace indexu
     */
    @Override
    String toString();
}
