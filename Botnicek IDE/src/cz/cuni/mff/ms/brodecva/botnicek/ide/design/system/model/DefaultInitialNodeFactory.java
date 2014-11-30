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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.IsolatedNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.implementations.IsolatedProcessingNode;

/**
 * Implementace {@link InitialNodeFactory}.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public class DefaultInitialNodeFactory implements InitialNodeFactory,
        Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Vytvoří továrnu.
     * 
     * @return továrna
     */
    public static DefaultInitialNodeFactory create() {
        return new DefaultInitialNodeFactory();
    }

    private DefaultInitialNodeFactory() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.InitialNodeFactory
     * #produce(cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord,
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network, int,
     * int)
     */
    @Override
    public IsolatedNode produce(final NormalWord name, final Network network,
            final int x, final int y) {
        Preconditions.checkNotNull(network);
        Preconditions.checkNotNull(name);
        Preconditions.checkNotNull(x);
        Preconditions.checkNotNull(y);

        return IsolatedProcessingNode.create(name, network, x, y);
    }

    private void readObject(final ObjectInputStream objectInputStream)
            throws ClassNotFoundException, IOException {
        objectInputStream.defaultReadObject();
    }

    private void writeObject(final ObjectOutputStream objectOutputStream)
            throws IOException {
        objectOutputStream.defaultWriteObject();
    }
}
