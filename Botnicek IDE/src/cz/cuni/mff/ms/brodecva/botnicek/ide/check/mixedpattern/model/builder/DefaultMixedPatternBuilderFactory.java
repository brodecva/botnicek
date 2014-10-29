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
package cz.cuni.mff.ms.brodecva.botnicek.ide.check.mixedpattern.model.builder;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.MixedPattern;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.builder.Builder;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.builder.BuilderFactory;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.mixedpattern.model.checker.MixedPatternChecker;

/**
 * Výchozí implementace {@link BuilderFactory}.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class DefaultMixedPatternBuilderFactory implements
        BuilderFactory<MixedPattern> {

    /**
     * Vytvoří továrnu.
     * 
     * @param checker
     *            validátor
     * @return továrna
     */
    public static DefaultMixedPatternBuilderFactory create(
            final MixedPatternChecker checker) {
        Preconditions.checkNotNull(checker);

        return new DefaultMixedPatternBuilderFactory(checker);
    }

    private final MixedPatternChecker checker;

    private DefaultMixedPatternBuilderFactory(final MixedPatternChecker checker) {
        this.checker = checker;
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.builder.BuilderFactory#produce(java.lang.String)
     */
    @Override
    public Builder<MixedPattern> produce(final String value) {
        Preconditions.checkNotNull(value);

        return DefaultMixedPatternBuilder.create(this.checker, value);
    }

}
