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

import static org.junit.Assert.*;

import java.util.Set;

import org.easymock.EasyMock;
import org.easymock.IAnswer;
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

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.Code;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWords;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.Visitor;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.dfs.DfsObserver;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.dfs.DfsVisitor;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.dfs.DfsVisitorFactory;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.events.ArcChangedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.events.ArcRetypedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.ArcModifier;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.RecurentArc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.TransitionArc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.events.ArcAddedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.events.NodeAddedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.events.NodeRemovedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.events.NodeMovedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.events.NodeRenamedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.events.NodeTypeChangedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.EnterNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.ExitNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.FunctionalNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.InputNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.IsolatedNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.NodeModifier;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.ProcessingNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.RealignmentProcessor;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.events.AvailableReferencesExtendedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.events.AvailableReferencesReducedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.events.NetworkAddedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.updates.Update;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.types.Priority;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Intended;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.Dispatcher;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.test.UnitTest;

/**
 * Testuje výchozí implementaci systému sítí.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see DefaultSystem
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({NetworkAddedEvent.class, Priority.class, NormalWords.class, TransitionArc.class, RecurentArc.class})
@Category(UnitTest.class)
public class DefaultSystemTest {

    private static final int FROM_NODE_X = 0;
    private static final int FROM_NODE_Y = 1;
    private static final int TO_NODE_X = 5;
    private static final int TO_NODE_Y = 6;
    private static final int OTHER_FROM_NODE_X = 10;
    private static final int OTHER_FROM_NODE_Y = 11;
    private static final int OTHER_TO_NODE_X = 15;
    private static final int OTHER_TO_NODE_Y = 16;
    private static final String FROM_NODE_NAME = "1";
    private static final String TO_NODE_NAME = "2";
    private static final String OTHER_FROM_NODE_NAME = "3";
    private static final String OTHER_TO_NODE_NAME = "4";
    private static final String REFERRING_ARC_NAME = "Referring";
    private static final String AFFECTED_ARC_NAME = "Affected";
    private static final String OTHER_NETWORK_NAME = "Other network";
    private static final String NETWORK_NAME = "Network";
    private static final String SYSTEM_NAME = "System";
    private static final String RESERVED_NETWORK_NAME = "Reserved";

    private System tested = Intended.nullReference();
    private SystemGraph graph = Intended.nullReference();
    private NamingAuthority statesNamingAuthority = Intended.nullReference();
    private NamingAuthority variablesNamingAuthority = Intended.nullReference();
    private NodeModifier nodeModifier = Intended.nullReference();
    private ArcModifier arcModifier = Intended.nullReference();
    private RealignmentProcessor realignmentProcessor = Intended.nullReference();
    private DfsVisitorFactory systemVisitorFactory = Intended.nullReference();
    private Dispatcher dispatcher = Intended.nullReference();
    private InitialNodeFactory initialNodeFactory = Intended.nullReference();
    private InitialArcFactory initialArcFactory = Intended.nullReference();
    private Network networkStub = Intended.nullReference();
    private Network otherNetworkStub = Intended.nullReference();
    private NormalWord affectedArcNameDummy = Intended.nullReference();
    private TransitionArc affectedArcStub = Intended.nullReference();
    private NormalWord fromNodeNameStub = Intended.nullReference();
    private NormalWord toNodeNameStub = Intended.nullReference();
    private NormalWord referringArcNameDummy = Intended.nullReference();
    private NormalWord otherFromNodeNameStub = Intended.nullReference();
    private NormalWord otherToNodeNameStub = Intended.nullReference();
    private Priority priorityDummy = Intended.nullReference();
    private EnterNode referredFromNodeStub = Intended.nullReference();
    private Code emptyCodeDummy = Intended.nullReference();
    private TransitionArc otherNetworkTemporaryArcStub = Intended.nullReference();
    private TransitionArc thisTemporaryArcStub = Intended.nullReference();
    private RecurentArc otherNetworkReferringArcStub = Intended.nullReference();
    private RecurentArc thisNetworkReferringArcStub = Intended.nullReference();
    private IsolatedNode fromIsolatedNodeStub = Intended.nullReference();
    private IsolatedNode toIsolatedNodeStub = Intended.nullReference();
    private IsolatedNode otherNetworkOtherFromIsolatedNodeStub = Intended.nullReference();
    private IsolatedNode otherFromIsolatedNodeStub = Intended.nullReference();
    private IsolatedNode otherToIsolatedNodeStub = Intended.nullReference();
    private IsolatedNode otherNetworkOtherToIsolatedNodeStub = Intended.nullReference();
    private ExitNode toExitNodeStub = Intended.nullReference();
    private EnterNode otherFromEnterNodeStub = Intended.nullReference();
    private ExitNode otherToExitNodeStub = Intended.nullReference();
    private EnterNode otherNetworkOtherFromEnterNodeStub = Intended.nullReference();
    private ExitNode otherNetworkOtherToExitNodeStub = Intended.nullReference();

    /**
     * Sestaví testovací objekty.
     * 
     * @throws java.lang.Exception pokud dojde k vyhození výjimky
     */
    @Before
    public void setUp() throws Exception {
        graph = EasyMock.createStrictMock(SystemGraph.class);
        
        statesNamingAuthority = EasyMock.createStrictMock(NamingAuthority.class);
        
        variablesNamingAuthority = EasyMock.createStrictMock(NamingAuthority.class);
        
        nodeModifier = EasyMock.createStrictMock(NodeModifier.class);
        
        arcModifier = EasyMock.createStrictMock(ArcModifier.class);
        
        realignmentProcessor = EasyMock.createStrictMock(RealignmentProcessor.class);
        
        systemVisitorFactory = EasyMock.createStrictMock(DfsVisitorFactory.class);
        
        initialNodeFactory = EasyMock.createStrictMock(InitialNodeFactory.class);
        
        initialArcFactory = EasyMock.createStrictMock(InitialArcFactory.class);
        
        dispatcher = EasyMock.createStrictMock(Dispatcher.class);
        
        this.tested = DefaultSystem.create(SYSTEM_NAME, graph, statesNamingAuthority, variablesNamingAuthority, nodeModifier, arcModifier, realignmentProcessor, ImmutableSet.of(RESERVED_NETWORK_NAME), systemVisitorFactory, initialNodeFactory, initialArcFactory, dispatcher);
        
        networkStub = EasyMock.createMock("networkStub", Network.class);
        EasyMock.expect(networkStub.getName()).andStubReturn(NETWORK_NAME);
        EasyMock.expect(networkStub.getSystem()).andStubReturn(tested);
        
        otherNetworkStub = EasyMock.createMock("otherNetworkStub", Network.class);
        EasyMock.expect(otherNetworkStub.getName()).andStubReturn(OTHER_NETWORK_NAME);
        EasyMock.expect(otherNetworkStub.getSystem()).andStubReturn(tested);
        
        affectedArcNameDummy = EasyMock.createStrictMock(NormalWord.class);
        EasyMock.expect(affectedArcNameDummy.getText()).andStubReturn(AFFECTED_ARC_NAME);
        
        affectedArcStub = PowerMock.createStrictMock(TransitionArc.class);
        EasyMock.expect(affectedArcStub.getName()).andStubReturn(affectedArcNameDummy);
        EasyMock.expect(affectedArcStub.getNetwork()).andStubReturn(networkStub);
        
        fromNodeNameStub = EasyMock.createStrictMock(NormalWord.class);
        EasyMock.expect(fromNodeNameStub.getText()).andStubReturn(FROM_NODE_NAME);
        
        toNodeNameStub = EasyMock.createStrictMock(NormalWord.class);
        EasyMock.expect(toNodeNameStub.getText()).andStubReturn(TO_NODE_NAME);
        
        referringArcNameDummy = EasyMock.createStrictMock("referringArcNameDummy", NormalWord.class);
        EasyMock.expect(referringArcNameDummy.getText()).andStubReturn(REFERRING_ARC_NAME);
        
        otherFromNodeNameStub = EasyMock.createStrictMock(NormalWord.class);
        EasyMock.expect(otherFromNodeNameStub.getText()).andStubReturn(OTHER_FROM_NODE_NAME);
        
        otherToNodeNameStub = EasyMock.createStrictMock(NormalWord.class);
        EasyMock.expect(otherToNodeNameStub.getText()).andStubReturn(OTHER_TO_NODE_NAME);
        
        priorityDummy = PowerMock.createStrictMock(Priority.class);
        
        referredFromNodeStub = EasyMock.createMock("referredFromNodeStub", EnterNode.class);
        EasyMock.expect(referredFromNodeStub.getName()).andStubReturn(fromNodeNameStub);
        EasyMock.expect(referredFromNodeStub.getNetwork()).andStubReturn(networkStub);
        
        emptyCodeDummy = EasyMock.createStrictMock(Code.class);
        
        thisTemporaryArcStub = PowerMock.createMock(TransitionArc.class);
        EasyMock.expect(thisTemporaryArcStub.getName()).andStubReturn(referringArcNameDummy);
        EasyMock.expect(thisTemporaryArcStub.getNetwork()).andStubReturn(networkStub);
        EasyMock.expect(thisTemporaryArcStub.getPriority()).andStubReturn(priorityDummy);
        
        otherNetworkTemporaryArcStub = PowerMock.createMock(TransitionArc.class);
        EasyMock.expect(otherNetworkTemporaryArcStub.getName()).andStubReturn(referringArcNameDummy);
        EasyMock.expect(otherNetworkTemporaryArcStub.getNetwork()).andStubReturn(otherNetworkStub);
        EasyMock.expect(otherNetworkTemporaryArcStub.getPriority()).andStubReturn(priorityDummy);
        
        otherNetworkReferringArcStub = PowerMock.createMock(RecurentArc.class);
        EasyMock.expect(otherNetworkReferringArcStub.getName()).andStubReturn(referringArcNameDummy);
        EasyMock.expect(otherNetworkReferringArcStub.getNetwork()).andStubReturn(otherNetworkStub);
        EasyMock.expect(otherNetworkReferringArcStub.getPriority()).andStubReturn(priorityDummy);
        EasyMock.expect(otherNetworkReferringArcStub.getNetwork()).andStubReturn(otherNetworkStub);
        EasyMock.expect(otherNetworkReferringArcStub.getTarget()).andStubReturn(referredFromNodeStub);
        
        thisNetworkReferringArcStub = PowerMock.createMock(RecurentArc.class);
        EasyMock.expect(thisNetworkReferringArcStub.getName()).andStubReturn(referringArcNameDummy);
        EasyMock.expect(thisNetworkReferringArcStub.getNetwork()).andStubReturn(networkStub);
        EasyMock.expect(thisNetworkReferringArcStub.getPriority()).andStubReturn(priorityDummy);
        EasyMock.expect(thisNetworkReferringArcStub.getNetwork()).andStubReturn(networkStub);
        EasyMock.expect(thisNetworkReferringArcStub.getTarget()).andStubReturn(referredFromNodeStub);
        
        fromIsolatedNodeStub = EasyMock.createStrictMock(IsolatedNode.class);
        EasyMock.expect(fromIsolatedNodeStub.getName()).andStubReturn(fromNodeNameStub);
        EasyMock.expect(fromIsolatedNodeStub.getNetwork()).andStubReturn(networkStub);
        EasyMock.expect(fromIsolatedNodeStub.pointsTo(EasyMock.notNull(Node.class))).andStubReturn(false);
        EasyMock.expect(fromIsolatedNodeStub.getOutDegree()).andStubReturn(0);
        EasyMock.expect(fromIsolatedNodeStub.getOuts()).andStubReturn(ImmutableSet.<Arc>of());
        EasyMock.expect(fromIsolatedNodeStub.getInDegree()).andStubReturn(0);
        EasyMock.expect(fromIsolatedNodeStub.getIns()).andStubReturn(ImmutableSet.<Arc>of());
        
        toIsolatedNodeStub = EasyMock.createStrictMock(IsolatedNode.class);
        EasyMock.expect(toIsolatedNodeStub.getName()).andStubReturn(toNodeNameStub);
        EasyMock.expect(toIsolatedNodeStub.getNetwork()).andStubReturn(networkStub);
        EasyMock.expect(toIsolatedNodeStub.pointsTo(EasyMock.notNull(Node.class))).andStubReturn(false);
        EasyMock.expect(toIsolatedNodeStub.getOutDegree()).andStubReturn(0);
        EasyMock.expect(toIsolatedNodeStub.getOuts()).andStubReturn(ImmutableSet.<Arc>of());
        EasyMock.expect(toIsolatedNodeStub.getInDegree()).andStubReturn(0);
        EasyMock.expect(toIsolatedNodeStub.getIns()).andStubReturn(ImmutableSet.<Arc>of());
        
        otherFromIsolatedNodeStub = EasyMock.createStrictMock("otherFromNodeStub", IsolatedNode.class);
        EasyMock.expect(otherFromIsolatedNodeStub.getName()).andStubReturn(otherFromNodeNameStub);
        EasyMock.expect(otherFromIsolatedNodeStub.getNetwork()).andStubReturn(networkStub);
        EasyMock.expect(otherFromIsolatedNodeStub.pointsTo(EasyMock.notNull(Node.class))).andStubReturn(false);
        EasyMock.expect(otherFromIsolatedNodeStub.getOutDegree()).andStubReturn(0);
        EasyMock.expect(otherFromIsolatedNodeStub.getOuts()).andStubReturn(ImmutableSet.<Arc>of());
        EasyMock.expect(otherFromIsolatedNodeStub.getInDegree()).andStubReturn(0);
        EasyMock.expect(otherFromIsolatedNodeStub.getIns()).andStubReturn(ImmutableSet.<Arc>of());
        
        otherNetworkOtherFromIsolatedNodeStub = EasyMock.createStrictMock("otherFromNodeStub", IsolatedNode.class);
        EasyMock.expect(otherNetworkOtherFromIsolatedNodeStub.getName()).andStubReturn(otherFromNodeNameStub);
        EasyMock.expect(otherNetworkOtherFromIsolatedNodeStub.getNetwork()).andStubReturn(otherNetworkStub);
        EasyMock.expect(otherNetworkOtherFromIsolatedNodeStub.pointsTo(EasyMock.notNull(Node.class))).andStubReturn(false);
        EasyMock.expect(otherNetworkOtherFromIsolatedNodeStub.getOutDegree()).andStubReturn(0);
        EasyMock.expect(otherNetworkOtherFromIsolatedNodeStub.getOuts()).andStubReturn(ImmutableSet.<Arc>of());
        EasyMock.expect(otherNetworkOtherFromIsolatedNodeStub.getInDegree()).andStubReturn(0);
        EasyMock.expect(otherNetworkOtherFromIsolatedNodeStub.getIns()).andStubReturn(ImmutableSet.<Arc>of());
        
        otherToIsolatedNodeStub = EasyMock.createStrictMock("otherFromNodeStub", IsolatedNode.class);
        EasyMock.expect(otherToIsolatedNodeStub.getName()).andStubReturn(otherToNodeNameStub);
        EasyMock.expect(otherToIsolatedNodeStub.getNetwork()).andStubReturn(networkStub);
        EasyMock.expect(otherToIsolatedNodeStub.pointsTo(EasyMock.notNull(Node.class))).andStubReturn(false);
        EasyMock.expect(otherToIsolatedNodeStub.getOutDegree()).andStubReturn(0);
        EasyMock.expect(otherToIsolatedNodeStub.getOuts()).andStubReturn(ImmutableSet.<Arc>of());
        EasyMock.expect(otherToIsolatedNodeStub.getInDegree()).andStubReturn(0);
        EasyMock.expect(otherToIsolatedNodeStub.getIns()).andStubReturn(ImmutableSet.<Arc>of());
        
        otherNetworkOtherToIsolatedNodeStub = EasyMock.createStrictMock("otherFromNodeStub", IsolatedNode.class);
        EasyMock.expect(otherNetworkOtherToIsolatedNodeStub.getName()).andStubReturn(otherToNodeNameStub);
        EasyMock.expect(otherNetworkOtherToIsolatedNodeStub.getNetwork()).andStubReturn(otherNetworkStub);
        EasyMock.expect(otherNetworkOtherToIsolatedNodeStub.pointsTo(EasyMock.notNull(Node.class))).andStubReturn(false);
        EasyMock.expect(otherNetworkOtherToIsolatedNodeStub.getOutDegree()).andStubReturn(0);
        EasyMock.expect(otherNetworkOtherToIsolatedNodeStub.getOuts()).andStubReturn(ImmutableSet.<Arc>of());
        EasyMock.expect(otherNetworkOtherToIsolatedNodeStub.getInDegree()).andStubReturn(0);
        EasyMock.expect(otherNetworkOtherToIsolatedNodeStub.getIns()).andStubReturn(ImmutableSet.<Arc>of());
        
        toExitNodeStub = EasyMock.createStrictMock(ExitNode.class);
        EasyMock.expect(toExitNodeStub.getName()).andStubReturn(toNodeNameStub);
        EasyMock.expect(toExitNodeStub.getNetwork()).andStubReturn(networkStub);
        
        otherFromEnterNodeStub = EasyMock.createStrictMock(EnterNode.class);
        EasyMock.expect(otherFromEnterNodeStub.getName()).andStubReturn(otherFromNodeNameStub);
        EasyMock.expect(otherFromEnterNodeStub.getNetwork()).andStubReturn(networkStub);
        
        otherToExitNodeStub = EasyMock.createStrictMock(ExitNode.class);
        EasyMock.expect(otherToExitNodeStub.getName()).andStubReturn(otherToNodeNameStub);
        EasyMock.expect(otherToExitNodeStub.getNetwork()).andStubReturn(networkStub);
        
        otherNetworkOtherFromEnterNodeStub = EasyMock.createStrictMock(EnterNode.class);
        EasyMock.expect(otherNetworkOtherFromEnterNodeStub.getName()).andStubReturn(otherFromNodeNameStub);
        EasyMock.expect(otherNetworkOtherFromEnterNodeStub.getNetwork()).andStubReturn(otherNetworkStub);
        
        otherNetworkOtherToExitNodeStub = EasyMock.createStrictMock(ExitNode.class);
        EasyMock.expect(otherNetworkOtherToExitNodeStub.getName()).andStubReturn(otherToNodeNameStub);
        EasyMock.expect(otherNetworkOtherToExitNodeStub.getNetwork()).andStubReturn(otherNetworkStub);
    }

    private void replayTested() {
        EasyMock.replay(graph, statesNamingAuthority, variablesNamingAuthority,
                nodeModifier, arcModifier, realignmentProcessor,
                systemVisitorFactory, initialNodeFactory, initialArcFactory,
                dispatcher, networkStub, otherNetworkStub,
                affectedArcNameDummy, fromNodeNameStub,
                toNodeNameStub, referringArcNameDummy, otherFromNodeNameStub,
                otherToNodeNameStub, referredFromNodeStub,
                emptyCodeDummy, fromIsolatedNodeStub, toIsolatedNodeStub,
                otherFromIsolatedNodeStub, otherToIsolatedNodeStub,
                otherNetworkOtherFromIsolatedNodeStub, otherNetworkOtherToIsolatedNodeStub,
                toExitNodeStub, otherFromEnterNodeStub, otherToExitNodeStub,
                otherNetworkOtherFromEnterNodeStub, otherNetworkOtherToExitNodeStub);
        PowerMock.replay(affectedArcStub, priorityDummy, thisTemporaryArcStub,
                otherNetworkTemporaryArcStub, otherNetworkReferringArcStub, thisNetworkReferringArcStub);
    }
    
    private void verifyTested() {
        EasyMock.verify(graph, statesNamingAuthority, variablesNamingAuthority,
                nodeModifier, arcModifier, realignmentProcessor,
                systemVisitorFactory, initialNodeFactory, initialArcFactory,
                dispatcher, networkStub, otherNetworkStub,
                affectedArcNameDummy, fromNodeNameStub,
                toNodeNameStub, referringArcNameDummy, otherFromNodeNameStub,
                otherToNodeNameStub, referredFromNodeStub,
                emptyCodeDummy, fromIsolatedNodeStub, toIsolatedNodeStub,
                otherFromIsolatedNodeStub, otherToIsolatedNodeStub,
                otherNetworkOtherFromIsolatedNodeStub, otherNetworkOtherToIsolatedNodeStub,
                toExitNodeStub, otherFromEnterNodeStub, otherToExitNodeStub,
                otherNetworkOtherFromEnterNodeStub, otherNetworkOtherToExitNodeStub);
        PowerMock.verify(affectedArcStub, priorityDummy, thisTemporaryArcStub,
                otherNetworkTemporaryArcStub, otherNetworkReferringArcStub, thisNetworkReferringArcStub);
    }
    
    /**
     * Uklidí testovací objekty.
     * 
     * @throws java.lang.Exception pokud dojde k vyhození výjimky
     */
    @After
    public void tearDown() throws Exception {
        tested = Intended.nullReference();
        graph = Intended.nullReference();
        statesNamingAuthority = Intended.nullReference();
        variablesNamingAuthority = Intended.nullReference();
        nodeModifier = Intended.nullReference();
        arcModifier = Intended.nullReference();
        realignmentProcessor = Intended.nullReference();
        systemVisitorFactory = Intended.nullReference();
        dispatcher = Intended.nullReference();
        initialNodeFactory = Intended.nullReference();
        initialArcFactory = Intended.nullReference();
        networkStub = Intended.nullReference();
        otherNetworkStub = Intended.nullReference();
        affectedArcNameDummy = Intended.nullReference();
        affectedArcStub = Intended.nullReference();
        fromNodeNameStub = Intended.nullReference();
        toNodeNameStub = Intended.nullReference();
        referringArcNameDummy = Intended.nullReference();
        otherFromNodeNameStub = Intended.nullReference();
        otherToNodeNameStub = Intended.nullReference();
        priorityDummy = Intended.nullReference();
        referredFromNodeStub = Intended.nullReference();
        emptyCodeDummy = Intended.nullReference();
        otherNetworkTemporaryArcStub = Intended.nullReference();
        thisTemporaryArcStub = Intended.nullReference();
        otherNetworkReferringArcStub = Intended.nullReference();
        thisNetworkReferringArcStub = Intended.nullReference();
        fromIsolatedNodeStub = Intended.nullReference();
        toIsolatedNodeStub = Intended.nullReference();
        otherNetworkOtherFromIsolatedNodeStub = Intended.nullReference();
        otherFromIsolatedNodeStub = Intended.nullReference();
        otherToIsolatedNodeStub = Intended.nullReference();
        otherNetworkOtherToIsolatedNodeStub = Intended.nullReference();
        toExitNodeStub = Intended.nullReference();
        otherFromEnterNodeStub = Intended.nullReference();
        otherToExitNodeStub = Intended.nullReference();
        otherNetworkOtherFromEnterNodeStub = Intended.nullReference();
        otherNetworkOtherToExitNodeStub = Intended.nullReference();
    }

    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.DefaultSystem#accept(cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.Visitor)} and {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.DefaultSystem#addNetwork(Network, String)}.
     */
    @Test
    public void testAccept() {
        final Visitor visitorMock = EasyMock.createStrictMock(Visitor.class);
        visitorMock.visit(tested);
        EasyMock.replay(visitorMock);
        
        final Network networkMock = networkStub;
        networkMock.accept(visitorMock);
        
        this.dispatcher.fire(EasyMock.notNull(NetworkAddedEvent.class));
        
        replayTested();
        
        this.tested.addNetwork(networkMock, NETWORK_NAME);
        this.tested.accept(visitorMock);
        
        EasyMock.verify(visitorMock);
        verifyTested();
    }

    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.DefaultSystem#getNetworks()} and {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.DefaultSystem#addNetwork(Network, String)}.
     */
    @Test
    public void testGetNetworks() {
        this.dispatcher.fire(EasyMock.notNull(NetworkAddedEvent.class));
        
        replayTested();
        
        this.tested.addNetwork(networkStub, NETWORK_NAME);
        assertEquals(ImmutableSet.of(networkStub), this.tested.getNetworks());
        
        verifyTested();
    }

    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.DefaultSystem#addNetwork(Network, String)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddNetworkWhenNameAlreadyPresent() {
        this.dispatcher.fire(EasyMock.notNull(NetworkAddedEvent.class));
        
        replayTested();
        
        this.tested.addNetwork(networkStub, NETWORK_NAME);
        try {
            this.tested.addNetwork(otherNetworkStub, NETWORK_NAME);
        } finally {
            verifyTested();
        }
    }
    
    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.DefaultSystem#addNetwork(Network, String)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddNetworkWhenNameAlreadyReserved() {
        replayTested();
        
        try {
            this.tested.addNetwork(networkStub, RESERVED_NETWORK_NAME);
        } finally {
            verifyTested();
        }
    }
    
    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.DefaultSystem#addNetwork(Network, String)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddNetworkWhenAlreadyPresent() {
        this.dispatcher.fire(EasyMock.notNull(NetworkAddedEvent.class));
        
        replayTested();
        
        this.tested.addNetwork(networkStub, NETWORK_NAME);
        try {
            this.tested.addNetwork(networkStub, "Same network");
        } finally {
            verifyTested();
        }
    }
    
    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.DefaultSystem#addNetwork(Network, String)}.
     */
    @Test
    public void testAddNetworkExpectEventFiredWithCorrectParameters() {
        final NetworkAddedEvent eventDummy = PowerMock.createStrictMock(NetworkAddedEvent.class);
        PowerMock.replay(eventDummy);
        
        PowerMock.mockStaticStrict(NetworkAddedEvent.class);
        EasyMock.expect(NetworkAddedEvent.create(tested, networkStub)).andStubReturn(eventDummy);
        PowerMock.replay(NetworkAddedEvent.class);
                
        this.dispatcher.fire(eventDummy);
        
        replayTested();
        
        this.tested.addNetwork(networkStub, NETWORK_NAME);
        
        PowerMock.verify(NetworkAddedEvent.class);
        PowerMock.verify(eventDummy);
        verifyTested();
    }

    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.DefaultSystem#removeNetwork(cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network)}, {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.DefaultSystem#getNetworks()} and {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.DefaultSystem#addNetwork(Network, String)}.
     */
    @Test
    public void testRemoveNetworkExpectAddedRemoved() {
        final DfsVisitor visitorDummy = EasyMock.createStrictMock(DfsVisitor.class);
        EasyMock.replay(visitorDummy);
        
        EasyMock.expect(this.systemVisitorFactory.produce(EasyMock.notNull(DfsObserver.class))).andStubReturn(visitorDummy);
        
        final Network networkMock = networkStub;
        networkMock.accept(visitorDummy);
        
        this.dispatcher.fire(EasyMock.notNull(NetworkAddedEvent.class));
        EasyMock.checkOrder(dispatcher, false);
        this.dispatcher.fire(EasyMock.notNull(cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.events.NetworkRemovedEvent.class));
        this.dispatcher.fire(EasyMock.notNull(cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.events.NetworkRemovedEvent.class));
        this.dispatcher.fire(EasyMock.notNull(AvailableReferencesReducedEvent.class));
        EasyMock.checkOrder(dispatcher, true);
        
        this.graph.purge(ImmutableSet.<Node>of());
        
        replayTested();
        
        this.tested.addNetwork(networkMock, NETWORK_NAME);
        this.tested.removeNetwork(networkMock);
        assertTrue(this.tested.getNetworks().isEmpty());
        
        PowerMock.verify(NetworkAddedEvent.class);
        EasyMock.verify(visitorDummy);
        verifyTested();
    }
    
    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.DefaultSystem#removeNetwork(cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemoveNetworkWhenNotPresent() {
        replayTested();
        
        try {
            this.tested.removeNetwork(networkStub);
        } finally {
            verifyTested();
        }
    }
    
    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.DefaultSystem#removeNetwork(cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network)} a další.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemoveNetworkWhenDependingArcsFromOtherNetwork() {
        EasyMock.expect(this.statesNamingAuthority.generate()).andReturn(FROM_NODE_NAME);
        EasyMock.expect(this.statesNamingAuthority.generate()).andReturn(TO_NODE_NAME);
        EasyMock.expect(this.statesNamingAuthority.use(AFFECTED_ARC_NAME)).andReturn(AFFECTED_ARC_NAME);
        EasyMock.expect(this.statesNamingAuthority.generate()).andReturn(FROM_NODE_NAME);
        EasyMock.expect(this.statesNamingAuthority.generate()).andReturn(TO_NODE_NAME);
        EasyMock.expect(this.statesNamingAuthority.use(REFERRING_ARC_NAME)).andReturn(REFERRING_ARC_NAME);
        EasyMock.expect(this.statesNamingAuthority.replace(REFERRING_ARC_NAME, REFERRING_ARC_NAME)).andReturn(REFERRING_ARC_NAME);
        
        PowerMock.mockStatic(NormalWords.class);
        EasyMock.expect(NormalWords.of(FROM_NODE_NAME)).andReturn(fromNodeNameStub);
        EasyMock.expect(NormalWords.of(TO_NODE_NAME)).andReturn(toNodeNameStub);
        EasyMock.expect(NormalWords.of(FROM_NODE_NAME)).andReturn(otherFromNodeNameStub);
        EasyMock.expect(NormalWords.of(TO_NODE_NAME)).andReturn(otherToNodeNameStub);
        EasyMock.expect(NormalWords.of(AFFECTED_ARC_NAME)).andReturn(affectedArcNameDummy);
        EasyMock.expect(NormalWords.of(REFERRING_ARC_NAME)).andReturn(referringArcNameDummy);
        EasyMock.expect(NormalWords.of(REFERRING_ARC_NAME)).andReturn(referringArcNameDummy);
        PowerMock.replay(NormalWords.class);
        
        EasyMock.expect(this.initialNodeFactory.produce(fromNodeNameStub, networkStub, FROM_NODE_X, FROM_NODE_Y)).andReturn(fromIsolatedNodeStub);
        EasyMock.expect(this.initialNodeFactory.produce(toNodeNameStub, networkStub, TO_NODE_X, TO_NODE_Y)).andReturn(toIsolatedNodeStub);
        EasyMock.expect(this.initialNodeFactory.produce(otherFromNodeNameStub, otherNetworkStub, OTHER_FROM_NODE_X, OTHER_FROM_NODE_Y)).andReturn(otherNetworkOtherFromIsolatedNodeStub);
        EasyMock.expect(this.initialNodeFactory.produce(otherToNodeNameStub, otherNetworkStub, OTHER_TO_NODE_X, OTHER_TO_NODE_Y)).andReturn(otherNetworkOtherToIsolatedNodeStub);
        
        EasyMock.expect(this.initialArcFactory.produce(networkStub, affectedArcNameDummy)).andReturn(affectedArcStub);
        EasyMock.expect(this.initialArcFactory.produce(otherNetworkStub, referringArcNameDummy)).andReturn(otherNetworkTemporaryArcStub);
        
        final Update addAffectedUpdateStub = EasyMock.createStrictMock(Update.class);
        EasyMock.expect(addAffectedUpdateStub.getInitialsAdded()).andStubReturn(ImmutableSet.of(referredFromNodeStub));
        EasyMock.expect(addAffectedUpdateStub.getInitialsRemoved()).andStubReturn(ImmutableSet.<EnterNode>of());
        EasyMock.expect(addAffectedUpdateStub.getSwitched()).andStubReturn(ImmutableMap.<Node, Node>of(fromIsolatedNodeStub, referredFromNodeStub, toIsolatedNodeStub, toExitNodeStub));
        EasyMock.replay(addAffectedUpdateStub);
        
        final Update addTemporaryUpdateStub = EasyMock.createStrictMock(Update.class);
        EasyMock.expect(addTemporaryUpdateStub.getInitialsAdded()).andStubReturn(ImmutableSet.of(otherNetworkOtherFromEnterNodeStub));
        EasyMock.expect(addTemporaryUpdateStub.getInitialsRemoved()).andStubReturn(ImmutableSet.<EnterNode>of());
        EasyMock.expect(addTemporaryUpdateStub.getSwitched()).andStubReturn(ImmutableMap.<Node, Node>of(otherNetworkOtherFromIsolatedNodeStub, otherNetworkOtherFromEnterNodeStub, otherNetworkOtherToIsolatedNodeStub, otherNetworkOtherToExitNodeStub));
        EasyMock.replay(addTemporaryUpdateStub);
        
        this.graph.add(fromIsolatedNodeStub);
        this.graph.add(toIsolatedNodeStub);
        EasyMock.expect(this.graph.addAndRealign(affectedArcStub, fromIsolatedNodeStub, toIsolatedNodeStub, realignmentProcessor, ImmutableSet.<EnterNode>of())).andReturn(addAffectedUpdateStub);
        this.graph.add(otherNetworkOtherFromIsolatedNodeStub);
        this.graph.add(otherNetworkOtherToIsolatedNodeStub);
        EasyMock.expect(this.graph.addAndRealign(otherNetworkTemporaryArcStub, otherNetworkOtherFromIsolatedNodeStub, otherNetworkOtherToIsolatedNodeStub, realignmentProcessor, ImmutableSet.<EnterNode>of())).andReturn(addTemporaryUpdateStub);
        EasyMock.expect(this.graph.containsEdge(otherNetworkTemporaryArcStub)).andReturn(true);
        EasyMock.expect(this.graph.containsVertex(referredFromNodeStub)).andReturn(true);
        this.graph.replaceEdge(otherNetworkTemporaryArcStub, otherNetworkReferringArcStub);
        
        EasyMock.expect(this.arcModifier.change(otherNetworkTemporaryArcStub, referringArcNameDummy, priorityDummy, RecurentArc.class, emptyCodeDummy, referredFromNodeStub)).andStubReturn(otherNetworkReferringArcStub);
        
        this.dispatcher.fire(EasyMock.notNull(NetworkAddedEvent.class));
        this.dispatcher.fire(EasyMock.notNull(NodeAddedEvent.class));
        this.dispatcher.fire(EasyMock.notNull(NodeAddedEvent.class));
        EasyMock.checkOrder(dispatcher, false);
        this.dispatcher.fire(EasyMock.notNull(NodeTypeChangedEvent.class));
        this.dispatcher.fire(EasyMock.notNull(NodeTypeChangedEvent.class));
        this.dispatcher.fire(EasyMock.notNull(ArcAddedEvent.class));
        this.dispatcher.fire(EasyMock.notNull(AvailableReferencesExtendedEvent.class));
        EasyMock.checkOrder(dispatcher, true);
        this.dispatcher.fire(EasyMock.notNull(NetworkAddedEvent.class));
        this.dispatcher.fire(EasyMock.notNull(NodeAddedEvent.class));
        this.dispatcher.fire(EasyMock.notNull(NodeAddedEvent.class));
        EasyMock.checkOrder(dispatcher, false);
        this.dispatcher.fire(EasyMock.notNull(NodeTypeChangedEvent.class));
        this.dispatcher.fire(EasyMock.notNull(NodeTypeChangedEvent.class));
        this.dispatcher.fire(EasyMock.notNull(ArcAddedEvent.class));
        this.dispatcher.fire(EasyMock.notNull(AvailableReferencesExtendedEvent.class));
        EasyMock.checkOrder(dispatcher, true);
        EasyMock.checkOrder(dispatcher, false);
        this.dispatcher.fire(EasyMock.notNull(ArcChangedEvent.class));
        this.dispatcher.fire(EasyMock.notNull(ArcRetypedEvent.class));
        EasyMock.checkOrder(dispatcher, true);
        
        replayTested();
        
        this.tested.addNetwork(networkStub, NETWORK_NAME);
        this.tested.addNode(networkStub, FROM_NODE_X, FROM_NODE_Y);
        this.tested.addNode(networkStub, TO_NODE_X, TO_NODE_Y);
        this.tested.addArc(networkStub, affectedArcNameDummy, fromIsolatedNodeStub, toIsolatedNodeStub);
        
        this.tested.addNetwork(otherNetworkStub, OTHER_NETWORK_NAME);
        this.tested.addNode(otherNetworkStub, OTHER_FROM_NODE_X, OTHER_FROM_NODE_Y);
        this.tested.addNode(otherNetworkStub, OTHER_TO_NODE_X, OTHER_TO_NODE_Y);
        this.tested.addArc(otherNetworkStub, referringArcNameDummy, otherNetworkOtherFromIsolatedNodeStub, otherNetworkOtherToIsolatedNodeStub);
        this.tested.changeArc(otherNetworkTemporaryArcStub, referringArcNameDummy, priorityDummy, RecurentArc.class, emptyCodeDummy, referredFromNodeStub);
                
        try {
            this.tested.removeNetwork(networkStub);
        } finally {
            EasyMock.verify(addAffectedUpdateStub);
            EasyMock.verify(addTemporaryUpdateStub);
            PowerMock.verify(NormalWords.class);
            verifyTested();
        }
    }
    
    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.DefaultSystem#removeNetwork(cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network)} a další.
     */
    @Test
    public void testRemoveNetworkWhenContainingDependingArcsExpectSubsequentReferredNetworkRemovalNotBlocked() {
        final DfsVisitor visitorDummy = EasyMock.createStrictMock(DfsVisitor.class);
        EasyMock.replay(visitorDummy);
                
        final Network networkMock = networkStub;
        networkMock.accept(visitorDummy);
        
        final Network otherNetworkMock = otherNetworkStub;
        otherNetworkMock.accept(visitorDummy);
        
        EasyMock.expect(this.statesNamingAuthority.generate()).andReturn(FROM_NODE_NAME);
        EasyMock.expect(this.statesNamingAuthority.generate()).andReturn(TO_NODE_NAME);
        EasyMock.expect(this.statesNamingAuthority.use(AFFECTED_ARC_NAME)).andReturn(AFFECTED_ARC_NAME);
        EasyMock.expect(this.statesNamingAuthority.generate()).andReturn(OTHER_FROM_NODE_NAME);
        EasyMock.expect(this.statesNamingAuthority.generate()).andReturn(OTHER_TO_NODE_NAME);
        EasyMock.expect(this.statesNamingAuthority.use(REFERRING_ARC_NAME)).andReturn(REFERRING_ARC_NAME);
        EasyMock.expect(this.statesNamingAuthority.replace(REFERRING_ARC_NAME, REFERRING_ARC_NAME)).andReturn(REFERRING_ARC_NAME);
        this.statesNamingAuthority.release(OTHER_FROM_NODE_NAME);
        this.statesNamingAuthority.release(REFERRING_ARC_NAME);
        this.statesNamingAuthority.release(OTHER_TO_NODE_NAME);
        this.statesNamingAuthority.release(FROM_NODE_NAME);
        this.statesNamingAuthority.release(AFFECTED_ARC_NAME);
        this.statesNamingAuthority.release(TO_NODE_NAME);
        
        PowerMock.mockStatic(NormalWords.class);
        EasyMock.expect(NormalWords.of(FROM_NODE_NAME)).andReturn(fromNodeNameStub);
        EasyMock.expect(NormalWords.of(TO_NODE_NAME)).andReturn(toNodeNameStub);
        EasyMock.expect(NormalWords.of(OTHER_FROM_NODE_NAME)).andReturn(otherFromNodeNameStub);
        EasyMock.expect(NormalWords.of(OTHER_TO_NODE_NAME)).andReturn(otherToNodeNameStub);
        EasyMock.expect(NormalWords.of(AFFECTED_ARC_NAME)).andReturn(affectedArcNameDummy);
        EasyMock.expect(NormalWords.of(REFERRING_ARC_NAME)).andReturn(referringArcNameDummy);
        EasyMock.expect(NormalWords.of(REFERRING_ARC_NAME)).andReturn(referringArcNameDummy);
        PowerMock.replay(NormalWords.class);
        
        EasyMock.expect(this.initialNodeFactory.produce(fromNodeNameStub, networkStub, FROM_NODE_X, FROM_NODE_Y)).andReturn(fromIsolatedNodeStub);
        EasyMock.expect(this.initialNodeFactory.produce(toNodeNameStub, networkMock, TO_NODE_X, TO_NODE_Y)).andReturn(toIsolatedNodeStub);
        EasyMock.expect(this.initialNodeFactory.produce(otherFromNodeNameStub, otherNetworkMock, OTHER_FROM_NODE_X, OTHER_FROM_NODE_Y)).andReturn(otherNetworkOtherFromIsolatedNodeStub);
        EasyMock.expect(this.initialNodeFactory.produce(otherToNodeNameStub, otherNetworkMock, OTHER_TO_NODE_X, OTHER_TO_NODE_Y)).andReturn(otherNetworkOtherToIsolatedNodeStub);
        
        EasyMock.expect(this.initialArcFactory.produce(networkMock, affectedArcNameDummy)).andReturn(affectedArcStub);
        EasyMock.expect(this.initialArcFactory.produce(otherNetworkMock, referringArcNameDummy)).andReturn(otherNetworkTemporaryArcStub);
        
        final Update addAffectedUpdateStub = EasyMock.createStrictMock(Update.class);
        EasyMock.expect(addAffectedUpdateStub.getInitialsAdded()).andStubReturn(ImmutableSet.of(referredFromNodeStub));
        EasyMock.expect(addAffectedUpdateStub.getInitialsRemoved()).andStubReturn(ImmutableSet.<EnterNode>of());
        EasyMock.expect(addAffectedUpdateStub.getSwitched()).andStubReturn(ImmutableMap.<Node, Node>of(fromIsolatedNodeStub, referredFromNodeStub, toIsolatedNodeStub, toExitNodeStub));
        EasyMock.replay(addAffectedUpdateStub);
        
        final Update addTemporaryUpdateStub = EasyMock.createStrictMock(Update.class);
        EasyMock.expect(addTemporaryUpdateStub.getInitialsAdded()).andStubReturn(ImmutableSet.of(otherNetworkOtherFromEnterNodeStub));
        EasyMock.expect(addTemporaryUpdateStub.getInitialsRemoved()).andStubReturn(ImmutableSet.<EnterNode>of());
        EasyMock.expect(addTemporaryUpdateStub.getSwitched()).andStubReturn(ImmutableMap.<Node, Node>of(otherNetworkOtherFromIsolatedNodeStub, otherNetworkOtherFromEnterNodeStub, otherNetworkOtherToIsolatedNodeStub, otherNetworkOtherToExitNodeStub));
        EasyMock.replay(addTemporaryUpdateStub);
        
        this.graph.add(fromIsolatedNodeStub);
        this.graph.add(toIsolatedNodeStub);
        EasyMock.expect(this.graph.addAndRealign(affectedArcStub, fromIsolatedNodeStub, toIsolatedNodeStub, realignmentProcessor, ImmutableSet.<EnterNode>of())).andReturn(addAffectedUpdateStub);
        this.graph.add(otherNetworkOtherFromIsolatedNodeStub);
        this.graph.add(otherNetworkOtherToIsolatedNodeStub);
        EasyMock.expect(this.graph.addAndRealign(otherNetworkTemporaryArcStub, otherNetworkOtherFromIsolatedNodeStub, otherNetworkOtherToIsolatedNodeStub, realignmentProcessor, ImmutableSet.<EnterNode>of())).andReturn(addTemporaryUpdateStub);
        EasyMock.expect(this.graph.containsEdge(otherNetworkTemporaryArcStub)).andReturn(true);
        EasyMock.expect(this.graph.containsVertex(referredFromNodeStub)).andReturn(true);
        this.graph.replaceEdge(otherNetworkTemporaryArcStub, otherNetworkReferringArcStub);
        this.graph.purge(ImmutableSet.of(otherNetworkOtherFromEnterNodeStub, otherNetworkOtherToExitNodeStub));
        this.graph.purge(ImmutableSet.of(referredFromNodeStub, toExitNodeStub));
        
        EasyMock.expect(this.arcModifier.change(otherNetworkTemporaryArcStub, referringArcNameDummy, priorityDummy, RecurentArc.class, emptyCodeDummy, referredFromNodeStub)).andStubReturn(otherNetworkReferringArcStub);
        
        EasyMock.expect(this.systemVisitorFactory.produce(EasyMock.notNull(DfsObserver.class))).andAnswer(new IAnswer<DfsVisitor>() {

            @Override
            public DfsVisitor answer() throws Throwable {
                final DfsObserver callback = (DfsObserver) EasyMock.getCurrentArguments()[0];
                
                callback.notifyDiscovery(otherNetworkOtherFromEnterNodeStub);
                callback.notifyExamination(otherNetworkReferringArcStub);
                callback.notifyDiscovery(otherNetworkOtherToExitNodeStub);
                
                return visitorDummy;
            }
            
        });
        EasyMock.expect(this.systemVisitorFactory.produce(EasyMock.notNull(DfsObserver.class))).andAnswer(new IAnswer<DfsVisitor>() {

            @Override
            public DfsVisitor answer() throws Throwable {
                final DfsObserver callback = (DfsObserver) EasyMock.getCurrentArguments()[0];
                
                callback.notifyDiscovery(referredFromNodeStub);
                callback.notifyExamination(affectedArcStub);
                callback.notifyDiscovery(toExitNodeStub);
                
                return visitorDummy;
            }
            
        });
        
        this.dispatcher.fire(EasyMock.notNull(NetworkAddedEvent.class));
        this.dispatcher.fire(EasyMock.notNull(NodeAddedEvent.class));
        this.dispatcher.fire(EasyMock.notNull(NodeAddedEvent.class));
        EasyMock.checkOrder(dispatcher, false);
        this.dispatcher.fire(EasyMock.notNull(NodeTypeChangedEvent.class));
        this.dispatcher.fire(EasyMock.notNull(NodeTypeChangedEvent.class));
        this.dispatcher.fire(EasyMock.notNull(ArcAddedEvent.class));
        this.dispatcher.fire(EasyMock.notNull(AvailableReferencesExtendedEvent.class));
        EasyMock.checkOrder(dispatcher, true);
        this.dispatcher.fire(EasyMock.notNull(NetworkAddedEvent.class));
        this.dispatcher.fire(EasyMock.notNull(NodeAddedEvent.class));
        this.dispatcher.fire(EasyMock.notNull(NodeAddedEvent.class));
        EasyMock.checkOrder(dispatcher, false);
        this.dispatcher.fire(EasyMock.notNull(NodeTypeChangedEvent.class));
        this.dispatcher.fire(EasyMock.notNull(NodeTypeChangedEvent.class));
        this.dispatcher.fire(EasyMock.notNull(ArcAddedEvent.class));
        this.dispatcher.fire(EasyMock.notNull(AvailableReferencesExtendedEvent.class));
        EasyMock.checkOrder(dispatcher, true);
        EasyMock.checkOrder(dispatcher, false);
        this.dispatcher.fire(EasyMock.notNull(ArcChangedEvent.class));
        this.dispatcher.fire(EasyMock.notNull(ArcRetypedEvent.class));
        EasyMock.checkOrder(dispatcher, true);
        this.dispatcher.fire(EasyMock.notNull(AvailableReferencesReducedEvent.class));
        EasyMock.checkOrder(dispatcher, false);
        this.dispatcher.fire(EasyMock.notNull(cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.events.NetworkRemovedEvent.class));
        this.dispatcher.fire(EasyMock.notNull(cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.events.NetworkRemovedEvent.class));
        EasyMock.checkOrder(dispatcher, true);
        this.dispatcher.fire(EasyMock.notNull(AvailableReferencesReducedEvent.class));
        EasyMock.checkOrder(dispatcher, false);
        this.dispatcher.fire(EasyMock.notNull(cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.events.NetworkRemovedEvent.class));
        this.dispatcher.fire(EasyMock.notNull(cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.events.NetworkRemovedEvent.class));
        EasyMock.checkOrder(dispatcher, true);
        
        replayTested();
        
        this.tested.addNetwork(networkMock, NETWORK_NAME);
        this.tested.addNode(networkMock, FROM_NODE_X, FROM_NODE_Y);
        this.tested.addNode(networkMock, TO_NODE_X, TO_NODE_Y);
        this.tested.addArc(networkMock, affectedArcNameDummy, fromIsolatedNodeStub, toIsolatedNodeStub);
        
        this.tested.addNetwork(otherNetworkMock, OTHER_NETWORK_NAME);
        this.tested.addNode(otherNetworkMock, OTHER_FROM_NODE_X, OTHER_FROM_NODE_Y);
        this.tested.addNode(otherNetworkMock, OTHER_TO_NODE_X, OTHER_TO_NODE_Y);
        this.tested.addArc(otherNetworkMock, referringArcNameDummy, otherNetworkOtherFromIsolatedNodeStub, otherNetworkOtherToIsolatedNodeStub);
        this.tested.changeArc(otherNetworkTemporaryArcStub, referringArcNameDummy, priorityDummy, RecurentArc.class, emptyCodeDummy, referredFromNodeStub);
                
        this.tested.removeNetwork(otherNetworkMock);
        this.tested.removeNetwork(networkMock);
        
        EasyMock.verify(addAffectedUpdateStub);
        EasyMock.verify(addTemporaryUpdateStub);
        PowerMock.verify(NormalWords.class);
        verifyTested();
    }

    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.DefaultSystem#getNodes(cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network)} and {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.DefaultSystem#addNode(Network, int, int)}.
     */
    @Test
    public void testGetNodesWhenAddedToNetworkContainsNewNode() {
        EasyMock.expect(this.statesNamingAuthority.generate()).andReturn(FROM_NODE_NAME);
        
        PowerMock.mockStatic(NormalWords.class);
        EasyMock.expect(NormalWords.of(FROM_NODE_NAME)).andReturn(fromNodeNameStub);
        PowerMock.replay(NormalWords.class);
        
        EasyMock.expect(this.initialNodeFactory.produce(fromNodeNameStub, networkStub, FROM_NODE_X, FROM_NODE_Y)).andReturn(fromIsolatedNodeStub);
        
        this.graph.add(fromIsolatedNodeStub);
        
        this.dispatcher.fire(EasyMock.notNull(NetworkAddedEvent.class));
        this.dispatcher.fire(EasyMock.notNull(NodeAddedEvent.class));
        
        replayTested();
        
        this.tested.addNetwork(networkStub, NETWORK_NAME);
        this.tested.addNode(networkStub, FROM_NODE_X, FROM_NODE_Y);
        assertEquals(ImmutableSet.of(fromIsolatedNodeStub), this.tested.getNodes(networkStub));
        
        PowerMock.verify(NormalWords.class);
        verifyTested();
    }
    
    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.DefaultSystem#getNodes(cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network)} and {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.DefaultSystem#removeNode(Node)}.
     */
    @Test
    public void testGetNodesWhenNodeRemovedExpectNotContained() {
        EasyMock.expect(this.statesNamingAuthority.generate()).andReturn(FROM_NODE_NAME);
        this.statesNamingAuthority.release(FROM_NODE_NAME);
        
        PowerMock.mockStatic(NormalWords.class);
        EasyMock.expect(NormalWords.of(FROM_NODE_NAME)).andReturn(fromNodeNameStub);
        PowerMock.replay(NormalWords.class);
        
        EasyMock.expect(this.initialNodeFactory.produce(fromNodeNameStub, networkStub, FROM_NODE_X, FROM_NODE_Y)).andReturn(fromIsolatedNodeStub);
        
        final Update emptyUpdateStub = EasyMock.createMock(Update.class);
        EasyMock.expect(emptyUpdateStub.getInitialsRemoved()).andStubReturn(ImmutableSet.<EnterNode>of());
        EasyMock.expect(emptyUpdateStub.getInitialsAdded()).andStubReturn(ImmutableSet.<EnterNode>of());
        EasyMock.expect(emptyUpdateStub.getSwitched()).andStubReturn(ImmutableMap.<Node, Node>of());
        EasyMock.expect(emptyUpdateStub.getEdgesRemoved()).andStubReturn(ImmutableSet.<Arc>of());
        EasyMock.expect(emptyUpdateStub.getReferencesRemoved()).andStubReturn(ImmutableSet.<RecurentArc>of());
        EasyMock.replay(emptyUpdateStub);
        
        this.graph.add(fromIsolatedNodeStub);
        EasyMock.expect(this.graph.containsVertex(fromIsolatedNodeStub)).andReturn(true);
        EasyMock.expect(this.graph.removeAndRealign(fromIsolatedNodeStub, realignmentProcessor, ImmutableMap.<EnterNode, Set<RecurentArc>>of(), ImmutableSet.<EnterNode>of())).andReturn(emptyUpdateStub);
        
        this.dispatcher.fire(EasyMock.notNull(NetworkAddedEvent.class));
        this.dispatcher.fire(EasyMock.notNull(NodeAddedEvent.class));
        this.dispatcher.fire(EasyMock.notNull(NodeRemovedEvent.class));
        
        replayTested();
        
        this.tested.addNetwork(networkStub, NETWORK_NAME);
        this.tested.addNode(networkStub, FROM_NODE_X, FROM_NODE_Y);
        this.tested.removeNode(fromIsolatedNodeStub);
        assertTrue(this.tested.getNodes(networkStub).isEmpty());
        
        EasyMock.verify(emptyUpdateStub);
        PowerMock.verify(NormalWords.class);
        verifyTested();
    }

    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.DefaultSystem#removeNode(cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemoveNodeWhenReferredByArc() {
        EasyMock.expect(this.statesNamingAuthority.generate()).andReturn(OTHER_FROM_NODE_NAME);
        EasyMock.expect(this.statesNamingAuthority.generate()).andReturn(OTHER_TO_NODE_NAME);
        EasyMock.expect(this.statesNamingAuthority.use(REFERRING_ARC_NAME)).andReturn(REFERRING_ARC_NAME);
        EasyMock.expect(this.statesNamingAuthority.replace(REFERRING_ARC_NAME, REFERRING_ARC_NAME)).andReturn(REFERRING_ARC_NAME);
        
        PowerMock.mockStatic(NormalWords.class);
        EasyMock.expect(NormalWords.of(OTHER_FROM_NODE_NAME)).andReturn(fromNodeNameStub);
        EasyMock.expect(NormalWords.of(OTHER_TO_NODE_NAME)).andReturn(toNodeNameStub);
        EasyMock.expect(NormalWords.of(REFERRING_ARC_NAME)).andReturn(referringArcNameDummy);
        EasyMock.expect(NormalWords.of(REFERRING_ARC_NAME)).andReturn(referringArcNameDummy);
        PowerMock.replay(NormalWords.class);
        
        EasyMock.expect(this.initialNodeFactory.produce(fromNodeNameStub, networkStub, OTHER_FROM_NODE_X, OTHER_FROM_NODE_Y)).andReturn(fromIsolatedNodeStub);
        EasyMock.expect(this.initialNodeFactory.produce(toNodeNameStub, networkStub, OTHER_TO_NODE_X, OTHER_TO_NODE_Y)).andReturn(toIsolatedNodeStub);
        
        EasyMock.expect(this.initialArcFactory.produce(networkStub, referringArcNameDummy)).andReturn(thisTemporaryArcStub);
        
        final Update addTemporaryUpdateStub = EasyMock.createStrictMock(Update.class);
        EasyMock.expect(addTemporaryUpdateStub.getInitialsAdded()).andStubReturn(ImmutableSet.<EnterNode>of(referredFromNodeStub));
        EasyMock.expect(addTemporaryUpdateStub.getInitialsRemoved()).andStubReturn(ImmutableSet.<EnterNode>of());
        EasyMock.expect(addTemporaryUpdateStub.getSwitched()).andStubReturn(ImmutableMap.<Node, Node>of(fromIsolatedNodeStub, referredFromNodeStub, toIsolatedNodeStub, toExitNodeStub));
        EasyMock.replay(addTemporaryUpdateStub);
        
        this.graph.add(fromIsolatedNodeStub);
        this.graph.add(toIsolatedNodeStub);
        EasyMock.expect(this.graph.addAndRealign(thisTemporaryArcStub, fromIsolatedNodeStub, toIsolatedNodeStub, realignmentProcessor, ImmutableSet.<EnterNode>of())).andReturn(addTemporaryUpdateStub);
        EasyMock.expect(this.graph.containsEdge(thisTemporaryArcStub)).andReturn(true);
        EasyMock.expect(this.graph.containsVertex(referredFromNodeStub)).andReturn(true);
        this.graph.replaceEdge(thisTemporaryArcStub, thisNetworkReferringArcStub);
        EasyMock.expect(this.graph.containsVertex(referredFromNodeStub)).andReturn(true);
        
        EasyMock.expect(this.arcModifier.change(thisTemporaryArcStub, referringArcNameDummy, priorityDummy, RecurentArc.class, emptyCodeDummy, referredFromNodeStub)).andStubReturn(thisNetworkReferringArcStub);
        
        this.dispatcher.fire(EasyMock.notNull(NetworkAddedEvent.class));
        this.dispatcher.fire(EasyMock.notNull(NodeAddedEvent.class));
        this.dispatcher.fire(EasyMock.notNull(NodeAddedEvent.class));
        EasyMock.checkOrder(dispatcher, false);
        this.dispatcher.fire(EasyMock.notNull(NodeTypeChangedEvent.class));
        this.dispatcher.fire(EasyMock.notNull(NodeTypeChangedEvent.class));
        this.dispatcher.fire(EasyMock.notNull(ArcAddedEvent.class));
        this.dispatcher.fire(EasyMock.notNull(AvailableReferencesExtendedEvent.class));
        EasyMock.checkOrder(dispatcher, true);
        EasyMock.checkOrder(dispatcher, false);
        this.dispatcher.fire(EasyMock.notNull(ArcChangedEvent.class));
        this.dispatcher.fire(EasyMock.notNull(ArcRetypedEvent.class));
        EasyMock.checkOrder(dispatcher, true);
        
        replayTested();
        
        this.tested.addNetwork(networkStub, OTHER_NETWORK_NAME);
        this.tested.addNode(networkStub, OTHER_FROM_NODE_X, OTHER_FROM_NODE_Y);
        this.tested.addNode(networkStub, OTHER_TO_NODE_X, OTHER_TO_NODE_Y);
        this.tested.addArc(networkStub, referringArcNameDummy, fromIsolatedNodeStub, toIsolatedNodeStub);
        this.tested.changeArc(thisTemporaryArcStub, referringArcNameDummy, priorityDummy, RecurentArc.class, emptyCodeDummy, referredFromNodeStub);
                
        try {
            this.tested.removeNode(referredFromNodeStub);
        } finally {
            EasyMock.verify(addTemporaryUpdateStub);
            PowerMock.verify(NormalWords.class);
            verifyTested();
        }
    }

    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.DefaultSystem#removeArc(cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc)} and {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.DefaultSystem#removeNode(Node)}.
     */
    @Test
    public void testRemoveArcWhenTheArcIsReferringExpectSubsequentFormerlyRefferedNodeRemovalNotBlocked() {
        EasyMock.expect(this.statesNamingAuthority.generate()).andReturn(FROM_NODE_NAME);
        EasyMock.expect(this.statesNamingAuthority.generate()).andReturn(TO_NODE_NAME);
        EasyMock.expect(this.statesNamingAuthority.use(AFFECTED_ARC_NAME)).andReturn(AFFECTED_ARC_NAME);
        EasyMock.expect(this.statesNamingAuthority.generate()).andReturn(OTHER_FROM_NODE_NAME);
        EasyMock.expect(this.statesNamingAuthority.generate()).andReturn(OTHER_TO_NODE_NAME);
        EasyMock.expect(this.statesNamingAuthority.use(REFERRING_ARC_NAME)).andReturn(REFERRING_ARC_NAME);
        EasyMock.expect(this.statesNamingAuthority.replace(REFERRING_ARC_NAME, REFERRING_ARC_NAME)).andReturn(REFERRING_ARC_NAME);
        this.statesNamingAuthority.release(REFERRING_ARC_NAME);
        this.statesNamingAuthority.release(FROM_NODE_NAME);
        this.statesNamingAuthority.release(AFFECTED_ARC_NAME);
        
        PowerMock.mockStatic(NormalWords.class);
        EasyMock.expect(NormalWords.of(FROM_NODE_NAME)).andReturn(fromNodeNameStub);
        EasyMock.expect(NormalWords.of(TO_NODE_NAME)).andReturn(toNodeNameStub);
        EasyMock.expect(NormalWords.of(OTHER_FROM_NODE_NAME)).andReturn(otherFromNodeNameStub);
        EasyMock.expect(NormalWords.of(OTHER_TO_NODE_NAME)).andReturn(otherToNodeNameStub);
        EasyMock.expect(NormalWords.of(AFFECTED_ARC_NAME)).andReturn(affectedArcNameDummy);
        EasyMock.expect(NormalWords.of(REFERRING_ARC_NAME)).andReturn(referringArcNameDummy);
        EasyMock.expect(NormalWords.of(REFERRING_ARC_NAME)).andReturn(referringArcNameDummy);
        PowerMock.replay(NormalWords.class);
        
        EasyMock.expect(this.initialNodeFactory.produce(fromNodeNameStub, networkStub, FROM_NODE_X, FROM_NODE_Y)).andReturn(fromIsolatedNodeStub);
        EasyMock.expect(this.initialNodeFactory.produce(toNodeNameStub, networkStub, TO_NODE_X, TO_NODE_Y)).andReturn(toIsolatedNodeStub);
        EasyMock.expect(this.initialNodeFactory.produce(otherFromNodeNameStub, networkStub, OTHER_FROM_NODE_X, OTHER_FROM_NODE_Y)).andReturn(otherFromIsolatedNodeStub);
        EasyMock.expect(this.initialNodeFactory.produce(otherToNodeNameStub, networkStub, OTHER_TO_NODE_X, OTHER_TO_NODE_Y)).andReturn(otherToIsolatedNodeStub);
        
        EasyMock.expect(this.initialArcFactory.produce(networkStub, affectedArcNameDummy)).andReturn(affectedArcStub);
        EasyMock.expect(this.initialArcFactory.produce(networkStub, referringArcNameDummy)).andReturn(thisTemporaryArcStub);
        
        final Update addAffectedUpdateStub = EasyMock.createStrictMock(Update.class);
        EasyMock.expect(addAffectedUpdateStub.getInitialsAdded()).andStubReturn(ImmutableSet.of(referredFromNodeStub));
        EasyMock.expect(addAffectedUpdateStub.getInitialsRemoved()).andStubReturn(ImmutableSet.<EnterNode>of());
        EasyMock.expect(addAffectedUpdateStub.getSwitched()).andStubReturn(ImmutableMap.<Node, Node>of(fromIsolatedNodeStub, referredFromNodeStub, toIsolatedNodeStub, toExitNodeStub));
        EasyMock.replay(addAffectedUpdateStub);
        
        final Update addTemporaryUpdateStub = EasyMock.createStrictMock(Update.class);
        EasyMock.expect(addTemporaryUpdateStub.getInitialsAdded()).andStubReturn(ImmutableSet.of(otherFromEnterNodeStub));
        EasyMock.expect(addTemporaryUpdateStub.getInitialsRemoved()).andStubReturn(ImmutableSet.<EnterNode>of());
        EasyMock.expect(addTemporaryUpdateStub.getSwitched()).andStubReturn(ImmutableMap.<Node, Node>of(otherFromIsolatedNodeStub, otherFromEnterNodeStub, otherToIsolatedNodeStub, otherToExitNodeStub));
        EasyMock.replay(addTemporaryUpdateStub);
        
        final Update referringRemovalUpdateStub = EasyMock.createStrictMock(Update.class);
        EasyMock.expect(referringRemovalUpdateStub.getInitialsAdded()).andStubReturn(ImmutableSet.<EnterNode>of());
        EasyMock.expect(referringRemovalUpdateStub.getInitialsRemoved()).andStubReturn(ImmutableSet.<EnterNode>of(otherFromEnterNodeStub));
        EasyMock.expect(referringRemovalUpdateStub.getSwitched()).andStubReturn(ImmutableMap.<Node, Node>of(otherFromEnterNodeStub, otherFromIsolatedNodeStub, otherToIsolatedNodeStub, otherToIsolatedNodeStub));
        EasyMock.expect(referringRemovalUpdateStub.getReferencesRemoved()).andStubReturn(ImmutableSet.of(thisNetworkReferringArcStub));
        EasyMock.replay(referringRemovalUpdateStub);
        
        final Update referredRemovalUpdateStub = EasyMock.createStrictMock(Update.class);
        EasyMock.expect(referredRemovalUpdateStub.getInitialsAdded()).andStubReturn(ImmutableSet.<EnterNode>of());
        EasyMock.expect(referredRemovalUpdateStub.getInitialsRemoved()).andStubReturn(ImmutableSet.<EnterNode>of(referredFromNodeStub));
        EasyMock.expect(referredRemovalUpdateStub.getSwitched()).andStubReturn(ImmutableMap.<Node, Node>of(toExitNodeStub, toIsolatedNodeStub));
        EasyMock.expect(referredRemovalUpdateStub.getReferencesRemoved()).andStubReturn(ImmutableSet.<RecurentArc>of());
        EasyMock.expect(referredRemovalUpdateStub.getEdgesRemoved()).andStubReturn(ImmutableSet.<Arc>of(affectedArcStub));
        EasyMock.replay(referredRemovalUpdateStub);
        
        this.graph.add(fromIsolatedNodeStub);
        this.graph.add(toIsolatedNodeStub);
        EasyMock.expect(this.graph.addAndRealign(affectedArcStub, fromIsolatedNodeStub, toIsolatedNodeStub, realignmentProcessor, ImmutableSet.<EnterNode>of())).andReturn(addAffectedUpdateStub);
        this.graph.add(otherFromIsolatedNodeStub);
        this.graph.add(otherToIsolatedNodeStub);
        EasyMock.expect(this.graph.addAndRealign(thisTemporaryArcStub, otherFromIsolatedNodeStub, otherToIsolatedNodeStub, realignmentProcessor, ImmutableSet.<EnterNode>of(referredFromNodeStub))).andReturn(addTemporaryUpdateStub);
        EasyMock.expect(this.graph.containsEdge(thisTemporaryArcStub)).andReturn(true);
        EasyMock.expect(this.graph.containsVertex(referredFromNodeStub)).andReturn(true);
        this.graph.replaceEdge(thisTemporaryArcStub, thisNetworkReferringArcStub);
        EasyMock.expect(this.graph.containsEdge(thisNetworkReferringArcStub)).andReturn(true);
        EasyMock.expect(this.graph.removeAndRealign(thisNetworkReferringArcStub, realignmentProcessor, ImmutableMap.of(referredFromNodeStub, ImmutableSet.of(thisNetworkReferringArcStub)), ImmutableSet.of(referredFromNodeStub, otherFromEnterNodeStub))).andReturn(referringRemovalUpdateStub);
        EasyMock.expect(this.graph.containsVertex(referredFromNodeStub)).andReturn(true);
        EasyMock.expect(this.graph.removeAndRealign(referredFromNodeStub, realignmentProcessor, ImmutableMap.<EnterNode, Set<RecurentArc>>of(), ImmutableSet.of(referredFromNodeStub))).andReturn(referredRemovalUpdateStub);
        
        EasyMock.expect(this.arcModifier.change(thisTemporaryArcStub, referringArcNameDummy, priorityDummy, RecurentArc.class, emptyCodeDummy, referredFromNodeStub)).andStubReturn(thisNetworkReferringArcStub);
        
        this.dispatcher.fire(EasyMock.notNull(NetworkAddedEvent.class));
        
        this.dispatcher.fire(EasyMock.notNull(NodeAddedEvent.class));
        
        this.dispatcher.fire(EasyMock.notNull(NodeAddedEvent.class));
        
        EasyMock.checkOrder(dispatcher, false);
        this.dispatcher.fire(EasyMock.notNull(NodeTypeChangedEvent.class));
        this.dispatcher.fire(EasyMock.notNull(NodeTypeChangedEvent.class));
        this.dispatcher.fire(EasyMock.notNull(ArcAddedEvent.class));
        this.dispatcher.fire(EasyMock.notNull(AvailableReferencesExtendedEvent.class));
        EasyMock.checkOrder(dispatcher, true);
        
        this.dispatcher.fire(EasyMock.notNull(NodeAddedEvent.class));
        
        this.dispatcher.fire(EasyMock.notNull(NodeAddedEvent.class));
        
        EasyMock.checkOrder(dispatcher, false);
        this.dispatcher.fire(EasyMock.notNull(NodeTypeChangedEvent.class));
        this.dispatcher.fire(EasyMock.notNull(NodeTypeChangedEvent.class));
        this.dispatcher.fire(EasyMock.notNull(ArcAddedEvent.class));
        this.dispatcher.fire(EasyMock.notNull(AvailableReferencesExtendedEvent.class));
        EasyMock.checkOrder(dispatcher, true);
        
        EasyMock.checkOrder(dispatcher, false);
        this.dispatcher.fire(EasyMock.notNull(ArcChangedEvent.class));
        this.dispatcher.fire(EasyMock.notNull(ArcRetypedEvent.class));
        EasyMock.checkOrder(dispatcher, true);
        
        EasyMock.checkOrder(dispatcher, false);
        this.dispatcher.fire(EasyMock.notNull(cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.events.ArcRemovedEvent.class));
        this.dispatcher.fire(EasyMock.notNull(cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.events.ArcRemovedEvent.class));
        this.dispatcher.fire(EasyMock.notNull(cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.events.ArcRemovedEvent.class));
        this.dispatcher.fire(EasyMock.notNull(AvailableReferencesReducedEvent.class));
        this.dispatcher.fire(EasyMock.notNull(NodeTypeChangedEvent.class));
        this.dispatcher.fire(EasyMock.notNull(NodeTypeChangedEvent.class));
        EasyMock.checkOrder(dispatcher, true);
        
        EasyMock.checkOrder(dispatcher, false);
        this.dispatcher.fire(EasyMock.notNull(cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.events.AvailableReferencesReducedEvent.class));
        this.dispatcher.fire(EasyMock.notNull(NodeRemovedEvent.class));
        this.dispatcher.fire(EasyMock.notNull(NodeTypeChangedEvent.class));
        this.dispatcher.fire(EasyMock.notNull(cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.events.ArcRemovedEvent.class));
        this.dispatcher.fire(EasyMock.notNull(cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.events.ArcRemovedEvent.class));
        this.dispatcher.fire(EasyMock.notNull(cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.events.ArcRemovedEvent.class));
        EasyMock.checkOrder(dispatcher, true);
        
        replayTested();
        
        this.tested.addNetwork(networkStub, NETWORK_NAME);
        this.tested.addNode(networkStub, FROM_NODE_X, FROM_NODE_Y);
        this.tested.addNode(networkStub, TO_NODE_X, TO_NODE_Y);
        this.tested.addArc(networkStub, affectedArcNameDummy, fromIsolatedNodeStub, toIsolatedNodeStub);
        
        this.tested.addNode(networkStub, OTHER_FROM_NODE_X, OTHER_FROM_NODE_Y);
        this.tested.addNode(networkStub, OTHER_TO_NODE_X, OTHER_TO_NODE_Y);
        this.tested.addArc(networkStub, referringArcNameDummy, otherFromIsolatedNodeStub, otherToIsolatedNodeStub);
        this.tested.changeArc(thisTemporaryArcStub, referringArcNameDummy, priorityDummy, RecurentArc.class, emptyCodeDummy, referredFromNodeStub);
                
        this.tested.removeArc(thisNetworkReferringArcStub);
        this.tested.removeNode(referredFromNodeStub);
        
        EasyMock.verify(addAffectedUpdateStub);
        EasyMock.verify(addTemporaryUpdateStub);
        PowerMock.verify(NormalWords.class);
        verifyTested();
    }

    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.DefaultSystem#changeNode(cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord, java.lang.Class)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChangeNodeTypeWhenUnsupportedTypeUsed() {
        final FunctionalNode unsupportedNodeTypePrototype = EasyMock.createStrictMock(FunctionalNode.class);
        EasyMock.replay(unsupportedNodeTypePrototype);
        
        EasyMock.expect(this.graph.containsVertex(fromIsolatedNodeStub)).andReturn(true);
        
        replayTested();
        
        try {
            this.tested.changeNode(fromIsolatedNodeStub, unsupportedNodeTypePrototype.getClass());
        } finally {
            verifyTested();
        }
    }
    
    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.DefaultSystem#changeNode(cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord, java.lang.Class)}.
     */
    @Test
    public void testChangeNodeTypeExpectNodeReplaced() {
        final InputNode oldVersionStub = EasyMock.createStrictMock(InputNode.class);
        EasyMock.expect(oldVersionStub.getNetwork()).andStubReturn(networkStub);
        EasyMock.replay(oldVersionStub);
        
        final ProcessingNode newVersionDummy = EasyMock.createStrictMock(ProcessingNode.class);
        EasyMock.replay(newVersionDummy);
        
        EasyMock.expect(this.graph.containsVertex(oldVersionStub)).andStubReturn(true);
        this.graph.replaceVertex(oldVersionStub, newVersionDummy);
        
        EasyMock.expect(this.nodeModifier.change(oldVersionStub, ProcessingNode.class)).andStubReturn(newVersionDummy);
        
        this.dispatcher.fire(EasyMock.notNull(NodeTypeChangedEvent.class));
        
        replayTested();
        
        this.tested.changeNode(oldVersionStub, ProcessingNode.class);
        
        EasyMock.verify(oldVersionStub);
        verifyTested();
    }

    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.DefaultSystem#changeNode(cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord, cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord)}.
     */
    @Test
    public void testChangeNodeNameExpectReplaced() {
        EasyMock.expect(this.graph.containsVertex(fromIsolatedNodeStub)).andStubReturn(true);
        this.graph.replaceVertex(fromIsolatedNodeStub, otherFromIsolatedNodeStub);
        
        PowerMock.mockStatic(NormalWords.class);
        EasyMock.expect(NormalWords.of(OTHER_FROM_NODE_NAME)).andStubReturn(otherFromNodeNameStub);
        PowerMock.replay(NormalWords.class);
        
        EasyMock.expect(this.nodeModifier.change(fromIsolatedNodeStub, otherFromNodeNameStub)).andStubReturn(otherFromIsolatedNodeStub);
        
        EasyMock.expect(this.statesNamingAuthority.replace(FROM_NODE_NAME, OTHER_FROM_NODE_NAME)).andReturn(OTHER_FROM_NODE_NAME);
        
        this.dispatcher.fire(EasyMock.notNull(NodeRenamedEvent.class));
        
        replayTested();
        
        this.tested.changeNode(fromIsolatedNodeStub, otherFromNodeNameStub);
        
        verifyTested();        
    }
    
    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.DefaultSystem#changeNode(cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord, int, int)}.
     */
    @Test
    public void testChangeNodePosition() {
        EasyMock.expect(fromIsolatedNodeStub.getX()).andStubReturn(FROM_NODE_X);
        EasyMock.expect(fromIsolatedNodeStub.getY()).andStubReturn(FROM_NODE_Y);
        
        EasyMock.expect(this.graph.containsVertex(fromIsolatedNodeStub)).andStubReturn(true);
        this.graph.replaceVertex(fromIsolatedNodeStub, otherFromIsolatedNodeStub);
        
        EasyMock.expect(this.nodeModifier.change(fromIsolatedNodeStub, OTHER_FROM_NODE_X, OTHER_FROM_NODE_Y)).andStubReturn(otherFromIsolatedNodeStub);
        
        this.dispatcher.fire(EasyMock.notNull(NodeMovedEvent.class));
        
        replayTested();
        
        this.tested.changeNode(fromIsolatedNodeStub, OTHER_FROM_NODE_X, OTHER_FROM_NODE_Y);
        
        verifyTested();
    }
}
