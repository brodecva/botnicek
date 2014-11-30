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

    /**
     * Vytvoří úsečku.
     * 
     * @param xFrom
     *            počátek v ose x
     * @param yFrom
     *            počátek v ose y
     * @param xTo
     *            konec v ose x
     * @param yTo
     *            konec v ose y
     * @return orientovaná úsečka z počátku do konce
     */
    public static Segment create(final int xFrom, final int yFrom,
            final int xTo, final int yTo) {
        return new Segment(xFrom, yFrom, xTo, yTo);
    }

    /**
     * Vytvoří úsečku.
     * 
     * @param from
     *            počátek
     * @param to
     *            konec
     * @return orientovaná úsečka z počátku do konce
     */
    public static Segment create(final Point from, final Point to) {
        return new Segment(from.x, from.y, to.x, to.y);
    }

    private final int xFrom;
    private final int yFrom;

    private final int xTo;

    private final int yTo;

    private Segment(final int x1, final int y1, final int x2, final int y2) {
        this.xFrom = x1;
        this.yFrom = y1;
        this.xTo = x2;
        this.yTo = y2;
    }

    /**
     * Vrátí, zda-li se bod nachází v prostoru tvořeném všemi kruhy se středy na
     * kružnici a poloměrem nejvýše velikosti tolerance.
     * 
     * @param x
     *            souřadnice bodu podle osy x
     * @param y
     *            souřadnice bodu podle osy y
     * @param tolerance
     *            tolerance
     * @return zda-li se bod nachází na úsečce v dané toleranci
     */
    public boolean contains(final int x, final int y, final double tolerance) {
        final int xFromDelta = x - this.xFrom;
        final int yFromDelta = y - this.yFrom;

        if (isPoint()) {
            return Math.abs(yFromDelta) <= tolerance
                    && Math.abs(xFromDelta) <= tolerance;
        }

        final int xSlope = this.xTo - this.xFrom;
        final int ySlope = this.yTo - this.yFrom;

        final double parameter =
                ((double) (xSlope * xFromDelta + ySlope * yFromDelta))
                        / ((double) (xSlope * xSlope + ySlope * ySlope));

        final double projectionX = this.xFrom + parameter * xSlope;
        final double projectionY = this.yFrom + parameter * ySlope;

        if ((parameter >= 0) && (parameter <= 1)) {
            final double projectionDeltaX = projectionX - x;
            final double projectionDeltaY = projectionY - y;
            final double distance =
                    Math.sqrt(projectionDeltaX * projectionDeltaX
                            + projectionDeltaY * projectionDeltaY);
            return distance <= tolerance;
        } else {
            final int xToDelta = x - this.xTo;
            final int yToDelta = y - this.yTo;

            final double fromDistanceSquared =
                    xFromDelta * xFromDelta + yFromDelta * yFromDelta;
            final double toDistanceSquared =
                    xToDelta * xToDelta + yToDelta * yToDelta;

            return Math.sqrt(Math.min(fromDistanceSquared, toDistanceSquared)) <= tolerance;
        }
    }

    /**
     * Porovná úsečku s objektem. Shoduje se pouze s úsečkou se stejnými
     * koordináty.
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
        if (this.xFrom != other.xFrom) {
            return false;
        }
        if (this.xTo != other.xTo) {
            return false;
        }
        if (this.yFrom != other.yFrom) {
            return false;
        }
        if (this.yTo != other.yTo) {
            return false;
        }
        return true;
    }

    /**
     * Vrátí počátek v souřadnici osy x.
     * 
     * @return počátek v souřadnici osy x
     */
    public int getFromX() {
        return this.xFrom;
    }

    /**
     * Vrátí počátek v souřadnici osy y.
     * 
     * @return počátek v souřadnici osy y
     */
    public int getFromY() {
        return this.yFrom;
    }

    /**
     * Vrátí délku úsečky.
     * 
     * @return délka úsečky
     */
    public double getLength() {
        final int deltaX = this.xFrom - this.xTo;
        final int deltaY = this.yFrom - this.yTo;

        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }

    /**
     * Vrátí konec v souřadnici osy x.
     * 
     * @return konec v souřadnici osy x
     */
    public int getToX() {
        return this.xTo;
    }

    /**
     * Vrátí konec v souřadnici osy y.
     * 
     * @return konec v souřadnici osy y
     */
    public int getToY() {
        return this.yTo;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.xFrom;
        result = prime * result + this.xTo;
        result = prime * result + this.yFrom;
        result = prime * result + this.yTo;
        return result;
    }

    /**
     * Indikuje, zda-li je úsečka bodem.
     * 
     * @return zda-li je úsečka bodem
     */
    public boolean isPoint() {
        return this.xFrom == this.xTo && this.yFrom == this.yTo;
    }

    /**
     * Vrátí kopii úsečky, která má obměněný počátek.
     * 
     * @param newFromX
     *            souřadnice nového počátku v ose x
     * @param newFromY
     *            sou5adnice nového počátku v ose y
     * @return úsečka s jiným počátkem
     */
    public Segment moveFrom(final int newFromX, final int newFromY) {
        return Segment.create(newFromX, newFromY, this.xTo, this.yTo);
    }

    /**
     * Vrátí kopii úsečky, která má obměněný počátek.
     * 
     * @param newFrom
     *            nový počátek
     * @return úsečka s jiným počátkem
     */
    public Segment moveFrom(final Point newFrom) {
        return moveFrom(newFrom.x, newFrom.y);
    }

    /**
     * Vrátí kopii úsečky, která má obměněný konec.
     * 
     * @param newToX
     *            souřadnice nového konce v ose x
     * @param newToY
     *            sou5adnice nového konce v ose y
     * @return úsečka s jiným počátkem
     */
    public Segment moveTo(final int newToX, final int newToY) {
        return Segment.create(this.xFrom, this.yFrom, newToX, newToY);
    }

    /**
     * Vrátí kopii úsečky, která má obměněný konec.
     * 
     * @param newTo
     *            nový koncem
     * @return úsečka s jiným koncem
     */
    public Segment moveTo(final Point newTo) {
        return moveTo(newTo.x, newTo.y);
    }

    /**
     * Vrátí úsečku (s koordináty zaokrouhlenými an nejbližší celé číslo), která
     * je oproti původní posunuta o danou vzdálenost kolmo doprava oproti směru
     * orientace.
     * 
     * @param offset
     *            vzdálenost posunutí
     * @return posunutá kopie úsečky
     */
    public Segment offset(final double offset) {
        if (offset == 0) {
            return this;
        }

        final double length = getLength();
        final int yDelta = this.yTo - this.yFrom;
        final int minusXDelta = this.xFrom - this.xTo;
        final double x1p = this.xFrom + offset * yDelta / length;
        final double x2p = this.xTo + offset * yDelta / length;
        final double y1p = this.yFrom + offset * minusXDelta / length;
        final double y2p = this.yTo + offset * minusXDelta / length;

        return Segment.create((int) Math.round(x1p), (int) Math.round(y1p),
                (int) Math.round(x2p), (int) Math.round(y2p));
    }

    /**
     * Vrátí obdélník, který úsečka vyznačuje v prostoru.
     * 
     * @return obdélník, který úsečka vyznačuje v prostoru
     */
    public Rectangle toBounds() {
        final int xMin = Math.min(this.xFrom, this.xTo);
        final int yMin = Math.min(this.yFrom, this.yTo);
        final int width = Math.max(this.xFrom, this.xTo) - xMin;
        final int height = Math.max(this.yFrom, this.yTo) - yMin;

        return new Rectangle(xMin, yMin, width, height);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Segment [xFrom=" + this.xFrom + ", yFrom=" + this.yFrom
                + ", xTo=" + this.xTo + ", yTo=" + this.yTo + "]";
    }
}