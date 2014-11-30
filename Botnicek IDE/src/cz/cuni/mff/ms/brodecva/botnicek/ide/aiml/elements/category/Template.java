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
package cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.category;

import java.util.List;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.AbstractCompoundElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.TemplateElement;

/**
 * <p>
 * Prvek popisující šablonu svým obsahem.
 * </p>
 * <p>
 * Prvek je vždy obsažen v kategorii, společně se vzorem promluvy a pro zmínku.
 * </p>
 * 
 * @version 1.0
 * @author Václav Brodec
 * @see <a
 *      href="http://www.alicebot.org/TR/2011/#section-template">http://www.alicebot.org/TR/2011/#section-template</a>
 */
public class Template extends AbstractCompoundElement {

    private static final String NAME = "template";

    /**
     * Vytvoří prvek.
     * 
     * @param children
     *            potomci
     * @return prvek
     */
    public static Template create(final List<? extends TemplateElement> children) {
        Preconditions.checkNotNull(children);

        return new Template(children);
    }

    /**
     * Vytvoří prvek.
     * 
     * @param children
     *            potomci
     * @return prvek
     */
    public static Template create(final TemplateElement... children) {
        Preconditions.checkNotNull(children);

        return new Template(children);
    }

    private Template(final List<? extends TemplateElement> children) {
        super(children);
    }

    private Template(final TemplateElement... children) {
        super(children);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.AbstractElement
     * #getName()
     */
    @Override
    public String getLocalName() {
        return NAME;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Template [getName()=" + getLocalName() + ", getChildren()="
                + getChildren() + "]";
    }
}
