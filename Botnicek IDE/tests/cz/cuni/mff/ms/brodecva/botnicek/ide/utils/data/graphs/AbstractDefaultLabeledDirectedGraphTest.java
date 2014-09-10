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

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Intended;

/**
 * Testuje implementaci {@link LabeledDirectedGraph}.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @param <V> typ vrcholu
 * @param <L> typ popisku vrcholu
 * @param <E> typ hrany
 * @param <M> typ popisku hrany
 * @see DefaultLabeledDirectedGraph
 */
public abstract class AbstractDefaultLabeledDirectedGraphTest<V, L, E, M> extends AbstractDefaultDirectedGraphTest<V, E> {

    private DefaultLabeledDirectedGraph<V, L, E, M> tested = Intended.nullReference();
    private DefaultLabeledDirectedGraph<V, L, E, M> testedWithVertices = Intended.nullReference();
    private V firstPresentVertex = Intended.nullReference();
    private V secondPresentVertex = Intended.nullReference();
    private DefaultLabeledDirectedGraph<V, L, E, M> testedWithVerticesAndEdge = Intended.nullReference();
    private E edgeBetweenPresentVertices = Intended.nullReference();
    private M edgeBetweenPresentVerticesLabel = Intended.nullReference();
    private L firstPresentVertexLabel = Intended.nullReference();
    
    @SuppressWarnings("unused")
    private L secondPresentVertexLabel = Intended.nullReference();

    /**
     * Vytvoří testovací objekty.
     * 
     * @throws java.lang.Exception pokud dojde k vyhození výjimky
     */
    @Before
    public void setUp() throws Exception {
        super.setUp();
        
        tested = createEmpty();
        
        final V[] presentVertices = createUniqueVertices(GENERATED_COUNT);
        final L[] presentVertexLabels = createUniqueVertexLabels(GENERATED_COUNT);
        
        this.testedWithVertices = createEmpty();
        for (int i = 0; i < GENERATED_COUNT; i++) {            
            this.testedWithVertices.add(presentVertices[i], presentVertexLabels[i]);
        }
        this.firstPresentVertex = presentVertices[FIRST_PICK];
        this.secondPresentVertex = presentVertices[SECOND_PICK];
        this.firstPresentVertexLabel = presentVertexLabels[FIRST_PICK];
        this.secondPresentVertexLabel = presentVertexLabels[SECOND_PICK];
        
        this.testedWithVerticesAndEdge = createEmpty();
        for (int i = 0; i < GENERATED_COUNT; i++) {            
            this.testedWithVerticesAndEdge.add(presentVertices[i], presentVertexLabels[i]);
        }
        this.edgeBetweenPresentVertices = createEdge();
        this.edgeBetweenPresentVerticesLabel = createEdgeLabel();
        this.testedWithVerticesAndEdge.add(this.edgeBetweenPresentVertices, this.edgeBetweenPresentVerticesLabel, this.firstPresentVertex, this.secondPresentVertex);
    }
    
