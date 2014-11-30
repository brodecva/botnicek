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
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model;

import java.util.List;

import org.easymock.EasyMock;
import org.junit.experimental.categories.Category;

import com.google.common.collect.ImmutableList;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.AbstractDefaultLabeledDirectedGraphTest;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultDirectedGraph;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultLabeledDirectedGraph;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.test.UnitTest;

/**
 * Testuje výchozí implementaci grafu s vrcholy typu {@link Node}, hranami typu
 * {@link Arc} a popisky typu {@link NormalWord}.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see DefaultDirectedGraph
 */
@Category(UnitTest.class)
public class DefaultLabeledDirectedGraphNodeArcTest
        extends
        AbstractDefaultLabeledDirectedGraphTest<Node, NormalWord, Arc, NormalWord> {

    private <T> List<T> createDummies(final int count, final Class<T> type) {
        final ImmutableList.Builder<T> resultBuilder = ImmutableList.builder();

        for (int i = 0; i < count; i++) {
            final T dummy = EasyMock.createStrictMock(type);

            resultBuilder.add(dummy);
        }

        return resultBuilder.build();
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.
     * AbstractDefaultLabeledDirectedGraphTest#createEmpty()
     */
    @Override
    protected DefaultLabeledDirectedGraph<Node, NormalWord, Arc, NormalWord>
            createEmpty() {
        return DefaultLabeledDirectedGraph
                .<Node, NormalWord, Arc, NormalWord> create();
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.
     * AbstractDefaultDirectedGraphTest#createHookCounter()
     */
    @Override
    protected
            cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.AbstractDefaultDirectedGraphTest.HookCountingDirectedGraph<Node, Arc>
            createHookCounter() {
        return new HookCountingDirectedGraph<>();
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.
     * AbstractDefaultLabeledDirectedGraphTest#createUniqueEdgeLabels(int)
     */
    @Override
    protected NormalWord[] createUniqueEdgeLabels(final int count) {
        return createUniqueVertexLabels(count);
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.
     * AbstractDefaultDirectedGraphTest#createUniqueEdges(int)
     */
    @Override
    protected Arc[] createUniqueEdges(final int count) {
        final List<Arc> dummies = createDummies(count, Arc.class);

        final Arc[] dummiesArray = dummies.toArray(new Arc[count]);
        EasyMock.replay((Object[]) dummiesArray);

        return dummiesArray;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.
     * AbstractDefaultLabeledDirectedGraphTest#createUniqueVertexLabels(int)
     */
    @Override
    protected NormalWord[] createUniqueVertexLabels(final int count) {
        final List<NormalWord> dummies = createDummies(count, NormalWord.class);

        final NormalWord[] dummiesArray =
                dummies.toArray(new NormalWord[count]);
        EasyMock.replay((Object[]) dummiesArray);

        return dummiesArray;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.
     * AbstractDefaultDirectedGraphTest#createUniqueVertices(int)
     */
    @Override
    protected Node[] createUniqueVertices(final int count) {
        final List<Node> dummies = createDummies(count, Node.class);

        final Node[] dummiesArray = dummies.toArray(new Node[count]);
        EasyMock.replay((Object[]) dummiesArray);

        return dummiesArray;
    }

}
