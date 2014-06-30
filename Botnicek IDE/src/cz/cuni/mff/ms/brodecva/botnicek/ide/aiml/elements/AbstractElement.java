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
package cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.api.Visitable;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.api.Visitor;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.Attribute;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.AttributeImplementation;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public abstract class AbstractElement implements Visitable, Element {

    /**
     * 
     */
    protected AbstractElement() {
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.api.Visitable#accept(cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.api.Visitor)
     */
    @Override
    public void accept(final Visitor visitor) {
        visitEnter(visitor);
        
        for (final Element child : getChildren()) {
            child.accept(visitor);
        }
        
        visitExit(visitor);
    }
    
    protected void visitEnter(final Visitor visitor) {
    }
    
    protected void visitExit(final Visitor visitor) {
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.elements.Element#getChildren()
     */
    @Override
    public List<Element> getChildren() {
        return Collections.emptyList();
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.elements.Element#getAttributes()
     */
    @Override
    public Set<Attribute> getAttributes() {
        return Collections.emptySet();
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.elements.Element#getName()
     */
    @Override
    public abstract String getName();

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.elements.Element#hasChildren()
     */
    @Override
    public boolean hasChildren() {
        return getChildren().isEmpty();
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.elements.Element#getText()
     */
    @Override
    public String getText() {
        return "";
    }
}
