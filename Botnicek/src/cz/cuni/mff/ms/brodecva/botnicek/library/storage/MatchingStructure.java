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
package cz.cuni.mff.ms.brodecva.botnicek.library.storage;

/**
 * Datová struktura umožňující ukládání a hledání reakcí na normalizovaná
 * vstupní data.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public interface MatchingStructure {
    /**
     * Pokusí se najít odpovídající šablonu pro vstupní cestu.
     * 
     * @param path
     *            vstupní cesta obsahující dotaz, na který se hledá reakce
     * @return výsledek hledání
     */
    MatchResult find(InputPath path);

    /**
     * Přidá šablonu pro danou vstupní cestu jako její výsledek.
     * 
     * @param path
     *            vstupní cesta obsahující dotaz
     * @param answer
     *            šablona s reakcí odpovídající dotazu ve vstupní cestě
     */
    void add(InputPath path, Template answer);

    /**
     * Vrátí počet uložených kategorií (dvojic šablona - vzor).
     * 
     * @return počet uložených kategorií
     */
    int getCategoryCount();

    /**
     * Značí nutnost dopředného zpracování.
     * 
     * @return true, pokud struktura vyžaduje dopředné zpracování
     */
    boolean isForwardCompatible();

    /**
     * Označí strukturu jako strukturu vyžadující dopředné zpracování.
     * 
     * @param forwardCompatible
     *            true pro označení struktury jako struktury obsahující prvky
     *            vyžadující dopředné zpracování
     */
    void setForwardCompatible(boolean forwardCompatible);
}
