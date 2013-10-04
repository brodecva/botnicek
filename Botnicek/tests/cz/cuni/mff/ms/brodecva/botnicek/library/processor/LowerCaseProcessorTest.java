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

import cz.cuni.mff.ms.brodecva.botnicek.library.parser.TemplateParser;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.test.UnitTest;

/**
 * Testuje procesor pro převod na malá písmena.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see LowerCaseProcessor
 */
@Category(UnitTest.class)
public final class LowerCaseProcessorTest {

    /**
     * Prázdný řetězec.
     */
    private static final String EMPTY_STRING = "";

    /**
     * Text ke zpracování.
     */
    private static final String TEXT_TO_LOWER_CASE =
            "Common text to convert to lower case. John, Paul, OMG, that's it!";

    /**
     * Očekávaný text po zpracování.
     */
    private static final String LOWER_CASED_TEXT =
            "common text to convert to lower case. john, paul, omg, that's it!";

    /**
     * Testovaný procesor.
     */
    private Processor processor = null;

    /**
     * Mock prvku.
     */
    private Element elementMock;

    /**
     * Nastaví objekty k testování.
     */
    @Before
    public void setUp() {
        processor = new LowerCaseProcessor();

        final NodeList nodeListStub = EasyMock.createNiceMock(NodeList.class);
        replay(nodeListStub);

        elementMock = EasyMock.createMock(Element.class);
        expect(elementMock.getChildNodes()).andReturn(nodeListStub);
        replay(elementMock);
    }

    /**
     * Uklidí po testování.
     */
    @After
    public void tearDown() {
        processor = null;

        elementMock = null;
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.processor.LowerCaseProcessor#process(org.w3c.dom.Element, cz.cuni.mff.ms.brodecva.botnicek.library.parser.TemplateParser)}
     * .
     * 
     * @throws ProcessorException
     *             chyba při zpracování
     */
    @Test
    public void testProcessWhenEmpty() throws ProcessorException {
        final TemplateParser parserMock =
                EasyMock.createMock(TemplateParser.class);
        expect(parserMock.evaluate(isA(NodeList.class)))
                .andReturn(EMPTY_STRING);
        replay(parserMock);

        final String result = processor.process(elementMock, parserMock);

        verify(elementMock);
        verify(parserMock);

        assertEquals(EMPTY_STRING, result);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.processor.LowerCaseProcessor#process(org.w3c.dom.Element, cz.cuni.mff.ms.brodecva.botnicek.library.parser.TemplateParser)}
     * .
     * 
     * @throws ProcessorException
     *             chyba při zpracování
     */
    @Test
    public void testProcessReturnsLowerCase() throws ProcessorException {
        final TemplateParser parserMock =
                EasyMock.createMock(TemplateParser.class);
        expect(parserMock.evaluate(isA(NodeList.class))).andReturn(
                TEXT_TO_LOWER_CASE);
        replay(parserMock);

        final String result = processor.process(elementMock, parserMock);

        verify(elementMock);
        verify(parserMock);

        assertEquals(LOWER_CASED_TEXT, result);
    }

}
