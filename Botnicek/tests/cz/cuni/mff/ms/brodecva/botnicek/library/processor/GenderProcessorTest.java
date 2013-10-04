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
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import cz.cuni.mff.ms.brodecva.botnicek.library.language.Language;
import cz.cuni.mff.ms.brodecva.botnicek.library.parser.TemplateParser;
import cz.cuni.mff.ms.brodecva.botnicek.library.responder.Bot;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.test.UnitTest;

/**
 * Testuje procesor pro záměnu pohlaví.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see GenderProcessor
 */
@Category(UnitTest.class)
public final class GenderProcessorTest {

    /**
     * Prázdný řetězec.
     */
    private static final String DUMMY_STRING = "dummy";

    /**
     * Hodnota větší než 0.
     */
    private static final int MORE_THAN_ZERO = 1;

    /**
     * Očekávaný výsledek zpracování zkráceného tagu.
     */
    private static final String EXPECTED_PROCESSING_OF_SHORTENED =
            "expected of shortened";

    /**
     * Očekávaný výsledek zpracování plného tagu.
     */
    private static final String EXPECTED_PROCESSING_OF_FULL =
            "expected of full";

    /**
     * Testovaný procesor.
     */
    private Processor processor = null;

    /**
     * Mock prvku ve zkrácené formě.
     */
    private Element shortFormElementMock = null;

    /**
     * Mock prvku v plné formě.
     */
    private Element fullFormElementMock = null;

    /**
     * Mock parser pro zkrácenou formu prvku.
     */
    private TemplateParser shortFormParserMock;

    /**
     * Mock parseru pro plnou formu prvku.
     */
    private TemplateParser fullFormParserMock = null;

    /**
     * Stub bota.
     */
    private Bot botMock = null;

    /**
     * Mock jazyka.
     */
    private Language languageMock = null;

    /**
     * Mock za prázdný seznam uzlů.
     */
    private NodeList emptyNodeListMock;

    /**
     * Mock za neprázdný seznam uzlů.
     */
    private NodeList notEmptyNodeListMock;

    /**
     * Nastaví objekty k testování.
     * 
     * @throws ProcessorException
     *             chyba při zpracování
     */
    @Before
    public void setUp() throws ProcessorException {
        processor = new GenderProcessor();

        emptyNodeListMock = EasyMock.createMock(NodeList.class);
        expect(emptyNodeListMock.getLength()).andReturn(0);
        replay(emptyNodeListMock);

        shortFormElementMock = EasyMock.createMock(Element.class);
        expect(shortFormElementMock.getChildNodes()).andReturn(
                emptyNodeListMock);
        replay(shortFormElementMock);

        notEmptyNodeListMock = EasyMock.createMock(NodeList.class);
        expect(notEmptyNodeListMock.getLength()).andReturn(MORE_THAN_ZERO);
        replay(notEmptyNodeListMock);

        fullFormElementMock = EasyMock.createMock(Element.class);
        expect(fullFormElementMock.getChildNodes()).andReturn(
                notEmptyNodeListMock);
        replay(fullFormElementMock);

        languageMock = EasyMock.createMock(Language.class);
        expect(languageMock.swapGender(isA(String.class))).andReturn(
                EXPECTED_PROCESSING_OF_FULL);
        replay(languageMock);

        botMock = EasyMock.createMock(Bot.class);
        expect(botMock.getLanguage()).andReturn(languageMock);
        replay(botMock);

        shortFormParserMock = EasyMock.createMock(TemplateParser.class);
        expect(
                shortFormParserMock.processShortenedTag(isA(Element.class),
                        isA(String.class), isA(String.class)))
                .andReturn(EXPECTED_PROCESSING_OF_SHORTENED);
        replay(shortFormParserMock);

        fullFormParserMock = EasyMock.createMock(TemplateParser.class);
        expect(fullFormParserMock.evaluate(isA(NodeList.class))).andReturn(
                DUMMY_STRING);
        expect(fullFormParserMock.getBot()).andReturn(botMock);
        replay(fullFormParserMock);
    }

    /**
     * Uklidí po testování.
     */
    @After
    public void tearDown() {
        processor = null;

        shortFormElementMock = null;

        fullFormElementMock = null;

        fullFormParserMock = null;

        botMock = null;

        languageMock = null;
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.processor.GenderProcessor#process(org.w3c.dom.Element, cz.cuni.mff.ms.brodecva.botnicek.library.parser.TemplateParser)}
     * .
     * 
     * @throws ProcessorException
     *             chyba při zpracování
     */
    @Test
    public void testProcessWhenShortenedFormReturnsExpected()
            throws ProcessorException {
        final String result =
                processor.process(shortFormElementMock, shortFormParserMock);

        verify(shortFormElementMock);
        verify(shortFormParserMock);
        verify(emptyNodeListMock);

        assertEquals(EXPECTED_PROCESSING_OF_SHORTENED, result);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.processor.GenderProcessor#process(org.w3c.dom.Element, cz.cuni.mff.ms.brodecva.botnicek.library.parser.TemplateParser)}
     * .
     * 
     * @throws ProcessorException
     *             chyba při zpracování
     */
    @Test
    public void testProcessWhenFullFormReturnsExpectedText()
            throws ProcessorException {
        final String result =
                processor.process(fullFormElementMock, fullFormParserMock);

        verify(fullFormElementMock);
        verify(botMock);
        verify(languageMock);
        verify(fullFormParserMock);
        verify(notEmptyNodeListMock);

        assertEquals(EXPECTED_PROCESSING_OF_FULL, result);
    }

}
