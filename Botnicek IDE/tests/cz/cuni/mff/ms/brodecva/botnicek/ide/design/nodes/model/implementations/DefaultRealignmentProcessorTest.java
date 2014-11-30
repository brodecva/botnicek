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
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.implementations;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.EnterNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.ExitNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.InnerNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.IsolatedNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.NodeModifier;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Intended;

/**
 * Testuje výchozí implementace procesoru pro opravu typu uzlů dle umístění v
 * grafu po změně.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see DefaultRealignmentProcessor
 */
public class DefaultRealignmentProcessorTest {

    private Node outputNodeDummy = Intended.nullReference();
    private Node inputNodeStub = Intended.nullReference();
    private NodeModifier nodeModifierMock = Intended.nullReference();

    /**
     * Inicializuje testovací objekty.
     * 
     * @throws java.lang.Exception
     *             pokud dojde k vyhození výjimky
     */
    @Before
    public void setUp() throws Exception {
        this.inputNodeStub = EasyMock.createMock(Node.class);

        this.outputNodeDummy = EasyMock.createStrictMock(Node.class);
        EasyMock.replay(this.outputNodeDummy);

        this.nodeModifierMock = EasyMock.createStrictMock(NodeModifier.class);
    }

    /**
     * Uklidí testovací objekty.
     * 
     * @throws java.lang.Exception
     *             pokud dojde k vyhození výjimky
     */
    @After
    public void tearDown() throws Exception {
        this.outputNodeDummy = Intended.nullReference();
        this.inputNodeStub = Intended.nullReference();
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.implementations.DefaultRealignmentProcessor#create(cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.NodeModifier)}
     * .
     */
    @Test
    public void testCreate() {
        final NodeModifier nodeModifierDummy =
                EasyMock.createStrictMock(NodeModifier.class);
        EasyMock.replay(nodeModifierDummy);

        DefaultRealignmentProcessor.create(nodeModifierDummy);

        EasyMock.verify(nodeModifierDummy);
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.implementations.DefaultRealignmentProcessor#realign(cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node)}
     * .
     */
    @Test
    public void testRealignWhenNoConnectionsChangesToIsolatedNode() {
        EasyMock.expect(this.inputNodeStub.getInDegree()).andStubReturn(0);
        EasyMock.expect(this.inputNodeStub.getOutDegree()).andStubReturn(0);
        EasyMock.replay(this.inputNodeStub);

        EasyMock.expect(
                this.nodeModifierMock.change(this.inputNodeStub,
                        IsolatedNode.class)).andReturn(this.outputNodeDummy);
        EasyMock.replay(this.nodeModifierMock);

        DefaultRealignmentProcessor.create(this.nodeModifierMock).realign(
                this.inputNodeStub);

        EasyMock.verify(this.inputNodeStub);
        EasyMock.verify(this.outputNodeDummy);
        EasyMock.verify(this.nodeModifierMock);
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.implementations.DefaultRealignmentProcessor#realign(cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node)}
     * .
     */
    @Test
    public void testRealignWhenNoInboundConnectionsChangesToEnterNode() {
        EasyMock.expect(this.inputNodeStub.getInDegree()).andStubReturn(0);
        EasyMock.expect(this.inputNodeStub.getOutDegree()).andStubReturn(1);
        EasyMock.replay(this.inputNodeStub);

        EasyMock.expect(
                this.nodeModifierMock.change(this.inputNodeStub,
                        EnterNode.class)).andReturn(this.outputNodeDummy);
        EasyMock.replay(this.nodeModifierMock);

        DefaultRealignmentProcessor.create(this.nodeModifierMock).realign(
                this.inputNodeStub);

        EasyMock.verify(this.inputNodeStub);
        EasyMock.verify(this.outputNodeDummy);
        EasyMock.verify(this.nodeModifierMock);
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.implementations.DefaultRealignmentProcessor#realign(cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node)}
     * .
     */
    @Test
    public void testRealignWhenNoOutboundConnectionsChangesToExitNode() {
        EasyMock.expect(this.inputNodeStub.getInDegree()).andStubReturn(1);
        EasyMock.expect(this.inputNodeStub.getOutDegree()).andStubReturn(0);
        EasyMock.replay(this.inputNodeStub);

        EasyMock.expect(
                this.nodeModifierMock
                        .change(this.inputNodeStub, ExitNode.class)).andReturn(
                this.outputNodeDummy);
        EasyMock.replay(this.nodeModifierMock);

        DefaultRealignmentProcessor.create(this.nodeModifierMock).realign(
                this.inputNodeStub);

        EasyMock.verify(this.inputNodeStub);
        EasyMock.verify(this.outputNodeDummy);
        EasyMock.verify(this.nodeModifierMock);
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.implementations.DefaultRealignmentProcessor#realign(cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node)}
     * .
     */
    @Test
    public void
            testRealignWhenSomeConnectionsInBothDirectionsChangesToInnerNode() {
        EasyMock.expect(this.inputNodeStub.getInDegree()).andStubReturn(1);
        EasyMock.expect(this.inputNodeStub.getOutDegree()).andStubReturn(1);
        EasyMock.replay(this.inputNodeStub);

        EasyMock.expect(
                this.nodeModifierMock.change(this.inputNodeStub,
                        InnerNode.class)).andReturn(this.outputNodeDummy);
        EasyMock.replay(this.nodeModifierMock);

        DefaultRealignmentProcessor.create(this.nodeModifierMock).realign(
                this.inputNodeStub);

        EasyMock.verify(this.inputNodeStub);
        EasyMock.verify(this.outputNodeDummy);
        EasyMock.verify(this.nodeModifierMock);
    }

}
