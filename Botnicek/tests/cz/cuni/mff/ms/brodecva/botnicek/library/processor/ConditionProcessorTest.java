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

import static org.easymock.EasyMock.eq;
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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import cz.cuni.mff.ms.brodecva.botnicek.library.parser.TemplateParser;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.AIML;
import cz.cuni.mff.ms.brodecva.botnicek.library.responder.Conversation;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.Matcher;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.test.UnitTest;

/**
 * Testuje procesor pro podmíněný výraz.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see ConditionProcessor
 */
@Category(UnitTest.class)
public final class ConditionProcessorTest {

    /**
     * Hodnota {@link AIML#ATT_VALUE} pro blokovou podmínku.
     */
    private static final String BLOCK_VALUE = "blockValue";

    /**
     * Hodnota {@link AIML#ATT_NAME} pro blokovou podmínku.
     */
    private static final String BLOCK_PREDICATE_NAME = "blockPredicateName";

    /**
     * Hodnota predikátu blokové podmínky.
     */
    private static final String BLOCK_PREDICATE_VALUE = "blockPredicateValue";

    /**
     * Název {@link AIML#ATT_VALUE}.
     */
    private static final String VALUE_ATTRIBUTE = AIML.ATT_VALUE.getValue();

    /**
     * Název {@link AIML#ATT_NAME}.
     */
    private static final String NAME_ATTRIBUTE = AIML.ATT_NAME.getValue();

    /**
     * Vybraný obsah.
     */
    private static final String CHOSEN_CONTENT = "chosenContent";

    /**
     * Název predikátu pro podmínku s jedním predikátem a více možnostmi.
     */
    private static final String SINGLE_CONDITION_LIST_PREDICATE_NAME =
            "sclPredicateName";

    /**
     * Hodnota predikátu pro podmínku s jedním predikátem a více možnostmi.
     */
    private static final String SINGLE_CONDITION_LIST_PREDICATE_VALUE =
            "sclPredicateValue";

    /**
     * S predikátem souhlasící hodnota {@link AIML#ATT_VALUE} pro větev podmínky
     * s jedním predikátem a více možnostmi.
     */
    private static final String SINGLE_CONDITION_LIST_MATCHING_VALUE =
            "sclMatchingValue";

    /**
     * S predikátem nesouhlasící hodnota {@link AIML#ATT_VALUE} pro větev
     * podmínky s jedním predikátem a více možnostmi.
     */
    private static final String SINGLE_CONDITION_LIST_NOT_MATCHING_VALUE =
            "sclNotMatchingValue";

    /**
     * S predikátem souhlasící hodnota {@link AIML#ATT_VALUE} pro větev podmínky
     * s více predikáty.
     */
    private static final String MULTIPLE_CONDITION_LIST_MATCHING_VALUE =
            "mclMatchingValue";

    /**
     * S predikátem nesouhlasící hodnota {@link AIML#ATT_VALUE} pro větev
     * podmínky s více predikáty.
     */
    private static final String MULTIPLE_CONDITION_LIST_NOT_MATCHING_VALUE =
            "mclNotMatchingValue";

    /**
     * Název predikátu nesouhlasícího s danou hodnotou {@link AIML#ATT_VALUE}
     * pro větev podmínky s více predikáty.
     */
    private static final String MULTIPLE_CONDITION_LIST_NOT_MATCHING_PREDICATE_NAME =
            "mclNotMatchingPredicateValue";

    /**
     * Název predikátu souhlasícího s danou hodnotou {@link AIML#ATT_VALUE} pro
     * větev podmínky s více predikáty.
     */
    private static final String MULTIPLE_CONDITION_LIST_MATCHING_PREDICATE_NAME =
            "mclMatchingPredicateName";

    /**
     * Hodnota predikátu souhlasícího s danou hodnotou {@link AIML#ATT_VALUE}
     * pro větev podmínky s více predikáty.
     */
    private static final String MULTIPLE_CONDITION_LIST_MATCHING_PREDICATE_VALUE =
            "mclMatchingPredicateValue";

    /**
     * Hodnota predikátu nesouhlasícího s danou hodnotou {@link AIML#ATT_VALUE}
     * pro větev podmínky s více predikáty.
     */
    private static final String MULTIPLE_CONDITION_LIST_NOT_MATCHING_PREDICATE_VALUE =
            "mclNotMatchingPredicateValue";

