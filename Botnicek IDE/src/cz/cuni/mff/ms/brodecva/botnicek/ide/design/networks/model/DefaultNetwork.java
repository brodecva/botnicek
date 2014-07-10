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
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model;

import java.util.Set;
import java.util.UUID;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.Visitable;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.Visitor;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.EnterNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.IsolatedNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.DefaultSystem;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.utils.Direction;


/**
 * @author Václav Brodec
 * @version 1.0
 */
public final class DefaultNetwork implements Visitable, Network {
    
    private final System parent;
    private final UUID id;
    
    public static Network create(final UUID id, final DefaultSystem parent) {
        return new DefaultNetwork(id, parent);
    }
    
    public static DefaultNetwork create(final DefaultSystem parent) {
        return new DefaultNetwork(UUID.randomUUID(), parent);
    }
    
    private DefaultNetwork(final UUID id, final DefaultSystem parent) {
        Preconditions.checkNotNull(id);
        Preconditions.checkNotNull(parent);
        
        this.id = id;
        this.parent = parent;
    }
    
    /**
     * @return the id
     */
    @Override
    public UUID getId() {
        return id;
    }
    
    @Override
    public void setName(final String name) {
        Preconditions.checkNotNull(name);
        Preconditions.checkArgument(!name.isEmpty());
        
        this.parent.renameNetwork(this, name);
    }
    
    /**
     * @return
     */
    @Override
    public String getName() {
        return this.parent.getNetworkName(this);
    }

    /**
     * @return the parent
     */
    @Override
    public final System getParent() {
        return parent;
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.Visitable#accept(cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.Visitor)
     */
    @Override
    public void accept(final Visitor visitor) {
        visitor.visit(this);
        
        final Set<IsolatedNode> isolatedNodes = this.parent.getIsolatedNodes(this);
        for (final IsolatedNode isolatedNode : isolatedNodes) {
            isolatedNode.accept(visitor);
        }
        
        final Set<EnterNode> initialNodes = this.parent.getInitialNodes(this);
        for (final EnterNode initialNode : initialNodes) {
            initialNode.accept(visitor);
        }
    }
    
    @Override
    public void addNode(final int x, final int y) {
        this.parent.addNode(this, x, y);
    }
    
    @Override
    public void removeNode(final NormalWord name) {
        this.parent.removeNode(name);
    }
    
    @Override
    public void addArc(final NormalWord name, final NormalWord fromName, NormalWord toName) {
        this.parent.addArc(this, name, fromName, toName);
    }

    /**
     * @param name
     */
    @Override
    public void removeArc(final NormalWord name) {
        this.parent.removeArc(name);
    }

    /**
     * @param node
     * @return
     */
    @Override
    public Set<Arc> getOuts(final Node node) {
        return this.parent.getOuts(node);
    }

    /**
     * @param abstractNode
     * @return
     */
    @Override
    public Set<Arc> getIns(final Node node) {
        return this.parent.getIns(node);
    }

    /**
     * @param abstractArc
     * @param direction
     * @return
     */
    @Override
    public Node getAttached(final Arc arc, final Direction direction) {
        return this.parent.getAttached(arc, direction);
    }

    /**
     * @param direction
     * @return
     */
    @Override
    public Set<Arc> getConnections(final Node node, final Direction direction) {
        return this.parent.getConnections(node, direction);
    }

    /**
     * @param name
     * @return
     */
    @Override
    public Node getNode(NormalWord name) {
        return this.parent.getNode(name);
    }

    /**
     * @param arcName
     * @return
     */
    @Override
    public Arc getArc(NormalWord arcName) {
        return this.parent.getArc(arcName);
    }

    /**
     * @return
     */
    @Override
    public Set<EnterNode> getAvailableReferences() {
        return this.parent.getAvailableReferences();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id.hashCode();
        result = prime * result + parent.hashCode();
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof DefaultNetwork)) {
            return false;
        }
        final DefaultNetwork other = (DefaultNetwork) obj;
        if (!id.equals(other.id)) {
            return false;
        }
        if (!parent.equals(other.parent)) {
            return false;
        }
        return true;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("id", this.id)
                .add("parent", this.parent)
                .add("name", getName())
                .toString();
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network#adjoins(cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node, cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node, cz.cuni.mff.ms.brodecva.botnicek.ide.design.utils.Direction)
     */
    @Override
    public boolean adjoins(Node first, Node second, Direction direction) {
        return this.parent.adjoins(first, second, direction);
    }
}
