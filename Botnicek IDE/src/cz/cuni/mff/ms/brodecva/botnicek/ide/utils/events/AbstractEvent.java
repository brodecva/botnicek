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
package cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events;

import com.google.common.base.Preconditions;

/**
 * Abstraktní událost.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @param <L>
 *            typ posluchače
 */
public abstract class AbstractEvent<L> implements Event<L> {

    /**
     * Konstruktor běžné události.
     */
    protected AbstractEvent() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.Visitable#accept(cz
     * .cuni.mff.ms.brodecva.botnicek.ide.utils.events.Visitor)
     */
    @Override
    public final void accept(final Visitor visitor) {
        Preconditions.checkNotNull(visitor);

        visitor.visit(this);
    }
}
