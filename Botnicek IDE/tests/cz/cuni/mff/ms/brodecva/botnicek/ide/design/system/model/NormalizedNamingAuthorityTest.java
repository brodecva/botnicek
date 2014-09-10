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
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.easymock.EasyMock;
import org.easymock.IAnswer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Intended;
import cz.cuni.mff.ms.brodecva.botnicek.library.preprocessor.Normalizer;

/**
 * Testuje autoritu, která uchovává normalizované názvy podle definice jazyka AIML.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see NormalizedNamingAuthority
 */
public class NormalizedNamingAuthorityTest {

    private static final String NAME = "NAME";
    private static final String INVALID_NAME = "INVALID";
    private static final int MAX_GENERATION_ATTEMPTS = 100;
    private static final int INITIAL_VALUE = 0;
    
    private Normalizer copyNormalizerStub = Intended.nullReference();
    private NormalizedNamingAuthority tested = Intended.nullReference();

    /**
     * @throws java.lang.Exception pokud dojde k vyhození výjimky
     */
    @Before
    public void setUp() throws Exception {
        copyNormalizerStub = EasyMock.createStrictMock(Normalizer.class);
        EasyMock.expect(copyNormalizerStub.convertToNormalChars(EasyMock.notNull(String.class))).andStubAnswer(new IAnswer<String>() {

            @Override
            public String answer() throws Throwable {
                final String argument = (String) EasyMock.getCurrentArguments()[0];
                if (argument.equals(INVALID_NAME)) {
                    return "";
                }
                
                return argument;
            }
            
        });
        EasyMock.replay(copyNormalizerStub);
        
        tested = NormalizedNamingAuthority.create(INITIAL_VALUE, copyNormalizerStub);       
    }

    /**
     * @throws java.lang.Exception pokud dojde k vyhození výjimky
     */
    @After
    public void tearDown() throws Exception {
        copyNormalizerStub = Intended.nullReference();
        
        tested = Intended.nullReference();
    }

    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#create(int, cz.cuni.mff.ms.brodecva.botnicek.library.preprocessor.Normalizer)}.
     */
    @Test
    public void testCreate() {
        final Normalizer normalizerDummy = EasyMock.createStrictMock(Normalizer.class);
        EasyMock.replay(normalizerDummy);
        
        NormalizedNamingAuthority.create(0, normalizerDummy);
        
        EasyMock.verify(normalizerDummy);
    }

    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#generate()}.
     */
    @Test
    public void testGenerate() {
        final Set<String> generated = new HashSet<>();
        for (int i = 0; i < MAX_GENERATION_ATTEMPTS; i++) {
            assertTrue(generated.add(tested.generate()));
        }
        
        EasyMock.verify(copyNormalizerStub);
    }

    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#use(java.lang.String)}.
     */
    @Test
    public void testUseWhenWhenConflicting() {
        final Set<String> generated = new HashSet<>();
        for (int i = 0; i < MAX_GENERATION_ATTEMPTS; i++) {
            assertTrue(generated.add(tested.use(NAME)));
        }
        
        EasyMock.verify(copyNormalizerStub);
    }
    
    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#use(java.lang.String)}.
     */
    @Test
    public void testUseWhenWhenInappropriate() {
        final Set<String> generated = new HashSet<>();
        for (int i = 0; i < MAX_GENERATION_ATTEMPTS; i++) {
            assertTrue(generated.add(tested.use(INVALID_NAME)));
        }
        
        EasyMock.verify(copyNormalizerStub);
    }
    
    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#use(java.lang.String)}.
     */
    @Test
    public void testUseWhenValid() {
        assertEquals(NAME, tested.use(NAME));
        
        EasyMock.verify(copyNormalizerStub);
    }

    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#isUsable(java.lang.String)} and {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#use(java.lang.String)}.
     */
    @Test
    public void testIsUsableWhenConflicting() {
        tested.use(NAME);
        
        assertFalse(tested.isUsable(NAME));
        
        EasyMock.verify(copyNormalizerStub);
    }
    
    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#isUsable(java.lang.String)}.
     */
    @Test
    public void testIsUsableWhenInappropriate() {
        assertFalse(tested.isUsable(INVALID_NAME));
        
        EasyMock.verify(copyNormalizerStub);
    }
    
    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#isUsable(java.lang.String)} and {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#use(java.lang.String)}..
     */
    @Test
    public void testIsUsableWhenValid() {
        assertTrue(tested.isUsable(NAME));
        
        EasyMock.verify(copyNormalizerStub);
    }

    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#tryUse(java.lang.String[])} and {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#getSnapshot()}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTryUseWhenAllInappropriateExpectNotChanged() {
        try {
            tested.tryUse(INVALID_NAME, INVALID_NAME, INVALID_NAME);
        } finally {
            assertTrue(tested.getSnapshot().isEmpty());
        }
        
