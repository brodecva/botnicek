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
package cz.cuni.mff.ms.brodecva.botnicek.library.responder;

import java.nio.file.Path;
import java.util.List;

import cz.cuni.mff.ms.brodecva.botnicek.library.language.Language;

/**
 * Definice robota.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public interface Bot {
    /**
     * Získání predikátu bota.
     * 
     * @param name
     *            název predikátu
     * @return hodnota predikátu, prázdný řetězec, pokud neexistuje
     */
    String getPredicateValue(String name);

    /**
     * Vrátí umístění zdrojových souborů bota.
     * 
     * @return umístění zdrojových souborů bota
     */
    Path getFilesPath();

    /**
     * Vrátí název bota.
     * 
     * @return název bota
     */
    String getName();

    /**
     * Vrátí soubory načítané v přednostním pořadí.
     * 
     * @return soubory načítané v přednostním pořadí
     */
    List<String> getBeforeOrder();

    /**
     * Vrátí soubory načítané dodatečně.
     * 
     * @return soubory načtené v dodatečně
     */
    List<String> getAfterOrder();

    /**
     * Vrátí jazyk bota.
     * 
     * @return jazyk bota
     */
    Language getLanguage();

    /**
     * Vrátí cestu k k souboru se zaznamenanými promluvami o osobách.
     * 
     * @return cesta k souboru se zaznamenanými promluvami o osobách
     */
    Path getGossipPath();

    /**
     * Vrátí hash spočítanou z definice robota.
     * 
     * @return hash spočítaná z definice robota
     */
    @Override
    int hashCode();

    /**
     * Porovná objekt s botem.
     * 
     * @param object
     *            objekt
     * @return true, pokud je objekt bot a má shodnou definici
     */
    @Override
    boolean equals(Object object);
}
