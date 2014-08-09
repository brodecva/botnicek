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
package cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs;

import org.junit.experimental.categories.Category;

import cz.cuni.mff.ms.brodecva.botnicek.library.utils.test.UnitTest;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.test.ObjectGenerator;

/**
 * Testuje výchozí implementaci grafu s vrcholy a hranami typu {@link Object}.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see DefaultDirectedGraph
 */
@Category(UnitTest.class)
public class DefaultDirectedGraphObjectObjectTest extends AbstractDefaultDirectedGraphTest<Object, Object>{

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.AbstractDefaultDirectedGraphTest#createUniqueVertices(int)
     */
    @Override
    protected Object[] createUniqueVertices(int count) {
        return ObjectGenerator.getUniqueObjects(count);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.AbstractDefaultDirectedGraphTest#createUniqueEdges(int)
     */
    @Override
    protected Object[] createUniqueEdges(int count) {
        return ObjectGenerator.getUniqueObjects(count);
    }

    
}
