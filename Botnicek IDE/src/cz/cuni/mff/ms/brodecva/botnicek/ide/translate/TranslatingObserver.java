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
import java.util.Map;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.toplevel.Topic;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.dfs.DfsObserver;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network;

/**
 * Pozorovatel DFS průchodu grafem, který provádí za běhu překlad jeho struktury do seznamu témat jazyka AIML.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see <a href="http://www.alicebot.org/TR/2011/#section-topic">http://www.alicebot.org/TR/2011/#section-topic</a>
 */
public interface TranslatingObserver extends DfsObserver {

    /**
     * Vrátí výsledek překladu. V případě zavolání před doběhnutím průchodu nemusí být výsledek nutně korektní, přestože se doporučuje implementacím, aby byl.
     * 
     * @return zobrazení sítí na seznamy témat jazyka AIML, do kterých byly přeloženy
     */
    Map<Network, List<Topic>> getResult();

}