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
package cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.translation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.elements.category.Template;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.elements.template.TemplateElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.elements.toplevel.Category;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.elements.toplevel.Topic;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.types.Patterns;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.types.PredicateName;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.Network;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.System;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.arcs.Arc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.nodes.Node;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.dfs.DfsObserver;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.translation.utils.Stack;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.AIML;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public class TranslatingObserver implements DfsObserver {
    
    private final String pullState;
    private final String pullStopState;
    private final String randomizeState;
    private final PredicateName testingPredicate;
    
    private final Map<Network, List<Topic>> units = new HashMap<>();
    private Optional<Network> current = Optional.absent();
    
    public static TranslatingObserver create(final String pullState, final String pullStopState,
            final PredicateName testingPredicate, final String randomizeState) {
        return new TranslatingObserver(pullState, pullStopState, testingPredicate, randomizeState, Optional.<Network>absent());
    }
    
    public static TranslatingObserver create(final String pullState, final String pullStopState,
            final PredicateName testingPredicate, final String randomizeState, final Network current) {
        return new TranslatingObserver(pullState, pullStopState, testingPredicate, randomizeState, Optional.of(current));
    }
    
    private TranslatingObserver(final String pullState, final String pullStopState,
            final PredicateName testingPredicate, final String randomizeState, final Optional<Network> current) {
        Preconditions.checkNotNull(pullState);
        Preconditions.checkNotNull(pullStopState);
        Preconditions.checkNotNull(testingPredicate);
        Preconditions.checkNotNull(randomizeState);
        Preconditions.checkNotNull(current);
        Preconditions.checkArgument(!pullState.isEmpty());
        Preconditions.checkArgument(!pullStopState.isEmpty());
        Preconditions.checkArgument(!randomizeState.isEmpty());
        Preconditions.checkArgument(Stack.allDifferent(pullState, pullStopState, testingPredicate.getValue(), randomizeState));
        
        this.current = current;
        this.pullState = pullState;
        this.pullStopState = pullStopState;
        this.testingPredicate = testingPredicate;
        this.randomizeState = randomizeState;
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.dfs.DfsObserver#notifyVisit(cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.System)
     */
    @Override
    public void notifyVisit(final System system) {
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.dfs.DfsObserver#notifyVisit(cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.Network)
     */
    @Override
    public void notifyVisit(final Network network) {
        Preconditions.checkNotNull(network);
        
        this.current = Optional.of(network);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.dfs.DfsObserver#notifyDiscovery(cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.Node)
     */
    public void notifyDiscovery(final Node node) {
        final DefaultStackProcessor stackProcessor = DefaultStackProcessor.create(this.pullState, this.pullStopState);
        node.accept(stackProcessor);
        
        final List<TemplateElement> processedStack = stackProcessor.getResult();
        
        final DefaultDispatchProcessor dispatchProcessor = DefaultDispatchProcessor.create(this.randomizeState, processedStack);
        node.accept(dispatchProcessor);
        
        final DefaultProceedProcessor proceedProcessor = DefaultProceedProcessor.create();
        node.accept(proceedProcessor);
        
        final List<TemplateElement> code = Lists.newArrayList(Iterables.concat(dispatchProcessor.getResult(), proceedProcessor.getResult()));
        
        final Topic topic =
                Topic.create(
                        Patterns.create(node.getName() + AIML.WORD_DELIMITER.getValue() + AIML.STAR.getValue()),
                        Lists.newArrayList(Category.create(
                                Patterns.createUniversal(),
                                Patterns.createUniversal(), Template.create(code))));
        add(topic);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.Visitor#visit(cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.Arc)
     */
    @Override
    public void notifyExamination(final Arc arc) {
        final DefaultTestProcessor testProcessor = DefaultTestProcessor.create(this.testingPredicate);
        arc.accept(testProcessor);
        
        final List<Category> categories = testProcessor.getResult();
        final Topic topic = Topic.create(Patterns.create(arc.getName() + AIML.WORD_DELIMITER.getValue() + AIML.STAR.getValue()), categories);
        add(topic);
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.Visitor#finish(cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.Node)
     */
    @Override
    public void notifyFinish(final Node node) {   
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.dfs.DfsObserver#notifyTree(cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.Arc)
     */
    @Override
    public void notifyTree(final Arc arc) {
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.Visitor#back(cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.Arc)
     */
    @Override
    public void notifyBack(final Arc arc) {
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.dfs.DfsObserver#notifyCross(cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.Arc)
     */
    @Override
    public void notifyCross(final Arc arc) {
    }
    
    private void add(final Topic added) {
        if (!this.current.isPresent()) {
            throw new IllegalStateException();
        }
        
        final Network currentRaw = this.current.get();
        final List<Topic> present = this.units.get(currentRaw);
        if (present == null) {
            this.units.put(currentRaw, new LinkedList<>(Arrays.asList(added)));
        } else {
           present.add(added);
        }
    }
    
    public Map<Network, List<Topic>> getResult() {
        final Map<Network, List<Topic>> result = Maps.newHashMapWithExpectedSize(this.units.size());
        
        for (final Entry<Network, List<Topic>> unit : this.units.entrySet()) {
            result.put(unit.getKey(), new ArrayList<>(unit.getValue()));
        }
        
        return result;
    }
}
