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
package cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types;

/**
 * Prostý vzor se nachází v definicích tématu a testovacích podmínkách. Od
 * složeného vzoru se liší tím, že nedovoluje výskyt značky <a
 * href="http://www.alicebot.org/TR/2011/#section-pattern-side-bot-elements"
 * >bot</a>.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see <a
 *      href="http://www.alicebot.org/TR/2011/#section-simple-pattern-expressions">http://www.alicebot.org/TR/2011/#section-simple-pattern-expressions</a>
 */
public interface SimplePattern extends MixedPattern {

    /**
     * Porovná objekt se vzorem. Shoduje se s každým prostým vzorem stejného
     * textu.
     */
    @Override
    public boolean equals(Object obj);

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.MixedPattern#hashCode()
     */
    @Override
    public int hashCode();
}
