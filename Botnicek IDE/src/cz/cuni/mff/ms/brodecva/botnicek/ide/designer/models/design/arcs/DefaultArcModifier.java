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
package cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.arcs;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.NamingAuthority;

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
        
        final Class<? extends Arc> mappedClass = type;
        Preconditions.checkArgument(mappedClass != null);
        
        final List<Class<?>> argumentsClasses = new ArrayList<>();
        argumentsClasses.add(newName.getClass());
        argumentsClasses.add(Integer.TYPE);
        argumentsClasses.add(type.getClass()); 
        for (final Object object : arguments) {
            argumentsClasses.add(object.getClass());
        }
        
        final Method constructor;
        try {
            constructor = mappedClass.getMethod(ARC_FACTORY_METHOD_NAME, (Class<?>[]) argumentsClasses.toArray());
        } catch (final NoSuchMethodException | SecurityException e) {
            throw new IllegalArgumentException(e);
        }
        
        try {
            return (Arc) constructor.invoke(null, newName, priority, arguments);
        } catch (final IllegalAccessException
                | IllegalArgumentException | InvocationTargetException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
