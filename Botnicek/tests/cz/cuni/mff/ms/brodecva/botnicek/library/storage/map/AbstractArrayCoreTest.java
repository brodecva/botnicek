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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.Before;
import org.junit.Test;

/**
 * Abstraktní testovací třída pro {@link ArrayCore}.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @param <K>
 *            klíč
 * @param <V>
 *            hodnota
 */
public abstract class AbstractArrayCoreTest<K, V> {

    /**
     * Testováno s kapacitou.
     */
    private static final int CHOSEN_CAPACITY = ArrayCore.DEFAULT_CAPACITY;

    /**
     * Prázdné jádro.
     */
    private ArrayCore<K, V> core = null;

    /**
     * Vrátí platnou instanci klíče.
     * 
     * @return klíč
     */
    protected final K getValidKey() {
        return getUniqueValidKeys(1)[0];
    }

    /**
     * Vrátí různé platné instance klíče.
     * 
     * @param count
     *            počet
     * @return klíč
     */
    protected abstract K[] getUniqueValidKeys(int count);

    /**
     * Vrátí rovné si platné instance klíče.
     * 
     * @param count
     *            počet
     * @return klíč
     */
    protected abstract K[] getEqualValidKeys(int count);

    /**
     * Vrátí platnou instanci hodnoty.
     * 
     * @return hodnota
     */
    protected final V getValidValue() {
        return getUniqueValidValues(1)[0];
    }

    /**
     * Vrátí různé platné instance hodnoty.
     * 
     * @param count
     *            počet
     * @return hodnoty
     */
    protected abstract V[] getUniqueValidValues(int count);

    /**
     * Vrátí rovné si platné instance hodnoty.
     * 
     * @param count
     *            počet
     * @return hodnoty
     */
    protected abstract V[] getEqualValidValues(int count);

