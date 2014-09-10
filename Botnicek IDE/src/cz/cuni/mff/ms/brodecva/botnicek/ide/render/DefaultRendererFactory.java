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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URI;
import java.util.Map;

import com.google.common.base.Preconditions;

/**
 * Továrna produkující výchozí implementaci generátoru kódu.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class DefaultRendererFactory implements RendererFactory, Serializable {

    private static final long serialVersionUID = 1L;
    
    private final RenderingVisitorFactory renderingVisitorFactory;
    
    /**
     * Vytvoří továrnu.
     * 
     * @param renderingVisitorFactory továrna na návštěvníky stromu dokumentu
     * @return továrna
     */
    public static DefaultRendererFactory create(final RenderingVisitorFactory renderingVisitorFactory) {
        return new DefaultRendererFactory(renderingVisitorFactory);
    }
    
    private DefaultRendererFactory(final RenderingVisitorFactory renderingVisitorFactory) {
        Preconditions.checkNotNull(renderingVisitorFactory);
        
        this.renderingVisitorFactory = renderingVisitorFactory;
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.render.RendererFactory#produce(java.util.Map)
     */
    @Override
    public Renderer produce(final Map<URI, String> namespacesToPrefixes) {
        Preconditions.checkNotNull(namespacesToPrefixes);
        
        return DefaultRenderer.create(this.renderingVisitorFactory, namespacesToPrefixes);
    }

    private void readObject(final ObjectInputStream objectInputStream)
            throws ClassNotFoundException, IOException {
        objectInputStream.defaultReadObject();

        Preconditions.checkNotNull(this.renderingVisitorFactory);
    }

    private void writeObject(final ObjectOutputStream objectOutputStream)
            throws IOException {
        objectOutputStream.defaultWriteObject();
    }
}
