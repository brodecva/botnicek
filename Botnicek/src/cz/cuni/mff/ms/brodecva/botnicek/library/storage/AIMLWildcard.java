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

/**
 * Speciální slova představující zástupné "znaky". Mohou nehradit jedno a více
 * slov. Liší se prioritou.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public enum AIMLWildcard implements Word {
    /**
     * Žolík podtržítko.
     */
    UNDERSCORE("_"),

    /**
     * Žolík hvězdička.
     */
    ASTERISK("*");

    /**
     * Textová hodnota.
     */
    private final String value;

    /**
     * Konstruktor zástupného "znaku".
     * 
     * @param value
     *            slovní reprezentace zástupného znaku
     */
    private AIMLWildcard(final String value) {
        this.value = value;
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
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return value;
    }
}
