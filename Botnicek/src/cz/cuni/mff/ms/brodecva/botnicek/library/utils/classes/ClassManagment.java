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
package cz.cuni.mff.ms.brodecva.botnicek.library.utils.classes;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.ExceptionMessageLocalizer;

/**
 * Správa tříd a vytváření instancí.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class ClassManagment {
    /**
     * Lokalizátor hlášek výjimek.
     */
    private static final ExceptionMessageLocalizer MESSAGE_LOCALIZER =
            ExceptionMessageLocalizer.getLocalizer();

    /**
     * Skrytý konstruktor.
     */
    private ClassManagment() {
    }

    /**
     * Vytvoří instanci dané třídy za použití výchozího konstruktoru.
     * 
     * @param klass
     *            definice třídy, nesmí být null
     * @return nová instance
     * @param <T>
     *            typ dané třídy
     */
    public static <T> T getNewInstance(final Class<T> klass) {
        return getNewInstance(klass, null);
    }

    /**
     * Vytvoří instanci dané třídy za použití konstruktoru s parametry jednoho
     * typu.
     * 
     * @param klass
     *            definice třídy, nesmí být null
     * @param argumentType
     *            typ argumentů
     * @param constructorArgs
     *            parametry konstruktoru, nesmí být null
     * @return nová instance
     * @param <T>
     *            typ dané třídy
     * @param <U>
     *            typ argumentů
     */
    @SafeVarargs
    public static <T, U> T getNewInstance(final Class<T> klass,
            final Class<U> argumentType, final U... constructorArgs) {
        if (argumentType == null && constructorArgs.length > 0) {
            throw new NullPointerException(
                    MESSAGE_LOCALIZER.getMessage("utils.classes.NullArgument"));
        }

        final Class<?>[] argTypes = new Class<?>[constructorArgs.length];
        Arrays.fill(argTypes, argumentType);

        final Constructor<T> constructor;
        try {
            constructor = klass.getConstructor(argTypes);
        } catch (final NoSuchMethodException e) {
            throw new ClassControlError(MESSAGE_LOCALIZER.getMessage(
                    "utils.classes.InvalidConstructor", klass, argTypes), e);
        } catch (final SecurityException e) {
            throw new ClassControlError(MESSAGE_LOCALIZER.getMessage(
                    "utils.classes.ConstructorSecurty", klass, argTypes), e);
        }

        try {
            return constructor.newInstance(constructorArgs);
        } catch (final IllegalAccessException e) {
            throw new ClassControlError(MESSAGE_LOCALIZER.getMessage(
                    "utils.classes.InaccessibleConstructor", klass, constructor), e);
        } catch (final InstantiationException e) {
            throw new ClassControlError(MESSAGE_LOCALIZER.getMessage(
                    "utils.classes.InstantiationNotPossible", klass, constructor), e);
        } catch (final IllegalArgumentException e) {
            throw new ClassControlError(MESSAGE_LOCALIZER.getMessage(
                    "utils.classes.IllegalConstructorArguments", klass, constructor,
                    constructorArgs), e);
        } catch (final InvocationTargetException e) {
            throw new ClassControlError(MESSAGE_LOCALIZER.getMessage(
                    "utils.classes.ConstructorThrewExeption", klass, constructor,
                    constructorArgs), e.getTargetException());
        }
    }
}
