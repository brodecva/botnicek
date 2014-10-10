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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableSet;

import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Callback;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Function;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Intended;

/**
 * Testuje výchozí implementaci grafu.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @param <V>
 *            typ vrcholu
 * @param <E>
 *            typ hrany
 * @see DefaultDirectedGraph
 */
public abstract class AbstractDefaultDirectedGraphTest<V, E> {

    /**
     * Třída k testování toho, zda-li jsou dle specifikace volány chráněné
     * metody.
     * 
     * @author Václav Brodec
     * @version 1.0
     * @param <V>
     *            typ vrcholu
     * @param <E>
     *            typ hrany
     */
    public final static class HookCountingDirectedGraph<V, E> extends
            DefaultDirectedGraph<V, E> {

        private static final long serialVersionUID = 1L;

        private int vertexRemovalHookCallCounter = 0;
        private int edgeRemovalHookCallCounter = 0;
        private int vertexReplacementHookCallCounter = 0;
        private int edgeReplacementHookCallCounter = 0;

        @Override
        protected void edgeRemovalHook(final E removed) {
            this.edgeRemovalHookCallCounter++;
        }

        @Override
        protected void edgeReplacementHook(final E old, final E fresh) {
            this.edgeReplacementHookCallCounter++;
        }

        /**
         * Vrátí počet volání dané metody.
         * 
         * @return počet volání
         */
        public int getEdgeRemovalHookCallCounter() {
            return this.edgeRemovalHookCallCounter;
        }

        /**
         * Vrátí počet volání dané metody.
         * 
         * @return počet volání
         */
        public int getEdgeReplacementHookCallCounter() {
            return this.edgeReplacementHookCallCounter;
        }

        /**
         * Vrátí počet volání dané metody.
         * 
         * @return počet volání
         */
        public int getVertexRemovalHookCallCounter() {
            return this.vertexRemovalHookCallCounter;
        }

        /**
         * Vrátí počet volání dané metody.
         * 
         * @return počet volání
         */
        public int getVertexReplacementHookCallCounter() {
            return this.vertexReplacementHookCallCounter;
        }

        @Override
        protected void vertexRemovalHook(final V removed) {
            this.vertexRemovalHookCallCounter++;
        }

        @Override
        protected void vertexReplacementHook(final V old, final V fresh) {
            this.vertexReplacementHookCallCounter++;
        }
    }

    /**
     * Počet vygenerovaných unikátních testovacích objektů.
     */
    protected static final int GENERATED_COUNT = 10;

    /**
     * Index prvního vybraného.
     */
    protected static final int FIRST_PICK = 3;

    /**
     * Index druhého vybraného.
     */
    protected static final int SECOND_PICK = 8;

    private DefaultDirectedGraph<V, E> tested = Intended.nullReference();
    private DefaultDirectedGraph<V, E> testedWithVertices = Intended
            .nullReference();
    private V firstPresentVertex = Intended.nullReference();
    private V secondPresentVertex = Intended.nullReference();
    private DefaultDirectedGraph<V, E> testedWithVerticesAndEdge = Intended
            .nullReference();
    private E edgeBetweenPresentVertices = Intended.nullReference();
    private HookCountingDirectedGraph<V, E> hookCounterWithVerticesAndEdge =
            Intended.nullReference();

    /**
     * Vytvoří instanci hrany.
     * 
     * @return instance hrany
     */
    protected E createEdge() {
        return createUniqueEdges(1)[0];
    }

    /**
     * Vytvoří prázdnou realizaci generického typu.
     * 
     * @return prázdná instance
     */
    protected abstract DefaultDirectedGraph<V, E> createEmpty();

    /**
     * Vytvoří prázdnou realizaci generického typu.
     * 
     * @return prázdná instance
     */
    protected abstract HookCountingDirectedGraph<V, E> createHookCounter();

    /**
     * Vytvoří zadaný počet navzájem různých hran.
     * 
     * @param count
     *            počet
     * @return hrany
     */
    protected abstract E[] createUniqueEdges(int count);

