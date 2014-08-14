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
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.controllers;

import java.net.URI;
import java.util.Map;
import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.Code;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.MixedPattern;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.SimplePattern;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.code.model.builder.CodeContentBuilder;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.code.model.builder.CodeContentBuilderFactory;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.code.model.builder.DefaultCodeContentBuilderFactory;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.code.model.checker.CodeChecker;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.code.model.checker.DefaultCodeChecker;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.CheckResult;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.mixedpattern.model.builder.DefaultMixedPatternBuilderFactory;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.mixedpattern.model.builder.MixedPatternBuilder;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.mixedpattern.model.builder.MixedPatternBuilderFactory;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.mixedpattern.model.checker.DefaultMixedPatternChecker;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.mixedpattern.model.checker.MixedPatternChecker;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.simplepattern.model.builder.DefaultSimplePatternBuilderFactory;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.simplepattern.model.builder.SimplePatternBuilder;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.simplepattern.model.builder.SimplePatternBuilderFactory;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.simplepattern.model.checker.DefaultSimplePatternChecker;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.simplepattern.model.checker.SimplePatternChecker;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.words.model.builder.DefaultNormalWordBuilderFactory;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.words.model.builder.NormalWordBuilder;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.words.model.builder.NormalWordBuilderFactory;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.words.model.checker.DefaultNormalWordChecker;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.words.model.checker.NormalWordChecker;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.events.ArcChangedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.events.ArcChangedListener;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.events.ArcRemovedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.events.ArcRemovedListener;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.events.FromRenamedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.events.FromRenamedListener;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.events.ToRenamedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.events.ToRenamedListener;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.CodeTestArc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.PatternArc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.PredicateTestArc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.RecurentArc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.TransitionArc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.api.Processor;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.properties.ArcView;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.EnterNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.types.Priority;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Callback;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.EventManager;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.mvc.AbstractController;
import cz.cuni.mff.ms.brodecva.botnicek.library.api.BotConfiguration;
import cz.cuni.mff.ms.brodecva.botnicek.library.api.LanguageConfiguration;

