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

import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.w3c.dom.Element;

import cz.cuni.mff.ms.brodecva.botnicek.library.parser.TemplateParser;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.test.UnitTest;

/**
 * Testuje procesor pro výpis aktuálního data.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see DateProcessor
 */
@Category(UnitTest.class)
public final class DateProcessorTest {

    /**
     * Vzor pro formátovač.
     */
    private static final String FORMATTED_DATE_PATTERN = "'formattedDate'";

    /**
     * Formátované datum.
     */
    private static final String FORMATTED_DATE = "formattedDate";

    /**
     * Mock parseru. Nečeká žádné volání.
     */
    private TemplateParser parserMock = null;

    /**
     * Testovaný procesor.
     */
    private Processor processor = null;

    /**
     * Mock prvku. Nečeká žádné volání.
     */
    private Element elementMock = null;

    /**
     * Stub formátovače.
     */
    private DateFormat dateFormatStub = null;

    /**
     * Nastaví objekty k testování.
     */
    @Before
    public void setUp() {
        dateFormatStub = new SimpleDateFormat(FORMATTED_DATE_PATTERN);

        processor = new DateProcessor(dateFormatStub);

        elementMock = EasyMock.createMock(Element.class);
        replay(elementMock);

        parserMock = EasyMock.createMock(TemplateParser.class);
        replay(parserMock);
    }

    /**
     * Uklidí po testování.
     */
    @After
    public void tearDown() {
        processor = null;

        parserMock = null;

        dateFormatStub = null;

        elementMock = null;
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.processor.DateProcessor#process(org.w3c.dom.Element, cz.cuni.mff.ms.brodecva.botnicek.library.parser.TemplateParser)}
     * .
     * 
     * @throws ProcessorException
     *             chyba při zpracování
     */
    @Test
    public void testProcessReturnsFormattedDate() throws ProcessorException {
        final String result = processor.process(elementMock, parserMock);

        verify(parserMock);
        verify(elementMock);

        assertEquals(FORMATTED_DATE, result);
    }

}
