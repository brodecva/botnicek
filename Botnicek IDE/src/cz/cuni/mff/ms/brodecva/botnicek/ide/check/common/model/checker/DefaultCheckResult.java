/**
 * Copyright Václav Brodec 2014.
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
package cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.checker;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.Objects;

/**
 * Výchozí implementace výsledku kontroly.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class DefaultCheckResult implements CheckResult {

    private static final String DEFAULT_OK_MESSAGE = "OK";

    /**
     * Vytvoří výsledek indikující neúspěšný výsledek kontroly víceřádkového
     * řetězce.
     * 
     * @param rowNumber
     *            číslo řádky s chybou
     * @param columnNumber
     *            číslo sloupce s chybou
     * @param message
     *            chybová zpráva
     * @param source
     *            zdroj kontrolovaného řetězce
     * @param subject
     *            předmět kontroly (společný pro související výsledky)
     * 
     * @return výsledek
     */
    public static DefaultCheckResult fail(final int rowNumber,
            final int columnNumber, final String message, final Object source,
            final Object subject) {
        return new DefaultCheckResult(false, rowNumber, columnNumber, message,
                source, subject);
    }

    /**
     * Vytvoří výsledek indikující neúspěšný výsledek kontroly řetězce bez více
     * řádků.
     * 
     * @param columnNumber
     *            číslo sloupce s chybou
     * @param message
     *            chybová zpráva
     * @param source
     *            zdroj kontrolovaného řetězce
     * @param subject
     *            předmět kontroly (společný pro související výsledky)
     * 
     * @return výsledek
     */
    public static DefaultCheckResult fail(final int columnNumber,
            final String message, final Object source, final Object subject) {
        return new DefaultCheckResult(false, NO_ROWS_DEFAULT_ROW_NUMBER,
                columnNumber, message, source, subject);
    }

    /**
     * Vytvoří výsledek indikující úspěšný výsledek kontroly.
     * 
     * @param source
     *            zdroj kontrolovaného řetězce
     * @param subject
     *            předmět kontroly (společný pro související výsledky)
     * @return výsledek
     */
    public static DefaultCheckResult succeed(final Object source,
            final Object subject) {
        return new DefaultCheckResult(true, OK_NUMBER, OK_NUMBER,
                DEFAULT_OK_MESSAGE, source, subject);
    }

    private final boolean valid;
    private final int errorColumnNumber;
    private final int errorRowNumber;

    private final String message;

    private final Object source;

    private final Object subject;

    private DefaultCheckResult(final boolean valid, final int rowNumber,
            final int columnNumber, final String message, final Object source,
            final Object subject) {
        Preconditions.checkNotNull(source);
        Preconditions.checkNotNull(subject);
        Preconditions.checkNotNull(message);
        Preconditions.checkArgument(rowNumber >= -1);
        Preconditions.checkArgument(columnNumber >= -1);
        assert !valid || (rowNumber == OK_NUMBER && columnNumber == OK_NUMBER);

        this.source = source;
        this.subject = subject;
        this.valid = valid;
        this.errorRowNumber = rowNumber;
        this.errorColumnNumber = columnNumber;
        this.message = message;
    }

    /*
     * (non-Javadoc)
     * 
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

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.CheckResult#
     * getErrorColumnNumber()
     */
    @Override
    public int getErrorColumnNumber() {
        return this.errorColumnNumber;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.CheckResult#
     * getErrorLineNumber()
     */
    @Override
    public int getErrorLineNumber() {
        return this.errorRowNumber;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.editor.checker.CheckResult#printMessage
     * ()
     */
    @Override
    public String getMessage() {
        return this.message;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.edit.check.model.CheckResult#getSource
     * ()
     */
    @Override
    public Object getSource() {
        return this.source;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.CheckResult#
     * getSubject()
     */
    @Override
    public Object getSubject() {
        return this.subject;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.subject.hashCode();
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.CheckResult#isValid
     * ()
     */
    @Override
    public boolean isValid() {
        return this.valid;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("DefaultCheckResult [subject=");
        builder.append(this.subject);
        builder.append(", valid=");
        builder.append(this.valid);
        builder.append(", source=");
        builder.append(this.source);
        builder.append(", errorRowNumber=");
        builder.append(this.errorRowNumber);
        builder.append(", errorColumnNumber=");
        builder.append(this.errorColumnNumber);
        builder.append(", message=");
        builder.append(this.message);
        builder.append("]");
        return builder.toString();
    }
}