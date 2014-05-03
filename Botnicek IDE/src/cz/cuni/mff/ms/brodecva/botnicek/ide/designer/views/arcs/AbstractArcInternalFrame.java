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
package cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.arcs;

import java.awt.CardLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeListener;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;

import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.controllers.ArcController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.controllers.NetworkController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.arcs.api.Processible;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.arcs.api.Processor;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public abstract class AbstractArcInternalFrame extends JInternalFrame implements ArcView, Processible {

    private static final int PRIORITY_SPINNER_MINIMUM = 0;
    private static final int PRIORITY_SPINNER_MAXIMUM = Integer.MAX_VALUE;
    private static final int PRIORITY_SPIINER_STEP = 1;
    private static final Map<Class<? extends AbstractArcInternalFrame>, String> DEFAULT_SUPPORTED_TYPES_TO_DESCRIPTIONS;
    
    static {
        DEFAULT_SUPPORTED_TYPES_TO_DESCRIPTIONS = ImmutableMap.<Class<? extends AbstractArcInternalFrame>, String>of(
                PatternArcInternalFrame.class, "Pattern test",
                PredicateTestArcInternalFrame.class, "Predicate test",
                CodeTestArcInternalFrame.class, "Code test",
                TransitionArcInternalFrame.class, "Transition",
                RecurentArcInternalFrame.class, "Recurent test");        
    }
    
    private final ArcController arcController;
    private String originalName;
    
    private final JTextField nameField;
    private final JTextField fromField;
    private final JTextField toField;
    private final JSpinner prioritySpinner;
    
    private final Map<Class<? extends AbstractArcInternalFrame>, JRadioButton> typesToRadios = new HashMap<>();
    private final ButtonGroup typeChangeButtonGroup = new ButtonGroup();
    
    private final JButton saveButton;
    
    protected AbstractArcInternalFrame(final ArcController arcController, final String originalName, final String name, final String fromNodeName, final String toNodeName, final int priority) {
        this(arcController, originalName, name, fromNodeName, toNodeName, priority, DEFAULT_SUPPORTED_TYPES_TO_DESCRIPTIONS);
    }
    
    protected AbstractArcInternalFrame(final ArcController arcController, final String originalName, final String name, final String fromNodeName, final String toNodeName, final int priority, final Map<Class<? extends AbstractArcInternalFrame>, String> supportedTypesToDescriptions) {
        Preconditions.checkNotNull(arcController);
        Preconditions.checkNotNull(name);
        Preconditions.checkNotNull(fromNodeName);
        Preconditions.checkNotNull(toNodeName);
        Preconditions.checkNotNull(supportedTypesToDescriptions);
        Preconditions.checkArgument(!originalName.isEmpty());
        Preconditions.checkArgument(!name.isEmpty());
        Preconditions.checkArgument(!fromNodeName.isEmpty());
        Preconditions.checkArgument(!toNodeName.isEmpty());
        Preconditions.checkArgument(PRIORITY_SPINNER_MINIMUM <= priority);
        Preconditions.checkArgument(PRIORITY_SPINNER_MAXIMUM >= priority);
        
        this.arcController = arcController;
        this.originalName = originalName;
        
        this.nameField = new JTextField(name);
        this.fromField = new JTextField(fromNodeName);
        this.toField = new JTextField(toNodeName);
        this.prioritySpinner = new JSpinner(new SpinnerNumberModel(priority, PRIORITY_SPINNER_MINIMUM, PRIORITY_SPINNER_MAXIMUM, PRIORITY_SPIINER_STEP));
        this.saveButton = new JButton("Uložit");
        for (final Entry<Class<? extends AbstractArcInternalFrame>, String> supported : supportedTypesToDescriptions.entrySet()) {
            final Class<? extends AbstractArcInternalFrame> type = supported.getKey();
            final JRadioButton typeChangeRadio = new JRadioButton(supported.getValue(), false);
            this.typesToRadios.put(type, typeChangeRadio);
            this.typeChangeButtonGroup.add(typeChangeRadio);
            typeChangeRadio.addActionListener(new ActionListener() {
                
                @Override
                public void actionPerformed(final ActionEvent e) {
                    if (!typeChangeRadio.isSelected()) {
                        return;
                    }
                    
                    final int typeChangeConfirmation = JOptionPane.showConfirmDialog(AbstractArcInternalFrame.this, "Do you really want to change the type of the arc? (The data unique to the arc type will be lost.)", "Arc type change confirmation", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (typeChangeConfirmation == JOptionPane.CANCEL_OPTION || typeChangeConfirmation == JOptionPane.NO_OPTION) {
                        return;
                    }
                    
                    AbstractArcInternalFrame.this.arcController.changeType(AbstractArcInternalFrame.this, type);
                }
            });
        }
        this.typesToRadios.get(this.getClass()).setSelected(true);
        
        this.saveButton.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(final ActionEvent e) {
                save();
            }
        });
        
        getContentPane().setLayout(new CardLayout(0, 0));
        
        getContentPane().add(this.nameField);
        getContentPane().add(this.fromField);
        getContentPane().add(this.toField);
        getContentPane().add(this.prioritySpinner);
        getContentPane().add(this.saveButton);
        
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
    }
    
    protected AbstractArcInternalFrame(final AbstractArcInternalFrame arcInternalFrame) {
        this(arcInternalFrame.arcController, arcInternalFrame.originalName, arcInternalFrame.getName(), arcInternalFrame.getFromNodeName(), arcInternalFrame.getToNodeName(), arcInternalFrame.getPriority());
    }
    
    protected ArcController getArcController() {
        return this.arcController;
    }

    /**
     * 
     */
    protected abstract void save();

    /**
     * @return
     */
    protected int getPriority() {
        return (int) this.prioritySpinner.getModel().getValue();
    }

    /**
     * @return
     */
    protected String getArcName() {
        return this.nameField.getText();
    }
    
    protected String getOriginalName() {
        return this.originalName;
    }
    
    protected String getFromNodeName() {
        return this.fromField.getName();
    }
    
    protected String getToNodeName() {
        return this.toField.getName();
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.arcs.ArcView#replace(cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.arcs.AbstractArcInternalFrame)
     */
    @Override
    public void replaced(AbstractArcInternalFrame transformed) {
        final Container parent = this.getParent();
        assert parent != null;
        
        this.dispose();
        parent.remove(this);
        
        parent.add(transformed);
        transformed.setVisible(true);
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.arcs.ArcView#updateName(java.lang.String)
     */
    @Override
    public void updatedName(final String name) {
        assert SwingUtilities.isEventDispatchThread();
        
        Preconditions.checkNotNull(name);
        Preconditions.checkArgument(!name.isEmpty());
        
        this.originalName = name;
        this.nameField.setText(name);
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.arcs.ArcView#updatePriority(int)
     */
    @Override
    public void updatedPriority(final int priority) {
        Preconditions.checkArgument(PRIORITY_SPINNER_MINIMUM <= priority);
        Preconditions.checkArgument(PRIORITY_SPINNER_MAXIMUM >= priority);
        
        this.prioritySpinner.setValue(priority);
    }
    

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.arcs.ArcView#updatedCode(java.lang.String)
     */
    @Override
    public void updatedCode(String code) {
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.arcs.ArcView#updatedPrepare(java.lang.String)
     */
    @Override
    public void updatedPrepare(String code) {
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.arcs.ArcView#updatedTested(java.lang.String)
     */
    @Override
    public void updatedTested(String code) {
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.arcs.ArcView#updatedValue(java.lang.String)
     */
    @Override
    public void updatedValue(String value) {
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.arcs.ArcView#updatedPredicate(java.lang.String)
     */
    @Override
    public void updatedPredicate(String name) {
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.arcs.ArcView#updatedTarget(java.lang.String)
     */
    @Override
    public void updatedTarget(String targetName) {
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.arcs.ArcView#updatedPattern(java.lang.String)
     */
    @Override
    public void updatedPattern(String pattern) {
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.arcs.ArcView#updatedThat(java.lang.String)
     */
    @Override
    public void updatedThat(String that) {
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.arcs.ArcView#updatedAvailableReferences(java.util.Set)
     */
    @Override
    public void updatedAvailableReferences(Set<String> references) {
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.arcs.ArcView#extendedAvailableReferences(java.util.Set)
     */
    @Override
    public void extendedAvailableReferences(Set<String> referenceNames) {
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.arcs.ArcView#removedAvailableReferences(java.util.Set)
     */
    @Override
    public void removedAvailableReferences(Set<String> referenceNames) {
    }
}
