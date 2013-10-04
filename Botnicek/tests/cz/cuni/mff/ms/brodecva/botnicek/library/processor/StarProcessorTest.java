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

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.easymock.EasyMock;
import org.easymock.IAnswer;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.w3c.dom.Element;

import cz.cuni.mff.ms.brodecva.botnicek.library.parser.IndexFactory;
import cz.cuni.mff.ms.brodecva.botnicek.library.parser.TemplateParser;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.Index;
import cz.cuni.mff.ms.brodecva.botnicek.library.preprocessor.Normalizer;
import cz.cuni.mff.ms.brodecva.botnicek.library.storage.AIMLPartMarker;
import cz.cuni.mff.ms.brodecva.botnicek.library.storage.MatchResult;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.test.UnitTest;

/**
 * Testuje procesor vracející část vstupní cesty zachycené hvězdičkou.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see StarProcessor
 */
@Category(UnitTest.class)
public final class StarProcessorTest {

    /**
     * Zástupný řetězec.
     */
    private static final String DUMMY = "dummy";

    /**
     * Očekávaná hodnota.
     */
    private static final String EXPECTED_VALUE = "expectedValue";

    /**
     * Stub zachycené části.
     */
    private static final List<String> MATCHED_PARTS =
            Arrays.asList(new String[] { DUMMY, DUMMY, DUMMY, EXPECTED_VALUE,
                    DUMMY });

    /**
     * Hodnota indexu větší než počet referencí..
     */
    private static final Integer LARGE_INDEX_VALUE = 6;

    /**
     * Platná hodnota indexu pro {@link #MATCHED_PARTS}.
     */
    private static final Integer VALID_INDEX_VALUE = 4;

    /**
     * Příliš velký index.
     */
    private static Index largeIndex = null;

    /**
     * Platný index.
     */
    private static Index validIndex = null;

    /**
     * Odpověď vracející vstupní řetězec pro funkce jednoho argumentu.
     */
    private static IAnswer<String> sameString;

    /**
     * Mock normalizéru.
     */
    private Normalizer normalizerMock = null;

    /**
     * Továrna na příliš velké indexy.
     */
    private IndexFactory largeIndexFactoryMock = null;

    /**
     * Továrna na platné indexy.
     */
    private IndexFactory validIndexFactoryMock = null;

    /**
     * Stub prvku.
     */
    private Element elementStub = null;

    /**
     * Mock výsledku.
     */
    private MatchResult resultMock = null;

    /**
     * Mock parseru šablony.
     */
    private TemplateParser parserMock = null;

    /**
     * Nastaví statické pomocné objekty.
     */
    @BeforeClass
    public static void setUpBeforeClass() {
        sameString = new IAnswer<String>() {

            @Override
            public String answer() throws Throwable {
                return (String) EasyMock.getCurrentArguments()[0];
            }

        };

        largeIndex = EasyMock.createMock(Index.class);
        expect(largeIndex.getValue()).andStubReturn(LARGE_INDEX_VALUE);
        replay(largeIndex);

        validIndex = EasyMock.createMock(Index.class);
        expect(validIndex.getValue()).andStubReturn(VALID_INDEX_VALUE);
        replay(validIndex);
    }

    /**
     * Uklidí statické pomocné objekty či ověří jejich stav.
     */
    @AfterClass
    public static void tearDownAfterClass() {
        sameString = null;

        largeIndex = null;

        validIndex = null;
    }

    /**
     * Nastaví mocky a stuby.
     */
    @Before
    public void setUp() {
        elementStub = EasyMock.createNiceMock(Element.class);

        largeIndexFactoryMock = EasyMock.createMock(IndexFactory.class);
        expect(largeIndexFactoryMock.createIndex(isA(Element.class)))
                .andReturn(largeIndex);
        replay(largeIndexFactoryMock);

        validIndexFactoryMock = EasyMock.createMock(IndexFactory.class);
        expect(validIndexFactoryMock.createIndex(isA(Element.class)))
                .andReturn(validIndex);
        replay(validIndexFactoryMock);

        resultMock = EasyMock.createMock(MatchResult.class);
        expect(resultMock.getStarMatchedParts(AIMLPartMarker.PATTERN))
                .andReturn(MATCHED_PARTS);
        replay(resultMock);

        parserMock = EasyMock.createMock(TemplateParser.class);
        expect(parserMock.getMatchResult()).andReturn(resultMock);
        replay(parserMock);

        normalizerMock = EasyMock.createMock(Normalizer.class);
        expect(normalizerMock.deconvertFromNormalChars(isA(String.class)))
                .andAnswer(sameString);
        replay(normalizerMock);
    }

    /**
     * Uklidí mocky a stuby.
     */
    @After
    public void tearDown() {
        elementStub = null;

        largeIndexFactoryMock = null;

        validIndexFactoryMock = null;

        normalizerMock = null;

        parserMock = null;

        resultMock = null;
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.processor.StarProcessor#process(org.w3c.dom.Element, cz.cuni.mff.ms.brodecva.botnicek.library.parser.TemplateParser)}
     * .
     * 
     * @throws ProcessorException
     *             chyba při zpracování
     */
    @Test(expected = ProcessorException.class)
    public void testProcessWhenReferenceIndexTooLarge()
            throws ProcessorException {
        new StarProcessor(largeIndexFactoryMock, normalizerMock).process(
                elementStub, parserMock);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.processor.StarProcessor#process(org.w3c.dom.Element, cz.cuni.mff.ms.brodecva.botnicek.library.parser.TemplateParser)}
     * .
     * 
     * @throws ProcessorException
     *             chyba při zpracování
     */
    @Test
    public void testProcessWhenIndexCorrectReturnsReference()
            throws ProcessorException {
        final Processor processor =
                new StarProcessor(validIndexFactoryMock, normalizerMock);

        final String result = processor.process(elementStub, parserMock);

        assertEquals(EXPECTED_VALUE, result);
    }
}
