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

import java.io.Serializable;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import cz.cuni.mff.ms.brodecva.botnicek.library.utils.ExceptionMessageLocalizer;

/**
 * Jádro pro {@link Mapper} implementované hašovací tabulkou.
 * 
 * @author Václav Brodec
 * @version 1.0
 * 
 * @param <K>
 *            klíč
 * @param <V>
 *            hodnota
 */
public final class HashMapCore<K, V> implements MapperCore<K, V>, Serializable {
    /**
     * UID serializované verze.
     */
    private static final long serialVersionUID = -7006837728732853438L;

    /**
     * Lokalizátor hlášek výjimek.
     */
    private static final ExceptionMessageLocalizer MESSAGE_LOCALIZER =
            ExceptionMessageLocalizer.getLocalizer();

    /**
     * Kapacita. Stejně jako {@link java.util.HashSet}.
     */
    public static final int CAPACITY = 1073741824;

    /**
     * Hašovací tabulka.
     */
    private final Map<K, V> map;

    /**
     * Vytvoří jádro implementované hašovací tabulkou o výchozí počáteční
     * velikosti.
     */
    public HashMapCore() {
        map = new HashMap<K, V>();
    }

    /**
     * Vytvoří jádro implementované hašovací tabulkou.
     * 
     * @param initialCapacity
     *            počáteční kapacita (tj. počáteční počet prvků, který tabulka
     *            snese bez zvětšování), musí být nezáporná
     * @see HashMap pro význam kapacity
     */
    public HashMapCore(final int initialCapacity) {
        map =
                new HashMap<K, V>(initialCapacity
                        * HASH_SET_RESIZE_PREVENTION_FACTOR);
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.MapperCore#get
     * (java.lang.Object)
     */
    @Override
    public V get(final K key) {
        return map.get(key);
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.MapperCore#put
     * (java.lang.Object, java.lang.Object)
     */
    @Override
    public boolean put(final K key, final V value) {
        if (key == null || value == null) {
            throw new NullPointerException(
                    MESSAGE_LOCALIZER.getMessage("storage.map.NullNotAccepted"));
        }

        map.put(key, value);

        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.MapperCore#resize ()
     */
    @Override
    public MapperCore<K, V> resize() {
        return this; // Hašovací tabulka by měla stačit všem.
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.MapperCore#
     * getEntries ()
     */
    @Override
    public Set<Entry<K, V>> getEntries() {
        final Set<Entry<K, V>> result =
                new HashSet<Entry<K, V>>(HASH_SET_RESIZE_PREVENTION_FACTOR
                        * map.size());

        for (final Entry<K, V> entry : map.entrySet()) {
            result.add(new SimpleImmutableEntry<K, V>(entry));
        }

        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.MapperCore#getSize
     * ()
     */
    @Override
    public int getSize() {
        return map.size();
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.MapperCore#
     * getCapacity()
     */
    @Override
    public int getCapacity() {
        return CAPACITY;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.brodecva.botnicek.library.storage.mapper.MapperCore#hashCode
     * ()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((map == null) ? 0 : map.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.brodecva.botnicek.library.storage.mapper.MapperCore#equals
     * (java.lang.Object);
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        @SuppressWarnings("rawtypes")
        final HashMapCore other = (HashMapCore) obj;
        if (map == null) {
            if (other.map != null) {
                return false;
            }
        } else if (!map.equals(other.map)) {
            return false;
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("HashMapCore [entries=");
        builder.append(map);
        builder.append(", size=");
        builder.append(getSize());
        builder.append(", capacity=");
        builder.append(getCapacity());
        builder.append("]");
        return builder.toString();
    }

}
