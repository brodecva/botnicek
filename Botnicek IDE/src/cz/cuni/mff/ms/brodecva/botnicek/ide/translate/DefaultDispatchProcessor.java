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
import java.util.LinkedList;
import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Ordering;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.TemplateElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.implementations.Set;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.implementations.Srai;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.implementations.Text;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWords;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.DispatchProcessor;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.ExitNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.IsolatedNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.OrderedNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.RandomNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.translate.utils.Stack;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.AIML;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public final class DefaultDispatchProcessor implements
        DispatchProcessor, TemplateElementsGenerator {
    private final NormalWord randomizeState;
    private final List<TemplateElement> oldStack;
    private final List<TemplateElement> code = new LinkedList<>();

    public static DefaultDispatchProcessor create(final NormalWord randomizeState, final List<TemplateElement> stack) {
        return new DefaultDispatchProcessor(randomizeState, stack);
    }
    
    private DefaultDispatchProcessor(final NormalWord randomizeState, final List<TemplateElement> stack) {
        Preconditions.checkNotNull(randomizeState);
        Preconditions.checkNotNull(stack);
        
        this.randomizeState = randomizeState;
        this.oldStack = ImmutableList.copyOf(stack);
    }
    
    public List<TemplateElement> getResult() {
        return new LinkedList<>(this.code);
    }

    @Override
    public void process(final OrderedNode node) {
        final java.util.Set<Arc> targets = node.getOuts();
        final List<Arc> sortedTargets = Ordering.from(new PrioritizedFirstComparator()).sortedCopy(targets);
        final List<String> targetNames = extractNames(sortedTargets);
        
        final String pushed = Stack.joinPushed(targetNames);
        final TemplateElement updateStack = Stack.update(pushed, this.oldStack);
        
        this.code.clear();
        this.code.add(updateStack);
    }

    private static List<String> extractNames(final List<Arc> targets) {
        final List<String> targetNames = new ArrayList<>(targets.size());
        for (final Arc target : targets) {
            targetNames.add(target.getName().getText());
        }
        
        return targetNames;
    }

    @Override
    public void process(final RandomNode node) {
        final java.util.Set<Arc> targets = node.getOuts();
        final String statesToRandomize = concatenateMultiplied(targets);
        
        final Set enterRandomizerState = Set.create(NormalWords.of(AIML.TOPIC.getValue()), Text.create(randomizeState.getText()));
        final Srai randomize = Srai.create(Text.create(statesToRandomize));
        
        final List<TemplateElement> pushed = new LinkedList<>();
        pushed.add(enterRandomizerState);
        pushed.add(randomize);
        pushed.add(Text.create(AIML.WORD_DELIMITER.getValue()));
        
        final TemplateElement updateStack = Stack.update(pushed, this.oldStack);
        
        this.code.clear();
        this.code.add(updateStack);
    }

    private static String concatenateMultiplied(final java.util.Set<Arc> targets) {
        final StringBuilder pushedStates = new StringBuilder();
        for (final Arc target : targets) {
            final NormalWord name = target.getName();
            final int repetitions = target.getPriority();
            
            // Prioritní uzly se násobí pro zvětšení jejich pravděpodobnosti výběru.
            for (int copyIndex = 0; copyIndex < repetitions; copyIndex++) {
                pushedStates.append(name.getText() + AIML.WORD_DELIMITER.getValue());
            }
        }
        pushedStates.setLength(Math.max(0, pushedStates.length() - AIML.WORD_DELIMITER.getValue().length()));
        
        final String statesToRandomize = pushedStates.toString();
        return statesToRandomize;
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.DispatchProcessor#dispatch(cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.nodes.ExitNode)
     */
    @Override
    public void process(final ExitNode node) {
        final TemplateElement updateStack = Stack.set(this.oldStack);
        
        this.code.clear();
        this.code.add(updateStack);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.api.DispatchProcessor#process(cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.nodes.implementations.IsolatedProcessingNode)
     */
    @Override
    public void process(final IsolatedNode node) {
        this.code.clear();
    }
}