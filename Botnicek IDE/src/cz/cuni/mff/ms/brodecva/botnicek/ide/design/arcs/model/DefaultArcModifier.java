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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.types.Priority;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Intended;

/**
 * <p>
 * Výchozí implementace modifikátoru hran využívá reflexi ke konstrukci hrany
 * nové.
 * </p>
 * <p>
 * U podporovaných hran se očekává existence statické tovární metody s názvem
 * {@value #ARC_FACTORY_METHOD_NAME}, jejímiž parametry jsou rodičovská síť,
 * název hrany, priorita a argumenty pro specifický typ.
 * </p>
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public class DefaultArcModifier implements ArcModifier, Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Název tovární metody.
     */
    public final static String ARC_FACTORY_METHOD_NAME = "create";

    /**
     * Vytvoří modifikátor.
     * 
     * @return modifikátor
     */
    public static DefaultArcModifier create() {
        return new DefaultArcModifier();
    }

    private DefaultArcModifier() {
    }

    private Object[] composeAllArguments(final Arc arc,
            final NormalWord newName, final Priority priority,
            final ImmutableList<Object> argumentsCopy) {
        final ImmutableList.Builder<Object> allArgumentsBuilder =
                ImmutableList.builder();
        allArgumentsBuilder.add(arc.getNetwork());
        allArgumentsBuilder.add(newName);
        allArgumentsBuilder.add(priority);
        allArgumentsBuilder.addAll(argumentsCopy);

        return allArgumentsBuilder.build().toArray();
    }

    private Method findCreateMethod(final Class<? extends Arc> mappedClass)
            throws NoSuchMethodException, SecurityException {
        final Method[] methods = mappedClass.getMethods();
        for (final Method method : methods) {
            if (method.getName().equals(ARC_FACTORY_METHOD_NAME)) {
                return method;
            }
        }

        throw new NoSuchMethodException();
    }

    private Method getFactoryMethod(final Class<? extends Arc> type) {
        final Method factoryMethod;
        try {
            factoryMethod = findCreateMethod(type);
        } catch (final NoSuchMethodException | SecurityException e) {
            throw new IllegalArgumentException(e);
        }

        return factoryMethod;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.ArcModifier#change
     * (cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc,
     * cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord,
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.types.Priority,
     * java.lang.Class, java.lang.Object[])
     */
    @Override
    public Arc change(final Arc arc, final NormalWord newName,
            final Priority priority, final Class<? extends Arc> type,
            final Object... arguments) {
        Preconditions.checkNotNull(arc);
        Preconditions.checkNotNull(newName);
        Preconditions.checkNotNull(priority);
        Preconditions.checkNotNull(type);
        Preconditions.checkNotNull(arguments);

        final Method factoryMethod = getFactoryMethod(type);
        final Object[] allArguments =
                composeAllArguments(arc, newName, priority,
                        ImmutableList.copyOf(arguments));

        try {
            return (Arc) factoryMethod.invoke(Intended.nullReference(),
                    allArguments);
        } catch (final IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private void readObject(final ObjectInputStream objectInputStream)
            throws ClassNotFoundException, IOException {
        objectInputStream.defaultReadObject();
    }

    private void writeObject(final ObjectOutputStream objectOutputStream)
            throws IOException {
        objectOutputStream.defaultWriteObject();
    }
}
