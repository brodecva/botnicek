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
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model;

import java.util.Set;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.DispatchProcessible;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.ProceedProcessible;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.StackProcessible;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.Visitable;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.Direction;


/**
 * Rozhraní uzlu poskytuje metody pro analýzu jeho okolí, místa v systému a zjištění jeho vlastností.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public interface Node extends Visitable, DispatchProcessible, ProceedProcessible, StackProcessible {
    
    /**
     * Vrátí název uzlu.
     * 
     * @return název uzlu
     */
    NormalWord getName();
    
    /**
     * Vrátí souřadnici polohy uzlu na ose x.
     * 
     * @return souřadnice polohy uzlu na ose x
     */
    int getX();
    
    /**
     * Vrátí souřadnici polohy uzlu na ose y.
     * 
     * @return souřadnice polohy uzlu na ose y
     */
    int getY();
    
    /**
     * Vrátí související hrany v daném směru. 
     * 
     * @param direction konec, kterým jsou připojeny
     * @return související hrany
     */
    Set<Arc> getConnections(Direction direction);
    
    /**
     * Vrátí odchozí hrany z uzlu.
     * 
     * @return odchozí hrany
     */
    Set<Arc> getOuts();
    
    /**
     * Vrátí příchozí hrany do uzlu.
     * 
     * @return příchozí hrany
     */
    Set<Arc> getIns();
    
    /**
     * Indikuje, zda-li uzel v daném směru sousedí s tímto.
     * 
     * @param node uzel sítě 
     * @param direction směr
     * @return zda-li uzel v daném směru sousedí s tímto
     */
    boolean adjoins(Node node, Direction direction);
    
    /**
     * Indikuje, zda-li z tohoto uzlu vede hrana do zadaného.
     * 
     * @param node daný uzel
     * @return zda-li z tohoto uzlu vede hrana do zadaného
     */
    boolean pointsTo(Node node);
    
    /**
     * Indikuje, zda-li z daného uzlu vede hrana do tohoto.
     * 
     * @param node daný uzel
     * @return zda-li z daného uzlu vede hrana do tohoto
     */
    boolean isPointedAtBy(Node node);
    
    /**
     * Vrátí stupeň uzlu v dané orientaci.
     * 
     * @param direction směr
     * @return stupeň uzlu v dané orientaci
     */
    int getDegree(Direction direction);
    
    /**
     * Vrátí výstupní stupeň uzlu.
     * 
     * @return výstupní stupeň uzlu
     */
    int getOutDegree();
    
    /**
     * Vrátí vstupní stupeň uzlu.
     * 
     * @return vstupní stupeň uzlu
     */
    int getInDegree();
    
    /**
     * Vrátí rodičovskou síť uzlu.
     * 
     * @return rodičovská síť
     */
    Network getNetwork();
    
    /**
     * Porovná objekt s uzlem. Shoduje se pouze, pokud jde o uzel stejného typu, názvu a umístění.
     */
    @Override
    public boolean equals(Object obj);
    
    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode();
}
