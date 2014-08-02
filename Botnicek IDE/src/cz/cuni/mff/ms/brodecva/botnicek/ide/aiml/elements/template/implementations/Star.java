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

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.AbstractIndexedElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.AtomicElement;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.Index;

/**
 * Vrátí hodnotu zachycenou žolíkem ve vzoru pro uživatelský vstup kategorie.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see <a href="http://www.alicebot.org/TR/2011/#section-star">http://www.alicebot.org/TR/2011/#section-star</a>
 */
public class Star extends AbstractIndexedElement implements AtomicElement {
    private static final String NAME = "star";

    /**
     * Vytvoří prvek s implicitním indexem.
     * 
     * @return prvek
     */
    public static Star create() {
        return new Star();
    }
    
    /**
     * Vytvoří prvek.
     * 
     * @param index explicitní index
     * @return prvek
     */
    public static Star create(final Index index) {
        return new Star(index);
    }
    
    private Star() {
        super();
    }
    
    private Star(final Index index) {
        super(index);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.elements.AbstractElement#getName()
     */
    @Override
    public String getLocalName() {
        return NAME;
    }
}
