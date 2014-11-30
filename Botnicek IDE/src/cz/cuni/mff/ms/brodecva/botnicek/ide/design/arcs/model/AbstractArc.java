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
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.Visitor;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.types.Priority;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.Objects;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.Direction;

/**
 * Abstraktní hrana.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public abstract class AbstractArc implements Arc, Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Výchozí priorita hrany.
     */
    protected final static Priority DEFAULT_PRIORITY = Priority.of(1);

    private final Network parent;
    private final NormalWord name;
    private final Priority priority;

    /**
     * Vytvoří hranu.
     * 
     * @param parent
     *            rodičovská síť
     * @param name
     *            název hrany
     * @param priority
     *            priorita
     */
    protected AbstractArc(final Network parent, final NormalWord name,
            final Priority priority) {
        Preconditions.checkNotNull(parent);
        Preconditions.checkNotNull(name);
        Preconditions.checkNotNull(priority);

        this.parent = parent;
        this.name = name;
        this.priority = priority;
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
    public final void accept(final Visitor visitor) {
        visitor.visit(this);

        final Node to = getTo();
        if (!visitor.visited(to)) {
            to.accept(visitor);
        }
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

        if (getClass() != obj.getClass()) {
            return false;
        }

        final AbstractArc other = (AbstractArc) obj;
        if (!this.name.equals(other.name)) {
            return false;
        }
        if (!this.parent.equals(other.parent)) {
            return false;
        }
        if (this.priority != other.priority) {
            return false;
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc#getAttached
     * (cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.Direction)
     */
    @Override
    public final Node getAttached(final Direction direction) {
        Preconditions.checkNotNull(direction);

        return this.parent.getAttached(this, direction);
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc#getFrom()
     */
    @Override
    public final Node getFrom() {
        return getAttached(Direction.IN);
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc#getName()
     */
    @Override
    public final NormalWord getName() {
        return this.name;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc#getNetwork()
     */
    @Override
    public final Network getNetwork() {
        return this.parent;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc#getPriority()
     */
    @Override
    public final Priority getPriority() {
        return this.priority;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc#getTo()
     */
    @Override
    public final Node getTo() {
        return getAttached(Direction.OUT);
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
        result = prime * result + this.name.hashCode();
        result = prime * result + this.parent.hashCode();
        result = prime * result + this.priority.hashCode();
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc#isAttached
     * (cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node,
     * cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.Direction)
     */
    @Override
    public final boolean isAttached(final Node node, final Direction direction) {
        final Node attached = getAttached(direction);

        return attached.equals(node);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc#isFrom(cz.
     * cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node)
     */
    @Override
    public final boolean isFrom(final Node node) {
        return isAttached(node, Direction.OUT);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc#isTo(cz.cuni
     * .mff.ms.brodecva.botnicek.ide.design.nodes.model.Node)
     */
    @Override
    public final boolean isTo(final Node node) {
        return isAttached(node, Direction.IN);
    }

    private void readObject(final ObjectInputStream objectInputStream)
            throws ClassNotFoundException, IOException {
        objectInputStream.defaultReadObject();

        Preconditions.checkNotNull(this.name);
        Preconditions.checkNotNull(this.parent);
        Preconditions.checkNotNull(this.priority);
    }

    private void writeObject(final ObjectOutputStream objectOutputStream)
            throws IOException {
        objectOutputStream.defaultWriteObject();
    }
}
