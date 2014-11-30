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
package cz.cuni.mff.ms.brodecva.botnicek.ide.check.simplepattern.model.checker;

import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.checker.CheckResult;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.checker.Checker;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.checker.DefaultCheckResult;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.checker.Source;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.resources.ExceptionLocalizer;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.AIML;

/**
 * Výchozí implementace aplikuje vlastní implementaci normalizéru prostého vzoru
 * jazyka AIML.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class DefaultSimplePatternChecker implements Checker,
        Source {

    /**
     * Vytvoří validátor.
     * 
     * @return validátor
     */
    public static DefaultSimplePatternChecker create() {
        return new DefaultSimplePatternChecker();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.Checker#check
     * (cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.Source,
     * java.lang.Object, java.lang.String)
     */
    @Override
    public CheckResult check(final Source source, final Object subject,
            final String patternContent) {
        if (patternContent.isEmpty()) {
            return DefaultCheckResult.fail(0,
                    ExceptionLocalizer.print("EmptyPattern"), source, subject);
        }

        boolean inWord = false;

        final char[] characters = patternContent.toCharArray();
        for (int index = 0; index < characters.length; index++) {
            final int position = index + 1;
            final char character = characters[index];

            if (character == ' ') {
                if (!inWord || index == characters.length - 1) {
                    return DefaultCheckResult.fail(position,
                            ExceptionLocalizer.print("ExcessiveWhitespace"),
                            source, subject);
                }
                inWord = false;
            } else if (Character.isDigit(character)) {
                inWord = true;
            } else if (Character.isUpperCase(character)) {
                inWord = true;
            } else if (Character.isLetter(character)
                    && !Character.isLowerCase(character)
                    && !Character.isUpperCase(character)
                    && !Character.isTitleCase(character)) {
                inWord = true;
            } else if (AIML.STAR_WILDCARD.getValue().equals(
                    Character.toString(character))
                    || AIML.UNDERSCORE_WILDCARD.getValue().equals(
                            Character.toString(character))) {
                inWord = true;
            } else {
                return DefaultCheckResult.fail(position,
                        ExceptionLocalizer.print("InvalidCharacter"), source,
                        subject);
            }
        }

        return DefaultCheckResult.succeed(source, subject);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.Checker#check
     * (java.lang.String)
     */
    @Override
    public CheckResult check(final String content) {
        return check(this, new Object(), content);
    }
}
