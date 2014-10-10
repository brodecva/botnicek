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
package cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.implementations;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.AbstractProperElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.SystemDefinedElement;

/**
 * Vrátí počet načtených kategorií.
 * 
 * @author Václav Brodec
 * @version 1.0
 * 
 * @see <a
 *      href="http://www.alicebot.org/TR/2011/#section-system-defined-predicates">http://www.alicebot.org/TR/2011/#section-system-defined-predicates</a>
 */
public class Size extends AbstractProperElement implements SystemDefinedElement {
    private static final String NAME = "size";

    /**
     * Vytvoří prvek.
     * 
     * @return prvek
     */
    public static Size create() {
        return new Size();
    }

    private Size() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.elements.
     * AbstractElement#getName()
     */
    @Override
    public String getLocalName() {
        return NAME;
    }
}
