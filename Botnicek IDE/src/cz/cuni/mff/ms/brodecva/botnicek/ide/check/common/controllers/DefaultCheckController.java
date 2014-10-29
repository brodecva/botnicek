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
package cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.controllers;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.events.CheckEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.events.CheckListener;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.builder.Builder;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.checker.CheckResult;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.checker.Source;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.validator.Validator;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.views.CheckView;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Callback;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.EventManager;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.mvc.AbstractController;

/**
 * Výchozí implementace řadiče kontroly obsahuje validátor, který provádí
 * vlastní validaci.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @param <T> validovaný typ
 */
public final class DefaultCheckController<T> extends AbstractController<CheckView>
        implements CheckController<T> {

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
                    view.updateResult(result);
                }

            });
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
    public static <T> DefaultCheckController<T> create(final Validator<T> validator,
            final EventManager eventManager) {
        final DefaultCheckController<T> newInstance =
                new DefaultCheckController<T>(validator, eventManager);

        newInstance.addListener(CheckEvent.class,
                newInstance.new DefaultCheckListener());

        return newInstance;
    }

    private final Validator<T> validator;

    private DefaultCheckController(final Validator<T> validator,
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

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.controllers.CheckController#provideBuilder(java.lang.String)
     */
    @Override
    public Builder<T> provideBuilder(final String value) {
        Preconditions.checkNotNull(value);
        
        return this.validator.provideBuilder(value);
    }
}
