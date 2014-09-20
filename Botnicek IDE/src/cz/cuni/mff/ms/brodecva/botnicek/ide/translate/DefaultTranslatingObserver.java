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
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.TemplateElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.implementations.Text;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.toplevel.Topic;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.DispatchProcessor;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.ProceedProcessor;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.StackProcessor;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.TestProcessor;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.dfs.AbstractDfsObserver;
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
 * <p>Užívá několika speciálních stavů, které pomáhají simulovat chod systému ATN, především rekurzivní zanořování, testování predikátů a nedeterminismus.</p>
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class DefaultTranslatingObserver extends AbstractDfsObserver implements TranslatingObserver {
    
    private final NodeTopicFactory nodeTopicFactory;
    private final StackProcessor<List<TemplateElement>> stackProcessor;
    private final DispatchProcessor<List<TemplateElement>> dispatchProcessor;
    private final ProceedProcessor<List<TemplateElement>> proceedProcessor;
    private final TestProcessor<List<Topic>> testProcessor;
    
    private final ListMultimap<Network, Topic> units = ArrayListMultimap.create();
    
    private Optional<Network> current = Optional.absent();
    
    /**
     * Vytvoří překladač s výchozími procesory.
     * 
     * @param pullState slovo popisující stav, který je vložen na zásobník po průchodu sítí až do koncového uzlu, a spustí tak proceduru uvolňování nezpracovaných stavů z něj 
     * @param pullStopState slovo popisující stav, který slouží jako zarážka při uvolňování nezpracovaných stavů úspěšně prošlé sítě ze zásobníku
     * @param randomizeState slovo popisující stav, který provede zamíchá přechody dle jejich priority před vložením na zásobník
     * @param successState slovo popisující stav, který indikuje úspěšné projití podsítě odkazované z následujícího stavu na zásobníku
     * @param returnState slovo popisující stav, který indikuje zpracování podsítě
     * @param testingPredicate rezervovaný název predikátu sloužící pro interní testy
     * @return překladač
     */
    public static DefaultTranslatingObserver create(final NormalWord pullState, final NormalWord pullStopState,
            final NormalWord randomizeState, NormalWord successState, NormalWord returnState, final NormalWord testingPredicate) {
        Preconditions.checkNotNull(pullState);
        Preconditions.checkNotNull(pullStopState);
        Preconditions.checkNotNull(randomizeState);
        Preconditions.checkNotNull(successState);
        Preconditions.checkNotNull(returnState);
        Preconditions.checkNotNull(testingPredicate);
        Preconditions.checkArgument(Comparisons.allDifferent(pullState, pullStopState, randomizeState, successState, returnState));
        
        return new DefaultTranslatingObserver(
                DefaultNodeTopicFactory.create(),
                DefaultStackProcessor.create(pullState, pullStopState),
                DefaultDispatchProcessor.create(randomizeState),
                DefaultProceedProcessor.create(),
                DefaultTestProcessor.create(testingPredicate, successState, returnState)
        );
    }
    
    /**
     * Vytvoří překladač.
     * 
     * @param nodeTopicFactory továrna na témata reprezentující uzel
     * @param stackProcessor instance {@link StackProcessor}
     * @param dispatchProcessor instance {@link DispatchProcessor}
     * @param proceedProcessor instance {@link ProceedProcessor}
     * @param testProcessor instance {@link TestProcessor} 
     * @return překladač
     */
    public static DefaultTranslatingObserver create(
            final NodeTopicFactory nodeTopicFactory,
            final StackProcessor<List<TemplateElement>> stackProcessor,
            final DispatchProcessor<List<TemplateElement>> dispatchProcessor,
            final ProceedProcessor<List<TemplateElement>> proceedProcessor,
            final TestProcessor<List<Topic>> testProcessor) {
        Preconditions.checkNotNull(stackProcessor);
        Preconditions.checkNotNull(dispatchProcessor);
        Preconditions.checkNotNull(proceedProcessor);
        Preconditions.checkNotNull(testProcessor);
        
        return new DefaultTranslatingObserver(nodeTopicFactory, stackProcessor, dispatchProcessor, proceedProcessor, testProcessor);
    }
    
    private DefaultTranslatingObserver(final NodeTopicFactory nodeTopicFactory, final StackProcessor<List<TemplateElement>> stackProcessor,
            final DispatchProcessor<List<TemplateElement>> dispatchProcessor,
            final ProceedProcessor<List<TemplateElement>> proceedProcessor,
            final TestProcessor<List<Topic>> testProcessor) {
        this.nodeTopicFactory = nodeTopicFactory;
        this.stackProcessor = stackProcessor;
        this.dispatchProcessor = dispatchProcessor;
        this.proceedProcessor = proceedProcessor;
        this.testProcessor = testProcessor;
    }
    
    /**
     * {@inheritDoc}
     * 
     * <p>Systém nehraje při překladu žádnou roli, a tak je v této implementaci ignorován.</p>
     */
    public void notifyVisit(final System system) {
        Preconditions.checkNotNull(system);
    }

    /**
     * {@inheritDoc}
     * 
     * <p>Tato implementace pouze nastaví aktuálně zpracovanou síť. V případě, že její obsah nevygeneruje žádná témata, neobjeví se ve výsledku {@link #getResult()}.</p>
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
        Preconditions.checkNotNull(node);
        Preconditions.checkState(this.current.isPresent());
        
        final List<TemplateElement> stackProcessorResult = node.accept(this.stackProcessor);
        final List<TemplateElement> dispatchProcessorResult = node.accept(this.dispatchProcessor);
        final List<TemplateElement> proceedProcessorResult = node.accept(this.proceedProcessor);
        
        final ImmutableList.Builder<TemplateElement> codeBuilder = ImmutableList.builder();
        codeBuilder.add(pushToStack(stackProcessorResult, dispatchProcessorResult));
        codeBuilder.addAll(proceedProcessorResult);
        
        add(this.nodeTopicFactory.produce(node, codeBuilder.build()));
    }

    private TemplateElement pushToStack(
            final List<TemplateElement> stackProcessorResult,
            final List<TemplateElement> dispatchProcessorResult) {
        final Iterable<TemplateElement> pushed;
        if (!dispatchProcessorResult.isEmpty() && !stackProcessorResult.isEmpty()) {
            pushed = Iterables.<TemplateElement>concat(dispatchProcessorResult, ImmutableList.of(Text.create(AIML.WORD_DELIMITER.getValue())), stackProcessorResult);
        } else {
            pushed = Iterables.<TemplateElement>concat(dispatchProcessorResult, stackProcessorResult);
        }
        
        return Stack.popAndPush(ImmutableList.copyOf(pushed));
    }

    /**
     * {@inheritDoc}
     * 
     * <p>Překladač podle toho, o jaký typ hrany jde vygeneruje kód pro daný typ testu.</p>
     * <p>Hrana stejně jako uzel tvoří vlastní témata, v nichž se nachází určitě množství kategorií podle typu testu.</p>
     */
    @Override
    public void notifyExamination(final Arc arc) {
        Preconditions.checkNotNull(arc);
        Preconditions.checkState(this.current.isPresent());
        
        final List<Topic> testProcessorResult = arc.accept(this.testProcessor);
        
        add(testProcessorResult);
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
        assert this.current.isPresent();
        
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
