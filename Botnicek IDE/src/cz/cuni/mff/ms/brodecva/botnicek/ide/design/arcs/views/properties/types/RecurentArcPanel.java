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
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.properties.types;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.checker.Source;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.controllers.ArcController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.controllers.AvailableReferencesController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.properties.elements.ReferenceHintingComboBox;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.EnterNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.resources.UiLocalizer;

/**
 * <p>
 * Panel s nastavením pro hranu, jejíž test spočívá v úspěšném výpočtu skrze
 * vnořenou síť.
 * </p>
 * <p>
 * Obsahuje volič vstupního uzlu pro vnoření.
 * </p>
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public class RecurentArcPanel extends AbstractTypePanel {

    private static final long serialVersionUID = 1L;

    private static final int SMALL_GAP = 5;

    private static final int REFERENCES_COMBO_BOX_WIDTH = 100;

    private static final int REFERENCES_COMBO_BOX_HEIGHT = 20;

    private static final Dimension REFERENCES_COMBO_BOX_DIMENSION =
            new Dimension(REFERENCES_COMBO_BOX_WIDTH,
                    REFERENCES_COMBO_BOX_HEIGHT);

    private static final Dimension REFERENCES_COMBO_BOX_CONSTRAINT_DIMENSION =
            new Dimension(Short.MAX_VALUE, REFERENCES_COMBO_BOX_HEIGHT);

    private static final Dimension HORIZONTAL_SMALL_GAP_DIMENSION =
            new Dimension(SMALL_GAP, 0);

    private static RecurentArcPanel create() {
        return create(DummyArcController.create(),
                DummyAvailableReferencesController.create());
    }

    /**
     * Vytvoří panel.
     * 
     * @param arcController
     *            řadič vlastností hrany
     * @param availableReferencesController
     *            řadič dostupných uzlů pro zanoření
     * @return panel
     */
    public static RecurentArcPanel create(final ArcController arcController,
            final AvailableReferencesController availableReferencesController) {
        Preconditions.checkNotNull(arcController);
        Preconditions.checkNotNull(availableReferencesController);

        final ReferenceHintingComboBox referencesComboxBox =
                ReferenceHintingComboBox.create();
        availableReferencesController.addView(referencesComboxBox);
        availableReferencesController.fill(referencesComboxBox);

        final RecurentArcPanel newInstance =
                new RecurentArcPanel(arcController,
                        availableReferencesController, referencesComboxBox);
        arcController.addView(newInstance);
        arcController.fill(newInstance);

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
                    final RecurentArcPanel panel = RecurentArcPanel.create();

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
    private final AvailableReferencesController availableReferencesController;

    private final JPanel referencesPane = new JPanel();

    private final JLabel referencesLabel = new JLabel(
            UiLocalizer.print("DiveInto"));

    private final ReferenceHintingComboBox referencesComboxBox;

    private RecurentArcPanel(final ArcController arcController,
            final AvailableReferencesController availableReferencesController,
            final ReferenceHintingComboBox referencesComboxBox) {
        Preconditions.checkNotNull(arcController);
        Preconditions.checkNotNull(availableReferencesController);
        Preconditions.checkNotNull(referencesComboxBox);

        this.arcController = arcController;
        this.availableReferencesController = availableReferencesController;
        this.referencesComboxBox = referencesComboxBox;

        this.referencesComboxBox.setMinimumSize(REFERENCES_COMBO_BOX_DIMENSION);
        this.referencesComboxBox
                .setMaximumSize(REFERENCES_COMBO_BOX_CONSTRAINT_DIMENSION);
        this.referencesComboxBox
                .setPreferredSize(REFERENCES_COMBO_BOX_DIMENSION);

        this.referencesPane.setLayout(new BoxLayout(this.referencesPane,
                BoxLayout.X_AXIS));
        this.referencesPane.add(this.referencesLabel);
        this.referencesPane.add(Box
                .createRigidArea(HORIZONTAL_SMALL_GAP_DIMENSION));
        this.referencesPane.add(this.referencesComboxBox);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        add(this.referencesPane);
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
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.types.TypeElementPanel
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

        this.arcController.updateRecurent(newName, priority, code,
                (EnterNode) this.referencesComboxBox.getSelectedItem());
    }

    private void unsubscribe() {
        this.arcController.removeView(this);
        this.availableReferencesController.removeView(this.referencesComboxBox);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.types.TypeElementPanel
     * #updatedTarget(cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.
     * EnterNode)
     */
    @Override
    public void updatedTarget(final EnterNode target) {
        assert SwingUtilities.isEventDispatchThread();

        Preconditions.checkNotNull(target);

        this.referencesComboxBox.setSelectedItem(target);
    }
}
