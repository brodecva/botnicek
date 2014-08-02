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
package cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types;

/**
 * Normální slovo (tj. slovo pouze z normálních znaků) dle definice jazyka AIML. Při porovnání s jiným slovem se zohledňuje samotný text.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see <a href="http://www.alicebot.org/TR/2011/#section-normal-words">http://www.alicebot.org/TR/2011/#section-normal-words</a>
 */
public interface NormalWord extends Comparable<NormalWord> {
    
    /**
     * Vrátí vlastní text slova.
     * 
     * @return text slova
     */
    public String getText();
    
    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode();
    
    /**
     * Porovná objekt s normálním slovem. Shoduje se pouze s jiným normálním slovem stejného textu.
     */
    @Override
    public boolean equals(Object obj);
}
