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

import java.io.Serializable;

/**
 * Zachovává bílé znaky v AIML.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class PreserveSpaceStrategy extends AbstractSpaceStrategy
        implements Serializable {

    /**
     * UID serializované verze.
     */
    private static final long serialVersionUID = -7958599597490153425L;

    /**
     * Vytvoří opatření nabádající k výchozímu zpracování v podstromu prvku.
     * 
     * @return opatření
     */
    public static PreserveSpaceStrategy create() {
        return new PreserveSpaceStrategy();
    }

    /**
     * Vytvoří opatření nabádající k výchozímu zpracování v podstromu prvku.
     */
    public PreserveSpaceStrategy() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.loader.SpaceStrategy#
     * transformOpenTag(java.lang.StringBuilder)
     */
    @Override
    public void transformOpenTag(final StringBuilder chars) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.loader.SpaceStrategy#
     * transformCloseTag(java.lang.StringBuilder)
     */
    @Override
    public void transformCloseTag(final StringBuilder chars) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.loader.SpaceStrategy
     * #transformChars(java.lang.StringBuilder, char[], int, int)
     */
    @Override
    public void transformChars(final StringBuilder chars, final char[] ch,
            final int start, final int length) {
        chars.append(ch, start, length);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "PreserveSpaceStrategy";
    }
}
