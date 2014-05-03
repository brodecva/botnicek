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
package cz.cuni.mff.ms.brodecva.botnicek.ide.utils.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public class CopyConstruction {
    public static <T, R> Constructor<T> resolveCopyConstructor(final Class<T> klass, final R parameter) {
        return resolveCopyConstructor(klass, parameter.getClass());
    }
    
    public static <T, R> Constructor<T> resolveCopyConstructor(final Class<T> klass, final Class<R> parameterType) {
        Preconditions.checkNotNull(klass);
        Preconditions.checkNotNull(parameterType);
        
        final Constructor<?>[] constructors = klass.getConstructors();
        final Collection<Constructor<?>> copyConstructors = Collections2.filter(Arrays.asList(constructors), new Predicate<Constructor<?>>() {

            @Override
            public boolean apply(final Constructor<?> input) {
                return input.getParameterTypes().length == 1;
            }
            
        });
        
        Class<? super R> relaxedType = parameterType;
        while (relaxedType != null) {
            for (final Constructor<?> copyConstructor : copyConstructors) {
                final Class<?>[] types = copyConstructor.getParameterTypes();
                assert types.length == 1;
                
                final Class<?> acceptedType = types[0];
                if (acceptedType.equals(relaxedType)) {
                    @SuppressWarnings("unchecked")
                    final Constructor<T> result = (Constructor<T>) copyConstructor;
                    return result;
                }
            }
            
            relaxedType = relaxedType.getSuperclass();
        }
        
        throw new IllegalArgumentException();
    }
    
    public static <T, R> T copy(final Class<T> to, final R from) throws InvocationTargetException {
        try {
            return resolveCopyConstructor(to, from).newInstance(from);
        } catch (final InstantiationException | IllegalAccessException e) {
            throw new AssertionError();
        }
    }
}
