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

import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.NamingAuthority;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.NormalizedNamingAuthority;
import cz.cuni.mff.ms.brodecva.botnicek.ide.editor.checker.CheckResult;
import cz.cuni.mff.ms.brodecva.botnicek.ide.editor.checker.CheckResultImplementation;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public class DefaultPredicateNameChecker implements PredicateNameChecker {
    private final NamingAuthority namingAuthority;
    
    public static DefaultPredicateNameChecker create(final NamingAuthority namingAuthority) {
        return new DefaultPredicateNameChecker(namingAuthority);
    }
    
    private DefaultPredicateNameChecker(final NamingAuthority namingAuthority) {
        Preconditions.checkNotNull(namingAuthority);
        
        this.namingAuthority = namingAuthority;
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.editor.checker.PredicateNameChecker#check(java.lang.String)
     */
    @Override
    public CheckResult check(final String name) {
        if (name.isEmpty()) {
            return CheckResultImplementation.fail(0, "The name is empty.");
        }
        
        final char[] characters = name.toCharArray();
        for (int index = 0; index < characters.length; index++) {
            final int position = index + 1;
            final char character = characters[index]; 
            
            if (!Character.isDigit(character) && !Character.isUpperCase(character) && !(Character.isLetter(character) && !Character.isLowerCase(character) && !Character.isUpperCase(character) && !Character.isTitleCase(character))) {
                return CheckResultImplementation.fail(position, "Invalid character at position %1$s.", position);
            }
        }
        
        if (!this.namingAuthority.isUsable(name)) {
            return CheckResultImplementation.fail(0, "The name of the predicate is already assigned to another entity.");
        }
        
        return CheckResultImplementation.succeed();
    }
}