    /**
     * Stub vybraného obsahu blokové podmínky.
     */
    private NodeList blockContentStub = null;

    /**
     * Mock kořenového prvku blokové podmínky.
     */
    private Element blockElementMock = null;

    /**
     * Mock konverzace během zpracování blokové podmínky.
     */
    private Conversation blockConversationMock = null;

    /**
     * {@link Matcher} během zpracování blokové podmínky.
     */
    private Matcher blockMatcher = null;

    /**
     * Mock parseru během zpracování blokové podmínky.
     */
    private TemplateParser blockParser = null;

    /**
     * Stub vybraného obsahu podmínky s jedním predikátem a více možnostmi.
     */
    private NodeList singleConditionListContentStub = null;

    /**
     * Mock kořenového prvku podmínky s jedním predikátem a více možnostmi.
     */
    private Element singleConditionListElementMock = null;

    /**
     * Mock konverzace během zpracování podmínky s jedním predikátem a více
     * možnostmi.
     */
    private Conversation singleConditionListConversationMock = null;

    /**
     * {@link Matcher} během zpracování podmínky s jedním predikátem a více
     * možnostmi.
     */
    private Matcher singleConditionListMatcher = null;

    /**
     * Mock parseru během zpracování podmínky s jedním predikátem a více
     * možnostmi.
     */
    private TemplateParser singleConditionListParser = null;

    /**
     * Mock seznamu uzlů s možnostmi během zpracování podmínky s jedním
     * predikátem a více možnostmi.
     */
    private NodeList singleConditionListOptionsMock = null;

    /**
     * Uzel, který nesplňuje podmínku.
     */
    private Element singleConditionListNotMatchingNode = null;

    /**
     * Uzel, který splňuje podmínku.
     */
    private Element singleConditionListMatchingNode = null;

    /**
     * Výchozí odpověď podmínky s jedním predikátem a více možnostmi.
     */
    private Element singleConditionListDefaultNode = null;

    /**
     * Stub vybraného obsahu podmínky s více predikáty.
     */
    private NodeList multipleConditionListContentStub = null;

    /**
     * Uzel, jehož predikát a hodnota souhlasí.
     */
    private Element multipleConditionListMatchingNode = null;

    /**
     * Uzel, jehož predikát a hodnota nesouhlasí.
     */
    private Element multipleConditionListNotMatchingNode = null;

    /**
     * Výchozí odpověď podmínky s více predikáty.
     */
    private Element multipleConditionListDefaultNode = null;

    /**
     * Mock seznamu uzlů s možnostmi během zpracování podmínky s více predikáty.
     */
    private NodeList multipleConditionListOptionsMock = null;

    /**
     * Mock kořenového prvku podmínky s více predikáty.
     */
    private Element multipleConditionListElementMock = null;

    /**
     * Mock konverzace během zpracování podmínky s více predikáty.
     */
    private Conversation multipleConditionListConversationMock = null;

    /**
     * {@link Matcher} během zpracování podmínky s více predikáty.
     */
    private Matcher multipleConditionListMatcher = null;

    /**
     * Mock parseru během zpracování podmínky s více predikáty.
     */
    private TemplateParser multipleConditionListParser = null;

