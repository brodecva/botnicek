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
package cz.cuni.mff.ms.brodecva.botnicek.ide.check.words.controllers;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.controllers.CheckController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.controllers.DefaultCheckController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.Source;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.views.CheckView;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.words.model.checker.DefaultNormalWordChecker;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.words.model.checker.NormalWordChecker;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.words.model.validator.DefaultNormalWordValidator;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.words.model.validator.NormalWordValidator;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NamingAuthority;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.EventManager;

/**
 * Výchozí implementace řadiče pro validaci normálního slova dle specifikace
 * jazyka AIML.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class DefaultNormalWordValidationController implements
        NormalWordValidationController {

    /**
     * Vytvoří řadič.
     * 
     * @param checkController
     *            implementující řadič (validuje normální slovo)
     * @return řadič
     */
    public static DefaultNormalWordValidationController create(
            final CheckController checkController) {
        return new DefaultNormalWordValidationController(checkController);
    }

    /**
     * Vytvoří řadič.
     * 
     * @param namingAuthority
     *            autorita pro přidělování unikátních řetězců
     * @param eventManager
     *            správce událostí
     * @return řadič
     */
    public static DefaultNormalWordValidationController create(
            final NamingAuthority namingAuthority,
            final EventManager eventManager) {
        return create(
                DefaultNormalWordValidator.create(
                        DefaultNormalWordChecker.create(namingAuthority),
                        eventManager), eventManager);
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
    public static DefaultNormalWordValidationController create(
            final NormalWordChecker checker, final EventManager eventManager) {
        return create(DefaultNormalWordValidator.create(checker, eventManager),
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
    public static DefaultNormalWordValidationController
            create(final NormalWordValidator validator,
                    final EventManager eventManager) {
        Preconditions.checkNotNull(validator);
        Preconditions.checkNotNull(eventManager);

        return create(DefaultCheckController.create(validator, eventManager));
    }

    private final CheckController checkController;

    private DefaultNormalWordValidationController(
            final CheckController checkController) {
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
     * cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.controllers.CheckController
     * #check(cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.Source,
     * java.lang.Object, java.lang.String)
     */
    @Override
    public void check(final Source client, final Object subject,
            final String value) {
        Preconditions.checkNotNull(client);
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
}
