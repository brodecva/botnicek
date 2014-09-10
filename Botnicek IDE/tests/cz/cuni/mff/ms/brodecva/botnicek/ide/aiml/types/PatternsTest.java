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
package cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types;

import static org.junit.Assert.*;

import org.easymock.EasyMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.CheckResult;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.simplepattern.model.checker.SimplePatternChecker;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.AIML;

/**
 * Testuje pomocné metody pro práci se vzory jazyka AIML.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see Patterns
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(Patterns.class)
public class PatternsTest {

    private static final String VALID_PATTERN_TEXT = "* CREATED *";
    private static final String INVALID_PATTERN_TEXT = ".; rer *";
    private static final String INVALID_RESULT_MESSAGE = "Invalid";

    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.Patterns#create(String)}.
     */
    @Test
    public void testCreateWhenValid() {
        final CheckResult validCheckResultStub = EasyMock.createNiceMock(CheckResult.class);
        EasyMock.expect(validCheckResultStub.isValid()).andStubReturn(true);
        EasyMock.replay(validCheckResultStub);
        
        final SimplePatternChecker checkerStub = EasyMock.createStrictMock(SimplePatternChecker.class);
        EasyMock.expect(checkerStub.check(VALID_PATTERN_TEXT)).andStubReturn(validCheckResultStub);
        EasyMock.replay(checkerStub);
        Whitebox.setInternalState(Patterns.class, "checker", checkerStub);
        
        assertEquals(VALID_PATTERN_TEXT, Patterns.create(VALID_PATTERN_TEXT).getText());
        
        EasyMock.verify(validCheckResultStub);
        EasyMock.verify(checkerStub);
    }
    
    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.Patterns#create(String)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateWhenInvalid() {
        final CheckResult invalidCheckResultStub = EasyMock.createNiceMock(CheckResult.class);
        EasyMock.expect(invalidCheckResultStub.isValid()).andStubReturn(false);
        EasyMock.expect(invalidCheckResultStub.getMessage()).andStubReturn(INVALID_RESULT_MESSAGE);
        EasyMock.replay(invalidCheckResultStub);
        
        final SimplePatternChecker checkerStub = EasyMock.createStrictMock(SimplePatternChecker.class);
        EasyMock.expect(checkerStub.check(INVALID_PATTERN_TEXT)).andStubReturn(invalidCheckResultStub);
        EasyMock.replay(checkerStub);
        Whitebox.setInternalState(Patterns.class, "checker", checkerStub);
        
        try {
            Patterns.create(INVALID_PATTERN_TEXT);
        } finally {
            EasyMock.verify(invalidCheckResultStub);
            EasyMock.verify(checkerStub);
        }
    }
    
    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.Codes#createEmpty()}.
     */
    @Test
    public void testCreateEmpty() {
        assertEquals(AIML.STAR_WILDCARD.getValue(), Patterns.createUniversal().getText());
    }
}
