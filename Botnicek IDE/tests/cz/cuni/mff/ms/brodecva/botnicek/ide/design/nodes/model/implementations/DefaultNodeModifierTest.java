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

import static org.junit.Assert.*;

import java.util.Set;

import org.easymock.EasyMock;
import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableTable;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.DispatchProcessor;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.ProceedProcessor;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.StackProcessor;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.Visitor;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.Direction;

/**
 * Testuje výchozí implementaci modifikátoru.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see DefaultNodeModifier
 */
public class DefaultNodeModifierTest {

    /**
     * Testovací abstraktní třída pahýlu uzlu, jež při zavolání jakékoli metody (všechny jsou neočekávané) vyhodí {@link IllegalStateException}.
     * 
     * @author Václav Brodec
     * @version 1.0
     */
    static abstract class AbstractNodeStub implements Node {

        /* (non-Javadoc)
         * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.Visitable#accept(cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.Visitor)
         */
        @Override
        public void accept(Visitor visitor) {
            throw new IllegalStateException();
        }

        /* (non-Javadoc)
         * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.DispatchProcessible#accept(cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.DispatchProcessor)
         */
        @Override
        public <T> T accept(DispatchProcessor<T> processor) {
            throw new IllegalStateException();
        }

        /* (non-Javadoc)
         * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.ProceedProcessible#accept(cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.ProceedProcessor)
         */
        @Override
        public <T> T accept(ProceedProcessor<T> processor) {
            throw new IllegalStateException();
        }

        /* (non-Javadoc)
         * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.StackProcessible#accept(cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.StackProcessor)
         */
        @Override
        public <T> T accept(StackProcessor<T> processor) {
            throw new IllegalStateException();
        }

        /* (non-Javadoc)
         * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node#getName()
         */
        @Override
        public NormalWord getName() {
            throw new IllegalStateException();
        }

        /* (non-Javadoc)
         * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node#getX()
         */
        @Override
        public int getX() {
            throw new IllegalStateException();
        }

        /* (non-Javadoc)
         * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node#getY()
         */
        @Override
        public int getY() {
            throw new IllegalStateException();
        }

        /* (non-Javadoc)
         * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node#getConnections(cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.Direction)
         */
        @Override
        public Set<Arc> getConnections(Direction direction) {
            throw new IllegalStateException();
        }

        /* (non-Javadoc)
         * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node#getOuts()
         */
        @Override
        public Set<Arc> getOuts() {
            throw new IllegalStateException();
        }

        /* (non-Javadoc)
         * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node#getIns()
         */
        @Override
        public Set<Arc> getIns() {
            throw new IllegalStateException();
        }

        /* (non-Javadoc)
         * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node#adjoins(cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node, cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.Direction)
         */
        @Override
        public boolean adjoins(Node node, Direction direction) {
            throw new IllegalStateException();
        }

        /* (non-Javadoc)
         * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node#pointsTo(cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node)
         */
        @Override
        public boolean pointsTo(Node node) {
            throw new IllegalStateException();
        }

        /* (non-Javadoc)
         * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node#isPointedAtBy(cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node)
         */
        @Override
        public boolean isPointedAtBy(Node node) {
            throw new IllegalStateException();
        }

        /* (non-Javadoc)
         * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node#getDegree(cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.Direction)
         */
        @Override
        public int getDegree(Direction direction) {
            throw new IllegalStateException();
        }

        /* (non-Javadoc)
         * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node#getOutDegree()
         */
        @Override
        public int getOutDegree() {
            throw new IllegalStateException();
        }

        /* (non-Javadoc)
         * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node#getInDegree()
         */
        @Override
        public int getInDegree() {
            throw new IllegalStateException();
        }

        /* (non-Javadoc)
         * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node#getNetwork()
         */
        @Override
        public Network getNetwork() {
            throw new IllegalStateException();
        }
    }
    
    /**
     * Testovací abstraktní třída pahýlu uzlu, který implementuje metody vracející hodnoty užité při vytváření objektu.
     * 
     * @author Václav Brodec
     * @version 1.0
     */
    static class AbstractFilledNodeStub extends AbstractNodeStub {
        
        private final NormalWord name;
        private final Network network;
        private final int x;
        private final int y;
        
        /**
         * Vytvoří uzel dle parametrů.
         * 
         * @param name název uzlu
         * @param network rodičovská síť
         * @param x umístění uzlu v souřadnici x
         * @param y umístění uzlu v souřadnici y
         */
        protected AbstractFilledNodeStub(final NormalWord name, final Network network, final int x, final int y) {
            Preconditions.checkNotNull(name);
            Preconditions.checkNotNull(network);        
            Preconditions.checkArgument(x >= 0);
            Preconditions.checkArgument(y >= 0);
            
            this.name = name;
            this.network = network;
            
            this.x = x;
            this.y = y;
        }

        /* (non-Javadoc)
         * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.implementations.DefaultNodeModifierTest.AbstractNodeStub#getName()
         */
        public NormalWord getName() {
            return name;
        }

