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
package cz.cuni.mff.ms.brodecva.botnicek.ide.check.code.controllers;

import java.net.URI;
import java.util.Map;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.check.code.model.checker.CodeChecker;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.code.model.checker.DefaultCodeChecker;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.code.model.validator.CodeValidator;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.code.model.validator.DefaultCodeValidator;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.controllers.CheckController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.controllers.DefaultCheckController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.Source;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.views.CheckView;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.EventManager;
import cz.cuni.mff.ms.brodecva.botnicek.library.api.BotConfiguration;
import cz.cuni.mff.ms.brodecva.botnicek.library.api.LanguageConfiguration;

/**
 * Výchozí implementace řadiče pro validaci kódu šablony jazyka AIML.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class DefaultCodeValidationController implements CodeValidationController {

    /**
     * Vytvoří řadič.
     * 
     * @param botSettings nastavení bota
     * @param languageSettings nastavení jazyka
     * @param namespacesToPrefixes prefixy na prostory na jmen
     * @param eventManager správce událostí
     * @return řadič
     */
    public static DefaultCodeValidationController create(final BotConfiguration botSettings, final LanguageConfiguration languageSettings, final Map<URI, String> namespacesToPrefixes, final EventManager eventManager) {
        return create(DefaultCodeValidator.create(DefaultCodeChecker.create(botSettings, languageSettings, namespacesToPrefixes), eventManager), eventManager);
    }
    
    /**
     * Vytvoří řadič.
     * 
     * @param checker přímý validátor kódu
     * @param eventManager správce událostí
     * @return řadič
     */
    public static DefaultCodeValidationController create(final CodeChecker checker, final EventManager eventManager) {
        return create(DefaultCodeValidator.create(checker, eventManager), eventManager);
    }
    
    /**
     * Vytvoří řadič.
     * 
     * @param validator validátor kódu vysílající událost o výsledku
     * @param eventManager správce událostí
     * @return řadič
     */
    public static DefaultCodeValidationController create(final CodeValidator validator, final EventManager eventManager) {
        final DefaultCodeValidationController newInstance = new DefaultCodeValidationController(DefaultCheckController.create(validator, eventManager));
        
        return newInstance;
    }
    
    /**
     * Vytvoří řadič.
     * 
     * @param checkController implementující řadič pro validaci kódu šablony jazyka AIML
     * @return řadič
     */
    static DefaultCodeValidationController create(final CheckController checkController) {
        return new DefaultCodeValidationController(checkController);
    }

    private CheckController checkController;
    
    private DefaultCodeValidationController(final CheckController checkController) {
        Preconditions.checkNotNull(checkController);
        
        this.checkController = checkController;
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.edit.check.controllers.CheckController#check(java.lang.String)
     */
    @Override
    public void check(final Source client, Object subject, final String value) {
        Preconditions.checkNotNull(client);
        Preconditions.checkNotNull(value);
        
        this.checkController.check(client, subject, value);
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.utils.mvc.Controller#addView(java.lang.Object)
     */
    @Override
    public void addView(CheckView view) {
        this.checkController.addView(view);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.utils.mvc.Controller#removeView(java.lang.Object)
     */
    @Override
    public void removeView(CheckView view) {
        this.checkController.removeView(view);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.utils.mvc.Controller#fill(java.lang.Object)
     */
    @Override
    public void fill(CheckView view) {
        this.checkController.fill(view);
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.controllers.CheckController#clear(java.lang.Object)
     */
    @Override
    public void clear(final Object subject) {
        this.checkController.clear(subject);
    }
}
