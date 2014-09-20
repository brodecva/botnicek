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
package cz.cuni.mff.ms.brodecva.botnicek.ide.project.model;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.Writer;
import java.nio.file.Path;

import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Intended;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.Presence;

/**
 * Továrna produkující {@link FileWriter} s vyrovnávací pamětí.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
final class BufferedFileWriterFactory implements WriterFactory, Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Vytvoří továrnu.
     * 
     * @return továrna
     */
    public static BufferedFileWriterFactory create() {
        return new BufferedFileWriterFactory();
    }
    
    private BufferedFileWriterFactory() {
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.project.model.WriterFactory#produce(java.nio.file.Path, java.lang.String, java.lang.String)
     */
    @Override
    public Writer produce(Path directory, String fileName)
            throws IOException {
        final Path filePath = directory.resolve(fileName);
        
        FileWriter fileOutput = Intended.nullReference();
        try {
            fileOutput = new FileWriter(filePath.toFile());
        } catch (final IOException e) {
            if (Presence.isPresent(fileOutput)) {
                fileOutput.close();     
            }
            
            throw e;
        }
        
        return new BufferedWriter(fileOutput);
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
