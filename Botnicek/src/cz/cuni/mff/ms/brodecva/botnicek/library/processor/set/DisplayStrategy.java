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
package cz.cuni.mff.ms.brodecva.botnicek.library.processor.set;

/**
 * Strategie rozhodující o podobě výstupu při nastavení uživatelského predikátu.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public interface DisplayStrategy {
    /**
     * Vrátí výstup k zobrazení.
     * 
     * @param name název predikátu
     * @param value hodnota predikátu
     * @return výstup k zobrazení
     */
    String display(String name, String value);
    
    /**
     * Vrátí hash strategie.
     * 
     * @return hash strategie
     */
    @Override
    int hashCode();

    /**
     * Porovná objekt s touto strategií.
     * 
     * @param object
     *            objekt
     * @return true, pokud je objekt strategie stejného typu
     */
    @Override
    boolean equals(final Object object);
}

