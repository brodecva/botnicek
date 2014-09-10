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
package cz.cuni.mff.ms.brodecva.botnicek.ide.check.mixedpattern.controllers;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.check.code.model.checker.CodeChecker;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.controllers.CheckController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.controllers.DefaultCheckController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.Source;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.views.CheckView;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.mixedpattern.model.checker.DefaultMixedPatternChecker;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.mixedpattern.model.checker.MixedPatternChecker;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.mixedpattern.model.validator.DefaultMixedPatternValidator;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.mixedpattern.model.validator.MixedPatternValidator;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.EventManager;

/**
 * Výchozí implementace řadiče pro validaci složeného vzoru dle specifikace jazyka AIML.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public class DefaultMixedPatternValidationController implements MixedPatternValidationController {

    /**
     * Vytvoří řadič.
     * 
     * @param codeChecker validátor kódu šablony, slouží pro validaci prvků bot ve vzoru, které se jinak vyskytují i v šablonách
     * @param eventManager správce událostí
     * 
     * @return řadič
     */
    public static DefaultMixedPatternValidationController create(final CodeChecker codeChecker, final EventManager eventManager) {
        return create(DefaultMixedPatternValidator.create(DefaultMixedPatternChecker.create(codeChecker), eventManager), eventManager);
    }
    
    /**
     * Vytvoří řadič.
     * 
     * @param checker přímý validátor
     * @param eventManager správce událostí
     * @return řadič
     */
    public static DefaultMixedPatternValidationController create(final MixedPatternChecker checker, final EventManager eventManager) {
        return create(DefaultMixedPatternValidator.create(checker, eventManager), eventManager);
    }
    
    /**
     * Vytvoří řadič.
     * 
     * @param validator vysílací validátor
     * @param eventManager správce událostí
     * @return řadič
     */
    public static DefaultMixedPatternValidationController create(final MixedPatternValidator validator, final EventManager eventManager) {
        return new DefaultMixedPatternValidationController(DefaultCheckController.create(validator, eventManager));
    }
    
    /**
     * Vytvoří řadič.
     * 
     * @param checkController implementující řadič (validuje složený vzor)
     * @return řadič
     */
    static DefaultMixedPatternValidationController create(final CheckController checkController) {
        return new DefaultMixedPatternValidationController(checkController);
    }

    private final CheckController checkController;
    
    private DefaultMixedPatternValidationController(final CheckController checkController) {
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
