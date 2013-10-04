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

import java.util.StringTokenizer;

import org.w3c.dom.Element;

import cz.cuni.mff.ms.brodecva.botnicek.library.parser.TemplateParser;

/**
 * Převede text na text se slovy začínajícími kapitálkami.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class FormalProcessor extends AbstractProcessor {
    /**
     * UID serializované verze.
     */
    private static final long serialVersionUID = 2933844678122511597L;

    /**
     * Mezera.
     */
    private static final char SPACE = ' ';

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
        return convertToTitleCase(parser.evaluate(element.getChildNodes()));
    }

    /**
     * Převede text na text se slovy začínajícími kapitálkami.
     * 
     * @param text
     *            text
     * @return text se slovy začínajícími velkým písmenem
     */
    private String convertToTitleCase(final String text) {
        if (TemplateParser.EMPTY_CONTENT.equals(text)) {
            return text;
        }

        final StringTokenizer tokenizer =
                new StringTokenizer(text, Character.toString(SPACE));

        final StringBuilder result = new StringBuilder(text.length());

        while (tokenizer.hasMoreTokens()) {
            final String word = tokenizer.nextToken();

            if (result.length() > 0) {
                result.append(SPACE);
            }

            result.append(Character.toTitleCase(word.charAt(0))
                    + word.substring(1));
        }

        return result.toString();
    }
}
