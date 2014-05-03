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

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

import org.bounce.text.ScrollableEditorPanel;
import org.bounce.text.xml.XMLEditorKit;
import org.bounce.text.xml.XMLStyleConstants;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.java2s.code.java.swingcomponents.autocompletecombobox.Java2sAutoComboBox;

import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.controllers.ArcController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.controllers.DummyArcController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.elements.template.TemplateElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.types.SimplePattern;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.arcs.api.Processor;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public class RecurentArcInternalFrame extends
        AbstractTestArcInternalFrame implements RecurentArcView {
    
    private final Java2sAutoComboBox targetNameComboBox = new Java2sAutoComboBox(new LinkedList<>());;
    
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    final RecurentArcInternalFrame frame = new RecurentArcInternalFrame();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    /**
     * 
     */
    public RecurentArcInternalFrame() {
        this(new DummyArcController(), "DUMMYPATTERNARC", "DUMMYFROM", "DUMMYTO", 1, "<think>Dummy code</think>", "DUMMYTARGET", ImmutableSet.of("TARGET1", "DUMMYTARGET", "TARGET3"), "DUMMY * VALUE PATTERN");
    }
    
    public RecurentArcInternalFrame(final ArcController arcController, final String name, final String fromNodeName, final String toNodeName, final int priority, final String code, final String targetName, Set<String> availableTargets, final String value) {
        this(arcController, name, name, fromNodeName, toNodeName, priority, code, targetName, availableTargets, value);
    }
    
    public RecurentArcInternalFrame(final ArcController arcController, final String originalName, final String name, final String fromNodeName, final String toNodeName, final int priority, final String code, final String targetName, Set<String> availableTargets, final String value) {
        super(arcController, originalName, name, fromNodeName, toNodeName, priority, code, value);
        
        initialize(Optional.of(targetName), arcController.getAvailableTargets());
    }
    
    private void initialize(final Optional<String> targetName, final Set<String> availableTargets) {
        this.targetNameComboBox.setDataList(new LinkedList<>(availableTargets));
        
        if (availableTargets.isEmpty()) {
            return;
        }
        
        if (targetName.isPresent()) {
            this.targetNameComboBox.setSelectedItem(targetName);            
        } else {
            this.targetNameComboBox.setSelectedIndex(-1);
        }
        
        getContentPane().add(this.targetNameComboBox);
    }
    
    public RecurentArcInternalFrame(final AbstractTestArcInternalFrame testArcInternalFrame) {
        super(testArcInternalFrame);
        
        initialize(Optional.<String>absent(), testArcInternalFrame.getArcController().getAvailableTargets());
    }
    
    public RecurentArcInternalFrame(final AbstractCodeArcInternalFrame codeArcInternalFrame) {
        super(codeArcInternalFrame);
        
        initialize(Optional.<String>absent(), codeArcInternalFrame.getArcController().getAvailableTargets());
    }
    
    public RecurentArcInternalFrame(final AbstractArcInternalFrame arcInternalFrame) {
        super(arcInternalFrame);
        
        initialize(Optional.<String>absent(), arcInternalFrame.getArcController().getAvailableTargets());
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.AbstractArcInternalFrame#save()
     */
    @Override
    protected void save() {
        getArcController().updateRecurent(getOriginalName(), getArcName(), getPriority(), getCode(), getTargetName(), getValue());
    }

    /**
     * @return
     */
    protected String getTargetName() {
        final Object selected = this.targetNameComboBox.getSelectedItem();
        
        return (String) selected;
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.arcs.RecurentArcView#updateTargetName(java.lang.String)
     */
    @Override
    public void updatedTarget(final String targetName) {
        Preconditions.checkNotNull(targetName);
        Preconditions.checkArgument(!targetName.isEmpty());
        
        this.targetNameComboBox.setSelectedItem(targetName);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.arcs.api.Processible#accept(cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.arcs.api.Processor)
     */
    @Override
    public void accept(final Processor processor) {
        processor.process(this);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.arcs.ArcView#updatedAvailableReferences(java.util.Set)
     */
    @Override
    public void updatedAvailableReferences(final Set<String> references) {
        this.targetNameComboBox.setDataList(new LinkedList<>(references));
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.arcs.ArcView#extendedAvailableReferences(java.util.Set)
     */
    @Override
    public void extendedAvailableReferences(final Set<String> referenceNames) {
        @SuppressWarnings("unchecked")
        final Set<String> updated = new HashSet<>(this.targetNameComboBox.getDataList());
        updated.addAll(referenceNames);
        
        this.targetNameComboBox.setDataList(new LinkedList<>(updated));
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.arcs.ArcView#removedAvailableReferences(java.util.Set)
     */
    @Override
    public void removedAvailableReferences(Set<String> referenceNames) {
        @SuppressWarnings("unchecked")
        final Set<String> updated = new HashSet<>(this.targetNameComboBox.getDataList());
        updated.removeAll(referenceNames);
        
        this.targetNameComboBox.setDataList(new LinkedList<>(updated));
    }
}
