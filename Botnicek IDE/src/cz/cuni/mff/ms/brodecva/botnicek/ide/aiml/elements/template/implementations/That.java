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

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.AbstractDoubleIndexedElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.AtomicElement;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.Index;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.TwoDimensionalIndex;

/**
 * Vrátí předchozí výstup robota určený první (kolikátý zpět) a druhou (pořadí
 * věty) položkou indexu.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see <a
 *      href="http://www.alicebot.org/TR/2011/#section-template-side-that">http://www.alicebot.org/TR/2011/#section-template-side-that</a>
 */
public class That extends AbstractDoubleIndexedElement implements AtomicElement {
    private static final String NAME = "that";

    /**
     * Vytvoří prvek s implicitním indexem.
     * 
     * @return prvek
     */
    public static That create() {
        return new That();
    }

    /**
     * Vytvoří prvek.
     * 
     * @param index
     *            částečně explicitní index
     * @return prvek
     */
    public static That create(final Index index) {
        return new That(index);
    }

    /**
     * Vytvoří prvek.
     * 
     * @param index2d
     *            explicitní index
     * @return prvek
     */
    public static That create(final TwoDimensionalIndex index2d) {
        return new That(index2d);
    }

    private That() {
        super();
    }

    private That(final Index index) {
        super(index);
    }

    private That(final TwoDimensionalIndex index2d) {
        super(index2d);
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
