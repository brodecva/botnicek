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
package cz.cuni.mff.ms.brodecva.botnicek.ide.compile;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;

/**
 * Továrna na výchozí implementace kompilátorů.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public class DefaultCompilerFactory implements CompilerFactory, Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Vytvoří továrnu.
     * 
     * @return továrna
     */
    public static DefaultCompilerFactory create() {
        return new DefaultCompilerFactory();
    }

    private DefaultCompilerFactory() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.projects.model.CompilerFactory#produce
     * (cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord,
     * cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord,
     * cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord,
     * cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord)
     */
    @Override
    public Compiler produce(final NormalWord pullState,
            final NormalWord pullStopState, final NormalWord randomizeState,
            final NormalWord successState, final NormalWord returnState,
            final NormalWord testingPredicate) {
        return DefaultCompiler.create(pullState, pullStopState, successState,
                returnState, randomizeState, testingPredicate);
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
