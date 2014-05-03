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
package cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design;

import java.util.HashSet;
import java.util.Set;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.library.preprocessor.Normalizer;
import cz.cuni.mff.ms.brodecva.botnicek.library.preprocessor.SimpleNormalizer;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public class NormalizedNamingAuthority implements NamingAuthority {
    
    private static final String EMPTY_SUBMITTED_PART = "";

    private static final int DEFAULT_START = 1;
    
    private int counter = DEFAULT_START;
    
    private final Set<String> used = new HashSet<>();

    private final Normalizer normalizer;
    
    public static NormalizedNamingAuthority create() {
        return new NormalizedNamingAuthority();
    }
    
    public static NormalizedNamingAuthority create(final int initial) {
        return new NormalizedNamingAuthority(initial);
    }
    
    public static NormalizedNamingAuthority create(final Normalizer normalizer) {
        return new NormalizedNamingAuthority(normalizer);
    }
    
    public static NormalizedNamingAuthority create(final int initial, final Normalizer normalizer) {
        return new NormalizedNamingAuthority(initial, normalizer);
    }
    
    private NormalizedNamingAuthority() {
        this(DEFAULT_START, new SimpleNormalizer());
    }
    
    private NormalizedNamingAuthority(final int initial) {
        this(initial, new SimpleNormalizer());
    }
    
    private NormalizedNamingAuthority(final Normalizer normalizer) {
        this(DEFAULT_START, new SimpleNormalizer());
    }
    
    private NormalizedNamingAuthority(final int initial, final Normalizer normalizer) {
        Preconditions.checkNotNull(normalizer);
        Preconditions.checkArgument(initial > DEFAULT_START);
        
        this.counter = initial;
        this.normalizer = normalizer;
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.NamingAuthority#generate()
     */
    @Override
    public String generate() {
        return use(EMPTY_SUBMITTED_PART);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.NamingAuthority#use(java.lang.String)
     */
    @Override
    public String use(final String name) {
        Preconditions.checkNotNull(name);
        
        final String normalized = this.normalizer.convertToNormalChars(name);
        
        String generated = normalized;
        if (generated.isEmpty()) {
            generated = Integer.toString(this.counter++);
        }
        while(this.used.contains(generated)) {
            generated = normalized + this.counter++;
        }
        
        this.used.add(generated);
        return generated;
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.NamingAuthority#isUsable(java.lang.String)
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
    
    public void tryUse(final String... names) {
        Preconditions.checkNotNull(names);
        
        for (final String name : names) {
            Preconditions.checkArgument(isUsable(name));
        }
        
        for (final String name : names) {
            this.used.add(name);
        }
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.NamingAuthority#release(java.lang.String)
     */
    @Override
    public void release(final String oldName) {
        Preconditions.checkNotNull(oldName);
        
        final boolean contained = this.used.remove(oldName);
        Preconditions.checkArgument(contained);
    }
}
