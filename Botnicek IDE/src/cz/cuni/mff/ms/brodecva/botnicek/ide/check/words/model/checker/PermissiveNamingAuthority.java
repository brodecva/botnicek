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
package cz.cuni.mff.ms.brodecva.botnicek.ide.check.words.model.checker;

import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NamingAuthority;

/**
 * Vše povolující autorita pro přidělování jmen.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
final class PermissiveNamingAuthority implements
        NamingAuthority {
    @Override
    public String use(String name) {
        return name;
    }

    @Override
    public void tryUse(String... name) {
    }

    @Override
    public String replace(String oldName, String newName) {
        return newName;
    }

    @Override
    public void release(String oldName) {
    }

    @Override
    public boolean isUsed(String name) {
        return false;
    }

    @Override
    public boolean isUsable(String name) {
        return true;
    }

    @Override
    public String generate() {
        return "";
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NamingAuthority#tryReplace(java.util.Map)
     */
    @Override
    public void tryReplace(Map<String, String> oldToNew) {
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NamingAuthority#getSnapshot()
     */
    @Override
    public Set<String> getSnapshot() {
        return ImmutableSet.of();
    }
}