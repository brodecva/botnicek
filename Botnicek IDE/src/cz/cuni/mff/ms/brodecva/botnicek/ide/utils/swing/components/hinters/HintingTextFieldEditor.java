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

import java.io.Serializable;
import java.util.List;

import javax.swing.plaf.basic.BasicComboBoxEditor;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;


/**
 * Editor combo boxu implementovaný pomocí napovídajícího textového pole.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @param <E> typ napovídaných položek
 */
final class HintingTextFieldEditor<E> extends BasicComboBoxEditor implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Vytvoří editor.
     * 
     * @param textField napovídající textové pole
     * @return napovídající editor
     */
    public static <E> HintingTextFieldEditor<E> create(final HintingTextField<E> textField) {
        return new HintingTextFieldEditor<E>(textField);
    }
    
    private HintingTextFieldEditor(final HintingTextField<E> textField) {
        Preconditions.checkNotNull(textField);
        
        super.editor = textField;
    }
    
    /**
     * Vrátí implementující textové pole.
     * 
     * @return vlastní textové pole
     */
    public HintingTextField<E> getTextField() {
        @SuppressWarnings("unchecked")
        final HintingTextField<E> castSuperEditor = (HintingTextField<E>) super.editor;
        
        return castSuperEditor;
    }

    /**
     * Indikuje možnost užití různě velkých znaků při napovídání.
     * 
     * @return zda-li bude napovídat i při různé velikosti písma
     */
    public boolean isCaseSensitive() {
        return getTextField().isCaseSensitive();
    }

    /**
     * Indikuje striktní mód - jdou zapsat jen povolené řetězce.
     * 
     * @return zda-li je nastaven striktní mód
     */
    public boolean isStrict() {
        return getTextField().isStrict();
    }

    /**
     * Vrátí kopii položek.
     * 
     * @return kopie položek
     */
    public List<E> getDataList() {
        return ImmutableList.copyOf(getTextField().getDataList());
    }
    
    /**
     * Nastaví napovídané položky.
     * 
     * @param list seznam položek
     */
    public void setDataList(final List<E> list) {
        Preconditions.checkNotNull(list);
        
        getTextField().setDataList(list);
    }
    
    /**
     * Přidá posluchač napovídání.
     * 
     * @param listener posluchač
     */
    void addHintListener(final HintListener listener) {
        Preconditions.checkNotNull(listener);
        
        getTextField().addHintListener(listener);
    }
    
    /**
     * Odebere posluchač napovídání.
     * 
     * @param listener posluchač
     */
    void removeHintListener(final HintListener listener) {
        Preconditions.checkNotNull(listener);
        
        getTextField().removeHintListener(listener);
    }
}