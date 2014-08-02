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
import java.util.Map;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.Element;

/**
 * Výchozí implementace generátoru zdrojového kódu.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class DefaultRenderer implements Renderer {

    private final RenderingVisitorFactory renderingVisitorFactory;
    
    private final Map<URI, String> namespacesToPrefixes;
    
    /**
     * Vytvoří generátor.
     * 
     * @param visitorFactory továrna na návštěvníky stromu dokumentu
     * @param namespacesToPrefixes nastavení prefixů pro prostory jmen
     * @return generátor
     */
    public static Renderer create(final RenderingVisitorFactory visitorFactory, final Map<URI, String> namespacesToPrefixes) {
        return new DefaultRenderer(visitorFactory, namespacesToPrefixes);
    }
    
    private DefaultRenderer(final RenderingVisitorFactory visitorFactory, final Map<URI, String> namespacesToPrefixes) {
        Preconditions.checkNotNull(visitorFactory);
        Preconditions.checkNotNull(namespacesToPrefixes);
        
        final Map<URI, String> namespacesToPrefixesCopy = ImmutableMap.copyOf(namespacesToPrefixes);
        
        this.renderingVisitorFactory = visitorFactory;
        this.namespacesToPrefixes = namespacesToPrefixesCopy;
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.render.Renderer#render(cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.Element)
     */
    public String render(final Element element) {
        Preconditions.checkNotNull(element);
        
        final RenderingVisitor visitor = this.renderingVisitorFactory.spawn(this.namespacesToPrefixes);
        element.accept(visitor);
        
        return visitor.getResult();
    }
}
