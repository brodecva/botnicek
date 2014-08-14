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

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.check.code.model.checker.CodeChecker;

/**
 * Výchozí implementace {@link CodeContentBuilderFactory}.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class DefaultCodeContentBuilderFactory implements
        CodeContentBuilderFactory {

    private final CodeChecker checker;
    
    /**
     * Vytvoří továrnu.
     * 
     * @param checker validátor
     * @return továrna
     */
    public static DefaultCodeContentBuilderFactory create(final CodeChecker checker) {
        Preconditions.checkNotNull(checker);
        
        return new DefaultCodeContentBuilderFactory(checker);
    }
    
    private DefaultCodeContentBuilderFactory(final CodeChecker checker) {
        this.checker = checker;
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.check.code.model.builder.CodeContentBuilderFactory#produce(java.lang.String)
     */
    @Override
    public CodeContentBuilder produce(final String startContent) {
        Preconditions.checkNotNull(startContent);
        
        return DefaultCodeContentBuilder.create(checker, startContent);
    }

}
