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
package cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.rendering;

import java.util.Collection;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.api.Visitor;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.elements.AbstractProperElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.elements.AbstractElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.elements.AbstractRawElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.types.Attribute;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.XML;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public class RenderingVisitor implements Visitor {

    private final StringBuilder output = new StringBuilder();
    private final Optional<String> prefix;
    
    public static RenderingVisitor create() {
        return new RenderingVisitor(Optional.<String>absent());
    }
    
    public static RenderingVisitor create(final String prefix) {
        return new RenderingVisitor(Optional.of(prefix));
    }
    
    private RenderingVisitor(final Optional<String> prefix) {
        Preconditions.checkNotNull(prefix);
        
        this.prefix = prefix;
    }
    
    @Override
    public void visitEnter(final AbstractProperElement element) {
        this.output.append(XML.TAG_START);
        
        if (this.prefix.isPresent()) {
            this.output.append(this.prefix.get());
            this.output.append(XML.PREFIX_DELIMITER);
        }
        this.output.append(element.getName());
        
        renderAttributes(element.getAttributes());
        
        if (!element.hasChildren()) {
            this.output.append(XML.TAG_END);
        }
    }

    /**
     * @param attributes
     * @return
     */
    private void renderAttributes(final Collection<Attribute> attributes) {
        for (final Attribute attribute : attributes) {
            this.output.append(XML.SPACE);
            this.output.append(attribute.getName());
            this.output.append(XML.EQ_SIGN);
            this.output.append(XML.QUOTE);
            this.output.append(attribute.getValue());
            this.output.append(XML.QUOTE);
        }
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.api.Visitor#visitExit(cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.AbstractElement)
     */
    @Override
    public void visitExit(final AbstractProperElement element) {
        if (element.hasChildren()) {
            this.output.append(XML.EMPTY_TAG_END);
        } else {
            this.output.append(XML.CLOSING_TAG_START);
            this.output.append(element.getName());
            this.output.append(XML.TAG_END);
        }
    }
    
    String getResult() {
        return this.output.toString();
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.api.Visitor#visitEnter(cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.elements.AbstractRawElement)
     */
    @Override
    public void visitEnter(final AbstractRawElement element) {
        output.append(element.getText());
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.api.Visitor#visitExit(cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.elements.AbstractRawElement)
     */
    @Override
    public void visitExit(final AbstractRawElement element) {
    }
}
