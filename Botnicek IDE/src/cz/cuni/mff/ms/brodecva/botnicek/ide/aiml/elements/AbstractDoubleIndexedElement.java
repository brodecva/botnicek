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
import com.google.common.collect.ImmutableSet;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.Attribute;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.AttributeImplementation;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.AIML;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.Index;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.TwoDimensionalIndex;

/**
 * <p>Abstraktní třída prvku který má dvourozměrný kladný celočíselný index zapsaný v atributu index s oddělovačem čárka.</p>
 * <p>Tento typ indexu používají některé prvky (a příslušné implementace by měly dědit od této třídy), které odkazují na jiný text (část zachycené promluvy...).</p>
 * <p>Volitelně lze vynechat (i jen pouze druhou část), pak značí index s čísly 1 a 1 (resp. zadaná část a 1).</p>
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public abstract class AbstractDoubleIndexedElement extends AbstractProperElement {
    
    private static final String ATT_INDEX = "index";
    
    private final Optional<Index> index;
    private final Optional<TwoDimensionalIndex> index2d;
    
    /**
     * Vytvoří prvek s implicitním indexem 1,1.
     */
    protected AbstractDoubleIndexedElement() {
        this(Optional.<Index>absent(), Optional.<TwoDimensionalIndex>absent());
    }
    
    /**
     * Vytvoří prvek s indexem, jehož druhá část je implicitní.
     * 
     * @param index index
     */
    protected AbstractDoubleIndexedElement(final Index index) {
        this(Optional.of(index), Optional.<TwoDimensionalIndex>absent());
    }
    
    /**
     * Vytvoří prvek s explicitním indexem.
     * 
     * @param index2d dvourozměrný index
     */
    protected AbstractDoubleIndexedElement(final TwoDimensionalIndex index2d) {
        this(Optional.<Index>absent(), Optional.of(index2d));
    }
    
    private AbstractDoubleIndexedElement(final Optional<Index> index, final Optional<TwoDimensionalIndex> index2d) {
        assert index != null;
        assert index2d != null;
        assert !(this.index.isPresent() && this.index2d.isPresent());
        
        this.index = index;
        this.index2d = index2d;
    }
    
    /**
     * {@inheritDoc}
     * 
     * Poskytne jediný atribut s indexem v případě explicitního zadání.
     * V případě implicitně zadané druhé části bude index jednorozměrný.
     */
    @Override
    public Set<Attribute> getAttributes() {
        assert !(this.index.isPresent() && this.index2d.isPresent());
        
        if (this.index.isPresent()) {
            return ImmutableSet.<Attribute>of(AttributeImplementation.create(ATT_INDEX, String.valueOf(this.index.get().getValue())));
        } else if (this.index2d.isPresent()) {
            final TwoDimensionalIndex raw2dIndex = this.index2d.get();
            return ImmutableSet.<Attribute>of(AttributeImplementation.create(ATT_INDEX, String.format("%s" + AIML.INDICES_DELIMITER + "%s", raw2dIndex.getFirstValue(), raw2dIndex.getSecondValue())));
        } else return ImmutableSet.of();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "AbstractDoubleIndexedElement [index=" + index + ", index2d="
                + index2d + ", getClass()=" + getClass() + "]";
    }
}
