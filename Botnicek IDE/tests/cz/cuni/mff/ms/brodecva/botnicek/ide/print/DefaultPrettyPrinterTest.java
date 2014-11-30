/**
 * Copyright Václav Brodec 2014.
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
package cz.cuni.mff.ms.brodecva.botnicek.ide.print;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Intended;

/**
 * Testuje výchozí implementaci formátovače XML.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see DefaultPrettyPrinter
 */
public class DefaultPrettyPrinterTest {

    private static final int TEST_INDENT = 4;
    private static final String NEW_LINE = System.lineSeparator();

    private DefaultPrettyPrinter tested = Intended.nullReference();

    /**
     * Vytvoří testovací objekt.
     * 
     * @throws java.lang.Exception
     *             pokud dojde k vyhození výjimky
     */
    @Before
    public void setUp() throws Exception {
        this.tested = DefaultPrettyPrinter.create(TEST_INDENT);
    }

    /**
     * Uklidí testovací objekt.
     * 
     * @throws java.lang.Exception
     *             pokud dojde k vyhození výjimky
     */
    @After
    public void tearDown() throws Exception {
        this.tested = Intended.nullReference();
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.print.DefaultPrettyPrinter#create(int)}
     * .
     */
    @Test
    public void testCreate() {
        DefaultPrettyPrinter.create(TEST_INDENT);
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.print.DefaultPrettyPrinter#print(java.lang.String)}
     * .
     * 
     * @throws PrintException
     *             pokud dojde k chybě při tisku
     */
    @Test
    public void testPrint() throws PrintException {
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + NEW_LINE
                + "<root>" + NEW_LINE + "    <element>Text</element>"
                + NEW_LINE + "</root>" + NEW_LINE,
                this.tested.print("<root><element>Text</element></root>"));
    }

}
