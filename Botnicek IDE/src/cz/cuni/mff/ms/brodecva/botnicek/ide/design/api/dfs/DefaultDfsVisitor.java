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
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.dfs;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System;

/**
 * Výchozí implementace návštěvníka procházejícího systém sítí do hloubky.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class DefaultDfsVisitor implements DfsVisitor {
    
    /**
     * Stavy vrcholů v algoritmu.
     */
    private static enum State {
        FRESH, OPEN, CLOSED;
    }
    
    private final Map<Node, State> states = new HashMap<>();
    
    private final Map<Node, Integer> timestamps = new HashMap<>();    
    private int nextTimestamp = 0;
    
    private final Set<DfsObserver> observers;
    
    /**
     * Vytvoří návštěvníka.
     * 
     * @param observers unikátní pozorovatelé
     * @return návštěvník
     */
    public static DefaultDfsVisitor create(final DfsObserver... observers) {
        Preconditions.checkNotNull(observers);
        
        return create(ImmutableSet.copyOf(observers));
    }
    
    /**
     * Vytvoří návštěvníka.
     * 
     * @param observers unikátní pozorovatelé
     * @return návštěvník
     */
    public static DefaultDfsVisitor create(final Set<? extends DfsObserver> observers) {
        Preconditions.checkNotNull(observers);
        
        return new DefaultDfsVisitor(ImmutableSet.copyOf(observers));
    }
    
    private DefaultDfsVisitor(final Set<DfsObserver> observers) {
        this.observers = observers;
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.dfs.DfsVisitor#visit(cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System)
     */
    @Override
    public void visit(final System system) {
        Preconditions.checkNotNull(system);
        
        notifyVisit(system);
    }

    private void notifyVisit(final System system) {
        for (final DfsObserver observer : this.observers) {
            observer.notifyVisit(system);
        }
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.dfs.DfsVisitor#visit(cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network)
     */
    @Override
    public void visit(final Network network) {
        Preconditions.checkNotNull(network);
        
        notifyVisit(network);
    }
    
    private void notifyVisit(final Network network) {
        for (final DfsObserver observer : this.observers) {
            observer.notifyVisit(network);
        }
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.Visitor#visitEnter(cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node)
     */
    @Override
    public void visitEnter(final Node node) {        
        Preconditions.checkNotNull(node);
        Preconditions.checkState(isFresh(node));
        
        setOpen(node);
        setTimestamp(node);
        
        notifyDiscovery(node);
    }

    private void notifyDiscovery(final Node node) {
        for (final DfsObserver observer : this.observers) {
            observer.notifyDiscovery(node);
        }
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.Visitor#visitExit(cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node)
     */
    @Override
    public void visitExit(final Node node) {
        Preconditions.checkNotNull(node);
        Preconditions.checkState(isOpen(node));
        
        notifyFinish(node);
        setClosed(node);
    }

    /**
     * @param node
     */
    private void notifyFinish(final Node node) {
        for (final DfsObserver observer : this.observers) {
            observer.notifyFinish(node);
        }
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.Visitor#visit(cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc)
     */
    @Override
    public void visit(final Arc arc) {
        Preconditions.checkNotNull(arc);
        
        final Node from = arc.getFrom();
        Preconditions.checkState(isOpen(from));
        
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

    private void notifyExamination(final Arc arc) {
        for (final DfsObserver observer : this.observers) {
            observer.notifyExamination(arc);
        }
    }

    private void tree(final Arc arc) {
        notifyTree(arc);
    }

    private void notifyTree(final Arc arc) {
        for (final DfsObserver observer : this.observers) {
            observer.notifyTree(arc);
        }
    }

    private void back(final Arc arc) {
        notifyBack(arc);
    }

    private void notifyBack(final Arc arc) {
        for (final DfsObserver observer : this.observers) {
            observer.notifyBack(arc);
        }
    }

    private void cross(final Arc arc) {
        notifyCross(arc);
    }

    private void notifyCross(final Arc arc) {
        for (final DfsObserver observer : this.observers) {
            observer.notifyCross(arc);
        }
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.Visitor#visited(cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node)
     */
    @Override
    public boolean visited(final Node node) {
        Preconditions.checkNotNull(node);
        
        return !isFresh(node);
    }
    
    private boolean isFresh(final Node node) {
        return getState(node) == State.FRESH;
    }    

    private boolean isOpen(final Node node) {
        return getState(node) == State.OPEN;
    }
    
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
    
    @SuppressWarnings("unused")
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
        if (state == State.FRESH) {
            states.remove(node);
        } else {
            states.put(node, state);
        }
    }
    
    private int getTimestamp(final Node node) {
        return timestamps.get(node);
    }
    
    private void setTimestamp(final Node node) {
        this.timestamps.put(node, nextTimestamp++);
    }
}
