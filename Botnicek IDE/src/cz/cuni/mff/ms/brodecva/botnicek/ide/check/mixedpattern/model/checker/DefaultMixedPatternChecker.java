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
package cz.cuni.mff.ms.brodecva.botnicek.ide.check.mixedpattern.model.checker;

import java.util.regex.Pattern;

import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.CheckResult;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.CheckResultImplementation;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.XML;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public class DefaultMixedPatternChecker implements MixedPatternChecker {
    
    private static class TagProcessingResult {
        private final CheckResult checkResult;
        private final int indexOffset;
        
        private TagProcessingResult(final CheckResult checkResult, final int indexOffset) {
            this.checkResult = checkResult;
            this.indexOffset = indexOffset;
        }

        /**
         * @return the checkResult
         */
        public CheckResult getCheckResult() {
            return checkResult;
        }

        /**
         * @return the indexOffset
         */
        public int getIndex() {
            return indexOffset;
        }
    }
    
    private static Pattern botTagPattern = java.util.regex.Pattern.compile("<(?:[^:]:)?bot[^/>]+name=\"[\\p{Digit}\\p{Lu}[\\p{L}&&\\p{IsUppercase}&&\\P{IsLowercase}&&\\P{IsLowercase}]]+\"[^/>]*(?:/>|></(?:[^:]:)?bot>)");
    
    public static DefaultMixedPatternChecker create() {
        return new DefaultMixedPatternChecker();
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.editor.checker.PatternChecker#check(java.lang.String)
     */
    @Override
    public CheckResult check(final Object source, final String patternContent) {
        if (patternContent.isEmpty()) {
            return CheckResultImplementation.fail(source, 0, "The pattern is empty.");
        }
        
        boolean inWord = false;
        
        final char[] characters = patternContent.toCharArray();
        int index = 0;
        while (index < characters.length) {
            final int position = index + 1;
            final char character = characters[index];
            
            if (XML.TAG_START.equals(Character.toString(character))) {
                inWord = true;
                
                final TagProcessingResult tagProcessingResult = processTag(source, characters, index);
                final CheckResult tagResult = tagProcessingResult.getCheckResult();
                index = tagProcessingResult.getIndex();
                
                if (!tagResult.isValid()) {
                    return tagResult;
                }
                
                index++;
            } else if (character == ' ') {
                if (!inWord || index == characters.length - 1) {
                    return CheckResultImplementation.fail(source, position, "Excesive whitespace at position %1$s.", position);
                }
                
                inWord = false;
                index++;
            } else if (Character.isDigit(character)) {
                inWord = true;
                index++;
            } else if (Character.isUpperCase(character)) {
                inWord = true;
                index++;
            } else if (Character.isLetter(character) && !Character.isLowerCase(character) && !Character.isUpperCase(character) && !Character.isTitleCase(character)) {
                inWord = true;
                index++;
            } else {
                return CheckResultImplementation.fail(source, position, "Invalid character at position %1$s.", position);
            }
        }
        
        return CheckResultImplementation.succeed(source);
    }

    private static TagProcessingResult processTag(final Object source, final char[] characters, final int index) {
        int offset = index;
        final int tagInitialPosition = offset + 1;
        
        final StringBuilder tagBuilder = new StringBuilder(characters[offset]);
        while (!botTagPattern.matcher(tagBuilder).matches()) {
            if (offset >= characters.length) {
                return new TagProcessingResult(CheckResultImplementation.fail(source, tagInitialPosition, "Invalid bot tag starting at %1$s", tagInitialPosition), offset);
            }
            
            offset++;
            tagBuilder.append(characters[offset]);
        }
        
        return new TagProcessingResult(CheckResultImplementation.succeed(source), offset);
    }
}
