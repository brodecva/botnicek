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
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.implementations;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableTable.Builder;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWords;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.EnterNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.ExitNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.InnerNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.InputNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.IsolatedNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.NodeModifier;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.OrderedNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.ProcessingNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.RandomNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NamingAuthority;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.utils.TableUtils;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public class DefaultNodeModifier implements NodeModifier {
    
    private static final String NODE_FACTORY_METHOD_NAME = "create";
    
    private static final Table<Class<? extends Node>, Class<? extends Node>, Class<? extends Node>> DEFAULTS;
    
    static {
        final Builder<Class<? extends Node>, Class<? extends Node>, Class<? extends Node>> defaultsBuilder = ImmutableTable.builder();
        
        defaultsBuilder.put(EnterOrderedInputNode.class, EnterNode.class, EnterOrderedInputNode.class);
        defaultsBuilder.put(EnterOrderedInputNode.class, ExitNode.class, ExitInputNode.class);
        defaultsBuilder.put(EnterOrderedInputNode.class, InnerNode.class, InnerOrderedInputNode.class);
        defaultsBuilder.put(EnterOrderedInputNode.class, InputNode.class, EnterOrderedInputNode.class);
        defaultsBuilder.put(EnterOrderedInputNode.class, ProcessingNode.class, EnterOrderedProcessingNode.class);
        defaultsBuilder.put(EnterOrderedInputNode.class, IsolatedNode.class, IsolatedInputNode.class);
        defaultsBuilder.put(EnterOrderedInputNode.class, Node.class, EnterOrderedInputNode.class);
        defaultsBuilder.put(EnterOrderedInputNode.class, OrderedNode.class, EnterOrderedInputNode.class);
        defaultsBuilder.put(EnterOrderedInputNode.class, RandomNode.class, EnterRandomInputNode.class);
        
        defaultsBuilder.put(EnterOrderedProcessingNode.class, EnterNode.class, EnterOrderedProcessingNode.class);
        defaultsBuilder.put(EnterOrderedProcessingNode.class, ExitNode.class, ExitProcessingNode.class);
        defaultsBuilder.put(EnterOrderedProcessingNode.class, InnerNode.class, InnerOrderedProcessingNode.class);
        defaultsBuilder.put(EnterOrderedProcessingNode.class, InputNode.class, EnterOrderedInputNode.class);
        defaultsBuilder.put(EnterOrderedProcessingNode.class, ProcessingNode.class, EnterOrderedProcessingNode.class);
        defaultsBuilder.put(EnterOrderedProcessingNode.class, IsolatedNode.class, IsolatedProcessingNode.class);
        defaultsBuilder.put(EnterOrderedProcessingNode.class, Node.class, EnterOrderedProcessingNode.class);
        defaultsBuilder.put(EnterOrderedProcessingNode.class, OrderedNode.class, EnterOrderedProcessingNode.class);
        defaultsBuilder.put(EnterOrderedProcessingNode.class, RandomNode.class, EnterRandomProcessingNode.class);
        
        defaultsBuilder.put(EnterRandomInputNode.class, EnterNode.class, EnterRandomInputNode.class);
        defaultsBuilder.put(EnterRandomInputNode.class, ExitNode.class, ExitInputNode.class);
        defaultsBuilder.put(EnterRandomInputNode.class, InnerNode.class, InnerRandomInputNode.class);
        defaultsBuilder.put(EnterRandomInputNode.class, InputNode.class, EnterRandomInputNode.class);
        defaultsBuilder.put(EnterRandomInputNode.class, ProcessingNode.class, EnterRandomProcessingNode.class);
        defaultsBuilder.put(EnterRandomInputNode.class, IsolatedNode.class, IsolatedInputNode.class);
        defaultsBuilder.put(EnterRandomInputNode.class, Node.class, EnterRandomInputNode.class);
        defaultsBuilder.put(EnterRandomInputNode.class, OrderedNode.class, EnterOrderedInputNode.class);
        defaultsBuilder.put(EnterRandomInputNode.class, RandomNode.class, EnterRandomInputNode.class);
        
        defaultsBuilder.put(EnterRandomProcessingNode.class, EnterNode.class, EnterRandomProcessingNode.class);
        defaultsBuilder.put(EnterRandomProcessingNode.class, ExitNode.class, ExitProcessingNode.class);
        defaultsBuilder.put(EnterRandomProcessingNode.class, InnerNode.class, InnerRandomProcessingNode.class);
        defaultsBuilder.put(EnterRandomProcessingNode.class, InputNode.class, EnterRandomInputNode.class);
        defaultsBuilder.put(EnterRandomProcessingNode.class, ProcessingNode.class, EnterRandomProcessingNode.class);
        defaultsBuilder.put(EnterRandomProcessingNode.class, IsolatedNode.class, IsolatedProcessingNode.class);
        defaultsBuilder.put(EnterRandomProcessingNode.class, Node.class, EnterRandomProcessingNode.class);
        defaultsBuilder.put(EnterRandomProcessingNode.class, OrderedNode.class, EnterOrderedProcessingNode.class);
        defaultsBuilder.put(EnterRandomProcessingNode.class, RandomNode.class, EnterRandomProcessingNode.class);
        
        defaultsBuilder.put(InnerOrderedInputNode.class, EnterNode.class, EnterOrderedInputNode.class);
        defaultsBuilder.put(InnerOrderedInputNode.class, ExitNode.class, ExitInputNode.class);
        defaultsBuilder.put(InnerOrderedInputNode.class, InnerNode.class, InnerOrderedInputNode.class);
        defaultsBuilder.put(InnerOrderedInputNode.class, InputNode.class, InnerOrderedInputNode.class);
        defaultsBuilder.put(InnerOrderedInputNode.class, ProcessingNode.class, InnerOrderedProcessingNode.class);
        defaultsBuilder.put(InnerOrderedInputNode.class, IsolatedNode.class, IsolatedInputNode.class);
        defaultsBuilder.put(InnerOrderedInputNode.class, Node.class, InnerOrderedInputNode.class);
        defaultsBuilder.put(InnerOrderedInputNode.class, OrderedNode.class, InnerOrderedInputNode.class);
        defaultsBuilder.put(InnerOrderedInputNode.class, RandomNode.class, InnerRandomInputNode.class);
        
        defaultsBuilder.put(InnerOrderedProcessingNode.class, EnterNode.class, EnterOrderedProcessingNode.class);
        defaultsBuilder.put(InnerOrderedProcessingNode.class, ExitNode.class, ExitProcessingNode.class);
        defaultsBuilder.put(InnerOrderedProcessingNode.class, InnerNode.class, InnerOrderedProcessingNode.class);
        defaultsBuilder.put(InnerOrderedProcessingNode.class, InputNode.class, InnerOrderedInputNode.class);
        defaultsBuilder.put(InnerOrderedProcessingNode.class, ProcessingNode.class, InnerOrderedProcessingNode.class);
        defaultsBuilder.put(InnerOrderedProcessingNode.class, IsolatedNode.class, IsolatedProcessingNode.class);
        defaultsBuilder.put(InnerOrderedProcessingNode.class, Node.class, InnerOrderedProcessingNode.class);
        defaultsBuilder.put(InnerOrderedProcessingNode.class, OrderedNode.class, InnerOrderedProcessingNode.class);
        defaultsBuilder.put(InnerOrderedProcessingNode.class, RandomNode.class, InnerRandomProcessingNode.class);
        
        defaultsBuilder.put(InnerRandomInputNode.class, EnterNode.class, EnterRandomInputNode.class);
        defaultsBuilder.put(InnerRandomInputNode.class, ExitNode.class, ExitInputNode.class);
        defaultsBuilder.put(InnerRandomInputNode.class, InnerNode.class, InnerRandomInputNode.class);
        defaultsBuilder.put(InnerRandomInputNode.class, InputNode.class, InnerRandomInputNode.class);
        defaultsBuilder.put(InnerRandomInputNode.class, ProcessingNode.class, InnerRandomProcessingNode.class);
        defaultsBuilder.put(InnerRandomInputNode.class, IsolatedNode.class, IsolatedInputNode.class);
        defaultsBuilder.put(InnerRandomInputNode.class, Node.class, InnerRandomInputNode.class);
        defaultsBuilder.put(InnerRandomInputNode.class, OrderedNode.class, InnerOrderedInputNode.class);
        defaultsBuilder.put(InnerRandomInputNode.class, RandomNode.class, InnerRandomInputNode.class);
        
        defaultsBuilder.put(InnerRandomProcessingNode.class, EnterNode.class, EnterRandomProcessingNode.class);
        defaultsBuilder.put(InnerRandomProcessingNode.class, ExitNode.class, ExitProcessingNode.class);
        defaultsBuilder.put(InnerRandomProcessingNode.class, InnerNode.class, InnerRandomProcessingNode.class);
        defaultsBuilder.put(InnerRandomProcessingNode.class, InputNode.class, InnerRandomInputNode.class);
        defaultsBuilder.put(InnerRandomProcessingNode.class, ProcessingNode.class, InnerRandomProcessingNode.class);
        defaultsBuilder.put(InnerRandomProcessingNode.class, IsolatedNode.class, IsolatedProcessingNode.class);
        defaultsBuilder.put(InnerRandomProcessingNode.class, Node.class, InnerRandomProcessingNode.class);
        defaultsBuilder.put(InnerRandomProcessingNode.class, OrderedNode.class, InnerOrderedProcessingNode.class);
        defaultsBuilder.put(InnerRandomProcessingNode.class, RandomNode.class, InnerRandomProcessingNode.class);
        
        defaultsBuilder.put(ExitInputNode.class, EnterNode.class, EnterOrderedInputNode.class);
        defaultsBuilder.put(ExitInputNode.class, ExitNode.class, ExitInputNode.class);
        defaultsBuilder.put(ExitInputNode.class, InnerNode.class, InnerOrderedInputNode.class);
        defaultsBuilder.put(ExitInputNode.class, InputNode.class, ExitInputNode.class);
        defaultsBuilder.put(ExitInputNode.class, ProcessingNode.class, ExitProcessingNode.class);
        defaultsBuilder.put(ExitInputNode.class, IsolatedNode.class, IsolatedInputNode.class);
        defaultsBuilder.put(ExitInputNode.class, Node.class, ExitInputNode.class);
        defaultsBuilder.put(ExitInputNode.class, OrderedNode.class, ExitInputNode.class);
        defaultsBuilder.put(ExitInputNode.class, RandomNode.class, ExitInputNode.class);
        
        defaultsBuilder.put(ExitProcessingNode.class, EnterNode.class, EnterOrderedProcessingNode.class);
        defaultsBuilder.put(ExitProcessingNode.class, ExitNode.class, ExitProcessingNode.class);
        defaultsBuilder.put(ExitProcessingNode.class, InnerNode.class, InnerOrderedProcessingNode.class);
        defaultsBuilder.put(ExitProcessingNode.class, InputNode.class, ExitInputNode.class);
        defaultsBuilder.put(ExitProcessingNode.class, ProcessingNode.class, ExitProcessingNode.class);
        defaultsBuilder.put(ExitProcessingNode.class, IsolatedNode.class, IsolatedProcessingNode.class);
        defaultsBuilder.put(ExitProcessingNode.class, Node.class, ExitProcessingNode.class);
        defaultsBuilder.put(ExitProcessingNode.class, OrderedNode.class, ExitProcessingNode.class);
        defaultsBuilder.put(ExitProcessingNode.class, RandomNode.class, ExitProcessingNode.class);
        
        defaultsBuilder.put(IsolatedInputNode.class, EnterNode.class, EnterOrderedInputNode.class);
        defaultsBuilder.put(IsolatedInputNode.class, ExitNode.class, ExitInputNode.class);
        defaultsBuilder.put(IsolatedInputNode.class, InnerNode.class, InnerOrderedInputNode.class);
        defaultsBuilder.put(IsolatedInputNode.class, InputNode.class, IsolatedInputNode.class);
        defaultsBuilder.put(IsolatedInputNode.class, ProcessingNode.class, IsolatedProcessingNode.class);
        defaultsBuilder.put(IsolatedInputNode.class, IsolatedNode.class, IsolatedInputNode.class);
        defaultsBuilder.put(IsolatedInputNode.class, Node.class, IsolatedInputNode.class);
        defaultsBuilder.put(IsolatedInputNode.class, OrderedNode.class, IsolatedInputNode.class);
        defaultsBuilder.put(IsolatedInputNode.class, RandomNode.class, IsolatedInputNode.class);
        
        defaultsBuilder.put(IsolatedProcessingNode.class, EnterNode.class, EnterOrderedProcessingNode.class);
        defaultsBuilder.put(IsolatedProcessingNode.class, ExitNode.class, ExitProcessingNode.class);
        defaultsBuilder.put(IsolatedProcessingNode.class, InnerNode.class, InnerOrderedProcessingNode.class);
        defaultsBuilder.put(IsolatedProcessingNode.class, InputNode.class, IsolatedInputNode.class);
        defaultsBuilder.put(IsolatedProcessingNode.class, ProcessingNode.class, IsolatedProcessingNode.class);
        defaultsBuilder.put(IsolatedProcessingNode.class, IsolatedNode.class, IsolatedProcessingNode.class);
        defaultsBuilder.put(IsolatedProcessingNode.class, Node.class, IsolatedProcessingNode.class);
        defaultsBuilder.put(IsolatedProcessingNode.class, OrderedNode.class, IsolatedProcessingNode.class);
        defaultsBuilder.put(IsolatedProcessingNode.class, RandomNode.class, IsolatedProcessingNode.class);
        
        DEFAULTS = defaultsBuilder.build();
    }
    
    private final Table<Class<? extends Node>, Class<? extends Node>, Class<? extends Node>> changes;
    
    public static DefaultNodeModifier create() {
        return new DefaultNodeModifier(DEFAULTS);
    }
    
    public static DefaultNodeModifier create(final Map< Class<? extends Node>, Map<Class<? extends Node>, Class<? extends Node>> > changes) {
        return new DefaultNodeModifier(changes);
    }
    
    private DefaultNodeModifier(final Map< Class<? extends Node>, Map<Class<? extends Node>, Class<? extends Node>> > changes) {
        this(TableUtils.toImmutableTable(changes));
    }
    
    private DefaultNodeModifier(final Table< Class<? extends Node>, Class<? extends Node>, Class<? extends Node>> changes) {
        Preconditions.checkNotNull(changes);
        
        this.changes = ImmutableTable.copyOf(changes);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.nodes.NodeModifier#change(cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.nodes.Node, java.lang.String, int, int, java.lang.Class)
     */
    @Override
    public Node change(final Node node, final NormalWord name, final int x, final int y, final Class<? extends Node> type) {
        Preconditions.checkNotNull(node);
        Preconditions.checkNotNull(name);
        Preconditions.checkNotNull(type);
        
        Preconditions.checkArgument(x >= 0);
        Preconditions.checkArgument(y >= 0);
        
        final NormalWord oldName = node.getName();
        
        final Class<? extends Node> nodeClass = node.getClass();
        final Class<? extends Node> mappedClass = this.changes.get(nodeClass, type);
        final Class<? extends Node> usedMappedClass;
        if (mappedClass == null) {
            usedMappedClass = nodeClass;
        } else {
            usedMappedClass = mappedClass;
        }
        
        final Method factoryMethod;
        try {
            factoryMethod = usedMappedClass.getMethod(NODE_FACTORY_METHOD_NAME, NormalWord.class, Network.class, Integer.TYPE, Integer.TYPE);
        } catch (final NoSuchMethodException | SecurityException e) {
            throw new IllegalArgumentException(e);
        }
        
        try {
            return (Node) factoryMethod.invoke(null, name, node.getNetwork(), x, y);
        } catch (final IllegalAccessException
                | IllegalArgumentException | InvocationTargetException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.nodes.NodeModifier#change(cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.nodes.Node, java.lang.Class)
     */
    @Override
    public Node change(Node node, Class<? extends Node> type) {
        return change(node, node.getName(), node.getX(), node.getY(), type);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.nodes.NodeModifier#change(cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.nodes.Node, java.lang.String)
     */
    @Override
    public Node change(Node node, NormalWord name) {
        return change(node, name, node.getX(), node.getY(), node.getClass());
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.nodes.NodeModifier#change(cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.nodes.Node, int, int)
     */
    @Override
    public Node change(Node node, int x, int y) {
        return change(node, node.getName(), x, y, node.getClass());
    }
}
