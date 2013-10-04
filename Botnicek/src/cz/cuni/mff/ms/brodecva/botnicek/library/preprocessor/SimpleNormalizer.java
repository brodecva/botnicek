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
import java.util.logging.Level;
import java.util.logging.Logger;

import cz.cuni.mff.ms.brodecva.botnicek.library.logging.BotnicekLogger;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.ExceptionMessageLocalizer;

/**
 * Jednoduchá implementace normalizace.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class SimpleNormalizer implements Normalizer, Serializable {

    /**
     * UID serializované verze.
     */
    private static final long serialVersionUID = -3057006446395457758L;

    /**
     * Logger.
     */
    private static final Logger LOGGER = BotnicekLogger
            .getLogger(SimpleNormalizer.class);
    
    /**
     * Lokalizátor hlášek výjimek.
     */
    private static final ExceptionMessageLocalizer MESSAGE_LOCALIZER =
            ExceptionMessageLocalizer.getLocalizer();

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.library.preprocessor.Normalizer#isNormal
     * (char)
     */
    @Override
    public boolean isNormal(final char character) {
        return Character.isDefined(character)
                && (Character.isUpperCase(character) || Character
                        .isDigit(character));
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.preprocessor.Normalizer#
     * convertToNormalChars(java.lang.String)
     */
    @Override
    public String convertToNormalChars(final String text) {
        final char[] charredText = text.toCharArray();
        final char[] convertedText = new char[charredText.length];

        for (int i = 0; i < charredText.length; i++) {
            final char character = charredText[i];

            if (!isNormal(character)) {
                if (Character.isLowerCase(character)) {
                    convertedText[i] = Character.toUpperCase(character);
                } else {
                    convertedText[i] = SPACE;
                }
            } else {
                convertedText[i] = character;
            }
        }

        final String cleanedUp =
                new String(convertedText)
                        .replaceAll("\\s+", String.valueOf(SPACE)).trim();
        
        LOGGER.log(Level.INFO, "preprocessor.NormalizerToNormal", new Object[] { text, cleanedUp });
        
        return cleanedUp;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.preprocessor.Normalizer#
     * deconvertFromNormalChars(java.lang.String)
     */
    @Override
    public String deconvertFromNormalChars(final String normalizedText) {
        final char[] charredText = normalizedText.toCharArray();
        final char[] convertedText = new char[charredText.length];

        for (int i = 0; i < charredText.length; i++) {
            final char character = charredText[i];

            if (!isNormal(character) && !Character.isWhitespace(character)) {
                throw new IllegalArgumentException(
                        MESSAGE_LOCALIZER.getMessage("preprocessor.InputNotNormalized",
                                normalizedText, i));
            }

            convertedText[i] = Character.toLowerCase(character);
        }
        
        LOGGER.log(Level.INFO, "preprocessor.NormalizerFromNormal", new Object[] { normalizedText, convertedText });

        return new String(convertedText);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result;
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
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "SimpleNormalizer";
    }
}
