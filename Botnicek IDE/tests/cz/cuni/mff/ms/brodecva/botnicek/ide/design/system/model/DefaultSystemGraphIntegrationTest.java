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
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.Codes;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWords;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.RecurentArc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.TransitionArc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.EnterNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.ExitNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.InnerNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.IsolatedNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.RealignmentProcessor;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.implementations.EnterOrderedInputNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.implementations.EnterOrderedProcessingNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.implementations.ExitInputNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.implementations.ExitProcessingNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.implementations.InnerOrderedProcessingNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.implementations.InnerRandomProcessingNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.implementations.IsolatedInputNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.implementations.IsolatedProcessingNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.updates.DefaultUpdateBuilderFactory;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.types.Priority;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.types.SystemName;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Intended;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultLabeledDirectedGraph;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.test.IntegrationTest;

/**
 * Testuje spolupráci dekorace a dekorovaného při volání metody
 * {@link DefaultSystemGraph#removeAndRealign(Node, RealignmentProcessor, java.util.Map, Set)}
 * .
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see DefaultSystemGraph
 * @see DefaultLabeledDirectedGraph
 */
@Category(IntegrationTest.class)
public class DefaultSystemGraphIntegrationTest {

    private DefaultLabeledDirectedGraph<Node, NormalWord, Arc, NormalWord> baseGraph =
            Intended.nullReference();
    private DefaultSystemGraph tested = Intended.nullReference();

    /**
     * Sestaví testovací objekty.
     * 
     * @throws java.lang.Exception
     *             pokud dojde k vyhození výjimky
     */
    @Before
    public void setUp() throws Exception {
        this.baseGraph =
                DefaultLabeledDirectedGraph
                        .<Node, NormalWord, Arc, NormalWord> create();

        this.tested =
                DefaultSystemGraph.of(this.baseGraph,
                        DefaultUpdateBuilderFactory.create());
    }

