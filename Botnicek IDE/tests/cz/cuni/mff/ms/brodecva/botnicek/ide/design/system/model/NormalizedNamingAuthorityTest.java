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
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
 * Testuje autoritu, která uchovává normalizované názvy podle definice jazyka
 * AIML.
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
     * @throws java.lang.Exception
     *             pokud dojde k vyhození výjimky
     */
    @Before
    public void setUp() throws Exception {
        this.copyNormalizerStub = EasyMock.createStrictMock(Normalizer.class);
        EasyMock.expect(
                this.copyNormalizerStub.convertToNormalChars(EasyMock
                        .notNull(String.class))).andStubAnswer(
                new IAnswer<String>() {

                    @Override
                    public String answer() throws Throwable {
                        final String argument =
                                (String) EasyMock.getCurrentArguments()[0];
                        if (argument.equals(INVALID_NAME)) {
                            return "";
                        }

                        return argument;
                    }

                });
        EasyMock.replay(this.copyNormalizerStub);

        this.tested =
                NormalizedNamingAuthority.create(INITIAL_VALUE,
                        this.copyNormalizerStub);
    }

    /**
     * @throws java.lang.Exception
     *             pokud dojde k vyhození výjimky
     */
    @After
    public void tearDown() throws Exception {
        this.copyNormalizerStub = Intended.nullReference();

        this.tested = Intended.nullReference();
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#create(int, cz.cuni.mff.ms.brodecva.botnicek.library.preprocessor.Normalizer)}
     * .
     */
    @Test
    public void testCreate() {
        final Normalizer normalizerDummy =
                EasyMock.createStrictMock(Normalizer.class);
        EasyMock.replay(normalizerDummy);

        NormalizedNamingAuthority.create(0, normalizerDummy);

        EasyMock.verify(normalizerDummy);
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#generate()}
     * .
     */
    @Test
    public void testGenerate() {
        final Set<String> generated = new HashSet<>();
        for (int i = 0; i < MAX_GENERATION_ATTEMPTS; i++) {
            assertTrue(generated.add(this.tested.generate()));
        }

        EasyMock.verify(this.copyNormalizerStub);
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#isUsable(java.lang.String)}
     * a
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#use(java.lang.String)}
     * .
     */
    @Test
    public void testIsUsableWhenConflicting() {
        this.tested.use(NAME);

        assertFalse(this.tested.isUsable(NAME));

        EasyMock.verify(this.copyNormalizerStub);
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#isUsable(java.lang.String)}
     * .
     */
    @Test
    public void testIsUsableWhenInappropriate() {
        assertFalse(this.tested.isUsable(INVALID_NAME));

        EasyMock.verify(this.copyNormalizerStub);
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#isUsable(java.lang.String)}
     * a
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#use(java.lang.String)}
     * ..
     */
    @Test
    public void testIsUsableWhenValid() {
        assertTrue(this.tested.isUsable(NAME));

        EasyMock.verify(this.copyNormalizerStub);
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#isUsed(java.lang.String)}
     * .
     */
    @Test
    public void testIsUsedWhenNotUsedReturnsFalse() {
        assertFalse(this.tested.isUsed(NAME));

        EasyMock.verify(this.copyNormalizerStub);
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#isUsed(java.lang.String)}
     * a
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#use(String)}
     * .
     */
    @Test
    public void testIsUsedWhenUsedReturnsTrue() {
        this.tested.use(NAME);

        assertTrue(this.tested.isUsed(NAME));

        EasyMock.verify(this.copyNormalizerStub);
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#release(java.lang.String)}
     * .
     */
    @Test(expected = IllegalArgumentException.class)
    public void testReleaseWhenNotPresent() {
        this.tested.release(NAME);

        EasyMock.verify(this.copyNormalizerStub);
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#release(java.lang.String)}
     * ,
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#use(String)}
     * a
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#isUsed(java.lang.String)}
     * .
     */
    @Test
    public void testReleaseWhenPresent() {
        this.tested.use(NAME);
        this.tested.release(NAME);

        assertFalse(this.tested.isUsed(NAME));

        EasyMock.verify(this.copyNormalizerStub);
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#replace(java.lang.String, java.lang.String)}
     * .
     */
    @Test(expected = IllegalArgumentException.class)
    public void testReplaceWhenReplacedNotPresent() {
        this.tested.replace("FIRST", "SECOND");

        EasyMock.verify(this.copyNormalizerStub);
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#replace(java.lang.String, java.lang.String)}
     * ,
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#use(String)}
     * a
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#getSnapshot()}
     * .
     */
    @Test(expected = IllegalArgumentException.class)
    public void testReplaceWhenReplacementInappropriateExpectUnchanged() {
        this.tested.use(NAME);

        try {
            this.tested.replace(NAME, INVALID_NAME);
        } finally {
            assertEquals(ImmutableSet.of(NAME), this.tested.getSnapshot());
        }

        EasyMock.verify(this.copyNormalizerStub);
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#replace(java.lang.String, java.lang.String)}
     * ,
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#use(String)}
     * a
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#isUsed(String)}
     * .
     */
    @Test
    public void testReplaceWhenValidExpectReplacedNotPresent() {
        this.tested.use(NAME);
        this.tested.replace(NAME, "REPLACEMENT");

        assertFalse(this.tested.isUsed(NAME));

        EasyMock.verify(this.copyNormalizerStub);
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#replace(java.lang.String, java.lang.String)}
     * ,
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#use(String)}
     * a
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#isUsed(String)}
     * .
     */
    @Test
    public void testReplaceWhenValidExpectReplacementPresent() {
        this.tested.use(NAME);
        this.tested.replace(NAME, "REPLACEMENT");

        assertTrue(this.tested.isUsed("REPLACEMENT"));

        EasyMock.verify(this.copyNormalizerStub);
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#tryReplace(java.util.Map)}
     * ,
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#tryUse(String...)}
     * a
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#getSnapshot()}
     * .
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTryReplaceWhenAllNotPresentExpectUnchaned() {
        this.tested.tryUse("FIRST", "SECOND");
        try {
            this.tested.tryReplace(ImmutableMap.of("FIRST", "NEWFIRST",
                    "NOTPRESENT", "NEWSECOND"));
        } finally {
            assertEquals(ImmutableSet.of("FIRST", "SECOND"),
                    this.tested.getSnapshot());
        }

        EasyMock.verify(this.copyNormalizerStub);
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#tryReplace(java.util.Map)}
     * ,
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#tryUse(String...)}
     * a
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#getSnapshot()}
     * .
     */
    @Test
    public void testTryReplaceWhenAllValidExpectReplaced() {
        this.tested.tryUse("FIRST", "SECOND");
        this.tested.tryReplace(ImmutableMap.of("FIRST", "NEWFIRST", "SECOND",
                "NEWSECOND"));

        assertEquals(ImmutableSet.of("NEWFIRST", "NEWSECOND"),
                this.tested.getSnapshot());

        EasyMock.verify(this.copyNormalizerStub);
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#tryReplace(java.util.Map)}
     * ,
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#tryUse(String...)}
     * a
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#getSnapshot()}
     * .
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTryReplaceWhenSomeReplacementNotValidExpectUnchaned() {
        this.tested.tryUse("FIRST", "SECOND");
        try {
            this.tested.tryReplace(ImmutableMap.of("FIRST", "NEWFIRST",
                    "SECOND", INVALID_NAME));
        } finally {
            assertEquals(ImmutableSet.of("FIRST", "SECOND"),
                    this.tested.getSnapshot());
        }

        EasyMock.verify(this.copyNormalizerStub);
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#tryReplace(java.util.Map)}
     * ,
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#tryUse(String...)}
     * a
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#getSnapshot()}
     * .
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTryReplaceWhenSomeReplacementRepeatedExpectUnchaned() {
        this.tested.tryUse("FIRST", "SECOND");
        try {
            this.tested.tryReplace(ImmutableMap.of("FIRST", NAME, "SECOND",
                    NAME));
        } finally {
            assertEquals(ImmutableSet.of("FIRST", "SECOND"),
                    this.tested.getSnapshot());
        }

        EasyMock.verify(this.copyNormalizerStub);
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#tryReplace(java.util.Map)}
     * ,
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#tryUse(String...)}
     * a
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#getSnapshot()}
     * .
     */
    @Test
    public void testTryReplaceWhenSomeReplacementsEqual() {
        this.tested.tryUse("FIRST", NAME);
        this.tested.tryReplace(ImmutableMap.of("FIRST", "REPLACEMENT", NAME,
                NAME));

        assertEquals(ImmutableSet.of("REPLACEMENT", NAME),
                this.tested.getSnapshot());

        EasyMock.verify(this.copyNormalizerStub);
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#tryUse(java.lang.String[])}
     * a
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#getSnapshot()}
     * .
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTryUseWhenAllInappropriateExpectNotChanged() {
        try {
            this.tested.tryUse(INVALID_NAME, INVALID_NAME, INVALID_NAME);
        } finally {
            assertTrue(this.tested.getSnapshot().isEmpty());
        }

        EasyMock.verify(this.copyNormalizerStub);
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#tryUse(java.lang.String[])}
     * a
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#getSnapshot()}
     * .
     */
    @Test
    public void testTryUseWhenAllValidExpectUsed() {
        this.tested.tryUse("FIRST", "SECOND", "THIRD");

        assertEquals(ImmutableSet.of("FIRST", "SECOND", "THIRD"),
                this.tested.getSnapshot());

        EasyMock.verify(this.copyNormalizerStub);
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#tryUse(java.lang.String[])}
     * a
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#getSnapshot()}
     * .
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTryUseWhenRepeatedExpectNotChanged() {
        try {
            this.tested.tryUse(NAME, NAME);
        } finally {
            assertTrue(this.tested.getSnapshot().isEmpty());
        }

        EasyMock.verify(this.copyNormalizerStub);
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#tryUse(java.lang.String[])}
     * a
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#getSnapshot()}
     * .
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTryUseWhenSomeInappropriateExpectNotChanged() {
        try {
            this.tested.tryUse("FIRST", INVALID_NAME, "SECOND");
        } finally {
            assertTrue(this.tested.getSnapshot().isEmpty());
        }

        EasyMock.verify(this.copyNormalizerStub);
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#use(java.lang.String)}
     * .
     */
    @Test
    public void testUseWhenValid() {
        assertEquals(NAME, this.tested.use(NAME));

        EasyMock.verify(this.copyNormalizerStub);
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#use(java.lang.String)}
     * .
     */
    @Test
    public void testUseWhenWhenConflicting() {
        final Set<String> generated = new HashSet<>();
        for (int i = 0; i < MAX_GENERATION_ATTEMPTS; i++) {
            assertTrue(generated.add(this.tested.use(NAME)));
        }

        EasyMock.verify(this.copyNormalizerStub);
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority#use(java.lang.String)}
     * .
     */
    @Test
    public void testUseWhenWhenInappropriate() {
        final Set<String> generated = new HashSet<>();
        for (int i = 0; i < MAX_GENERATION_ATTEMPTS; i++) {
            assertTrue(generated.add(this.tested.use(INVALID_NAME)));
        }

        EasyMock.verify(this.copyNormalizerStub);
    }
}
