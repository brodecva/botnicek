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

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.Code;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.MixedPattern;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.SimplePattern;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.controllers.CheckController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.builder.Builder;
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
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.events.NetworkRemovedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.events.NetworkRemovedListener;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.EnterNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.types.Priority;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Callback;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.EventManager;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.mvc.AbstractController;

/**
 * Výchozí řadič vlastností hrany provádí přes samotnou aktualizací modelu celou
 * řadu validací zadaných údajů, které nemusí nutně odpovídat požadované formě.
 * Případné nálezy jsou nepřímo ohlášeny příslušným posluchačům a je pak
 * znemožněno aktualizovat model, dokud se chyby neodstraní.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class DefaultArcController extends AbstractController<ArcView>
        implements ArcController {

    private final class DefaultArcChangedListener implements ArcChangedListener {
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
    
    private class DefaultNetworkRemovedListener implements NetworkRemovedListener {

        /**
         * {@inheritDoc}
         * 
         * Užívá se k odstraní pohledů na podrobnosti hrany sítě, jež byla odstraněna.
         */
        @Override
        public void removed(final Network network) {
            Preconditions.checkNotNull(network);

            callViews(new Callback<ArcView>() {

                @Override
                public void call(final ArcView view) {
                    view.removed();
                }
            });
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
    private final static class ViewProcessor implements
            Processor<Callback<ArcView>> {

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
    }

    /**
     * Vytvoří řadič a zaregistruje jej na modelu.
     * 
     * @param system
     *            systém sítí, v kterém se hrana nachází
     * @param eventManager
     *            správce událostí
     * @param current
     *            aktuální podoba hrany
     * @param nameValidationController řadič validace názvu uzlů
     * @param codeValidationController řadič validace kódu šablony
     * @param simplePatternValidationController řadič validace prostého vzoru
     * @param mixedPatternValidationController řadič validace složeného vzoru
     * @param predicateNameValidationController řadič validace názvu predikátu
     * @return řadič
     */
    public static DefaultArcController create(final System system,
            final EventManager eventManager, final Arc current,
            final CheckController<NormalWord> nameValidationController,
            final CheckController<Code> codeValidationController,
            final CheckController<SimplePattern> simplePatternValidationController,
            final CheckController<MixedPattern> mixedPatternValidationController,
            final CheckController<NormalWord> predicateNameValidationController) {
        Preconditions.checkNotNull(system);
        Preconditions.checkNotNull(current);
        Preconditions.checkNotNull(eventManager);
        Preconditions.checkNotNull(nameValidationController);
        Preconditions.checkNotNull(codeValidationController);
        Preconditions.checkNotNull(simplePatternValidationController);
        Preconditions.checkNotNull(mixedPatternValidationController);
        Preconditions.checkNotNull(predicateNameValidationController);

        final DefaultArcController newInstance =
                new DefaultArcController(system, eventManager, current, nameValidationController, codeValidationController, simplePatternValidationController, mixedPatternValidationController, predicateNameValidationController);

        newInstance.addListeners();
        
        // Rodičovská síť nelze u hrany změnit, tento posluchač tedy není nutné odstraňovat po dobu životnosti. 
        newInstance.addListener(NetworkRemovedEvent.class, current.getNetwork(), newInstance.new DefaultNetworkRemovedListener());

        return newInstance;
    }

    private final System system;
    private final CheckController<NormalWord> nameValidationController;
    private final CheckController<Code> codeValidationController;
    private final CheckController<SimplePattern> simplePatternValidationController;
    private final CheckController<MixedPattern> mixedPatternValidationController;
    private final CheckController<NormalWord> predicateNameValidationController;
    
    private Arc current;

    private DefaultArcController(
            final System system,
            final EventManager eventManager,
            final Arc current,
            final CheckController<NormalWord> nameValidationController,
            final CheckController<Code> codeValidationController,
            final CheckController<SimplePattern> simplePatternValidationController,
            final CheckController<MixedPattern> mixedPatternValidationController,
            final CheckController<NormalWord> predicateNameValidationController) {
        super(eventManager);

        this.system = system;
        this.current = current;
        this.nameValidationController = nameValidationController;
        this.codeValidationController = codeValidationController;
        this.simplePatternValidationController =
                simplePatternValidationController;
        this.mixedPatternValidationController =
                mixedPatternValidationController;
        this.predicateNameValidationController =
                predicateNameValidationController;
    }

    private void addListeners() {
        addListener(ArcChangedEvent.class, this.current,
                new DefaultArcChangedListener());
        addListener(ArcRemovedEvent.class, this.current,
                new DefaultArcRemovedListener());
        addListener(FromRenamedEvent.class, this.current,
                new DefaultFromRenamedListener());
        addListener(ToRenamedEvent.class, this.current,
                new DefaultToRenamedListener());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.utils.mvc.AbstractController#fill
     * (java.lang.Object)
     */
    @Override
    public void fill(final ArcView view) {
        Preconditions.checkNotNull(view);

        getCallbackOnCurrent().call(view);
    }

    private Callback<ArcView> getCallbackOnCurrent() {
        return this.current.accept(new ViewProcessor());
    }

    private void removeListeners() {
        removeAllListeners(ArcRemovedEvent.class, this.current);
        removeAllListeners(ArcChangedEvent.class, this.current);
        removeAllListeners(FromRenamedEvent.class, this.current);
        removeAllListeners(ToRenamedEvent.class, this.current);
    }

    private void setCurrent(final Arc arc) {
        Preconditions.checkNotNull(arc);

        removeListeners();
        this.current = arc;
        addListeners();
    }

    private void updateCodeTest(final NormalWord newName,
            final Priority priority, final Code code,
            final SimplePattern expectedValue, final Code tested) {
        this.system.changeArc(this.current, newName, priority,
                CodeTestArc.class, code, expectedValue, tested);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.designer.controllers.NetworkController
     * #toCodeTest(java.lang.String, int, java.lang.String, java.lang.String,
     * java.lang.String)
     */
    @Override
    public void updateCodeTest(final String newName, final int priority,
            final String code, final String testedCode, final String value) {
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
            final Builder<NormalWord> nodeNameBuilder = this.nameValidationController.provideBuilder(newName);
            if (!nodeNameBuilder.isValid()) {
                return;
            }

            name = nodeNameBuilder.build();
        }

        final Builder<Code> codeBuilder = this.codeValidationController.provideBuilder(code);
        if (!codeBuilder.isValid()) {
            return;
        }

        final Builder<Code> testedCodeBuilder = this.codeValidationController.provideBuilder(testedCode);
        if (!testedCodeBuilder.isValid()) {
            return;
        }

        final Builder<SimplePattern> valueBuilder = this.simplePatternValidationController.provideBuilder(value);
        if (!valueBuilder.isValid()) {
            return;
        }

        updateCodeTest(name, Priority.of(priority), codeBuilder.build(),
                valueBuilder.build(), testedCodeBuilder.build());
    }

    private void updatePattern(final NormalWord newName,
            final Priority priority, final Code code,
            final MixedPattern pattern, final MixedPattern that) {
        this.system.changeArc(this.current, newName, priority,
                PatternArc.class, code, pattern, that);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.designer.controllers.NetworkController
     * #toPattern(java.lang.String, int, java.lang.String, java.lang.String,
     * java.lang.String)
     */
    @Override
    public void updatePattern(final String newName, final int priority,
            final String pattern, final String that, final String code) {
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
            final Builder<NormalWord> nodeNameBuilder = this.nameValidationController.provideBuilder(newName);
            if (!nodeNameBuilder.isValid()) {
                return;
            }

            name = nodeNameBuilder.build();
        }

        final Builder<Code> codeBuilder = this.codeValidationController.provideBuilder(code);
        if (!codeBuilder.isValid()) {
            return;
        }

        final Builder<MixedPattern> patternBuilder = this.mixedPatternValidationController.provideBuilder(pattern);
        if (!patternBuilder.isValid()) {
            return;
        }

        final Builder<MixedPattern> thatBuilder = this.mixedPatternValidationController.provideBuilder(that);
        if (!thatBuilder.isValid()) {
            return;
        }

        updatePattern(name, Priority.of(priority), codeBuilder.build(),
                patternBuilder.build(), thatBuilder.build());
    }

    private void updatePredicateTest(final NormalWord newName,
            final Priority priority, final Code code,
            final SimplePattern expectedValue, final Code prepareCode,
            final NormalWord predicateName) {
        this.system.changeArc(this.current, newName, priority,
                PredicateTestArc.class, code, expectedValue, prepareCode,
                predicateName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.designer.controllers.NetworkController
     * #toPredicateTest(java.lang.String, int, java.lang.String,
     * java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void updatePredicateTest(final String newName, final int priority,
            final String code, final String prepareCode,
            final String predicateName, final String value) {
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
            final Builder<NormalWord> nodeNameBuilder = this.nameValidationController.provideBuilder(newName);
            if (!nodeNameBuilder.isValid()) {
                return;
            }

            name = nodeNameBuilder.build();
        }

        final Builder<Code> codeBuilder = this.codeValidationController.provideBuilder(code);
        if (!codeBuilder.isValid()) {
            return;
        }

        final Builder<Code> prepareCodeBuilder = this.codeValidationController.provideBuilder(prepareCode);
        if (!prepareCodeBuilder.isValid()) {
            return;
        }

        final Builder<NormalWord> predicateNameBuilder = this.predicateNameValidationController.provideBuilder(predicateName);
        if (!predicateNameBuilder.isValid()) {
            return;
        }

        final Builder<SimplePattern> valueBuilder = this.simplePatternValidationController.provideBuilder(value);
        if (!valueBuilder.isValid()) {
            return;
        }

        updatePredicateTest(name, Priority.of(priority), codeBuilder.build(),
                valueBuilder.build(), prepareCodeBuilder.build(),
                predicateNameBuilder.build());
    }

    private void updateRecurent(final NormalWord newName,
            final Priority priority, final Code code, final EnterNode target) {
        this.system.changeArc(this.current, newName, priority,
                RecurentArc.class, code, target);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.designer.controllers.NetworkController
     * #toRecurentTest(java.lang.String, int, java.lang.String,
     * java.lang.String, java.lang.String)
     */
    @Override
    public void updateRecurent(final String newName, final int priority,
            final String code, final EnterNode target) {
        Preconditions.checkNotNull(newName);
        Preconditions.checkNotNull(code);
        Preconditions.checkNotNull(target);
        Preconditions.checkArgument(priority >= 0);

        final NormalWord name;
        final NormalWord currentName = this.current.getName();
        if (currentName.getText().equals(newName)) {
            name = currentName;
        } else {
            final Builder<NormalWord> nodeNameBuilder = this.nameValidationController.provideBuilder(newName);
            if (!nodeNameBuilder.isValid()) {
                return;
            }

            name = nodeNameBuilder.build();
        }

        final Builder<Code> codeBuilder = this.codeValidationController.provideBuilder(code);
        if (!codeBuilder.isValid()) {
            return;
        }

        updateRecurent(name, Priority.of(priority), codeBuilder.build(), target);
    }

    private void updateTransition(final NormalWord newName,
            final Priority priority, final Code code) {
        this.system.changeArc(this.current, newName, priority,
                TransitionArc.class, code);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.designer.controllers.NetworkController
     * #toTransition(java.lang.String, int, java.lang.String)
     */
    @Override
    public void updateTransition(final String newName, final int priority,
            final String code) {
        Preconditions.checkNotNull(newName);
        Preconditions.checkNotNull(code);
        Preconditions.checkArgument(priority >= 0);

        final NormalWord name;
        final NormalWord currentName = this.current.getName();
        if (currentName.getText().equals(newName)) {
            name = currentName;
        } else {
            final Builder<NormalWord> nodeNameBuilder = this.nameValidationController.provideBuilder(newName);
            if (!nodeNameBuilder.isValid()) {
                return;
            }

            name = nodeNameBuilder.build();
        }

        final Builder<Code> codeBuilder = this.codeValidationController.provideBuilder(code);
        if (!codeBuilder.isValid()) {
            return;
        }

        updateTransition(name, Priority.of(priority), codeBuilder.build());
    }
}
