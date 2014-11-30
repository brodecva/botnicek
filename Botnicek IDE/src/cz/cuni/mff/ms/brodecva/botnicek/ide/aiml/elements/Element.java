/**
 * Copyright Václav Brodec 2014.
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
package cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements;

import java.util.List;
import java.util.Set;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.api.Visitable;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.api.Visitor;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.Attribute;

/**
 * Rozhraní odlehčené (oproti standardnímu DOM) reprezentace prvku stromu jazyka
 * AIML. Poskytuje metody pro přístup k potomkům, atributům a vnitřnímu textu.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public interface Element extends Visitable {

    /**
     * {@inheritDoc}
     * 
     * Průchod stromem prvků je obvyklý, tj. prefixový.
     */
    @Override
    public void accept(Visitor visitor);

    /**
     * Vrací množinu atributů.
     * 
     * @return množina atributů
     */
    Set<Attribute> getAttributes();

    /**
     * Vrací uspořádané potomky prvku.
     * 
     * @return potomci prvku
     */
    List<Element> getChildren();

    /**
     * Vrací název prvku.
     * 
     * @return název prvku
     */
    String getLocalName();

    /**
     * Vrací vnitřní text prvku.
     * 
     * @return vnitřní text prvku
     */
    String getText();

    /**
     * Indikuje, zda-li má prvek potomky.
     * 
     * @return zda-li má prvek potomky
     */
    boolean hasChildren();
}