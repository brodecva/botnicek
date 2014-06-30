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
package cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.root;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.AbstractProperElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.Element;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.category.Pattern;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.category.That;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.Attribute;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.AttributeImplementation;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.AIML;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.XML;


/**
 * @author Václav Brodec
 * @version 1.0
 */
public class Aiml extends AbstractProperElement {
    private static final String NAME = "aiml";
    
    private final List<Toplevel> content;
    
    private final BiMap<URI, String> namespacesToPrefixes;
    private final URI schemaLocation;
    
    public static Aiml create(final List<? extends Toplevel> content, final Map<URI, String> namespacesToPrefixes) {
        return new Aiml(content, namespacesToPrefixes, URI.create(AIML.BACKUP_SCHEMA_LOCATION.getValue()));
    }
    
    private Aiml(final List<? extends Toplevel> content, final Map<URI, String> namespacesToPrefixes, final URI schemaLocation) {
        Preconditions.checkNotNull(content);
        Preconditions.checkNotNull(schemaLocation);
        Preconditions.checkNotNull(namespacesToPrefixes);
        Preconditions.checkArgument(namespacesToPrefixes.containsKey(URI.create(AIML.NAMESPACE_URI.getValue())));
        Preconditions.checkArgument(namespacesToPrefixes.containsKey(URI.create(XML.SCHEMA_NAMESPACE_URI.getValue())));
        
        this.content = ImmutableList.copyOf(content);
        this.namespacesToPrefixes = HashBiMap.create(namespacesToPrefixes);
        this.schemaLocation = schemaLocation;
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.AbstractElement#getName()
     */
    @Override
    public String getName() {
        return NAME;
    }
    
    public List<Element> getChildren() {
        return ImmutableList.<Element>copyOf(this.content);
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.AbstractElement#getAttributes()
     */
    @Override
    public Set<Attribute> getAttributes() {
        final Builder<Attribute> attributesBuilder = ImmutableSet.builder();
        
        for (final Entry<URI, String> namespacePrefix : this.namespacesToPrefixes.entrySet()) {
            final URI namespace = namespacePrefix.getKey();
            final String prefix = namespacePrefix.getValue();
            
            attributesBuilder.add(AttributeImplementation.create("xmlns" + (prefix.isEmpty() ? "" : ":") + prefix, namespace.toString(), URI.create(XML.XMLNS_NAMESPACE.getValue())));
        }
        
        attributesBuilder.add(AttributeImplementation.create(AIML.ATT_VERSION.getValue(), AIML.IMPLEMENTED_VERSION.getValue()));
        attributesBuilder.add(AttributeImplementation.create(XML.SCHEMA_ATT.getValue(), this.schemaLocation.toString(), URI.create(XML.SCHEMA_NAMESPACE_URI.getValue())));
        
        return attributesBuilder.build();
    }
}
