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
package cz.cuni.mff.ms.brodecva.botnicek.library.utils;

import java.io.Serializable;

import cz.cuni.mff.ms.brodecva.botnicek.library.processor.set.DisplayStrategy;

/**
 * Testovací implementace {@link DisplayStrategy} s nepřístupným konstruktorem.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
final class InacessibleDisplayStrategyStub implements DisplayStrategy, Serializable {

    /**
     * UID serializované verze.
     */
    private static final long serialVersionUID = -481328300115030202L;
    
    /**
     * Nepřístupný konstruktor.
     */
    private InacessibleDisplayStrategyStub() {
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.processor.set.DisplayStrategy#display(java.lang.String, java.lang.String)
     */
    @Override
    public String display(final String name, final String value) {
        return "output";
    }
    
}
