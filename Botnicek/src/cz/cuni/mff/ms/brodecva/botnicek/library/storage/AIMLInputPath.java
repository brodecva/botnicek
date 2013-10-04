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
package cz.cuni.mff.ms.brodecva.botnicek.library.storage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import cz.cuni.mff.ms.brodecva.botnicek.library.logging.BotnicekLogger;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.AIML;
import cz.cuni.mff.ms.brodecva.botnicek.library.preprocessor.SimpleNormalizer;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.ExceptionMessageLocalizer;

/**
 * Vstupní cesta - sekvence slov obsahující normalizovaný dotaz.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class AIMLInputPath implements InputPath, Serializable {

    /**
     * UID serializované verze.
     */
    private static final long serialVersionUID = -670958083181586400L;
    
    
    /**
     * Logger.
     */
    private static final Logger LOGGER = BotnicekLogger
            .getLogger(AIMLInputPath.class);
    
    /**
     * Lokalizátor hlášek výjimek.
     */
    private static final ExceptionMessageLocalizer MESSAGE_LOCALIZER =
            ExceptionMessageLocalizer.getLocalizer();

    /**
     * Obousměrný seznam slov v zachovaném pořadí.
     */
    private final List<Word> words;

    /**
     * Konstruktor vstupní cesty ze seznamu slov.
     * 
     * @param queryWordList
     *            seznam slov dotazu, nesmí být null
     */
    public AIMLInputPath(final List<Word> queryWordList) {
        for (Word addedWord : queryWordList) {
            if (addedWord == null) {
                throw new NullPointerException(MESSAGE_LOCALIZER.getMessage(
                        "storage.NullWord", queryWordList));
            }
        }
        words = new ArrayList<Word>(queryWordList);
        if (LOGGER.isLoggable(Level.FINER)) {
            LOGGER.log(Level.FINER, "storage.AIMLInputPathCreated", words);
        }
    }

    /**
     * Konstruktor vstupní cesty z jejích částí s respektováním pravidel pro
     * slova jazyka AIML.
     * 
     * @param pattern
     *            vzor
     * @param that
     *            předchozí promluva robota
     * @param topic
     *            téma
     */
    public AIMLInputPath(final String pattern, final String that,
            final String topic) {
        this(pattern, that, topic, new AIMLWordFactory(new SimpleNormalizer()));
    }

    /**
     * Konstruktor vstupní cesty z jejích částí.
     * 
     * @param pattern
     *            vzor
     * @param that
     *            předchozí promluva robota
     * @param topic
     *            téma
     * @param wordFactory
     *            továrna interpretující tokeny slov
     */
    AIMLInputPath(final String pattern, final String that,
            final String topic, final WordFactory wordFactory) {
        final String spaceRegex = "\\s+";
        final int partsCount = 3;

        final String[] parts = new String[partsCount];

        if (pattern == null) {
            parts[0] = AIML.STAR_WILDCARD.getValue();
        } else {
            parts[0] = pattern;
        }

        if (that == null) {
            parts[1] = AIML.STAR_WILDCARD.getValue();
        } else {
            parts[1] = that;
        }

        if (topic == null) {
            parts[2] = AIML.STAR_WILDCARD.getValue();
        } else {
            parts[2] = topic;
        }

        final String[][] splittedParts = new String[partsCount][];
        int combinedPartsLength = 0;
        for (int i = 0; i < partsCount; i++) {
            splittedParts[i] = parts[i].split(spaceRegex);
            combinedPartsLength += splittedParts[i].length;
        }

        final List<Word> words =
                new ArrayList<Word>(combinedPartsLength + partsCount - 1);

        for (int i = 0; i < splittedParts[0].length; i++) {
            words.add(wordFactory.create(splittedParts[0][i]));
        }

        words.add(AIMLPartMarker.THAT);

        for (int i = 0; i < splittedParts[1].length; i++) {
            words.add(wordFactory.create(splittedParts[1][i]));
        }

        words.add(AIMLPartMarker.TOPIC);

        for (int i = 0; i < splittedParts[2].length; i++) {
            words.add(wordFactory.create(splittedParts[2][i]));
        }

        this.words = words;
        if (LOGGER.isLoggable(Level.FINER)) {
            LOGGER.log(Level.FINER, "storage.AIMLInputPathCreated", words);
        }
    }

    /**
     * Vytvoří úsek původní cesty na jeho základě.
     * 
     * @param original
     *            původní cesta, nesmí být null
     * @param from
     *            platný počáteční index (včetně)
     * @param to
     *            platný koncový index (mimo)
     */
    public AIMLInputPath(final AIMLInputPath original, final int from,
            final int to) {
        words = original.words.subList(from, to); // Neměnné.
        if (LOGGER.isLoggable(Level.FINER)) {
            LOGGER.log(Level.FINER, "storage.AIMLInputPathCreated", words);
        }
    }

    /**
     * Konstruktor prázdné vstupní cesty.
     */
    public AIMLInputPath() {
        words = new ArrayList<Word>();
        if (LOGGER.isLoggable(Level.FINER)) {
            LOGGER.log(Level.FINER, "storage.AIMLInputPathCreated", words);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.library.storage.InputPath#listIterator()
     */
    @Override
    public ListIterator<Word> listIterator() {
        return (Collections.unmodifiableList(words)).listIterator();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.library.storage.InputPath#iterator()
     */
    @Override
    public Iterator<Word> iterator() {
        return listIterator();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.library.storage.InputPath#getSuffix(
     * java.util.ListIterator)
     */
    @Override
    public InputPath getSuffix(final ListIterator<Word> iterator) {
        return new AIMLInputPath(this, iterator.nextIndex(), words.size());
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.storage.InputPath#
     * getRemainingLength (java.util.ListIterator)
     */
    @Override
    public int getRemainingLength(final ListIterator<Word> pathIterator) {
        return words.size() - pathIterator.nextIndex();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.library.storage.InputPath#getLength()
     */
    @Override
    public int getLength() {
        return words.size();
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.storage.InputPath#isEmpty()
     */
    @Override
    public boolean isEmpty() {
        return words.isEmpty();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.library.storage.InputPath#subPath(int,
     * int)
     */
    @Override
    public InputPath subPath(final int fromIndex, final int toIndex) {
        return new AIMLInputPath(this, fromIndex, toIndex);
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.storage.InputPath#head()
     */
    @Override
    public Word head() {
        if (words.isEmpty()) {
            return null;
        }

        return words.get(0);
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.storage.InputPath#tail()
     */
    @Override
    public InputPath tail() {
        if (words.isEmpty()) {
            return null;
        }

        return subPath(1, words.size());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.library.storage.InputPath#toString()
     */
    @Override
    public String toString() {
        if (words.size() == 0) {
            return EMPTY_STRING;
        }

        final StringBuilder buffer = new StringBuilder();
        for (final Word word : this) {
            buffer.append(WORD_DELIMITER + word.getValue());
        }

        return buffer.toString().substring(1);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.library.storage.InputPath#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + words.hashCode();
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.library.storage.InputPath#equals(java
     * .lang.Object)
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
        final AIMLInputPath other = (AIMLInputPath) obj;
        if (!words.equals(other.words)) {
            return false;
        }
        return true;
    }
}