/**
 * Výchozí řadič vlastností hrany provádí přes samotnou aktualizací modelu celou řadu validací zadaných údajů, které nemusí nutně odpovídat požadované formě. Případné nálezy jsou nepřímo ohlášeny příslušným posluchačům a je pak znemožněno aktualizovat model, dokud se chyby neodstraní.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class DefaultArcController extends AbstractController<ArcView> implements ArcController {
    
    private class DefaultArcRemovedListener implements ArcRemovedListener {
        
        /**
         * {@inheritDoc}
         * 
         * Odstraní pohledy na podrobnosti hrany.
         */
        @Override
        public void arcRemoved(final Arc arc) {
            Preconditions.checkNotNull(arc);
            
            callViews(new Callback<ArcView>() {

                @Override
                public void call(final ArcView view) {
                    view.removed();
                }                
            });
        }
        
    }
    
    private final class DefaultArcChangedListener implements
            ArcChangedListener {
        /**
         * {@inheritDoc}
         * 
         * Aktualizuje zobrazené podrobnosti hrany.
         */
        @Override
        public void arcChanged(final Arc arc) {
            Preconditions.checkNotNull(arc);
            
            setCurrent(arc);
            
            callViews(getCallbackOnCurrent());
        }
    }
    
    private class DefaultFromRenamedListener implements FromRenamedListener {
        
        /**
         * {@inheritDoc}
         * 
         * Aktualizuje zobrazené podrobnosti hrany.
         */
        @Override
        public void fromRenamed(final Node oldVersion, final Node newVersion) {
            Preconditions.checkNotNull(oldVersion);
            Preconditions.checkNotNull(newVersion);
            
            callViews(new Callback<ArcView>() {

                @Override
                public void call(final ArcView view) {
                    Preconditions.checkNotNull(view);
                    
                    view.updateFrom(newVersion.getName());
                }                
            });
        }
        
    }
    
    private class DefaultToRenamedListener implements ToRenamedListener {
        
        /**
         * {@inheritDoc}
         * 
         * Aktualizuje zobrazené podrobnosti hrany.
         */
        @Override
        public void toRenamed(final Node oldVersion, final Node newVersion) {
            Preconditions.checkNotNull(oldVersion);
            Preconditions.checkNotNull(newVersion);
            
            callViews(new Callback<ArcView>() {

                @Override
                public void call(final ArcView view) {
                    Preconditions.checkNotNull(view);
                    
                    view.updateTo(newVersion.getName());
                }                
            });
        }
        
    }

    /**
     * Aktualizuje pohledy s ohledem na typ hrany, jejíž podrobnosti zobrazují.
     */
    private final static class ViewProcessor implements Processor<Callback<ArcView>> {
        
        @Override
        public final Callback<ArcView> process(final TransitionArc arc) {
            return new Callback<ArcView>() {
    
                @Override
                public void call(final ArcView parameter) {
                    parameter.updateName(arc.getName());
                    parameter.updatePriority(arc.getPriority());
                    parameter.updateType(TransitionArc.class);
                    parameter.updateFrom(arc.getFrom().getName());
                    parameter.updateTo(arc.getTo().getName());
                    
                    parameter.updatedCode(arc.getCode());
                }
                
            };
        }
    
        @Override
        public final Callback<ArcView> process(final PredicateTestArc arc) {
            return new Callback<ArcView>() {
    
                @Override
                public void call(final ArcView parameter) {
                    parameter.updateName(arc.getName());
                    parameter.updatePriority(arc.getPriority());
                    parameter.updateType(PredicateTestArc.class);
                    parameter.updateFrom(arc.getFrom().getName());
                    parameter.updateTo(arc.getTo().getName());
                    
                    parameter.updatedCode(arc.getCode());
                    
                    parameter.updatedPrepare(arc.getPrepareCode());
                    parameter.updatedPredicate(arc.getPredicateName());
                    parameter.updatedValue(arc.getValue());
                }
                
            };
        }
    
        @Override
        public final Callback<ArcView> process(final CodeTestArc arc) {
            return new Callback<ArcView>() {
    
                @Override
                public void call(final ArcView parameter) {
                    parameter.updateName(arc.getName());
                    parameter.updatePriority(arc.getPriority());
                    parameter.updateType(CodeTestArc.class);
                    parameter.updateFrom(arc.getFrom().getName());
                    parameter.updateTo(arc.getTo().getName());
                    
                    parameter.updatedCode(arc.getCode());
                    
                    parameter.updatedTested(arc.getTested());
                    parameter.updatedValue(arc.getValue());
                }
                
            };
        }
    
        @Override
        public final Callback<ArcView> process(final RecurentArc arc) {
            return new Callback<ArcView>() {
    
                @Override
                public void call(final ArcView parameter) {
                    parameter.updateName(arc.getName());
                    parameter.updatePriority(arc.getPriority());
                    parameter.updateType(RecurentArc.class);
                    parameter.updateFrom(arc.getFrom().getName());
                    parameter.updateTo(arc.getTo().getName());
                    
                    parameter.updatedCode(arc.getCode());
                    
                    parameter.updatedTarget(arc.getTarget());
                }
                
            };
        }
    
        @Override
        public final Callback<ArcView> process(final PatternArc arc) {
            return new Callback<ArcView>() {
    
                @Override
                public void call(final ArcView parameter) {
                    parameter.updateName(arc.getName());
                    parameter.updatePriority(arc.getPriority());
                    parameter.updateType(PatternArc.class);
                    parameter.updateFrom(arc.getFrom().getName());
                    parameter.updateTo(arc.getTo().getName());
                    
                    parameter.updatedCode(arc.getCode());
                    
                    parameter.updatedPattern(arc.getPattern());
                    parameter.updatedThat(arc.getThat());
                }
                
            };
        }
    }

    private static final SimplePatternChecker DEFAULT_SIMPLE_PATTERN_CHECKER =
            DefaultSimplePatternChecker.create();
    private static final MixedPatternChecker DEFAULT_MIXED_PATTERN_CHECKER =
            DefaultMixedPatternChecker.create();
    
    private final System system;
    private Arc current;
    
    private final NormalWordBuilderFactory nodeNameBuilderFactory;
    private final NormalWordBuilderFactory predicateNameBuilderFactory;
    private final SimplePatternBuilderFactory simplePatternBuilderFactory;
    private final MixedPatternBuilderFactory mixedPatternBuilderFactory;
    private final CodeContentBuilderFactory codeBuilderFactory;
    
    /**
     * Vytvoří řadič a zaregistruje jej na modelu.
     * 
     * @param system systém sítí, v kterém se hrana nachází
     * @param eventManager správce událostí
     * @param current aktuální podoba hrany
     * @param botSettings nastavení bota
     * @param languageSettings nastavení jazyka
     * @param namespacesToPrefixes prefixy pro prostory jmen AIML
     * @return řadič
     */
    public static DefaultArcController create(final System system, final EventManager eventManager, final Arc current, final BotConfiguration botSettings, final LanguageConfiguration languageSettings, final Map<URI, String> namespacesToPrefixes) {
        final NormalWordChecker nameChecker = DefaultNormalWordChecker.create(system.getStatesNamingAuthority());
        final NormalWordChecker predicatesChecker = DefaultNormalWordChecker.create(system.getPredicatesNamingAuthority());
        final CodeChecker codeChecker = DefaultCodeChecker.create(botSettings, languageSettings, namespacesToPrefixes);
        
        return create(system, eventManager, current, nameChecker, codeChecker, DEFAULT_SIMPLE_PATTERN_CHECKER, DEFAULT_MIXED_PATTERN_CHECKER, predicatesChecker);
    }
    
    /**
     * Vytvoří řadič a zaregistruje jej na modelu.
     * 
     * @param system systém sítí, v kterém se hrana nachází
     * @param eventManager správce událostí
     * @param current aktuální podoba hrany
     * @param nodeNameChecker validátor názvů stavu
     * @param codeChecker validátor kódu
     * @param simplePatternChecker validátor prostých vzorů
     * @param mixedPatternChecker validátor složených vzorů
     * @param predicateNameChecker validátor názvů predikátů
     * @return řadič
     */
    public static DefaultArcController create(
            final System system, final EventManager eventManager,
            final Arc current,
            final NormalWordChecker nodeNameChecker,
            final CodeChecker codeChecker,
            final SimplePatternChecker simplePatternChecker,
            final MixedPatternChecker mixedPatternChecker,
            final NormalWordChecker predicateNameChecker) {
        Preconditions.checkNotNull(system);
        Preconditions.checkNotNull(current);
        Preconditions.checkNotNull(eventManager);
        Preconditions.checkNotNull(nodeNameChecker);
        Preconditions.checkNotNull(codeChecker);
        Preconditions.checkNotNull(simplePatternChecker);
        Preconditions.checkNotNull(mixedPatternChecker);
        Preconditions.checkNotNull(predicateNameChecker);
        
        return create(system, eventManager, current, DefaultNormalWordBuilderFactory.create(nodeNameChecker), DefaultCodeContentBuilderFactory.create(codeChecker), DefaultSimplePatternBuilderFactory.create(simplePatternChecker), DefaultMixedPatternBuilderFactory.create(mixedPatternChecker), DefaultNormalWordBuilderFactory.create(predicateNameChecker));
    }
    
    /**
     * Vytvoří řadič a zaregistruje jej na modelu.
     * 
     * @param system systém sítí, v kterém se hrana nachází
     * @param eventManager správce událostí
     * @param current aktuální podoba hrany
     * @param nodeNameBuilderFactory továrna na konstruktor názvů stavu
     * @param codeBuilderFactory továrna na konstruktor kódu
     * @param simplePatternBuilderFactory továrna na konstruktor prostých vzorů
     * @param mixedPatternBuilderFactory továrna na konstruktor složených vzorů
     * @param predicateNameBuilderFactory továrna na konstruktor názvů predikátů
     * @return řadič
     */
    public static DefaultArcController create(final System system,
            final EventManager eventManager,
            final Arc current,
            final NormalWordBuilderFactory nodeNameBuilderFactory,
            final CodeContentBuilderFactory codeBuilderFactory,
            final SimplePatternBuilderFactory simplePatternBuilderFactory,
            final MixedPatternBuilderFactory mixedPatternBuilderFactory,
            final NormalWordBuilderFactory predicateNameBuilderFactory) {
        Preconditions.checkNotNull(system);
        Preconditions.checkNotNull(current);
        Preconditions.checkNotNull(eventManager);
        Preconditions.checkNotNull(nodeNameBuilderFactory);
        Preconditions.checkNotNull(codeBuilderFactory);
        Preconditions.checkNotNull(simplePatternBuilderFactory);
        Preconditions.checkNotNull(predicateNameBuilderFactory);
        
        final DefaultArcController newInstance = new DefaultArcController(system, eventManager, current, nodeNameBuilderFactory, codeBuilderFactory, simplePatternBuilderFactory, mixedPatternBuilderFactory, predicateNameBuilderFactory);
        
        newInstance.addListeners();
        
        return newInstance;
    }

    private void addListeners() {
        addListener(ArcChangedEvent.class, this.current, new DefaultArcChangedListener());
        addListener(ArcRemovedEvent.class, this.current, new DefaultArcRemovedListener());
        addListener(FromRenamedEvent.class, this.current, new DefaultFromRenamedListener());
        addListener(ToRenamedEvent.class, this.current, new DefaultToRenamedListener());
    }
    
    private DefaultArcController(final System system,
            final EventManager eventManager,
            final Arc current,
            final NormalWordBuilderFactory nodeNameBuilderFactory,
            final CodeContentBuilderFactory codeBuilderFactory,
            final SimplePatternBuilderFactory simplePatternBuilderFactory,
            final MixedPatternBuilderFactory mixedPatternBuilderFactory,
            final NormalWordBuilderFactory predicateNameBuilderFactory) {
        super(eventManager);
        
        this.system = system;
        this.current = current;
        this.nodeNameBuilderFactory = nodeNameBuilderFactory;
        this.codeBuilderFactory = codeBuilderFactory;
        this.simplePatternBuilderFactory = simplePatternBuilderFactory;
        this.mixedPatternBuilderFactory = mixedPatternBuilderFactory;
        this.predicateNameBuilderFactory = predicateNameBuilderFactory;
    }
    
    private void updatePattern(NormalWord newName, Priority priority, Code code,
            MixedPattern pattern, MixedPattern that) {
        this.system.changeArc(this.current, newName, priority, PatternArc.class, code, pattern, that);
    }

    private void updateCodeTest(NormalWord newName, Priority priority,
            Code code, SimplePattern expectedValue,
            Code tested) {
        this.system.changeArc(this.current, newName, priority, CodeTestArc.class, code, expectedValue, tested);
    }

    private void updatePredicateTest(NormalWord newName, Priority priority,
            Code code, SimplePattern expectedValue,
            Code prepareCode, NormalWord predicateName) {
        this.system.changeArc(this.current, newName, priority, PredicateTestArc.class, code, expectedValue, prepareCode, predicateName);
    }

    private void updateRecurent(NormalWord newName, Priority priority, Code code,
            EnterNode target) {
        this.system.changeArc(this.current, newName, priority, RecurentArc.class, code, target);
    }

    private void updateTransition(NormalWord newName, Priority priority, final Code code) {
        this.system.changeArc(this.current, newName, priority, TransitionArc.class, code);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.controllers.NetworkController#toPattern(java.lang.String, int, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void updatePattern(final String newName, final int priority, final String pattern,
            final String that, final String code) {
        Preconditions.checkNotNull(newName);
        Preconditions.checkNotNull(code);
        Preconditions.checkNotNull(pattern);
        Preconditions.checkNotNull(that);
        Preconditions.checkArgument(priority >= 0);
        
        final NormalWord name;
        final NormalWord currentName = this.current.getName();
        if (currentName.getText().equals(newName)) {
            name = currentName;
        } else {
            final NormalWordBuilder nameBuilder = this.nodeNameBuilderFactory.produce(newName);
            final CheckResult nodeNameCheckResult = nameBuilder.check();
            
            if (!nodeNameCheckResult.isValid()) {
                return;
            }
            
            name = nameBuilder.build();
        }
        
        final CodeContentBuilder codeBuilder = this.codeBuilderFactory.produce(code);
        final CheckResult codeCheckResult = codeBuilder.check();
        if (!codeCheckResult.isValid()) {
            return;
        }
        
        final MixedPatternBuilder patternBuilder = this.mixedPatternBuilderFactory.produce(pattern);
        final CheckResult patternCheckResult = patternBuilder.check();
        if (!patternCheckResult.isValid()) {
            return;
        }
        
        final MixedPatternBuilder thatBuilder = this.mixedPatternBuilderFactory.produce(that);
        final CheckResult thatCheckResult = thatBuilder.check();
        if (!thatCheckResult.isValid()) {
            return;
        }
        
        updatePattern(name, Priority.of(priority), codeBuilder.build(), patternBuilder.build(), thatBuilder.build());
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.controllers.NetworkController#toCodeTest(java.lang.String, int, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void updateCodeTest(String newName, int priority, String code,
            String testedCode, String value) {
        Preconditions.checkNotNull(newName);
        Preconditions.checkNotNull(code);
        Preconditions.checkNotNull(testedCode);
        Preconditions.checkNotNull(value);
        Preconditions.checkArgument(priority >= 0);
        
        final NormalWord name;
        final NormalWord currentName = this.current.getName();
        if (currentName.getText().equals(newName)) {
            name = currentName;
        } else {
            final NormalWordBuilder nameBuilder = this.nodeNameBuilderFactory.produce(newName);
            final CheckResult nodeNameCheckResult = nameBuilder.check();
            
            if (!nodeNameCheckResult.isValid()) {
                return;
            }
            
            name = nameBuilder.build();
        }
        
        final CodeContentBuilder codeBuilder = this.codeBuilderFactory.produce(code);
        final CheckResult codeCheckResult = codeBuilder.check();
        if (!codeCheckResult.isValid()) {
            return;
        }
        
        final CodeContentBuilder testedCodeBuilder = this.codeBuilderFactory.produce(testedCode);
        final CheckResult testedCodeCheckResult = testedCodeBuilder.check();
        if (!testedCodeCheckResult.isValid()) {
            return;
        }
        
        final SimplePatternBuilder valueBuilder = this.simplePatternBuilderFactory.produce(value);
        final CheckResult valueCheckResult = valueBuilder.check();
        if (!valueCheckResult.isValid()) {
            return;
        }
                
        updateCodeTest(name, Priority.of(priority), codeBuilder.build(), valueBuilder.build(), testedCodeBuilder.build());
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.controllers.NetworkController#toPredicateTest(java.lang.String, int, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void updatePredicateTest(String newName, int priority, String code,
            String prepareCode, String predicateName, String value) {
        Preconditions.checkNotNull(newName);
        Preconditions.checkNotNull(code);
        Preconditions.checkNotNull(prepareCode);
        Preconditions.checkNotNull(predicateName);
        Preconditions.checkNotNull(value);
        Preconditions.checkArgument(priority >= 0);
        
        final NormalWord name;
        final NormalWord currentName = this.current.getName();
        if (currentName.getText().equals(newName)) {
            name = currentName;
        } else {
            final NormalWordBuilder nameBuilder = this.nodeNameBuilderFactory.produce(newName);
            final CheckResult nodeNameCheckResult = nameBuilder.check();
            
            if (!nodeNameCheckResult.isValid()) {
                return;
            }
            
            name = nameBuilder.build();
        }
        
        final CodeContentBuilder codeBuilder = this.codeBuilderFactory.produce(code);
        final CheckResult codeCheckResult = codeBuilder.check();
        if (!codeCheckResult.isValid()) {
            return;
        }
        
        final CodeContentBuilder preparedCodeBuilder = this.codeBuilderFactory.produce(prepareCode);
        final CheckResult preparedCodeCheckResult = preparedCodeBuilder.check();
        if (!preparedCodeCheckResult.isValid()) {
            return;
        }
        
        final NormalWordBuilder predicateNameBuilder = this.predicateNameBuilderFactory.produce(predicateName);
        final CheckResult predicateNameCheckResult = predicateNameBuilder.check();
        if (!predicateNameCheckResult.isValid()) {
            return;
        }
        
        final SimplePatternBuilder valueBuilder = this.simplePatternBuilderFactory.produce(value);
        final CheckResult valueCheckResult = valueBuilder.check();
        if (!valueCheckResult.isValid()) {
            return;
        }
        
        updatePredicateTest(name, Priority.of(priority), codeBuilder.build(), valueBuilder.build(), preparedCodeBuilder.build(), predicateNameBuilder.build());
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.controllers.NetworkController#toRecurentTest(java.lang.String, int, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void updateRecurent(final String newName, final int priority, final String code,
            final EnterNode target) {
        Preconditions.checkNotNull(newName);
        Preconditions.checkNotNull(code);
        Preconditions.checkNotNull(target);
        Preconditions.checkArgument(priority >= 0);
        
        final NormalWord name;
        final NormalWord currentName = this.current.getName();
        if (currentName.getText().equals(newName)) {
            name = currentName;
        } else {
            final NormalWordBuilder nameBuilder = this.nodeNameBuilderFactory.produce(newName);
            final CheckResult nodeNameCheckResult = nameBuilder.check();
            
            if (!nodeNameCheckResult.isValid()) {
                return;
            }
            
            name = nameBuilder.build();
        }
        
        final CodeContentBuilder codeBuilder = this.codeBuilderFactory.produce(code);
        final CheckResult codeCheckResult = codeBuilder.check();
        if (!codeCheckResult.isValid()) {
            return;
        }
        
        updateRecurent(name, Priority.of(priority), codeBuilder.build(), target);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.controllers.NetworkController#toTransition(java.lang.String, int, java.lang.String)
     */
    @Override
    public void updateTransition(final String newName, final int priority, final String code) {
        Preconditions.checkNotNull(newName);
        Preconditions.checkNotNull(code);
        Preconditions.checkArgument(priority >= 0);
        
        final NormalWord name;
        final NormalWord currentName = this.current.getName();
        if (currentName.getText().equals(newName)) {
            name = currentName;
        } else {
            final NormalWordBuilder nameBuilder = this.nodeNameBuilderFactory.produce(newName);
            final CheckResult nodeNameCheckResult = nameBuilder.check();
            
            if (!nodeNameCheckResult.isValid()) {
                return;
            }
            
            name = nameBuilder.build();
        }
        
        final CodeContentBuilder codeBuilder = this.codeBuilderFactory.produce(code);
        final CheckResult codeCheckResult = codeBuilder.check();
        if (!codeCheckResult.isValid()) {
            return;
        }
        
        updateTransition(name, Priority.of(priority), codeBuilder.build());
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.utils.mvc.AbstractController#fill(java.lang.Object)
     */
    @Override
    public void fill(final ArcView view) {
        Preconditions.checkNotNull(view);
        
        getCallbackOnCurrent().call(view);
    }

    private void setCurrent(final Arc arc) {
        Preconditions.checkNotNull(arc);
        
        removeListeners();
        this.current = arc;
        addListeners();
    }

    private void removeListeners() {
        removeAllListeners(ArcRemovedEvent.class, this.current);
        removeAllListeners(ArcChangedEvent.class, this.current);
        removeAllListeners(FromRenamedEvent.class, this.current);
        removeAllListeners(ToRenamedEvent.class, this.current);
    }
    
    private Callback<ArcView> getCallbackOnCurrent() {
        return this.current.accept(new ViewProcessor());
    }
}
