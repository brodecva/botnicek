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
package cz.cuni.mff.ms.brodecva.botnicek.ide.project.controllers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;

import cz.cuni.mff.ms.brodecva.botnicek.ide.project.views.ProjectView;
import cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.model.RunException;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.mvc.Controller;

/**
 * Řadič projektů.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public interface ProjectController extends Controller<ProjectView> {
    /**
     * Zavře projekt.
     */
    void close();

    /**
     * Vytvoří nový projekt.
     * 
     * @param name
     *            název projektu
     */
    void createNew(String name);

    /**
     * Vyexportuje interpretovatelné zdrojové kódy bota.
     * 
     * @param location
     *            adresář pro export
     * @throws IOException
     *             pokud dojde k chybě při zápisu exportovaných souborů
     */
    void export(Path location) throws IOException;

    /**
     * Indikuje, zda-li řadič právě obstarává otevřený projekt.
     * 
     * @return zda-li je otevřen projekt
     */
    boolean isOpen();

    /**
     * Otevře projekt.
     * 
     * @param projectPath
     *            cesta k projektovému souboru
     * @throws FileNotFoundException
     *             pokud zadaný soubor nelze nalézt
     * @throws ClassNotFoundException
     *             pokud je zadaný soubor nekompatibilní
     * @throws IOException
     *             pokud dojde k chybě při načítání
     */
    void open(Path projectPath) throws FileNotFoundException,
            ClassNotFoundException, IOException;

    /**
     * Otevře nastavení bota.
     */
    void openBotSettings();

    /**
     * Otevře nastavení konverzace.
     */
    void openConversationSettings();

    /**
     * Otevře nastavení jazyka.
     */
    void openLanguageSettings();

    /**
     * Otevře nastavení.
     */
    void openSettings();

    /**
     * Uloží projekt.
     * 
     * @param projectPath
     *            cesta k úložnému souboru
     * @throws IOException
     *             pokud dojde k chybě při zápisu
     */
    void save(Path projectPath) throws IOException;

    /**
     * Spustí testovací konverzaci.
     * 
     * @throws RunException
     *             pokud dojde k chybě při inicializaci konverzace
     */
    void test() throws RunException;
}
