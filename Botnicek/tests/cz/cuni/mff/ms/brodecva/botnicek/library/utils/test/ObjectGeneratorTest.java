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
package cz.cuni.mff.ms.brodecva.botnicek.library.utils.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.experimental.categories.Category;


/**
 * Test nástrojů k testování.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see ObjectGenerator
 */
@Category(UnitTest.class)
public final class ObjectGeneratorTest {

    /**
     * Počet kopií.
     */
    private static final int COUNT = 10;

    /**
     * Otestuje neekvivalenci.
     * 
     * @param objects
     *            objekty
     */
    private static void testAllNotEqualInPairs(final Object[] objects) {
        for (int i = 0; i < objects.length; i++) {
            for (int j = 0; j < objects.length; j++) {
                if (i == j) {
                    continue;
                }

                assertThat(objects[i], not(equalTo(objects[j])));
            }
        }
    }

    /**
     * Otestuje ekvivalenci.
     * 
     * @param objects
     *            objekty
     */
    private static void testAllEqual(final Object[] objects) {
        final Object prototype = objects[0];

        for (final Object current : objects) {
            assertEquals(prototype, current);
        }
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.test.ObjectGenerator#getUniqueObjects(int)}
     * .
     */
    @Test
    public void testGetUniqueObjectsALLNotEqualInPairs() {
        final Object[] objects = ObjectGenerator.getUniqueObjects(COUNT);

        testAllNotEqualInPairs(objects);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.test.ObjectGenerator#getEqualObjects(int)}
     * .
     */
    @Test
    public void testGetEqualObjectsAllEqual() {
        final Object[] objects = ObjectGenerator.getEqualObjects(COUNT);

        testAllEqual(objects);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.test.ObjectGenerator#getUniqueStrings(int)}
     * .
     */
    @Test
    public void testGetUniqueStringsALLNotEqualInPairs() {
        final String[] objects = ObjectGenerator.getUniqueStrings(COUNT);

        testAllNotEqualInPairs(objects);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.test.ObjectGenerator#getEqualStrings(int)}
     * .
     */
    @Test
    public void testGetEqualStringsAllEqual() {
        final String[] objects = ObjectGenerator.getEqualStrings(COUNT);

        testAllEqual(objects);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.test.ObjectGenerator#copyAndReplaceOneInEvery(Object[], Object)}
     * .
     */
    @Test(expected = NullPointerException.class)
    public void testCopyAndReplaceOneInEveryWhenArrayNull() {
        ObjectGenerator.copyAndReplaceOneInEvery(null, "dummy");
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.test.ObjectGenerator#copyAndReplaceOneInEvery(Object[], Object)}
     * .
     */
    @Test
    public void testCopyAndReplaceOneInEveryWhenArrayEmptyReturnsEmptyArray() {
        assertArrayEquals(new Object[0], ObjectGenerator.copyAndReplaceOneInEvery(new Object[0], "dummy"));
    }
    
    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.test.ObjectGenerator#copyAndReplaceOneInEvery(Object[], Object)}
     * .
     */
    @Test
    public void testCopyAndReplaceOneInEveryWhenArrayEmptyReturnsCopiesWithReplacement() {
        final Object[] input = new String[] { "first", "second", "third", "fourth" };
        final Object[][] expectedResult = new Object[][] {
                new Object[] { "replacement", "second", "third", "fourth" },
                new Object[] { "first", "replacement", "third", "fourth" },
                new Object[] { "first", "second", "replacement", "fourth" },
                new Object[] { "first", "second", "third", "replacement" },
        };
        
        assertArrayEquals(expectedResult, ObjectGenerator.copyAndReplaceOneInEvery(input, "replacement"));
    }
}
