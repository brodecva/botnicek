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

import java.awt.EventQueue;
import javax.swing.JTextField;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.controllers.ArcController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.controllers.DummyArcController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.arcs.api.Processor;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public class PatternArcInternalFrame extends
        AbstractCodeArcInternalFrame implements PatternArcView {
    
    private final JTextField patternField = new JTextField();
    private final JTextField thatField = new JTextField();
    
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    final PatternArcInternalFrame frame = new PatternArcInternalFrame();
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
    public PatternArcInternalFrame() {
        this(new DummyArcController(), "DUMMYPATTERNARC", "DUMMYFROM", "DUMMYTO", 1, "<think>Dummy code</think>", "DUMMY PATTERN *", "DUMMY _ THAT");
    }
    
    public PatternArcInternalFrame(final ArcController arcController, final String name, final String fromNodeName, final String toNodeName, final int priority, final String code, final String pattern, final String that) {
        this(arcController, name, name, fromNodeName, toNodeName, priority, code, pattern, that);
    }
    
    public PatternArcInternalFrame(final ArcController arcController, final String originalName, final String name, final String fromNodeName, final String toNodeName, final int priority, final String code, final String pattern, final String that) {
        super(arcController, originalName, name, fromNodeName, toNodeName, priority, code);
        
        Preconditions.checkNotNull(pattern);
        Preconditions.checkNotNull(that);
        
        initialize(pattern, that);
    }
    
    private void initialize(final String pattern ,final String that) {
        this.patternField.setText(pattern);
        this.thatField.setText(that);
        
        getContentPane().add(this.patternField);
        getContentPane().add(this.thatField);
    }
    
    public PatternArcInternalFrame(final AbstractCodeArcInternalFrame codeArcInternalFrame) {
        super(codeArcInternalFrame);
        
        initialize("", "");
    }
    
    public PatternArcInternalFrame(final AbstractArcInternalFrame arcInternalFrame) {
        super(arcInternalFrame);
        
        initialize("", "");
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.AbstractArcInternalFrame#save()
     */
    @Override
    protected void save() {
        getArcController().updatePattern(getOriginalName(), getArcName(), getPriority(), getPattern(), getThat(), getCode());
    }

    /**
     * @return
     */
    protected String getThat() {
        return this.thatField.getText();
    }

    /**
     * @return
     */
    protected String getPattern() {
        return this.patternField.getText();
    }

    @Override
    public void updatedPattern(final String pattern) {
        Preconditions.checkNotNull(pattern);
        Preconditions.checkArgument(!pattern.isEmpty());
        
        this.patternField.setText(pattern);
    }

    @Override
    public void updatedThat(String that) {
        Preconditions.checkNotNull(that);
        Preconditions.checkArgument(!that.isEmpty());
        
        this.patternField.setText(that);
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.arcs.api.Processible#accept(cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.arcs.api.Processor)
     */
    @Override
    public void accept(final Processor processor) {
        processor.process(this);
    }
}
