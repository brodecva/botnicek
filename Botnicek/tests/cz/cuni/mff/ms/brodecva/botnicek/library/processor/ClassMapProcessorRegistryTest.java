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
package cz.cuni.mff.ms.brodecva.botnicek.library.processor;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.not;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.net.URI;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Set;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.w3c.dom.Element;

import cz.cuni.mff.ms.brodecva.botnicek.library.parser.TemplateParser;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.classes.ClassControlError;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.classes.ClassMap;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.test.UnitTest;

/**
 * Testuje registr procesorů načtený do {@link ClassMap}.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see ClassMapProcessorRegistry
 */
@Category(UnitTest.class)
public final class ClassMapProcessorRegistryTest {

    /**
     * Implementace rozhraní {@link Processor} pro testovací účely.
     * 
     * @author Václav Brodec
     * @version 1.0
     */
    public static final class ProcessorImplementation implements Processor {

        /*
         * (non-Javadoc)
         * 
         * @see
         * cz.cuni.mff.ms.brodecva.botnicek.library.processor.Processor#process
         * (org.w3c.dom.Element,
         * cz.cuni.mff.ms.brodecva.botnicek.library.parser.TemplateParser)
         */
        @Override
        public String
                process(final Element element, final TemplateParser parser)
                        throws ProcessorException {
            return null;
        }

    }

    /**
     * Zástupné URI prostoru jmen.
     */
    private static final URI NAMESPACE = URI.create("http://stub.com/");

    /**
     * Klíč třídy.
     */
    private static final String CLASS_KEY = "classKey";

    /**
     * Jméno uložené třídy.
     */
    private static final String CLASS_NAME = ProcessorImplementation.class
            .getName();

    /**
     * Klíč nenalezené třídy.
     */
    private static final String KEY_FOR_NOT_FOUND_CLASS = "notFoundClassKey";

    /**
     * Název nenalezené třídy.
     */
    private static final String NOT_FOUND_CLASS_NAME =
            "cz.cuni.mff.ms.brodecva.botnicek.library.processor.not.found.Klass";

    /**
     * Jiné URI než jmenného prostoru registru.
     */
    private static final URI ANOTHER_NAMESPACE = URI
            .create("http://another.namespace.cz");

    /**
     * Klíč, který se nenachází v registru.
     */
    private static final String NOT_PRESENT_KEY = "notPresentKey";

    /**
     * Mock {@link ClassMap}.
     */
    private ClassMap<String, Processor> putClassMapMock = null;

    /**
     * Stub {@link java.util.Map} s platnými názvy tříd.
     */
    private Map<String, String> allValidConfigStub = null;

    /**
     * Stub {@link java.util.Map} s názvy tříd, jež nejsou k nalezení.
     */
    private Map<String, String> notFoundConfigStub = null;

    /**
     * Registr určený k výběru.
     */
    private ProcessorRegistry registryForGet = null;

    /**
     * Mock {@link ClassMap} pro výběrový registr.
     */
    private ClassMap<String, Processor> getClassMapMock;

