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
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model;

import java.util.Set;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.AutonomousComponent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.EnterNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.FunctionalNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.types.Priority;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.types.SystemName;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.Direction;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.Dispatcher;

/**
 * Rozhraní systému sítí zapouzdřuje všechny operace nad sítěmi.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public interface System extends
        cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.Visitable,
        AutonomousComponent {

    /**
     * Přidá do sítě hranu mezi uzly.
     * 
     * @param network
     *            síť
     * @param name
     *            název nové hrany
     * @param from
     *            název výchozí uzel
     * @param to
     *            název cílový uzel
     */
    void addArc(Network network, NormalWord name, Node from, Node to);

    /**
     * Přidá do sítě hranu mezi uzly.
     * 
     * @param network
     *            síť
     * @param name
     *            název nové hrany
     * @param fromNodeName
     *            název výchozího uzlu
     * @param toNodeName
     *            název cílového uzlu
     */
    void addArc(Network network, NormalWord name, NormalWord fromNodeName,
            NormalWord toNodeName);

    /**
     * Přidá síť.
     * 
     * @param added
     *            přidávaná síť
     * @param name
     *            název přidávané sítě
     */
    void addNetwork(Network added, SystemName name);

    /**
     * Přidá síť.
     * 
     * @param name
     *            název sítě
     */
    void addNetwork(SystemName name);

    /**
     * Přidá výchozí typ uzlu do sítě.
     * 
     * @param network
     *            síť
     * @param x
     *            souřadnice umístění na ose x
     * @param y
     *            souřadnice umístění na ose y
     */
    void addNode(Network network, int x, int y);

    /**
     * Indikuje, zda-li je první uzel spojen s druhým hranou dané orientace.
     * 
     * @param first
     *            první uzel
     * @param second
     *            druhý uzel
     * @param direction
     *            orientace hrany
     * @return zda-li je první uzel spojen s druhým hranou dané orientace
     */
    boolean adjoins(Node first, Node second, Direction direction);

    /**
     * Indikuje, zda-li systém obsahuje zadanou síť.
     * 
     * @param network
     *            síť
     * @return zda-li systém obsahuje zadanou síť
     */
    boolean contains(Network network);

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    boolean equals(Object obj);

    /**
     * Vrátí hranu s daným názvem.
     * 
     * @param name
     *            název hrany
     * @return hrana
     */
    Arc getArc(NormalWord name);

    /**
     * Vrátí uzel na daném konci hrany.
     * 
     * @param arc
     *            hrana
     * @param direction
     *            konec hrany
     * @return uzel
     */
    Node getAttached(Arc arc, Direction direction);

    /**
     * Vrátí všechny dostupné vstupní uzly (možné vstupy pro zanoření výpočtu).
     * 
     * @return množina vstupních uzlů systému
     */
    Set<EnterNode> getAvailableReferences();

    /**
     * Vrátí hrany které jsou připojené k uzlu na daném kraji
     * 
     * @param node
     *            uzel
     * @param direction
     *            místo připojení hran
     * @return hrany
     */
    Set<Arc> getConnections(Node node, Direction direction);

    /**
     * Vrátí hrany směřující do uzlu.
     * 
     * @param node
     *            uzel
     * @return množina hran směřujících do uzlu
     */
    Set<Arc> getIns(Node node);

    /**
     * Vrátí maximální počet odchozích hran z uzlu.
     * 
     * @return maximální počet odchozích hran z uzlu
     */
    int getMaxBranchingFactor();

    /**
     * Vrátí síť daného názvu.
     * 
     * @param name
     *            název sítě
     * @return síť
     */
    Network getNetwork(SystemName name);

    /**
     * Vrátí název sítě.
     * 
     * @param network
     *            síť
     * @return název
     */
    SystemName getNetworkName(Network network);

    /**
     * Vrátí všechny sítě systému.
     * 
     * @return sítě systému
     */
    public Set<Network> getNetworks();

    /**
     * Vrátí uzel daného názvu.
     * 
     * @param name
     *            název uzlu
     * @return uzel
     */
    Node getNode(NormalWord name);

    /**
     * Vrátí uzly v síti.
     * 
     * @param network
     *            síť
     * @return všechny uzly v síti
     */
    Set<Node> getNodes(Network network);

    /**
     * Vrátí hrany směřující ven z uzlu.
     * 
     * @param node
     *            uzel
     * @return množina hran směřujících z uzlu
     */
    Set<Arc> getOuts(Node node);

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

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    int hashCode();

    /**
     * Změní hranu.
     * 
     * @param changed
     *            hrana
     * @param newName
     *            nový název
     * @param priority
     *            nová priorita
     * @param type
     *            nový typ
     * @param arguments
     *            specifické argumenty podporovaného typu
     */
    void changeArc(Arc changed, NormalWord newName, Priority priority,
            Class<? extends Arc> type, Object... arguments);

    /**
     * Změní (nestrukturální, tj. nezávislý na umístění v grafu) typ uzlu.
     * 
     * @param node
     *            uzel
     * @param type
     *            nový typ uzlu
     */
    void
            changeNode(final Node node,
                    final Class<? extends FunctionalNode> type);

    /**
     * Změní umístění uzlu.
     * 
     * @param node
     *            uzel
     * @param x
     *            souřadnice x
     * @param y
     *            souřadnice y
     */
    void changeNode(Node node, final int x, final int y);

    /**
     * Změní název uzlu.
     * 
     * @param node
     *            uzel
     * @param newName
     *            nový název
     */
    void changeNode(final Node node, final NormalWord newName);

    /**
     * Změní (nestrukturální, tj. nezávislý na umístění v grafu) typ uzlu.
     * 
     * @param name
     *            název uzlu
     * @param type
     *            nový typ uzlu
     */
    void changeNode(NormalWord name, Class<? extends FunctionalNode> type);

    /**
     * Změní umístění uzlu.
     * 
     * @param name
     *            název uzlu
     * @param x
     *            souřadnice x
     * @param y
     *            souřadnice y
     */
    void changeNode(NormalWord name, int x, int y);

    /**
     * Změní název uzlu.
     * 
     * @param name
     *            název uzlu
     * @param newName
     *            nový název
     */
    void changeNode(NormalWord name, NormalWord newName);

    /**
     * Odstraní hranu ze systému.
     * 
     * @param removed
     *            hrana
     */
    void removeArc(Arc removed);

    /**
     * Odstraní hranu ze systému.
     * 
     * @param name
     *            název hrany
     */
    void removeArc(NormalWord name);

    /**
     * Odstraní síť.
     * 
     * @param removed
     *            síť
     */
    void removeNetwork(Network removed);

    /**
     * Odstraní síť.
     * 
     * @param name
     *            název sítě
     */
    void removeNetwork(SystemName name);

    /**
     * Odstraní uzel ze systému.
     * 
     * @param removed
     *            uzel
     */
    void removeNode(Node removed);

    /**
     * Odstraní uzel ze systému.
     * 
     * @param name
     *            název uzlu
     */
    void removeNode(NormalWord name);

    /**
     * Přejmenuje síť.
     * 
     * @param network
     *            síť
     * @param newName
     *            nový název sítě
     */
    void renameNetwork(Network network, SystemName newName);

    /**
     * Nastaví rozesílač událostí.
     * 
     * @param dispatcher
     *            rozesílač událostí
     */
    void setDispatcher(Dispatcher dispatcher);

    /**
     * Nastaví název systému.
     * 
     * @param newName
     *            nový název systému
     */
    void setName(SystemName newName);
}