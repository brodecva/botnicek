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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.types.SystemName;
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
@PrepareForTest({ NetworkAddedEvent.class, Priority.class, NormalWords.class,
        TransitionArc.class, RecurentArc.class, SystemName.class })
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

    private SystemName otherNetworkNameStub = Intended.nullReference();
    private SystemName networkNameStub = Intended.nullReference();
    private SystemName someNetworkNameStub = Intended.nullReference();
    private SystemName systemNameStub = Intended.nullReference();
    private SystemName reservedNetworkNameStub = Intended.nullReference();

    private System tested = Intended.nullReference();
    private SystemGraph graph = Intended.nullReference();
    private NamingAuthority statesNamingAuthority = Intended.nullReference();
    private NamingAuthority variablesNamingAuthority = Intended.nullReference();
    private NodeModifier nodeModifier = Intended.nullReference();
    private ArcModifier arcModifier = Intended.nullReference();
    private RealignmentProcessor realignmentProcessor = Intended
            .nullReference();
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
    private TransitionArc otherNetworkTemporaryArcStub = Intended
            .nullReference();
    private TransitionArc thisTemporaryArcStub = Intended.nullReference();
    private RecurentArc otherNetworkReferringArcStub = Intended.nullReference();
    private RecurentArc thisNetworkReferringArcStub = Intended.nullReference();
    private IsolatedNode fromIsolatedNodeStub = Intended.nullReference();
    private IsolatedNode toIsolatedNodeStub = Intended.nullReference();
    private IsolatedNode otherNetworkOtherFromIsolatedNodeStub = Intended
            .nullReference();
    private IsolatedNode otherFromIsolatedNodeStub = Intended.nullReference();
    private IsolatedNode otherToIsolatedNodeStub = Intended.nullReference();
    private IsolatedNode otherNetworkOtherToIsolatedNodeStub = Intended
            .nullReference();
    private ExitNode toExitNodeStub = Intended.nullReference();
    private EnterNode otherFromEnterNodeStub = Intended.nullReference();
    private ExitNode otherToExitNodeStub = Intended.nullReference();
    private EnterNode otherNetworkOtherFromEnterNodeStub = Intended
            .nullReference();
    private ExitNode otherNetworkOtherToExitNodeStub = Intended.nullReference();

    private void replayTested() {
        EasyMock.replay(this.graph, this.statesNamingAuthority,
                this.variablesNamingAuthority, this.nodeModifier,
                this.arcModifier, this.realignmentProcessor,
                this.systemVisitorFactory, this.initialNodeFactory,
                this.initialArcFactory, this.dispatcher, this.networkStub,
                this.otherNetworkStub, this.affectedArcNameDummy,
                this.fromNodeNameStub, this.toNodeNameStub,
                this.referringArcNameDummy, this.otherFromNodeNameStub,
                this.otherToNodeNameStub, this.referredFromNodeStub,
                this.emptyCodeDummy, this.fromIsolatedNodeStub,
                this.toIsolatedNodeStub, this.otherFromIsolatedNodeStub,
                this.otherToIsolatedNodeStub,
                this.otherNetworkOtherFromIsolatedNodeStub,
                this.otherNetworkOtherToIsolatedNodeStub, this.toExitNodeStub,
                this.otherFromEnterNodeStub, this.otherToExitNodeStub,
                this.otherNetworkOtherFromEnterNodeStub,
                this.otherNetworkOtherToExitNodeStub);
        PowerMock.replay(this.affectedArcStub, this.priorityDummy,
                this.thisTemporaryArcStub, this.otherNetworkTemporaryArcStub,
                this.otherNetworkReferringArcStub,
                this.thisNetworkReferringArcStub, this.otherNetworkNameStub,
                this.networkNameStub, this.someNetworkNameStub,
                this.systemNameStub, this.reservedNetworkNameStub);
    }

    /**
     * Sestaví testovací objekty.
     * 
     * @throws java.lang.Exception
     *             pokud dojde k vyhození výjimky
     */
    @Before
    public void setUp() throws Exception {
        this.otherNetworkNameStub =
                PowerMock.createStrictMock(SystemName.class);
        EasyMock.expect(this.otherNetworkNameStub.getText()).andStubReturn(
                "otherNetwork");
        this.networkNameStub = PowerMock.createStrictMock(SystemName.class);
        EasyMock.expect(this.networkNameStub.getText())
                .andStubReturn("network");
        this.someNetworkNameStub = PowerMock.createStrictMock(SystemName.class);
        EasyMock.expect(this.someNetworkNameStub.getText()).andStubReturn(
                "someNetwork");
        this.systemNameStub = PowerMock.createStrictMock(SystemName.class);
        EasyMock.expect(this.systemNameStub.getText()).andStubReturn("system");
        this.reservedNetworkNameStub =
                PowerMock.createStrictMock(SystemName.class);
        EasyMock.expect(this.reservedNetworkNameStub.getText()).andStubReturn(
                "reservedNetwork");

        this.graph = EasyMock.createStrictMock(SystemGraph.class);

        this.statesNamingAuthority =
                EasyMock.createStrictMock(NamingAuthority.class);

        this.variablesNamingAuthority =
                EasyMock.createStrictMock(NamingAuthority.class);

        this.nodeModifier = EasyMock.createStrictMock(NodeModifier.class);

        this.arcModifier = EasyMock.createStrictMock(ArcModifier.class);

        this.realignmentProcessor =
                EasyMock.createStrictMock(RealignmentProcessor.class);

        this.systemVisitorFactory =
                EasyMock.createStrictMock(DfsVisitorFactory.class);

        this.initialNodeFactory =
                EasyMock.createStrictMock(InitialNodeFactory.class);

        this.initialArcFactory =
                EasyMock.createStrictMock(InitialArcFactory.class);

        this.dispatcher = EasyMock.createStrictMock(Dispatcher.class);

        this.tested =
                DefaultSystem.create(this.systemNameStub, this.graph,
                        this.statesNamingAuthority,
                        this.variablesNamingAuthority, this.nodeModifier,
                        this.arcModifier, this.realignmentProcessor,
                        ImmutableSet.of(this.reservedNetworkNameStub),
                        this.systemVisitorFactory, this.initialNodeFactory,
                        this.initialArcFactory, this.dispatcher);

        this.networkStub = EasyMock.createMock("networkStub", Network.class);
        EasyMock.expect(this.networkStub.getName()).andStubReturn(
                this.networkNameStub);
        EasyMock.expect(this.networkStub.getSystem())
                .andStubReturn(this.tested);

        this.otherNetworkStub =
                EasyMock.createMock("otherNetworkStub", Network.class);
        EasyMock.expect(this.otherNetworkStub.getName()).andStubReturn(
                this.otherNetworkNameStub);
        EasyMock.expect(this.otherNetworkStub.getSystem()).andStubReturn(
                this.tested);

        this.affectedArcNameDummy = EasyMock.createStrictMock(NormalWord.class);
        EasyMock.expect(this.affectedArcNameDummy.getText()).andStubReturn(
                AFFECTED_ARC_NAME);

        this.affectedArcStub = PowerMock.createStrictMock(TransitionArc.class);
        EasyMock.expect(this.affectedArcStub.getName()).andStubReturn(
                this.affectedArcNameDummy);
        EasyMock.expect(this.affectedArcStub.getNetwork()).andStubReturn(
                this.networkStub);

        this.fromNodeNameStub = EasyMock.createStrictMock(NormalWord.class);
        EasyMock.expect(this.fromNodeNameStub.getText()).andStubReturn(
                FROM_NODE_NAME);

        this.toNodeNameStub = EasyMock.createStrictMock(NormalWord.class);
        EasyMock.expect(this.toNodeNameStub.getText()).andStubReturn(
                TO_NODE_NAME);

        this.referringArcNameDummy =
                EasyMock.createStrictMock("referringArcNameDummy",
                        NormalWord.class);
        EasyMock.expect(this.referringArcNameDummy.getText()).andStubReturn(
                REFERRING_ARC_NAME);

        this.otherFromNodeNameStub =
                EasyMock.createStrictMock(NormalWord.class);
        EasyMock.expect(this.otherFromNodeNameStub.getText()).andStubReturn(
                OTHER_FROM_NODE_NAME);

        this.otherToNodeNameStub = EasyMock.createStrictMock(NormalWord.class);
        EasyMock.expect(this.otherToNodeNameStub.getText()).andStubReturn(
                OTHER_TO_NODE_NAME);

        this.priorityDummy = PowerMock.createStrictMock(Priority.class);

        this.referredFromNodeStub =
                EasyMock.createMock("referredFromNodeStub", EnterNode.class);
        EasyMock.expect(this.referredFromNodeStub.getName()).andStubReturn(
                this.fromNodeNameStub);
        EasyMock.expect(this.referredFromNodeStub.getNetwork()).andStubReturn(
                this.networkStub);

        this.emptyCodeDummy = EasyMock.createStrictMock(Code.class);

        this.thisTemporaryArcStub = PowerMock.createMock(TransitionArc.class);
        EasyMock.expect(this.thisTemporaryArcStub.getName()).andStubReturn(
                this.referringArcNameDummy);
        EasyMock.expect(this.thisTemporaryArcStub.getNetwork()).andStubReturn(
                this.networkStub);
        EasyMock.expect(this.thisTemporaryArcStub.getPriority()).andStubReturn(
                this.priorityDummy);

        this.otherNetworkTemporaryArcStub =
                PowerMock.createMock(TransitionArc.class);
        EasyMock.expect(this.otherNetworkTemporaryArcStub.getName())
                .andStubReturn(this.referringArcNameDummy);
        EasyMock.expect(this.otherNetworkTemporaryArcStub.getNetwork())
                .andStubReturn(this.otherNetworkStub);
        EasyMock.expect(this.otherNetworkTemporaryArcStub.getPriority())
                .andStubReturn(this.priorityDummy);

        this.otherNetworkReferringArcStub =
                PowerMock.createMock(RecurentArc.class);
        EasyMock.expect(this.otherNetworkReferringArcStub.getName())
                .andStubReturn(this.referringArcNameDummy);
        EasyMock.expect(this.otherNetworkReferringArcStub.getNetwork())
                .andStubReturn(this.otherNetworkStub);
        EasyMock.expect(this.otherNetworkReferringArcStub.getPriority())
                .andStubReturn(this.priorityDummy);
        EasyMock.expect(this.otherNetworkReferringArcStub.getNetwork())
                .andStubReturn(this.otherNetworkStub);
        EasyMock.expect(this.otherNetworkReferringArcStub.getTarget())
                .andStubReturn(this.referredFromNodeStub);

        this.thisNetworkReferringArcStub =
                PowerMock.createMock(RecurentArc.class);
        EasyMock.expect(this.thisNetworkReferringArcStub.getName())
                .andStubReturn(this.referringArcNameDummy);
        EasyMock.expect(this.thisNetworkReferringArcStub.getNetwork())
                .andStubReturn(this.networkStub);
        EasyMock.expect(this.thisNetworkReferringArcStub.getPriority())
                .andStubReturn(this.priorityDummy);
        EasyMock.expect(this.thisNetworkReferringArcStub.getNetwork())
                .andStubReturn(this.networkStub);
        EasyMock.expect(this.thisNetworkReferringArcStub.getTarget())
                .andStubReturn(this.referredFromNodeStub);

        this.fromIsolatedNodeStub =
                EasyMock.createStrictMock(IsolatedNode.class);
        EasyMock.expect(this.fromIsolatedNodeStub.getName()).andStubReturn(
                this.fromNodeNameStub);
        EasyMock.expect(this.fromIsolatedNodeStub.getNetwork()).andStubReturn(
                this.networkStub);
        EasyMock.expect(
                this.fromIsolatedNodeStub.pointsTo(EasyMock.notNull(Node.class)))
                .andStubReturn(false);
        EasyMock.expect(this.fromIsolatedNodeStub.getOutDegree())
                .andStubReturn(0);
        EasyMock.expect(this.fromIsolatedNodeStub.getOuts()).andStubReturn(
                ImmutableSet.<Arc> of());
        EasyMock.expect(this.fromIsolatedNodeStub.getInDegree()).andStubReturn(
                0);
        EasyMock.expect(this.fromIsolatedNodeStub.getIns()).andStubReturn(
                ImmutableSet.<Arc> of());

        this.toIsolatedNodeStub = EasyMock.createStrictMock(IsolatedNode.class);
        EasyMock.expect(this.toIsolatedNodeStub.getName()).andStubReturn(
                this.toNodeNameStub);
        EasyMock.expect(this.toIsolatedNodeStub.getNetwork()).andStubReturn(
                this.networkStub);
        EasyMock.expect(
                this.toIsolatedNodeStub.pointsTo(EasyMock.notNull(Node.class)))
                .andStubReturn(false);
        EasyMock.expect(this.toIsolatedNodeStub.getOutDegree())
                .andStubReturn(0);
        EasyMock.expect(this.toIsolatedNodeStub.getOuts()).andStubReturn(
                ImmutableSet.<Arc> of());
        EasyMock.expect(this.toIsolatedNodeStub.getInDegree()).andStubReturn(0);
        EasyMock.expect(this.toIsolatedNodeStub.getIns()).andStubReturn(
                ImmutableSet.<Arc> of());

        this.otherFromIsolatedNodeStub =
                EasyMock.createStrictMock("otherFromNodeStub",
                        IsolatedNode.class);
        EasyMock.expect(this.otherFromIsolatedNodeStub.getName())
                .andStubReturn(this.otherFromNodeNameStub);
        EasyMock.expect(this.otherFromIsolatedNodeStub.getNetwork())
                .andStubReturn(this.networkStub);
        EasyMock.expect(
                this.otherFromIsolatedNodeStub.pointsTo(EasyMock
                        .notNull(Node.class))).andStubReturn(false);
        EasyMock.expect(this.otherFromIsolatedNodeStub.getOutDegree())
                .andStubReturn(0);
        EasyMock.expect(this.otherFromIsolatedNodeStub.getOuts())
                .andStubReturn(ImmutableSet.<Arc> of());
        EasyMock.expect(this.otherFromIsolatedNodeStub.getInDegree())
                .andStubReturn(0);
        EasyMock.expect(this.otherFromIsolatedNodeStub.getIns()).andStubReturn(
                ImmutableSet.<Arc> of());

        this.otherNetworkOtherFromIsolatedNodeStub =
                EasyMock.createStrictMock("otherFromNodeStub",
                        IsolatedNode.class);
        EasyMock.expect(this.otherNetworkOtherFromIsolatedNodeStub.getName())
                .andStubReturn(this.otherFromNodeNameStub);
        EasyMock.expect(this.otherNetworkOtherFromIsolatedNodeStub.getNetwork())
                .andStubReturn(this.otherNetworkStub);
        EasyMock.expect(
                this.otherNetworkOtherFromIsolatedNodeStub.pointsTo(EasyMock
                        .notNull(Node.class))).andStubReturn(false);
        EasyMock.expect(
                this.otherNetworkOtherFromIsolatedNodeStub.getOutDegree())
                .andStubReturn(0);
        EasyMock.expect(this.otherNetworkOtherFromIsolatedNodeStub.getOuts())
                .andStubReturn(ImmutableSet.<Arc> of());
        EasyMock.expect(
                this.otherNetworkOtherFromIsolatedNodeStub.getInDegree())
                .andStubReturn(0);
        EasyMock.expect(this.otherNetworkOtherFromIsolatedNodeStub.getIns())
                .andStubReturn(ImmutableSet.<Arc> of());

        this.otherToIsolatedNodeStub =
                EasyMock.createStrictMock("otherFromNodeStub",
                        IsolatedNode.class);
        EasyMock.expect(this.otherToIsolatedNodeStub.getName()).andStubReturn(
                this.otherToNodeNameStub);
        EasyMock.expect(this.otherToIsolatedNodeStub.getNetwork())
                .andStubReturn(this.networkStub);
        EasyMock.expect(
                this.otherToIsolatedNodeStub.pointsTo(EasyMock
                        .notNull(Node.class))).andStubReturn(false);
        EasyMock.expect(this.otherToIsolatedNodeStub.getOutDegree())
                .andStubReturn(0);
        EasyMock.expect(this.otherToIsolatedNodeStub.getOuts()).andStubReturn(
                ImmutableSet.<Arc> of());
        EasyMock.expect(this.otherToIsolatedNodeStub.getInDegree())
                .andStubReturn(0);
        EasyMock.expect(this.otherToIsolatedNodeStub.getIns()).andStubReturn(
                ImmutableSet.<Arc> of());

        this.otherNetworkOtherToIsolatedNodeStub =
                EasyMock.createStrictMock("otherFromNodeStub",
                        IsolatedNode.class);
        EasyMock.expect(this.otherNetworkOtherToIsolatedNodeStub.getName())
                .andStubReturn(this.otherToNodeNameStub);
        EasyMock.expect(this.otherNetworkOtherToIsolatedNodeStub.getNetwork())
                .andStubReturn(this.otherNetworkStub);
        EasyMock.expect(
                this.otherNetworkOtherToIsolatedNodeStub.pointsTo(EasyMock
                        .notNull(Node.class))).andStubReturn(false);
        EasyMock.expect(this.otherNetworkOtherToIsolatedNodeStub.getOutDegree())
                .andStubReturn(0);
        EasyMock.expect(this.otherNetworkOtherToIsolatedNodeStub.getOuts())
                .andStubReturn(ImmutableSet.<Arc> of());
        EasyMock.expect(this.otherNetworkOtherToIsolatedNodeStub.getInDegree())
                .andStubReturn(0);
        EasyMock.expect(this.otherNetworkOtherToIsolatedNodeStub.getIns())
                .andStubReturn(ImmutableSet.<Arc> of());

        this.toExitNodeStub = EasyMock.createStrictMock(ExitNode.class);
        EasyMock.expect(this.toExitNodeStub.getName()).andStubReturn(
                this.toNodeNameStub);
        EasyMock.expect(this.toExitNodeStub.getNetwork()).andStubReturn(
                this.networkStub);

        this.otherFromEnterNodeStub =
                EasyMock.createStrictMock(EnterNode.class);
        EasyMock.expect(this.otherFromEnterNodeStub.getName()).andStubReturn(
                this.otherFromNodeNameStub);
        EasyMock.expect(this.otherFromEnterNodeStub.getNetwork())
                .andStubReturn(this.networkStub);

        this.otherToExitNodeStub = EasyMock.createStrictMock(ExitNode.class);
        EasyMock.expect(this.otherToExitNodeStub.getName()).andStubReturn(
                this.otherToNodeNameStub);
        EasyMock.expect(this.otherToExitNodeStub.getNetwork()).andStubReturn(
                this.networkStub);

        this.otherNetworkOtherFromEnterNodeStub =
                EasyMock.createStrictMock(EnterNode.class);
        EasyMock.expect(this.otherNetworkOtherFromEnterNodeStub.getName())
                .andStubReturn(this.otherFromNodeNameStub);
        EasyMock.expect(this.otherNetworkOtherFromEnterNodeStub.getNetwork())
                .andStubReturn(this.otherNetworkStub);

        this.otherNetworkOtherToExitNodeStub =
                EasyMock.createStrictMock(ExitNode.class);
        EasyMock.expect(this.otherNetworkOtherToExitNodeStub.getName())
                .andStubReturn(this.otherToNodeNameStub);
        EasyMock.expect(this.otherNetworkOtherToExitNodeStub.getNetwork())
                .andStubReturn(this.otherNetworkStub);
    }

    /**
     * Uklidí testovací objekty.
     * 
     * @throws java.lang.Exception
     *             pokud dojde k vyhození výjimky
     */
    @After
    public void tearDown() throws Exception {
        this.otherNetworkNameStub = Intended.nullReference();
        this.networkNameStub = Intended.nullReference();
        this.someNetworkNameStub = Intended.nullReference();
        this.systemNameStub = Intended.nullReference();
        this.reservedNetworkNameStub = Intended.nullReference();
        this.tested = Intended.nullReference();
        this.graph = Intended.nullReference();
        this.statesNamingAuthority = Intended.nullReference();
        this.variablesNamingAuthority = Intended.nullReference();
        this.nodeModifier = Intended.nullReference();
        this.arcModifier = Intended.nullReference();
        this.realignmentProcessor = Intended.nullReference();
        this.systemVisitorFactory = Intended.nullReference();
        this.dispatcher = Intended.nullReference();
        this.initialNodeFactory = Intended.nullReference();
        this.initialArcFactory = Intended.nullReference();
        this.networkStub = Intended.nullReference();
        this.otherNetworkStub = Intended.nullReference();
        this.affectedArcNameDummy = Intended.nullReference();
        this.affectedArcStub = Intended.nullReference();
        this.fromNodeNameStub = Intended.nullReference();
        this.toNodeNameStub = Intended.nullReference();
        this.referringArcNameDummy = Intended.nullReference();
        this.otherFromNodeNameStub = Intended.nullReference();
        this.otherToNodeNameStub = Intended.nullReference();
        this.priorityDummy = Intended.nullReference();
        this.referredFromNodeStub = Intended.nullReference();
        this.emptyCodeDummy = Intended.nullReference();
        this.otherNetworkTemporaryArcStub = Intended.nullReference();
        this.thisTemporaryArcStub = Intended.nullReference();
        this.otherNetworkReferringArcStub = Intended.nullReference();
        this.thisNetworkReferringArcStub = Intended.nullReference();
        this.fromIsolatedNodeStub = Intended.nullReference();
        this.toIsolatedNodeStub = Intended.nullReference();
        this.otherNetworkOtherFromIsolatedNodeStub = Intended.nullReference();
        this.otherFromIsolatedNodeStub = Intended.nullReference();
        this.otherToIsolatedNodeStub = Intended.nullReference();
        this.otherNetworkOtherToIsolatedNodeStub = Intended.nullReference();
        this.toExitNodeStub = Intended.nullReference();
        this.otherFromEnterNodeStub = Intended.nullReference();
        this.otherToExitNodeStub = Intended.nullReference();
        this.otherNetworkOtherFromEnterNodeStub = Intended.nullReference();
        this.otherNetworkOtherToExitNodeStub = Intended.nullReference();
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.DefaultSystem#accept(cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.Visitor)}
     * a
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.DefaultSystem#addNetwork(Network, SystemName)}
     * .
     */
    @Test
    public void testAccept() {
        final Visitor visitorMock = EasyMock.createStrictMock(Visitor.class);
        visitorMock.visit(this.tested);
        EasyMock.replay(visitorMock);

        final Network networkMock = this.networkStub;
        networkMock.accept(visitorMock);

        this.dispatcher.fire(EasyMock.notNull(NetworkAddedEvent.class));

        replayTested();

        this.tested.addNetwork(networkMock, this.networkNameStub);
        this.tested.accept(visitorMock);

        EasyMock.verify(visitorMock);
        verifyTested();
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.DefaultSystem#addNetwork(Network, SystemName)}
     * .
     */
    @Test
    public void testAddNetworkExpectEventFiredWithCorrectParameters() {
        final NetworkAddedEvent eventDummy =
                PowerMock.createStrictMock(NetworkAddedEvent.class);
        PowerMock.replay(eventDummy);

        PowerMock.mockStaticStrict(NetworkAddedEvent.class);
        EasyMock.expect(NetworkAddedEvent.create(this.tested, this.networkStub))
                .andStubReturn(eventDummy);
        PowerMock.replay(NetworkAddedEvent.class);

        this.dispatcher.fire(eventDummy);

        replayTested();

        this.tested.addNetwork(this.networkStub, this.networkNameStub);

        PowerMock.verify(NetworkAddedEvent.class);
        PowerMock.verify(eventDummy);
        verifyTested();
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.DefaultSystem#addNetwork(Network, SystemName)}
     * .
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddNetworkWhenAlreadyPresent() {
        this.dispatcher.fire(EasyMock.notNull(NetworkAddedEvent.class));

        replayTested();

        this.tested.addNetwork(this.networkStub, this.networkNameStub);
        try {
            this.tested.addNetwork(this.networkStub, this.someNetworkNameStub);
        } finally {
            verifyTested();
        }
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.DefaultSystem#addNetwork(Network, SystemName)}
     * .
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddNetworkWhenNameAlreadyPresent() {
        this.dispatcher.fire(EasyMock.notNull(NetworkAddedEvent.class));

        replayTested();

        this.tested.addNetwork(this.networkStub, this.networkNameStub);
        try {
            this.tested
                    .addNetwork(this.otherNetworkStub, this.networkNameStub);
        } finally {
            verifyTested();
        }
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.DefaultSystem#addNetwork(Network, SystemName)}
     * .
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddNetworkWhenNameAlreadyReserved() {
        replayTested();

        try {
            this.tested.addNetwork(this.networkStub,
                    this.reservedNetworkNameStub);
        } finally {
            verifyTested();
        }
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.DefaultSystem#getNetworks()}
     * a
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.DefaultSystem#addNetwork(Network, SystemName)}
     * .
     */
    @Test
    public void testGetNetworks() {
        this.dispatcher.fire(EasyMock.notNull(NetworkAddedEvent.class));

        replayTested();

        this.tested.addNetwork(this.networkStub, this.networkNameStub);
        assertEquals(ImmutableSet.of(this.networkStub),
                this.tested.getNetworks());

        verifyTested();
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.DefaultSystem#getNodes(cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network)}
     * a
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.DefaultSystem#addNode(Network, int, int)}
     * .
     */
    @Test
    public void testGetNodesWhenAddedToNetworkContainsNewNode() {
        EasyMock.expect(this.statesNamingAuthority.generate()).andReturn(
                FROM_NODE_NAME);

        PowerMock.mockStatic(NormalWords.class);
        EasyMock.expect(NormalWords.of(FROM_NODE_NAME)).andReturn(
                this.fromNodeNameStub);
        PowerMock.replay(NormalWords.class);

        EasyMock.expect(
                this.initialNodeFactory.produce(this.fromNodeNameStub,
                        this.networkStub, FROM_NODE_X, FROM_NODE_Y)).andReturn(
                this.fromIsolatedNodeStub);

        this.graph.add(this.fromIsolatedNodeStub);

        this.dispatcher.fire(EasyMock.notNull(NetworkAddedEvent.class));
        this.dispatcher.fire(EasyMock.notNull(NodeAddedEvent.class));

        replayTested();

        this.tested.addNetwork(this.networkStub, this.networkNameStub);
        this.tested.addNode(this.networkStub, FROM_NODE_X, FROM_NODE_Y);
        assertEquals(ImmutableSet.of(this.fromIsolatedNodeStub),
                this.tested.getNodes(this.networkStub));

        PowerMock.verify(NormalWords.class);
        verifyTested();
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.DefaultSystem#getNodes(cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network)}
     * a
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.DefaultSystem#removeNode(Node)}
     * .
     */
    @Test
    public void testGetNodesWhenNodeRemovedExpectNotContained() {
        EasyMock.expect(this.statesNamingAuthority.generate()).andReturn(
                FROM_NODE_NAME);
        this.statesNamingAuthority.release(FROM_NODE_NAME);

        PowerMock.mockStatic(NormalWords.class);
        EasyMock.expect(NormalWords.of(FROM_NODE_NAME)).andReturn(
                this.fromNodeNameStub);
        PowerMock.replay(NormalWords.class);

        EasyMock.expect(
                this.initialNodeFactory.produce(this.fromNodeNameStub,
                        this.networkStub, FROM_NODE_X, FROM_NODE_Y)).andReturn(
                this.fromIsolatedNodeStub);

        final Update emptyUpdateStub = EasyMock.createMock(Update.class);
        EasyMock.expect(emptyUpdateStub.getInitialsRemoved()).andStubReturn(
                ImmutableSet.<EnterNode> of());
        EasyMock.expect(emptyUpdateStub.getInitialsAdded()).andStubReturn(
                ImmutableSet.<EnterNode> of());
        EasyMock.expect(emptyUpdateStub.getSwitched()).andStubReturn(
                ImmutableMap.<Node, Node> of());
        EasyMock.expect(emptyUpdateStub.getEdgesRemoved()).andStubReturn(
                ImmutableSet.<Arc> of());
        EasyMock.expect(emptyUpdateStub.getReferencesRemoved()).andStubReturn(
                ImmutableSet.<RecurentArc> of());
        EasyMock.replay(emptyUpdateStub);

        this.graph.add(this.fromIsolatedNodeStub);
        EasyMock.expect(this.graph.containsVertex(this.fromIsolatedNodeStub))
                .andReturn(true);
        EasyMock.expect(
                this.graph.removeAndRealign(this.fromIsolatedNodeStub,
                        this.realignmentProcessor,
                        ImmutableMap.<EnterNode, Set<RecurentArc>> of(),
                        ImmutableSet.<EnterNode> of())).andReturn(
                emptyUpdateStub);

        this.dispatcher.fire(EasyMock.notNull(NetworkAddedEvent.class));
        this.dispatcher.fire(EasyMock.notNull(NodeAddedEvent.class));
        this.dispatcher.fire(EasyMock.notNull(NodeRemovedEvent.class));

        replayTested();

        this.tested.addNetwork(this.networkStub, this.networkNameStub);
        this.tested.addNode(this.networkStub, FROM_NODE_X, FROM_NODE_Y);
        this.tested.removeNode(this.fromIsolatedNodeStub);
        assertTrue(this.tested.getNodes(this.networkStub).isEmpty());

        EasyMock.verify(emptyUpdateStub);
        PowerMock.verify(NormalWords.class);
        verifyTested();
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.DefaultSystem#changeNode(cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord, cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord)}
     * .
     */
    @Test
    public void testChangeNodeNameExpectReplaced() {
        EasyMock.expect(this.graph.containsVertex(this.fromIsolatedNodeStub))
                .andStubReturn(true);
        this.graph.replaceVertex(this.fromIsolatedNodeStub,
                this.otherFromIsolatedNodeStub);

        PowerMock.mockStatic(NormalWords.class);
        EasyMock.expect(NormalWords.of(OTHER_FROM_NODE_NAME)).andStubReturn(
                this.otherFromNodeNameStub);
        PowerMock.replay(NormalWords.class);

        EasyMock.expect(
                this.nodeModifier.change(this.fromIsolatedNodeStub,
                        this.otherFromNodeNameStub)).andStubReturn(
                this.otherFromIsolatedNodeStub);

        EasyMock.expect(
                this.statesNamingAuthority.replace(FROM_NODE_NAME,
                        OTHER_FROM_NODE_NAME)).andReturn(OTHER_FROM_NODE_NAME);

        this.dispatcher.fire(EasyMock.notNull(NodeRenamedEvent.class));

        replayTested();

        this.tested.changeNode(this.fromIsolatedNodeStub,
                this.otherFromNodeNameStub);

        verifyTested();
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.DefaultSystem#changeNode(cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord, int, int)}
     * .
     */
    @Test
    public void testChangeNodePosition() {
        EasyMock.expect(this.fromIsolatedNodeStub.getX()).andStubReturn(
                FROM_NODE_X);
        EasyMock.expect(this.fromIsolatedNodeStub.getY()).andStubReturn(
                FROM_NODE_Y);

        EasyMock.expect(this.graph.containsVertex(this.fromIsolatedNodeStub))
                .andStubReturn(true);
        this.graph.replaceVertex(this.fromIsolatedNodeStub,
                this.otherFromIsolatedNodeStub);

        EasyMock.expect(
                this.nodeModifier.change(this.fromIsolatedNodeStub,
                        OTHER_FROM_NODE_X, OTHER_FROM_NODE_Y)).andStubReturn(
                this.otherFromIsolatedNodeStub);

        this.dispatcher.fire(EasyMock.notNull(NodeMovedEvent.class));

        replayTested();

        this.tested.changeNode(this.fromIsolatedNodeStub, OTHER_FROM_NODE_X,
                OTHER_FROM_NODE_Y);

        verifyTested();
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.DefaultSystem#changeNode(cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord, java.lang.Class)}
     * .
     */
    @Test
    public void testChangeNodeTypeExpectNodeReplaced() {
        final InputNode oldVersionStub =
                EasyMock.createStrictMock(InputNode.class);
        EasyMock.expect(oldVersionStub.getNetwork()).andStubReturn(
                this.networkStub);
        EasyMock.replay(oldVersionStub);

        final ProcessingNode newVersionDummy =
                EasyMock.createStrictMock(ProcessingNode.class);
        EasyMock.replay(newVersionDummy);

        EasyMock.expect(this.graph.containsVertex(oldVersionStub))
                .andStubReturn(true);
        this.graph.replaceVertex(oldVersionStub, newVersionDummy);

        EasyMock.expect(
                this.nodeModifier.change(oldVersionStub, ProcessingNode.class))
                .andStubReturn(newVersionDummy);

        this.dispatcher.fire(EasyMock.notNull(NodeTypeChangedEvent.class));

        replayTested();

        this.tested.changeNode(oldVersionStub, ProcessingNode.class);

        EasyMock.verify(oldVersionStub);
        verifyTested();
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.DefaultSystem#changeNode(cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord, java.lang.Class)}
     * .
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChangeNodeTypeWhenUnsupportedTypeUsed() {
        final FunctionalNode unsupportedNodeTypePrototype =
                EasyMock.createStrictMock(FunctionalNode.class);
        EasyMock.replay(unsupportedNodeTypePrototype);

        EasyMock.expect(this.graph.containsVertex(this.fromIsolatedNodeStub))
                .andReturn(true);

        replayTested();

        try {
            this.tested.changeNode(this.fromIsolatedNodeStub,
                    unsupportedNodeTypePrototype.getClass());
        } finally {
            verifyTested();
        }
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.DefaultSystem#removeArc(cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc)}
     * a
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.DefaultSystem#removeNode(Node)}
     * .
     */
    @Test
    public
            void
            testRemoveArcWhenTheArcIsReferringExpectSubsequentFormerlyRefferedNodeRemovalNotBlocked() {
        EasyMock.expect(this.statesNamingAuthority.generate()).andReturn(
                FROM_NODE_NAME);
        EasyMock.expect(this.statesNamingAuthority.generate()).andReturn(
                TO_NODE_NAME);
        EasyMock.expect(this.statesNamingAuthority.use(AFFECTED_ARC_NAME))
                .andReturn(AFFECTED_ARC_NAME);
        EasyMock.expect(this.statesNamingAuthority.generate()).andReturn(
                OTHER_FROM_NODE_NAME);
        EasyMock.expect(this.statesNamingAuthority.generate()).andReturn(
                OTHER_TO_NODE_NAME);
        EasyMock.expect(this.statesNamingAuthority.use(REFERRING_ARC_NAME))
                .andReturn(REFERRING_ARC_NAME);
        EasyMock.expect(
                this.statesNamingAuthority.replace(REFERRING_ARC_NAME,
                        REFERRING_ARC_NAME)).andReturn(REFERRING_ARC_NAME);
        this.statesNamingAuthority.release(REFERRING_ARC_NAME);
        this.statesNamingAuthority.release(FROM_NODE_NAME);
        this.statesNamingAuthority.release(AFFECTED_ARC_NAME);

        PowerMock.mockStatic(NormalWords.class);
        EasyMock.expect(NormalWords.of(FROM_NODE_NAME)).andReturn(
                this.fromNodeNameStub);
        EasyMock.expect(NormalWords.of(TO_NODE_NAME)).andReturn(
                this.toNodeNameStub);
        EasyMock.expect(NormalWords.of(OTHER_FROM_NODE_NAME)).andReturn(
                this.otherFromNodeNameStub);
        EasyMock.expect(NormalWords.of(OTHER_TO_NODE_NAME)).andReturn(
                this.otherToNodeNameStub);
        EasyMock.expect(NormalWords.of(AFFECTED_ARC_NAME)).andReturn(
                this.affectedArcNameDummy);
        EasyMock.expect(NormalWords.of(REFERRING_ARC_NAME)).andReturn(
                this.referringArcNameDummy);
        EasyMock.expect(NormalWords.of(REFERRING_ARC_NAME)).andReturn(
                this.referringArcNameDummy);
        PowerMock.replay(NormalWords.class);

        EasyMock.expect(
                this.initialNodeFactory.produce(this.fromNodeNameStub,
                        this.networkStub, FROM_NODE_X, FROM_NODE_Y)).andReturn(
                this.fromIsolatedNodeStub);
        EasyMock.expect(
                this.initialNodeFactory.produce(this.toNodeNameStub,
                        this.networkStub, TO_NODE_X, TO_NODE_Y)).andReturn(
                this.toIsolatedNodeStub);
        EasyMock.expect(
                this.initialNodeFactory.produce(this.otherFromNodeNameStub,
                        this.networkStub, OTHER_FROM_NODE_X, OTHER_FROM_NODE_Y))
                .andReturn(this.otherFromIsolatedNodeStub);
        EasyMock.expect(
                this.initialNodeFactory.produce(this.otherToNodeNameStub,
                        this.networkStub, OTHER_TO_NODE_X, OTHER_TO_NODE_Y))
                .andReturn(this.otherToIsolatedNodeStub);

        EasyMock.expect(
                this.initialArcFactory.produce(this.networkStub,
                        this.affectedArcNameDummy)).andReturn(
                this.affectedArcStub);
        EasyMock.expect(
                this.initialArcFactory.produce(this.networkStub,
                        this.referringArcNameDummy)).andReturn(
                this.thisTemporaryArcStub);

        final Update addAffectedUpdateStub =
                EasyMock.createStrictMock(Update.class);
        EasyMock.expect(addAffectedUpdateStub.getInitialsAdded())
                .andStubReturn(ImmutableSet.of(this.referredFromNodeStub));
        EasyMock.expect(addAffectedUpdateStub.getInitialsRemoved())
                .andStubReturn(ImmutableSet.<EnterNode> of());
        EasyMock.expect(addAffectedUpdateStub.getSwitched()).andStubReturn(
                ImmutableMap.<Node, Node> of(this.fromIsolatedNodeStub,
                        this.referredFromNodeStub, this.toIsolatedNodeStub,
                        this.toExitNodeStub));
        EasyMock.replay(addAffectedUpdateStub);

        final Update addTemporaryUpdateStub =
                EasyMock.createStrictMock(Update.class);
        EasyMock.expect(addTemporaryUpdateStub.getInitialsAdded())
                .andStubReturn(ImmutableSet.of(this.otherFromEnterNodeStub));
        EasyMock.expect(addTemporaryUpdateStub.getInitialsRemoved())
                .andStubReturn(ImmutableSet.<EnterNode> of());
        EasyMock.expect(addTemporaryUpdateStub.getSwitched())
                .andStubReturn(
                        ImmutableMap.<Node, Node> of(
                                this.otherFromIsolatedNodeStub,
                                this.otherFromEnterNodeStub,
                                this.otherToIsolatedNodeStub,
                                this.otherToExitNodeStub));
        EasyMock.replay(addTemporaryUpdateStub);

        final Update referringRemovalUpdateStub =
                EasyMock.createStrictMock(Update.class);
        EasyMock.expect(referringRemovalUpdateStub.getInitialsAdded())
                .andStubReturn(ImmutableSet.<EnterNode> of());
        EasyMock.expect(referringRemovalUpdateStub.getInitialsRemoved())
                .andStubReturn(
                        ImmutableSet
                                .<EnterNode> of(this.otherFromEnterNodeStub));
        EasyMock.expect(referringRemovalUpdateStub.getSwitched())
                .andStubReturn(
                        ImmutableMap.<Node, Node> of(
                                this.otherFromEnterNodeStub,
                                this.otherFromIsolatedNodeStub,
                                this.otherToIsolatedNodeStub,
                                this.otherToIsolatedNodeStub));
        EasyMock.expect(referringRemovalUpdateStub.getReferencesRemoved())
                .andStubReturn(
                        ImmutableSet.of(this.thisNetworkReferringArcStub));
        EasyMock.replay(referringRemovalUpdateStub);

        final Update referredRemovalUpdateStub =
                EasyMock.createStrictMock(Update.class);
        EasyMock.expect(referredRemovalUpdateStub.getInitialsAdded())
                .andStubReturn(ImmutableSet.<EnterNode> of());
        EasyMock.expect(referredRemovalUpdateStub.getInitialsRemoved())
                .andStubReturn(
                        ImmutableSet.<EnterNode> of(this.referredFromNodeStub));
        EasyMock.expect(referredRemovalUpdateStub.getSwitched()).andStubReturn(
                ImmutableMap.<Node, Node> of(this.toExitNodeStub,
                        this.toIsolatedNodeStub));
        EasyMock.expect(referredRemovalUpdateStub.getReferencesRemoved())
                .andStubReturn(ImmutableSet.<RecurentArc> of());
        EasyMock.expect(referredRemovalUpdateStub.getEdgesRemoved())
                .andStubReturn(ImmutableSet.<Arc> of(this.affectedArcStub));
        EasyMock.replay(referredRemovalUpdateStub);

        this.graph.add(this.fromIsolatedNodeStub);
        this.graph.add(this.toIsolatedNodeStub);
        EasyMock.expect(
                this.graph.addAndRealign(this.affectedArcStub,
                        this.fromIsolatedNodeStub, this.toIsolatedNodeStub,
                        this.realignmentProcessor,
                        ImmutableSet.<EnterNode> of())).andReturn(
                addAffectedUpdateStub);
        this.graph.add(this.otherFromIsolatedNodeStub);
        this.graph.add(this.otherToIsolatedNodeStub);
        EasyMock.expect(
                this.graph.addAndRealign(this.thisTemporaryArcStub,
                        this.otherFromIsolatedNodeStub,
                        this.otherToIsolatedNodeStub,
                        this.realignmentProcessor,
                        ImmutableSet.<EnterNode> of(this.referredFromNodeStub)))
                .andReturn(addTemporaryUpdateStub);
        EasyMock.expect(this.graph.containsEdge(this.thisTemporaryArcStub))
                .andReturn(true);
        EasyMock.expect(this.graph.containsVertex(this.referredFromNodeStub))
                .andReturn(true);
        this.graph.replaceEdge(this.thisTemporaryArcStub,
                this.thisNetworkReferringArcStub);
        EasyMock.expect(
                this.graph.containsEdge(this.thisNetworkReferringArcStub))
                .andReturn(true);
        EasyMock.expect(
                this.graph.removeAndRealign(this.thisNetworkReferringArcStub,
                        this.realignmentProcessor, ImmutableMap.of(
                                this.referredFromNodeStub, ImmutableSet
                                        .of(this.thisNetworkReferringArcStub)),
                        ImmutableSet.of(this.referredFromNodeStub,
                                this.otherFromEnterNodeStub))).andReturn(
                referringRemovalUpdateStub);
        EasyMock.expect(this.graph.containsVertex(this.referredFromNodeStub))
                .andReturn(true);
        EasyMock.expect(
                this.graph.removeAndRealign(this.referredFromNodeStub,
                        this.realignmentProcessor,
                        ImmutableMap.<EnterNode, Set<RecurentArc>> of(),
                        ImmutableSet.of(this.referredFromNodeStub))).andReturn(
                referredRemovalUpdateStub);

        EasyMock.expect(
                this.arcModifier.change(this.thisTemporaryArcStub,
                        this.referringArcNameDummy, this.priorityDummy,
                        RecurentArc.class, this.emptyCodeDummy,
                        this.referredFromNodeStub)).andStubReturn(
                this.thisNetworkReferringArcStub);

        this.dispatcher.fire(EasyMock.notNull(NetworkAddedEvent.class));

        this.dispatcher.fire(EasyMock.notNull(NodeAddedEvent.class));

        this.dispatcher.fire(EasyMock.notNull(NodeAddedEvent.class));

        EasyMock.checkOrder(this.dispatcher, false);
        this.dispatcher.fire(EasyMock.notNull(NodeTypeChangedEvent.class));
        this.dispatcher.fire(EasyMock.notNull(NodeTypeChangedEvent.class));
        this.dispatcher.fire(EasyMock.notNull(ArcAddedEvent.class));
        this.dispatcher.fire(EasyMock
                .notNull(AvailableReferencesExtendedEvent.class));
        EasyMock.checkOrder(this.dispatcher, true);

        this.dispatcher.fire(EasyMock.notNull(NodeAddedEvent.class));

        this.dispatcher.fire(EasyMock.notNull(NodeAddedEvent.class));

        EasyMock.checkOrder(this.dispatcher, false);
        this.dispatcher.fire(EasyMock.notNull(NodeTypeChangedEvent.class));
        this.dispatcher.fire(EasyMock.notNull(NodeTypeChangedEvent.class));
        this.dispatcher.fire(EasyMock.notNull(ArcAddedEvent.class));
        this.dispatcher.fire(EasyMock
                .notNull(AvailableReferencesExtendedEvent.class));
        EasyMock.checkOrder(this.dispatcher, true);

        EasyMock.checkOrder(this.dispatcher, false);
        this.dispatcher.fire(EasyMock.notNull(ArcChangedEvent.class));
        this.dispatcher.fire(EasyMock.notNull(ArcRetypedEvent.class));
        EasyMock.checkOrder(this.dispatcher, true);

        EasyMock.checkOrder(this.dispatcher, false);
        this.dispatcher
                .fire(EasyMock
                        .notNull(cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.events.ArcRemovedEvent.class));
        this.dispatcher
                .fire(EasyMock
                        .notNull(cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.events.ArcRemovedEvent.class));
        this.dispatcher
                .fire(EasyMock
                        .notNull(cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.events.ArcRemovedEvent.class));
        this.dispatcher.fire(EasyMock
                .notNull(AvailableReferencesReducedEvent.class));
        this.dispatcher.fire(EasyMock.notNull(NodeTypeChangedEvent.class));
        this.dispatcher.fire(EasyMock.notNull(NodeTypeChangedEvent.class));
        EasyMock.checkOrder(this.dispatcher, true);

        EasyMock.checkOrder(this.dispatcher, false);
        this.dispatcher
                .fire(EasyMock
                        .notNull(cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.events.AvailableReferencesReducedEvent.class));
        this.dispatcher.fire(EasyMock.notNull(NodeRemovedEvent.class));
        this.dispatcher.fire(EasyMock.notNull(NodeTypeChangedEvent.class));
        this.dispatcher
                .fire(EasyMock
                        .notNull(cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.events.ArcRemovedEvent.class));
        this.dispatcher
                .fire(EasyMock
                        .notNull(cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.events.ArcRemovedEvent.class));
        this.dispatcher
                .fire(EasyMock
                        .notNull(cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.events.ArcRemovedEvent.class));
        EasyMock.checkOrder(this.dispatcher, true);

        replayTested();

        this.tested.addNetwork(this.networkStub, this.networkNameStub);
        this.tested.addNode(this.networkStub, FROM_NODE_X, FROM_NODE_Y);
        this.tested.addNode(this.networkStub, TO_NODE_X, TO_NODE_Y);
        this.tested.addArc(this.networkStub, this.affectedArcNameDummy,
                this.fromIsolatedNodeStub, this.toIsolatedNodeStub);

        this.tested.addNode(this.networkStub, OTHER_FROM_NODE_X,
                OTHER_FROM_NODE_Y);
        this.tested.addNode(this.networkStub, OTHER_TO_NODE_X, OTHER_TO_NODE_Y);
        this.tested.addArc(this.networkStub, this.referringArcNameDummy,
                this.otherFromIsolatedNodeStub, this.otherToIsolatedNodeStub);
        this.tested.changeArc(this.thisTemporaryArcStub,
                this.referringArcNameDummy, this.priorityDummy,
                RecurentArc.class, this.emptyCodeDummy,
                this.referredFromNodeStub);

        this.tested.removeArc(this.thisNetworkReferringArcStub);
        this.tested.removeNode(this.referredFromNodeStub);

        EasyMock.verify(addAffectedUpdateStub);
        EasyMock.verify(addTemporaryUpdateStub);
        PowerMock.verify(NormalWords.class);
        verifyTested();
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.DefaultSystem#removeNetwork(cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network)}
     * ,
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.DefaultSystem#getNetworks()}
     * a
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.DefaultSystem#addNetwork(Network, SystemName)}
     * .
     */
    @Test
    public void testRemoveNetworkExpectAddedRemoved() {
        final DfsVisitor visitorDummy =
                EasyMock.createStrictMock(DfsVisitor.class);
        EasyMock.replay(visitorDummy);

        EasyMock.expect(
                this.systemVisitorFactory.produce(EasyMock
                        .notNull(DfsObserver.class))).andStubReturn(
                visitorDummy);

        final Network networkMock = this.networkStub;
        networkMock.accept(visitorDummy);

        this.dispatcher.fire(EasyMock.notNull(NetworkAddedEvent.class));
        EasyMock.checkOrder(this.dispatcher, false);
        this.dispatcher
                .fire(EasyMock
                        .notNull(cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.events.NetworkRemovedEvent.class));
        this.dispatcher
                .fire(EasyMock
                        .notNull(cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.events.NetworkRemovedEvent.class));
        this.dispatcher.fire(EasyMock
                .notNull(AvailableReferencesReducedEvent.class));
        EasyMock.checkOrder(this.dispatcher, true);

        this.graph.purge(ImmutableSet.<Node> of());

        replayTested();

        this.tested.addNetwork(networkMock, this.networkNameStub);
        this.tested.removeNetwork(networkMock);
        assertTrue(this.tested.getNetworks().isEmpty());

        PowerMock.verify(NetworkAddedEvent.class);
        EasyMock.verify(visitorDummy);
        verifyTested();
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.DefaultSystem#removeNetwork(cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network)}
     * a další.
     */
    @Test
    public
            void
            testRemoveNetworkWhenContainingDependingArcsExpectSubsequentReferredNetworkRemovalNotBlocked() {
        final DfsVisitor visitorDummy =
                EasyMock.createStrictMock(DfsVisitor.class);
        EasyMock.replay(visitorDummy);

        final Network networkMock = this.networkStub;
        networkMock.accept(visitorDummy);

        final Network otherNetworkMock = this.otherNetworkStub;
        otherNetworkMock.accept(visitorDummy);

        EasyMock.expect(this.statesNamingAuthority.generate()).andReturn(
                FROM_NODE_NAME);
        EasyMock.expect(this.statesNamingAuthority.generate()).andReturn(
                TO_NODE_NAME);
        EasyMock.expect(this.statesNamingAuthority.use(AFFECTED_ARC_NAME))
                .andReturn(AFFECTED_ARC_NAME);
        EasyMock.expect(this.statesNamingAuthority.generate()).andReturn(
                OTHER_FROM_NODE_NAME);
        EasyMock.expect(this.statesNamingAuthority.generate()).andReturn(
                OTHER_TO_NODE_NAME);
        EasyMock.expect(this.statesNamingAuthority.use(REFERRING_ARC_NAME))
                .andReturn(REFERRING_ARC_NAME);
        EasyMock.expect(
                this.statesNamingAuthority.replace(REFERRING_ARC_NAME,
                        REFERRING_ARC_NAME)).andReturn(REFERRING_ARC_NAME);
        this.statesNamingAuthority.release(OTHER_FROM_NODE_NAME);
        this.statesNamingAuthority.release(REFERRING_ARC_NAME);
        this.statesNamingAuthority.release(OTHER_TO_NODE_NAME);
        this.statesNamingAuthority.release(FROM_NODE_NAME);
        this.statesNamingAuthority.release(AFFECTED_ARC_NAME);
        this.statesNamingAuthority.release(TO_NODE_NAME);

        PowerMock.mockStatic(NormalWords.class);
        EasyMock.expect(NormalWords.of(FROM_NODE_NAME)).andReturn(
                this.fromNodeNameStub);
        EasyMock.expect(NormalWords.of(TO_NODE_NAME)).andReturn(
                this.toNodeNameStub);
        EasyMock.expect(NormalWords.of(OTHER_FROM_NODE_NAME)).andReturn(
                this.otherFromNodeNameStub);
        EasyMock.expect(NormalWords.of(OTHER_TO_NODE_NAME)).andReturn(
                this.otherToNodeNameStub);
        EasyMock.expect(NormalWords.of(AFFECTED_ARC_NAME)).andReturn(
                this.affectedArcNameDummy);
        EasyMock.expect(NormalWords.of(REFERRING_ARC_NAME)).andReturn(
                this.referringArcNameDummy);
        EasyMock.expect(NormalWords.of(REFERRING_ARC_NAME)).andReturn(
                this.referringArcNameDummy);
        PowerMock.replay(NormalWords.class);

        EasyMock.expect(
                this.initialNodeFactory.produce(this.fromNodeNameStub,
                        this.networkStub, FROM_NODE_X, FROM_NODE_Y)).andReturn(
                this.fromIsolatedNodeStub);
        EasyMock.expect(
                this.initialNodeFactory.produce(this.toNodeNameStub,
                        networkMock, TO_NODE_X, TO_NODE_Y)).andReturn(
                this.toIsolatedNodeStub);
        EasyMock.expect(
                this.initialNodeFactory.produce(this.otherFromNodeNameStub,
                        otherNetworkMock, OTHER_FROM_NODE_X, OTHER_FROM_NODE_Y))
                .andReturn(this.otherNetworkOtherFromIsolatedNodeStub);
        EasyMock.expect(
                this.initialNodeFactory.produce(this.otherToNodeNameStub,
                        otherNetworkMock, OTHER_TO_NODE_X, OTHER_TO_NODE_Y))
                .andReturn(this.otherNetworkOtherToIsolatedNodeStub);

        EasyMock.expect(
                this.initialArcFactory.produce(networkMock,
                        this.affectedArcNameDummy)).andReturn(
                this.affectedArcStub);
        EasyMock.expect(
                this.initialArcFactory.produce(otherNetworkMock,
                        this.referringArcNameDummy)).andReturn(
                this.otherNetworkTemporaryArcStub);

        final Update addAffectedUpdateStub =
                EasyMock.createStrictMock(Update.class);
        EasyMock.expect(addAffectedUpdateStub.getInitialsAdded())
                .andStubReturn(ImmutableSet.of(this.referredFromNodeStub));
        EasyMock.expect(addAffectedUpdateStub.getInitialsRemoved())
                .andStubReturn(ImmutableSet.<EnterNode> of());
        EasyMock.expect(addAffectedUpdateStub.getSwitched()).andStubReturn(
                ImmutableMap.<Node, Node> of(this.fromIsolatedNodeStub,
                        this.referredFromNodeStub, this.toIsolatedNodeStub,
                        this.toExitNodeStub));
        EasyMock.replay(addAffectedUpdateStub);

        final Update addTemporaryUpdateStub =
                EasyMock.createStrictMock(Update.class);
        EasyMock.expect(addTemporaryUpdateStub.getInitialsAdded())
                .andStubReturn(
                        ImmutableSet
                                .of(this.otherNetworkOtherFromEnterNodeStub));
        EasyMock.expect(addTemporaryUpdateStub.getInitialsRemoved())
                .andStubReturn(ImmutableSet.<EnterNode> of());
        EasyMock.expect(addTemporaryUpdateStub.getSwitched()).andStubReturn(
                ImmutableMap.<Node, Node> of(
                        this.otherNetworkOtherFromIsolatedNodeStub,
                        this.otherNetworkOtherFromEnterNodeStub,
                        this.otherNetworkOtherToIsolatedNodeStub,
                        this.otherNetworkOtherToExitNodeStub));
        EasyMock.replay(addTemporaryUpdateStub);

        this.graph.add(this.fromIsolatedNodeStub);
        this.graph.add(this.toIsolatedNodeStub);
        EasyMock.expect(
                this.graph.addAndRealign(this.affectedArcStub,
                        this.fromIsolatedNodeStub, this.toIsolatedNodeStub,
                        this.realignmentProcessor,
                        ImmutableSet.<EnterNode> of())).andReturn(
                addAffectedUpdateStub);
        this.graph.add(this.otherNetworkOtherFromIsolatedNodeStub);
        this.graph.add(this.otherNetworkOtherToIsolatedNodeStub);
        EasyMock.expect(
                this.graph.addAndRealign(this.otherNetworkTemporaryArcStub,
                        this.otherNetworkOtherFromIsolatedNodeStub,
                        this.otherNetworkOtherToIsolatedNodeStub,
                        this.realignmentProcessor,
                        ImmutableSet.<EnterNode> of())).andReturn(
                addTemporaryUpdateStub);
        EasyMock.expect(
                this.graph.containsEdge(this.otherNetworkTemporaryArcStub))
                .andReturn(true);
        EasyMock.expect(this.graph.containsVertex(this.referredFromNodeStub))
                .andReturn(true);
        this.graph.replaceEdge(this.otherNetworkTemporaryArcStub,
                this.otherNetworkReferringArcStub);
        this.graph.purge(ImmutableSet.of(
                this.otherNetworkOtherFromEnterNodeStub,
                this.otherNetworkOtherToExitNodeStub));
        this.graph.purge(ImmutableSet.of(this.referredFromNodeStub,
                this.toExitNodeStub));

        EasyMock.expect(
                this.arcModifier.change(this.otherNetworkTemporaryArcStub,
                        this.referringArcNameDummy, this.priorityDummy,
                        RecurentArc.class, this.emptyCodeDummy,
                        this.referredFromNodeStub)).andStubReturn(
                this.otherNetworkReferringArcStub);

        EasyMock.expect(
                this.systemVisitorFactory.produce(EasyMock
                        .notNull(DfsObserver.class))).andAnswer(
                new IAnswer<DfsVisitor>() {

                    @Override
                    public DfsVisitor answer() throws Throwable {
                        final DfsObserver callback =
                                (DfsObserver) EasyMock.getCurrentArguments()[0];

                        callback.notifyDiscovery(DefaultSystemTest.this.otherNetworkOtherFromEnterNodeStub);
                        callback.notifyExamination(DefaultSystemTest.this.otherNetworkReferringArcStub);
                        callback.notifyDiscovery(DefaultSystemTest.this.otherNetworkOtherToExitNodeStub);

                        return visitorDummy;
                    }

                });
        EasyMock.expect(
                this.systemVisitorFactory.produce(EasyMock
                        .notNull(DfsObserver.class))).andAnswer(
                new IAnswer<DfsVisitor>() {

                    @Override
                    public DfsVisitor answer() throws Throwable {
                        final DfsObserver callback =
                                (DfsObserver) EasyMock.getCurrentArguments()[0];

                        callback.notifyDiscovery(DefaultSystemTest.this.referredFromNodeStub);
                        callback.notifyExamination(DefaultSystemTest.this.affectedArcStub);
                        callback.notifyDiscovery(DefaultSystemTest.this.toExitNodeStub);

                        return visitorDummy;
                    }

                });

        this.dispatcher.fire(EasyMock.notNull(NetworkAddedEvent.class));
        this.dispatcher.fire(EasyMock.notNull(NodeAddedEvent.class));
        this.dispatcher.fire(EasyMock.notNull(NodeAddedEvent.class));
        EasyMock.checkOrder(this.dispatcher, false);
        this.dispatcher.fire(EasyMock.notNull(NodeTypeChangedEvent.class));
        this.dispatcher.fire(EasyMock.notNull(NodeTypeChangedEvent.class));
        this.dispatcher.fire(EasyMock.notNull(ArcAddedEvent.class));
        this.dispatcher.fire(EasyMock
                .notNull(AvailableReferencesExtendedEvent.class));
        EasyMock.checkOrder(this.dispatcher, true);
        this.dispatcher.fire(EasyMock.notNull(NetworkAddedEvent.class));
        this.dispatcher.fire(EasyMock.notNull(NodeAddedEvent.class));
        this.dispatcher.fire(EasyMock.notNull(NodeAddedEvent.class));
        EasyMock.checkOrder(this.dispatcher, false);
        this.dispatcher.fire(EasyMock.notNull(NodeTypeChangedEvent.class));
        this.dispatcher.fire(EasyMock.notNull(NodeTypeChangedEvent.class));
        this.dispatcher.fire(EasyMock.notNull(ArcAddedEvent.class));
        this.dispatcher.fire(EasyMock
                .notNull(AvailableReferencesExtendedEvent.class));
        EasyMock.checkOrder(this.dispatcher, true);
        EasyMock.checkOrder(this.dispatcher, false);
        this.dispatcher.fire(EasyMock.notNull(ArcChangedEvent.class));
        this.dispatcher.fire(EasyMock.notNull(ArcRetypedEvent.class));
        EasyMock.checkOrder(this.dispatcher, true);
        this.dispatcher.fire(EasyMock
                .notNull(AvailableReferencesReducedEvent.class));
        EasyMock.checkOrder(this.dispatcher, false);
        this.dispatcher
                .fire(EasyMock
                        .notNull(cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.events.NetworkRemovedEvent.class));
        this.dispatcher
                .fire(EasyMock
                        .notNull(cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.events.NetworkRemovedEvent.class));
        EasyMock.checkOrder(this.dispatcher, true);
        this.dispatcher.fire(EasyMock
                .notNull(AvailableReferencesReducedEvent.class));
        EasyMock.checkOrder(this.dispatcher, false);
        this.dispatcher
                .fire(EasyMock
                        .notNull(cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.events.NetworkRemovedEvent.class));
        this.dispatcher
                .fire(EasyMock
                        .notNull(cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.events.NetworkRemovedEvent.class));
        EasyMock.checkOrder(this.dispatcher, true);

        replayTested();

        this.tested.addNetwork(networkMock, this.networkNameStub);
        this.tested.addNode(networkMock, FROM_NODE_X, FROM_NODE_Y);
        this.tested.addNode(networkMock, TO_NODE_X, TO_NODE_Y);
        this.tested.addArc(networkMock, this.affectedArcNameDummy,
                this.fromIsolatedNodeStub, this.toIsolatedNodeStub);

        this.tested.addNetwork(otherNetworkMock, this.otherNetworkNameStub);
        this.tested.addNode(otherNetworkMock, OTHER_FROM_NODE_X,
                OTHER_FROM_NODE_Y);
        this.tested.addNode(otherNetworkMock, OTHER_TO_NODE_X, OTHER_TO_NODE_Y);
        this.tested.addArc(otherNetworkMock, this.referringArcNameDummy,
                this.otherNetworkOtherFromIsolatedNodeStub,
                this.otherNetworkOtherToIsolatedNodeStub);
        this.tested.changeArc(this.otherNetworkTemporaryArcStub,
                this.referringArcNameDummy, this.priorityDummy,
                RecurentArc.class, this.emptyCodeDummy,
                this.referredFromNodeStub);

        this.tested.removeNetwork(otherNetworkMock);
        this.tested.removeNetwork(networkMock);

        EasyMock.verify(addAffectedUpdateStub);
        EasyMock.verify(addTemporaryUpdateStub);
        PowerMock.verify(NormalWords.class);
        verifyTested();
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.DefaultSystem#removeNetwork(cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network)}
     * a další.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemoveNetworkWhenDependingArcsFromOtherNetwork() {
        EasyMock.expect(this.statesNamingAuthority.generate()).andReturn(
                FROM_NODE_NAME);
        EasyMock.expect(this.statesNamingAuthority.generate()).andReturn(
                TO_NODE_NAME);
        EasyMock.expect(this.statesNamingAuthority.use(AFFECTED_ARC_NAME))
                .andReturn(AFFECTED_ARC_NAME);
        EasyMock.expect(this.statesNamingAuthority.generate()).andReturn(
                FROM_NODE_NAME);
        EasyMock.expect(this.statesNamingAuthority.generate()).andReturn(
                TO_NODE_NAME);
        EasyMock.expect(this.statesNamingAuthority.use(REFERRING_ARC_NAME))
                .andReturn(REFERRING_ARC_NAME);
        EasyMock.expect(
                this.statesNamingAuthority.replace(REFERRING_ARC_NAME,
                        REFERRING_ARC_NAME)).andReturn(REFERRING_ARC_NAME);

        PowerMock.mockStatic(NormalWords.class);
        EasyMock.expect(NormalWords.of(FROM_NODE_NAME)).andReturn(
                this.fromNodeNameStub);
        EasyMock.expect(NormalWords.of(TO_NODE_NAME)).andReturn(
                this.toNodeNameStub);
        EasyMock.expect(NormalWords.of(FROM_NODE_NAME)).andReturn(
                this.otherFromNodeNameStub);
        EasyMock.expect(NormalWords.of(TO_NODE_NAME)).andReturn(
                this.otherToNodeNameStub);
        EasyMock.expect(NormalWords.of(AFFECTED_ARC_NAME)).andReturn(
                this.affectedArcNameDummy);
        EasyMock.expect(NormalWords.of(REFERRING_ARC_NAME)).andReturn(
                this.referringArcNameDummy);
        EasyMock.expect(NormalWords.of(REFERRING_ARC_NAME)).andReturn(
                this.referringArcNameDummy);
        PowerMock.replay(NormalWords.class);

        EasyMock.expect(
                this.initialNodeFactory.produce(this.fromNodeNameStub,
                        this.networkStub, FROM_NODE_X, FROM_NODE_Y)).andReturn(
                this.fromIsolatedNodeStub);
        EasyMock.expect(
                this.initialNodeFactory.produce(this.toNodeNameStub,
                        this.networkStub, TO_NODE_X, TO_NODE_Y)).andReturn(
                this.toIsolatedNodeStub);
        EasyMock.expect(
                this.initialNodeFactory.produce(this.otherFromNodeNameStub,
                        this.otherNetworkStub, OTHER_FROM_NODE_X,
                        OTHER_FROM_NODE_Y)).andReturn(
                this.otherNetworkOtherFromIsolatedNodeStub);
        EasyMock.expect(
                this.initialNodeFactory
                        .produce(this.otherToNodeNameStub,
                                this.otherNetworkStub, OTHER_TO_NODE_X,
                                OTHER_TO_NODE_Y)).andReturn(
                this.otherNetworkOtherToIsolatedNodeStub);

        EasyMock.expect(
                this.initialArcFactory.produce(this.networkStub,
                        this.affectedArcNameDummy)).andReturn(
                this.affectedArcStub);
        EasyMock.expect(
                this.initialArcFactory.produce(this.otherNetworkStub,
                        this.referringArcNameDummy)).andReturn(
                this.otherNetworkTemporaryArcStub);

        final Update addAffectedUpdateStub =
                EasyMock.createStrictMock(Update.class);
        EasyMock.expect(addAffectedUpdateStub.getInitialsAdded())
                .andStubReturn(ImmutableSet.of(this.referredFromNodeStub));
        EasyMock.expect(addAffectedUpdateStub.getInitialsRemoved())
                .andStubReturn(ImmutableSet.<EnterNode> of());
        EasyMock.expect(addAffectedUpdateStub.getSwitched()).andStubReturn(
                ImmutableMap.<Node, Node> of(this.fromIsolatedNodeStub,
                        this.referredFromNodeStub, this.toIsolatedNodeStub,
                        this.toExitNodeStub));
        EasyMock.replay(addAffectedUpdateStub);

        final Update addTemporaryUpdateStub =
                EasyMock.createStrictMock(Update.class);
        EasyMock.expect(addTemporaryUpdateStub.getInitialsAdded())
                .andStubReturn(
                        ImmutableSet
                                .of(this.otherNetworkOtherFromEnterNodeStub));
        EasyMock.expect(addTemporaryUpdateStub.getInitialsRemoved())
                .andStubReturn(ImmutableSet.<EnterNode> of());
        EasyMock.expect(addTemporaryUpdateStub.getSwitched()).andStubReturn(
                ImmutableMap.<Node, Node> of(
                        this.otherNetworkOtherFromIsolatedNodeStub,
                        this.otherNetworkOtherFromEnterNodeStub,
                        this.otherNetworkOtherToIsolatedNodeStub,
                        this.otherNetworkOtherToExitNodeStub));
        EasyMock.replay(addTemporaryUpdateStub);

        this.graph.add(this.fromIsolatedNodeStub);
        this.graph.add(this.toIsolatedNodeStub);
        EasyMock.expect(
                this.graph.addAndRealign(this.affectedArcStub,
                        this.fromIsolatedNodeStub, this.toIsolatedNodeStub,
                        this.realignmentProcessor,
                        ImmutableSet.<EnterNode> of())).andReturn(
                addAffectedUpdateStub);
        this.graph.add(this.otherNetworkOtherFromIsolatedNodeStub);
        this.graph.add(this.otherNetworkOtherToIsolatedNodeStub);
        EasyMock.expect(
                this.graph.addAndRealign(this.otherNetworkTemporaryArcStub,
                        this.otherNetworkOtherFromIsolatedNodeStub,
                        this.otherNetworkOtherToIsolatedNodeStub,
                        this.realignmentProcessor,
                        ImmutableSet.<EnterNode> of())).andReturn(
                addTemporaryUpdateStub);
        EasyMock.expect(
                this.graph.containsEdge(this.otherNetworkTemporaryArcStub))
                .andReturn(true);
        EasyMock.expect(this.graph.containsVertex(this.referredFromNodeStub))
                .andReturn(true);
        this.graph.replaceEdge(this.otherNetworkTemporaryArcStub,
                this.otherNetworkReferringArcStub);

        EasyMock.expect(
                this.arcModifier.change(this.otherNetworkTemporaryArcStub,
                        this.referringArcNameDummy, this.priorityDummy,
                        RecurentArc.class, this.emptyCodeDummy,
                        this.referredFromNodeStub)).andStubReturn(
                this.otherNetworkReferringArcStub);

        this.dispatcher.fire(EasyMock.notNull(NetworkAddedEvent.class));
        this.dispatcher.fire(EasyMock.notNull(NodeAddedEvent.class));
        this.dispatcher.fire(EasyMock.notNull(NodeAddedEvent.class));
        EasyMock.checkOrder(this.dispatcher, false);
        this.dispatcher.fire(EasyMock.notNull(NodeTypeChangedEvent.class));
        this.dispatcher.fire(EasyMock.notNull(NodeTypeChangedEvent.class));
        this.dispatcher.fire(EasyMock.notNull(ArcAddedEvent.class));
        this.dispatcher.fire(EasyMock
                .notNull(AvailableReferencesExtendedEvent.class));
        EasyMock.checkOrder(this.dispatcher, true);
        this.dispatcher.fire(EasyMock.notNull(NetworkAddedEvent.class));
        this.dispatcher.fire(EasyMock.notNull(NodeAddedEvent.class));
        this.dispatcher.fire(EasyMock.notNull(NodeAddedEvent.class));
        EasyMock.checkOrder(this.dispatcher, false);
        this.dispatcher.fire(EasyMock.notNull(NodeTypeChangedEvent.class));
        this.dispatcher.fire(EasyMock.notNull(NodeTypeChangedEvent.class));
        this.dispatcher.fire(EasyMock.notNull(ArcAddedEvent.class));
        this.dispatcher.fire(EasyMock
                .notNull(AvailableReferencesExtendedEvent.class));
        EasyMock.checkOrder(this.dispatcher, true);
        EasyMock.checkOrder(this.dispatcher, false);
        this.dispatcher.fire(EasyMock.notNull(ArcChangedEvent.class));
        this.dispatcher.fire(EasyMock.notNull(ArcRetypedEvent.class));
        EasyMock.checkOrder(this.dispatcher, true);

        replayTested();

        this.tested.addNetwork(this.networkStub, this.networkNameStub);
        this.tested.addNode(this.networkStub, FROM_NODE_X, FROM_NODE_Y);
        this.tested.addNode(this.networkStub, TO_NODE_X, TO_NODE_Y);
        this.tested.addArc(this.networkStub, this.affectedArcNameDummy,
                this.fromIsolatedNodeStub, this.toIsolatedNodeStub);

        this.tested.addNetwork(this.otherNetworkStub,
                this.otherNetworkNameStub);
        this.tested.addNode(this.otherNetworkStub, OTHER_FROM_NODE_X,
                OTHER_FROM_NODE_Y);
        this.tested.addNode(this.otherNetworkStub, OTHER_TO_NODE_X,
                OTHER_TO_NODE_Y);
        this.tested.addArc(this.otherNetworkStub, this.referringArcNameDummy,
                this.otherNetworkOtherFromIsolatedNodeStub,
                this.otherNetworkOtherToIsolatedNodeStub);
        this.tested.changeArc(this.otherNetworkTemporaryArcStub,
                this.referringArcNameDummy, this.priorityDummy,
                RecurentArc.class, this.emptyCodeDummy,
                this.referredFromNodeStub);

        try {
            this.tested.removeNetwork(this.networkStub);
        } finally {
            EasyMock.verify(addAffectedUpdateStub);
            EasyMock.verify(addTemporaryUpdateStub);
            PowerMock.verify(NormalWords.class);
            verifyTested();
        }
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.DefaultSystem#removeNetwork(cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network)}
     * .
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemoveNetworkWhenNotPresent() {
        replayTested();

        try {
            this.tested.removeNetwork(this.networkStub);
        } finally {
            verifyTested();
        }
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.DefaultSystem#removeNode(cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node)}
     * .
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemoveNodeWhenReferredByArc() {
        EasyMock.expect(this.statesNamingAuthority.generate()).andReturn(
                OTHER_FROM_NODE_NAME);
        EasyMock.expect(this.statesNamingAuthority.generate()).andReturn(
                OTHER_TO_NODE_NAME);
        EasyMock.expect(this.statesNamingAuthority.use(REFERRING_ARC_NAME))
                .andReturn(REFERRING_ARC_NAME);
        EasyMock.expect(
                this.statesNamingAuthority.replace(REFERRING_ARC_NAME,
                        REFERRING_ARC_NAME)).andReturn(REFERRING_ARC_NAME);

        PowerMock.mockStatic(NormalWords.class);
        EasyMock.expect(NormalWords.of(OTHER_FROM_NODE_NAME)).andReturn(
                this.fromNodeNameStub);
        EasyMock.expect(NormalWords.of(OTHER_TO_NODE_NAME)).andReturn(
                this.toNodeNameStub);
        EasyMock.expect(NormalWords.of(REFERRING_ARC_NAME)).andReturn(
                this.referringArcNameDummy);
        EasyMock.expect(NormalWords.of(REFERRING_ARC_NAME)).andReturn(
                this.referringArcNameDummy);
        PowerMock.replay(NormalWords.class);

        EasyMock.expect(
                this.initialNodeFactory.produce(this.fromNodeNameStub,
                        this.networkStub, OTHER_FROM_NODE_X, OTHER_FROM_NODE_Y))
                .andReturn(this.fromIsolatedNodeStub);
        EasyMock.expect(
                this.initialNodeFactory.produce(this.toNodeNameStub,
                        this.networkStub, OTHER_TO_NODE_X, OTHER_TO_NODE_Y))
                .andReturn(this.toIsolatedNodeStub);

        EasyMock.expect(
                this.initialArcFactory.produce(this.networkStub,
                        this.referringArcNameDummy)).andReturn(
                this.thisTemporaryArcStub);

        final Update addTemporaryUpdateStub =
                EasyMock.createStrictMock(Update.class);
        EasyMock.expect(addTemporaryUpdateStub.getInitialsAdded())
                .andStubReturn(
                        ImmutableSet.<EnterNode> of(this.referredFromNodeStub));
        EasyMock.expect(addTemporaryUpdateStub.getInitialsRemoved())
                .andStubReturn(ImmutableSet.<EnterNode> of());
        EasyMock.expect(addTemporaryUpdateStub.getSwitched()).andStubReturn(
                ImmutableMap.<Node, Node> of(this.fromIsolatedNodeStub,
                        this.referredFromNodeStub, this.toIsolatedNodeStub,
                        this.toExitNodeStub));
        EasyMock.replay(addTemporaryUpdateStub);

        this.graph.add(this.fromIsolatedNodeStub);
        this.graph.add(this.toIsolatedNodeStub);
        EasyMock.expect(
                this.graph.addAndRealign(this.thisTemporaryArcStub,
                        this.fromIsolatedNodeStub, this.toIsolatedNodeStub,
                        this.realignmentProcessor,
                        ImmutableSet.<EnterNode> of())).andReturn(
                addTemporaryUpdateStub);
        EasyMock.expect(this.graph.containsEdge(this.thisTemporaryArcStub))
                .andReturn(true);
        EasyMock.expect(this.graph.containsVertex(this.referredFromNodeStub))
                .andReturn(true);
        this.graph.replaceEdge(this.thisTemporaryArcStub,
                this.thisNetworkReferringArcStub);
        EasyMock.expect(this.graph.containsVertex(this.referredFromNodeStub))
                .andReturn(true);

        EasyMock.expect(
                this.arcModifier.change(this.thisTemporaryArcStub,
                        this.referringArcNameDummy, this.priorityDummy,
                        RecurentArc.class, this.emptyCodeDummy,
                        this.referredFromNodeStub)).andStubReturn(
                this.thisNetworkReferringArcStub);

        this.dispatcher.fire(EasyMock.notNull(NetworkAddedEvent.class));
        this.dispatcher.fire(EasyMock.notNull(NodeAddedEvent.class));
        this.dispatcher.fire(EasyMock.notNull(NodeAddedEvent.class));
        EasyMock.checkOrder(this.dispatcher, false);
        this.dispatcher.fire(EasyMock.notNull(NodeTypeChangedEvent.class));
        this.dispatcher.fire(EasyMock.notNull(NodeTypeChangedEvent.class));
        this.dispatcher.fire(EasyMock.notNull(ArcAddedEvent.class));
        this.dispatcher.fire(EasyMock
                .notNull(AvailableReferencesExtendedEvent.class));
        EasyMock.checkOrder(this.dispatcher, true);
        EasyMock.checkOrder(this.dispatcher, false);
        this.dispatcher.fire(EasyMock.notNull(ArcChangedEvent.class));
        this.dispatcher.fire(EasyMock.notNull(ArcRetypedEvent.class));
        EasyMock.checkOrder(this.dispatcher, true);

        replayTested();

        this.tested.addNetwork(this.networkStub, this.otherNetworkNameStub);
        this.tested.addNode(this.networkStub, OTHER_FROM_NODE_X,
                OTHER_FROM_NODE_Y);
        this.tested.addNode(this.networkStub, OTHER_TO_NODE_X, OTHER_TO_NODE_Y);
        this.tested.addArc(this.networkStub, this.referringArcNameDummy,
                this.fromIsolatedNodeStub, this.toIsolatedNodeStub);
        this.tested.changeArc(this.thisTemporaryArcStub,
                this.referringArcNameDummy, this.priorityDummy,
                RecurentArc.class, this.emptyCodeDummy,
                this.referredFromNodeStub);

        try {
            this.tested.removeNode(this.referredFromNodeStub);
        } finally {
            EasyMock.verify(addTemporaryUpdateStub);
            PowerMock.verify(NormalWords.class);
            verifyTested();
        }
    }

    private void verifyTested() {
        EasyMock.verify(this.graph, this.statesNamingAuthority,
                this.variablesNamingAuthority, this.nodeModifier,
                this.arcModifier, this.realignmentProcessor,
                this.systemVisitorFactory, this.initialNodeFactory,
                this.initialArcFactory, this.dispatcher, this.networkStub,
                this.otherNetworkStub, this.affectedArcNameDummy,
                this.fromNodeNameStub, this.toNodeNameStub,
                this.referringArcNameDummy, this.otherFromNodeNameStub,
                this.otherToNodeNameStub, this.referredFromNodeStub,
                this.emptyCodeDummy, this.fromIsolatedNodeStub,
                this.toIsolatedNodeStub, this.otherFromIsolatedNodeStub,
                this.otherToIsolatedNodeStub,
                this.otherNetworkOtherFromIsolatedNodeStub,
                this.otherNetworkOtherToIsolatedNodeStub, this.toExitNodeStub,
                this.otherFromEnterNodeStub, this.otherToExitNodeStub,
                this.otherNetworkOtherFromEnterNodeStub,
                this.otherNetworkOtherToExitNodeStub);
        PowerMock.verify(this.affectedArcStub, this.priorityDummy,
                this.thisTemporaryArcStub, this.otherNetworkTemporaryArcStub,
                this.otherNetworkReferringArcStub,
                this.thisNetworkReferringArcStub, this.otherNetworkNameStub,
                this.networkNameStub, this.someNetworkNameStub,
                this.systemNameStub, this.reservedNetworkNameStub);
    }
}
