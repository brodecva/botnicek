/**
 * Copyright Václav Brodec 2014.
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
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.views;

import java.awt.Color;

/**
 * Výčet zobrazení typů umístění uzlu.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
enum PositionalType {
    /**
     * Izolovaný.
     */
    ISOLATED(Color.YELLOW),

    /**
     * Vnitřní.
     */
    INNER(Color.BLACK),

    /**
     * Vstupní.
     */
    ENTER(Color.BLUE),

    /**
     * Výstupní.
     */
    EXIT(Color.GREEN);

    private final Color rim;

    private PositionalType(final Color rim) {
        this.rim = rim;
    }

    /**
     * Vrátí barvu okraje.
     * 
     * @return barva okraje
     */
    public final Color getRim() {
        return this.rim;
    }
}