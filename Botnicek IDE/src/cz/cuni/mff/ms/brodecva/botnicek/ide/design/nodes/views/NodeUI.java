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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.Icon;
import javax.swing.JOptionPane;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.graph.ArcUI;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.views.EditMode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.views.Editable;
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
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Intended;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.Presence;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.resources.ExceptionLocalizer;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.resources.UiLocalizer;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.Components;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.components.FramedComponent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.graphics.Rendering;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.listeners.DragFinishedListener;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.listeners.DragListener;

/**
 * Komponenta reprezentující uzel v grafu sítě.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class NodeUI extends FramedComponent implements
        DragFinishedListener, Editable {

    private static final long serialVersionUID = 1L;

    private static final Color LABEL_COLOR = Color.BLACK;
    private static final Map<Class<? extends PositionalNode>, PositionalType> TO_POSITIONAL_DEFAULTS;
    private static final Map<Class<? extends ProceedNode>, ProceedType> TO_PROCEED_DEFAULTS;
    private static final Map<Class<? extends DispatchNode>, DispatchType> TO_DISPATCH_DEFAULTS;

    private static final int SIZE = 24;
    private static final Dimension DEFAULT_DIMENSION =
            new Dimension(SIZE, SIZE);

    private static boolean areComplementary(final ArcUI first,
            final ArcUI second) {
        return first.getFrom().equals(second.getTo())
                && first.getTo().equals(second.getFrom());
    }

    /**
     * Vytvoří komponentu hrany a zaregistruje na ní posluchače tak, aby po
     * poklepání na ni byla realizována funkcionalita specifikovaná v popis
     * grafu sítě (přejmenovávání, změna typů uzlů, odebírání).
     * 
     * @param node
     *            model uzlu
     * @param dimension
     *            rozměry
     * @param controller
     *            řadič uzlů grafu
     * @return komponenta uzlu v zobrazeném grafu
     */
    public static NodeUI create(final Node node, final Dimension dimension,
            final NodesController controller) {
        return create(node.getName(), toPositional(node), toProceed(node),
                toDispatch(node), new Point(node.getX(), node.getY()),
                DEFAULT_DIMENSION, controller);
    }

    private static NodeUI create(final NormalWord name,
            final PositionalType positional, final ProceedType proceed,
            final DispatchType dispatch, final Point centerLocation,
            final Dimension dimension, final NodesController controller) {
        Preconditions.checkNotNull(name);
        Preconditions.checkNotNull(positional);
        Preconditions.checkNotNull(proceed);
        Preconditions.checkNotNull(dispatch);
        Preconditions.checkNotNull(centerLocation);
        Preconditions.checkNotNull(dimension);
        Preconditions.checkNotNull(controller);

        final Point centerLocationCopy = new Point(centerLocation);
        final Dimension dimensionCopy = new Dimension(dimension);
        Preconditions.checkArgument(centerLocationCopy.x >= 0);
        Preconditions.checkArgument(centerLocationCopy.y >= 0);
        Preconditions.checkArgument(dimensionCopy.height > 0);
        Preconditions.checkArgument(dimensionCopy.width > 0);

        final NodeUI newInstance =
                new NodeUI(name, positional, proceed, dispatch, controller);

        final Point cornerLocation =
                getCornerLocation(centerLocationCopy, dimensionCopy);
        final Rectangle bounds = new Rectangle(cornerLocation, dimensionCopy);
        newInstance.setFramedBounds(bounds);

        newInstance.addComponentListener(new ComponentAdapter() {
            /*
             * (non-Javadoc)
             * 
             * @see
             * java.awt.event.ComponentAdapter#componentMoved(java.awt.event
             * .ComponentEvent)
             */
            @Override
            public void componentMoved(final ComponentEvent e) {
                newInstance.jointMoved();
            }
        });

        final DragListener dragListener = DragListener.create(newInstance);
        dragListener.addDragFinishedListener(newInstance);

        newInstance.addMouseListener(dragListener);
        newInstance.addMouseMotionListener(dragListener);

        newInstance.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(final MouseEvent e) {
                if (e.getClickCount() != 2) {
                    return;
                }

                if (newInstance.editMode == EditMode.REMOVE || (newInstance.editMode == EditMode.DESIGN && e.isControlDown() && e.isShiftDown())) {
                    if (!newInstance.removalConfirmed()) {
                        return;
                    }

                    newInstance.removeNode();
                } else if (newInstance.editMode == EditMode.INTERACTIVITY || (newInstance.editMode == EditMode.DESIGN && e.isControlDown())) {
                    newInstance.toggleProceedType();
                } else if (newInstance.editMode == EditMode.DISPATCH_ORDER || (newInstance.editMode == EditMode.DESIGN && e.isAltDown())) {
                    newInstance.toggleDispatchType();
                } else if (newInstance.editMode == EditMode.RENAME || (newInstance.editMode == EditMode.DESIGN && e.isShiftDown())) {
                    newInstance.attemptToRename();
                }
            }
        });

        return newInstance;
    }

    private static Point getCenterLocation(final Point cornerLocation,
            final Dimension dimension) {
        return new Point(cornerLocation.x + dimension.width / 2,
                cornerLocation.y + dimension.height / 2);
    }

    private static Point getCornerLocation(final Point centerLocation,
            final Dimension dimension) {
        return new Point(centerLocation.x - dimension.width / 2,
                centerLocation.y - dimension.height / 2);
    }

    private static DispatchType toDispatch(final Node node) {
        final DispatchType type = TO_DISPATCH_DEFAULTS.get(node.getClass());

        final DispatchType usedType;
        if (Presence.isAbsent(type)) {
            assert node instanceof ExitNode || node instanceof IsolatedNode;

            usedType = DispatchType.DEFAULT;
        } else {
            usedType = type;
        }

        return usedType;
    }

    private static PositionalType toPositional(final Node node) {
        final PositionalType type = TO_POSITIONAL_DEFAULTS.get(node.getClass());
        assert Presence.isPresent(type);

        return type;
    }

    private static ProceedType toProceed(final Node node) {
        final ProceedType type = TO_PROCEED_DEFAULTS.get(node.getClass());
        assert Presence.isPresent(type);

        return type;
    }

    private final NodesController nodesController;

    private NormalWord name;

    private PositionalType positional;

    private ProceedType proceed;

    private DispatchType dispatch;

    private final Set<ArcUI> outs = new HashSet<>();

    private final Set<ArcUI> ins = new HashSet<>();

    private EditMode editMode = EditMode.DESIGN;

    static {
        final Builder<Class<? extends PositionalNode>, PositionalType> toPositionalDefaultsBuilder =
                ImmutableMap.builder();
        toPositionalDefaultsBuilder.put(EnterOrderedInputNode.class,
                PositionalType.ENTER);
        toPositionalDefaultsBuilder.put(EnterOrderedProcessingNode.class,
                PositionalType.ENTER);
        toPositionalDefaultsBuilder.put(EnterRandomInputNode.class,
                PositionalType.ENTER);
        toPositionalDefaultsBuilder.put(EnterRandomProcessingNode.class,
                PositionalType.ENTER);
        toPositionalDefaultsBuilder.put(ExitInputNode.class,
                PositionalType.EXIT);
        toPositionalDefaultsBuilder.put(ExitProcessingNode.class,
                PositionalType.EXIT);
        toPositionalDefaultsBuilder.put(InnerOrderedInputNode.class,
                PositionalType.INNER);
        toPositionalDefaultsBuilder.put(InnerOrderedProcessingNode.class,
                PositionalType.INNER);
        toPositionalDefaultsBuilder.put(InnerRandomInputNode.class,
                PositionalType.INNER);
        toPositionalDefaultsBuilder.put(InnerRandomProcessingNode.class,
                PositionalType.INNER);
        toPositionalDefaultsBuilder.put(IsolatedInputNode.class,
                PositionalType.ISOLATED);
        toPositionalDefaultsBuilder.put(IsolatedProcessingNode.class,
                PositionalType.ISOLATED);
        TO_POSITIONAL_DEFAULTS = toPositionalDefaultsBuilder.build();

        final Builder<Class<? extends ProceedNode>, ProceedType> toProceedDefaultsBuilder =
                ImmutableMap.builder();
        toProceedDefaultsBuilder.put(EnterOrderedInputNode.class,
                ProceedType.INPUT);
        toProceedDefaultsBuilder.put(EnterRandomInputNode.class,
                ProceedType.INPUT);
        toProceedDefaultsBuilder.put(ExitInputNode.class, ProceedType.INPUT);
        toProceedDefaultsBuilder.put(InnerOrderedInputNode.class,
                ProceedType.INPUT);
        toProceedDefaultsBuilder.put(InnerRandomInputNode.class,
                ProceedType.INPUT);
        toProceedDefaultsBuilder
                .put(IsolatedInputNode.class, ProceedType.INPUT);
        toProceedDefaultsBuilder.put(EnterOrderedProcessingNode.class,
                ProceedType.PROCESSING);
        toProceedDefaultsBuilder.put(EnterRandomProcessingNode.class,
                ProceedType.PROCESSING);
        toProceedDefaultsBuilder.put(ExitProcessingNode.class,
                ProceedType.PROCESSING);
        toProceedDefaultsBuilder.put(InnerOrderedProcessingNode.class,
                ProceedType.PROCESSING);
        toProceedDefaultsBuilder.put(InnerRandomProcessingNode.class,
                ProceedType.PROCESSING);
        toProceedDefaultsBuilder.put(IsolatedProcessingNode.class,
                ProceedType.PROCESSING);
        TO_PROCEED_DEFAULTS = toProceedDefaultsBuilder.build();

        final Builder<Class<? extends DispatchNode>, DispatchType> toDispatchDefaultsBuilder =
                ImmutableMap.builder();
        toDispatchDefaultsBuilder.put(EnterOrderedInputNode.class,
                DispatchType.ORDERED);
        toDispatchDefaultsBuilder.put(EnterOrderedProcessingNode.class,
                DispatchType.ORDERED);
        toDispatchDefaultsBuilder.put(InnerOrderedInputNode.class,
                DispatchType.ORDERED);
        toDispatchDefaultsBuilder.put(InnerOrderedProcessingNode.class,
                DispatchType.ORDERED);
        toDispatchDefaultsBuilder.put(EnterRandomInputNode.class,
                DispatchType.RANDOM);
        toDispatchDefaultsBuilder.put(EnterRandomProcessingNode.class,
                DispatchType.RANDOM);
        toDispatchDefaultsBuilder.put(InnerRandomInputNode.class,
                DispatchType.RANDOM);
        toDispatchDefaultsBuilder.put(InnerRandomProcessingNode.class,
                DispatchType.RANDOM);
        TO_DISPATCH_DEFAULTS = toDispatchDefaultsBuilder.build();
    }

    /**
     * Vytvoří komponentu hrany a zaregistruje na ní posluchače tak, aby po
     * poklepání na ni fungovala funkcionalita specifikovaná v popis grafu sítě
     * (přejmenovávání, změna typů uzlů, odebírání).
     * 
     * @param node
     *            model uzlu
     * @param controller
     *            řadič uzlů grafu
     * @return komponenta uzlu v zobrazeném grafu
     */
    public static NodeUI create(final Node node,
            final NodesController controller) {
        return create(node, DEFAULT_DIMENSION, controller);
    }

    private NodeUI(final NormalWord name, final PositionalType positional,
            final ProceedType proceed, final DispatchType dispatch,
            final NodesController controller) {
        super();

        Preconditions.checkNotNull(name);
        Preconditions.checkNotNull(positional);
        Preconditions.checkNotNull(proceed);
        Preconditions.checkNotNull(dispatch);
        Preconditions.checkNotNull(controller);

        this.nodesController = controller;

        this.positional = positional;
        this.proceed = proceed;
        this.dispatch = dispatch;

        setNodeName(name);
    }

    /**
     * Přidá vstupní hranu k uzlu, v případě že k ní existuje protisměrná,
     * zpraví je o nutnosti vytvoření mezery mezi nimi.
     * 
     * @param arc
     *            komponenta hrany
     */
    public void addIn(final ArcUI arc) {
        Preconditions.checkNotNull(arc);
        Preconditions.checkArgument(equals(arc.getTo()));
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

    /**
     * Přidá výstupní hranu k uzlu, v případě že k ní existuje protisměrná,
     * zpraví je o nutnosti vytvoření mezery mezi nimi.
     * 
     * @param arc
     *            komponenta hrany
     */
    public void addOut(final ArcUI arc) {
        Preconditions.checkNotNull(arc);
        Preconditions.checkArgument(equals(arc.getFrom()));
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

    private void attemptToRename() {
        final Object newNameInput =
                JOptionPane.showInputDialog(this,
                        UiLocalizer.print("NODE_RENAME_MESSAGE"),
                        UiLocalizer.print("NODE_RENAME_TITLE_CONTENT"),
                        JOptionPane.PLAIN_MESSAGE,
                        Intended.<Icon> nullReference(), Intended.arrayNull(),
                        getName());
        if (Components.hasUserCanceledInput(newNameInput)) {
            return;
        }

        try {
            rename(newNameInput.toString());
        } catch (final IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    ExceptionLocalizer.print("NodeRenameError") + " "
                            + ex.getMessage(),
                    UiLocalizer.print("NodeRenameErrorTitle"),
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.JComponent#contains(int, int)
     */
    @Override
    public boolean contains(final int x, final int y) {
        final double radius = getContentWidth() / 2d;
        final double centerX = getFrameWidth() + radius;
        final double centerY = getFrameHeight() + radius;

        final double xDelta = x - centerX;
        final double yDelta = y - centerY;

        return xDelta * xDelta + yDelta * yDelta <= radius * radius;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.events.
     * DragFinishedListener#finished()
     */
    @Override
    public void finished() {
        final Dimension parentDimension = getParent().getSize();

        final Point contentLocation = new Point(getContentX(), getContentY());
        final Dimension contentDimension =
                new Dimension(getContentWidth(), getContentHeight());

        final Point centerLocation =
                getCenterLocation(contentLocation, contentDimension);

        final int minimumContentX = 0;
        final int minimumContentY = 0;
        final int maximumContentX = parentDimension.width;
        final int maximumContentY = parentDimension.height;

        this.nodesController.changeNode(
                getNodeName(),
                Math.min(maximumContentX,
                        Math.max(minimumContentX, centerLocation.x)),
                Math.min(maximumContentY,
                        Math.max(minimumContentY, centerLocation.y)));
    }

    /**
     * Vrátí název uzlu.
     * 
     * @return název uzlu
     */
    public NormalWord getNodeName() {
        return this.name;
    }

    private void jointMoved() {
        for (final ArcUI out : this.outs) {
            out.jointMoved();
        }

        for (final ArcUI in : this.ins) {
            in.jointMoved();
        }
    }

    /**
     * Posune komponentu podle nové verze.
     * 
     * @param newVersion
     *            nová verze
     */
    public final void nodeMoved(final Node newVersion) {
        Preconditions.checkNotNull(newVersion);

        final Point cornerLocation =
                getCornerLocation(
                        new Point(newVersion.getX(), newVersion.getY()),
                        new Dimension(getContentWidth(), getContentHeight()));

        setContentLocation(cornerLocation.x, cornerLocation.y);
        invalidate();
    }

    /**
     * Změní zobrazení názvu uzlu.
     * 
     * @param newVersion
     *            nová verze uzlu
     */
    public void nodeRenamed(final Node newVersion) {
        Preconditions.checkNotNull(newVersion);

        setNodeName(newVersion.getName());
        this.repaint();
    }

    /**
     * Změní zobrazený typ uzlu.
     * 
     * @param newVersion
     *            nová verze uzlu
     */
    public void nodeRetyped(final Node newVersion) {
        Preconditions.checkNotNull(newVersion);

        this.positional = toPositional(newVersion);
        this.proceed = toProceed(newVersion);
        this.dispatch = toDispatch(newVersion);
        this.repaint();
    }

    private void paintCircle(final Graphics2D graphics2d) {
        final Ellipse2D.Double circle =
                new Ellipse2D.Double(getFrameWidth(), getFrameHeight(),
                        getContentWidth(), getContentHeight());

        graphics2d.setColor(this.positional.getRim());
        graphics2d.setStroke(this.dispatch.getStroke());
        graphics2d.draw(circle);

        graphics2d.setColor(this.proceed.getBackground());
        graphics2d.fill(circle);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    @Override
    public void paintComponent(final Graphics graphics) {
        super.paintComponent(graphics);

        final Graphics2D graphics2d = (Graphics2D) graphics;
        Rendering.preset(graphics2d);

        paintCircle(graphics2d);
        paintLabel(graphics2d);
    }

    private void paintLabel(final Graphics2D graphics2d) {
        final String nameString = getNodeName().getText();

        final Font font = getFont();
        graphics2d.setFont(font);
        graphics2d.setColor(LABEL_COLOR);
        final FontMetrics fontMetrics = graphics2d.getFontMetrics();
        final int nameStringWidth = fontMetrics.stringWidth(nameString);
        final int stringHeight = fontMetrics.getHeight();

        final int width = getWidth();
        final int height = getHeight();

        graphics2d.drawString(nameString,
                Math.max(0, (width - nameStringWidth) / 2),
                Math.max(0, (height + stringHeight) / 2));
    }

    private boolean removalConfirmed() {
        final int removalConfirmed =
                JOptionPane
                        .showConfirmDialog(this,
                                UiLocalizer.print("NODE_REMOVE_MESSAGE"),
                                UiLocalizer.print("NODE_REMOVE_TITLE_CONTENT"),
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.QUESTION_MESSAGE);
        if (removalConfirmed != JOptionPane.YES_OPTION) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Odebere vstupní hranu uzlu, v případě že k ní existuje protisměrná,
     * zpraví ji o nutnosti odstranění posunu kvůli přehlednosti.
     * 
     * @param arc
     *            komponenta hrany
     */
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

    private void removeNode() {
        this.nodesController.removeNode(this.name);
    }

    /**
     * Odebere výstupní hranu uzlu, v případě že k ní existuje protisměrná,
     * zpraví ji o nutnosti odstranění posunu kvůli přehlednosti.
     * 
     * @param arc
     *            komponenta hrany
     */
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

    private void rename(final String name) {
        Preconditions.checkNotNull(name);

        this.nodesController.rename(this.name, name);
    }

    private void setNodeName(final NormalWord name) {
        Preconditions.checkNotNull(name);

        this.name = name;
        super.setName(name.getText());
    }

    private void toggleDispatchType() {
        this.nodesController.toggleNodeDispatchType(this.name);
    }

    private void toggleProceedType() {
        this.nodesController.toggleNodeProceedType(this.name);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.views.Editable#setEditMode(cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.views.EditMode)
     */
    @Override
    public void setEditMode(final EditMode mode) {
        Preconditions.checkNotNull(mode);
        
        this.editMode  = mode;
    }
}
