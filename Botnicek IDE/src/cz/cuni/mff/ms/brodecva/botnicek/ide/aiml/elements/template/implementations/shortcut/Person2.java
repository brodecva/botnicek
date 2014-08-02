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
package cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.implementations.shortcut;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.AbstractProperElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.ShortcutElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.implementations.Star;

/**
 * Zkratka za prvek {@link cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.implementations.Person2} obsahující prvek {@link Star}.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see <a href="http://www.alicebot.org/TR/2011/#section-short-cut-elements">http://www.alicebot.org/TR/2011/#section-short-cut-elements</a>
 */
public final class Person2 extends AbstractProperElement implements ShortcutElement {
    private static final String NAME = "person2";

    /**
     * Vytvoří prvek.
     * 
     * @return prvek
     */
    public static Person2 create() {
        return new Person2();
    }
    
    private Person2() {
        super();
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.elements.AbstractElement#getName()
     */
    @Override
    public String getLocalName() {
        return NAME;
    }
}
