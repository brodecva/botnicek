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
package cz.cuni.mff.ms.brodecva.botnicek.library.utils.classes;

/**
 * Mapa tříd.
 * 
 * @author Václav Brodec
 * @version 1.0
 * 
 * @param <K>
 *            typ klíče
 * @param <T>
 *            typ registrované třídy
 */
public interface ClassMap<K, T> {

    /**
     * Vloží do mapy třídu pod daným klíčem.
     * 
     * @param key
     *            klíč
     * @param klass
     *            třída
     */
    void put(K key, Class<? extends T> klass);

    /**
     * Vloží do mapy třídu daného názvu pod daným klíčem.
     * 
     * @param key
     *            klíč
     * @param fullName
     *            plný název třídy
     * @throws ClassNotFoundException
     *             pokud třída s daným názvem nebyla nalezena
     */
    void put(K key, String fullName) throws ClassNotFoundException;

    /**
     * Vrátí implementaci.
     * 
     * @param key
     *            plné jméno třídy
     * @return třída, null pokud nebyla pod daným klíčem nalezena
     */
    Class<? extends T> get(K key);

    /**
     * Vrátí hash z prvků.
     * 
     * @return hash z prvků
     */
    @Override
    int hashCode();

    /**
     * Porovná objekt s mapou.
     * 
     * @param object
     *            objekt
     * @return true, pokud je objekt mapa definic tříd stejného typu
     */
    @Override
    boolean equals(Object object);
}
