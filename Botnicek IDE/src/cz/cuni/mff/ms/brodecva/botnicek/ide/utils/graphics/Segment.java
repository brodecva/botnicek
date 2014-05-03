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
package cz.cuni.mff.ms.brodecva.botnicek.ide.utils.graphics;

import java.awt.Point;
import java.awt.Rectangle;

public class Segment {
    private final int xFrom;
    private final int yFrom;
    private final int xTo;
    private final int yTo;
    
    public static Segment create(final Point from, final Point to) {
        return new Segment((int) from.getX(), (int) from.getY(), (int) to.getX(), (int) to.getY());
    }
    
    public static Segment create(final int xFrom, final int yFrom, final int xTo, final int yTo) {
        return new Segment(xFrom, yFrom, xTo, yTo);
    }
    
    private Segment(final int x1, final int y1, final int x2, final int y2) {
        this.xFrom = x1;
        this.yFrom = y1;
        this.xTo = x2;
        this.yTo = y2;
    }
    
    private double getLength() {
        final int deltaX = xFrom - xTo;
        final int deltaY = yFrom - yTo;
        
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }
    
    public Rectangle toBounds() {
        final int xMin = Math.min(xFrom, xTo);
        final int yMin = Math.min(yFrom, yTo);
        final int width = Math.max(xFrom, xTo) - xMin;
        final int height = Math.max(yFrom, yTo) - yMin;
        
        return new Rectangle(xMin, yMin, width, height);
    }

    /**
     * @param offset
     * @return
     */
    public Segment offset(final double offset) {
        if (offset == 0) {
            return this;
        }
        
        final double length = getLength();
        final double x1p = xFrom + offset * (yTo-yFrom) / length;
        final double x2p = xTo + offset * (yTo-yFrom) / length;
        final double y1p = yFrom + offset * (xFrom-xTo) / length;
        final double y2p = yTo + offset * (xFrom-xTo) / length;

        return Segment.create((int) x1p, (int) y1p, (int) x2p, (int) y2p);
    }
    
    public boolean contains(final int x, final int y, final double tolerance) {
        final int xFromDelta = x - xFrom;
        final int yFromDelta = y - yFrom;
        
        if (isPoint()) {
            return Math.abs(yFromDelta) <= tolerance && Math.abs(xFromDelta) <= tolerance;
        }
        
        final int xSlope = xTo - xFrom;
        final int ySlope = yTo - yFrom;
        final double parameter = - (xSlope * xFromDelta + ySlope * yFromDelta) / (xSlope * xSlope) + (ySlope * ySlope);

        final double projectionX = xFrom + parameter * xSlope;
        final double projectionY = yFrom + parameter * ySlope;

        if ((parameter >= 0) && (parameter <= 1) )
        {
            final double projectionDeltaX = projectionX - x;
            final double projectionDeltaY = projectionY - y;
            final double distance = Math.sqrt(projectionDeltaX * projectionDeltaX + projectionDeltaY * projectionDeltaY);
            return distance <= tolerance;
        } else {
            final int xToDelta = x - xTo;
            final int yToDelta = y - yTo;
            
            final double fromDistanceSquared = xFromDelta * xFromDelta + yFromDelta * yFromDelta;
            final double toDistanceSquared = xToDelta * xToDelta + yToDelta * yToDelta;
            
            return Math.sqrt(Math.min(fromDistanceSquared, toDistanceSquared)) <= tolerance;
        }
    }
    
    public Segment moveFrom(final Point newFrom) {
        return moveFrom((int) newFrom.getX(), (int) newFrom.getY());
    }
    
    public Segment moveFrom(final int newFromX, final int newFromY) {
        return Segment.create(newFromX, newFromY, this.xTo, this.yTo);
    }
    
    public Segment moveTo(final Point newTo) {
        return moveTo((int) newTo.getX(), (int) newTo.getY());
    }
    
    public Segment moveTo(final int newToX, final int newToY) {
        return Segment.create(newToX, newToY, this.xTo, this.yTo);
    }
    
    public boolean isPoint() {
        return xFrom == xTo && yFrom == yTo;
    }

    /**
     * @return
     */
    public int getFromX() {
        return xFrom;
    }

    /**
     * @return
     */
    public int getFromY() {
        return yFrom;
    }

    /**
     * @return
     */
    public int getToX() {
        return xTo;
    }

    /**
     * @return
     */
    public int getToY() {
        return yTo;
    }
}