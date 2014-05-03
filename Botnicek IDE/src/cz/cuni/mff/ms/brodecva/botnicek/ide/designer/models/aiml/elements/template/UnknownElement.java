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
package cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.elements.template;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.elements.AbstractProperElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.types.Attribute;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public class UnknownElement extends AbstractProperElement implements TemplateElement {

    private final String name;
    private final Set<Attribute> attributes;
    private final Optional<List<TemplateElement>> content;
    
    public static UnknownElement create(final String name) {
        return new UnknownElement(name, Collections.<Attribute>emptySet(), Optional.<List<TemplateElement>>absent());
    }
    
    public static UnknownElement create(final String name, final Set<Attribute> attributes) {
        return new UnknownElement(name, attributes, Optional.<List<TemplateElement>>absent());
    }
    
    public static UnknownElement create(final String name, Set<Attribute> attributes, final TemplateElement... content) {
        Preconditions.checkNotNull(content);
        
        return create(name, attributes, Arrays.asList(content));
    }
    
    public static UnknownElement create(final String name, Set<Attribute> attributes, final List<TemplateElement> content) {
        Preconditions.checkNotNull(content);
        
        return new UnknownElement(name, attributes, Optional.<List<TemplateElement>>of(content));
    }
    
    private UnknownElement(final String name, final Set<Attribute> attributes, final Optional<List<TemplateElement>> content) {
        Preconditions.checkNotNull(name);
        Preconditions.checkNotNull(attributes);
        Preconditions.checkNotNull(content);
        Preconditions.checkArgument(!name.isEmpty());
        //TODO: Validace name.
        
        this.name = name;
        this.attributes = ImmutableSet.copyOf(attributes);
        this.content = Optional.<List<TemplateElement>>of(ImmutableList.copyOf(content.get()));
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.elements.AbstractElement#getName()
     */
    @Override
    public String getName() {
        return this.name;
    }
}
