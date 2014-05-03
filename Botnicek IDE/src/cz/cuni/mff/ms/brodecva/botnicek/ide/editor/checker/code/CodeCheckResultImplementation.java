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
package cz.cuni.mff.ms.brodecva.botnicek.ide.editor.checker.code;

import com.google.common.base.Preconditions;


/**
 * @author Václav Brodec
 * @version 1.0
 */
public final class CodeCheckResultImplementation implements
        CodeCheckResult {
    
    private static final int OK_NUMBER = 0;
    
    private final boolean valid;
    private final int errorColumnNumber;
    private final int errorRowNumber;
    private final String message;
    
    private final Object[] messageParameters;

    public static CodeCheckResultImplementation create(final boolean valid,  final int rowNumber, final int columnNumber, final String message, final Object... messageParameters) {
        return new CodeCheckResultImplementation(valid, rowNumber, columnNumber, message, messageParameters);
    }
    
    public static CodeCheckResultImplementation succeed() {
        return new CodeCheckResultImplementation(true, OK_NUMBER, OK_NUMBER, "OK");
    }
    
    public static CodeCheckResultImplementation fail(final int rowNumber, final int columnNumber, final String message, final Object... messageParameters) {
        return new CodeCheckResultImplementation(false, rowNumber, columnNumber, message, messageParameters);
    }
    
    private CodeCheckResultImplementation(final boolean valid, final int rowNumber, final int columnNumber, final String message, final Object... messageParameters) {
        Preconditions.checkNotNull(message);
        Preconditions.checkNotNull(messageParameters);
        Preconditions.checkArgument(rowNumber >= -1);
        Preconditions.checkArgument(columnNumber >= -1);
        Preconditions.checkArgument(!valid || (rowNumber == OK_NUMBER && columnNumber == OK_NUMBER));
        
        this.valid = valid;
        this.errorRowNumber = rowNumber;
        this.errorColumnNumber = columnNumber;
        this.message = message;
        this.messageParameters = messageParameters;
    }
    
    @Override
    public boolean isValid() {
        return this.valid;
    }
    
    /**
     * @return the rowNumber
     */
    public int getErrorRowNumber() {
        return errorRowNumber;
    }

    /**
     * @return the columnNumber
     */
    public int getErrorColumnNumber() {
        return errorColumnNumber;
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.editor.checker.CheckResult#printMessage()
     */
    @Override
    public String printMessage() {
        return String.format(message, messageParameters);
    }
}