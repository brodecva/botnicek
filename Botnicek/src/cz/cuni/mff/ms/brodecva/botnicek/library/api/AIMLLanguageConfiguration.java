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
package cz.cuni.mff.ms.brodecva.botnicek.library.api;

import static cz.cuni.mff.ms.brodecva.botnicek.library.utils.Configuration.readReplacements;
import static cz.cuni.mff.ms.brodecva.botnicek.library.utils.Property.toMap;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import cz.cuni.mff.ms.brodecva.botnicek.library.logging.BotnicekLogger;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.Configuration;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.ExceptionMessageLocalizer;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.Text;

/**
 * Konfigurace jazyka.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see cz.cuni.mff.ms.brodecva.botnicek.library.language.Language
 */
public final class AIMLLanguageConfiguration implements LanguageConfiguration,
        Serializable {

    /**
     * UID serializované verze.
     */
    private static final long serialVersionUID = -1372788897997147873L;

    /**
     * Lokalizátor hlášek výjimek.
     */
    private static final ExceptionMessageLocalizer MESSAGE_LOCALIZER =
            ExceptionMessageLocalizer.getLocalizer();

    /**
     * Logger.
     */
    private static final Logger LOGGER = BotnicekLogger
            .getLogger(AIMLLanguageConfiguration.class);

    /**
     * Klíč položky jméno.
     */
    public static final String NAME_KEY = "Name";

    /**
     * Klíč položky oddělovače.
     */
    public static final String DELIM_KEY = "SentenceDelim";

    /**
     * Jméno.
     */
    private final String name;

    /**
     * Oddělovač vět.
     */
    private final Pattern sentenceDelim;

    /**
     * Nahrazení tvarů podle pohlaví.
     */
    private final Map<Pattern, String> genderSubs;

    /**
     * Nahrazení 1. a 2. osoby.
     */
    private final Map<Pattern, String> personSubs;

    /**
     * Nahrazení 1. a 3. osoby.
     */
    private final Map<Pattern, String> person2Subs;

    /**
     * Nahrazení zkratek.
     */
    private final Map<Pattern, String> abbreviationsSubs;

    /**
     * Pravopisné náhrady.
     */
    private final Map<Pattern, String> spellingSubs;

    /**
     * Nahrazení emotikon.
     */
    private final Map<Pattern, String> emoticonsSubs;

    /**
     * Nahrazení vnitřní interpunkce.
     */
    private final Map<Pattern, String> innerPunctuationSubs;

    /**
     * Načte konfiguraci jazyka z {@link Properties}.
     * 
     * @param languageSettings
     *            nastavení jazyka
     * @param genderSubsProperties
     *            nastavení záměn rodových tvarů
     * @param personSubsProperties
     *            nastavení záměn 1. a 2. osoby
     * @param person2SubsProperties
     *            nastavení záměn 1. a 3. osoby
     * @param abbreviationsSubsProperties
     *            nastavení nahrazení zkratek
     * @param spellingSubsProperties
     *            nastavení nahrazení pravopisných chyb a kolokvialismů
     * @param emoticonsSubsProperties
     *            nastavení nahrazení emotikon
     * @param innerPunctuationSubsProperties
     *            nastavení nahrazení vnitřní interpunkce
     * @return načtená konfigurace
     * @throws ConfigurationException
     *             chyba ve formátu konfigurace
     */
    public static LanguageConfiguration create(
            final Properties languageSettings,
            final Properties genderSubsProperties,
            final Properties personSubsProperties,
            final Properties person2SubsProperties,
            final Properties abbreviationsSubsProperties,
            final Properties spellingSubsProperties,
            final Properties emoticonsSubsProperties,
            final Properties innerPunctuationSubsProperties)
            throws ConfigurationException {
        return create(toMap(languageSettings), toMap(genderSubsProperties),
                toMap(personSubsProperties), toMap(person2SubsProperties),
                toMap(abbreviationsSubsProperties),
                toMap(spellingSubsProperties), toMap(emoticonsSubsProperties),
                toMap(innerPunctuationSubsProperties));
    }

    /**
     * Načte konfiguraci jazyka z {@link Map}.
     * 
     * @param languageSettings
     *            nastavení jazyka
     * @param genderSubsProperties
     *            nastavení záměn rodových tvarů
     * @param personSubsProperties
     *            nastavení záměn 1. a 2. osoby
     * @param person2SubsProperties
     *            nastavení záměn 1. a 3. osoby
     * @param abbreviationsSubsProperties
     *            nastavení nahrazení zkratek
     * @param spellingSubsProperties
     *            nastavení nahrazení pravopisných chyb a kolokvialismů
     * @param emoticonsSubsProperties
     *            nastavení nahrazení emotikon
     * @param innerPunctuationSubsProperties
     *            nastavení nahrazení vnitřní interpunkce
     * @return načtená konfigurace
     * @throws ConfigurationException
     *             chyba ve formátu konfigurace
     */
    public static LanguageConfiguration create(
            final Map<String, String> languageSettings,
            final Map<String, String> genderSubsProperties,
            final Map<String, String> personSubsProperties,
            final Map<String, String> person2SubsProperties,
            final Map<String, String> abbreviationsSubsProperties,
            final Map<String, String> spellingSubsProperties,
            final Map<String, String> emoticonsSubsProperties,
            final Map<String, String> innerPunctuationSubsProperties)
            throws ConfigurationException {
        LOGGER.log(Level.INFO, "api.LanguageSettingsReading",
                new Object[] { languageSettings });

        final String name = Configuration.readValue(languageSettings, NAME_KEY);
        LOGGER.log(Level.INFO, "api.LanguageNameRead", new Object[] { name });

        final Pattern sentenceDelim =
                Configuration.readPattern(languageSettings, DELIM_KEY);
        LOGGER.log(Level.INFO, "api.LanguageDelimRead",
                new Object[] { sentenceDelim });

        final Map<Pattern, String> validGenderSubs =
                readReplacements(genderSubsProperties);
        LOGGER.log(Level.INFO, "api.LanguageGenderRead",
                new Object[] { validGenderSubs });

        final Map<Pattern, String> validPersonSubs =
                readReplacements(personSubsProperties);
        LOGGER.log(Level.INFO, "api.LanguagePersonRead",
                new Object[] { validPersonSubs });

        final Map<Pattern, String> validPerson2Subs =
                readReplacements(person2SubsProperties);
        LOGGER.log(Level.INFO, "api.LanguagePerson2Read",
                new Object[] { validPerson2Subs });

        final Map<Pattern, String> validAbbreviationsSubs =
                readReplacements(abbreviationsSubsProperties);
        LOGGER.log(Level.INFO, "api.LanguageAbbreviationsRead",
                new Object[] { validAbbreviationsSubs });

        final Map<Pattern, String> validSpellingSubs =
                readReplacements(spellingSubsProperties);
        LOGGER.log(Level.INFO, "api.LanguageSpellingRead",
                new Object[] { validSpellingSubs });

        final Map<Pattern, String> validEmoticonsSubs =
                readReplacements(emoticonsSubsProperties);
        LOGGER.log(Level.INFO, "api.LanguageEmoticonsRead",
                new Object[] { validEmoticonsSubs });

        final Map<Pattern, String> validInnerPunctuationSubs =
                readReplacements(innerPunctuationSubsProperties);
        LOGGER.log(Level.INFO, "api.LanguagePunctuationRead",
                new Object[] { validInnerPunctuationSubs });

        return new AIMLLanguageConfiguration(name, sentenceDelim,
                validGenderSubs, validPersonSubs, validPerson2Subs,
                validAbbreviationsSubs, validSpellingSubs, validEmoticonsSubs,
                validInnerPunctuationSubs);
    }

    /**
     * Vytvoří konfiguraci jazyka.
     * 
     * @param name
     *            jméno
     * @param sentenceDelim
     *            oddělovač vět
     * @param genderSubs
     *            nahrazení
     * @param personSubs
     *            nahrazení 1. a 2. osoby
     * @param person2Subs
     *            nahrazení 1. a 3. osoby
     * @param abbreviationsSubs
     *            nahrazení zkratek
     * @param spellingSubs
     *            oprava pravopisu
     * @param emoticonsSubs
     *            nahrazení emotikon
     * @param innerPunctuationSubs
     *            nahrazení vnitřní interpunkce
     * @return konfigurace
     */
    public static AIMLLanguageConfiguration of(final String name,
            final Pattern sentenceDelim, final Map<Pattern, String> genderSubs,
            final Map<Pattern, String> personSubs,
            final Map<Pattern, String> person2Subs,
            final Map<Pattern, String> abbreviationsSubs,
            final Map<Pattern, String> spellingSubs,
            final Map<Pattern, String> emoticonsSubs,
            final Map<Pattern, String> innerPunctuationSubs) {
        if (name == null || genderSubs == null || personSubs == null
                || person2Subs == null || abbreviationsSubs == null
                || spellingSubs == null || emoticonsSubs == null
                || innerPunctuationSubs == null) {
            throw new NullPointerException(
                    MESSAGE_LOCALIZER.getMessage("api.NullArgument"));
        }

        if (name.isEmpty()) {
            throw new IllegalArgumentException(
                    MESSAGE_LOCALIZER.getMessage("api.EmptyNameArgument"));
        }
        checkSubs(genderSubs);
        checkSubs(personSubs);
        checkSubs(person2Subs);
        checkSubs(abbreviationsSubs);
        checkSubs(spellingSubs);
        checkSubs(emoticonsSubs);
        checkSubs(innerPunctuationSubs);

        return new AIMLLanguageConfiguration(name, sentenceDelim, genderSubs,
                personSubs, person2Subs, abbreviationsSubs, spellingSubs,
                emoticonsSubs, innerPunctuationSubs);
    }

    /**
     * Ověří vstup substitucí.
     * 
     * @param subs
     *            substituce
     */
    private static void checkSubs(final Map<Pattern, String> subs) {
        for (final Entry<Pattern, String> substitution : subs.entrySet()) {
            final Pattern key = substitution.getKey();
            final String value = substitution.getValue();

            if (key == null || value == null) {
                throw new NullPointerException(
                        MESSAGE_LOCALIZER
                                .getMessage("api.NullSubstitutionEntry"));
            }

            if (value.isEmpty()) {
                throw new IllegalArgumentException(
                        MESSAGE_LOCALIZER
                                .getMessage("api.EmptySubstitutionValue"));
            }
        }
    }

    /**
     * Vytvoří konfiguraci jazyka.
     * 
     * @param name
     *            jméno
     * @param sentenceDelim
     *            oddělovač vět
     * @param genderSubs
     *            nahrazení
     * @param personSubs
     *            nahrazení 1. a 2. osoby
     * @param person2Subs
     *            nahrazení 1. a 3. osoby
     * @param abbreviationsSubs
     *            nahrazení zkratek
     * @param spellingSubs
     *            oprava pravopisu
     * @param emoticonsSubs
     *            nahrazení emotikon
     * @param innerPunctuationSubs
     *            nahrazení vnitřní interpunkce
     */
    private AIMLLanguageConfiguration(final String name,
            final Pattern sentenceDelim, final Map<Pattern, String> genderSubs,
            final Map<Pattern, String> personSubs,
            final Map<Pattern, String> person2Subs,
            final Map<Pattern, String> abbreviationsSubs,
            final Map<Pattern, String> spellingSubs,
            final Map<Pattern, String> emoticonsSubs,
            final Map<Pattern, String> innerPunctuationSubs) {
        this.name = name;
        this.sentenceDelim = sentenceDelim;
        this.genderSubs = new HashMap<>(genderSubs);
        this.personSubs = new HashMap<>(personSubs);
        this.person2Subs = new HashMap<>(person2Subs);
        this.abbreviationsSubs = new HashMap<>(abbreviationsSubs);
        this.spellingSubs = new HashMap<>(spellingSubs);
        this.emoticonsSubs = new HashMap<>(emoticonsSubs);
        this.innerPunctuationSubs = new HashMap<>(innerPunctuationSubs);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.library.api.LanguageConfiguration#getName
     * ()
     */
    @Override
    public String getName() {
        return name;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.api.LanguageConfiguration#
     * getSentenceDelim()
     */
    @Override
    public Pattern getSentenceDelim() {
        return sentenceDelim;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.api.LanguageConfiguration#
     * getGenderSubs()
     */
    @Override
    public Map<Pattern, String> getGenderSubs() {
        return Collections.unmodifiableMap(genderSubs);
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.api.LanguageConfiguration#
     * getPersonSubs()
     */
    @Override
    public Map<Pattern, String> getPersonSubs() {
        return Collections.unmodifiableMap(personSubs);
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.api.LanguageConfiguration#
     * getPerson2Subs()
     */
    @Override
    public Map<Pattern, String> getPerson2Subs() {
        return Collections.unmodifiableMap(person2Subs);
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.api.LanguageConfiguration#
     * getAbbreviationsSubs()
     */
    @Override
    public Map<Pattern, String> getAbbreviationsSubs() {
        return Collections.unmodifiableMap(abbreviationsSubs);
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.api.LanguageConfiguration#
     * getSpellingSubs()
     */
    @Override
    public Map<Pattern, String> getSpellingSubs() {
        return Collections.unmodifiableMap(spellingSubs);
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.api.LanguageConfiguration#
     * getEmoticonsSubstitution()
     */
    @Override
    public Map<Pattern, String> getEmoticonsSubs() {
        return Collections.unmodifiableMap(emoticonsSubs);
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.api.LanguageConfiguration#
     * getInnerPunctuationSubs()
     */
    @Override
    public Map<Pattern, String> getInnerPunctuationSubs() {
        return Collections.unmodifiableMap(innerPunctuationSubs);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        final int maxLen = 10;
        final StringBuilder builder = new StringBuilder();
        builder.append("AIMLLanguageConfiguration [name=");
        builder.append(name);
        builder.append(", sentenceDelim=");
        builder.append(sentenceDelim);
        builder.append(", genderSubs=");
        builder.append(Text.toString(genderSubs.entrySet(), maxLen));
        builder.append(", personSubs=");
        builder.append(Text.toString(personSubs.entrySet(), maxLen));
        builder.append(", person2Subs=");
        builder.append(Text.toString(person2Subs.entrySet(), maxLen));
        builder.append(", abbreviationsSubs=");
        builder.append(Text.toString(abbreviationsSubs.entrySet(), maxLen));
        builder.append(", spellingSubs=");
        builder.append(Text.toString(spellingSubs.entrySet(), maxLen));
        builder.append(", emoticonsSubs=");
        builder.append(Text.toString(emoticonsSubs.entrySet(), maxLen));
        builder.append(", innerPunctuationSubs=");
        builder.append(Text.toString(innerPunctuationSubs.entrySet(), maxLen));
        builder.append("]");
        return builder.toString();
    }
}
