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

import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.Document;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Intended;

/**
 * Napovídající textové pole.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @param <E>
 *            typ napovídaných prvků
 */
public class HintingTextField<E> extends JTextField implements
        SelectionChangedListener {

    private static final long serialVersionUID = 1L;

    /**
     * Vytvoří napovídající textové pole.
     * 
     * @param list
     *            seznam napovídaných prvků
     * @param caseSensitive
     *            nastaví citlivost na velikost písmen při napovídání
     * @param strict
     *            vynutí užití jen povolených slov
     * @return textové pole
     */
    public static <E> HintingTextField<E> create(final List<E> list,
            final boolean caseSensitive, final boolean strict) {
        final HintingTextField<E> newInstance = new HintingTextField<>();

        final HintingDocument<E> document =
                HintingDocument.create(list, caseSensitive, strict,
                        new SelectionBoundsProvider() {

                            @Override
                            public int getEnd() {
                                return newInstance.getSelectionEnd();
                            }

                            @Override
                            public int getStart() {
                                return newInstance.getSelectionStart();
                            }
                        });

        document.addSelectionChangedListener(newInstance);

        newInstance.initialize(document);

        return newInstance;
    }

    private HintingTextField() {
    }

    /**
     * Inicializuje pole napovídajícím modelem dokumentu.
     * 
     * @param document
     *            model napovídajícího textového pole
     */
    private HintingTextField(final HintingDocument<E> document) {
        Preconditions.checkNotNull(document);

        initialize(document);
    }

    /**
     * Přidá posluchač napovídání.
     * 
     * @param listener
     *            posluchač
     */
    final void addHintListener(final HintListener listener) {
        getCastDocument().addHintListener(listener);
    }

    private HintingDocument<E> getCastDocument() {
        final Document currentDocument = getDocument();
        Preconditions.checkState(currentDocument instanceof HintingDocument);

        @SuppressWarnings("unchecked")
        final HintingDocument<E> castCurrentDocument =
                (HintingDocument<E>) currentDocument;
        return castCurrentDocument;
    }

    /**
     * Vrátí kopii položek.
     * 
     * @return kopie položek
     */
    public final List<E> getDataList() {
        return getCastDocument().getDataList();
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.components.hinters.
     * SelectionChangedListener#selectionChanged(int, int)
     */
    @Override
    public void changeSelection(final int start, final int end) {
        Preconditions.checkArgument(start >= 0);
        Preconditions.checkArgument(end >= start);

        setSelectionStart(start);
        setSelectionEnd(end);
    }

    private void initialize(final HintingDocument<E> document) {
        setDocument(document);
        rewriteDisplayed(document.getDataList());
    }

    /**
     * Indikuje možnost užití různě velkých znaků při napovídání.
     * 
     * @return zda-li bude napovídat i při různé velikosti písma
     */
    public final boolean isCaseSensitive() {
        return getCastDocument().isCaseSensitive();
    }

    /**
     * Indikuje striktní mód - jdou zapsat jen povolené řetězce.
     * 
     * @return zda-li je nastaven striktní mód
     */
    public final boolean isStrict() {
        return getCastDocument().isStrict();
    }

    /**
     * Odebere posluchač napovídání.
     * 
     * @param listener
     *            posluchač
     */
    final void removeHintListener(final HintListener listener) {
        getCastDocument().removeHintListener(listener);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.text.JTextComponent#replaceSelection(java.lang.String)
     */
    @Override
    public void replaceSelection(final String content) {
        try {
            final Caret caret = getCaret();
            final int dot = caret.getDot();
            final int mark = caret.getMark();

            final int dotMarkMin = Math.min(dot, mark);
            final int dotMarkMax = Math.max(dot, mark);

            getCastDocument().replace(dotMarkMin, dotMarkMax - dotMarkMin,
                    content, Intended.<AttributeSet> nullReference());
        } catch (final BadLocationException e) {
            return;
        }
    }

    private void rewriteDisplayed(final List<E> list) {
        if (isStrict() && !list.isEmpty()) {
            setText(list.iterator().next().toString());
        }
    }

    /**
     * Nastaví napovídané položky.
     * 
     * @param list
     *            seznam položek
     */
    final public void setDataList(final List<E> list) {
        Preconditions.checkNotNull(list);

        getCastDocument().setDataList(list);
        rewriteDisplayed(list);
    }
}