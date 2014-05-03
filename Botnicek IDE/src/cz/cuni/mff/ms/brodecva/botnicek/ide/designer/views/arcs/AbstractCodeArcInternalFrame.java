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
import java.awt.Font;
import java.util.List;

import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

import org.bounce.text.ScrollableEditorPanel;
import org.bounce.text.xml.XMLEditorKit;
import org.bounce.text.xml.XMLStyleConstants;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.controllers.ArcController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.elements.template.TemplateElement;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public abstract class AbstractCodeArcInternalFrame extends AbstractArcInternalFrame implements CodeArcView {
    private final XMLEditorKit codeKit;
    private final JEditorPane codeEditorPane = new JEditorPane();
    private final ScrollableEditorPanel scrollableCodeEditorPanel;
    private final JScrollPane codeEditorScrollPane;
    private final Document codeDocument;
    
    {
        this.codeKit = new XMLEditorKit(); 
        
        this.codeKit.setAutoIndentation(true);
        this.codeKit.setTagCompletion(true);
        
        this.codeEditorPane.setEditorKit(this.codeKit); 
        this.codeEditorPane.setFont(new Font("Courier", Font.PLAIN, 12)); 
        
        this.codeDocument = this.codeEditorPane.getDocument();
        this.codeDocument.putProperty(PlainDocument.tabSizeAttribute, new Integer(4));
        
        this.codeKit.setStyle(XMLStyleConstants.ATTRIBUTE_NAME, new Color(255, 0, 0), Font.BOLD);
        
        this.scrollableCodeEditorPanel = new ScrollableEditorPanel(this.codeEditorPane);
        this.codeEditorScrollPane = new JScrollPane(this.scrollableCodeEditorPanel);
    }
    
    protected AbstractCodeArcInternalFrame(final ArcController arcController, final String originalName, final String name, final String fromNodeName, final String toNodeName, final int priority, final String code) {
        super(arcController, originalName, name, fromNodeName, toNodeName, priority);
        
        initialize(code);
    }
    
    private void initialize(final String code) {        
        Preconditions.checkNotNull(code);
        
        this.codeEditorPane.setText(code);
        getContentPane().add(this.codeEditorScrollPane);
    }
    
    /**
     * @param codeArcInternalFrame
     */
    public AbstractCodeArcInternalFrame(
            AbstractCodeArcInternalFrame codeArcInternalFrame) {
        super(codeArcInternalFrame);
        
        initialize(codeArcInternalFrame.getCode());
    }
    
    public AbstractCodeArcInternalFrame(
            AbstractArcInternalFrame codeArcInternalFrame) {
        super(codeArcInternalFrame);
        
        initialize("");
    }

    /**
     * @return
     */
    protected String getCode() {
        return this.codeEditorPane.getText();
    }
    
    @Override
    public void updatedCode(final String code) {
        Preconditions.checkNotNull(code);
        
        this.codeEditorPane.setText(code);
    }
}
