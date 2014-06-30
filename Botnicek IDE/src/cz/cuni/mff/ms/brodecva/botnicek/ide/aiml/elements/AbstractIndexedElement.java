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
package cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements;

import java.util.Set;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.AbstractProperElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.AtomicElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.CaptureElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.TemplateElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.Attribute;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.AttributeImplementation;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.Index;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public abstract class AbstractIndexedElement extends AbstractProperElement implements AtomicElement {
    private static final String ATT_INDEX = "index";
    
    private final Optional<Index> index;
    
    protected AbstractIndexedElement() {
        this(Optional.<Index>absent());
    }
    
    protected AbstractIndexedElement(final Index index) {
        this(Optional.of(index));
    }
    
    private AbstractIndexedElement(final Optional<Index> index) {
        Preconditions.checkNotNull(index);
        
        this.index = index;
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.elements.AbstractElement#getAttributes()
     */
    @Override
    public Set<Attribute> getAttributes() {
        if (this.index.isPresent()) {
            return ImmutableSet.<Attribute>of(AttributeImplementation.create(ATT_INDEX, String.valueOf(this.index.get().getValue())));
        } else {
            return ImmutableSet.of();
        }
    }
}
