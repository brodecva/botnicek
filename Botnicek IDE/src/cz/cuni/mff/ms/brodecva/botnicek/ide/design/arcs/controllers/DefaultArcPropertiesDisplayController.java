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
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.controllers.CheckController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.events.ArcPropertiesDisplayedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.events.ArcPropertiesDisplayedListener;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.properties.ArcPropertiesDisplayView;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Callback;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.Presence;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.EventManager;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.mvc.AbstractController;
import cz.cuni.mff.ms.brodecva.botnicek.library.api.BotConfiguration;
import cz.cuni.mff.ms.brodecva.botnicek.library.api.LanguageConfiguration;

/**
 * Výchozí implementace řadiče podrobností hran.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public class DefaultArcPropertiesDisplayController extends
        AbstractController<ArcPropertiesDisplayView> implements
        ArcPropertiesDisplayController {

    private final class DefaultArcPropertiesDisplayedListener implements
            ArcPropertiesDisplayedListener {

        /**
         * {@inheritDoc}
         * 
         * <p>
         * Vytvoří řadič vlastností hrany a předá jej zobrazovačům vlastností.
         * </p>
         */
        @Override
        public void propertiesDisplayedOf(final Arc arc) {
            Preconditions.checkNotNull(arc);

            final ArcController arcController =
                    DefaultArcController
                            .create(DefaultArcPropertiesDisplayController.this.system,
                                    getEventManager(),
                                    arc,
                                    DefaultArcPropertiesDisplayController.this.nameValidationController,
                                    DefaultArcPropertiesDisplayController.this.codeValidationController,
                                    DefaultArcPropertiesDisplayController.this.simplePatternValidationController,
                                    DefaultArcPropertiesDisplayController.this.mixedPatternValidationController,
                                    DefaultArcPropertiesDisplayController.this.predicateValidationController);

            callViews(new Callback<ArcPropertiesDisplayView>() {

                @Override
                public void call(final ArcPropertiesDisplayView view) {
                    Preconditions.checkNotNull(view);

                    view.arcDisplayed(
                            arcController,
                            DefaultArcPropertiesDisplayController.this.availableReferencesController,
                            DefaultArcPropertiesDisplayController.this.nameValidationController,
                            DefaultArcPropertiesDisplayController.this.codeValidationController,
                            DefaultArcPropertiesDisplayController.this.simplePatternValidationController,
                            DefaultArcPropertiesDisplayController.this.mixedPatternValidationController,
                            DefaultArcPropertiesDisplayController.this.predicateValidationController);
                }

            });
        }

    }

    /**
     * Vytvoří řadič a zaregistruje posluchače.
     * 
     * @param system
     *            system sítí s hranou
     * @param eventManager
     *            správce událostí
     * @param botSettings
     *            nastavení bota
     * @param languageSettings
     *            nastavení jazyka
     * @param namespacesToPrefixes
     *            nastavení prefixů pro prostory jmen
     * @param availableReferencesController
     *            řadič dostupných míst zanoření
     * @param nameValidationController
     *            validátor názvů stavů
     * @param codeValidationController
     *            validátor kódu šablony
     * @param simplePatternValidationController
     *            validátor prostých vzorů
     * @param mixedPatternValidationController
     *            validátor složených vzorů
     * @param predicateValidationController
     *            validátor názvů predikátů
     * @return řadič
     */
    public static
            DefaultArcPropertiesDisplayController
            create(final System system,
                    final EventManager eventManager,
                    final BotConfiguration botSettings,
                    final LanguageConfiguration languageSettings,
                    final Map<URI, String> namespacesToPrefixes,
                    final AvailableReferencesController availableReferencesController,
                    final CheckController<NormalWord> nameValidationController,
                    final CheckController<Code> codeValidationController,
                    final CheckController<SimplePattern> simplePatternValidationController,
                    final CheckController<MixedPattern> mixedPatternValidationController,
                    final CheckController<NormalWord> predicateValidationController) {
        final DefaultArcPropertiesDisplayController newInstance =
                new DefaultArcPropertiesDisplayController(system, eventManager,
                        botSettings, languageSettings, namespacesToPrefixes,
                        availableReferencesController,
                        nameValidationController, codeValidationController,
                        simplePatternValidationController,
                        mixedPatternValidationController,
                        predicateValidationController);

        newInstance.addListener(ArcPropertiesDisplayedEvent.class, system,
                newInstance.new DefaultArcPropertiesDisplayedListener());

        return newInstance;
    }

    private final System system;
    private final AvailableReferencesController availableReferencesController;
    private final CheckController<NormalWord> nameValidationController;
    private final CheckController<Code> codeValidationController;
    private final CheckController<SimplePattern> simplePatternValidationController;
    private final CheckController<MixedPattern> mixedPatternValidationController;
    private final CheckController<NormalWord> predicateValidationController;
    
    private DefaultArcPropertiesDisplayController(
            final System system,
            final EventManager eventManager,
            final BotConfiguration botSettings,
            final LanguageConfiguration languageSettings,
            final Map<URI, String> namespacesToPrefixes,
            final AvailableReferencesController availableReferencesController,
            final CheckController<NormalWord> nameValidationController,
            final CheckController<Code> codeValidationController,
            final CheckController<SimplePattern> simplePatternValidationController,
            final CheckController<MixedPattern> mixedPatternValidationController,
            final CheckController<NormalWord> predicateValidationController) {
        super(eventManager);

        Preconditions.checkNotNull(system);
        Preconditions.checkNotNull(botSettings);
        Preconditions.checkNotNull(availableReferencesController);
        Preconditions.checkNotNull(nameValidationController);
        Preconditions.checkNotNull(codeValidationController);
        Preconditions.checkNotNull(simplePatternValidationController);
        Preconditions.checkNotNull(mixedPatternValidationController);
        Preconditions.checkNotNull(predicateValidationController);

        this.system = system;
        this.availableReferencesController = availableReferencesController;
        this.nameValidationController = nameValidationController;
        this.codeValidationController = codeValidationController;
        this.simplePatternValidationController =
                simplePatternValidationController;
        this.mixedPatternValidationController =
                mixedPatternValidationController;
        this.predicateValidationController = predicateValidationController;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.controllers.
     * ArcPropertiesController#displayProperties(java.lang.String,
     * java.util.Collection)
     */
    @Override
    public void displayArcProperties(final NormalWord name) {
        Preconditions.checkNotNull(name);

        final Arc arc = this.system.getArc(name);
        Preconditions.checkArgument(Presence.isPresent(arc));

        fire(ArcPropertiesDisplayedEvent.create(this.system, arc));
    }
}
