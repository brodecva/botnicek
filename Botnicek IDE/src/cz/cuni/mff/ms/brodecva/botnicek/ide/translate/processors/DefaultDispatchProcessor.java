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
package cz.cuni.mff.ms.brodecva.botnicek.ide.translate.processors;

import java.util.Comparator;
import java.util.List;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
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
import cz.cuni.mff.ms.brodecva.botnicek.ide.translate.Stack;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.AIML;

/**
 * Výchozí implementace procesoru, která vytváří kód pro (ne)deterministický přechod do dalších stavů.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class DefaultDispatchProcessor implements DispatchProcessor<List<TemplateElement>> {
    
    private static final class PrioritizedFirstComparator implements Comparator<Arc> {
        @Override
        public int compare(final Arc first, final Arc second) {
            return -Integer.compare(first.getPriority().getValue(), second.getPriority().getValue());
        }
    }
    
    private final NormalWord randomizeState;

    /**
     * Vytvoří procesor, který vytváří kód pro (ne)deterministický přechod do dalších stavů. 
     * 
     * @param randomizeState stav pro zamíchání stavů při náhodném výběru
     * @return nový procesor
     */
    public static DefaultDispatchProcessor create(final NormalWord randomizeState) {
        Preconditions.checkNotNull(randomizeState);
        
        return new DefaultDispatchProcessor(randomizeState);
    }
    
    private DefaultDispatchProcessor(final NormalWord randomizeState) {
        this.randomizeState = randomizeState;
    }

    /**
     * {@inheritDoc}
     * 
     * <p>Uzel s uspořádaným další výběrem je interpretován tak, že se při zpracování seřadí hrany podle priority od největší po nejmenší a zařadí v daném pořadí do zásobníku.</p>
     */
    public List<TemplateElement> process(final OrderedNode node) {
        final java.util.Set<Arc> targets = node.getOuts();
        final List<Arc> sortedTargets = Ordering.from(new PrioritizedFirstComparator()).sortedCopy(targets);
        final List<NormalWord> targetNames = extractNames(sortedTargets);
        
        return ImmutableList.<TemplateElement>of(Text.create(Stack.joinWithSpaces(targetNames)));
    }

    
    private static List<NormalWord> extractNames(final List<Arc> targets) {
        return ImmutableList.copyOf(Lists.transform(targets, new Function<Arc, NormalWord>() {
            public NormalWord apply(Arc input) {
                return input.getName();
            }
        }));
    }
    
    /**
     * {@inheritDoc}
     * 
     * <p>Náhodný uzel musí být za běhu interpretovaný tak, že vybere náhodně jednu z hran. Šance hrany na výběr při průchodu je ovlivněna její prioritou, která poměrně vůči ostatním odchozím hranám zvyšuje pravděpodobnost výběru.</p>
     */
    public List<TemplateElement> process(final RandomNode node) {
        Preconditions.checkNotNull(node);
        
        final java.util.Set<Arc> targets = node.getOuts();
        final String statesToRandomize = concatenateMultiplied(targets);
        
        // Vstup do knihovního tématu
        final Set enterRandomizerState = Set.create(NormalWords.of(AIML.TOPIC_PREDICATE.getValue()), Text.create(this.randomizeState.getText()));
        final Srai randomize = Srai.create(Text.create(this.randomizeState.getText() + AIML.WORD_DELIMITER), Text.create(statesToRandomize));
        
        return ImmutableList.<TemplateElement>of(enterRandomizerState, randomize);
    }

    /**
     * Prioritní hrany se násobí pro zvětšení jejich pravděpodobnosti výběru dle jejich významu.
     * 
     * @param targets odchozí hrany
     * @return konkatenace názvů odchozích hran, kde priorita odpovídá počtu kopií
     */
    private static String concatenateMultiplied(final java.util.Set<Arc> targets) {
        final StringBuilder pushedStates = new StringBuilder();
        for (final Arc target : targets) {
            final NormalWord name = target.getName();
            final int repetitions = target.getPriority().getValue();
            
            for (int copyIndex = 0; copyIndex < repetitions; copyIndex++) {
                pushedStates.append(name.getText() + AIML.WORD_DELIMITER.getValue());
            }
        }
        pushedStates.setLength(Math.max(0, pushedStates.length() - AIML.WORD_DELIMITER.getValue().length()));
        
        final String statesToRandomize = pushedStates.toString();
        return statesToRandomize;
    }

    /** 
     * {@inheritDoc}
     * 
     * <p>Výstupní uzel nemá žádné odchozí hrany, tedy neovlivňuje způsob dalšího postupu.</p>
     */
    public List<TemplateElement> process(final ExitNode node) {
        Preconditions.checkNotNull(node);
        
        return ImmutableList.of();
    }

    /** 
     * {@inheritDoc}
     * 
     * <p>Izolovaný uzel je nedosažitelný, tedy jím provedené úpravy zásobníku jsou irelevantní.</p>
     */
    public List<TemplateElement> process(final IsolatedNode node) {
        Preconditions.checkNotNull(node);
        
        return ImmutableList.of();
    }
}