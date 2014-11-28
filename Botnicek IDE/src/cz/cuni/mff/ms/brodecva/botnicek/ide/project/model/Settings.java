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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;
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
public final class Settings implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Map<URI, String> DEFAULT_NAMESACES_TO_PREFIXES =
            ImmutableMap.of(URI.create(AIML.NAMESPACE_URI.getValue()),
                    AIML.DEFAULT_PREFIX.getValue(),
                    URI.create(XML.SCHEMA_NAMESPACE_URI.getValue()),
                    XML.DEFAULT_SCHEMA_PREFIX.getValue());
    private static final NormalWord DEFAULT_PREFIX = NormalWords.of("BOTNICEK");
    private static final NormalWord DEFAULT_PULL_STATE = NormalWords.of("PULL");
    private static final NormalWord DEFAULT_PULL_STOP_STATE = NormalWords
            .of("PULLSTOP");
    private static final NormalWord DEFAULT_RANDOMIZE_STATE = NormalWords
            .of("RANDOMIZE");
    private static final NormalWord DEFAULT_TESTING_PREDICATE = NormalWords
            .of("TESTING");
    private static final NormalWord DEFAULT_SUCCESS_STATE = NormalWords
            .of("SUCCESS");
    private static final NormalWord DEFAULT_FAIL_STATE = NormalWords.of("FAIL");
    private static final NormalWord DEFAULT_RETURN_STATE = NormalWords
            .of("RETURN");

    private static final Settings DEFAULT = Settings.create(
            DEFAULT_NAMESACES_TO_PREFIXES, DEFAULT_PREFIX, DEFAULT_PULL_STATE,
            DEFAULT_PULL_STOP_STATE, DEFAULT_SUCCESS_STATE, DEFAULT_FAIL_STATE,
            DEFAULT_RETURN_STATE, DEFAULT_RANDOMIZE_STATE,
            DEFAULT_TESTING_PREDICATE);

    /**
     * Vrátí sjednocené nastavení
     * 
     * @param namespacesToPrefixes
     *            prostory jmen na prefixy
     * @param prefix
     *            prefix provozních stavů
     * @param pullState
     *            název stavu začínajícího vyhození nezpracovaných stavů ze
     *            zásobníku
     * @param pullStopState
     *            název stavu ukončujícího vyhození nezpracovaných stavů ze
     *            zásobníku
     * @param successState
     *            název stavu pro úspěšný průchod podsítí
     * @param failState
     *            název stavu pro neúspěšný průchod podsítí
     * @param returnState
     *            název stavu návratu z podsítě
     * @param randomizeState
     *            název stavu pro zamíchání stavy
     * @param testingPredicate
     *            název testovacího predikátu
     * @return nastavení
     */
    public static Settings create(final Map<? extends URI, ? extends String> namespacesToPrefixes,
            final NormalWord prefix, final NormalWord pullState,
            final NormalWord pullStopState, final NormalWord successState,
            final NormalWord failState, final NormalWord returnState,
            final NormalWord randomizeState, final NormalWord testingPredicate) {
        return new Settings(namespacesToPrefixes, prefix, pullState,
                pullStopState, successState, failState, returnState,
                testingPredicate, randomizeState);
    }

    /**
     * Vrátí výchozí nastavení.
     * 
     * @return výchozí nastavení
     */
    public static Settings getDefault() {
        return DEFAULT;
    }

    private final Map<URI, String> namespacesToPrefixes;
    private final NormalWord prefix;
    private final NormalWord pullState;
    private final NormalWord pullStopState;
    private final NormalWord randomizeState;
    private final NormalWord successState;
    private final NormalWord failState;

    private final NormalWord returnState;

    private final NormalWord testingPredicate;

    private Settings(final Map<? extends URI, ? extends String> namespacesToPrefixes,
            final NormalWord prefix, final NormalWord pullState,
            final NormalWord pullStopState, final NormalWord successState,
            final NormalWord failState, final NormalWord returnState,
            final NormalWord testingPredicate, final NormalWord randomizeState) {
        Preconditions.checkNotNull(namespacesToPrefixes);
        Preconditions.checkNotNull(prefix);
        Preconditions.checkNotNull(pullState);
        Preconditions.checkNotNull(pullStopState);
        Preconditions.checkNotNull(randomizeState);
        Preconditions.checkNotNull(successState);
        Preconditions.checkNotNull(failState);
        Preconditions.checkNotNull(returnState);
        Preconditions.checkNotNull(testingPredicate);

        Preconditions.checkArgument(namespacesToPrefixes.containsKey(URI
                .create(AIML.NAMESPACE_URI.getValue())));
        Preconditions.checkArgument(namespacesToPrefixes.containsKey(URI
                .create(XML.SCHEMA_NAMESPACE_URI.getValue())));

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

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (Objects.isNull(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Settings other = (Settings) obj;
        if (!this.failState.equals(other.failState)) {
            return false;
        }
        if (!this.namespacesToPrefixes.equals(other.namespacesToPrefixes)) {
            return false;
        }
        if (!this.prefix.equals(other.prefix)) {
            return false;
        }
        if (!this.pullState.equals(other.pullState)) {
            return false;
        }
        if (!this.pullStopState.equals(other.pullStopState)) {
            return false;
        }
        if (!this.randomizeState.equals(other.randomizeState)) {
            return false;
        }
        if (!this.returnState.equals(other.returnState)) {
            return false;
        }
        if (!this.successState.equals(other.successState)) {
            return false;
        }
        if (!this.testingPredicate.equals(other.testingPredicate)) {
            return false;
        }
        return true;
    }

    /**
     * Vrátí název stavu pro neúspěšný průchod podsítí.
     * 
     * @return název stavu pro neúspěšný průchod podsítí
     */
    public NormalWord getFailState() {
        return this.failState;
    }

    /**
     * Vrátí prostory jmen a jejich prefixy.
     * 
     * @return prostory jmen a jejich prefixy
     */
    public Map<URI, String> getNamespacesToPrefixes() {
        return this.namespacesToPrefixes;
    }

    /**
     * Vrátí prefix provozních stavů.
     * 
     * @return prefix stavů
     */
    public NormalWord getPrefix() {
        return this.prefix;
    }

    /**
     * Vrátí název stavu začínajícího vyhození nezpracovaných stavů ze
     * zásobníku.
     * 
     * @return název stavu začínajícího vyhození nezpracovaných stavů ze
     *         zásobníku
     */
    public NormalWord getPullState() {
        return this.pullState;
    }

    /**
     * Vrátí název stavu ukončujícího vyhození nezpracovaných stavů ze
     * zásobníku.
     * 
     * @return název stavu ukončujícího vyhození nezpracovaných stavů ze
     *         zásobníku
     */
    public NormalWord getPullStopState() {
        return this.pullStopState;
    }

    /**
     * Vrátí název stavu pro zamíchání stavy.
     * 
     * @return název stavu pro zamíchání stavy
     */
    public NormalWord getRandomizeState() {
        return this.randomizeState;
    }

    /**
     * Vrátí název stavu návratu z podsítě.
     * 
     * @return název stavu návratu z podsítě
     */
    public NormalWord getReturnState() {
        return this.returnState;
    }

    /**
     * Vrátí název stavu pro úspěšný průchod podsítí.
     * 
     * @return název stavu pro úspěšný průchod podsítí
     */
    public NormalWord getSuccessState() {
        return this.successState;
    }

    /**
     * Vrátí název testovacího predikátu.
     * 
     * @return název testovacího predikátu
     */
    public NormalWord getTestingPredicate() {
        return this.testingPredicate;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.failState.hashCode();
        result = prime * result + this.namespacesToPrefixes.hashCode();
        result = prime * result + this.prefix.hashCode();
        result = prime * result + this.pullState.hashCode();
        result = prime * result + this.pullStopState.hashCode();
        result = prime * result + this.randomizeState.hashCode();
        result = prime * result + this.returnState.hashCode();
        result = prime * result + this.successState.hashCode();
        result = prime * result + this.testingPredicate.hashCode();
        return result;
    }

    private void readObject(final ObjectInputStream objectInputStream)
            throws ClassNotFoundException, IOException {
        objectInputStream.defaultReadObject();
    }

    private Object readResolve() throws ObjectStreamException {
        return new Settings(this.namespacesToPrefixes, DEFAULT_PREFIX,
                this.pullState, this.pullStopState, this.successState,
                this.failState, this.returnState, this.testingPredicate,
                this.randomizeState);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        final int maxLen = 10;
        final StringBuilder builder = new StringBuilder();
        builder.append("Settings [namespacesToPrefixes=");
        builder.append(Text.toString(this.namespacesToPrefixes.entrySet(),
                maxLen));
        builder.append(", prefix=");
        builder.append(this.prefix);
        builder.append(", pullState=");
        builder.append(this.pullState);
        builder.append(", pullStopState=");
        builder.append(this.pullStopState);
        builder.append(", randomizeState=");
        builder.append(this.randomizeState);
        builder.append(", successState=");
        builder.append(this.successState);
        builder.append(", failState=");
        builder.append(this.failState);
        builder.append(", returnState=");
        builder.append(this.returnState);
        builder.append(", testingPredicate=");
        builder.append(this.testingPredicate);
        builder.append("]");
        return builder.toString();
    }

    private void writeObject(final ObjectOutputStream objectOutputStream)
            throws IOException {
        objectOutputStream.defaultWriteObject();
    }
}