        /* (non-Javadoc)
         * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.implementations.DefaultNodeModifierTest.AbstractNodeStub#getNetwork()
         */
        public Network getNetwork() {
            return network;
        }

        /* (non-Javadoc)
         * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.implementations.DefaultNodeModifierTest.AbstractNodeStub#getX()
         */
        public int getX() {
            return x;
        }

        /* (non-Javadoc)
         * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.implementations.DefaultNodeModifierTest.AbstractNodeStub#getY()
         */
        public int getY() {
            return y;
        }

        /* (non-Javadoc)
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + name.hashCode();
            result =
                    prime * result
                            + network.hashCode();
            result = prime * result + x;
            result = prime * result + y;
            return result;
        }

        /* (non-Javadoc)
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final AbstractFilledNodeStub other = (AbstractFilledNodeStub) obj;
            if (!name.equals(other.name)) {
                return false;
            }
            if (!network.equals(other.network)) {
                return false;
            }
            if (x != other.x) {
                return false;
            }
            if (y != other.y) {
                return false;
            }
            return true;
        }
    }
    
    /**
     * Testovací třída uzlu, který je modifikován.
     * 
     * @author Václav Brodec
     * @version 1.0
     */
    static final class NodeInputStub extends AbstractFilledNodeStub {

        /**
         * Vytvoří uzel dle parametrů.
         * 
         * @param name název uzlu
         * @param network rodičovská síť
         * @param x umístění uzlu v souřadnici x
         * @param y umístění uzlu v souřadnici y
         * @return uzel
         */
        public static NodeInputStub create(NormalWord name, Network network, int x, int y) {
            return new NodeInputStub(name, network, x, y);
        }
        
        /**
         * Vytvoří uzel dle parametrů.
         * 
         * @param name název uzlu
         * @param network rodičovská síť
         * @param x umístění uzlu v souřadnici x
         * @param y umístění uzlu v souřadnici y
         */
        private NodeInputStub(NormalWord name, Network network, int x, int y) {
            super(name, network, x, y);
        }
    }

    /**
     * Testovací třída, která je parametrem modifikace.
     * 
     * @author Václav Brodec
     * @version 1.0
     */
    static final class NodeFactorStub extends AbstractNodeStub {
    }
    
    /**
     * Testovací třída, která je typem výsledku modifikace.
     * 
     * @author Václav Brodec
     * @version 1.0
     */
    static final class NodeOutputStub extends AbstractFilledNodeStub {
        /**
         * Vytvoří uzel dle parametrů.
         * 
         * @param name název uzlu
         * @param network rodičovská síť
         * @param x umístění uzlu v souřadnici x
         * @param y umístění uzlu v souřadnici y
         * @return uzel
         */
        public static NodeOutputStub create(NormalWord name, Network network, int x, int y) {
            return new NodeOutputStub(name, network, x, y);
        }
        
        /**
         * Vytvoří uzel dle parametrů.
         * 
         * @param name název uzlu
         * @param network rodičovská síť
         * @param x umístění uzlu v souřadnici x
         * @param y umístění uzlu v souřadnici y
         */
        public NodeOutputStub(NormalWord name, Network network, int x, int y) {
            super(name, network, x, y);
        }
    }

    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.implementations.DefaultNodeModifier#create(java.util.Map)}.
     */
    @Test
    public
            void
            testCreateMapOfClassOfQextendsNodeMapOfClassOfQextendsNodeClassOfQextendsNode() {
        DefaultNodeModifier.create(ImmutableTable.<Class<? extends Node>, Class<? extends Node>, Class<? extends Node>>of(NodeInputStub.class, NodeFactorStub.class, NodeOutputStub.class).rowMap());
    }

    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.implementations.DefaultNodeModifier#change(cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node, cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord, int, int, java.lang.Class)}.
     */
    @Test
    public void testChangeNodeNormalWordIntIntClassOfQextendsNode() {
        final NormalWord oldNameDummy = EasyMock.createMock(NormalWord.class);
        EasyMock.replay(oldNameDummy);
        
        final NormalWord newNameDummy = EasyMock.createMock(NormalWord.class);
        EasyMock.replay(newNameDummy);
        
        final Network networkDummy = EasyMock.createMock(Network.class);
        EasyMock.replay(networkDummy);
        
        final int oldX = 13;
        final int oldY = 38;
        
        final int x = 19;
        final int y = 77;
        
        final Node nodeInputStub = NodeInputStub.create(oldNameDummy, networkDummy, oldX, oldY);
        
        final DefaultNodeModifier tested = DefaultNodeModifier.create(ImmutableTable.<Class<? extends Node>, Class<? extends Node>, Class<? extends Node>>of(NodeInputStub.class, NodeFactorStub.class, NodeOutputStub.class).rowMap());
        
        final Node prototype = NodeOutputStub.create(newNameDummy, networkDummy, x, y);
        assertEquals(prototype, tested.change(nodeInputStub, newNameDummy, x, y, NodeFactorStub.class));
    }

}
