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

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.Objects;


/**
 * Výchozí implementace výsledku kontroly.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class DefaultCheckResult implements
        CheckResult {
    
    private final boolean valid;
    private final int errorColumnNumber;
    private final int errorRowNumber;
    private final String message;
    private final Object source;
    private final Object subject;

    /**
     * Vytvoří výsledek indikující úspěšný výsledek kontroly.
     * 
     * @param source zdroj kontrolovaného řetězce
     * @param subject předmět kontroly (společný pro související výsledky)
     * @return výsledek
     */
    public static DefaultCheckResult succeed(final Object source, final Object subject) {
        return new DefaultCheckResult(true, OK_NUMBER, OK_NUMBER, "OK", source, subject);
    }
    
    /**
     * Vytvoří výsledek indikující neúspěšný výsledek kontroly víceřádkového řetězce.
     * @param rowNumber číslo řádky s chybou
     * @param columnNumber číslo sloupce s chybou
     * @param message chybová zpráva
     * @param source zdroj kontrolovaného řetězce
     * @param subject předmět kontroly (společný pro související výsledky)
     * 
     * @return výsledek
     */
    public static DefaultCheckResult fail(final int rowNumber, final int columnNumber, final String message, final Object source, final Object subject) {
        return new DefaultCheckResult(false, rowNumber, columnNumber, message, source, subject);
    }
    
    /**
     * Vytvoří výsledek indikující neúspěšný výsledek kontroly řetězce bez více řádků.
     * @param columnNumber číslo sloupce s chybou
     * @param message chybová zpráva
     * @param source zdroj kontrolovaného řetězce
     * @param subject předmět kontroly (společný pro související výsledky)
     * 
     * @return výsledek
     */
    public static DefaultCheckResult fail(final int columnNumber, final String message, final Object source, final Object subject) {
        return new DefaultCheckResult(false, NO_ROWS_DEFAULT_ROW_NUMBER, columnNumber, message, source, subject);
    }
    
    private DefaultCheckResult(final boolean valid, final int rowNumber, final int columnNumber, final String message, final Object source, Object subject) {
        Preconditions.checkNotNull(source);
        Preconditions.checkNotNull(subject);
        Preconditions.checkNotNull(message);
        Preconditions.checkArgument(rowNumber >= -1);
        Preconditions.checkArgument(columnNumber >= -1);
        Preconditions.checkArgument(!valid || (rowNumber == OK_NUMBER && columnNumber == OK_NUMBER));
        
        this.source = source;
        this.subject = subject;
        this.valid = valid;
        this.errorRowNumber = rowNumber;
        this.errorColumnNumber = columnNumber;
        this.message = message;
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.CheckResult#isValid()
     */
    @Override
    public boolean isValid() {
        return this.valid;
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.CheckResult#getErrorLineNumber()
     */
    public int getErrorLineNumber() {
        return errorRowNumber;
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.CheckResult#getErrorColumnNumber()
     */
    public int getErrorColumnNumber() {
        return errorColumnNumber;
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.editor.checker.CheckResult#printMessage()
     */
    @Override
    public String getMessage() {
        return this.message;
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.edit.check.model.CheckResult#getSource()
     */
    @Override
    public Object getSource() {
        return this.source;
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.CheckResult#getSubject()
     */
    @Override
    public Object getSubject() {
        return this.subject;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.subject.hashCode();
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (Objects.isNull(obj)) {
            return false;
        }
        if (!(obj instanceof CheckResult)) {
            return false;
        }
        final CheckResult other = (CheckResult) obj;
        if (!this.subject.equals(other.getSubject())) {
            return false;
        }
        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("DefaultCheckResult [subject=");
        builder.append(subject);
        builder.append(", valid=");
        builder.append(valid);
        builder.append(", source=");
        builder.append(source);
        builder.append(", errorRowNumber=");
        builder.append(errorRowNumber);
        builder.append(", errorColumnNumber=");
        builder.append(errorColumnNumber);
        builder.append(", message=");
        builder.append(message);
        builder.append("]");
        return builder.toString();
    }
}