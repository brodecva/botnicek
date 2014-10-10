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
package cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing;

import javax.swing.UIManager;

import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.resources.UiLocalizer;

/**
 * Přeloží popisky komponent. Nezbytné pro nepodporované jazyky uživatelského
 * rozhraní nástroje Swing.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class Localization {

    /**
     * Přeloží užité komponenty.
     */
    public static void localize() {
        UIManager.put("FileChooser.openDialogTitleText",
                UiLocalizer.print("FileChooser.openDialogTitleText"));
        UIManager.put("FileChooser.saveDialogTitleText",
                UiLocalizer.print("FileChooser.saveDialogTitleText"));
        UIManager.put("FileChooser.lookInLabelText",
                UiLocalizer.print("FileChooser.lookInLabelText"));
        UIManager.put("FileChooser.saveInLabelText",
                UiLocalizer.print("FileChooser.saveInLabelText"));
        UIManager.put("FileChooser.upFolderToolTipText",
                UiLocalizer.print("FileChooser.upFolderToolTipText"));
        UIManager.put("FileChooser.homeFolderToolTipText",
                UiLocalizer.print("FileChooser.homeFolderToolTipText"));
        UIManager.put("FileChooser.newFolderToolTipText",
                UiLocalizer.print("FileChooser.newFolderToolTipText"));
        UIManager.put("FileChooser.listViewButtonToolTipText",
                UiLocalizer.print("FileChooser.listViewButtonToolTipText"));
        UIManager.put("FileChooser.detailsViewButtonToolTipText",
                UiLocalizer.print("FileChooser.detailsViewButtonToolTipText"));
        UIManager.put("FileChooser.fileNameHeaderText",
                UiLocalizer.print("FileChooser.fileNameHeaderText"));
        UIManager.put("FileChooser.fileSizeHeaderText",
                UiLocalizer.print("FileChooser.fileSizeHeaderText"));
        UIManager.put("FileChooser.fileTypeHeaderText",
                UiLocalizer.print("FileChooser.fileTypeHeaderText"));
        UIManager.put("FileChooser.fileDateHeaderText",
                UiLocalizer.print("FileChooser.fileDateHeaderText"));
        UIManager.put("FileChooser.fileAttrHeaderText",
                UiLocalizer.print("FileChooser.fileAttrHeaderText"));
        UIManager.put("FileChooser.fileNameLabelText",
                UiLocalizer.print("FileChooser.fileNameLabelText"));
        UIManager.put("FileChooser.filesOfTypeLabelText",
                UiLocalizer.print("FileChooser.filesOfTypeLabelText"));
        UIManager.put("FileChooser.openButtonText",
                UiLocalizer.print("FileChooser.openButtonText"));
        UIManager.put("FileChooser.openButtonToolTipText",
                UiLocalizer.print("FileChooser.openButtonToolTipText"));
        UIManager.put("FileChooser.saveButtonText",
                UiLocalizer.print("FileChooser.saveButtonText"));
        UIManager.put("FileChooser.saveButtonToolTipText",
                UiLocalizer.print("FileChooser.saveButtonToolTipText"));
        UIManager.put("FileChooser.directoryOpenButtonText",
                UiLocalizer.print("FileChooser.directoryOpenButtonText"));
        UIManager
                .put("FileChooser.directoryOpenButtonToolTipText", UiLocalizer
                        .print("FileChooser.directoryOpenButtonToolTipText"));
        UIManager.put("FileChooser.cancelButtonText",
                UiLocalizer.print("FileChooser.cancelButtonText"));
        UIManager.put("FileChooser.cancelButtonToolTipText",
                UiLocalizer.print("FileChooser.cancelButtonToolTipText"));
        UIManager.put("FileChooser.updateButtonText",
                UiLocalizer.print("FileChooser.updateButtonText"));
        UIManager.put("FileChooser.updateButtonToolTipText",
                UiLocalizer.print("FileChooser.updateButtonToolTipText"));
        UIManager.put("FileChooser.helpButtonText",
                UiLocalizer.print("FileChooser.helpButtonText"));
        UIManager.put("FileChooser.helpButtonToolTipText",
                UiLocalizer.print("FileChooser.helpButtonToolTipText"));
        UIManager.put("FileChooser.newFolderErrorText",
                UiLocalizer.print("FileChooser.newFolderErrorText"));
        UIManager.put("FileChooser.acceptAllFileFilterText",
                UiLocalizer.print("FileChooser.acceptAllFileFilterText"));
        UIManager.put("OptionPane.yesButtonText",
                UiLocalizer.print("OptionPane.yesButtonText"));
        UIManager.put("OptionPane.noButtonText",
                UiLocalizer.print("OptionPane.noButtonText"));
        UIManager.put("OptionPane.cancelButtonText",
                UiLocalizer.print("OptionPane.cancelButtonText"));
        UIManager.put("ProgressMonitor.progressText",
                UiLocalizer.print("ProgressMonitor.progressText"));
    }

    private Localization() {
    }
}
