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
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model;

import java.util.Map;
import java.util.Set;

/**
 * <p>Autorita, která přiděluje nekonfliktní názvy.</p>
 * <p>Z implementačních důvodů je nutné rezervovat některé názvy, autorita umožňuje uživateli užívat vlastní názvy tak, aby bylo zajištěno, že se nebudou shodovat s již použitými.</p>
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public interface NamingAuthority {
    /**
     * Vygeneruje unikátní řetězec v rámci autority.
     * 
     * @return unikátní řetězec v rámci autority
     */
    String generate();
    
    /**
     * Testuje, zda-li je název nekonfliktní a ve shodu s pravidly pojmenování.
     * 
     * @param name navržený název
     * @return zda-li je název nekonfliktní a ve shodu s pravidly pojmenování
     */
    boolean isUsable(String name);
    
    /**
     * Zkusí užít název. V případě, že je konfliktní či nevhodný, autorita se pro něj bude snažit vygenerovat podobný, ten užije a vrátí uživateli zpět.
     * 
     * @param name název
     * @return případě pozměněný užitý název
     */
    String use(String name);
    
    /**
     * Odstraní původní název a použije místo něj nový.
     * 
     * @param oldName původní užitý název
     * @param newName náhradní název
     * @return případě pozměněný užitý název
     */
    String replace(String oldName, String newName);
    
    /**
     * Pokusí se užít sérii názvu, v případě selhání nedojde k aktualizaci autority.
     * 
     * @param names názvy
     * @throws IllegalArgumentException pokud je některý z názvu nepoužitelný v původní podobě
     */
    void tryUse(String... names);
    
    /**
     * Pokusí se nahradit sérii názvů již přítomných, v případě selhání nedojde k aktualizaci autority.
     * 
     * @param oldToNew nahrazující názvy
     * @throws IllegalArgumentException pokud se původní nevyskytuje nebo je některý z názvu nepoužitelný v původní podobě
     */
    void tryReplace(Map<String, String> oldToNew);
    
    /**
     * Odebere název z autority.
     * 
     * @param oldName název
     */
    void release(String oldName);
    
    /**
     * Indikuje, zda-li je název již obsazen.
     * 
     * @param name název
     * @return zda-li je název již obsazen
     */
    boolean isUsed(String name);
    
    /**
     * Vrátí registrované názvy.
     * 
     * @return registrované názvy
     */
    Set<String> getSnapshot();
}
