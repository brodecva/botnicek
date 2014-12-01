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
package cz.cuni.mff.ms.brodecva.botnicek.library.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.cuni.mff.ms.brodecva.botnicek.library.logging.BotnicekLogger;

/**
 * Pomocná třída pro práci s textem.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class Text {
    
    /**
     * Logger.
     */
    private static final Logger LOGGER = BotnicekLogger
            .getLogger(Text.class);
    
    /**
     * Základ desítkové soustavy.
     */
    private static final int DECIMAL_BASE = 10;

    /**
     * Nejvyšší cifra při zápisu čísla v desítkové soustavě.
     */
    private static final int HIGHEST_DECIMAL_DIGIT = 9;

    /**
     * Lokalizátor hlášek výjimek.
     */
    private static final ExceptionMessageLocalizer MESSAGE_LOCALIZER =
            ExceptionMessageLocalizer.getLocalizer();

    /**
     * Vzor pro nalezení žolíků ze složených závorek.
     */
    private static final Pattern BRACE_WILDCARD = Pattern
            .compile("\\{(0|[1-9][0-9]*)\\}");

    /**
     * <p>
     * Nahradí ve vstupu všechny výskyty klíčů nahrazení příslušnými hodnotami.
     * {@code Null} není přípustný mezi argumenty a nahrazeními.
     * </p>
     * <p>
     * Klíče nahrazení jsou regulární výrazy, hodnoty prosté řetězce.
     * </p>
     * 
     * @param input
     *            vstup
     * @param replacements
     *            nahrazení
     * @return vstup s nahrazenými výskyty
     */
    public static String replace(final String input,
            final Map<Pattern, String> replacements) {
        LOGGER.log(Level.INFO, "utils.TextReplaceStart", new Object[] { input, replacements });
        
        if (input == null) {
            throw new NullPointerException(
                    MESSAGE_LOCALIZER.getMessage("utils.NullArgument"));
        }

        final List<String> unprocessed = new LinkedList<String>();
        unprocessed.add(input);

        final LinkedList<String> substitutions = new LinkedList<String>();

        for (final Pattern find : replacements.keySet()) {
            Matcher matcher = null;

            final ListIterator<String> unprocessedIterator =
                    unprocessed.listIterator(0);

            boolean beginning = true;

            while (unprocessedIterator.hasNext()) {
                final String processing = unprocessedIterator.next();

                if (matcher != null) {
                    matcher.reset(processing);
                } else {
                    matcher = find.matcher(processing);
                }

                if (matcher.find()) {
                    final int startIndex = matcher.start();
                    final int endIndex = matcher.end();

                    // Pro případ, že zachytí prázdný řetězec.
                    int unprocessedEnd = startIndex;
                    int remainingStart = endIndex;
                    if (remainingStart == startIndex) {
                        if (!beginning) {
                            unprocessedEnd++;
                            remainingStart++;
                        }
                    }

                    beginning = false;

                    if (remainingStart > processing.length()) {
                        continue;
                    }

                    final String rawSubstitution = replacements.get(find);
                    final String substituted =
                            applyReplacement(matcher, processing,
                                    rawSubstitution);
                    substitutions.add(unprocessedIterator.nextIndex() - 1,
                            substituted);

                    final String newUnprocessed =
                            processing.substring(0, unprocessedEnd);
                    unprocessedIterator.set(newUnprocessed);

                    final String remaining =
                            processing.substring(remainingStart);
                    unprocessedIterator.add(remaining);
                    unprocessedIterator.previous();
                }
            }
        }

        final StringBuilder result = new StringBuilder();

        final ListIterator<String> unprocessedIterator =
                unprocessed.listIterator(0);
        final ListIterator<String> subsIterator = substitutions.listIterator(0);
        while (unprocessedIterator.hasNext()) {
            result.append(unprocessedIterator.next());

            if (subsIterator.hasNext()) {
                result.append(subsIterator.next());
            }
        }
        
        LOGGER.log(Level.INFO, "utils.TextReplaced", new Object[] { input, result });

        return result.toString();
    }

    /**
     * <p>
     * Aplikuje nahrazení (včetně skupin).
     * </p>
     * <p>
     * Podle <a href="http://openjdk.java.net">openJDK</a>
     * </p>
     * 
     * @param matcher
     *            {@link Matcher}, který v předchozím kroku úspěšně nalezl vzor
     * @param text
     *            původní text
     * @param replacement
     *            nahrazení
     * @return nahrazený řetězec
     * @see java.util.regex.Matcher#appendReplacement(StringBuffer, String) pro formát nahrazení
     */
    private static String applyReplacement(final Matcher matcher,
            final String text, final String replacement) {
        final StringBuilder result = new StringBuilder();

        int cursor = 0;
        while (cursor < replacement.length()) {
            char nextChar = replacement.charAt(cursor);
            if (nextChar == '\\') {
                cursor++;
                nextChar = replacement.charAt(cursor);
                result.append(nextChar);
                cursor++;
            } else if (nextChar == '$') {
                cursor++;
                nextChar = replacement.charAt(cursor);
                int refNum = -1;
                if (nextChar == '{') {
                    cursor++;
                    final StringBuilder gsb = new StringBuilder();
                    while (cursor < replacement.length()) {
                        nextChar = replacement.charAt(cursor);
                        if (isAsciiLower(nextChar) || isAsciiUpper(nextChar)
                                || isAsciiDigit(nextChar)) {
                            gsb.append(nextChar);
                            cursor++;
                        } else {
                            break;
                        }
                    }
                    if (gsb.length() == 0) {
                        throw new IllegalArgumentException(MESSAGE_LOCALIZER.getMessage("utils.EmptyGroupName"));
                    }
                    if (nextChar != '}') {
                        throw new IllegalArgumentException(MESSAGE_LOCALIZER.getMessage("utils.OpenGroupName"));
                    }
                    final String gname = gsb.toString();
                    if (isAsciiDigit(gname.charAt(0))) {
                        throw new IllegalArgumentException(MESSAGE_LOCALIZER.getMessage("utils.DigitStartGroupName", gname));
                    }

                    final Map<String, Integer> namedGroups =
                            getNamedGroups(matcher.pattern());

                    if (!namedGroups.containsKey(gname)) {
                        throw new IllegalArgumentException(MESSAGE_LOCALIZER.getMessage("utils.NoGroupWithNameOrSupport", gname));
                    }
                    refNum = namedGroups.get(gname);
                    cursor++;
                } else {
                    refNum = (int) nextChar - '0';
                    if ((refNum < 0) || (refNum > HIGHEST_DECIMAL_DIGIT)) {
                        throw new IllegalArgumentException(MESSAGE_LOCALIZER.getMessage("utils.IllegalgroupReference"));
                    }

                    cursor++;

                    boolean done = false;
                    while (!done) {
                        if (cursor >= replacement.length()) {
                            break;
                        }
                        final int nextDigit = replacement.charAt(cursor) - '0';
                        if ((nextDigit < 0)
                                || (nextDigit > HIGHEST_DECIMAL_DIGIT)) {
                            break;
                        }
                        final int newRefNum =
                                (refNum * DECIMAL_BASE) + nextDigit;
                        if (matcher.groupCount() < newRefNum) {
                            done = true;
                        } else {
                            refNum = newRefNum;
                            cursor++;
                        }
                    }
                }
                if (matcher.start(refNum) != -1 && matcher.end(refNum) != -1) {
                    result.append(text, matcher.start(refNum),
                            matcher.end(refNum));
                }
            } else {
                result.append(nextChar);
                cursor++;
            }
        }

        return result.toString();
    }

    /**
     * Toto je {@link java.lang.Character#isDigit(char)} pro ASCII znak.
     * 
     * @param ch
     *            znak v ACII
     * @return true, pokud je znak číslo
     */
    private static boolean isAsciiDigit(final int ch) {
        return ((ch - '0') | ('9' - ch)) >= 0;
    }

    /**
     * Toto je {@link java.lang.Character#isLower(char)} pro ASCII znak.
     * 
     * @param ch
     *            znak v ACII
     * @return true, pokud je znak malé písmeno
     */
    private static boolean isAsciiLower(final int ch) {
        return ((ch - 'a') | ('z' - ch)) >= 0;
    }

    /**
     * Toto je {@link java.lang.Character#isUpper(char)} pro ASCII znak.
     * 
     * @param ch
     *            znak v ACII
     * @return true, pokud je znak velké písmeno
     */
    private static boolean isAsciiUpper(final int ch) {
        return ((ch - 'A') | ('Z' - ch)) >= 0;
    }

    /**
     * <p>
     * Vrátí zobrazení názvů pojmenovaných skupin na čísla.
     * </p>
     * <p>
     * V případě selhání vrátí prázdnou {@link Map}.
     * </p>
     * 
     * @param pattern
     *            vzor
     * @return zobrazení názvů skupin na pořadí
     */
    private static Map<String, Integer> getNamedGroups(final Pattern pattern) {
        try {
            final Method namedGroupsMethod =
                    Pattern.class.getDeclaredMethod("namedGroups");
            namedGroupsMethod.setAccessible(true);

            @SuppressWarnings("unchecked")
            final Map<String, Integer> result =
                    (Map<String, Integer>) namedGroupsMethod.invoke(pattern);

            return new HashMap<String, Integer>(result);
        } catch (NoSuchMethodException | SecurityException
                | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            return new HashMap<String, Integer>();
        }
    }

    /**
     * Nahradí výskyty žolíku typu {číslo} za žolíky dle třídy
     * {@link java.util.Formatter}.
     * 
     * @param textWithBraceWildcards
     *            text používající {0} formát žolíků pro substituci
     * @return text používající formát třídy Formatter
     * @see java.util.Formatter pro definici výstupního formátu
     */
    public static String substituteBraceWildcards(
            final String textWithBraceWildcards) {
        if (textWithBraceWildcards == null) {
            throw new NullPointerException(
                    MESSAGE_LOCALIZER.getMessage("utils.NullArgument"));
        }

        final Matcher braceWildcards =
                BRACE_WILDCARD.matcher(textWithBraceWildcards);

        final StringBuffer result = new StringBuffer();
        while (braceWildcards.find()) {
            braceWildcards.appendReplacement(
                    result,
                    "%"
                            + String.valueOf(Integer.parseInt(braceWildcards
                                    .group(1)) + 1 + "\\$s"));
        }
        braceWildcards.appendTail(result);

        return result.toString();
    }
    
    /**
     * Vypíše nejvýše prvních {@code maxLen} prvků kolekce.
     * 
     * @param collection kolekce
     * @param maxLen maximální počet prvků ve výpisu
     * @return vypsaných nejvýše prvních {@code maxLen} prvků kolekce
     */
    public static String toString(final Collection<?> collection, final int maxLen) {
        final StringBuilder builder = new StringBuilder();
        builder.append("[");
        int i = 0;
        for (final Iterator<?> iterator = collection.iterator(); iterator.hasNext()
                && i < maxLen; i++) {
            if (i > 0) {
                builder.append(", ");
            }
            builder.append(iterator.next());
        }
        builder.append("]");
        return builder.toString();
    }

    /**
     * Skrytý konstruktor.
     */
    private Text() {
    }
}
