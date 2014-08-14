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
package cz.cuni.mff.ms.brodecva.botnicek.ide.project.model;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.root.Aiml;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.root.Toplevel;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.toplevel.Topic;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWords;
import cz.cuni.mff.ms.brodecva.botnicek.ide.compile.Compiler;
import cz.cuni.mff.ms.brodecva.botnicek.ide.compile.CompilerFactory;
import cz.cuni.mff.ms.brodecva.botnicek.ide.compile.DefaultCompilerFactory;
import cz.cuni.mff.ms.brodecva.botnicek.ide.compile.library.Randomize;
import cz.cuni.mff.ms.brodecva.botnicek.ide.compile.library.Recursion;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NamingAuthority;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NormalizedNamingAuthority;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.DefaultSystem;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.types.Priority;
import cz.cuni.mff.ms.brodecva.botnicek.ide.print.DefaultPrettyPrinter;
import cz.cuni.mff.ms.brodecva.botnicek.ide.print.PrintException;
import cz.cuni.mff.ms.brodecva.botnicek.ide.print.Printer;
import cz.cuni.mff.ms.brodecva.botnicek.ide.project.events.BotSettingsChangedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.project.events.ConversationSettingsChangedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.project.events.LanguageSettingsChangedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.project.events.ProjectOpenedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.project.events.SettingsChangedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.render.DefaultRendererFactory;
import cz.cuni.mff.ms.brodecva.botnicek.ide.render.DefaultRenderingVisitorFactory;
import cz.cuni.mff.ms.brodecva.botnicek.ide.render.Renderer;
import cz.cuni.mff.ms.brodecva.botnicek.ide.render.RendererFactory;
import cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.events.RunsTerminatedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.events.RuntimeRunEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.model.DefaultRuntimeFactory;
import cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.model.RunException;
import cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.model.Runtime;
import cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.model.RuntimeFactory;
import cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.model.RuntimeSettings;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.Dispatcher;
import cz.cuni.mff.ms.brodecva.botnicek.library.api.BotConfiguration;
import cz.cuni.mff.ms.brodecva.botnicek.library.api.ConversationConfiguration;
import cz.cuni.mff.ms.brodecva.botnicek.library.api.LanguageConfiguration;
import cz.cuni.mff.ms.brodecva.botnicek.library.api.SessionException;
import cz.cuni.mff.ms.brodecva.botnicek.library.loader.LoaderException;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.AIML;
import cz.cuni.mff.ms.brodecva.botnicek.library.preprocessor.SimpleNormalizer;

