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
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.util.Map;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.MixedPattern;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.SimplePattern;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.controllers.ArcPropertiesController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.CodeTestArc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.PatternArc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.PredicateTestArc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.RecurentArc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.TransitionArc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.EnterNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.views.NodeUI;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.types.Code;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.graphics.Segment;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.GraphComponent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.IrregularMouseListener;


/**
 * @author Václav Brodec
 * @version 1.0
 */
public final class ArcUI extends GraphComponent implements ArcView {
    
    private static final Map<Class<? extends Arc>, ArcType> TO_ARC_TYPE_DEFAULTS;
    
    static {        
        final Builder<Class<? extends Arc>, ArcType> toArcTypeDefaultsBuilder = ImmutableMap.builder();
        toArcTypeDefaultsBuilder.put(PatternArc.class, ArcType.PATTERN);
        toArcTypeDefaultsBuilder.put(PredicateTestArc.class, ArcType.PREDICATE_TEST);
        toArcTypeDefaultsBuilder.put(CodeTestArc.class, ArcType.CODE_TEST);
        toArcTypeDefaultsBuilder.put(TransitionArc.class, ArcType.TRANSITION);
        toArcTypeDefaultsBuilder.put(RecurentArc.class, ArcType.RECURENT);
        TO_ARC_TYPE_DEFAULTS = toArcTypeDefaultsBuilder.build();
    }
    
    private static final int ARROW_SIDE_LENGTH = 10;
    private static final double ARROW_ARM_ANGLE = Math.toRadians(30);
    private static final int DEFAULT_THICKNESS = 6;
    
    private final ArcPropertiesController arcPropertiesController;
    
    private final NodeUI from;
    private final NodeUI to;
    
    private Offset offset;
    private int thickness;
    private NormalWord name;
    private int priority;
    private ArcType type;
    
    public static ArcUI create(final Arc arc, final NodeUI from, final NodeUI to, final ArcPropertiesController arcPropertiesController) {
        return create(arc.getName(), arc.getPriority(), toType(arc), from, to, arcPropertiesController);
    }
    
    public static ArcUI create(final NormalWord name, final int priority, final ArcType type, final NodeUI from, final NodeUI to, final ArcPropertiesController arcPropertiesController) {
        return create(name, priority, type, from, to, Offset.LOWER_OFFSET, DEFAULT_THICKNESS, arcPropertiesController);
    }
    
    private static ArcUI create(final NormalWord name, final int priority, final ArcType type, final NodeUI from, final NodeUI to, final Offset offset, final int thickness, final ArcPropertiesController arcPropertiesController) {
        Preconditions.checkNotNull(type);
        Preconditions.checkNotNull(name);
        
        final ArcUI newInstance = new ArcUI(from, to, arcPropertiesController);
        newInstance.setArcName(name);
        
        newInstance.setPriority(priority);
        newInstance.setType(type);
        newInstance.setOffset(offset);
        newInstance.setThickness(thickness);
        
        newInstance.setFramedBounds(newInstance.getSegment().toBounds());
        
        from.addOut(newInstance);
        to.addIn(newInstance);
        
        newInstance.addMouseListener(IrregularMouseListener.decorate(newInstance, new MouseAdapter() {
            
            public void mouseClicked(final MouseEvent e) {
                if (e.getClickCount() != 2) {
                    return;
                }
                
                newInstance.showProperties();
            }
        }));
        
        newInstance.setBackground(Color.PINK);
        
        return newInstance;
    }
    
    private static ArcType toType(final Arc arc) {
        return toType(arc.getClass());
    }
    
    private static ArcType toType(final Class<? extends Arc> arcClass) {
        final ArcType type = TO_ARC_TYPE_DEFAULTS.get(arcClass);
        Preconditions.checkNotNull(type);
        
        return type;
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
        this.repaint();
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
        
        final Graphics2D graphics2d = (Graphics2D) graphics;
        graphics2d.setColor(this.type.getColor());
        graphics2d.setStroke(this.type.getStroke());
        
        final Segment segment = getSegment();
        
        final int xFrom = segment.getFromX();
        final int yFrom = segment.getFromY();
        
        final int xTo = segment.getToX();
        final int yTo = segment.getToY();
        
        final int xOrigin = getX();
        final int yOrigin = getY();
        
        final int xFromOrigin = xFrom - xOrigin;
        final int xToOrigin = xTo - xOrigin;
        final int yFromOrigin = yFrom - yOrigin;
        final int yToOrigin = yTo - yOrigin;
        
        final Line2D.Double mainLine = new Line2D.Double(xFromOrigin, yFromOrigin, xToOrigin, yToOrigin);
        graphics2d.draw(mainLine);
        
        final double theta = Math.atan2(yTo - yFrom, xTo - xFrom);
        final int xCenter = (xFromOrigin + xToOrigin) /2;
        final int yCenter = (yFromOrigin + yToOrigin) /2;
        drawArrowArms(xCenter, yCenter, graphics2d, theta);
        
        graphics.drawString(String.format("%s (%s)", this.name.getText(), getPriority()), xCenter, yCenter);
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
        setFramedBounds(getSegment().toBounds());
        repaint();
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
    
    public NormalWord getArcName() {
        return this.name;
    }
    
    public void setArcName(final NormalWord name) {
        Preconditions.checkNotNull(name);
        
        this.name = name;
    }

    /**
     * 
     */
    public void showProperties() {
        this.arcPropertiesController.displayProperties(this.name);
    }

    @Override
    public void updatedName(final NormalWord name) {
        Preconditions.checkNotNull(name);
        
        this.setArcName(name);
        repaint();
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.ArcView#updatedPriority(int)
     */
    @Override
    public void updatedPriority(int priority) {
        this.priority = priority;
        
        repaint();
    }
    

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.ArcView#updatedCode(java.lang.String)
     */
    @Override
    public void updatedCode(Code code) {
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.ArcView#updatedPrepare(java.lang.String)
     */
    @Override
    public void updatedPrepare(Code code) {
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.ArcView#updatedTested(java.lang.String)
     */
    @Override
    public void updatedTested(Code code) {
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.ArcView#updatedValue(java.lang.String)
     */
    @Override
    public void updatedValue(SimplePattern value) {
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.ArcView#updatedPredicate(java.lang.String)
     */
    @Override
    public void updatedPredicate(NormalWord name) {
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.ArcView#updatedTarget(java.lang.String)
     */
    @Override
    public void updatedTarget(EnterNode target) {
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.ArcView#updatedPattern(java.lang.String)
     */
    @Override
    public void updatedPattern(MixedPattern pattern) {
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.ArcView#updatedThat(java.lang.String)
     */
    @Override
    public void updatedThat(MixedPattern that) {
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.ArcView#updatedType(java.lang.Class)
     */
    @Override
    public void updatedType(Class<? extends Arc> arcClass) {
        Preconditions.checkNotNull(arcClass);
        
        this.setType(toType(arcClass));
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.ArcView#removed()
     */
    @Override
    public void removed() {
    }
}
