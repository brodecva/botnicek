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
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.views;

import java.awt.BasicStroke;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.util.Set;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.graph.ArcUI;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.controllers.NetworkController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.views.NodeUI;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Intended;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.resources.ExceptionLocalizer;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.resources.UiLocalizer;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.Components;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.graphics.Rendering;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.graphics.Segment;

/**
 * Výchozí implementace posluchače návrhu hrany v síti.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class DefaultArcDesignListener extends MouseAdapter implements
        ArcDesignListener {

    private static final String ARC_NAME_INITIAL_VALUE = "";
    
    private EditMode mode = EditMode.DESIGN;

    /**
     * Vytvoří posluchače.
     * 
     * @param designPanel
     *            panel s grafem
     * @param nodes
     *            přítomné uzly
     * @param arcs
     *            přítomné hrany
     * @param controller
     *            řadič sítě (pro přidávání hran)
     * @return posluchač
     */
    public static DefaultArcDesignListener create(final JPanel designPanel,
            final Set<? extends NodeUI> nodes, final Set<? extends ArcUI> arcs,
            final NetworkController controller) {
        return new DefaultArcDesignListener(designPanel, nodes, arcs,
                controller);
    }

    /**
     * Komponenta zobrazené spojnice výchozího uzlu a bodu pod ukazatelem.
     * Aktualizuje se s pohybem ukazatele myši.
     */
    private final JComponent lineComponent = new JComponent() {

        private static final long serialVersionUID = 1L;

        @Override
        public void paint(final Graphics graphics) {
            super.paint(graphics);

            final Graphics2D graphics2d = (Graphics2D) graphics;
            Rendering.preset(graphics2d);

            graphics2d.setStroke(new BasicStroke());

            final int offsetX = getX();
            final int offsetY = getY();

            assert DefaultArcDesignListener.this.segment.isPresent();
            final Segment rawSegment =
                    DefaultArcDesignListener.this.segment.get();
            final Line2D line =
                    new Line2D.Float(rawSegment.getFromX() - offsetX,
                            rawSegment.getFromY() - offsetY,
                            rawSegment.getToX() - offsetX, rawSegment.getToY()
                                    - offsetY);
            graphics2d.draw(line);
        };

    };

    private final NetworkController controller;
    private final JPanel designPanel;
    private final Set<? extends NodeUI> nodes;

    private final Set<? extends ArcUI> arcs;
    private volatile boolean connecting = false;
    private Optional<Segment> segment = Optional.<Segment> absent();

    private Optional<NodeUI> from = Optional.<NodeUI> absent();

    private DefaultArcDesignListener(final JPanel designPanel,
            final Set<? extends NodeUI> nodes, final Set<? extends ArcUI> arcs,
            final NetworkController controller) {
        Preconditions.checkNotNull(designPanel);
        Preconditions.checkNotNull(nodes);
        Preconditions.checkNotNull(arcs);
        Preconditions.checkNotNull(controller);

        this.designPanel = designPanel;
        this.nodes = nodes;
        this.arcs = arcs;
        this.controller = controller;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.views.ArcDesignListener
     * #mouseClicked(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseClicked(final MouseEvent e) {
        if (this.mode != EditMode.DESIGN) {
            return;
        }
        
        final Object source = e.getSource();

        if (!this.connecting) {
            assert !this.from.isPresent();

            if (this.nodes.contains(source)) {
                if (e.getClickCount() != 2) {
                    return;
                }

                if (e.isControlDown() || e.isAltDown() || e.isShiftDown()) {
                    return;
                }

                final NodeUI newRawFrom = (NodeUI) source;
                this.from = Optional.of(newRawFrom);

                final Rectangle fromBounds = newRawFrom.getBounds();

                final Segment newRawSegment =
                        Segment.create((int) fromBounds.getCenterX(),
                                (int) fromBounds.getCenterY(),
                                (int) fromBounds.getX() + e.getX(),
                                (int) fromBounds.getY() + e.getY());
                this.segment = Optional.of(newRawSegment);
                this.lineComponent.setBounds(newRawSegment.toBounds());

                this.designPanel.add(this.lineComponent);
                placeOnBottom(this.lineComponent);

                this.connecting = true;
            }
        } else {
            assert this.from.isPresent();

            this.segment = Optional.absent();
            this.connecting = false;
            this.designPanel.remove(this.lineComponent);
            this.designPanel.repaint();

            if (!this.nodes.contains(source) || this.from.get().equals(source)) {
                return;
            }

            final NodeUI to = (NodeUI) source;
            final Object newNameInput =
                    JOptionPane.showInputDialog(to,
                            UiLocalizer.print("ARC_NAME_MESSAGE"),
                            UiLocalizer.print("ARC_NAME_TITLE_CONTENT"),
                            JOptionPane.PLAIN_MESSAGE,
                            Intended.<Icon> nullReference(),
                            Intended.arrayNull(), ARC_NAME_INITIAL_VALUE);
            if (Components.hasUserCanceledInput(newNameInput)) {
                return;
            }

            try {
                this.controller.addArc(newNameInput.toString(), this.from.get()
                        .getNodeName(), to.getNodeName());
            } catch (final IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(
                        to,
                        ExceptionLocalizer.print("ArcCreationError") + " "
                                + ex.getMessage(),
                        UiLocalizer.print("ARC_NAME_ERROR_TITLE"),
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.views.ArcDesignListener
     * #mouseMoved(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseMoved(final MouseEvent e) {
        if (this.mode != EditMode.DESIGN) {
            return;
        }
        
        if (this.connecting) {
            assert this.segment.isPresent();
            final Segment rawSegment = this.segment.get();

            final Object source = e.getSource();

            if (this.nodes.contains(source) || this.arcs.contains(source)) {
                final Component component = (Component) source;

                final Segment newRawSegment =
                        rawSegment.moveTo(component.getX() + e.getX(),
                                component.getY() + e.getY());
                this.segment = Optional.of(newRawSegment);
                this.lineComponent.setBounds(newRawSegment.toBounds());
            } else if (source == this.designPanel) {
                final Segment newRawSegment =
                        rawSegment.moveTo(e.getX(), e.getY());
                this.segment = Optional.of(newRawSegment);
                this.lineComponent.setBounds(newRawSegment.toBounds());
            }

            this.lineComponent.repaint();
        }
    }

    /**
     * Zobraz komponentu nad ostatními, aby s ní bylo možné přehledně
     * manipulovat.
     * 
     * @param component
     *            komponenta
     */
    private void placeOnBottom(final Component component) {
        final Container parent = component.getParent();
        Preconditions.checkState(Components.hasParent(parent));

        parent.setComponentZOrder(component, parent.getComponentCount() - 1);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.views.Editable#setEditMode(cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.views.EditMode)
     */
    @Override
    public void setEditMode(final EditMode mode) {
        Preconditions.checkNotNull(mode);
        
        this.mode = mode;
    }
}