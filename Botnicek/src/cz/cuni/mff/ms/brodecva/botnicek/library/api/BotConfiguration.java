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
package cz.cuni.mff.ms.brodecva.botnicek.library.api;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

/**
 * Konfigurace robota.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public interface BotConfiguration {
    /**
     * Vrátí jméno robota.
     * 
     * @return jméno robota
     */
    String getName();
    
    /**
     * Vrátí umístění zdrojových souborů.
     * 
     * @return umístění zdrojových souborů
     */
    Path getFilesLocation();
    
    /**
     * Vrátí cestu k souboru na uložení promluv.
     * 
     * @return cesta k souboru na uložení promluv
     */
    Path getGossipPath();
    
    /**
     * Vrátí výchozí uživatelské predikáty.
     * 
     * @return výchozí uživatelské predikáty
     */
    Map<String, String> getPredicates();
    
    /**
     * Vrátí soubory načítané přednostně.
     * 
     * @return soubory načítané přednostně
     */
    List<String> getBeforeLoadingOrder();
    
    /**
     * Vrátí soubory načítané dodatečně.
     * 
     * @return soubory načítané dodatečně
     */
    List<String> getAfterLoadingOrder();
}
