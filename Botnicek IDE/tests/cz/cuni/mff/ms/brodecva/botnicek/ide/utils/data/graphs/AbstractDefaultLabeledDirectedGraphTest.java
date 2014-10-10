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

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Intended;

/**
 * Testuje implementaci {@link LabeledDirectedGraph}.
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
 * @see DefaultLabeledDirectedGraph
 */
public abstract class AbstractDefaultLabeledDirectedGraphTest<V, L, E, M>
        extends AbstractDefaultDirectedGraphTest<V, E> {

    private DefaultLabeledDirectedGraph<V, L, E, M> tested = Intended
            .nullReference();
    private DefaultLabeledDirectedGraph<V, L, E, M> testedWithVertices =
            Intended.nullReference();
    private V firstPresentVertex = Intended.nullReference();
    private V secondPresentVertex = Intended.nullReference();
    private DefaultLabeledDirectedGraph<V, L, E, M> testedWithVerticesAndEdge =
            Intended.nullReference();
    private E edgeBetweenPresentVertices = Intended.nullReference();
    private M edgeBetweenPresentVerticesLabel = Intended.nullReference();
    private L firstPresentVertexLabel = Intended.nullReference();

    @SuppressWarnings("unused")
    private L secondPresentVertexLabel = Intended.nullReference();

    /**
     * Vytvoří instanci popisku hrany.
     * 
     * @return instance popisku hrany
     */
    protected M createEdgeLabel() {
        return createUniqueEdgeLabels(1)[0];
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.
     * AbstractDefaultDirectedGraphTest#createEmpty()
     */
    @Override
    protected abstract DefaultLabeledDirectedGraph<V, L, E, M> createEmpty();

    /**
     * Vytvoří zadaný počet navzájem různých popisků hran.
     * 
     * @param count
     *            počet
     * @return popisky
     */
    protected abstract M[] createUniqueEdgeLabels(int count);

    /**
     * Vytvoří zadaný počet navzájem různých popisků vrcholů.
     * 
     * @param count
     *            počet
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
     * Vytvoří testovací objekty.
     * 
     * @throws java.lang.Exception
     *             pokud dojde k vyhození výjimky
     */
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();

        this.tested = createEmpty();

        final V[] presentVertices = createUniqueVertices(GENERATED_COUNT);
        final L[] presentVertexLabels =
                createUniqueVertexLabels(GENERATED_COUNT);

        this.testedWithVertices = createEmpty();
        for (int i = 0; i < GENERATED_COUNT; i++) {
            this.testedWithVertices.add(presentVertices[i],
                    presentVertexLabels[i]);
        }
        this.firstPresentVertex = presentVertices[FIRST_PICK];
        this.secondPresentVertex = presentVertices[SECOND_PICK];
        this.firstPresentVertexLabel = presentVertexLabels[FIRST_PICK];
        this.secondPresentVertexLabel = presentVertexLabels[SECOND_PICK];

        this.testedWithVerticesAndEdge = createEmpty();
        for (int i = 0; i < GENERATED_COUNT; i++) {
            this.testedWithVerticesAndEdge.add(presentVertices[i],
                    presentVertexLabels[i]);
        }
        this.edgeBetweenPresentVertices = createEdge();
        this.edgeBetweenPresentVerticesLabel = createEdgeLabel();
        this.testedWithVerticesAndEdge.add(this.edgeBetweenPresentVertices,
                this.edgeBetweenPresentVerticesLabel, this.firstPresentVertex,
                this.secondPresentVertex);
    }

    /**
     * Uklidí testovací objekty.
     * 
     * @throws java.lang.Exception
     *             pokud dojde k vyhození výjimky
     */
    @Override
    @After
    public void tearDown() throws Exception {
        super.tearDown();

        this.tested = Intended.nullReference();
        this.testedWithVertices = Intended.nullReference();
        this.firstPresentVertex = Intended.nullReference();
        this.secondPresentVertex = Intended.nullReference();
        this.testedWithVerticesAndEdge = Intended.nullReference();
        this.edgeBetweenPresentVertices = Intended.nullReference();
        this.edgeBetweenPresentVerticesLabel = Intended.nullReference();
        this.firstPresentVertexLabel = Intended.nullReference();
        this.secondPresentVertexLabel = Intended.nullReference();
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultLabeledDirectedGraph#create()}
     * .
     */
    @Override
    @Test
    public void testCreate() {
        createEmpty();
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultLabeledDirectedGraph#getEdge(java.lang.Object)}
     * .
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetEdgeWhenNotPresent() {
        final M label = createEdgeLabel();

        this.tested.getEdge(label);
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultLabeledDirectedGraph#getEdge(java.lang.Object)}
     * and
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultLabeledDirectedGraph#add(Object, Object, Object, Object)}
     * .
     */
    @Test
    public void testGetEdgeWhenPresent() {
        final E edge = createEdge();
        final M label = createEdgeLabel();

        this.testedWithVertices.add(edge, label, this.firstPresentVertex,
                this.secondPresentVertex);

        assertEquals(edge, this.testedWithVertices.getEdge(label));
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultLabeledDirectedGraph#getVertex(java.lang.Object)}
     * .
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetVertexWhenNotPresent() {
        final L label = createVertexLabel();

        this.tested.getVertex(label);
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultLabeledDirectedGraph#getVertex(java.lang.Object)}
     * and
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultLabeledDirectedGraph#add(Object, Object)}
     * .
     */
    @Test
    public void testGetVertexWhenPresent() {
        final V vertex = createVertex();
        final L label = createVertexLabel();

        this.tested.add(vertex, label);

        assertEquals(vertex, this.tested.getVertex(label));
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultLabeledDirectedGraph#removeEdge(Object)}
     * and
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultLabeledDirectedGraph#getEdge(Object)}
     * .
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemoveEdgeAndGetEdge() {
        this.testedWithVerticesAndEdge
                .removeEdge(this.edgeBetweenPresentVertices);
        this.testedWithVerticesAndEdge
                .getEdge(this.edgeBetweenPresentVerticesLabel);
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultLabeledDirectedGraph#removeVertex(Object)}
     * and
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultLabeledDirectedGraph#getEdge(Object)}
     * .
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemoveVertexAndGetEdgeAdjoined() {
        this.testedWithVerticesAndEdge.removeVertex(this.firstPresentVertex);
        this.testedWithVerticesAndEdge
                .getEdge(this.edgeBetweenPresentVerticesLabel);
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultLabeledDirectedGraph#removeVertex(Object)}
     * and
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultLabeledDirectedGraph#getVertex(Object)}
     * .
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemoveVertexAndGetVertex() {
        this.testedWithVertices.removeVertex(this.firstPresentVertex);
        this.testedWithVertices.getVertex(this.firstPresentVertexLabel);
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultLabeledDirectedGraph#replaceEdge(Object, Object)}
     * and
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultLabeledDirectedGraph#getEdge(Object)}
     * .
     */
    @Test
    public void testReplaceEdgeAndGetEdgeReturnsNew() {
        final E newEdge = createEdge();

        this.testedWithVerticesAndEdge.replaceEdge(
                this.edgeBetweenPresentVertices, newEdge);
        assertEquals(newEdge,
                this.testedWithVerticesAndEdge
                        .getEdge(this.edgeBetweenPresentVerticesLabel));
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultLabeledDirectedGraph#replaceVertex(Object, Object)}
     * and
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultLabeledDirectedGraph#getVertex(Object)}
     * .
     */
    @Test
    public void testReplaceVertexAndGetVertexReturnsNew() {
        final V newVertex = createVertex();

        this.testedWithVertices.replaceVertex(this.firstPresentVertex,
                newVertex);
        assertEquals(newVertex,
                this.testedWithVertices.getVertex(this.firstPresentVertexLabel));
    }

}
