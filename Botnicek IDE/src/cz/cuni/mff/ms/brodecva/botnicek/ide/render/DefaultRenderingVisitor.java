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
package cz.cuni.mff.ms.brodecva.botnicek.ide.render;

import java.net.URI;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.AbstractProperElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.AbstractRawElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.Attribute;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.Presence;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.AIML;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.XML;

/**
 * Výchozí implementace návštěvníka generujícího zdrojový kód AIML z objektového
 * modelu dokumentu.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class DefaultRenderingVisitor implements RenderingVisitor {

    /**
     * Vytvoří návštěvníka.
     * 
     * @param namespacesToPrefixes
     *            nastavení prefixů pro prostory jmen prvků
     * @return návštěvník
     */
    public static DefaultRenderingVisitor create(
            final Map<? extends URI, ? extends String> namespacesToPrefixes) {
        return new DefaultRenderingVisitor(namespacesToPrefixes);
    }

    private final StringBuilder output = new StringBuilder();

    private final Map<URI, String> namespacesToPrefixes;

    private DefaultRenderingVisitor(
            final Map<? extends URI, ? extends String> namespacesToPrefixes) {
        Preconditions.checkNotNull(namespacesToPrefixes);

        final ImmutableMap<URI, String> namespacesToPrefixesCopy =
                ImmutableMap.copyOf(namespacesToPrefixes);
        Preconditions.checkArgument(namespacesToPrefixesCopy.containsKey(URI
                .create(AIML.NAMESPACE_URI.getValue())));

        this.namespacesToPrefixes = namespacesToPrefixesCopy;
    }

    private void appendName(final AbstractProperElement element) {
        final String aimlPrefix =
                this.namespacesToPrefixes.get(URI.create(AIML.NAMESPACE_URI
                        .getValue()));
        assert Presence.isPresent(aimlPrefix);

        if (!aimlPrefix.isEmpty()) {
            this.output.append(aimlPrefix);
            this.output.append(XML.PREFIX_DELIMITER);
        }
        this.output.append(element.getLocalName());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.render.RenderingVisitor#getResult()
     */
    @Override
    public String getResult() {
        return this.output.toString();
    }

    private void renderAttributes(final Set<Attribute> attributes) {
        for (final Attribute attribute : attributes) {
            this.output.append(XML.SPACE);

            final URI namespace = attribute.getNamespace();
            if (Presence.isPresent(namespace)) {
                final String prefix = this.namespacesToPrefixes.get(namespace);
                Preconditions.checkArgument(Presence.isPresent(prefix));

                if (!prefix.isEmpty()) {
                    this.output.append(prefix);
                    this.output.append(XML.PREFIX_DELIMITER);
                }
            }

            this.output.append(attribute.getName());
            this.output.append(XML.EQ_SIGN);
            this.output.append(XML.QUOTE);
            this.output.append(attribute.getValue());
            this.output.append(XML.QUOTE);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * <p>
     * Sestaví textovou podobu otevírací značky prvku, včetně atributů.
     * </p>
     */
    @Override
    public void visitEnter(final AbstractProperElement element) {
        this.output.append(XML.TAG_START);

        appendName(element);

        renderAttributes(element.getAttributes());

        if (element.hasChildren()) {
            this.output.append(XML.TAG_END);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.api.Visitor
     * #visitEnter
     * (cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.elements
     * .AbstractRawElement)
     */
    @Override
    public void visitEnter(final AbstractRawElement element) {
        this.output.append(element.getText());
    }

    /**
     * {@inheritDoc}
     * 
     * <p>
     * Sestaví textovou podobu koncové značky prvku (zkrácené verze v případě,
     * že prvek nemá potomky).
     * </p>
     */
    @Override
    public void visitExit(final AbstractProperElement element) {
        if (element.hasChildren()) {
            this.output.append(XML.CLOSING_TAG_START);

            appendName(element);

            this.output.append(XML.TAG_END);
        } else {
            this.output.append(XML.EMPTY_TAG_END);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.api.Visitor
     * #visitExit
     * (cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.elements
     * .AbstractRawElement)
     */
    @Override
    public void visitExit(final AbstractRawElement element) {
    }
}
