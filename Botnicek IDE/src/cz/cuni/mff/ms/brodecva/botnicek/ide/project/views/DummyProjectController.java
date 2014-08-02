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
package cz.cuni.mff.ms.brodecva.botnicek.ide.project.views;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;

import cz.cuni.mff.ms.brodecva.botnicek.ide.project.controllers.ProjectController;

/**
 * Atrapa řadiče.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
final class DummyProjectController implements ProjectController {

    /**
     * Vytvoří atrapu.
     * 
     * @return atrapa
     */
    public static DummyProjectController create() {
        return new DummyProjectController();
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.utils.mvc.Controller#addView(java.lang.Object)
     */
    @Override
    public void addView(ProjectView view) {
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.utils.mvc.Controller#removeView(java.lang.Object)
     */
    @Override
    public void removeView(ProjectView view) {
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.projects.controllers.ProjectController#create(java.lang.String)
     */
    @Override
    public void createNew(String name) {
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.projects.controllers.ProjectController#save(java.io.File)
     */
    @Override
    public void save(Path projectPath) throws IOException {
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.projects.controllers.ProjectController#open(java.io.File)
     */
    @Override
    public void open(Path projectPath) throws FileNotFoundException,
            ClassNotFoundException, IOException {
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.projects.controllers.ProjectController#export(java.io.File)
     */
    @Override
    public void export(Path location) throws IOException {
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.projects.controllers.ProjectController#isOpen()
     */
    @Override
    public boolean isOpen() {
        return false;
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.utils.mvc.Controller#fill(java.lang.Object)
     */
    @Override
    public void fill(ProjectView view) {
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.projects.controllers.ProjectController#openSettings()
     */
    @Override
    public void openSettings() {
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.projects.controllers.ProjectController#openBotSettings()
     */
    @Override
    public void openBotSettings() {
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.projects.controllers.ProjectController#openLanguageSettings()
     */
    @Override
    public void openLanguageSettings() {
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.projects.controllers.ProjectController#openConversationSettings()
     */
    @Override
    public void openConversationSettings() {
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.projects.controllers.ProjectController#run()
     */
    @Override
    public void test() {
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.projects.controllers.ProjectController#close()
     */
    @Override
    public void close() {
    }
}
