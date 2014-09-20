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
package cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.views.utils;

import java.util.Map;

import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableBiMap;

import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.Presence;
import cz.cuni.mff.ms.brodecva.botnicek.library.processor.set.DisplayStrategy;
import cz.cuni.mff.ms.brodecva.botnicek.library.processor.set.NameDisplayStretegy;
import cz.cuni.mff.ms.brodecva.botnicek.library.processor.set.ValueDisplayStrategy;

/**
 * Výchozí továrna na strategie. Bez další konfigurace podporuje strategie pro zobrazení názvu predikátu či právě nastavené hodnoty.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class DefaultDisplayStrategyFactory implements DisplayStrategyFactory {
    
    private static final BiMap<String, DisplayStrategy> DEFAULTS = ImmutableBiMap.<String, DisplayStrategy>of("name", new NameDisplayStretegy(), "value", new ValueDisplayStrategy()); 
    
    private final BiMap<String, DisplayStrategy> supported;
    
    /**
     * Vytvoří továrnu.
     * 
     * @return továrna
     */
    public static DisplayStrategyFactory create() {
        return new DefaultDisplayStrategyFactory(DEFAULTS);
    }
    
    /**
     * Vytvoří továrna dle zadání.
     * 
     * @param supported vzájemně jednoznačné podporované popisy a strategie
     * @return továrna
     */
    public static DisplayStrategyFactory create(final Map<String, DisplayStrategy> supported) {
        return new DefaultDisplayStrategyFactory(HashBiMap.create(supported));
    }
    
    private DefaultDisplayStrategyFactory(final BiMap<String, DisplayStrategy> supported) {
        this.supported = ImmutableBiMap.copyOf(supported);
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.views.utils.DisplayStrategyFactory#provide(java.lang.String)
     */
    @Override
    public DisplayStrategy provide(final String description) {
        Preconditions.checkNotNull(description);
        
        final DisplayStrategy result = this.supported.get(description);
        Preconditions.checkArgument(Presence.isPresent(result));
        
        return result;
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.views.utils.DisplayStrategyFactory#getSupported()
     */
    @Override
    public Map<String, DisplayStrategy> getSupported() {
        return this.supported;
    }
}