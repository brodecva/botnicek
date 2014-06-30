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
package cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.hinting;

import java.awt.EventQueue;
import java.awt.event.ItemEvent;
import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;


public class HintingComboBox<E> extends JComboBox<E> {

    private final HintingTextFieldEditor<E> hintingTextFieldEditor;
    
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    final List<String> content = ImmutableList.of("apple", "butter", "Chevrolet", "9 monkeys");
                    
                    final HintingComboBox<String> nonStrictCaseInsensitiveComboBox = HintingComboBox.create(content, false, false);
                    final HintingComboBox<String> strictCaseSensitiveComboBox = HintingComboBox.create(content, true, true);
                    
                    final JFrame frame = new JFrame();
                    frame.add(nonStrictCaseInsensitiveComboBox);
                    frame.add(strictCaseSensitiveComboBox);
                    frame.setVisible(true);
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    public static <E> HintingComboBox<E> create(final List<E> list, final boolean caseSensitive, final boolean strict) {
        return new HintingComboBox<>(list, caseSensitive, strict);
    }
    
    protected HintingComboBox(final List<E> list, final boolean caseSensitive, final boolean strict) {
        this(HintingTextField.create(list, caseSensitive, strict));
    }
    
    private HintingComboBox(final HintingTextField<E> hintingTextField) {
        this(HintingTextFieldEditor.create(hintingTextField), new BlockedFireChangedComboBoxModel<E>(hintingTextField.getDataList()));
    }
    
    private HintingComboBox(final HintingTextFieldEditor<E> hintingTextFieldEditor, final BlockedFireChangedComboBoxModel<E> model) {
        Preconditions.checkNotNull(hintingTextFieldEditor);
        Preconditions.checkNotNull(model);
        
        hintingTextFieldEditor.addHintListener(new HintListener<E>() {
            
            @Override
            public void hinted(final E item) {
                Preconditions.checkNotNull(item);
                
                setSelectedValue(item);
            }
            
        });
        
        this.hintingTextFieldEditor = hintingTextFieldEditor;
        setEditable(true);
        setEditor(this.hintingTextFieldEditor);
        setModel(model);
    }

    public boolean isCaseSensitive() {
        return this.hintingTextFieldEditor.isCaseSensitive();
    }

    public boolean isStrict() {
        return this.hintingTextFieldEditor.isStrict();
    }

    public List<E> getDataList() {
        return this.hintingTextFieldEditor.getDataList();
    }

    public void setDataList(final List<E> list) {
        Preconditions.checkNotNull(list);
        
        this.hintingTextFieldEditor.setDataList(list);
        setModel(new BlockedFireChangedComboBoxModel<>(list));
    }
    
    void setSelectedValue(final E selected) {
        Preconditions.checkNotNull(selected);
        
        final BlockedFireChangedComboBoxModel<E> model = getCastModel();
        if (model.hasFired()) {
            return;
        }
        
        model.setFired(true);
        setSelectedItem(selected);
        fireItemStateChanged(new ItemEvent(this, 701, selectedItemReminder,
                1));
        model.setFired(false);
    }

    protected void fireActionEvent() {
        if (getCastModel().hasFired()) {
            return;
        }
        
        super.fireActionEvent();
    }
    
    private BlockedFireChangedComboBoxModel<E> getCastModel() {
        final ComboBoxModel<E> model = getModel();
        Preconditions.checkState(model instanceof BlockedFireChangedComboBoxModel);
        
        final BlockedFireChangedComboBoxModel<E> castModel = (BlockedFireChangedComboBoxModel<E>) model;
        return castModel;
    }
}
