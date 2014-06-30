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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.ImmutableList.Builder;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.AbstractProperElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.Element;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.CompoundElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.ConditionElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.TemplateElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.lists.DefaultListItem;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.lists.ListItem;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.lists.NameAndValueListItem;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.AttributeImplementation;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public final class MultiPredicateCondition extends AbstractProperElement implements ConditionElement {
    private static final String NAME = "condition";
    
    private final List<NameAndValueListItem> items;
    private final Optional<DefaultListItem> defaultItem;
    
    public static MultiPredicateCondition of(ListItem... items) {
        return of(Arrays.asList(items));
    }
    
    public static MultiPredicateCondition of(List<ListItem> items) {
        Preconditions.checkNotNull(items);
        
        final Builder<NameAndValueListItem> listBuilder = ImmutableList.builder();
        Optional<DefaultListItem> defaultItem = Optional.absent();
        for (final ListItem item : items) {
            Preconditions.checkNotNull(item);
            if (item instanceof DefaultListItem) {
                if (defaultItem.isPresent()) {
                    throw new IllegalArgumentException();
                }
                
                defaultItem = Optional.of((DefaultListItem) item);
            } else if (item instanceof NameAndValueListItem) {
                listBuilder.add((NameAndValueListItem) item);
            } else throw new IllegalArgumentException();
        }
        
        return new MultiPredicateCondition(defaultItem, listBuilder.build());
    }
    
    public static MultiPredicateCondition create(final DefaultListItem defaultItem, final NameAndValueListItem... items) {
        Preconditions.checkNotNull(items);
        
        return new MultiPredicateCondition(Optional.of(defaultItem), Arrays.asList(items));
    }
    
    public static MultiPredicateCondition create(final NameAndValueListItem... items) {
        Preconditions.checkNotNull(items);
        
        return create(Arrays.asList(items));
    }
    
    public static MultiPredicateCondition create(final DefaultListItem defaultItem, final List<NameAndValueListItem> items) {
        Preconditions.checkNotNull(defaultItem);
        
        return new MultiPredicateCondition(Optional.of(defaultItem), items);
    }
    
    public static MultiPredicateCondition create(final List<NameAndValueListItem> items) {
        return new MultiPredicateCondition(Optional.<DefaultListItem>absent(), items);
    }
    
    private MultiPredicateCondition(final Optional<DefaultListItem> defaultItem, final List<NameAndValueListItem> items) {
        Preconditions.checkNotNull(defaultItem);
        Preconditions.checkNotNull(items);
        
        this.items = ImmutableList.copyOf(items);
        this.defaultItem = defaultItem;
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.elements.AbstractElement#getName()
     */
    @Override
    public String getName() {
        return NAME;
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.elements.AbstractElement#getChildren()
     */
    @Override
    public List<Element> getChildren() {
        final ImmutableList.Builder<Element> resultBuilder = ImmutableList.builder();
        
        resultBuilder.addAll(this.items);
        if (this.defaultItem.isPresent()) {
            resultBuilder.add(this.defaultItem.get());
        }
        
        return resultBuilder.build();
    }
}
