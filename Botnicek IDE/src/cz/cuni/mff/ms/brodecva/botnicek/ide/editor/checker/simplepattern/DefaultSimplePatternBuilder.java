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
package cz.cuni.mff.ms.brodecva.botnicek.ide.editor.checker.simplepattern;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.elements.template.TemplateElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.types.SimplePattern;
import cz.cuni.mff.ms.brodecva.botnicek.ide.editor.checker.CheckResult;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public class DefaultSimplePatternBuilder implements SimplePatternBuilder {
    
    private final static class PatternImplementation implements SimplePattern {
        private final String rawContent;
        
        public static PatternImplementation create() {
            return new PatternImplementation("");
        }
        
        public static PatternImplementation create(final String rawContent) {
            return new PatternImplementation(rawContent);
        }
        
        private PatternImplementation(final String rawContent) {
            Preconditions.checkNotNull(rawContent);
            
            this.rawContent = rawContent;
        }

        /* (non-Javadoc)
         * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.Pattern#getText()
         */
        @Override
        public String getText() {
            return this.rawContent;
        }
    }
    
    private final StringBuilder contentBuilder;
    private final SimplePatternChecker checker;
    
    public static DefaultSimplePatternBuilder create(final SimplePatternChecker checker, final String startContent) {
        return new DefaultSimplePatternBuilder(checker, startContent);
    }
    
    public DefaultSimplePatternBuilder(final SimplePatternChecker checker, final String startContent) {
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
        
        return PatternImplementation.create(this.contentBuilder.toString());
    }

    @Override
    public CheckResult check() {
        return this.checker.check(this.contentBuilder.toString());
    }
}
