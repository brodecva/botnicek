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
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.properties.types;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.checker.Source;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.controllers.ArcController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.resources.UiLocalizer;

/**
 * Panel pro hranu, která neprovádí žádný test.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public class TransitionArcPanel extends AbstractTypePanel {

    private static final long serialVersionUID = 1L;

    private static TransitionArcPanel create() {
        return create(DummyArcController.create());
    }

    /**
     * Vytvoří panel.
     * 
     * @param arcController
     *            řadič vlastností hrany
     * @return panel
     */
    public static TransitionArcPanel create(final ArcController arcController) {
        Preconditions.checkNotNull(arcController);

        final TransitionArcPanel newInstance =
                new TransitionArcPanel(arcController);
        newInstance.subscribe(arcController);

        return newInstance;
    }

    /**
     * Spustí testovací verzi.
     * 
     * @param args
     *            argumenty
     */
    public static void main(final String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    final TransitionArcPanel panel =
                            TransitionArcPanel.create();

                    final JPanel contentPane = new JPanel(new BorderLayout());
                    contentPane.add(panel, BorderLayout.CENTER);

                    final JFrame frame = new JFrame();
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    frame.add(contentPane);
                    frame.pack();
                    frame.setVisible(true);
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private final ArcController arcController;

    private final JLabel noticeLabel = new JLabel(
            UiLocalizer.print("TransitionNotice"));

    private TransitionArcPanel(final ArcController arcController) {
        Preconditions.checkNotNull(arcController);

        this.arcController = arcController;

        setLayout(new BorderLayout());
        this.noticeLabel.setHorizontalAlignment(JLabel.CENTER);
        add(this.noticeLabel, BorderLayout.CENTER);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.properties.Clearable
     * #clear()
     */
    @Override
    public void clear() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.types.ArcViewAdapter
     * #removed()
     */
    @Override
    public void removed() {
        unsubscribe();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.properties.Clearable
     * #reset(cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.Source)
     */
    @Override
    public void reset(final Source client) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.types.TypeElement
     * #save(cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord, int,
     * java.lang.String)
     */
    @Override
    public void
            save(final String newName, final int priority, final String code) {
        Preconditions.checkNotNull(newName);
        Preconditions.checkNotNull(code);

        this.arcController.updateTransition(newName, priority, code);
    }

    private void subscribe(final ArcController arcController) {
        this.arcController.addView(this);
        this.arcController.fill(this);
    }

    private void unsubscribe() {
        this.arcController.removeView(this);
    }
}
