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
import java.util.AbstractMap.SimpleEntry;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import cz.cuni.mff.ms.brodecva.botnicek.library.utils.ExceptionMessageLocalizer;

/**
 * Polem implementované jádro pro {@link Mapper}.
 * 
 * @author Václav Brodec
 * @version 1.0
 * 
 * @param <K>
 *            klíč
 * @param <V>
 *            hodnota
 */
public final class ArrayCore<K, V> implements MapperCore<K, V>, Serializable {
    /**
     * UID serializované verze.
     */
    private static final long serialVersionUID = 7403020878065503795L;

    /**
     * Lokalizátor hlášek výjimek.
     */
    private static final ExceptionMessageLocalizer MESSAGE_LOCALIZER =
            ExceptionMessageLocalizer.getLocalizer();

    /**
     * Maximální kapacita.
     */
    public static final int MAXIMUM_CAPACITY = 3;

    /**
     * Minimální kapacita.
     */
    public static final int MINIMUM_CAPACITY = 2;

    /**
     * Výchozí kapacita.
     */
    public static final int DEFAULT_CAPACITY = 3;

    /**
     * Uložené páry.
     */
    private final Entry<?, ?>[] entries;

    /**
     * Volný index.
     */
    private int freeIndex = 0;

    /**
     * Vytvoří jádro výchozí kapacity ({@value #DEFAULT_CAPACITY}).
     */
    public ArrayCore() {
        entries = new Entry<?, ?>[DEFAULT_CAPACITY];
    }

    /**
     * Vytvoří jádro dané velikosti.
     * 
     * @param capacity
     *            maximální počet položek, které se do jádra vejdou (nejvýše
     *            {@value #MAXIMUM_CAPACITY} a nejméně
     *            {@value #MINIMUM_CAPACITY})
     */
    public ArrayCore(final int capacity) {
        if (capacity < MINIMUM_CAPACITY || capacity > MAXIMUM_CAPACITY) {
            throw new IllegalArgumentException(MESSAGE_LOCALIZER.getMessage(
                    "storage.map.CapacityOutOfBounds", capacity));
        }

        entries = new Entry<?, ?>[capacity];
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.MapperCore#get
     * (java.lang.Object)
     */
    @Override
    public V get(final K key) {
        final Entry<K, V> result = findEntry(key);

        if (result == null) {
            return null;
        }

        return result.getValue();
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

        if (freeIndex == entries.length) {
            return true;
        }

        final Entry<K, V> result = findEntry(key);

        if (result != null) {
            result.setValue(value);
            return false;
        }

        entries[freeIndex++] = new SimpleEntry<K, V>(key, value);

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
        if (freeIndex < entries.length) {
            return this;
        }

        final MapperCore<K, V> newCore;

        if (entries.length < MAXIMUM_CAPACITY) {
            newCore = new ArrayCore<K, V>(entries.length + 1);
        } else {
            newCore = new HashMapCore<K, V>(entries.length);
        }

        for (final Entry<?, ?> entry : entries) {
            @SuppressWarnings("unchecked")
            final Entry<K, V> castEntry = (Entry<K, V>) entry;
            newCore.put(castEntry.getKey(), castEntry.getValue());
        }

        return newCore;
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
                        * freeIndex);

        for (int i = 0; i < freeIndex; i++) {
            @SuppressWarnings("unchecked")
            final Entry<K, V> castEntry = (Entry<K, V>) entries[i];
            
            result.add(new SimpleImmutableEntry<K, V>(castEntry));
        }

        return new HashSet<Entry<K, V>>(result);
    }

    /**
     * Prohledá záznamy.
     * 
     * @param key
     *            klíč záznamu
     * @return nalezený záznam, null pokud nenalezen
     */
    private Entry<K, V> findEntry(final K key) {
        for (int i = 0; i < freeIndex; i++) {
            final Entry<?, ?> entry = entries[i];
            
            if (key.equals(entry.getKey())) {
                @SuppressWarnings("unchecked")
                final Entry<K, V> castEntry = (Entry<K, V>) entry;
                
                return castEntry;
            }
        }

        return null;
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
        return freeIndex;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.MapperCore#
     * getCapacity()
     */
    @Override
    public int getCapacity() {
        return entries.length;
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
        result = prime * result + Arrays.hashCode(entries);
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
        final ArrayCore other = (ArrayCore) obj;
        if (!Arrays.equals(entries, other.entries)) {
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
        builder.append("ArrayCore [entries=");
        builder.append(freeIndex == 0 ? "{}" : Arrays.toString(entries)
                .replaceAll(", null", "").replaceFirst("\\[", "{")
                .replaceFirst("\\]", "}"));
        builder.append(", size=");
        builder.append(getSize());
        builder.append(", capacity=");
        builder.append(getCapacity());
        builder.append("]");
        return builder.toString();
    }
}
