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
import java.util.Map.Entry;
import java.util.Set;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.Before;
import org.junit.Test;

/**
 * Abstraktní testovací třída pro {@link SingleEntryCore}.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @param <K>
 *            klíč
 * @param <V>
 *            hodnota
 */
public abstract class AbstractSingleEntryCoreTest<K, V> {

    /**
     * Prázdné jádro.
     */
    private SingleEntryCore<K, V> core = new SingleEntryCore<K, V>();

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
        core = new SingleEntryCore<K, V>();
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.SingleEntryCore#SingleEntryCore()}
     * .
     */
    @Test
    public abstract void testSingleEntryCoreInstantiation();

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.SingleEntryCore#SingleEntryCore(int)}
     * .
     */
    @Test
    public abstract void testSingleEntryCoreIntWhenLegalCapacityInstantiation();

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.SingleEntryCore#SingleEntryCore(int)}
     * .
     */
    @Test(expected = IllegalArgumentException.class)
    public abstract void testSingleEntryCoreIntWhenIllegalCapacity();

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.SingleEntryCore#equals(java.lang.Object)}
     * a
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.SingleEntryCore#hashCode()}
     * .
     */
    @Test
    public final void testEqualsAndHashCode() {
        EqualsVerifier.forClass(SingleEntryCore.class)
                .suppress(Warning.NONFINAL_FIELDS).usingGetClass().verify();
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.SingleEntryCore#get(java.lang.Object)}
     * .
     */
    @Test
    public final void testGetWhenEmptyReturnsNull() {
        assertNull(core.get(getValidKey()));
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.SingleEntryCore#get(java.lang.Object)}
     * .
     */
    @Test
    public final void testGetWhenKeyNullReturnsNull() {
        assertNull(core.get(null));
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.SingleEntryCore#put(java.lang.Object, java.lang.Object)}
     * a
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.SingleEntryCore#get(java.lang.Object)}
     * .
     */
    @Test
    public final void testPutAndGetWhenEmptyExpectNewValuePresent() {
        final K key = getValidKey();
        final V value = getValidValue();

        final boolean resize = core.put(key, value);
        assertFalse(resize);

        final V result = core.get(key);
        assertEquals(value, result);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.SingleEntryCore#put(java.lang.Object, java.lang.Object)}
     * a
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.SingleEntryCore#get(java.lang.Object)}
     * .
     */
    @Test
    public final
            void
            testPutAndGetWhenFullAndTheKeyIsEqualAndValueIsEqualExpectTheValuePresent() {
        final K[] equalKeys = getEqualValidKeys(2);
        final V value = getValidValue();

        core.put(equalKeys[0], value);

        final boolean resize = core.put(equalKeys[1], value);
        assertFalse(resize);

        final V result = core.get(equalKeys[1]);
        assertEquals(value, result);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.SingleEntryCore#put(java.lang.Object, java.lang.Object)}
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.SingleEntryCore#get(java.lang.Object)}
     * .
     */
    @Test
    public final
            void
            testPutAndGetWhenFullAndTheKeyIsEqualAndValuesAreUniqueExpectNewValuePresent() {
        final K[] equalKeys = getEqualValidKeys(2);
        final V[] uniqueValues = getUniqueValidValues(2);

        core.put(equalKeys[0], uniqueValues[0]);

        final boolean resize = core.put(equalKeys[1], uniqueValues[1]);
        assertFalse(resize);

        final V result = core.get(equalKeys[1]);
        assertEquals(uniqueValues[1], result);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.SingleEntryCore#put(java.lang.Object, java.lang.Object)}
     * .
     */
    @Test
    public final void testPutWhenFullAndTheKeyIsNotEqualReturnsResizeNeeded() {
        final K[] uniqueKeys = getUniqueValidKeys(2);
        final V[] uniqueValues = getUniqueValidValues(2);

        core.put(uniqueKeys[0], uniqueValues[0]);

        final boolean result = core.put(uniqueKeys[1], uniqueValues[1]);

        assertTrue(result);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.SingleEntryCore#put(java.lang.Object, java.lang.Object)}
     * .
     */
    @Test(expected = NullPointerException.class)
    public final void testPutWhenKeyNull() {
        core.put(null, getValidValue());
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.SingleEntryCore#put(java.lang.Object, java.lang.Object)}
     * .
     */
    @Test(expected = NullPointerException.class)
    public final void testPutWhenValueNull() {
        core.put(getValidKey(), null);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.SingleEntryCore#getEntries()}
     * .
     */
    @Test
    public final void testGetEntriesWhenEmptyReturnsEmptySet() {
        final Set<Entry<K, V>> result = core.getEntries();

        assertTrue(result.isEmpty());
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.SingleEntryCore#getEntries()}
     * .
     */
    @Test
    public final void
            testGetEntriesWhenFullReturnsSetWithEntryFromEqualKeyAndValue() {
        final K key = getValidKey();
        final V value = getValidValue();

        core.put(key, value);

        final Set<Entry<K, V>> result = core.getEntries();

        final Entry<K, V> resultEntry = result.iterator().next();

        assertEquals(key, resultEntry.getKey());
        assertEquals(value, resultEntry.getValue());
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.SingleEntryCore#resize()}
     * .
     */
    @Test
    public final void testResizeWhenEmptyReturnsSelf() {
        final MapperCore<K, V> newCore = core.resize();

        assertSame(core, newCore);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.SingleEntryCore#resize()}
     * .
     */
    @Test
    public final void testResizeWhenFullReturnsArrayCore() {
        core.put(getValidKey(), getValidValue());

        final MapperCore<K, V> newCore = core.resize();

        assertTrue(newCore instanceof ArrayCore);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.SingleEntryCore#resize()}
     * .
     */
    @Test
    public final void
            testResizeWhenFullReturnsMapperCoreWithEqualSetOfEntries() {
        core.put(getValidKey(), getValidValue());

        final MapperCore<K, V> newCore = core.resize();

        final Set<Entry<K, V>> oldEntries = core.getEntries();
        final Set<Entry<K, V>> newEntries = newCore.getEntries();

        assertEquals(oldEntries, newEntries);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.SingleEntryCore#getSize()}
     * .
     */
    @Test
    public final void testGetSizeWhenEmptyReturnsZero() {
        final int size = core.getSize();

        assertEquals(0, size);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.SingleEntryCore#getSize()}
     * .
     */
    @Test
    public final void testGetSizeWhenFullReturnsOne() {
        core.put(getValidKey(), getValidValue());

        final int size = core.getSize();

        assertEquals(1, size);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.SingleEntryCore#getCapacity()}
     * .
     */
    @Test
    public final void testGetCapacityWhenEmptyReturnsOne() {
        final int capacity = core.getCapacity();

        assertEquals(1, capacity);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.SingleEntryCore#getCapacity()}
     * .
     */
    @Test
    public final void testGetCapacityWhenFullReturnsOne() {
        core.put(getValidKey(), getValidValue());

        final int capacity = core.getCapacity();

        assertEquals(1, capacity);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.SingleEntryCore#toString()}
     * .
     */
    @Test
    public final void testToStringWhenEmpty() {
        final String expected =
                "SingleEntryCore [entries={}, size=0, capacity=1]";

        final String result = core.toString();

        assertEquals(expected, result);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.SingleEntryCore#toString()}
     * .
     */
    @Test
    public final void testToStringWhenFull() {
        final K key = getValidKey();
        final V value = getValidValue();

        core.put(key, value);

        final String expected =
                "SingleEntryCore [entries={" + key.toString() + "="
                        + value.toString() + "}, size=1, capacity=1]";

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

        final SingleEntryCore<K, V> original = core;
        original.put(getValidKey(), getValidValue());

        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final ObjectOutputStream oos = new ObjectOutputStream(out);
        oos.writeObject(original);
        oos.close();

        final byte[] pickled = out.toByteArray();
        final InputStream in = new ByteArrayInputStream(pickled);
        final ObjectInputStream ois = new ObjectInputStream(in);
        final Object o = ois.readObject();
        @SuppressWarnings("unchecked")
        final SingleEntryCore<K, V> copy = (SingleEntryCore<K, V>) o;

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

        final SingleEntryCore<K, V> object = core;
        object.put(getValidKey(), getValidValue());

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
