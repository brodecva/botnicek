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
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.dfs;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.easymock.EasyMock;
import org.junit.Test;

import com.google.common.collect.ImmutableSet;

import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public class DefaultDfsVisitorTest {

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.dfs.DefaultDfsVisitor#create(java.util.Set)}
     * .
     */
    @Test
    public void testCreateSetOfQextendsDfsObserver() {
        final DfsObserver firstObserverDummy =
                EasyMock.createStrictMock(DfsObserver.class);
        EasyMock.replay(firstObserverDummy);

        final DfsObserver secondObserverDummy =
                EasyMock.createStrictMock(DfsObserver.class);
        EasyMock.replay(secondObserverDummy);

        DefaultDfsVisitor.create(ImmutableSet.of(firstObserverDummy,
                secondObserverDummy));
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.dfs.DefaultDfsVisitor#visitEnter(Node)}
     * ,
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.dfs.DefaultDfsVisitor#visitExit(Node)}
     * and
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.dfs.DefaultDfsVisitor#visit(cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc)}
     * .
     */
    @Test(expected = IllegalStateException.class)
    public void testVisitArcWhenFromEnteredAndExited() {
        final Node fromNodeDummy = EasyMock.createStrictMock(Node.class);
        EasyMock.replay(fromNodeDummy);

        final Node toNodeDummy = EasyMock.createStrictMock(Node.class);
        EasyMock.replay(toNodeDummy);

        final Arc arcStub = EasyMock.createMock(Arc.class);
        EasyMock.expect(arcStub.getFrom()).andStubReturn(fromNodeDummy);
        EasyMock.expect(arcStub.getTo()).andStubReturn(toNodeDummy);
        EasyMock.replay(arcStub);

        final DfsObserver observerMock =
                EasyMock.createStrictMock(DfsObserver.class);
        observerMock.notifyDiscovery(fromNodeDummy);
        observerMock.notifyFinish(fromNodeDummy);
        EasyMock.replay(observerMock);

        final DefaultDfsVisitor tested = DefaultDfsVisitor.create(observerMock);
        tested.visitEnter(fromNodeDummy);
        tested.visitExit(fromNodeDummy);
        tested.visit(arcStub);

        EasyMock.verify(observerMock);
        EasyMock.verify(fromNodeDummy);
        EasyMock.verify(toNodeDummy);
        EasyMock.verify(arcStub);
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.dfs.DefaultDfsVisitor#visit(cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc)}
     * .
     */
    @Test(expected = IllegalStateException.class)
    public void testVisitArcWhenFromNotVisited() {
        final Node fromNodeDummy = EasyMock.createStrictMock(Node.class);
        EasyMock.replay(fromNodeDummy);

        final Node toNodeDummy = EasyMock.createStrictMock(Node.class);
        EasyMock.replay(toNodeDummy);

        final Arc arcStub = EasyMock.createMock(Arc.class);
        EasyMock.expect(arcStub.getFrom()).andStubReturn(fromNodeDummy);
        EasyMock.expect(arcStub.getTo()).andStubReturn(toNodeDummy);
        EasyMock.replay(arcStub);

        final DfsObserver observerMock =
                EasyMock.createStrictMock(DfsObserver.class);
        EasyMock.replay(observerMock);

        DefaultDfsVisitor.create(observerMock).visit(arcStub);

        EasyMock.verify(observerMock);
        EasyMock.verify(fromNodeDummy);
        EasyMock.verify(toNodeDummy);
        EasyMock.verify(arcStub);
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.dfs.DefaultDfsVisitor#visitEnter(Node)}
     * ,
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.dfs.DefaultDfsVisitor#visitExit(Node)}
     * and
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.dfs.DefaultDfsVisitor#visit(cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc)}
     * .
     */
    @Test
    public
            void
            testVisitArcWhenToEnteredAndExitedAfterFromVisitNotifiesForwardArc() {
        final Node fromNodeDummy = EasyMock.createStrictMock(Node.class);
        EasyMock.replay(fromNodeDummy);

        final Node toNodeDummy = EasyMock.createStrictMock(Node.class);
        EasyMock.replay(toNodeDummy);

        final Arc arcStub = EasyMock.createMock(Arc.class);
        EasyMock.expect(arcStub.getFrom()).andStubReturn(fromNodeDummy);
        EasyMock.expect(arcStub.getTo()).andStubReturn(toNodeDummy);
        EasyMock.replay(arcStub);

        final DfsObserver observerMock =
                EasyMock.createStrictMock(DfsObserver.class);
        observerMock.notifyDiscovery(fromNodeDummy);
        observerMock.notifyDiscovery(toNodeDummy);
        observerMock.notifyFinish(toNodeDummy);
        observerMock.notifyExamination(arcStub);
        observerMock.notifyForward(arcStub);
        EasyMock.replay(observerMock);

        final DefaultDfsVisitor tested = DefaultDfsVisitor.create(observerMock);
        tested.visitEnter(fromNodeDummy);
        tested.visitEnter(toNodeDummy);
        tested.visitExit(toNodeDummy);
        tested.visit(arcStub);

        EasyMock.verify(observerMock);
        EasyMock.verify(fromNodeDummy);
        EasyMock.verify(toNodeDummy);
        EasyMock.verify(arcStub);
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.dfs.DefaultDfsVisitor#visitEnter(Node)}
     * ,
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.dfs.DefaultDfsVisitor#visitExit(Node)}
     * and
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.dfs.DefaultDfsVisitor#visit(cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc)}
     * .
     */
    @Test
    public void
            testVisitArcWhenToEnteredAndExitedBeforeFromVisitNotifiesCrossArc() {
        final Node fromNodeDummy = EasyMock.createStrictMock(Node.class);
        EasyMock.replay(fromNodeDummy);

        final Node toNodeDummy = EasyMock.createStrictMock(Node.class);
        EasyMock.replay(toNodeDummy);

        final Arc arcStub = EasyMock.createMock(Arc.class);
        EasyMock.expect(arcStub.getFrom()).andStubReturn(fromNodeDummy);
        EasyMock.expect(arcStub.getTo()).andStubReturn(toNodeDummy);
        EasyMock.replay(arcStub);

        final DfsObserver observerMock =
                EasyMock.createStrictMock(DfsObserver.class);
        observerMock.notifyDiscovery(toNodeDummy);
        observerMock.notifyFinish(toNodeDummy);
        observerMock.notifyDiscovery(fromNodeDummy);
        observerMock.notifyExamination(arcStub);
        observerMock.notifyCross(arcStub);
        EasyMock.replay(observerMock);

        final DefaultDfsVisitor tested = DefaultDfsVisitor.create(observerMock);
        tested.visitEnter(toNodeDummy);
        tested.visitExit(toNodeDummy);
        tested.visitEnter(fromNodeDummy);
        tested.visit(arcStub);

        EasyMock.verify(observerMock);
        EasyMock.verify(fromNodeDummy);
        EasyMock.verify(toNodeDummy);
        EasyMock.verify(arcStub);
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.dfs.DefaultDfsVisitor#visitEnter(Node)}
     * and
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.dfs.DefaultDfsVisitor#visit(cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc)}
     * .
     */
    @Test
    public void testVisitArcWhenToEnteredButNotExitedNotifiesBackArc() {
        final Node fromNodeDummy = EasyMock.createStrictMock(Node.class);
        EasyMock.replay(fromNodeDummy);

        final Node toNodeDummy = EasyMock.createStrictMock(Node.class);
        EasyMock.replay(toNodeDummy);

        final Arc arcStub = EasyMock.createMock(Arc.class);
        EasyMock.expect(arcStub.getFrom()).andStubReturn(fromNodeDummy);
        EasyMock.expect(arcStub.getTo()).andStubReturn(toNodeDummy);
        EasyMock.replay(arcStub);

        final DfsObserver observerMock =
                EasyMock.createStrictMock(DfsObserver.class);
        observerMock.notifyDiscovery(toNodeDummy);
        observerMock.notifyDiscovery(fromNodeDummy);
        observerMock.notifyExamination(arcStub);
        observerMock.notifyBack(arcStub);
        EasyMock.replay(observerMock);

        final DefaultDfsVisitor tested = DefaultDfsVisitor.create(observerMock);
        tested.visitEnter(toNodeDummy);
        tested.visitEnter(fromNodeDummy);
        tested.visit(arcStub);

        EasyMock.verify(observerMock);
        EasyMock.verify(fromNodeDummy);
        EasyMock.verify(toNodeDummy);
        EasyMock.verify(arcStub);
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.dfs.DefaultDfsVisitor#visitEnter(Node)}
     * and
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.dfs.DefaultDfsVisitor#visit(cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc)}
     * .
     */
    @Test
    public void testVisitArcWhenToNotEnteredNotifiesTreeArc() {
        final Node fromNodeDummy = EasyMock.createStrictMock(Node.class);
        EasyMock.replay(fromNodeDummy);

        final Node toNodeDummy = EasyMock.createStrictMock(Node.class);
        EasyMock.replay(toNodeDummy);

        final Arc arcStub = EasyMock.createMock(Arc.class);
        EasyMock.expect(arcStub.getFrom()).andStubReturn(fromNodeDummy);
        EasyMock.expect(arcStub.getTo()).andStubReturn(toNodeDummy);
        EasyMock.replay(arcStub);

        final DfsObserver observerMock =
                EasyMock.createStrictMock(DfsObserver.class);
        observerMock.notifyDiscovery(fromNodeDummy);
        observerMock.notifyExamination(arcStub);
        observerMock.notifyTree(arcStub);
        EasyMock.replay(observerMock);

        final DefaultDfsVisitor tested = DefaultDfsVisitor.create(observerMock);
        tested.visitEnter(fromNodeDummy);
        tested.visit(arcStub);

        EasyMock.verify(observerMock);
        EasyMock.verify(fromNodeDummy);
        EasyMock.verify(toNodeDummy);
        EasyMock.verify(arcStub);
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.dfs.DefaultDfsVisitor#visited(cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node)}
     * .
     */
    @Test
    public void testVisitedWhenNotVisitedReturnsFalse() {
        final Node nodeDummy = EasyMock.createStrictMock(Node.class);
        EasyMock.replay(nodeDummy);

        assertFalse(DefaultDfsVisitor.create().visited(nodeDummy));

        EasyMock.verify(nodeDummy);
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.dfs.DefaultDfsVisitor#visited(cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node)}
     * and
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.dfs.DefaultDfsVisitor#visitEnter(cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node)}
     * .
     */
    @Test
    public void testVisitedWhenVisitedReturnsTrue() {
        final Node nodeDummy = EasyMock.createStrictMock(Node.class);
        EasyMock.replay(nodeDummy);

        final DefaultDfsVisitor tested = DefaultDfsVisitor.create();
        tested.visitEnter(nodeDummy);

        assertTrue(tested.visited(nodeDummy));

        EasyMock.verify(nodeDummy);
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.dfs.DefaultDfsVisitor#visitEnter(cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node)}
     * and
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.dfs.DefaultDfsVisitor#visitExit(cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node)}
     * .
     */
    @Test
    public void testVisitEnterAndVisitExitNotifiesFinish() {
        final Node nodeDummy = EasyMock.createStrictMock(Node.class);
        EasyMock.replay(nodeDummy);

        final DfsObserver observerMock =
                EasyMock.createStrictMock(DfsObserver.class);
        observerMock.notifyDiscovery(nodeDummy);
        observerMock.notifyFinish(nodeDummy);
        EasyMock.replay(observerMock);

        final DefaultDfsVisitor tested = DefaultDfsVisitor.create(observerMock);
        tested.visitEnter(nodeDummy);
        tested.visitExit(nodeDummy);

        EasyMock.verify(observerMock);
        EasyMock.verify(nodeDummy);
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.dfs.DefaultDfsVisitor#visitEnter(cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node)}
     * .
     */
    @Test
    public void testVisitEnterNotifiesDiscovery() {
        final Node nodeDummy = EasyMock.createStrictMock(Node.class);
        EasyMock.replay(nodeDummy);

        final DfsObserver observerMock =
                EasyMock.createStrictMock(DfsObserver.class);
        observerMock.notifyDiscovery(nodeDummy);
        EasyMock.replay(observerMock);

        DefaultDfsVisitor.create(observerMock).visitEnter(nodeDummy);

        EasyMock.verify(observerMock);
        EasyMock.verify(nodeDummy);
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.dfs.DefaultDfsVisitor#visitEnter(cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node)}
     * .
     */
    @Test(expected = IllegalStateException.class)
    public void testVisitEnterWhenAlreadyVisited() {
        final Node nodeDummy = EasyMock.createStrictMock(Node.class);
        EasyMock.replay(nodeDummy);

        final DfsObserver observerMock =
                EasyMock.createStrictMock(DfsObserver.class);
        observerMock.notifyDiscovery(nodeDummy);
        EasyMock.replay(observerMock);

        final DefaultDfsVisitor tested = DefaultDfsVisitor.create(observerMock);
        tested.visitEnter(nodeDummy);
        tested.visitEnter(nodeDummy);

        EasyMock.verify(observerMock);
        EasyMock.verify(nodeDummy);
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.dfs.DefaultDfsVisitor#visitExit(cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node)}
     * .
     */
    @Test(expected = IllegalStateException.class)
    public void testVisitExitWhenNotEntered() {
        final Node nodeDummy = EasyMock.createStrictMock(Node.class);
        EasyMock.replay(nodeDummy);

        final DfsObserver observerMock =
                EasyMock.createStrictMock(DfsObserver.class);
        observerMock.notifyFinish(nodeDummy);
        EasyMock.replay(observerMock);

        DefaultDfsVisitor.create(observerMock).visitExit(nodeDummy);

        EasyMock.verify(observerMock);
        EasyMock.verify(nodeDummy);
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.dfs.DefaultDfsVisitor#visit(cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network)}
     * .
     */
    @Test
    public void testVisitNetworkNotifies() {
        final Network networkDummy = EasyMock.createStrictMock(Network.class);
        EasyMock.replay(networkDummy);

        final DfsObserver observerMock =
                EasyMock.createStrictMock(DfsObserver.class);
        observerMock.notifyVisit(networkDummy);
        EasyMock.replay(observerMock);

        DefaultDfsVisitor.create(observerMock).visit(networkDummy);

        EasyMock.verify(observerMock);
        EasyMock.verify(networkDummy);
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.dfs.DefaultDfsVisitor#visit(cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System)}
     * .
     */
    @Test
    public void testVisitSystemNotifies() {
        final System systemDummy = EasyMock.createStrictMock(System.class);
        EasyMock.replay(systemDummy);

        final DfsObserver observerMock =
                EasyMock.createStrictMock(DfsObserver.class);
        observerMock.notifyVisit(systemDummy);
        EasyMock.replay(observerMock);

        DefaultDfsVisitor.create(observerMock).visit(systemDummy);

        EasyMock.verify(observerMock);
        EasyMock.verify(systemDummy);
    }

}
