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

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import cz.cuni.mff.ms.brodecva.botnicek.library.utils.test.UnitTest;

/**
 * Testuje neúspěšný výsledek hledání.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see SuccesfulResult
 */
@Category(UnitTest.class)
public final class SuccesfulResultTest {

    /**
     * Počet zachycených částí.
     */
    private static final int EXPECTED_PARTS_COUNT = 3;

    /**
     * Počet slov zachycené části.
     */
    private static final int EXPECTED_PART_LENGTH = 6;

    /**
     * Mock označené části.
     */
    private static PartMarker partMock = null;

    /**
     * Mock části vstupní cesty.
     */
    private static List<InputPath> pathMocks = new ArrayList<InputPath>(
            EXPECTED_PARTS_COUNT);

    /**
     * Mock šablony.
     */
    private static Template templateMock = null;

    /**
     * Očekávané řetězce zachycených části.
     */
    private static Deque<String> expectedMatchResults = new ArrayDeque<String>(
            EXPECTED_PARTS_COUNT);

    /**
     * Neúspěšný výsledek.
     */
    private SuccesfulResult result = null;

    /**
     * Vytvoří mocky.
     */
    @BeforeClass
    public static void setUpBeforeClass() {
        partMock = EasyMock.createNiceMock(PartMarker.class);
        replay(partMock);

        for (int i = 0; i < EXPECTED_PARTS_COUNT; i++) {
            final List<Word> wordMocks =
                    new ArrayList<Word>(EXPECTED_PART_LENGTH);
            final StringBuilder matchResult = new StringBuilder();
            for (int j = 0; j < EXPECTED_PART_LENGTH; j++) {
                final String wordText = "word" + i + "-" + j;

                final Word wordMock = EasyMock.createNiceMock(Word.class);
                expect(wordMock.getValue()).andReturn(wordText);
                replay(wordMock);

                wordMocks.add(wordMock);
                matchResult.append(' ' + wordText);
            }
            expectedMatchResults.push(matchResult.toString().substring(1));

            final InputPath pathMock = EasyMock.createNiceMock(InputPath.class);
            expect(pathMock.iterator()).andStubReturn(wordMocks.iterator());
            expect(pathMock.getLength()).andStubReturn(EXPECTED_PART_LENGTH);
            replay(pathMock);
            pathMocks.add(pathMock);
        }

        templateMock = EasyMock.createNiceMock(Template.class);
        replay(templateMock);
    }

    /**
     * Uklidí mocky a jiné struktury.
     */
    @AfterClass
    public static void tearDownAfterClass() {
        partMock = null;
        expectedMatchResults = null;
        pathMocks = null;
        templateMock = null;
    }

    /**
     * Vytvoří výsledek.
     */
    @Before
    public void setUp() {
        result = new SuccesfulResult(templateMock);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.SuccesfulResult#SuccesfulResult(Template)}
     * .
     */
    @Test
    public void testSuccesfulResult() {
        new SuccesfulResult(templateMock);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.SuccesfulResult#SuccesfulResult(Template)}
     * .
     */
    @Test(expected = NullPointerException.class)
    public void testSuccesfulResultWhenTemplateNull() {
        new SuccesfulResult(null);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.SuccesfulResult#isSuccesful()}
     * .
     */
    @Test
    public void testIsSuccesful() {
        assertTrue(result.isSuccesful());
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.SuccesfulResult#getTemplate()}
     * .
     */
    @Test
    public void testGetTemplate() {
        assertEquals(templateMock, result.getTemplate());
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.SuccesfulResult#addStarMatchedPart(cz.cuni.mff.ms.brodecva.botnicek.library.storage.PartMarker, cz.cuni.mff.ms.brodecva.botnicek.library.storage.InputPath)}
     * a
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.SuccesfulResult#getStarMatchedParts(cz.cuni.mff.ms.brodecva.botnicek.library.storage.PartMarker)}
     * .
     */
    @Test
    public void testAddAndGetStarMatchedParts() {
        for (final InputPath pathMock : pathMocks) {
            result.addStarMatchedPart(partMock, pathMock);
        }

        assertArrayEquals(
                expectedMatchResults.toArray(new String[EXPECTED_PARTS_COUNT]),
                result.getStarMatchedParts(partMock).toArray(
                        new String[EXPECTED_PARTS_COUNT]));
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.SuccesfulResult#addStarMatchedPart(cz.cuni.mff.ms.brodecva.botnicek.library.storage.PartMarker, cz.cuni.mff.ms.brodecva.botnicek.library.storage.InputPath)}
     * a
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.SuccesfulResult#getStarMatchedParts(cz.cuni.mff.ms.brodecva.botnicek.library.storage.PartMarker)}
     * .
     */
    @Test(expected = NullPointerException.class)
    public void testAddStarMatchedPartsWhenPartsNull() {
        result.addStarMatchedPart(partMock, null);
    }

}
