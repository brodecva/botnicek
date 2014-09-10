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
package cz.cuni.mff.ms.brodecva.botnicek.ide.check.simplepattern.model.builder;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.SimplePattern;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.CheckResult;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.Source;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.simplepattern.model.checker.SimplePatternChecker;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.Objects;

/**
 * Výchozí implementace konstruktoru validního prostého vzoru dle specifikace jazyka AIML.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class DefaultSimplePatternBuilder implements SimplePatternBuilder, Source {
    
    private final static class SimplePatternImplementation implements SimplePattern, Serializable {
        private static final long serialVersionUID = 1L;
        
        private final String text;
        
        public static SimplePatternImplementation create(final String rawContent) {
            return new SimplePatternImplementation(rawContent);
        }
        
        private SimplePatternImplementation(final String rawContent) {
            Preconditions.checkNotNull(rawContent);
            
            this.text = rawContent;
        }

        /* (non-Javadoc)
         * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.Pattern#getText()
         */
        @Override
        public String getText() {
            return this.text;
        }

        /* (non-Javadoc)
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + text.hashCode();
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
            if (Objects.isNull(obj)) {
                return false;
            }
            if (!(obj instanceof SimplePattern)) {
                return false;
            }
            SimplePattern other =
                    (SimplePattern) obj;
            if (!text.equals(other.getText())) {
                return false;
            }
            return true;
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
    
    private final StringBuilder contentBuilder;
    private final SimplePatternChecker checker;
    
    /**
     * Vytvoří konstruktor.
     * 
     * @param checker přímý validátor
     * @param startContent úvodní řetězec k sestavení
     * @return konstruktor
     */
    public static DefaultSimplePatternBuilder create(final SimplePatternChecker checker, final String startContent) {
        return new DefaultSimplePatternBuilder(checker, startContent);
    }
    
    private DefaultSimplePatternBuilder(final SimplePatternChecker checker, final String startContent) {
        Preconditions.checkNotNull(checker);
        Preconditions.checkNotNull(startContent);
        
        this.checker = checker;
        this.contentBuilder = new StringBuilder(startContent);
    }
    
    public void add(final String content) {
        this.contentBuilder.append(content);
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.editor.checker.PatternContentBuilder#isValid()
     */
    @Override
    public boolean isValid() {
        return check().isValid();
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.editor.checker.PatternContentBuilder#build()
     */
    @Override
    public SimplePattern build() {
        if (!isValid()) {
            throw new IllegalStateException();
        }
        
        return SimplePatternImplementation.create(this.contentBuilder.toString());
    }

    @Override
    public CheckResult check() {
        return this.checker.check(this, this, this.contentBuilder.toString());
    }
}
