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

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.RecurentArc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.EnterNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.RealignmentProcessor;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DirectedGraph;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.Direction;

/**
 * <p>Graf hran a uzlů systému. Kromě vztahů uzlů a hran ukládá i jejich názvy.</p>
 * <p>Dále podporuje opravy typů uzlů po strukturálních změnách.</p>
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public interface SystemGraph extends DirectedGraph<Node, Arc> {

    /**
     * Vrátí uzel daného názvu.
     * 
     * @param name název uzlu
     * @return uzel či {@code null}, pokud uzel daného názvu neexistuje
     */
    Node getVertex(final NormalWord name);

    /**
     * Vrátí hranu uzlu.
     * 
     * @param name název hrany
     * @return hrana či {@code null}, pokud hrana daného názvu neexistuje
     */
    Arc getEdge(final NormalWord name);

    /**
     * Odstraní uzel a opraví jeho nejbližší okolí (změny se dále nepropagují, neboť jsou určené změnou stupňů postižených vrcholů) tak, aby byl vzniklý graf opět konzistentní částí modelu ATN.
     * 
     * @param removed odstraněný uzel
     * @param processor procesor vypočítávající provedené nahrazení uzlů v okolí
     * @param references aktuální odkaz mezi uzly
     * @param initialNodes vstupní uzly
     * @return pokyny k aktualizaci systému na základě změny
     * @throws IllegalArgumentException pokud jsou odstraněný nebo změnou postižený uzel cílem odkazu, pak změnu nelze provést
     */
            Update
            removeAndRealign(
                    final Node removed,
                    final RealignmentProcessor processor,
                    final Map<? extends EnterNode, ? extends Collection<? extends RecurentArc>> references,
                    final Set<? extends EnterNode> initialNodes)
                    throws IllegalArgumentException;

    /**
     * Odstraní hranu a opraví koncové uzly (změny se dále nepropagují, neboť jsou určené změnou stupňů postižených vrcholů) tak, aby byl vzniklý graf opět konzistentní částí modelu ATN.
     * 
     * @param removed odstraněná hrana
     * @param processor procesor vypočítávající provedené nahrazení uzlů v okolí
     * @param references aktuální odkaz mezi uzly
     * @param initials vstupní uzly
     * @return pokyny k aktualizaci systému na základě změny
     * @throws IllegalArgumentException pokud je změnou postižený uzel cílem odkazu, pak nemůže být změněn
     */
            Update
            removeAndRealign(
                    final Arc removed,
                    final RealignmentProcessor processor,
                    final Map<? extends EnterNode, ? extends Collection<? extends RecurentArc>> references,
                    Set<? extends EnterNode> initials)
                    throws IllegalArgumentException;

    /**
     * Přidá hranu a opraví její koncové uzly (změny se dále nepropagují, neboť jsou určené změnou stupňů postižených vrcholů) tak, aby byl vzniklý graf opět konzistentní částí modelu ATN.
     * 
     * @param added přidaná hrana
     * @param from výchozí uzel hrany
     * @param to cílový uzel hrany
     * @param processor procesor vypočítávající provedené nahrazení uzlů v okolí
     * @param initials vstupní uzly
     * @return pokyny k aktualizaci systému na základě změny
     */
    Update addAndRealign(final Arc added, final Node from, final Node to,
            final RealignmentProcessor processor,
            final Set<? extends EnterNode> initials);

    /**
     * Vrátí hrany které jsou připojené k uzlu na daném kraji
     * 
     * @param vertex uzel
     * @param direction místo připojení hran
     * @return hrany
     */
    Set<Arc> connections(final Node vertex, final Direction direction);

    /**
     * Vrátí uzel na daném konci hrany.
     * 
     * @param arc hrana
     * @param direction konec hrany
     * @return uzel
     */
    Node attached(final Arc arc, final Direction direction);

    /**
     * Indikuje, zda-li je první uzel spojen s druhým hranou dané orientace. 
     * 
     * @param first první uzel
     * @param second druhý uzel
     * @param direction orientace hrany
     * @return zda-li je první uzel spojen s druhým hranou dané orientace
     */
    boolean adjoins(final Node first, final Node second,
            final Direction direction);
}