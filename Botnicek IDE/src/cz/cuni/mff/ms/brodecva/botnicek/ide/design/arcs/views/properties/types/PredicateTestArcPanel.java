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

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import org.bounce.text.ScrollableEditorPanel;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.Code;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.SimplePattern;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.controllers.CheckController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.checker.Source;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.controllers.ArcController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.properties.elements.CodeEditorPane;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.properties.elements.NormalWordTextField;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.properties.elements.SimplePatternTextField;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.resources.UiLocalizer;

/**
 * Panel pro nastavení hrany, jež testuje hodnotu predikátu.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class PredicateTestArcPanel extends AbstractTypePanel {

    private static final long serialVersionUID = 1L;

    private static final int SMALL_GAP = 5;

    private static final int TEXT_FIELD_WIDTH = 100;

    private static final int TEXT_FIELD_HEIGHT = 20;

    private static final int CODE_EDITOR_MIN_WIDTH = 500;

    private static final int CODE_EDITOR_MIN_HEIGHT = 100;

    private static final Dimension TEXT_FIELD_REQUESTED_DIMENSION =
            new Dimension(TEXT_FIELD_WIDTH, TEXT_FIELD_HEIGHT);

    private static final Dimension TEXT_FIELD_CONSTRAINT_DIMENSION =
            new Dimension(Short.MAX_VALUE, TEXT_FIELD_HEIGHT);

    private static final Dimension HORIZONTAL_SMALL_GAP_DIMENSION =
            new Dimension(SMALL_GAP, 0);

    private static final Dimension VERTICAL_SMALL_GAP_DIMENSION =
            new Dimension(0, SMALL_GAP);

    private static final Dimension CODE_EDITOR_DIMENSION = new Dimension(
            CODE_EDITOR_MIN_WIDTH, CODE_EDITOR_MIN_HEIGHT);

    private static PredicateTestArcPanel create() {
        return create(new Source() {
        }, DummyArcController.create(), DummyCodeValidationController.create(),
                DummySimplePatternValidationController.create(),
                DummyNormalWordValidationController.create());
    }

    /**
     * Vytvoří panel.
     * 
     * @param parent
     *            rodič panelu
     * @param arcController
     *            řadič vlastnosti hrany
     * @param codeValidationController
     *            řadič validace přípravného kódu
     * @param simplePatternValidationController
     *            řadič validace očekávané hodnoty
     * @param predicateNameValidationController
     *            řadič validace názvu testovaného predikátu
     * @return panel
     */
    public static
            PredicateTestArcPanel
            create(final Source parent,
                    final ArcController arcController,
                    final CheckController<? extends Code> codeValidationController,
                    final CheckController<? extends SimplePattern> simplePatternValidationController,
                    final CheckController<? extends NormalWord> predicateNameValidationController) {
        Preconditions.checkNotNull(parent);
        Preconditions.checkNotNull(arcController);
        Preconditions.checkNotNull(codeValidationController);
        Preconditions.checkNotNull(simplePatternValidationController);

        final CodeEditorPane prepareCodeEditorPane =
                CodeEditorPane.create(parent, codeValidationController);
        final NormalWordTextField predicateTextField =
                NormalWordTextField.create(parent,
                        predicateNameValidationController);
        final SimplePatternTextField valueTextField =
                SimplePatternTextField.create(parent,
                        simplePatternValidationController);

        final PredicateTestArcPanel newInstance =
                new PredicateTestArcPanel(arcController, valueTextField,
                        prepareCodeEditorPane, predicateTextField);

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
                    final PredicateTestArcPanel panel =
                            PredicateTestArcPanel.create();
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
    private final CodeEditorPane prepareCodeEditorPane;

    private final JPanel predicatePane = new JPanel();
    private final JLabel predicateLabel = new JLabel(
            UiLocalizer.print("PredicateName"));
    private final NormalWordTextField predicateTextField;

    private final JPanel valuePane = new JPanel();

    private final JLabel valueLabel = new JLabel(
            UiLocalizer.print("ExpectedValue"));

    private final SimplePatternTextField valueTextField;

    private PredicateTestArcPanel(final ArcController arcController,
            final SimplePatternTextField valueTextField,
            final CodeEditorPane prepareCodeEditorPane,
            final NormalWordTextField predicateTextField) {
        Preconditions.checkNotNull(arcController);
        Preconditions.checkNotNull(valueTextField);
        Preconditions.checkNotNull(predicateTextField);
        Preconditions.checkNotNull(prepareCodeEditorPane);

        this.arcController = arcController;
        this.valueTextField = valueTextField;
        this.predicateTextField = predicateTextField;
        this.prepareCodeEditorPane = prepareCodeEditorPane;

        this.predicateTextField.setMinimumSize(TEXT_FIELD_REQUESTED_DIMENSION);
        this.predicateTextField.setMaximumSize(TEXT_FIELD_CONSTRAINT_DIMENSION);
        this.predicateTextField
                .setPreferredSize(TEXT_FIELD_REQUESTED_DIMENSION);

        this.valueTextField.setMinimumSize(TEXT_FIELD_REQUESTED_DIMENSION);
        this.valueTextField.setMaximumSize(TEXT_FIELD_CONSTRAINT_DIMENSION);
        this.valueTextField.setPreferredSize(TEXT_FIELD_REQUESTED_DIMENSION);

        this.predicatePane.setLayout(new BoxLayout(this.predicatePane,
                BoxLayout.X_AXIS));
        this.predicatePane.add(this.predicateLabel);
        this.predicatePane.add(Box
                .createRigidArea(HORIZONTAL_SMALL_GAP_DIMENSION));
        this.predicatePane.add(this.predicateTextField);

        this.valuePane
                .setLayout(new BoxLayout(this.valuePane, BoxLayout.X_AXIS));
        this.valuePane.add(this.valueLabel);
        this.valuePane.add(Box.createRigidArea(HORIZONTAL_SMALL_GAP_DIMENSION));
        this.valuePane.add(this.valueTextField);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        final ScrollableEditorPanel prepareCodeScrollableEditorPanel =
                new ScrollableEditorPanel(this.prepareCodeEditorPane);
        final JScrollPane prepareCodeScrollPane =
                new JScrollPane(prepareCodeScrollableEditorPanel);
        prepareCodeScrollPane.setMinimumSize(CODE_EDITOR_DIMENSION);
        prepareCodeScrollPane.setPreferredSize(CODE_EDITOR_DIMENSION);
        prepareCodeScrollPane.setBorder(BorderFactory
                .createTitledBorder(UiLocalizer.print("InitCode")));

        add(prepareCodeScrollPane);
        add(Box.createRigidArea(VERTICAL_SMALL_GAP_DIMENSION));
        add(this.predicatePane);
        add(Box.createRigidArea(VERTICAL_SMALL_GAP_DIMENSION));
        add(this.valuePane);
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
        this.predicateTextField.clear();
        this.prepareCodeEditorPane.clear();
        this.valueTextField.clear();
    }

    @Override
    public void removed() {
        unsubscribe();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.properties.types
     * .TypeView#reset()
     */
    @Override
    public void reset(final Source client) {
        Preconditions.checkNotNull(client);

        this.predicateTextField.reset(client);
        this.prepareCodeEditorPane.reset(client);
        this.valueTextField.reset(client);
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

        this.arcController.updatePredicateTest(newName, priority, code,
                this.prepareCodeEditorPane.getText(),
                this.predicateTextField.getText(),
                this.valueTextField.getText());
    }

    private void unsubscribe() {
        this.arcController.removeView(this);
    }

    @Override
    public void updatedPredicate(final NormalWord name) {
        assert SwingUtilities.isEventDispatchThread();

        Preconditions.checkNotNull(name);

        this.predicateTextField.setText(name.getText());
    }

    @Override
    public void updatedTested(final Code code) {
        assert SwingUtilities.isEventDispatchThread();

        Preconditions.checkNotNull(code);

        this.prepareCodeEditorPane.setText(code.getText());
    }

    @Override
    public void updatedValue(final SimplePattern value) {
        assert SwingUtilities.isEventDispatchThread();

        Preconditions.checkNotNull(value);

        this.valueTextField.setText(value.getText());
    }
}
