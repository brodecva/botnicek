/**
 * Copyright Václav Brodec 2014.
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

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.Code;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.code.model.checker.CodeChecker;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.builder.Builder;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.builder.BuilderFactory;

/**
 * Výchozí implementace {@link BuilderFactory}.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class DefaultCodeBuilderFactory implements
        BuilderFactory<Code> {

    /**
     * Vytvoří továrnu.
     * 
     * @param checker
     *            validátor
     * @return továrna
     */
    public static DefaultCodeBuilderFactory create(
            final CodeChecker checker) {
        Preconditions.checkNotNull(checker);

        return new DefaultCodeBuilderFactory(checker);
    }

    private final CodeChecker checker;

    private DefaultCodeBuilderFactory(final CodeChecker checker) {
        this.checker = checker;
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.builder.BuilderFactory#produce(java.lang.String)
     */
    @Override
    public Builder<Code> produce(final String startContent) {
        Preconditions.checkNotNull(startContent);

        return DefaultCodeBuilder.create(this.checker, startContent);
    }

}
