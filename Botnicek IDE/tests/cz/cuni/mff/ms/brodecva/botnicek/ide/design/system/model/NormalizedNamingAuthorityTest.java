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
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Intended;
import cz.cuni.mff.ms.brodecva.botnicek.library.preprocessor.Normalizer;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public class NormalizedNamingAuthorityTest {

    private static final String NAME = "NAME";
    private static final String INVALID_NAME = "INVALID";
    private static final int MAX_GENERATION_ATTEMPTS = 100;
    private static final int INITIAL_VALUE = 0;
    
    private Normalizer copyNormalizerStub = Intended.nullReference();

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    /**
     * @throws java.lang.Exception
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
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        copyNormalizerStub = Intended.nullReference();
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
        final NormalizedNamingAuthority tested = NormalizedNamingAuthority.create(INITIAL_VALUE, copyNormalizerStub);       
        
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
        final NormalizedNamingAuthority tested = NormalizedNamingAuthority.create(INITIAL_VALUE, copyNormalizerStub);       
        
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
        final NormalizedNamingAuthority tested = NormalizedNamingAuthority.create(INITIAL_VALUE, copyNormalizerStub);       
        
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
        final NormalizedNamingAuthority tested = NormalizedNamingAuthority.create(INITIAL_VALUE, copyNormalizerStub);       
                
        assertEquals(NAME, tested.use(NAME));
        
        EasyMock.verify(copyNormalizerStub);
    }

    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#isUsable(java.lang.String)} and {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#use(java.lang.String)}.
     */
    @Test
    public void testIsUsableWhenConflicting() {
        final NormalizedNamingAuthority tested = NormalizedNamingAuthority.create(INITIAL_VALUE, copyNormalizerStub);       
        
        tested.use(NAME);
        assertFalse(tested.isUsable(NAME));
        
        EasyMock.verify(copyNormalizerStub);
    }
    
    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#isUsable(java.lang.String)}.
     */
    @Test
    public void testIsUsableWhenInappropriate() {
        final NormalizedNamingAuthority tested = NormalizedNamingAuthority.create(INITIAL_VALUE, copyNormalizerStub);       
        
        assertFalse(tested.isUsable(INVALID_NAME));
        
        EasyMock.verify(copyNormalizerStub);
    }
    
    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#isUsable(java.lang.String)} and {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#use(java.lang.String)}..
     */
    @Test
    public void testIsUsableWhenValid() {
        final NormalizedNamingAuthority tested = NormalizedNamingAuthority.create(INITIAL_VALUE, copyNormalizerStub);       
        
        assertTrue(tested.isUsable(NAME));
        
        EasyMock.verify(copyNormalizerStub);
    }

    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#tryUse(java.lang.String[])} and {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#getSnapshot()}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTryUseWhenAllInappropriateExpectNotChanged() {
        final NormalizedNamingAuthority tested = NormalizedNamingAuthority.create(INITIAL_VALUE, copyNormalizerStub);       
        
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
        final NormalizedNamingAuthority tested = NormalizedNamingAuthority.create(INITIAL_VALUE, copyNormalizerStub);       
        
        tested.tryUse("FIRST", "SECOND", "THIRD");
        assertEquals(ImmutableSet.of("FIRST", "SECOND", "THIRD"), tested.getSnapshot());
        
        EasyMock.verify(copyNormalizerStub);
    }
    
    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#tryUse(java.lang.String[])} and {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#getSnapshot()}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTryUseWhenSomeInappropriateExpectNotChanged() {
        final NormalizedNamingAuthority tested = NormalizedNamingAuthority.create(INITIAL_VALUE, copyNormalizerStub);       
        
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
        final NormalizedNamingAuthority tested = NormalizedNamingAuthority.create(INITIAL_VALUE, copyNormalizerStub);       
        
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
        final NormalizedNamingAuthority tested = NormalizedNamingAuthority.create(INITIAL_VALUE, copyNormalizerStub);       
        
        tested.tryUse("FIRST", "SECOND");
        tested.tryReplace(ImmutableMap.of("FIRST", "NEWFIRST", "SECOND", "NEWSECOND"));
        assertEquals(ImmutableSet.of("NEWFIRST", "NEWSECOND"), tested.getSnapshot());
        
        EasyMock.verify(copyNormalizerStub);
    }
    
    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#tryReplace(java.util.Map)}, {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#tryUse(String...)} and {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#getSnapshot()}.
     */
    @Test
    public void testTryReplaceWhenAllNotPresentValidExpectReplaced() {
        final NormalizedNamingAuthority tested = NormalizedNamingAuthority.create(INITIAL_VALUE, copyNormalizerStub);       
        
        tested.tryUse("FIRST", "SECOND");
        tested.tryReplace(ImmutableMap.of("FIRST", "NEWFIRST", "SECOND", "NEWSECOND"));
        assertEquals(ImmutableSet.of("NEWFIRST", "NEWSECOND"), tested.getSnapshot());
        
        EasyMock.verify(copyNormalizerStub);
    }

    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#release(java.lang.String)}.
     */
    @Test
    public void testRelease() {
        fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#isUsed(java.lang.String)}.
     */
    @Test
    public void testIsUsed() {
        fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#replace(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testReplace() {
        fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#getSnapshot()}.
     */
    @Test
    public void testGetSnapshot() {
        fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#toString()}.
     */
    @Test
    public void testToString() {
        fail("Not yet implemented"); // TODO
    }

}
