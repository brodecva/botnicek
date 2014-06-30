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

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.check.simplepattern.model.checker.DefaultSimplePatternChecker;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.simplepattern.model.checker.SimplePatternChecker;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.AIML;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public class Patterns {
    
    private static final class SimplePatternImplementation implements SimplePattern {
        private final String text;
        
        public static SimplePatternImplementation create(final String text) {            
            return new SimplePatternImplementation(text);
        }
        
        private SimplePatternImplementation(final String text) {
            Preconditions.checkNotNull(text);
            Preconditions.checkArgument(!text.isEmpty());
            
            this.text = text;
        }

        /* (non-Javadoc)
         * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.Pattern#getText()
         */
        @Override
        public String getText() {
            return this.text;
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
            if (obj == null) {
                return false;
            }
            if (!(obj instanceof SimplePattern)) {
                return false;
            }
            final SimplePattern other = (SimplePattern) obj;
            if (!text.equals(other.getText())) {
                return false;
            }
            return true;
        }
    }
    
    private static SimplePatternChecker checker = new DefaultSimplePatternChecker();
    
    public static SimplePattern createUniversal() {
        return SimplePatternImplementation.create(AIML.STAR_WILDCARD.getValue()); 
    }

    public static SimplePattern create(final String text) {
        Preconditions.checkNotNull(text);
        Preconditions.checkArgument(checker.check(text, text).isValid());
        
        return SimplePatternImplementation.create(text);
    }
    
    public static SimplePattern join(final SimplePattern... patterns) {
        Preconditions.checkNotNull(patterns);
        
        final StringBuilder textBuilder = new StringBuilder();
        for (final SimplePattern pattern : patterns) {
            Preconditions.checkNotNull(pattern);
            
            textBuilder.append(pattern.getText());
        }
        
        return Patterns.create(textBuilder.toString());
    }
}
