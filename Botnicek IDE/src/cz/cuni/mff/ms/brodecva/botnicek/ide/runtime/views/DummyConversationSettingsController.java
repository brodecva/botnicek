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
package cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.views;

import cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.controllers.ConversationSettingsController;
import cz.cuni.mff.ms.brodecva.botnicek.library.api.ConversationConfiguration;

/**
 * Atrapa řadiče.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
final class DummyConversationSettingsController implements
        ConversationSettingsController {

    /**
     * Vytvoří atrapu.
     * 
     * @return atrapa
     */
    public static DummyConversationSettingsController create() {
        return new DummyConversationSettingsController();
    }

    private DummyConversationSettingsController() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.utils.mvc.Controller#addView(java
     * .lang.Object)
     */
    @Override
    public void addView(final ConversationSettingsView view) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.utils.mvc.Controller#fill(java.lang
     * .Object)
     */
    @Override
    public void fill(final ConversationSettingsView view) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.utils.mvc.Controller#removeView(
     * java.lang.Object)
     */
    @Override
    public void removeView(final ConversationSettingsView view) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.controllers.
     * ConversationSettingsController
     * #set(cz.cuni.mff.ms.brodecva.botnicek.library
     * .api.ConversationConfiguration)
     */
    @Override
    public void set(final ConversationConfiguration configuration) {
    }
}
