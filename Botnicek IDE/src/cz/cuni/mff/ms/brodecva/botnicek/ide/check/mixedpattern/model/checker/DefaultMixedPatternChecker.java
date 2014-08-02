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
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.DefaultCheckResult;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.Source;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.resources.ExceptionLocalizer;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.AIML;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.XML;

/**
 * Výchozí implementace aplikuje vlastní implementaci normalizéru složeného vzoru jazyka AIML.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public class DefaultMixedPatternChecker implements MixedPatternChecker {
    
    /**
     * Výsledek zpracování značky bot, který může být obsažena v šabloně.
     */
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
    
    /**
     * Vytvoří validátor.
     * 
     * @return validátor
     */
    public static DefaultMixedPatternChecker create() {
        return new DefaultMixedPatternChecker();
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.editor.checker.PatternChecker#check(java.lang.String)
     */
    @Override
    public CheckResult check(final Source source, Object subject, final String patternContent) {
        if (patternContent.isEmpty()) {
            return DefaultCheckResult.fail(0, ExceptionLocalizer.print("EmptyPattern"), source, subject);
        }
        
        boolean inWord = false;
        
        final char[] characters = patternContent.toCharArray();
        int index = 0;
        while (index < characters.length) {
            final int position = index + 1;
            final char character = characters[index];
            
            if (XML.TAG_START.equals(Character.toString(character))) {
                inWord = true;
                
                final TagProcessingResult tagProcessingResult = processTag(source, subject, characters, index);
                final CheckResult tagResult = tagProcessingResult.getCheckResult();
                index = tagProcessingResult.getIndex();
                
                if (!tagResult.isValid()) {
                    return tagResult;
                }
                
                index++;
            } else if (character == ' ') {
                if (!inWord || index == characters.length - 1) {
                    return DefaultCheckResult.fail(position, ExceptionLocalizer.print("ExcessiveWhitespace"), source, subject);
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
            } else if (AIML.STAR_WILDCARD.getValue().equals(Character.toString(character)) || AIML.UNDERSCORE_WILDCARD.getValue().equals(Character.toString(character))) {
                inWord = true;
                index++;
            } else {
                return DefaultCheckResult.fail(position, ExceptionLocalizer.print("InvalidCharacter"), source, subject);
            }
        }
        
        return DefaultCheckResult.succeed(source, subject);
    }

    private static TagProcessingResult processTag(final Object source, Object subject, final char[] characters, final int index) {
        int offset = index;
        final int tagInitialPosition = offset + 1;
        
        final StringBuilder tagBuilder = new StringBuilder(characters[offset]);
        while (!botTagPattern.matcher(tagBuilder).matches()) {
            if (offset >= characters.length) {
                return new TagProcessingResult(DefaultCheckResult.fail(tagInitialPosition, ExceptionLocalizer.print("InvalidBotTag"), source, subject), offset);
            }
            
            offset++;
            tagBuilder.append(characters[offset]);
        }
        
        return new TagProcessingResult(DefaultCheckResult.succeed(source, subject), offset);
    }
}
