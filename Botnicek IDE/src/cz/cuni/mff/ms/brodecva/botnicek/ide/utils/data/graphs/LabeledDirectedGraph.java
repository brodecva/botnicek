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

/**
 * <p>
 * Graf s nepovinnými, ale jednoznačnými popisky vrcholů a hran jednotných typů.
 * </p>
 * <p>
 * Umožňuje k označeným vrcholům přistupovat přes popisek.
 * </p>
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
public interface LabeledDirectedGraph<V, L, E, M> extends DirectedGraph<V, E> {
    /**
     * Přidá hranu do grafu.
     * 
     * @param edge
     *            hrana
     * @param label
     *            popisek hrany
     * @param from
     *            výchozí vrchol hrany
     * @param to
     *            cílový vrchol hrany
     */
    void add(E edge, M label, V from, V to);

    /**
     * Přidá vrchol do grafu.
     * 
     * @param vertex
     *            vrchol
     * @param label
     *            popisek vrcholu
     */
    void add(V vertex, L label);

    /**
     * Vrátí hranu s daným popiskem.
     * 
     * @param label
     *            popisek hrany
     * @return hrana
     */
    E getEdge(M label);

    /**
     * Vrátí uzel s daným popiskem.
     * 
     * @param label
     *            popisek uzlu
     * @return uzel
     */
    V getVertex(L label);

    /**
     * Nahradí hranu a její popisek.
     * 
     * @param old
     *            stará hrana
     * @param fresh
     *            nová hrana
     * @param freshLabel
     *            popisek nové hrany
     */
    void replaceEdge(final E old, final E fresh, final M freshLabel);

    /**
     * Nahradí vrchol a jeho popisek.
     * 
     * @param old
     *            starý vrchol
     * @param fresh
     *            nový vrchol
     * @param freshLabel
     *            popisek nového vrcholu
     */
    void replaceVertex(final V old, final V fresh, final L freshLabel);
}
