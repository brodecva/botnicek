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

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.SimplePattern;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.controllers.CheckController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.checker.CheckResult;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.checker.Source;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.views.CheckView;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.properties.Clearable;

/**
 * Textové pole pro zápis prostého vzoru jazyka AIML.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public class SimplePatternTextField extends JTextField implements Clearable, CheckView {

    private static final long serialVersionUID = 1L;

    /**
     * Vytvoří textové pole.
     * 
     * @param client
     *            klient textového pole
     * @param validationController
     *            řadič validace prostého vzoru
     * @return textové pole
     */
    public static SimplePatternTextField create(final Source client,
            final CheckController<? extends SimplePattern> validationController) {
        Preconditions.checkNotNull(client);
        Preconditions.checkNotNull(validationController);

        final SimplePatternTextField newInstance =
                new SimplePatternTextField(client, validationController);

        newInstance.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void changedUpdate(final DocumentEvent e) {
                newInstance.checkDocument(e.getDocument());
            }

            @Override
            public void insertUpdate(final DocumentEvent e) {
                newInstance.checkDocument(e.getDocument());
            }

            @Override
            public void removeUpdate(final DocumentEvent e) {
                newInstance.checkDocument(e.getDocument());
            }
        });

        validationController.addView(newInstance);
        
        return newInstance;
    }

    private final CheckController<? extends SimplePattern> validationController;
    private final Source client;

    private SimplePatternTextField(
            final Source client,
            final CheckController<? extends SimplePattern> validationController) {
        this.client = client;
        this.validationController = validationController;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.properties.elements
     * .Clearable#clear()
     */
    @Override
    public void clear() {
        this.validationController.clear(this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.properties.elements
     * .Clearable#reset()
     */
    @Override
    public void reset(final Source client) {
        Preconditions.checkNotNull(client);

        setText("");
        this.validationController.check(client, this, getText());
    }
    
    private void checkDocument(final Document document) {
        try {
            this.validationController.check(this.client, this,
                    document.getText(0, document.getLength()));
        } catch (final BadLocationException ex) {
            throw new IllegalStateException(ex);
        }
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.views.CheckView#closed()
     */
    @Override
    public void closed() {
        this.validationController.removeView(this);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.views.CheckView#updateResult(cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.checker.CheckResult)
     */
    @Override
    public void updateResult(final CheckResult result) {
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.views.CheckView#repeal()
     */
    @Override
    public void repeal() {
        checkDocument(getDocument());
    }
}
