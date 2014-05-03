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
public class PredicateTestArcInternalFrame extends
        AbstractTestArcInternalFrame implements PredicateTestArcView {
    
    private final JTextField predicateNameField = new JTextField();
    
    private final Document prepareCodeDocument;
    private final XMLEditorKit prepareCodeKit = new XMLEditorKit();
    private final JEditorPane prepareCodeEditorPane = new JEditorPane();
    private final ScrollableEditorPanel scrollablePrepareCodeEditorPanel;
    private final JScrollPane testedPrepareEditorScrollPane;
    
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    final PredicateTestArcInternalFrame frame = new PredicateTestArcInternalFrame();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    {
        this.prepareCodeKit.setAutoIndentation(true);
        this.prepareCodeKit.setTagCompletion(true);
        
        this.prepareCodeEditorPane.setEditorKit(this.prepareCodeKit); 
        this.prepareCodeEditorPane.setFont(new Font("Courier", Font.PLAIN, 12)); 
        
        this.prepareCodeDocument = this.prepareCodeEditorPane.getDocument();
        this.prepareCodeDocument.putProperty(PlainDocument.tabSizeAttribute, new Integer(4));
        
        this.prepareCodeKit.setStyle(XMLStyleConstants.ATTRIBUTE_NAME, new Color(255, 0, 0), Font.BOLD);
        
        this.scrollablePrepareCodeEditorPanel = new ScrollableEditorPanel(this.prepareCodeEditorPane);
        this.testedPrepareEditorScrollPane = new JScrollPane(this.scrollablePrepareCodeEditorPanel);
    }
    
    /**
     * 
     */
    public PredicateTestArcInternalFrame() {
        this(new DummyArcController(), "DUMMYPATTERNARC", "DUMMYFROM", "DUMMYTO", 1, "<think>Dummy code</think>", "<set name=\"dummy\">dummy text</set>", "dummy", "DUMMY * VALUE PATTERN");
    }
    
    public PredicateTestArcInternalFrame(final ArcController arcController, final String name, final String fromNodeName, final String toNodeName, final int priority, final String code, final String prepareCode, final String predicateName, final String value) {
        this(arcController, name, name, fromNodeName, toNodeName, priority, code, prepareCode, predicateName, value);
    }
    
    public PredicateTestArcInternalFrame(final ArcController arcController, final String originalName, final String name, final String fromNodeName, final String toNodeName, final int priority, final String code, final String prepareCode, final String predicateName, final String value) {
        super(arcController, originalName, name, fromNodeName, toNodeName, priority, code, value);
        
        initialize(prepareCode, predicateName);
    }
    
    private void initialize(final String prepareCode, final String predicateName) {
        this.prepareCodeEditorPane.setText(prepareCode);
        
        this.predicateNameField.setText(predicateName);
        
        getContentPane().add(this.testedPrepareEditorScrollPane);
        getContentPane().add(this.predicateNameField);
    }
    
    public PredicateTestArcInternalFrame(final AbstractTestArcInternalFrame testArcInternalFrame) {
        super(testArcInternalFrame);
        
        initialize("", "");
    }
    
    public PredicateTestArcInternalFrame(final AbstractCodeArcInternalFrame arcCodeInternalFrame) {
        super(arcCodeInternalFrame);
        
        initialize("", "");
    }
    
    public PredicateTestArcInternalFrame(final AbstractArcInternalFrame arcInternalFrame) {
        super(arcInternalFrame);
        
        initialize("", "");
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.AbstractArcInternalFrame#save()
     */
    @Override
    protected void save() {
        getArcController().updatePredicateTest(getOriginalName(), getArcName(), getPriority(), getCode(), getPrepareCode(), getName(), getValue());
    }

    /**
     * @return
     */
    protected String getNamee() {
        return this.predicateNameField.getText();
    }

    /**
     * @return
     */
    protected String getPrepareCode() {
        return this.prepareCodeEditorPane.getText();
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.arcs.api.Processible#accept(cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.arcs.api.Processor)
     */
    @Override
    public void accept(final Processor processor) {
        processor.process(this);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.arcs.PredicateTestArcView#updatePrepareCode(java.lang.String)
     */
    @Override
    public void updatedPrepare(String prepareCode) {
        Preconditions.checkNotNull(prepareCode);
        
        this.prepareCodeEditorPane.setText(prepareCode);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.arcs.PredicateTestArcView#updatePredicateName(java.lang.String)
     */
    @Override
    public void updatedPredicate(String predicateName) {
        Preconditions.checkNotNull(predicateName);
        
        this.predicateNameField.setText(predicateName);
    }
}
