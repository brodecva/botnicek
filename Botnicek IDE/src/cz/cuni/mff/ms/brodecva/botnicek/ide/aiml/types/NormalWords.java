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

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.CheckResult;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.Source;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.words.model.checker.DefaultNormalWordChecker;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.words.model.checker.NormalWordChecker;
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
    private static final class NormalWordImplementation implements NormalWord {
        private final String text;
        
        public static NormalWordImplementation create(final String text) {
            return new NormalWordImplementation(text);
        }
        
        private NormalWordImplementation(final String text) {
            Preconditions.checkNotNull(text);
            Preconditions.checkArgument(!text.isEmpty());
            
            this.text = text;
        }

        @Override
        public String getText() {
            return this.text;
        }

        /* (non-Javadoc)
         * @see java.lang.Comparable#compareTo(java.lang.Object)
         */
        @Override
        public int compareTo(final NormalWord other) {
            Preconditions.checkNotNull(other);
            
            return this.text.compareTo(other.getText());
        }

        /* (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return "NormalWordImplementation [text=" + text + "]";
        }

        /* (non-Javadoc)
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + text.hashCode();
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
            if (!(obj instanceof NormalWord)) {
                return false;
            }
            NormalWord other = (NormalWord) obj;
            if (!text.equals(other.getText())) {
                return false;
            }
            return true;
        }
    }
    
    private static NormalWordChecker checker = DefaultNormalWordChecker.create();
    
    /**
     * Převede text vyhovující syntaxi normálního slova na normální slovo.
     * 
     * @param text text
     * @return normální slovo
     * @throws IllegalArgumentException v případě, že slovo nelze převést
     */
    public static NormalWord of(final String text) {
        Preconditions.checkNotNull(text);
        
        final CheckResult result = checker.check(new Source() {}, text, text);
        Preconditions.checkArgument(result.isValid(), result.getMessage());
        
        return NormalWordImplementation.create(text);
    }
    
    /**
     * Vytvoří z textu normální slovo ve stylu "nejlepší snahy".
     * 
     * @param text text
     * @return normální slovo
     * @throws IllegalArgumentException v případě, že slovo nelze převést
     */
    public static NormalWord from(final String text) {
        Preconditions.checkNotNull(text);
        Preconditions.checkArgument(!text.isEmpty(), ExceptionLocalizer.print("EmptyText"));
        
        final Normalizer normalizer = new SimpleNormalizer();
        
        final String converted = normalizer.convertToNormalChars(text);
        Preconditions.checkArgument(!converted.isEmpty(), ExceptionLocalizer.print("CannotBeNormalized"));
        
        return NormalWordImplementation.create(converted);
    }
    
    /**
     * Sloučí normální slova do jednoho.
     * 
     * @param names normální slova
     * @return spojené normální slovo
     */
    public static NormalWord join(final NormalWord... names) {
        Preconditions.checkNotNull(names);
        
        return join(ImmutableList.copyOf(names));
    }
    
    /**
     * Sloučí normální slova do jednoho.
     * 
     * @param names normální slova
     * @return spojené normální slovo
     */
    public static NormalWord join(final List<NormalWord> names) {
        Preconditions.checkNotNull(names);
        
        final StringBuilder textBuilder = new StringBuilder();
        for (final NormalWord name : names) {
            Preconditions.checkNotNull(name);
            
            textBuilder.append(name.getText());
        }
        
        return of(textBuilder.toString());
    }
    
    /**
     * Převede klíče v podobě normálních slov tabulky na řetězce.
     * 
     * @param from zdroj
     * @return výstup
     */
    public static <V> Map<String, V> toUntyped(final Map<NormalWord, V> from) {
        Preconditions.checkNotNull(from);
        
        return transformKeys(from, new Function<NormalWord, String>() {

            @Override
            public String apply(final NormalWord input) {
                Preconditions.checkNotNull(input);
                
                return input.getText();
            }
            
        });
    }
    
    /**
     * Převede klíče v podobě řetězců tabulky na normální slova. Klíče musí vyhovovat syntaxi.
     * 
     * @param from zdroj
     * @return výstup
     */
    public static <V> Map<NormalWord, V> toTyped(final Map<String, V> from) {
        Preconditions.checkNotNull(from);
        
        return transformKeys(from, new Function<String, NormalWord>() {

            @Override
            public NormalWord apply(final String input) {
                Preconditions.checkNotNull(input);
                
                return of(input);
            }
        });
    }
    
    private static <K, V, L> Map<L, V> transformKeys(final Map<K, V> from, final Function<K, L> transformation) {
        Preconditions.checkNotNull(from);
        Preconditions.checkNotNull(transformation);
        
        final ImmutableMap.Builder<L, V> builder = ImmutableMap.builder();
        final Set<Entry<K, V>> fromEntries = from.entrySet();
        for (final Entry<K, V> entry : fromEntries) {
            final K key = entry.getKey();
            final V value = entry.getValue();
            
            Preconditions.checkNotNull(key);
            Preconditions.checkNotNull(value);
            
            builder.put(transformation.apply(key), value);
        }
        
        return builder.build();
    }
}
