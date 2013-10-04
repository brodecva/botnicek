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

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.easymock.EasyMock;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import cz.cuni.mff.ms.brodecva.botnicek.library.storage.PartMarker;
import cz.cuni.mff.ms.brodecva.botnicek.library.storage.Word;
import cz.cuni.mff.ms.brodecva.botnicek.library.storage.WordNode;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.test.UnitTest;

/**
 * Testuje tovární třídu na úsporné mapy.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see FrugalMapperFactory
 */
@Category(UnitTest.class)
public final class FrugalMapperFactoryTest {

    /**
     * Hloubka listu.
     */
    private static final int LEAF_DEPTH = 15;

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.FrugalMapperFactory#getMapper(int, cz.cuni.mff.ms.brodecva.botnicek.library.storage.Word, cz.cuni.mff.ms.brodecva.botnicek.library.storage.PartMarker)}
     * .
     */
    @Test
    public void testGetMapperWhenParentalWordNullReturnsMapper() {
        final PartMarker marker = EasyMock.createNiceMock(PartMarker.class);
        replay(marker);

        assertNotNull(new FrugalMapperFactory().getMapper(0, null, marker));
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.FrugalMapperFactory#getMapper(int, cz.cuni.mff.ms.brodecva.botnicek.library.storage.Word, cz.cuni.mff.ms.brodecva.botnicek.library.storage.PartMarker)}
     * .
     */
    @Test(expected = NullPointerException.class)
    public void testGetMapperWhenPartNull() {
        final Word parentalWord = EasyMock.createNiceMock(Word.class);
        replay(parentalWord);

        new FrugalMapperFactory().getMapper(0, parentalWord, null);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.FrugalMapperFactory#getMapper(int, cz.cuni.mff.ms.brodecva.botnicek.library.storage.Word, cz.cuni.mff.ms.brodecva.botnicek.library.storage.PartMarker)}
     * .
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetMapperWhenDepthNegative() {
        final Word parentalWord = EasyMock.createNiceMock(Word.class);
        replay(parentalWord);
        final PartMarker marker = EasyMock.createNiceMock(PartMarker.class);
        replay(marker);

        new FrugalMapperFactory().getMapper(-1, parentalWord, marker);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.FrugalMapperFactory#getMapper(int, cz.cuni.mff.ms.brodecva.botnicek.library.storage.Word, cz.cuni.mff.ms.brodecva.botnicek.library.storage.PartMarker)}
     * .
     */
    @Test
    public void testGetMapperWhenArgumentsLegalReturnsMapper() {
        final Word parentalWord = EasyMock.createNiceMock(Word.class);
        expect(parentalWord.getValue()).andStubReturn("dummy");
        replay(parentalWord);
        final PartMarker marker = EasyMock.createNiceMock(PartMarker.class);
        replay(marker);

        assertNotNull(new FrugalMapperFactory().getMapper(2, parentalWord,
                marker));
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.FrugalMapperFactory#getMapper(int, cz.cuni.mff.ms.brodecva.botnicek.library.storage.Word, cz.cuni.mff.ms.brodecva.botnicek.library.storage.PartMarker)}
     * .
     */
    @Test
    public void testGetMapperWhenRootReturnsLargeMapper() {
        final Word parentalWord = EasyMock.createNiceMock(Word.class);
        expect(parentalWord.getValue()).andStubReturn("mediumWord");
        replay(parentalWord);
        final PartMarker marker = EasyMock.createNiceMock(PartMarker.class);
        replay(marker);

        final Mapper<Word, WordNode> mapper =
                new FrugalMapperFactory().getMapper(0, parentalWord, marker);

        assertThat(mapper.getCapacity(),
                greaterThan(ArrayCore.MAXIMUM_CAPACITY));
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.FrugalMapperFactory#getMapper(int, cz.cuni.mff.ms.brodecva.botnicek.library.storage.Word, cz.cuni.mff.ms.brodecva.botnicek.library.storage.PartMarker)}
     * .
     */
    @Test
    public void testGetMapperWhenLeafReturnsTinyMapper() {
        final Word parentalWord = EasyMock.createNiceMock(Word.class);
        expect(parentalWord.getValue()).andStubReturn("mediumWord");
        replay(parentalWord);
        final PartMarker marker = EasyMock.createNiceMock(PartMarker.class);
        replay(marker);

        final Mapper<Word, WordNode> mapper =
                new FrugalMapperFactory().getMapper(LEAF_DEPTH, parentalWord,
                        marker);

        assertEquals(SingleEntryCore.CAPACITY, mapper.getCapacity());
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.FrugalMapperFactory#getMapper(int, cz.cuni.mff.ms.brodecva.botnicek.library.storage.Word, cz.cuni.mff.ms.brodecva.botnicek.library.storage.PartMarker)}
     * .
     */
    @Test
    public void testGetMapperWhenReallyLongWordReturnsTinyMapper() {
        final Word longParentalWord = EasyMock.createNiceMock(Word.class);
        expect(longParentalWord.getValue()).andStubReturn(
                "aaaaaaaaaaaaaaaaaaaa");
        replay(longParentalWord);
        final PartMarker marker = EasyMock.createNiceMock(PartMarker.class);
        replay(marker);

        final Mapper<Word, WordNode> mapper =
                new FrugalMapperFactory()
                        .getMapper(3, longParentalWord, marker);

        assertEquals(SingleEntryCore.CAPACITY, mapper.getCapacity());
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.FrugalMapperFactory#getMapper(int, cz.cuni.mff.ms.brodecva.botnicek.library.storage.Word, cz.cuni.mff.ms.brodecva.botnicek.library.storage.PartMarker)}
     * .
     */
    @Test
    public void testGetMapperWhenMediumWordAtSubrootLevelReturnsMediumMapper() {
        final Word mediumParentalWord = EasyMock.createNiceMock(Word.class);
        expect(mediumParentalWord.getValue()).andStubReturn("notLong");
        replay(mediumParentalWord);
        final PartMarker marker = EasyMock.createNiceMock(PartMarker.class);
        replay(marker);

        final Mapper<Word, WordNode> mapper =
                new FrugalMapperFactory().getMapper(1, mediumParentalWord,
                        marker);

        assertThat(mapper.getCapacity(),
                greaterThanOrEqualTo(ArrayCore.MINIMUM_CAPACITY));
        assertThat(mapper.getCapacity(),
                lessThanOrEqualTo(ArrayCore.MAXIMUM_CAPACITY));
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.FrugalMapperFactory#FrugalMapperFactory()}
     * .
     */
    @Test
    public void testFrugalMapperFactory() {
        new FrugalMapperFactory();
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
        final FrugalMapperFactory original = new FrugalMapperFactory();

        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final ObjectOutputStream oos = new ObjectOutputStream(out);
        oos.writeObject(original);
        oos.close();

        final byte[] pickled = out.toByteArray();
        final InputStream in = new ByteArrayInputStream(pickled);
        final ObjectInputStream ois = new ObjectInputStream(in);
        final Object o = ois.readObject();
        final FrugalMapperFactory copy = (FrugalMapperFactory) o;

        assertNotNull(copy);
    }
}
