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

import java.util.AbstractMap.SimpleEntry;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import cz.cuni.mff.ms.brodecva.botnicek.library.utils.ExceptionMessageLocalizer;

/**
 * Jednoprvkové jádro pro {@link Mapper}.
 * 
 * @author Václav Brodec
 * @version 1.0
 * 
 * 
 * @param <K>
 *            klíč
 * @param <V>
 *            hodnota
 */
public final class SingleEntryCore<K, V> implements MapperCore<K, V> {
    /**
     * Lokalizátor hlášek výjimek.
     */
    private static final ExceptionMessageLocalizer MESSAGE_LOCALIZER =
            ExceptionMessageLocalizer.getLocalizer();

    /**
     * Kapacita.
     */
    public static final int CAPACITY = 1;

    /**
     * Velikost prázdného jádra.
     */
    public static final int EMPTY_SIZE = 0;

    /**
     * Uložený prvek.
     */
    private Entry<K, V> entry = null;

    /**
     * Vytvoří jednoprvkové jádro.
     */
    public SingleEntryCore() {
        this(CAPACITY);
    }

    /**
     * Vytvoří jádro s danou kapacitou. Konstruktor, který vyhovuje definici
     * jádra pro {@link Mapper}. Nedovoluje užití jiné hodnoty v parametru, než
     * {@value #CAPACITY}.
     * 
     * @param capacity
     *            kapacita (ignorováno)
     */
    public SingleEntryCore(final int capacity) {
        if (capacity != CAPACITY) {
            throw new IllegalArgumentException(MESSAGE_LOCALIZER.getMessage(
                    "storage.map.CapacityOutOfBounds", capacity));
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.MapperCore#get
     * (java.lang.Object)
     */
    @Override
    public V get(final K key) {
        if (entry == null) {
            return null;
        }

        if (entry.getKey().equals(key)) {
            return entry.getValue();
        }

        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.MapperCore#put
     * (java.lang.Object, java.lang.Object)
     */
    @Override
    public boolean put(final K key, final V value) {
        if (key == null | value == null) {
            throw new NullPointerException(
                    MESSAGE_LOCALIZER.getMessage("storage.map.NullNotAccepted"));
        }

        if (entry == null) {
            entry = new SimpleEntry<K, V>(key, value);
            return false;
        }

        if (entry.getKey().equals(key)) {
            entry.setValue(value);
            return false;
        }

        return true;
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
                new HashSet<Entry<K, V>>(HASH_SET_RESIZE_PREVENTION_FACTOR);

        if (entry != null) {
            result.add(new SimpleImmutableEntry<K, V>(entry));
        }

        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.MapperCore#resize ()
     */
    @Override
    public MapperCore<K, V> resize() {
        if (entry == null) {
            return this;
        }

        final MapperCore<K, V> result = new ArrayCore<K, V>();

        result.put(entry.getKey(), entry.getValue());

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
        if (entry == null) {
            return EMPTY_SIZE;
        }

        return CAPACITY;
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
        result = prime * result + ((entry == null) ? 0 : entry.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.brodecva.botnicek.library.storage.mapper.MapperCore#equals
     * (java.lang.Object)
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
        final SingleEntryCore other = (SingleEntryCore) obj;
        if (entry == null) {
            if (other.entry != null) {
                return false;
            }
        } else if (!entry.equals(other.entry)) {
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
        builder.append("SingleEntryCore [entries={");
        builder.append(entry == null ? "" : entry);
        builder.append("}, size=");
        builder.append(getSize());
        builder.append(", capacity=");
        builder.append(getCapacity());
        builder.append("]");
        return builder.toString();
    }
}
