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
package cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design;

import java.util.Set;

import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.api.Visitable;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.api.Visitor;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.arcs.Arc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.arcs.RecurentArc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.nodes.EnterNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.nodes.Node;
import cz.cuni.mff.ms.brodecva.botnicek.ide.editor.checker.predicates.PredicateNameChecker;


/**
 * @author Václav Brodec
 * @version 1.0
 */
public final class Network implements Visitable, NetworkInfo {
    private final String name;
    
    private final System parent;
    
    public static Network create(final String name, final System parent) {
        return new Network(name, parent);
    }
    
    private Network(final String name, final System parent) {
        this.name = name;
        this.parent = parent;
    }
    
    /**
     * @return
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return the parent
     */
    public final System getParent() {
        return parent;
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.Visitable#accept(cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.Visitor)
     */
    @Override
    public void accept(final Visitor visitor) {
        visitor.visit(this);
        
        final Set<EnterNode> initialNodes = this.parent.getInitialNodes(this);
        for (final EnterNode initialNode : initialNodes) {
            initialNode.accept(visitor);
        }
    }
    
    public void addNode(final int x, final int y) {
        this.parent.addNode(this, x, y);
    }
    
    public void addNode(final String name, final int x, final int y) {
        this.parent.addNode(this, name, x, y);
    }
    
    public void removeNode(final String name) {
        this.parent.removeNode(name);
    }
    
    public void addArc(final String name, final String fromName, String toName) {
        this.parent.addArc(this, name, fromName, toName);
    }

    /**
     * @param name
     */
    public void removeArc(final String name) {
        this.parent.removeArc(name);
    }

    /**
     * @param node
     * @return
     */
    public Set<Arc> getOuts(final Node node) {
        return this.parent.getOuts(node);
    }

    /**
     * @param abstractNode
     * @return
     */
    public Set<Arc> getIns(final Node node) {
        return this.parent.getIns(node);
    }

    /**
     * @param name
     * @param type
     */
    public void changeNode(final String name, final Class<? extends Node> type) {
        this.parent.changeNode(name, type);
    }
    
    public void changeNode(final String name, String newName) {
        this.parent.changeNode(name, newName);
    }
    
    /**
     * @param nodeName
     * @param x
     * @param y
     */
    public void changeNode(String nodeName, int x, int y) {
        this.parent.changeNode(name, x, y);
    }
    
    public void changeNode(String nodeName, String newName, int x, int y, final Class<? extends Node> type) {
        this.parent.changeNode(nodeName, newName, x, y, type);
    }

    /**
     * @param abstractArc
     * @param direction
     * @return
     */
    public Node getAttached(final Arc arc, final Direction direction) {
        return this.parent.getAttached(arc, direction);
    }

    /**
     * @param name
     * @param newName
     * @param priority
     * @param pattern
     * @param that
     * @param code
     */
    public void changeArc(final String name, final String newName, final int priority, final Class<? extends Arc> type, final Object... arguments) {
        this.parent.changeArc(name, newName, priority, type, arguments);
    }

    /**
     * @param direction
     * @return
     */
    public Set<Arc> getConnections(final Node node, final Direction direction) {
        return this.parent.getConnections(node, direction);
    }

    /**
     * @param name
     * @return
     */
    public Node getNode(String name) {
        return this.parent.getNode(name);
    }

    /**
     * @param arcName
     * @return
     */
    public Arc getArc(String arcName) {
        return this.parent.getArc(arcName);
    }

    /**
     * @return
     */
    public NamingAuthority getPredicateNamingAuthority() {
        return this.parent.getPredicatesNamingAuthority();
    }

    /**
     * @param newInstance
     * @param target
     */
    public void registerReference(final RecurentArc arc, final EnterNode target) {
        this.parent.registerReference(arc, target);
    }

    /**
     * @return
     */
    public Set<EnterNode> getAvailableReferences() {
        return this.parent.getAvailableReferences();
    }
}
