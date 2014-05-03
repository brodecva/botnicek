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
import java.util.List;

import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

import org.bounce.text.ScrollableEditorPanel;
import org.bounce.text.xml.XMLEditorKit;
import org.bounce.text.xml.XMLStyleConstants;

import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.controllers.ArcController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.controllers.DummyArcController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.elements.template.TemplateElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.types.SimplePattern;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.arcs.api.Processor;
import cz.cuni.mff.ms.brodecva.botnicek.ide.editor.checker.CheckResult;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public class TransitionArcInternalFrame extends
        AbstractCodeArcInternalFrame implements TransitionView {
    
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    final TransitionArcInternalFrame frame = new TransitionArcInternalFrame();
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
    public TransitionArcInternalFrame() {
        this(new DummyArcController(), "DUMMYPATTERNARC", "DUMMYFROM", "DUMMYTO", 1, "<think>Dummy code</think>");
    }
    
    public TransitionArcInternalFrame(final ArcController arcController, final String name, final String fromNodeName, final String toNodeName, final int priority, final String code) {
        this(arcController, name, name, fromNodeName, toNodeName, priority, code);
    }
    
    public TransitionArcInternalFrame(final ArcController arcController, final String originalName, final String name, final String fromNodeName, final String toNodeName, final int priority, final String code) {
        super(arcController, originalName, name, fromNodeName, toNodeName, priority, code);
    }
    
    public TransitionArcInternalFrame(final AbstractCodeArcInternalFrame codeArcInternalFrame) {
        super(codeArcInternalFrame);
    }
    
    public TransitionArcInternalFrame(final AbstractArcInternalFrame arcInternalFrame) {
        super(arcInternalFrame);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.AbstractArcInternalFrame#save()
     */
    @Override
    protected void save() {
        getArcController().updateTransition(getOriginalName(), getArcName(), getPriority(), getCode());
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.arcs.api.Processible#accept(cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.arcs.api.Processor)
     */
    @Override
    public void accept(final Processor processor) {
        processor.process(this);
    }
}
