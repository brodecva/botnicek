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

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.AbstractProperElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.AtomicElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.CaptureElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.TemplateElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.Attribute;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.AttributeImplementation;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public final class Get extends AbstractProperElement implements AtomicElement {
    private static final String NAME = "get";

    private static final String ATT_NAME = null;
    
    private final NormalWord name;
    
    public static Get create(final NormalWord name) {
        return new Get(name);
    }
    
    private Get(final NormalWord name) {
        Preconditions.checkNotNull(name);
        
        this.name = name;
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.elements.AbstractElement#getName()
     */
    @Override
    public String getName() {
        return NAME;
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.elements.AbstractElement#getAttributes()
     */
    @Override
    public Set<Attribute> getAttributes() {
        return ImmutableSet.<Attribute>of(AttributeImplementation.create(ATT_NAME, name.getText()));
    }
}