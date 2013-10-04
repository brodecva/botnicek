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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Značka příslušící částí vstupní cesty.
 * 
 * @author Václav Brodec
 * @version 1.0
 * 
 */
public enum AIMLPartMarker implements PartMarker {
    /**
     * Vzor.
     */
    PATTERN(null, 1),

    /**
     * That část (odkaz na předchozí promluvu robota).
     */
    THAT("<that>", 2),

    /**
     * Téma.
     */
    TOPIC("<topic>", 3),

    /**
     * Šablona.
     */
    TEMPLATE(null, 4),

    /**
     * Načtení dodatečného souboru.
     */
    LEARN(null, 5),

    /**
     * Nedefinovaná část.
     */
    UNDEFINED(null, 6);

    /**
     * Textová hodnota.
     */
    private final String value;

    /**
     * Pořadí ve vstupní cestě.
     */
    private final int order;

    /**
     * Cache pro množinu všech hodnot výčtu.
     */
    private static Set<PartMarker> allValuesCache = null;

    /**
     * Konstruktor značky části cesty.
     * 
     * @param value
     *            slovní reprezentace značky
     * @param order
     *            pořadí
     */
    private AIMLPartMarker(final String value, final int order) {
        this.value = value;
        this.order = order;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return getValue();
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.storage.Word#getValue()
     */
    @Override
    public String getValue() {
        return value;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.library.storage.PartMarker#getOrder()
     */
    @Override
    public int getOrder() {
        return order;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.library.storage.PartMarker#allValues()
     */
    @Override
    public Set<PartMarker> allValues() {
        if (allValuesCache == null) {
            allValuesCache = new HashSet<PartMarker>(Arrays.asList(values()));
        }

        return allValuesCache;
    }
}
