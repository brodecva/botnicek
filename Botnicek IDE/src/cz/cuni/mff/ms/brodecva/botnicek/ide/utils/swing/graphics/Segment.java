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
package cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.graphics;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.Serializable;

import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.Objects;

/**
 * Orientovaná úsečka s celočíselnými koordináty.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class Segment implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private final int xFrom;
    private final int yFrom;
    private final int xTo;
    private final int yTo;
    
    /**
     * Vytvoří úsečku.
     * 
     * @param from počátek
     * @param to konec
     * @return orientovaná úsečka z počátku do konce
     */
    public static Segment create(final Point from, final Point to) {
        return new Segment(from.x, from.y, to.x, to.y);
    }
    
    /**
     * Vytvoří úsečku.
     * 
     * @param xFrom počátek v ose x
     * @param yFrom počátek v ose y
     * @param xTo konec v ose x
     * @param yTo konec v ose y
     * @return orientovaná úsečka z počátku do konce
     */
    public static Segment create(final int xFrom, final int yFrom, final int xTo, final int yTo) {
        return new Segment(xFrom, yFrom, xTo, yTo);
    }
    
    private Segment(final int x1, final int y1, final int x2, final int y2) {
        this.xFrom = x1;
        this.yFrom = y1;
        this.xTo = x2;
        this.yTo = y2;
    }
    
    /**
     * Vrátí délku úsečky.
     * 
     * @return délka úsečky
     */
    public double getLength() {
        final int deltaX = xFrom - xTo;
        final int deltaY = yFrom - yTo;
        
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }
    
    /**
     * Vrátí obdélník, který úsečka vyznačuje v prostoru.
     * 
     * @return obdélník, který úsečka vyznačuje v prostoru
     */
    public Rectangle toBounds() {
        final int xMin = Math.min(xFrom, xTo);
        final int yMin = Math.min(yFrom, yTo);
        final int width = Math.max(xFrom, xTo) - xMin;
        final int height = Math.max(yFrom, yTo) - yMin;
        
        return new Rectangle(xMin, yMin, width, height);
    }

    /**
     * Vrátí úsečku (s koordináty zaokrouhlenými an nejbližší celé číslo), která je oproti původní posunuta o danou vzdálenost kolmo doprava oproti směru orientace.
     * 
     * @param offset vzdálenost posunutí
     * @return posunutá kopie úsečky
     */
    public Segment offset(final double offset) {
        if (offset == 0) {
            return this;
        }
        
        final double length = getLength();
        final int yDelta = yTo - yFrom;
        final int minusXDelta = xFrom - xTo;
        final double x1p = xFrom + offset * yDelta / length;
        final double x2p = xTo + offset * yDelta / length;
        final double y1p = yFrom + offset * minusXDelta / length;
        final double y2p = yTo + offset * minusXDelta / length;

        return Segment.create((int) Math.round(x1p), (int) Math.round(y1p), (int) Math.round(x2p), (int) Math.round(y2p));
    }
    
    /**
     * Vrátí, zda-li se bod nachází v prostoru tvořeném všemi kruhy se středy na kružnici a poloměrem nejvýše velikosti tolerance.
     * 
     * @param x souřadnice bodu podle osy x
     * @param y souřadnice bodu podle osy y
     * @param tolerance tolerance
     * @return zda-li se bod nachází na úsečce v dané toleranci
     */
    public boolean contains(final int x, final int y, final double tolerance) {
        final int xFromDelta = x - xFrom;
        final int yFromDelta = y - yFrom;
        
        if (isPoint()) {
            return Math.abs(yFromDelta) <= tolerance && Math.abs(xFromDelta) <= tolerance;
        }
        
        final int xSlope = xTo - xFrom;
        final int ySlope = yTo - yFrom;
        
        final double parameter = ((double) (xSlope * xFromDelta + ySlope * yFromDelta)) / ((double) (xSlope * xSlope + ySlope * ySlope));

        final double projectionX = xFrom + parameter * xSlope;
        final double projectionY = yFrom + parameter * ySlope;

        if ((parameter >= 0) && (parameter <= 1)) {
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
    
    /**
     * Vrátí kopii úsečky, která má obměněný počátek.
     * 
     * @param newFrom nový počátek
     * @return úsečka s jiným počátkem
     */
    public Segment moveFrom(final Point newFrom) {
        return moveFrom(newFrom.x, newFrom.y);
    }
    
    /**
     * Vrátí kopii úsečky, která má obměněný počátek.
     * 
     * @param newFromX souřadnice nového počátku v ose x
     * @param newFromY sou5adnice nového počátku v ose y
     * @return úsečka s jiným počátkem
     */
    public Segment moveFrom(final int newFromX, final int newFromY) {
        return Segment.create(newFromX, newFromY, this.xTo, this.yTo);
    }
    
    /**
     * Vrátí kopii úsečky, která má obměněný konec.
     * 
     * @param newTo nový koncem
     * @return úsečka s jiným koncem
     */
    public Segment moveTo(final Point newTo) {
        return moveTo(newTo.x, newTo.y);
    }
    
    /**
     * Vrátí kopii úsečky, která má obměněný konec.
     * 
     * @param newToX souřadnice nového konce v ose x
     * @param newToY sou5adnice nového konce v ose y
     * @return úsečka s jiným počátkem
     */
    public Segment moveTo(final int newToX, final int newToY) {
        return Segment.create(this.xFrom, this.yFrom, newToX, newToY);
    }
    
    /**
     * Indikuje, zda-li je úsečka bodem.
     * 
     * @return zda-li je úsečka bodem
     */
    public boolean isPoint() {
        return xFrom == xTo && yFrom == yTo;
    }

    /**
     * Vrátí počátek v souřadnici osy x.
     * 
     * @return počátek v souřadnici osy x
     */
    public int getFromX() {
        return xFrom;
    }

    /**
     * Vrátí počátek v souřadnici osy y.
     * 
     * @return počátek v souřadnici osy y
     */
    public int getFromY() {
        return yFrom;
    }

    /**
     * Vrátí konec v souřadnici osy x.
     * 
     * @return konec v souřadnici osy x
     */
    public int getToX() {
        return xTo;
    }

    /**
     * Vrátí konec v souřadnici osy y.
     * 
     * @return konec v souřadnici osy y
     */
    public int getToY() {
        return yTo;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + xFrom;
        result = prime * result + xTo;
        result = prime * result + yFrom;
        result = prime * result + yTo;
        return result;
    }

    /**
     * Porovná úsečku s objektem. Shoduje se pouze s úsečkou se stejnými koordináty.
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (Objects.isNull(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Segment other = (Segment) obj;
        if (xFrom != other.xFrom) {
            return false;
        }
        if (xTo != other.xTo) {
            return false;
        }
        if (yFrom != other.yFrom) {
            return false;
        }
        if (yTo != other.yTo) {
            return false;
        }
        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Segment [xFrom=" + xFrom + ", yFrom=" + yFrom + ", xTo=" + xTo
                + ", yTo=" + yTo + "]";
    }
}