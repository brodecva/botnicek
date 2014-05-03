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

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.util.Vector;

import javax.swing.JComponent;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.controllers.ArcPropertiesController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.nodes.NodeUI;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.graphics.Segment;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.GraphComponent;


/**
 * @author Václav Brodec
 * @version 1.0
 */
public final class ArcUI extends GraphComponent {
    
    private static final int ARROW_SIDE_LENGTH = 10;
    private static final double ARROW_ARM_ANGLE = Math.toRadians(30);
    private static final int DEFAULT_THICKNESS = 3;
    
    private final ArcPropertiesController arcPropertiesController;
    
    private final NodeUI from;
    private final NodeUI to;
    
    private Offset offset;
    private int thickness;
    private int priority;
    private ArcType type;
    
    public static ArcUI create(final String name, final int priority, final ArcType type, final NodeUI from, final NodeUI to, final ArcPropertiesController arcPropertiesController) {
        return create(name, priority, type, from, to, Offset.LOWER_OFFSET, DEFAULT_THICKNESS, arcPropertiesController);
    }
    
    private static ArcUI create(final String name, final int priority, final ArcType type, final NodeUI from, final NodeUI to, final Offset offset, final int thickness, final ArcPropertiesController arcPropertiesController) {
        Preconditions.checkNotNull(type);
        Preconditions.checkNotNull(name);
        
        final ArcUI newInstance = new ArcUI(from, to, arcPropertiesController);
        newInstance.setName(name);
        
        newInstance.setBounds(newInstance.getSegment().toBounds());
        newInstance.setPriority(priority);
        newInstance.setType(type);
        newInstance.setOffset(offset);
        newInstance.setThickness(thickness);
        
        from.addOut(newInstance);
        to.addIn(newInstance);
        
        return newInstance;
    }

    /**
     * @return the thickness
     */
    public int getThickness() {
        return thickness;
    }

    /**
     * @param thickness the thickness to set
     */
    public void setThickness(final int thickness) {
        Preconditions.checkArgument(thickness >= 0);
        
        this.thickness = thickness;
    }

    /**
     * @param from2
     * @param to2
     * @param offset
     * @return
     */
    private Segment getSegment() {
        final Rectangle fromBounds = from.getBounds();
        final Rectangle toBounds = to.getBounds();
        
        final Point fromCenter = new Point((int) fromBounds.getCenterX(), (int) fromBounds.getCenterY());
        final Point toCenter = new Point((int) toBounds.getCenterX(), (int) toBounds.getCenterY());
        
        if (fromCenter.equals(toCenter)) {
            return Segment.create(from.getX(), from.getY(), to.getX(), to.getY());
        }
        
        final Segment original = Segment.create(fromCenter, toCenter);
        return original.offset(offset.getValue());
    }

    private ArcUI(final NodeUI from, final NodeUI to, final ArcPropertiesController arcPropertiesController) {
        Preconditions.checkNotNull(from);
        Preconditions.checkNotNull(to);
        Preconditions.checkNotNull(arcPropertiesController);
        Preconditions.checkArgument(!from.equals(to));
        
        this.from = from;
        this.to = to;
        this.arcPropertiesController = arcPropertiesController;
    }

    /**
     * @return the priority
     */
    public final int getPriority() {
        return priority;
    }

    /**
     * @param priority the priority to set
     */
    public final void setPriority(int priority) {
        Preconditions.checkArgument(priority >= 0);
        
        this.priority = priority;
    }

    /**
     * @return the type
     */
    public final ArcType getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public final void setType(ArcType type) {
        Preconditions.checkNotNull(type);
        
        this.type = type;
    }

    /**
     * @return the from
     */
    public final NodeUI getFrom() {
        return from;
    }

    /**
     * @return the to
     */
    public final NodeUI getTo() {
        return to;
    }
    
    @Override
    public boolean contains(final int x, final int y) {
        return getSegment().contains(x, y, this.thickness / 2d);
    }

    @Override
    public void paintComponent(final Graphics graphics) {
        super.paintComponent(graphics);
        
        final Segment segment = getSegment();
        final int xFrom = segment.getFromX();
        final int yFrom = segment.getFromY();
        
        final int xTo = segment.getToX();
        final int yTo = segment.getToY();
        
        final Graphics2D graphics2d = (Graphics2D) graphics;
        graphics2d.setColor(this.type.getColor());
        graphics2d.setStroke(this.type.getStroke());
        
        final double theta = Math.atan2(yTo - yFrom, xTo - xFrom);
        
        final Line2D.Double mainLine = new Line2D.Double(xFrom, yFrom, xTo, yTo);
        graphics2d.draw(mainLine);
        
        final int xCenter = (xFrom + xTo) /2;
        final int yCenter = (yFrom + yTo) /2;
        drawArrowArms(xCenter, yCenter, graphics2d, theta);
        
        graphics.drawString(getName(), xCenter, yCenter);
    }

    /**
     * @param xTip
     * @param yTip
     * @param graphics2d
     * @param arrowTheta
     */
    private void drawArrowArms(final int xTip, final int yTip,
            final Graphics2D graphics2d, final double arrowTheta) {
        graphics2d.translate(xTip, yTip);
        graphics2d.rotate(arrowTheta);
        
        graphics2d.rotate(ARROW_ARM_ANGLE);
        final Line2D.Double leftSideLine = new Line2D.Double(0, 0, ARROW_SIDE_LENGTH, 0);
        graphics2d.draw(leftSideLine);
        graphics2d.rotate(-ARROW_ARM_ANGLE);
        
        graphics2d.rotate(-ARROW_ARM_ANGLE);
        final Line2D.Double rightSideLine = new Line2D.Double(xTip, yTip, xTip, yTip);
        graphics2d.draw(rightSideLine);
        graphics2d.rotate(ARROW_ARM_ANGLE);
        
        graphics2d.rotate(-arrowTheta);
        graphics2d.translate(-xTip, -yTip);
    }
    
    public void jointMoved() {
        repaint();
    }

    /**
     * @return
     */
    public ArcUI reverse() {
        return create(getName(), this.priority, this.type, this.to, this.from, this.offset, this.thickness, this.arcPropertiesController);
    }

    /**
     * 
     */
    public void jointDilated() {
        this.setOffset(Offset.UPPER_OFFSET);
        repaint();
    }
    
    /**
     * @param upperLimit
     */
    private void setOffset(final Offset offset) {
        this.offset = offset;
    }

    public void jointContracted() {
        this.setOffset(Offset.LOWER_OFFSET);
        jointMoved();
    }

    /**
     * 
     */
    public void showProperties() {
        this.arcPropertiesController.displayProperties(getName());
    }
}