    /**
     * Vytvoří nové prázdné jádro.
     */
    @Before
    public final void setUp() {
        core = new ArrayCore<K, V>();
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.ArrayCore#ArrayCore()}
     * .
     */
    @Test
    public abstract void testArrayCoreInstantiation();

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.ArrayCore#ArrayCore(int)}
     * .
     */
    @Test
    public abstract void testArrayCoreIntWhenLegalCapacityInstantiation();

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.ArrayCore#ArrayCore(int)}
     * .
     */
    @Test(expected = IllegalArgumentException.class)
    public abstract void testArrayCoreIntWhenMoreThanLegalCapacity();

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.ArrayCore#ArrayCore(int)}
     * .
     */
    @Test(expected = IllegalArgumentException.class)
    public abstract void testArrayCoreIntWhenLessThanLegalCapacity();

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.ArrayCore#equals(java.lang.Object)}
     * a
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.ArrayCore#hashCode()}
     * .
     */
    @Test
    public final void testEqualsAndHashCode() {
        EqualsVerifier.forClass(ArrayCore.class).suppress(Warning.NULL_FIELDS)
                .usingGetClass().verify();
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.ArrayCore#get(java.lang.Object)}
     * .
     */
    @Test
    public final void testGetWhenEmptyReturnsNull() {
        assertNull(core.get(getValidKey()));
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.ArrayCore#get(java.lang.Object)}
     * .
     */
    @Test
    public final void testGetWhenKeyNullReturnsNull() {
        assertNull(core.get(null));
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.ArrayCore#put(java.lang.Object, java.lang.Object)}
     * a
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.ArrayCore#get(java.lang.Object)}
     * .
     */
    @Test
    public final void testPutAndGetWhenNotFullExpectNewValuePresent() {
        final K key = getValidKey();
        final V value = getValidValue();

        final boolean resize = core.put(key, value);
        assertFalse(resize);

        final V result = core.get(key);
        assertEquals(value, result);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.ArrayCore#put(java.lang.Object, java.lang.Object)}
     * a
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.ArrayCore#get(java.lang.Object)}
     * .
     */
    @Test
    public final
            void
            testPutAndGetWhenWhenFullTheKeyIsEqualAndValueIsEqualExpectTheValuePresent() {
        final K[] equalKeys = getEqualValidKeys(2);
        final K[] uniqueKeys = getEqualValidKeys(CHOSEN_CAPACITY - 2);
        final V value = getValidValue();

        for (final K uniqueKey : uniqueKeys) {
            core.put(uniqueKey, value);
        }
        core.put(equalKeys[0], value);

        final boolean resize = core.put(equalKeys[1], value);
        assertFalse(resize);

        final V result = core.get(equalKeys[1]);
        assertEquals(value, result);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.ArrayCore#put(java.lang.Object, java.lang.Object)}
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.ArrayCore#get(java.lang.Object)}
     * .
     */
    @Test
    public final
            void
            testPutAndGetWhenFullAndTheKeyIsEqualAndValuesAreUniqueExpectNewValuePresent() {
        final K[] equalKeys = getEqualValidKeys(CHOSEN_CAPACITY + 1);
        final V[] uniqueValues = getUniqueValidValues(CHOSEN_CAPACITY + 1);

        for (int i = 0; i < CHOSEN_CAPACITY; i++) {
            core.put(equalKeys[i], uniqueValues[i]);
        }

        final boolean resize =
                core.put(equalKeys[CHOSEN_CAPACITY],
                        uniqueValues[CHOSEN_CAPACITY]);
        assertFalse(resize);

        final V result = core.get(equalKeys[CHOSEN_CAPACITY]);
        assertEquals(uniqueValues[CHOSEN_CAPACITY], result);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.ArrayCore#put(java.lang.Object, java.lang.Object)}
     * .
     */
    @Test
    public final void testPutWhenFullAndTheKeyIsNotEqualReturnsResizeNeeded() {
        final K[] uniqueKeys = getUniqueValidKeys(CHOSEN_CAPACITY + 1);
        final V[] uniqueValues = getUniqueValidValues(CHOSEN_CAPACITY + 1);

        for (int i = 0; i < CHOSEN_CAPACITY; i++) {
            core.put(uniqueKeys[i], uniqueValues[i]);
        }

        final boolean result =
                core.put(uniqueKeys[CHOSEN_CAPACITY],
                        uniqueValues[CHOSEN_CAPACITY]);

        assertTrue(result);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.ArrayCore#put(java.lang.Object, java.lang.Object)}
     * .
     */
    @Test(expected = NullPointerException.class)
    public final void testPutWhenKeyNull() {
        core.put(null, getValidValue());
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.ArrayCore#put(java.lang.Object, java.lang.Object)}
     * .
     */
    @Test(expected = NullPointerException.class)
    public final void testPutWhenValueNull() {
        core.put(getValidKey(), null);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.ArrayCore#getEntries()}
     * .
     */
    @Test
    public final void testGetEntriesWhenEmptyReturnsEmptySet() {
        final Set<Entry<K, V>> result = core.getEntries();

        assertTrue(result.isEmpty());
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.ArrayCore#getEntries()}
     * .
     */
    @Test
    public final void
            testGetEntriesWhenNotEmptyReturnsSetWithEntryFromEqualKeyAndValue() {
        final K[] uniqueKeys = getUniqueValidKeys(CHOSEN_CAPACITY);
        final V[] uniqueValues = getUniqueValidValues(CHOSEN_CAPACITY);

        final Set<Entry<K, V>> reference = new HashSet<Entry<K, V>>();

        for (int i = 0; i < CHOSEN_CAPACITY; i++) {
            core.put(uniqueKeys[i], uniqueValues[i]);
            reference.add(new SimpleImmutableEntry<K, V>(uniqueKeys[i],
                    uniqueValues[i]));
        }

        final Set<Entry<K, V>> result = core.getEntries();

        assertEquals(reference, result);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.ArrayCore#resize()}
     * .
     */
    @Test
    public final void testResizeWhenEmptyReturnsSelf() {
        final MapperCore<K, V> newCore = core.resize();

        assertSame(core, newCore);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.ArrayCore#resize()}
     * .
     */
    @Test
    public final void testResizeWhenFullReturnsHashMapCore() {
        final K[] uniqueKeys = getUniqueValidKeys(CHOSEN_CAPACITY);
        final V[] uniqueValues = getUniqueValidValues(CHOSEN_CAPACITY);

        for (int i = 0; i < CHOSEN_CAPACITY; i++) {
            core.put(uniqueKeys[i], uniqueValues[i]);
        }

        final MapperCore<K, V> newCore = core.resize();

        assertTrue(newCore instanceof HashMapCore);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.ArrayCore#resize()}
     * .
     */
    @Test
    public final void
            testResizeWhenFullReturnsMapperCoreWithEqualSetOfEntries() {
        final K[] uniqueKeys = getUniqueValidKeys(CHOSEN_CAPACITY);
        final V[] uniqueValues = getUniqueValidValues(CHOSEN_CAPACITY);

        for (int i = 0; i < CHOSEN_CAPACITY; i++) {
            core.put(uniqueKeys[i], uniqueValues[i]);
        }

        final MapperCore<K, V> newCore = core.resize();

        assertEquals(core.getEntries(), newCore.getEntries());
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.ArrayCore#getSize()}
     * .
     */
    @Test
    public final void testGetSizeWhenEmptyReturnsZero() {
        final int size = core.getSize();

        assertEquals(0, size);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.ArrayCore#getSize()}
     * .
     */
    @Test
    public final void testGetSizeWhenFullReturnsCapacity() {
        core.put(getValidKey(), getValidValue());

        final int capacity = core.getCapacity();

        assertEquals(CHOSEN_CAPACITY, capacity);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.ArrayCore#getCapacity()}
     * .
     */
    @Test
    public final void testGetCapacityWhenEmptyReturnsCorrectCapacity() {
        final int capacity = core.getCapacity();

        assertEquals(CHOSEN_CAPACITY, capacity);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.ArrayCore#getCapacity()}
     * .
     */
    @Test
    public final void testGetCapacityWhenNotEmptyReturnsCorrectCapacity() {
        final K[] uniqueKeys = getUniqueValidKeys(CHOSEN_CAPACITY);
        final V[] uniqueValues = getUniqueValidValues(CHOSEN_CAPACITY);

        for (int i = 0; i < CHOSEN_CAPACITY; i++) {
            core.put(uniqueKeys[i], uniqueValues[i]);
        }

        final int capacity = core.getCapacity();

        assertEquals(CHOSEN_CAPACITY, capacity);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.ArrayCore#toString()}
     * .
     */
    @Test
    public final void testToStringWhenEmpty() {
        final String expected =
                "ArrayCore [entries={}, size=0, capacity=" + CHOSEN_CAPACITY
                        + "]";

        final String result = core.toString();

        assertEquals(expected, result);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.ArrayCore#toString()}
     * .
     */
    @Test
    public final void testToStringWhenFull() {
        final K[] uniqueKeys = getUniqueValidKeys(CHOSEN_CAPACITY);
        final V[] uniqueValues = getUniqueValidValues(CHOSEN_CAPACITY);

        final StringBuilder entriesExpected = new StringBuilder();
        for (int i = 0; i < CHOSEN_CAPACITY; i++) {
            core.put(uniqueKeys[i], uniqueValues[i]);
            entriesExpected.append(", " + uniqueKeys[i].toString() + "="
                    + uniqueValues[i]);
        }

        final String expected =
                "ArrayCore [entries={"
                        + entriesExpected.toString().substring(2) + "}, size="
                        + CHOSEN_CAPACITY + ", capacity=" + CHOSEN_CAPACITY
                        + "]";

        final String result = core.toString();

        assertEquals(expected, result);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.ArrayCore#toString()}
     * .
     */
    @Test
    public final void testToStringWhenSizeOne() {
        final K key = getValidKey();
        final V value = getValidValue();

        core.put(key, value);

        final String expected =
                "ArrayCore [entries={" + key.toString() + "="
                        + value.toString() + "}, size=1, capacity="
                        + CHOSEN_CAPACITY + "]";

        final String result = core.toString();

        assertEquals(expected, result);
    }

    /**
     * Test serializace.
     * 
     * @throws IOException
     *             pokud dojde k I/O chybě
     * @throws ClassNotFoundException
     *             pokud není nalezena třída
     */
    @Test
    public final void testRoundTripSerialization() throws IOException,
            ClassNotFoundException {
        assumeTrue(getValidKey() instanceof Serializable);
        assumeTrue(getValidValue() instanceof Serializable);

        final ArrayCore<K, V> original = core;
        for (int i = 0; i < CHOSEN_CAPACITY; i++) {
            original.put(getValidKey(), getValidValue());
        }

        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final ObjectOutputStream oos = new ObjectOutputStream(out);
        oos.writeObject(original);
        oos.close();

        final byte[] pickled = out.toByteArray();
        final InputStream in = new ByteArrayInputStream(pickled);
        final ObjectInputStream ois = new ObjectInputStream(in);
        final Object o = ois.readObject();
        @SuppressWarnings("unchecked")
        final ArrayCore<K, V> copy = (ArrayCore<K, V>) o;

        assertEquals(original, copy);
    }

    /**
     * Test neschopnosti serializace.
     * 
     * @throws IOException
     *             pokud dojde k I/O chybě
     */
    @Test
    public final void testNotSerializable() throws IOException {
        assumeTrue(!(getValidKey() instanceof Serializable)
                || !(getValidValue() instanceof Serializable));

        final ArrayCore<K, V> object = core;
        for (int i = 0; i < CHOSEN_CAPACITY; i++) {
            object.put(getValidKey(), getValidValue());
        }

        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final ObjectOutputStream oout = new ObjectOutputStream(out);

        try {
            oout.writeObject(object);
            fail("Serializable");
        } catch (final NotSerializableException e) {
            return;
        }
    }

}
