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
package cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design;

import java.util.Set;

import com.google.common.collect.Sets;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public enum Direction {
    IN, OUT;
    
    public static Direction getOpposite(final Direction direction) {
        final Direction[] values = values();
        assert values.length == 2;
        
        final Set<Direction> valuesSet = Sets.newHashSet(values);
        valuesSet.remove(direction);
        
        return valuesSet.iterator().next();
    }
}
