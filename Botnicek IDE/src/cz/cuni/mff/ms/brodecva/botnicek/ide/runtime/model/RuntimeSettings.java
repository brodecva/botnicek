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
package cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Paths;
import java.util.regex.Pattern;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import cz.cuni.mff.ms.brodecva.botnicek.library.api.AIMLBotConfiguration;
import cz.cuni.mff.ms.brodecva.botnicek.library.api.AIMLConversationConfiguration;
import cz.cuni.mff.ms.brodecva.botnicek.library.api.AIMLLanguageConfiguration;
import cz.cuni.mff.ms.brodecva.botnicek.library.api.BotConfiguration;
import cz.cuni.mff.ms.brodecva.botnicek.library.api.ConversationConfiguration;
import cz.cuni.mff.ms.brodecva.botnicek.library.api.LanguageConfiguration;
import cz.cuni.mff.ms.brodecva.botnicek.library.processor.set.DisplayStrategy;

/**
 * Sjednocená nastavení běhového prostředí.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public class RuntimeSettings implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final RuntimeSettings DEFAULT;

    static {
        final BotConfiguration defaultBotConfiguration =
                AIMLBotConfiguration.of("My bot", Paths.get("files"),
                        Paths.get("gossip/gossip.txt"),
                        ImmutableMap.<String, String> of(),
                        ImmutableList.<String> of(),
                        ImmutableList.<String> of());
        final LanguageConfiguration defaultLanguageConfiguration =
                AIMLLanguageConfiguration.of("Language",
                        Pattern.compile("[\\.!;\\?]\\w*"),
                        ImmutableMap.<Pattern, String> of(),
                        ImmutableMap.<Pattern, String> of(),
                        ImmutableMap.<Pattern, String> of(),
                        ImmutableMap.<Pattern, String> of(),
                        ImmutableMap.<Pattern, String> of(),
                        ImmutableMap.<Pattern, String> of(),
                        ImmutableMap.<Pattern, String> of());
        final ConversationConfiguration defaultConversationConfiguration =
                AIMLConversationConfiguration.of(
                        ImmutableMap.<String, String> of(),
                        ImmutableMap.<String, DisplayStrategy> of());

        DEFAULT =
                create(defaultBotConfiguration, defaultLanguageConfiguration,
                        defaultConversationConfiguration);
    }

    /**
     * Vytvoří nastavení běhového prostředí sjednocením dílčích.
     * 
     * @param botConfiguration
     *            nastavení robota
     * @param languageConfiguration
     *            nastavení jazyka
     * @param conversationConfiguration
     *            nastavení konverzace
     * @return nastavení běhového prostředí
     */
    public static RuntimeSettings create(
            final BotConfiguration botConfiguration,
            final LanguageConfiguration languageConfiguration,
            final ConversationConfiguration conversationConfiguration) {
        return new RuntimeSettings(botConfiguration, languageConfiguration,
                conversationConfiguration);
    }

    /**
     * Vrátí výchozí nastavení.
     * 
     * @return výchozí nastavení
     */
    public static RuntimeSettings getDefault() {
        return DEFAULT;
    }

    private final BotConfiguration botConfiguration;

    private final LanguageConfiguration languageConfiguration;

    private final ConversationConfiguration conversationConfiguration;

    private RuntimeSettings(final BotConfiguration botConfiguration,
            final LanguageConfiguration languageConfiguration,
            final ConversationConfiguration conversationConfiguration) {
        Preconditions.checkNotNull(botConfiguration);
        Preconditions.checkNotNull(languageConfiguration);
        Preconditions.checkNotNull(conversationConfiguration);

        this.botConfiguration = botConfiguration;
        this.languageConfiguration = languageConfiguration;
        this.conversationConfiguration = conversationConfiguration;
    }

    /**
     * Vrátí nastavení robota.
     * 
     * @return nastavení robota
     */
    public BotConfiguration getBotConfiguration() {
        return this.botConfiguration;
    }

    /**
     * Vrátí nastavení konverzace.
     * 
     * @return nastavení konverzace
     */
    public ConversationConfiguration getConversationConfiguration() {
        return this.conversationConfiguration;
    }

    /**
     * Vrátí nastavení jazyka.
     * 
     * @return nastavení jazyka
     */
    public LanguageConfiguration getLanguageConfiguration() {
        return this.languageConfiguration;
    }

    private void readObject(final ObjectInputStream objectInputStream)
            throws ClassNotFoundException, IOException {
        objectInputStream.defaultReadObject();

        Preconditions.checkNotNull(this.botConfiguration);
        Preconditions.checkNotNull(this.languageConfiguration);
        Preconditions.checkNotNull(this.conversationConfiguration);
    }

    private void writeObject(final ObjectOutputStream objectOutputStream)
            throws IOException {
        objectOutputStream.defaultWriteObject();
    }
}