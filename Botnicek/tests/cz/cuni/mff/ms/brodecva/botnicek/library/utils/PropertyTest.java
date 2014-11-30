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
package cz.cuni.mff.ms.brodecva.botnicek.library.utils;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.easymock.EasyMock;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import cz.cuni.mff.ms.brodecva.botnicek.library.utils.test.UnitTest;

/**
 * Testuje pomocnou třídu pro práci s {@link java.util.Properties}.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see Property
 */
@Category(UnitTest.class)
public final class PropertyTest {
    
    /**
     * Testovací metoda pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.Property#load(java.io.InputStream)}.
     * @throws IOException chyba při čtení
     */
    @Test(expected = NullPointerException.class)
    public void testLoadInputStreamWhenStreamNull() throws IOException {
        Property.load((InputStream) null);
    }
    
    /**
     * Testovací metoda pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.Property#load(java.io.InputStream)}.
     * @throws IOException chyba při čtení
     */
    @Test
    public void testLoadInputStreamWhenValidInputReturnsExpected() throws IOException {
        final String source = "hello = there\nI\\ am = Jack"; 
        
        final InputStream input = new ByteArrayInputStream(source.getBytes(Charset.defaultCharset()));
        
        final Map<Object, Object> expected = new HashMap<Object, Object>();
        expected.put("hello", "there");
        expected.put("I am", "Jack");
        
        assertEquals(expected, Property.load(input));
    }
    
    /**
     * Testovací metoda pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.Property#load(java.io.InputStream)}.
     * @throws IOException chyba při čtení
     */
    @Test
    public void testLoadInputStreamWhenEmptyValueReturnsExpected() throws IOException {
        final String source = "hello ="; 
        
        final InputStream input = new ByteArrayInputStream(source.getBytes(Charset.defaultCharset()));
        
        final Map<Object, Object> expected = new HashMap<Object, Object>();
        expected.put("hello", "");
        
        assertEquals(expected, Property.load(input));
    }
    
    /**
     * Testovací metoda pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.Property#toMap(java.util.Properties)}.
     */
    @Test(expected = NullPointerException.class)
    public void testToMapWhenNull() {
        Property.toMap(null);
    }
    
    /**
     * Testovací metoda pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.Property#toMap(java.util.Properties)}.
     */
    @Test
    public void testToMapWhenAllValuesValidReturnsExpected() {
        final Set<Entry<Object, Object>> entriesStub = new HashSet<Entry<Object, Object>>();
        entriesStub.add(new SimpleImmutableEntry<Object, Object>("dummyKey1", "dummyValue1"));
        entriesStub.add(new SimpleImmutableEntry<Object, Object>("dummyKey2", "dummyValue2"));
        entriesStub.add(new SimpleImmutableEntry<Object, Object>("dummyKey3", "dummyValue3"));
        
        final Map<String, String> expected = new HashMap<String, String>();
        expected.put("dummyKey1", "dummyValue1");
        expected.put("dummyKey2", "dummyValue2");
        expected.put("dummyKey3", "dummyValue3");
        
        final Properties propertiesStub = EasyMock.createMock(Properties.class);
        expect(propertiesStub.entrySet()).andStubReturn(entriesStub);
        replay(propertiesStub);
        
        assertEquals(expected, Property.toMap(propertiesStub));
    }
    
    /**
     * Testovací metoda pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.Property#toMap(java.util.Properties)}.
     */
    @Test
    public void testToMapWhenSomeKeyNullReturnsExpected() {
        final Set<Entry<Object, Object>> entriesStub = new HashSet<Entry<Object, Object>>();
        entriesStub.add(new SimpleImmutableEntry<Object, Object>(null, "dummyValue1"));
        entriesStub.add(new SimpleImmutableEntry<Object, Object>("dummyKey2", "dummyValue2"));
        entriesStub.add(new SimpleImmutableEntry<Object, Object>("dummyKey3", "dummyValue3"));
        
        final Map<String, String> expected = new HashMap<String, String>();
        expected.put(null, "dummyValue1");
        expected.put("dummyKey2", "dummyValue2");
        expected.put("dummyKey3", "dummyValue3");
        
        final Properties propertiesStub = EasyMock.createMock(Properties.class);
        expect(propertiesStub.entrySet()).andStubReturn(entriesStub);
        replay(propertiesStub);
        
        assertEquals(expected, Property.toMap(propertiesStub));
    }
    
