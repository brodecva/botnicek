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
import java.util.LinkedList;
import java.util.List;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.AbstractProperElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.CompoundElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.ConditionElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.TemplateElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.lists.DefaultListItem;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.lists.ListItem;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.lists.ValueOnlyListItem;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public final class SinglePredicateCondition extends AbstractProperElement implements ConditionElement {
    private static final String NAME = "condition";
    
    private final NormalWord name;
    private final List<ValueOnlyListItem> items;
    private final Optional<DefaultListItem> defaultItem;
    
    public static SinglePredicateCondition of(final NormalWord name, final ListItem... items) {
        return of(name, Arrays.asList(items));
    }
    
    public static SinglePredicateCondition of(final NormalWord name, final List<ListItem> items) {
        Preconditions.checkNotNull(items);
        
        final Builder<ValueOnlyListItem> listBuilder = ImmutableList.builder();
        Optional<DefaultListItem> defaultItem = Optional.absent();
        for (final ListItem item : items) {
            Preconditions.checkNotNull(item);
            if (item instanceof DefaultListItem) {
                if (defaultItem.isPresent()) {
                    throw new IllegalArgumentException();
                }
                
                defaultItem = Optional.of((DefaultListItem) item);
            } else if (item instanceof ValueOnlyListItem) {
                listBuilder.add((ValueOnlyListItem) item);
            } else throw new IllegalArgumentException();
        }
        
        return new SinglePredicateCondition(name, defaultItem, listBuilder.build());
    }
    
    public static SinglePredicateCondition create(final NormalWord name, final DefaultListItem defaultItem, final ValueOnlyListItem... items) {
        Preconditions.checkNotNull(items);
        
        return create(name, defaultItem, Arrays.asList(items));
    }
    
    public static SinglePredicateCondition create(final NormalWord name, final ValueOnlyListItem... items) {
        Preconditions.checkNotNull(items);
        
        return create(name, Arrays.asList(items));
    }
    
    public static SinglePredicateCondition create(final NormalWord name, final DefaultListItem defaultItem, final List<ValueOnlyListItem> items) {
        Preconditions.checkNotNull(defaultItem);
        
        return new SinglePredicateCondition(name, Optional.of(defaultItem), items);
    }
    
    public static SinglePredicateCondition create(final NormalWord name, final List<ValueOnlyListItem> items) {
        return new SinglePredicateCondition(name, Optional.<DefaultListItem>absent(), items);
    }
    
    private SinglePredicateCondition(final NormalWord name, final Optional<DefaultListItem> defaultItem, final List<ValueOnlyListItem> items) {
        Preconditions.checkNotNull(name);
        Preconditions.checkNotNull(items);
        Preconditions.checkNotNull(defaultItem);
        
        this.items = ImmutableList.copyOf(items);
        this.name = name;
        this.defaultItem = defaultItem;
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.elements.AbstractElement#getName()
     */
    @Override
    public String getName() {
        return NAME;
    }
}