    /**
     * Vytvoří zadaný počet navzájem různých vrcholů.
     * 
     * @param count
     *            počet
     * @return vrcholy
     */
    protected abstract V[] createUniqueVertices(int count);

    /**
     * Vytvoří instanci vrcholu.
     * 
     * @return instance vrcholu
     */
    protected V createVertex() {
        return createUniqueVertices(1)[0];
    }

    /**
     * Vytvoří testované grafy (prázdný, graf se dvěma izolovanými vrcholy) a
     * označí vložené vrcholy.
     * 
     * @throws java.lang.Exception
     *             pokud je vyhozena výjimka
     */
    @Before
    public void setUp() throws Exception {
        this.tested = createEmpty();

        this.testedWithVertices = createEmpty();
        final V[] presentVertices = createUniqueVertices(GENERATED_COUNT);
        for (final V present : presentVertices) {
            this.testedWithVertices.add(present);
        }
        this.firstPresentVertex = presentVertices[FIRST_PICK];
        this.secondPresentVertex = presentVertices[SECOND_PICK];

        this.testedWithVerticesAndEdge = createEmpty();
        for (final V present : presentVertices) {
            this.testedWithVerticesAndEdge.add(present);
        }
        this.edgeBetweenPresentVertices = createEdge();
        this.testedWithVerticesAndEdge.add(this.edgeBetweenPresentVertices,
                this.firstPresentVertex, this.secondPresentVertex);

        this.hookCounterWithVerticesAndEdge = createHookCounter();
        for (final V present : presentVertices) {
            this.hookCounterWithVerticesAndEdge.add(present);
        }
        this.hookCounterWithVerticesAndEdge.add(
                this.edgeBetweenPresentVertices, this.firstPresentVertex,
                this.secondPresentVertex);
    }

