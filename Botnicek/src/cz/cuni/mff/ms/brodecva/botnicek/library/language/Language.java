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
package cz.cuni.mff.ms.brodecva.botnicek.library.language;

import java.util.regex.Pattern;

/**
 * Zpracovává text dle definice charakteristik jazyka konverzace.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public interface Language {
    /**
     * Vrátí regulární výraz definující hranici věty.
     * 
     * @return regulární výraz definující hranici věty
     */
    Pattern getSentenceDelim();

    /**
     * Zamění pohlaví v textu.
     * 
     * @param text
     *            text ke zpracování
     * @return text s prohozenými zmínkami o pohlaví
     */
    String swapGender(String text);

    /**
     * Prohodí v textu 1. a 2. osobu.
     * 
     * @param text
     *            text ke zpracování
     * @return text s prohozenou 1. a 2. osobou
     */
    String transformPerson(String text);

    /**
     * Prohodí v textu 1. a 3. osobu.
     * 
     * @param text
     *            text ke zpracování
     * @return text s prohozenou 1. a 2. osobou
     */
    String transformPerson2(String text);

    /**
     * Nahradí zkratky v textu zkracovanými výrazy.
     * 
     * @param text
     *            text ke zpracování
     * @return text s rozvinutými zkratkami
     */
    String expandAbbreviations(String text);

    /**
     * Opraví pravopis.
     * 
     * @param text
     *            text ke zpracování
     * @return text s opraveným pravopisem a kolokvialismy nahrazenými
     *         spisovnými variantami
     */
    String correctSpellingAndColloquialisms(final String text);

    /**
     * Nahradí emotikony vyjádřeními v jazyce.
     * 
     * @param text
     *            text ke zpracování
     * @return emotikony vyjádřené prostředky jazyka
     */
    String substituteEmoticons(final String text);
    

    /**
     * Zbaví text interpunkčních znamének, která se nacházejí mimo hranice vět.
     * 
     * @param text
     *            text
     * @return text bez interpunkčních znamének mimo hranice vět
     */
    String removeInnerSentencePunctuation(String text);

    /**
     * Vrátí název jazyka.
     * 
     * @return název jazyka
     */
    String getName();

    /**
     * Vrátí hash vypočítaný z prvků charakterizujících jazyk.
     * 
     * @return hash vypočítaný z prvků charakterizujících jazyk
     */
    @Override
    int hashCode();

    /**
     * Zjistí, zda-li je jazyk roven objektu.
     * 
     * @param obj
     *            objekt k porovnání
     * @return true pokud je object také jazyk a jeho charakteristiky jsou si
     *         rovny
     */
    @Override
    boolean equals(Object obj);
}