        EasyMock.verify(copyNormalizerStub);
    }
    
    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#tryUse(java.lang.String[])} and {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#getSnapshot()}.
     */
    @Test
    public void testTryUseWhenAllValidExpectUsed() {
        tested.tryUse("FIRST", "SECOND", "THIRD");
        
        assertEquals(ImmutableSet.of("FIRST", "SECOND", "THIRD"), tested.getSnapshot());
        
        EasyMock.verify(copyNormalizerStub);
    }
    
    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#tryUse(java.lang.String[])} and {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#getSnapshot()}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTryUseWhenSomeInappropriateExpectNotChanged() {
        try {
            tested.tryUse("FIRST", INVALID_NAME, "SECOND");
        } finally {
            assertTrue(tested.getSnapshot().isEmpty());
        }
        
        EasyMock.verify(copyNormalizerStub);
    }
    
    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#tryUse(java.lang.String[])} and {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#getSnapshot()}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTryUseWhenRepeatedExpectNotChanged() {
        try {
            tested.tryUse(NAME, NAME);
        } finally {
            assertTrue(tested.getSnapshot().isEmpty());
        }
        
        EasyMock.verify(copyNormalizerStub);
    }

    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#tryReplace(java.util.Map)}, {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#tryUse(String...)} and {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#getSnapshot()}.
     */
    @Test
    public void testTryReplaceWhenAllValidExpectReplaced() {
        tested.tryUse("FIRST", "SECOND");
        tested.tryReplace(ImmutableMap.of("FIRST", "NEWFIRST", "SECOND", "NEWSECOND"));
        
        assertEquals(ImmutableSet.of("NEWFIRST", "NEWSECOND"), tested.getSnapshot());
        
        EasyMock.verify(copyNormalizerStub);
    }
    
    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#tryReplace(java.util.Map)}, {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#tryUse(String...)} and {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#getSnapshot()}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTryReplaceWhenAllNotPresentExpectUnchaned() {
        tested.tryUse("FIRST", "SECOND");
        try {
            tested.tryReplace(ImmutableMap.of("FIRST", "NEWFIRST", "NOTPRESENT", "NEWSECOND"));
        } finally {
            assertEquals(ImmutableSet.of("FIRST", "SECOND"), tested.getSnapshot());
        }
        
        EasyMock.verify(copyNormalizerStub);
    }
    
    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#tryReplace(java.util.Map)}, {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#tryUse(String...)} and {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#getSnapshot()}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTryReplaceWhenSomeReplacementNotValidExpectUnchaned() {
        tested.tryUse("FIRST", "SECOND");
        try {
            tested.tryReplace(ImmutableMap.of("FIRST", "NEWFIRST", "SECOND", INVALID_NAME));
        } finally {
            assertEquals(ImmutableSet.of("FIRST", "SECOND"), tested.getSnapshot());
        }
        
        EasyMock.verify(copyNormalizerStub);
    }
    
    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#tryReplace(java.util.Map)}, {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#tryUse(String...)} and {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#getSnapshot()}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTryReplaceWhenSomeReplacementRepeatedExpectUnchaned() {
        tested.tryUse("FIRST", "SECOND");
        try {
            tested.tryReplace(ImmutableMap.of("FIRST", NAME, "SECOND", NAME));
        } finally {
            assertEquals(ImmutableSet.of("FIRST", "SECOND"), tested.getSnapshot());
        }
        
        EasyMock.verify(copyNormalizerStub);
    }
    
    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#tryReplace(java.util.Map)}, {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#tryUse(String...)} and {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#getSnapshot()}.
     */
    @Test
    public void testTryReplaceWhenSomeReplacementsEqual() {
        tested.tryUse("FIRST", NAME);
        tested.tryReplace(ImmutableMap.of("FIRST", "REPLACEMENT", NAME, NAME));
        
        assertEquals(ImmutableSet.of("REPLACEMENT", NAME), tested.getSnapshot());
        
        EasyMock.verify(copyNormalizerStub);
    }

    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#release(java.lang.String)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testReleaseWhenNotPresent() {
        tested.release(NAME);
        
        EasyMock.verify(copyNormalizerStub);
    }
    
    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#release(java.lang.String)}, {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#use(String)} and {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#isUsed(java.lang.String)}.
     */
    @Test
    public void testReleaseWhenPresent() {
        tested.use(NAME);
        tested.release(NAME);
        
        assertFalse(tested.isUsed(NAME));
        
        EasyMock.verify(copyNormalizerStub);
    }

    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#isUsed(java.lang.String)} and {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#use(String)}.
     */
    @Test
    public void testIsUsedWhenUsedReturnsTrue() {
        tested.use(NAME);
        
        assertTrue(tested.isUsed(NAME));
        
        EasyMock.verify(copyNormalizerStub);
    }
    
    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#isUsed(java.lang.String)}.
     */
    @Test
    public void testIsUsedWhenNotUsedReturnsFalse() {
        assertFalse(tested.isUsed(NAME));
        
        EasyMock.verify(copyNormalizerStub);
    }

    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#replace(java.lang.String, java.lang.String)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testReplaceWhenReplacedNotPresent() {
        tested.replace("FIRST", "SECOND");
        
        EasyMock.verify(copyNormalizerStub);
    }
    
    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#replace(java.lang.String, java.lang.String)}, {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#use(String)} and {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#getSnapshot()}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testReplaceWhenReplacementInappropriateExpectUnchanged() {
        tested.use(NAME);
        
        try {
            tested.replace(NAME, INVALID_NAME);
        } finally {
            assertEquals(ImmutableSet.of(NAME), tested.getSnapshot());
        }
        
        EasyMock.verify(copyNormalizerStub);
    }
    
    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#replace(java.lang.String, java.lang.String)}, {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#use(String)} and {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#isUsed(String)}.
     */
    @Test
    public void testReplaceWhenValidExpectReplacedNotPresent() {
        tested.use(NAME);
        tested.replace(NAME, "REPLACEMENT");
        
        assertFalse(tested.isUsed(NAME));
        
        EasyMock.verify(copyNormalizerStub);
    }
    
    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#replace(java.lang.String, java.lang.String)}, {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#use(String)} and {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#isUsed(String)}.
     */
    @Test
    public void testReplaceWhenValidExpectReplacementPresent() {
        tested.use(NAME);
        tested.replace(NAME, "REPLACEMENT");
        
        assertTrue(tested.isUsed("REPLACEMENT"));
        
        EasyMock.verify(copyNormalizerStub);
    }
}
