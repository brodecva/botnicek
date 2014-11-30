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
package cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements;

import java.util.Set;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.AtomicElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.Attribute;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.AttributeImplementation;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.Index;

/**
 * <p>
 * Abstraktní třída prvku který má jednorozměrný kladný celočíselný index
 * zapsaný v atributu index.
 * </p>
 * <p>
 * Tento typ indexu používají některé prvky (a příslušné implementace by měly
 * dědit od této třídy), které odkazují na jiný text (část zachycené
 * promluvy...).
 * </p>
 * <p>
 * Volitelně lze vynechat, pak značí index s číslem 1.
 * </p>
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public abstract class AbstractIndexedElement extends AbstractProperElement
        implements AtomicElement {

    private static final String ATT_INDEX = "index";

    private final Optional<Index> index;

    /**
     * Vytvoří prvek s implicitním indexem 1.
     */
    protected AbstractIndexedElement() {
        this(Optional.<Index> absent());
    }

    /**
     * Vytvoří prvek s explicitním indexem.
     * 
     * @param index
     *            index
     */
    protected AbstractIndexedElement(final Index index) {
        this(Optional.of(index));
    }

    private AbstractIndexedElement(final Optional<Index> index) {
        this.index = index;
    }

    /**
     * {@inheritDoc}
     * 
     * Poskytne jediný atribut s indexem v případě explicitního zadání.
     */
    @Override
    public Set<Attribute> getAttributes() {
        if (this.index.isPresent()) {
            return ImmutableSet.<Attribute> of(AttributeImplementation.create(
                    ATT_INDEX, String.valueOf(this.index.get().getValue())));
        } else {
            return ImmutableSet.of();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "AbstractIndexedElement [index=" + this.index + ", getClass()="
                + getClass() + "]";
    }
}