    /**
     * Nastaví stuby a mocky.
     */
    @Before
    public void setUp() {
        blockContentStub = EasyMock.createMock(NodeList.class);
        replay(blockContentStub);

        blockElementMock = EasyMock.createMock(Element.class);
        expect(blockElementMock.hasAttribute(NAME_ATTRIBUTE)).andStubReturn(
                true);
        expect(blockElementMock.hasAttribute(VALUE_ATTRIBUTE)).andStubReturn(
                true);
        expect(blockElementMock.getAttribute(NAME_ATTRIBUTE)).andReturn(
                BLOCK_PREDICATE_NAME);
        expect(blockElementMock.getAttribute(VALUE_ATTRIBUTE)).andReturn(
                BLOCK_VALUE);

        blockConversationMock = EasyMock.createMock(Conversation.class);
        expect(blockConversationMock.getPredicateValue(BLOCK_PREDICATE_NAME))
                .andStubReturn(BLOCK_PREDICATE_VALUE);
        replay(blockConversationMock);

        blockMatcher = EasyMock.createMock(Matcher.class);

        blockParser = EasyMock.createMock(TemplateParser.class);

        singleConditionListContentStub = EasyMock.createMock(NodeList.class);
        replay(singleConditionListContentStub);

        singleConditionListMatchingNode = EasyMock.createMock(Element.class);

        singleConditionListNotMatchingNode = EasyMock.createMock(Element.class);
        expect(singleConditionListNotMatchingNode.hasAttribute(NAME_ATTRIBUTE))
                .andStubReturn(false);
        expect(singleConditionListNotMatchingNode.hasAttribute(VALUE_ATTRIBUTE))
                .andStubReturn(true);
        expect(singleConditionListNotMatchingNode.getAttribute(VALUE_ATTRIBUTE))
                .andReturn(SINGLE_CONDITION_LIST_NOT_MATCHING_VALUE);
        expect(singleConditionListNotMatchingNode.getNodeType()).andReturn(
                Node.ELEMENT_NODE);
        replay(singleConditionListNotMatchingNode);

        singleConditionListDefaultNode = EasyMock.createMock(Element.class);

        singleConditionListOptionsMock = EasyMock.createMock(NodeList.class);

        singleConditionListElementMock = EasyMock.createMock(Element.class);
        expect(singleConditionListElementMock.hasAttribute(NAME_ATTRIBUTE))
                .andStubReturn(true);
        expect(singleConditionListElementMock.getAttribute(NAME_ATTRIBUTE))
                .andReturn(SINGLE_CONDITION_LIST_PREDICATE_NAME);
        expect(singleConditionListElementMock.hasAttribute(VALUE_ATTRIBUTE))
                .andStubReturn(false);
        expect(singleConditionListElementMock.getChildNodes()).andReturn(
                singleConditionListOptionsMock);
        replay(singleConditionListElementMock);

        singleConditionListConversationMock =
                EasyMock.createMock(Conversation.class);
        expect(
                singleConditionListConversationMock
                        .getPredicateValue(SINGLE_CONDITION_LIST_PREDICATE_NAME))
                .andStubReturn(SINGLE_CONDITION_LIST_PREDICATE_VALUE);
        replay(singleConditionListConversationMock);

        singleConditionListMatcher = EasyMock.createMock(Matcher.class);
        expect(
                singleConditionListMatcher.matches(
                        eq(SINGLE_CONDITION_LIST_PREDICATE_VALUE),
                        eq(SINGLE_CONDITION_LIST_NOT_MATCHING_VALUE)))
                .andStubReturn(false);
        expect(
                singleConditionListMatcher.matches(
                        eq(SINGLE_CONDITION_LIST_PREDICATE_VALUE),
                        eq(SINGLE_CONDITION_LIST_MATCHING_VALUE)))
                .andStubReturn(true);
        replay(singleConditionListMatcher);

        singleConditionListParser = EasyMock.createMock(TemplateParser.class);
        expect(singleConditionListParser.getConversation()).andStubReturn(
                singleConditionListConversationMock);

        multipleConditionListContentStub = EasyMock.createMock(NodeList.class);
        replay(multipleConditionListContentStub);

        multipleConditionListMatchingNode = EasyMock.createMock(Element.class);

        multipleConditionListNotMatchingNode =
                EasyMock.createMock(Element.class);
        expect(
                multipleConditionListNotMatchingNode
                        .hasAttribute(NAME_ATTRIBUTE)).andStubReturn(true);
        expect(
                multipleConditionListNotMatchingNode
                        .hasAttribute(VALUE_ATTRIBUTE)).andStubReturn(true);
        expect(
                multipleConditionListNotMatchingNode
                        .getAttribute(NAME_ATTRIBUTE)).andReturn(
                MULTIPLE_CONDITION_LIST_NOT_MATCHING_PREDICATE_NAME);
        expect(
                multipleConditionListNotMatchingNode
                        .getAttribute(VALUE_ATTRIBUTE)).andReturn(
                MULTIPLE_CONDITION_LIST_NOT_MATCHING_VALUE);
        expect(multipleConditionListNotMatchingNode.getNodeType()).andReturn(
                Node.ELEMENT_NODE);
        replay(multipleConditionListNotMatchingNode);

        multipleConditionListDefaultNode = EasyMock.createMock(Element.class);

        multipleConditionListOptionsMock = EasyMock.createMock(NodeList.class);

        multipleConditionListElementMock = EasyMock.createMock(Element.class);
        expect(multipleConditionListElementMock.hasAttribute(NAME_ATTRIBUTE))
                .andStubReturn(false);
        expect(multipleConditionListElementMock.hasAttribute(VALUE_ATTRIBUTE))
                .andStubReturn(false);
        expect(multipleConditionListElementMock.getChildNodes()).andReturn(
                multipleConditionListOptionsMock);
        replay(multipleConditionListElementMock);

        multipleConditionListConversationMock =
                EasyMock.createMock(Conversation.class);
        expect(
                multipleConditionListConversationMock
                        .getPredicateValue(MULTIPLE_CONDITION_LIST_NOT_MATCHING_PREDICATE_NAME))
                .andStubReturn(
                        MULTIPLE_CONDITION_LIST_NOT_MATCHING_PREDICATE_VALUE);
        expect(
                multipleConditionListConversationMock
                        .getPredicateValue(MULTIPLE_CONDITION_LIST_MATCHING_PREDICATE_NAME))
                .andStubReturn(MULTIPLE_CONDITION_LIST_MATCHING_PREDICATE_VALUE);
        replay(multipleConditionListConversationMock);

        multipleConditionListMatcher = EasyMock.createMock(Matcher.class);
        expect(
                multipleConditionListMatcher
                        .matches(
                                eq(MULTIPLE_CONDITION_LIST_NOT_MATCHING_PREDICATE_VALUE),
                                eq(MULTIPLE_CONDITION_LIST_NOT_MATCHING_VALUE)))
                .andStubReturn(false);
        expect(
                multipleConditionListMatcher.matches(
                        eq(MULTIPLE_CONDITION_LIST_MATCHING_PREDICATE_VALUE),
                        eq(MULTIPLE_CONDITION_LIST_MATCHING_VALUE)))
                .andStubReturn(true);
        replay(multipleConditionListMatcher);

        multipleConditionListParser = EasyMock.createMock(TemplateParser.class);
        expect(multipleConditionListParser.getConversation()).andStubReturn(
                multipleConditionListConversationMock);
    }

