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
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.updates;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Implementace {@link UpdateBuilderFactory}.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class DefaultUpdateBuilderFactory implements UpdateBuilderFactory,
        Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Vytvoří továrnu.
     * 
     * @return továrna
     */
    public static DefaultUpdateBuilderFactory create() {
        return new DefaultUpdateBuilderFactory();
    }

    private DefaultUpdateBuilderFactory() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.updates.
     * UpdateBuilderFactory#produce()
     */
    @Override
    public UpdateBuilder produce() {
        return DefaultUpdateBuilder.create();
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
