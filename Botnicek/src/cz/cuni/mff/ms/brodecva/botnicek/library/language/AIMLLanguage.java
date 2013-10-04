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
package cz.cuni.mff.ms.brodecva.botnicek.library.language;

import static cz.cuni.mff.ms.brodecva.botnicek.library.utils.Text.replace;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import cz.cuni.mff.ms.brodecva.botnicek.library.logging.BotnicekLogger;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.ExceptionMessageLocalizer;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.Text;

/**
 * Definice jazyka pro potřeby zpracování AIML.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class AIMLLanguage implements Language, Serializable {

    /**
     * UID serializované verze.
     */
    private static final long serialVersionUID = 3780747340698692921L;
    
    /**
     * Lokalizátor hlášek výjimek.
     */
    private static final ExceptionMessageLocalizer MESSAGE_LOCALIZER =
            ExceptionMessageLocalizer.getLocalizer();
    
    /**
     * Logger.
     */
    private static final Logger LOGGER = BotnicekLogger
            .getLogger(AIMLLanguage.class);

    /**
     * Kritérium dělení na věty.
     */
    private final Pattern sentenceDelimiter;

    /**
     * Substituce pohlaví.
     */
    private final Map<Pattern, String> genderSubs;

    /**
     * Substituce 1. a 2. osoby.
     */
    private final Map<Pattern, String> personSubs;

    /**
     * Substituce 1. a 3. osoby.
     */
    private final Map<Pattern, String> person2Subs;

    /**
     * Substituce zkratek.
     */
    private final Map<Pattern, String> abbreviationsSubs;

    /**
     * Opravy pravopisu a hovorových výrazů.
     */
    private final Map<Pattern, String> spellingSubstitutons;

    /**
     * Nahrazení emotikon.
     */
    private final Map<Pattern, String> emoticonsSubs;

    /**
     * Nahrazení znaků interpunkce mimo hranice vět.
     */
    private Map<Pattern, String> innerPunctuationSubs;

    /**
     * Název jazyka.
     */
    private final String name;

    /**
     * Cachovaný {@link #hashCode()}.
     */
    private transient Integer cachedHash = null;

    /**
     * Vytvoří definici jazyka pro účely zpracování AIML. Nepřijímá hodnoty
     * {@code null}.
     * 
     * @param name
     *            jméno
     * @param sentenceDelimiter
     *            kritérium dělení na věty
     * @param genderSubs
     *            substituce pohlaví
     * @param personSubs
     *            substituce 1. a 2. osoby
     * @param person2Subs
     *            substituce 1. a 3. osoby
     * @param abbreviationsSubs
     *            substituce zkratek
     * @param spellingSubs
     *            substituce chyb v pravopisu a kolokvialismů
     * @param emoticonsSubs
     *            substituce emotikon
     * @param innerPunctuationSubs
     *            substituce interpunkčních znaků uvnitř vět
     */
    public AIMLLanguage(final String name, final Pattern sentenceDelimiter,
            final Map<Pattern, String> genderSubs,
            final Map<Pattern, String> personSubs,
            final Map<Pattern, String> person2Subs,
            final Map<Pattern, String> abbreviationsSubs,
            final Map<Pattern, String> spellingSubs,
            final Map<Pattern, String> emoticonsSubs,
            final Map<Pattern, String> innerPunctuationSubs) {
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, "language.LanguageCreating", new Object[] { name, sentenceDelimiter, genderSubs, personSubs, person2Subs, abbreviationsSubs, spellingSubs, emoticonsSubs, innerPunctuationSubs });
        }
        
        if (name == null) {
            throw new NullPointerException(
                    MESSAGE_LOCALIZER.getMessage("language.NameNull"));
        }

        if (sentenceDelimiter == null) {
            throw new NullPointerException(
                    MESSAGE_LOCALIZER.getMessage("language.SentenceDelimiterNull"));
        }

        if (genderSubs == null) {
            throw new NullPointerException(
                    MESSAGE_LOCALIZER.getMessage("language.GenderNull"));
        }

        if (personSubs == null) {
            throw new NullPointerException(
                    MESSAGE_LOCALIZER.getMessage("language.PersonNull"));
        }

        if (person2Subs == null) {
            throw new NullPointerException(
                    MESSAGE_LOCALIZER.getMessage("language.Person2Null"));
        }

        if (abbreviationsSubs == null) {
            throw new NullPointerException(
                    MESSAGE_LOCALIZER.getMessage("language.AbbreviationsNull"));
        }

        if (spellingSubs == null) {
            throw new NullPointerException(
                    MESSAGE_LOCALIZER.getMessage("language.SpellingNull"));
        }

        if (emoticonsSubs == null) {
            throw new NullPointerException(
                    MESSAGE_LOCALIZER.getMessage("language.EmoticonsNull"));
        }

        if (innerPunctuationSubs == null) {
            throw new NullPointerException(
                    MESSAGE_LOCALIZER.getMessage("language.PunctuationNull"));
        }

        this.name = name;
        this.sentenceDelimiter = sentenceDelimiter;
        this.genderSubs = new HashMap<Pattern, String>(genderSubs);
        this.personSubs = new HashMap<Pattern, String>(personSubs);
        this.person2Subs = new HashMap<Pattern, String>(person2Subs);
        this.abbreviationsSubs =
                new HashMap<Pattern, String>(abbreviationsSubs);
        this.spellingSubstitutons = new HashMap<Pattern, String>(spellingSubs);
        this.emoticonsSubs = new HashMap<Pattern, String>(emoticonsSubs);
        this.innerPunctuationSubs =
                new HashMap<Pattern, String>(innerPunctuationSubs);
        
        LOGGER.log(Level.INFO, "language.LanguageCreated", name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.language.Language#
     * getSentenceDelimiter()
     */
    @Override
    public Pattern getSentenceDelim() {
        return sentenceDelimiter;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.language.Language#getName()
     */
    @Override
    public String getName() {
        return name;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.library.language.Language#swapGender
     * (java.lang.String)
     */
    @Override
    public String swapGender(final String text) {
        LOGGER.log(Level.INFO, "language.LanguageGenderSwap", text);
        return replace(text, genderSubs);
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.language.Language#
     * transformPerson (java.lang.String)
     */
    @Override
    public String transformPerson(final String text) {
        LOGGER.log(Level.INFO, "language.LanguagePersonTransform", text);
        return replace(text, Collections.unmodifiableMap(personSubs));
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.language.Language#
     * transformPerson2 (java.lang.String)
     */
    @Override
    public String transformPerson2(final String text) {
        LOGGER.log(Level.INFO, "language.LanguagePerson2Transform", text);
        return Text.replace(text, Collections.unmodifiableMap(person2Subs));
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.language.
     * Language#expandAbbreviations (java.lang.String)
     */
    @Override
    public String expandAbbreviations(final String text) {
        LOGGER.log(Level.INFO, "language.LanguageAbbreviationsExpand", text);
        return replace(text, Collections.unmodifiableMap(abbreviationsSubs));
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.language.Language#
     * correctSpellingAndColloquialisms(java.lang.String)
     */
    @Override
    public String correctSpellingAndColloquialisms(final String text) {
        LOGGER.log(Level.INFO, "language.LanguageSpellingCorrection", text);
        return replace(text, Collections.unmodifiableMap(spellingSubstitutons));
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.language.Language#
     * substituteEmoticons(java.lang.String)
     */
    @Override
    public String substituteEmoticons(final String text) {
        LOGGER.log(Level.INFO, "language.LanguageEmoticonsSubstitution", text);
        return replace(text, Collections.unmodifiableMap(emoticonsSubs));
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.language.Language#
     * removeInnerSentencePunctuation(java.lang.String)
     */
    @Override
    public String removeInnerSentencePunctuation(final String text) {
        LOGGER.log(Level.INFO, "language.LanguagePunctuationRemoval", text);
        return replace(text, Collections.unmodifiableMap(innerPunctuationSubs));
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        if (cachedHash != null) {
            return cachedHash;
        }

        final int prime = 31;
        int result = 1;
        result = prime * result + name.hashCode();
        result = prime * result + genderSubs.hashCode();
        result = prime * result + person2Subs.hashCode();
        result = prime * result + personSubs.hashCode();
        result = prime * result + sentenceDelimiter.hashCode();
        result = prime * result + abbreviationsSubs.hashCode();
        result = prime * result + spellingSubstitutons.hashCode();
        result = prime * result + emoticonsSubs.hashCode();
        result = prime * result + innerPunctuationSubs.hashCode();

        cachedHash = result;

        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AIMLLanguage other = (AIMLLanguage) obj;
        if (!name.equals(other.name)) {
            return false;
        }
        if (!sentenceDelimiter.equals(other.sentenceDelimiter)) {
            return false;
        }
        if (!genderSubs.equals(other.genderSubs)) {
            return false;
        }
        if (!person2Subs.equals(other.person2Subs)) {
            return false;
        }
        if (!personSubs.equals(other.personSubs)) {
            return false;
        }
        if (!abbreviationsSubs.equals(other.abbreviationsSubs)) {
            return false;
        }
        if (!emoticonsSubs.equals(other.emoticonsSubs)) {
            return false;
        }
        if (!spellingSubstitutons.equals(other.spellingSubstitutons)) {
            return false;
        }
        if (!innerPunctuationSubs.equals(other.innerPunctuationSubs)) {
            return false;
        }
        return true;
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
        builder.append("AIMLLanguage [sentenceDelimiter=");
        builder.append(sentenceDelimiter);
        builder.append(", genderSubs=");
        builder.append(Text.toString(genderSubs.entrySet(), maxLen));
        builder.append(", personSubs=");
        builder.append(Text.toString(personSubs.entrySet(), maxLen));
        builder.append(", person2Subs=");
        builder.append(Text.toString(person2Subs.entrySet(), maxLen));
        builder.append(", abbreviationsSubs=");
        builder.append(Text.toString(abbreviationsSubs.entrySet(), maxLen));
        builder.append(", spellingSubstitutons=");
        builder.append(Text.toString(spellingSubstitutons.entrySet(), maxLen));
        builder.append(", emoticonsSubs=");
        builder.append(Text.toString(emoticonsSubs.entrySet(), maxLen));
        builder.append(", innerPunctuationSubs=");
        builder.append(Text.toString(innerPunctuationSubs.entrySet(), maxLen));
        builder.append(", name=");
        builder.append(name);
        builder.append("]");
        return builder.toString();
    }
}
