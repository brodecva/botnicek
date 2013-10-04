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
package cz.cuni.mff.ms.brodecva.botnicek.library.demo.alice;

import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import cz.cuni.mff.ms.brodecva.botnicek.library.api.AIMLSession;
import cz.cuni.mff.ms.brodecva.botnicek.library.api.ConfigurationException;
import cz.cuni.mff.ms.brodecva.botnicek.library.api.Session;
import cz.cuni.mff.ms.brodecva.botnicek.library.api.SessionException;
import cz.cuni.mff.ms.brodecva.botnicek.library.demo.BlockingInputActionListener;
import cz.cuni.mff.ms.brodecva.botnicek.library.demo.Gui;
import cz.cuni.mff.ms.brodecva.botnicek.library.demo.GuiXhtml;
import cz.cuni.mff.ms.brodecva.botnicek.library.logging.BotnicekLogger;

/**
 * GUI demonstrační aplikace s asynchronními reakcemi na vstupy a zobrazením čistého textu.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class MainXhtml {
    
    /**
     * Logger.
     */
    private static final Logger LOGGER = BotnicekLogger
            .getLogger(MainXhtml.class);
    
    /**
     * Main metoda.
     * 
     * @param args
     *            argumenty příkazové řádky
     */
    public static void main(final String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    // Pokud není zadáno, použije se výchozí umístění demonstračního robota.
                    final String configPath = System.getProperty("location", "demo/alice");
                    
                    // Nastavení loggeru mimo konfigurační soubor pro odstranění nutnosti definovat parametry VM.
                    if (System.getProperty("java.util.logging.config.file") == null) {
                        LogManager.getLogManager().readConfiguration(BotnicekLogger.class.getResourceAsStream("run.properties"));                        
                    }
                    
                    final Session session = AIMLSession.start(Paths.get(configPath));
                    final Gui gui = new GuiXhtml(session.getBot().getName());
                    
                    final ActionListener listener = new BlockingInputActionListener(session, gui);
                    
                    gui.createAndShowGUI(listener);
                    
                    LOGGER.log(Level.INFO, "demo.alice.GuiCreatedAndShown", new Object[] { gui, session });
                } catch (IOException | ConfigurationException
                        | SessionException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    /**
     * Skrytý konstruktor.
     */
    private MainXhtml() {
    }
}
