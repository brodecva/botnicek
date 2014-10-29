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
package cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.check.code.model.checker.CodeChecker;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.Objects;

/**
 * Pomocné metody pro tvorbu kódu šablony AIML.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public class Codes {
    /**
     * Interní implementace kódu.
     */
    private static final class CodeImplementation implements Code, Serializable {

        private static final long serialVersionUID = 1L;

        private final String text;
        
        public static CodeImplementation create(final String text) {
            return new CodeImplementation(text);
        }

        private CodeImplementation(final String text) {
            Preconditions.checkNotNull(text);

            this.text = text;
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
            if (Objects.isNull(obj)) {
                return false;
            }
            if (!(obj instanceof Code)) {
                return false;
            }
            final Code other = (Code) obj;
            if (!this.text.equals(other.getText())) {
                return false;
            }
            return true;
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.Code#getText
         * ()
         */
        @Override
        public String getText() {
            return this.text;
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
            result = prime * result + this.text.hashCode();
            return result;
        }

        private void readObject(final ObjectInputStream objectInputStream)
                throws ClassNotFoundException, IOException {
            objectInputStream.defaultReadObject();

            Preconditions.checkNotNull(this.text);
        }

        private void writeObject(final ObjectOutputStream objectOutputStream)
                throws IOException {
            objectOutputStream.defaultWriteObject();
        }
    }

    /**
     * Vytvoří prázdný kód.
     * 
     * @return žádný kód
     */
    public static Code createEmpty() {
        return CodeImplementation.create("");
    }

    /**
     * Vytvoří kód z textu, jež projde validací.
     * 
     * @param code zdrojový kód šablony v textové podobě
     * @param checker validátor zdrojového kódu
     * @return zdrojový kód
     */
    public static Code of(final String code, final CodeChecker checker) {
        Preconditions.checkNotNull(code);
        Preconditions.checkNotNull(checker);
        Preconditions.checkArgument(checker.check(code).isValid());
        
        return CodeImplementation.create(code);
    }
}
