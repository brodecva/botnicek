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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.MixedPattern;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.Source;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.mixedpattern.controllers.MixedPatternValidationController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.controllers.ArcController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.properties.elements.MixedPatternTextField;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.resources.UiLocalizer;

import javax.swing.BoxLayout;

/**
 * Panel pro nastavení typu hrany, který testuje vzory kategorie jazyka AIML.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public class PatternArcPanel extends AbstractTypePanel {

    private static final long serialVersionUID = 1L;

    private static final int SMALL_GAP = 5;

    private static final int TEXT_FIELD_WIDTH = 100;
    private static final int TEXT_FIELD_HEIGHT = 20;
    private static final Dimension TEXT_FIELD_REQUIRED_DIMENSION = new Dimension(TEXT_FIELD_WIDTH, TEXT_FIELD_HEIGHT);

    private static final Dimension TEXT_FIELD_CONSTRAINT_DIMENSION = new Dimension(Short.MAX_VALUE, TEXT_FIELD_HEIGHT);

    private static final Dimension HORIZONTAL_SMALL_GAP_DIMENSION = new Dimension(SMALL_GAP, 0);

    private static final Dimension VERTICAL_SMALL_GAP_DIMENSION = new Dimension(0, SMALL_GAP);
    
    private final ArcController arcController;
    
    private final JPanel patternPane = new JPanel();
    private final JLabel patternLabel = new JLabel(UiLocalizer.print("Pattern"));
    private final MixedPatternTextField patternTextField;
    
    private final JPanel thatPane = new JPanel();
    private final JLabel thatLabel = new JLabel(UiLocalizer.print("ThatPattern"));
    private final MixedPatternTextField thatTextField;
    
    /**
     * Spustí testovací verzi.
     * 
     * @param args argumenty
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    final PatternArcPanel panel = PatternArcPanel.create();
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
    
    private static PatternArcPanel create() {
        return create(new Source() {}, DummyArcController.create(), DummyMixedPatternValidationController.create());
    }
    
    /**
     * Vytvoří panel.
     * 
     * @param parent rodič
     * @param arcController řadič vlastností hrany
     * @param mixedPatternValidationController řadič pro validaci vzorů
     * @return panel
     */
    public static PatternArcPanel create(final Source parent, final ArcController arcController, final MixedPatternValidationController mixedPatternValidationController) {
        Preconditions.checkNotNull(parent);
        Preconditions.checkNotNull(arcController);
        Preconditions.checkNotNull(mixedPatternValidationController);
        
        final MixedPatternTextField patternTextField = MixedPatternTextField.create(parent, mixedPatternValidationController);
        final MixedPatternTextField thatTextField = MixedPatternTextField.create(parent, mixedPatternValidationController);
        
        final PatternArcPanel newInstance = new PatternArcPanel(arcController, patternTextField, thatTextField);
        
        arcController.addView(newInstance);
        arcController.fill(newInstance);
        
        return newInstance;
    }
    
    private PatternArcPanel(final ArcController arcController, final MixedPatternTextField patternTextField, final MixedPatternTextField thatTextField) {
        Preconditions.checkNotNull(arcController);
        Preconditions.checkNotNull(patternTextField);
        Preconditions.checkNotNull(thatTextField);
        
        this.arcController = arcController;
        this.patternTextField = patternTextField;
        this.thatTextField = thatTextField;
        
        this.patternTextField.setMinimumSize(TEXT_FIELD_REQUIRED_DIMENSION);
        this.patternTextField.setMaximumSize(TEXT_FIELD_CONSTRAINT_DIMENSION);
        this.patternTextField.setPreferredSize(TEXT_FIELD_REQUIRED_DIMENSION);
        
        this.patternPane.setLayout(new BoxLayout(this.patternPane, BoxLayout.X_AXIS));
        this.patternPane.add(this.patternLabel);
        this.patternPane.add(Box.createRigidArea(HORIZONTAL_SMALL_GAP_DIMENSION));
        this.patternPane.add(this.patternTextField);
        
        this.thatTextField.setMinimumSize(TEXT_FIELD_REQUIRED_DIMENSION);
        this.thatTextField.setMaximumSize(TEXT_FIELD_CONSTRAINT_DIMENSION);
        this.thatTextField.setPreferredSize(TEXT_FIELD_REQUIRED_DIMENSION);       
        
        this.thatPane.setLayout(new BoxLayout(this.thatPane, BoxLayout.X_AXIS));
        this.thatPane.add(this.thatLabel);
        this.thatPane.add(Box.createRigidArea(HORIZONTAL_SMALL_GAP_DIMENSION));
        this.thatPane.add(this.thatTextField);
        
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        add(this.patternPane);
        add(Box.createRigidArea(VERTICAL_SMALL_GAP_DIMENSION));
        add(this.thatPane);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.types.TypeElement#save(cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord, java.lang.String, int, java.lang.String)
     */
    @Override
    public void save(final String newName, int priority, final String code) {
        Preconditions.checkNotNull(newName);
        Preconditions.checkNotNull(code);
        
        this.arcController.updatePattern(newName, priority, this.patternTextField.getText(), this.thatTextField.getText(), code);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.types.TypeElement#close()
     */
    @Override
    public void close() {
        unsubscribe();
    }
    
    public void updatedPattern(final MixedPattern pattern) {
        assert SwingUtilities.isEventDispatchThread();
        
        Preconditions.checkNotNull(pattern);
        
        this.patternTextField.setText(pattern.getText());
    }
    
    public void updatedThat(final MixedPattern that) {
        assert SwingUtilities.isEventDispatchThread();
        
        Preconditions.checkNotNull(that);
        
        this.thatTextField.setText(that.getText());
    }
    
    @Override
    public void removed() {
        unsubscribe();
        
        super.removed();
    }

    private void unsubscribe() {
        this.arcController.removeView(this);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.properties.types.TypeView#reset()
     */
    @Override
    public void reset(final Source client) {
        Preconditions.checkNotNull(client);
        
        this.patternTextField.reset(client);
        this.thatTextField.reset(client);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.properties.Clearable#clear()
     */
    @Override
    public void clear() {
        this.patternTextField.clear();
        this.thatTextField.clear();
    }
}
