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

import java.util.List;
import java.util.Set;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.AbstractProperElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.Element;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.ConditionElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.lists.DefaultListItem;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.lists.ValueOnlyListItem;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.Attribute;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.AttributeImplementation;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;

/**
 * Podmínka, který testuje hodnotu predikátu oproti vzorům vnořených položek.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see <a
 *      href="http://www.alicebot.org/TR/2011/#section-conditional-elements">http://www.alicebot.org/TR/2011/#section-conditional-elements</a>
 */
public final class SinglePredicateCondition extends AbstractProperElement
        implements ConditionElement {
    private static final String NAME = "condition";

    private static final String ATT_NAME = "name";

    /**
     * Vytvoří prvek.
     * 
     * @param name
     *            název testovaného predikátu
     * @param defaultItem
     *            výchozí položka, jejíž obsah bude zpracování v případě, že
     *            neuspěje žádná s hodnotou
     * @param items
     *            položky se vzory pro hodnotu testovaného predikátu
     * @return prvek
     */
    public static SinglePredicateCondition create(final NormalWord name,
            final DefaultListItem defaultItem,
            final List<? extends ValueOnlyListItem> items) {
        Preconditions.checkNotNull(defaultItem);

        return new SinglePredicateCondition(name, Optional.of(defaultItem),
                items);
    }

    /**
     * Vytvoří prvek.
     * 
     * @param name
     *            název testovaného predikátu
     * @param defaultItem
     *            výchozí položka, jejíž obsah bude zpracování v případě, že
     *            neuspěje žádná s hodnotou
     * @param items
     *            položky se vzory pro hodnotu testovaného predikátu
     * @return prvek
     */
    public static SinglePredicateCondition
            create(final NormalWord name, final DefaultListItem defaultItem,
                    final ValueOnlyListItem... items) {
        Preconditions.checkNotNull(items);

        return create(name, defaultItem, ImmutableList.copyOf(items));
    }

    /**
     * Vytvoří prvek.
     * 
     * @param name
     *            název testovaného predikátu
     * @param items
     *            položky se vzory pro hodnotu testovaného predikátu
     * @return prvek
     */
    public static SinglePredicateCondition create(final NormalWord name,
            final List<? extends ValueOnlyListItem> items) {
        return new SinglePredicateCondition(name,
                Optional.<DefaultListItem> absent(), items);
    }

    /**
     * Vytvoří prvek.
     * 
     * @param name
     *            název testovaného predikátu
     * @param items
     *            položky se vzory pro hodnotu testovaného predikátu
     * @return prvek
     */
    public static SinglePredicateCondition create(final NormalWord name,
            final ValueOnlyListItem... items) {
        Preconditions.checkNotNull(items);

        return create(name, ImmutableList.copyOf(items));
    }

    private final NormalWord name;

    private final List<ValueOnlyListItem> items;

    private final Optional<DefaultListItem> defaultItem;

    private SinglePredicateCondition(final NormalWord name,
            final Optional<DefaultListItem> defaultItem,
            final List<? extends ValueOnlyListItem> items) {
        Preconditions.checkNotNull(name);
        Preconditions.checkNotNull(items);
        Preconditions.checkNotNull(defaultItem);

        this.items = ImmutableList.copyOf(items);
        this.name = name;
        this.defaultItem = defaultItem;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.AbstractElement#
     * getAttributes()
     */
    @Override
    public Set<Attribute> getAttributes() {
        return ImmutableSet.<Attribute> of(AttributeImplementation.create(
                ATT_NAME, this.name.getText()));
    }

    /**
     * Vrátí výchozí položku, pokud je obsažena, jinak {@code null}.
     * 
     * @return výchozí položka
     */
    public DefaultListItem getDefaultItem() {
        return this.defaultItem.orNull();
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.elements.
     * AbstractElement#getChildren()
     */
    @Override
    public List<Element> getChildren() {
        final ImmutableList.Builder<Element> resultBuilder =
                ImmutableList.builder();

        resultBuilder.addAll(this.items);
        if (this.defaultItem.isPresent()) {
            resultBuilder.add(this.defaultItem.get());
        }

        return resultBuilder.build();
    }

    /**
     * Vrátí obsažené položky se vzorem.
     * 
     * @return obsažené položky se vzorem
     */
    public List<ValueOnlyListItem> getItems() {
        return this.items;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.elements.
     * AbstractElement#getName()
     */
    @Override
    public String getLocalName() {
        return NAME;
    }

    /**
     * Vrátí název testovaného predikátu.
     * 
     * @return název testovaného predikátu
     */
    public NormalWord getName() {
        return this.name;
    }
}
