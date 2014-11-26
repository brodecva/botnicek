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
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.views;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.AutonomousComponent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.controllers.NetworkDisplayController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.controllers.SystemController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Intended;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.Presence;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.resources.UiLocalizer;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.Components;

/**
 * Přehled struktury systému v podobě plochého stromu. Navíc poskytuje tlačítko pro přidání sítě.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public class SystemOverview implements SystemView {

    private final class DefaultDeleteAction extends AbstractAction {

        private static final long serialVersionUID = 1L;

        /* (non-Javadoc)
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        @Override
        public void actionPerformed(final ActionEvent e) {
            final Object selected =
                    SystemOverview.this.tree.getLastSelectedPathComponent();
            if (Presence.isAbsent(selected)) {
                return;
            }

            if (!selected.equals(SystemOverview.this.tree.getModel().getRoot())) {
                Preconditions.checkState(selected instanceof Network);
                final Network node = (Network) selected;

                try {
                    SystemOverview.this.systemController.removeNetwork(node);
                } catch (final IllegalArgumentException ex) {
                    JOptionPane
                            .showMessageDialog(
                                    Intended.<Component> nullReference(),
                                    ex.getMessage(),
                                    UiLocalizer
                                            .print("NetworkDeleteImpossibleTitle"),
                                    JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    
    /**
     * Posluchač dvojkliků na stromě. V případě kořene zobrazí dialog pro zadání
     * jména nové sítě, v případě sítí je otevře.
     */
    private final class DefaultDoubleClickListener extends MouseAdapter {

        @Override
        public void mousePressed(final MouseEvent e) {
            final int x = e.getX();
            final int y = e.getY();

            final int selectionRow =
                    SystemOverview.this.tree.getRowForLocation(x, y);
            if (selectionRow == -1) {
                return;
            }

            final TreePath selectionPath =
                    SystemOverview.this.tree.getPathForLocation(x, y);
            final Object selected = selectionPath.getLastPathComponent();
            if (Presence.isAbsent(selected)) {
                return;
            }

            if (e.getClickCount() == 2) {
                final TreeModel model = SystemOverview.this.tree.getModel();
                final Object root = model.getRoot();

                if (selected.equals(root)) {
                    addNetwork();
                } else {
                    Preconditions.checkState(selected instanceof Network);
                    final Network node = (Network) selected;

                    SystemOverview.this.networkPropertiesController
                            .displayNetwork(node);
                }
            }
        }
    }

    /**
     * Posluchač, který vybere síť v případě jejího označení ve stromu.
     */
    private final class DefaultSelectionListener implements
            TreeSelectionListener {

        @Override
        public void valueChanged(final TreeSelectionEvent e) {
            final Object selected =
                    SystemOverview.this.tree.getLastSelectedPathComponent();
            if (Presence.isAbsent(selected)) {
                return;
            }

            if (!selected.equals(SystemOverview.this.tree.getModel().getRoot())) {
                Preconditions.checkState(selected instanceof Network);
                final Network node = (Network) selected;

                SystemOverview.this.systemController.selectNetwork(node);
            }
        }
    }

    /**
     * Vždy rozbalený stromu obsahující modely sítí se systémem jako kořenem.
     */
    private static final class SystemTree extends JTree {
        private static final long serialVersionUID = 1L;

        private SystemTree(final TreeModel name) {
            super(name);
        }

        /*
         * (non-Javadoc)
         * 
         * @see javax.swing.JTree#convertValueToText(java.lang.Object, boolean,
         * boolean, boolean, int, boolean)
         */
        @Override
        public String convertValueToText(final Object value,
                final boolean selected, final boolean expanded,
                final boolean leaf, final int row, final boolean hasFocus) {
            Preconditions.checkState(value instanceof AutonomousComponent);
            final AutonomousComponent castValue = (AutonomousComponent) value;

            return castValue.getName().getText();
        }

        @Override
        public void treeDidChange() {
            expandRow(0);

            super.treeDidChange();
        }
    }

    private static final int VISIBLE_ROW_COUNT = 100;
    
    private static final String DELETE_NETWORK_ACTION_KEY = "deleteNetwork";

    /**
     * Vytvoří správce přehledu.
     * 
     * @param parent
     *            rodičovský kontejner
     * @param systemController
     *            řadič systému
     * @param networkPropertiesController
     *            řadič zobrazení grafů sítí
     * @return správce přehledu v podobě stromu
     */
    public static SystemOverview create(final Container parent,
            final SystemController systemController,
            final NetworkDisplayController networkPropertiesController) {
        return create(parent, DefaultSystemTreeModel.create(systemController),
                systemController, networkPropertiesController);
    }

    /**
     * Vytvoří správce přehledu.
     * 
     * @param parent
     *            rodičovský kontejner
     * @param model
     *            model systémového stromu
     * @param systemController
     *            řadič systému
     * @param networkPropertiesController
     *            řadič zobrazení grafů sítí
     * @return správce přehledu v podobě stromu
     */
    static SystemOverview create(final Container parent,
            final SystemTreeModel model,
            final SystemController systemController,
            final NetworkDisplayController networkPropertiesController) {
        Preconditions.checkNotNull(parent);
        Preconditions.checkNotNull(model);
        Preconditions.checkNotNull(systemController);
        Preconditions.checkNotNull(networkPropertiesController);

        final SystemTree tree = new SystemTree(model);
        tree.setEditable(true);
        tree.setVisibleRowCount(VISIBLE_ROW_COUNT);

        final SystemOverview newInstance =
                new SystemOverview(systemController,
                        networkPropertiesController, tree);
        tree.addMouseListener(newInstance.new DefaultDoubleClickListener());
        tree.addTreeSelectionListener(newInstance.new DefaultSelectionListener());
        
        tree.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), DELETE_NETWORK_ACTION_KEY);
        tree.getActionMap().put(DELETE_NETWORK_ACTION_KEY, newInstance.new DefaultDeleteAction());
        newInstance.addNetworkButton.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                newInstance.addNetwork();
            }
        });
        
        
        tree.setBackground(UIManager.getColor("Tree.textBackground"));

        systemController.addView(newInstance);
        systemController.fill(newInstance);
        
        newInstance.networksScrollPane.setViewportView(tree);
        parent.setLayout(new BorderLayout());
        parent.add(newInstance.networksScrollPane, BorderLayout.CENTER);
        parent.add(newInstance.addNetworkButton, BorderLayout.SOUTH);
        
        return newInstance;
    }

    private final SystemController systemController;

    private final NetworkDisplayController networkPropertiesController;
    
    private final JScrollPane networksScrollPane = new JScrollPane();
    private final SystemTree tree;
    private final JButton addNetworkButton = new JButton(UiLocalizer.print("AddNetwork"));

    private SystemOverview(final SystemController systemController,
            final NetworkDisplayController networkPropertiesController,
            final SystemTree tree) {
        Preconditions.checkNotNull(systemController);
        Preconditions.checkNotNull(networkPropertiesController);
        Preconditions.checkNotNull(tree);

        this.systemController = systemController;
        this.networkPropertiesController = networkPropertiesController;
        this.tree = tree;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.views.SystemView#closed
     * ()
     */
    @Override
    public void closed() {
        removeFromParent();
        unsubscribe();
    }

    private SystemTreeModel getModel() {
        return (SystemTreeModel) this.tree.getModel();
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.views.SystemView#
     * networkAdded
     * (cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network)
     */
    @Override
    public void networkAdded(final Network added) {
        getModel().networkAdded(added);
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.views.SystemView#
     * networkRemoved
     * (cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network)
     */
    @Override
    public void networkRemoved(final Network network) {
        getModel().networkRemoved(network);
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.views.SystemView#
     * networkRenamed
     * (cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network)
     */
    @Override
    public void networkRenamed(final Network network) {
        getModel().networkRenamed(network);
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.views.SystemView#
     * networkSelected
     * (cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network)
     */
    @Override
    public void networkSelected(final Network network) {
    }

    private void removeFromParent() {
        final Container networksPaneParent = networksScrollPane.getParent();
        final Container addButtonParent = this.addNetworkButton.getParent();
        Preconditions.checkState(Components.hasParent(networksPaneParent));
        Preconditions.checkState(Components.hasParent(addButtonParent));

        networksPaneParent.remove(this.networksScrollPane);
        addButtonParent.remove(this.addNetworkButton);
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.views.SystemView#
     * systemNameChanged
     * (cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System)
     */
    @Override
    public void systemNameChanged(final System system) {
        getModel().systemNameChanged(system);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.views.SystemView#systemSet
     * (cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System)
     */
    @Override
    public void systemSet(final System system) {
        getModel().systemSet(system);
    }

    private void unsubscribe() {
        this.systemController.removeView(this);
    }

    private void addNetwork() {
        final Object addedNetworkNameInput =
                JOptionPane.showInputDialog(
                        Intended.<Component> nullReference(),
                        UiLocalizer.print("NewNetworkName"),
                        UiLocalizer.print("NewNetworkNameTitle"),
                        JOptionPane.QUESTION_MESSAGE);
        if (Components.hasUserCanceledInput(addedNetworkNameInput)) {
            return;
        }

        assert addedNetworkNameInput instanceof String;
        try {
            SystemOverview.this.systemController
                    .addNetwork((String) addedNetworkNameInput);
        } catch (final IllegalArgumentException ex) {
            JOptionPane
                    .showMessageDialog(
                            Intended.<Component> nullReference(),
                            UiLocalizer
                                    .print("NetworkNameImpossibleMessage"),
                            UiLocalizer
                                    .print("NetworkNameImpossibleTitle"),
                            JOptionPane.ERROR_MESSAGE);
        }
    }
}
