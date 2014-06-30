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
package cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.lists;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.TemplateElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.Attribute;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.AttributeImplementation;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public final class NameAndValueListItem extends AbstractListItem {
    private static final String ATT_NAME = null;
    private static final String ATT_VALUE = null;
    private final String name;
    private final String value;
    
    public static NameAndValueListItem create(final String name, final String value, final TemplateElement... content) {
        return new NameAndValueListItem(name, value, Arrays.asList(content));
    }
    
    public static NameAndValueListItem create(final String name, final String value, final List<TemplateElement> content) {
        return new NameAndValueListItem(name, value, content);
    }
    
    private NameAndValueListItem(final String name, final String value, final TemplateElement... content) {
        this(name, value, Arrays.asList(content));
    }
    
    private NameAndValueListItem(final String name, final String value, final List<TemplateElement> content) {
        super(content);
        
        Preconditions.checkNotNull(name);
        Preconditions.checkNotNull(value);
        Preconditions.checkArgument(!name.isEmpty());
        
        this.name = name;
        this.value = value;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.elements.AbstractElement#getAttributes()
     */
    @Override
    public Set<Attribute> getAttributes() {
        return ImmutableSet.<Attribute>of(AttributeImplementation.create(ATT_NAME, this.name), AttributeImplementation.create(ATT_VALUE, this.value));
    }
}
