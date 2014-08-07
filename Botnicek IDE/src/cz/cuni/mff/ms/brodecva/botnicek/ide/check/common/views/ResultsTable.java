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
package cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.views;

import java.awt.Component;
import java.util.List;
import java.util.Set;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.table.TableColumn;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.controllers.CheckController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.CheckResult;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Intended;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.Components;

/**
 * <p>Správce tabulky s výsledky neúspěšných kontrol.</p>
 * <p>Umožňuje aktualizovat výsledky tak, že nahrazuje ty, které mají stejný předmět. Výsledek úspěšné kontroly nebude přidán, pouze smaže související negativní.</p>
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class ResultsTable implements CheckView {

    private static final int DEFAULT_COLUMN_WIDTH = 100;
    private static final List<Integer> COLUMNS_SIZES = ImmutableList.of(DEFAULT_COLUMN_WIDTH * 10, DEFAULT_COLUMN_WIDTH * 3, DEFAULT_COLUMN_WIDTH, DEFAULT_COLUMN_WIDTH);
    
    private final Set<CheckController> checkControllers;
    
    private final JTable table;
    
    /**
     * Vytvoří správce tabulky.
     * 
     * @param parent rodičovský posunovací panel
     * @param checkControllers řadiče kontrol
     * @return správce tabulky
     */
    public static ResultsTable create(final JScrollPane parent, final Set<CheckController> checkControllers) {
        return create(parent, checkControllers, ResultsTableModel.create());
    }
    
    /**
     * Vytvoří správce tabulky.
     * 
     * @param parent rodičovský posunovací panel
     * @param checkControllers řadiče kontrol
     * @param model model tabulky
     * @return správce tabulky
     */
    static ResultsTable create(final JScrollPane parent, final Set<CheckController> checkControllers, final ResultsTableModel model) {
        Preconditions.checkNotNull(parent);
        Preconditions.checkNotNull(checkControllers);
        Preconditions.checkNotNull(model);
        
        final Set<CheckController> checkControllersCopy = ImmutableSet.copyOf(checkControllers);
        
        final ResultsTable newInstance = new ResultsTable(checkControllersCopy, model);
               
        newInstance.subscribe(checkControllersCopy);
        newInstance.addToParent(parent);
        
        return newInstance;
    }

    
    private ResultsTable(final Set<CheckController> checkControllers, final ResultsTableModel model) {
        Preconditions.checkNotNull(model);
        Preconditions.checkNotNull(checkControllers);
        
        this.table = new JTable(model);
        this.checkControllers = ImmutableSet.copyOf(checkControllers);
        
        final int columnCount = this.table.getColumnCount();
        assert columnCount == COLUMNS_SIZES.size();
        for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
            final TableColumn column = this.table.getColumnModel().getColumn(columnIndex);
            
            column.setPreferredWidth(COLUMNS_SIZES.get(columnIndex));
        }
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.views.CheckResultView#updateResult(cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.CheckResult)
     */
    @Override
    public void updateResult(final CheckResult result) {
        Preconditions.checkNotNull(result);
        
        getModel().updateResult(result);
    }
    
    private ResultsTableModel getModel() {
        return (ResultsTableModel) this.table.getModel();
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.views.CheckResultView#closed()
     */
    @Override
    public void closed() {
        unsubscribe();
        removeFromParent();
    }
    
    private void addToParent(final JScrollPane parent) {
        parent.setViewportView(this.table);
    }
    
    private void removeFromParent() {
        final JViewport parent = (JViewport) this.table.getParent();
        Preconditions.checkState(Components.hasParent(parent));
        
        parent.setView(Intended.<Component>nullReference());
    }
    
    private void subscribe(final Set<CheckController> checkControllers) {
        for (final CheckController checkController : checkControllers) {
            checkController.addView(this);
        }
    }
    
    private void unsubscribe() {
        for (final CheckController checkController : this.checkControllers) {
            checkController.removeView(this);
        }
    }
}
