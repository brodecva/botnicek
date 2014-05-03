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
package cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.elements.template.implementations;

import java.util.Set;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;

import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.elements.AbstractDoubleIndexedElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.elements.AbstractProperElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.elements.template.AtomicElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.types.Attribute;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.AIML;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.Index;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.TwoDimensionalIndex;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public class Input extends AbstractDoubleIndexedElement implements AtomicElement {
    private static final String NAME = "input";
    
    public static Input create() {
        return new Input();
    }
    
    public static Input create(final Index index) {
        return new Input(index);
    }
    
    public static Input create(final TwoDimensionalIndex index2d) {
        return new Input(index2d);
    }
    
    private Input() {
        super();
    }
    
    private Input(final Index index) {
        super(index);
    }
    
    private Input(final TwoDimensionalIndex index2d) {
        super(index2d);
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.elements.AbstractElement#getName()
     */
    @Override
    public String getName() {
        return NAME;
    }
}