/**
 * <p>Instance projektové třídy slouží k provádění základních operací, jako je otevření, uložení, export, konfigurace a provedení testu.</p>
 * <p>Je společně s potřebnými součástmi serializovatelná, a lze tedy užít k uložení a pozdějšímu načtení aktuálního stavu práce.</p>
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public class Project implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Rezervovaný název exportního souboru knihovny.
     */
    public static final String RESERVED_LIBRARY_NAME = "botnicek";
    
    private final Dispatcher dispatcher;
    private final NamingAuthority statesNamingAuthority;
    private final NamingAuthority predicatesNamingAuthority;
    private final RendererFactory rendererFactory;
    private final CompilerFactory compilerFactory;
    private final RuntimeFactory runtimeFactory;
    private final Printer printer;
    private final System system;
    
    private Settings settings;
    private RuntimeSettings runtimeSettings;    

    /**
     * Vytvoří projekt z daných závislostí.
     * 
     * @param system systém sítí
     * @param statesNamingAuthority autorita, která řídí přidělování jmen stavům sítí
     * @param predicatesNamingAuthority autorita, která řídí přidělování jmen testovacím predikátům
     * @param dispatcher rozesílač událostí
     * @param settings obecná nastavení projektu
     * @param runtimeSettings nastavení běhového prostředí
     * @param rendererFactory továrna na formátování zdrojového kódu
     * @param printer formátovač kódu
     * @param compilerFactory  továrna na překlad systému sítí do jazyka AIML
     * @param runtimeFactory továrna na běhové prostředí botů
     * @return projekt
     */
    static Project create(final System system, final NamingAuthority statesNamingAuthority, final NamingAuthority predicatesNamingAuthority, final Dispatcher dispatcher, final Settings settings, final RuntimeSettings runtimeSettings, final RendererFactory rendererFactory, final Printer printer, final CompilerFactory compilerFactory, final RuntimeFactory runtimeFactory) {
        final Project newInstance = new Project(system, statesNamingAuthority, predicatesNamingAuthority, dispatcher, settings, runtimeSettings, rendererFactory, printer, compilerFactory, runtimeFactory);
        
        dispatcher.fire(ProjectOpenedEvent.create(newInstance));
        
        return newInstance;
    }
    
    /**
     * Vytvoří výchozí projekt.
     * 
     * @param name název projektu
     * @param dispatcher vysílač událostí
     * @return výchozí projekt
     */
    public static Project create(final String name, final Dispatcher dispatcher) {        
        Preconditions.checkNotNull(name);
        Preconditions.checkNotNull(dispatcher);
        Preconditions.checkArgument(!name.isEmpty());
        
        final NamingAuthority statesNamingAuthority = NormalizedNamingAuthority.create(new SimpleNormalizer());
        final NamingAuthority predicatesNamingAuthority = NormalizedNamingAuthority.create(new SimpleNormalizer());
        
        final System system = DefaultSystem.create(name, dispatcher, statesNamingAuthority, predicatesNamingAuthority, ImmutableSet.of(RESERVED_LIBRARY_NAME));
        
        final Settings settings = Settings.getDefault();
        final RuntimeSettings runtimeSettings = RuntimeSettings.getDefault();
        
        final RendererFactory rendererFactory = DefaultRendererFactory.create(DefaultRenderingVisitorFactory.create());
        final Printer printer = DefaultPrettyPrinter.create();
        final CompilerFactory compilerFactory = DefaultCompilerFactory.create();
        final RuntimeFactory runtimeFactory = DefaultRuntimeFactory.create();        
        
        statesNamingAuthority.use(NormalWords.join(settings.getPrefix(), settings.getPullState()).getText());
        statesNamingAuthority.use(NormalWords.join(settings.getPrefix(), settings.getPullStopState()).getText());
        statesNamingAuthority.use(NormalWords.join(settings.getPrefix(), settings.getRandomizeState()).getText());
        statesNamingAuthority.use(NormalWords.join(settings.getPrefix(), settings.getReturnState()).getText());
        statesNamingAuthority.use(NormalWords.join(settings.getPrefix(), settings.getSuccessState()).getText());
        statesNamingAuthority.use(NormalWords.join(settings.getPrefix(), settings.getFailState()).getText());
        
        predicatesNamingAuthority.use(NormalWords.join(settings.getPrefix(), settings.getTestingPredicate()).getText());
        
        return create(system, statesNamingAuthority, predicatesNamingAuthority, dispatcher, settings, runtimeSettings, rendererFactory, printer, compilerFactory, runtimeFactory);
    }
    
    /**
     * Načte projekt z umístění.
     * 
     * @param projectPath cesta k souboru s projektem
     * @return načtený projekt
     * @throws IOException pokud dojde k chybě při otevírání či načítání
     * @throws ClassNotFoundException pokud je otevřená definice projektu nekompatibilní
     */
    public static Project open(final Path projectPath) throws IOException, ClassNotFoundException {
        Preconditions.checkNotNull(projectPath.toFile());
        
        try (final InputStream fileInput = new FileInputStream(projectPath.toFile())) {
            return open(fileInput);
        } catch (final FileNotFoundException e) {
            throw e;
        }
    }
    
    /**
     * Načte projekt ze vstupního proudu.
     * 
     * @param inputStream vstupní proud
     * @return načtený projekt
     * @throws IOException pokud dojde k chybě při otevírání či načítání
     * @throws ClassNotFoundException pokud je otevřená definice projektu nekompatibilní
     */
    public static Project open(final InputStream inputStream) throws IOException, ClassNotFoundException {
        Preconditions.checkNotNull(inputStream);
        
        try (final InputStream inputBuffer = new BufferedInputStream(inputStream);
            final ObjectInput objectInput = new ObjectInputStream(inputBuffer)) {
            
            final Project loaded = (Project) objectInput.readObject();
            loaded.dispatcher.fire(ProjectOpenedEvent.create(loaded));
            return loaded;
        }
    }

    private Project(final System system, final NamingAuthority statesNamingAuthority, final NamingAuthority predicatesNamingAuthority, final Dispatcher dispatcher, final Settings settings, final RuntimeSettings runtimeSettings, final RendererFactory rendererFactory, final Printer printer, final CompilerFactory compilerFactory, final RuntimeFactory runtimeFactory) {
        Preconditions.checkNotNull(system);
        Preconditions.checkNotNull(statesNamingAuthority);
        Preconditions.checkNotNull(predicatesNamingAuthority);
        Preconditions.checkNotNull(dispatcher);
        Preconditions.checkNotNull(settings);
        Preconditions.checkNotNull(runtimeSettings);
        Preconditions.checkNotNull(rendererFactory);
        Preconditions.checkNotNull(compilerFactory);
        Preconditions.checkNotNull(runtimeFactory);
        Preconditions.checkNotNull(printer);
        
        Preconditions.checkArgument(statesNamingAuthority.isUsed(NormalWords.join(settings.getPrefix(), settings.getPullState()).getText()));
        Preconditions.checkArgument(statesNamingAuthority.isUsed(NormalWords.join(settings.getPrefix(), settings.getPullStopState()).getText()));
        Preconditions.checkArgument(statesNamingAuthority.isUsed(NormalWords.join(settings.getPrefix(), settings.getRandomizeState()).getText()));
        Preconditions.checkArgument(statesNamingAuthority.isUsed(NormalWords.join(settings.getPrefix(), settings.getReturnState()).getText()));
        Preconditions.checkArgument(statesNamingAuthority.isUsed(NormalWords.join(settings.getPrefix(), settings.getSuccessState()).getText()));
        Preconditions.checkArgument(statesNamingAuthority.isUsed(NormalWords.join(settings.getPrefix(), settings.getFailState()).getText()));
        
        Preconditions.checkArgument(predicatesNamingAuthority.isUsed(NormalWords.join(settings.getPrefix(), settings.getTestingPredicate()).getText()));

        this.system = system;
        this.statesNamingAuthority = statesNamingAuthority;
        this.predicatesNamingAuthority = predicatesNamingAuthority;
        this.dispatcher = dispatcher;
        this.settings = settings;
        this.runtimeSettings = runtimeSettings;
        this.compilerFactory = compilerFactory;
        this.rendererFactory = rendererFactory;
        this.runtimeFactory = runtimeFactory;
        this.printer = printer;
    }

    /**
     * Uloží projekt jako soubor do zadaného umístění.
     * 
     * @param projectPath cesta pro uložení
     * @throws IOException pokud dojde k chybě při ukládání
     */
    public void save(final Path projectPath) throws IOException {
        Preconditions.checkNotNull(projectPath);
        
        try (final FileOutputStream fileOutput =
                new FileOutputStream(projectPath.toFile());
                final OutputStream outputBuffer =
                        new BufferedOutputStream(fileOutput)) {
            save(outputBuffer);
        } catch (final FileNotFoundException e) {
            throw e;
        }
    }
    
    /**
     * Uloží projekt do výstupního proudu.
     * 
     * @param outputStream výstupní proud
     * @throws IOException pokud dojde k chybě při ukládání
     */
    public void save(final OutputStream outputStream) throws IOException {
        Preconditions.checkNotNull(outputStream);
        
        try (final ObjectOutputStream objectOutput =
                        new ObjectOutputStream(outputStream)) {
            objectOutput.writeObject(this);
        } catch (final IOException e) {
            throw e;
        }
    }

    /**
     * Vyexportuje zdrojové kódy bota vyvíjeného v rámci projektu.
     * 
     * @param directory adresář pro export
     * @throws IOException pokud dojde k chybě při exportu
     */
    public void export(final Path directory) throws IOException {
        final Map<Network, List<Topic>> result = compile();
        
        final Renderer render = this.rendererFactory.produce(this.settings.getNamespacesToPrefixes());
        final Set<Entry<Network, List<Topic>>> units = result.entrySet();
        for (final Entry<Network, List<Topic>> unit : units) {
            final Network network = unit.getKey();
            final List<Topic> content = unit.getValue();
            
            exportUnit(network.getName(), content, render, directory);
        }
        
        exportUnit(RESERVED_LIBRARY_NAME, getLibraries(), render, directory);
    }

    private List<Topic> getLibraries() {
        final ImmutableList.Builder<Topic> builder = ImmutableList.builder();
        
        builder.addAll(Recursion.getLibrary(joinPullState(), joinPullStopState(), joinSuccessState(), joinFailState(), joinReturnState()));
        builder.addAll(Randomize.getLibrary(joinRandomizeState(), Priority.MAX_VALUE, this.system.getMaxBranchingFactor(), this.statesNamingAuthority));
        
        return builder.build();
    }

    /**
     * Převede systém sítí na strom objektového modelu jazyka AIML pomocí kompilátoru z dodané továrny.
     * 
     * @return sítě a k nim příslušející témata jazyka AIML
     */
    private Map<Network, List<Topic>> compile() {
        final Compiler compiler = this.compilerFactory.produce(joinPullState(), joinPullStopState(), joinRandomizeState(), joinSuccessState(), joinReturnState(), joinTestingPredicate());

        final Map<Network, List<Topic>> result = compiler.compile(this.system);
        return result;
    }

    /**
     * Vygeneruje kód z obsahu a uloží pod daným popisem jako soubor do adresáře.
     * 
     * @param description popis souboru
     * @param content zdroj obsahu souboru
     * @param render generátor kódu ze zdroje
     * @param directory umístění souboru
     * @throws IOException pokud dojde k chybě při zápisu
     */
    void exportUnit(final String description, final List<Topic> content,
            final Renderer render, final Path directory) throws IOException {
        final String text = render(render, content);
        final String formatted = format(text);
        
        final String fileName =
                String.format("%1s$.%2s$", description, AIML.FILE_SUFFIX);
        
        writeUnit(directory, fileName, formatted);
    }

    /**
     * Zformátuje dodaný kód do lidsky čitelné podoby pomocí dodaného formátovače.
     * 
     * @param code zdrojový kód
     * @return naformátovaný kód
     * @throws IOException pokud dojde k chybě při pokusu o převod do zformátovaného tvaru
     */
    private String format(final String code) throws IOException {
        final String formatted;
        try {
            formatted = this.printer.print(code);
        } catch (final PrintException e) {
            throw new IOException(e);
        }
        return formatted;
    }

    /**
     * Zapíše do adresáře soubor daného jména s dodaným obsahem.
     * 
     * @param directory adresář
     * @param fileName jméno souboru
     * @param text obsah
     * @throws IOException pokud dojde k chybě při zápisu
     */
    void writeUnit(final Path directory, final String fileName,
            final String text) throws IOException {
        final Path filePath = directory.resolve(fileName);
        try (final FileWriter fileOutput = new FileWriter(filePath.toFile());
                final BufferedWriter outputBuffer =
                        new BufferedWriter(fileOutput)) {
            outputBuffer.write(text);
        } catch (final IOException e) {
            throw e;
        }
    }
    
    /**
     * Spustí test.
     * 
     * @throws RunException pokud dojde k chybě při inicializaci běhového prostředí
     */
    public void test() throws RunException {
        final Map<Network, List<Topic>> result = compile();
        final Runtime runtime = startRuntime();
        
        loadToRuntime(runtime, result);
        
        this.dispatcher.fire(RunsTerminatedEvent.create());
        this.dispatcher.fire(RuntimeRunEvent.create(this, runtime.run()));
    }

    /**
     * Načte příslušná témata sítí z objektového modelu a knihoven do běhového prostředí.
     * 
     * @param runtime běhové prostředí
     * @param metastructure sítě a jejich témata, která budou zpracována generátorem kódu
     * @throws RunException pokud dojde k chybě při inicializaci
     */
    private void loadToRuntime(final Runtime runtime,
            final Map<Network, List<Topic>> metastructure) throws RunException {
        final Renderer renderer = this.rendererFactory.produce(this.settings.getNamespacesToPrefixes());
        
        try {
            for (final Entry<Network, List<Topic>> unit : metastructure.entrySet()) {
                final Network network = unit.getKey(); 
                final List<? extends Toplevel> content = unit.getValue();
    
                loadUnit(network.getName(), content, renderer, runtime);
            }
            loadUnit(RESERVED_LIBRARY_NAME, getLibraries(), renderer, runtime);
        } catch (final LoaderException e) {
            throw new RunException(e);
        }
    }

    /**
     * Vytvoří běhové prostředí pomocí dodané továrny a z nastavení dodaných projektu.
     * 
     * @return běhové prostředí
     * @throws RunException pokud dojde k chybě při inicializaci
     */
    private Runtime startRuntime() throws RunException {
        final Runtime runtime;
        try {
            runtime = this.runtimeFactory.produce(this.runtimeSettings, this.dispatcher);
        } catch (final SessionException e) {
            throw new RunException(e);
        }
        return runtime;
    }

    /**
     * Vygeneruje a načte kód obsah do běhového prostředí.
     * 
     * @param description formální popis jednotky
     * @param content zdroj dat ke generování kódu
     * @param renderer generátor kódu
     * @param runtime plněné běhové prostředí
     * @throws LoaderException pokud dojde k chybě při načítání kódu
     */
    private void loadUnit(final String description,
            final List<? extends Toplevel> content, final Renderer renderer,
            final Runtime runtime) throws LoaderException {
        final String text = render(renderer, content);
        
        runtime.load(description, text);
    }

    /**
     * Vygeneruje kód ze zdroje s pomocí dodaného generátoru.
     * 
     * @param renderer generátor kódu
     * @param content zdroj
     * @return vygenerovaný kód
     */
    private String render(final Renderer renderer,
            final List<? extends Toplevel> content) {
        final String text = renderer.render(Aiml.create(content, this.settings.getNamespacesToPrefixes()));
        return text;
    }

    /**
     * Vrátí nastavení projektu.
     * 
     * @return nastavení projektu
     */
    public Settings getSettings() {
        return this.settings;
    }
    
    /**
     * Vrátí editovaný systém sítí.
     * 
     * @return systém sítí
     */
    public System getSystem() {
        return this.system;
    }

    /**
     * Nastaví projekt.
     * 
     * @param settings nastavení
     */
    public void set(final Settings settings) {
        Preconditions.checkNotNull(settings);
        
        if (settings.equals(this.settings)) {
            return;
        }
        
        final NormalWord newNamePrefix = settings.getPrefix();
        final NormalWord newJoinedPullState = NormalWords.join(newNamePrefix, settings.getPullState());
        final NormalWord newJoinedPullStopState = NormalWords.join(newNamePrefix, settings.getPullStopState());
        final NormalWord newJoinedRandomizeState = NormalWords.join(newNamePrefix, settings.getRandomizeState());
        final NormalWord newJoinedPredicateName = NormalWords.join(newNamePrefix, settings.getTestingPredicate());
        final NormalWord newJoinedReturnState = NormalWords.join(newNamePrefix, settings.getReturnState());
        final NormalWord newJoinedSuccessState = NormalWords.join(newNamePrefix, settings.getSuccessState());
        final NormalWord newJoinedFailState = NormalWords.join(newNamePrefix, settings.getFailState());
        
        this.predicatesNamingAuthority.tryReplace(ImmutableMap.of(joinTestingPredicate().getText(), newJoinedPredicateName.getText()));
        try {
            final ImmutableMap.Builder<String, String> replacements = ImmutableMap.builder();
            replacements.put(joinPullState().getText(), newJoinedPullState.getText());
            replacements.put(joinPullStopState().getText(), newJoinedPullStopState.getText());
            replacements.put(joinRandomizeState().getText(), newJoinedRandomizeState.getText());
            replacements.put(joinReturnState().getText(), newJoinedReturnState.getText());
            replacements.put(joinSuccessState().getText(), newJoinedSuccessState.getText());
            replacements.put(joinFailState().getText(), newJoinedFailState.getText());
            
            this.statesNamingAuthority.tryReplace(replacements.build());
        } catch (final Exception e) {
            this.predicatesNamingAuthority.tryReplace(ImmutableMap.of(newJoinedPredicateName.getText(), joinTestingPredicate().getText()));
            throw e;
        }
        
        this.settings = settings;
        this.dispatcher.fire(SettingsChangedEvent.create(this, this.settings));
    }
    
    /**
     * Vrátí konfiguraci bota.
     * 
     * @return konfigurace bota
     */
    public BotConfiguration getBotConfiguration() {
        return this.runtimeSettings.getBotConfiguration();
    }
    
    /**
     * Nastaví bota.
     * 
     * @param botConfiguration konfigurace bota
     */
    public void setBotConfiguration(final BotConfiguration botConfiguration) {
        Preconditions.checkNotNull(botConfiguration);
        
        this.runtimeSettings = RuntimeSettings.create(botConfiguration, runtimeSettings.getLanguageConfiguration(), runtimeSettings.getConversationConfiguration());
        this.dispatcher.fire(BotSettingsChangedEvent.create(this, botConfiguration));
    }
    

    /**
     * Vrátí konfiguraci jazyka.
     * 
     * @return konfigurace jazyka
     */
    public LanguageConfiguration getLanguageConfiguration() {
        return this.runtimeSettings.getLanguageConfiguration();
    }
    
    /**
     * Nastaví jazyk.
     * 
     * @param languageConfiguration konfigurace jazyka
     */
    public void setLanguageConfiguration(final LanguageConfiguration languageConfiguration) {
        Preconditions.checkNotNull(languageConfiguration);
        
        this.runtimeSettings = RuntimeSettings.create(runtimeSettings.getBotConfiguration(), languageConfiguration, runtimeSettings.getConversationConfiguration());
        this.dispatcher.fire(LanguageSettingsChangedEvent.create(this, languageConfiguration));
    }
    
    /**
     * Vrátí konfiguraci bota.
     * 
     * @return konfigurace bota
     */
    public ConversationConfiguration getConversationConfiguration() {
        return this.runtimeSettings.getConversationConfiguration();
    }
    
    /**
     * Nastaví konverzaci.
     * 
     * @param conversationConfiguration konfigurace konverzace
     */
    public void setConversationConfiguration(final ConversationConfiguration conversationConfiguration) {
        Preconditions.checkNotNull(conversationConfiguration);
        
        this.runtimeSettings = RuntimeSettings.create(runtimeSettings.getBotConfiguration(), runtimeSettings.getLanguageConfiguration(), conversationConfiguration);
        this.dispatcher.fire(ConversationSettingsChangedEvent.create(this, conversationConfiguration));
    }

    private NormalWord joinTestingPredicate() {
        return joinWithCurrentPrefix(this.settings.getTestingPredicate());
    }

    private NormalWord joinRandomizeState() {
        return joinWithCurrentPrefix(this.settings.getRandomizeState());
    }

    private NormalWord joinPullStopState() {
        return joinWithCurrentPrefix(this.settings.getPullStopState());
    }

    private NormalWord joinPullState() {
        return joinWithCurrentPrefix(this.settings.getPullState());
    }
    
    private NormalWord joinSuccessState() {
        return joinWithCurrentPrefix(this.settings.getSuccessState());
    }
    
    private NormalWord joinReturnState() {
        return joinWithCurrentPrefix(this.settings.getReturnState());
    }
    
    private NormalWord joinFailState() {
        return joinWithCurrentPrefix(this.settings.getFailState());
    }
    
    /**
     * Spojí aktuální prefix provozních stavů s daným názvem stavu.
     * 
     * @param word pojmenování stavu bez prefixu
     * @return název stavu s odlišovacím prefixem
     */
    private NormalWord joinWithCurrentPrefix(final NormalWord word) {
        return NormalWords.join(this.settings.getPrefix(), word);
    }
}
