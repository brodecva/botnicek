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

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.w3c.dom.Element;
import cz.cuni.mff.ms.brodecva.botnicek.library.parser.TemplateParser;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.XML;

/**
 * Zvolí náhodně jednu možnost.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class RandomProcessor extends AbstractProcessor {

    /**
     * UID serializované verze.
     */
    private static final long serialVersionUID = -3195663397159636996L;

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
        final List<Element> possibilities = XML.listFilialElements(element);

        final int possibilitiesCount = possibilities.size();

        final int randomPick =
                Math.abs(ThreadLocalRandom.current().nextInt(possibilitiesCount));

        return parser.evaluate(possibilities.get(randomPick)
                .getChildNodes());
    }

}
