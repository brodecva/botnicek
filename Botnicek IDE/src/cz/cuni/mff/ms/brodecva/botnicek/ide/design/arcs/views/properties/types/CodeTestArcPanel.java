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
import javax.swing.UIManager;

import org.bounce.text.ScrollableEditorPanel;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.Code;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.SimplePattern;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.code.controllers.CodeValidationController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.Source;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.simplepattern.controllers.SimplePatternValidationController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.controllers.ArcController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.properties.elements.CodeEditorPane;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.properties.elements.SimplePatternTextField;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.resources.UiLocalizer;

/**
 * Panel pro nastavení hrany, která testuje výstup kódu šablony oproti očekávané hodnotě.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public class CodeTestArcPanel extends AbstractTypePanel {

    private static final long serialVersionUID = 1L;

    private static final int CODE_EDITOR_WIDTH = 500;

    private static final int CODE_EDITOR_HEIGHT = 100;

    private static final int VALUE_TEXT_FIELD_WIDTH = 100;

    private static final int VALUE_TEXT_FIELD_HEIGHT = 20;

    private static final int SMALL_GAP = 5;

    private static final Dimension VALUE_REQUESTED_DIMENSION = new Dimension(VALUE_TEXT_FIELD_WIDTH, VALUE_TEXT_FIELD_HEIGHT);

    private static final Dimension VALUE_CONSTRAINED_DIMENSION = new Dimension(Short.MAX_VALUE, VALUE_TEXT_FIELD_HEIGHT);

    private static final Dimension CODE_EDITOR_DIMENSION = new Dimension(CODE_EDITOR_WIDTH, CODE_EDITOR_HEIGHT);

    private static final Dimension HORIZONTAL_SMALL_GAP_DIMENSION = new Dimension(SMALL_GAP, 0);

    private static final Dimension VERTICAL_SMALL_GAP_DIMENSION = new Dimension(0, SMALL_GAP);

    private final ArcController arcController;
    
    private final CodeEditorPane testedCodeEditorPane;
    
    private final JPanel valuePane = new JPanel();
    private final JLabel valueLabel = new JLabel(UiLocalizer.print("ExpectedValue"));
    private final SimplePatternTextField valueTextField;

    /**
     * Spustí testovací verzi.
     * 
     * @param args argumenty
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    
                    final CodeTestArcPanel panel = CodeTestArcPanel.create();
                    final JPanel contentPane = new JPanel(new BorderLayout());
                    contentPane.add(panel, BorderLayout.CENTER);
                    
                    final JFrame frame = new JFrame();
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.setContentPane(contentPane);
                    frame.pack();
                    frame.setVisible(true);
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    private static CodeTestArcPanel create() {
        return create(new Source() { }, DummyArcController.create(), DummyCodeValidationController.create(), DummySimplePatternValidationController.create());
    }
    
    /**
     * Vytvoří panel.
     * 
     * @param parent rodič
     * @param arcController řadič vlastností hrany
     * @param codeValidationController řadič validace kódu
     * @param simplePatternValidationController řadič validace očekávané hodnoty
     * @return panel
     */
    public static CodeTestArcPanel create(final Source parent, final ArcController arcController, final CodeValidationController codeValidationController, final SimplePatternValidationController simplePatternValidationController) {
        Preconditions.checkNotNull(parent);
        Preconditions.checkNotNull(arcController);
        Preconditions.checkNotNull(codeValidationController);
        Preconditions.checkNotNull(simplePatternValidationController);
        
        final SimplePatternTextField valueTextField = SimplePatternTextField.create(parent, simplePatternValidationController);
        final CodeEditorPane testedCodeEditorPane = CodeEditorPane.create(parent, codeValidationController);
        
        final CodeTestArcPanel newInstance = new CodeTestArcPanel(arcController, valueTextField, testedCodeEditorPane);
        
        arcController.addView(newInstance);
        arcController.fill(newInstance);
        
        return newInstance;
    }
    
    private CodeTestArcPanel(final ArcController arcController, final SimplePatternTextField valueTextField, final CodeEditorPane testedCodeEditorPane) {
        Preconditions.checkNotNull(arcController);
        Preconditions.checkNotNull(valueTextField);
        Preconditions.checkNotNull(testedCodeEditorPane);
        
        this.arcController = arcController;
        this.valueTextField = valueTextField;
        this.testedCodeEditorPane = testedCodeEditorPane;
        
        this.valueTextField.setMinimumSize(VALUE_REQUESTED_DIMENSION);
        this.valueTextField.setMaximumSize(VALUE_CONSTRAINED_DIMENSION);
        this.valueTextField.setPreferredSize(VALUE_REQUESTED_DIMENSION);
        
        this.valuePane.setLayout(new BoxLayout(this.valuePane, BoxLayout.X_AXIS));
        this.valuePane.add(this.valueLabel);
        this.valuePane.add(Box.createRigidArea(HORIZONTAL_SMALL_GAP_DIMENSION));
        this.valuePane.add(this.valueTextField);
        
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
       
        final ScrollableEditorPanel testedCodeScrollableEditorPanel = new ScrollableEditorPanel(this.testedCodeEditorPane);
        final JScrollPane testedCodeScrollPane = new JScrollPane(testedCodeScrollableEditorPanel); 
        testedCodeScrollPane.setMinimumSize(CODE_EDITOR_DIMENSION);
        testedCodeScrollPane.setPreferredSize(CODE_EDITOR_DIMENSION);        
        testedCodeScrollPane.setBorder(BorderFactory.createTitledBorder(UiLocalizer.print("ExpressionGeneratingCode")));
                
        add(testedCodeScrollPane);
        add(Box.createRigidArea(VERTICAL_SMALL_GAP_DIMENSION));
        add(this.valuePane);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.types.TypeElement#save(cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord, int, java.lang.String)
     */
    @Override
    public void save(final String newName, int priority, String code) {
        Preconditions.checkNotNull(newName);
        Preconditions.checkNotNull(code);
        
        this.arcController.updateCodeTest(newName, priority, code, this.valueTextField.getText(), this.testedCodeEditorPane.getText());
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.types.TypeElement#close()
     */
    @Override
    public void close() {
        unsubscribe();
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.types.AbstractTypePanel#updatedTested(cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.Code)
     */
    @Override
    public void updatedTested(final Code code) {
        assert SwingUtilities.isEventDispatchThread();
        
        Preconditions.checkNotNull(code);
        
        this.testedCodeEditorPane.setText(code.getText());
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.types.AbstractTypePanel#updatedValue(cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.SimplePattern)
     */
    @Override
    public void updatedValue(final SimplePattern value) {
        assert SwingUtilities.isEventDispatchThread();
        
        Preconditions.checkNotNull(value);
        
        this.valueTextField.setText(value.getText());
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.types.AbstractTypePanel#removed()
     */
    @Override
    public void removed() {
        assert SwingUtilities.isEventDispatchThread();
        
        unsubscribe();
    }

    private void unsubscribe() {
        this.arcController.removeView(this);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.properties.types.TypeView#reset()
     */
    @Override
    public void reset(Source client) {
        Preconditions.checkNotNull(client);
        
        this.valueTextField.reset(client);
        this.testedCodeEditorPane.reset(client);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.properties.Clearable#clear()
     */
    @Override
    public void clear() {
        this.valueTextField.clear();
        this.testedCodeEditorPane.clear();
    }
}
