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
 * Vytvoří slovo reprezentované nejpříhodnější implementací na základě textové
 * hodnoty.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see AIMLWord
 * @see AIMLPartMarker
 * @see AIMLWildcard
 */
public final class AIMLWordFactory implements WordFactory, Serializable {

    /**
     * UID serializované verze.
     */
    private static final long serialVersionUID = 7514950004357222026L;

    /**
     * Lokalizátor hlášek výjimek.
     */
    private static final ExceptionMessageLocalizer MESSAGE_LOCALIZER =
            ExceptionMessageLocalizer.getLocalizer();

    /**
     * Normalizér. Převádí hodnotu na normální slovo.
     */
    private final Normalizer normalizer;

    /**
     * Vytvoří továrnu na slova.
     * 
     * @param normalizer
     *            normalizér pro věření formátu slova
     */
    public AIMLWordFactory(final Normalizer normalizer) {
        if (normalizer == null) {
            throw new NullPointerException(
                    MESSAGE_LOCALIZER.getMessage("storage.NullArgument"));
        }

        this.normalizer = normalizer;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.library.storage.WordFactory#create(java
     * .lang.String)
     */
    @Override
    public Word create(final String value) {
        for (final AIMLWildcard wildcard : AIMLWildcard.values()) {
            if (value.equals(wildcard.getValue())) {
                return wildcard;
            }
        }

        for (final AIMLPartMarker marker : AIMLPartMarker.values()) {
            if (value.equals(marker.getValue())) {
                return marker;
            }
        }

        return new AIMLWord(value, normalizer);
    }

}
