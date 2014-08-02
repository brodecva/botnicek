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
package cz.cuni.mff.ms.brodecva.botnicek.ide.translate;

import java.util.List;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.toplevel.Topic;

/**
 * Rozhraní objektů, které vytvářejí seznam kategorií jazyka AIML.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see <a href="http://www.alicebot.org/TR/2011/#section-category">http://www.alicebot.org/TR/2011/#section-category</a>
 */
public interface TopicsGenerator {
    /**
     * Vrátí seznam témat.
     * 
     * @return seznam témat
     */
    List<Topic> getResult();
}
