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
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.graph;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.controllers.ArcPropertiesDisplayController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.controllers.ArcsController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.CodeTestArc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.PatternArc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.PredicateTestArc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.RecurentArc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.TransitionArc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.views.EditMode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.views.Editable;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.views.NodeUI;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.types.Priority;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.resources.UiLocalizer;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.components.FramedComponent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.graphics.Rendering;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.graphics.Segment;

/**
 * Komponenta reprezentující hranu v grafu sítě.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class ArcUI extends FramedComponent implements Editable {

    private static final long serialVersionUID = 1L;

    private static final Map<Class<? extends Arc>, ArcType> TO_ARC_TYPE_DEFAULTS;

    static {
        final Builder<Class<? extends Arc>, ArcType> toArcTypeDefaultsBuilder =
                ImmutableMap.builder();
        toArcTypeDefaultsBuilder.put(PatternArc.class, ArcType.PATTERN);
        toArcTypeDefaultsBuilder.put(PredicateTestArc.class,
                ArcType.PREDICATE_TEST);
        toArcTypeDefaultsBuilder.put(CodeTestArc.class, ArcType.CODE_TEST);
        toArcTypeDefaultsBuilder.put(TransitionArc.class, ArcType.TRANSITION);
        toArcTypeDefaultsBuilder.put(RecurentArc.class, ArcType.RECURENT);
        TO_ARC_TYPE_DEFAULTS = toArcTypeDefaultsBuilder.build();
    }

    private static final int ARROW_SIDE_LENGTH = 10;
    private static final double ARROW_ARM_ANGLE = Math.toRadians(150);
    private static final int DEFAULT_THICKNESS = 10;

    /**
     * Vytvoří komponentu hrany a zaregistruje na ní posluchače tak, aby po
     * poklepání v jejím blízkém okolí byl zaslán požadavek řadič na zobrazení
     * jejích vlastností.
     * 
     * @param arc
     *            model hrany
     * @param from
     *            reprezentace výchozího uzlu
     * @param to
     *            reprezentace cílového uzlu
     * @param arcsController
     *            řadič pro odstranění hrany
     * @param arcPropertiesController
     *            řadič pro zobrazení vlastností hrany
     * @return komponenta hrany v zobrazeném grafu
     */
    public static ArcUI create(final Arc arc, final NodeUI from,
            final NodeUI to, final ArcsController arcsController,
            final ArcPropertiesDisplayController arcPropertiesController) {
        return create(arc.getName(), arc.getPriority(), toType(arc), from, to,
                arcsController, arcPropertiesController);
    }

    private static ArcUI create(final NormalWord name, final Priority priority,
            final ArcType type, final NodeUI from, final NodeUI to,
            final ArcsController arcsController,
            final ArcPropertiesDisplayController arcPropertiesController) {
        return create(name, priority, type, from, to, Offset.NO_OFFSET,
                DEFAULT_THICKNESS, arcsController, arcPropertiesController);
    }

    private static ArcUI create(final NormalWord name, final Priority priority,
            final ArcType type, final NodeUI from, final NodeUI to,
            final Offset offset, final int thickness,
            final ArcsController arcsController,
            final ArcPropertiesDisplayController arcPropertiesController) {
        Preconditions.checkNotNull(name);
        Preconditions.checkNotNull(type);
        Preconditions.checkNotNull(priority);
        Preconditions.checkNotNull(offset);
        Preconditions.checkNotNull(arcsController);
        Preconditions.checkNotNull(arcPropertiesController);
        Preconditions.checkArgument(thickness >= 0);

        final ArcUI newInstance =
                new ArcUI(from, to, arcsController, arcPropertiesController);
        newInstance.setArcName(name);
        newInstance.setPriority(priority);
        newInstance.setType(type);
        newInstance.setOffset(offset);
        newInstance.setThickness(thickness);
        newInstance.setFramedBounds(newInstance.getSegment().toBounds());

        from.addOut(newInstance);
        to.addIn(newInstance);

        newInstance.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(final MouseEvent e) {
                if (e.getClickCount() != 2) {
                    return;
                }

                if (newInstance.editMode == EditMode.REMOVE || (newInstance.editMode == EditMode.DESIGN && e.isShiftDown() && e.isControlDown())) {
                    if (!newInstance.removalConfirmed()) {
                        return;
                    }

                    newInstance.removeArc();
                } else {
                    newInstance.showProperties();
                }
            }
        });

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

    private final ArcsController arcsController;
    private final ArcPropertiesDisplayController arcPropertiesController;
    private final NodeUI from;
    private final NodeUI to;

    private NormalWord name;

    private Priority priority;

    private ArcType type;

    private Offset offset;

    private int thickness;

    private EditMode editMode = EditMode.DESIGN;

    private ArcUI(final NodeUI from, final NodeUI to,
            final ArcsController arcsController,
            final ArcPropertiesDisplayController arcPropertiesController) {
        Preconditions.checkNotNull(from);
        Preconditions.checkNotNull(to);
        Preconditions.checkNotNull(arcPropertiesController);
        Preconditions.checkNotNull(arcsController);
        Preconditions.checkArgument(!from.equals(to));

        this.from = from;
        this.to = to;
        this.arcPropertiesController = arcPropertiesController;
        this.arcsController = arcsController;
    }

    /**
     * Změní zobrazené jméno hrany.
     * 
     * @param newVersion
     *            nová verze modelu hrany
     */
    public void arcRenamed(final Arc newVersion) {
        assert SwingUtilities.isEventDispatchThread();

        Preconditions.checkNotNull(newVersion);

        setArcName(newVersion.getName());
        repaint();
    }

    /**
     * Změní zobrazenou prioritu hrany.
     * 
     * @param newVersion
     *            nová verze modelu hrany
     */
    public void arcReprioritized(final Arc newVersion) {
        assert SwingUtilities.isEventDispatchThread();

        Preconditions.checkNotNull(newVersion);

        setPriority(newVersion.getPriority());
        repaint();
    }

    /**
     * Změní zobrazený typ hrany.
     * 
     * @param newVersion
     *            nová verze modelu hrany
     */
    public void arcRetyped(final Arc newVersion) {
        assert SwingUtilities.isEventDispatchThread();

        Preconditions.checkNotNull(newVersion);

        setType(toType(newVersion.getClass()));
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.JComponent#contains(int, int)
     */
    @Override
    public boolean contains(final int x, final int y) {
        final boolean result =
                getSegment().contains(x + getX(), y + getY(),
                        this.thickness / 2d);
        return result;
    }

    private void drawArrowArms(final int xTip, final int yTip,
            final Graphics2D graphics2d, final double arrowTheta) {
        graphics2d.translate(xTip, yTip);
        graphics2d.rotate(arrowTheta);

        graphics2d.rotate(ARROW_ARM_ANGLE);
        final Line2D.Double leftSideLine =
                new Line2D.Double(0, 0, ARROW_SIDE_LENGTH, 0);
        graphics2d.draw(leftSideLine);
        graphics2d.rotate(-ARROW_ARM_ANGLE);

        graphics2d.rotate(-ARROW_ARM_ANGLE);
        final Line2D.Double rightSideLine =
                new Line2D.Double(0, 0, ARROW_SIDE_LENGTH, 0);
        graphics2d.draw(rightSideLine);
        graphics2d.rotate(ARROW_ARM_ANGLE);

        graphics2d.rotate(-arrowTheta);
        graphics2d.translate(-xTip, -yTip);
    }

    /**
     * Vrátí zobrazený výchozí uzel hrany.
     * 
     * @return zobrazený výchozí uzel hrany
     */
    public final NodeUI getFrom() {
        return this.from;
    }

    /**
     * Vrátí úsečku, která jako diagonála vyznačuje polohu a rozměry komponenty.
     * Může se odchylovat v kolmém směru od nejkratší spojnice zobrazených uzlů
     * v případě, že existuje hrana protisměrná.
     * 
     * @return úsečka hrany
     */
    private Segment getSegment() {
        final Rectangle fromBounds = this.from.getBounds();
        final Rectangle toBounds = this.to.getBounds();

        final Point fromCenter =
                new Point((int) fromBounds.getCenterX(),
                        (int) fromBounds.getCenterY());
        final Point toCenter =
                new Point((int) toBounds.getCenterX(),
                        (int) toBounds.getCenterY());

        final Segment original = Segment.create(fromCenter, toCenter);
        return original.offset(this.offset.getValue());
    }

    /**
     * Vrátí cílový uzel hrany.
     * 
     * @return cílový uzel hrany
     */
    public final NodeUI getTo() {
        return this.to;
    }

    /**
     * Oznámí komponentě, že byla odebrána její protisměrná hrana, aby se mohla
     * překreslit.
     */
    public void jointContracted() {
        setOffset(Offset.NO_OFFSET);
        jointMoved();
    }

    /**
     * Oznámí komponentě, že byla vůči ní přidána protisměrná hrana, aby se
     * mohla překreslit.
     */
    public void jointDilated() {
        setOffset(Offset.OFFSET);
        repaint();
    }

    /**
     * Oznámí komponentě, že se posunul jeden z jejích koncových uzlů, aby se
     * mohla překreslit.
     */
    public void jointMoved() {
        setFramedBounds(getSegment().toBounds());
        repaint();
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

        graphics2d.setColor(this.type.getColor());

        final Line2D.Double mainLine =
                new Line2D.Double(xFromOrigin, yFromOrigin, xToOrigin,
                        yToOrigin);
        graphics2d.draw(mainLine);

        final double theta = Math.atan2(yTo - yFrom, xTo - xFrom);
        final int xCenter = (xFromOrigin + xToOrigin) / 2;
        final int yCenter = (yFromOrigin + yToOrigin) / 2;
        drawArrowArms(xCenter, yCenter, graphics2d, theta);

        graphics2d.setColor(Color.WHITE);
        graphics.drawString(
                String.format("%s (%s)", this.name.getText(),
                        this.priority.getValue()), xCenter, yCenter);
    }

    private boolean removalConfirmed() {
        final int removalConfirmed =
                JOptionPane
                        .showConfirmDialog(this,
                                UiLocalizer.print("ArcRemovalConfirmMessage"),
                                UiLocalizer.print("ArcRemovalConfirmTitle"),
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.QUESTION_MESSAGE);
        if (removalConfirmed != JOptionPane.YES_OPTION) {
            return false;
        } else {
            return true;
        }
    }

    private void removeArc() {
        this.arcsController.removeArc(this.name);
    }

    private void setArcName(final NormalWord name) {
        Preconditions.checkNotNull(name);

        this.name = name;
        super.setName(name.getText());
    }

    private void setOffset(final Offset offset) {
        this.offset = offset;
    }

    private final void setPriority(final Priority priority) {
        Preconditions.checkNotNull(priority);

        this.priority = priority;
    }

    private void setThickness(final int thickness) {
        Preconditions.checkArgument(thickness >= 0);

        this.thickness = thickness;
    }

    private final void setType(final ArcType type) {
        Preconditions.checkNotNull(type);

        this.type = type;
        this.repaint();
    }

    private void showProperties() {
        this.arcPropertiesController.displayArcProperties(this.name);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.views.Editable#setEditMode(cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.views.EditMode)
     */
    @Override
    public void setEditMode(final EditMode mode) {
        Preconditions.checkNotNull(mode);
        
        this.editMode = mode;
    }
}
