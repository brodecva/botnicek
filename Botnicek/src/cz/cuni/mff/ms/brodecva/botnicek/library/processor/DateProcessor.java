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

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.w3c.dom.Element;

import cz.cuni.mff.ms.brodecva.botnicek.library.parser.TemplateParser;

/**
 * Vrátí naformátovaný aktuální místní čas.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class DateProcessor extends AbstractProcessor {
    /**
     * UID serializované verze.
     */
    private static final long serialVersionUID = 5715687213400217000L;

    /**
     * Formátovač data.
     */
    private final DateFormat dateFormat;

    /**
     * Vytvoří procesor s výchozím formátovačem data.
     */
    public DateProcessor() {
        dateFormat =
                DateFormat
                        .getDateInstance(DateFormat.FULL, Locale.getDefault());
    }

    /**
     * Vytvoří procesor s dodaným formátovačem data.
     * 
     * @param dateFormat
     *            formátovač data
     */
    public DateProcessor(final DateFormat dateFormat) {
        this.dateFormat = (DateFormat) dateFormat.clone();
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
        final String formattedDate = dateFormat.format(new Date());

        return formattedDate;
    }

}
