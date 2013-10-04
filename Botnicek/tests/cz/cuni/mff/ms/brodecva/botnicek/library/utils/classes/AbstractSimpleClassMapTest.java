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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
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
import java.util.regex.Pattern;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.Before;
import org.junit.Test;

/**
 * Abstraktní test jednoduché mapy tříd.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see SimpleClassMap
 * 
 * @param <K>
 *            klíč
 * @param <T>
 *            supertyp uložených objektů tříd {@link Class}
 */
public abstract class AbstractSimpleClassMapTest<K, T> {

    /**
     * Testováno s velikostí.
     */
    private static final int CHOSEN_SIZE = 1000;

    /**
     * Prázdné jádro.
     */
    private SimpleClassMap<K, T> map = null;

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
    protected final Class<? extends T> getValidValue() {
        return getUniqueValidValues(1)[0];
    }

    /**
     * Vrátí různé platné instance hodnoty.
     * 
     * @param count
     *            počet
     * @return hodnoty
     */
    protected abstract Class<? extends T>[] getUniqueValidValues(int count);

    /**
     * Vrátí rovné si platné instance hodnoty.
     * 
     * @param count
     *            počet
     * @return hodnoty
     */
    protected abstract Class<? extends T>[] getEqualValidValues(int count);

    /**
     * Vytvoří nové prázdné jádro.
     */
    @Before
    public final void setUp() {
        map = new SimpleClassMap<K, T>();
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.classes.SimpleClassMap#SimpleClassMap()}
     * .
     */
    @Test
    public abstract void testHashMapCoreInstantiation();

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.classes.SimpleClassMap#equals(java.lang.Object)}
     * a
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.classes.SimpleClassMap#hashCode()}
     * .
     */
    @Test
    public final void testEqualsAndHashCode() {
        EqualsVerifier.forClass(SimpleClassMap.class).usingGetClass().suppress(Warning.NULL_FIELDS).verify();
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.classes.SimpleClassMap#get(java.lang.Object)}
     * .
     */
    @Test
    public final void testGetWhenEmptyReturnsNull() {
        assertNull(map.get(getValidKey()));
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.classes.SimpleClassMap#get(java.lang.Object)}
     * .
     */
    @Test
    public final void testGetWhenKeyNullReturnsNull() {
        assertNull(map.get(null));
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.classes.SimpleClassMap#put(java.lang.Object, Class)}
     * a
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.classes.SimpleClassMap#get(java.lang.Object)}
     * .
     */
    @Test
    public final void testPutClassAndGetWhenNotEmptyExpectNewValuePresent() {
        final K key = getValidKey();
        final Class<? extends T> value = getValidValue();

        map.put(key, value);

        final Class<? extends T> result = map.get(key);
        assertEquals(value, result);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.classes.SimpleClassMap#put(java.lang.Object, Class)}
     * a
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.classes.SimpleClassMap#get(java.lang.Object)}
     * .
     */
    @Test
    public final
            void
            testPutClassAndGetTheKeyIsEqualAndValueIsEqualExpectTheValuePresent() {
        final K[] equalKeys = getEqualValidKeys(CHOSEN_SIZE + 1);
        final Class<? extends T> value = getValidValue();

        for (int i = 0; i < CHOSEN_SIZE; i++) {
            map.put(equalKeys[i], value);
        }

        map.put(equalKeys[CHOSEN_SIZE], value);

        final Class<? extends T> result = map.get(equalKeys[CHOSEN_SIZE]);
        assertEquals(value, result);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.classes.SimpleClassMap#put(java.lang.Object, Class)}
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.classes.SimpleClassMap#get(java.lang.Object)}
     * .
     */
    @Test
    public final
            void
            testPutClassAndGetWhenTheKeyIsEqualAndValuesAreUniqueExpectNewValuePresent() {
        final K[] equalKeys = getEqualValidKeys(CHOSEN_SIZE + 1);
        final Class<? extends T>[] uniqueValues =
                getUniqueValidValues(CHOSEN_SIZE + 1);

        for (int i = 0; i < CHOSEN_SIZE; i++) {
            map.put(equalKeys[i], uniqueValues[i]);
        }

        map.put(equalKeys[CHOSEN_SIZE], uniqueValues[CHOSEN_SIZE]);

        final Class<? extends T> result = map.get(equalKeys[CHOSEN_SIZE]);
        assertEquals(uniqueValues[CHOSEN_SIZE], result);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.classes.SimpleClassMap#put(java.lang.Object, Class)}
     * .
     */
    @Test
    public final void testPutClassWhenKeyNullAccepts() {
        map.put(null, getValidValue());
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.classes.SimpleClassMap#put(java.lang.Object, Class)}
     * .
     */
    @Test
    public final void testPutClassWhenValueNullAccepts() {
        map.put(getValidKey(), (Class<? extends T>) null);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.classes.SimpleClassMap#put(java.lang.Object, java.lang.String)}
     * .
     * 
     * @throws ClassNotFoundException
     *             pokud není třída s daným jménem nalezena
     */
    @Test
    public final void testPutStringWhenKeyNullAccepts()
            throws ClassNotFoundException {
        map.put(null, "java.lang.Object");
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.classes.SimpleClassMap#put(java.lang.Object, java.lang.String)}
     * .
     * 
     * @throws ClassNotFoundException
     *             pokud není třída s daným jménem nalezena
     */
    @Test(expected = ClassNotFoundException.class)
    public final void testPutStringWhenValueNotExists()
            throws ClassNotFoundException {
        map.put(getValidKey(),
                "cz.cuni.mff.ms.brodecva.botnicek.library.utils.classes.SimpleClassMap.NoSuchClass");
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.classes.SimpleClassMap#put(java.lang.Object, java.lang.String)}
     * .
     * 
     * @throws ClassNotFoundException
     *             pokud není třída s daným jménem nalezena
     */
    @Test
    public final void testPutStringWhenValueExists()
            throws ClassNotFoundException {
        map.put(getValidKey(), "java.lang.Object");
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.classes.SimpleClassMap#put(java.lang.Object, java.lang.String)}
     * .
     * 
     * @throws ClassNotFoundException
     *             pokud není třída s daným jménem nalezena
     */
    @Test(expected = NullPointerException.class)
    public final void testPutStringWhenValueNull()
            throws ClassNotFoundException {
        map.put(getValidKey(), (String) null);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.classes.SimpleClassMap#toString()}
     * .
     */
    @Test
    public final void testToStringWhenEmpty() {
        final String expected = "SimpleClassMap [classMap={}]";

        final String result = map.toString();

        assertEquals(expected, result);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.classes.SimpleClassMap#toString()}
     * .
     */
    @Test
    public final void testToStringWhenSizeMoreThanOne() {
        final K[] uniqueKeys = getUniqueValidKeys(CHOSEN_SIZE);
        final Class<? extends T>[] uniqueValues =
                getUniqueValidValues(CHOSEN_SIZE);

        final StringBuilder entriesExpected = new StringBuilder();
        for (int i = 0; i < CHOSEN_SIZE; i++) {
            map.put(uniqueKeys[i], uniqueValues[i]);
            entriesExpected.append(", " + uniqueKeys[i].toString() + "="
                    + uniqueValues[i]);
        }

        final String result = map.toString();

        final String expectedRegex =
                "\\ASimpleClassMap \\[classMap=\\{.*(, .*)+\\}\\]\\z";

        assertTrue(Pattern.matches(expectedRegex, result));
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.classes.SimpleClassMap#toString()}
     * .
     */
    @Test
    public final void testToStringWhenSizeOne() {
        final K key = getValidKey();
        final Class<? extends T> value = getValidValue();

        map.put(key, value);

        final String expected =
                "SimpleClassMap [classMap={" + key.toString() + "="
                        + value.toString() + "}]";

        final String result = map.toString();

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

        final SimpleClassMap<K, T> original = map;

        for (int i = 0; i < CHOSEN_SIZE; i++) {
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
        final SimpleClassMap<K, T> copy = (SimpleClassMap<K, T>) o;

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

        final SimpleClassMap<K, T> object = map;

        for (int i = 0; i < CHOSEN_SIZE; i++) {
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
