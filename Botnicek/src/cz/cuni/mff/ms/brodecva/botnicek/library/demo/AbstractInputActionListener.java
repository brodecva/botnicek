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
package cz.cuni.mff.ms.brodecva.botnicek.library.demo;

import java.awt.event.ActionListener;

import cz.cuni.mff.ms.brodecva.botnicek.library.api.Session;

/**
 * Abstraktní třída posluchačů uživatelských vstupů do relace konverzace v GUI.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public abstract class AbstractInputActionListener implements ActionListener {
    
    /**
     * Relace, do které se zaznamenává vstup.
     */
    private final Session session;
    
    /**
     * Vstupní GUI.
     */
    private final Gui gui;

    /**
     * Vytvoří asynchronní GUI pro konverzaci.
     * 
     * @param session připojená konverzace
     * @param gui poslouchané GUI
     */
    public AbstractInputActionListener(final Session session, final Gui gui) {
        this.session = session;
        this.gui = gui;
    }

    /**
     * Vrátí aktivní relaci.
     * 
     * @return aktivní relace
     */
    protected Session getSession() {
        return session;
    }

    /**
     * Vrátí poslouchané GUI.
     * 
     * @return poslouchané GUI
     */
    protected Gui getGui() {
        return gui;
    }
}