    /**
     * Uklidí testovací objekty.
     * 
     * @throws java.lang.Exception pokud dojde k vyhození výjimky
     */
    @After
    public void tearDown() throws Exception {
        super.tearDown();
        
        tested = Intended.nullReference();
        testedWithVertices = Intended.nullReference();
        firstPresentVertex = Intended.nullReference();
        secondPresentVertex = Intended.nullReference();
        testedWithVerticesAndEdge = Intended.nullReference();
        edgeBetweenPresentVertices = Intended.nullReference();
        edgeBetweenPresentVerticesLabel = Intended.nullReference();
        firstPresentVertexLabel = Intended.nullReference();
        secondPresentVertexLabel = Intended.nullReference();
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.AbstractDefaultDirectedGraphTest#createEmpty()
     */
    protected abstract DefaultLabeledDirectedGraph<V, L, E, M> createEmpty();

    /**
     * Vytvoří zadaný počet navzájem různých popisků vrcholů.
     * 
     * @param count počet
     * @return popisky
     */
    protected abstract L[] createUniqueVertexLabels(int count);
    
    /**
     * Vytvoří instanci popisku vrcholu.
     * 
     * @return instance popisku vrcholu
     */
    protected L createVertexLabel() {
        return createUniqueVertexLabels(1)[0];
    }
    
    /**
     * Vytvoří zadaný počet navzájem různých popisků hran.
     * 
     * @param count počet
     * @return popisky
     */
    protected abstract M[] createUniqueEdgeLabels(int count);
    
    /**
     * Vytvoří instanci popisku hrany.
     * 
     * @return instance popisku hrany
     */
    protected M createEdgeLabel() {
        return createUniqueEdgeLabels(1)[0];
    }


    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultLabeledDirectedGraph#create()}.
     */
    @Test
    public void testCreate() {
        createEmpty();
    }

    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultLabeledDirectedGraph#getVertex(java.lang.Object)} and {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultLabeledDirectedGraph#add(Object, Object)}.
     */
    @Test
    public void testGetVertexWhenPresent() {
        final V vertex = createVertex();
        final L label = createVertexLabel();
        
        tested.add(vertex, label);
        
        assertEquals(vertex, tested.getVertex(label));
    }
    
    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultLabeledDirectedGraph#getVertex(java.lang.Object)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetVertexWhenNotPresent() {
        final L label = createVertexLabel();
        
        tested.getVertex(label);
    }

    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultLabeledDirectedGraph#getEdge(java.lang.Object)} and {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultLabeledDirectedGraph#add(Object, Object, Object, Object)}.
     */
    @Test
    public void testGetEdgeWhenPresent() {
        final E edge = createEdge();
        final M label = createEdgeLabel();
        
        testedWithVertices.add(edge, label, this.firstPresentVertex, this.secondPresentVertex);
        
        assertEquals(edge, testedWithVertices.getEdge(label));
    }

    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultLabeledDirectedGraph#getEdge(java.lang.Object)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetEdgeWhenNotPresent() {
        final M label = createEdgeLabel();
        
        tested.getEdge(label);
    }

    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultLabeledDirectedGraph#removeVertex(Object)} and {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultLabeledDirectedGraph#getVertex(Object)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemoveVertexAndGetVertex() {
        this.testedWithVertices.removeVertex(firstPresentVertex);
        this.testedWithVertices.getVertex(firstPresentVertexLabel);
    }
    
    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultLabeledDirectedGraph#removeVertex(Object)} and {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultLabeledDirectedGraph#getEdge(Object)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemoveVertexAndGetEdgeAdjoined() {
        this.testedWithVerticesAndEdge.removeVertex(firstPresentVertex);
        this.testedWithVerticesAndEdge.getEdge(edgeBetweenPresentVerticesLabel);
    }
    
    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultLabeledDirectedGraph#replaceVertex(Object, Object)} and {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultLabeledDirectedGraph#getVertex(Object)}.
     */
    @Test
    public void testReplaceVertexAndGetVertexReturnsNew() {
        final V newVertex = createVertex();
        
        this.testedWithVertices.replaceVertex(firstPresentVertex, newVertex);
        assertEquals(newVertex, this.testedWithVertices.getVertex(firstPresentVertexLabel));
    }

    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultLabeledDirectedGraph#removeEdge(Object)} and {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultLabeledDirectedGraph#getEdge(Object)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemoveEdgeAndGetEdge() {
        this.testedWithVerticesAndEdge.removeEdge(edgeBetweenPresentVertices);
        this.testedWithVerticesAndEdge.getEdge(edgeBetweenPresentVerticesLabel);
    }
    
    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultLabeledDirectedGraph#replaceEdge(Object, Object)} and {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultLabeledDirectedGraph#getEdge(Object)}.
     */
    @Test
    public void testReplaceEdgeAndGetEdgeReturnsNew() {
        final E newEdge = createEdge();
        
        this.testedWithVerticesAndEdge.replaceEdge(edgeBetweenPresentVertices, newEdge);
        assertEquals(newEdge, this.testedWithVerticesAndEdge.getEdge(edgeBetweenPresentVerticesLabel));
    }

}
