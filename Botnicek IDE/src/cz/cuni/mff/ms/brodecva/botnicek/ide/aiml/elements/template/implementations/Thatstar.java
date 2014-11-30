/**
 * Copyright Václav Brodec 2014.
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

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.AbstractIndexedElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.AtomicElement;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.Index;

/**
 * Vrátí hodnotu zachycenou žolíkem ve vzoru posledního výstupu robota.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see <a
 *      href="http://www.alicebot.org/TR/2011/#section-thatstar">http://www.alicebot.org/TR/2011/#section-thatstar</a>
 */
public class Thatstar extends AbstractIndexedElement implements AtomicElement {
    private static final String NAME = "thatstar";

    /**
     * Vytvoří prvek s implicitním indexem.
     * 
     * @return prvek
     */
    public static Thatstar create() {
        return new Thatstar();
    }

    /**
     * Vytvoří prvek.
     * 
     * @param index
     *            explicitní index
     * @return prvek
     */
    public static Thatstar create(final Index index) {
        return new Thatstar(index);
    }

    private Thatstar() {
        super();
    }

    private Thatstar(final Index index) {
        super(index);
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
