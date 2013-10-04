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

import static org.easymock.EasyMock.anyChar;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ListIterator;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.easymock.EasyMock;
import org.easymock.IAnswer;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import cz.cuni.mff.ms.brodecva.botnicek.library.preprocessor.Normalizer;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.test.UnitTest;

/**
 * Test AIML implementace vstupní cesty.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see AIMLInputPath
 */
@Category(UnitTest.class)
public final class AIMLInputPathTest {

    /**
     * Očekávaná délka.
     */
    private static final int EXPECTED_LENGTH = 6;

    /**
     * Menší index výběru.
     */
    private static final int SMALLER_RANGE_INDEX = 2;

    /**
     * Větší index výběru.
     */
    private static final int LARGER_RANGE_INDEX = 4;

    /**
     * Mírně větší délka než normální.
     */
    private static final int SLIGHTLY_LARGER_THAN_EXPECTED =
            EXPECTED_LENGTH + 1;

    /**
     * Mnohem větší délka než normální.
     */
    private static final int MUCH_LARGER_THAN_EXPECTED =
            SLIGHTLY_LARGER_THAN_EXPECTED + 2;

    /**
     * Cesta délky {@value #EXPECTED_LENGTH}.
     */
    private static AIMLInputPath normalPath = null;

    /**
     * Cesta délky {@value #MUCH_LARGER_THAN_EXPECTED}.
     */
    private static AIMLInputPath largePath = null;

    /**
     * Prázdná cesta.
     */
    private static AIMLInputPath emptyPath = null;
    
    /**
     * Stub továrny na slova produkující pouze zabalené řetězce.
     */
    private static WordFactory wordFactoryStub = null;

