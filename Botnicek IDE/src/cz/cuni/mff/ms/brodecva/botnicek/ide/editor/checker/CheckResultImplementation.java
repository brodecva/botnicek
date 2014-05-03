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
package cz.cuni.mff.ms.brodecva.botnicek.ide.editor.checker;

import com.google.common.base.Preconditions;


/**
 * @author Václav Brodec
 * @version 1.0
 */
public final class CheckResultImplementation implements
        CheckResult {
    
    final private boolean valid;
    final private int firstBadPosition;
    final String message;
    private final Object[] messageParameters;

    public static CheckResultImplementation create(final boolean valid, final int firstBadPosition, final String message, final Object... messageParameters) {
        return new CheckResultImplementation(valid, firstBadPosition, message, messageParameters);
    }
    
    public static CheckResultImplementation succeed() {
        return new CheckResultImplementation(true, Integer.MAX_VALUE, "OK");
    }
    
    public static CheckResultImplementation fail(final int firstBadPosition, final String message, final Object... messageParameters) {
        return new CheckResultImplementation(false, firstBadPosition, message, messageParameters);
    }
    
    private CheckResultImplementation(final boolean valid, final int firstBadPosition, final String message, final Object... messageParameters) {
        Preconditions.checkNotNull(message);
        Preconditions.checkNotNull(messageParameters);
        Preconditions.checkArgument(firstBadPosition >= 0);
        Preconditions.checkArgument(!valid || firstBadPosition == Integer.MAX_VALUE);
        
        this.valid = valid;
        this.firstBadPosition = firstBadPosition;
        this.message = message;
        this.messageParameters = messageParameters;
    }
    
    @Override
    public boolean isValid() {
        return this.valid;
    }

    @Override
    public int firstBadPosition() {
        return this.firstBadPosition;
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.editor.checker.CheckResult#printMessage()
     */
    @Override
    public String printMessage() {
        return String.format(message, messageParameters);
    }
}