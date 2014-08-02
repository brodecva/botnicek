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
package cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types;

import java.net.URI;

/**
 * Atribut prvku stromu odlehčeného objektového modelu jazyka AIML.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public interface Attribute {

    /**
     * Vrátí název atributu.
     * 
     * @return název
     */
    String getName();

    /**
     * Vrátí hodnotu atributu.
     * 
     * @return hodnota
     */
    String getValue();

    /**
     * Vrátí prostor jmen atributu.
     * 
     * @return prostor jmen atributu
     */
    URI getNamespace();

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    int hashCode();

    /**
     * Atribut je shodný s objektem, pokud to je též atribut a má stejné jméno a URI prostoru jmen.
     */
    boolean equals(final Object obj);
}