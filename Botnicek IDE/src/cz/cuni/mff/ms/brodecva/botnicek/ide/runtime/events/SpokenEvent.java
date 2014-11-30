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
package cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.events;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.model.Run;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.AbstractMappedEvent;

/**
 * Událost promluvy objektu vzniká v situacích, kdy dochází ke komunikaci v
 * rámci konverzace s robotem, a to oběma směry.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public class SpokenEvent extends AbstractMappedEvent<Run, SpokenListener> {

    /**
     * Vytvoří událost.
     * 
     * @param run
     *            běžící konverzace
     * @param author
     *            odesílatel promluvy
     * @param content
     *            promluva
     * @return událost
     */
    public static SpokenEvent create(final Run run, final String author,
            final String content) {
        return new SpokenEvent(run, author, content);
    }

    private final String author;

    private final String content;

    private SpokenEvent(final Run run, final String author, final String content) {
        super(run);

        Preconditions.checkNotNull(author);
        Preconditions.checkNotNull(content);

        this.author = author;
        this.content = content;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.utils.Event#dispatchTo(java.lang
     * .Object)
     */
    @Override
    public void dispatchTo(final SpokenListener listener) {
        listener.spoken(this.author, this.content);
    }
}
