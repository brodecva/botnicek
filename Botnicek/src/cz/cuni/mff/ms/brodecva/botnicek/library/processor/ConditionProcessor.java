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

import java.util.logging.Level;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import cz.cuni.mff.ms.brodecva.botnicek.library.parser.TemplateParser;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.AIML;
import cz.cuni.mff.ms.brodecva.botnicek.library.responder.Conversation;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.Matcher;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.AIMLStructuralMatcher;

/**
 * Zpracovává podmínkové výrazy.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class ConditionProcessor extends AbstractProcessor {
    /**
     * UID serializované verze.
     */
    private static final long serialVersionUID = 4317257546866888312L;

    /**
     * Porovnávač vzorů.
     */
    private final Matcher matcher;

    /**
     * Výchozí konstruktor.
     */
    public ConditionProcessor() {
        matcher = new AIMLStructuralMatcher();
    }

    /**
     * Vytvoří procesor s daným porovnávačem vzorů.
     * 
     * @param matcher
     *            porovnávač vzorů
     */
    public ConditionProcessor(final Matcher matcher) {
        this.matcher = matcher;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.library.processor.Processor#process(
     * org.w3c.dom.Element,
     * cz.cuni.mff.ms.brodecva.botnicek.library.parser.TemplateParser)
     */
    @Override
    public String process(final Element element, final TemplateParser parser)
            throws ProcessorException {
        final Conversation conversation = parser.getConversation();

        String predicateValue = null;

        if (element.hasAttribute(AIML.ATT_NAME.getValue())) {
            final String predicateName =
                    element.getAttribute(AIML.ATT_NAME.getValue());

            predicateValue = conversation.getPredicateValue(predicateName);

            if (element.hasAttribute(AIML.ATT_VALUE.getValue())) {
                final String result = processSingleConditionBlock(element, parser,
                        predicateValue);
                
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.log(Level.FINE, "processor.SingleConditionProcessorResult", new Object[] { element, predicateName, predicateValue, result });
                }
                
                return result;
            }
        }
        
        final String result = processOptionList(element.getChildNodes(), parser,
                predicateValue);
        
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, "processor.MultipleConditionProcessorResult", new Object[] { element, predicateValue, result });
        }
        
        return result;
    }

    /**
     * Zpracuje jednoduchou podmínku.
     * 
     * @param element
     *            prvek
     * @param parser
     *            parser
     * @param predicateValue
     *            hodnota predikátu
     * @return výstup
     * @throws ProcessorException
     *             chyba při zpracování
     */
    private String processSingleConditionBlock(final Element element,
            final TemplateParser parser, final String predicateValue)
            throws ProcessorException {
        final String value = element.getAttribute(AIML.ATT_VALUE.getValue());

        if (matcher.matches(predicateValue, value)) {
            return parser.evaluate(element.getChildNodes());
        } else {
            return EMPTY_RESPONSE;
        }
    }

    /**
     * Zpracuje seznam možností.
     * 
     * @param list
     *            seznam možností
     * @param parser
     *            parser
     * @param predicateValue
     *            hodnota predikátu (null v případě vícepredikátové podmínky)
     * @return response výstup
     * @throws ProcessorException
     *             chyba při zpracování
     */
    private String processOptionList(final NodeList list,
            final TemplateParser parser, final String predicateValue)
            throws ProcessorException {
        String result = EMPTY_RESPONSE;

        final int length = list.getLength();
        for (int index = 0; index < length; index++) {
            final Node node = list.item(index);

            if (node == null) {
                continue;
            }

            switch (node.getNodeType()) {
            case Node.ELEMENT_NODE:
                final Element element = (Element) node;

                if (!element.hasAttribute(AIML.ATT_NAME.getValue())
                        && !element.hasAttribute(AIML.ATT_VALUE.getValue())) {
                    result = result + parser.evaluate(node.getChildNodes());
                    break;
                }

                if (!element.hasAttribute(AIML.ATT_VALUE.getValue())) {
                    break;
                }

                final Conversation conversation = parser.getConversation();
                final String itemValue =
                        element.getAttribute(AIML.ATT_VALUE.getValue());

                final String usedPredicateValue;
                if (predicateValue == null) {
                    // Zpracování seznamu se jménem a hodnotou.

                    if (!element.hasAttribute(AIML.ATT_NAME.getValue())) {
                        break;
                    }

                    final String itemName =
                            element.getAttribute(AIML.ATT_NAME.getValue());

                    usedPredicateValue =
                            conversation.getPredicateValue(itemName);
                } else {
                    // Zpracování seznamu jen s hodnotami.

                    usedPredicateValue = predicateValue;
                }

                if (matcher.matches(usedPredicateValue, itemValue)) {
                    return result + parser.evaluate(element.getChildNodes());
                }
            default:
                break;
            }
        }

        return result;
    }
}
