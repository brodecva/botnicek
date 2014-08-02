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

import java.util.List;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.DefaultEventManager;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.EventManager;

/**
 * Dokument implementující napovídání.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @param <E> typ napovídaných položek
 */
final class HintingDocument<E> extends PlainDocument {

    private static final long serialVersionUID = 1L;
    
    private final boolean caseSensitive;
    private final boolean strict;
    private final SelectionBoundsProvider selectionBoundsProvider;
    private final EventManager eventManager;
    
    private List<E> list;
    
    /**
     * Vytvoří napovídající dokument.
     * 
     * @param list seznam napovídaných prvků
     * @param caseSensitive nastaví citlivost na velikost písmen při napovídání
     * @param strict vynutí užití jen povolených slov
     * @param selectionBoundsProvider poskytovatel mezí výběru
     * @return textové pole
     */
    public static <E> HintingDocument<E> create(final List<E> list, final boolean caseSensitive, final boolean strict, final SelectionBoundsProvider selectionBoundsProvider) {
        return create(list, caseSensitive, strict, selectionBoundsProvider, DefaultEventManager.create());
    }
    
    /**
     * Vytvoří napovídající dokument.
     * 
     * @param list seznam napovídaných prvků
     * @param caseSensitive nastaví citlivost na velikost písmen při napovídání
     * @param strict vynutí užití jen povolených slov
     * @param selectionBoundsProvider poskytovatel mezí výběru
     * @param eventManager správce událostí
     * @return textové pole
     */
    public static <E> HintingDocument<E> create(final List<E> list, final boolean caseSensitive, final boolean strict, final SelectionBoundsProvider selectionBoundsProvider, final EventManager eventManager) {
        Preconditions.checkNotNull(list);
        Preconditions.checkNotNull(selectionBoundsProvider);
        Preconditions.checkNotNull(eventManager);
        
        return new HintingDocument<>(ImmutableList.copyOf(list), caseSensitive, strict, selectionBoundsProvider, eventManager);
    }
        
    private HintingDocument(final List<E> list, final boolean caseSensitive, final boolean strict, final SelectionBoundsProvider selectionStartProvider, final EventManager eventManager) {
        assert list != null;
        assert selectionStartProvider != null;
        assert eventManager != null;
        
        this.list = list;
        this.strict = strict;
        this.caseSensitive = caseSensitive;
        this.selectionBoundsProvider = selectionStartProvider;
        this.eventManager = eventManager;
    }
    
    /**
     * Přidá posluchač nápověd.
     * 
     * @param listener posluchač
     */
    public void addHintListener(final HintListener listener) {
        Preconditions.checkNotNull(listener);
        
        this.eventManager.addListener(HintEvent.class, listener);
    }
    
    /**
     * Odebere posluchač nápověd.
     * 
     * @param listener posluchač
     */
    public void removeHintListener(final HintListener listener) {
        Preconditions.checkNotNull(listener);
        
        this.eventManager.removeListener(HintEvent.class, listener);
    }
    
    private void fireHintEvent(final E item) {
        final HintEvent event = HintEvent.create(item);
        
        this.eventManager.fire(event);
    }
    
    /**
     * Přidá posluchač změny hranic výběru.
     * 
     * @param listener posluchač
     */
    public void addSelectionChangedListener(final SelectionChangedListener listener) {
        Preconditions.checkNotNull(listener);
        
        this.eventManager.addListener(SelectionChangedEvent.class, listener);
    }
    
    /**
     * Odebere posluchač změny hranic výběru.
     * 
     * @param listener posluchač
     */
    public void removeSelectionChangedListener(final SelectionChangedListener listener) {
        Preconditions.checkNotNull(listener);
        
        this.eventManager.removeListener(SelectionChangedEvent.class, listener);
    }
    
    private void fireSelectionChangedEvent(final int start, final int end) {
        final SelectionChangedEvent event = SelectionChangedEvent.create(start, end);
        
        this.eventManager.fire(event);
    }
    
    /* (non-Javadoc)
     * @see javax.swing.text.AbstractDocument#replace(int, int, java.lang.String, javax.swing.text.AttributeSet)
     */
    @Override
    public void replace(final int offset, final int length, final String text, final AttributeSet attributes)
            throws BadLocationException {
        super.remove(offset, length);
        
        insertString(offset, text, attributes);
    }

    /* (non-Javadoc)
     * @see javax.swing.text.PlainDocument#insertString(int, java.lang.String, javax.swing.text.AttributeSet)
     */
    @Override
    public void insertString(final int offset, final String string, final AttributeSet attributes)
            throws BadLocationException {
        if (string == null || string.isEmpty()) {
            return;
        }
        
        final String textFromStartToOffset = getText(0, offset);
        final E matched = match(textFromStartToOffset + string);
        final int selectionStart = offset + string.length() - 1;
        
        final E ultimateMatched;
        final int ultimateSelectionStart;
        if (matched == null) {
            if (this.strict) {
                ultimateMatched = match(textFromStartToOffset);
                ultimateSelectionStart = selectionStart - 1;
            } else {
                super.insertString(offset, string, attributes);
                return;
            }
        } else {
            ultimateMatched = matched;
            ultimateSelectionStart = selectionStart;
            
            fireHintEvent(ultimateMatched);
        }
        
        super.remove(0, getLength());
        super.insertString(0, (ultimateMatched == null ? null : ultimateMatched.toString()), attributes);
        
        fireSelectionChangedEvent(ultimateSelectionStart + 1, getLength());
    }

    public void remove(final int offset, final int length) throws BadLocationException {
        Preconditions.checkArgument(offset >= 0);
        Preconditions.checkArgument(length >= 0);
        
        final int selectionStart = this.selectionBoundsProvider.getStart();
        final int ultimateSelectionStart;
        if (selectionStart > 0) {
            ultimateSelectionStart = selectionStart - 1;
        } else {
            ultimateSelectionStart = selectionStart;
        }
        
        final E matched = match(getText(0, ultimateSelectionStart));
        if (matched == null) {
            if (this.strict) {
                super.remove(0, getLength());
            } else {
                super.remove(offset, length);
            }
        } else {
            super.remove(0, getLength());
            super.insertString(0, matched.toString(), null);
            
            fireHintEvent(matched);
        }
        
        fireSelectionChangedEvent(ultimateSelectionStart, getLength());
    }
    
    private E match(final String value) {
        final String normalizedValue = normalize(value);
        
        for (final E item : this.list) {
            final String itemValue = item.toString();
            if (itemValue == null) {
                continue;
            }
            
            if (normalize(itemValue).startsWith(normalizedValue)) {
                return item;
            }
        }

        return null;
    }

    private String normalize(final String string) {
        if (!this.caseSensitive) {
            return string.toLowerCase();
        }
        
        return string;
    }

    /**
     * Indikuje možnost užití různě velkých znaků při napovídání.
     * 
     * @return zda-li bude napovídat i při různé velikosti písma
     */
    public boolean isCaseSensitive() {
        return this.caseSensitive;
    }

    /**
     * Indikuje striktní mód - jdou zapsat jen povolené řetězce.
     * 
     * @return zda-li je nastaven striktní mód
     */
    public boolean isStrict() {
        return this.strict;
    }

    /**
     * Vrátí kopii položek.
     * 
     * @return kopie položek
     */
    public List<E> getDataList() {
        return this.list;
    }
    
    /**
     * Nastaví napovídané položky.
     * 
     * @param list seznam položek
     */
    public void setDataList(final List<E> list) {
        Preconditions.checkNotNull(list);
        
        this.list = ImmutableList.copyOf(list);
    }
}