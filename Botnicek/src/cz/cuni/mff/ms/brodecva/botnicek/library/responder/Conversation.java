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

import java.io.Serializable;
import java.nio.file.Path;

import cz.cuni.mff.ms.brodecva.botnicek.library.loader.LoaderException;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.TwoDimensionalIndex;

/**
 * Konverzace uživatele s botem.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public interface Conversation extends Serializable {
    /**
     * Výstup při nenalezení predikátu.
     */
    String NOT_FOUND_PRED_VALUE = "";

    /**
     * Prázdné slovo.
     */
    String EMPTY_WORD_VALUE = "";

    /**
     * Vrátí daný uživatelův vstup.
     * 
     * @param index
     *            index uživatelova vstupu, ne větší než počet zaznamenaných
     *            vstupů
     * @return ntý poslední uživatelův vstup, kde n je index
     */
    String getUserInput(TwoDimensionalIndex index);

    /**
     * Vrátí daný botův výstup.
     * 
     * @param index
     *            index robotova výstupu, ne větší než počet zaznamenaných
     *            výstupů
     * @return ntý poslední robotův výstup, kde n je index
     */
    String getBotOutput(TwoDimensionalIndex index);

    /**
     * Předá vstup botu k odpovědi s tím, že bude společně s výstupem zaznamenán
     * pro další vyhodnocování.
     * 
     * @param speech
     *            uživatelova promluva směrem k botu
     * @throws ConversationException
     *             pokud dojde k chybě při vyhodnocování vstupu
     */
    void talk(String speech) throws ConversationException;

    /**
     * Promluví k botu a čeká odpověď pomocí objektu @{link {@link Listener}.
     * 
     * @param speech
     *            promluva k botu
     * @param listener
     *            posluchač k registraci
     */
    void talk(String speech, Listener listener);

    /**
     * <p>
     * Předá vstup botu k odpovědi s tím, že nebude zaznamenán. Ani reakce na
     * něj.
     * </p>
     * <p>
     * Kromě toho se zaznamenává počet volání této metody, a v případě, že
     * překročí hodnotu {@link #getStackSize()}, vyhodí výjimku
     * {@link ConversationException}.
     * </p>
     * 
     * @param speech
     *            uživatelova promluva směrem k botu
     * @return výstup bota, reakce na uživatelovu promluvu
     * @throws ConversationException
     *             pokud dojde k chybě při vyhodnocování vstupu
     */
    String attemptTalk(String speech) throws ConversationException;

    /**
     * Resetuje počitadlo pokusů metody {@link #attemptTalk(String)}.
     */
    void resetAttempts();

    /**
     * Vrátí velikost zásobníku pro vnořené volání metody
     * {@link #attemptTalk(String)}.
     * 
     * @return velikost zásobníku
     */
    int getStackSize();

    /**
     * Vrátí reakci na uživatelův poslední vstup určený k záznamu.
     * 
     * @return reakce na poslední zaznamenaný vstup, null pokud neexistuje
     */
    String listen();

    /**
     * Doplní rozhodovací strukturu od zpracovaný obsah načteného souboru.
     * 
     * @param location
     *            umístění souboru
     * @throws LoaderException
     *             pokud dojde k chybě při načítání
     */
    void learn(Path location) throws LoaderException;

    /**
     * Vrátí bota, se kterým si uživatel povídá.
     * 
     * @return protějšek konverzace
     */
    Bot getBot();

    /**
     * Vrátí počet načtených kategorií, které bot používá při konverzaci.
     * 
     * @return počet kategorií
     */
    int getCategoryCount();

    /**
     * Vrátí hodnotu predikátu.
     * 
     * @param name
     *            název predikátu
     * @return hodnota predikátu, pokud není definován, tak prázdný řetězec
     */
    String getPredicateValue(String name);

    /**
     * Nastaví hodnotu predikátu.
     * 
     * @param name
     *            název predikátu
     * @param value
     *            hodnota predikátu
     * @return uložená hodnota
     */
    String setPredicateValue(String name, String value);
}
