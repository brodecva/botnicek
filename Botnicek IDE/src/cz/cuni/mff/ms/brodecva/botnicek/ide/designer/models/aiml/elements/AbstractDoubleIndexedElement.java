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
package cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.elements;

import java.util.Set;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;

import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.types.Attribute;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.AIML;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.Index;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.TwoDimensionalIndex;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public abstract class AbstractDoubleIndexedElement extends AbstractProperElement {
    private static final String ATT_INDEX = "index";
    
    private final Optional<Index> index;
    private final Optional<TwoDimensionalIndex> index2d;
    
    protected AbstractDoubleIndexedElement() {
        this(Optional.<Index>absent(), Optional.<TwoDimensionalIndex>absent());
    }
    
    protected AbstractDoubleIndexedElement(final Index index) {
        this(Optional.of(index), Optional.<TwoDimensionalIndex>absent());
    }
    
    protected AbstractDoubleIndexedElement(final TwoDimensionalIndex index2d) {
        this(Optional.<Index>absent(), Optional.of(index2d));
    }
    
    private AbstractDoubleIndexedElement(final Optional<Index> index, final Optional<TwoDimensionalIndex> index2d) {
        Preconditions.checkNotNull(index);
        Preconditions.checkNotNull(index2d);
        Preconditions.checkArgument(!(this.index.isPresent() && this.index2d.isPresent()));
        
        this.index = index;
        this.index2d = index2d;
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.elements.AbstractElement#getAttributes()
     */
    @Override
    public Set<Attribute> getAttributes() {
        assert !(this.index.isPresent() && this.index2d.isPresent());
        
        if (this.index.isPresent()) {
            return ImmutableSet.of(Attribute.create(ATT_INDEX, String.valueOf(this.index.get().getValue())));
        } else if (this.index2d.isPresent()) {
            final TwoDimensionalIndex raw2dIndex = this.index2d.get();
            return ImmutableSet.of(Attribute.create(ATT_INDEX, String.format("%s" + AIML.INDICES_DELIMITER + "%s", raw2dIndex.getFirstValue(), raw2dIndex.getSecondValue())));
        } else return ImmutableSet.of();
    }
}
