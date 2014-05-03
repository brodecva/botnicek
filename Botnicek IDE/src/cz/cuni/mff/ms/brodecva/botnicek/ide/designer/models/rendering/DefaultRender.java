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

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.elements.Aiml;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.elements.Element;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.elements.toplevel.Category;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.elements.toplevel.Topic;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public class DefaultRender implements Render {

    private final RenderingVisitorFactory renderingVisitorFactory;
    
    public DefaultRender create(final RenderingVisitorFactory factory) {
        return new DefaultRender(factory);
    }
    
    /**
     * 
     */
    private DefaultRender(final RenderingVisitorFactory factory) {
        Preconditions.checkNotNull(factory);
        
        this.renderingVisitorFactory = factory;
    }

    public String render(final Element element) {
        final RenderingVisitor visitor = this.renderingVisitorFactory.create();
        element.accept(visitor);
        
        return visitor.getResult();
    }
}
