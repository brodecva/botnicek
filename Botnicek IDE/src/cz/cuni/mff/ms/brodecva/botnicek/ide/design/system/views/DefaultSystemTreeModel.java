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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreePath;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.controllers.SystemController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Intended;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.Presence;

/**
 * Výchozí implementace modelu stromu systému řadí ve výchozí podobě sítě podle
 * názvu. Umožňuje je přejmenovat, stejně jako samotný systém.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
final class DefaultSystemTreeModel implements SystemTreeModel {

    private static final class AlphabeticalNameComparator implements
            Comparator<Network> {
        @Override
        public int compare(final Network first, final Network second) {
            Preconditions.checkNotNull(first, second);

            return first.getName().compareTo(second.getName());
        }
    }

    /**
     * Vytvoří model.
     * 
     * @param systemController
     *            řadič systému
     * @return model
     */
    public static SystemTreeModel
            create(final SystemController systemController) {
        return create(systemController, new AlphabeticalNameComparator());
    }

    /**
     * Vytvoří model.
     * 
     * @param systemController
     *            řadič systému
     * @param comparator
     *            komparátor sítí
     * @return model
     */
    static SystemTreeModel create(final SystemController systemController,
            final Comparator<? super Network> comparator) {
        Preconditions.checkNotNull(systemController);

        final SystemTreeModel newInstance =
                new DefaultSystemTreeModel(systemController, comparator);

        return newInstance;
    }

    private final SystemController systemController;

    private System root = Intended.nullReference();

    private final List<Network> children = new ArrayList<>();

    private final Collection<TreeModelListener> listeners = new Vector<>();

    private final Comparator<? super Network> comparator;

    private DefaultSystemTreeModel(final SystemController systemController,
            final Comparator<? super Network> comparator) {
        Preconditions.checkNotNull(systemController);
        Preconditions.checkNotNull(comparator);

        this.systemController = systemController;
        this.comparator = comparator;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.tree.TreeModel#addTreeModelListener(javax.swing.event.
     * TreeModelListener)
     */
    @Override
    public void addTreeModelListener(final TreeModelListener l) {
        Preconditions.checkNotNull(l);

        this.listeners.add(l);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.tree.TreeModel#getChild(java.lang.Object, int)
     */
    @Override
    public Object getChild(final Object parent, final int index) {
        Preconditions.checkNotNull(parent);
        if (!parent.equals(this.root)) {
            return Intended.nullReference();
        }

        if (index < 0 || index >= this.children.size()) {
            return Intended.nullReference();
        }

        return this.children.get(index);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.tree.TreeModel#getChildCount(java.lang.Object)
     */
    @Override
    public int getChildCount(final Object parent) {
        Preconditions.checkNotNull(parent);
        if (!parent.equals(this.root)) {
            return 0;
        }

        return this.children.size();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.tree.TreeModel#getIndexOfChild(java.lang.Object,
     * java.lang.Object)
     */
    @Override
    public int getIndexOfChild(final Object parent, final Object child) {
        if (parent == Intended.nullReference()
                || child == Intended.nullReference()) {
            return -1;
        }

        if (!parent.equals(this.root)) {
            return -1;
        }

        return this.children.indexOf(child);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.tree.TreeModel#getRoot()
     */
    @Override
    public Object getRoot() {
        return this.root;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.tree.TreeModel#isLeaf(java.lang.Object)
     */
    @Override
    public boolean isLeaf(final Object node) {
        if (node.equals(this.root)) {
            return false;
        }

        return this.children.contains(node);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.views.SystemTreeModel
     * #networkAdded
     * (cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network)
     */
    @Override
    public void networkAdded(final Network added) {
        Preconditions.checkNotNull(added);

        Preconditions.checkArgument(!this.children.contains(added));
        this.children.add(added);
        Collections.sort(this.children, this.comparator);

        final TreeModelEvent event =
                new TreeModelEvent(this, new Object[] { this.root },
                        new int[] { this.children.indexOf(added) },
                        new Object[] { added });

        for (final TreeModelListener listener : this.listeners) {
            listener.treeNodesInserted(event);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.views.SystemTreeModel
     * #networkRemoved
     * (cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network)
     */
    @Override
    public void networkRemoved(final Network network) {
        Preconditions.checkNotNull(network);

        final Network removed = network;

        final int removedIndex = this.children.indexOf(removed);
        Preconditions.checkArgument(removedIndex != -1);
        this.children.remove(removed);

        final TreeModelEvent event =
                new TreeModelEvent(this, new Object[] { this.root },
                        new int[] { removedIndex }, new Object[] { removed });

        for (final TreeModelListener listener : this.listeners) {
            listener.treeNodesRemoved(event);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.views.SystemTreeModel
     * #networkRenamed
     * (cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network)
     */
    @Override
    public void networkRenamed(final Network network) {
        Preconditions.checkNotNull(network);

        final Network removed = network;
        final Network added = network;

        final int removedIndex = this.children.indexOf(removed);
        Preconditions.checkArgument(removedIndex != -1);
        this.children.remove(removedIndex);
        this.children.add(added);

        Collections.sort(this.children, this.comparator);
        final int addIndex = this.children.indexOf(added);

        final TreeModelEvent removeEvent =
                new TreeModelEvent(this, new Object[] { this.root },
                        new int[] { removedIndex }, new Object[] { removed });
        final TreeModelEvent insertEvent =
                new TreeModelEvent(this, new Object[] { this.root },
                        new int[] { addIndex }, new Object[] { added });

        for (final TreeModelListener listener : this.listeners) {
            listener.treeNodesRemoved(removeEvent);
            listener.treeNodesInserted(insertEvent);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.swing.tree.TreeModel#removeTreeModelListener(javax.swing.event.
     * TreeModelListener)
     */
    @Override
    public void removeTreeModelListener(final TreeModelListener l) {
        Preconditions.checkNotNull(l);

        this.listeners.remove(l);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.views.SystemTreeModel
     * #systemNameChanged
     * (cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System)
     */
    @Override
    public void systemNameChanged(final System system) {
        Preconditions.checkNotNull(system);
        Preconditions.checkArgument(this.root.equals(system));

        final TreeModelEvent event =
                new TreeModelEvent(this, new Object[] { this.root },
                        Intended.intArrayNull(), Intended.arrayNull());

        for (final TreeModelListener listener : this.listeners) {
            listener.treeNodesChanged(event);
            listener.treeStructureChanged(event);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.views.SystemTreeModel
     * #systemSet
     * (cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System)
     */
    @Override
    public void systemSet(final System system) {
        Preconditions.checkNotNull(system);

        this.root = system;
        final TreeModelEvent event =
                new TreeModelEvent(this, new Object[] { this.root },
                        Intended.intArrayNull(), Intended.arrayNull());

        for (final TreeModelListener listener : this.listeners) {
            listener.treeNodesChanged(event);
            listener.treeStructureChanged(event);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.swing.tree.TreeModel#valueForPathChanged(javax.swing.tree.TreePath,
     * java.lang.Object)
     */
    @Override
    public void valueForPathChanged(final TreePath path, final Object newValue) {
        Preconditions.checkNotNull(path);
        Preconditions.checkNotNull(newValue);

        final int pathCount = path.getPathCount();
        Preconditions.checkArgument(pathCount > 0);

        final String newName = newValue.toString();
        if (newName.isEmpty()) {
            return;
        }

        final Object replaced = path.getLastPathComponent();
        Preconditions.checkState(Presence.isPresent(replaced));
        if (pathCount == 1) {
            Preconditions.checkState(replaced.equals(this.root));

            try {
                this.systemController.renameSystem(newName);
            } catch (final IllegalArgumentException e) {
                return;
            }
        } else if (pathCount == 2) {
            Preconditions.checkState(this.children.contains(replaced));

            final Network child = (Network) replaced;

            try {
                this.systemController.renameNetwork(child, newName);
            } catch (final IllegalArgumentException e) {
                return;
            }
        } else {
            throw new IllegalStateException();
        }
    }
}
