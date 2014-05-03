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
package cz.cuni.mff.ms.brodecva.botnicek.library.parser;

import java.io.Serializable;

import org.w3c.dom.Element;

import cz.cuni.mff.ms.brodecva.botnicek.library.platform.AIML;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.AIML2DIndex;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.AIMLIndex;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.Index;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.TwoDimensionalIndex;

/**
 * Výchozí implementace indexové tovární třídy.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class DefaultIndexFactory implements IndexFactory, Serializable {
    /**
     * UID serializované verze.
     */
    private static final long serialVersionUID = -1764372632713653455L;

    /**
     * Vrátí číselnou hodnotu atributu index. Číslo 1 je výchozí hodnota v
     * případě chyby.
     * 
     * @param element
     *            prvek s atributem index obsahujícím číslo
     * @return číslo načteného indexu, 1 v případě chybného načtení
     */
    @Override
    public Index createIndex(final Element element) {
        try {
            return new AIMLIndex(Integer.parseInt(element
                    .getAttribute(AIML.ATT_INDEX.getValue())));
        } catch (final NumberFormatException e) {
            return new AIMLIndex();
        }
    }

    /**
     * Vrátí číselný pár podle hodnoty atributu index. Pár (1, 1) je výchozí
     * hodnota v případě chyby.
     * 
     * @param element
     *            prvek s atributem index obsahujícím dvě čísla oddělená čárkou
     * @return načtený dvojrozměrný index, pár (1, 1) v případě chybného načtení
     */
    @Override
    public TwoDimensionalIndex create2DIndex(final Element element) {
        final String indexValue =
                element.getAttribute(AIML.ATT_INDEX.getValue());

        if (EMPTY_ATTRIBUTE_VALUE.equals(indexValue)) {
            return new AIML2DIndex();
        }

        int firstValue = -1;

        final int commaPosition = indexValue.indexOf(COMMA);
        if (commaPosition < 0) {
            try {
                firstValue = Integer.parseInt(indexValue);
            } catch (final NumberFormatException e) {
                return new AIML2DIndex();
            }

            return new AIML2DIndex(firstValue,
                    TwoDimensionalIndex.DEFAULT_VALUE);
        }

        try {
            firstValue =
                    Integer.parseInt(indexValue.substring(0, commaPosition));
        } catch (final NumberFormatException e) {
            return new AIML2DIndex();
        }

        int secondValue = -1;

        try {
            secondValue =
                    Integer.parseInt(indexValue.substring(commaPosition + 1));
        } catch (final NumberFormatException e) {
            return new AIML2DIndex(firstValue,
                    TwoDimensionalIndex.DEFAULT_VALUE);
        }

        return new AIML2DIndex(firstValue, secondValue);
    }
}
