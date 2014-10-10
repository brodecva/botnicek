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
package cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.AbstractRawElement;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.XML;

/**
 * CDATA sekce.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see <a
 *      href="http://www.alicebot.org/TR/2011/#section-cdata-sections">http://www.alicebot.org/TR/2011/#section-cdata-sections</a>
 */
public class Cdata extends AbstractRawElement implements TemplateElement {
    private static final String NAME = "cdata";

    /**
     * Vytvoří CDATA sekci v šabloně.
     * 
     * @param content
     *            obsah
     * @return CDATA
     */
    public static Cdata create(final String content) {
        return new Cdata(content);
    }

    private final String content;

    private Cdata(final String content) {
        Preconditions.checkNotNull(content);

        this.content = content;
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

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.elements.
     * AbstractElement#getText()
     */
    @Override
    public String getText() {
        return XML.CDATA_START + this.content + XML.CDATA_END;
    }
}
