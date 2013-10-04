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

import cz.cuni.mff.ms.brodecva.botnicek.library.preprocessor.Normalizer;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.ExceptionMessageLocalizer;

/**
 * Slovo normalizované vstupní sekvence.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class AIMLWord implements Word, Serializable {

    /**
     * UID serializované verze.
     */
    private static final long serialVersionUID = -8668119977234047341L;

    /**
     * Lokalizátor hlášek výjimek.
     */
    private static final ExceptionMessageLocalizer MESSAGE_LOCALIZER =
            ExceptionMessageLocalizer.getLocalizer();

    /**
     * Textová hodnota.
     */
    private final String value;

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.storage.Word#getValue()
     */
    @Override
    public String getValue() {
        return value;
    }

    /**
     * Vytvoří slovo z řetězce (nesmí být null).
     * 
     * @param value
     *            řetězec obsahující vlastní slovo
     * @param normalizer
     *            normalizér kontrolující normalitu hodnoty
     */
    public AIMLWord(final String value, final Normalizer normalizer) {
        if (value == null) {
            throw new NullPointerException("WordStringNull");
        }

        for (char c : value.toCharArray()) {
            if (!normalizer.isNormal(c) || c == Normalizer.SPACE) {
                throw new IllegalArgumentException(
                        MESSAGE_LOCALIZER.getMessage("storage.InvalidChar", c, value));
            }
        }

        this.value = value;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.storage.Word#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + value.toUpperCase().hashCode();
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.library.storage.Word#equals(java.lang
     * .Object)
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
        final AIMLWord other = (AIMLWord) obj;
        if (!value.equals(other.value)) {
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
        return value;
    }
}
