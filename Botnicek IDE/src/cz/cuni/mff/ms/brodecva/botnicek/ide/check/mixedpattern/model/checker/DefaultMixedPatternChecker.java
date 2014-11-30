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
package cz.cuni.mff.ms.brodecva.botnicek.ide.check.mixedpattern.model.checker;

import java.net.URI;
import java.util.Map;
import java.util.regex.Pattern;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.MixedPattern;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.code.model.checker.CodeChecker;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.checker.CheckResult;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.checker.DefaultCheckResult;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.checker.Source;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.resources.ExceptionLocalizer;
import cz.cuni.mff.ms.brodecva.botnicek.library.api.BotConfiguration;
import cz.cuni.mff.ms.brodecva.botnicek.library.api.LanguageConfiguration;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.AIML;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.XML;

/**
 * Výchozí implementace aplikuje vlastní implementaci normalizéru složeného
 * vzoru jazyka AIML.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see MixedPattern
 */
public class DefaultMixedPatternChecker implements MixedPatternChecker, Source {

    /**
     * Výsledek zpracování značky bot, který může být obsažena v šabloně.
     */
    private static class TagProcessingResult {
        private final CheckResult checkResult;
        private final int indexOffset;

        private TagProcessingResult(final CheckResult checkResult,
                final int indexOffset) {
            this.checkResult = checkResult;
            this.indexOffset = indexOffset;
        }

        /**
         * @return the checkResult
         */
        public CheckResult getCheckResult() {
            return this.checkResult;
        }

        /**
         * @return the indexOffset
         */
        public int getIndex() {
            return this.indexOffset;
        }
    }

    private static final String TAG_PATTERN_TEXT =
            "<(?:[^:]+:)?bot[^/>]+(?:/>|></(?:[^:]+:)?bot>)";

    private static Pattern botTagPattern = java.util.regex.Pattern
            .compile(TAG_PATTERN_TEXT);

    /**
     * Vytvoří validátor.
     * 
     * @param codeChecker
     *            validátor kódu šablony, slouží pro validaci prvků bot ve
     *            vzoru, které se jinak vyskytují i v šablonách
     * 
     * @return validátor
     */
    public static DefaultMixedPatternChecker create(
            final CodeChecker codeChecker) {
        Preconditions.checkNotNull(codeChecker);

        return new DefaultMixedPatternChecker(codeChecker);
    }

    private final CodeChecker codeChecker;

    private DefaultMixedPatternChecker(final CodeChecker codeChecker) {
        this.codeChecker = codeChecker;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.editor.checker.PatternChecker#check
     * (java.lang.String)
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
        int index = 0;
        while (index < characters.length) {
            final int position = index + 1;
            final char character = characters[index];

            if (XML.TAG_START.getValue().equals(Character.toString(character))) {
                inWord = true;

                final TagProcessingResult tagProcessingResult =
                        processTag(source, subject, characters, index);
                final CheckResult tagResult =
                        tagProcessingResult.getCheckResult();
                index = tagProcessingResult.getIndex();

                if (!tagResult.isValid()) {
                    return tagResult;
                }

                index++;
            } else if (character == ' ') {
                if (!inWord || index == characters.length - 1) {
                    return DefaultCheckResult.fail(position,
                            ExceptionLocalizer.print("ExcessiveWhitespace"),
                            source, subject);
                }

                inWord = false;
                index++;
            } else if (Character.isDigit(character)) {
                inWord = true;
                index++;
            } else if (Character.isUpperCase(character)) {
                inWord = true;
                index++;
            } else if (Character.isLetter(character)
                    && !Character.isLowerCase(character)
                    && !Character.isUpperCase(character)
                    && !Character.isTitleCase(character)) {
                inWord = true;
                index++;
            } else if (AIML.STAR_WILDCARD.getValue().equals(
                    Character.toString(character))
                    || AIML.UNDERSCORE_WILDCARD.getValue().equals(
                            Character.toString(character))) {
                inWord = true;
                index++;
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

    private TagProcessingResult processTag(final Object source,
            final Object subject, final char[] characters, final int index) {
        int offset = index;
        final int tagInitialPosition = offset + 1;

        final StringBuilder tagBuilder = new StringBuilder();
        tagBuilder.append(characters[offset]);
        while (!botTagPattern.matcher(tagBuilder).matches()) {
            if (offset >= characters.length - 1) {
                return new TagProcessingResult(DefaultCheckResult.fail(
                        tagInitialPosition,
                        ExceptionLocalizer.print("InvalidBotTag"), source,
                        subject), offset);
            }

            offset++;
            tagBuilder.append(characters[offset]);
        }

        final CheckResult parseResult =
                this.codeChecker.check(tagBuilder.toString());
        if (!parseResult.isValid()) {
            return new TagProcessingResult(
                    DefaultCheckResult.fail(tagInitialPosition,
                            ExceptionLocalizer.print("InvalidBotTag"), source,
                            subject), offset);
        }

        return new TagProcessingResult(DefaultCheckResult.succeed(source,
                subject), offset);
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.check.mixedpattern.model.checker.MixedPatternChecker#getBotSettings()
     */
    @Override
    public BotConfiguration getBotSettings() {
        return this.codeChecker.getBotSettings();
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.check.mixedpattern.model.checker.MixedPatternChecker#getLanguageSettings()
     */
    @Override
    public LanguageConfiguration getLanguageSettings() {
        return this.codeChecker.getLanguageSettings();
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.check.mixedpattern.model.checker.MixedPatternChecker#getNamespacesToPrefixes()
     */
    @Override
    public Map<URI, String> getNamespacesToPrefixes() {
        return this.codeChecker.getNamespacesToPrefixes();
    }
}
