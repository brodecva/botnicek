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
package cz.cuni.mff.ms.brodecva.botnicek.library.language;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import cz.cuni.mff.ms.brodecva.botnicek.library.utils.test.ObjectGenerator;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.test.UnitTest;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

/**
 * Testuje definici jazyka pro AIMl.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see AIMLLanguage
 */
@RunWith(JUnitParamsRunner.class)
@Category(UnitTest.class)
public final class AIMLLanguageTest {
    
    /**
     * Existující mapa.
     */
    private static final Map<Pattern, String> EXISTING_MAP = Collections.unmodifiableMap(new HashMap<Pattern, String>());
    
    /**
     * Existující @{link java.lang.String}.
     */
    private static final String EXISTING_STRING = "";
    
    /**
     * Existující {@link java.util.regex.Pattern}.
     */
    private static final Pattern EXISTING_PATTERN = Pattern.compile("");

    /**
     * Existující parametry konstruktoru.
     */
    private static final Object[] ALL_EXISTING = new Object[] { EXISTING_STRING, EXISTING_PATTERN, EXISTING_MAP, EXISTING_MAP, EXISTING_MAP, EXISTING_MAP, EXISTING_MAP, EXISTING_MAP, EXISTING_MAP };
    
    /**
     * Test pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.language.AIMLLanguage#AIMLLanguage(java.lang.String, java.util.regex.Pattern, java.util.Map, java.util.Map, java.util.Map, java.util.Map, java.util.Map, java.util.Map, java.util.Map)}.
     * 
     * @param name
     *            jméno
     * @param sentenceDelimiter
     *            kritérium dělení na věty
     * @param genderSubs
     *            substituce pohlaví
     * @param personSubs
     *            substituce 1. a 2. osoby
     * @param person2Subs
     *            substituce 1. a 3. osoby
     * @param abbreviationsSubs
     *            substituce zkratek
     * @param spellingSubs
     *            substituce chyb v pravopisu a kolokvialismů
     * @param emoticonsSubstitution
     *            substituce emotikon
     * @param innerPunctuationSubs
     *            substituce vnitřní interpunkce
     */
    @Test(expected = NullPointerException.class)
    @Parameters
    public void testAIMLLanguageWhenSomeNull(final String name, final Pattern sentenceDelimiter,
            final Map<Pattern, String> genderSubs,
            final Map<Pattern, String> personSubs,
            final Map<Pattern, String> person2Subs,
            final Map<Pattern, String> abbreviationsSubs,
            final Map<Pattern, String> spellingSubs,
            final Map<Pattern, String> emoticonsSubstitution,
            final Map<Pattern, String> innerPunctuationSubs) {
        new AIMLLanguage(name, sentenceDelimiter, genderSubs, personSubs, person2Subs, abbreviationsSubs, spellingSubs, emoticonsSubstitution, innerPunctuationSubs);
    }
    
    /**
     * Dodává parametry pro {@link #testAIMLLanguageWhenSomeNull(String, Pattern, Map, Map, Map, Map, Map, EmoticonsSubstitution)}.
     * 
     * @return parametry
     */
    @SuppressWarnings("unused")
    private Object[] parametersForTestAIMLLanguageWhenSomeNull() {
        return ObjectGenerator.copyAndReplaceOneInEvery(ALL_EXISTING, null);
    }
    
    /**
     * Test pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.language.AIMLLanguage#AIMLLanguage(java.lang.String, java.util.regex.Pattern, java.util.Map, java.util.Map, java.util.Map, java.util.Map, java.util.Map, java.util.Map, java.util.Map)}.
     * 
     * @param name
     *            jméno
     * @param sentenceDelimiter
     *            kritérium dělení na věty
     * @param genderSubs
     *            substituce pohlaví
     * @param personSubs
     *            substituce 1. a 2. osoby
     * @param person2Subs
     *            substituce 1. a 3. osoby
     * @param abbreviationsSubs
     *            substituce zkratek
     * @param spellingSubs
     *            substituce chyb v pravopisu a kolokvialismů
     * @param emoticonsSubstitution
     *            substituce emotikon
     * @param innerPunctuationSubs
     *            substituce vnitřní interpunkce
     */
    @Test
    @Parameters
    public void testAIMLLanguageWhenAllNotNull(final String name, final Pattern sentenceDelimiter,
            final Map<Pattern, String> genderSubs,
            final Map<Pattern, String> personSubs,
            final Map<Pattern, String> person2Subs,
            final Map<Pattern, String> abbreviationsSubs,
            final Map<Pattern, String> spellingSubs,
            final Map<Pattern, String> emoticonsSubstitution,
            final Map<Pattern, String> innerPunctuationSubs) {
        new AIMLLanguage(name, sentenceDelimiter, genderSubs, personSubs, person2Subs, abbreviationsSubs, spellingSubs, emoticonsSubstitution, innerPunctuationSubs);
    }
    
    /**
     * Dodává parametry pro {@link #testAIMLLanguageWhenAllNotNull(String, Pattern, Map, Map, Map, Map, Map, EmoticonsSubstitution)}.
     * 
     * @return parametry
     */
    @SuppressWarnings("unused")
    private Object[] parametersForTestAIMLLanguageWhenAllNotNull() {
        return new Object[] { ALL_EXISTING };
    }
    
    /**
     * Test pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.language.AIMLLanguage#equals(java.lang.Object)} a {@link cz.cuni.mff.ms.brodecva.botnicek.library.language.AIMLLanguage#hashCode()}.
     */
    @Test
    public void testEqualsAndHashCode() {
        EqualsVerifier.forClass(AIMLLanguage.class).usingGetClass().allFieldsShouldBeUsed().suppress(Warning.NULL_FIELDS);
    }
}
