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
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import cz.cuni.mff.ms.brodecva.botnicek.library.logging.BotnicekLogger;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.ExceptionMessageLocalizer;

/**
 * Reprezentuje úspěšný výsledek hledání.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class SuccesfulResult implements MatchResult, Serializable {
    /**
     * UID serializované verze.
     */
    private static final long serialVersionUID = 410238872537992333L;
    
    /**
     * Logger.
     */
    private static final Logger LOGGER = BotnicekLogger
            .getLogger(SuccesfulResult.class);
    
    /**
     * Lokalizátor hlášek výjimek.
     */
    private static final ExceptionMessageLocalizer MESSAGE_LOCALIZER =
            ExceptionMessageLocalizer.getLocalizer();

    /**
     * Prázdná zachycená část.
     */
    private static final String EMPTY_MATCHED_PART = "";

    /**
     * Mezera.
     */
    private static final String SPACE = " ";

    /**
     * Nalezená šablona reakce.
     */
    private final Template template;

    /**
     * Žolíky nahrazené části pro každou část vstupní cesty.
     */
    private final Map<PartMarker, Deque<InputPath>> wildcardMatchedParts =
            new HashMap<PartMarker, Deque<InputPath>>(3);

    /**
     * Konstruktor výsledku hledání.
     * 
     * @param template
     *            nalezená šablona s reakcí, nesmí být null
     */
    public SuccesfulResult(final Template template) {
        if (LOGGER.isLoggable(Level.FINER)) {
            LOGGER.log(Level.FINER, "storage.SuccesfulResultCreating", template);
        }
        
        if (template == null) {
            throw new NullPointerException(
                    MESSAGE_LOCALIZER.getMessage("storage.TemplateNull"));
        }

        this.template = template;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.library.storage.MatchResult#isSuccesful
     * ()
     */
    @Override
    public boolean isSuccesful() {
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.library.storage.MatchResult#getTemplate
     * ()
     */
    @Override
    public Template getTemplate() {
        return template;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.storage.MatchResult#
     * getStarMatchedParts
     * (cz.cuni.mff.ms.brodecva.botnicek.library.storage.PartMarker)
     */
    @Override
    public List<String> getStarMatchedParts(final PartMarker part) {
        final List<String> result = new LinkedList<String>();

        final StringBuilder matchedStringBuilder = new StringBuilder();
        final Deque<InputPath> matchedInPart = wildcardMatchedParts.get(part);
        
        if (matchedInPart == null) {
            return result;
        }
        
        for (final InputPath matchedPart : matchedInPart) {
            if (matchedPart.getLength() == 0) {
                result.add(EMPTY_MATCHED_PART);
                continue;
            }

            for (final Word matchedWord : matchedPart) {
                final String wordValue = matchedWord.getValue();

                matchedStringBuilder.append(SPACE + wordValue);
            }

            result.add(matchedStringBuilder.toString().substring(1));

            matchedStringBuilder.setLength(0); // Vymaže builder.
        }
        
        if (LOGGER.isLoggable(Level.FINER)) {
            LOGGER.log(Level.FINER, "storage.StarMatchedPartsResult", new Object[] { this, result, part });
        }
        
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.storage.MatchResult#
     * addStarMatchedPart
     * (cz.cuni.mff.ms.brodecva.botnicek.library.storage.PartMarker,
     * cz.cuni.mff.ms.brodecva.botnicek.library.storage.InputPath)
     */
    @Override
    public void addStarMatchedPart(final PartMarker pathPart,
            final InputPath matchedPart) {
        if (pathPart == null) {
            throw new NullPointerException(
                    MESSAGE_LOCALIZER.getMessage("storage.PathPartNull"));
        }

        if (matchedPart == null) {
            throw new NullPointerException(
                    MESSAGE_LOCALIZER.getMessage("storage.MatchedPartNull"));
        }

        if (wildcardMatchedParts.get(pathPart) == null) {
            wildcardMatchedParts.put(pathPart, new ArrayDeque<InputPath>());
        }

        wildcardMatchedParts.get(pathPart).push(matchedPart);
        
        if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.log(Level.FINEST, "storage.StarMatchedPartAdd", new Object[] { this, matchedPart, pathPart });
        }
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "SuccesfulResult [template=" + template
                + ", wildcardMatchedParts=" + wildcardMatchedParts + "]";
    }
}
