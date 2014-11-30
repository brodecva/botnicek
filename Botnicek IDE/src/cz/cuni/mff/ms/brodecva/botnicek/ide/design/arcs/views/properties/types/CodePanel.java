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
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

import org.bounce.text.ScrollableEditorPanel;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.Code;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.controllers.CheckController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.checker.Source;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.controllers.ArcController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.properties.elements.CodeEditorPane;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public class CodePanel extends AbstractPartPanel {

    private static final long serialVersionUID = 1L;

    private static final int CODE_EDITOR_MIN_WIDTH = 500;
    private static final int CODE_EDITOR_MIN_HEIGHT = 100;
    private static final Dimension CODE_EDITOR_DIMENSION = new Dimension(
            CODE_EDITOR_MIN_WIDTH, CODE_EDITOR_MIN_HEIGHT);

    private static CodePanel create() {
        return create(new Source() {
        }, DummyArcController.create(), DummyCodeValidationController.create());
    }

    /**
     * Vytvoří panel.
     * 
     * @param parent
     *            rodič
     * @param arcController
     *            řadič vlastností hrany
     * @param codeValidationController
     *            řadič validace kódu
     * @return panel
     */
    public static CodePanel create(final Source parent,
            final ArcController arcController,
            final CheckController<? extends Code> codeValidationController) {
        Preconditions.checkNotNull(arcController);
        Preconditions.checkNotNull(codeValidationController);

        final CodeEditorPane codeEditorPane =
                CodeEditorPane.create(parent, codeValidationController);

        final CodePanel newInstance =
                new CodePanel(arcController, codeEditorPane);
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

                    final JPanel contentPane = new JPanel(new BorderLayout());
                    final CodePanel panel = CodePanel.create();
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

    private final CodeEditorPane codeEditorPane;

    private CodePanel(final ArcController arcController,
            final CodeEditorPane codeEditorPane) {
        Preconditions.checkNotNull(arcController);
        Preconditions.checkNotNull(codeEditorPane);

        this.arcController = arcController;
        this.codeEditorPane = codeEditorPane;

        final ScrollableEditorPanel codeScrollablEditorPanel =
                new ScrollableEditorPanel(this.codeEditorPane);
        final JScrollPane codeScrollPane =
                new JScrollPane(codeScrollablEditorPanel);

        codeScrollPane.setMinimumSize(CODE_EDITOR_DIMENSION);
        codeScrollPane.setPreferredSize(CODE_EDITOR_DIMENSION);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        add(codeScrollPane);
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
        this.codeEditorPane.clear();
    }

    /**
     * Vrátí kód.
     * 
     * @return kód
     */
    public String getCode() {
        return this.codeEditorPane.getText();
    };

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
     * #reset()
     */
    @Override
    public void reset(final Source client) {
        Preconditions.checkNotNull(client);

        this.codeEditorPane.reset(client);
    }

    private void unsubscribe() {
        this.arcController.removeView(this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.types.
     * AbstractPartPanel
     * #updatedCode(cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.Code)
     */
    @Override
    public void updatedCode(final Code code) {
        Preconditions.checkNotNull(code);

        this.codeEditorPane.setText(code.getText());
    }
}