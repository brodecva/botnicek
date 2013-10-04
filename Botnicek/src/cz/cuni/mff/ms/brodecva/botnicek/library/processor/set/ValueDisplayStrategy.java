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
package cz.cuni.mff.ms.brodecva.botnicek.library.processor.set;

import java.io.Serializable;

/**
 * Vrátí hodnotu predikátu místo jeho jména.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class ValueDisplayStrategy extends AbstractDisplayStrategy
        implements Serializable {

    /**
     * UID serializované verze.
     */
    private static final long serialVersionUID = 4615591048080824067L;

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.library.processor.set.DisplayStrategy
     * #display(java.lang.String, java.lang.String)
     */
    @Override
    public String display(final String name, final String value) {
        return value;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ValueDisplayStrategy";
    }

}
