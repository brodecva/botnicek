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
package cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.types;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.library.platform.AIML;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public class Patterns {
    private static final class PatternImplementation implements SimplePattern {
        private final String text;
        
        public static PatternImplementation create(final String text) {
            return new PatternImplementation(text);
        }
        
        private PatternImplementation(final String text) {
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
    }
    
    public static SimplePattern createUniversal() {
        return PatternImplementation.create(AIML.STAR_WILDCARD.getValue()); 
    }

    /**
     * @param string
     * @return
     */
    public static SimplePattern create(final String string) {
        
        
        return PatternImplementation.create(string);
    }
}
