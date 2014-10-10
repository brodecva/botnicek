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
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.Visitable;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.api.Processible;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.types.Priority;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.Direction;

/**
 * Hrana sítě.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public interface Arc extends Processible, Visitable {
    /**
     * Porovná objekt s hranou. Shoduje se pouze pokud jde o hranu se stejnými
     * vlastnostmi a stejného typu.
     */
    @Override
    public boolean equals(Object obj);

    /**
     * Vrátí koncový uzel v daném směru.
     * 
     * @param direction
     *            směr
     * @return koncový uzel
     */
    Node getAttached(Direction direction);

    /**
     * Vrátí výchozí uzel.
     * 
     * @return výchozí uzel
     */
    Node getFrom();

    /**
     * Vrátí název hrany.
     * 
     * @return název hrany
     */
    NormalWord getName();

    /**
     * Vrátí rodičovskou síť.
     * 
     * @return rodičovská síť
     */
    Network getNetwork();

    /**
     * Vrátí prioritu hrany.
     * 
     * @return priorita
     */
    Priority getPriority();

    /**
     * Vrátí cílový uzel.
     * 
     * @return cílový uzel
     */
    Node getTo();

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode();

    /**
     * Zjistí, zda-li je v daném směru přichycena hrana k uzlu.
     * 
     * @param node
     *            uzel
     * @param direction
     *            směr
     * @return zda-li je v daném směru přichycena hrana k uzlu
     */
    boolean isAttached(Node node, Direction direction);

    /**
     * Zjistí, zda-li z uzlu vychází tato hrana.
     * 
     * @param node
     *            uzel
     * @return zda-li z něj vychází tato hrana
     */
    boolean isFrom(Node node);

    /**
     * Zjistí, zda-li do uzlu míří tato hrana.
     * 
     * @param node
     *            uzel
     * @return zda-li do něj míří tato hrana
     */
    boolean isTo(Node node);
}
