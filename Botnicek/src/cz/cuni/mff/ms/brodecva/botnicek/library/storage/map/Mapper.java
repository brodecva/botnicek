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
package cz.cuni.mff.ms.brodecva.botnicek.library.storage.map;

import java.util.Map.Entry;
import java.util.Set;

/**
 * Jednoduchá mapa. Nedovoluje užít null ani jako klíč, ani jako hodnotu.
 * 
 * @author Václav Brodec
 * @version 1.0
 * 
 * @param <K>
 *            klíč
 * @param <V>
 *            hodnota příslušící danému klíči
 */
public interface Mapper<K, V> {
    /**
     * Vrátí hodnotu asociovanou s daným klíčem. Pokud daná asociace neexistuje,
     * vrátí null.
     * 
     * @param key
     *            klíč
     * @return asociovaná hodnota
     */
    V get(K key);

    /**
     * Uloží pod daným klíčem hodnotu do mapy.
     * 
     * @param key
     *            klíč, nesmí být null
     * @param value
     *            hodnota, nesmí být null
     */
    void put(K key, V value);

    /**
     * Vrátí množinu záznamů.
     * 
     * @return množina záznamů
     */
    Set<Entry<K, V>> getEntries();

    /**
     * Provede pokus o změnu jádra za účelem zvětšení či optimalizace času
     * přístupu.
     */
    void resize();

    /**
     * Vrátí velikost.
     * 
     * @return počet uložených záznamů
     */
    int getSize();

    /**
     * Vrátí maximální počet prvků, které uloží bez změny velikosti.
     * 
     * @return maximální počet uložených záznamů bez změny velikosti
     */
    int getCapacity();

    /**
     * Vrátí hash z obsažených záznamů.
     * 
     * @return hash ze záznamů
     */
    @Override
    int hashCode();

    /**
     * Porovná objekt s mapperem.
     * 
     * @param object
     *            objekt
     * @return pokud je objekt mapper stejného typu a se stejnými záznamy
     */
    @Override
    boolean equals(Object object);
}
