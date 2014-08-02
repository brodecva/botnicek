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
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.graph;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;

import com.google.common.base.Preconditions;

/**
 * Výčet formátování typů hran v grafu.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
enum ArcType {
    /**
     * 
     */
    PATTERN(Color.MAGENTA, new BasicStroke()),
    
    /**
     * 
     */
    PREDICATE_TEST(Color.YELLOW, new BasicStroke()),
    
    /**
     * 
     */
    CODE_TEST(Color.ORANGE, new BasicStroke()),
    
    /**
     * 
     */
    TRANSITION(Color.BLACK, new BasicStroke()),
    
    /**
     * 
     */
    RECURENT(Color.RED, new BasicStroke());
    
    private final Color color;
    private final Stroke stroke;

    private ArcType(final Color color, final Stroke stroke) {
        Preconditions.checkNotNull(color);
        Preconditions.checkNotNull(stroke);
        
        this.color = color;
        this.stroke = stroke;
    }
    
    /**
     * Vrátí štětec pro vykreslení spojnice hrany.
     * 
     * @return štětec
     */
    public Stroke getStroke() {
        return this.stroke;
    }
    
    /**
     * Vrátí barvu spojnice hrany.
     * 
     * @return barva
     */
    public Color getColor() {
        return this.color;
    }
}