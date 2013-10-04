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
 * Testuje procesor pro převod textu na text s velkými písmeny na začátku slov.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see FormalProcessor
 */
@Category(UnitTest.class)
public final class FormalProcessorTest {

    /**
     * Text ke zpracování.
     */
    private static final String TEXT_TO_FORMALIZE =
            "Common text to formalize. John, Paul, OMG, that's it!";

    /**
     * Očekávaný text po zpracování.
     */
    private static final String FORMALIZED_TEXT =
            "Common Text To Formalize. John, Paul, OMG, That's It!";

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
        processor = new FormalProcessor();

        final NodeList nodeListStub = EasyMock.createNiceMock(NodeList.class);
        replay(nodeListStub);

        elementMock = EasyMock.createMock(Element.class);
        expect(elementMock.getChildNodes()).andStubReturn(nodeListStub);
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
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.processor.FormalProcessor#process(org.w3c.dom.Element, cz.cuni.mff.ms.brodecva.botnicek.library.parser.TemplateParser)}
     * .
     * 
     * @throws ProcessorException
     *             chyba při zpracování
     */
    @Test
    public void testProcessWhenEmpty() throws ProcessorException {
        final TemplateParser parserMock =
                EasyMock.createMock(TemplateParser.class);
        expect(parserMock.evaluate(isA(NodeList.class))).andStubReturn(
                Processor.EMPTY_RESPONSE);
        replay(parserMock);

        final String result = processor.process(elementMock, parserMock);

        verify(elementMock);
        verify(parserMock);

        assertEquals(Processor.EMPTY_RESPONSE, result);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.processor.FormalProcessor#process(org.w3c.dom.Element, cz.cuni.mff.ms.brodecva.botnicek.library.parser.TemplateParser)}
     * .
     * 
     * @throws ProcessorException
     *             chyba při zpracování
     */
    @Test
    public void testProcessReturnsFormalizedText() throws ProcessorException {
        final TemplateParser parserMock =
                EasyMock.createMock(TemplateParser.class);
        expect(parserMock.evaluate(isA(NodeList.class))).andStubReturn(
                TEXT_TO_FORMALIZE);
        replay(parserMock);

        final String result = processor.process(elementMock, parserMock);

        verify(elementMock);
        verify(parserMock);

        assertEquals(FORMALIZED_TEXT, result);
    }

}
