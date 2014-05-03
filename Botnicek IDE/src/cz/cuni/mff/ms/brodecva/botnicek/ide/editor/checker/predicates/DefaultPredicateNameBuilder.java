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
package cz.cuni.mff.ms.brodecva.botnicek.ide.editor.checker.predicates;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.elements.template.RawContentElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.elements.template.TemplateElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.types.PredicateName;
import cz.cuni.mff.ms.brodecva.botnicek.ide.editor.checker.CheckResult;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public class DefaultPredicateNameBuilder implements PredicateNameBuilder {
    
    private final static class PredicateNameImplementation implements PredicateName {
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
        public String getValue() {
            return this.rawContent;
        }
    }
    
    private final StringBuilder contentBuilder;
    private final PredicateNameChecker checker;
    
    public static DefaultPredicateNameBuilder create(final PredicateNameChecker checker, final String startContent) {
        return new DefaultPredicateNameBuilder(checker, startContent);
    }
    
    public DefaultPredicateNameBuilder(final PredicateNameChecker checker, final String startContent) {
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
    public PredicateName build() {
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
        return this.checker.check(this.contentBuilder.toString());
    }
}
