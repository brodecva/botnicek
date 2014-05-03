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
package cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.dfs;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;

import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.Network;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.System;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.api.Visitor;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.arcs.Arc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.nodes.Node;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public final class DfsVisitor implements Visitor {
    
    private static enum State {
        FRESH, OPEN, CLOSED;
    }
    
    private final Map<Node, State> states = new HashMap<>();
    
    private final Map<Node, Integer> timestamps = new HashMap<>();    
    private int nextTimestamp = 0;
    
    private final Set<DfsObserver> observers;
    
    public static DfsVisitor create(final Set<DfsObserver> observers) {
        return new DfsVisitor(observers);
    }
    
    private DfsVisitor(final Set<DfsObserver> observers) {
        Preconditions.checkNotNull(observers);
        
        this.observers = ImmutableSet.copyOf(observers);
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.dfs.Visitor#visit(cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.System)
     */
    @Override
    public void visit(System system) {
        notifyVisit(system);
    }

    /**
     * @param system
     */
    private void notifyVisit(final System system) {
        for (final DfsObserver observer : this.observers) {
            observer.notifyVisit(system);
        }
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.dfs.Visitor#visit(cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.Network)
     */
    @Override
    public void visit(final Network network) {
        notifyVisit(network);
    }
    
    /**
     * @param network
     */
    private void notifyVisit(Network network) {
        for (final DfsObserver observer : this.observers) {
            observer.notifyVisit(network);
        }
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.dfs.Visitor#discover(cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.Node)
     */
    @Override
    public void visitEnter(final Node node) {        
        if (node == null) {
            throw new NullPointerException();
        }        
        
        if (!isFresh(node)) {
            throw new IllegalStateException();
        }
        
        setOpen(node);
        setTimestamp(node);
        
        notifyDiscovery(node);
    }

    /**
     * @param node
     */
    private void notifyDiscovery(final Node node) {
        for (final DfsObserver observer : this.observers) {
            observer.notifyDiscovery(node);
        }
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.Visitor#visitExit(cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.Node)
     */
    @Override
    public void visitExit(final Node node) {
        notifyFinish(node);
        
        if (node == null) {
            throw new NullPointerException();
        }
        
        if (!isOpen(node)) {
            throw new IllegalStateException();
        }
        
        setClosed(node);
    }

    /**
     * @param node
     */
    private void notifyFinish(Node node) {
        for (final DfsObserver observer : this.observers) {
            observer.notifyFinish(node);
        }
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.dfs.Visitor#examine(cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.Arc)
     */
    @Override
    public void visit(final Arc arc) {
        final Node from = arc.getFrom();
        if (!isOpen(from)) {
            throw new IllegalStateException();
        }
        
        notifyExamination(arc);
        
        final Node to = arc.getTo();
                
        if (isFresh(to)) {
            tree(arc);
        } else if (isOpen(to)) {
            back(arc);
        } else if (isClosed(to)) {
            final int fromTimestamp = getTimestamp(from);
            final int toTimestamp = getTimestamp(to);
            
            if (fromTimestamp < toTimestamp) {
                tree(arc);
            } else if (fromTimestamp > toTimestamp) {
                cross(arc);
            } else {
                assert(false);
            }
        } else {
            assert(false);
        }
    }

    /**
     * @param arc
     */
    private void notifyExamination(Arc arc) {
        for (final DfsObserver observer : this.observers) {
            observer.notifyExamination(arc);
        }
    }

    private void tree(final Arc arc) {
        notifyTree(arc);
    }

    /**
     * @param arc
     */
    private void notifyTree(Arc arc) {
        for (final DfsObserver observer : this.observers) {
            observer.notifyTree(arc);
        }
    }

    private void back(final Arc arc) {
        notifyBack(arc);
    }

    /**
     * @param arc
     */
    private void notifyBack(final Arc arc) {
        for (final DfsObserver observer : this.observers) {
            observer.notifyBack(arc);
        }
    }

    private void cross(final Arc arc) {
        notifyCross(arc);
    }

    /**
     * @param arc
     */
    private void notifyCross(final Arc arc) {
        for (final DfsObserver observer : this.observers) {
            observer.notifyCross(arc);
        }
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.dfs.Visitor#isVisited(cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.Node)
     */
    @Override
    public boolean visited(final Node node) {
        if (node == null) {
            throw new NullPointerException();
        }
        
        return !isFresh(node);
    }
    
    private boolean isFresh(final Node node) {
        return getState(node) == State.FRESH;
    }    

    /**
     * @param node
     * @return
     */
    private boolean isOpen(final Node node) {
        return getState(node) == State.OPEN;
    }
    
    /**
     * @param to
     * @return
     */
    private boolean isClosed(final Node node) {
        return getState(node) == State.CLOSED;
    }
    
    private State getState(final Node node) {
        final State result = states.get(node);
        
        if (result == null) {
            return State.FRESH;
        } else {
            return result;
        }
    }
    
    private void setFresh(final Node node) {
        setState(node, State.FRESH);
    }
    
    private void setOpen(final Node node) {
        setState(node, State.OPEN);
    }
    
    private void setClosed(final Node node) {
        setState(node, State.CLOSED);
    }
    
    private void setState(final Node node, final State state) {
        states.put(node, state);
    }
    
    private int getTimestamp(final Node node) {
        return timestamps.get(node);
    }
    
    /**
     * @param node
     */
    private void setTimestamp(final Node node) {
        this.timestamps.put(node, nextTimestamp++);
    }
}
