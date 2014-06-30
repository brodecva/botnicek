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
package cz.cuni.mff.ms.brodecva.botnicek.ide.edit.utils;

import cz.cuni.mff.ms.brodecva.botnicek.library.platform.AIML;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.AIML2DIndex;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.AIMLIndex;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.Index;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.TwoDimensionalIndex;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public class DefaultIndexReader implements IndexReader {

    private static final String COMMA = ",";
    
    public static DefaultIndexReader create() {
        return new DefaultIndexReader();
    }
    
    private DefaultIndexReader() {
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.editor.checker.utils.IndexReader#readOneDimensional(java.lang.String)
     */
    @Override
    public Index readOneDimensional(String indexString) {
        try {
            return new AIMLIndex(Integer.parseInt(indexString));
        } catch (final NumberFormatException e) {
            return null;
        }
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.editor.checker.utils.IndexReader#readTwoDimensional(java.lang.String)
     */
    @Override
    public TwoDimensionalIndex readTwoDimensional(String indexValue) {
        final String[] parts = indexValue.split(COMMA);
        
        if (parts.length != 2) {
            return null;
        }
        
        final int first;
        try {
            first = Integer.parseInt(parts[0]);
        } catch (final NumberFormatException e) {
            return null;
        }
        
        final int second;
        try {
            second = Integer.parseInt(parts[1]);
        } catch (final NumberFormatException e) {
            return null;
        }
        
        return new AIML2DIndex(first, second);
    }

}