    /**
     * Vytvoří pomocné stuby, mocky a testované objekty.
     * 
     * @throws ClassNotFoundException
     *             třída nenalezena
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Before
    public void setUp() throws ClassNotFoundException {
        putClassMapMock = EasyMock.createMock(ClassMap.class);
        
        allValidConfigStub = EasyMock.createMock(Map.class);
        expect(allValidConfigStub.get(CLASS_KEY)).andStubReturn(CLASS_NAME);
        
        final Set<Entry<String, String>> allValidConfigStubEntries = new HashSet<Entry<String, String>>();
        allValidConfigStubEntries.add(new SimpleImmutableEntry<String, String>(CLASS_KEY, CLASS_NAME));
        expect(allValidConfigStub.entrySet()).andStubReturn(allValidConfigStubEntries);
        
        replay(allValidConfigStub);
        
        notFoundConfigStub = EasyMock.createMock(Map.class);
        expect(notFoundConfigStub.get(KEY_FOR_NOT_FOUND_CLASS)).andStubReturn(NOT_FOUND_CLASS_NAME);
        
        final Set<Entry<String, String>> notFoundConfigStubEntries = new HashSet<Entry<String, String>>();
        notFoundConfigStubEntries.add(new SimpleImmutableEntry<String, String>(KEY_FOR_NOT_FOUND_CLASS, NOT_FOUND_CLASS_NAME));
        expect(notFoundConfigStub.entrySet()).andStubReturn(notFoundConfigStubEntries);
        
        replay(notFoundConfigStub);

        getClassMapMock = EasyMock.createMock(ClassMap.class);
        getClassMapMock.put(eq(CLASS_KEY), eq(CLASS_NAME));
        expect((Class) getClassMapMock.get(CLASS_KEY)).andStubReturn(
                ProcessorImplementation.class);
        expect((Class) getClassMapMock.get(NOT_PRESENT_KEY)).andStubReturn(null);
        replay(getClassMapMock);

        registryForGet =
                ClassMapProcessorRegistry.create(NAMESPACE, allValidConfigStub,
                        getClassMapMock);
    }

    /**
     * Uklidí stuby a mocky.
     */
    @After
    public void tearDown() {
        allValidConfigStub = null;
        
        getClassMapMock = null;
        
        notFoundConfigStub = null;
        
        putClassMapMock = null;
        
        registryForGet = null;
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.processor.ClassMapProcessorRegistry#hashCode()}
     * a
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.processor.ClassMapProcessorRegistry#equals(java.lang.Object)}
     * .
     */
    @Test
    public void testEqualsAndHashCode() {
        EqualsVerifier.forClass(ClassMapProcessorRegistry.class).usingGetClass();
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.processor.ClassMapProcessorRegistry#create(URI, Map, ClassMap)}
     * .
     * 
     * @throws ClassNotFoundException
     *             třída nenalezena
     */
    @Test
    public void testCreate() throws ClassNotFoundException {
        putClassMapMock.put(eq(CLASS_KEY), eq(CLASS_NAME));
        replay(putClassMapMock);

        ClassMapProcessorRegistry.create(NAMESPACE, allValidConfigStub,
                putClassMapMock);

        verify(putClassMapMock);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.processor.ClassMapProcessorRegistry#create(java.net.URI, java.util.Map, cz.cuni.mff.ms.brodecva.botnicek.library.utils.classes.ClassMap)}
     * .
     * 
     * @throws ClassNotFoundException
     *             třída nenalezena
     */
    @Test(expected = ClassControlError.class)
    public void testCreateWhenNotFoundClass() throws ClassNotFoundException {
        putClassMapMock.put(anyObject(String.class), not(eq(CLASS_NAME)));
        EasyMock.expectLastCall().andThrow(new ClassNotFoundException());
        replay(putClassMapMock);

        ClassMapProcessorRegistry.create(NAMESPACE, notFoundConfigStub,
                putClassMapMock);

        verify(putClassMapMock);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.processor.ClassMapProcessorRegistry#get(java.lang.String, java.net.URI)}
     * .
     * 
     * @throws ClassNotFoundException
     *             třída nenalezena
     */
    @Test(expected = ClassNotFoundException.class)
    public void testGetWhenNamespaceDoesNotComply()
            throws ClassNotFoundException {
        registryForGet.get(CLASS_KEY, ANOTHER_NAMESPACE);
        
        verify(getClassMapMock);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.processor.ClassMapProcessorRegistry#get(java.lang.String, java.net.URI)}
     * .
     * 
     * @throws ClassNotFoundException
     *             třída nenalezena
     */
    @Test(expected = ClassNotFoundException.class)
    public void testGetWhenKeyHasNoClassBound() throws ClassNotFoundException {
        registryForGet.get(NOT_PRESENT_KEY, NAMESPACE);
        
        verify(getClassMapMock);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.processor.ClassMapProcessorRegistry#get(java.lang.String, java.net.URI)}
     * .
     * 
     * @throws ClassNotFoundException
     *             třída nenalezena
     */
    @Test
    public void testGetWhenNamespaceNullOtherwiseValidReturnsClass()
            throws ClassNotFoundException {
        final Class<? extends Processor> result =
                registryForGet.get(CLASS_KEY, NAMESPACE);
        
        verify(getClassMapMock);
        
        assertEquals(result, ProcessorImplementation.class);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.processor.ClassMapProcessorRegistry#get(java.lang.String, java.net.URI)}
     * .
     * 
     * @throws ClassNotFoundException
     *             třída nenalezena
     */
    @Test
    public void testGetWhenArgumentsValidReturnsClass()
            throws ClassNotFoundException {
        final Class<? extends Processor> result =
                registryForGet.get(CLASS_KEY, null);
        
        verify(getClassMapMock);
        
        assertEquals(result, ProcessorImplementation.class);
    }
}