    /**
     * Odstraní testované grafy a označené vrcholy.
     * 
     * @throws java.lang.Exception
     *             pokud je vyhozena výjimka
     */
    @After
    public void tearDown() throws Exception {
        this.tested = Intended.nullReference();
        this.testedWithVertices = Intended.nullReference();
        this.firstPresentVertex = Intended.nullReference();
        this.secondPresentVertex = Intended.nullReference();
        this.testedWithVerticesAndEdge = Intended.nullReference();
        this.edgeBetweenPresentVertices = Intended.nullReference();
        this.hookCounterWithVerticesAndEdge = Intended.nullReference();
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultDirectedGraph#add(Object, Object, Object)}
     * and
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultDirectedGraph#containsEdge(Object)}
     * .
     */
    @Test
    public void testAddEdgeAndExpectContainsNew() {
        final E added = createEdge();

        this.testedWithVertices.add(added, this.firstPresentVertex,
                this.secondPresentVertex);

        assertTrue(this.testedWithVertices.containsEdge(added));
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultDirectedGraph#add(Object, Object, Object)}
     * and
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultDirectedGraph#removeEdge(Object)}
     * and
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultDirectedGraph#edges()}
     * .
     */
    @Test
    public void testAddEdgeAndRemoveTheSameEdgeAndExpectNotPresentInEdgees() {
        final E added = createEdge();

        this.testedWithVertices.add(added, this.firstPresentVertex,
                this.secondPresentVertex);
        this.testedWithVertices.removeEdge(added);

        assertFalse(this.testedWithVertices.edges().contains(added));
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultDirectedGraph#add(Object, Object, Object)}
     * and
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultDirectedGraph#removeEdge(Object)}
     * and
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultDirectedGraph#containsEdge(Object)}
     * .
     */
    @Test
    public void testAddEdgeAndRemoveTheSameEdgeAndExpectThatDoesNotContain() {
        final E added = createEdge();

        this.testedWithVertices.add(added, this.firstPresentVertex,
                this.secondPresentVertex);
        this.testedWithVertices.removeEdge(added);

        assertFalse(this.testedWithVertices.containsEdge(added));
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultDirectedGraph#add(Object, Object, Object)}
     * and
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultDirectedGraph#edges()}
     * .
     */
    @Test
    public void testAddEdgeExpectEdgesContainAdded() {
        final E added = createEdge();

        this.testedWithVertices.add(added, this.firstPresentVertex,
                this.secondPresentVertex);

        assertTrue(this.testedWithVertices.edges().contains(added));
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultDirectedGraph#add(Object, Object, Object)}
     * and
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultDirectedGraph#from(Object)}
     * and
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultDirectedGraph#to(Object)}
     * .
     */
    @Test
    public void testAddEdgeExpectFromAndToAsAdded() {
        final E added = createEdge();

        this.testedWithVertices.add(added, this.firstPresentVertex,
                this.secondPresentVertex);

        assertEquals(this.firstPresentVertex,
                this.testedWithVertices.from(added));
        assertEquals(this.secondPresentVertex,
                this.testedWithVertices.to(added));
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultDirectedGraph#add(Object, Object, Object)}
     * and
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultDirectedGraph#ins(Object)}
     * and
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultDirectedGraph#outs(Object)}
     * .
     */
    @Test
    public void testAddEdgeExpectInsAndOutsOfPresentToBeTheAdded() {
        final E added = createEdge();

        this.testedWithVertices.add(added, this.firstPresentVertex,
                this.secondPresentVertex);

        assertEquals(ImmutableSet.of(added),
                this.testedWithVertices.ins(this.secondPresentVertex));
        assertEquals(ImmutableSet.of(added),
                this.testedWithVertices.outs(this.firstPresentVertex));
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultDirectedGraph#add(java.lang.Object)}
     * and
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultDirectedGraph#containsVertex(Object)}
     * .
     */
    @Test
    public void testAddVertexAndExpectContainsNew() {
        final V added = createVertex();

        this.tested.add(added);

        assertTrue(this.tested.containsVertex(added));
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultDirectedGraph#add(java.lang.Object)}
     * and
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultDirectedGraph#removeVertex(Object)}
     * and
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultDirectedGraph#vertices()}
     * .
     */
    @Test
    public void
            testAddVertexAndRemoveTheSameVertexAndExpectNotPresentInVertices() {
        final V added = createVertex();

        this.tested.add(added);
        this.tested.removeVertex(added);

        assertFalse(this.tested.vertices().contains(added));
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultDirectedGraph#add(java.lang.Object)}
     * and
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultDirectedGraph#removeVertex(Object)}
     * and
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultDirectedGraph#containsVertex(Object)}
     * .
     */
    @Test
    public void
            testAddVertexAndRemoveTheSameVertexAndExpectThatDoesNotContain() {
        final V added = createVertex();

        this.tested.add(added);
        this.tested.removeVertex(added);

        assertFalse(this.tested.containsVertex(added));
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultDirectedGraph#add(java.lang.Object)}
     * and
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultDirectedGraph#vertices()}
     * .
     */
    @Test
    public void testAddVertexAndVerticesExpectVerticesContainNew() {
        final V added = createVertex();

        this.tested.add(added);

        assertTrue(this.tested.vertices().contains(added));
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultDirectedGraph#create()}
     * .
     */
    @Test
    public void testCreate() {
        DefaultDirectedGraph.create();
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultDirectedGraph#create()}
     * .
     */
    @Test
    public void testCreateExpectEmpty() {
        assertTrue(this.tested.edges().isEmpty());
        assertTrue(this.tested.vertices().isEmpty());
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultDirectedGraph#extractVertex(java.lang.Object, cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Function, cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Callback, cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Callback)}
     * and
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultDirectedGraph#containsEdge(Object)}
     * .
     */
    @Test
    public
            void
            testExtractVertexWhenConnectionsCallThrowsExceptionExpectConnectedEdgeNotRemoved() {
        try {
            this.testedWithVerticesAndEdge.extractVertex(
                    this.firstPresentVertex, new Function<V, V>() {

                        @Override
                        public V apply(final V argument) {
                            return argument;
                        }
                    },

                    new Callback<V>() {

                        @Override
                        public void call(final V parameter) {
                        }
                    },

                    new Callback<E>() {

                        @Override
                        public void call(final E parameter) {
                            throw new IllegalArgumentException();
                        }
                    });
        } catch (final IllegalArgumentException e) {
            assertTrue(this.testedWithVerticesAndEdge
                    .containsEdge(this.edgeBetweenPresentVertices));
            return;
        }

        fail();
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultDirectedGraph#extractVertex(java.lang.Object, cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Function, cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Callback, cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Callback)}
     * and
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultDirectedGraph#containsVertex(Object)}
     * .
     */
    @Test
    public
            void
            testExtractVertexWhenConnectionsCallThrowsExceptionExpectNotExtracted() {
        try {
            this.testedWithVerticesAndEdge.extractVertex(
                    this.firstPresentVertex, new Function<V, V>() {

                        @Override
                        public V apply(final V argument) {
                            return argument;
                        }
                    },

                    new Callback<V>() {

                        @Override
                        public void call(final V parameter) {
                        }
                    },

                    new Callback<E>() {

                        @Override
                        public void call(final E parameter) {
                            throw new IllegalArgumentException();
                        }
                    });
        } catch (final IllegalArgumentException e) {
            assertTrue(this.testedWithVerticesAndEdge
                    .containsVertex(this.firstPresentVertex));
            return;
        }

        fail();
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultDirectedGraph#extractVertex(java.lang.Object, cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Function, cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Callback, cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Callback)}
     * and
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultDirectedGraph#containsEdge(Object)}
     * .
     */
    @Test
    public
            void
            testExtractVertexWhenEmptyCallbacksAndIdenticalRepairFunctionExpectConnectedEdgeRemoved() {
        this.testedWithVerticesAndEdge.extractVertex(this.firstPresentVertex,
                new Function<V, V>() {

                    @Override
                    public V apply(final V argument) {
                        return argument;
                    }
                },

                new Callback<V>() {

                    @Override
                    public void call(final V parameter) {
                    }
                },

                new Callback<E>() {

                    @Override
                    public void call(final E parameter) {
                    }
                });

        assertFalse(this.testedWithVerticesAndEdge
                .containsEdge(this.edgeBetweenPresentVertices));
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultDirectedGraph#extractVertex(java.lang.Object, cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Function, cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Callback, cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Callback)}
     * and
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultDirectedGraph#containsVertex(Object)}
     * .
     */
    @Test
    public
            void
            testExtractVertexWhenEmptyCallbacksAndIdenticalRepairFunctionExpectVertexRemoved() {
        final V extracted = createVertex();

        this.tested.add(extracted);
        this.tested.extractVertex(extracted, new Function<V, V>() {

            @Override
            public V apply(final V argument) {
                return argument;
            }
        },

        new Callback<V>() {

            @Override
            public void call(final V parameter) {
            }
        },

        new Callback<E>() {

            @Override
            public void call(final E parameter) {
            }
        });

        assertFalse(this.tested.containsVertex(extracted));
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultDirectedGraph#extractVertex(java.lang.Object, cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Function, cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Callback, cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Callback)}
     * and
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultDirectedGraph#containsEdge(Object)}
     * .
     */
    @Test
    public
            void
            testExtractVertexWhenNeighboursCallThrowsExceptionExpectConnectedEdgeNotRemoved() {
        try {
            this.testedWithVerticesAndEdge.extractVertex(
                    this.firstPresentVertex, new Function<V, V>() {

                        @Override
                        public V apply(final V argument) {
                            return argument;
                        }
                    },

                    new Callback<V>() {

                        @Override
                        public void call(final V parameter) {
                            throw new IllegalArgumentException();
                        }
                    },

                    new Callback<E>() {

                        @Override
                        public void call(final E parameter) {
                        }
                    });
        } catch (final IllegalArgumentException e) {
            assertTrue(this.testedWithVerticesAndEdge
                    .containsEdge(this.edgeBetweenPresentVertices));
            return;
        }

        fail();
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultDirectedGraph#extractVertex(java.lang.Object, cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Function, cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Callback, cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Callback)}
     * and
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultDirectedGraph#containsVertex(Object)}
     * .
     */
    @Test
    public
            void
            testExtractVertexWhenNeighboursCallThrowsExceptionExpectNotExtracted() {
        try {
            this.testedWithVerticesAndEdge.extractVertex(
                    this.firstPresentVertex, new Function<V, V>() {

                        @Override
                        public V apply(final V argument) {
                            return argument;
                        }
                    },

                    new Callback<V>() {

                        @Override
                        public void call(final V parameter) {
                            throw new IllegalArgumentException();
                        }
                    },

                    new Callback<E>() {

                        @Override
                        public void call(final E parameter) {
                        }
                    });
        } catch (final IllegalArgumentException e) {
            assertTrue(this.testedWithVerticesAndEdge
                    .containsVertex(this.firstPresentVertex));
            return;
        }

        fail();
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultDirectedGraph#extractVertex(java.lang.Object, cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Function, cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Callback, cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Callback)}
     * and
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultDirectedGraph#containsVertex(Object)}
     * .
     */
    @Test
    public
            void
            testExtractVertexWhenTheRepairChangesOldVerticesToNewOneExpectNewContained() {
        final V newVertex = createVertex();

        this.testedWithVerticesAndEdge.extractVertex(this.firstPresentVertex,
                new Function<V, V>() {

                    @Override
                    public V apply(final V argument) {
                        return newVertex;
                    }
                },

                new Callback<V>() {

                    @Override
                    public void call(final V parameter) {
                    }
                },

                new Callback<E>() {

                    @Override
                    public void call(final E parameter) {
                    }
                });

        assertTrue(this.testedWithVerticesAndEdge.containsVertex(newVertex));
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultDirectedGraph#removeEdge(java.lang.Object)}
     * .
     */
    @Test
    public void testRemoveEdgeExpectEdgeRemovalHookCalleEdgeVertex() {
        this.hookCounterWithVerticesAndEdge
                .removeEdge(this.edgeBetweenPresentVertices);

        assertEquals(1,
                this.hookCounterWithVerticesAndEdge
                        .getEdgeRemovalHookCallCounter());
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultDirectedGraph#removeVertex(java.lang.Object)}
     * .
     */
    @Test
    public void testRemoveVertexExpectAdjoinedEdgeRemovalHookCalled() {
        this.hookCounterWithVerticesAndEdge
                .removeVertex(this.firstPresentVertex);

        assertEquals(1,
                this.hookCounterWithVerticesAndEdge
                        .getEdgeRemovalHookCallCounter());
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultDirectedGraph#removeVertex(java.lang.Object)}
     * and
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultDirectedGraph#containsEdge(Object)}
     * .
     */
    @Test
    public void testRemoveVertexExpectEdgesAroundRemoved() {
        final E added = createEdge();

        this.testedWithVertices.add(added, this.firstPresentVertex,
                this.secondPresentVertex);
        this.testedWithVertices.removeVertex(this.firstPresentVertex);

        assertFalse(this.testedWithVertices.containsEdge(added));
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultDirectedGraph#removeVertex(java.lang.Object)}
     * .
     */
    @Test
    public void testRemoveVertexExpectVertexRemovalHookCalled() {
        this.hookCounterWithVerticesAndEdge
                .removeVertex(this.firstPresentVertex);

        assertEquals(1,
                this.hookCounterWithVerticesAndEdge
                        .getVertexRemovalHookCallCounter());
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultDirectedGraph#replaceEdge(java.lang.Object, java.lang.Object)}
     * .
     */
    @Test
    public void testReplaceEdge() {
        final E[] added = createUniqueEdges(2);

        this.testedWithVertices.add(added[0], this.firstPresentVertex,
                this.secondPresentVertex);
        this.testedWithVertices.replaceEdge(added[0], added[1]);

        assertFalse(this.testedWithVertices.containsEdge(added[0]));
        assertTrue(this.testedWithVertices.containsEdge(added[1]));
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultDirectedGraph#replaceEdge(java.lang.Object, java.lang.Object)}
     * .
     */
    @Test
    public void testReplaceEdgeExpectEdgeReplacementHookCalled() {
        final E replacement = createEdge();

        this.hookCounterWithVerticesAndEdge.replaceEdge(
                this.edgeBetweenPresentVertices, replacement);

        assertEquals(1,
                this.hookCounterWithVerticesAndEdge
                        .getEdgeReplacementHookCallCounter());
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultDirectedGraph#replaceEdge(Object, Object)}
     * and
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultDirectedGraph#from(Object)}
     * and
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultDirectedGraph#to(Object)}
     * .
     */
    @Test
    public void testReplaceEdgeExpectReflectedByFromAndTo() {
        final E added = createEdge();

        this.testedWithVertices.add(added, this.firstPresentVertex,
                this.secondPresentVertex);

        final E newAdded = createEdge();
        this.testedWithVertices.replaceEdge(added, newAdded);

        assertEquals(this.firstPresentVertex,
                this.testedWithVertices.from(newAdded));
        assertEquals(this.secondPresentVertex,
                this.testedWithVertices.to(newAdded));
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultDirectedGraph#replaceEdge(Object, Object)}
     * and
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultDirectedGraph#outs(Object)}
     * and
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultDirectedGraph#ins(Object)}
     * .
     */
    @Test
    public void testReplaceEdgeExpectReflectedByInsAndOuts() {
        final E added = createEdge();

        this.testedWithVertices.add(added, this.firstPresentVertex,
                this.secondPresentVertex);

        final E newAdded = createEdge();
        this.testedWithVertices.replaceEdge(added, newAdded);

        assertEquals(ImmutableSet.of(newAdded),
                this.testedWithVertices.outs(this.firstPresentVertex));
        assertEquals(ImmutableSet.of(newAdded),
                this.testedWithVertices.ins(this.secondPresentVertex));
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultDirectedGraph#replaceVertex(java.lang.Object, java.lang.Object)}
     * .
     */
    @Test
    public void testReplaceVertex() {
        final V[] added = createUniqueVertices(2);

        this.tested.add(added[0]);
        this.tested.replaceVertex(added[0], added[1]);

        assertFalse(this.tested.containsVertex(added[0]));
        assertTrue(this.tested.containsVertex(added[1]));
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultDirectedGraph#replaceVertex(java.lang.Object, java.lang.Object)}
     * .
     */
    @Test
    public void testReplaceVertexExpectVertexReplacementHookCalled() {
        final V replacement = createVertex();

        this.hookCounterWithVerticesAndEdge.replaceVertex(
                this.firstPresentVertex, replacement);

        assertEquals(1,
                this.hookCounterWithVerticesAndEdge
                        .getVertexReplacementHookCallCounter());
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultDirectedGraph#replaceVertex(Object, Object)}
     * and
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultDirectedGraph#from(Object)}
     * and
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultDirectedGraph#to(Object)}
     * .
     */
    @Test
    public void testReplaceVerticesExpectReflectedByFromAndTo() {
        final E added = createEdge();

        this.testedWithVertices.add(added, this.firstPresentVertex,
                this.secondPresentVertex);

        final V newFirst = createVertex();
        final V newSecond = createVertex();
        this.testedWithVertices
                .replaceVertex(this.firstPresentVertex, newFirst);
        this.testedWithVertices.replaceVertex(this.secondPresentVertex,
                newSecond);

        assertEquals(newFirst, this.testedWithVertices.from(added));
        assertEquals(newSecond, this.testedWithVertices.to(added));
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultDirectedGraph#replaceVertex(Object, Object)}
     * and
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultDirectedGraph#outs(Object)}
     * and
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultDirectedGraph#ins(Object)}
     * .
     */
    @Test
    public void testReplaceVerticesExpectReflectedByInsAndOuts() {
        final E added = createEdge();

        this.testedWithVertices.add(added, this.firstPresentVertex,
                this.secondPresentVertex);

        final V newFirst = createVertex();
        final V newSecond = createVertex();
        this.testedWithVertices
                .replaceVertex(this.firstPresentVertex, newFirst);
        this.testedWithVertices.replaceVertex(this.secondPresentVertex,
                newSecond);

        assertEquals(ImmutableSet.of(added),
                this.testedWithVertices.outs(newFirst));
        assertEquals(ImmutableSet.of(added),
                this.testedWithVertices.ins(newSecond));
    }
}
