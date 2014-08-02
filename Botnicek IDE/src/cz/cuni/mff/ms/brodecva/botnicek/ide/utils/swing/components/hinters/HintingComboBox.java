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
package cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.components.hinters;

import java.awt.EventQueue;
import java.awt.event.ItemEvent;
import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;


/**
 * Combo box který při psaní v něm nabízí souhlasné obsažené prvky.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @param <E> typ obsažených prvků
 */
public class HintingComboBox<E> extends JComboBox<E> implements HintListener {

    private static final long serialVersionUID = 1L;
    
    private final HintingTextFieldEditor<E> hintingTextFieldEditor;
    
    /**
     * Spustí testovací ukázku. První box nerespektuje a druhý respektuje velikost písmen při napovídání. První box dále dovoluje vepsat i nepovolené řetězce, zatímco druhý nikoli.
     * 
     * @param args argumenty
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    final List<String> content = ImmutableList.of("apple", "butter", "Chevrolet", "9 monkeys");
                    
                    final HintingComboBox<String> nonStrictCaseInsensitiveComboBox = HintingComboBox.create(content, false, false);
                    final HintingComboBox<String> strictCaseSensitiveComboBox = HintingComboBox.create(content, true, true);
                    
                    final JPanel contentPane = new JPanel();
                    contentPane.add(nonStrictCaseInsensitiveComboBox);
                    contentPane.add(strictCaseSensitiveComboBox);
                    
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
     * Vytvoří napovídající combo box.
     * 
     * @param list seznam prvků
     * @param caseSensitive nastavuje citlivost na velikost znaků
     * @param strict vynucuje užití jen napovězených řetězců
     * @return napovídající combo box
     */
    public static <E> HintingComboBox<E> create(final List<E> list, final boolean caseSensitive, final boolean strict) {
        return new HintingComboBox<>(list, caseSensitive, strict);
    }
    
    /**
     * Vytvoří napovídající combo box.
     * 
     * @param list seznam prvků
     * @param caseSensitive nastavuje citlivost na velikost znaků
     * @param strict vynucuje užití jen napovězených řetězců
     */
    protected HintingComboBox(final List<E> list, final boolean caseSensitive, final boolean strict) {
        this(HintingTextField.create(list, caseSensitive, strict));
    }
    
    /**
     * Komponenta je postavena na napovídajícím textovém poli.
     * 
     * @param hintingTextField základní textové pole
     */
    private HintingComboBox(final HintingTextField<E> hintingTextField) {
        this(HintingTextFieldEditor.create(hintingTextField), IgnoreFireContentsChangedComboBoxModel.create(hintingTextField.getDataList()));
    }
    
    private HintingComboBox(final HintingTextFieldEditor<E> hintingTextFieldEditor, final IgnoreFireContentsChangedComboBoxModel<E> model) {
        Preconditions.checkNotNull(hintingTextFieldEditor);
        Preconditions.checkNotNull(model);
        
        hintingTextFieldEditor.addHintListener(this);
        
        this.hintingTextFieldEditor = hintingTextFieldEditor;
        setEditable(true);
        setEditor(this.hintingTextFieldEditor);
        setModel(model);
    }

    /**
     * Indikuje možnost užití různě velkých znaků při napovídání.
     * 
     * @return zda-li bude napovídat i při různé velikosti písma
     */
    public final boolean isCaseSensitive() {
        return this.hintingTextFieldEditor.isCaseSensitive();
    }

    /**
     * Indikuje striktní mód - jdou zapsat jen povolené řetězce.
     * 
     * @return zda-li je nastaven striktní mód
     */
    public final boolean isStrict() {
        return this.hintingTextFieldEditor.isStrict();
    }

    /**
     * Vrátí kopii položek.
     * 
     * @return kopie položek
     */
    public final List<E> getDataList() {
        return this.hintingTextFieldEditor.getDataList();
    }

    /**
     * Nastaví napovídané (a obsažené položky).
     * 
     * @param list seznam položek
     */
    public final void setDataList(final List<E> list) {
        Preconditions.checkNotNull(list);
        
        this.hintingTextFieldEditor.setDataList(list);
        setModel(IgnoreFireContentsChangedComboBoxModel.create(list));
    }
    
    /**
     * Nastaví vybranou hodnotu.
     * 
     * @param selected vybraná hodnota
     */
    private final void setSelectedValue(final Object selected) {
        Preconditions.checkNotNull(selected);
        
        final IgnoreFireContentsChangedComboBoxModel<E> model = getCastModel();
        if (model.isIgnored()) {
            return;
        }
        
        model.setIgnored(true);
        setSelectedItem(selected);
        fireItemStateChanged(new ItemEvent(this, 701, this.selectedItemReminder,
                1));
        model.setIgnored(false);
    }

    /* (non-Javadoc)
     * @see javax.swing.JComboBox#fireActionEvent()
     */
    @Override
    protected void fireActionEvent() {
        if (getCastModel().isIgnored()) {
            return;
        }
        
        super.fireActionEvent();
    }
    
    private IgnoreFireContentsChangedComboBoxModel<E> getCastModel() {
        final ComboBoxModel<E> model = getModel();
        Preconditions.checkState(model instanceof IgnoreFireContentsChangedComboBoxModel);
        
        final IgnoreFireContentsChangedComboBoxModel<E> castModel = (IgnoreFireContentsChangedComboBoxModel<E>) model;
        return castModel;
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.components.hinters.HintListener#hinted(java.lang.Object)
     */
    @Override
    public final void hint(Object item) {
        Preconditions.checkNotNull(item);
        
        setSelectedValue(item);
    }
}
