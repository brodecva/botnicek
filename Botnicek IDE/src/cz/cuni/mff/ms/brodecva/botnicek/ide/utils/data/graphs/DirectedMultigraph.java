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
 * Orientovaný multigraf.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @param <V> typu vrcholu
 * @param <E> typ hrany
 */
public interface DirectedMultigraph<V, E> {
    /**
     * Indikuje přítomnost vrcholu v grafu.
     * 
     * @param vertex vrchol
     * @return zda-li graf obsahuje vrchol
     */
    boolean containsVertex(V vertex);
    
    /**
     * Indikuje přítomnost hrany v grafu.
     * 
     * @param edge hrana
     * @return zda-li graf obsahuje hranu
     */
    boolean containsEdge(E edge);
    
    /**
     * Přidá izolovaný vrchol do grafu.
     * 
     * @param vertex vrchol
     */
    void add(V vertex);
    
    /**
     * Přidá mezi vrcholy hranu.
     * 
     * @param edge hrana
     * @param from výchozí vrchol
     * @param to cílový vrchol
     */
    void add(E edge, V from, V to);
    
    /**
     * Odstraní vrchol a související hrany. Lze užít i v případě, že vrchol se v grafu nenachází
     * 
     * @param vertex vrchol
     * @return zda-li byl graf změněn
     */
    boolean removeVertex(V vertex);
    
    /**
     * Odebere vrchol z grafu a opraví okolí.
     * 
     * @param vertex odstraňovaný vrchol
     * @param neighboursRepair funkce změny okolních vrcholů
     * @param neighbours zpětné volání pro každého souseda
     * @param connections zpětné volání pro každou odebranou související hranu
     */
    void extractVertex(V vertex, Function<V, V> neighboursRepair, Callback<V> neighbours, Callback<E> connections);
    
    /**
     * Odstraní hranu. Lze užít i v případě, že vrchol se v grafu nenachází
     * 
     * @param edge hrana
     * @return zda-li byl graf změněn
     */
    boolean removeEdge(E edge);
    
    /**
     * Nahradí vrchol.
     * 
     * @param fresh nový vrchol
     * @param old starý vrchol
     */
    void replaceVertex(V fresh, V old);
    
    /**
     * Nahradí hranu.
     * 
     * @param fresh nová hrana
     * @param old stará hrana
     */
    void replaceEdge(E fresh, E old);
    
    /**
     * Vrátí množinu vrcholů grafu.
     * 
     * @return množina vrcholů
     */
    Set<V> vertices();
    
    /**
     * Vrátí množinu hran grafu.
     * 
     * @return množina hran
     */
    Set<E> edges();
    
    /**
     * Vrátí množinu hran vstupujících do vrcholu.
     * 
     * @param vertex vrchol
     * @return množina vstupujících hran
     */
    Set<E> ins(V vertex);
    
    /**
     * Vrátí množinu hran vystupujících do vrcholu.
     * 
     * @param vertex vrchol
     * @return množina vystupujících hran
     */
    Set<E> outs(V vertex);
    
    /**
     * Vrátí výchozí vrchol hrany.
     * 
     * @param arc hrana
     * @return výchozí vrchol
     */
    V from(E arc);
    
    /**
     * Vrátí cílový vrchol hrany.
     * 
     * @param arc hrana
     * @return cílový vrchol
     */
    V to(E arc);
}
