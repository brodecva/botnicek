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
package cz.cuni.mff.ms.brodecva.botnicek.ide.check.code.model.builder;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.Code;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.code.model.checker.CodeChecker;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.CheckResult;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.Source;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.Objects;

/**
 * Výchozí implementace konstruktoru validního kódu šablony jazyka AIML.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class DefaultCodeContentBuilder implements CodeContentBuilder,
        Source, Serializable {

    private final static class CodeImplementation implements Code, Serializable {
        private static final long serialVersionUID = 1L;

        public static CodeImplementation create(final String rawContent) {
            return new CodeImplementation(rawContent);
        }

        private final String text;

        private CodeImplementation(final String rawContent) {
            Preconditions.checkNotNull(rawContent);

            this.text = rawContent;
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

    private static final long serialVersionUID = 1L;

    /**
     * Vytvoří konstruktor.
     * 
     * @param checker
     *            přímý validátor
     * @param startContent
     *            úvodní řetězec k sestavení
     * @return konstruktor
     */
    public static DefaultCodeContentBuilder create(final CodeChecker checker,
            final String startContent) {
        return new DefaultCodeContentBuilder(checker, startContent);
    }

    private final StringBuilder contentBuilder;

    private final CodeChecker checker;

    private DefaultCodeContentBuilder(final CodeChecker checker,
            final String startContent) {
        Preconditions.checkNotNull(checker);
        Preconditions.checkNotNull(startContent);

        this.checker = checker;
        this.contentBuilder = new StringBuilder(startContent);
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.builder.
     * ContentAggregator#add(java.lang.String)
     */
    @Override
    public void add(final String content) {
        this.contentBuilder.append(content);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.editor.checker.CodeContentBuilder
     * #build()
     */
    @Override
    public Code build() {
        if (!isValid()) {
            throw new IllegalStateException();
        }

        return CodeImplementation.create(this.contentBuilder.toString());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.editor.checker.CodeContentBuilder
     * #check()
     */
    @Override
    public CheckResult check() {
        return this.checker.check(this, this, this.contentBuilder.toString());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.editor.checker.CodeContentBuilder
     * #isValid()
     */
    @Override
    public boolean isValid() {
        return check().isValid();
    }
}
