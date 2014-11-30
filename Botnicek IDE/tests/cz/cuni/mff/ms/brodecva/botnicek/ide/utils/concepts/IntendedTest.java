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
package cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts;

import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import cz.cuni.mff.ms.brodecva.botnicek.library.utils.test.UnitTest;

/**
 * Testuje zamýšlená užití prvků jazyka.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see Intended
 */
@Category(UnitTest.class)
public class IntendedTest {

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Intended#arrayNull()}
     * .
     */
    @Test
    public void testArrayNullWhenConstrained() {
        assertNull(Intended.<String> arrayNull());
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Intended#arrayNull()}
     * .
     */
    @Test
    public void testArrayNullWhenUnconstrained() {
        assertNull(Intended.arrayNull());
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Intended#booleanArrayNull()}
     * .
     */
    @Test
    public void testBooleanArrayNull() {
        assertNull(Intended.booleanArrayNull());
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Intended#byteArrayNull()}
     * .
     */
    @Test
    public void testByteArrayNull() {
        assertNull(Intended.byteArrayNull());
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Intended#doubleArrayNull()}
     * .
     */
    @Test
    public void testDoubleArrayNull() {
        assertNull(Intended.doubleArrayNull());
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Intended#floatArrayNull()}
     * .
     */
    @Test
    public void testFloatArrayNull() {
        assertNull(Intended.floatArrayNull());
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Intended#charArrayNull()}
     * .
     */
    @Test
    public void testCharArrayNull() {
        assertNull(Intended.charArrayNull());
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Intended#intArrayNull()}
     * .
     */
    @Test
    public void testIntArrayNull() {
        assertNull(Intended.intArrayNull());
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Intended#longArrayNull()}
     * .
     */
    @Test
    public void testLongArrayNull() {
        assertNull(Intended.longArrayNull());
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Intended#nullReference()}
     * .
     */
    @Test
    public void testNullReferenceWhenConstrained() {
        assertNull(Intended.<String> nullReference());
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Intended#nullReference()}
     * .
     */
    @Test
    public void testNullReferenceWhenUnconstrained() {
        assertNull(Intended.nullReference());
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Intended#shortArrayNull()}
     * .
     */
    @Test
    public void testShortArrayNull() {
        assertNull(Intended.shortArrayNull());
    }

}
