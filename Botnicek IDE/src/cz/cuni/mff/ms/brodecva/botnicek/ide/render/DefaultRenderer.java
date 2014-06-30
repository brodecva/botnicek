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
package cz.cuni.mff.ms.brodecva.botnicek.ide.render;

import java.net.URI;
import java.util.List;
import java.util.Map;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.Element;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.root.Aiml;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.root.Toplevel;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public class DefaultRenderer implements Renderer {

    private final RenderingVisitorFactory renderingVisitorFactory;
    
    private final Map<URI, String> namespacesToPrefixes;
    
    public static Renderer create(final RenderingVisitorFactory visitorFactory, final Map<URI, String> namespacesToPrefixes) {
        return new DefaultRenderer(visitorFactory, namespacesToPrefixes);
    }
    
    /**
     * 
     */
    private DefaultRenderer(final RenderingVisitorFactory visitorFactory, final Map<URI, String> namespacesToPrefixes) {
        Preconditions.checkNotNull(visitorFactory);
        Preconditions.checkNotNull(namespacesToPrefixes);
        
        this.renderingVisitorFactory = visitorFactory;
        this.namespacesToPrefixes = namespacesToPrefixes;
    }

    public String render(final Element element) {
        Preconditions.checkNotNull(element);
        
        final RenderingVisitor visitor = this.renderingVisitorFactory.spawn(this.namespacesToPrefixes);
        element.accept(visitor);
        
        return visitor.getResult();
    }
    
    @Override
    public String render(final List<? extends Toplevel> documentContents) {
        Preconditions.checkNotNull(documentContents);
        
        final Aiml document = Aiml.create(documentContents, this.namespacesToPrefixes);
        
        return render(document);
    }
}
