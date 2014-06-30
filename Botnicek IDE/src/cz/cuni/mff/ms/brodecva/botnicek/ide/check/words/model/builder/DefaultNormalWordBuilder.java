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

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.RawContentElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.TemplateElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.CheckResult;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.words.model.checker.NormalWordChecker;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public class DefaultNormalWordBuilder implements NormalWordBuilder {
    
    private final static class PredicateNameImplementation implements NormalWord {
        private final String rawContent;
        
        public static PredicateNameImplementation create() {
            return new PredicateNameImplementation("");
        }
        
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
    }
    
    private final StringBuilder contentBuilder;
    private final NormalWordChecker checker;
    
    public static DefaultNormalWordBuilder create(final NormalWordChecker checker, final String startContent) {
        return new DefaultNormalWordBuilder(checker, startContent);
    }
    
    public DefaultNormalWordBuilder(final NormalWordChecker checker, final String startContent) {
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
        return this.checker.check(this, this.contentBuilder.toString());
    }
}