    /**
     * Uklidí stuby a mocky.
     */
    @After
    public void tearDown() {
        blockContentStub = null;

        blockElementMock = null;

        blockConversationMock = null;

        blockMatcher = null;

        blockParser = null;

        singleConditionListContentStub = null;

        singleConditionListConversationMock = null;

        singleConditionListDefaultNode = null;

        singleConditionListElementMock = null;

        singleConditionListMatcher = null;

        singleConditionListMatchingNode = null;

        singleConditionListNotMatchingNode = null;

        singleConditionListOptionsMock = null;

        singleConditionListParser = null;

        multipleConditionListContentStub = null;

        multipleConditionListConversationMock = null;

        multipleConditionListDefaultNode = null;

        multipleConditionListElementMock = null;

        multipleConditionListMatcher = null;

        multipleConditionListMatchingNode = null;

        multipleConditionListNotMatchingNode = null;

        multipleConditionListOptionsMock = null;

        multipleConditionListParser = null;
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.processor.ConditionProcessor#process(org.w3c.dom.Element, cz.cuni.mff.ms.brodecva.botnicek.library.parser.TemplateParser)}
     * .
     * 
     * @throws ProcessorException
     *             chyba při zpracování
     */
    @Test
    public void testProcessWhenBlockMatchesReturnsValue()
            throws ProcessorException {
        expect(blockElementMock.getChildNodes()).andReturn(blockContentStub);
        replay(blockElementMock);

        expect(blockMatcher.matches(eq(BLOCK_PREDICATE_VALUE), eq(BLOCK_VALUE)))
                .andReturn(true);
        replay(blockMatcher);

        expect(blockParser.getConversation()).andStubReturn(
                blockConversationMock);
        expect(blockParser.evaluate(isA(NodeList.class))).andReturn(
                CHOSEN_CONTENT);
        replay(blockParser);

        final Processor processor = new ConditionProcessor(blockMatcher);
        final String result = processor.process(blockElementMock, blockParser);

        verify(blockElementMock);
        verify(blockMatcher);
        verify(blockParser);
        verify(blockConversationMock);
        verify(blockContentStub);

        assertEquals(CHOSEN_CONTENT, result);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.processor.ConditionProcessor#process(org.w3c.dom.Element, cz.cuni.mff.ms.brodecva.botnicek.library.parser.TemplateParser)}
     * .
     * @throws ProcessorException
     *             chyba při zpracování
     */
    @Test
    public void testProcessWhenBlockDoesNotMatchReturnsEmpty()
            throws ProcessorException {
        replay(blockElementMock);

        expect(blockMatcher.matches(eq(BLOCK_PREDICATE_VALUE), eq(BLOCK_VALUE)))
                .andReturn(false);
        replay(blockMatcher);

        expect(blockParser.getConversation()).andStubReturn(
                blockConversationMock);
        replay(blockParser);

        final Processor processor = new ConditionProcessor(blockMatcher);
        final String result = processor.process(blockElementMock, blockParser);

        verify(blockElementMock);
        verify(blockMatcher);
        verify(blockParser);
        verify(blockConversationMock);
        verify(blockContentStub);

        assertEquals(Processor.EMPTY_RESPONSE, result);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.processor.ConditionProcessor#process(org.w3c.dom.Element, cz.cuni.mff.ms.brodecva.botnicek.library.parser.TemplateParser)}
     * .
     * @throws ProcessorException
     *             chyba při zpracování
     */
    @Test
    public void testProcessWhenSingleConditionListMatchesReturnsValue()
            throws ProcessorException {
        expect(singleConditionListMatchingNode.hasAttribute(VALUE_ATTRIBUTE))
                .andStubReturn(true);
        expect(singleConditionListMatchingNode.hasAttribute(NAME_ATTRIBUTE))
                .andStubReturn(false);
        expect(singleConditionListMatchingNode.getAttribute(VALUE_ATTRIBUTE))
                .andReturn(SINGLE_CONDITION_LIST_MATCHING_VALUE);
        expect(singleConditionListMatchingNode.getNodeType()).andReturn(
                Node.ELEMENT_NODE);
        expect(singleConditionListMatchingNode.getChildNodes()).andReturn(
                singleConditionListContentStub);
        replay(singleConditionListMatchingNode);

        replay(singleConditionListDefaultNode);

        expect(singleConditionListOptionsMock.item(0)).andReturn(
                singleConditionListNotMatchingNode);
        expect(singleConditionListOptionsMock.item(1)).andReturn(
                singleConditionListMatchingNode);
        expect(singleConditionListOptionsMock.getLength()).andReturn(2);
        replay(singleConditionListOptionsMock);

        expect(singleConditionListParser.evaluate(isA(NodeList.class)))
                .andReturn(CHOSEN_CONTENT);
        replay(singleConditionListParser);

        final Processor processor =
                new ConditionProcessor(singleConditionListMatcher);
        final String result =
                processor.process(singleConditionListElementMock,
                        singleConditionListParser);

        verify(singleConditionListConversationMock);
        verify(singleConditionListElementMock);
        verify(singleConditionListMatcher);
        verify(singleConditionListMatchingNode);
        verify(singleConditionListNotMatchingNode);
        verify(singleConditionListOptionsMock);
        verify(singleConditionListParser);

        assertEquals(CHOSEN_CONTENT, result);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.processor.ConditionProcessor#process(org.w3c.dom.Element, cz.cuni.mff.ms.brodecva.botnicek.library.parser.TemplateParser)}
     * .
     * 
     * @throws ProcessorException
     *             chyba při zpracování
     */
    @Test
    public
            void
            testProcessWhenSingleConditionListWithoutDefaultDoesNotMatchReturnsEmpty()
                    throws ProcessorException {
        replay(singleConditionListMatchingNode);

        replay(singleConditionListDefaultNode);

        expect(singleConditionListOptionsMock.item(0)).andReturn(
                singleConditionListNotMatchingNode);
        expect(singleConditionListOptionsMock.getLength()).andReturn(1);
        replay(singleConditionListOptionsMock);

        replay(singleConditionListParser);

        final Processor processor =
                new ConditionProcessor(singleConditionListMatcher);
        final String result =
                processor.process(singleConditionListElementMock,
                        singleConditionListParser);

        verify(singleConditionListConversationMock);
        verify(singleConditionListElementMock);
        verify(singleConditionListMatcher);
        verify(singleConditionListMatchingNode);
        verify(singleConditionListNotMatchingNode);
        verify(singleConditionListOptionsMock);
        verify(singleConditionListParser);

        assertEquals(Processor.EMPTY_RESPONSE, result);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.processor.ConditionProcessor#process(org.w3c.dom.Element, cz.cuni.mff.ms.brodecva.botnicek.library.parser.TemplateParser)}
     * .
     * 
     * @throws ProcessorException
     *             chyba při zpracování
     */
    @Test
    public
            void
            testProcessWhenSingleConditionListWithDefaultDoesNotMatchReturnsDefault()
                    throws ProcessorException {
        replay(singleConditionListMatchingNode);

        expect(singleConditionListDefaultNode.getNodeType()).andReturn(
                Node.ELEMENT_NODE);
        expect(singleConditionListDefaultNode.hasAttribute(NAME_ATTRIBUTE))
                .andStubReturn(false);
        expect(singleConditionListDefaultNode.hasAttribute(VALUE_ATTRIBUTE))
                .andStubReturn(false);
        expect(singleConditionListDefaultNode.getChildNodes()).andReturn(
                singleConditionListContentStub);
        replay(singleConditionListDefaultNode);

        expect(singleConditionListOptionsMock.item(0)).andReturn(
                singleConditionListNotMatchingNode);
        expect(singleConditionListOptionsMock.item(1)).andReturn(
                singleConditionListDefaultNode);
        expect(singleConditionListOptionsMock.getLength()).andReturn(2);
        replay(singleConditionListOptionsMock);

        expect(singleConditionListParser.evaluate(isA(NodeList.class)))
                .andReturn(CHOSEN_CONTENT);
        replay(singleConditionListParser);

        final Processor processor =
                new ConditionProcessor(singleConditionListMatcher);
        final String result =
                processor.process(singleConditionListElementMock,
                        singleConditionListParser);

        verify(singleConditionListConversationMock);
        verify(singleConditionListElementMock);
        verify(singleConditionListMatcher);
        verify(singleConditionListMatchingNode);
        verify(singleConditionListNotMatchingNode);
        verify(singleConditionListDefaultNode);
        verify(singleConditionListOptionsMock);
        verify(singleConditionListParser);

        assertEquals(CHOSEN_CONTENT, result);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.processor.ConditionProcessor#process(org.w3c.dom.Element, cz.cuni.mff.ms.brodecva.botnicek.library.parser.TemplateParser)}
     * .
     * 
     * @throws ProcessorException
     *             chyba při zpracování
     */
    @Test
    public void testProcessWhenMultipleConditionsMatchesReturnsValue()
            throws ProcessorException {
        expect(multipleConditionListMatchingNode.hasAttribute(VALUE_ATTRIBUTE))
                .andStubReturn(true);
        expect(multipleConditionListMatchingNode.hasAttribute(NAME_ATTRIBUTE))
                .andStubReturn(true);
        expect(multipleConditionListMatchingNode.getAttribute(NAME_ATTRIBUTE))
                .andReturn(MULTIPLE_CONDITION_LIST_MATCHING_PREDICATE_NAME);
        expect(multipleConditionListMatchingNode.getAttribute(VALUE_ATTRIBUTE))
                .andReturn(MULTIPLE_CONDITION_LIST_MATCHING_VALUE);
        expect(multipleConditionListMatchingNode.getNodeType()).andReturn(
                Node.ELEMENT_NODE);
        expect(multipleConditionListMatchingNode.getChildNodes()).andReturn(
                multipleConditionListContentStub);
        replay(multipleConditionListMatchingNode);

        replay(multipleConditionListDefaultNode);

        expect(multipleConditionListOptionsMock.item(0)).andReturn(
                multipleConditionListNotMatchingNode);
        expect(multipleConditionListOptionsMock.item(1)).andReturn(
                multipleConditionListMatchingNode);
        expect(multipleConditionListOptionsMock.getLength()).andReturn(2);
        replay(multipleConditionListOptionsMock);

        expect(multipleConditionListParser.evaluate(isA(NodeList.class)))
                .andReturn(CHOSEN_CONTENT);
        replay(multipleConditionListParser);

        final Processor processor =
                new ConditionProcessor(multipleConditionListMatcher);
        final String result =
                processor.process(multipleConditionListElementMock,
                        multipleConditionListParser);

        verify(multipleConditionListConversationMock);
        verify(multipleConditionListElementMock);
        verify(multipleConditionListMatcher);
        verify(multipleConditionListMatchingNode);
        verify(multipleConditionListNotMatchingNode);
        verify(multipleConditionListOptionsMock);
        verify(multipleConditionListParser);

        assertEquals(CHOSEN_CONTENT, result);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.processor.ConditionProcessor#process(org.w3c.dom.Element, cz.cuni.mff.ms.brodecva.botnicek.library.parser.TemplateParser)}
     * .
     * 
     * @throws ProcessorException
     *             chyba při zpracování
     */
    @Test
    public
            void
            testProcessWhenMultipleConditionsWithoutDefaultDoesNotMatchReturnsEmpty()
                    throws ProcessorException {
        replay(multipleConditionListMatchingNode);

        replay(multipleConditionListDefaultNode);

        expect(multipleConditionListOptionsMock.item(0)).andReturn(
                multipleConditionListNotMatchingNode);
        expect(multipleConditionListOptionsMock.getLength()).andReturn(1);
        replay(multipleConditionListOptionsMock);

        replay(multipleConditionListParser);

        final Processor processor =
                new ConditionProcessor(multipleConditionListMatcher);
        final String result =
                processor.process(multipleConditionListElementMock,
                        multipleConditionListParser);

        verify(multipleConditionListConversationMock);
        verify(multipleConditionListElementMock);
        verify(multipleConditionListMatcher);
        verify(multipleConditionListMatchingNode);
        verify(multipleConditionListNotMatchingNode);
        verify(multipleConditionListOptionsMock);
        verify(multipleConditionListParser);

        assertEquals(Processor.EMPTY_RESPONSE, result);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.processor.ConditionProcessor#process(org.w3c.dom.Element, cz.cuni.mff.ms.brodecva.botnicek.library.parser.TemplateParser)}
     * .
     * 
     * @throws ProcessorException
     *             chyba při zpracování
     */
    @Test
    public
            void
            testProcessWhenMultipleConditionsWithDefaultDoesNotMatchReturnsDefault()
                    throws ProcessorException {
        replay(multipleConditionListMatchingNode);

        expect(multipleConditionListDefaultNode.getNodeType()).andReturn(
                Node.ELEMENT_NODE);
        expect(multipleConditionListDefaultNode.hasAttribute(NAME_ATTRIBUTE))
                .andStubReturn(false);
        expect(multipleConditionListDefaultNode.hasAttribute(VALUE_ATTRIBUTE))
                .andStubReturn(false);
        expect(multipleConditionListDefaultNode.getChildNodes()).andReturn(
                multipleConditionListContentStub);
        replay(multipleConditionListDefaultNode);

        expect(multipleConditionListOptionsMock.item(0)).andReturn(
                multipleConditionListNotMatchingNode);
        expect(multipleConditionListOptionsMock.item(1)).andReturn(
                multipleConditionListDefaultNode);
        expect(multipleConditionListOptionsMock.getLength()).andReturn(2);
        replay(multipleConditionListOptionsMock);

        expect(multipleConditionListParser.evaluate(isA(NodeList.class)))
                .andReturn(CHOSEN_CONTENT);
        replay(multipleConditionListParser);

        final Processor processor =
                new ConditionProcessor(multipleConditionListMatcher);
        final String result =
                processor.process(multipleConditionListElementMock,
                        multipleConditionListParser);

        verify(multipleConditionListConversationMock);
        verify(multipleConditionListElementMock);
        verify(multipleConditionListMatcher);
        verify(multipleConditionListMatchingNode);
        verify(multipleConditionListNotMatchingNode);
        verify(multipleConditionListDefaultNode);
        verify(multipleConditionListOptionsMock);
        verify(multipleConditionListParser);

        assertEquals(CHOSEN_CONTENT, result);
    }

}
