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
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.views;

/**
 * Režimy editace prvků sítě zužují okruh dostupných operací nad uzly prostřednictvím klávesnice a myši.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public enum EditMode {
    /**
     * Režim návrhu dovoluje přidávat uzly, hran a navíc vstupovat do ostatních režimu pomocí klávesových zkratek dle specifikace okna sítě {@link NetworkInternalWindow}.
     */
    DESIGN,
    
    /**
     * Režim přejmenovávání prvků.
     */
    RENAME,
    
    /**
     * Režim odstraňování prvků.
     */
    REMOVE,
    
    /**
     * Režim přepínání míry interaktivity uzlu při zpracování (čekající na uživatele či zpracovávající rekurzivní vstup).
     */
    INTERACTIVITY,
    
    /**
     * Režim přepínání způsobu výběru následující odchozí hrany (mezi váženým náhodným a uspořádaným dle priority).
     */
    DISPATCH_ORDER;
}
