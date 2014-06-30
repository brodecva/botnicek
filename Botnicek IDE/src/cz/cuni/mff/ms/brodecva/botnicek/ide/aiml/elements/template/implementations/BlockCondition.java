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
package cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.implementations;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.AbstractCompoundElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.ConditionElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.TemplateElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.Attribute;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.AttributeImplementation;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.SimplePattern;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public final class BlockCondition extends AbstractCompoundElement implements ConditionElement {
    private static final String NAME = "condition";
    private static final String ATT_NAME = "name";
    private static final String ATT_VALUE = "value";
    
    private final NormalWord name;
    private final SimplePattern value;
    
    public static BlockCondition create(final NormalWord name, final SimplePattern value, final TemplateElement... content) {
        return new BlockCondition(name, value, Arrays.asList(content));
    }
    
    public static BlockCondition create(final NormalWord name, final SimplePattern value, final List<TemplateElement> content) {
        return new BlockCondition(name, value, content);
    }
    
    private BlockCondition(final NormalWord name, final SimplePattern value, final List<TemplateElement> content) {
        super(content);
        
        Preconditions.checkNotNull(name);
        Preconditions.checkNotNull(value);
        
        this.name = name;
        this.value = value;
    }

    /**
     * @return the name
     */
    public String getName() {
        return NAME;
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.elements.AbstractElement#getAttributes()
     */
    @Override
    public Set<Attribute> getAttributes() {
        return ImmutableSet.<Attribute>of(AttributeImplementation.create(ATT_NAME, name.getText()), AttributeImplementation.create(ATT_VALUE, value.getText()));
    }
}
