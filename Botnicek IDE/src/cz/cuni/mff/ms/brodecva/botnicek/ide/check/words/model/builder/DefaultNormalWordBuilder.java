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
package cz.cuni.mff.ms.brodecva.botnicek.ide.check.words.model.builder;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.CheckResult;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.Source;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.words.model.checker.NormalWordChecker;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.Objects;

/**
 * Výchozí implementace konstruktoru validního normálního názvu dle specifikace jazyka AIML.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class DefaultNormalWordBuilder implements NormalWordBuilder, Source {
    
    private final static class PredicateNameImplementation implements NormalWord, Serializable {
        private static final long serialVersionUID = 1L;
        
        private final String rawContent;
        
        public static PredicateNameImplementation create(final String rawContent) {
            return new PredicateNameImplementation(rawContent);
        }
        
        private PredicateNameImplementation(final String rawContent) {
            Preconditions.checkNotNull(rawContent);
            
            this.rawContent = rawContent;
        }

        /* (non-Javadoc)
         * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.PredicateName#getValue()
         */
        @Override
        public String getText() {
            return this.rawContent;
        }

        /* (non-Javadoc)
         * @see java.lang.Comparable#compareTo(java.lang.Object)
         */
        @Override
        public int compareTo(final NormalWord other) {
            Preconditions.checkNotNull(other);
            
            return this.rawContent.compareTo(other.getText());
        }

        /* (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return "PredicateName [rawContent=" + rawContent
                    + "]";
        }
        
        /* (non-Javadoc)
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (Objects.isNull(obj)) {
                return false;
            }
            if (!(obj instanceof NormalWord)) {
                return false;
            }
            NormalWord other = (NormalWord) obj;
            if (!rawContent.equals(other.getText())) {
                return false;
            }
            return true;
        }
        
        private void readObject(final ObjectInputStream objectInputStream)
                throws ClassNotFoundException, IOException {
            objectInputStream.defaultReadObject();
            
            Preconditions.checkNotNull(this.rawContent);
            //TODO: Validace.
        }

        private void writeObject(final ObjectOutputStream objectOutputStream)
                throws IOException {
            objectOutputStream.defaultWriteObject();
        }
    }
    
    private final StringBuilder contentBuilder;
    private final NormalWordChecker checker;
    
    /**
     * Vytvoří konstruktor.
     * 
     * @param checker přímý validátor
     * @param startContent úvodní řetězec k sestavení
     * @return konstruktor
     */
    public static DefaultNormalWordBuilder create(final NormalWordChecker checker, final String startContent) {
        return new DefaultNormalWordBuilder(checker, startContent);
    }
    
    private DefaultNormalWordBuilder(final NormalWordChecker checker, final String startContent) {
        Preconditions.checkNotNull(checker);
        Preconditions.checkNotNull(startContent);
        
        this.checker = checker;
        this.contentBuilder = new StringBuilder(startContent);
    }
    
    public void add(final String content) {
        this.contentBuilder.append(content);
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.editor.checker.CodeContentBuilder#isValid()
     */
    @Override
    public boolean isValid() {
        return check().isValid();
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.editor.checker.CodeContentBuilder#build()
     */
    @Override
    public NormalWord build() {
        if (!isValid()) {
            throw new IllegalStateException();
        }
        
        return PredicateNameImplementation.create(this.contentBuilder.toString());
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.editor.checker.CodeContentBuilder#check()
     */
    @Override
    public CheckResult check() {
        return this.checker.check(this, this, this.contentBuilder.toString());
    }
}
