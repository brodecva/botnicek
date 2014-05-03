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
package cz.cuni.mff.ms.brodecva.botnicek.ide.designer.controllers;

import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.events.arcs.ArcChangedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.events.arcs.ArcChangedListener;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.events.system.AvailableReferencesChangedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.events.system.AvailableReferencesChangedListener;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.events.system.AvailableReferencesExtendedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.events.system.AvailableReferencesExtendedListener;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.events.system.AvailableReferencesReducedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.events.system.AvailableReferencesReducedListener;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.types.MixedPattern;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.types.PredicateName;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.types.SimplePattern;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.System;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.arcs.Arc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.arcs.CodeTestArc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.arcs.PatternArc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.arcs.PredicateTestArc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.arcs.RecurentArc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.arcs.TransitionArc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.arcs.api.Processor;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.nodes.EnterNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.types.Code;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.utils.Callback;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.arcs.AbstractArcInternalFrame;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.arcs.ArcType;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.arcs.ArcView;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.arcs.CodeTestArcInternalFrame;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.arcs.PatternArcInternalFrame;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.arcs.PredicateTestArcInternalFrame;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.arcs.RecurentArcInternalFrame;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.arcs.TransitionArcInternalFrame;
import cz.cuni.mff.ms.brodecva.botnicek.ide.editor.checker.CheckResult;
import cz.cuni.mff.ms.brodecva.botnicek.ide.editor.checker.code.CodeCheckResult;
import cz.cuni.mff.ms.brodecva.botnicek.ide.editor.checker.code.CodeChecker;
import cz.cuni.mff.ms.brodecva.botnicek.ide.editor.checker.code.CodeContentBuilder;
import cz.cuni.mff.ms.brodecva.botnicek.ide.editor.checker.code.DefaultCodeContentBuilder;
import cz.cuni.mff.ms.brodecva.botnicek.ide.editor.checker.code.DefaultCodeChecker;
import cz.cuni.mff.ms.brodecva.botnicek.ide.editor.checker.mixedpattern.DefaultMixedPatternBuilder;
import cz.cuni.mff.ms.brodecva.botnicek.ide.editor.checker.mixedpattern.DefaultMixedPatternChecker;
import cz.cuni.mff.ms.brodecva.botnicek.ide.editor.checker.mixedpattern.MixedPatternBuilder;
import cz.cuni.mff.ms.brodecva.botnicek.ide.editor.checker.mixedpattern.MixedPatternCheckResult;
import cz.cuni.mff.ms.brodecva.botnicek.ide.editor.checker.mixedpattern.MixedPatternChecker;
import cz.cuni.mff.ms.brodecva.botnicek.ide.editor.checker.nodes.DefaultTargetNameBuilder;
import cz.cuni.mff.ms.brodecva.botnicek.ide.editor.checker.nodes.DefaultTargetNameChecker;
import cz.cuni.mff.ms.brodecva.botnicek.ide.editor.checker.nodes.TargetNameBuilder;
import cz.cuni.mff.ms.brodecva.botnicek.ide.editor.checker.nodes.TargetNameCheckResult;
import cz.cuni.mff.ms.brodecva.botnicek.ide.editor.checker.nodes.TargetNameChecker;
import cz.cuni.mff.ms.brodecva.botnicek.ide.editor.checker.predicates.DefaultPredicateNameBuilder;
import cz.cuni.mff.ms.brodecva.botnicek.ide.editor.checker.predicates.DefaultPredicateNameChecker;
import cz.cuni.mff.ms.brodecva.botnicek.ide.editor.checker.predicates.PredicateNameBuilder;
import cz.cuni.mff.ms.brodecva.botnicek.ide.editor.checker.predicates.PredicateNameCheckResult;
import cz.cuni.mff.ms.brodecva.botnicek.ide.editor.checker.predicates.PredicateNameChecker;
import cz.cuni.mff.ms.brodecva.botnicek.ide.editor.checker.simplepattern.DefaultSimplePatternChecker;
import cz.cuni.mff.ms.brodecva.botnicek.ide.editor.checker.simplepattern.DefaultSimplePatternBuilder;
import cz.cuni.mff.ms.brodecva.botnicek.ide.editor.checker.simplepattern.SimplePatternBuilder;
import cz.cuni.mff.ms.brodecva.botnicek.ide.editor.checker.simplepattern.SimplePatternCheckResult;
import cz.cuni.mff.ms.brodecva.botnicek.ide.editor.checker.simplepattern.SimplePatternChecker;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.EventManager;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.mvc.AbstractController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.reflection.CopyConstruction;
import cz.cuni.mff.ms.brodecva.botnicek.library.responder.Bot;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public class DefaultArcController extends AbstractController<ArcView> implements ArcController {
    
    private final class DefaultAvailableReferencesChangedListener implements AvailableReferencesChangedListener {

        /* (non-Javadoc)
         * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.events.system.AvailableReferencesChangedListener#referencesChanged(java.util.Set)
         */
        @Override
        public void referencesChanged(final Set<EnterNode> references) {
            callViews(new Callback<ArcView>() {

                @Override
                public void call(final ArcView view) {
                    final Set<String> referenceNames = new HashSet<>();
                    
                    for (final EnterNode node : references) {
                        referenceNames.add(node.getName());
                    }
                    
                    view.updatedAvailableReferences(referenceNames);
                }
                
            });
        }
        
    }
    
    private final class DefaultAvailableReferencesExtendedListener implements AvailableReferencesExtendedListener {

        /* (non-Javadoc)
         * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.events.system.AvailableReferencesChangedListener#referencesChanged(java.util.Set)
         */
        @Override
        public void referencesExtended(final Set<EnterNode> references) {
            callViews(new Callback<ArcView>() {

                @Override
                public void call(final ArcView view) {
                    final Set<String> referenceNames = new HashSet<>();
                    
                    for (final EnterNode node : references) {
                        referenceNames.add(node.getName());
                    }
                    
                    view.extendedAvailableReferences(referenceNames);
                }
                
            });
        }
        
    }
    
    private final class DefaultAvailableReferencesReducedListener implements AvailableReferencesReducedListener {

        /* (non-Javadoc)
         * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.events.system.AvailableReferencesChangedListener#referencesChanged(java.util.Set)
         */
        @Override
        public void referencesReduced(final Set<EnterNode> references) {
            callViews(new Callback<ArcView>() {

                @Override
                public void call(final ArcView view) {
                    final Set<String> referenceNames = new HashSet<>();
                    
                    for (final EnterNode node : references) {
                        referenceNames.add(node.getName());
                    }
                    
                    view.removedAvailableReferences(referenceNames);
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
            final Processor processor = new Processor() {
                
                @Override
                public void process(final TransitionArc arc) {
                    callViews(new Callback<ArcView>() {

                        @Override
                        public void call(final ArcView parameter) {
                            parameter.updatedName(arc.getName());
                            parameter.updatedPriority(arc.getPriority());
                            
                            parameter.updatedCode(arc.getCode().getText());
                        }
                        
                    });
                }
                
                @Override
                public void process(final PredicateTestArc arc) {
                    callViews(new Callback<ArcView>() {

                        @Override
                        public void call(final ArcView parameter) {
                            parameter.updatedName(arc.getName());
                            parameter.updatedPriority(arc.getPriority());
                            
                            parameter.updatedCode(arc.getCode().getText());
                            
                            parameter.updatedPrepare(arc.getPrepareCode().getText());
                            parameter.updatedPredicate(arc.getPredicateName().getValue());
                            parameter.updatedValue(arc.getValue().getText());
                        }
                        
                    });
                }
                
                @Override
                public void process(final CodeTestArc arc) {
                    callViews(new Callback<ArcView>() {

                        @Override
                        public void call(final ArcView parameter) {
                            parameter.updatedName(arc.getName());
                            parameter.updatedPriority(arc.getPriority());
                            
                            parameter.updatedCode(arc.getCode().getText());
                            
                            parameter.updatedTested(arc.getTested().getText());
                            parameter.updatedValue(arc.getValue().getText());
                        }
                        
                    });
                }
                
                @Override
                public void process(final RecurentArc arc) {
                    callViews(new Callback<ArcView>() {

                        @Override
                        public void call(final ArcView parameter) {
                            parameter.updatedName(arc.getName());
                            parameter.updatedPriority(arc.getPriority());
                            
                            parameter.updatedCode(arc.getCode().getText());
                            
                            parameter.updatedTarget(arc.getTarget().getName());
                            parameter.updatedValue(arc.getValue().getText());
                        }
                        
                    });
                }
                
                @Override
                public void process(final PatternArc arc) {
                    callViews(new Callback<ArcView>() {

                        @Override
                        public void call(final ArcView parameter) {
                            parameter.updatedName(arc.getName());
                            parameter.updatedPriority(arc.getPriority());
                            
                            parameter.updatedCode(arc.getCode().getText());
                            
                            parameter.updatedPattern(arc.getPattern().getText());
                            parameter.updatedThat(arc.getThat().getText());
                        }
                        
                    });
                }
            };
        }
    }

    private static class FrameTransformer implements cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.arcs.api.Processor {

        private final Class<? extends AbstractArcInternalFrame> to;
        
        private Optional<AbstractArcInternalFrame> transformedFrame = Optional.<AbstractArcInternalFrame>absent();
        
        /**
         * @param newType
         * @return
         */
        public static FrameTransformer create(
                Class<? extends AbstractArcInternalFrame> newType) {
            return new FrameTransformer(newType);
        }
        
        private FrameTransformer(final Class<? extends AbstractArcInternalFrame> to) {
            Preconditions.checkNotNull(to);
            
            this.to = to;
        }
        
        public AbstractArcInternalFrame getTransformed() {
            return this.transformedFrame.get();
        }
        
        /* (non-Javadoc)
         * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.arcs.api.Processor#process(cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.arcs.PatternArcInternalFrame)
         */
        @Override
        public void process(final PatternArcInternalFrame frame) {
            try {
                this.transformedFrame = Optional.<AbstractArcInternalFrame>of(CopyConstruction.copy(this.to, frame));
            } catch (final InvocationTargetException e) {
                throw new IllegalArgumentException(e.getCause());
            }
        }

        /* (non-Javadoc)
         * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.arcs.api.Processor#process(cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.arcs.PredicateTestArcInternalFrame)
         */
        @Override
        public void process(final PredicateTestArcInternalFrame frame) {
            try {
                this.transformedFrame = Optional.<AbstractArcInternalFrame>of(CopyConstruction.copy(this.to, frame));
            } catch (final InvocationTargetException e) {
                throw new IllegalArgumentException(e.getCause());
            }
        }

        /* (non-Javadoc)
         * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.arcs.api.Processor#process(cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.arcs.RecurentArcInternalFrame)
         */
        @Override
        public void process(RecurentArcInternalFrame frame) {
            try {
                this.transformedFrame = Optional.<AbstractArcInternalFrame>of(CopyConstruction.copy(this.to, frame));
            } catch (final InvocationTargetException e) {
                throw new IllegalArgumentException(e.getCause());
            }
        }

        /* (non-Javadoc)
         * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.arcs.api.Processor#process(cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.arcs.TransitionArcInternalFrame)
         */
        @Override
        public void process(TransitionArcInternalFrame frame) {
            try {
                this.transformedFrame = Optional.<AbstractArcInternalFrame>of(CopyConstruction.copy(this.to, frame));
            } catch (final InvocationTargetException e) {
                throw new IllegalArgumentException(e.getCause());
            }
        }

        /* (non-Javadoc)
         * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.arcs.api.Processor#process(cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.arcs.CodeTestArcInternalFrame)
         */
        @Override
        public void process(CodeTestArcInternalFrame frame) {
            try {
                this.transformedFrame = Optional.<AbstractArcInternalFrame>of(CopyConstruction.copy(this.to, frame));
            } catch (final InvocationTargetException e) {
                throw new IllegalArgumentException(e.getCause());
            }            
        }
    }
    
    private static final Map<Class<? extends Arc>, ArcType> TO_ARC_TYPE_DEFAULTS;
    private static final SimplePatternChecker DEFAULT_SIMPLE_PATTERN_CHECKER =
            DefaultSimplePatternChecker.create();
    private static final MixedPatternChecker DEFAULT_MIXED_PATTERN_CHECKER =
            DefaultMixedPatternChecker.create();
    
    static {
        final Builder<Class<? extends Arc>, ArcType> toArcTypeDefaultsBuilder = ImmutableMap.builder();
        toArcTypeDefaultsBuilder.put(PatternArc.class, ArcType.PATTERN);
        toArcTypeDefaultsBuilder.put(PredicateTestArc.class, ArcType.PREDICATE_TEST);
        toArcTypeDefaultsBuilder.put(CodeTestArc.class, ArcType.CODE_TEST);
        toArcTypeDefaultsBuilder.put(TransitionArc.class, ArcType.TRANSITION);
        toArcTypeDefaultsBuilder.put(RecurentArc.class, ArcType.RECURENT);
        TO_ARC_TYPE_DEFAULTS = toArcTypeDefaultsBuilder.build();
    }
    
    public final ImmutableMap<Class<? extends Arc>, ArcType> toArcType;
    
    private final System system;
    
    private final CodeChecker codeChecker;
    private final SimplePatternChecker simplePatternChecker;
    private final MixedPatternChecker mixedPatternChecker;
    private final PredicateNameChecker predicateNameChecker;
    private final TargetNameChecker targetNameChecker;
    
    public static DefaultArcController create(final System system, final EventManager eventManager, final Bot bot, final Map<URI, String> namespacesToPrefixes) {
        final DefaultArcController newInstance = new DefaultArcController(system, eventManager, bot, namespacesToPrefixes);
        
        newInstance.addEventListener(ArcChangedEvent.class, newInstance.new DefaultArcChangedListener());
        newInstance.addEventListener(AvailableReferencesChangedEvent.class, newInstance.new DefaultAvailableReferencesChangedListener());
        newInstance.addEventListener(AvailableReferencesExtendedEvent.class, newInstance.new DefaultAvailableReferencesExtendedListener());
        newInstance.addEventListener(AvailableReferencesReducedEvent.class, newInstance.new DefaultAvailableReferencesReducedListener());
        
        return newInstance;
    }
    
    private DefaultArcController(final System system,
            final EventManager eventManager, final Bot bot, Map<URI, String> namespacesToPrefixes) {
        this(system, eventManager,  DefaultCodeChecker.create(bot, namespacesToPrefixes), DEFAULT_SIMPLE_PATTERN_CHECKER, DEFAULT_MIXED_PATTERN_CHECKER, DefaultPredicateNameChecker.create(system.getPredicatesNamingAuthority()), DefaultTargetNameChecker.create(system), TO_ARC_TYPE_DEFAULTS);
    }
    
    private DefaultArcController(final System system,
            final EventManager eventManager,
            final CodeChecker codeChecker,
            final SimplePatternChecker simplePatternChecker,
            final MixedPatternChecker mixedPatternChecker,
            final PredicateNameChecker predicateNameChecker,
            final TargetNameChecker targetNameChecker, final Map<Class<? extends Arc>, ArcType> toArcType) {
        super(eventManager);
        
        Preconditions.checkNotNull(system);
        Preconditions.checkNotNull(codeChecker);
        Preconditions.checkNotNull(simplePatternChecker);
        Preconditions.checkNotNull(predicateNameChecker);
        Preconditions.checkNotNull(targetNameChecker);
        Preconditions.checkNotNull(toArcType);
        
        this.system = system;
        this.codeChecker = codeChecker;
        this.simplePatternChecker = simplePatternChecker;
        this.mixedPatternChecker = mixedPatternChecker;
        this.predicateNameChecker = predicateNameChecker;
        this.targetNameChecker = targetNameChecker;
        this.toArcType = ImmutableMap.copyOf(toArcType);
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.controllers.NetworkController#toPattern(java.lang.String, int, cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.Pattern, cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.That, java.util.List)
     */
    @Override
    public void updatePattern(String arcName, String newName, int priority,
            Code code, MixedPattern pattern, MixedPattern that) {
        this.system.changeArc(arcName, arcName, priority, PatternArc.class, code, pattern, that);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.controllers.NetworkController#toCodeTest(java.lang.String, int, java.util.List, java.lang.String, java.util.List)
     */
    @Override
    public void updateCodeTest(String arcName, String newName,
            int priority, Code code,
            SimplePattern expectedValue, Code tested) {
        this.system.changeArc(arcName, arcName, priority, CodeTestArc.class, code, expectedValue, tested);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.controllers.NetworkController#toPredicateTest(java.lang.String, int, java.util.List, java.lang.String, java.lang.String, java.util.List)
     */
    @Override
    public void updatePredicateTest(String arcName, String newName,
            int priority, Code code,
            SimplePattern expectedValue, Code prepareCode, PredicateName predicateName) {
        this.system.changeArc(arcName, arcName, priority, PredicateTestArc.class, code, expectedValue, prepareCode, predicateName);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.controllers.NetworkController#toRecurent(java.lang.String, int, java.lang.String, java.lang.String, java.util.List)
     */
    @Override
    public void updateRecurent(String arcName, String newName, int priority,
            Code code, SimplePattern expectedValue, String targetName) {
        this.system.changeArc(arcName, arcName, priority, RecurentArc.class, code, expectedValue, targetName);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.controllers.NetworkController#toTransition(java.lang.String, int, cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.Pattern, cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.That, java.util.List)
     */
    @Override
    public void updateTransition(String arcName, String newName, int priority, final Code code) {
        this.system.changeArc(arcName, arcName, priority, TransitionArc.class, code);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.controllers.NetworkController#toPattern(java.lang.String, int, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void updatePattern(String arcName, String newName, int priority,
            String pattern, String that, String code) {
        final CodeContentBuilder codeBuilder = DefaultCodeContentBuilder.create(this.codeChecker, code);
        final CodeCheckResult codeCheckResult = codeBuilder.check();
        
        final MixedPatternBuilder patternBuilder = DefaultMixedPatternBuilder.create(this.mixedPatternChecker, pattern);
        final CheckResult patternCheckResult = patternBuilder.check();
        
        final MixedPatternBuilder thatBuilder = DefaultMixedPatternBuilder.create(this.mixedPatternChecker, that);
        final CheckResult thatCheckResult = thatBuilder.check();
        
        if (codeCheckResult.isValid() && patternCheckResult.isValid() && thatCheckResult.isValid()) {
            updatePattern(arcName, newName, priority, codeBuilder.build(), patternBuilder.build(), thatBuilder.build());
        }
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.controllers.NetworkController#toCodeTest(java.lang.String, int, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void updateCodeTest(String arcName, String newName, int priority,
            String code, String testedCode, String value) {
        final CodeContentBuilder codeBuilder = DefaultCodeContentBuilder.create(this.codeChecker, code);
        final CodeCheckResult codeCheckResult = codeBuilder.check();
        
        final CodeContentBuilder testedCodeBuilder = DefaultCodeContentBuilder.create(this.codeChecker, testedCode);
        final CodeCheckResult testedCodeCheckResult = testedCodeBuilder.check();
        
        final SimplePatternBuilder valueBuilder = DefaultSimplePatternBuilder.create(this.simplePatternChecker, value);
        final CheckResult valueCheckResult = valueBuilder.check();
        
        if (codeCheckResult.isValid() && valueCheckResult.isValid()) {
            updateCodeTest(arcName, arcName, priority, codeBuilder.build(), valueBuilder.build(), testedCodeBuilder.build());
        }
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.controllers.NetworkController#toPredicateTest(java.lang.String, int, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void updatePredicateTest(String arcName, String newName, int priority,
            String code, String prepareCode, String name, String value) {
        final CodeContentBuilder codeBuilder = DefaultCodeContentBuilder.create(this.codeChecker, code);
        final CodeCheckResult codeCheckResult = codeBuilder.check();
        
        final CodeContentBuilder preparedCodeBuilder = DefaultCodeContentBuilder.create(this.codeChecker, prepareCode);
        final CodeCheckResult preparedCodeCheckResult = preparedCodeBuilder.check();
        
        final PredicateNameBuilder predicateNameBuilder = DefaultPredicateNameBuilder.create(this.predicateNameChecker, name);
        final CheckResult predicateNameCheckResult = predicateNameBuilder.check();
        
        final SimplePatternBuilder valueBuilder = DefaultSimplePatternBuilder.create(this.simplePatternChecker, value);
        final CheckResult valueCheckResult = valueBuilder.check();
        
        if (codeCheckResult.isValid() && valueCheckResult.isValid()) {
            updatePredicateTest(arcName, newName, priority, codeBuilder.build(), valueBuilder.build(), preparedCodeBuilder.build(), predicateNameBuilder.build());
        }
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.controllers.NetworkController#toRecurentTest(java.lang.String, int, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void updateRecurent(String arcName, String newName, int priority,
            String code, String targetName, String value) {
        final CodeContentBuilder codeBuilder = DefaultCodeContentBuilder.create(this.codeChecker, code);
        final CodeCheckResult codeCheckResult = codeBuilder.check();
        
        final TargetNameBuilder targetNameBuilder = DefaultTargetNameBuilder.create(this.targetNameChecker, targetName);
        final CheckResult targetNameCheckResult = targetNameBuilder.check();
        
        final SimplePatternBuilder valueBuilder = DefaultSimplePatternBuilder.create(this.simplePatternChecker, value);
        final CheckResult valueCheckResult = valueBuilder.check();
        
        if (codeCheckResult.isValid() && valueCheckResult.isValid() && targetNameCheckResult.isValid()) {
            updateRecurent(arcName, newName, priority, codeBuilder.build(), valueBuilder.build(), targetNameBuilder.build().getValue());
        }
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.controllers.NetworkController#toTransition(java.lang.String, int, java.lang.String)
     */
    @Override
    public void updateTransition(final String arcName, final String newName, final int priority, final String code) {
        final CodeContentBuilder codeBuilder = DefaultCodeContentBuilder.create(this.codeChecker, code);
        final CodeCheckResult codeCheckResult = codeBuilder.check();
        
        if (codeCheckResult.isValid()) {
            updateTransition(arcName, newName, priority, codeBuilder.build());
        }
    }

    @Override
    public void changeType(final AbstractArcInternalFrame old,
            final Class<? extends AbstractArcInternalFrame> newType) {
        final FrameTransformer transformer = FrameTransformer.create(newType);
        
        this.callViews(new Callback<ArcView>() {
            
            @Override
            public void call(final ArcView view) {
                old.accept(transformer);
                final AbstractArcInternalFrame transformed = transformer.getTransformed();
                
                view.replaced(transformed);
            }
        });
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.controllers.ArcController#getAvailableTargets()
     */
    @Override
    public Set<String> getAvailableTargets() {
        final Set<EnterNode> available = this.system.getAvailableReferences();
        final Set<String> result = new HashSet<>();
        
        for (final EnterNode node : available) {
            result.add(node.getName());
        }
        
        return result;
    }
}
