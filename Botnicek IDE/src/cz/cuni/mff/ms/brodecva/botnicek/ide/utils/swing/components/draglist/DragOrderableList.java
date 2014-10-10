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
package cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.components.draglist;

import java.awt.EventQueue;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.DropMode;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

/**
 * Seznam, který je možno uspořádat tahem prvků.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @param <E>
 *            typ prvků v seznamu
 */
public final class DragOrderableList<E> extends JList<E> {

    private static final long serialVersionUID = 1L;

    /**
     * Vytvořený prázdný seznam s využitím výchozího modelu.
     * 
     * @return prázdný seznam
     */
    public static <E> DragOrderableList<E> create() {
        return create(new DefaultListModel<E>());
    }

    /**
     * Vytvoří seznam na základě dodaného modelu.
     * 
     * @param model
     *            inicializační model
     * @return seznam inicializovaný daným modelem
     */
    public static <E> DragOrderableList<E> create(
            final DefaultListModel<E> model) {
        Preconditions.checkNotNull(model);

        final DragOrderableList<E> newInstance =
                new DragOrderableList<E>(model);

        newInstance.setDragEnabled(true);
        newInstance.setDropMode(DropMode.INSERT);
        newInstance.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        newInstance.setTransferHandler(DropHandler.create(newInstance));
        DragListener.create(newInstance);

        return newInstance;
    }

    /**
     * Spustí testovací ukázku.
     * 
     * @param args
     *            argumenty
     */
    public static void main(final String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager
                            .getSystemLookAndFeelClassName());

                    final DefaultListModel<String> model =
                            new DefaultListModel<>();
                    model.addElement("one");
                    model.addElement("two");
                    model.addElement("three");

                    final DragOrderableList<String> list =
                            DragOrderableList.<String> create(model);

                    final JPanel contentPane = new JPanel();
                    contentPane.add(new JScrollPane(list));

                    final JFrame frame = new JFrame();
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    frame.setContentPane(contentPane);
                    frame.pack();
                    frame.setVisible(true);
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private DragOrderableList(final DefaultListModel<E> model) {
        super(model);
    }

    /**
     * Přidá prvek do seznamu.
     * 
     * @param element
     *            přidá prvek do seznamu
     */
    public final void addElement(final E element) {
        Preconditions.checkNotNull(element);

        final ListModel<E> model = getModel();

        Preconditions.checkState(model instanceof DefaultListModel<?>);
        final DefaultListModel<E> defaultModel = (DefaultListModel<E>) model;

        defaultModel.addElement(element);
    }

    /**
     * Vrátí všechny, vybrané i nevybrané hodnoty seznamu.
     * 
     * @return všechny hodnoty seznamu
     */
    public final List<E> getValues() {
        final ListModel<E> model = getModel();
        final ImmutableList.Builder<E> result = ImmutableList.builder();

        for (int index = 0; index < model.getSize(); index++) {
            result.add(model.getElementAt(index));
        }

        return result.build();
    }

    /**
     * Přesune prvek v rámci seznamu. Indexy musí být v mezích počtu prvků.
     * 
     * @param fromIndex
     *            původní index prvku
     * @param toIndex
     *            nový index prvku
     * @throws IllegalStateException
     *             pokud byl změněn model seznamu na jiný než výchozí
     */
    public final void moveToIndex(final int fromIndex, final int toIndex) {
        final ListModel<E> model = getModel();
        Preconditions.checkPositionIndex(fromIndex, model.getSize());
        Preconditions.checkPositionIndex(toIndex, model.getSize());
        if (fromIndex == toIndex) {
            return;
        }

        Preconditions.checkState(model instanceof DefaultListModel<?>);
        final DefaultListModel<E> defaultModel = (DefaultListModel<E>) model;

        if (fromIndex > toIndex) {
            final E movedElement = defaultModel.remove(fromIndex);
            defaultModel.add(toIndex, movedElement);
        } else if (fromIndex < toIndex) {
            final E movedElement = defaultModel.get(fromIndex);
            defaultModel.add(toIndex, movedElement);
            defaultModel.remove(fromIndex);
        } else {
            assert false;
        }
    }

    /**
     * Smaže prvek daného indexu.
     * 
     * @param index
     *            index smazaného prvku
     * @throws IllegalStateException
     *             pokud byl změněn model na jiný než výchozí
     */
    public final void removeElementAt(final int index) {
        final ListModel<E> model = getModel();
        Preconditions.checkPositionIndex(index, model.getSize());

        Preconditions.checkState(model instanceof DefaultListModel<?>);
        final DefaultListModel<E> defaultModel = (DefaultListModel<E>) model;

        defaultModel.remove(index);
    }

    /**
     * Smaže vybraný prvek seznamu.
     */
    public final void removeSelected() {
        final int selectedIndex = getSelectedIndex();
        if (selectedIndex == -1) {
            return;
        }

        assert getSelectionMode() == ListSelectionModel.SINGLE_SELECTION;

        removeElementAt(selectedIndex);
    }

    /**
     * Nastaví nový výchozí model seznamu.
     * 
     * @param data
     *            prvky modelu
     */
    public final void setDefaultModel(final List<E> data) {
        Preconditions.checkNotNull(data);

        final DefaultListModel<E> model = new DefaultListModel<>();

        for (final E element : data) {
            model.addElement(element);
        }

        setModel(model);
    }
}