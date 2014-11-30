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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableSet;

import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.Comparisons;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.resources.ExceptionLocalizer;
import cz.cuni.mff.ms.brodecva.botnicek.library.preprocessor.Normalizer;
import cz.cuni.mff.ms.brodecva.botnicek.library.preprocessor.SimpleNormalizer;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.Text;

/**
 * Autorita, která uchovává normalizované názvy podle definice jazyka AIML.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class NormalizedNamingAuthority implements NamingAuthority,
        Serializable {

    private static final long serialVersionUID = 1L;

    private static final String EMPTY_SUBMITTED_PART = "";
    private static final int DEFAULT_START = 1;

    /**
     * Vytvoří autoritu s výchozím nastavením.
     * 
     * @return autorita
     */
    public static NormalizedNamingAuthority create() {
        return new NormalizedNamingAuthority();
    }

    /**
     * Vytvoří autoritu s výchozí implementací normalizéru řetězců.
     * 
     * @param initial
     *            výchozí hodnota čítače pro generování názvů
     * 
     * @return autorita
     */
    public static NormalizedNamingAuthority create(final int initial) {
        return new NormalizedNamingAuthority(initial);
    }

    /**
     * Vytvoří autoritu.
     * 
     * @param initial
     *            výchozí hodnota čítače pro generování názvů
     * @param normalizer
     *            normalizér řetězců
     * 
     * @return autorita
     */
    public static NormalizedNamingAuthority create(final int initial,
            final Normalizer normalizer) {
        return new NormalizedNamingAuthority(initial, normalizer);
    }

    /**
     * Vytvoří autoritu s výchozím nastavením čítače.
     * 
     * @param normalizer
     *            normalizér řetězců
     * 
     * @return autorita
     */
    public static NormalizedNamingAuthority create(final Normalizer normalizer) {
        return new NormalizedNamingAuthority(normalizer);
    }

    private final Set<String> used = new HashSet<>();

    private final Normalizer normalizer;

    private int counter = DEFAULT_START;

    private NormalizedNamingAuthority() {
        this(DEFAULT_START, new SimpleNormalizer());
    }

    private NormalizedNamingAuthority(final int initial) {
        this(initial, new SimpleNormalizer());
    }

    private NormalizedNamingAuthority(final int initial,
            final Normalizer normalizer) {
        Preconditions.checkNotNull(normalizer);
        Preconditions.checkArgument(initial >= 0);

        this.counter = initial;
        this.normalizer = normalizer;
    }

    private NormalizedNamingAuthority(final Normalizer normalizer) {
        this(DEFAULT_START, new SimpleNormalizer());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.NamingAuthority
     * #generate()
     */
    @Override
    public String generate() {
        return use(EMPTY_SUBMITTED_PART);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NamingAuthority
     * #getSnapshot()
     */
    @Override
    public Set<String> getSnapshot() {
        return ImmutableSet.copyOf(this.used);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.NamingAuthority
     * #isUsable(java.lang.String)
     */
    @Override
    public boolean isUsable(final String name) {
        Preconditions.checkNotNull(name);

        if (!this.normalizer.convertToNormalChars(name).equals(name)) {
            return false;
        }

        if (this.used.contains(name)) {
            return false;
        }

        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NamingAuthority
     * #isUsed(java.lang.String)
     */
    @Override
    public boolean isUsed(final String name) {
        Preconditions.checkNotNull(name);

        return this.used.contains(name);
    }

    private void readObject(final ObjectInputStream objectInputStream)
            throws ClassNotFoundException, IOException {
        objectInputStream.defaultReadObject();

        Preconditions.checkNotNull(this.normalizer);
        Preconditions.checkArgument(this.counter >= 0);
        Preconditions.checkNotNull(this.used);

        final Collection<String> nonNullUsed =
                Collections2.filter(this.used, Predicates.notNull());
        Preconditions.checkArgument(nonNullUsed.size() == this.used.size());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.NamingAuthority
     * #release(java.lang.String)
     */
    @Override
    public void release(final String oldName) {
        Preconditions.checkNotNull(oldName);

        final boolean contained = this.used.remove(oldName);
        Preconditions.checkArgument(contained);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NamingAuthority
     * #replace(java.lang.String, java.lang.String)
     */
    @Override
    public String replace(final String oldName, final String newName) {
        Preconditions.checkNotNull(oldName);
        Preconditions.checkNotNull(newName);
        Preconditions.checkArgument(isUsed(oldName),
                ExceptionLocalizer.print("NameNotUsed", oldName));
        Preconditions.checkArgument(oldName.equals(newName)
                || isUsable(newName),
                ExceptionLocalizer.print("NameNotUsable", newName));

        release(oldName);
        return use(newName);
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
        builder.append("NormalizedNamingAuthority [used=");
        builder.append(Text.toString(this.used, maxLen));
        builder.append(", counter=");
        builder.append(this.counter);
        builder.append("]");
        return builder.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NamingAuthority
     * #tryReplace(java.util.Map)
     */
    @Override
    public void tryReplace(final Map<? extends String, ? extends String> oldToNew) {
        Preconditions.checkNotNull(oldToNew);

        final ImmutableBiMap<String, String> copy =
                ImmutableBiMap.copyOf(oldToNew);

        for (final Map.Entry<String, String> replacementPair : copy.entrySet()) {
            final String oldName = replacementPair.getKey();
            final String newName = replacementPair.getValue();

            Preconditions.checkArgument(isUsed(oldName),
                    ExceptionLocalizer.print("NameNotUsed", oldName));
            Preconditions.checkArgument(oldName.equals(newName)
                    || isUsable(newName),
                    ExceptionLocalizer.print("NameNotUsable", newName));
        }

        final Set<String> oldNames = copy.keySet();
        final Set<String> newNames = copy.values();
        for (final String oldName : oldNames) {
            this.used.remove(oldName);
        }

        for (final String newName : newNames) {
            this.used.add(newName);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NamingAuthority
     * #tryUse(java.lang.String[])
     */
    @Override
    public void tryUse(final String... names) throws IllegalArgumentException {
        Preconditions.checkNotNull(names);
        Preconditions.checkArgument(Comparisons.allDifferent((Object[]) names));

        for (final String name : names) {
            Preconditions.checkArgument(isUsable(name));
        }

        for (final String name : names) {
            this.used.add(name);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.NamingAuthority
     * #use(java.lang.String)
     */
    @Override
    public String use(final String name) {
        Preconditions.checkNotNull(name);

        final String normalized = this.normalizer.convertToNormalChars(name);

        String generated = normalized;
        if (generated.isEmpty()) {
            generated = Integer.toString(this.counter++);
        }
        while (this.used.contains(generated)) {
            generated = normalized + this.counter++;
        }

        this.used.add(generated);
        return generated;
    }

    private void writeObject(final ObjectOutputStream objectOutputStream)
            throws IOException {
        objectOutputStream.defaultWriteObject();
    }
}
