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

import java.util.Set;

import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Callback;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Function;

/**
 * Orientovaný graf.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @param <V>
 *            typu vrcholu
 * @param <E>
 *            typ hrany
 */
public interface DirectedGraph<V, E> {
    /**
     * Přidá mezi vrcholy hranu.
     * 
     * @param edge
     *            hrana
     * @param from
     *            výchozí vrchol
     * @param to
     *            cílový vrchol
     */
    void add(E edge, V from, V to);

    /**
     * Přidá izolovaný vrchol do grafu.
     * 
     * @param vertex
     *            vrchol
     */
    void add(V vertex);

    /**
     * Indikuje přítomnost hrany v grafu.
     * 
     * @param edge
     *            hrana
     * @return zda-li graf obsahuje hranu
     */
    boolean containsEdge(E edge);

    /**
     * Indikuje přítomnost vrcholu v grafu.
     * 
     * @param vertex
     *            vrchol
     * @return zda-li graf obsahuje vrchol
     */
    boolean containsVertex(V vertex);

    /**
     * Vrátí množinu hran grafu.
     * 
     * @return množina hran
     */
    Set<E> edges();

    /**
     * Odebere vrchol z grafu a opraví okolí.
     * 
     * @param vertex
     *            odstraňovaný vrchol
     * @param neighboursRepair
     *            funkce změny okolních vrcholů
     * @param neighbours
     *            zpětné volání pro každého souseda
     * @param connections
     *            zpětné volání pro každou odebranou související hranu
     */
    void extractVertex(V vertex, Function<V, V> neighboursRepair,
            Callback<V> neighbours, Callback<E> connections);

    /**
     * Vrátí výchozí vrchol hrany.
     * 
     * @param arc
     *            hrana
     * @return výchozí vrchol
     */
    V from(E arc);

    /**
     * Vrátí množinu hran vstupujících do vrcholu.
     * 
     * @param vertex
     *            vrchol
     * @return množina vstupujících hran
     */
    Set<E> ins(V vertex);

    /**
     * Vrátí množinu hran vystupujících do vrcholu.
     * 
     * @param vertex
     *            vrchol
     * @return množina vystupujících hran
     */
    Set<E> outs(V vertex);

    /**
     * Odstraní hranu.
     * 
     * @param edge
     *            hrana
     */
    void removeEdge(E edge);

    /**
     * Odstraní vrchol a související hrany.
     * 
     * @param vertex
     *            vrchol
     */
    void removeVertex(V vertex);

    /**
     * Nahradí hranu.
     * 
     * @param old
     *            stará hrana
     * @param fresh
     *            nová hrana
     */
    void replaceEdge(E old, E fresh);

    /**
     * Nahradí vrchol.
     * 
     * @param old
     *            starý vrchol
     * @param fresh
     *            nový vrchol
     */
    void replaceVertex(V old, V fresh);

    /**
     * Vrátí cílový vrchol hrany.
     * 
     * @param arc
     *            hrana
     * @return cílový vrchol
     */
    V to(E arc);

    /**
     * Vrátí množinu vrcholů grafu.
     * 
     * @return množina vrcholů
     */
    Set<V> vertices();
}
