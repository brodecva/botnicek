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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.w3c.dom.Element;

import cz.cuni.mff.ms.brodecva.botnicek.library.parser.TemplateParser;

/**
 * Zvětší první písmeno každé věty.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class SentenceProcessor extends AbstractProcessor {
    /**
     * UID serializované verze.
     */
    private static final long serialVersionUID = 2426300304781729355L;

    /**
     * Vzor na rozpoznání vět dle definice.
     */
    private static Pattern sentence = Pattern.compile("\\w[^.]*\\.|^\\w[^.]*$");

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
        final String text = parser.evaluate(element.getChildNodes());

        final Matcher sentenceMatcher = sentence.matcher(text);
        final StringBuffer result = new StringBuffer(text.length());
        while (sentenceMatcher.find()) {
            final String sentence = sentenceMatcher.group();

            sentenceMatcher.appendReplacement(
                    result,
                    Character.toUpperCase(sentence.charAt(0))
                            + sentence.substring(1));
        }
        sentenceMatcher.appendTail(result);

        return result.toString();
    }

}
