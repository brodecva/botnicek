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
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Set;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.Visitor;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.Objects;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.Direction;

/**
 * Abstraktní uzel.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public abstract class AbstractNode implements Node, Serializable {
    private static final long serialVersionUID = 1L;

    private final NormalWord name;
    private final Network parent;

    private final int x;
    private final int y;

    /**
     * Kopírovací konstruktor.
     * 
     * @param original
     *            původní uzel
     */
    protected AbstractNode(final Node original) {
        Preconditions.checkNotNull(original);

        this.name = original.getName();
        this.parent = original.getNetwork();

        this.x = original.getX();
        this.y = original.getY();
    }

    /**
     * Vytvoří uzel dle parametrů.
     * 
     * @param name
     *            název uzlu
     * @param parent
     *            rodičovská síť
     * @param x
     *            umístění uzlu v souřadnici x
     * @param y
     *            umístění uzlu v souřadnici y
     */
    protected AbstractNode(final NormalWord name, final Network parent,
            final int x, final int y) {
        Preconditions.checkNotNull(name);
        Preconditions.checkNotNull(parent);
        Preconditions.checkArgument(x >= 0);
        Preconditions.checkArgument(y >= 0);

        this.name = name;
        this.parent = parent;

        this.x = x;
        this.y = y;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.Visitable#accept(cz.cuni
     * .mff.ms.brodecva.botnicek.ide.design.api.Visitor)
     */
    @Override
    public final void accept(final Visitor visitor) {
        visitor.visitEnter(this);

        final Set<Arc> outs = this.parent.getOuts(this);
        for (final Arc arc : outs) {
            arc.accept(visitor);
        }

        visitor.visitExit(this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node#adjoins(
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node,
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.utils.Direction)
     */
    @Override
    public boolean adjoins(final Node neighbour, final Direction direction) {
        return this.parent.adjoins(this, neighbour, direction);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node#equals(java
     * .lang.Object)
     */
    @Override
    public final boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if (Objects.isNull(obj)) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        final AbstractNode other = (AbstractNode) obj;
        if (!this.name.equals(other.name)) {
            return false;
        }
        if (!this.parent.equals(other.parent)) {
            return false;
        }
        if (this.x != other.x) {
            return false;
        }
        if (this.y != other.y) {
            return false;
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.nodes.Node
     * #getConnections
     * (cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.Direction)
     */
    @Override
    public Set<Arc> getConnections(final Direction direction) {
        return this.parent.getConnections(this, direction);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.nodes.Node
     * #getDegree
     * (cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.Direction)
     */
    @Override
    public int getDegree(final Direction direction) {
        return this.parent.getConnections(this, direction).size();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.nodes.Node
     * #getInDegree()
     */
    @Override
    public int getInDegree() {
        return getDegree(Direction.IN);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node#getIns()
     */
    @Override
    public final Set<Arc> getIns() {
        return this.parent.getIns(this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node#getName()
     */
    @Override
    public final NormalWord getName() {
        return this.name;
    }

    @Override
    public Network getNetwork() {
        return this.parent;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.nodes.Node
     * #getOutDegree()
     */
    @Override
    public int getOutDegree() {
        return getDegree(Direction.OUT);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node#getOuts()
     */
    @Override
    public final Set<Arc> getOuts() {
        return this.parent.getOuts(this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node#getX()
     */
    @Override
    public final int getX() {
        return this.x;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node#getY()
     */
    @Override
    public final int getY() {
        return this.y;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.name.hashCode();
        result = prime * result + this.parent.hashCode();
        result = prime * result + this.x;
        result = prime * result + this.y;
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node#isDirectedAtBy
     * (cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node)
     */
    @Override
    public boolean isPointedAtBy(final Node node) {
        return adjoins(node, Direction.IN);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node#directsAt
     * (cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node)
     */
    @Override
    public boolean pointsTo(final Node node) {
        return adjoins(node, Direction.OUT);
    }

    private void readObject(final ObjectInputStream objectInputStream)
            throws ClassNotFoundException, IOException {
        objectInputStream.defaultReadObject();

        Preconditions.checkNotNull(this.name);
        Preconditions.checkNotNull(this.parent);
        Preconditions.checkArgument(this.x >= 0);
        Preconditions.checkArgument(this.y >= 0);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Node [name=");
        builder.append(this.name.getText());
        builder.append(", parent=");
        builder.append(this.parent.getName().getText());
        builder.append(", x=");
        builder.append(this.x);
        builder.append(", y=");
        builder.append(this.y);
        builder.append(", getClass()=");
        builder.append(getClass());
        builder.append("]");
        return builder.toString();
    }

    private void writeObject(final ObjectOutputStream objectOutputStream)
            throws IOException {
        objectOutputStream.defaultWriteObject();
    }
}
