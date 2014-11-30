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
package cz.cuni.mff.ms.brodecva.botnicek.ide.check.simplepattern.controllers;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.SimplePattern;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.controllers.CheckController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.controllers.DefaultCheckController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.builder.Builder;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.checker.Checker;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.checker.Source;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.validator.Validator;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.views.CheckView;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.simplepattern.model.checker.DefaultSimplePatternChecker;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.simplepattern.model.validator.DefaultSimplePatternValidator;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.EventManager;

/**
 * Výchozí implementace řadiče pro validaci prostého vzoru dle specifikace
 * jazyka AIML.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public class DefaultSimplePatternValidationController implements
        CheckController<SimplePattern> {

    /**
     * Vytvoří řadič.
     * 
     * @param eventManager
     *            správce událostí
     * @return řadič
     */
    public static DefaultSimplePatternValidationController create(
            final EventManager eventManager) {
        return create(DefaultSimplePatternValidator.create(
                DefaultSimplePatternChecker.create(), eventManager),
                eventManager);
    }

    /**
     * Vytvoří řadič.
     * 
     * @param checkController
     *            implementující řadič (validuje prostý vzor)
     * @return řadič
     */
    static DefaultSimplePatternValidationController create(
            final CheckController<SimplePattern> checkController) {
        return new DefaultSimplePatternValidationController(checkController);
    }

    /**
     * Vytvoří řadič.
     * 
     * @param checker
     *            přímý validátor
     * @param eventManager
     *            správce událostí
     * @return řadič
     */
    public static DefaultSimplePatternValidationController
            create(final Checker checker,
                    final EventManager eventManager) {
        return create(
                DefaultSimplePatternValidator.create(checker, eventManager),
                eventManager);
    }

    /**
     * Vytvoří řadič.
     * 
     * @param validator
     *            vysílací validátor
     * @param eventManager
     *            správce událostí
     * @return řadič
     */
    public static DefaultSimplePatternValidationController create(
            final Validator<SimplePattern> validator,
            final EventManager eventManager) {
        return create(DefaultCheckController.create(validator, eventManager));
    }

    private final CheckController<SimplePattern> checkController;

    private DefaultSimplePatternValidationController(
            final CheckController<SimplePattern> checkController) {
        Preconditions.checkNotNull(checkController);

        this.checkController = checkController;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.utils.mvc.Controller#addView(java
     * .lang.Object)
     */
    @Override
    public void addView(final CheckView view) {
        this.checkController.addView(view);
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
        this.checkController.clear(subject);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.utils.mvc.Controller#fill(java.lang
     * .Object)
     */
    @Override
    public void fill(final CheckView view) {
        this.checkController.fill(view);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.edit.check.controllers.CheckController
     * #check(java.lang.String)
     */
    @Override
    public void check(final Source client, final Object subject,
            final String value) {
        Preconditions.checkNotNull(value);

        this.checkController.check(client, subject, value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.utils.mvc.Controller#removeView(
     * java.lang.Object)
     */
    @Override
    public void removeView(final CheckView view) {
        this.checkController.removeView(view);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.controllers.CheckController#provideBuilder(java.lang.String)
     */
    @Override
    public Builder<SimplePattern> provideBuilder(String value) {
        Preconditions.checkNotNull(value);
        
        return this.checkController.provideBuilder(value);
    }
}
