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
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.api;

import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.EnterNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.ExitNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.InnerNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.IsolatedNode;

/**
 * Procesor uzlů, který je analyzuje kvůli modifikaci zásobníku.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @param <T>
 *            typ výsledku zpracování
 */
public interface StackProcessor<T> {
    /**
     * Zpracuje vstupní uzel.
     * 
     * @param node
     *            vstupní uzel
     * @return výsledek zpracování
     */
    T process(EnterNode node);

    /**
     * Zpracuje výstupní uzel.
     * 
     * @param node
     *            výstupní uzel
     * @return výsledek zpracování
     */
    T process(ExitNode node);

    /**
     * Zpracuje vnitřní uzel.
     * 
     * @param node
     *            vnitřní uzel
     * @return výsledek zpracování
     */
    T process(InnerNode node);

    /**
     * Zpracuje izolovaný uzel.
     * 
     * @param node
     *            izolovaný uzel
     * @return výsledek zpracování
     */
    T process(IsolatedNode node);
}
