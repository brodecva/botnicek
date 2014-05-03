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

import javax.swing.JTextField;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.controllers.ArcController;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public abstract class AbstractTestArcInternalFrame extends
        AbstractCodeArcInternalFrame implements TestArcView {
    
    private final JTextField valueField = new JTextField();
    
    protected AbstractTestArcInternalFrame(final ArcController arcController, final String originalName, final String name, final String fromNodeName, final String toNodeName, final int priority, final String code, final String value) {
        super(arcController, originalName, name, fromNodeName, toNodeName, priority, code);
        
        initialize(value);
    }
    
    private void initialize(final String value) {
        Preconditions.checkNotNull(value);
        Preconditions.checkArgument(!value.isEmpty());
        
        this.valueField.setText(value);
        
        getContentPane().add(this.valueField);
    }
    
    protected AbstractTestArcInternalFrame(final AbstractTestArcInternalFrame testArcInternalFrame) {
        super(testArcInternalFrame);
        
        initialize(testArcInternalFrame.getValue());
    }
    
    protected AbstractTestArcInternalFrame(final AbstractCodeArcInternalFrame codeArcInternalFrame) {
        super(codeArcInternalFrame);
        
        initialize("");
    }
    
    protected AbstractTestArcInternalFrame(final AbstractArcInternalFrame arcInternalFrame) {
        super(arcInternalFrame);
        
        initialize("");
    }

    /**
     * @return
     */
    protected final String getValue() {
        return this.valueField.getText();
    }
    
    @Override
    public void updatedValue(String value) {
        Preconditions.checkNotNull(value);
        Preconditions.checkArgument(!value.isEmpty());
        
        this.valueField.setText(value);
    }
}
