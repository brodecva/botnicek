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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.checker.CheckResult;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.checker.Checker;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.words.model.checker.DefaultNormalWordChecker;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.Objects;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.resources.ExceptionLocalizer;
import cz.cuni.mff.ms.brodecva.botnicek.library.preprocessor.Normalizer;
import cz.cuni.mff.ms.brodecva.botnicek.library.preprocessor.SimpleNormalizer;

/**
 * Pomocné metody pro práci s normálními slovy.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class NormalWords {

    /**
     * Interní implementace.
     */
    private static final class NormalWordImplementation implements NormalWord,
            Serializable {
        private static final long serialVersionUID = 1L;

        public static NormalWordImplementation create(final String text) {
            return new NormalWordImplementation(text);
        }

        private final String text;

        private NormalWordImplementation(final String text) {
            Preconditions.checkNotNull(text);
            Preconditions.checkArgument(!text.isEmpty());

            this.text = text;
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.lang.Comparable#compareTo(java.lang.Object)
         */
        @Override
        public int compareTo(final NormalWord other) {
            Preconditions.checkNotNull(other);

            return this.text.compareTo(other.getText());
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
            if (!(obj instanceof NormalWord)) {
                return false;
            }
            final NormalWord other = (NormalWord) obj;
            if (!this.text.equals(other.getText())) {
                return false;
            }
            return true;
        }

        @Override
        public String getText() {
            return this.text;
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
            result = prime * result + this.text.hashCode();
            return result;
        }

        private void readObject(final ObjectInputStream objectInputStream)
                throws ClassNotFoundException, IOException {
            objectInputStream.defaultReadObject();

            Preconditions.checkNotNull(this.text);

            final CheckResult result = checker.check(this.text);
            Preconditions.checkArgument(result.isValid(), result.getMessage());
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return "NormalWordImplementation [text=" + this.text + "]";
        }

        private void writeObject(final ObjectOutputStream objectOutputStream)
                throws IOException {
            objectOutputStream.defaultWriteObject();
        }
    }

    private static Checker checker = DefaultNormalWordChecker
            .create();
    private static Normalizer normalizer = new SimpleNormalizer();

    /**
     * Vytvoří z textu normální slovo ve stylu "nejlepší snahy".
     * 
     * @param text
     *            text
     * @return normální slovo
     * @throws IllegalArgumentException
     *             v případě, že slovo nelze převést
     */
    public static NormalWord from(final String text) {
        Preconditions.checkNotNull(text);
        Preconditions.checkArgument(!text.isEmpty(),
                ExceptionLocalizer.print("EmptyText"));

        final String converted = normalizer.convertToNormalChars(text);
        Preconditions.checkArgument(!converted.isEmpty(),
                ExceptionLocalizer.print("CannotBeNormalized"));

        return NormalWordImplementation.create(converted);
    }

    /**
     * Sloučí normální slova do jednoho.
     * 
     * @param names
     *            normální slova
     * @return spojené normální slovo
     */
    public static NormalWord join(final List<? extends NormalWord> names) {
        Preconditions.checkNotNull(names);
        Preconditions.checkArgument(!names.isEmpty());

        final StringBuilder textBuilder = new StringBuilder();
        for (final NormalWord name : names) {
            Preconditions.checkNotNull(name);

            textBuilder.append(name.getText());
        }

        return of(textBuilder.toString());
    }

    /**
     * Sloučí normální slova do jednoho.
     * 
     * @param names
     *            normální slova
     * @return spojené normální slovo
     */
    public static NormalWord join(final NormalWord... names) {
        Preconditions.checkNotNull(names);

        return join(ImmutableList.copyOf(names));
    }

    /**
     * Převede text vyhovující syntaxi normálního slova na normální slovo.
     * 
     * @param text
     *            text
     * @return normální slovo
     * @throws IllegalArgumentException
     *             v případě, že slovo nelze převést
     */
    public static NormalWord of(final String text) {
        Preconditions.checkNotNull(text);

        final CheckResult result = checker.check(text);
        Preconditions.checkArgument(result.isValid(), result.getMessage());

        return NormalWordImplementation.create(text);
    }

    /**
     * Převede klíče v podobě řetězců tabulky na normální slova. Klíče musí
     * vyhovovat syntaxi.
     * 
     * @param from
     *            zdroj
     * @return výstup
     */
    public static <V> Map<NormalWord, V> toTyped(final Map<? extends String, ? extends V> from) {
        Preconditions.checkNotNull(from);

        return transformKeys(from, new Function<String, NormalWord>() {

            @Override
            public NormalWord apply(final String input) {
                Preconditions.checkNotNull(input);

                return of(input);
            }
        });
    }

    /**
     * Převede klíče v podobě normálních slov tabulky na řetězce.
     * 
     * @param from
     *            zdroj
     * @return výstup
     */
    public static <V> Map<String, V> toUntyped(final Map<? extends NormalWord, ? extends V> from) {
        Preconditions.checkNotNull(from);

        return transformKeys(from, new Function<NormalWord, String>() {

            @Override
            public String apply(final NormalWord input) {
                Preconditions.checkNotNull(input);

                return input.getText();
            }

        });
    }

    private static <K, V, L> Map<L, V> transformKeys(final Map<? extends K, ? extends V> from,
            final Function<K, L> transformation) {
        Preconditions.checkNotNull(from);
        Preconditions.checkNotNull(transformation);

        final ImmutableMap.Builder<L, V> builder = ImmutableMap.builder();
        final Set<? extends Entry<? extends K, ? extends V>> fromEntries = from.entrySet();
        for (final Entry<? extends K, ? extends V> entry : fromEntries) {
            final K key = entry.getKey();
            final V value = entry.getValue();

            Preconditions.checkNotNull(key);
            Preconditions.checkNotNull(value);

            builder.put(transformation.apply(key), value);
        }

        return builder.build();
    }
}
