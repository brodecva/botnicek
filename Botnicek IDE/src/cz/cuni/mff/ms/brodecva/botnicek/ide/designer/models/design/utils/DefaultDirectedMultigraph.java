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
package cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.utils;

import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.BiMap;
import com.google.common.collect.Collections2;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.Direction;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.nodes.Node;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public class DefaultDirectedMultigraph<V, E> implements DirectedMultigraph<V, E> {
    
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
         * @return the start
         */
        public T getStart() {
            return this.start;
        }

        /**
         * @return the end
         */
        public T getEnd() {
            return this.end;
        }
        
        public void replace(final T fresh, final T old) {
            if (this.start.equals(old)) {
                this.start = fresh;
            } else if (this.end.equals(old)) {
                this.end = fresh;
            } else {
                throw new IllegalArgumentException();
            }
        }
    }
    
    private final Set<V> vertices = new HashSet<V>();
    private final Map<V, Set<Joint<V>>> verticesToEndings = new HashMap<>();
    private final BiMap<E, Joint<V>> edgesToEndings = HashBiMap.create();
    
    public static <V, E> DirectedMultigraph<V, E> create() {
        return new DefaultDirectedMultigraph<>();
    }
    
    private DefaultDirectedMultigraph() {
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.utils.DirectedMultigraph#add(java.lang.Object)
     */
    @Override
    public void add(final V vertex) {
        Preconditions.checkNotNull(vertex);
        
        Preconditions.checkArgument(!this.vertices.contains(vertex));
        
        this.vertices.add(vertex);
        this.verticesToEndings.put(vertex, new HashSet<Joint<V>>());
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
        this.verticesToEndings.get(from).add(created);
        this.verticesToEndings.get(to).add(created);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.utils.DirectedMultigraph#removeVertex(java.lang.Object)
     */
    @Override
    public void removeVertex(final V vertex) {
        Preconditions.checkNotNull(vertex);
        
        final boolean contained = this.vertices.remove(vertex);
        Preconditions.checkArgument(contained);
        
        final Collection<Joint<V>> joints = this.verticesToEndings.get(vertex);
        for (final Joint<V> affected : joints) {
            this.edgesToEndings.inverse().remove(affected);
        }
    }
    
    @Override
    public void extractVertex(final V vertex, final Function<V, V> neighboursRepair, final Callback<V> neighboursCall, final Callback<E> connectionsCall) throws IllegalArgumentException {
        Preconditions.checkNotNull(vertex);
        Preconditions.checkArgument(this.vertices.contains(vertex));
        
        final Collection<Joint<V>> joints = this.verticesToEndings.get(vertex);
        final Set<V> neighbours = getNeighbours(vertex, joints);
        
        for (final Joint<V> joint : joints) {
            final E connection = this.edgesToEndings.inverse().get(joint);
            connectionsCall.call(connection);
        }
        for (final V neighbour : neighbours) {
            neighboursCall.call(neighbour);
        }
               
        this.vertices.remove(vertex);
        remove(joints);
        
        repair(neighbours, neighboursRepair);
    }

    /**
     * @param neighbours
     * @param neighboursRepair
     */
    private void repair(final Set<V> neighbours,
            final Function<V, V> neighboursRepair) {
        final BiMap<V, V> repairTransitions =
                projectNeighboursRepairs(neighboursRepair, neighbours);
        applyRepairments(repairTransitions);
    }

    /**
     * @param vertex
     * @param joints
     * @return
     */
    private Set<V> getNeighbours(final V vertex,
            final Collection<Joint<V>> joints) {
        final Set<V> affected = new HashSet<>(joints.size());
        for (final Joint<V> joint : joints) {
            final V start = joint.getStart();
            final V end = joint.getEnd();
            
            affected.add(vertex.equals(start) ? end : start);
        }
        return affected;
    }

    /**
     * @param neighboursRepair
     * @param affected
     * @return
     */
    private BiMap<V, V> projectNeighboursRepairs(
            final Function<V, V> neighboursRepair, final Set<V> affected) {
        final BiMap<V, V> oldToNovel = HashBiMap.create();
        for (final V old : affected) {
            oldToNovel.put(old, neighboursRepair.apply(old));
        }
        return oldToNovel;
    }

    /**
     * @param vertex
     * @param joints
     * @param connectionsCall 
     */
    private void remove(final Collection<Joint<V>> joints) {
        for (final Joint<V> joint : joints) {
            this.edgesToEndings.inverse().remove(joint);
        }
    }

    /**
     * @param oldToNovel
     * @param neighboursCall 
     */
    private void applyRepairments(final BiMap<V, V> oldToNovel) {
        for (final Entry<V, V> transition : oldToNovel.entrySet()) {
            final V old = transition.getKey();
            final V novel = transition.getValue();
            
            replaceVertex(novel, old);
        }
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.utils.DirectedMultigraph#removeEdge(java.lang.Object)
     */
    @Override
    public void removeEdge(final E edge) {
        Preconditions.checkNotNull(edge);
        
        final Joint<V> affected = this.edgesToEndings.get(edge);
        Preconditions.checkArgument(affected != null);
        
        final Collection<Joint<V>> outJoints = this.verticesToEndings.get(affected.getStart());
        final Collection<Joint<V>> inJoints = this.verticesToEndings.get(affected.getEnd());
        
        outJoints.remove(affected);
        inJoints.remove(affected);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.utils.DirectedMultigraph#replaceVertex(java.lang.Object, java.lang.Object)
     */
    @Override
    public void replaceVertex(final V fresh, final V old) {
        Preconditions.checkNotNull(fresh);
        Preconditions.checkNotNull(old);
        
        Preconditions.checkArgument(this.vertices.contains(old));
        
        final Iterable<Joint<V>> joints = this.verticesToEndings.get(old);
        for (final Joint<V> affected : joints) {
            affected.replace(fresh, old);
        }
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
        return new HashSet<>(this.vertices);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.utils.DirectedMultigraph#edges()
     */
    @Override
    public Set<E> edges() {
        return new HashSet<>(this.edgesToEndings.keySet());
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
        
        final Collection<Joint<V>> allJoints = this.verticesToEndings.get(vertex);
        Preconditions.checkArgument(allJoints != null);
        
        final Collection<Joint<V>> directedJoints = Collections2.filter(allJoints, filter);
        
        final Set<E> result = new HashSet<>();
        for (final Joint<V> joint : directedJoints) {
            result.add(this.edgesToEndings.inverse().get(joint));
        }
        
        return result;
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
