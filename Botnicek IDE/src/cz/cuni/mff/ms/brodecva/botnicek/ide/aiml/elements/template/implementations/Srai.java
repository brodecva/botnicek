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

import java.util.List;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.AbstractCompoundElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.TemplateElement;

/**
 * Vrátí výstup potomků k novém zpracování celým interpretačním procesem.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see <a
 *      href="http://www.alicebot.org/TR/2011/#section-srai">http://www.alicebot.org/TR/2011/#section-srai</a>
 */
public final class Srai extends AbstractCompoundElement implements
        TemplateElement {
    private static final String NAME = "srai";

    /**
     * Vytvoří prvek.
     * 
     * @param content
     *            potomci
     * @return prvek
     */
    public static Srai create(final List<? extends TemplateElement> content) {
        return new Srai(content);
    }

    /**
     * Vytvoří prvek.
     * 
     * @param content
     *            potomci
     * @return prvek
     */
    public static Srai create(final TemplateElement... content) {
        return new Srai(content);
    }

    private Srai(final List<? extends TemplateElement> content) {
        super(content);
    }

    private Srai(final TemplateElement... content) {
        super(content);
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
