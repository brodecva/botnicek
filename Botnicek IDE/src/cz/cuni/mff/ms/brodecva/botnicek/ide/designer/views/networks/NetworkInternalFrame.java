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

import java.awt.EventQueue;

import javax.swing.JInternalFrame;
import java.awt.CardLayout;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JEditorPane;
import javax.swing.SwingUtilities;

import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.controllers.ArcPropertiesController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.controllers.DummyArcPropertiesController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.controllers.DummyNetworkController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.controllers.NetworkController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.arcs.ArcType;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.arcs.ArcUI;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.nodes.DispatchType;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.nodes.NodeUI;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.nodes.PositionalType;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.nodes.ProceedType;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.utils.IrregularMouseListener;

import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JScrollPane;
import javax.swing.event.MouseInputListener;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public final class NetworkInternalFrame extends JInternalFrame implements NetworkView {
    
    public static enum ElementLayer {
        NODES_LAYER(0), DESIGNED_ARCS_LAYER(1), ARCS_LAYER(2);
        
        private final int level;
        
        private ElementLayer(final int level) {
            assert level >= 0;
            
            this.level = level;
        }
        
        public int getLevel() {
            return this.level;
        }
    }
    
    private static final String NODE_RENAME_MESSAGE = "Zadejte nový název uzlu:";
    private static final String NODE_RENAME_TITLE_CONTENT = "Přejmenovat uzel";
    
    private final JTabbedPane modesTabbedPane = new JTabbedPane(JTabbedPane.BOTTOM);
    
    private final JPanel designPanel = new JPanel();
    private final JPanel codePanel = new JPanel();
    
    private final JScrollPane designScrollPane = new JScrollPane(this.designPanel);
    private final JEditorPane editorPane = new JEditorPane();
    
    private final BiMap<String, NodeUI> nodes = HashBiMap.create();
    private final BiMap<String, ArcUI> arcs = HashBiMap.create();
    
    private final ArcDesignMouseInputListener  arcDesignMouseInputListener;
    
    private final NetworkController controller;
    private final ArcPropertiesController arcPropertiesController;
    

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    final NetworkInternalFrame frame = new NetworkInternalFrame();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static NetworkInternalFrame create(final NetworkController controller, final ArcPropertiesController arcPropertiesController) {
        final NetworkInternalFrame newInstance = new NetworkInternalFrame(controller, arcPropertiesController);
        
        newInstance.designPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                if (e.getClickCount() == 2) {
                    controller.addNode(e.getX(), e.getY());
                }
            }
        });
        
        newInstance.designPanel.addMouseListener(newInstance.arcDesignMouseInputListener);
        newInstance.designPanel.addMouseMotionListener(newInstance.arcDesignMouseInputListener);
        
        return newInstance;
    }
    
    private NetworkInternalFrame(final NetworkController controller, final ArcPropertiesController arcPropertiesController) {
        this.controller = controller;
        this.arcPropertiesController = arcPropertiesController;
        this.arcDesignMouseInputListener = ArcDesignMouseInputListener.create(this.designPanel, this.nodes.values(), this.arcs.values(), this.controller);
        
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setBounds(100, 100, 450, 300);
        getContentPane().setLayout(new CardLayout(0, 0));
        
        getContentPane().add(this.modesTabbedPane, "name_1084640649379039");
        
        this.modesTabbedPane.addTab("Code", null, this.codePanel, "Source code preview"); //TODO: Icon.
        this.codePanel.setLayout(new GridLayout(1, 1, 0, 0));
        
        this.codePanel.add(this.editorPane);
        
        this.modesTabbedPane.addTab("Design", null, this.designScrollPane, "To edit the network"); //TODO: Icon.
    }
    
    /**
     * 
     */
    private NetworkInternalFrame() {
        this(new DummyNetworkController(), new DummyArcPropertiesController());
    }

    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.NetworkView#nodeAdded(java.lang.String, cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.PositionalType, cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.ProceedType, cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.DispatchType, int, int, cz.cuni.mff.ms.brodecva.botnicek.ide.designer.controllers.NetworkController)
     */
    @Override
    public void nodeAdded(String name, PositionalType positional,
            ProceedType proceed, DispatchType dispatch, int x, int y,
            NetworkController controller) {
        assert SwingUtilities.isEventDispatchThread();
        
        nodeAdded(NodeUI.create(name, positional, proceed, dispatch, x, y, controller));
    }
    
    private void nodeAdded(final NodeUI node) {
        Preconditions.checkNotNull(node);
        
        final NodeUI present = this.nodes.put(node.getName(), node);
        Preconditions.checkArgument(present != null);
        
        node.addMouseListener(IrregularMouseListener.decorate(node, new MouseAdapter() {
            
            @Override
            public void mouseClicked(final MouseEvent e) {
                if (e.getClickCount() != 2) {
                    return;
                }
                
                if (e.isControlDown()) {
                    node.toggleProceedType();
                } else if (e.isAltDown()) {
                    node.toggleDispatchType();
                } else if (e.isShiftDown()) {
                    final Object newNameInput = JOptionPane.showInputDialog(node, NODE_RENAME_MESSAGE, NODE_RENAME_TITLE_CONTENT, JOptionPane.PLAIN_MESSAGE, null, null, node.getName());
                    if (newNameInput == null) {
                        return;
                    }
                    
                    node.rename(newNameInput.toString());
                }
            }
        }));
        node.addMouseListener(IrregularMouseListener.decorate(node, (MouseListener) arcDesignMouseInputListener));
        node.addMouseMotionListener(IrregularMouseListener.decorate(node, (MouseMotionListener) arcDesignMouseInputListener));
        
        this.designPanel.add(node);
        this.designPanel.setComponentZOrder(node, ElementLayer.NODES_LAYER.getLevel());
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.NetworkView#nodeRemoved(java.lang.String)
     */
    @Override
    public void nodeRemoved(final String name) {
        assert SwingUtilities.isEventDispatchThread();
        Preconditions.checkNotNull(name);
        
        final NodeUI present = this.nodes.get(name);
        Preconditions.checkArgument(present != null);
        
        present.removeAllComponentListeners();
        this.designPanel.remove(present);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.NetworkView#nodeRenamed(java.lang.String)
     */
    @Override
    public void nodeRenamed(final String name, final String newName) {
        assert SwingUtilities.isEventDispatchThread();
        Preconditions.checkNotNull(name);
        Preconditions.checkNotNull(newName);
        Preconditions.checkArgument(!newName.isEmpty());
        
        final NodeUI present = this.nodes.get(name);
        Preconditions.checkArgument(present != null);
        
        present.renamed(newName);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.NetworkView#nodeMoved(java.lang.String, int, int)
     */
    @Override
    public void nodeMoved(String name, int x, int y) {
        assert SwingUtilities.isEventDispatchThread();
        Preconditions.checkNotNull(name);
        Preconditions.checkArgument(x > 0);
        Preconditions.checkArgument(y > 0);
        
        final NodeUI present = this.nodes.get(name);
        Preconditions.checkArgument(present != null);
        
        present.moved(x, y);
    }

    /**
     * @param name
     * @param type
     */
    @Override
    public void nodeDispatchTypeChanged(final String name, final DispatchType type) {
        assert SwingUtilities.isEventDispatchThread();
        Preconditions.checkNotNull(name);
        Preconditions.checkNotNull(type);
        
        final NodeUI present = this.nodes.get(name);
        Preconditions.checkArgument(present != null);
        
        present.changedType(type);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.NetworkView#nodeDispatchTypeChanged(java.lang.String, cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.NodeUI.ProceedType)
     */
    @Override
    public void nodeProceedTypeChanged(final String name, final ProceedType type) {
        assert SwingUtilities.isEventDispatchThread();
        Preconditions.checkNotNull(name);
        Preconditions.checkNotNull(type);
        
        final NodeUI present = this.nodes.get(name);
        Preconditions.checkArgument(present != null);
        
        present.changedType(type);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.NetworkView#nodePositionalTypeChanged(java.lang.String, cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.NodeUI.PositionalType)
     */
    @Override
    public void nodePositionalTypeChanged(final String name, final PositionalType type) {
        assert SwingUtilities.isEventDispatchThread();
        
        Preconditions.checkNotNull(name);
        Preconditions.checkNotNull(type);
        
        final NodeUI present = this.nodes.get(name);
        Preconditions.checkArgument(present != null);
        
        present.changedType(type);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.NetworkView#arcAdded(java.lang.String, int, cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.ArcUI.Type, java.lang.String, java.lang.String)
     */
    @Override
    public void arcAdded(String name, int priority, ArcType type, String from,
            String to) {
        assert SwingUtilities.isEventDispatchThread();
        
        Preconditions.checkNotNull(from);
        Preconditions.checkNotNull(to);
        
        final NodeUI fromNode = this.nodes.get(from);
        Preconditions.checkArgument(fromNode != null);
        
        final NodeUI toNode = this.nodes.get(to);
        Preconditions.checkArgument(toNode != null);
        
        arcAdded(name, priority, type, fromNode, toNode);
    }
    
    public void arcAdded(final String name, final int priority, final ArcType type, final NodeUI from,
            final NodeUI to) {
        assert SwingUtilities.isEventDispatchThread();
        
        Preconditions.checkNotNull(name);
        Preconditions.checkNotNull(type);
        Preconditions.checkNotNull(from);
        Preconditions.checkNotNull(to);
        Preconditions.checkArgument(priority >= 0);
        
        final ArcUI present = this.arcs.get(name);
        Preconditions.checkArgument(present == null);
        
        final ArcUI newArc = ArcUI.create(name, priority, type, from, to, this.arcPropertiesController);
        
        newArc.addMouseListener(new MouseAdapter() {
            
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() != 2) {
                    return;
                }
                
                newArc.showProperties();
            }
        });
        newArc.addMouseListener(IrregularMouseListener.decorate(newArc, (MouseListener) this.arcDesignMouseInputListener));
        newArc.addMouseMotionListener(IrregularMouseListener.decorate(newArc, (MouseMotionListener) this.arcDesignMouseInputListener));
        
        this.designPanel.add(newArc);
        this.designPanel.setComponentZOrder(newArc, ElementLayer.ARCS_LAYER.getLevel());
        
        this.arcs.put(name, newArc);
    }
    

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.networks.NetworkView#arcRemoved(java.lang.String)
     */
    @Override
    public void arcRemoved(String name) {
        assert SwingUtilities.isEventDispatchThread();
        Preconditions.checkNotNull(name);
        
        final ArcUI present = this.arcs.get(name);
        Preconditions.checkArgument(present != null);
        
        present.removeAllComponentListeners();
        this.designPanel.remove(present);
    }
}
