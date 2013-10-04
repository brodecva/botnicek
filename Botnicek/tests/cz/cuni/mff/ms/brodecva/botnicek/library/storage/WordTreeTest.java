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

import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.easymock.EasyMock;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.Mapper;
import cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.MapperFactory;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.test.UnitTest;

/**
 * Testuje strom prohledávací struktury.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see WordTree
 */
@Category(UnitTest.class)
public final class WordTreeTest {

    /**
     * Stub továrny na mapu.
     */
    private static MapperFactory mapperFactoryStub = null;

    /**
     * Vytvoří stuby.
     */
    @BeforeClass
    public static void setUpBeforeClass() {
        mapperFactoryStub = EasyMock.createNiceMock(MapperFactory.class);
        replay(mapperFactoryStub);
    }

    /**
     * Uklidí stuby.
     */
    @AfterClass
    public static void tearDownAfterClass() {
        mapperFactoryStub = null;
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.WordTree#WordTree(cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.MapperFactory)}
     * .
     */
    @Test(expected = NullPointerException.class)
    public void testWordTreeWhenFactoryNull() {
        new WordTree(null);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.WordTree#WordTree(cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.MapperFactory)}
     * .
     */
    @Test
    public void testWordTree() {
        new WordTree(mapperFactoryStub);
    }

    /**
     * Stub továrny.
     * 
     * @author Václav Brodec
     * @version 1.0
     */
    private static class MapperFactoryStub implements MapperFactory,
            Serializable {

        /**
         * UID serializované verze.
         */
        private static final long serialVersionUID = 840719547760414456L;

        /**
         * Náhradní pole.
         */
        private final String dummyField = "dummyfactoryField";

        /*
         * (non-Javadoc)
         * 
         * @see
         * cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.MapperFactory
         * #getMapper(int,
         * cz.cuni.mff.ms.brodecva.botnicek.library.storage.Word,
         * cz.cuni.mff.ms.brodecva.botnicek.library.storage.PartMarker)
         */
        @Override
        public Mapper<Word, WordNode> getMapper(final int depth,
                final Word parentalWord, final PartMarker part) {
            return null;
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result =
                    prime
                            * result
                            + ((dummyField == null) ? 0 : dummyField.hashCode());
            return result;
        }

        /*
         * (non-Javadoc)
         * 
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
            final MapperFactoryStub other = (MapperFactoryStub) obj;
            if (dummyField == null) {
                if (other.dummyField != null) {
                    return false;
                }
            } else if (!dummyField.equals(other.dummyField)) {
                return false;
            }
            return true;
        }
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
        final WordTree original = new WordTree(new MapperFactoryStub());

        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final ObjectOutputStream oos = new ObjectOutputStream(out);
        oos.writeObject(original);
        oos.close();

        final byte[] pickled = out.toByteArray();
        final InputStream in = new ByteArrayInputStream(pickled);
        final ObjectInputStream ois = new ObjectInputStream(in);
        final Object o = ois.readObject();
        final WordTree copy = (WordTree) o;

        assertEquals(original.getCategoryCount(), copy.getCategoryCount());
        assertEquals(original.getMapperFactory(), copy.getMapperFactory());
    }

}
