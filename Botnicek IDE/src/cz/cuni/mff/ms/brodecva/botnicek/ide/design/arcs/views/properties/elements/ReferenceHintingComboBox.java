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
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.properties.elements;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JPanel;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Ordering;

import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.properties.AvailableReferencesView;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.EnterNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Intended;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.components.hinters.HintingComboBox;

/**
 * ComboBox nabízející dostupné vstupní uzly pro zanoření do sítě během výpočtu.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class ReferenceHintingComboBox extends HintingComboBox<EnterNode> implements AvailableReferencesView {

    private static final long serialVersionUID = 1L;
    
    private static final boolean CASE_SENSITIVE = false;
    private static final boolean STRICT = false;
    private static final Ordering<EnterNode> REFERENCE_ORDERING = Ordering.from(new Comparator<EnterNode>() {

        @Override
        public int compare(final EnterNode first, final EnterNode second) {
            Preconditions.checkNotNull(first);
            Preconditions.checkNotNull(second);
            
            return first.getName().compareTo(second.getName());
        }
        
    });

    /**
     * Spustí testovací verzi.
     * 
     * @param args argumenty
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    final ReferenceHintingComboBox box = ReferenceHintingComboBox.create();
                    
                    final JPanel contentPane = new JPanel(new BorderLayout());
                    contentPane.add(box, BorderLayout.CENTER);
                    
                    final JFrame frame = new JFrame();
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.setContentPane(contentPane);
                    frame.pack();
                    frame.setVisible(true);
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    /**
     * Vytvoří ComboBox.
     * 
     * @return ComboBox
     */
    public static ReferenceHintingComboBox create() {
        return new ReferenceHintingComboBox(ImmutableList.<EnterNode>of(), CASE_SENSITIVE, STRICT);        
    }

    private ReferenceHintingComboBox(final List<EnterNode> list,
            final boolean caseSensitive, final boolean strict) {
        super(list, caseSensitive, strict);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.arcs.ArcView#updatedAvailableReferences(java.util.Set)
     */
    @Override
    public void updateAvailableReferences(final Set<EnterNode> references) {
        Preconditions.checkNotNull(references);
        
        final Object selected = getSelectedItem();
        
        final List<EnterNode> update = REFERENCE_ORDERING.immutableSortedCopy(references);
        setDataList(update);
        
        if (isSomeItemSelected(selected)) {
            if (references.contains(selected)) {
                setSelectedItem(selected);
            } else {                
                setSelectedIndex(-1);
            }
        }
    }
    
    private boolean isSomeItemSelected(final Object selectedItemResult) {
        return selectedItemResult != Intended.nullReference();
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.AvailableReferencesView#extendedAvailableReferences(java.util.Set)
     */
    @Override
    public void extendAvailableReferences(final Set<EnterNode> extension) {
        Preconditions.checkNotNull(extension);
        
        final Set<EnterNode> updated = new HashSet<>(getDataList());
        updated.addAll(extension);
        
        final Object selected = getSelectedItem();
        setDataList(REFERENCE_ORDERING.immutableSortedCopy(updated));
        
        if (isSomeItemSelected(selected)) {
            assert updated.contains(selected);
            
            setSelectedItem(selected);
        }
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.AvailableReferencesView#removedAvailableReferences(java.util.Set)
     */
    @Override
    public void removeAvailableReferences(final Set<EnterNode> removed) {
        Preconditions.checkNotNull(removed);
        
        final Set<EnterNode> updated = new HashSet<>(getDataList());
        updated.removeAll(removed);
        
        final Object selected = getSelectedItem();
        setDataList(REFERENCE_ORDERING.immutableSortedCopy(updated));
        
        if (isSomeItemSelected(selected)) {
            if (updated.contains(selected)) {
                setSelectedItem(selected);
            } else {                
                setSelectedIndex(-1);
            }
        }
    }
}
