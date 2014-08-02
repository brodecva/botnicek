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
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model;

import java.util.Set;
import java.util.UUID;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.AutonomousComponent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.Visitable;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.EnterNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.Direction;

/**
 * Rozhraní sítě dovoluje kromě přístupu k jejím částem (hrany a uzly) též tyto části přidávat a odebírat. Též poskytuje odkaz na rodičovský systém.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public interface Network extends AutonomousComponent, Visitable {

    /**
     * Vrátí dostupné vstupní uzly této sítě pro zanoření výpočtu do ní.
     * 
     * @return dostupné vstupní uzly této sítě pro zanoření výpočtu do ní
     */
    Set<EnterNode> getAvailableReferences();

    /**
     * Vrátí hranu v síti daného názvu.
     * 
     * @param name název hrany 
     * @return hrana
     */
    Arc getArc(NormalWord name);

    /**
     * Vrátí uzel v síti daného názvu.
     * 
     * @param name název uzlu 
     * @return uzel
     */
    Node getNode(NormalWord name);

    /**
     * Vrátí hrany napojené na uzel v síti.
     * 
     * @param node uzel
     * @param direction místo připojení uzlu k hranám
     * @return hrany napojené na uzel v síti
     */
    Set<Arc> getConnections(final Node node, final Direction direction);

    /**
     * Vrátí uzel na daném konci hrany.
     * 
     * @param arc hrana v síti
     * @param direction místo napojení
     * @return uzel na daném konci hrany
     */
    Node getAttached(final Arc arc, final Direction direction);

    /**
     * Vrátí vstupní hrany uzlu.
     * 
     * @param node uzel v síti
     * @return vstupní hrany
     */
    Set<Arc> getIns(final Node node);

    /**
     * Vrátí výstupní hrany uzlu.
     * 
     * @param node uzel v síti
     * @return výstupní hrany
     */
    Set<Arc> getOuts(final Node node);

    /**
     * Vrátí rodičovský systém sítě.
     * 
     * @return rodičovský systém sítě
     */
    System getSystem();

    /**
     * Vrátí jednoznačný identifikátor sítě.
     * 
     * @return identifikátor sítě
     */
    UUID getId();

    /**
     * Odebere hranu sítě.
     * 
     * @param name název odebírané hrany
     */
    void removeArc(final NormalWord name);

    /**
     * Přidá výchozí typ hrany mezi uzly sítě.
     * 
     * @param name název nové hrany
     * @param fromName výchozí uzel hrany
     * @param toName cílový uzel hrany
     */
    void addArc(final NormalWord name, final NormalWord fromName, NormalWord toName);

    /**
     * Odebere uzel sítě.
     * 
     * @param name název odebraného uzlu
     */
    void removeNode(final NormalWord name);

    /**
     * Přidá izolovaný uzel do sítě.
     * 
     * @param x souřadnice nového uzlu na ose x
     * @param y souřadnice nového uzlu na ose y
     */
    void addNode(final int x, final int y);

    /**
     * Přejmenuje síť.
     * 
     * @param name název sítě
     */
    void setName(final String name);

    /**
     * Indikuje, zda-li první uzel v daném směru sousedí s druhým.
     * 
     * @param first první uzel sítě
     * @param second druhý uzel sítě
     * @param direction směr
     * @return zda-li první uzel v daném směru sousedí s druhým
     */
    boolean adjoins(Node first, Node second,
            Direction direction);
    
    /**
     * Porovná objekt s sítí.
     * 
     * @param obj objekt
     * @return zda-li jde o stejnou síť (má stejný identifikátor a rodiče)
     */
    boolean equals(final Object obj);

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode();
}
