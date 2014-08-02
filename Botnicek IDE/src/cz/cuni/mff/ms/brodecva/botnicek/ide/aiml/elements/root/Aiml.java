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
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.AbstractProperElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.Element;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.Attribute;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.AttributeImplementation;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.AIML;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.XML;

/**
 * <p>
 * Kořenový prvek AIML dokumentu.
 * </p>
 * <p>
 * Obsahuje buďto přímo kategorie či kategorie sdružené do témat.
 * </p>
 * <p>
 * Specifikuje schéma a obvykle též prostory jmen a případné prefixy v nich
 * obsažených prvků.
 * </p>
 * 
 * @version 1.0
 * @author Václav Brodec
 * @see <a
 *      href="http://www.alicebot.org/TR/2011/#section-aiml-element">http://www.alicebot.org/TR/2011/#section-aiml-element</a>
 */
public class Aiml extends AbstractProperElement {

    private static final String NAME = "aiml";

    private static final URI SCHEMA_NAMESPACE_URI = URI
            .create(XML.SCHEMA_NAMESPACE_URI.getValue());

    private static final URI AIML_NAMESPACE_URI = URI.create(AIML.NAMESPACE_URI
            .getValue());

    private static final URI DEFAULT_SCHEMA_URI = URI
            .create(AIML.BACKUP_SCHEMA_LOCATION.getValue());

    private static final URI XMLNS_NAMESPACE_URI = URI
            .create(XML.XMLNS_NAMESPACE.getValue());

    private final List<Toplevel> content;

    private final BiMap<URI, String> namespacesToPrefixes;
    private final URI schemaLocation;

    /**
     * Vytvoří kořenový prvek.
     * 
     * @param content
     *            obsah první úrovně
     * @param namespacesToPrefixes
     *            zobrazení prostorů jmen na prefixy XML
     * @return prvek
     */
    public static Aiml create(final List<? extends Toplevel> content,
            final Map<URI, String> namespacesToPrefixes) {
        return create(content, namespacesToPrefixes, DEFAULT_SCHEMA_URI);
    }

    /**
     * Vytvoří kořenový prvek.
     * 
     * @param content
     *            obsah první úrovně
     * @param namespacesToPrefixes
     *            zobrazení prostorů jmen na prefixy XML
     * @param schemaLocation
     *            URI schématu pro AIML
     * @return prvek
     */
    public static Aiml create(final List<? extends Toplevel> content,
            final Map<URI, String> namespacesToPrefixes,
            final URI schemaLocation) {
        Preconditions.checkNotNull(content);
        Preconditions.checkNotNull(namespacesToPrefixes);
        Preconditions.checkNotNull(schemaLocation);

        final List<Toplevel> contentCopy = ImmutableList.copyOf(content);
        final BiMap<URI, String> namespacesToPrefixesCopy =
                ImmutableBiMap.copyOf(namespacesToPrefixes);

        Preconditions.checkArgument(namespacesToPrefixesCopy
                .containsKey(AIML_NAMESPACE_URI));
        Preconditions.checkArgument(namespacesToPrefixesCopy
                .containsKey(SCHEMA_NAMESPACE_URI));

        return new Aiml(contentCopy, namespacesToPrefixesCopy, schemaLocation);
    }

    private Aiml(final List<Toplevel> content,
            final BiMap<URI, String> namespacesToPrefixes,
            final URI schemaLocation) {
        assert content != null;
        assert namespacesToPrefixes != null;
        assert schemaLocation != null;

        this.content = content;
        this.namespacesToPrefixes = namespacesToPrefixes;
        this.schemaLocation = schemaLocation;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.AbstractElement#getName
     * ()
     */
    @Override
    public String getLocalName() {
        return NAME;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.AbstractElement#
     * getChildren()
     */
    public List<Element> getChildren() {
        return ImmutableList.<Element> copyOf(this.content);
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.AbstractElement#
     * getAttributes()
     */
    @Override
    public Set<Attribute> getAttributes() {
        final Builder<Attribute> attributesBuilder = ImmutableSet.builder();

        addCustomNamespacesTo(attributesBuilder);
        addMandatoryNamespacesTo(attributesBuilder);

        return attributesBuilder.build();
    }

    private void addMandatoryNamespacesTo(
            final Builder<Attribute> attributesBuilder) {
        attributesBuilder.add(AttributeImplementation.create(
                AIML.ATT_VERSION.getValue(),
                AIML.IMPLEMENTED_VERSION.getValue()));
        attributesBuilder.add(AttributeImplementation.create(
                XML.SCHEMA_ATT.getValue(), this.schemaLocation.toString(),
                SCHEMA_NAMESPACE_URI));
    }

    private void addCustomNamespacesTo(
            final Builder<Attribute> attributesBuilder) {
        for (final Entry<URI, String> namespacePrefix : this.namespacesToPrefixes
                .entrySet()) {
            final URI namespace = namespacePrefix.getKey();
            final String prefix = namespacePrefix.getValue();

            attributesBuilder.add(AttributeImplementation.create(XML.XMLNS_ATT
                    + (prefix.isEmpty() ? "" : XML.PREFIX_SEPARATOR.getValue())
                    + prefix, namespace.toString(), XMLNS_NAMESPACE_URI));
        }
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Aiml [getName()=");
        builder.append(getLocalName());
        builder.append(", getChildren()=");
        builder.append(getChildren());
        builder.append(", getAttributes()=");
        builder.append(getAttributes());
        builder.append(", namespacesToPrefixes=");
        builder.append(namespacesToPrefixes);
        builder.append(", schemaLocation=");
        builder.append(schemaLocation);
        builder.append(", content=");
        builder.append(content);
        builder.append("]");
        return builder.toString();
    }
    
}
