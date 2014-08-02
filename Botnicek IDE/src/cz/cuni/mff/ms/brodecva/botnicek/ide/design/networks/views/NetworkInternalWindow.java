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
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.controllers.ArcPropertiesDisplayController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.controllers.ArcsController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.graph.ArcUI;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.graph.ArcsView;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.controllers.NetworkController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.controllers.NodesController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.views.NodeUI;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.views.NodesView;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.logging.LocalizedLogger;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.components.FramedComponent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.components.UnobscuredInternalFrame;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyVetoException;
import java.util.logging.Logger;

import javax.swing.JScrollPane;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

/**
 * <p>Správce vnitřního okna, které zobrazuje editovatelný graf sítě.</p>
 * <p>Podporuje přidávání vrcholů (poklepem na prázdný prostor), jejich přejmenování (poklep s klávesou Shift), změnu typu interakce (poklep s drženou klávesou Alt), typu přechodu na hrany (s klávesou Ctrl) a odebrání vrcholu (Ctrl a Shift).</p>
 * <p>Mezi vrcholy lze vytvořit hrany. Jejich zakreslení započne poklepáním na výchozí uzel, Ukončí se poklepem na cílový a zadáním jména. Hrany lze odebírat stejně jako vrcholy. Vlastnosti hran lze editovat ve zvláštním rámu, který se otevře po běžném poklepání v okolí hrany.</p>
 * <p>Barvy prvků sítě či jejich tvar reflektují aktuální stav, ve kterém se nacházejí.</p>
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class NetworkInternalWindow implements NetworkView, NodesView, ArcsView {
    

    private static final Logger LOGGER = LocalizedLogger.getLogger(NetworkInternalWindow.class);
    
    private static final int PLANE_WIDTH = 8000;
    private static final int PLANE_HEIGHT = 4500;
    
    private static final int HEIGHT = 300;
    private static final int WIDTH = 400;

    private final UnobscuredInternalFrame frame = UnobscuredInternalFrame.create();    
    
    private final JPanel designPanel = new JPanel(null) {
        
        private static final long serialVersionUID = 1L;

        public Dimension getPreferredSize() {
            return new Dimension(PLANE_WIDTH, PLANE_HEIGHT);
        };
    };    
    private final JScrollPane designScrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    
    private final BiMap<NormalWord, NodeUI> nodes = HashBiMap.create();
    private final BiMap<NormalWord, ArcUI> arcs = HashBiMap.create();
    
    private final ArcDesignListener  arcDesignListener;
    
    private final NetworkController networkController;
    private final NodesController nodesController;
    private final ArcsController arcsController;
    private final ArcPropertiesDisplayController arcPropertiesController;   

    /**
     * Spustí testovací verzi.
     * 
     * @param args argumenty
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    final JFrame frame = new JFrame();
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.setSize(new Dimension(600, 600));
                    
                    final JDesktopPane desktopPane = new JDesktopPane();
                    frame.setContentPane(desktopPane);
                    
                    final NetworkInternalWindow window = create(desktopPane);
                    window.show();
                    
                    frame.setVisible(true);
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static NetworkInternalWindow create(final Container parent) {
        return create(parent, DummyNetworkController.create(), DummyNodesController.create(), DummyArcsController.create(), DummyArcPropertiesDisplayController.create(), DefaultArcDesignListenerFactory.create());
    }
    
    /**
     * Vytvoří správce vnitřního rámu zobrazeného úseku grafu sítě.
     * 
     * @param parent rodičovský kontejner
     * @param networkController řadič sítě (přidává prvky)
     * @param nodesController řadič uzlů (modifikuje tyto prvky přes graf a odebírá je z něj)
     * @param arcsController řadič hran (odebírá tyto prvky z grafu)
     * @param arcPropertiesController řadič zobrazení vlastností hran
     * @return správce rámu
     */
    public static NetworkInternalWindow create(final Container parent, final NetworkController networkController, final NodesController nodesController, final ArcsController arcsController, final ArcPropertiesDisplayController arcPropertiesController) {
        return create(parent, networkController, nodesController, arcsController, arcPropertiesController, DefaultArcDesignListenerFactory.create());
    }
    
    /**
     * Vytvoří správce vnitřního rámu zobrazeného úseku grafu sítě.
     * 
     * @param parent rodičovský kontejner
     * @param networkController řadič sítě (přidává prvky)
     * @param nodesController řadič uzlů (modifikuje tyto prvky přes graf a odebírá je z něj)
     * @param arcsController řadič hran (odebírá tyto prvky z grafu)
     * @param arcPropertiesController řadič zobrazení vlastností hran
     * @param arcDesignListenerFactory továrna na posluchače návrhu hrany
     * @return správce rámu
     */
    static NetworkInternalWindow create(final Container parent, final NetworkController networkController, final NodesController nodesController, final ArcsController arcsController, final ArcPropertiesDisplayController arcPropertiesController, final ArcDesignListenerFactory arcDesignListenerFactory) {
        Preconditions.checkNotNull(parent);
        Preconditions.checkNotNull(networkController);
        Preconditions.checkNotNull(arcsController);
        Preconditions.checkNotNull(nodesController);
        Preconditions.checkNotNull(arcPropertiesController);
        
        final NetworkInternalWindow newInstance = new NetworkInternalWindow(networkController, arcsController, nodesController, arcPropertiesController, arcDesignListenerFactory);
        
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
        
        arcsController.addView(newInstance);
        
        newInstance.frame.addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameClosed(final InternalFrameEvent e) {
                newInstance.unsubscribe();
                
                super.internalFrameClosed(e);
            }
        });
        
        parent.add(newInstance.frame);
        newInstance.frame.offset();
        
        return newInstance;
    }

    private NetworkInternalWindow(final NetworkController networkController, final ArcsController arcsController, final NodesController nodesController, final ArcPropertiesDisplayController arcPropertiesController, final ArcDesignListenerFactory arcDesignListenerFactory) {
        Preconditions.checkNotNull(networkController);
        Preconditions.checkNotNull(nodesController);
        Preconditions.checkNotNull(arcsController);
        Preconditions.checkNotNull(arcPropertiesController);
        Preconditions.checkNotNull(arcDesignListenerFactory);
        
        this.networkController = networkController;
        this.nodesController = nodesController;
        this.arcsController = arcsController;
        this.arcPropertiesController = arcPropertiesController;
        this.arcDesignListener = arcDesignListenerFactory.produce(this.designPanel, this.nodes.values(), this.arcs.values(), networkController);
        
        this.designPanel.setLayout(null);
        this.designPanel.setBackground(Color.GRAY);
        
        this.designScrollPane.setViewportView(this.designPanel);
        
        this.frame.setClosable(true);
        this.frame.setIconifiable(true);
        this.frame.setMaximizable(true);
        this.frame.setResizable(true);
        this.frame.setSize(WIDTH, HEIGHT);
        this.frame.setContentPane(this.designScrollPane);
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
        
        
        added.addMouseListener(this.arcDesignListener);
        added.addMouseMotionListener(this.arcDesignListener);
        
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
        
        this.designPanel.remove(present);
        this.designPanel.repaint();
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
        
        present.nodeRenamed(newVersion);
        
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
        
        present.nodeMoved(newVersion);
        
        this.nodes.put(newVersion.getName(), present);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.views.NodesView#nodeRetyped(cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node, cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node)
     */
    @Override
    public void nodeRetyped(final Node oldVersion, final Node newVersion) {
        assert SwingUtilities.isEventDispatchThread();
        Preconditions.checkNotNull(oldVersion);
        Preconditions.checkNotNull(newVersion);
        
        final NodeUI present = this.nodes.remove(oldVersion.getName());
        Preconditions.checkArgument(present != null);
        
        present.nodeRetyped(newVersion);
        
        this.nodes.put(newVersion.getName(), present);
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.views.NetworkView#arcAdded(cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc)
     */
    @Override
    public void arcAdded(final Arc arc) {
        assert SwingUtilities.isEventDispatchThread();
        
        Preconditions.checkNotNull(arc);
        Preconditions.checkArgument(!this.arcs.containsKey(arc.getName()));
        
        final NodeUI from = this.nodes.get(arc.getFrom().getName());
        Preconditions.checkArgument(from != null);
        
        final NodeUI to = this.nodes.get(arc.getTo().getName());
        Preconditions.checkArgument(to != null);
        
        final ArcUI added = ArcUI.create(arc, from, to, this.arcsController, this.arcPropertiesController);
        added.addMouseListener(this.arcDesignListener);
        added.addMouseMotionListener(this.arcDesignListener);
        
        this.designPanel.add(added);
        this.designPanel.setComponentZOrder(added, this.designPanel.getComponentCount() - 1);
        this.designPanel.repaint();
        
        this.arcs.put(arc.getName(), added);
    }
    

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.networks.NetworkView#arcRemoved(java.lang.String)
     */
    @Override
    public void arcRemoved(final Arc arc) {
        assert SwingUtilities.isEventDispatchThread();
        Preconditions.checkNotNull(arc);
        
        final FramedComponent present = this.arcs.remove(arc.getName());
        Preconditions.checkArgument(present != null);
        
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
        
        removeFromParent();
        unsubscribe();
    }

    private void unsubscribe() {
        this.networkController.removeView(this);
        this.nodesController.removeView(this);
        this.arcsController.removeView(this);
    }

    private void removeFromParent() {
        final Container parent = this.frame.getParent();
        Preconditions.checkState(parent != null);
        
        parent.remove(this.frame);
        parent.revalidate();
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.views.NetworkView#selected()
     */
    @Override
    public void selected() {
        this.frame.show();
        
        try {
            this.frame.setIcon(false);
        } catch (final PropertyVetoException e) {
            LOGGER.warning("InternalFrameVeto");
        }
        
        try {
            this.frame.setSelected(true);
        } catch (final PropertyVetoException e) {
            LOGGER.warning("InternalFrameVeto");
        }
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.ArcsView#arcRenamed(cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc, cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc)
     */
    @Override
    public void arcRenamed(Arc oldVersion, Arc newVersion) {
        assert SwingUtilities.isEventDispatchThread();
        Preconditions.checkNotNull(oldVersion);
        Preconditions.checkNotNull(newVersion);
        
        final ArcUI present = this.arcs.remove(oldVersion.getName());
        Preconditions.checkArgument(present != null);
        
        present.arcRenamed(newVersion);
        
        this.arcs.put(newVersion.getName(), present);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.ArcsView#arcReprioritized(cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc, cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc)
     */
    @Override
    public void arcReprioritized(Arc oldVersion, Arc newVersion) {
        assert SwingUtilities.isEventDispatchThread();
        Preconditions.checkNotNull(oldVersion);
        Preconditions.checkNotNull(newVersion);
        
        final ArcUI present = this.arcs.remove(oldVersion.getName());
        Preconditions.checkArgument(present != null);
        
        present.arcReprioritized(newVersion);
        
        this.arcs.put(newVersion.getName(), present);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.ArcsView#arcRetyped(cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc, cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc)
     */
    @Override
    public void arcRetyped(Arc oldVersion, Arc newVersion) {
        assert SwingUtilities.isEventDispatchThread();
        Preconditions.checkNotNull(oldVersion);
        Preconditions.checkNotNull(newVersion);
        
        final ArcUI present = this.arcs.remove(oldVersion.getName());
        Preconditions.checkArgument(present != null);
        
        present.arcRetyped(newVersion);
        
        this.arcs.put(newVersion.getName(), present);
    }
    
    /**
     * Zobrazí vnitřní okno sítě.
     */
    public void show() {
        this.frame.show();
    }
}
