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
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.RecurentArc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.EnterNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.RealignmentProcessor;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.Update.NodeSwitch;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Callback;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Function;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.Presence;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultDirectedGraph;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DirectedGraph;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.Direction;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.resources.ExceptionLocalizer;

/**
 * <p>Graf hran a uzlů systému. Kromě vztahů uzlů a hran ukládá i jejich názvy.</p>
 * <p>Přestože podporuje více hran mezi dvěma uzly, jež jsou využitelné v této implementaci ATN, bylo v této verzi od jejich umožnění v návrhu upuštěno,
 * neboť bylo posouzeno, že je velmi obtížné navrhnout přehledné rozhraní pro jejich zobrazení a editaci. Jejich vypuštění nemá vliv na sílu systému, neboť je lze nahradit kombinací původní hrany, procesního uzlu a přechodové hrany.</p>
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class SystemGraph implements DirectedGraph<Node, Arc> {
    
    private final DirectedGraph<Node, Arc> innerGraph = DefaultDirectedGraph.create();
    private final Map<NormalWord, Node> namesToNodes = new HashMap<>();
    private final Map<NormalWord, Arc> namesToArcs = new HashMap<>();
    
    /**
     * Vytvoří graf.
     * 
     * @return prázdný graf
     */
    public static SystemGraph create() {
        return new SystemGraph();
    }
    
    private SystemGraph() {
    }
    
    /**
     * Vrátí uzel daného názvu.
     * 
     * @param name název uzlu
     * @return uzel či {@code null}, pokud uzel daného názvu neexistuje
     */
    public Node getVertex(final NormalWord name) {
        Preconditions.checkNotNull(name);
        
        return this.namesToNodes.get(name);
    }
    
    /**
     * Vrátí hranu uzlu.
     * 
     * @param name název hrany
     * @return hrana či {@code null}, pokud hrana daného názvu neexistuje
     */
    public Arc getEdge(final NormalWord name) {
        Preconditions.checkNotNull(name);
        
        return this.namesToArcs.get(name);
    }
    
    /**
     * Odstraní uzel a opraví jeho nejbližší okolí (změny se dále nepropagují, neboť jsou určené změnou stupňů postižených vrcholů) tak, aby byl vzniklý graf opět konzistentní částí modelu ATN.
     * 
     * @param removed odstraněný uzel
     * @param processor procesor vypočítávající provedené nahrazení uzlů v okolí
     * @param references aktuální odkaz mezi uzly
     * @param initialNodes vstupní uzly
     * @return pokyny k aktualizaci systému na základě změny
     * @throws IllegalArgumentException pokud jsou odstraněný nebo změnou postižený uzel cílem odkazu, pak změnu nelze provést
     */
    public Update removeAndRealign(final Node removed, final RealignmentProcessor processor, final Map<? extends EnterNode, ? extends Collection<? extends RecurentArc>> references, final Set<? extends EnterNode> initialNodes) throws IllegalArgumentException {
        Preconditions.checkNotNull(removed);
        Preconditions.checkNotNull(references);
        Preconditions.checkNotNull(initialNodes);
        
        final ImmutableMap.Builder<EnterNode, RecurentArc> referencesRemovedBuilder = ImmutableMap.builder();
        final ImmutableSet.Builder<EnterNode> initialsAddedBuilder = ImmutableSet.builder();
        final ImmutableSet.Builder<EnterNode> initialsRemovedBuilder = ImmutableSet.builder();
        final ImmutableSet.Builder<NodeSwitch> affectedBuilder = ImmutableSet.builder();
        final ImmutableSet.Builder<Arc> removedEdgesBuilder = ImmutableSet.builder();
        
        extractVertex(removed, new Function<Node, Node>() {
            @Override
            public Node apply(final Node input) {
                final Node realigned = processor.realign(input);
                return realigned;
            }
        }, new Callback<Node>() {
            @Override
            public void call(final Node input) {
                final Node realigned = processor.realign(input);               
                
                if (!realigned.equals(input)) {
                    final Collection<? extends RecurentArc> referring = references.get(input);
                    if (Presence.isPresent(referring)) {
                        throw new IllegalArgumentException(ExceptionLocalizer.print("NodeRemovalForbidden", input.getName(), removed.getName(), removed.getNetwork(), referring.iterator().next().getName(), referring.iterator().next().getNetwork().getName()));
                    }
                    
                    if (initialNodes.contains(input)) {
                        initialsRemovedBuilder.add((EnterNode) input);
                    }
                    
                    if (realigned instanceof EnterNode) {
                        initialsAddedBuilder.add((EnterNode) input);
                    }
                    
                    affectedBuilder.add(NodeSwitch.of(input, realigned));
                }
            }
        }, new Callback<Arc>() {
            @Override
            public void call(final Arc parameter) {
                if (parameter instanceof RecurentArc) {
                    final RecurentArc reference = (RecurentArc) parameter;
                    final EnterNode target = reference.getTarget();
                    
                    referencesRemovedBuilder.put(target, reference);
                }
                
                removedEdgesBuilder.add(parameter);
            }
        });
        
        return Update.of(referencesRemovedBuilder.build(), initialsAddedBuilder.build(), initialsRemovedBuilder.build(), affectedBuilder.build(), removedEdgesBuilder.build());
    }
    
    /**
     * Odstraní hranu a opraví koncové uzly (změny se dále nepropagují, neboť jsou určené změnou stupňů postižených vrcholů) tak, aby byl vzniklý graf opět konzistentní částí modelu ATN.
     * 
     * @param removed odstraněná hrana
     * @param processor procesor vypočítávající provedené nahrazení uzlů v okolí
     * @param references aktuální odkaz mezi uzly
     * @param initials vstupní uzly
     * @return pokyny k aktualizaci systému na základě změny
     * @throws IllegalArgumentException pokud je změnou postižený uzel cílem odkazu, pak nemůže být změněn
     */
    public Update removeAndRealign(final Arc removed, final RealignmentProcessor processor, final Map<? extends EnterNode, ? extends Collection<? extends RecurentArc>> references, Set<? extends EnterNode> initials) throws IllegalArgumentException {
        Preconditions.checkNotNull(removed);
        Preconditions.checkNotNull(processor);
        Preconditions.checkNotNull(references);
        
        removeEdge(removed);
        
        final Node from = removed.getFrom();
        final Node to = removed.getTo();
        final Node newFrom = processor.realign(from);
        final Node newTo = processor.realign(to);
        final Collection<? extends RecurentArc> referring = references.get(from);
        try {            
            Preconditions.checkArgument(Presence.isAbsent(referring) || newFrom.equals(from) || (referring.size() == 1 && referring.contains(removed)), ExceptionLocalizer.print("ArcRemovalForbidden", from.getName(), removed.getName(), from.getNetwork().getName(), referring.iterator().next().getName(), referring.iterator().next().getNetwork().getName()));
        } catch (final Exception e) {
            add(removed, from, to);
            
            throw e;
        }
        
        replaceVertex(newFrom, from);
        replaceVertex(newTo, to);
        
        final ImmutableMap.Builder<EnterNode, RecurentArc> referencesRemovedBuilder = ImmutableMap.builder();
        final ImmutableSet.Builder<EnterNode> initialsAddedBuilder = ImmutableSet.builder();
        final ImmutableSet.Builder<EnterNode> initialsRemovedBuilder = ImmutableSet.builder();
        
        if (removed instanceof RecurentArc) {
            final RecurentArc reference = (RecurentArc) removed;
            final EnterNode target = reference.getTarget();
            
            references.get(target).remove(reference);
            referencesRemovedBuilder.put(target, reference);
        }
        
        if (!from.equals(newFrom)) {
            if (initials.contains(from)) {
                initialsRemovedBuilder.add((EnterNode) from);                    
            }
        }
        
        if (!to.equals(newTo)) {
            if (newTo instanceof EnterNode) {
                initialsAddedBuilder.add((EnterNode) newTo);
            }
        }
        
        final Set<NodeSwitch> affected = ImmutableSet.of(NodeSwitch.of(from, newFrom), NodeSwitch.of(to, newTo));
        return Update.of(referencesRemovedBuilder.build(), initialsAddedBuilder.build(), initialsRemovedBuilder.build(), affected, ImmutableSet.<Arc>of());
    }
    
    /**
     * Přidá hranu a opraví její koncové uzly (změny se dále nepropagují, neboť jsou určené změnou stupňů postižených vrcholů) tak, aby byl vzniklý graf opět konzistentní částí modelu ATN.
     * 
     * @param added přidaná hrana
     * @param from výchozí uzel hrany
     * @param to cílový uzel hrany
     * @param processor procesor vypočítávající provedené nahrazení uzlů v okolí
     * @param initials vstupní uzly
     * @return pokyny k aktualizaci systému na základě změny
     */
    public Update addAndRealign(final Arc added, final Node from, final Node to,
            final RealignmentProcessor processor, final Set<? extends EnterNode> initials) {
        Preconditions.checkNotNull(added);
        Preconditions.checkNotNull(from);
        Preconditions.checkNotNull(to);
        Preconditions.checkNotNull(processor);
        Preconditions.checkNotNull(initials);
                
        add(added, from, to);
        
        final Node newFrom = processor.realign(from);
        final Node newTo = processor.realign(to);
        
        replaceVertex(newFrom, from);
        replaceVertex(newTo, to);
        
        final ImmutableSet.Builder<EnterNode> initialsAddedBuilder = ImmutableSet.builder();
        final ImmutableSet.Builder<EnterNode> initialsRemovedBuilder = ImmutableSet.builder();
        
        if (!from.equals(newFrom)) {
            if (initials.contains(from)) {
                assert false;
            }
            
            if (newFrom instanceof EnterNode) {
                initialsAddedBuilder.add((EnterNode) newFrom);
            }
        }
        
        if (!to.equals(newTo)) {
            if (initials.contains(to)) {
                initialsRemovedBuilder.add((EnterNode) to);
            }
        }
        
        final Set<NodeSwitch> affected = ImmutableSet.of(NodeSwitch.of(from, newFrom), NodeSwitch.of(to, newTo));
        return Update.of(ImmutableMap.<EnterNode, RecurentArc>of(), initialsAddedBuilder.build(), initialsRemovedBuilder.build(), affected, ImmutableSet.<Arc>of());
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.utils.DirectedMultigraph#containsVertex(java.lang.Object)
     */
    @Override
    public boolean containsVertex(final Node vertex) {
        Preconditions.checkNotNull(vertex);
        
        return this.innerGraph.containsVertex(vertex);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.utils.DirectedMultigraph#containsEdge(java.lang.Object)
     */
    @Override
    public boolean containsEdge(final Arc edge) {
        Preconditions.checkNotNull(edge);
        
        return this.innerGraph.containsEdge(edge);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.utils.DirectedMultigraph#add(java.lang.Object)
     */
    @Override
    public void add(final Node vertex) {
        Preconditions.checkNotNull(vertex);
        
        this.innerGraph.add(vertex);
        this.namesToNodes.put(vertex.getName(), vertex);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.utils.DirectedMultigraph#add(java.lang.Object, java.lang.Object, java.lang.Object)
     */
    @Override
    public void add(final Arc edge, final Node from, final Node to) {
        Preconditions.checkNotNull(edge);
        Preconditions.checkNotNull(from);
        Preconditions.checkNotNull(to);
        
        this.innerGraph.add(edge, from, to);
        this.namesToArcs.put(edge.getName(), edge);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.utils.DirectedMultigraph#removeVertex(java.lang.Object)
     */
    @Override
    public boolean removeVertex(final Node vertex) {
        Preconditions.checkNotNull(vertex);
        
        final boolean contained = this.innerGraph.removeVertex(vertex);
        if (!contained) {
            return false;
        }
        
        this.namesToNodes.remove(vertex.getName());
        
        return true;
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.utils.DirectedMultigraph#extractVertex(java.lang.Object, com.google.common.base.Function)
     */
    @Override
    public void
            extractVertex(final Node vertex, final Function<Node, Node> neighboursRepair, final Callback<Node> neighboursCall, final Callback<Arc> connectionsCall) {
        Preconditions.checkNotNull(vertex);
        Preconditions.checkNotNull(neighboursRepair);
        Preconditions.checkNotNull(neighboursCall);
        Preconditions.checkNotNull(connectionsCall);
        
        this.innerGraph.extractVertex(vertex, neighboursRepair, neighboursCall, connectionsCall);
        this.namesToNodes.remove(vertex);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.utils.DirectedMultigraph#removeEdge(java.lang.Object)
     */
    @Override
    public boolean removeEdge(final Arc edge) {
        Preconditions.checkNotNull(edge);
        
        final boolean contained = this.innerGraph.removeEdge(edge);
        if (!contained) {
            return false;
        }
        
        this.namesToArcs.remove(edge.getName());
        
        return true;
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.utils.DirectedMultigraph#replaceVertex(java.lang.Object, java.lang.Object)
     */
    @Override
    public void replaceVertex(final Node fresh, final Node old) {
        Preconditions.checkNotNull(fresh);
        Preconditions.checkNotNull(old);
        
        this.innerGraph.replaceVertex(fresh, old);
        this.namesToNodes.remove(old.getName());
        this.namesToNodes.put(fresh.getName(), fresh);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.utils.DirectedMultigraph#replaceEdge(java.lang.Object, java.lang.Object)
     */
    @Override
    public void replaceEdge(final Arc fresh, final Arc old) {
        Preconditions.checkNotNull(fresh);
        Preconditions.checkNotNull(old);
        
        this.innerGraph.replaceEdge(fresh, old);
        this.namesToArcs.remove(old.getName());
        this.namesToArcs.put(fresh.getName(), fresh);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.utils.DirectedMultigraph#vertices()
     */
    @Override
    public Set<Node> vertices() {
        return this.innerGraph.vertices();
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.utils.DirectedMultigraph#edges()
     */
    @Override
    public Set<Arc> edges() {
        return this.innerGraph.edges();
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.utils.DirectedMultigraph#ins(java.lang.Object)
     */
    @Override
    public Set<Arc> ins(final Node vertex) {
        Preconditions.checkNotNull(vertex);
        
        return this.innerGraph.ins(vertex);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.utils.DirectedMultigraph#outs(java.lang.Object)
     */
    @Override
    public Set<Arc> outs(final Node vertex) {
        Preconditions.checkNotNull(vertex);
        
        return this.innerGraph.outs(vertex);
    }
    
    /**
     * Vrátí hrany které jsou připojené k uzlu na daném kraji
     * 
     * @param vertex uzel
     * @param direction místo připojení hran
     * @return hrany
     */
    public Set<Arc> connections(final Node vertex, final Direction direction) {
        Preconditions.checkNotNull(vertex);
        Preconditions.checkNotNull(direction);
        
        switch (direction) {
            case IN:
                    return this.innerGraph.ins(vertex);
            case OUT:
                    return this.innerGraph.outs(vertex);
                default:
                    throw new UnsupportedOperationException();
        }
    }

    /**
     * Vrátí uzel na daném konci hrany.
     * 
     * @param arc hrana
     * @param direction konec hrany
     * @return uzel
     */
    public Node attached(final Arc arc, final Direction direction) {
        Preconditions.checkNotNull(arc);
        Preconditions.checkNotNull(direction);
        
        switch (direction) {
        case IN:
                return this.innerGraph.from(arc);
        case OUT:
                return this.innerGraph.to(arc);
            default:
                throw new UnsupportedOperationException();
        }
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.utils.DirectedMultigraph#from(java.lang.Object)
     */
    @Override
    public Node from(final Arc arc) {
        Preconditions.checkNotNull(arc);
        
        return this.innerGraph.from(arc);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.utils.DirectedMultigraph#to(java.lang.Object)
     */
    @Override
    public Node to(final Arc arc) {
        Preconditions.checkNotNull(arc);
        
        return this.innerGraph.to(arc);
    }

    /**
     * Indikuje, zda-li je první uzel spojen s druhým hranou dané orientace. 
     * 
     * @param first první uzel
     * @param second druhý uzel
     * @param direction orientace hrany
     * @return zda-li je první uzel spojen s druhým hranou dané orientace
     */
    public boolean adjoins(final Node first, final Node second, final Direction direction) {
        Preconditions.checkNotNull(first);
        Preconditions.checkNotNull(second);
        Preconditions.checkNotNull(direction);
        
        final Set<Arc> connections = connections(first, direction);
        
        for (final Arc connection : connections) {
            if (connection.isAttached(second, direction)) {
                return true;
            }
        }
        
        return false;
    }
}
