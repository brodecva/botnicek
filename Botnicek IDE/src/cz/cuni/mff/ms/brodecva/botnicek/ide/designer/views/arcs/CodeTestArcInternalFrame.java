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
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

import org.bounce.text.ScrollableEditorPanel;
import org.bounce.text.xml.XMLEditorKit;
import org.bounce.text.xml.XMLStyleConstants;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.controllers.ArcController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.controllers.DummyArcController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.arcs.api.Processor;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public class CodeTestArcInternalFrame extends
        AbstractTestArcInternalFrame implements CodeTestArcView {
    
    private final Document testedCodeDocument;
    private final XMLEditorKit testedCodeKit = new XMLEditorKit();
    private final JEditorPane testedCodeEditorPane = new JEditorPane();
    private final ScrollableEditorPanel scrollableTestedCodeEditorPanel;
    private final JScrollPane testedCodeEditorScrollPane;
    
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    final CodeTestArcInternalFrame frame = new CodeTestArcInternalFrame();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    {
        this.testedCodeKit.setAutoIndentation(true);
        this.testedCodeKit.setTagCompletion(true);
        
        this.testedCodeEditorPane.setEditorKit(this.testedCodeKit); 
        this.testedCodeEditorPane.setFont(new Font("Courier", Font.PLAIN, 12)); 
        
        this.testedCodeDocument = this.testedCodeEditorPane.getDocument();
        this.testedCodeDocument.putProperty(PlainDocument.tabSizeAttribute, new Integer(4));
        
        this.testedCodeKit.setStyle(XMLStyleConstants.ATTRIBUTE_NAME, new Color(255, 0, 0), Font.BOLD);
        
        this.scrollableTestedCodeEditorPanel = new ScrollableEditorPanel(this.testedCodeEditorPane);
        this.testedCodeEditorScrollPane = new JScrollPane(this.scrollableTestedCodeEditorPanel);
    }
    
    /**
     * 
     */
    public CodeTestArcInternalFrame() {
        this(new DummyArcController(), "DUMMYPATTERNARC", "DUMMYFROM", "DUMMYTO", 1, "<think>Dummy code</think>", "<get name=\"dummy\"> dummy text", "DUMMY * VALUE PATTERN");
    }
    
    public CodeTestArcInternalFrame(final ArcController arcController, final String name, final String fromNodeName, final String toNodeName, final int priority, final String code, final String testedCode, final String value) {
        this(arcController, name, name, fromNodeName, toNodeName, priority, code, testedCode, value);
    }
    
    public CodeTestArcInternalFrame(final ArcController arcController, final String originalName, final String name, final String fromNodeName, final String toNodeName, final int priority, final String code, final String testedCode, final String value) {
        super(arcController, originalName, name, fromNodeName, toNodeName, priority, code, value);
        
        initialize(testedCode);
    }
    
    private void initialize(final String testedCode) {
        this.testedCodeEditorPane.setText(testedCode);
        getContentPane().add(this.testedCodeEditorScrollPane);
    }
    
    public CodeTestArcInternalFrame(final AbstractTestArcInternalFrame testArcInternalFrame) {
        super(testArcInternalFrame);
        
        initialize("");
    }
    
    public CodeTestArcInternalFrame(final AbstractCodeArcInternalFrame arcCodeInternalFrame) {
        super(arcCodeInternalFrame);
        
        initialize("");
    }
    
    public CodeTestArcInternalFrame(final AbstractArcInternalFrame arcInternalFrame) {
        super(arcInternalFrame);
        
        initialize("");
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.AbstractArcInternalFrame#save()
     */
    @Override
    protected void save() {
        getArcController().updateCodeTest(getOriginalName(), getArcName(), getPriority(), getCode(), getTestedCode(), getValue());
    }

    /**
     * @return
     */
    protected String getTestedCode() {
        return this.testedCodeEditorPane.getText();
    }

    @Override
    public void updatedTested(final String testedCode) {
        Preconditions.checkNotNull(testedCode);
        
        this.testedCodeEditorPane.setText(testedCode);
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.arcs.api.Processible#accept(cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.arcs.api.Processor)
     */
    @Override
    public void accept(final Processor processor) {
        processor.process(this);
    }
}
