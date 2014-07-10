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
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.utils;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
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
    private final SetMultimap<V, Joint<V>> verticesToEndings = HashMultimap.create();
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
    public void removeVertex(final V vertex) {
        Preconditions.checkNotNull(vertex);
        
        final boolean contained = this.vertices.remove(vertex);
        Preconditions.checkArgument(contained);
        
        final Set<Joint<V>> joints = this.verticesToEndings.get(vertex);
        for (final Joint<V> affected : joints) {
            this.edgesToEndings.inverse().remove(affected);
        }
    }
    
    @Override
    public void extractVertex(final V vertex, final Function<V, V> neighboursRepair, final Callback<V> neighboursCall, final Callback<E> connectionsCall) throws IllegalArgumentException {
        Preconditions.checkNotNull(vertex);
        Preconditions.checkNotNull(neighboursCall);
        Preconditions.checkNotNull(connectionsCall);
        Preconditions.checkNotNull(neighboursRepair);
        Preconditions.checkArgument(this.vertices.contains(vertex));
        
        final Set<Joint<V>> joints = this.verticesToEndings.get(vertex);
        final Set<V> neighbours = getNeighbours(vertex, joints);
        
        for (final Joint<V> joint : joints) {
            final E connection = this.edgesToEndings.inverse().get(joint);
            connectionsCall.call(connection);
        }
               
        final Set<Entry<Joint<V>, E>> jointEntries = removeJoints(joints);
        this.verticesToEndings.removeAll(vertex);
        this.vertices.remove(vertex);
        
        try {
            for (final V neighbour : neighbours) {
                neighboursCall.call(neighbour);
            }
            
            for (final V neighbour : neighbours) {
                replaceVertex(neighboursRepair.apply(neighbour), neighbour);
            }
        } catch (final Exception e) {
            this.vertices.add(vertex);
            this.verticesToEndings.putAll(vertex, joints);
            addJoints(jointEntries);
            throw e;
        }
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
     * @param vertex
     * @param joints
     * @param connectionsCall 
     */
    private Set<Entry<Joint<V>, E>> removeJoints(final Set<? extends Joint<V>> joints) {
        final ImmutableSet.Builder<Entry<Joint<V>, E>> entriesBuilder = ImmutableSet.builder(); 
        for (final Joint<V> joint : joints) {
            final E edge = this.edgesToEndings.inverse().remove(joint);
            
            final Entry<Joint<V>, E> entry = new AbstractMap.SimpleImmutableEntry<>(joint, edge);
            entriesBuilder.add(entry);
        }
        
        return entriesBuilder.build();
    }
    
    private void addJoints(final Set<Entry<Joint<V>, E>> jointsEdges) {
        this.edgesToEndings.inverse().entrySet().addAll(jointsEdges);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.utils.DirectedMultigraph#removeEdge(java.lang.Object)
     */
    @Override
    public void removeEdge(final E edge) {
        Preconditions.checkNotNull(edge);
        
        final Joint<V> affected = this.edgesToEndings.get(edge);
        Preconditions.checkArgument(affected != null);
        
        this.verticesToEndings.remove(affected.getStart(), affected);
        this.verticesToEndings.remove(affected.getEnd(), affected);
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
        
        final Set<Joint<V>> joints = this.verticesToEndings.removeAll(old);
        for (final Joint<V> affected : joints) {
            affected.replace(fresh, old);
        }
        this.verticesToEndings.putAll(fresh, joints);
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
        Preconditions.checkArgument(!allJoints.isEmpty());
        
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
