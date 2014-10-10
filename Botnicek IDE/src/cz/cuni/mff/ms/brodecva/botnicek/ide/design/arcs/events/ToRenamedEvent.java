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
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.events;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.AbstractMappedEvent;

/**
 * Událost změny názvu cílového uzlu hrany.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class ToRenamedEvent extends
        AbstractMappedEvent<Arc, ToRenamedListener> {

    /**
     * Vytvoří událost.
     * 
     * @param arc
     *            hrana
     * @param oldVersion
     *            původní verze cílového uzlu
     * @param newVersion
     *            nová verze cílového uzlu
     * @return událost
     */
    public static ToRenamedEvent create(final Arc arc, final Node oldVersion,
            final Node newVersion) {
        return new ToRenamedEvent(arc, oldVersion, newVersion);
    }

    private final Node oldVersion;

    private final Node newVersion;

    private ToRenamedEvent(final Arc arc, final Node oldVersion,
            final Node newVersion) {
        super(arc);

        Preconditions.checkNotNull(oldVersion);
        Preconditions.checkNotNull(newVersion);

        this.oldVersion = oldVersion;
        this.newVersion = newVersion;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.utils.Event#dispatchTo(java.lang
     * .Object)
     */
    @Override
    public void dispatchTo(final ToRenamedListener listener) {
        listener.toRenamed(this.oldVersion, this.newVersion);
    }
}
