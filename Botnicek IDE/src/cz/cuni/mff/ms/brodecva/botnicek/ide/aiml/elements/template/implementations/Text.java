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

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.AbstractRawElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.TemplateText;

/**
 * Prostý text uvnitř šablony.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public class Text extends AbstractRawElement implements TemplateText {
    private static final String NAME = "text";

    /**
     * Vytvoří prvek.
     * 
     * @param content
     *            obsah
     * @return prvek
     */
    public static Text create(final String content) {
        return new Text(content);
    }

    /**
     * Vytvoří prvek s prázdným obsahem.
     * 
     * @return prvek
     */
    public static Text empty() {
        return new Text();
    }

    private final String content;

    private Text() {
        this("");
    }

    private Text(final String content) {
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
        return this.content;
    }
}
