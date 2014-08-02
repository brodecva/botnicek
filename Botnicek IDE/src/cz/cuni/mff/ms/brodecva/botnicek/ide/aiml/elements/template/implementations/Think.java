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

import java.util.List;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.AbstractCompoundElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.CovertElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.TemplateElement;

/**
 * Skryje výstup vyprodukovaný potomky.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see <a href="http://www.alicebot.org/TR/2011/#section-think">http://www.alicebot.org/TR/2011/#section-think</a>
 */
public final class Think extends AbstractCompoundElement implements CovertElement {
    private static final String NAME = "think";
    
    /**
     * Vytvoří prvek.
     * 
     * @param content potomci
     * @return prvek
     */
    public static Think create(final TemplateElement... content) {
        return new Think(content);
    }
    
    /**
     * Vytvoří prvek.
     * 
     * @param content potomci
     * @return prvek
     */
    public static Think create(final List<TemplateElement> content) {
        return new Think(content);
    }
    
    private Think(final TemplateElement... content) {
        super(content);
    }
    
    private Think(final List<TemplateElement> content) {
        super(content);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.elements.AbstractElement#getName()
     */
    @Override
    public String getLocalName() {
        return NAME;
    }
}
