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
package cz.cuni.mff.ms.brodecva.botnicek.ide.check.words.model.checker;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.CheckResult;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.DefaultCheckResult;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.Source;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NamingAuthority;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.resources.ExceptionLocalizer;

/**
 * Výchozí implementace aplikuje vlastní implementaci normalizéru jazyka AIML.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class DefaultNormalWordChecker implements NormalWordChecker {
    private final NamingAuthority namingAuthority;
    
    /**
     * Vytvoří validátor, který neomezuje výskyt normálního slova.
     * 
     * @return validátor
     */
    public static DefaultNormalWordChecker create() {
        return new DefaultNormalWordChecker(new PermissiveNamingAuthority());
    }
    
    /**
     * Vytvoří validátor.
     * 
     * @param namingAuthority autorita přidělující unikátní jména 
     * 
     * @return validátor
     */
    public static DefaultNormalWordChecker create(final NamingAuthority namingAuthority) {
        return new DefaultNormalWordChecker(namingAuthority);
    }
    
    private DefaultNormalWordChecker(final NamingAuthority namingAuthority) {
        Preconditions.checkNotNull(namingAuthority);
        
        this.namingAuthority = namingAuthority;
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.editor.checker.PredicateNameChecker#check(java.lang.String)
     */
    @Override
    public CheckResult check(final Source source, Object subject, final String name) {
        if (name.isEmpty()) {
            return DefaultCheckResult.fail(0, ExceptionLocalizer.print("EmptyName"), source, subject);
        }
        
        final char[] characters = name.toCharArray();
        for (int index = 0; index < characters.length; index++) {
            final int position = index + 1;
            final char character = characters[index]; 
            
            if (!Character.isDigit(character) && !Character.isUpperCase(character) && !(Character.isLetter(character) && !Character.isLowerCase(character) && !Character.isUpperCase(character) && !Character.isTitleCase(character))) {
                return DefaultCheckResult.fail(position, ExceptionLocalizer.print("InvalidCharacter"), source, subject);
            }
        }
        
        if (!this.namingAuthority.isUsable(name)) {
            return DefaultCheckResult.fail(0, ExceptionLocalizer.print("AlreadyAssigned"), source, subject);
        }
        
        return DefaultCheckResult.succeed(source, subject);
    }
}
