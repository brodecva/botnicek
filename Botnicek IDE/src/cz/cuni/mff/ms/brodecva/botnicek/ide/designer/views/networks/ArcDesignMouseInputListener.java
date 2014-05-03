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
package cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.networks;

import java.awt.BasicStroke;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.controllers.NetworkController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.arcs.ArcUI;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.networks.NetworkInternalFrame.ElementLayer;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.nodes.NodeUI;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.graphics.Segment;

public final class ArcDesignMouseInputListener extends MouseAdapter implements MouseInputListener {
    private static final Object ARC_NAME_MESSAGE = "Zadejte název nového přechodu:";
    private static final String ARC_NAME_TITLE_CONTENT = "Pojmenovat přechod";
    private static final Object ARC_NAME_INITIAL_VALUE = "";
    
    private final JComponent lineComponent = new JComponent() {
        
        public void paint(Graphics graphics) {
            super.paint(graphics);
            
            final Graphics2D graphics2d = (Graphics2D) graphics;
            final BasicStroke stroke = new BasicStroke();
            graphics2d.setStroke(stroke);
            
            final Line2D line = new Line2D.Float(segment.getFromX(), segment.getFromY(), segment.getToX(), segment.getToY());
            graphics2d.draw(line);
        };
        
    };
    private final JPanel designPanel;
    private final Set<NodeUI> nodes;
    private final Set<ArcUI> arcs;
    private final NetworkController controller;
    
    private boolean connecting = false;
    private Segment segment = null;
    private NodeUI from = null;
    
    public static ArcDesignMouseInputListener create(final JPanel designPanel, final Set<NodeUI> nodes, final Set<ArcUI> arcs, final NetworkController controller) {
        return new ArcDesignMouseInputListener(designPanel, nodes, arcs, controller);
    }
    
    private ArcDesignMouseInputListener(final JPanel designPanel, final Set<NodeUI> nodes, final Set<ArcUI> arcs, final NetworkController controller) {
        Preconditions.checkNotNull(designPanel);
        Preconditions.checkNotNull(nodes);
        Preconditions.checkNotNull(arcs);
        Preconditions.checkNotNull(controller);
        
        this.designPanel = designPanel;
        this.nodes = nodes;
        this.arcs = arcs;
        this.controller = controller;
    }

    @Override
    public void mouseClicked(final MouseEvent e) {
        final Object source = e.getSource();
        
        if (!this.connecting) {
            assert this.from == null;
            
            if (this.nodes.contains(source)) {
                if (e.getClickCount() != 2) {
                    return;
                }
                
                if (e.isControlDown() || e.isAltDown() || e.isShiftDown()) {
                    return;
                }
                
                this.from = (NodeUI) source;
                
                final Rectangle fromBounds = this.from.getBounds();
                this.segment = Segment.create((int) fromBounds.getCenterX(), (int) fromBounds.getCenterX(), (int) fromBounds.getX() + e.getX(), (int) fromBounds.getY() + e.getY());
                this.designPanel.add(this.lineComponent, ElementLayer.DESIGNED_ARCS_LAYER.getLevel());
                this.connecting = true; 
            }
        } else {
            assert this.from != null;
            
            this.segment = null;
            this.connecting = false;
            this.designPanel.remove(this.lineComponent);
            
            if (this.nodes.contains(source) && !this.from.equals(source)) {
                final NodeUI to = (NodeUI) source;
                
                final Object newNameInput = JOptionPane.showInputDialog(to, ARC_NAME_MESSAGE, ARC_NAME_TITLE_CONTENT, JOptionPane.PLAIN_MESSAGE, null, null, ARC_NAME_INITIAL_VALUE);
                if (newNameInput == null) {
                    return;
                }                
                
                this.controller.addArc(newNameInput.toString(), this.from.getName(), to.getName());
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (this.connecting) {
            assert this.segment != null;
            
            final Object source = e.getSource();
            
            if (this.nodes.contains(source) || this.arcs.contains(source)) {
                final Component component = (NodeUI) source;
                
                this.segment = this.segment.moveTo(component.getX() + e.getX(), component.getY() + e.getY());                    
            } else if (source == this.designPanel) {
                this.segment = this.segment.moveTo(e.getX(), e.getY());
            }
            
            this.lineComponent.repaint();
        }
    }
}