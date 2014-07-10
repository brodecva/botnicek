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

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;

import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.CheckResult;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.words.model.checker.DefaultNormalWordChecker;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.words.model.checker.NormalWordChecker;
import cz.cuni.mff.ms.brodecva.botnicek.library.preprocessor.Normalizer;
import cz.cuni.mff.ms.brodecva.botnicek.library.preprocessor.SimpleNormalizer;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public class NormalWords {
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
    }
    
    private static NormalWordChecker checker = DefaultNormalWordChecker.create();
    
    public static NormalWord of(final String text) {
        Preconditions.checkNotNull(text);
        
        final CheckResult result = checker.check(text, text);
        Preconditions.checkArgument(result.isValid(), result.getMessage());
        
        return NormalWordImplementation.create(text);
    }
    
    public static NormalWord from(final String text) {
        Preconditions.checkNotNull(text);
        Preconditions.checkArgument(!text.isEmpty(), "Text je prázdný.");
        
        final Normalizer normalizer = new SimpleNormalizer();
        
        final String converted = normalizer.convertToNormalChars(text);
        Preconditions.checkArgument(!converted.isEmpty(), "Nelze převést na normální název.");
        
        return NormalWordImplementation.create(converted);
    }
    
    public static NormalWord join(final NormalWord... names) {
        Preconditions.checkNotNull(names);
        
        final StringBuilder textBuilder = new StringBuilder();
        for (final NormalWord name : names) {
            Preconditions.checkNotNull(name);
            
            textBuilder.append(name.getText());
        }
        
        return of(textBuilder.toString());
    }
    
    public static Map<String, String> toUntyped(final Map<NormalWord, String> from) {
        Preconditions.checkNotNull(from);
        
        return transformKeys(from, new Function<NormalWord, String>() {

            @Override
            public String apply(NormalWord input) {
                Preconditions.checkNotNull(input);
                
                return input.getText();
            }
            
        });
    }
    
    public static Map<NormalWord, String> toTyped(final Map<String, String> from) {
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
