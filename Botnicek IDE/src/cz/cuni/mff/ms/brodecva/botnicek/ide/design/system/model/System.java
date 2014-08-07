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
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model;

import java.util.Set;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.AutonomousComponent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.EnterNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.types.Priority;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.Direction;

/**
 * Rozhraní systému sítí zapouzdřuje všechny operace nad sítěmi.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public interface System extends cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.Visitable, AutonomousComponent {

    /**
     * Přidá síť.
     * 
     * @param name název sítě
     */
    void addNetwork(final String name);

    /**
     * Odstraní síť.
     * 
     * @param name název sítě
     */
    void removeNetwork(final String name);

    /**
     * Odstraní síť.
     * 
     * @param removed síť
     */
    void removeNetwork(final Network removed);

    /**
     * Přidá výchozí typ uzlu do sítě.
     * 
     * @param network síť
     * @param x souřadnice umístění na ose x
     * @param y souřadnice umístění na ose y
     */
    void addNode(final Network network, final int x, final int y);

    /**
     * Vrátí uzly v síti.
     * 
     * @param network síť
     * @return všechny uzly v síti
     */
    Set<Node> getNodes(final Network network);

    /**
     * Odstraní uzel ze systému.
     * 
     * @param name název uzlu
     */
    void removeNode(final NormalWord name);

    /**
     * Odstraní uzel ze systému.
     * 
     * @param removed uzel
     */
    void removeNode(Node removed);

    /**
     * Přidá do sítě hranu mezi uzly. 
     * 
     * @param network síť
     * @param name název nové hrany
     * @param fromNodeName název výchozího uzlu
     * @param toNodeName název cílového uzlu
     */
    void addArc(final Network network, final NormalWord name,
            final NormalWord fromNodeName, final NormalWord toNodeName);

    /**
     * Odstraní hranu ze systému.
     * 
     * @param name název hrany
     */
    void removeArc(final NormalWord name);
    

    /**
     * Odstraní hranu ze systému.
     * 
     * @param removed hrana
     */
    void removeArc(Arc removed);

    /**
     * Vrátí hrany směřující do uzlu.
     * 
     * @param node uzel
     * @return množina hran směřujících do uzlu
     */
    Set<Arc> getIns(final Node node);

    /**
     * Vrátí hrany směřující ven z uzlu.
     * 
     * @param node uzel
     * @return množina hran směřujících z uzlu
     */
    Set<Arc> getOuts(final Node node);

    /**
     * Změní typ uzlu.
     * 
     * @param name název uzlu
     * @param type nový typ uzlu
     */
    void changeNode(NormalWord name, Class<? extends Node> type);

    /**
     * Změní název uzlu.
     * 
     * @param name název uzlu
     * @param newName nový název
     */
    void changeNode(final NormalWord name, NormalWord newName);

    /**
     * Změní umístění uzlu.
     * 
     * @param name název uzlu
     * @param x souřadnice x
     * @param y souřadnice y
     */
    void changeNode(NormalWord name, int x, int y);

    /**
     * Změní hranu.
     * 
     * @param changed hrana
     * @param newName nový název
     * @param priority nová priorita
     * @param type nový typ
     * @param arguments specifické argumenty podporovaného typu
     */
    void changeArc(Arc changed, NormalWord newName, Priority priority,
            Class<? extends Arc> type, Object... arguments);
    
    /**
     * Vrátí hrany které jsou připojené k uzlu na daném kraji
     * 
     * @param node uzel
     * @param direction místo připojení hran
     * @return hrany
     */
    Set<Arc> getConnections(final Node node, final Direction direction);

    /**
     * Vrátí uzel na daném konci hrany.
     * 
     * @param arc hrana
     * @param direction konec hrany
     * @return uzel
     */
    Node getAttached(final Arc arc, final Direction direction);

    /**
     * Vrátí uzel daného názvu.
     * 
     * @param name název uzlu
     * @return uzel
     */
    Node getNode(final NormalWord name);

    /**
     * Vrátí hranu s daným názvem.
     * 
     * @param name název hrany
     * @return hrana
     */
    Arc getArc(final NormalWord name);

    /**
     * Vrátí užitou autoritu pro přidělování názvů predikátů.
     * 
     * @return autorita
     */
    NamingAuthority getPredicatesNamingAuthority();

    /**
     * Vrátí autoritu pro přidělování názvů stavů.
     * 
     * @return autorita
     */
    NamingAuthority getStatesNamingAuthority();

    /**
     * Vrátí všechny dostupné vstupní uzly (možné vstupy pro zanoření výpočtu).
     * 
     * @return množina vstupních uzlů systému
     */
    Set<EnterNode> getAvailableReferences();

    /**
     * Nastaví název systému.
     * 
     * @param newName nový název systému
     */
    void setName(final String newName);

    /**
     * Přejmenuje síť.
     * 
     * @param network síť
     * @param newName nový název sítě
     */
    void renameNetwork(final Network network, final String newName);

    /**
     * Vrátí název sítě.
     * 
     * @param network síť
     * @return síť
     */
    String getNetworkName(final Network network);

    /**
     * Indikuje, zda-li systém obsahuje zadanou síť.
     * 
     * @param network síť
     * @return zda-li systém obsahuje zadanou síť
     */
    boolean contains(final Network network);

    /**
     * Vrátí síť daného názvu.
     * 
     * @param name název sítě
     * @return síť
     */
    Network getNetwork(final String name);

    /**
     * Indikuje, zda-li je první uzel spojen s druhým hranou dané orientace. 
     * 
     * @param first první uzel
     * @param second druhý uzel
     * @param direction orientace hrany
     * @return zda-li je první uzel spojen s druhým hranou dané orientace
     */
    boolean adjoins(Node first, Node second, Direction direction);
    
    /**
     * Vrátí všechny sítě systému.
     * 
     * @return sítě systému
     */
    public Set<Network> getNetworks();
    
    /**
     * Vrátí maximální počet odchozích hran z uzlu.
     * 
     * @return maximální počet odchozích hran z uzlu
     */
    int getMaxBranchingFactor();
    
    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    int hashCode();

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    boolean equals(final Object obj);
}