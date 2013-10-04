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
package cz.cuni.mff.ms.brodecva.botnicek.library.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.eq;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Set;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.Mapper;
import cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.MapperFactory;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.test.UnitTest;

/**
 * Testuje uzel pro {@link WordTree}.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see WordNode
 */
/**
 * @author Václav Brodec
 * @version 1.0
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ WordNode.class })
@Category(UnitTest.class)
public final class WordNodeTest {

    /**
     * Hloubka uzlu při testu.
     */
    private static final int TESTING_DEPTH = 5;
    
    /**
     * Očekávaná délka.
     */
    private static final int EXPECTED_LENGTH = 6;
    
    /**
     * Stub rodičovského slova.
     */
    private Word parentalWordStub = null;
    
    /**
     * Stub slova na začátku cesty.
     */
    private Word headWordStub = null;

    /**
     * Stub části, ve které se nacházíme při zpracování cesty.
     */
    private PartMarker partStub = null;
    
    /**
     * Stub mapy při inicializaci.
     */
    private Mapper<Word, WordNode> initMapperStub = null;
    
    /**
     * Stub jednoduché mapy, která vrací pozitivní výsledek na dotaz s klíčem.
     */
    private Mapper<Word, WordNode> positiveMapperStub = null;

    /**
     * Stub továrny pro test inicializace.
     */
    private MapperFactory initFactoryStub = null;
    
    /**
     * Stub továrny vracející {@link #positiveMapperStub}.
     */
    private MapperFactory positiveMapperFactoryStub = null;
    
    /**
     * Stub šablony.
     */
    private Template templateStub = null;
    
    /**
     * Stub úspěšného výsledku.
     */
    private MatchResult successStub = null;
    
    /**
     * Stub ocasu cesty.
     */
    private InputPath tailStub = null;
    
    /**
     * Stub neprázdné cesty.
     */
    private InputPath nonemptyPathMock = null;

    /**
     * Očekávaný uzel, korektně provádí operace hledání a přidávání.
     */
    private WordNode expectedNode = null;
    
    /**
     * Testovaný uzel.
     */
    private WordNode testedNode;

    /**
     * Nastaví testované objekty.
     */
    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        parentalWordStub = EasyMock.createMock(Word.class);
        replay(parentalWordStub);
        
        partStub = EasyMock.createMock(PartMarker.class);
        expect(partStub.allValues()).andReturn(new HashSet<PartMarker>()).anyTimes();
        replay(partStub);
        
        initMapperStub = EasyMock.createMock(Mapper.class);
        replay(initMapperStub);
        
        initFactoryStub = EasyMock.createMock(MapperFactory.class);
        expect(initFactoryStub.getMapper(TESTING_DEPTH, parentalWordStub, partStub)).andReturn(initMapperStub);
        replay(initFactoryStub);
        
        successStub = EasyMock.createMock(MatchResult.class);
        expect(successStub.isSuccesful()).andReturn(true).anyTimes();
        replay(successStub);
        
        expectedNode = PowerMock.createNiceMock(WordNode.class);
        expect(expectedNode.find(isA(InputPath.class), isA(PartMarker.class))).andReturn(successStub).anyTimes();
        expectedNode.add(isA(InputPath.class), anyObject(Template.class), isA(PartMarker.class), eq(positiveMapperFactoryStub));
        PowerMock.expectLastCall().anyTimes();
        PowerMock.replay(expectedNode);
        
        positiveMapperStub = EasyMock.createMock(Mapper.class);
        expect(positiveMapperStub.get(isA(Word.class))).andReturn(expectedNode);
        expect(positiveMapperStub.getEntries()).andStubReturn(new HashSet<Entry<Word, WordNode>>());
        replay(positiveMapperStub);
        
        positiveMapperFactoryStub = EasyMock.createMock(MapperFactory.class);
        expect(positiveMapperFactoryStub.getMapper(TESTING_DEPTH, parentalWordStub, partStub)).andReturn(positiveMapperStub);
        replay(positiveMapperFactoryStub);
        
        initFactoryStub = EasyMock.createMock(MapperFactory.class);
        expect(initFactoryStub.getMapper(eq(TESTING_DEPTH), anyObject(PartMarker.class), eq(partStub))).andReturn(initMapperStub);
        replay(initFactoryStub);
        
        templateStub = EasyMock.createMock(Template.class);
        replay(templateStub);
        
        tailStub = EasyMock.createMock(InputPath.class);
        expect(tailStub.getLength()).andReturn(EXPECTED_LENGTH);
        replay(tailStub);
        
        headWordStub = EasyMock.createMock(Word.class);
        replay(headWordStub);
        
        nonemptyPathMock = EasyMock.createMock(InputPath.class);
        expect(nonemptyPathMock.isEmpty()).andReturn(false);
        expect(nonemptyPathMock.tail()).andReturn(tailStub);
        expect(nonemptyPathMock.head()).andReturn(headWordStub);
        replay(nonemptyPathMock);
        
        testedNode = new WordNode(TESTING_DEPTH, parentalWordStub, partStub, positiveMapperFactoryStub);
    }

    /**
     * Uklidí testované objekty.
     */
    @After
    public void tearDown() {
        expectedNode = null;
        
        headWordStub = null;
        
        initFactoryStub = null;
        
        initMapperStub = null;
        
        nonemptyPathMock = null;
        
        parentalWordStub = null;
        
        partStub = null;
        
        positiveMapperFactoryStub = null;
        
        positiveMapperStub = null;
        
        successStub = null;
        
        tailStub = null;
        
        templateStub = null;
        
        testedNode = null;
    }
    
    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.WordNode#WordNode(int, cz.cuni.mff.ms.brodecva.botnicek.library.storage.Word, cz.cuni.mff.ms.brodecva.botnicek.library.storage.PartMarker, cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.MapperFactory)}
     * .
     */
    @Test
    public void testWordNodeIntWordPartMarkerMapperFactory() {
        new WordNode(TESTING_DEPTH, parentalWordStub, partStub,
                initFactoryStub);
        
        verify(parentalWordStub, partStub, initFactoryStub);
    }
    
    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.WordNode#WordNode(int, cz.cuni.mff.ms.brodecva.botnicek.library.storage.Word, cz.cuni.mff.ms.brodecva.botnicek.library.storage.PartMarker, cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.MapperFactory)}
     * .
     */
    @Test
    public void testWordNodeIntWordPartMarkerMapperFactoryWhenParentalWordNullSucceeds() {
        new WordNode(TESTING_DEPTH, null, partStub,
                initFactoryStub);
        
        verify(partStub, initFactoryStub);
    }
    
    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.WordNode#WordNode(int, cz.cuni.mff.ms.brodecva.botnicek.library.storage.Word, cz.cuni.mff.ms.brodecva.botnicek.library.storage.PartMarker, cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.MapperFactory)}
     * .
     */
    @Test(expected = NullPointerException.class)
    public void testWordNodeIntWordPartMarkerMapperFactoryWhenPartNull() {
        new WordNode(TESTING_DEPTH, parentalWordStub, null,
                initFactoryStub);
        
        verify(parentalWordStub, initFactoryStub);
    }
    
    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.WordNode#WordNode(int, cz.cuni.mff.ms.brodecva.botnicek.library.storage.Word, cz.cuni.mff.ms.brodecva.botnicek.library.storage.PartMarker, cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.MapperFactory)}
     * .
     */
    @Test(expected = NullPointerException.class)
    public void testWordNodeIntWordPartMarkerMapperFactoryWhenFactoryNull() {
        new WordNode(TESTING_DEPTH, parentalWordStub, partStub, null);
        
        verify(parentalWordStub, partStub);
    }
    
    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.WordNode#add(InputPath, Template, PartMarker, MapperFactory)}.
     * .
     */
    @Test
    public void testAddChangesMaxHeight() {
        testedNode.add(nonemptyPathMock , templateStub, partStub,
                positiveMapperFactoryStub);
        
        PowerMock.verify(tailStub, nonemptyPathMock, templateStub, partStub, positiveMapperFactoryStub, positiveMapperStub);
        
        assertEquals(EXPECTED_LENGTH, testedNode.getMaxHeight());
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.WordNode#add(InputPath, Template, PartMarker, MapperFactory)}
     * .
     */
    @Test(expected = NullPointerException.class)
    public void testAddWhenPathNull() {
        testedNode.add(null, templateStub, partStub,
                positiveMapperFactoryStub);
        
        verify(templateStub, partStub);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.WordNode#add(InputPath, Template, PartMarker, MapperFactory)}
     * .
     */
    @Test
    public void testAddWhenTemplateNullSucceeds() {
        testedNode.add(nonemptyPathMock, null, partStub,
                positiveMapperFactoryStub);
        
        verify(nonemptyPathMock, partStub, positiveMapperFactoryStub);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.WordNode#add(InputPath, Template, PartMarker, MapperFactory)}
     * .
     */
    @Test(expected = NullPointerException.class)
    public void testAddWhenPartNull() {
        testedNode.add(nonemptyPathMock, templateStub, null,
                positiveMapperFactoryStub);
        
        verify(templateStub);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.WordNode#add(InputPath, Template, PartMarker, MapperFactory)}
     * .
     */
    @Test(expected = NullPointerException.class)
    public void testAddWhenFactoryNull() {
        testedNode.add(nonemptyPathMock, templateStub, partStub,
                null);
        
        verify(templateStub);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.WordNode#add(cz.cuni.mff.ms.brodecva.botnicek.library.storage.InputPath, cz.cuni.mff.ms.brodecva.botnicek.library.storage.Template, cz.cuni.mff.ms.brodecva.botnicek.library.storage.PartMarker, cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.MapperFactory)}
     * a
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.WordNode#find(cz.cuni.mff.ms.brodecva.botnicek.library.storage.InputPath, cz.cuni.mff.ms.brodecva.botnicek.library.storage.PartMarker)}
     * .
     */
    @Test
    public void testAddAndFindWhenPathEmptyNotSuccesful() {
        final InputPath emptyPathMock = EasyMock.createMock(InputPath.class);
        expect(emptyPathMock.isEmpty()).andReturn(true);
        replay(emptyPathMock);
        
        assertFalse(testedNode.find(emptyPathMock, partStub)
                .isSuccesful());
        
        verify(emptyPathMock, partStub);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.WordNode#find(cz.cuni.mff.ms.brodecva.botnicek.library.storage.InputPath, cz.cuni.mff.ms.brodecva.botnicek.library.storage.PartMarker)}
     * .
     */
    @Test(expected = NullPointerException.class)
    public void testFindWhenPathNull() {
        testedNode.find(null, partStub);
        
        verify(partStub);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.WordNode#find(InputPath, PartMarker)}
     * .
     */
    @Test(expected = NullPointerException.class)
    public void testFindWhenPartNull() {
        testedNode.find(nonemptyPathMock, null);
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
    public void testRoundTripSerialization() throws IOException,
            ClassNotFoundException {
        final Set<Entry<Word, WordNode>> entries = new HashSet<Entry<Word, WordNode>>();
        entries.add(new SimpleImmutableEntry<Word, WordNode>(new SerializableWord(), expectedNode));
        
        @SuppressWarnings("unchecked")
        final Mapper<Word, WordNode> inspectableMapperStub = EasyMock.createMock(Mapper.class);
        expect(inspectableMapperStub.get(isA(Word.class))).andReturn(expectedNode);
        expect(inspectableMapperStub.getCapacity()).andReturn(1);
        expect(inspectableMapperStub.getSize()).andReturn(1);
        expect(inspectableMapperStub.getEntries()).andReturn(entries);
        replay(inspectableMapperStub);
        
        final MapperFactory inspectableMapperFactoryStub = EasyMock.createMock(MapperFactory.class);
        expect(inspectableMapperFactoryStub.getMapper(TESTING_DEPTH, parentalWordStub, partStub)).andReturn(inspectableMapperStub);
        replay(inspectableMapperFactoryStub);
        
        final WordNode original = new WordNode(TESTING_DEPTH, parentalWordStub, partStub, inspectableMapperFactoryStub);

        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final ObjectOutputStream oos = new ObjectOutputStream(out);
        oos.writeObject(original);
        oos.close();

        final byte[] pickled = out.toByteArray();
        final InputStream in = new ByteArrayInputStream(pickled);
        final ObjectInputStream ois = new ObjectInputStream(in);
        final Object o = ois.readObject();
        final WordNode copy = (WordNode) o;

        assertEquals(original.getDepth(), copy.getDepth());
        assertEquals(original.getMaxHeight(), copy.getMaxHeight());
        assertEquals(original.getTemplate(), copy.getTemplate());
        assertEquals(original.getBranchWords(), copy.getBranchWords());
    }
    
    /**
     * Testovací implementace serializovatelného slova.
     * 
     * @author Václav Brodec
     * @version 1.0
     */
    public static final class SerializableWord implements Word, Serializable {
        
        /**
         * Hodnota.
         */
        private String value = "dummy";
        
        /**
         * UID serializované verze.
         */
        private static final long serialVersionUID = -5200229130082694733L;

        /* (non-Javadoc)
         * @see cz.cuni.mff.ms.brodecva.botnicek.library.storage.Word#getValue()
         */
        @Override
        public String getValue() {
            return value;
        }

        /* (non-Javadoc)
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + value.hashCode();
            return result;
        }

        /* (non-Javadoc)
         * @see java.lang.Object#equals(java.lang.Object)
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
            final SerializableWord other = (SerializableWord) obj;
            if (!value.equals(other.value)) {
                return false;
            }
            return true;
        }
    } 
}
