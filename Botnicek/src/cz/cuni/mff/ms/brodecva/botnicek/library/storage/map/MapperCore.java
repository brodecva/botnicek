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
package cz.cuni.mff.ms.brodecva.botnicek.library.storage.map;

import java.util.Map.Entry;
import java.util.Set;

/**
 * Vyměnitelné jádro pro {@link Mapper}. Kromě předefinování metod z definice
 * rozhraní se očekává výskyt bezparametrického konstruktoru a konstruktoru s
 * jedním parametrem typu {@link Integer} pro určení kapacity uložiště.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see Mapper pro využití
 * 
 * @param <K>
 *            klíč
 * @param <V>
 *            hodnota
 */
public interface MapperCore<K, V> {

    /**
     * Koeficient pro znásobení počtu původních prvků při konstrukci množiny
     * implementované java.util.HashSet.
     */
    int HASH_SET_RESIZE_PREVENTION_FACTOR = 2;

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
     * Uloží pod daným klíčem hodnotu do mapy a indikuje zaplnění.
     * 
     * @param key
     *            klíč, nesmí být null
     * @param value
     *            hodnota, nesmí být null
     * @return true, pokud vložení selhalo, protože je jádro třeba změnit
     */
    boolean put(K key, V value);

    /**
     * Vrátí novou verzi uložiště optimalizovanou pro aktuální počet prvků.
     * 
     * @return vrátí novou verzi uložiště
     */
    MapperCore<K, V> resize();

    /**
     * Vrátí uložené záznamy.
     * 
     * @return množina všech uložených záznamů
     */
    Set<Entry<K, V>> getEntries();

    /**
     * Vrátí velikost.
     * 
     * @return velikost
     */
    int getSize();

    /**
     * Vrátí kapacitu.
     * 
     * @return kapacita
     */
    int getCapacity();

    /**
     * Vrátí hash pro záznamy.
     * 
     * @return hash spočítaná z obsažených záznamů
     */
    @Override
    int hashCode();

    /**
     * Porovná objekt s jádrem.
     * 
     * @param object
     *            objekt
     * @return true, pokud je objekt jádrem stejného typu, obsahuje stejné
     *         záznamy a má stejnou kapacitu
     */
    @Override
    boolean equals(Object object);
}
