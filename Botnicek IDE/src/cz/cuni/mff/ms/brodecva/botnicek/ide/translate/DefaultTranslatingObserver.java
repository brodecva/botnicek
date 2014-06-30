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
package cz.cuni.mff.ms.brodecva.botnicek.ide.translate;

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

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.category.Template;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.TemplateElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.toplevel.Category;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.toplevel.Topic;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.Patterns;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.dfs.DfsObserver;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System;
import cz.cuni.mff.ms.brodecva.botnicek.ide.translate.utils.Stack;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.AIML;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public class DefaultTranslatingObserver implements TranslatingObserver {
    
    private final NormalWord pullState;
    private final NormalWord pullStopState;
    private final NormalWord randomizeState;
    private final NormalWord testingPredicate;
    
    private final Map<Network, List<Topic>> units = new HashMap<>();
    
    private Optional<Network> current;
    
    public static TranslatingObserver create(final NormalWord pullState, final NormalWord pullStopState,
            final NormalWord randomizeState, final NormalWord testingPredicate) {
        return new DefaultTranslatingObserver(pullState, pullStopState, randomizeState, testingPredicate, Optional.<Network>absent());
    }
    
    private DefaultTranslatingObserver(final NormalWord pullState, final NormalWord pullStopState,
            final NormalWord randomizeState, final NormalWord testingPredicate, final Optional<Network> current) {
        Preconditions.checkNotNull(pullState);
        Preconditions.checkNotNull(pullStopState);
        Preconditions.checkNotNull(testingPredicate);
        Preconditions.checkNotNull(randomizeState);
        Preconditions.checkNotNull(current);
        Preconditions.checkArgument(Stack.allDifferent(pullState, pullStopState, randomizeState));
        
        this.current = current;
        this.pullState = pullState;
        this.pullStopState = pullStopState;
        this.testingPredicate = testingPredicate;
        this.randomizeState = randomizeState;
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.dfs.DfsObserver#notifyVisit(cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System)
     */
    public void notifyVisit(final System system) {
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.dfs.DfsObserver#notifyVisit(cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network)
     */
    @Override
    public void notifyVisit(final Network network) {
        Preconditions.checkNotNull(network);
        
        this.current = Optional.of(network);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.dfs.DfsObserver#notifyDiscovery(cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node)
     */
    @Override
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
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.dfs.DfsObserver#notifyExamination(cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc)
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
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.dfs.DfsObserver#notifyFinish(cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node)
     */
    @Override
    public void notifyFinish(final Node node) {   
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.dfs.DfsObserver#notifyTree(cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc)
     */
    @Override
    public void notifyTree(final Arc arc) {
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.dfs.DfsObserver#notifyBack(cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc)
     */
    @Override
    public void notifyBack(final Arc arc) {
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.dfs.DfsObserver#notifyCross(cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc)
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
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.translate.TranslatingObserver#getResult()
     */
    @Override
    public Map<Network, List<Topic>> getResult() {
        final Map<Network, List<Topic>> result = Maps.newHashMapWithExpectedSize(this.units.size());
        
        for (final Entry<Network, List<Topic>> unit : this.units.entrySet()) {
            result.put(unit.getKey(), new ArrayList<>(unit.getValue()));
        }
        
        return result;
    }
}
