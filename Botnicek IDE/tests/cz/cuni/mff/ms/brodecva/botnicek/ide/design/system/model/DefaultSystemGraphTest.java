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
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.RecurentArc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.EnterNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.IsolatedNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.RealignmentProcessor;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.updates.Update;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.updates.UpdateBuilder;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.updates.UpdateBuilderFactory;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.types.SystemName;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Intended;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.Direction;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.LabeledDirectedGraph;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.test.UnitTest;

/**
 * Testuje implementaci {@link SystemGraph}.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see DefaultSystemGraphTest
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ RecurentArc.class, SystemName.class })
@Category(UnitTest.class)
public class DefaultSystemGraphTest {

    private LabeledDirectedGraph<Node, NormalWord, Arc, NormalWord> baseGraphStub =
            Intended.nullReference();
    private UpdateBuilderFactory updateBuilderFactoryStub = Intended
            .nullReference();
    private DefaultSystemGraph tested = Intended.nullReference();
    private SystemName networkNameStub = Intended.nullReference();
    private NormalWord fromNodeNameDummy = Intended.nullReference();
    private NormalWord toNodeNameDummy = Intended.nullReference();

    private void replayTestedStubs() {
        EasyMock.replay(this.baseGraphStub);
        EasyMock.replay(this.updateBuilderFactoryStub);
        PowerMock.replay(this.networkNameStub);
    }

    /**
     * Sestaví testovací objekty.
     * 
     * @throws java.lang.Exception
     *             pokud dojde k vyhození výjimky
     */
    @Before
    public void setUp() throws Exception {
        @SuppressWarnings("unchecked")
        final LabeledDirectedGraph<Node, NormalWord, Arc, NormalWord> castBaseGraphStub =
                EasyMock.createStrictMock(LabeledDirectedGraph.class);
        this.baseGraphStub = castBaseGraphStub;

        this.updateBuilderFactoryStub =
                EasyMock.createMock(UpdateBuilderFactory.class);

        this.tested =
                DefaultSystemGraph.of(this.baseGraphStub,
                        this.updateBuilderFactoryStub);

        this.fromNodeNameDummy = EasyMock.createStrictMock(NormalWord.class);
        EasyMock.replay(this.fromNodeNameDummy);

        this.toNodeNameDummy = EasyMock.createStrictMock(NormalWord.class);
        EasyMock.replay(this.toNodeNameDummy);

        this.networkNameStub = PowerMock.createStrictMock(SystemName.class);
        EasyMock.expect(this.networkNameStub.getText()).andStubReturn("network");
        PowerMock.replay(this.networkNameStub);
    }

    /**
     * Uklidí testovací objekty.
     * 
     * @throws java.lang.Exception
     *             pokud dojde k vyhození výjimky
     */
    @After
    public void tearDown() throws Exception {
        this.baseGraphStub = Intended.nullReference();
        this.updateBuilderFactoryStub = Intended.nullReference();
        this.tested = Intended.nullReference();
        this.fromNodeNameDummy = Intended.nullReference();
        this.toNodeNameDummy = Intended.nullReference();
        this.networkNameStub = Intended.nullReference();
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.DefaultSystemGraph#addAndRealign(cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc, cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node, cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node, cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.RealignmentProcessor, java.util.Set)}
     * .
     */
    @Test
    public void testAddAndRealignWhenNoInitialBothChangedFromToEnterNode() {
        final NormalWord addedArcNameDummy =
                EasyMock.createStrictMock(NormalWord.class);
        EasyMock.replay(addedArcNameDummy);

        final Arc addedArcStub = EasyMock.createStrictMock(Arc.class);
        EasyMock.expect(addedArcStub.getName())
                .andStubReturn(addedArcNameDummy);
        EasyMock.replay(addedArcStub);

        final Node fromNodeDummy = EasyMock.createStrictMock(Node.class);
        EasyMock.replay(fromNodeDummy);

        final EnterNode toNodeDummy =
                EasyMock.createStrictMock(EnterNode.class);
        EasyMock.replay(toNodeDummy);

        final EnterNode newFromNodeStub =
                EasyMock.createStrictMock(EnterNode.class);
        EasyMock.expect(newFromNodeStub.getName()).andStubReturn(
                this.fromNodeNameDummy);
        EasyMock.replay(newFromNodeStub);

        final Node newToNodeStub = EasyMock.createStrictMock(Node.class);
        EasyMock.expect(newToNodeStub.getName()).andStubReturn(
                this.toNodeNameDummy);
        EasyMock.replay(newToNodeStub);

        final RealignmentProcessor processorStub =
                EasyMock.createMock(RealignmentProcessor.class);
        EasyMock.expect(processorStub.realign(fromNodeDummy)).andStubReturn(
                newFromNodeStub);
        EasyMock.expect(processorStub.realign(toNodeDummy)).andStubReturn(
                newToNodeStub);
        EasyMock.replay(processorStub);

        final Update updateDummy = EasyMock.createStrictMock(Update.class);
        EasyMock.replay(updateDummy);

        final UpdateBuilder updateBuilderMock =
                EasyMock.createMock(UpdateBuilder.class);
        updateBuilderMock.addNewInitial(newFromNodeStub);
        updateBuilderMock.addRemovedInitial(toNodeDummy);
        updateBuilderMock.addSwitched(fromNodeDummy, newFromNodeStub);
        updateBuilderMock.addSwitched(toNodeDummy, newToNodeStub);
        EasyMock.expect(updateBuilderMock.build()).andStubReturn(updateDummy);
        EasyMock.replay(updateBuilderMock);

        this.baseGraphStub.add(addedArcStub, addedArcNameDummy, fromNodeDummy,
                toNodeDummy);
        EasyMock.checkOrder(this.baseGraphStub, false);
        this.baseGraphStub.replaceVertex(fromNodeDummy, newFromNodeStub,
                this.fromNodeNameDummy);
        this.baseGraphStub.replaceVertex(toNodeDummy, newToNodeStub,
                this.toNodeNameDummy);
        EasyMock.checkOrder(this.baseGraphStub, true);

        EasyMock.expect(this.updateBuilderFactoryStub.produce()).andStubReturn(
                updateBuilderMock);

        replayTestedStubs();

        this.tested.addAndRealign(addedArcStub, fromNodeDummy, toNodeDummy,
                processorStub, ImmutableSet.of(toNodeDummy));

        EasyMock.verify(addedArcNameDummy);
        EasyMock.verify(addedArcStub);
        EasyMock.verify(fromNodeDummy);
        EasyMock.verify(toNodeDummy);
        EasyMock.verify(newFromNodeStub);
        EasyMock.verify(newToNodeStub);
        EasyMock.verify(processorStub);
        EasyMock.verify(updateDummy);
        EasyMock.verify(updateBuilderMock);
        verifyTestedStubs();
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.DefaultSystemGraph#addAndRealign(cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc, cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node, cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node, cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.RealignmentProcessor, java.util.Set)}
     * .
     */
    @Test
    public void testAddAndRealignWhenToInitialBothChangedFromToEnterNode() {
        final NormalWord addedArcNameDummy =
                EasyMock.createStrictMock(NormalWord.class);
        EasyMock.replay(addedArcNameDummy);

        final Arc addedArcStub = EasyMock.createStrictMock(Arc.class);
        EasyMock.expect(addedArcStub.getName())
                .andStubReturn(addedArcNameDummy);
        EasyMock.replay(addedArcStub);

        final Node fromNodeDummy = EasyMock.createStrictMock(Node.class);
        EasyMock.replay(fromNodeDummy);

        final EnterNode toNodeDummy =
                EasyMock.createStrictMock(EnterNode.class);
        EasyMock.replay(toNodeDummy);

        final EnterNode newFromNodeStub =
                EasyMock.createStrictMock(EnterNode.class);
        EasyMock.expect(newFromNodeStub.getName()).andStubReturn(
                this.fromNodeNameDummy);
        EasyMock.replay(newFromNodeStub);

        final EnterNode newToNodeStub =
                EasyMock.createStrictMock(EnterNode.class);
        EasyMock.expect(newToNodeStub.getName()).andStubReturn(
                this.toNodeNameDummy);
        EasyMock.replay(newToNodeStub);

        final RealignmentProcessor processorStub =
                EasyMock.createMock(RealignmentProcessor.class);
        EasyMock.expect(processorStub.realign(fromNodeDummy)).andStubReturn(
                newFromNodeStub);
        EasyMock.expect(processorStub.realign(toNodeDummy)).andStubReturn(
                newToNodeStub);
        EasyMock.replay(processorStub);

        final Update updateDummy = EasyMock.createStrictMock(Update.class);
        EasyMock.replay(updateDummy);

        final UpdateBuilder updateBuilderMock =
                EasyMock.createMock(UpdateBuilder.class);
        updateBuilderMock.addNewInitial(newFromNodeStub);
        updateBuilderMock.addRemovedInitial(toNodeDummy);
        updateBuilderMock.addSwitched(fromNodeDummy, newFromNodeStub);
        updateBuilderMock.addSwitched(toNodeDummy, newToNodeStub);
        EasyMock.expect(updateBuilderMock.build()).andStubReturn(updateDummy);
        EasyMock.replay(updateBuilderMock);

        this.baseGraphStub.add(addedArcStub, addedArcNameDummy, fromNodeDummy,
                toNodeDummy);
        EasyMock.checkOrder(this.baseGraphStub, false);
        this.baseGraphStub.replaceVertex(fromNodeDummy, newFromNodeStub,
                this.fromNodeNameDummy);
        this.baseGraphStub.replaceVertex(toNodeDummy, newToNodeStub,
                this.toNodeNameDummy);
        EasyMock.checkOrder(this.baseGraphStub, true);

        EasyMock.expect(this.updateBuilderFactoryStub.produce()).andStubReturn(
                updateBuilderMock);

        replayTestedStubs();

        this.tested.addAndRealign(addedArcStub, fromNodeDummy, toNodeDummy,
                processorStub, ImmutableSet.of(toNodeDummy));

        EasyMock.verify(addedArcNameDummy);
        EasyMock.verify(addedArcStub);
        EasyMock.verify(fromNodeDummy);
        EasyMock.verify(toNodeDummy);
        EasyMock.verify(newFromNodeStub);
        EasyMock.verify(newToNodeStub);
        EasyMock.verify(processorStub);
        EasyMock.verify(updateDummy);
        EasyMock.verify(updateBuilderMock);
        verifyTestedStubs();
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.DefaultSystemGraph#adjoins(cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node, cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node, cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.Direction)}
     * .
     */
    @Test
    public void testAdjoinsWhenInAndFalse() {
        final Node firstNodeDummy = EasyMock.createStrictMock(Node.class);
        EasyMock.replay(firstNodeDummy);

        final Node secondNodeDummy = EasyMock.createStrictMock(Node.class);
        EasyMock.replay(secondNodeDummy);

        final Arc arcStub = EasyMock.createStrictMock(Arc.class);
        EasyMock.expect(arcStub.isAttached(secondNodeDummy, Direction.IN))
                .andStubReturn(false);
        EasyMock.replay(arcStub);

        EasyMock.expect(this.baseGraphStub.ins(firstNodeDummy)).andStubReturn(
                ImmutableSet.of(arcStub));

        replayTestedStubs();

        assertFalse(this.tested.adjoins(firstNodeDummy, secondNodeDummy,
                Direction.IN));

        EasyMock.verify(arcStub);
        EasyMock.verify(firstNodeDummy);
        EasyMock.verify(secondNodeDummy);
        verifyTestedStubs();
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.DefaultSystemGraph#adjoins(cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node, cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node, cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.Direction)}
     * .
     */
    @Test
    public void testAdjoinsWhenInAndTrue() {
        final Node firstNodeDummy = EasyMock.createStrictMock(Node.class);
        EasyMock.replay(firstNodeDummy);

        final Node secondNodeDummy = EasyMock.createStrictMock(Node.class);
        EasyMock.replay(secondNodeDummy);

        final Arc arcStub = EasyMock.createStrictMock(Arc.class);
        EasyMock.expect(arcStub.isAttached(secondNodeDummy, Direction.IN))
                .andStubReturn(true);
        EasyMock.replay(arcStub);

        EasyMock.expect(this.baseGraphStub.ins(firstNodeDummy)).andStubReturn(
                ImmutableSet.of(arcStub));

        replayTestedStubs();

        assertTrue(this.tested.adjoins(firstNodeDummy, secondNodeDummy,
                Direction.IN));

        EasyMock.verify(arcStub);
        EasyMock.verify(firstNodeDummy);
        EasyMock.verify(secondNodeDummy);
        verifyTestedStubs();
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.DefaultSystemGraph#adjoins(cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node, cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node, cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.Direction)}
     * .
     */
    @Test
    public void testAdjoinsWhenOutAndFalse() {
        final Node firstNodeDummy = EasyMock.createStrictMock(Node.class);
        EasyMock.replay(firstNodeDummy);

        final Node secondNodeDummy = EasyMock.createStrictMock(Node.class);
        EasyMock.replay(secondNodeDummy);

        final Arc arcStub = EasyMock.createStrictMock(Arc.class);
        EasyMock.expect(arcStub.isAttached(secondNodeDummy, Direction.OUT))
                .andStubReturn(false);
        EasyMock.replay(arcStub);

        EasyMock.expect(this.baseGraphStub.outs(firstNodeDummy)).andStubReturn(
                ImmutableSet.of(arcStub));

        replayTestedStubs();

        assertFalse(this.tested.adjoins(firstNodeDummy, secondNodeDummy,
                Direction.OUT));

        EasyMock.verify(arcStub);
        EasyMock.verify(firstNodeDummy);
        EasyMock.verify(secondNodeDummy);
        verifyTestedStubs();
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.DefaultSystemGraph#adjoins(cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node, cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node, cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.Direction)}
     * .
     */
    @Test
    public void testAdjoinsWhenOutAndTrue() {
        final Node firstNodeDummy = EasyMock.createStrictMock(Node.class);
        EasyMock.replay(firstNodeDummy);

        final Node secondNodeDummy = EasyMock.createStrictMock(Node.class);
        EasyMock.replay(secondNodeDummy);

        final Arc arcStub = EasyMock.createStrictMock(Arc.class);
        EasyMock.expect(arcStub.isAttached(secondNodeDummy, Direction.OUT))
                .andStubReturn(true);
        EasyMock.replay(arcStub);

        EasyMock.expect(this.baseGraphStub.outs(firstNodeDummy)).andStubReturn(
                ImmutableSet.of(arcStub));

        replayTestedStubs();

        assertTrue(this.tested.adjoins(firstNodeDummy, secondNodeDummy,
                Direction.OUT));

        EasyMock.verify(arcStub);
        EasyMock.verify(firstNodeDummy);
        EasyMock.verify(secondNodeDummy);
        verifyTestedStubs();
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.DefaultSystemGraph#attached(cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc, cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.Direction)}
     * .
     */
    @Test
    public void testAttachedWhenIn() {
        final Arc arcDummy = EasyMock.createStrictMock(Arc.class);
        EasyMock.replay(arcDummy);

        final Node nodeDummy = EasyMock.createStrictMock(Node.class);
        EasyMock.replay(nodeDummy);

        EasyMock.expect(this.baseGraphStub.from(arcDummy)).andStubReturn(
                nodeDummy);

        replayTestedStubs();

        this.tested.attached(arcDummy, Direction.IN);

        EasyMock.verify(arcDummy);
        EasyMock.verify(nodeDummy);
        verifyTestedStubs();
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.DefaultSystemGraph#attached(cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc, cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.Direction)}
     * .
     */
    @Test
    public void testAttachedWhenOut() {
        final Arc arcDummy = EasyMock.createStrictMock(Arc.class);
        EasyMock.replay(arcDummy);

        final Node nodeDummy = EasyMock.createStrictMock(Node.class);
        EasyMock.replay(nodeDummy);

        EasyMock.expect(this.baseGraphStub.to(arcDummy)).andStubReturn(
                nodeDummy);

        replayTestedStubs();

        this.tested.attached(arcDummy, Direction.OUT);

        EasyMock.verify(arcDummy);
        EasyMock.verify(nodeDummy);
        verifyTestedStubs();
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.DefaultSystemGraph#connections(cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node, cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.Direction)}
     * .
     */
    @Test
    public void testConnectionsWhenIn() {
        final Node nodeDummy = EasyMock.createStrictMock(Node.class);
        EasyMock.replay(nodeDummy);

        EasyMock.expect(this.baseGraphStub.ins(nodeDummy)).andStubReturn(
                ImmutableSet.<Arc> of());

        replayTestedStubs();

        this.tested.connections(nodeDummy, Direction.IN);

        EasyMock.verify(nodeDummy);
        verifyTestedStubs();
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.DefaultSystemGraph#connections(cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node, cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.Direction)}
     * .
     */
    @Test
    public void testConnectionsWhenOut() {
        final Node nodeDummy = EasyMock.createStrictMock(Node.class);
        EasyMock.replay(nodeDummy);

        EasyMock.expect(this.baseGraphStub.outs(nodeDummy)).andStubReturn(
                ImmutableSet.<Arc> of());

        replayTestedStubs();

        this.tested.connections(nodeDummy, Direction.OUT);

        EasyMock.verify(nodeDummy);
        verifyTestedStubs();
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.DefaultSystemGraph#of(cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.LabeledDirectedGraph, cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.updates.UpdateBuilderFactory)}
     * .
     */
    @Test
    public void testOf() {
        DefaultSystemGraph
                .of(this.baseGraphStub, this.updateBuilderFactoryStub);
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.DefaultSystemGraph#purge(java.util.Set)}
     * .
     */
    @Test
    public void testPurge() {
        final Node firstNodeDummy = EasyMock.createStrictMock(Node.class);
        EasyMock.replay(firstNodeDummy);

        final Node secondNodeDummy = EasyMock.createStrictMock(Node.class);
        EasyMock.replay(secondNodeDummy);

        this.baseGraphStub.removeVertex(firstNodeDummy);
        this.baseGraphStub.removeVertex(secondNodeDummy);

        replayTestedStubs();

        this.tested.purge(ImmutableSet.of(firstNodeDummy, secondNodeDummy));

        EasyMock.verify(firstNodeDummy);
        EasyMock.verify(secondNodeDummy);
        verifyTestedStubs();
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.DefaultSystemGraph#removeAndRealign(cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc, cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.RealignmentProcessor, java.util.Map, java.util.Set)}
     * .
     */
    @Test
    public void testRemoveAndRealignArcWhenFromNotReferred() {
        final Node fromNodeDummy = EasyMock.createStrictMock(Node.class);
        EasyMock.replay(fromNodeDummy);

        final Node toNodeDummy = EasyMock.createStrictMock(Node.class);
        EasyMock.replay(toNodeDummy);

        final Node newFromNodeStub = EasyMock.createStrictMock(Node.class);
        EasyMock.expect(newFromNodeStub.getName()).andStubReturn(
                this.fromNodeNameDummy);
        EasyMock.replay(newFromNodeStub);

        final Node newToNodeStub = EasyMock.createStrictMock(Node.class);
        EasyMock.expect(newToNodeStub.getName()).andStubReturn(
                this.toNodeNameDummy);
        EasyMock.replay(newToNodeStub);

        final Arc removedArcStub = EasyMock.createMock(Arc.class);
        EasyMock.expect(removedArcStub.getFrom()).andStubReturn(fromNodeDummy);
        EasyMock.expect(removedArcStub.getTo()).andStubReturn(toNodeDummy);
        EasyMock.replay(removedArcStub);

        final RealignmentProcessor processorStub =
                EasyMock.createMock(RealignmentProcessor.class);
        EasyMock.expect(processorStub.realign(fromNodeDummy)).andStubReturn(
                newFromNodeStub);
        EasyMock.expect(processorStub.realign(toNodeDummy)).andStubReturn(
                newToNodeStub);
        EasyMock.replay(processorStub);

        final Update updateDummy = EasyMock.createStrictMock(Update.class);
        EasyMock.replay(updateDummy);

        final UpdateBuilder updateBuilderMock =
                EasyMock.createMock(UpdateBuilder.class);
        updateBuilderMock.addSwitched(fromNodeDummy, newFromNodeStub);
        updateBuilderMock.addSwitched(toNodeDummy, newToNodeStub);
        EasyMock.expect(updateBuilderMock.build()).andStubReturn(updateDummy);
        EasyMock.replay(updateBuilderMock);

        this.baseGraphStub.removeEdge(removedArcStub);
        EasyMock.checkOrder(this.baseGraphStub, false);
        this.baseGraphStub.replaceVertex(fromNodeDummy, newFromNodeStub,
                this.fromNodeNameDummy);
        this.baseGraphStub.replaceVertex(toNodeDummy, newToNodeStub,
                this.toNodeNameDummy);
        EasyMock.checkOrder(this.baseGraphStub, true);

        EasyMock.expect(this.updateBuilderFactoryStub.produce()).andStubReturn(
                updateBuilderMock);

        replayTestedStubs();

        this.tested.removeAndRealign(removedArcStub, processorStub,
                ImmutableMap.<EnterNode, Set<RecurentArc>> of(),
                ImmutableSet.<EnterNode> of());

        EasyMock.verify(removedArcStub);
        EasyMock.verify(fromNodeDummy);
        EasyMock.verify(toNodeDummy);
        EasyMock.verify(newFromNodeStub);
        EasyMock.verify(newToNodeStub);
        EasyMock.verify(processorStub);
        EasyMock.verify(updateDummy);
        EasyMock.verify(updateBuilderMock);
        verifyTestedStubs();
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.DefaultSystemGraph#removeAndRealign(cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc, cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.RealignmentProcessor, java.util.Map, java.util.Set)}
     * .
     */
    @Test
    public void testRemoveAndRealignArcWhenFromReferredButRemainsItself() {
        final EnterNode fromNodeStub =
                EasyMock.createStrictMock(EnterNode.class);
        EasyMock.expect(fromNodeStub.getName()).andStubReturn(
                this.fromNodeNameDummy);
        EasyMock.replay(fromNodeStub);

        final Node toNodeDummy = EasyMock.createStrictMock(Node.class);
        EasyMock.replay(toNodeDummy);

        final Node newToNodeStub = EasyMock.createStrictMock(Node.class);
        EasyMock.expect(newToNodeStub.getName()).andStubReturn(
                this.toNodeNameDummy);
        EasyMock.replay(newToNodeStub);

        final Arc removedArcStub = EasyMock.createMock(Arc.class);
        EasyMock.expect(removedArcStub.getFrom()).andStubReturn(fromNodeStub);
        EasyMock.expect(removedArcStub.getTo()).andStubReturn(toNodeDummy);
        EasyMock.replay(removedArcStub);

        final RealignmentProcessor processorStub =
                EasyMock.createMock(RealignmentProcessor.class);
        EasyMock.expect(processorStub.realign(fromNodeStub)).andStubReturn(
                fromNodeStub);
        EasyMock.expect(processorStub.realign(toNodeDummy)).andStubReturn(
                newToNodeStub);
        EasyMock.replay(processorStub);

        final Update updateDummy = EasyMock.createStrictMock(Update.class);
        EasyMock.replay(updateDummy);

        final UpdateBuilder updateBuilderMock =
                EasyMock.createMock(UpdateBuilder.class);
        updateBuilderMock.addSwitched(fromNodeStub, fromNodeStub);
        updateBuilderMock.addSwitched(toNodeDummy, newToNodeStub);
        EasyMock.expect(updateBuilderMock.build()).andStubReturn(updateDummy);
        EasyMock.replay(updateBuilderMock);

        this.baseGraphStub.removeEdge(removedArcStub);
        EasyMock.checkOrder(this.baseGraphStub, false);
        this.baseGraphStub.replaceVertex(fromNodeStub, fromNodeStub,
                this.fromNodeNameDummy);
        this.baseGraphStub.replaceVertex(toNodeDummy, newToNodeStub,
                this.toNodeNameDummy);
        EasyMock.checkOrder(this.baseGraphStub, true);

        EasyMock.expect(this.updateBuilderFactoryStub.produce()).andStubReturn(
                updateBuilderMock);

        final RecurentArc dependentArcDummy =
                PowerMock.createStrictMock(RecurentArc.class);
        PowerMock.replay(dependentArcDummy);

        replayTestedStubs();

        this.tested.removeAndRealign(removedArcStub, processorStub,
                ImmutableMap.<EnterNode, Set<RecurentArc>> of(fromNodeStub,
                        ImmutableSet.of(dependentArcDummy)), ImmutableSet
                        .<EnterNode> of(fromNodeStub));

        EasyMock.verify(removedArcStub);
        EasyMock.verify(fromNodeStub);
        EasyMock.verify(toNodeDummy);
        EasyMock.verify(newToNodeStub);
        EasyMock.verify(processorStub);
        EasyMock.verify(updateDummy);
        EasyMock.verify(updateBuilderMock);
        PowerMock.verify(dependentArcDummy);
        verifyTestedStubs();
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.DefaultSystemGraph#removeAndRealign(cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc, cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.RealignmentProcessor, java.util.Map, java.util.Set)}
     * .
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemoveAndRealignArcWhenFromReferredByOtherThanRemoved() {
        final NormalWord nameDummy =
                EasyMock.createStrictMock(NormalWord.class);
        EasyMock.replay(nameDummy);

        final Network networkStub = EasyMock.createStrictMock(Network.class);
        EasyMock.expect(networkStub.getName()).andStubReturn(
                this.networkNameStub);
        EasyMock.replay(networkStub);

        final EnterNode fromNodeStub = EasyMock.createMock(EnterNode.class);
        EasyMock.expect(fromNodeStub.getName()).andReturn(nameDummy);
        EasyMock.expect(fromNodeStub.getNetwork()).andReturn(networkStub);
        EasyMock.replay(fromNodeStub);

        final IsolatedNode newFromNodeDummy =
                EasyMock.createStrictMock(IsolatedNode.class);
        EasyMock.replay(newFromNodeDummy);

        final Node toNodeDummy = EasyMock.createStrictMock(Node.class);
        EasyMock.replay(toNodeDummy);

        final Node newToNodeDummy = EasyMock.createStrictMock(Node.class);
        EasyMock.replay(newToNodeDummy);

        final Arc removedArcStub = EasyMock.createMock(Arc.class);
        EasyMock.expect(removedArcStub.getFrom()).andStubReturn(fromNodeStub);
        EasyMock.expect(removedArcStub.getTo()).andStubReturn(toNodeDummy);
        EasyMock.expect(removedArcStub.getName()).andStubReturn(nameDummy);
        EasyMock.replay(removedArcStub);

        final RealignmentProcessor processorStub =
                EasyMock.createMock(RealignmentProcessor.class);
        EasyMock.expect(processorStub.realign(fromNodeStub)).andStubReturn(
                newFromNodeDummy);
        EasyMock.expect(processorStub.realign(toNodeDummy)).andStubReturn(
                newToNodeDummy);
        EasyMock.replay(processorStub);

        final Update updateDummy = EasyMock.createStrictMock(Update.class);
        EasyMock.replay(updateDummy);

        final RecurentArc dependentArcStub =
                PowerMock.createMock(RecurentArc.class);
        EasyMock.expect(dependentArcStub.getName()).andReturn(nameDummy);
        EasyMock.expect(dependentArcStub.getNetwork()).andReturn(networkStub);
        PowerMock.replay(dependentArcStub);

        this.baseGraphStub.removeEdge(removedArcStub);
        this.baseGraphStub.add(removedArcStub, removedArcStub.getName(),
                fromNodeStub, toNodeDummy);

        replayTestedStubs();

        try {
            this.tested.removeAndRealign(removedArcStub, processorStub,
                    ImmutableMap.<EnterNode, Set<RecurentArc>> of(fromNodeStub,
                            ImmutableSet.of(dependentArcStub)), ImmutableSet
                            .<EnterNode> of(fromNodeStub));
        } finally {
            EasyMock.verify(removedArcStub);
            EasyMock.verify(fromNodeStub);
            EasyMock.verify(networkStub);
            EasyMock.verify(processorStub);
            EasyMock.verify(nameDummy);
            EasyMock.verify(newFromNodeDummy);
            EasyMock.verify(toNodeDummy);
            EasyMock.verify(newToNodeDummy);
            EasyMock.verify(updateDummy);
            PowerMock.verify(dependentArcStub);
            verifyTestedStubs();
        }
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.DefaultSystemGraph#removeAndRealign(cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc, cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.RealignmentProcessor, java.util.Map, java.util.Set)}
     * .
     */
    @Test
    public void testRemoveAndRealignArcWhenOnlyJustRemovedRefersFrom() {
        final EnterNode fromNodeDummy =
                EasyMock.createStrictMock(EnterNode.class);
        EasyMock.replay(fromNodeDummy);

        final Node toNodeDummy = EasyMock.createStrictMock(Node.class);
        EasyMock.replay(toNodeDummy);

        final IsolatedNode newFromNodeStub =
                EasyMock.createStrictMock(IsolatedNode.class);
        EasyMock.expect(newFromNodeStub.getName()).andStubReturn(
                this.fromNodeNameDummy);
        EasyMock.replay(newFromNodeStub);

        final Node newToNodeStub = EasyMock.createStrictMock(Node.class);
        EasyMock.expect(newToNodeStub.getName()).andStubReturn(
                this.toNodeNameDummy);
        EasyMock.replay(newToNodeStub);

        final RecurentArc removedArcStub =
                PowerMock.createMock(RecurentArc.class);
        EasyMock.expect(removedArcStub.getFrom()).andStubReturn(fromNodeDummy);
        EasyMock.expect(removedArcStub.getTo()).andStubReturn(toNodeDummy);
        EasyMock.expect(removedArcStub.getTarget())
                .andStubReturn(fromNodeDummy);
        PowerMock.replay(removedArcStub);

        final RealignmentProcessor processorStub =
                EasyMock.createMock(RealignmentProcessor.class);
        EasyMock.expect(processorStub.realign(fromNodeDummy)).andStubReturn(
                newFromNodeStub);
        EasyMock.expect(processorStub.realign(toNodeDummy)).andStubReturn(
                newToNodeStub);
        EasyMock.replay(processorStub);

        final Update updateDummy = EasyMock.createStrictMock(Update.class);
        EasyMock.replay(updateDummy);

        final UpdateBuilder updateBuilderMock =
                EasyMock.createMock(UpdateBuilder.class);
        updateBuilderMock.addSwitched(fromNodeDummy, newFromNodeStub);
        updateBuilderMock.addSwitched(toNodeDummy, newToNodeStub);
        updateBuilderMock.addRemovedReference(removedArcStub);
        updateBuilderMock.addRemovedInitial(fromNodeDummy);
        EasyMock.expect(updateBuilderMock.build()).andStubReturn(updateDummy);
        EasyMock.replay(updateBuilderMock);

        this.baseGraphStub.removeEdge(removedArcStub);
        EasyMock.checkOrder(this.baseGraphStub, false);
        this.baseGraphStub.replaceVertex(fromNodeDummy, newFromNodeStub,
                this.fromNodeNameDummy);
        this.baseGraphStub.replaceVertex(toNodeDummy, newToNodeStub,
                this.toNodeNameDummy);
        EasyMock.checkOrder(this.baseGraphStub, true);

        EasyMock.expect(this.updateBuilderFactoryStub.produce()).andStubReturn(
                updateBuilderMock);

        replayTestedStubs();

        this.tested.removeAndRealign(removedArcStub, processorStub,
                ImmutableMap.<EnterNode, Set<RecurentArc>> of(fromNodeDummy,
                        ImmutableSet.of(removedArcStub)), ImmutableSet
                        .<EnterNode> of(fromNodeDummy));

        EasyMock.verify(fromNodeDummy);
        EasyMock.verify(newFromNodeStub);
        EasyMock.verify(toNodeDummy);
        EasyMock.verify(newToNodeStub);
        EasyMock.verify(processorStub);
        EasyMock.verify(updateDummy);
        EasyMock.verify(updateBuilderMock);
        PowerMock.verify(removedArcStub);
        verifyTestedStubs();
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.DefaultSystemGraph#removeAndRealign(cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc, cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.RealignmentProcessor, java.util.Map, java.util.Set)}
     * .
     */
    @Test
    public void testRemoveAndRealignArcWhenToIsEnterNodeNow() {
        final Node fromNodeDummy = EasyMock.createStrictMock(Node.class);
        EasyMock.replay(fromNodeDummy);

        final Node toNodeDummy = EasyMock.createStrictMock(Node.class);
        EasyMock.replay(toNodeDummy);

        final Node newFromNodeStub = EasyMock.createStrictMock(Node.class);
        EasyMock.expect(newFromNodeStub.getName()).andStubReturn(
                this.fromNodeNameDummy);
        EasyMock.replay(newFromNodeStub);

        final EnterNode newToNodeStub =
                EasyMock.createStrictMock(EnterNode.class);
        EasyMock.expect(newToNodeStub.getName()).andStubReturn(
                this.toNodeNameDummy);
        EasyMock.replay(newToNodeStub);

        final Arc removedArcStub = PowerMock.createMock(Arc.class);
        EasyMock.expect(removedArcStub.getFrom()).andStubReturn(fromNodeDummy);
        EasyMock.expect(removedArcStub.getTo()).andStubReturn(toNodeDummy);
        PowerMock.replay(removedArcStub);

        final RealignmentProcessor processorStub =
                EasyMock.createMock(RealignmentProcessor.class);
        EasyMock.expect(processorStub.realign(fromNodeDummy)).andStubReturn(
                newFromNodeStub);
        EasyMock.expect(processorStub.realign(toNodeDummy)).andStubReturn(
                newToNodeStub);
        EasyMock.replay(processorStub);

        final Update updateDummy = EasyMock.createStrictMock(Update.class);
        EasyMock.replay(updateDummy);

        final UpdateBuilder updateBuilderMock =
                EasyMock.createMock(UpdateBuilder.class);
        updateBuilderMock.addSwitched(fromNodeDummy, newFromNodeStub);
        updateBuilderMock.addSwitched(toNodeDummy, newToNodeStub);
        updateBuilderMock.addNewInitial(newToNodeStub);
        EasyMock.expect(updateBuilderMock.build()).andStubReturn(updateDummy);
        EasyMock.replay(updateBuilderMock);

        this.baseGraphStub.removeEdge(removedArcStub);
        EasyMock.checkOrder(this.baseGraphStub, false);
        this.baseGraphStub.replaceVertex(fromNodeDummy, newFromNodeStub,
                this.fromNodeNameDummy);
        this.baseGraphStub.replaceVertex(toNodeDummy, newToNodeStub,
                this.toNodeNameDummy);
        EasyMock.checkOrder(this.baseGraphStub, true);

        EasyMock.expect(this.updateBuilderFactoryStub.produce()).andStubReturn(
                updateBuilderMock);

        replayTestedStubs();

        this.tested.removeAndRealign(removedArcStub, processorStub,
                ImmutableMap.<EnterNode, Set<RecurentArc>> of(),
                ImmutableSet.<EnterNode> of());

        EasyMock.verify(fromNodeDummy);
        EasyMock.verify(newFromNodeStub);
        EasyMock.verify(toNodeDummy);
        EasyMock.verify(newToNodeStub);
        EasyMock.verify(processorStub);
        EasyMock.verify(updateDummy);
        EasyMock.verify(updateBuilderMock);
        PowerMock.verify(removedArcStub);
        verifyTestedStubs();
    }

    private void verifyTestedStubs() {
        EasyMock.verify(this.baseGraphStub, this.updateBuilderFactoryStub,
                this.fromNodeNameDummy, this.toNodeNameDummy,
                this.networkNameStub);
    }

}
