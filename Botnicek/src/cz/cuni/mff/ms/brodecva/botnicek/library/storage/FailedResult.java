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
package cz.cuni.mff.ms.brodecva.botnicek.library.storage;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Reprezentuje neúspěšný výsledek hledání.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class FailedResult implements MatchResult, Serializable {
    /**
     * UID serializované verze.
     */
    private static final long serialVersionUID = 8101303385220960832L;

    /**
     * Konstruktor indikátoru neúspěšného hledání.
     */
    public FailedResult() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.library.storage.MatchResult#isSuccesful
     * ()
     */
    @Override
    public boolean isSuccesful() {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.library.storage.MatchResult#getTemplate
     * ()
     */
    @Override
    public Template getTemplate() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.storage.MatchResult#
     * getStarMatchedParts
     * (cz.cuni.mff.ms.brodecva.botnicek.library.storage.PartMarker)
     */
    @Override
    public List<String> getStarMatchedParts(final PartMarker part) {
        return new LinkedList<String>();
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.storage.MatchResult#
     * addStarMatchedPart
     * (cz.cuni.mff.ms.brodecva.botnicek.library.storage.PartMarker,
     * cz.cuni.mff.ms.brodecva.botnicek.library.storage.InputPath)
     */
    @Override
    public void addStarMatchedPart(final PartMarker pathPart,
            final InputPath matchedPart) {
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "FailedResult";
    }
}
