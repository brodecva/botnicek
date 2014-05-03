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
package cz.cuni.mff.ms.brodecva.botnicek.ide.editor.checker.simplepattern;

import java.util.regex.Pattern;

import cz.cuni.mff.ms.brodecva.botnicek.ide.editor.checker.CheckResult;
import cz.cuni.mff.ms.brodecva.botnicek.ide.editor.checker.CheckResultImplementation;


/**
 * @author Václav Brodec
 * @version 1.0
 */
public class DefaultSimplePatternChecker implements SimplePatternChecker {
    
    public static DefaultSimplePatternChecker create() {
        return new DefaultSimplePatternChecker();
    }

    @Override
    public CheckResult check(final String patternContent) {
        if (patternContent.isEmpty()) {
            return CheckResultImplementation.fail(0, "The pattern is empty.");
        }
        
        boolean inWord = false;
        
        final char[] characters = patternContent.toCharArray();
        for (int index = 0; index < characters.length; index++) {
            final int position = index + 1;
            final char character = characters[index]; 
            
            if (character == ' ') {
                if (!inWord || index == characters.length - 1) {
                    return CheckResultImplementation.fail(position, "Excesive whitespace at position %1$s.", position);
                }
                inWord = false;
            } else if (Character.isDigit(character)) {
                inWord = true;
            } else if (Character.isUpperCase(character)) {
                inWord = true;
            } else if (Character.isLetter(character) && !Character.isLowerCase(character) && !Character.isUpperCase(character) && !Character.isTitleCase(character)) {
                inWord = true;
            } else {
                return CheckResultImplementation.fail(position, "Invalid character at position %1$s.", position);
            }
        }
        
        return CheckResultImplementation.succeed();
    }
}
