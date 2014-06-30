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
package cz.cuni.mff.ms.brodecva.botnicek.ide.check.targets.model.builder;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.RawContentElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.TemplateElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.CheckResult;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.targets.model.checker.TargetNameChecker;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.types.TargetName;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public class DefaultTargetNameBuilder implements TargetNameBuilder {
    
    private final static class TargetNameImplementation implements TargetName {
        private final String rawContent;
        
        public static TargetNameImplementation create() {
            return new TargetNameImplementation("");
        }
        
        public static TargetNameImplementation create(final String rawContent) {
            return new TargetNameImplementation(rawContent);
        }
        
        private TargetNameImplementation(final String rawContent) {
            Preconditions.checkNotNull(rawContent);
            
            this.rawContent = rawContent;
        }

        /* (non-Javadoc)
         * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.TargetName#getValue()
         */
        @Override
        public String getValue() {
            return this.rawContent;
        }
    }
    
    private final StringBuilder contentBuilder;
    private final TargetNameChecker checker;
    
    public static DefaultTargetNameBuilder create(final TargetNameChecker checker, final String startContent) {
        return new DefaultTargetNameBuilder(checker, startContent);
    }
    
    public DefaultTargetNameBuilder(final TargetNameChecker checker, final String startContent) {
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
    public TargetName build() {
        if (!isValid()) {
            throw new IllegalStateException();
        }
        
        return TargetNameImplementation.create(this.contentBuilder.toString());
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.editor.checker.CodeContentBuilder#check()
     */
    @Override
    public CheckResult check() {
        return this.checker.check(this, this.contentBuilder.toString());
    }
}
