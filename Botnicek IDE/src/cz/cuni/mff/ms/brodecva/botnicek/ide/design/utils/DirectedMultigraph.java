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

import java.util.Set;

import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public interface DirectedMultigraph<V, E> {
    boolean containsVertex(V vertex);
    boolean containsEdge(E edge);
    void add(V vertex);
    void add(E edge, V from, V to);
    void removeVertex(V vertex);
    void extractVertex(V vertex, Function<V, V> neighboursRepair, Callback<V> neighbours, Callback<E> connections);
    void removeEdge(E edge);
    void replaceVertex(V fresh, V old);
    void replaceEdge(E fresh, E old);
    Set<V> vertices();
    Set<E> edges();
    Set<E> ins(V vertex);
    Set<E> outs(V vertex);
    V from(E arc);
    V to(E arc);
}
