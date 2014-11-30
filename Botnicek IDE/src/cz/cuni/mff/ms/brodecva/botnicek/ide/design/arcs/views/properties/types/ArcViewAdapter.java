/**
 * Copyright Václav Brodec 2014.
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

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.Code;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.MixedPattern;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.SimplePattern;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.properties.ArcView;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.EnterNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.types.Priority;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public abstract class ArcViewAdapter implements ArcView {

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.ArcView#removed()
     */
    @Override
    public void removed() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.ArcView#updatedCode
     * (cz.cuni.mff.ms.brodecva.botnicek.ide.design.types.Code)
     */
    @Override
    public void updatedCode(final Code code) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.ArcView#updatedPattern
     * (cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.MixedPattern)
     */
    @Override
    public void updatedPattern(final MixedPattern pattern) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.ArcView#
     * updatedPredicate
     * (cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord)
     */
    @Override
    public void updatedPredicate(final NormalWord name) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.ArcView#updatedPrepare
     * (cz.cuni.mff.ms.brodecva.botnicek.ide.design.types.Code)
     */
    @Override
    public void updatedPrepare(final Code code) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.ArcView#updatedTarget
     * (cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.EnterNode)
     */
    @Override
    public void updatedTarget(final EnterNode target) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.ArcView#updatedTested
     * (cz.cuni.mff.ms.brodecva.botnicek.ide.design.types.Code)
     */
    @Override
    public void updatedTested(final Code code) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.ArcView#updatedThat
     * (cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.MixedPattern)
     */
    @Override
    public void updatedThat(final MixedPattern that) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.ArcView#updatedValue
     * (cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.SimplePattern)
     */
    @Override
    public void updatedValue(final SimplePattern value) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.ArcView#updatedFrom
     * (cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord)
     */
    @Override
    public void updateFrom(final NormalWord name) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.ArcView#updatedName
     * (cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord)
     */
    @Override
    public void updateName(final NormalWord name) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.ArcView#
     * updatedPriority(int)
     */
    @Override
    public void updatePriority(final Priority priority) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.ArcView#updatedTo
     * (cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord)
     */
    @Override
    public void updateTo(final NormalWord name) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.ArcView#updatedType
     * (java.lang.Class)
     */
    @Override
    public void updateType(final Class<? extends Arc> arcClass) {
    }
}
