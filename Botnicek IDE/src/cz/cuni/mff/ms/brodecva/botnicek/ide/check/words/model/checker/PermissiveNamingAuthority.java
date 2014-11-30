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
final class PermissiveNamingAuthority implements NamingAuthority {
    @Override
    public String generate() {
        return "";
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NamingAuthority
     * #getSnapshot()
     */
    @Override
    public Set<String> getSnapshot() {
        return ImmutableSet.of();
    }

    @Override
    public boolean isUsable(final String name) {
        return true;
    }

    @Override
    public boolean isUsed(final String name) {
        return false;
    }

    @Override
    public void release(final String oldName) {
    }

    @Override
    public String replace(final String oldName, final String newName) {
        return newName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NamingAuthority
     * #tryReplace(java.util.Map)
     */
    @Override
    public void tryReplace(final Map<? extends String, ? extends String> oldToNew) {
    }

    @Override
    public void tryUse(final String... name) {
    }

    @Override
    public String use(final String name) {
        return name;
    }
}