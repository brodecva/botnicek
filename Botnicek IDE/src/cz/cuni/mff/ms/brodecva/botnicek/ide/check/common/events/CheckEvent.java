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
package cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.events;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.checker.CheckResult;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.AbstractEvent;

/**
 * Událost provedení kontroly.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class CheckEvent extends AbstractEvent<CheckListener> {

    /**
     * Vytvoří událost.
     * 
     * @param result
     *            výsledek kontroly
     * @return událost
     */
    public static CheckEvent create(final CheckResult result) {
        return new CheckEvent(result);
    }

    private final CheckResult result;

    private CheckEvent(final CheckResult result) {
        Preconditions.checkNotNull(result);

        this.result = result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.utils.Event#dispatchTo(java.lang
     * .Object)
     */
    @Override
    public void dispatchTo(final CheckListener listener) {
        Preconditions.checkNotNull(listener);

        listener.checked(this.result);
    }
}
