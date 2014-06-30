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

import java.util.List;
import javax.swing.JTextField;
import javax.swing.text.*;

import com.google.common.base.Preconditions;

public class HintingTextField<E> extends JTextField {

    public static <E> HintingTextField<E> create(final List<E> list,
            final boolean caseSensitive, final boolean strict) {
        final HintingTextField<E> newInstance = new HintingTextField<>();

        final HintingDocument<E> document =
                HintingDocument.create(list, caseSensitive, strict,
                        new SelectionBoundsProvider() {

                            @Override
                            public int getStart() {
                                return newInstance.getSelectionStart();
                            }

                            @Override
                            public int getEnd() {
                                return newInstance.getSelectionEnd();
                            }
                        });

        document.addSelectionChangedListener(new SelectionChangedListener() {
            
            @Override
            public void selectionChanged(final int start, final int end) {
                Preconditions.checkArgument(start >= 0);
                Preconditions.checkArgument(end >= start);
                
                newInstance.setSelectionStart(start);
                newInstance.setSelectionEnd(end);
            }
        });
        
        newInstance.initialize(document);

        return newInstance;
    }

    protected HintingTextField(final HintingDocument<E> document) {
        Preconditions.checkNotNull(document);

        initialize(document);
    }

    private void initialize(final HintingDocument<E> document) {
        setDocument(document);
        rewriteDisplayed(document.getDataList());
    }

    private HintingTextField() {
    }

    private HintingDocument<E> getCastDocument() {
        final Document currentDocument = getDocument();
        Preconditions.checkState(currentDocument instanceof HintingDocument);

        @SuppressWarnings("unchecked")
        final HintingDocument<E> castCurrentDocument =
                (HintingDocument<E>) currentDocument;
        return castCurrentDocument;
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
                    content, null);
        } catch (final BadLocationException e) {
            return;
        }
    }

    public boolean isCaseSensitive() {
        return getCastDocument().isCaseSensitive();
    }

    public boolean isStrict() {
        return getCastDocument().isStrict();
    }

    public List<E> getDataList() {
        return getCastDocument().getDataList();
    }

    public void setDataList(final List<E> list) {
        getCastDocument().setDataList(list);
        rewriteDisplayed(list);
    }

    private void rewriteDisplayed(final List<E> list) {
        if (isStrict() && !list.isEmpty()) {
            setText(list.iterator().next().toString());
        }
    }

    void addHintListener(final HintListener<E> listener) {
        getCastDocument().addHintListener(listener);
    }

    void removeHintListener(final HintListener<E> listener) {
        getCastDocument().removeHintListener(listener);
    }
}