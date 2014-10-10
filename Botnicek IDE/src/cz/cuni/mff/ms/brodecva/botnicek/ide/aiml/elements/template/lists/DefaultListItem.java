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
package cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.lists;

import java.util.List;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.TemplateElement;

/**
 * Výchozí položka seznamu nemá žádné významné atributy. Je buďto nepovinnou
 * spádovou možností v bloku podmínek, pokud žádná jiná neuspěje, nebo jednou z
 * možností náhodného výběru.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see <a
 *      href="http://www.alicebot.org/TR/2011/#section-condition">http://www.alicebot.org/TR/2011/#section-condition</a>
 * @see <a
 *      href="http://www.alicebot.org/TR/2011/#section-random">http://www.alicebot.org/TR/2011/#section-random</a>
 */
public final class DefaultListItem extends AbstractListItem {

    /**
     * Vytvoří položku.
     * 
     * @param content
     *            potomci prvku
     * @return vytvoří výchozího položku
     */
    public static DefaultListItem create(final List<TemplateElement> content) {
        return new DefaultListItem(content);
    }

    /**
     * Vytvoří položku.
     * 
     * @param content
     *            potomci prvku
     * @return vytvoří výchozího položku
     */
    public static DefaultListItem create(final TemplateElement... content) {
        return new DefaultListItem(content);
    }

    private DefaultListItem(final List<TemplateElement> content) {
        super(content);
    }

    private DefaultListItem(final TemplateElement... content) {
        super(content);
    }
}
