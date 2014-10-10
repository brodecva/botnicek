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
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.properties;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.Code;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.MixedPattern;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.SimplePattern;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.EnterNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.types.Priority;

/**
 * Pohled na vlastnosti hrany. Je jednotný pro všechny typy, které dle potřeby
 * předefinují relevantní aktualizační metody.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public interface ArcView {
    /**
     * Zpraví o odstranění hrany.
     */
    void removed();

    /**
     * Aktualizuje zobrazený kód pro provedení této hrany.
     * 
     * @param code
     *            nový kód
     */
    void updatedCode(Code code);

    /**
     * Aktualizuje testovací vzor kategorie AIML hrany.
     * 
     * @param pattern
     *            vzor
     */
    void updatedPattern(MixedPattern pattern);

    /**
     * Aktualizuje název testovaného predikátu této hrany.
     * 
     * @param name
     *            nový název testovaného predikátu
     */
    void updatedPredicate(NormalWord name);

    /**
     * Aktualizuje zobrazený přípravný kód na test predikátu této hrany.
     * 
     * @param code
     *            nový testovací kód
     */
    void updatedPrepare(Code code);

    /**
     * Aktualizuje nastavené místo zanoření při testu hrany.
     * 
     * @param target
     *            místo zanoření
     */
    void updatedTarget(EnterNode target);

    /**
     * Aktualizuje zobrazený testovací kód hrany.
     * 
     * @param code
     *            nový testovací kód
     */
    void updatedTested(Code code);

    /**
     * Aktualizuje testovací vzor zmínky kategorie AIML hrany.
     * 
     * @param that
     *            vzor zmínky
     */
    void updatedThat(MixedPattern that);

    /**
     * Aktualizuje vzor očekávané hodnoty.
     * 
     * @param value
     *            nový vzor očekávané hodnoty
     */
    void updatedValue(SimplePattern value);

    /**
     * Aktualizuje zobrazený název výchozího uzlu hrany.
     * 
     * @param name
     *            nový název výchozího uzlu hrany
     */
    void updateFrom(NormalWord name);

    /**
     * Aktualizuje zobrazený název hrany.
     * 
     * @param name
     *            nový název hrany
     */
    void updateName(NormalWord name);

    /**
     * Aktualizuje zobrazenou prioritu hrany.
     * 
     * @param priority
     *            nová priorita hrany
     */
    void updatePriority(Priority priority);

    /**
     * Aktualizuje zobrazený název cílového uzlu hrany.
     * 
     * @param name
     *            nový název cílového uzlu hrany
     */
    void updateTo(NormalWord name);

    /**
     * Aktualizuje zobrazený typ hrany.
     * 
     * @param arcClass
     *            nový typ
     */
    void updateType(Class<? extends Arc> arcClass);
}
