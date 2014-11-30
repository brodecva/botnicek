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
package cz.cuni.mff.ms.brodecva.botnicek.ide.project.views;

import java.util.Set;

import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.controllers.CheckController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.controllers.ArcPropertiesDisplayController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.controllers.NetworkDisplayController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.controllers.SystemController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.project.controllers.SettingsController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.controllers.BotSettingsController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.controllers.ConversationSettingsController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.controllers.LanguageSettingsController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.controllers.RunController;

/**
 * Pohled na projekt.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public interface ProjectView {
    /**
     * Zobrazí pohled na nastavení bota.
     * 
     * @param botSettingsController
     *            řadič nastavení bota
     */
    void botSettingsOpened(BotSettingsController botSettingsController);

    /**
     * Změní zobrazení prostředí po uzavření projektu.
     */
    void close();

    /**
     * Zobrazí pohled na nastavení konverzace s botem.
     * 
     * @param conversationSettingsController
     *            řadič nastavení konverzace
     */
    void conversationSettingsOpened(
            ConversationSettingsController conversationSettingsController);

    /**
     * Zobrazí pohled na nastavení jazyka běhového bota a běhového prostředí.
     * 
     * @param languageSettingsController
     *            řadič nastavení jazyka
     */
    void languageSettingsOpened(
            LanguageSettingsController languageSettingsController);

    /**
     * Zobrazí otevřený projekt, obsluhovaný danými řadiči.
     * 
     * @param systemController
     *            řadič systému
     * @param networkDisplayController
     *            řadič zobrazovače sítí
     * @param arcPropertiesDisplayController
     *            řadič zobrazovače vlastností
     * @param checkControllers
     *            množina řadičů ladění
     */
    void open(SystemController systemController,
            NetworkDisplayController networkDisplayController,
            ArcPropertiesDisplayController arcPropertiesDisplayController,
            Set<? extends CheckController<?>> checkControllers);

    /**
     * Zobrazí spuštěnou konverzaci.
     * 
     * @param runController
     *            řadič instance běžící konverzace
     */
    void run(RunController runController);

    /**
     * Zobrazí pohled na nastavení projektu.
     * 
     * @param settingsController
     *            řadič nastavení projektu
     */
    void settingsOpened(SettingsController settingsController);
}
