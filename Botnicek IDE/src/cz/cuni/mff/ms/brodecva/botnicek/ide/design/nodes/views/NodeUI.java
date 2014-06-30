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
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.geom.Ellipse2D;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.event.MouseInputListener;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.ArcUI;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.events.DragFinishedListener;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.controllers.NodesController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.DispatchNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.ExitNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.IsolatedNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.PositionalNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.ProceedNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.implementations.EnterOrderedInputNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.implementations.EnterOrderedProcessingNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.implementations.EnterRandomInputNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.implementations.EnterRandomProcessingNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.implementations.ExitInputNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.implementations.ExitProcessingNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.implementations.InnerOrderedInputNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.implementations.InnerOrderedProcessingNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.implementations.InnerRandomInputNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.implementations.InnerRandomProcessingNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.implementations.IsolatedInputNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.implementations.IsolatedProcessingNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.DragListener;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.GraphComponent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.IrregularMouseListener;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public class NodeUI extends GraphComponent implements NodesView {
    
    private static final Color LABEL_COLOR = Color.BLACK;
    private static final Map<Class<? extends PositionalNode>, PositionalType> TO_POSITIONAL_DEFAULTS;
    private static final Map<Class<? extends ProceedNode>, ProceedType> TO_PROCEED_DEFAULTS;
    private static final Map<Class<? extends DispatchNode>, DispatchType> TO_DISPATCH_DEFAULTS;
    
    private static final Dimension DEFAULT_DIMENSION = new Dimension(24, 24);
    
    private final NodesController controller;
    
    private NormalWord name;
    
    private PositionalType positional;

    private ProceedType proceed;

    private DispatchType dispatch;
    
    private Set<ArcUI> outs = new HashSet<>();
    
    private Set<ArcUI> ins = new HashSet<>();
    
    static {
        final Builder<Class<? extends PositionalNode>, PositionalType> toPositionalDefaultsBuilder = ImmutableMap.builder();
        toPositionalDefaultsBuilder.put(EnterOrderedInputNode.class, PositionalType.ENTER);
        toPositionalDefaultsBuilder.put(EnterOrderedProcessingNode.class, PositionalType.ENTER);
        toPositionalDefaultsBuilder.put(EnterRandomInputNode.class, PositionalType.ENTER);
        toPositionalDefaultsBuilder.put(EnterRandomProcessingNode.class, PositionalType.ENTER);
        toPositionalDefaultsBuilder.put(ExitInputNode.class, PositionalType.EXIT);
        toPositionalDefaultsBuilder.put(ExitProcessingNode.class, PositionalType.EXIT);
        toPositionalDefaultsBuilder.put(InnerOrderedInputNode.class, PositionalType.INNER);
        toPositionalDefaultsBuilder.put(InnerOrderedProcessingNode.class, PositionalType.INNER);
        toPositionalDefaultsBuilder.put(InnerRandomInputNode.class, PositionalType.INNER);
        toPositionalDefaultsBuilder.put(InnerRandomProcessingNode.class, PositionalType.INNER);
        toPositionalDefaultsBuilder.put(IsolatedInputNode.class, PositionalType.ISOLATED);
        toPositionalDefaultsBuilder.put(IsolatedProcessingNode.class, PositionalType.ISOLATED);
        TO_POSITIONAL_DEFAULTS = toPositionalDefaultsBuilder.build();
        
        final Builder<Class<? extends ProceedNode>, ProceedType> toProceedDefaultsBuilder = ImmutableMap.builder();
        toProceedDefaultsBuilder.put(EnterOrderedInputNode.class, ProceedType.INPUT);
        toProceedDefaultsBuilder.put(EnterRandomInputNode.class, ProceedType.INPUT);
        toProceedDefaultsBuilder.put(ExitInputNode.class, ProceedType.INPUT);
        toProceedDefaultsBuilder.put(InnerOrderedInputNode.class, ProceedType.INPUT);
        toProceedDefaultsBuilder.put(InnerRandomInputNode.class, ProceedType.INPUT);
        toProceedDefaultsBuilder.put(IsolatedInputNode.class, ProceedType.INPUT);
        toProceedDefaultsBuilder.put(EnterOrderedProcessingNode.class, ProceedType.PROCESSING);
        toProceedDefaultsBuilder.put(EnterRandomProcessingNode.class, ProceedType.PROCESSING);
        toProceedDefaultsBuilder.put(ExitProcessingNode.class, ProceedType.PROCESSING);
        toProceedDefaultsBuilder.put(InnerOrderedProcessingNode.class, ProceedType.PROCESSING);
        toProceedDefaultsBuilder.put(InnerRandomProcessingNode.class, ProceedType.PROCESSING);
        toProceedDefaultsBuilder.put(IsolatedProcessingNode.class, ProceedType.PROCESSING);
        TO_PROCEED_DEFAULTS = toProceedDefaultsBuilder.build();
        
        final Builder<Class<? extends DispatchNode>, DispatchType> toDispatchDefaultsBuilder = ImmutableMap.builder();
        toDispatchDefaultsBuilder.put(EnterOrderedInputNode.class, DispatchType.ORDERED);
        toDispatchDefaultsBuilder.put(EnterOrderedProcessingNode.class, DispatchType.ORDERED);
        toDispatchDefaultsBuilder.put(InnerOrderedInputNode.class, DispatchType.ORDERED);
        toDispatchDefaultsBuilder.put(InnerOrderedProcessingNode.class, DispatchType.ORDERED);
        toDispatchDefaultsBuilder.put(EnterRandomInputNode.class, DispatchType.RANDOM);
        toDispatchDefaultsBuilder.put(EnterRandomProcessingNode.class, DispatchType.RANDOM);
        toDispatchDefaultsBuilder.put(InnerRandomInputNode.class, DispatchType.RANDOM);
        toDispatchDefaultsBuilder.put(InnerRandomProcessingNode.class, DispatchType.RANDOM);
        TO_DISPATCH_DEFAULTS = toDispatchDefaultsBuilder.build();
    }
    
    public static NodeUI create(final Node node, final NodesController controller) {
        return create(node, DEFAULT_DIMENSION, controller);
    }
    
    public static NodeUI create(final Node node, final Dimension dimension, final NodesController controller) {
        return create(node.getName(), toPositional(node), toProceed(node), toDispatch(node), new Point(node.getX(), node.getY()), DEFAULT_DIMENSION, controller);
    }
    
    private static NodeUI create(final NormalWord name, final PositionalType positional, final ProceedType proceed, final DispatchType dispatch, final Point location, final Dimension dimension, final NodesController controller) {
        Preconditions.checkNotNull(name);
        Preconditions.checkNotNull(positional);
        Preconditions.checkNotNull(proceed);
        Preconditions.checkNotNull(dispatch);
        Preconditions.checkNotNull(location);
        Preconditions.checkNotNull(dimension);
        Preconditions.checkNotNull(controller);
        Preconditions.checkArgument(location.getX() > 0);
        Preconditions.checkArgument(location.getY() > 0);
        Preconditions.checkArgument(dimension.getHeight() > 0);
        Preconditions.checkArgument(dimension.getWidth() > 0);    
        
        final NodeUI newInstance = new NodeUI(name, positional, proceed, dispatch, controller);
        
        final Rectangle bounds = new Rectangle(new Point((int) (location.getX() - dimension.getWidth() / 2), (int) (location.getY() -  dimension.getHeight() / 2)), dimension);
        newInstance.setBounds(bounds);
        
        newInstance.addComponentListener(new ComponentAdapter() {
            /* (non-Javadoc)
             * @see java.awt.event.ComponentAdapter#componentMoved(java.awt.event.ComponentEvent)
             */
            @Override
            public void componentMoved(final ComponentEvent e) {
                newInstance.jointMoved();
            }
        });
        
        final DragListener dragListener = DragListener.create(newInstance);
        dragListener.addDragFinishedListener(new DragFinishedListener() {
            
            @Override
            public void finished() {
                controller.changeNode(name, newInstance.getX(), newInstance.getY());
            }
        });
        
        final MouseInputListener irregularDragListener = IrregularMouseListener.decorate(newInstance, (MouseInputListener) dragListener);
        newInstance.addMouseListener(irregularDragListener);
        newInstance.addMouseMotionListener(irregularDragListener);
        
        return newInstance;
    }
    
    private static PositionalType toPositional(final Node node) {
        final PositionalType type = TO_POSITIONAL_DEFAULTS.get(node.getClass());
        assert type != null;
        
        return type;
    }
    
    private static ProceedType toProceed(final Node node) {
        final ProceedType type = TO_PROCEED_DEFAULTS.get(node.getClass());
        assert type != null;
        
        return type;
    }
    
    private static DispatchType toDispatch(final Node node) {
        final DispatchType type = TO_DISPATCH_DEFAULTS.get(node.getClass());
        
        final DispatchType usedType;
        if (type == null) {
            assert node instanceof ExitNode || node instanceof IsolatedNode;
            
            usedType = DispatchType.DEFAULT;
        } else {
            usedType = type;
        }        
        
        return usedType;
    }
    
    private NodeUI(final NormalWord name, final PositionalType positional, final ProceedType proceed, final DispatchType dispatch, final NodesController controller) {
        super();
        
        Preconditions.checkNotNull(name);
        Preconditions.checkNotNull(positional);
        Preconditions.checkNotNull(proceed);
        Preconditions.checkNotNull(dispatch);
        Preconditions.checkNotNull(controller);
        
        this.controller = controller;
        
        this.positional = positional;
        this.proceed = proceed;
        this.dispatch = dispatch;
        
        setNodeName(name);
    }

    public final void rename(final NormalWord name) {
        Preconditions.checkNotNull(name);
        
        this.controller.rename(this.name, name);
    }
    
    public final void nodeRenamed(Node original, final Node newVersion) {
        Preconditions.checkNotNull(newVersion);
        
        setNodeName(newVersion.getName());
        this.repaint();
    }
    
    public void setNodeName(final NormalWord name) {
        Preconditions.checkNotNull(name);
        
        this.name = name;
        super.setName(name.getText());
    }
    
    public NormalWord getNodeName() {
        return this.name;
    }
    
    public final void nodeMoved(Node original, final Node newVersion) {
        Preconditions.checkNotNull(newVersion);
        
        setLocation(newVersion.getX(), newVersion.getY());
        this.invalidate();
    }
    
    public void dragFinished() {
        this.controller.changeNode(this.name, getX(), getY());
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
        
        paintCircle((Graphics2D) graphics);
        paintLabel(graphics);
    }

    /**
     * @param graphics2d
     */
    private void paintCircle(final Graphics2D graphics2d) {
        final Ellipse2D.Double circle = new Ellipse2D.Double(0, 0, this.getWidth(), this.getHeight());
        
        graphics2d.setColor(this.positional.getRim());
        graphics2d.setStroke(this.dispatch.getStroke());
        graphics2d.draw(circle);
        
        graphics2d.setColor(this.proceed.getBackground());
        graphics2d.fill(circle);
    }

    /**
     * @param graphics
     */
    private void paintLabel(final Graphics graphics) {
        final String nameString = getName();
        
        final Font font = this.getFont();
        graphics.setFont(font);
        graphics.setColor(LABEL_COLOR);
        final FontMetrics fontMetrics = graphics.getFontMetrics();
        final int nameStringWidth = fontMetrics.stringWidth(nameString);
        final int stringHeight = fontMetrics.getHeight();
        
        final int width = this.getWidth();
        final int height = this.getHeight();
        
        graphics.drawString(nameString, Math.max(0, (this.getWidth() - nameStringWidth) / 2), Math.max(0, (height + stringHeight) / 2));
    }

    /**
     * 
     */
    public void toggleProceedType() {
        this.controller.toggleNodeProceedType(this.name);
    }

    /**
     * 
     */
    public void toggleDispatchType() {
        this.controller.toggleNodeDispatchType(this.name);
    }
    
    public void removeAllComponentListeners() {
        final ComponentListener[] listeners = getComponentListeners();
        final ComponentListener[] listenersCopy = Arrays.copyOf(listeners, listeners.length);
        
        for (final ComponentListener removed : listenersCopy) {
            this.removeComponentListener(removed);
        }
    }

    public void nodeRetyped(Node original, final Node newVersion) {
        Preconditions.checkNotNull(newVersion);
        
        this.positional = toPositional(newVersion);
        this.proceed = toProceed(newVersion);
        this.dispatch = toDispatch(newVersion);
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
            if (areComplementary(arc, opposite)) {
                arc.jointDilated();
                opposite.jointDilated();
                break;
            }
        }
    }
    
    private static boolean areComplementary(final ArcUI first, final ArcUI second) {
        return first.getFrom().equals(second.getTo()) && first.getTo().equals(second.getFrom());
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
            if (areComplementary(arc, opposite)) {
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
            if (areComplementary(arc, opposite)) {
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
            if (areComplementary(arc, opposite)) {
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
