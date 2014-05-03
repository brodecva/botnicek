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
package cz.cuni.mff.ms.brodecva.botnicek.library.parser;

import org.w3c.dom.Element;

import cz.cuni.mff.ms.brodecva.botnicek.library.platform.Index;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.TwoDimensionalIndex;

/**
 * Tovární třída pro AIML index. Přečte hodnotu index z atributu index prvku.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public interface IndexFactory {

    /**
     * Prázdná hodnota atributu.
     */
    String EMPTY_ATTRIBUTE_VALUE = "";

    /**
     * Čárka (pro oddělení částí dvojrozměrných indexů).
     */
    String COMMA = ",";

    /**
     * Vrátí číselnou hodnotu atributu index.
     * 
     * @param element
     *            prvek s atributem index obsahujícím číslo
     * @return číslo načteného indexu
     */
    Index createIndex(Element element);

    /**
     * Vrátí číselný pár podle hodnoty atributu index.
     * 
     * @param element
     *            prvek s atributem index obsahujícím dvě čísla oddělená čárkou
     * @return načtený dvojrozměrný index
     */
    TwoDimensionalIndex create2DIndex(Element element);
}
