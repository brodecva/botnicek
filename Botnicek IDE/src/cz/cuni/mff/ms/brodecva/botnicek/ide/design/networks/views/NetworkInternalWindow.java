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
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.views;

import java.awt.Color;
import java.awt.Container;
import java.awt.EventQueue;

import javax.swing.JInternalFrame;
import java.awt.CardLayout;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JEditorPane;
import javax.swing.SwingUtilities;

import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWords;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.code.controllers.CodeValidationController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.mixedpattern.controllers.MixedPatternValidationController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.simplepattern.controllers.SimplePatternValidationController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.words.controllers.NormalWordValidationController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.controllers.ArcController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.controllers.ArcPropertiesController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.controllers.AvailableReferencesController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.ArcPropertiesView;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.ArcUI;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.controllers.NetworkController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.controllers.NodesController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.views.NodeUI;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.views.NodesView;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.IrregularMouseListener;

import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyVetoException;

import javax.swing.JScrollPane;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public final class NetworkInternalWindow implements NetworkView, NodesView {
    
    private static final String NODE_RENAME_MESSAGE = "Zadejte nový název uzlu:";
    private static final String NODE_RENAME_TITLE_CONTENT = "Přejmenovat uzel";
    private static final String NODE_RENAME_ERROR_TITLE = "Neplatný formát jména uzlu";
    private static final String NORE_RENAME_ERROR_MESSAGE = "Zadejte platné jméno uzlu!";
    
    private final JInternalFrame frame;
    
    private final JTabbedPane modesTabbedPane = new JTabbedPane(JTabbedPane.BOTTOM);
    
    private final JPanel designPanel = new JPanel();
    private final JPanel codePanel = new JPanel();
    
    private final JScrollPane designScrollPane = new JScrollPane(this.designPanel);
    private final JEditorPane editorPane = new JEditorPane();
    
    private final BiMap<NormalWord, NodeUI> nodes = HashBiMap.create();
    private final BiMap<NormalWord, ArcUI> arcs = HashBiMap.create();
    
    private final ArcDesignListener  arcDesignListener;
    
    private final NodesController nodesController;
    private final ArcPropertiesController arcPropertiesController;
    

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    final JFrame frame = new JFrame();
                    
                    final JDesktopPane desktopPane = new JDesktopPane();
                    frame.add(desktopPane);
                    
                    final NetworkInternalWindow window = create(desktopPane);
                    window.show();
                    
                    frame.setVisible(true);
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 
     */
    public void show() {
        this.frame.show();
    }

    static NetworkInternalWindow create(final Container parent) {
        return create(parent, DummyNetworkController.create(), DummyNodesController.create(), DummyArcPropertiesController.create(), DefaultArcDesignListenerFactory.create());
    }
    
    public static NetworkInternalWindow create(final Container parent, final NetworkController networkController, final NodesController nodesController, final ArcPropertiesController arcPropertiesController) {
        return create(parent, networkController, nodesController, arcPropertiesController, DefaultArcDesignListenerFactory.create());
    }
    
    public static NetworkInternalWindow create(final Container parent, final NetworkController networkController, final NodesController nodesController, final ArcPropertiesController arcPropertiesController, final ArcDesignListenerFactory arcDesignListenerFactory) {
        Preconditions.checkNotNull(parent);
        Preconditions.checkNotNull(networkController);
        Preconditions.checkNotNull(nodesController);
        Preconditions.checkNotNull(arcPropertiesController);
        
        final NetworkInternalWindow newInstance = new NetworkInternalWindow(networkController, nodesController, arcPropertiesController, arcDesignListenerFactory);
        
        newInstance.designPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                if (e.getClickCount() == 2) {
                    networkController.addNode(e.getX(), e.getY());
                }
            }
        });
        
        newInstance.designPanel.addMouseListener(newInstance.arcDesignListener);
        newInstance.designPanel.addMouseMotionListener(newInstance.arcDesignListener);
        
        networkController.addView(newInstance);
        networkController.fill(newInstance);
        
        nodesController.addView(newInstance);
        nodesController.fill(newInstance);
        
        newInstance.testColorize();
        
        parent.add(newInstance.frame);
        
        return newInstance;
    }

    private NetworkInternalWindow(final NetworkController networkController, final NodesController nodesController, final ArcPropertiesController arcPropertiesController, final ArcDesignListenerFactory arcDesignListenerFactory) {
        Preconditions.checkNotNull(networkController);
        Preconditions.checkNotNull(nodesController);
        Preconditions.checkNotNull(arcPropertiesController);
        Preconditions.checkNotNull(arcDesignListenerFactory);
        
        this.nodesController = nodesController;
        this.arcPropertiesController = arcPropertiesController;
        this.arcDesignListener = arcDesignListenerFactory.produce(designPanel, nodes.values(), arcs.values(), networkController);
        
        this.frame = new JInternalFrame();
        
        this.frame.setClosable(true);
        this.frame.setIconifiable(true);
        this.frame.setMaximizable(true);
        this.frame.setResizable(true);
        this.frame.setBounds(100, 100, 450, 300);
        this.frame.getContentPane().setLayout(new CardLayout(0, 0));
        
        this.frame.getContentPane().add(this.modesTabbedPane, "name_1084640649379039");
        
        this.designPanel.setLayout(null);
        
        this.modesTabbedPane.addTab("Design", null, this.designScrollPane, "To edit the network");
        
        this.modesTabbedPane.addTab("Code", null, this.codePanel, "Source code preview");
        this.codePanel.setLayout(new GridLayout(1, 1, 0, 0));
        
        this.codePanel.add(this.editorPane);
    }
    
    private void testColorize() {
        this.designPanel.setBackground(Color.CYAN);
        this.codePanel.setBackground(Color.ORANGE);
        this.designScrollPane.setBackground(Color.BLUE);
        this.editorPane.setBackground(Color.MAGENTA);
        this.modesTabbedPane.setBackground(Color.GREEN);
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.NetworkView#nodeAdded(java.lang.String, cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.PositionalType, cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.ProceedType, cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.DispatchType, int, int, cz.cuni.mff.ms.brodecva.botnicek.ide.designer.controllers.NetworkController)
     */
    @Override
    public void nodeAdded(final Node node) {
        assert SwingUtilities.isEventDispatchThread();
        
        Preconditions.checkNotNull(node);
        Preconditions.checkArgument(!this.nodes.containsKey(node.getName()));
        
        final NodeUI added = NodeUI.create(node, this.nodesController);
        this.nodes.put(node.getName(), added);
        
        added.addMouseListener(IrregularMouseListener.decorate(added, new MouseAdapter() {
            
            @Override
            public void mouseClicked(final MouseEvent e) {
                if (e.getClickCount() != 2) {
                    return;
                }
                
                if (e.isControlDown()) {
                    added.toggleProceedType();
                } else if (e.isAltDown()) {
                    added.toggleDispatchType();
                } else if (e.isShiftDown()) {
                    Object newNameInput;
                    do {
                        newNameInput = JOptionPane.showInputDialog(added, NODE_RENAME_MESSAGE, NODE_RENAME_TITLE_CONTENT, JOptionPane.PLAIN_MESSAGE, null, null, added.getName());
                        
                        try {
                            final NormalWord normalNewNameInput = NormalWords.of(newNameInput.toString());
                            added.rename(normalNewNameInput);
                            return;
                        } catch (final IllegalArgumentException ex) {
                            JOptionPane.showMessageDialog(frame, NORE_RENAME_ERROR_MESSAGE, NODE_RENAME_ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
                        }
                    } while (newNameInput != null);
                }
            }
        }));
        added.addMouseListener(IrregularMouseListener.decorate(added, (MouseListener) arcDesignListener));
        added.addMouseMotionListener(IrregularMouseListener.decorate(added, (MouseMotionListener) arcDesignListener));
        
        added.setVisible(true);
        
        this.designPanel.add(added);
        this.designPanel.setComponentZOrder(added, 0);
        this.designPanel.repaint();
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.NetworkView#nodeRemoved(java.lang.String)
     */
    @Override
    public void nodeRemoved(final Node node) {
        assert SwingUtilities.isEventDispatchThread();
        Preconditions.checkNotNull(node);
        
        final NodeUI present = this.nodes.remove(node.getName());
        Preconditions.checkArgument(present != null);
        
        present.removeAllComponentListeners();
        this.designPanel.remove(present);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.views.NetworkView#nodeRenamed(cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node, cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node)
     */
    @Override
    public void nodeRenamed(final Node oldVersion, final Node newVersion) {
        assert SwingUtilities.isEventDispatchThread();
        Preconditions.checkNotNull(oldVersion);
        Preconditions.checkNotNull(newVersion);
        
        final NodeUI present = this.nodes.remove(oldVersion.getName());
        Preconditions.checkArgument(present != null);
        
        present.nodeRenamed(oldVersion, newVersion);
        
        this.nodes.put(newVersion.getName(), present);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.NetworkView#nodeMoved(java.lang.String, int, int)
     */
    @Override
    public void nodeMoved(final Node oldVersion, final Node newVersion) {
        assert SwingUtilities.isEventDispatchThread();
        Preconditions.checkNotNull(oldVersion);
        Preconditions.checkNotNull(newVersion);
        
        final NodeUI present = this.nodes.remove(oldVersion.getName());
        Preconditions.checkArgument(present != null);
        
        present.nodeMoved(oldVersion, newVersion);
        
        this.nodes.put(newVersion.getName(), present);
    }

    /**
     * @param name
     * @param type
     */
    @Override
    public void nodeRetyped(final Node oldVersion, final Node newVersion) {
        assert SwingUtilities.isEventDispatchThread();
        Preconditions.checkNotNull(oldVersion);
        Preconditions.checkNotNull(newVersion);
        
        final NodeUI present = this.nodes.remove(oldVersion.getName());
        Preconditions.checkArgument(present != null);
        
        present.nodeRetyped(oldVersion, newVersion);
        
        this.nodes.put(newVersion.getName(), present);
    }
    
    @Override
    public void arcAdded(final Arc arc) {
        assert SwingUtilities.isEventDispatchThread();
        
        Preconditions.checkNotNull(arc);
        Preconditions.checkArgument(!this.arcs.containsKey(arc.getName()));
        
        final NodeUI from = this.nodes.get(arc.getFrom().getName());
        Preconditions.checkArgument(from != null);
        
        final NodeUI to = this.nodes.get(arc.getTo().getName());
        Preconditions.checkArgument(to != null);
        
        final ArcUI added = ArcUI.create(arc, from, to, this.arcPropertiesController);
        
        added.addMouseListener(new MouseAdapter() {
            
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() != 2) {
                    return;
                }
                
                added.showProperties();
            }
        });
        added.addMouseListener(IrregularMouseListener.decorate(added, (MouseListener) this.arcDesignListener));
        added.addMouseMotionListener(IrregularMouseListener.decorate(added, (MouseMotionListener) this.arcDesignListener));
        
        this.designPanel.add(added);
        this.designPanel.setComponentZOrder(added, this.designPanel.getComponentCount() - 1);
        
        this.arcs.put(arc.getName(), added);
    }
    

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.networks.NetworkView#arcRemoved(java.lang.String)
     */
    @Override
    public void arcRemoved(final Arc arc) {
        assert SwingUtilities.isEventDispatchThread();
        Preconditions.checkNotNull(arc);
        
        final ArcUI present = this.arcs.remove(arc.getName());
        Preconditions.checkArgument(present != null);
        
        present.removeAllComponentListeners();
        this.designPanel.remove(present);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.views.NetworkView#nameChanged(cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.NetworkInfo)
     */
    @Override
    public void renamed(final Network network) {
        Preconditions.checkNotNull(network);
        
        this.frame.setTitle(network.getName());
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.views.NetworkView#removed()
     */
    @Override
    public void removed() {
        this.frame.dispose();
        
        final Container parent = this.frame.getParent();
        Preconditions.checkState(parent != null);
        
        parent.remove(this.frame);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.views.NetworkView#selected()
     */
    @Override
    public void selected() {
        frame.show();
        
        try {
            frame.setIcon(false);
        } catch (final PropertyVetoException e) {
            //TODO:
        }
        
        try {
            frame.setSelected(true);
        } catch (final PropertyVetoException e) {
            //TODO:
        }
    }
}
