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

import java.awt.BasicStroke;
import java.awt.Stroke;

/**
 * Výčet zobrazení typů vypravení uzlu.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
enum DispatchType {
    /**
     * Řadící typ.
     */
    ORDERED(new BasicStroke(5.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
            10.0f, new float[] { 10.0f }, 0.0f)),

    /**
     * Náhodný typ.
     */
    RANDOM(new BasicStroke(3.0f)),

    /**
     * Výchozí typ.
     */
    DEFAULT(new BasicStroke(3.0f));

    private final Stroke stroke;

    private DispatchType(final Stroke stroke) {
        this.stroke = stroke;
    }

    /**
     * Vrátí štětec typu.
     * 
     * @return štětec
     */
    public final Stroke getStroke() {
        return this.stroke;
    }
}