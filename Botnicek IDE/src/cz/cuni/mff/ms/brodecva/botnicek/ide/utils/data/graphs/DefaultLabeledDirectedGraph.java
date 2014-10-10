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
package cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.Presence;

/**
 * Implementace {@link LabeledDirectedGraph}.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @param <V>
 *            typ vrcholu
 * @param <L>
 *            typ popisku vrcholu
 * @param <E>
 *            typ hrany
 * @param <M>
 *            typ popisku hrany
 */
public class DefaultLabeledDirectedGraph<V, L, E, M> extends
        DefaultDirectedGraph<V, E> implements LabeledDirectedGraph<V, L, E, M>,
        Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Vytvoří prázdný multigraf.
     * 
     * @return multigraf
     */
    public static <V, L, E, M> DefaultLabeledDirectedGraph<V, L, E, M> create() {
        return new DefaultLabeledDirectedGraph<V, L, E, M>();
    }

    private final BiMap<L, V> labelsToVertices = HashBiMap.create();

    private final BiMap<M, E> labelsToEdges = HashBiMap.create();

    /**
     * Vytvoří prázdný multigraf.
     */
    protected DefaultLabeledDirectedGraph() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.graph.
     * LabeledDirectedGraph#add(java.lang.Object, java.lang.Object,
     * java.lang.Object, java.lang.Object)
     */
    @Override
    public void add(final E edge, final M label, final V from, final V to) {
        Preconditions.checkNotNull(edge);
        Preconditions.checkNotNull(label);
        Preconditions.checkNotNull(from);
        Preconditions.checkNotNull(to);

        add(edge, from, to);
        this.labelsToEdges.put(label, edge);
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.graph.
     * LabeledDirectedGraph#add(java.lang.Object, java.lang.Object)
     */
    @Override
    public void add(final V vertex, final L label) {
        Preconditions.checkNotNull(vertex);
        Preconditions.checkNotNull(label);

        add(vertex);
        this.labelsToVertices.put(label, vertex);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultDirectedGraph
     * #removeEdge(java.lang.Object)
     */
    @Override
    protected void edgeRemovalHook(final E removed) {
        Preconditions.checkNotNull(removed);

        this.labelsToEdges.inverse().remove(removed);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultDirectedGraph
     * #edgeReplacementHook(java.lang.Object, java.lang.Object)
     */
    @Override
    protected void edgeReplacementHook(final E old, final E fresh) {
        Preconditions.checkNotNull(old);
        Preconditions.checkNotNull(fresh);

        final M label = this.labelsToEdges.inverse().remove(old);
        if (Presence.isPresent(label)) {
            this.labelsToEdges.put(label, fresh);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.graph.
     * LabeledDirectedGraph#getEdge(java.lang.Object)
     */
    @Override
    public E getEdge(final M label) {
        Preconditions.checkNotNull(label);

        final E result = this.labelsToEdges.get(label);
        Preconditions.checkArgument(Presence.isPresent(result));

        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.graph.
     * LabeledDirectedGraph#getVertex(java.lang.Object)
     */
    @Override
    public V getVertex(final L label) {
        Preconditions.checkNotNull(label);

        final V result = this.labelsToVertices.get(label);
        Preconditions.checkArgument(Presence.isPresent(result));

        return result;
    }

    private void readObject(final ObjectInputStream objectInputStream)
            throws ClassNotFoundException, IOException {
        objectInputStream.defaultReadObject();

        Preconditions.checkNotNull(this.labelsToEdges);
        Preconditions.checkNotNull(this.labelsToVertices);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.LabeledDirectedGraph
     * #replaceEdge(java.lang.Object, java.lang.Object, java.lang.Object)
     */
    @Override
    public final void
            replaceEdge(final E old, final E fresh, final M freshLabel) {
        Preconditions.checkNotNull(fresh);
        Preconditions.checkNotNull(old);
        Preconditions.checkNotNull(freshLabel);

        super.replaceEdge(old, fresh);
        this.labelsToEdges.forcePut(freshLabel, fresh);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.LabeledDirectedGraph
     * #replaceVertex(java.lang.Object, java.lang.Object, java.lang.Object)
     */
    @Override
    public void replaceVertex(final V old, final V fresh, final L freshLabel) {
        Preconditions.checkNotNull(fresh);
        Preconditions.checkNotNull(old);
        Preconditions.checkNotNull(freshLabel);

        super.replaceVertex(old, fresh);
        this.labelsToVertices.forcePut(freshLabel, fresh);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultDirectedGraph
     * #vertexRemovalHook(java.lang.Object)
     */
    @Override
    protected void vertexRemovalHook(final V removed) {
        Preconditions.checkNotNull(removed);

        this.labelsToVertices.inverse().remove(removed);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultDirectedGraph
     * #vertexReplacementHook(java.lang.Object, java.lang.Object)
     */
    @Override
    protected void vertexReplacementHook(final V old, final V fresh) {
        Preconditions.checkNotNull(old);
        Preconditions.checkNotNull(fresh);

        final L label = this.labelsToVertices.inverse().remove(old);
        if (Presence.isPresent(label)) {
            this.labelsToVertices.put(label, fresh);
        }
    }

    private void writeObject(final ObjectOutputStream objectOutputStream)
            throws IOException {
        objectOutputStream.defaultWriteObject();
    }
}