    /**
     * Vytvoří znovupoužitelné testované objekty.
     */
    @BeforeClass
    public static void setUpBeforeClass() {
        emptyPath = new AIMLInputPath();

        final Word[] words = new Word[MUCH_LARGER_THAN_EXPECTED];
        for (int i = 0; i < MUCH_LARGER_THAN_EXPECTED; i++) {
            words[i] = EasyMock.createNiceMock(Word.class);
            replay(words[i]);
        }

        normalPath =
                new AIMLInputPath(Arrays.asList(Arrays.copyOfRange(words, 0,
                        EXPECTED_LENGTH)));

        largePath = new AIMLInputPath(Arrays.asList(words));
        
        wordFactoryStub = EasyMock.createMock(WordFactory.class);
        expect(wordFactoryStub.create(isA(String.class))).andStubAnswer(new IAnswer<Word>() {
            
            private Normalizer normalizerStub = EasyMock.createMock(Normalizer.class);
            
            {
                expect(normalizerStub.isNormal(anyChar())).andStubReturn(true);
                replay(normalizerStub);
            }

            @Override
            public Word answer() throws Throwable {
                return new AIMLWord((String) EasyMock.getCurrentArguments()[0], normalizerStub);
            }
        });
        replay(wordFactoryStub);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.AIMLInputPath#equals(java.lang.Object)}
     * a
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.AIMLInputPath#hashCode()}
     * .
     */
    @Test
    public void testEqualsAndHashCode() {
        EqualsVerifier.forClass(AIMLInputPath.class)
                .suppress(Warning.NULL_FIELDS).usingGetClass().verify();
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.AIMLInputPath#AIMLInputPath(java.util.List)}
     * .
     */
    @Test(expected = NullPointerException.class)
    public void testAIMLInputPathListOfWordWhenNull() {
        new AIMLInputPath(null);
    }
    
    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.AIMLInputPath#AIMLInputPath(java.util.List)}
     * .
     */
    @Test(expected = NullPointerException.class)
    public void testAIMLInputPathListOfWordWhenWordNull() {
        new AIMLInputPath(Arrays.asList(new Word[] { null }));
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.AIMLInputPath#AIMLInputPath(java.util.List)}
     * .
     */
    @Test
    public void testAIMLInputPathListOfWordWhenEmpty() {
        new AIMLInputPath(new ArrayList<Word>(0));
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.AIMLInputPath#AIMLInputPath(java.util.List)}
     * .
     */
    @Test
    public void testAIMLInputPathListOfWordWhenNotEmpty() {
        final int expectedLength = EXPECTED_LENGTH;

        final Word[] words = new Word[expectedLength];
        for (int i = 0; i < expectedLength; i++) {
            words[i] = EasyMock.createNiceMock(Word.class);
            replay(words[i]);
        }

        new AIMLInputPath(Arrays.asList(words));
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.AIMLInputPath#AIMLInputPath(java.lang.String, java.lang.String, java.lang.String, cz.cuni.mff.ms.brodecva.botnicek.library.storage.WordFactory)}
     * .
     */
    @Test
    public void testAIMLInputPathStringStringStringWhenPartsNullAccepts() {
        new AIMLInputPath(null, null, null, wordFactoryStub);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.AIMLInputPath#AIMLInputPath(java.lang.String, java.lang.String, java.lang.String, cz.cuni.mff.ms.brodecva.botnicek.library.storage.WordFactory)}
     * .
     */
    @Test
    public
            void
            testAIMLInputPathStringStringStringWhenPartsNullReturnsUniversalPath() {
        final InputPath path = new AIMLInputPath(null, null, null, wordFactoryStub);

        assertEquals("* <that> * <topic> *", path.toString());
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.AIMLInputPath#AIMLInputPath(java.lang.String, java.lang.String, java.lang.String, cz.cuni.mff.ms.brodecva.botnicek.library.storage.WordFactory)}
     * .
     */
    @Test
    public void testAIMLInputPathStringStringStringReturnsCorrectToString() {
        final InputPath path =
                new AIMLInputPath("dummyPattern", "dummyThat", "dummyTopic", wordFactoryStub);

        assertEquals("dummyPattern <that> dummyThat <topic> dummyTopic",
                path.toString());
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.AIMLInputPath#AIMLInputPath(cz.cuni.mff.ms.brodecva.botnicek.library.storage.AIMLInputPath, int, int)}
     * .
     */
    @Test(expected = NullPointerException.class)
    public void testAIMLInputPathAIMLInputPathIntIntWhenOriginalNull() {
        new AIMLInputPath(null, SMALLER_RANGE_INDEX, LARGER_RANGE_INDEX);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.AIMLInputPath#AIMLInputPath(cz.cuni.mff.ms.brodecva.botnicek.library.storage.AIMLInputPath, int, int)}
     * .
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testAIMLInputPathAIMLInputPathIntIntWhenFromLessThanLegal() {
        new AIMLInputPath(normalPath, -1, EXPECTED_LENGTH);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.AIMLInputPath#AIMLInputPath(cz.cuni.mff.ms.brodecva.botnicek.library.storage.AIMLInputPath, int, int)}
     * .
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testAIMLInputPathAIMLInputPathIntIntWhenToMoreThanLegal() {
        new AIMLInputPath(normalPath, 0, EXPECTED_LENGTH + 1);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.AIMLInputPath#AIMLInputPath(cz.cuni.mff.ms.brodecva.botnicek.library.storage.AIMLInputPath, int, int)}
     * .
     */
    @Test
    public void testAIMLInputPathAIMLInputPathIntIntWholeRange() {
        new AIMLInputPath(normalPath, 0, EXPECTED_LENGTH);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.AIMLInputPath#AIMLInputPath(cz.cuni.mff.ms.brodecva.botnicek.library.storage.AIMLInputPath, int, int)}
     * .
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAIMLInputPathAIMLInputPathIntIntFromLargerThanTo() {
        new AIMLInputPath(normalPath, LARGER_RANGE_INDEX, SMALLER_RANGE_INDEX);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.AIMLInputPath#AIMLInputPath()}
     * .
     */
    @Test
    public void testAIMLInputPath() {
        new AIMLInputPath();
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.AIMLInputPath#getSuffix(java.util.ListIterator)}
     * .
     */
    @Test(expected = NullPointerException.class)
    public void testGetSuffixWhenIteratorNull() {
        normalPath.getSuffix(null);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.AIMLInputPath#getSuffix(java.util.ListIterator)}
     * .
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetSuffixWhenIteratorSetBeyondThePath() {
        final AIMLInputPath iteratorPath = largePath;
        final AIMLInputPath suffixedPath = normalPath;

        final int iteratorIndex = SLIGHTLY_LARGER_THAN_EXPECTED;

        final ListIterator<Word> longIterator = iteratorPath.listIterator();
        while (longIterator.nextIndex() < iteratorIndex) {
            longIterator.next();
        }

        suffixedPath.getSuffix(longIterator);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.AIMLInputPath#getSuffix(java.util.ListIterator)}
     * .
     */
    @Test
    public void testGetSuffixWhenEmptyPathReturnsEmptyPath() {
        final AIMLInputPath suffixedPath = emptyPath;

        assertEquals(emptyPath,
                suffixedPath.getSuffix(suffixedPath.listIterator()));
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.AIMLInputPath#getSuffix(java.util.ListIterator)}
     * .
     */
    @Test
    public void testGetSuffixWhenIteratorSetToEndOfNonemptyReturnsEmptyPath() {
        final AIMLInputPath suffixedPath = normalPath;

        final ListIterator<Word> iterator = suffixedPath.listIterator();
        while (iterator.hasNext()) {
            iterator.next();
        }

        assertEquals(emptyPath, suffixedPath.getSuffix(iterator));
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.AIMLInputPath#getSuffix(java.util.ListIterator)}
     * .
     */
    @Test
    public void testGetSuffixWhenNonemptyPathReturnsSuffix() {
        final Word[] words = new Word[EXPECTED_LENGTH];
        for (int i = 0; i < EXPECTED_LENGTH; i++) {
            words[i] = EasyMock.createMock(Word.class);
            replay(words[i]);
        }

        final AIMLInputPath suffixedPath =
                new AIMLInputPath(Arrays.asList(words));

        final AIMLInputPath expectedSuffixPath =
                new AIMLInputPath(Arrays.asList(Arrays.copyOfRange(words,
                        SMALLER_RANGE_INDEX, EXPECTED_LENGTH)));

        final ListIterator<Word> iterator = suffixedPath.listIterator();
        while (iterator.nextIndex() < SMALLER_RANGE_INDEX) {
            iterator.next();
        }

        assertEquals(expectedSuffixPath, suffixedPath.getSuffix(iterator));
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.AIMLInputPath#getRemainingLength(java.util.ListIterator)}
     * .
     */
    @Test(expected = NullPointerException.class)
    public void testGetRemainingLengthWhenIteratorNull() {
        normalPath.getRemainingLength(null);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.AIMLInputPath#getRemainingLength(java.util.ListIterator)}
     * .
     */
    @Test
    public void testGetRemainingLengthWhenIteratorAtBeginning() {
        assertEquals(EXPECTED_LENGTH,
                normalPath.getRemainingLength(normalPath.listIterator()));
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.AIMLInputPath#getRemainingLength(java.util.ListIterator)}
     * .
     */
    @Test
    public void testGetRemainingLengthWhenIteratorInside() {
        final int expectedLength = EXPECTED_LENGTH - SMALLER_RANGE_INDEX;

        final ListIterator<Word> iterator = normalPath.listIterator();
        while (iterator.nextIndex() < SMALLER_RANGE_INDEX) {
            iterator.next();
        }

        assertEquals(expectedLength, normalPath.getRemainingLength(iterator));
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.AIMLInputPath#getRemainingLength(java.util.ListIterator)}
     * .
     */
    @Test
    public void testGetRemainingLengthWhenIteratorBeyond() {
        final int expectedLength =
                EXPECTED_LENGTH - SLIGHTLY_LARGER_THAN_EXPECTED;

        final AIMLInputPath iteratorPath = largePath;

        final ListIterator<Word> longIterator = iteratorPath.listIterator();
        while (longIterator.nextIndex() < SLIGHTLY_LARGER_THAN_EXPECTED) {
            longIterator.next();
        }

        assertEquals(expectedLength,
                normalPath.getRemainingLength(longIterator));
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.AIMLInputPath#getLength()}
     * .
     */
    @Test
    public void testGetLengthWhenEmpty() {
        assertEquals(0, emptyPath.getLength());
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.AIMLInputPath#getLength()}
     * .
     */
    @Test
    public void testGetLengthWhenNotEmpty() {
        assertEquals(EXPECTED_LENGTH, normalPath.getLength());
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.AIMLInputPath#subPath(int, int)}
     * .
     */
    @Test
    public void testSubPath() {
        final Word[] words = new Word[EXPECTED_LENGTH];
        for (int i = 0; i < EXPECTED_LENGTH; i++) {
            words[i] = EasyMock.createMock(Word.class);
            replay(words[i]);
        }

        final InputPath original = new AIMLInputPath(Arrays.asList(words));
        final InputPath subPath =
                original.subPath(SMALLER_RANGE_INDEX, LARGER_RANGE_INDEX);

        final Word[] result =
                new Word[LARGER_RANGE_INDEX - SMALLER_RANGE_INDEX];
        int i = 0;
        for (final Word resultWord : subPath) {
            result[i++] = resultWord;
        }

        assertArrayEquals(Arrays.copyOfRange(words, SMALLER_RANGE_INDEX,
                LARGER_RANGE_INDEX), result);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.AIMLInputPath#toString()}
     * .
     */
    @Test
    public void testToStringWhenLengthMoreThanOne() {
        final Word[] words = new Word[EXPECTED_LENGTH];
        final StringBuilder expected = new StringBuilder();
        for (int i = 0; i < EXPECTED_LENGTH; i++) {
            final String wordText = "word" + i;

            words[i] = EasyMock.createMock(Word.class);
            expect(words[i].getValue()).andStubReturn(wordText);
            replay(words[i]);

            expected.append(" " + wordText);
        }

        final InputPath path = new AIMLInputPath(Arrays.asList(words));

        assertEquals(expected.toString().substring(1), path.toString());
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
        final AIMLInputPath original =
                new AIMLInputPath("DUMMY", "DUMMY", "DUMMY");

        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final ObjectOutputStream oos = new ObjectOutputStream(out);
        oos.writeObject(original);
        oos.close();

        final byte[] pickled = out.toByteArray();
        final InputStream in = new ByteArrayInputStream(pickled);
        final ObjectInputStream ois = new ObjectInputStream(in);
        final Object o = ois.readObject();
        final AIMLInputPath copy = (AIMLInputPath) o;

        assertEquals(original, copy);
    }
}
