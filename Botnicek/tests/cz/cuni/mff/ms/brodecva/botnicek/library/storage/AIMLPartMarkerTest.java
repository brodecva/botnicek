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
package cz.cuni.mff.ms.brodecva.botnicek.library.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import cz.cuni.mff.ms.brodecva.botnicek.library.utils.test.UnitTest;

/**
 * Testuje oddělovače cesty pro AIML.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see AIMLPartMarker
 */
@Category(UnitTest.class)
public final class AIMLPartMarkerTest {

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.AIMLPartMarker#allValues()}
     * .
     */
    @Test
    public void testAllValuesReturnsArrayEqualToStaticVersion() {
        final Set<PartMarker> allValues = AIMLPartMarker.PATTERN.allValues();

        for (final AIMLPartMarker marker : AIMLPartMarker.values()) {
            assertTrue(allValues.contains(marker));
        }

        assertEquals(AIMLPartMarker.values().length, allValues.size());
    }

}
