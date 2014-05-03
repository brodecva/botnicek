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
package cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.arcs;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;

import com.google.common.base.Preconditions;

public enum ArcType {
    PATTERN("Pattern test", Color.BLACK, new BasicStroke()), PREDICATE_TEST("Predicate test", Color.ORANGE, new BasicStroke()), CODE_TEST("Code test", Color.YELLOW, new BasicStroke()), TRANSITION("Transition test", Color.BLACK, new BasicStroke()), RECURENT("Recurents", Color.RED, new BasicStroke());
    
    private final String name;
    private final Color color;
    private final Stroke stroke;

    private ArcType(final String name, final Color color, final Stroke stroke) {
        Preconditions.checkNotNull(name);
        Preconditions.checkNotNull(color);
        Preconditions.checkNotNull(stroke);
        Preconditions.checkArgument(!name.isEmpty());
        
        this.name = name;
        this.color = color;
        this.stroke = stroke;
    }
    
    /**
     * @return
     */
    public Stroke getStroke() {
        return this.stroke;
    }
    
    public Color getColor() {
        return this.color;
    }
    
    public String getName() {
        return this.name;
    }
}