    /**
     * Uklidí testovací objekty.
     * 
     * @throws java.lang.Exception
     *             pokud dojde k vyhození výjimky
     */
    @After
    public void tearDown() throws Exception {
        this.baseGraph = Intended.nullReference();
        this.tested = Intended.nullReference();
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.DefaultSystemGraph#removeAndRealign(Node, RealignmentProcessor, java.util.Map, Set)}
     * .
     */
    @Test
    public void testRemoveAndRealignNodeIsolatedLoneExpectGraphEmpty() {
        final Network networkDummy = EasyMock.createStrictMock(Network.class);
        EasyMock.replay(networkDummy);

        final RealignmentProcessor processorDummy =
                EasyMock.createMock(RealignmentProcessor.class);
        EasyMock.replay(processorDummy);

        final IsolatedNode node =
                IsolatedInputNode.create(NormalWords.of("NAME"), networkDummy,
                        25, 25);

        this.baseGraph.add(node, node.getName());

        this.tested.removeAndRealign(node, processorDummy,
                ImmutableMap.<EnterNode, Set<? extends RecurentArc>> of(),
                ImmutableSet.<EnterNode> of());

        assertTrue(this.baseGraph.vertices().isEmpty());

        EasyMock.verify(networkDummy);
        EasyMock.verify(processorDummy);
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.DefaultSystemGraph#removeAndRealign(Node, RealignmentProcessor, java.util.Map, Set)}
     * .
     */
    @Test
    public
            void
            testRemoveAndRealignNodeWhenPathOfThreeFirstRemovedExpectSecondAddedToInitials() {
        final Network networkDummy = EasyMock.createMock(Network.class);
        EasyMock.expect(networkDummy.getName()).andStubReturn(
                SystemName.of("Network"));
        EasyMock.replay(networkDummy);

        final EnterNode firstNode =
                EnterOrderedInputNode.create(NormalWords.of("FIRST"),
                        networkDummy, 25, 25);
        final InnerNode secondNode =
                InnerRandomProcessingNode.create(NormalWords.of("SECOND"),
                        networkDummy, 50, 50);
        final ExitNode thirdNode =
                ExitProcessingNode.create(NormalWords.of("THIRD"),
                        networkDummy, 75, 75);

        final EnterNode secondNodeRealigned =
                EnterOrderedProcessingNode.create(secondNode);

        final RealignmentProcessor processorStub =
                EasyMock.createMock(RealignmentProcessor.class);
        EasyMock.expect(processorStub.realign(secondNode)).andStubReturn(
                secondNodeRealigned);
        EasyMock.replay(processorStub);

        final Arc firstToSecondArc =
                TransitionArc.getInitial(networkDummy, NormalWords.of("A"));
        final Arc secondToThirdArc =
                TransitionArc.getInitial(networkDummy, NormalWords.of("B"));

        this.baseGraph.add(firstNode);
        this.baseGraph.add(secondNode);
        this.baseGraph.add(thirdNode);
        this.baseGraph.add(firstToSecondArc, firstToSecondArc.getName(),
                firstNode, secondNode);
        this.baseGraph.add(secondToThirdArc, secondToThirdArc.getName(),
                secondNode, thirdNode);

        assertEquals(
                ImmutableSet.of(secondNodeRealigned),
                this.tested.removeAndRealign(
                        firstNode,
                        processorStub,
                        ImmutableMap
                                .<EnterNode, Set<? extends RecurentArc>> of(),
                        ImmutableSet.<EnterNode> of(firstNode))
                        .getInitialsAdded());

        EasyMock.verify(processorStub);
        EasyMock.verify(networkDummy);
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.DefaultSystemGraph#removeAndRealign(Node, RealignmentProcessor, java.util.Map, Set)}
     * .
     */
    @Test(expected = IllegalArgumentException.class)
    public
            void
            testRemoveAndRealignNodeWhenTwoConnectedByRecurentArcAndSecondRemoved() {
        final Network networkDummy = EasyMock.createMock(Network.class);
        EasyMock.replay(networkDummy);

        final EnterNode firstNode =
                EnterOrderedProcessingNode.create(NormalWords.of("FIRST"),
                        networkDummy, 25, 25);
        final ExitNode secondNode =
                ExitInputNode.create(NormalWords.of("SECOND"), networkDummy,
                        50, 50);

        final IsolatedNode firstNodeRealigned =
                IsolatedProcessingNode.create(secondNode);

        final RealignmentProcessor processorStub =
                EasyMock.createMock(RealignmentProcessor.class);
        EasyMock.expect(processorStub.realign(firstNode)).andStubReturn(
                firstNodeRealigned);
        EasyMock.replay(processorStub);

        final RecurentArc arc =
                RecurentArc.create(networkDummy, NormalWords.of("ARC"),
                        Priority.of(1), Codes.createEmpty(), firstNode);

        this.baseGraph.add(firstNode);
        this.baseGraph.add(secondNode);
        this.baseGraph.add(arc, arc.getName(), firstNode, secondNode);

        try {
            this.tested.removeAndRealign(firstNodeRealigned, processorStub,
                    ImmutableMap.of(firstNode, ImmutableSet.of(arc)),
                    ImmutableSet.of(firstNode));
        } finally {
            EasyMock.verify(processorStub);
            EasyMock.verify(networkDummy);
        }
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.DefaultSystemGraph#removeAndRealign(Node, RealignmentProcessor, java.util.Map, Set)}
     * .
     */
    @Test(expected = IllegalArgumentException.class)
    public
            void
            testRemoveAndRealignNodeWhenTwoConnectedByRecurentArcAndSecondRemovedExpectGraphUnchanged() {
        final Network networkDummy = EasyMock.createMock(Network.class);
        EasyMock.replay(networkDummy);

        final EnterNode firstNode =
                EnterOrderedProcessingNode.create(NormalWords.of("FIRST"),
                        networkDummy, 25, 25);
        final ExitNode secondNode =
                ExitInputNode.create(NormalWords.of("SECOND"), networkDummy,
                        50, 50);

        final IsolatedNode firstNodeRealigned =
                IsolatedProcessingNode.create(secondNode);

        final RealignmentProcessor processorStub =
                EasyMock.createMock(RealignmentProcessor.class);
        EasyMock.expect(processorStub.realign(firstNode)).andStubReturn(
                firstNodeRealigned);
        EasyMock.replay(processorStub);

        final RecurentArc arc =
                RecurentArc.create(networkDummy, NormalWords.of("ARC"),
                        Priority.of(1), Codes.createEmpty(), firstNode);

        this.baseGraph.add(firstNode);
        this.baseGraph.add(secondNode);
        this.baseGraph.add(arc, arc.getName(), firstNode, secondNode);

        try {
            this.tested.removeAndRealign(firstNodeRealigned, processorStub,
                    ImmutableMap.of(firstNode, ImmutableSet.of(arc)),
                    ImmutableSet.of(firstNode));
        } finally {
            assertEquals(ImmutableSet.of(firstNode, secondNode),
                    this.baseGraph.vertices());
            assertEquals(ImmutableSet.of(arc), this.baseGraph.edges());
            assertEquals(firstNode, this.baseGraph.from(arc));
            assertEquals(secondNode, this.baseGraph.to(arc));

            EasyMock.verify(processorStub);
            EasyMock.verify(networkDummy);
        }

    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.DefaultSystemGraph#removeAndRealign(Node, RealignmentProcessor, java.util.Map, Set)}
     * .
     */
    @Test
    public
            void
            testRemoveAndRealignNodeWhenTwoConnectedByRecurentArcExpectTheArcRemovedFromReferences() {
        final Network networkDummy = EasyMock.createMock(Network.class);
        EasyMock.replay(networkDummy);

        final EnterNode firstNode =
                EnterOrderedProcessingNode.create(NormalWords.of("FIRST"),
                        networkDummy, 25, 25);
        final ExitNode secondNode =
                ExitInputNode.create(NormalWords.of("SECOND"), networkDummy,
                        50, 50);

        final IsolatedNode secondNodeRealigned =
                IsolatedProcessingNode.create(secondNode);

        final RealignmentProcessor processorStub =
                EasyMock.createMock(RealignmentProcessor.class);
        EasyMock.expect(processorStub.realign(secondNode)).andStubReturn(
                secondNodeRealigned);
        EasyMock.replay(processorStub);

        final RecurentArc arc =
                RecurentArc.create(networkDummy, NormalWords.of("ARC"),
                        Priority.of(1), Codes.createEmpty(), firstNode);

        this.baseGraph.add(firstNode);
        this.baseGraph.add(secondNode);
        this.baseGraph.add(arc, arc.getName(), firstNode, secondNode);

        assertEquals(
                ImmutableSet.of(arc),
                this.tested.removeAndRealign(firstNode, processorStub,
                        ImmutableMap.of(firstNode, ImmutableSet.of(arc)),
                        ImmutableSet.of(firstNode)).getEdgesRemoved());

        EasyMock.verify(processorStub);
        EasyMock.verify(networkDummy);
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.DefaultSystemGraph#removeAndRealign(Node, RealignmentProcessor, java.util.Map, Set)}
     * .
     */
    @Test
    public
            void
            testRemoveAndRealignNodeWhenTwoConnectedOnlyExpectIsolatedNodeLeft() {
        final Network networkDummy = EasyMock.createMock(Network.class);
        EasyMock.replay(networkDummy);

        final InnerNode firstNode =
                InnerOrderedProcessingNode.create(NormalWords.of("FIRST"),
                        networkDummy, 25, 25);
        final InnerNode secondNode =
                InnerOrderedProcessingNode.create(NormalWords.of("SECOND"),
                        networkDummy, 50, 50);

        final IsolatedNode secondNodeRealigned =
                IsolatedProcessingNode.create(secondNode);

        final RealignmentProcessor processorStub =
                EasyMock.createMock(RealignmentProcessor.class);
        EasyMock.expect(processorStub.realign(secondNode)).andStubReturn(
                secondNodeRealigned);
        EasyMock.replay(processorStub);

        final Arc arc =
                TransitionArc.getInitial(networkDummy, NormalWords.of("ARC"));
        final Arc oppositeArc =
                TransitionArc.getInitial(networkDummy,
                        NormalWords.of("OPPOSITE"));

        this.baseGraph.add(firstNode);
        this.baseGraph.add(secondNode);
        this.baseGraph.add(arc, arc.getName(), firstNode, secondNode);
        this.baseGraph.add(oppositeArc, oppositeArc.getName(), secondNode,
                firstNode);

        this.tested.removeAndRealign(firstNode, processorStub,
                ImmutableMap.<EnterNode, Set<? extends RecurentArc>> of(),
                ImmutableSet.<EnterNode> of());

        assertEquals(ImmutableSet.of(secondNodeRealigned),
                this.baseGraph.vertices());

        EasyMock.verify(processorStub);
        EasyMock.verify(networkDummy);
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.DefaultSystemGraph#removeAndRealign(Node, RealignmentProcessor, java.util.Map, Set)}
     * .
     */
    @Test
    public
            void
            testRemoveAndRealignNodeWhenTwoConnectedOnlyExpectLeftSwitchedForIsolated() {
        final Network networkDummy = EasyMock.createMock(Network.class);
        EasyMock.replay(networkDummy);

        final InnerNode firstNode =
                InnerOrderedProcessingNode.create(NormalWords.of("FIRST"),
                        networkDummy, 25, 25);
        final InnerNode secondNode =
                InnerOrderedProcessingNode.create(NormalWords.of("SECOND"),
                        networkDummy, 50, 50);

        final IsolatedNode secondNodeRealigned =
                IsolatedProcessingNode.create(secondNode);

        final RealignmentProcessor processorStub =
                EasyMock.createMock(RealignmentProcessor.class);
        EasyMock.expect(processorStub.realign(secondNode)).andStubReturn(
                secondNodeRealigned);
        EasyMock.replay(processorStub);

        final Arc arc =
                TransitionArc.getInitial(networkDummy, NormalWords.of("ARC"));

        this.baseGraph.add(firstNode);
        this.baseGraph.add(secondNode);
        this.baseGraph.add(arc, arc.getName(), firstNode, secondNode);

        assertEquals(
                ImmutableMap.of(secondNode, secondNodeRealigned),
                this.tested.removeAndRealign(
                        firstNode,
                        processorStub,
                        ImmutableMap
                                .<EnterNode, Set<? extends RecurentArc>> of(),
                        ImmutableSet.<EnterNode> of()).getSwitched());

        EasyMock.verify(processorStub);
        EasyMock.verify(networkDummy);
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.DefaultSystemGraph#removeAndRealign(Node, RealignmentProcessor, java.util.Map, Set)}
     * .
     */
    @Test
    public
            void
            testRemoveAndRealignNodeWhenTwoConnectedOnlyExpectRemovedArcAddedToUpdate() {
        final Network networkDummy = EasyMock.createMock(Network.class);
        EasyMock.replay(networkDummy);

        final EnterNode firstNode =
                EnterOrderedProcessingNode.create(NormalWords.of("FIRST"),
                        networkDummy, 25, 25);
        final ExitNode secondNode =
                ExitProcessingNode.create(NormalWords.of("SECOND"),
                        networkDummy, 50, 50);

        final IsolatedNode secondNodeRealigned =
                IsolatedProcessingNode.create(secondNode);

        final RealignmentProcessor processorStub =
                EasyMock.createMock(RealignmentProcessor.class);
        EasyMock.expect(processorStub.realign(secondNode)).andStubReturn(
                secondNodeRealigned);
        EasyMock.replay(processorStub);

        final Arc arc =
                TransitionArc.getInitial(networkDummy, NormalWords.of("ARC"));

        this.baseGraph.add(firstNode);
        this.baseGraph.add(secondNode);
        this.baseGraph.add(arc, arc.getName(), firstNode, secondNode);

        assertEquals(
                ImmutableSet.of(arc),
                this.tested.removeAndRealign(
                        firstNode,
                        processorStub,
                        ImmutableMap
                                .<EnterNode, Set<? extends RecurentArc>> of(),
                        ImmutableSet.of(firstNode)).getEdgesRemoved());

        EasyMock.verify(processorStub);
        EasyMock.verify(networkDummy);
    }
}
