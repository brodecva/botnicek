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
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.CheckResult;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.Source;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.words.model.checker.NormalWordChecker;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Intended;
import cz.cuni.mff.ms.brodecva.botnicek.library.preprocessor.Normalizer;

/**
 * Testuje pomocné metody pro práci s normálními slovy.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see NormalWords
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(NormalWords.class)
public class NormalWordsTest {

    private static final String CREATED_WORD_VALID_NAME = "CREATED";
    private static final String CREATED_WORD_FIXABLE_NAME = "cReATeD";
    private static final String CREATED_WORD_NOT_FIXABLE_NAME = ".;'#";
    private static final String INVALID_RESULT_MESSAGE = "Invalid";
    private static final String FIRST_WORD_TEXT = "FIRST";
    private static final String SECOND_WORD_TEXT = "SECOND";
    
    private NormalWord firstWordStub = Intended.nullReference();
    private NormalWord secondWordStub = Intended.nullReference();
    private Object firstValueDummy = Intended.nullReference();
    private Object secondValueDummy = Intended.nullReference();

    /**
     * Vytvoří testovací objekty.
     * 
     * @throws java.lang.Exception pokud dojde k vyhození výjimky
     */
    @Before
    public void setUp() throws Exception {
        firstWordStub = EasyMock.createStrictMock(NormalWord.class);
        EasyMock.expect(firstWordStub.getText()).andStubReturn(FIRST_WORD_TEXT);
        EasyMock.replay(firstWordStub);
        
        secondWordStub = EasyMock.createStrictMock(NormalWord.class);
        EasyMock.expect(secondWordStub.getText()).andStubReturn(SECOND_WORD_TEXT);
        EasyMock.replay(secondWordStub);
        
        firstValueDummy = new Object();
        secondValueDummy = new Object();
    }

    /**
     * Uklidí testovací objekty.
     * 
     * @throws java.lang.Exception pokud dojde k vyhození výjimky
     */
    @After
    public void tearDown() throws Exception {
        firstWordStub = Intended.nullReference();
        secondWordStub = Intended.nullReference();
        
        firstValueDummy = Intended.nullReference();
        secondValueDummy = Intended.nullReference();
    }
    
    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWords#of(String)}.
     */
    @Test
    public void testOfWhenValid() {
        final CheckResult validCheckResultStub = EasyMock.createNiceMock(CheckResult.class);
        EasyMock.expect(validCheckResultStub.isValid()).andStubReturn(true);
        EasyMock.replay(validCheckResultStub);
        
        final NormalWordChecker checkerStub = EasyMock.createStrictMock(NormalWordChecker.class);
        EasyMock.expect(checkerStub.check(EasyMock.notNull(Source.class), EasyMock.notNull(), EasyMock.eq(CREATED_WORD_VALID_NAME))).andStubReturn(validCheckResultStub);
        EasyMock.replay(checkerStub);
        Whitebox.setInternalState(NormalWords.class, "checker", checkerStub);
        
        assertEquals(CREATED_WORD_VALID_NAME, NormalWords.of(CREATED_WORD_VALID_NAME).getText());
        
        EasyMock.verify(validCheckResultStub);
        EasyMock.verify(checkerStub);
    }
    
    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWords#of(String)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOfWhenInvalid() {
        final CheckResult invalidCheckResultStub = EasyMock.createNiceMock(CheckResult.class);
        EasyMock.expect(invalidCheckResultStub.isValid()).andStubReturn(false);
        EasyMock.expect(invalidCheckResultStub.getMessage()).andStubReturn(INVALID_RESULT_MESSAGE);
        EasyMock.replay(invalidCheckResultStub);
        
        final NormalWordChecker checkerStub = EasyMock.createStrictMock(NormalWordChecker.class);
        EasyMock.expect(checkerStub.check(EasyMock.notNull(Source.class), EasyMock.notNull(), EasyMock.eq(CREATED_WORD_NOT_FIXABLE_NAME))).andStubReturn(invalidCheckResultStub);
        EasyMock.replay(checkerStub);
        Whitebox.setInternalState(NormalWords.class, "checker", checkerStub);
        
        NormalWords.of(CREATED_WORD_NOT_FIXABLE_NAME);
        
        EasyMock.verify(invalidCheckResultStub);
        EasyMock.verify(checkerStub);
    }
    
    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWords#from(String)}.
     */
    @Test
    public void testFromWhenValid() {
        final Normalizer normalizerStub = EasyMock.createStrictMock(Normalizer.class);
        EasyMock.expect(normalizerStub.convertToNormalChars(CREATED_WORD_VALID_NAME)).andStubReturn(CREATED_WORD_VALID_NAME);
        EasyMock.replay(normalizerStub);
        Whitebox.setInternalState(NormalWords.class, "normalizer", normalizerStub);
        
        assertEquals(CREATED_WORD_VALID_NAME, NormalWords.from(CREATED_WORD_VALID_NAME).getText());
        
        EasyMock.verify(normalizerStub);
    }
    
    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWords#from(String)}.
     */
    @Test
    public void testFromWhenFixable() {
        final Normalizer normalizerStub = EasyMock.createStrictMock(Normalizer.class);
        EasyMock.expect(normalizerStub.convertToNormalChars(CREATED_WORD_FIXABLE_NAME)).andStubReturn(CREATED_WORD_VALID_NAME);
        EasyMock.replay(normalizerStub);
        Whitebox.setInternalState(NormalWords.class, "normalizer", normalizerStub);
        
        assertEquals(CREATED_WORD_VALID_NAME, NormalWords.from(CREATED_WORD_FIXABLE_NAME).getText());
        
        EasyMock.verify(normalizerStub);
    }
    
    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWords#from(String)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFromWhenNotFixable() {
        final Normalizer normalizerStub = EasyMock.createStrictMock(Normalizer.class);
        EasyMock.expect(normalizerStub.convertToNormalChars(CREATED_WORD_NOT_FIXABLE_NAME)).andStubReturn("");
        EasyMock.replay(normalizerStub);
        Whitebox.setInternalState(NormalWords.class, "normalizer", normalizerStub);
        
        NormalWords.from(CREATED_WORD_NOT_FIXABLE_NAME);
        
        EasyMock.verify(normalizerStub);
    }

    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWords#join(java.util.List)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testJoinListOfNormalWordWhenEmpty() {
        NormalWords.join(ImmutableList.<NormalWord>of());
    }
    
    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWords#join(java.util.List)}.
     */
    @Test
    public void testJoinListOfNormalWordWhenNotEmpty() {
        final NormalWord resultDummy = EasyMock.createMock(NormalWord.class);
        EasyMock.replay(resultDummy);
        
        PowerMock.mockStaticPartial(NormalWords.class, "of");
        EasyMock.expect(NormalWords.of("FIRSTSECOND")).andReturn(resultDummy);
        PowerMock.replay(NormalWords.class);
        
        NormalWords.join(ImmutableList.of(firstWordStub, secondWordStub));
        
        PowerMock.verify(NormalWords.class);
        EasyMock.verify(firstWordStub);
        EasyMock.verify(secondWordStub);
        EasyMock.verify(resultDummy);
    }

    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWords#toUntyped(java.util.Map)}.
     */
    @Test
    public void testToUntyped() {        
        assertEquals(ImmutableMap.of(FIRST_WORD_TEXT, firstValueDummy, SECOND_WORD_TEXT, secondValueDummy), NormalWords.toUntyped(ImmutableMap.of(firstWordStub, firstValueDummy, secondWordStub, secondValueDummy)));
        
        EasyMock.verify(firstWordStub);
        EasyMock.verify(secondWordStub);
    }

    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWords#toTyped(java.util.Map)}.
     */
    @Test
    public void testToTyped() {
        PowerMock.mockStaticPartial(NormalWords.class, "of");
        EasyMock.expect(NormalWords.of("FIRST")).andStubReturn(firstWordStub);
        EasyMock.expect(NormalWords.of("SECOND")).andStubReturn(secondWordStub);
        PowerMock.replay(NormalWords.class);
        
        assertEquals(ImmutableMap.of(firstWordStub, firstValueDummy, secondWordStub, secondValueDummy), NormalWords.toTyped(ImmutableMap.of(FIRST_WORD_TEXT, firstValueDummy, SECOND_WORD_TEXT, secondValueDummy)));
        
        PowerMock.verify(NormalWords.class);
        EasyMock.verify(firstWordStub);
        EasyMock.verify(secondWordStub);
    }

}
