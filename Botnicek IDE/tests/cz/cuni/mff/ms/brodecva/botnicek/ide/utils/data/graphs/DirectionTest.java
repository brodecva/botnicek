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
package cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import cz.cuni.mff.ms.brodecva.botnicek.library.utils.test.UnitTest;

/**
 * Testuje orientaci hrany grafu vůči uzlu.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see Direction
 */
@Category(UnitTest.class)
public class DirectionTest {

    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.Direction#getOpposite(cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.Direction)}.
     */
    @Test
    public void testGetOppositeDirection() {
        assertEquals(Direction.OUT, Direction.getOpposite(Direction.IN));
        assertEquals(Direction.IN, Direction.getOpposite(Direction.OUT));
    }

    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.Direction#getOpposite()}.
     */
    @Test
    public void testGetOpposite() {
        assertEquals(Direction.OUT, Direction.IN.getOpposite());
        assertEquals(Direction.IN, Direction.OUT.getOpposite());
    }

}
