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

/**
 * <p>Výchozí implementace grafu.</p>
 * <p>Využívá tří struktur:</p>
 * <ul>
 *     <li>První je množina, která uchovává vrcholy grafu.</li>
 *     <li>Druhá je vzájemně jednoznačné zobrazení hran na spojnice vrcholů, které reprezentují počáteční a koncový vrchol hrany.</li>
 *     <li>Ke každém vrcholu je konečně přiřazena množina spojnic, v nichž se vrchol nachází na jednom z krajů spojnice.</li>
 * </ul>
 * <p>Veškeré operace jsou pak přímočarými manipulacemi s těmito strukturami.</p>
 * 
 * @author Václav Brodec
 * @version 1.0
 * @param <V> typ vrcholů
 * @param <E> typ hran
 */
public class DefaultDirectedGraph<V, E> implements DirectedMultigraph<V, E> {
    
    /**
     * Spojka mezi vrcholy. Pomáhá definovat, které vrcholy jsou propojeny, aniž by se tak dělo s užitím typu hrany.
     * 
     * @author Václav Brodec
     * @version 1.0
     * @param <T> typ konců spojky
     */
    private static final class Joint<T> {
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
         * Vrátí začátek spojky.
         * 
         * @return začátek spojky
         */
        public T getStart() {
            return this.start;
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
         * Nahradí souhlasný kraj spojky novým.
         * 
         * @param fresh nový kraj
         * @param old jeden z krajů spojky
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

        /* (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return "Joint [start=" + start + ", end=" + end + "]";
        }
    }
    
    private final Set<V> vertices = new HashSet<V>();
    private final BiMap<E, Joint<V>> edgesToEndings = HashBiMap.create();
    private final SetMultimap<V, Joint<V>> verticesToEndings = HashMultimap.create();
    
    /**
     * Vytvoří prázdný multigraf.
     * 
     * @return multigraf
     */
    public static <V, E> DirectedMultigraph<V, E> create() {
        return new DefaultDirectedGraph<>();
    }
    
    private DefaultDirectedGraph() {
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.utils.DirectedMultigraph#add(java.lang.Object)
     */
    @Override
    public void add(final V vertex) {
        Preconditions.checkNotNull(vertex);
        
        Preconditions.checkArgument(!this.vertices.contains(vertex));
        
        this.vertices.add(vertex);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.utils.DirectedMultigraph#add(java.lang.Object, java.lang.Object, java.lang.Object)
     */
    @Override
    public void add(final E edge, final V from, final V to) {
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

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.utils.DirectedMultigraph#removeVertex(java.lang.Object)
     */
    @Override
    public boolean removeVertex(final V vertex) {
        Preconditions.checkNotNull(vertex);
        
        final boolean contained = this.vertices.remove(vertex);
        if (!contained) {
            return false;
        }
        
        final Set<Joint<V>> joints = removeJointsForRemoved(vertex);
        for (final Joint<V> affected : joints) {
            this.edgesToEndings.inverse().remove(affected);
        }
        
        return true;
    }
    
    @Override
    public void extractVertex(final V vertex, final Function<V, V> neighboursRepair, final Callback<V> neighboursCall, final Callback<E> connectionsCall) throws IllegalArgumentException {
        Preconditions.checkNotNull(vertex);
        Preconditions.checkNotNull(neighboursCall);
        Preconditions.checkNotNull(connectionsCall);
        Preconditions.checkNotNull(neighboursRepair);
        Preconditions.checkArgument(this.vertices.contains(vertex));
        
        // Odstranění spojek.
        final Set<Joint<V>> joints = removeJointsForRemoved(vertex);
        final Set<V> neighbours = removeJointsForNeighbours(joints, vertex);
        final Set<Entry<Joint<V>, E>> jointEntries = removeJointsForEdges(joints);
        
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
        
        // Oprava sousedů
        for (final V neighbour : neighbours) {
            replaceVertex(neighboursRepair.apply(neighbour), neighbour);
        }
    }

    private void checkEdges(final Set<Entry<Joint<V>, E>> jointEntries,
            final Callback<E> connectionsCall) {
        for (final Entry<Joint<V>, E> jointEntry : jointEntries) {
            connectionsCall.call(jointEntry.getValue());
        }
    }

    private void restoreJointsForNeighbours(final Set<Joint<V>> joints, final V vertex) {        
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

    private void checkNeighbours(final Set<V> neighbours,
            final Callback<V> neighboursCall) {
        for (final V neighbour : neighbours) {
            neighboursCall.call(neighbour);
        }
    }
    
    private void restoreJointsForRemoved(final Set<Joint<V>> joints,
            final V vertex) {
        this.verticesToEndings.putAll(vertex, joints);
    }


    private Set<Joint<V>> removeJointsForRemoved(final V vertex) {
        final Set<Joint<V>> joints = this.verticesToEndings.removeAll(vertex);
        return joints;
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

    private Set<Entry<Joint<V>, E>> removeJointsForEdges(final Set<? extends Joint<V>> joints) {
        final ImmutableSet.Builder<Entry<Joint<V>, E>> entriesBuilder = ImmutableSet.builder(); 
        for (final Joint<V> joint : joints) {
            final E edge = this.edgesToEndings.inverse().remove(joint);
            
            final Entry<Joint<V>, E> entry = new AbstractMap.SimpleImmutableEntry<>(joint, edge);
            entriesBuilder.add(entry);
        }
        
        return entriesBuilder.build();
    }
    
    private void restoreJointsForEdges(final Set<Entry<Joint<V>, E>> jointsEdges) {
        for (final Entry<Joint<V>, E> entry : jointsEdges) {
            this.edgesToEndings.inverse().put(entry.getKey(), entry.getValue());
        }
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.utils.DirectedMultigraph#removeEdge(java.lang.Object)
     */
    @Override
    public boolean removeEdge(final E edge) {
        Preconditions.checkNotNull(edge);
        
        final Joint<V> affected = this.edgesToEndings.get(edge);
        if (affected == null) {
            return false;
        }
        
        this.verticesToEndings.remove(affected.getStart(), affected);
        this.verticesToEndings.remove(affected.getEnd(), affected);
        
        return true;
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.utils.DirectedMultigraph#replaceVertex(java.lang.Object, java.lang.Object)
     */
    @Override
    public void replaceVertex(final V fresh, final V old) {
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
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.utils.DirectedMultigraph#replaceEdge(java.lang.Object, java.lang.Object)
     */
    @Override
    public void replaceEdge(final E fresh, final E old) {
        Preconditions.checkNotNull(fresh);
        Preconditions.checkNotNull(old);
        
        final Joint<V> affected = this.edgesToEndings.get(old);
        Preconditions.checkArgument(affected != null);
        
        this.edgesToEndings.inverse().forcePut(affected, fresh);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.utils.DirectedMultigraph#containsVertex(java.lang.Object)
     */
    @Override
    public boolean containsVertex(final V vertex) {
        Preconditions.checkNotNull(vertex);
        
        return this.vertices.contains(vertex);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.utils.DirectedMultigraph#containsEdge(java.lang.Object)
     */
    @Override
    public boolean containsEdge(final E edge) {
        Preconditions.checkNotNull(edge);
        
        return this.edgesToEndings.containsKey(edge);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.utils.DirectedMultigraph#vertices()
     */
    @Override
    public Set<V> vertices() {
        return ImmutableSet.copyOf(this.vertices);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.utils.DirectedMultigraph#edges()
     */
    @Override
    public Set<E> edges() {
        return ImmutableSet.copyOf(this.edgesToEndings.keySet());
    }
    
    public Set<E> outs(final V vertex) {
        return connections(vertex, new Predicate<Joint<V>>() {
            @Override
            public boolean apply(final Joint<V> input) {
                return input.getStart().equals(vertex);
            }
        });
    }
    
    public Set<E> ins(final V vertex) {
        return connections(vertex, new Predicate<Joint<V>>() {
            @Override
            public boolean apply(final Joint<V> input) {
                return input.getEnd().equals(vertex);
            }
        });
    }
    
    private Set<E> connections(final V vertex, final Predicate<Joint<V>> filter) {
        Preconditions.checkNotNull(vertex);
        Preconditions.checkNotNull(filter);
        
        final Set<Joint<V>> allJoints = this.verticesToEndings.get(vertex);
        
        final Collection<Joint<V>> directedJoints = Collections2.filter(allJoints, filter);
        
        final ImmutableSet.Builder<E> resultBuilder = ImmutableSet.builder();
        for (final Joint<V> joint : directedJoints) {
            resultBuilder.add(this.edgesToEndings.inverse().get(joint));
        }
        
        return resultBuilder.build();
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.utils.DirectedMultigraph#from(java.lang.Object)
     */
    @Override
    public V from(final E edge) {
        final Joint<V> joint = this.edgesToEndings.get(edge);
        Preconditions.checkArgument(joint != null);
        
        return joint.getStart();
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.utils.DirectedMultigraph#to(java.lang.Object)
     */
    @Override
    public V to(final E edge) {
        final Joint<V> joint = this.edgesToEndings.get(edge);
        Preconditions.checkArgument(joint != null);
        
        return joint.getEnd();
    }
}
