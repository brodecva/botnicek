/**
 * Copyright Václav Brodec 2014.
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
package cz.cuni.mff.ms.brodecva.botnicek.ide.check.code.controllers;

import java.net.URI;
import java.util.Map;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.Code;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.code.model.checker.CodeChecker;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.code.model.checker.DefaultCodeChecker;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.code.model.validator.CodeValidator;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.code.model.validator.DefaultCodeValidator;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.controllers.CheckController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.events.CheckEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.events.CheckListener;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.builder.Builder;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.checker.CheckResult;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.checker.Source;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.views.CheckView;
import cz.cuni.mff.ms.brodecva.botnicek.ide.project.events.BotSettingsChangedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.project.events.BotSettingsChangedListener;
import cz.cuni.mff.ms.brodecva.botnicek.ide.project.events.LanguageSettingsChangedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.project.events.LanguageSettingsChangedListener;
import cz.cuni.mff.ms.brodecva.botnicek.ide.project.events.SettingsChangedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.project.events.SettingsChangedListener;
import cz.cuni.mff.ms.brodecva.botnicek.ide.project.model.Project;
import cz.cuni.mff.ms.brodecva.botnicek.ide.project.model.Settings;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Callback;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.EventManager;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.mvc.AbstractController;
import cz.cuni.mff.ms.brodecva.botnicek.library.api.BotConfiguration;
import cz.cuni.mff.ms.brodecva.botnicek.library.api.LanguageConfiguration;

/**
 * Výchozí implementace řadiče pro validaci kódu šablony jazyka AIML.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class DefaultCodeValidationController extends AbstractController<CheckView> implements
        CheckController<Code> {

    /**
     * Posluchač událost provedené kontroly, který aktualizuje zaregistrované
     * pohledy novými výsledky.
     */
    private class DefaultCheckListener implements CheckListener {

        /*
         * (non-Javadoc)
         * 
         * @see cz.cuni.mff.ms.brodecva.botnicek.ide.edit.check.input.events.
         * CodeCheckListener
         * #checked(cz.cuni.mff.ms.brodecva.botnicek.ide.edit.check
         * .input.model.CodeCheckResult)
         */
        @Override
        public void checked(final CheckResult result) {
            callViews(new Callback<CheckView>() {

                @Override
                public void call(final CheckView view) {
                    Preconditions.checkNotNull(view);
                    
                    view.updateResult(result);
                }

            });
        }

    }
    
    private final class DefaultSettingsChangedListener implements
            SettingsChangedListener {

        /* (non-Javadoc)
         * @see cz.cuni.mff.ms.brodecva.botnicek.ide.project.events.SettingsChangedListener#changed(cz.cuni.mff.ms.brodecva.botnicek.ide.project.model.Settings)
         */
        @Override
        public void changed(final Settings settings) {
            Preconditions.checkNotNull(settings);

            DefaultCodeValidationController.this.validator = DefaultCodeValidator.create(DefaultCodeChecker.create(
                    validator.getBotSettings(), validator.getLanguageSettings(), settings.getNamespacesToPrefixes()),
                    getEventManager());
            
            repealViews();
        }
    }
    
    private final class DefaultLanguageSettingsChangedListener implements
            LanguageSettingsChangedListener {

        /* (non-Javadoc)
         * @see cz.cuni.mff.ms.brodecva.botnicek.ide.project.events.LanguageSettingsChangedListener#changed(cz.cuni.mff.ms.brodecva.botnicek.library.api.LanguageConfiguration)
         */
        @Override
        public void changed(final LanguageConfiguration settings) {
            Preconditions.checkNotNull(settings);

            DefaultCodeValidationController.this.validator = DefaultCodeValidator.create(DefaultCodeChecker.create(
                    validator.getBotSettings(), settings, validator.getNamespacesToPrefixes()),
                    getEventManager());
            
            repealViews();
        }

    }

    private final class DefaultBotSettingsChangedListener implements
            BotSettingsChangedListener {

        /* (non-Javadoc)
         * @see cz.cuni.mff.ms.brodecva.botnicek.ide.project.events.BotSettingsChangedListener#changed(cz.cuni.mff.ms.brodecva.botnicek.library.api.BotConfiguration)
         */
        @Override
        public void changed(final BotConfiguration settings) {
            Preconditions.checkNotNull(settings);

            DefaultCodeValidationController.this.validator = DefaultCodeValidator.create(DefaultCodeChecker.create(
                    settings, validator.getLanguageSettings(), validator.getNamespacesToPrefixes()),
                    getEventManager());
            
            repealViews();
        }

    }

    /**
     * Vytvoří řadič.
     * 
     * @param validator
     *            validátor provádějící vlastní validaci
     * @param eventManager
     *            správce událostí
     * @return řadič
     */
    public static DefaultCodeValidationController create(final CodeValidator validator,
            final EventManager eventManager) {
        final DefaultCodeValidationController newInstance =
                new DefaultCodeValidationController(validator, eventManager);

        newInstance.addListener(CheckEvent.class,
                newInstance.new DefaultCheckListener());

        return newInstance;
    }

    private CodeValidator validator;

    private DefaultCodeValidationController(final CodeValidator validator,
            final EventManager eventManager) {
        super(eventManager);

        Preconditions.checkNotNull(validator);

        this.validator = validator;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.controllers.CheckController
     * #clear(java.lang.Object)
     */
    @Override
    public void clear(final Object subject) {
        Preconditions.checkNotNull(subject);

        this.validator.clear(subject);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.controllers.CheckController
     * #check(java.lang.Object, java.lang.String)
     */
    @Override
    public void check(final Source client, final Object subject,
            final String value) {
        Preconditions.checkNotNull(client);
        Preconditions.checkNotNull(value);

        this.validator.validate(client, subject, value);
    }
    
    /**
     * Vytvoří řadič.
     * 
     * @param project projekt, ve kterém probíhá validace
     * @param botSettings
     *            nastavení bota
     * @param languageSettings
     *            nastavení jazyka
     * @param namespacesToPrefixes
     *            prefixy na prostory na jmen
     * @param eventManager
     *            správce událostí
     * 
     * @return řadič
     */
    public static DefaultCodeValidationController create(
            final Project project,
            final BotConfiguration botSettings,
            final LanguageConfiguration languageSettings,
            final Map<URI, String> namespacesToPrefixes, final EventManager eventManager) {
        return create(project, DefaultCodeValidator.create(DefaultCodeChecker.create(
                        botSettings, languageSettings, namespacesToPrefixes),
                        eventManager), eventManager);
    }

    /**
     * Vytvoří řadič.
     * @param project projekt, ve kterém probíhá validace
     * @param checker
     *            přímý validátor kódu
     * @param eventManager
     *            správce událostí
     * 
     * @return řadič
     */
    public static DefaultCodeValidationController create(
            final Project project, final CodeChecker checker, final EventManager eventManager) {
        return create(project,
                DefaultCodeValidator.create(checker, eventManager), eventManager);
    }

    /**
     * Vytvoří řadič.
     * 
     * @param project projekt, ve kterém probíhá validace
     * @param validator
     *            validátor kódu vysílající událost o výsledku
     * @param eventManager
     *            správce událostí
     * 
     * @return řadič
     */
    public static DefaultCodeValidationController create(
            final Project project, final CodeValidator validator, final EventManager eventManager) {
        Preconditions.checkNotNull(project);
        
        final DefaultCodeValidationController newInstance =
                new DefaultCodeValidationController(validator, eventManager);
        
        newInstance.addListener(CheckEvent.class, newInstance.new DefaultCheckListener());
        newInstance.addListener(SettingsChangedEvent.class, project, newInstance.new DefaultSettingsChangedListener());
        newInstance.addListener(LanguageSettingsChangedEvent.class, project, newInstance.new DefaultLanguageSettingsChangedListener());
        newInstance.addListener(BotSettingsChangedEvent.class, project, newInstance.new DefaultBotSettingsChangedListener());
        
        return newInstance;
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.controllers.CheckController#provideBuilder(java.lang.String)
     */
    @Override
    public Builder<Code> provideBuilder(String value) {
        Preconditions.checkNotNull(value);
        
        return this.validator.provideBuilder(value);
    }

    private void repealViews() {
        callViews(new Callback<CheckView>() {

            @Override
            public void call(final CheckView view) {
                Preconditions.checkNotNull(view);
                
                view.repeal();
            }

        });
    }
}
