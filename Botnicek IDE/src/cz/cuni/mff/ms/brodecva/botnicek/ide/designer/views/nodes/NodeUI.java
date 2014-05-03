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
package cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.nodes;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.event.MouseInputListener;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.controllers.NetworkController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.arcs.ArcUI;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.utils.DragListener;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.utils.IrregularMouseListener;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.GraphComponent;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public class NodeUI extends GraphComponent {
    private static final Dimension DEFAULT_DIMENSION = new Dimension(24, 24);
    
    private final NetworkController controller;
    
    private String name;
    
    private PositionalType positional;

    private ProceedType proceed;

    private DispatchType dispatch;
    
    private Set<ArcUI> outs = new HashSet<>();
    
    private Set<ArcUI> ins = new HashSet<>();
    
    public static NodeUI create(final String name, final PositionalType positional, final ProceedType proceed, final DispatchType dispatch, final int x, final int y, final NetworkController controller) {
        return create(name, positional, proceed, dispatch, new Point(x, y), DEFAULT_DIMENSION, controller);
    }
    
    public static NodeUI create(final String name, final PositionalType positional, final ProceedType proceed, final DispatchType dispatch, final int x, final int y, final int width, final int height, final NetworkController controller) {
        return create(name, positional, proceed, dispatch, new Point(x, y), new Dimension(width, height), controller);
    }
    
    private static NodeUI create(final String name, final PositionalType positional, final ProceedType proceed, final DispatchType dispatch, final Point location, final Dimension dimension, final NetworkController controller) {
        Preconditions.checkNotNull(name);
        Preconditions.checkNotNull(location);
        Preconditions.checkNotNull(dimension);
        Preconditions.checkArgument(!name.isEmpty());
        Preconditions.checkArgument(location.getX() > 0);
        Preconditions.checkArgument(location.getY() > 0);
        Preconditions.checkArgument(dimension.getHeight() > 0);
        Preconditions.checkArgument(dimension.getWidth() > 0);    
        
        final NodeUI newInstance = new NodeUI(positional, proceed, dispatch, controller);
        newInstance.setName(name);
        newInstance.setBounds(new Rectangle(new Point((int) (location.getX() - dimension.getWidth() / 2), (int) (location.getY() -  dimension.getHeight() / 2)), dimension));
        
        newInstance.addComponentListener(new ComponentAdapter() {
            /* (non-Javadoc)
             * @see java.awt.event.ComponentAdapter#componentMoved(java.awt.event.ComponentEvent)
             */
            @Override
            public void componentMoved(final ComponentEvent e) {
                newInstance.jointMoved();
            }
        });
        
        final MouseInputListener irregularDragListener = IrregularMouseListener.decorate(newInstance, (MouseInputListener) DragListener.create(newInstance));
        newInstance.addMouseListener(irregularDragListener);
        newInstance.addMouseMotionListener(irregularDragListener);
        
        return newInstance;
    }
    
    private NodeUI(final PositionalType positional, final ProceedType proceed, final DispatchType dispatch, final NetworkController controller) {
        super();
        
        Preconditions.checkNotNull(positional);
        Preconditions.checkNotNull(proceed);
        Preconditions.checkNotNull(dispatch);
        Preconditions.checkNotNull(controller);
        Preconditions.checkArgument(!name.isEmpty());
        
        this.controller = controller;
        
        this.positional = positional;
        this.proceed = proceed;
        this.dispatch = dispatch;
    }

    public final void rename(String name) {
        this.controller.changeNode(getName(), name);
    }
    
    public final void renamed(String name) {
        setName(name);
        this.repaint();
    }
    
    public final void moved(final int x, final int y) {
        setLocation(x, y);
        this.invalidate();
    }
    
    public void dragFinished() {
        this.controller.changeNode(getName(), getX(), getY());
    }
    
    @Override
    public boolean contains(final int x, final int y) {
        final int width = this.getWidth();
        assert width == this.getHeight();
        
        final double radius = width / 2d;
        final double centerX = radius;
        final double centerY = radius;
        
        final double xDelta = x - centerX;
        final double yDelta = y - centerY;
        
        return xDelta * xDelta + yDelta * yDelta <= radius * radius;
    }    
    
    @Override
    public void paintComponent(final Graphics graphics) {
        super.paintComponent(graphics);
        
        final int x = this.getX();
        final int y = this.getY();
        final Graphics2D graphics2d = (Graphics2D) graphics;
        
        final Ellipse2D.Double circle = new Ellipse2D.Double(x, y, this.getWidth(), this.getHeight());
        
        graphics2d.setColor(this.positional.getRim());
        graphics2d.setStroke(this.dispatch.getStroke());
        graphics2d.draw(circle);
        
        graphics2d.setColor(this.proceed.getBackground());
        graphics2d.fill(circle);
        
        graphics.drawString(getName(), x, y);
    }

    /**
     * 
     */
    public void toggleProceedType() {
        this.controller.toggleNodeProceedType(this.getName());
    }

    /**
     * 
     */
    public void toggleDispatchType() {
        this.controller.toggleNodeDispatchType(this.getName());
    }
    
    public void removeAllComponentListeners() {
        final ComponentListener[] listeners = getComponentListeners();
        final ComponentListener[] listenersCopy = Arrays.copyOf(listeners, listeners.length);
        
        for (final ComponentListener removed : listenersCopy) {
            this.removeComponentListener(removed);
        }
    }

    /**
     * @param type
     */
    public void changedType(final PositionalType type) {
        Preconditions.checkNotNull(type);
        
        this.positional = type;
        this.repaint();
    }

    /**
     * @param type
     */
    public void changedType(final ProceedType type) {
        Preconditions.checkNotNull(type);
        
        this.proceed = type;
        this.repaint();
    }

    /**
     * @param type
     */
    public void changedType(final DispatchType type) {
        Preconditions.checkNotNull(type);
        
        this.dispatch = type;
        this.repaint();
    }

    /**
     * @param newInstance
     */
    public void addOut(final ArcUI arc) {
        Preconditions.checkNotNull(arc);
        Preconditions.checkArgument(this.equals(arc.getFrom()));
        Preconditions.checkArgument(!this.ins.contains(arc));
        Preconditions.checkArgument(!this.outs.contains(arc));
        
        this.outs.add(arc);
        
        for (final ArcUI opposite : this.ins) {
            if (opposite.equals(arc.reverse())) {
                arc.jointDilated();
                opposite.jointDilated();
                break;
            }
        }
    }

    /**
     * @param newInstance
     */
    public void addIn(final ArcUI arc) {
        Preconditions.checkNotNull(arc);
        Preconditions.checkArgument(this.equals(arc.getTo()));
        Preconditions.checkArgument(!this.outs.contains(arc));
        Preconditions.checkArgument(!this.ins.contains(arc));
        
        this.ins.add(arc);
        
        for (final ArcUI opposite : this.ins) {
            if (opposite.equals(arc.reverse())) {
                arc.jointDilated();
                opposite.jointDilated();
                break;
            }
        }
    }
    
    public void removeOut(final ArcUI arc) {
        Preconditions.checkNotNull(arc);
        Preconditions.checkArgument(this.outs.contains(arc));
        
        this.outs.remove(arc);
        
        for (final ArcUI opposite : this.ins) {
            if (opposite.equals(arc.reverse())) {
                opposite.jointContracted();
                break;
            }
        }
    }
    
    public void removeIn(final ArcUI arc) {
        Preconditions.checkNotNull(arc);
        Preconditions.checkArgument(this.ins.contains(arc));
        
        this.ins.remove(arc);
        
        for (final ArcUI opposite : this.outs) {
            if (opposite.equals(arc.reverse())) {
                opposite.jointContracted();
                break;
            }
        }
    }
    
    private void jointMoved() {
        for (final ArcUI out : this.outs) {
            out.jointMoved();
        }
        
        for (final ArcUI in : this.ins) {
            in.jointMoved();
        }
    }
}
