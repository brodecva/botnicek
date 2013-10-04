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
package cz.cuni.mff.ms.brodecva.botnicek.library.preprocessor;

import java.io.Serializable;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import cz.cuni.mff.ms.brodecva.botnicek.library.language.Language;
import cz.cuni.mff.ms.brodecva.botnicek.library.logging.BotnicekLogger;

/**
 * Třída pro dělení textu na zpracovatelné úseky pomocí. Využívá jednoduché
 * postupy (regulární výraz,...).
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class SimpleSplitter implements Splitter, Serializable {

    /**
     * UID serializované verze.
     */
    private static final long serialVersionUID = 3801460004722577643L;
    
    /**
     * Logger.
     */
    private static final Logger LOGGER = BotnicekLogger
            .getLogger(SimpleSplitter.class);
    
    /**
     * Definice jazyka.
     */
    private final Language language;

    /**
     * Vytvoří objekt pro dělení textu na zpracovatelné úseky.
     * 
     * @param language
     *            jazyk, dle něhož se rozhoduje
     */
    public SimpleSplitter(final Language language) {
        this.language = language;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.preprocessor.Splitter#
     * splitToSentences(java.lang.String)
     */
    @Override
    public String[] splitToSentences(final String text) {
        final Pattern delimiter = language.getSentenceDelim();
        
        final String[] result = delimiter.split(text);
        
        LOGGER.log(Level.INFO, "preprocessor.SplitterSplit", new Object[] { text, Arrays.toString(result), delimiter });
        
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result =
                prime * result + ((language == null) ? 0 : language.hashCode());
        return result;
    }

    /* (non-Javadoc)
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
        SimpleSplitter other = (SimpleSplitter) obj;
        if (language == null) {
            if (other.language != null) {
                return false;
            }
        } else if (!language.equals(other.language)) {
            return false;
        }
        return true;
    }
}
