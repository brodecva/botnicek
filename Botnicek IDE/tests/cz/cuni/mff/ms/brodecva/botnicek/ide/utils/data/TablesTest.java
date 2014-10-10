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

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;

import cz.cuni.mff.ms.brodecva.botnicek.library.utils.test.UnitTest;

/**
 * Testuje pomocné metody pro práci s tabulkami.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see Tables
 */
@Category(UnitTest.class)
public class TablesTest {

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.Tables#toImmutableTable(java.util.Map)}
     * .
     */
    @Test
    public void testToImmutableTableSymetry() {
        final Table<String, Integer, Boolean> left = HashBasedTable.create();
        left.put("one", 1, true);
        left.put("two", 2, false);
        left.put("three", 3, true);

        assertEquals(left, Tables.toImmutableTable(left.rowMap()));
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.Tables#toImmutableTable(java.util.Map)}
     * .
     */
    @Test
    public void testToImmutableTableWhenEmpty() {
        assertEquals(ImmutableTable.<String, Integer, Boolean> of(),
                Tables.toImmutableTable(ImmutableMap
                        .<String, Map<Integer, Boolean>> of()));
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.Tables#toImmutableTable(java.util.Map)}
     * .
     */
    @Test
    public void testToImmutableTableWhenMultiple() {
        final Table<String, Integer, Boolean> left = HashBasedTable.create();
        left.put("one", 1, true);
        left.put("two", 2, false);
        left.put("three", 3, true);

        final Map<String, Map<Integer, Boolean>> right = new HashMap<>();
        right.put("one", ImmutableMap.of(1, true));
        right.put("two", ImmutableMap.of(2, false));
        right.put("three", ImmutableMap.of(3, true));

        assertEquals(left, Tables.toImmutableTable(right));
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.Tables#toImmutableTable(java.util.Map)}
     * .
     */
    @Test
    public void testToImmutableTableWhenSingleCell() {
        assertEquals(ImmutableTable.<String, Integer, Boolean> of("key", 5,
                false), Tables.toImmutableTable(ImmutableMap
                .<String, Map<Integer, Boolean>> of("key",
                        ImmutableMap.of(5, false))));
    }

}
