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
package cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.toplevel;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.AbstractElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.AbstractProperElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.Element;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.root.Toplevel;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.Attribute;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.AttributeImplementation;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.SimplePattern;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public final class Topic extends AbstractProperElement implements Toplevel {
    private static final String NAME = "topic";
    
    private static final String ATT_NAME = "name";
    
    private final SimplePattern name;
    
    private final List<Category> categories;
    
    public static Topic create(final SimplePattern name, final Category... categories) {
        return new Topic(name, categories);
    }
    
    public static Topic create(final SimplePattern name, final List<Category> categories) {
        return new Topic(name, categories);
    }
    
    private Topic(final SimplePattern name, final Category... categories) {
        this(name, Arrays.asList(categories));
    }
    
    private Topic(final SimplePattern name, final List<Category> categories) {
        Preconditions.checkNotNull(name);
        Preconditions.checkNotNull(categories);
        
        this.categories = ImmutableList.copyOf(categories);
        this.name = name;
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.AbstractElement#getName()
     */
    @Override
    public String getName() {
        return NAME;
    }
    
    @Override
    public List<Element> getChildren() {
        return ImmutableList.<Element>copyOf(this.categories);
    }
    
    public Set<Attribute> getAttributes() {
        return ImmutableSet.<Attribute>of(AttributeImplementation.create(ATT_NAME, this.name.getText()));
    }
}
