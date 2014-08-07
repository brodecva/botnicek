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

import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.InputNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.ProcessingNode;

/**
 * Procesor, který analyzuje uzly pro míru interakce uživatele při přechodu do dalšího stavu.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @param <T> typ výsledku zpracování
 */
public interface ProceedProcessor<T> {
    /**
     * Zpracuje uzel čekající na uživatelský vstup.
     * 
     * @param node blokující uzel
     * @return výsledek zpracování
     */
    T process(InputNode node);
    
    /**
     * Zpracuje výpočetní uzel.
     * 
     * @param node uzel, který pokračuje ve výpočtu
     * @return výsledek zpracování
     */
    T process(ProcessingNode node);
}
