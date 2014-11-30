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
package cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.components;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.Presence;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.resources.UiLocalizer;

/**
 * Dialog na výběr souboru, který si nechává potvrzovat přepsání.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class OverwriteCheckingFileChooser extends JFileChooser {

    private static final long serialVersionUID = 1L;

    /**
     * Vytvoří dialog pro výběr souboru, který potvrzuje vybrání existujícího
     * souboru.
     * 
     * @return dialog
     */
    public static OverwriteCheckingFileChooser create() {
        return new OverwriteCheckingFileChooser();
    }

    /**
     * Spustí testovací verzi.
     * 
     * @param args
     *            argumenty
     */
    public static void main(final String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            final JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setVisible(true);

            OverwriteCheckingFileChooser.create().showSaveDialog(frame);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    private OverwriteCheckingFileChooser() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.JFileChooser#approveSelection()
     */
    @Override
    public void approveSelection() {
        if (getDialogType() == SAVE_DIALOG) {
            final File selected = getSelectedFile();

            if (Presence.isPresent(selected) && selected.exists()) {
                final int response =
                        JOptionPane.showConfirmDialog(this, UiLocalizer.print(
                                "OvewriteFileMessage", selected.getName()),
                                UiLocalizer.print("OvewriteFileTitle"),
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.WARNING_MESSAGE);

                if (response != JOptionPane.YES_OPTION) {
                    return;
                }
            }
        }

        super.approveSelection();
    }
}
