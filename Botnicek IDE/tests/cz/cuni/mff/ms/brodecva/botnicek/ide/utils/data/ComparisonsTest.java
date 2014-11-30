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
package cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.google.common.collect.ObjectArrays;

import cz.cuni.mff.ms.brodecva.botnicek.library.utils.test.ObjectGenerator;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.test.UnitTest;

/**
 * Testuje pomocné metody porovnávání objektů.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see Comparisons
 */
@Category(UnitTest.class)
public class ComparisonsTest {

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.Comparisons#allDifferent(Object...)}
     * .
     */
    @Test
    public void testAllDifferentWhenAllDifferentReturnsTrue() {
        assertTrue(Comparisons.allDifferent(ObjectGenerator
                .getUniqueObjects(10)));
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.Comparisons#allDifferent(Object...)}
     * .
     */
    @Test
    public void testAllDifferentWhenAllEqualReturnsFalse() {
        assertFalse(Comparisons.allDifferent(ObjectGenerator
                .getEqualObjects(10)));
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.Comparisons#allDifferent(Object...)}
     * .
     */
    @Test
    public void testAllDifferentWhenAutoboxedEqualReturnsFalse() {
        assertFalse(Comparisons.allDifferent(5, 5, 5));
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.Comparisons#allDifferent(Object...)}
     * .
     */
    @Test
    public void testAllDifferentWhenAutoboxedUniqueReturnsTrue() {
        assertTrue(Comparisons.allDifferent(5, 6, 9));
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.Comparisons#allDifferent(Object...)}
     * .
     */
    @Test
    public void testAllDifferentWhenEmptyReturnsTrue() {
        assertTrue(Comparisons.allDifferent());
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.Comparisons#allDifferent(Object...)}
     * .
     */
    @Test
    public void testAllDifferentWhenOnlyOneReturnsTrue() {
        assertTrue(Comparisons.allDifferent(new Object()));
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.Comparisons#allDifferent(Object...)}
     * .
     */
    @Test
    public void testAllDifferentWhenSomeEqualReturnsFalse() {
        final Object[] allDifferent = ObjectGenerator.getUniqueObjects(10);
        final Object copy = allDifferent[4];

        assertFalse(Comparisons.allDifferent(ObjectArrays.concat(allDifferent,
                copy)));
    }

}
