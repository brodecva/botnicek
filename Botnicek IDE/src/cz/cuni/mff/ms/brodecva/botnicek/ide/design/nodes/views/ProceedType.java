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
 * Výčet zobrazení typů posunu dle míry interakce.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
enum ProceedType {
    /**
     * Zadávací typ.
     */
    INPUT(Color.RED),

    /**
     * Procesní typ.
     */
    PROCESSING(Color.WHITE);

    private final Color background;

    private ProceedType(final Color background) {
        this.background = background;
    }

    /**
     * Vrátí barvu pozadí.
     * 
     * @return barva pozadí
     */
    public final Color getBackground() {
        return this.background;
    }
}