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
package cz.cuni.mff.ms.brodecva.botnicek.library.processor;

import java.net.URI;

/**
 * Registr procesorů.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public interface ProcessorRegistry {

    /**
     * Vrátí URI jmenného prostoru.
     * 
     * @return URI jmenného prostoru
     */
    URI getNamespace();

    /**
     * Vrátí procesor pod klíčem, pokud se shodují jmenné prostory či je jmenný
     * prostor {@code null}.
     * 
     * @param key
     *            klíč
     * @param namespace
     *            jmenný prostor
     * @return registrovaná třída
     * @throws ClassNotFoundException
     *             pokud pod daným klíčem neexistuje žádná třída
     */
    Class<? extends Processor> get(final String key, final URI namespace)
            throws ClassNotFoundException;

    /**
     * Vrátí hash registru.
     * 
     * @return hash z uložených položek a definovaného namespace
     */
    @Override
    int hashCode();

    /**
     * Porovná objekt s registrem.
     * 
     * @param obj
     *            objekt
     * @return true, pokud objekt je procesorem registrů stejného typu se
     *         stejným namespace a uloženými položkami
     */
    @Override
    boolean equals(Object obj);

}
