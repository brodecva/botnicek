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
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.properties.elements;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import org.bounce.text.ScrollableEditorPanel;
import org.bounce.text.xml.XMLEditorKit;
import org.bounce.text.xml.XMLStyleConstants;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.check.code.controllers.CodeValidationController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.Source;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.properties.Clearable;

/**
 * Editovací panel kódu šablony jazyka AIML.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class CodeEditorPane extends JEditorPane implements Clearable {
    
    private static final long serialVersionUID = 1L;

    /**
     * Vytvoří editovací panel.
     * 
     * @return editor
     */
    public static CodeEditorPane create() {
        return create(new Source() {}, DummyCodeValidationController.create());
    }
    
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
                    
                    final JPanel contentPane = new JPanel(new BorderLayout());
                    
                    final JEditorPane editorPane = CodeEditorPane.create();
                    final ScrollableEditorPanel scrollableCodeEditorPanel = new ScrollableEditorPanel(editorPane);
                    final JScrollPane scrollPane = new JScrollPane(scrollableCodeEditorPanel);
                    contentPane.add(scrollPane, BorderLayout.CENTER);
                    
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
    
    /**
     * Vytvoří editovací panel a napojí jej na řadič validace.
     * 
     * @param client klient editoru
     * @param validationController řadič validace kódu
     * @return editovací panel
     */
    public static CodeEditorPane create(final Source client, final CodeValidationController validationController) {
        Preconditions.checkNotNull(client);
        Preconditions.checkNotNull(validationController);
        
        final XMLEditorKit codeKit = new XMLEditorKit(); 
        codeKit.setAutoIndentation(true);
        codeKit.setTagCompletion(true);
        codeKit.setStyle(XMLStyleConstants.ATTRIBUTE_NAME, new Color(255, 0, 0), Font.BOLD);
        
        final CodeEditorPane newInstance = new CodeEditorPane(validationController);
        
        newInstance.setEditorKit(codeKit); 
        newInstance.setFont(new Font("Courier", Font.PLAIN, 12));
        
        final Document document = newInstance.getDocument();
        document.putProperty(PlainDocument.tabSizeAttribute, new Integer(4));
        document.addDocumentListener(new DocumentListener() {
            
            @Override
            public void removeUpdate(final DocumentEvent e) {
                try {
                    final Document document = e.getDocument();
                    validationController.check(client, newInstance, document.getText(0, document.getLength()));
                } catch (final BadLocationException ex) {
                    throw new IllegalStateException(ex);
                }
            }
            
            @Override
            public void insertUpdate(final DocumentEvent e) {
                try {
                    final Document document = e.getDocument();
                    validationController.check(client, newInstance, document.getText(0, document.getLength()));
                } catch (final BadLocationException ex) {
                    throw new IllegalStateException(ex);
                }
            }
            
            @Override
            public void changedUpdate(final DocumentEvent e) {
                try {
                    final Document document = e.getDocument();
                    validationController.check(client, newInstance, document.getText(0, document.getLength()));
                } catch (final BadLocationException ex) {
                    throw new IllegalStateException(ex);
                }
            }
        });
        
        return newInstance;
    }

    private final CodeValidationController validationController;
    
    private CodeEditorPane(final CodeValidationController validationController) {
        this.validationController = validationController;
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.properties.elements.Clearable#clear()
     */
    @Override
    public void clear() {
        this.validationController.clear(this);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.properties.elements.Clearable#reset()
     */
    @Override
    public void reset(final Source client) {
        Preconditions.checkNotNull(client);
        
        setText("");
        this.validationController.check(client, this, getText());
    }
}
