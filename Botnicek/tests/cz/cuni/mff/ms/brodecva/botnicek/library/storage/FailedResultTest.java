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

import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import cz.cuni.mff.ms.brodecva.botnicek.library.utils.test.UnitTest;

/**
 * Testuje neúspěšný výsledek hledání.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see FailedResult
 */
@Category(UnitTest.class)
public final class FailedResultTest {

    /**
     * Mock označení části.
     */
    private static PartMarker partMock = null;

    /**
     * Mock části vstupní cesty.
     */
    private static InputPath pathMock = null;

    /**
     * Neúspěšný výsledek.
     */
    private FailedResult result = null;

    /**
     * Vytvoří mocky.
     */
    @BeforeClass
    public static void setUpBeforeClass() {
        partMock = EasyMock.createNiceMock(PartMarker.class);
        replay(partMock);

        pathMock = EasyMock.createNiceMock(InputPath.class);
        replay(pathMock);
    }

    /**
     * Vytvoří výsledek.
     */
    @Before
    public void setUp() {
        result = new FailedResult();
        result.addStarMatchedPart(partMock, pathMock);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.FailedResult#FailedResult()}
     * .
     */
    @Test
    public void testFailedResult() {
        new FailedResult();
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.FailedResult#isSuccesful()}
     * .
     */
    @Test
    public void testIsSuccesful() {
        assertFalse(result.isSuccesful());
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.FailedResult#getTemplate()}
     * .
     */
    @Test
    public void testGetTemplate() {
        assertNull(result.getTemplate());
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.FailedResult#addStarMatchedPart(cz.cuni.mff.ms.brodecva.botnicek.library.storage.PartMarker, cz.cuni.mff.ms.brodecva.botnicek.library.storage.InputPath)}
     * a
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.FailedResult#getStarMatchedParts(cz.cuni.mff.ms.brodecva.botnicek.library.storage.PartMarker)}
     * .
     */
    @Test
    public void testAddAndGetStarMatchedParts() {
        result.addStarMatchedPart(partMock, pathMock);
        assertEquals(0, result.getStarMatchedParts(partMock).size());
    }

}
