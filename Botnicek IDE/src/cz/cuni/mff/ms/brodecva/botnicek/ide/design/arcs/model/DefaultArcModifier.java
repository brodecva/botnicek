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
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import com.google.common.base.Preconditions;
import com.google.common.collect.ObjectArrays;

import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NamingAuthority;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public class DefaultArcModifier implements ArcModifier {
    
    private final static String ARC_FACTORY_METHOD_NAME = "create";
    
    public static DefaultArcModifier create(final NamingAuthority namingAuthority) {
        return new DefaultArcModifier(namingAuthority);
    }

    private final NamingAuthority namingAuthority;
    
    private DefaultArcModifier(final NamingAuthority namingAuthority) {
        Preconditions.checkNotNull(namingAuthority);
        
        this.namingAuthority = namingAuthority;
    }
    
    @Override
    public Arc change(final Arc arc, final String newName, final int priority, final Class<? extends Arc> type, final Object... arguments) {
        Preconditions.checkNotNull(arc);
        Preconditions.checkNotNull(newName);
        Preconditions.checkNotNull(type);
        Preconditions.checkNotNull(arguments);
        
        final Method factoryMethod;
        try {
            factoryMethod = findCreateMethod(type);
        } catch (final NoSuchMethodException | SecurityException e) {
            throw new IllegalArgumentException(e);
        }
        
        try {
            return (Arc) factoryMethod.invoke(null, ObjectArrays.concat(new Object[] { newName, priority }, arguments, Object.class));
        } catch (final IllegalAccessException
                | IllegalArgumentException | InvocationTargetException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * @param mappedClass
     * @throws NoSuchMethodException, SecurityException 
     */
    private Method findCreateMethod(final Class<? extends Arc> mappedClass) throws NoSuchMethodException, SecurityException {
        final Method[] methods = mappedClass.getMethods();
        for (final Method method : methods) {
            if (method.getName().equals(ARC_FACTORY_METHOD_NAME)) {
                return method;
            }
        }
        
        throw new NoSuchMethodException();
    }
}
