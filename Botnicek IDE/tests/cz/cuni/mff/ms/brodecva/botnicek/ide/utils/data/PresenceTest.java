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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.google.common.collect.ImmutableMap;

import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Intended;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.test.UnitTest;

/**
 * Testuje pomocné metody pro práci s návratovými hodnotami či argumenty metod,
 * jež vrací {@code null} jako indikátor nepřítomnosti výsledku či argumentu.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see Presence
 */
@Category(UnitTest.class)
public class PresenceTest {

    private static Map<String, String> map = Intended.nullReference();

    /**
     * Nastaví neměnné testovací zobrazení.
     * 
     * @throws java.lang.Exception
     *             pokud dojde k vyhození výjimky
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        map = ImmutableMap.of("key", "value");
    }

    /**
     * Uklidí neměnné testovací zobrazení.
     * 
     * @throws java.lang.Exception
     *             pokud dojde k vyhození výjimky
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        map = Intended.nullReference();
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.Presence#isAbsent(java.lang.Object)}
     * .
     */
    @Test
    public void testIsAbsentWhenNotPresentReturnsTrue() {
        assertTrue(Presence.isAbsent(map.get("otherKey")));
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.Presence#isAbsent(java.lang.Object)}
     * .
     */
    @Test
    public void testIsAbsentWhenPresentReturnsFalse() {
        assertFalse(Presence.isAbsent(map.get("key")));
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.Presence#isPresent(java.lang.Object)}
     * .
     */
    @Test
    public void testIsPresentWhenNotPresentReturnsFalse() {
        assertFalse(Presence.isPresent(map.get("otherKey")));
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.Presence#isPresent(java.lang.Object)}
     * .
     */
    @Test
    public void testIsPresentWhenPresentReturnsTrue() {
        assertTrue(Presence.isPresent(map.get("key")));
    }

}
