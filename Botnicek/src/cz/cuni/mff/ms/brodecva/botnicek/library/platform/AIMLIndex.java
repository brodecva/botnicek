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
 * Jednorozměrná hodnota atributu index.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class AIMLIndex implements Serializable, Index {

    /**
     * UID serializované verze.
     */
    private static final long serialVersionUID = 4841166925774215964L;

    /**
     * Lokalizátor hlášek výjimek.
     */
    private static final ExceptionMessageLocalizer MESSAGE_LOCALIZER =
            ExceptionMessageLocalizer.getLocalizer();

    /**
     * Hodnota. Přirozené číslo větší nebo rovno 1.
     */
    private final int value;

    /**
     * Vytvoří výchozí jednorozměrný index.
     */
    public AIMLIndex() {
        value = DEFAULT_VALUE;
    }

    /**
     * Vytvoří jednorozměrný index.
     * 
     * @param value
     *            hodnota. Musí být alespoň {@value cz.cuni.mff.ms.brodecva.botnicek.library.platform.Index#LOWER_BOUND}.
     */
    public AIMLIndex(final int value) {
        if (value < 1) {
            throw new IndexOutOfBoundsException(MESSAGE_LOCALIZER.getMessage(
                    "platform.AIMLIndexOutOfBounds", value));
        }

        this.value = value;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.platform.Index#getValue()
     */
    @Override
    public int getValue() {
        return value;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.platform.Index#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + getValue();
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.library.platform.Index#equals(java.lang
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
        final AIMLIndex other = (AIMLIndex) obj;
        if (value != other.value) {
            return false;
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.platform.Index#toString()
     */
    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
