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
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model;

import static org.junit.Assert.assertEquals;

import org.easymock.EasyMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.Visitor;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.api.Processor;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.types.Priority;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.Direction;

/**
 * Testuje výchozí implementaci modifikátoru hran využívá reflexi ke konstrukci
 * hrany nové.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see DefaultArcModifier
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(Priority.class)
public class DefaultArcModifierTest {

    /**
     * Testovací implementace hrany.
     * 
     * @author Václav Brodec
     * @version 1.0
     */
    static final class ArcStub implements Arc {

        /**
         * Vytvoří pahýl hrany.
         * 
         * @param network
         *            rodičovská síť
         * @param name
         *            název
         * @param priority
         *            priorita
         * @param first
         *            první přídatný parametr
         * @param second
         *            druhý přídatný parametr
         * @param third
         *            třetí přídatný parametr
         * @return pahýl hrany
         */
        public static ArcStub create(final Network network,
                final NormalWord name, final Priority priority,
                final String first, final Object second, final int third) {
            Preconditions.checkNotNull(network);
            Preconditions.checkNotNull(name);
            Preconditions.checkNotNull(priority);
            Preconditions.checkNotNull(first);
            Preconditions.checkNotNull(second);
            Preconditions.checkNotNull(third);

            return new ArcStub(network, name, priority, first, second, third);
        }

        private final Network network;
        private final NormalWord name;
        private final Priority priority;
        private final String first;
        private final Object second;

        private final int third;

        private ArcStub(final Network network, final NormalWord name,
                final Priority priority, final String first,
                final Object second, final int third) {
            this.network = network;
            this.name = name;
            this.priority = priority;
            this.first = first;
            this.second = second;
            this.third = third;
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.api.Processible
         * #accept(cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.api.
         * Processor)
         */
        @Override
        public <T> T accept(final Processor<? extends T> processor) {
            throw new IllegalStateException();
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.Visitable#accept(
         * cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.Visitor)
         */
        @Override
        public void accept(final Visitor visitor) {
            throw new IllegalStateException();
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final ArcStub other = (ArcStub) obj;
            if (!this.first.equals(other.first)) {
                return false;
            }
            if (!this.name.equals(other.name)) {
                return false;
            }
            if (!this.network.equals(other.network)) {
                return false;
            }
            if (!this.priority.equals(other.priority)) {
                return false;
            }
            if (!this.second.equals(other.second)) {
                return false;
            }
            if (this.third != other.third) {
                return false;
            }
            return true;
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc#getAttached
         * (cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.Direction)
         */
        @Override
        public Node getAttached(final Direction direction) {
            throw new IllegalStateException();
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc#getFrom()
         */
        @Override
        public Node getFrom() {
            throw new IllegalStateException();
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc#getName()
         */
        @Override
        public NormalWord getName() {
            return this.name;
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc#getNetwork
         * ()
         */
        @Override
        public Network getNetwork() {
            return this.network;
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc#getPriority
         * ()
         */
        @Override
        public Priority getPriority() {
            return this.priority;
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc#getTo()
         */
        @Override
        public Node getTo() {
            throw new IllegalStateException();
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + this.first.hashCode();
            result = prime * result + this.name.hashCode();
            result = prime * result + this.network.hashCode();
            result = prime * result + this.priority.hashCode();
            result = prime * result + this.second.hashCode();
            result = prime * result + this.third;
            return result;
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc#isAttached
         * (cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node,
         * cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.Direction)
         */
        @Override
        public boolean isAttached(final Node node, final Direction direction) {
            throw new IllegalStateException();
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc#isFrom
         * (cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node)
         */
        @Override
        public boolean isFrom(final Node node) {
            throw new IllegalStateException();
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc#isTo(cz
         * .cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node)
         */
        @Override
        public boolean isTo(final Node node) {
            throw new IllegalStateException();
        }

    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.DefaultArcModifier#create()}
     * .
     */
    @Test
    public void testCreate() {
        DefaultArcModifier.create();
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.DefaultArcModifier#change(cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc, cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord, cz.cuni.mff.ms.brodecva.botnicek.ide.design.types.Priority, java.lang.Class, java.lang.Object[])}
     * .
     */
    @Test
    public void testChange() {
        final DefaultArcModifier tested = DefaultArcModifier.create();

        final Network networkDummy = EasyMock.createStrictMock(Network.class);
        EasyMock.replay(networkDummy);

        final Arc arcStub = EasyMock.createStrictMock(Arc.class);
        EasyMock.expect(arcStub.getNetwork()).andStubReturn(networkDummy);
        EasyMock.replay(arcStub);

        final NormalWord newNameDummy =
                EasyMock.createStrictMock(NormalWord.class);
        EasyMock.replay(newNameDummy);

        final Priority priorityDummy =
                PowerMock.createStrictMock(Priority.class);
        PowerMock.replay(priorityDummy);

        final String first = "first";
        final Object second = new Object();
        final int third = 3;

        final Arc prototype =
                ArcStub.create(networkDummy, newNameDummy, priorityDummy,
                        first, second, third);
        assertEquals(prototype, tested.change(arcStub, newNameDummy,
                priorityDummy, ArcStub.class, first, second, third));

        EasyMock.verify(networkDummy);
        EasyMock.verify(newNameDummy);
        PowerMock.verify(priorityDummy);
        EasyMock.verify(arcStub);
    }

}
