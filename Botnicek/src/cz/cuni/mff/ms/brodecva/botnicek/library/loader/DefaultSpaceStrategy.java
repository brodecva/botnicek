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
package cz.cuni.mff.ms.brodecva.botnicek.library.loader;

import static java.lang.Character.isWhitespace;

import java.io.Serializable;

import cz.cuni.mff.ms.brodecva.botnicek.library.platform.XML;

/**
 * Zpracovává znaky výchozím způsobem pro AIML.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class DefaultSpaceStrategy extends AbstractSpaceStrategy implements
        Serializable {

    /**
     * UID serializované verze.
     */
    private static final long serialVersionUID = 8164433217111476764L;

    /**
     * Mezera.
     */
    private static final char ONE_SPACE = ' ';

    /**
     * Koncový znak tagu.
     */
    private static final char TAG_END = XML.TAG_END.getValue().toCharArray()[0];

    /**
     * Vytvoří opatření nabádající k výchozímu zpracování v podstromu prvku.
     * 
     * @return opatření
     */
    public static DefaultSpaceStrategy create() {
        return new DefaultSpaceStrategy();
    }

    /**
     * Vytvoří opatření nabádající k výchozímu zpracování v podstromu prvku v
     * šabloně AIML.
     */
    private DefaultSpaceStrategy() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.loader.SpaceStrategy#
     * transformOpenTag(java.lang.StringBuilder)
     */
    @Override
    public void transformOpenTag(final StringBuilder chars) {
        clearSpacesButOneIfAny(chars);
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.loader.SpaceStrategy#
     * transformCloseTag(java.lang.StringBuilder)
     */
    @Override
    public void transformCloseTag(final StringBuilder chars) {
        clearSpacesButOneIfAny(chars);
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.loader.SpaceStrategy
     * #transformChars (java.lang.StringBuilder, char[], int, int)
     */
    @Override
    public void transformChars(final StringBuilder chars, final char[] ch,
            final int start, final int length) {
        if (chars.length() < 2) {
            chars.append(ch, start, length);
            return;
        }

        final int end = start + length;
        for (int i = start; i < end; i++) {
            final int bufferLength = chars.length();

            final char lastChar = chars.charAt(bufferLength - 1);
            final char nextLastChar = chars.charAt(bufferLength - 2);

            final char addedChar = ch[i];

            if (!isWhitespace(addedChar)) {
                chars.append(addedChar);
                continue;
            }

            if (lastChar == TAG_END) {
                chars.append(ONE_SPACE);
                continue;
            }

            if (nextLastChar == TAG_END && isWhitespace(lastChar)) {
                assert lastChar == ONE_SPACE;
                continue;
            }

            chars.append(addedChar);
        }
    }

    /**
     * Vyčistí buffer od konce od všech mezer, pokud tam jsou, a nahradí je
     * jednou. V případě, že není žádná nalezena, neudělá nic.
     * 
     * @param chars
     *            buffer znaků
     */
    private void clearSpacesButOneIfAny(final StringBuilder chars) {
        if (chars.length() == 0) {
            return;
        }

        int index = chars.length() - 1;
        boolean found = false;
        while (isWhitespace(chars.charAt(index))) {
            chars.deleteCharAt(index);
            found = true;
            index--;
        }

        if (found) {
            chars.append(ONE_SPACE);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "DefaultSpaceStrategy";
    }
}