    /**
     * Testovací metoda pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.Property#toMap(java.util.Properties)}.
     */
    @Test
    public void testToMapWhenSomeValueNullReturnsExpected() {
        final Set<Entry<Object, Object>> entriesStub = new HashSet<Entry<Object, Object>>();
        entriesStub.add(new SimpleImmutableEntry<Object, Object>("dummyKey1", null));
        entriesStub.add(new SimpleImmutableEntry<Object, Object>("dummyKey2", "dummyValue2"));
        entriesStub.add(new SimpleImmutableEntry<Object, Object>("dummyKey3", "dummyValue3"));
        
        final Map<String, String> expected = new HashMap<String, String>();
        expected.put("dummyKey1", null);
        expected.put("dummyKey2", "dummyValue2");
        expected.put("dummyKey3", "dummyValue3");
        
        final Properties propertiesStub = EasyMock.createMock(Properties.class);
        expect(propertiesStub.entrySet()).andStubReturn(entriesStub);
        replay(propertiesStub);
        
        assertEquals(expected, Property.toMap(propertiesStub));
    }
    
    /**
     * Testovací metoda pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.Property#toMap(java.util.Properties)}.
     */
    @Test(expected = ClassCastException.class)
    public void testToMapWhenSomeKeyNotString() {
        final Set<Entry<Object, Object>> entriesStub = new HashSet<Entry<Object, Object>>();
        entriesStub.add(new SimpleImmutableEntry<Object, Object>("dummyKey1", "dummyValue1"));
        entriesStub.add(new SimpleImmutableEntry<Object, Object>(new Object(), "dummyValue2"));
        entriesStub.add(new SimpleImmutableEntry<Object, Object>("dummyKey3", "dummyValue3"));
        
        final Properties propertiesStub = EasyMock.createMock(Properties.class);
        expect(propertiesStub.entrySet()).andStubReturn(entriesStub);
        replay(propertiesStub);
        
        Property.toMap(propertiesStub);
    }
    
    /**
     * Testovací metoda pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.Property#toMap(java.util.Properties)}.
     */
    @Test(expected = ClassCastException.class)
    public void testToMapWhenSomeValueNotString() {
        final Set<Entry<Object, Object>> entriesStub = new HashSet<Entry<Object, Object>>();
        entriesStub.add(new SimpleImmutableEntry<Object, Object>("dummyKey1", "dummyValue1"));
        entriesStub.add(new SimpleImmutableEntry<Object, Object>("dummyKey2", "dummyValue2"));
        entriesStub.add(new SimpleImmutableEntry<Object, Object>("dummyKey3", new Object()));
        
        final Properties propertiesStub = EasyMock.createMock(Properties.class);
        expect(propertiesStub.entrySet()).andStubReturn(entriesStub);
        replay(propertiesStub);
        
        Property.toMap(propertiesStub);
    }

    /**
     * Testovací metoda pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.Property#checkEntriesNotNull(java.util.Map)}.
     */
    @Test
    public void testCheckEntriesNotNullWhenAllNotNullReturnsTrue() {
        final Map<String, String> notNullProperties = new HashMap<String, String>();
        
        notNullProperties.put("dummyKey1", "dummyValue1");
        notNullProperties.put("dummyKey2", "dummyValue2");
        notNullProperties.put("dummyKey3", "dummyValue3");
        
        assertTrue(Property.checkEntriesNotNull(notNullProperties));
    }
    
    /**
     * Testovací metoda pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.Property#checkEntriesNotNull(java.util.Map)}.
     */
    @Test
    public void testCheckEntriesNotNullWhenEmptyReturnsTrue() {
        final Map<String, String> empty = new HashMap<String, String>();
        assertTrue(Property.checkEntriesNotNull(empty));
    }
    
    /**
     * Testovací metoda pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.Property#checkEntriesNotNull(java.util.Map)}.
     */
    @Test(expected = NullPointerException.class)
    public void testCheckEntriesWhenNull() {
        Property.checkEntriesNotNull(null);
    }
    
    /**
     * Testovací metoda pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.Property#checkEntriesNotNull(java.util.Map)}.
     */
    @Test
    public void testCheckEntriesNotNullWhenSomeKeyNullReturnsFalse() {
        final Map<String, String> notNullProperties = new HashMap<String, String>();
        
        notNullProperties.put("dummyKey1", "dummyValue1");
        notNullProperties.put(null, "dummyValue2");
        notNullProperties.put("dummyKey3", "dummyValue3");
        
        assertFalse(Property.checkEntriesNotNull(notNullProperties));
    }
    
    /**
     * Testovací metoda pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.Property#checkEntriesNotNull(java.util.Map)}.
     */
    @Test
    public void testCheckEntriesNotNullWhenSomeValueNullReturnsFalse() {
        final Map<String, String> notNullProperties = new HashMap<String, String>();
        
        notNullProperties.put("dummyKey1", "dummyValue1");
        notNullProperties.put("dummyKey2", "dummyValue2");
        notNullProperties.put("dummyKey3", null);
        
        assertFalse(Property.checkEntriesNotNull(notNullProperties));
    }

}
