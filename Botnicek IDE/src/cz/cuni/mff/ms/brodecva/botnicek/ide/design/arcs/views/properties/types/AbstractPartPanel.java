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
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.properties.types;

import javax.swing.JPanel;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.Code;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.MixedPattern;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.SimplePattern;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.EnterNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.types.Priority;

/**
 * Panel se specifickými poli pro daný typ hrany.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
abstract class AbstractPartPanel extends JPanel implements PartView {

    private static final long serialVersionUID = 1L;
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.ArcView#updatedType(java.lang.Class)
     */
    @Override
    public void updateType(Class<? extends Arc> arcClass) {
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.ArcView#updatedName(cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord)
     */
    @Override
    public void updateName(final NormalWord name) {
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.ArcView#updatedPriority(int)
     */
    @Override
    public void updatePriority(Priority priority) {
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.ArcView#updatedCode(cz.cuni.mff.ms.brodecva.botnicek.ide.design.types.Code)
     */
    @Override
    public void updatedCode(Code code) {        
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.ArcView#updatedPrepare(cz.cuni.mff.ms.brodecva.botnicek.ide.design.types.Code)
     */
    @Override
    public void updatedPrepare(Code code) {
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.ArcView#updatedTested(cz.cuni.mff.ms.brodecva.botnicek.ide.design.types.Code)
     */
    @Override
    public void updatedTested(Code code) {
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.ArcView#updatedValue(cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.SimplePattern)
     */
    @Override
    public void updatedValue(SimplePattern value) {
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.ArcView#updatedPredicate(cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord)
     */
    @Override
    public void updatedPredicate(NormalWord name) {
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.ArcView#updatedTarget(cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.EnterNode)
     */
    @Override
    public void updatedTarget(EnterNode target) {
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.ArcView#updatedPattern(cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.MixedPattern)
     */
    @Override
    public void updatedPattern(MixedPattern pattern) {
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.ArcView#updatedThat(cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.MixedPattern)
     */
    @Override
    public void updatedThat(MixedPattern that) {
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.ArcView#removed()
     */
    @Override
    public void removed() {
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.ArcView#updatedFrom(cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord)
     */
    @Override
    public void updateFrom(NormalWord name) {
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.ArcView#updatedTo(cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord)
     */
    @Override
    public void updateTo(NormalWord name) {
    }
}
