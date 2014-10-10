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

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.AbstractProperElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.Element;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.ConditionElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.lists.DefaultListItem;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.lists.NameAndValueListItem;

/**
 * Podmínka, která testuje hodnoty predikátů oproti vzorům v každé z vnořených
 * položek.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see <a
 *      href="http://www.alicebot.org/TR/2011/#section-conditional-elements">http://www.alicebot.org/TR/2011/#section-conditional-elements</a>
 */
public final class MultiPredicateCondition extends AbstractProperElement
        implements ConditionElement {
    private static final String NAME = "condition";

    /**
     * Vytvoří prvek.
     * 
     * @param defaultItem
     *            výchozí položka, jejíž obsah bude zpracování v případě, že
     *            neuspěje žádná s predikátem a hodnotou
     * @param items
     *            položky s názvy testovaných predikátů vzory pro hodnotu
     *            predikátu
     * @return prvek
     */
    public static MultiPredicateCondition create(
            final DefaultListItem defaultItem,
            final List<NameAndValueListItem> items) {
        Preconditions.checkNotNull(defaultItem);

        return new MultiPredicateCondition(Optional.of(defaultItem), items);
    }

    /**
     * Vytvoří prvek.
     * 
     * @param defaultItem
     *            výchozí položka, jejíž obsah bude zpracování v případě, že
     *            neuspěje žádná s predikátem a hodnotou
     * @param items
     *            položky s názvy testovaných predikátů vzory pro hodnotu
     *            predikátu
     * @return prvek
     */
    public static MultiPredicateCondition create(
            final DefaultListItem defaultItem,
            final NameAndValueListItem... items) {
        Preconditions.checkNotNull(items);

        return new MultiPredicateCondition(Optional.of(defaultItem),
                ImmutableList.copyOf(items));
    }

    /**
     * Vytvoří prvek.
     * 
     * @param items
     *            položky s názvy testovaných predikátů vzory pro hodnotu
     *            predikátu
     * @return prvek
     */
    public static MultiPredicateCondition create(
            final List<NameAndValueListItem> items) {
        return new MultiPredicateCondition(Optional.<DefaultListItem> absent(),
                items);
    }

    /**
     * Vytvoří prvek.
     * 
     * @param items
     *            položky s názvy testovaných predikátů vzory pro hodnotu
     *            predikátu
     * @return prvek
     */
    public static MultiPredicateCondition create(
            final NameAndValueListItem... items) {
        Preconditions.checkNotNull(items);

        return create(ImmutableList.copyOf(items));
    }

    private final List<NameAndValueListItem> items;

    private final Optional<DefaultListItem> defaultItem;

    private MultiPredicateCondition(
            final Optional<DefaultListItem> defaultItem,
            final List<NameAndValueListItem> items) {
        Preconditions.checkNotNull(defaultItem);
        Preconditions.checkNotNull(items);

        this.items = ImmutableList.copyOf(items);
        this.defaultItem = defaultItem;
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
     * Vrátí obsažené položky s názvem a vzorem.
     * 
     * @return obsažené položky s názvem a vzorem
     */
    public List<NameAndValueListItem> getItems() {
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
}
