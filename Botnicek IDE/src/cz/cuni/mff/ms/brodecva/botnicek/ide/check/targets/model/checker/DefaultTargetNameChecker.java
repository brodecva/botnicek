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
package cz.cuni.mff.ms.brodecva.botnicek.ide.check.targets.model.checker;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.CheckResult;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.CheckResultImplementation;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.EnterNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public class DefaultTargetNameChecker implements TargetNameChecker {
    private final System system;

    public static DefaultTargetNameChecker create(final System system) {
        return new DefaultTargetNameChecker(system);
    }

    private DefaultTargetNameChecker(final System system) {
        Preconditions.checkNotNull(system);

        this.system = system;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.editor.checker.TargetNameChecker
     * #check(java.lang.String)
     */
    @Override
    public CheckResult check(final Object source, final String name) {
        if (name.isEmpty()) {
            return CheckResultImplementation.fail(source, 0, "The name is empty.");
        }

        final char[] characters = name.toCharArray();
        for (int index = 0; index < characters.length; index++) {
            final int position = index + 1;
            final char character = characters[index];

            if (!Character.isDigit(character)
                    && !Character.isUpperCase(character)
                    && !(Character.isLetter(character)
                            && !Character.isLowerCase(character)
                            && !Character.isUpperCase(character) && !Character
                                .isTitleCase(character))) {
                return CheckResultImplementation.fail(source, position,
                        "Invalid character at position %1$s.", position);
            }
        }

        if (Collections2.filter(this.system.getAvailableReferences(),
                new Predicate<EnterNode>() {
                    @Override
                    public boolean apply(final EnterNode input) {
                        return input.getName().equals(name);
                    }
                }).isEmpty()) {
            return CheckResultImplementation
                    .fail(source, 0,
                            "The name is not an available target.");
        }

        return CheckResultImplementation.succeed(source);
    }
}
