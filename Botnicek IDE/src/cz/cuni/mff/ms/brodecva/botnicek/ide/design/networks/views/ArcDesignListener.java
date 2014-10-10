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
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.views;

import java.awt.event.MouseEvent;

import javax.swing.event.MouseInputListener;

/**
 * Návrhář hran sítě.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public interface ArcDesignListener extends MouseInputListener, Editable {

    /**
     * <p>
     * Poklepáním na uzel zahájí plánování hrany. Dalším poklepem na jiný uzel
     * pak vyvolá podnět k vytvoření hrany. Poklep mimo uzly konstrukci přeruší.
     * </p>
     */
    @Override
    void mouseClicked(final MouseEvent e);

    /**
     * <p>
     * Pohybem je aktualizována nakreslená spojnice výchozího uzlu a bodu
     * prostoru s grafem pod ukazatelem.
     * </p>
     */
    @Override
    void mouseMoved(MouseEvent e);

}