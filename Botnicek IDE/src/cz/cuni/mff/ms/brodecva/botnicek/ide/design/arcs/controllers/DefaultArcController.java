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
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.MixedPattern;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.SimplePattern;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.code.model.builder.CodeContentBuilder;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.code.model.builder.DefaultCodeContentBuilder;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.code.model.checker.CodeChecker;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.code.model.checker.DefaultCodeChecker;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.CheckResult;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.mixedpattern.model.builder.DefaultMixedPatternBuilder;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.mixedpattern.model.builder.MixedPatternBuilder;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.mixedpattern.model.checker.DefaultMixedPatternChecker;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.mixedpattern.model.checker.MixedPatternChecker;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.simplepattern.model.builder.DefaultSimplePatternBuilder;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.simplepattern.model.builder.SimplePatternBuilder;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.simplepattern.model.checker.DefaultSimplePatternChecker;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.simplepattern.model.checker.SimplePatternChecker;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.targets.model.checker.DefaultTargetNameChecker;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.targets.model.checker.TargetNameChecker;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.words.model.builder.DefaultNormalWordBuilder;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.words.model.builder.NormalWordBuilder;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.words.model.checker.DefaultNormalWordChecker;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.words.model.checker.NormalWordChecker;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.events.ArcChangedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.events.ArcChangedListener;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.events.ArcRemovedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.events.ArcRemovedListener;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.CodeTestArc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.PatternArc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.PredicateTestArc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.RecurentArc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.TransitionArc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.api.Processor;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.ArcView;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.views.NetworkView;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.EnterNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.types.Code;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.utils.Callback;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.DefaultEventManager;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.EventManager;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.mvc.AbstractController;
import cz.cuni.mff.ms.brodecva.botnicek.library.api.BotConfiguration;
import cz.cuni.mff.ms.brodecva.botnicek.library.api.LanguageConfiguration;
import cz.cuni.mff.ms.brodecva.botnicek.library.responder.AIMLBot;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public class DefaultArcController extends AbstractController<ArcView> implements ArcController {
    
    private class DefaultArcRemovedListener implements ArcRemovedListener {
        
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
    
    /**
     * @author Václav Brodec
     * @version 1.0
     */
    private final class DefaultArcChangedListener implements
            ArcChangedListener {
        @Override
        public void arcChanged(final Arc arc) {
            Preconditions.checkNotNull(arc);
            
            setCurrent(arc);
            
            final Processor processor = new UpdateProcessor();
            arc.accept(processor);
        }
    }

    private abstract class ViewProcessor implements Processor {
        
        public abstract void apply(final Callback<ArcView> viewCallback);
        
        @Override
        public final void process(final TransitionArc arc) {
            apply(new Callback<ArcView>() {
    
                @Override
                public void call(final ArcView parameter) {
                    parameter.updatedName(arc.getName());
                    parameter.updatedPriority(arc.getPriority());
                    
                    parameter.updatedCode(arc.getCode());
                }
                
            });
        }
    
        @Override
        public final void process(final PredicateTestArc arc) {
            apply(new Callback<ArcView>() {
    
                @Override
                public void call(final ArcView parameter) {
                    parameter.updatedName(arc.getName());
                    parameter.updatedPriority(arc.getPriority());
                    
                    parameter.updatedCode(arc.getCode());
                    
                    parameter.updatedPrepare(arc.getPrepareCode());
                    parameter.updatedPredicate(arc.getPredicateName());
                    parameter.updatedValue(arc.getValue());
                }
                
            });
        }
    
        @Override
        public final void process(final CodeTestArc arc) {
            apply(new Callback<ArcView>() {
    
                @Override
                public void call(final ArcView parameter) {
                    parameter.updatedName(arc.getName());
                    parameter.updatedPriority(arc.getPriority());
                    
                    parameter.updatedCode(arc.getCode());
                    
                    parameter.updatedTested(arc.getTested());
                    parameter.updatedValue(arc.getValue());
                }
                
            });
        }
    
        @Override
        public final void process(final RecurentArc arc) {
            apply(new Callback<ArcView>() {
    
                @Override
                public void call(final ArcView parameter) {
                    parameter.updatedName(arc.getName());
                    parameter.updatedPriority(arc.getPriority());
                    
                    parameter.updatedCode(arc.getCode());
                    
                    parameter.updatedTarget(arc.getTarget());
                    parameter.updatedValue(arc.getValue());
                }
                
            });
        }
    
        @Override
        public final void process(final PatternArc arc) {
            apply(new Callback<ArcView>() {
    
                @Override
                public void call(final ArcView parameter) {
                    parameter.updatedName(arc.getName());
                    parameter.updatedPriority(arc.getPriority());
                    
                    parameter.updatedCode(arc.getCode());
                    
                    parameter.updatedPattern(arc.getPattern());
                    parameter.updatedThat(arc.getThat());
                }
                
            });
        }
    }
    
    private final class UpdateProcessor extends ViewProcessor {

        /* (non-Javadoc)
         * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.controllers.DefaultArcController.ViewProcessor#apply(cz.cuni.mff.ms.brodecva.botnicek.ide.design.utils.Callback)
         */
        @Override
        public void apply(final Callback<ArcView> viewCallback) {
            Preconditions.checkNotNull(viewCallback);
            
            callViews(viewCallback);
        }
        
    }
    
    private final class FillProcessor extends ViewProcessor {

        private final ArcView filled;
        
        public FillProcessor(final ArcView filled) {
            Preconditions.checkNotNull(filled);
            
            this.filled = filled;
        }
        
        /* (non-Javadoc)
         * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.controllers.DefaultArcController.ViewProcessor#apply(cz.cuni.mff.ms.brodecva.botnicek.ide.design.utils.Callback)
         */
        @Override
        public void apply(final Callback<ArcView> viewCallback) {
            Preconditions.checkNotNull(viewCallback);
            
            viewCallback.call(this.filled);
        }
        
    }

    private static final SimplePatternChecker DEFAULT_SIMPLE_PATTERN_CHECKER =
            DefaultSimplePatternChecker.create();
    private static final MixedPatternChecker DEFAULT_MIXED_PATTERN_CHECKER =
            DefaultMixedPatternChecker.create();
    
    private final System system;
    private Arc current;
    
    private final NormalWordChecker nodeNameChecker;
    private final CodeChecker codeChecker;
    private final SimplePatternChecker simplePatternChecker;
    private final MixedPatternChecker mixedPatternChecker;
    private final NormalWordChecker predicateNameChecker;    
    
    public static DefaultArcController create(final System system, final EventManager eventManager, final Arc current, final BotConfiguration botSettings, final LanguageConfiguration languageSettings, final Map<URI, String> namespacesToPrefixes) {
        final NormalWordChecker nameChecker = DefaultNormalWordChecker.create(system.getStatesNamingAuthority());
        final NormalWordChecker predicatesChecker  = DefaultNormalWordChecker.create(system.getPredicatesNamingAuthority());
        final CodeChecker codeChecker = DefaultCodeChecker.create(botSettings, languageSettings, namespacesToPrefixes);
        final TargetNameChecker targetNameChecker = DefaultTargetNameChecker.create(system);
        
        return create(system, eventManager, current, nameChecker, codeChecker, DEFAULT_SIMPLE_PATTERN_CHECKER, DEFAULT_MIXED_PATTERN_CHECKER, predicatesChecker, targetNameChecker);
    }
    
    public static DefaultArcController create(
            final System system, final EventManager eventManager,
            final Arc current,
            final NormalWordChecker nodeNameChecker,
            final CodeChecker codeChecker,
            final SimplePatternChecker simplePatternChecker,
            final MixedPatternChecker mixedPatternChecker,
            final NormalWordChecker predicateNameChecker, final TargetNameChecker targetChecker) {
        final DefaultArcController newInstance = new DefaultArcController(system, eventManager, current, nodeNameChecker, codeChecker, simplePatternChecker, mixedPatternChecker, predicateNameChecker, targetChecker);
        
        newInstance.addListeners();
        
        return newInstance;
    }

    /**
     * @param newInstance
     * @param current
     */
    private void addListeners() {
        addListener(ArcChangedEvent.class, this.current, new DefaultArcChangedListener());
        addListener(ArcRemovedEvent.class, this.current, new DefaultArcRemovedListener());
    }
    
    private DefaultArcController(final System system,
            final EventManager eventManager,
            final Arc current,
            final NormalWordChecker nodeNameChecker,
            final CodeChecker codeChecker,
            final SimplePatternChecker simplePatternChecker,
            final MixedPatternChecker mixedPatternChecker,
            final NormalWordChecker predicateNameChecker,
            final TargetNameChecker targetChecker) {
        super(eventManager);
        
        Preconditions.checkNotNull(system);
        Preconditions.checkNotNull(current);
        Preconditions.checkNotNull(nodeNameChecker);
        Preconditions.checkNotNull(codeChecker);
        Preconditions.checkNotNull(simplePatternChecker);
        Preconditions.checkNotNull(predicateNameChecker);
        Preconditions.checkNotNull(targetChecker);
        
        this.system = system;
        this.current = current;
        this.nodeNameChecker = nodeNameChecker;
        this.codeChecker = codeChecker;
        this.simplePatternChecker = simplePatternChecker;
        this.mixedPatternChecker = mixedPatternChecker;
        this.predicateNameChecker = predicateNameChecker;
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.controllers.NetworkController#toPattern(java.lang.String, int, cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.Pattern, cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.That, java.util.List)
     */
    @Override
    public void updatePattern(NormalWord newName, int priority, Code code,
            MixedPattern pattern, MixedPattern that) {
        this.system.changeArc(this.current, newName, priority, PatternArc.class, code, pattern, that);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.controllers.NetworkController#toCodeTest(java.lang.String, int, java.util.List, java.lang.String, java.util.List)
     */
    @Override
    public void updateCodeTest(NormalWord newName, int priority,
            Code code, SimplePattern expectedValue,
            Code tested) {
        this.system.changeArc(this.current, newName, priority, CodeTestArc.class, code, expectedValue, tested);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.controllers.NetworkController#toPredicateTest(java.lang.String, int, java.util.List, java.lang.String, java.lang.String, java.util.List)
     */
    @Override
    public void updatePredicateTest(NormalWord newName, int priority,
            Code code, SimplePattern expectedValue,
            Code prepareCode, NormalWord predicateName) {
        this.system.changeArc(this.current, newName, priority, PredicateTestArc.class, code, expectedValue, prepareCode, predicateName);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.controllers.NetworkController#toRecurent(java.lang.String, int, java.lang.String, java.lang.String, java.util.List)
     */
    @Override
    public void updateRecurent(NormalWord newName, int priority, Code code,
            SimplePattern expectedValue, EnterNode target) {
        this.system.changeArc(this.current, newName, priority, RecurentArc.class, code, expectedValue, target);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.controllers.NetworkController#toTransition(java.lang.String, int, cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.Pattern, cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.That, java.util.List)
     */
    @Override
    public void updateTransition(NormalWord newName, int priority, final Code code) {
        this.system.changeArc(this.current, newName, priority, TransitionArc.class, code);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.controllers.NetworkController#toPattern(java.lang.String, int, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void updatePattern(String newName, int priority, String pattern,
            String that, String code) {
        final NormalWordBuilder nameBuilder = DefaultNormalWordBuilder.create(this.nodeNameChecker, newName);
        final CheckResult nodeNameCheckResult = nameBuilder.check();
        
        final CodeContentBuilder codeBuilder = DefaultCodeContentBuilder.create(this.codeChecker, code);
        final CheckResult codeCheckResult = codeBuilder.check();
        
        final MixedPatternBuilder patternBuilder = DefaultMixedPatternBuilder.create(this.mixedPatternChecker, pattern);
        final CheckResult patternCheckResult = patternBuilder.check();
        
        final MixedPatternBuilder thatBuilder = DefaultMixedPatternBuilder.create(this.mixedPatternChecker, that);
        final CheckResult thatCheckResult = thatBuilder.check();
        
        if (nodeNameCheckResult.isValid() && codeCheckResult.isValid() && patternCheckResult.isValid() && thatCheckResult.isValid()) {
            updatePattern(nameBuilder.build(), priority, codeBuilder.build(), patternBuilder.build(), thatBuilder.build());
        }
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.controllers.NetworkController#toCodeTest(java.lang.String, int, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void updateCodeTest(String newName, int priority, String code,
            String testedCode, String value) {
        final NormalWordBuilder nameBuilder = DefaultNormalWordBuilder.create(this.nodeNameChecker, newName);
        final CheckResult nodeNameCheckResult = nameBuilder.check();
        
        final CodeContentBuilder codeBuilder = DefaultCodeContentBuilder.create(this.codeChecker, code);
        final CheckResult codeCheckResult = codeBuilder.check();
        
        final CodeContentBuilder testedCodeBuilder = DefaultCodeContentBuilder.create(this.codeChecker, testedCode);
        final CheckResult testedCodeCheckResult = testedCodeBuilder.check();
        
        final SimplePatternBuilder valueBuilder = DefaultSimplePatternBuilder.create(this.simplePatternChecker, value);
        final CheckResult valueCheckResult = valueBuilder.check();
        
        if (nodeNameCheckResult.isValid() && testedCodeCheckResult.isValid() && codeCheckResult.isValid() && valueCheckResult.isValid()) {
            updateCodeTest(nameBuilder.build(), priority, codeBuilder.build(), valueBuilder.build(), testedCodeBuilder.build());
        }
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.controllers.NetworkController#toPredicateTest(java.lang.String, int, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void updatePredicateTest(String newName, int priority, String code,
            String prepareCode, String name, String value) {
        final NormalWordBuilder nameBuilder = DefaultNormalWordBuilder.create(this.nodeNameChecker, newName);
        final CheckResult nodeNameCheckResult = nameBuilder.check();
        
        final CodeContentBuilder codeBuilder = DefaultCodeContentBuilder.create(this.codeChecker, code);
        final CheckResult codeCheckResult = codeBuilder.check();
        
        final CodeContentBuilder preparedCodeBuilder = DefaultCodeContentBuilder.create(this.codeChecker, prepareCode);
        final CheckResult preparedCodeCheckResult = preparedCodeBuilder.check();
        
        final NormalWordBuilder predicateNameBuilder = DefaultNormalWordBuilder.create(this.predicateNameChecker, name);
        final CheckResult predicateNameCheckResult = predicateNameBuilder.check();
        
        final SimplePatternBuilder valueBuilder = DefaultSimplePatternBuilder.create(this.simplePatternChecker, value);
        final CheckResult valueCheckResult = valueBuilder.check();
        
        if (nodeNameCheckResult.isValid() && preparedCodeCheckResult.isValid() && predicateNameCheckResult.isValid() && codeCheckResult.isValid() && valueCheckResult.isValid()) {
            updatePredicateTest(nameBuilder.build(), priority, codeBuilder.build(), valueBuilder.build(), preparedCodeBuilder.build(), predicateNameBuilder.build());
        }
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.controllers.NetworkController#toRecurentTest(java.lang.String, int, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void updateRecurent(String newName, int priority, String code,
            EnterNode target, String value) {
        final NormalWordBuilder nameBuilder = DefaultNormalWordBuilder.create(this.nodeNameChecker, newName);
        final CheckResult nodeNameCheckResult = nameBuilder.check();
        
        final CodeContentBuilder codeBuilder = DefaultCodeContentBuilder.create(this.codeChecker, code);
        final CheckResult codeCheckResult = codeBuilder.check();
        
        final SimplePatternBuilder valueBuilder = DefaultSimplePatternBuilder.create(this.simplePatternChecker, value);
        final CheckResult valueCheckResult = valueBuilder.check();
        
        if (nodeNameCheckResult.isValid() && codeCheckResult.isValid() && valueCheckResult.isValid()) {
            updateRecurent(nameBuilder.build(), priority, codeBuilder.build(), valueBuilder.build(), target);
        }
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.controllers.NetworkController#toTransition(java.lang.String, int, java.lang.String)
     */
    @Override
    public void updateTransition(final String newName, final int priority, final String code) {
        final NormalWordBuilder nameBuilder = DefaultNormalWordBuilder.create(this.nodeNameChecker, newName);
        final CheckResult nodeNameCheckResult = nameBuilder.check();
        
        final CodeContentBuilder codeBuilder = DefaultCodeContentBuilder.create(this.codeChecker, code);
        final CheckResult codeCheckResult = codeBuilder.check();
        
        if (nodeNameCheckResult.isValid() && codeCheckResult.isValid()) {
            updateTransition(nameBuilder.build(), priority, codeBuilder.build());
        }
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.utils.mvc.AbstractController#fill(java.lang.Object)
     */
    @Override
    public void fill(final ArcView view) {
        Preconditions.checkNotNull(view);
        
        final Processor processor = new FillProcessor(view);
        this.current.accept(processor);
    }

    /**
     * @param arc
     */
    private void setCurrent(final Arc arc) {
        Preconditions.checkNotNull(arc);
        
        removeListeners();
        this.current = arc;
        addListeners();
    }

    private void removeListeners() {
        removeAllListeners(ArcRemovedEvent.class, this.current);
        removeAllListeners(ArcChangedEvent.class, this.current);
    }
}
