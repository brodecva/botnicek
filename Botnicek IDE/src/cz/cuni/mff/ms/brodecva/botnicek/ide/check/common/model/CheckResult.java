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
package cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model;

/**
 * Výsledek kontroly výrazu pro konverzi na datový typ.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public interface CheckResult {
    
    /**
     * Číslo řádku v případě úspěšného výsledku kontroly.
     */
    int OK_NUMBER = 0;
    
    /**
     * Číslo řádku v případě, že vstup není víceřádkový.
     */
    int NO_ROWS_DEFAULT_ROW_NUMBER = 1;
    
    /**
     * Indikuje, zda-li byl řetězec validní.
     * 
     * @return zda-li byl řetězec validní
     */
    boolean isValid();
    
    /**
     * Vrátí zdroj řetězce.
     * 
     * @return zdroj řetězec
     */
    Object getSource();
    
    /**
     * Vrátí identifikátor souvisejících výsledků. Klienti validačního mechanismu mohou přiřazovat požadavkům o validaci vlastní předmět, který dovoluje aktualizovat výsledky týkající se stejného předmětu.
     * 
     * @return identifikátor souvisejících výsledků
     */
    Object getSubject();
    
    /**
     * Vrátí chybovou zprávu.
     * 
     * @return chybová zpráva
     */
    String getMessage();
    
    /**
     * Vrátí číslo řádku s chybou.
     * 
     * @return číslo řádku s chybou
     */
    int getErrorLineNumber();
    
    /**
     * Vrátí číslo sloupce s chybou.
     * 
     * @return číslo sloupce s chybou
     */
    int getErrorColumnNumber();
    
    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode();
    
    /** 
     * Porovná výsledek s objektem. Shoduje se pouze s dalším výsledkem, který má stejný předmět.
     */
    @Override
    public boolean equals(Object obj);
}
