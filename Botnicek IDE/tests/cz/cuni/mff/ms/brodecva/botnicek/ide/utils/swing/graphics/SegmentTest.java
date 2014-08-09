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

import static org.junit.Assert.*;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import cz.cuni.mff.ms.brodecva.botnicek.library.utils.test.UnitTest;

/**
 * Test orientované úsečky s celočíselnými koordináty.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see Segment
 */
@Category(UnitTest.class)
public class SegmentTest {

    private static final double DOUBLE_COMPARISONS_TOLERANCE = 0.002d;

    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.graphics.Segment#create(java.awt.Point, java.awt.Point)}.
     */
    @Test
    public void testCreatePointPointDifferent() {
        Segment.create(new Point(0, 1), new Point(2, 3));
    }
    
    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.graphics.Segment#create(java.awt.Point, java.awt.Point)}.
     */
    @Test
    public void testCreatePointPointSame() {
        final Point point = new Point(0, 1); 
        
        Segment.create(point, point);
    }

    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.graphics.Segment#getLength()}.
     */
    @Test
    public void testGetLengthWhenPointReturnsZero() {
        final Point coordinates = new Point(2, 3);
        
        final Segment point = Segment.create(coordinates, coordinates);
        assertEquals(0, point.getLength(), 0); // Aritmetika typu zaručuju exaktní práci s nulovými argumenty.
    }
    
    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.graphics.Segment#getLength()}.
     */
    @Test
    public void testGetLengthWhenEqualsReturnsEqualLength() {
        final Segment first = Segment.create(0, 1, -2, 3);
        final Segment second = Segment.create(0, 1, -2, 3);
        
        assertEquals(first.getLength(), second.getLength(), 0);
    }
    
    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.graphics.Segment#getLength()}.
     */
    @Test
    public void testGetLengthWhenUnitDiagonalReturnsSquareRootOfTwo() {
        
        final Segment segment = Segment.create(new Point(0, 0), new Point(1, 1));
        assertEquals(Math.sqrt(2), segment.getLength(), DOUBLE_COMPARISONS_TOLERANCE);
    }
    
    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.graphics.Segment#getLength()}.
     */
    @Test
    public void testGetLengthWhenNegativeAndPositiveCoordinatesReturnsSame() {
        final Segment segmentInPositive = Segment.create(new Point(3, 2), new Point(1, 1));
        final Segment segmentInNegative = Segment.create(new Point(-3, -2), new Point(-1, -1));
        assertEquals(segmentInPositive.getLength(), segmentInNegative.getLength(), DOUBLE_COMPARISONS_TOLERANCE);
    }

    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.graphics.Segment#toBounds()}.
     */
    @Test
    public void testToBounds() {
        final Rectangle source = new Rectangle(new Point(2, 3), new Dimension(2, 3));
        
        final Segment segment = Segment.create((int) source.getMinX(), (int) source.getMinY(), (int) source.getMaxX(), (int) source.getMaxY());
        assertEquals(source, segment.toBounds());
    }
    
    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.graphics.Segment#toBounds()}.
     */
    @Test
    public void testToBoundsWhenPoint() {
        final Point coordinates = new Point(2, 3);
        
        final Segment point = Segment.create(coordinates, coordinates);
        assertEquals(new Rectangle(new Point(2, 3)), point.toBounds());
    }
    
    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.graphics.Segment#offset(double)}.
     */
    @Test
    public void testOffsetWhenZeroReturnsEqual() {
        final Segment original = Segment.create(new Point(2, 3), new Point(-3,1));
        
        assertEquals(original, original.offset(0));
    }
    
    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.graphics.Segment#offset(double)} and {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.graphics.Segment#getLength()}.
     */
    @Test
    public void testOffsetAndGetLengthWhenSetOffReturnsEqualLength() {
        final Segment original = Segment.create(new Point(2, 3), new Point(-3,1));
        final Segment offset = original.offset(5);
        
        assertEquals(offset.getLength(), original.getLength(), DOUBLE_COMPARISONS_TOLERANCE);
    }
    
    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.graphics.Segment#offset(double)}.
     */
    @Test
    public void testOffsetWhenSetOffAndThenSetBackReturnsEqual() {
        final Segment original = Segment.create(new Point(2, 3), new Point(-3,1));
        
        final double value = 5;
        final Segment offset = original.offset(value);
        
        assertEquals(original, offset.offset(-value));
    }
    
    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.graphics.Segment#offset(double)}.
     */
    @Test
    public void testOffsetWhenHorizontal() {
        final Segment original = Segment.create(new Point(2, 3), new Point(4, 3));
        
        assertEquals(Segment.create(new Point(2, 2), new Point(4, 2)), original.offset(1));
    }
    
    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.graphics.Segment#offset(double)}.
     */
    @Test
    public void testOffset() {
        final Segment original = Segment.create(new Point(0, 0), new Point(2, 2));
        
        assertEquals(Segment.create(new Point(1, -1), new Point(3, 1)), original.offset(Math.sqrt(2)));
    }

    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.graphics.Segment#contains(int, int, double)}.
     */
    @Test
    public void testContainsWhenPointReturnsTrueForItself() {
        final Point coordinates = new Point(2, 3);
        final Segment point = Segment.create(coordinates, coordinates);
        
        assertTrue(point.contains(coordinates.x, coordinates.y, DOUBLE_COMPARISONS_TOLERANCE));
    }
    
    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.graphics.Segment#contains(int, int, double)}.
     */
    @Test
    public void testContainsWhenStartAsArgumentReturnsTrue() {
        final Point coordinates = new Point(2, 3);
        final Segment point = Segment.create(coordinates, new Point(-8, -16));
        
        assertTrue(point.contains(coordinates.x, coordinates.y, DOUBLE_COMPARISONS_TOLERANCE));
    }
    
    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.graphics.Segment#contains(int, int, double)}.
     */
    @Test
    public void testContainsWhenEndAsArgumentReturnsTrue() {
        final Point coordinates = new Point(2, -3);
        final Segment point = Segment.create(new Point(-19, 16), coordinates);
        
        assertTrue(point.contains(coordinates.x, coordinates.y, DOUBLE_COMPARISONS_TOLERANCE));
    }
    
    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.graphics.Segment#contains(int, int, double)}.
     */
    @Test
    public void testContainsWhenInnerPointAsArgumentReturnsTrue() {
        final Segment point = Segment.create(new Point(0, 0), new Point(2, 2));
        
        assertTrue(point.contains(1, 1, DOUBLE_COMPARISONS_TOLERANCE));
    }
    
    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.graphics.Segment#contains(int, int, double)}.
     */
    @Test
    public void testContainsWhenAroundInnerPointAsArgumentReturnsTrue() {
        final Segment point = Segment.create(new Point(0, 0), new Point(100, 100));
        
        assertTrue(point.contains(51, 49, 5));
    }
    
    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.graphics.Segment#contains(int, int, double)}.
     */
    @Test
    public void testContainsWhenAroundStartAsArgumentReturnsTrue() {
        final Segment point = Segment.create(new Point(0, 0), new Point(100, 100));
        
        assertTrue(point.contains(-1, 2, 6));
    }
    
    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.graphics.Segment#contains(int, int, double)}.
     */
    @Test
    public void testContainsWhenAroundEndAsArgumentReturnsTrue() {
        final Segment point = Segment.create(new Point(0, 0), new Point(100, 100));
        
        assertTrue(point.contains(101, 102, 3));
    }

    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.graphics.Segment#moveFrom(java.awt.Point)} and {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.graphics.Segment#getFromX()} and {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.graphics.Segment#getFromY()}.
     */
    @Test
    public void testMoveFromAndGetFromReflectsNewFrom() {
        final Segment original = Segment.create(new Point(2, 3), new Point(-100, 2_000));
        
        final int newFromX = -500;
        final int newFromY = 1_000;
        
        final Segment moved = original.moveFrom(new Point(newFromX, newFromY));
        
        assertEquals(newFromX, moved.getFromX());
        assertEquals(newFromY, moved.getFromY());
    }
    
    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.graphics.Segment#moveFrom(java.awt.Point)} and {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.graphics.Segment#getToX()} and {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.graphics.Segment#getToY()}.
     */
    @Test
    public void testMoveFromAndGetFromDoesNotChangeTo() {
        final int originalToX = -500;
        final int originalToY = 1_000;
        
        final Segment original = Segment.create(new Point(2, 3), new Point(originalToX, originalToY));
        final Segment moved = original.moveFrom(new Point(13, -9));
        
        assertEquals(originalToX, moved.getToX());
        assertEquals(originalToY, moved.getToY());
    }
    
    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.graphics.Segment#moveFrom(java.awt.Point)}.
     */
    @Test
    public void testMoveFromReturnsEqualToNewlyCreated() {
        final Point oldTo = new Point(-100, 2_000);        
        final Point newFrom = new Point(-500, 1_000);
        
        final Segment original = Segment.create(new Point(2, 3), oldTo);
        final Segment moved = original.moveFrom(newFrom);
        
        assertEquals(Segment.create(newFrom, oldTo), moved);
    }
    
    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.graphics.Segment#moveTo(java.awt.Point)} and {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.graphics.Segment#getToX()} and {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.graphics.Segment#getToY()}.
     */
    @Test
    public void testMoveToAndGetToReflectsNewTo() {
        final Segment original = Segment.create(new Point(2, 3), new Point(-100, 2_000));
        
        final int newToX = -500;
        final int newToY = 1_000;
        
        final Segment moved = original.moveTo(new Point(newToX, newToY));
        
        assertEquals(newToX, moved.getToX());
        assertEquals(newToY, moved.getToY());
    }
    
    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.graphics.Segment#moveFrom(java.awt.Point)} and {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.graphics.Segment#getFromX()} and {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.graphics.Segment#getFromY()}.
     */
    @Test
    public void testMoveToAndGetToDoesNotChangeFrom() {
        final int originalFromX = -500;
        final int originalFromY = 1_000;
        
        final Segment original = Segment.create(new Point(originalFromX, originalFromY), new Point(2, 3));
        final Segment moved = original.moveTo(new Point(13, -9));
        
        assertEquals(originalFromX, moved.getFromX());
        assertEquals(originalFromY, moved.getFromY());
    }
    
    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.graphics.Segment#moveTo(java.awt.Point)}.
     */
    @Test
    public void testMoveToReturnsEqualToNewlyCreated() {
        final Point oldFrom = new Point(-100, 2_000);        
        final Point newTo = new Point(-500, 1_000);
        
        final Segment original = Segment.create(oldFrom, new Point(56, -93));
        final Segment moved = original.moveTo(newTo);
        
        assertEquals(Segment.create(oldFrom, newTo), moved);
    }

    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.graphics.Segment#isPoint()}.
     */
    @Test
    public void testIsPointWhenPointReturnsTrue() {
        assertTrue(Segment.create(new Point(2, 3), new Point(2, 3)).isPoint());
    }
    
    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.graphics.Segment#isPoint()}.
     */
    @Test
    public void testIsPointWhenNotPointReturnsFalse() {
        assertFalse(Segment.create(new Point(2, 8), new Point(1, 3)).isPoint());
    }

    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.graphics.Segment#getFromX()}.
     */
    @Test
    public void testGetFromXReturnsAsCreated() {
        final int fromX = 5;
        final Segment created = Segment.create(fromX, 3, 2, 1);
        assertEquals(fromX, created.getFromX());
    }

    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.graphics.Segment#getFromY()}.
     */
    @Test
    public void testGetFromYReturnsAsCreated() {
        final int fromY = 5;
        final Segment created = Segment.create(-8, fromY, 2, 1);
        assertEquals(fromY, created.getFromY());
    }

    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.graphics.Segment#getToX()}.
     */
    @Test
    public void testGetToXReturnsAsCreated() {
        final int toX = 5;
        final Segment created = Segment.create(-8, 3, toX, 1);
        assertEquals(toX, created.getToX());
    }

    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.graphics.Segment#getToY()}.
     */
    @Test
    public void testGetToYReturnsAsCreated() {
        final int toY = -1;
        final Segment created = Segment.create(-8, 3, 2, toY);
        assertEquals(toY, created.getToY());
    }

    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.graphics.Segment#equals(java.lang.Object)} and {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.graphics.Segment#hashCode()}.
     */
    @Test
    public void testEqualsAndHashCode() {
        EqualsVerifier.forClass(Segment.class).usingGetClass().suppress(Warning.NULL_FIELDS).verify();
    }

}
