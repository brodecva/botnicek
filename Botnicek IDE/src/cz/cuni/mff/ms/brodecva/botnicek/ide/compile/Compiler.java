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
package cz.cuni.mff.ms.brodecva.botnicek.ide.compile;

import java.util.List;
import java.util.Map;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.toplevel.Topic;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System;

/**
 * Kompilátor systému sítí ve smyslu ATN do témat jazyka AIML pro jednotlivé sítě.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public interface Compiler {
    /**
     * Zkompiluje systém sítí.
     * 
     * @param system systém sítí
     * @return sítě a témata, jež z nich byla vytvořena
     */
    Map<Network, List<Topic>> compile(final System system);
}
