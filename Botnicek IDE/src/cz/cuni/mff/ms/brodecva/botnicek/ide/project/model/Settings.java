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
package cz.cuni.mff.ms.brodecva.botnicek.ide.project.model;

import java.net.URI;
import java.util.Map;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWords;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.Objects;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.AIML;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.XML;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.Text;

/**
 * Nastavení projektu.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class Settings {

    private static final Map<URI, String> DEFAULT_NAMESACES_TO_PREFIXES = ImmutableMap.of(URI.create(AIML.NAMESPACE_URI.getValue()), AIML.DEFAULT_PREFIX.getValue(), URI.create(XML.SCHEMA_NAMESPACE_URI.getValue()), XML.DEFAULT_SCHEMA_PREFIX.getValue());
    private static final NormalWord DEFAULT_PREFIX = NormalWords.of("BOTNICEK");
    private static final NormalWord DEFAULT_PULL_STATE = NormalWords.of("PULL");
    private static final NormalWord DEFAULT_PULL_STOP_STATE = NormalWords.of("PULLSTOP");
    private static final NormalWord DEFAULT_RANDOMIZE_STATE = NormalWords.of("RANDOMIZE");
    private static final NormalWord DEFAULT_TESTING_PREDICATE = NormalWords.of("TESTING");
    private static final NormalWord DEFAULT_SUCCESS_STATE = NormalWords.of("SUCCESS");
    private static final NormalWord DEFAULT_FAIL_STATE = NormalWords.of("FAIL");
    private static final NormalWord DEFAULT_RETURN_STATE = NormalWords.of("RETURN");
    
    private static final Settings DEFAULT = Settings.create(DEFAULT_NAMESACES_TO_PREFIXES, DEFAULT_PREFIX, DEFAULT_PULL_STATE, DEFAULT_PULL_STOP_STATE, DEFAULT_SUCCESS_STATE, DEFAULT_FAIL_STATE, DEFAULT_RETURN_STATE, DEFAULT_RANDOMIZE_STATE, DEFAULT_TESTING_PREDICATE);
        
    private final Map<URI, String> namespacesToPrefixes;
    private final NormalWord prefix;
    private final NormalWord pullState;
    private final NormalWord pullStopState;
    private final NormalWord randomizeState;
    private final NormalWord successState;
    private final NormalWord failState;
    private final NormalWord returnState;
    private final NormalWord testingPredicate;
    
    /**
     * Vrátí výchozí nastavení.
     * 
     * @return výchozí nastavení
     */
    public static Settings getDefault() {
        return DEFAULT;
    }
    
    /**
     * Vrátí sjednocené nastavení
     * 
     * @param namespacesToPrefixes prostory jmen na prefixy
     * @param prefix prefix provozních stavů
     * @param pullState název stavu začínajícího vyhození nezpracovaných stavů ze zásobníku
     * @param pullStopState název stavu ukončujícího vyhození nezpracovaných stavů ze zásobníku
     * @param successState název stavu pro úspěšný průchod podsítí
     * @param failState název stavu pro neúspěšný průchod podsítí
     * @param returnState název stavu návratu z podsítě
     * @param randomizeState název stavu pro zamíchání stavy
     * @param testingPredicate název testovacího predikátu
     * @return nastavení
     */
    public static Settings create(
            final Map<URI, String> namespacesToPrefixes,
            final NormalWord prefix,
            final NormalWord pullState, final NormalWord pullStopState,
            NormalWord successState,
            NormalWord failState, NormalWord returnState, final NormalWord randomizeState, final NormalWord testingPredicate) {
        return new Settings(namespacesToPrefixes, prefix, pullState, pullStopState, successState, failState, returnState, testingPredicate, randomizeState);
    }

    private Settings(
            final Map<URI, String> namespacesToPrefixes,
            final NormalWord prefix,
            final NormalWord pullState, final NormalWord pullStopState,
            final NormalWord successState,
            final NormalWord failState, final NormalWord returnState, final NormalWord testingPredicate, final NormalWord randomizeState) {
        Preconditions.checkNotNull(namespacesToPrefixes);
        Preconditions.checkNotNull(prefix);
        Preconditions.checkNotNull(pullState);
        Preconditions.checkNotNull(pullStopState);
        Preconditions.checkNotNull(randomizeState);
        Preconditions.checkNotNull(successState);
        Preconditions.checkNotNull(failState);
        Preconditions.checkNotNull(returnState);
        Preconditions.checkNotNull(testingPredicate);
        
        Preconditions.checkArgument(namespacesToPrefixes.containsKey(URI.create(AIML.NAMESPACE_URI.getValue())));
        Preconditions.checkArgument(namespacesToPrefixes.containsKey(URI.create(XML.SCHEMA_NAMESPACE_URI.getValue())));
        
        this.namespacesToPrefixes = ImmutableMap.copyOf(namespacesToPrefixes);
        this.prefix = prefix;
        this.pullState = pullState;
        this.pullStopState = pullStopState;
        this.randomizeState = randomizeState;
        this.successState = successState;
        this.failState = failState;
        this.returnState = returnState;
        this.testingPredicate = testingPredicate;
    }

    /**
     * Vrátí prostory jmen a jejich prefixy.
     * 
     * @return prostory jmen a jejich prefixy
     */
    public Map<URI, String> getNamespacesToPrefixes() {
        return namespacesToPrefixes;
    }

    /**
     * Vrátí prefix provozních stavů.
     * 
     * @return prefix stavů
     */
    public NormalWord getPrefix() {
        return prefix;
    }

    /**
     * Vrátí název stavu začínajícího vyhození nezpracovaných stavů ze zásobníku.
     * 
     * @return název stavu začínajícího vyhození nezpracovaných stavů ze zásobníku
     */
    public NormalWord getPullState() {
        return pullState;
    }

    /**
     * Vrátí název stavu ukončujícího vyhození nezpracovaných stavů ze zásobníku.
     * 
     * @return název stavu ukončujícího vyhození nezpracovaných stavů ze zásobníku
     */
    public NormalWord getPullStopState() {
        return pullStopState;
    }

    /**
     * Vrátí název testovacího predikátu.
     * 
     * @return název testovacího predikátu
     */
    public NormalWord getTestingPredicate() {
        return testingPredicate;
    }

    /**
     * Vrátí název stavu pro zamíchání stavy.
     * 
     * @return název stavu pro zamíchání stavy
     */
    public NormalWord getRandomizeState() {
        return randomizeState;
    }

    /**
     * Vrátí název stavu pro úspěšný průchod podsítí.
     * 
     * @return název stavu pro úspěšný průchod podsítí
     */
    public NormalWord getSuccessState() {
        return successState;
    }

    /**
     * Vrátí název stavu pro neúspěšný průchod podsítí.
     * 
     * @return název stavu pro neúspěšný průchod podsítí
     */
    public NormalWord getFailState() {
        return failState;
    }

    /**
     * Vrátí název stavu návratu z podsítě.
     * 
     * @return název stavu návratu z podsítě
     */
    public NormalWord getReturnState() {
        return returnState;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result =
                prime * result
                        + failState.hashCode();
        result =
                prime
                        * result
                        + namespacesToPrefixes.hashCode();
        result = prime * result + prefix.hashCode();
        result =
                prime * result
                        + pullState.hashCode();
        result =
                prime
                        * result
                        + pullStopState
                                .hashCode();
        result =
                prime
                        * result
                        + randomizeState
                                .hashCode();
        result =
                prime * result
                        + returnState.hashCode();
        result =
                prime
                        * result
                        + successState.hashCode();
        result =
                prime
                        * result
                        + testingPredicate
                                .hashCode();
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (Objects.isNull(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Settings other = (Settings) obj;
        if (!failState.equals(other.failState)) {
            return false;
        }
        if (!namespacesToPrefixes.equals(other.namespacesToPrefixes)) {
            return false;
        }
        if (!prefix.equals(other.prefix)) {
            return false;
        }
        if (!pullState.equals(other.pullState)) {
            return false;
        }
        if (!pullStopState.equals(other.pullStopState)) {
            return false;
        }
        if (!randomizeState.equals(other.randomizeState)) {
            return false;
        }
        if (!returnState.equals(other.returnState)) {
            return false;
        }
        if (!successState.equals(other.successState)) {
            return false;
        }
        if (!testingPredicate.equals(other.testingPredicate)) {
            return false;
        }
        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        final int maxLen = 10;
        StringBuilder builder = new StringBuilder();
        builder.append("Settings [namespacesToPrefixes=");
        builder.append(Text.toString(namespacesToPrefixes.entrySet(), maxLen));
        builder.append(", prefix=");
        builder.append(prefix);
        builder.append(", pullState=");
        builder.append(pullState);
        builder.append(", pullStopState=");
        builder.append(pullStopState);
        builder.append(", randomizeState=");
        builder.append(randomizeState);
        builder.append(", successState=");
        builder.append(successState);
        builder.append(", failState=");
        builder.append(failState);
        builder.append(", returnState=");
        builder.append(returnState);
        builder.append(", testingPredicate=");
        builder.append(testingPredicate);
        builder.append("]");
        return builder.toString();
    }
}
