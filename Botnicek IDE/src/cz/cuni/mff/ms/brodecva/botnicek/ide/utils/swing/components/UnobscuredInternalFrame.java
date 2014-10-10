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
package cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.components;

import java.awt.CardLayout;
import java.awt.Dimension;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.Components;

/**
 * Interní rám, který lze posunout, aby nezakrýval sourozence po vytvoření.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public class UnobscuredInternalFrame extends JInternalFrame {

    private static final long serialVersionUID = 1L;

    private static final int X_OFFSET = 30;

    private static final int Y_OFFSET = 30;

    /**
     * Vytvoří vnitřní rám.
     * 
     * @return vnitřní rám
     */
    public static UnobscuredInternalFrame create() {
        return new UnobscuredInternalFrame();
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

            final JDesktopPane desktopPane = new JDesktopPane();
            final JPanel contentPane = new JPanel(new CardLayout());
            contentPane.setPreferredSize(new Dimension(300, 300));
            contentPane.setMinimumSize(new Dimension(300, 300));
            contentPane.add(desktopPane);

            final JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setContentPane(contentPane);
            frame.setSize(300, 300);
            frame.setVisible(true);

            for (int i = 0; i < 5; i++) {
                final UnobscuredInternalFrame child =
                        UnobscuredInternalFrame.create();
                child.setSize(new Dimension(133, 133));
                desktopPane.add(child);
                child.offset();
                child.show();
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Vytvoří vnitřní rám.
     */
    protected UnobscuredInternalFrame() {
        super();
    }

    private boolean foundFreeSpot(final int x, final int y,
            final JInternalFrame[] siblings) {
        for (final JInternalFrame sibling : siblings) {
            if (sibling != this && sibling.getX() == x && sibling.getY() == y) {
                return false;
            }
        }

        return true;
    }

    /**
     * Poskočí na volný násobek posuvu v rámci rodiče.
     */
    public final void offset() {
        final JDesktopPane desktop = getDesktopPane();
        if (!Components.hasParent(desktop)) {
            return;
        }

        final JInternalFrame[] siblings = desktop.getAllFrames();

        int x = getX();
        int y = getY();
        boolean found = foundFreeSpot(x, y, siblings);
        while (!found) {
            x += X_OFFSET;
            y += Y_OFFSET;

            found = foundFreeSpot(x, y, siblings);
        }

        setLocation(x, y);
    }
}
