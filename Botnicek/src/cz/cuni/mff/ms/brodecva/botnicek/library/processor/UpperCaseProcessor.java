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

import org.w3c.dom.Element;

import cz.cuni.mff.ms.brodecva.botnicek.library.parser.TemplateParser;

/**
 * Převede všechna písmena na velká.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class UpperCaseProcessor extends AbstractProcessor {
    /**
     * UID serializované verze.
     */
    private static final long serialVersionUID = 5550429151578597751L;

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
        return parser.evaluate(element.getChildNodes()).toUpperCase();
    }

}
