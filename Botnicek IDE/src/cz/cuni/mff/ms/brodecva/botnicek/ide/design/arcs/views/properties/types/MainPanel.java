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
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.Source;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.words.controllers.NormalWordValidationController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.controllers.ArcController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.properties.elements.NormalWordTextField;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.types.Priority;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.resources.UiLocalizer;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.layouts.WrapLayout;

/**
 * Panel s prvky společnými všem typům hran.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class MainPanel extends AbstractPartPanel {

    private static final long serialVersionUID = 1L;

    private static final int PRIORITY_SPINNER_MINIMUM = Priority.MIN_VALUE;
    private static final int PRIORITY_SPINNER_MAXIMUM = Priority.MAX_VALUE;
    private static final int PRIORITY_SPIINER_STEP = 1;
    private static final int DEFAULT_PRIORITY = Priority.DEFAULT;
    private static final int TEXT_FIELD_WIDTH = 100;
    private static final int TEXT_FIELD_HEIGHT = 20;
    private static final int LABEL_WIDTH = 50;
    private static final int LABEL_HEIGHT = 20;
    private static final int SPINNER_WIDTH = 50;
    private static final int SPINNER_HEIGHT = 20;
    private static final int BORDER_TOP = 0;
    private static final int BORDER_BOTTOM = 24;
    private static final int BORDER_SIDES = 0;
    private static final Dimension LABEL_DIMENSION = new Dimension(LABEL_WIDTH,
            LABEL_HEIGHT);
    private static final Dimension TEXT_FIELD_DIMENSION = new Dimension(
            TEXT_FIELD_WIDTH, TEXT_FIELD_HEIGHT);
    private static final Dimension SPINNER_DIMENSION = new Dimension(
            SPINNER_WIDTH, SPINNER_HEIGHT);

    static {
        assert DEFAULT_PRIORITY >= PRIORITY_SPINNER_MINIMUM;
        assert DEFAULT_PRIORITY <= PRIORITY_SPINNER_MAXIMUM;
    }

    private static MainPanel create() {
        return create(new Source() {
        }, DummyArcController.create(),
                DummyNormalWordValidationController.create());
    }

    /**
     * Vytvoří panel.
     * 
     * @param parent
     *            rodič
     * @param arcController
     *            řadič vlastností hrany
     * @param nameValidationController
     *            řadič validace názvu hrany
     * @return panel
     */
    public static MainPanel create(final Source parent,
            final ArcController arcController,
            final NormalWordValidationController nameValidationController) {
        Preconditions.checkNotNull(parent);
        Preconditions.checkNotNull(arcController);
        Preconditions.checkNotNull(nameValidationController);

        final NormalWordTextField nameTextField =
                NormalWordTextField.create(parent, nameValidationController);

        final MainPanel newInstance =
                new MainPanel(arcController, nameTextField);
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
                    UIManager.setLookAndFeel(UIManager
                            .getSystemLookAndFeelClassName());

                    final MainPanel panel = MainPanel.create();

                    final JPanel contentPane = new JPanel(new BorderLayout());
                    contentPane.add(panel, BorderLayout.CENTER);

                    final JFrame frame = new JFrame();
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    frame.setContentPane(contentPane);
                    frame.pack();
                    frame.setVisible(true);
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private final ArcController arcController;

    private final JPanel namePane = new JPanel();
    private final JLabel nameLabel = new JLabel(UiLocalizer.print("ArcName"));
    private final NormalWordTextField nameTextField;

    private final JPanel fromPane = new JPanel();
    private final JLabel fromLabel = new JLabel(UiLocalizer.print("FromNode"));
    private final JTextField fromTextField = new JTextField();

    private final JPanel toPane = new JPanel();
    private final JLabel toLabel = new JLabel(UiLocalizer.print("ToNode"));
    private final JTextField toTextField = new JTextField();

    private final JPanel priorityPane = new JPanel();

    private final JLabel priorityLabel = new JLabel(
            UiLocalizer.print("Priority"));

    private final JSpinner prioritySpinner = new JSpinner(
            new SpinnerNumberModel(DEFAULT_PRIORITY, PRIORITY_SPINNER_MINIMUM,
                    PRIORITY_SPINNER_MAXIMUM, PRIORITY_SPIINER_STEP));

    private MainPanel(final ArcController arcController,
            final NormalWordTextField nameTextField) {
        Preconditions.checkNotNull(arcController);
        Preconditions.checkNotNull(nameTextField);

        this.arcController = arcController;
        this.nameTextField = nameTextField;

        this.nameLabel.setLabelFor(this.nameTextField);
        this.nameLabel.setDisplayedMnemonic(KeyEvent.VK_N);
        this.fromLabel.setLabelFor(this.toTextField);
        this.fromLabel.setDisplayedMnemonic(KeyEvent.VK_F);
        this.toLabel.setLabelFor(this.fromTextField);
        this.toLabel.setDisplayedMnemonic(KeyEvent.VK_T);
        this.priorityLabel.setLabelFor(this.prioritySpinner);
        this.priorityLabel.setDisplayedMnemonic(KeyEvent.VK_P);

        this.fromTextField.setEnabled(false);
        this.toTextField.setEnabled(false);

        this.nameLabel.setPreferredSize(LABEL_DIMENSION);
        this.nameTextField.setMinimumSize(TEXT_FIELD_DIMENSION);
        this.nameTextField.setPreferredSize(TEXT_FIELD_DIMENSION);

        this.fromLabel.setPreferredSize(LABEL_DIMENSION);
        this.fromTextField.setMinimumSize(TEXT_FIELD_DIMENSION);
        this.fromTextField.setPreferredSize(TEXT_FIELD_DIMENSION);

        this.toLabel.setPreferredSize(LABEL_DIMENSION);
        this.toTextField.setMinimumSize(TEXT_FIELD_DIMENSION);
        this.toTextField.setPreferredSize(TEXT_FIELD_DIMENSION);

        this.priorityLabel.setPreferredSize(LABEL_DIMENSION);
        this.priorityLabel.setMinimumSize(LABEL_DIMENSION);
        this.prioritySpinner.setMinimumSize(SPINNER_DIMENSION);
        this.prioritySpinner.setMaximumSize(SPINNER_DIMENSION);
        this.prioritySpinner.setPreferredSize(SPINNER_DIMENSION);

        this.namePane.setLayout(new BoxLayout(this.namePane, BoxLayout.X_AXIS));
        this.namePane.add(this.nameLabel);
        this.namePane.add(this.nameTextField);

        this.fromPane.setLayout(new BoxLayout(this.fromPane, BoxLayout.X_AXIS));
        this.fromPane.add(this.fromLabel);
        this.fromPane.add(this.fromTextField);

        this.toPane.setLayout(new BoxLayout(this.toPane, BoxLayout.X_AXIS));
        this.toPane.add(this.toLabel);
        this.toPane.add(this.toTextField);

        this.priorityPane.setLayout(new BorderLayout());
        this.priorityPane.add(this.priorityLabel, BorderLayout.WEST);
        this.priorityPane.add(this.prioritySpinner, BorderLayout.EAST);

        setLayout(new WrapLayout(WrapLayout.LEADING));
        setBorder(BorderFactory.createEmptyBorder(BORDER_TOP, BORDER_SIDES,
                BORDER_BOTTOM, BORDER_SIDES));

        add(this.namePane);
        add(this.fromPane);
        add(this.toPane);
        add(this.priorityPane);
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
        this.nameTextField.clear();
    }

    /**
     * Vrátí zadaný název.
     * 
     * @return název
     */
    public String getNewName() {
        return this.nameTextField.getText();
    }

    /**
     * Vrátí zadanou prioritu.
     * 
     * @return priorita
     */
    public int getPriority() {
        return (Integer) this.prioritySpinner.getValue();
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
        this.arcController.removeView(this);

        super.removed();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.properties.Clearable
     * #reset()
     */
    @Override
    public void reset(final Source client) {
        Preconditions.checkNotNull(client);

        this.nameTextField.reset(client);
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.types.
     * AbstractPartPanel
     * #updatedFrom(cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord)
     */
    @Override
    public void updateFrom(final NormalWord from) {
        assert SwingUtilities.isEventDispatchThread();

        Preconditions.checkNotNull(from);

        this.fromTextField.setText(from.getText());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.types.ArcViewAdapter
     * #updatedName(java.lang.String)
     */
    @Override
    public void updateName(final NormalWord name) {
        assert SwingUtilities.isEventDispatchThread();

        Preconditions.checkNotNull(name);

        this.nameTextField.setText(name.getText());
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.types.
     * AbstractPartPanel
     * #updatedPriority(cz.cuni.mff.ms.brodecva.botnicek.ide.design
     * .types.Priority)
     */
    @Override
    public void updatePriority(final Priority priority) {
        assert SwingUtilities.isEventDispatchThread();

        this.prioritySpinner.setValue(priority.getValue());
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.types.
     * AbstractPartPanel
     * #updatedTo(cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord)
     */
    @Override
    public void updateTo(final NormalWord to) {
        assert SwingUtilities.isEventDispatchThread();

        Preconditions.checkNotNull(to);

        this.toTextField.setText(to.getText());
    }
}
