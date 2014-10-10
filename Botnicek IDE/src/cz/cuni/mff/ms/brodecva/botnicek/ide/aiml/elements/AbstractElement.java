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

import java.util.List;
import java.util.Set;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.api.Visitor;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.Attribute;

/**
 * Pomocná abstraktní třída obecného prvku stromu jazyka AIML. Poskytuje výchozí
 * implementace příslušných metod rozhraní, které obvykle vrací prázdný obsah.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public abstract class AbstractElement implements Element {

    /**
     * Navštíví každý element.
     * 
     * @param elements
     *            elementy
     * @param visitor
     *            návštěvník
     */
    public static final void acceptForEach(
            final Iterable<? extends Element> elements, final Visitor visitor) {
        Preconditions.checkNotNull(elements);
        Preconditions.checkNotNull(visitor);

        for (final Element element : elements) {
            element.accept(visitor);
        }
    }

    /**
     * Konstruktor abstraktní verze nepřijímá žádné parametry, neboť jím
     * vytvořený prvek je zcela prázdný.
     */
    protected AbstractElement() {
    }

    /**
     * {@inheritDoc}
     * 
     * Dědící třídy mají zapovězeno měnit směr průchodu. Pro samotnou
     * implementaci návrhového vzoru musí předefinovat metody
     * {@link #visitEnter(Visitor)} a {@link #visitExit(Visitor)}.
     */
    @Override
    public final void accept(final Visitor visitor) {
        Preconditions.checkNotNull(visitor);

        visitEnter(visitor);

        final List<Element> children = getChildren();
        for (final Element child : children) {
            child.accept(visitor);
        }

        visitExit(visitor);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Attribute> getAttributes() {
        return ImmutableSet.of();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.elements.Element
     * #getChildren()
     */
    @Override
    public List<Element> getChildren() {
        return ImmutableList.of();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.elements.Element
     * #getName()
     */
    @Override
    public abstract String getLocalName();

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.elements.Element
     * #getText()
     */
    @Override
    public String getText() {
        return "";
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.elements.Element
     * #hasChildren()
     */
    @Override
    public boolean hasChildren() {
        return !getChildren().isEmpty();
    }

    /**
     * <p>
     * Metoda pro implementaci průchodu návštěvníka. Nutno předefinovat v
     * podtřídě s obsahem:
     * </p>
     * 
     * <pre>
     * super.visitEnter(visitor);
     * 
     * visitor.visitEnter(this);
     * </pre>
     * 
     * @param visitor
     *            návštěvník
     */
    protected void visitEnter(final Visitor visitor) {
        Preconditions.checkNotNull(visitor);
    }

    /**
     * <p>
     * Metoda pro implementaci průchodu návštěvníka. Nutno předefinovat v
     * podtřídě s obsahem:
     * </p>
     * 
     * <pre>
     * super.visitExit(visitor);
     * 
     * visitor.visitExit(this);
     * </pre>
     * 
     * @param visitor
     *            návštěvník
     */
    protected void visitExit(final Visitor visitor) {
        Preconditions.checkNotNull(visitor);
    }
}
