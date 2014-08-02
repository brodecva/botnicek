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
package cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;

/**
 * Pomocné metody porovnávání objektů.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class Comparisons {

    /**
     * Zjistí, zda-li jsou prvky navzájem různé.
     * 
     * @param elements prvky
     * @return zda-li jsou všechny prvky navzájem různé
     */
    @SafeVarargs
    public static <E> boolean allDifferent(final E... elements) {
        Preconditions.checkNotNull(elements);
        
        return ImmutableSet.copyOf(elements).size() == elements.length;
    }

}
