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
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.Visitable;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.Visitor;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.types.SystemName;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.Objects;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.Direction;

/**
 * Výchozí implementace sítě deleguje většinu metod na systém sítí.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class DefaultNetwork implements Visitable, Network, Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Vytvoří prázdnou síť s náhodně přiděleným globálním identifikátorem.
     * 
     * @param parent
     *            rodičovský systém sítí
     * @return síť
     */
    public static DefaultNetwork create(final System parent) {
        return new DefaultNetwork(UUID.randomUUID(), parent);
    }

    /**
     * Vytvoří prázdnou síť.
     * 
     * @param id
     *            identifikátor sítě
     * @param parent
     *            rodičovský systém sítí
     * @return síť
     */
    public static Network create(final UUID id, final System parent) {
        return new DefaultNetwork(id, parent);
    }

    private final System system;

    private final UUID id;

    private DefaultNetwork(final UUID id, final System parent) {
        Preconditions.checkNotNull(id);
        Preconditions.checkNotNull(parent);

        this.id = id;
        this.system = parent;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.Visitable
     * #accept
     * (cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.Visitor)
     */
    @Override
    public void accept(final Visitor visitor) {
        Preconditions.checkNotNull(visitor);

        visitor.visit(this);

        final Set<Node> nodes = this.system.getNodes(this);
        for (final Node node : nodes) {
            if (!visitor.visited(node)) {
                node.accept(visitor);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network#addArc
     * (cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord,
     * cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord,
     * cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord)
     */
    @Override
    public void addArc(final NormalWord name, final NormalWord fromName,
            final NormalWord toName) {
        this.system.addArc(this, name, fromName, toName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network#addNode
     * (int, int)
     */
    @Override
    public void addNode(final int x, final int y) {
        this.system.addNode(this, x, y);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network#adjoins
     * (cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node,
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node,
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.utils.Direction)
     */
    @Override
    public boolean adjoins(final Node first, final Node second,
            final Direction direction) {
        return this.system.adjoins(first, second, direction);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (Objects.isNull(obj)) {
            return false;
        }
        if (!(obj instanceof Network)) {
            return false;
        }
        final Network other = (Network) obj;
        if (!this.id.equals(other.getId())) {
            return false;
        }
        if (!this.system.equals(other.getSystem())) {
            return false;
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network#
     * getAttached(cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc,
     * cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.Direction)
     */
    @Override
    public Node getAttached(final Arc arc, final Direction direction) {
        return this.system.getAttached(arc, direction);
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network#
     * getConnections
     * (cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node,
     * cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.Direction)
     */
    @Override
    public Set<Arc> getConnections(final Node node, final Direction direction) {
        return this.system.getConnections(node, direction);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network#getId
     * ()
     */
    @Override
    public UUID getId() {
        return this.id;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network#getIns
     * (cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node)
     */
    @Override
    public Set<Arc> getIns(final Node node) {
        return this.system.getIns(node);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.AutonomousComponent#getName
     * ()
     */
    @Override
    public SystemName getName() {
        return this.system.getNetworkName(this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network#getOuts
     * (cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node)
     */
    @Override
    public Set<Arc> getOuts(final Node node) {
        return this.system.getOuts(node);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network#getSystem
     * ()
     */
    @Override
    public final System getSystem() {
        return this.system;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.id.hashCode();
        result = prime * result + this.system.hashCode();
        return result;
    }

    private void readObject(final ObjectInputStream objectInputStream)
            throws ClassNotFoundException, IOException {
        objectInputStream.defaultReadObject();

        Preconditions.checkNotNull(this.system);
        Preconditions.checkNotNull(this.id);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "DefaultNetwork [id=" + this.id + ", system=" + this.system
                + "]";
    }

    private void writeObject(final ObjectOutputStream objectOutputStream)
            throws IOException {
        objectOutputStream.defaultWriteObject();
    }
}
