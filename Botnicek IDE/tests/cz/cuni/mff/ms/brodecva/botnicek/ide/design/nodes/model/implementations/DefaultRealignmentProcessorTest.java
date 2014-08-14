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
 * Testuje výchozí implementace procesoru pro opravu typu uzlů dle umístění v grafu po změně.
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
     * @throws java.lang.Exception pokud dojde k vyhození výjimky
     */
    @Before
    public void setUp() throws Exception {
        inputNodeStub = EasyMock.createMock(Node.class);
        
        outputNodeDummy = EasyMock.createStrictMock(Node.class);
        EasyMock.replay(outputNodeDummy);
        
        nodeModifierMock = EasyMock.createStrictMock(NodeModifier.class);
    }

    /**
     * Uklidí testovací objekty.
     * 
     * @throws java.lang.Exception pokud dojde k vyhození výjimky
     */
    @After
    public void tearDown() throws Exception {
        outputNodeDummy = Intended.nullReference();
        inputNodeStub = Intended.nullReference();
    }

    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.implementations.DefaultRealignmentProcessor#create(cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.NodeModifier)}.
     */
    @Test
    public void testCreate() {
        final NodeModifier nodeModifierDummy = EasyMock.createStrictMock(NodeModifier.class);
        EasyMock.replay(nodeModifierDummy);
        
        DefaultRealignmentProcessor.create(nodeModifierDummy);
        
        EasyMock.verify(nodeModifierDummy);
    }

    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.implementations.DefaultRealignmentProcessor#realign(cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node)}.
     */
    @Test
    public void testRealignWhenNoConnectionsChangesToIsolatedNode() {
        EasyMock.expect(inputNodeStub.getInDegree()).andStubReturn(0);
        EasyMock.expect(inputNodeStub.getOutDegree()).andStubReturn(0);
        EasyMock.replay(inputNodeStub);
        
        EasyMock.expect(nodeModifierMock.change(inputNodeStub, IsolatedNode.class)).andReturn(outputNodeDummy);
        EasyMock.replay(nodeModifierMock);
        
        DefaultRealignmentProcessor.create(nodeModifierMock).realign(inputNodeStub);
        
        EasyMock.verify(inputNodeStub);
        EasyMock.verify(outputNodeDummy);
        EasyMock.verify(nodeModifierMock);
    }
    
    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.implementations.DefaultRealignmentProcessor#realign(cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node)}.
     */
    @Test
    public void testRealignWhenNoInboundConnectionsChangesToEnterNode() {
        EasyMock.expect(inputNodeStub.getInDegree()).andStubReturn(0);
        EasyMock.expect(inputNodeStub.getOutDegree()).andStubReturn(1);
        EasyMock.replay(inputNodeStub);
        
        EasyMock.expect(nodeModifierMock.change(inputNodeStub, EnterNode.class)).andReturn(outputNodeDummy);
        EasyMock.replay(nodeModifierMock);
        
        DefaultRealignmentProcessor.create(nodeModifierMock).realign(inputNodeStub);
        
        EasyMock.verify(inputNodeStub);
        EasyMock.verify(outputNodeDummy);
        EasyMock.verify(nodeModifierMock);
    }
    
    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.implementations.DefaultRealignmentProcessor#realign(cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node)}.
     */
    @Test
    public void testRealignWhenNoOutboundConnectionsChangesToExitNode() {
        EasyMock.expect(inputNodeStub.getInDegree()).andStubReturn(1);
        EasyMock.expect(inputNodeStub.getOutDegree()).andStubReturn(0);
        EasyMock.replay(inputNodeStub);
        
        EasyMock.expect(nodeModifierMock.change(inputNodeStub, ExitNode.class)).andReturn(outputNodeDummy);
        EasyMock.replay(nodeModifierMock);
        
        DefaultRealignmentProcessor.create(nodeModifierMock).realign(inputNodeStub);
        
        EasyMock.verify(inputNodeStub);
        EasyMock.verify(outputNodeDummy);
        EasyMock.verify(nodeModifierMock);
    }
    
    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.implementations.DefaultRealignmentProcessor#realign(cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node)}.
     */
    @Test
    public void testRealignWhenSomeConnectionsInBothDirectionsChangesToInnerNode() {
        EasyMock.expect(inputNodeStub.getInDegree()).andStubReturn(1);
        EasyMock.expect(inputNodeStub.getOutDegree()).andStubReturn(1);
        EasyMock.replay(inputNodeStub);
        
        EasyMock.expect(nodeModifierMock.change(inputNodeStub, InnerNode.class)).andReturn(outputNodeDummy);
        EasyMock.replay(nodeModifierMock);
        
        DefaultRealignmentProcessor.create(nodeModifierMock).realign(inputNodeStub);
        
        EasyMock.verify(inputNodeStub);
        EasyMock.verify(outputNodeDummy);
        EasyMock.verify(nodeModifierMock);
    }

}
