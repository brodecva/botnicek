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

import static org.easymock.EasyMock.and;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.geq;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.lt;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.easymock.EasyMock;
import org.easymock.IAnswer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import cz.cuni.mff.ms.brodecva.botnicek.library.parser.TemplateParser;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.XML;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.test.UnitTest;

/**
 * Testuje procesor pro volbu náhodné odpovědi. Provádí opakovaně zpracování a
 * ověřuje náhodné chování.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see RandomProcessor
 */
@Category(UnitTest.class)
@RunWith(PowerMockRunner.class)
@PrepareForTest(XML.class)
public final class RandomProcessorTest {

    /**
     * Počet voleb.
     */
    private static final int CHOICES_COUNT = 7;

    /**
     * Počet opakování výpočtu.
     */
    private static final int REPETITIONS_COUNT = 1000;

    /**
     * Povolená chyba.
     */
    private static final float EXPECTED_ERROR = 0.05f;

    /**
     * Mock parseru.
     */
    private TemplateParser parserMock = null;

    /**
     * Testovaný procesor.
     */
    private Processor processor = null;

    /**
     * Stub prvku.
     */
    private Element elementStub = null;

    /**
     * Stub seznamu prvků s možnostmi.
     */
    private List<Element> possibilitiesStub = null;

    /**
     * Nastaví objekty k testování.
     * 
     * @throws ProcessorException
     *             chyba při zpracování
     */
    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws ProcessorException {
        processor = new RandomProcessor();

        final IAnswer<Element> elementAnswer = new IAnswer<Element>() {

            @Override
            public Element answer() throws Throwable {
                final int argument = (int) EasyMock.getCurrentArguments()[0];

                final NodeList childrenMock =
                        EasyMock.createMock(NodeList.class);
                expect(childrenMock.getLength()).andStubReturn(argument); // Tolik
                                                                          // jako
                                                                          // argument.
                replay(childrenMock);

                final Element result = EasyMock.createMock(Element.class);
                expect(result.getChildNodes()).andReturn(childrenMock);
                replay(result);

                return result;
            }

        };

        possibilitiesStub = EasyMock.createMock(List.class);
        expect(possibilitiesStub.size()).andStubReturn(CHOICES_COUNT);
        expect(possibilitiesStub.get(and(lt(CHOICES_COUNT), geq(0))))
                .andAnswer(elementAnswer).times(REPETITIONS_COUNT);
        replay(possibilitiesStub);

        elementStub = EasyMock.createMock(Element.class);
        replay(elementStub);

        parserMock = EasyMock.createMock(TemplateParser.class);
        expect(parserMock.evaluate(isA(NodeList.class))).andAnswer(
                new IAnswer<String>() {

                    @Override
                    public String answer() throws Throwable {
                        return String.valueOf(((NodeList) EasyMock
                                .getCurrentArguments()[0]).getLength());
                    }

                }).times(REPETITIONS_COUNT);
        replay(parserMock);
    }

    /**
     * Uklidí po testování.
     */
    @After
    public void tearDown() {
        processor = null;

        parserMock = null;

        elementStub = null;

        possibilitiesStub = null;
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.processor.RandomProcessor#process(org.w3c.dom.Element, cz.cuni.mff.ms.brodecva.botnicek.library.parser.TemplateParser)}
     * .
     * 
     * @throws ProcessorException
     *             chyba při zpracování
     */
    @Test
    public void testProcessBehavesUniformly() throws ProcessorException {
        final int[] valuesCount = new int[CHOICES_COUNT];

        PowerMock.mockStatic(XML.class);
        expect(XML.listFilialElements(elementStub))
                .andReturn(possibilitiesStub).times(REPETITIONS_COUNT);
        PowerMock.replayAll();

        for (int i = 0; i < REPETITIONS_COUNT; i++) {
            final String result = processor.process(elementStub, parserMock);

            final int value = Integer.parseInt(result);

            valuesCount[value]++;
        }

        verify(elementStub);
        verify(parserMock);

        final float expectedCount =
                (float) REPETITIONS_COUNT / (float) CHOICES_COUNT;

        for (final int count : valuesCount) {
            final float difference = Math.abs(count - expectedCount);

            final float error = difference / REPETITIONS_COUNT;

            assertThat(error, lessThan(EXPECTED_ERROR));
        }
    }
}
