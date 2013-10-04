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
package cz.cuni.mff.ms.brodecva.botnicek.library.platform;

import java.io.Serializable;

import cz.cuni.mff.ms.brodecva.botnicek.library.utils.ExceptionMessageLocalizer;

/**
 * Dvojrozměrná hodnota atributu index.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class AIML2DIndex implements Serializable, TwoDimensionalIndex {
    /**
     * UID serializované verze.
     */
    private static final long serialVersionUID = -7233289085208956273L;

    /**
     * Lokalizátor hlášek výjimek.
     */
    private static final ExceptionMessageLocalizer MESSAGE_LOCALIZER =
            ExceptionMessageLocalizer.getLocalizer();

    /**
     * První hodnota. Přirozené číslo větší nebo rovno {@link TwoDimensionalIndex#LOWER_BOUND}.
     */
    private final int firstValue;

    /**
     * Druhá hodnota. Přirozené číslo větší nebo rovno {@value cz.cuni.mff.ms.brodecva.botnicek.library.platform.TwoDimensionalIndex#LOWER_BOUND}.
     */
    private final int secondValue;

    /**
     * Vytvoří výchozí jednorozměrný index.
     */
    public AIML2DIndex() {
        firstValue = DEFAULT_VALUE;
        secondValue = DEFAULT_VALUE;
    }

    /**
     * Vytvoří jednorozměrný index.
     * 
     * @param firstValue
     *            první hodnota. Musí být alespoň {@value cz.cuni.mff.ms.brodecva.botnicek.library.platform.TwoDimensionalIndex#LOWER_BOUND}.
     * @param secondValue
     *            druhá hodnota. Musí být alespoň {@value cz.cuni.mff.ms.brodecva.botnicek.library.platform.TwoDimensionalIndex#LOWER_BOUND}.
     */
    public AIML2DIndex(final int firstValue, final int secondValue) {
        if (firstValue < LOWER_BOUND && secondValue < LOWER_BOUND) {
            throw new IndexOutOfBoundsException(MESSAGE_LOCALIZER.getMessage(
                    "platform.AIMLIndicesOutOfBounds", firstValue, secondValue));
        }

        if (firstValue < LOWER_BOUND) {
            throw new IndexOutOfBoundsException(MESSAGE_LOCALIZER.getMessage(
                    "platform.AIMLIndexOutOfBounds", firstValue));
        }

        if (secondValue < LOWER_BOUND) {
            throw new IndexOutOfBoundsException(MESSAGE_LOCALIZER.getMessage(
                    "platform.AIMLIndexOutOfBounds", secondValue));
        }

        this.firstValue = firstValue;
        this.secondValue = secondValue;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.library.platform.TwoDimensionalIndex
     * #getFirstValue()
     */
    @Override
    public int getFirstValue() {
        return firstValue;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.library.platform.TwoDimensionalIndex
     * #getSecondValue()
     */
    @Override
    public int getSecondValue() {
        return secondValue;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.library.platform.TwoDimensionalIndex
     * #hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + firstValue;
        result = prime * result + secondValue;
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.library.platform.TwoDimensionalIndex
     * #equals(java.lang.Object)
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
        final AIML2DIndex other = (AIML2DIndex) obj;
        if (firstValue != other.firstValue) {
            return false;
        }
        if (secondValue != other.secondValue) {
            return false;
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.library.platform.TwoDimensionalIndex
     * #toString()
     */
    @Override
    public String toString() {
        return "(" + firstValue + ", " + secondValue
                + ")";
    }
}
