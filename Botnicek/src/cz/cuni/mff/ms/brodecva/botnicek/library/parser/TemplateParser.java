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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import cz.cuni.mff.ms.brodecva.botnicek.library.processor.ProcessorException;
import cz.cuni.mff.ms.brodecva.botnicek.library.responder.Bot;
import cz.cuni.mff.ms.brodecva.botnicek.library.responder.Conversation;
import cz.cuni.mff.ms.brodecva.botnicek.library.storage.MatchResult;

/**
 * Parser reakce.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public interface TemplateParser {

    /**
     * Prázdný výstup.
     */
    String EMPTY_RESPONSE = "";

    /**
     * Prázdný obsah.
     */
    String EMPTY_CONTENT = "";

    /**
     * Vrátí kontext bota, se kterým je parsováno.
     * 
     * @return kontext bota
     */
    Bot getBot();

    /**
     * Vrátí kontext konverzace, se kterou je parsováno.
     * 
     * @return kontext konverzace
     */
    Conversation getConversation();

    /**
     * Vrátí kontext výsledku porovnávání vzoru, se kterým je parsováno.
     * 
     * @return kontext výsledku porovnání vzoru
     */
    MatchResult getMatchResult();

    /**
     * Naparsuje a zpracuje textový vstup.
     * 
     * @param input
     *            textový vstup
     * @return výstup parsování a zpracování
     * @throws ProcessorException
     *             chyba při zpracování
     */
    String process(String input) throws ProcessorException;

    /**
     * Zpracuje prvek.
     * 
     * @param element
     *            prvek ke zpracování
     * @return výstup zpracování
     * @throws ProcessorException
     *             chyba při zpracování
     */
    String process(Element element) throws ProcessorException;

    /**
     * Zpracuje element.
     * 
     * @param document
     *            dokument
     * @return zpracovaný dokument
     * @throws ProcessorException
     *             chyba při zpracování
     */
    String evaluate(Document document) throws ProcessorException;

    /**
     * Zpracuje uzel po uzlu a výsledek spojí do jednoho řetězce.
     * 
     * @param list
     *            seznam uzlů
     * @return zpracovaný seznam uzlů
     * @throws ProcessorException
     *             chyba při zpracování
     */
    String evaluate(NodeList list) throws ProcessorException;

    /**
     * Podle typu uzlu buďto porvede jeho zpracování jako prvku nebo expanduje
     * jeho obsah do textové podoby.
     * 
     * @param node
     *            uzel ke zpracování
     * @return zpracovaný uzel
     * @throws ProcessorException
     *             chyba při zpracování
     */
    String evaluate(Node node) throws ProcessorException;

    /**
     * Rozbalí zkrácený prvek pod novým jménem, s novým synem, a zpracuje jej.
     * 
     * @param element
     *            zkrácený prvek
     * @param newElementName
     *            nové jméno prvku po rozbalení
     * @param childName
     *            jméno synovského uzlu po rozbalení
     * @return zpracovaný zkrácený element
     * @throws ProcessorException
     *             chyba při zpracování
     */
    String processShortenedTag(Element element, String newElementName,
            String childName) throws ProcessorException;

    /**
     * @return číslo verze
     */
    String getVersion();
}
