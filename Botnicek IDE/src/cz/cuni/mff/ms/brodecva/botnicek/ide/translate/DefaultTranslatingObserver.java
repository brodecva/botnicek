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

import java.util.Collection;
import java.util.List;
import java.util.Map;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.ListMultimap;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.category.Template;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.TemplateElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.toplevel.Category;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.toplevel.Topic;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.Patterns;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System;
import cz.cuni.mff.ms.brodecva.botnicek.ide.translate.processors.DefaultDispatchProcessor;
import cz.cuni.mff.ms.brodecva.botnicek.ide.translate.processors.DefaultProceedProcessor;
import cz.cuni.mff.ms.brodecva.botnicek.ide.translate.processors.DefaultStackProcessor;
import cz.cuni.mff.ms.brodecva.botnicek.ide.translate.processors.DefaultTestProcessor;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.Comparisons;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.AIML;

/**
 * <p>Výchozí implementace překladače.</p>
 * <p>Užívá několika speciálních stavů, které pomáhají simulovat chod ATN, především rekurzivní zanořování, testování predikátů a nedeterminismus.</p>
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class DefaultTranslatingObserver implements TranslatingObserver {
    
    private final NormalWord pullState;
    private final NormalWord pullStopState;
    private final NormalWord randomizeState;
    private final NormalWord successState;
    private final NormalWord returnState;
    private final NormalWord testingPredicate;
    
    private final ListMultimap<Network, Topic> units = ArrayListMultimap.create();
    
    private Optional<Network> current;
    
    /**
     * Vytvoří překladač.
     * 
     * @param pullState slovo popisující stav, který je vložen na zásobník po průchodu sítí až do koncového uzlu, a spustí tak proceduru uvolňování nezpracovaných stavů z něj 
     * @param pullStopState slovo popisující stav, který slouží jako zarážka při uvolňování nezpracovaných stavů úspěšně prošlé sítě ze zásobníku
     * @param randomizeState slovo popisující stav, který provede zamíchá přechody dle jejich priority před vložením na zásobník
     * @param successState slovo popisující stav, který indikuje úspěšné projití podsítě odkazované z následujícího stavu na zásobníku
     * @param returnState slovo popisující stav, který indikuje zpracování podsítě
     * @param testingPredicate rezervovaný název predikátu sloužící pro interní testy
     * @return překladač
     */
    public static TranslatingObserver create(final NormalWord pullState, final NormalWord pullStopState,
            final NormalWord randomizeState, NormalWord successState, NormalWord returnState, final NormalWord testingPredicate) {
        Preconditions.checkNotNull(pullState);
        Preconditions.checkNotNull(pullStopState);
        Preconditions.checkNotNull(randomizeState);
        Preconditions.checkNotNull(successState);
        Preconditions.checkNotNull(returnState);
        Preconditions.checkNotNull(testingPredicate);
        Preconditions.checkArgument(Comparisons.allDifferent(pullState, pullStopState, randomizeState));
        
        return new DefaultTranslatingObserver(pullState, pullStopState, randomizeState, successState, returnState, testingPredicate, Optional.<Network>absent());
    }
    
    private DefaultTranslatingObserver(final NormalWord pullState, final NormalWord pullStopState,
            final NormalWord randomizeState, final NormalWord successState, final NormalWord returnState, final NormalWord testingPredicate, final Optional<Network> current) {
        this.current = current;
        this.pullState = pullState;
        this.pullStopState = pullStopState;
        this.randomizeState = randomizeState;
        this.successState = successState;
        this.returnState = returnState;
        this.testingPredicate = testingPredicate;
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.dfs.DfsObserver#notifyVisit(cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System)
     */
    public void notifyVisit(final System system) {
    }

    /**
     * {@inheritDoc}
     * 
     * <p>Tato implementace nastaví aktuálně zpracovanou síť.</p>
     */
    @Override
    public void notifyVisit(final Network network) {
        Preconditions.checkNotNull(network);
        
        this.current = Optional.of(network);
    }

    /** 
     * {@inheritDoc}
     * 
     * <p>Překladač na objevení uzlu reaguje tak, že jej nechá zpracovat třemi procesory, které mají na starost vygenerování kódu pro modifikaci zásobníku, způsob a volbu přechod do dalších uzlů sítě a konečně to, zda-li takový přechod proběhne ihned, či až jako reakce na uživatelský vstup.
     * <p>Co uzel, to téma s jedinou kategorií. Téma je označeno vzorem "Název_uzlu žolík", který je možné porovnat s aktuálním tématem a přejít tak v případě shody do tohoto uzlu. Kategorie projde vždy, neboť je označena jen žolíky.</p>
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
        
        final List<TemplateElement> code = ImmutableList.copyOf(Iterables.concat(dispatchProcessor.getResult(), proceedProcessor.getResult()));
        
        final Topic topic = createNodeTopic(node, code);
        add(topic);
    }

    private Topic
            createNodeTopic(final Node node, final List<TemplateElement> code) {
        final Topic topic =
                Topic.create(
                        Patterns.create(node.getName() + AIML.WORD_DELIMITER.getValue() + AIML.STAR.getValue()),
                        Category.createUniversal(Template.create(code)));
        return topic;
    }

    /**
     * {@inheritDoc}
     * 
     * <p>Překladač podle toho, o jaký typ hrany jde vygeneruje kód pro daný typ testu.</p>
     * <p>Hrana stejně jako uzel tvoří vlastní témata, v nichž se nachází určitě množství kategorií podle typu testu.</p>
     */
    @Override
    public void notifyExamination(final Arc arc) {
        final DefaultTestProcessor testProcessor = DefaultTestProcessor.create(this.testingPredicate, this.successState, this.returnState);
        arc.accept(testProcessor);
        
        final List<Topic> topics = testProcessor.getResult();
        add(topics);
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
    
    /**
     * Přidá témata do aktuální sítě.
     * 
     * @param added nové témata
     */
    private void add(final Topic... added) {
        add(ImmutableList.copyOf(added));
    }
    
    /**
     * Přidá témata do aktuální sítě.
     * 
     * @param added nové témata
     */
    private void add(final List<Topic> added) {
        if (!this.current.isPresent()) {
            throw new IllegalStateException();
        }
        
        final Network currentRaw = this.current.get();
        this.units.putAll(currentRaw, added);
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.translate.TranslatingObserver#getResult()
     */
    @Override
    public Map<Network, List<Topic>> getResult() {
        final Map<Network, Collection<Topic>> collectionResult = this.units.asMap();
        final Map<Network, ? extends Collection<Topic>> rawResult = collectionResult;
        
        @SuppressWarnings("unchecked")
        final Map<Network, List<Topic>> castResult = (Map<Network, List<Topic>>) rawResult; 
        return castResult;
    }
}
