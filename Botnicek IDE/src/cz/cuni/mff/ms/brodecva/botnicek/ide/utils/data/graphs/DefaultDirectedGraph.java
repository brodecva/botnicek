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
package cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.BiMap;
import com.google.common.collect.Collections2;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.SetMultimap;

import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Callback;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Function;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.Presence;

/**
 * <p>
 * Výchozí implementace grafu.
 * </p>
 * <p>
 * Využívá tří struktur:
 * </p>
 * <ul>
 * <li>První je množina, která uchovává vrcholy grafu.</li>
 * <li>Druhá je vzájemně jednoznačné zobrazení hran na spojnice vrcholů, které
 * reprezentují počáteční a koncový vrchol hrany.</li>
 * <li>Ke každém vrcholu je konečně přiřazena množina spojnic, v nichž se vrchol
 * nachází na jednom z krajů spojnice.</li>
 * </ul>
 * <p>
 * Veškeré operace jsou pak přímočarými manipulacemi s těmito strukturami.
 * </p>
 * 
 * @author Václav Brodec
 * @version 1.0
 * @param <V>
 *            typ vrcholů
 * @param <E>
 *            typ hran
 */
public class DefaultDirectedGraph<V, E> implements DirectedGraph<V, E>,
        Serializable {

    /**
     * Spojka mezi vrcholy. Pomáhá definovat, které vrcholy jsou propojeny, aniž
     * by se tak dělo s užitím typu hrany.
     * 
     * @author Václav Brodec
     * @version 1.0
     * @param <T>
     *            typ konců spojky
     */
    private static final class Joint<T> implements Serializable {
        private static final long serialVersionUID = 1L;

        private T start;
        private T end;

        private Joint(final T start, final T end) {
            Preconditions.checkNotNull(start);
            Preconditions.checkNotNull(end);
            Preconditions.checkArgument(!start.equals(end));

            this.start = start;
            this.end = end;
        }

        /**
         * Vrátí konec spojky.
         * 
         * @return konec spojky
         */
        public T getEnd() {
            return this.end;
        }

        /**
         * Vrátí začátek spojky.
         * 
         * @return začátek spojky
         */
        public T getStart() {
            return this.start;
        }

        private void readObject(final ObjectInputStream objectInputStream)
                throws ClassNotFoundException, IOException {
            objectInputStream.defaultReadObject();

            Preconditions.checkNotNull(this.start);
            Preconditions.checkNotNull(this.end);
            Preconditions.checkArgument(!this.start.equals(this.end));
        }

        /**
         * Nahradí souhlasný kraj spojky novým.
         * 
         * @param fresh
         *            nový kraj
         * @param old
         *            jeden z krajů spojky
         */
        public void replace(final T fresh, final T old) {
            if (this.start.equals(old)) {
                this.start = fresh;
            } else if (this.end.equals(old)) {
                this.end = fresh;
            } else {
                throw new IllegalArgumentException();
            }
        }
        
        /**
         * Vrátí protější kraj spojky.
         * 
         * @param ending jeden konec spojky
         * @return druhý konec spojky
         */
        public T getOpposite(final T ending) {
            if (this.start.equals(ending)) {
                return this.end;
            } else if (this.end.equals(ending)) {
                return this.start;
            } else {
                throw new IllegalArgumentException();
            }
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return "Joint [start=" + this.start + ", end=" + this.end + "]";
        }

        private void writeObject(final ObjectOutputStream objectOutputStream)
                throws IOException {
            objectOutputStream.defaultWriteObject();
        }
    }

    private static final long serialVersionUID = 1L;

    /**
     * Vytvoří prázdný multigraf.
     * 
     * @return multigraf
     */
    public static <V, E> DefaultDirectedGraph<V, E> create() {
        return new DefaultDirectedGraph<>();
    }

    private final Set<V> vertices = new HashSet<V>();
    private final BiMap<E, Joint<V>> edgesToEndings = HashBiMap.create();

    private final SetMultimap<V, Joint<V>> verticesToEndings = HashMultimap
            .create();

    /**
     * Vytvoří prázdný multigraf.
     */
    protected DefaultDirectedGraph() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.utils.
     * DirectedMultigraph#add(java.lang.Object, java.lang.Object,
     * java.lang.Object)
     */
    @Override
    public final void add(final E edge, final V from, final V to) {
        Preconditions.checkNotNull(edge);
        Preconditions.checkNotNull(from);
        Preconditions.checkNotNull(to);

        Preconditions.checkArgument(!from.equals(to));
        Preconditions.checkArgument(!this.edgesToEndings.containsKey(edge));

        Preconditions.checkArgument(this.vertices.contains(from));
        Preconditions.checkArgument(this.vertices.contains(to));

        final Joint<V> created = new Joint<V>(from, to);
        this.edgesToEndings.put(edge, created);
        this.verticesToEndings.put(from, created);
        this.verticesToEndings.put(to, created);
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.utils.
     * DirectedMultigraph#add(java.lang.Object)
     */
    @Override
    public final void add(final V vertex) {
        Preconditions.checkNotNull(vertex);

        Preconditions.checkArgument(!this.vertices.contains(vertex));

        this.vertices.add(vertex);
    }

    private Set<E>
            connections(final V vertex, final Predicate<Joint<V>> filter) {
        Preconditions.checkNotNull(vertex);
        Preconditions.checkNotNull(filter);

        final Set<Joint<V>> allJoints = this.verticesToEndings.get(vertex);

        final Collection<Joint<V>> directedJoints =
                Collections2.filter(allJoints, filter);

        final ImmutableSet.Builder<E> resultBuilder = ImmutableSet.builder();
        for (final Joint<V> joint : directedJoints) {
            resultBuilder.add(this.edgesToEndings.inverse().get(joint));
        }

        return resultBuilder.build();
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.utils.
     * DirectedMultigraph#containsEdge(java.lang.Object)
     */
    @Override
    public final boolean containsEdge(final E edge) {
        Preconditions.checkNotNull(edge);

        return this.edgesToEndings.containsKey(edge);
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.utils.
     * DirectedMultigraph#containsVertex(java.lang.Object)
     */
    @Override
    public final boolean containsVertex(final V vertex) {
        Preconditions.checkNotNull(vertex);

        return this.vertices.contains(vertex);
    }

    /**
     * Metoda volaná při odstraňování hrany.
     * 
     * @param removed
     *            odstraňovaná hrana
     */
    protected void edgeRemovalHook(final E removed) {
    }

    /**
     * Metoda volaná při nahrazování hrany.
     * 
     * @param old
     *            stará verze
     * @param fresh
     *            nová verze
     */
    protected void edgeReplacementHook(final E old, final E fresh) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.utils.
     * DirectedMultigraph#edges()
     */
    @Override
    public final Set<E> edges() {
        return ImmutableSet.copyOf(this.edgesToEndings.keySet());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DirectedGraph#
     * extractVertex(java.lang.Object,
     * cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Function,
     * cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Callback,
     * cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Callback)
     */
    @Override
    public final void
            extractVertex(final V vertex,
                    final Function<? super V, V> neighboursRepair,
                    final Callback<? super V> neighboursCall,
                    final Callback<? super E> connectionsCall)
                    throws IllegalArgumentException {
        Preconditions.checkNotNull(vertex);
        Preconditions.checkNotNull(neighboursCall);
        Preconditions.checkNotNull(connectionsCall);
        Preconditions.checkNotNull(neighboursRepair);
        Preconditions.checkArgument(this.vertices.contains(vertex));

        // Odstranění spojek.
        final Set<Joint<V>> joints = removeJointsForRemoved(vertex);
        final Set<V> neighbours = removeJointsForNeighbours(joints, vertex);
        final Set<Entry<Joint<V>, E>> jointEntries =
                removeJointsForEdges(joints);

        // Odstranění vrcholu.
        this.vertices.remove(vertex);

        try {
            checkEdges(jointEntries, connectionsCall);
            checkNeighbours(neighbours, neighboursCall);
        } catch (final Exception e) {
            // Navrácení vrcholu
            this.vertices.add(vertex);

            // Navrácení spojek
            restoreJointsForEdges(jointEntries);
            restoreJointsForNeighbours(joints, vertex);
            restoreJointsForRemoved(joints, vertex);

            throw e;
        }

        vertexRemovalHook(vertex);
        for (final Entry<Joint<V>, E> jointEntry : jointEntries) {
            edgeRemovalHook(jointEntry.getValue());
        }

        // Oprava sousedů
        for (final V neighbour : neighbours) {
            replaceVertex(neighbour, neighboursRepair.apply(neighbour));
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.utils.
     * DirectedMultigraph#from(java.lang.Object)
     */
    @Override
    public final V from(final E edge) {
        final Joint<V> joint = this.edgesToEndings.get(edge);
        Preconditions.checkArgument(Presence.isPresent(joint));

        return joint.getStart();
    }

    private void checkEdges(final Set<Entry<Joint<V>, E>> jointEntries,
            final Callback<? super E> connectionsCall) {
        for (final Entry<Joint<V>, E> jointEntry : jointEntries) {
            connectionsCall.call(jointEntry.getValue());
        }
    }

    private void checkNeighbours(final Set<V> neighbours,
            final Callback<? super V> neighboursCall) {
        for (final V neighbour : neighbours) {
            neighboursCall.call(neighbour);
        }
    }

    @Override
    public final Set<E> ins(final V vertex) {
        return connections(vertex, new Predicate<Joint<V>>() {
            @Override
            public boolean apply(final Joint<V> input) {
                return input.getEnd().equals(vertex);
            }
        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DirectedGraph#
     * outs(java.lang.Object)
     */
    @Override
    public final Set<E> outs(final V vertex) {
        return connections(vertex, new Predicate<Joint<V>>() {
            @Override
            public boolean apply(final Joint<V> input) {
                return input.getStart().equals(vertex);
            }
        });
    }

    private void readObject(final ObjectInputStream objectInputStream)
            throws ClassNotFoundException, IOException {
        objectInputStream.defaultReadObject();

        Preconditions.checkNotNull(this.edgesToEndings);
        Preconditions.checkNotNull(this.vertices);
        Preconditions.checkNotNull(this.verticesToEndings);
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.utils.
     * DirectedMultigraph#removeEdge(java.lang.Object)
     */
    @Override
    public final void removeEdge(final E edge) {
        Preconditions.checkNotNull(edge);

        final Joint<V> affected = this.edgesToEndings.remove(edge);
        Preconditions.checkArgument(Presence.isPresent(affected));

        final boolean startContained =
                this.verticesToEndings.remove(affected.getStart(), affected);
        assert startContained;

        final boolean endContained =
                this.verticesToEndings.remove(affected.getEnd(), affected);
        assert endContained;

        edgeRemovalHook(edge);
    }

    private Set<Entry<Joint<V>, E>> removeJointsForEdges(
            final Set<? extends Joint<V>> joints) {
        final ImmutableSet.Builder<Entry<Joint<V>, E>> entriesBuilder =
                ImmutableSet.builder();
        for (final Joint<V> joint : joints) {
            final E edge = this.edgesToEndings.inverse().remove(joint);

            final Entry<Joint<V>, E> entry =
                    new AbstractMap.SimpleImmutableEntry<>(joint, edge);
            entriesBuilder.add(entry);
        }

        return entriesBuilder.build();
    }

    private Set<V> removeJointsForNeighbours(final Set<Joint<V>> joints,
            final V vertex) {
        final Set<V> neighbours = new HashSet<>(joints.size());
        for (final Joint<V> joint : joints) {
            final V start = joint.getStart();
            final V end = joint.getEnd();

            if (vertex.equals(start)) {
                this.verticesToEndings.remove(end, joint);
                neighbours.add(end);
            } else {
                this.verticesToEndings.remove(start, joint);
                neighbours.add(start);
            }
        }
        return neighbours;
    }

    private Set<Joint<V>> removeJointsForRemoved(final V vertex) {
        final Set<Joint<V>> joints = this.verticesToEndings.removeAll(vertex);
        return joints;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.utils.
     * DirectedMultigraph#removeVertex(java.lang.Object)
     */
    @Override
    public final void removeVertex(final V vertex) {
        Preconditions.checkNotNull(vertex);

        Preconditions.checkArgument(this.vertices.remove(vertex));

        vertexRemovalHook(vertex);

        final Set<Joint<V>> joints = removeJointsForRemoved(vertex);
        for (final Joint<V> affected : joints) {
            final V opposite = affected.getOpposite(vertex);
            
            final boolean oppositeRemovedNow = this.verticesToEndings.remove(opposite, affected);
            assert oppositeRemovedNow;
            
            final E removed = this.edgesToEndings.inverse().remove(affected);

            assert Presence.isPresent(removed);
            edgeRemovalHook(removed);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.utils.
     * DirectedMultigraph#replaceEdge(java.lang.Object, java.lang.Object)
     */
    @Override
    public final void replaceEdge(final E old, final E fresh) {
        Preconditions.checkNotNull(fresh);
        Preconditions.checkNotNull(old);

        final Joint<V> affected = this.edgesToEndings.get(old);
        Preconditions.checkArgument(Presence.isPresent(affected));

        this.edgesToEndings.inverse().forcePut(affected, fresh);

        edgeReplacementHook(old, fresh);
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.utils.
     * DirectedMultigraph#replaceVertex(java.lang.Object, java.lang.Object)
     */
    @Override
    public final void replaceVertex(final V old, final V fresh) {
        Preconditions.checkNotNull(fresh);
        Preconditions.checkNotNull(old);

        final boolean present = this.vertices.remove(old);
        Preconditions.checkArgument(present);

        final Set<Joint<V>> joints = removeJointsForRemoved(old);
        for (final Joint<V> affected : joints) {
            affected.replace(fresh, old);
        }
        restoreJointsForRemoved(joints, fresh);
        this.vertices.add(fresh);

        vertexReplacementHook(old, fresh);
    }

    private void
            restoreJointsForEdges(final Set<Entry<Joint<V>, E>> jointsEdges) {
        for (final Entry<Joint<V>, E> entry : jointsEdges) {
            this.edgesToEndings.inverse().put(entry.getKey(), entry.getValue());
        }
    }

    private void restoreJointsForNeighbours(final Set<Joint<V>> joints,
            final V vertex) {
        for (final Joint<V> joint : joints) {
            final V start = joint.getStart();
            final V end = joint.getEnd();

            if (vertex.equals(start)) {
                this.verticesToEndings.put(end, joint);
            } else if (vertex.equals(end)) {
                this.verticesToEndings.put(start, joint);
            }
        }
    }

    private void restoreJointsForRemoved(final Set<Joint<V>> joints,
            final V vertex) {
        this.verticesToEndings.putAll(vertex, joints);
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.utils.
     * DirectedMultigraph#to(java.lang.Object)
     */
    @Override
    public final V to(final E edge) {
        final Joint<V> joint = this.edgesToEndings.get(edge);
        Preconditions.checkArgument(Presence.isPresent(joint));

        return joint.getEnd();
    }

    /**
     * Metoda volaná při odstraňování vrcholu.
     * 
     * @param removed
     *            odstraňovaný vrchol
     */
    protected void vertexRemovalHook(final V removed) {
    }

    /**
     * Metoda volaná při nahrazování vrcholu.
     * 
     * @param old
     *            stará verze
     * @param fresh
     *            nová verze
     */
    protected void vertexReplacementHook(final V old, final V fresh) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.utils.
     * DirectedMultigraph#vertices()
     */
    @Override
    public final Set<V> vertices() {
        return ImmutableSet.copyOf(this.vertices);
    }

    private void writeObject(final ObjectOutputStream objectOutputStream)
            throws IOException {
        objectOutputStream.defaultWriteObject();
    }
}
