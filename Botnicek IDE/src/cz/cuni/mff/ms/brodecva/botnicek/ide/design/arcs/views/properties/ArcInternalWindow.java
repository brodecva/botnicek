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
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.properties;

import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.Code;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.MixedPattern;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.SimplePattern;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.controllers.CheckController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.controllers.ArcController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.controllers.AvailableReferencesController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.CodeTestArc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.PatternArc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.PredicateTestArc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.RecurentArc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.TransitionArc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.properties.types.AbstractTypePanel;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.properties.types.ArcViewAdapter;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.properties.types.CodePanel;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.properties.types.CodeTestArcPanel;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.properties.types.MainPanel;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.properties.types.PatternArcPanel;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.properties.types.PredicateTestArcPanel;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.properties.types.RecurentArcPanel;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.properties.types.TransitionArcPanel;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.properties.types.TypeView;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.Presence;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.logging.LocalizedLogger;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.resources.UiLocalizer;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.layouts.WrapLayout;

/**
 * Správce vnitřního rámu ovládajícího a zobrazujícího podrobnosti hrany.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public class ArcInternalWindow extends ArcViewAdapter {

    private final static Logger LOGGER = LocalizedLogger
            .getLogger(ArcInternalWindow.class);

    private static final int STRUT_SIZE = 10;

    private static final String VIEW_COPY_IDENTIFIER_SEPARATOR = " @ ";

    private static final Map<Class<? extends Arc>, String> DEFAULT_SUPPORTED_TYPES_TO_DESCRIPTIONS =
            ImmutableMap.<Class<? extends Arc>, String> of(PatternArc.class,
                    UiLocalizer.print("PatternTest"), PredicateTestArc.class,
                    UiLocalizer.print("PredicateTest"), CodeTestArc.class,
                    UiLocalizer.print("CodeTest"), TransitionArc.class,
                    UiLocalizer.print("Transition"), RecurentArc.class,
                    UiLocalizer.print("Recursion"));

    private static final List<Class<? extends Arc>> DEFAULT_SUPPORTED_TYPES =
            ImmutableList.<Class<? extends Arc>> of(TransitionArc.class,
                    PatternArc.class, CodeTestArc.class,
                    PredicateTestArc.class, RecurentArc.class);

    private static final int SAVE_BUTTON_WIDTH = 100;

    private static final int SAVE_BUTTON_HEIGHT = 24;

    private static final Dimension SAVE_BUTTON_DIMENSION = new Dimension(
            SAVE_BUTTON_WIDTH, SAVE_BUTTON_HEIGHT);

    private static ArcInternalWindow create(final Container parent) {
        return create(parent, DummyArcController.create(),
                DummyAvailableReferencesController.create(),
                DummyNormalWordValidationController.create(),
                DummyCodeValidationController.create(),
                DummySimplePatternValidationController.create(),
                DummyMixedPatternValidationController.create(),
                DummyNormalWordValidationController.create());
    }

    private final ArcController arcController;
    private final ReferrableInternalFrame frame;

    private final JPanel contentPane = new JPanel();

    private final MainPanel mainArcPanel;
    private final JPanel typeChoicePanel = new JPanel(new WrapLayout(
            WrapLayout.CENTER));

    private final ButtonGroup typeChangeButtonGroup = new ButtonGroup();
    private Class<? extends Arc> currentType;
    private final Map<Class<? extends Arc>, JRadioButton> typesToRadios =
            new HashMap<>();
    private final Map<Class<? extends Arc>, AbstractTypePanel> arcTypesToElements;
    private final CardLayout typesPanelLayout = new CardLayout();

    private final JPanel typesPanel = new JPanel(this.typesPanelLayout);

    private final CodePanel codePanel;

    private final JButton saveButton = new JButton(UiLocalizer.print("Save"));

    private final BiMap<String, ImageIcon> icons;
    private static final String BOTNICEK_ICON_NAME = "botnicekIcon";

    private static final BiMap<String, String> iconNamesToPaths =
            ImmutableBiMap.<String, String> of(BOTNICEK_ICON_NAME,
                    "images/botnicek.png");

    /**
     * Vytvoří správce vnitřního rámu.
     * 
     * @param parent
     *            rodičovský kontejner
     * @param arcController
     *            řadič vlastností hrany
     * @param availableReferencesController
     *            řadič dostupných uzlů k zanoření výpočtu
     * @param nameValidationController
     *            řadič validace názvu hrany
     * @param codeValidationController
     *            řadič validace provedeného kódu
     * @param simplePatternValidationController
     *            řadič validace vzoru očekávané hodnoty
     * @param mixedPatternValidationController
     *            řadič validace složených vzoru kategorií
     * @param predicateNameValidationController
     *            řadič validace názvu predikátu
     * @return správce vnitřního rámu
     */
    public static
            ArcInternalWindow
            create(final Container parent,
                    final ArcController arcController,
                    final AvailableReferencesController availableReferencesController,
                    final CheckController<? extends NormalWord> nameValidationController,
                    final CheckController<? extends Code> codeValidationController,
                    final CheckController<? extends SimplePattern> simplePatternValidationController,
                    final CheckController<? extends MixedPattern> mixedPatternValidationController,
                    final CheckController<? extends NormalWord> predicateNameValidationController) {
        Preconditions.checkNotNull(arcController);
        Preconditions.checkNotNull(availableReferencesController);
        Preconditions.checkNotNull(nameValidationController);
        Preconditions.checkNotNull(codeValidationController);
        Preconditions.checkNotNull(simplePatternValidationController);
        Preconditions.checkNotNull(mixedPatternValidationController);
        Preconditions.checkNotNull(predicateNameValidationController);

        final ReferrableInternalFrame frame = ReferrableInternalFrame.create();

        final MainPanel mainArcPanel =
                MainPanel.create(frame, arcController,
                        predicateNameValidationController);
        final CodePanel codePanel =
                CodePanel
                        .create(frame, arcController, codeValidationController);

        final CodeTestArcPanel codeTestArcPanel =
                CodeTestArcPanel.create(frame, arcController,
                        codeValidationController,
                        simplePatternValidationController);
        final PatternArcPanel patternArcPanel =
                PatternArcPanel.create(frame, arcController,
                        mixedPatternValidationController);
        final PredicateTestArcPanel predicateTestArcPanel =
                PredicateTestArcPanel.create(frame, arcController,
                        codeValidationController,
                        simplePatternValidationController,
                        predicateNameValidationController);
        final RecurentArcPanel recurentArcPanel =
                RecurentArcPanel.create(arcController,
                        availableReferencesController);
        final TransitionArcPanel transitionArcPanel =
                TransitionArcPanel.create(arcController);

        final ImmutableMap.Builder<Class<? extends Arc>, AbstractTypePanel> arcTypesToElementsBuilder =
                ImmutableMap.builder();
        arcTypesToElementsBuilder.put(CodeTestArc.class, codeTestArcPanel);
        arcTypesToElementsBuilder.put(PatternArc.class, patternArcPanel);
        arcTypesToElementsBuilder.put(PredicateTestArc.class,
                predicateTestArcPanel);
        arcTypesToElementsBuilder.put(RecurentArc.class, recurentArcPanel);
        arcTypesToElementsBuilder.put(TransitionArc.class, transitionArcPanel);

        return create(parent, frame, mainArcPanel, codePanel,
                arcTypesToElementsBuilder.build(),
                DEFAULT_SUPPORTED_TYPES_TO_DESCRIPTIONS,
                DEFAULT_SUPPORTED_TYPES, arcController);
    }

    /**
     * Vytvoří správce vnitřního rámu.
     * 
     * @param parent
     *            rodičovský kontejner
     * @param frame
     *            samotný vnitřní rám
     * @param mainArcPanel
     *            panel s obecným nastavením hrany
     * @param codePanel
     *            panel s provedeným kódem v případě úspěšného testu
     * @param typesToPanels
     *            přiřazení typů hran k panelům tvořícím části rámu
     * @param typesToDescriptions
     *            přiřazení typů k jejich popiskům
     * @param supportedTypes
     *            podporované typy hran
     * @param arcController
     *            řadič vlastností hrany
     * @return správce rámu
     */
    public static ArcInternalWindow create(final Container parent,
            final ReferrableInternalFrame frame, final MainPanel mainArcPanel,
            final CodePanel codePanel,
            final Map<Class<? extends Arc>, AbstractTypePanel> typesToPanels,
            final Map<Class<? extends Arc>, String> typesToDescriptions,
            final List<Class<? extends Arc>> supportedTypes,
            final ArcController arcController) {
        Preconditions.checkNotNull(parent);
        Preconditions.checkNotNull(frame);
        Preconditions.checkNotNull(arcController);
        Preconditions.checkNotNull(mainArcPanel);
        Preconditions.checkNotNull(codePanel);
        Preconditions.checkNotNull(typesToPanels);
        Preconditions.checkNotNull(typesToDescriptions);
        Preconditions.checkNotNull(supportedTypes);

        final ArcInternalWindow newInstance =
                new ArcInternalWindow(frame, mainArcPanel, codePanel,
                        typesToPanels, typesToDescriptions, supportedTypes,
                        arcController);

        for (final Entry<Class<? extends Arc>, JRadioButton> provided : newInstance.typesToRadios
                .entrySet()) {
            final Class<? extends Arc> type = provided.getKey();
            final JRadioButton typeChangeRadio = provided.getValue();

            typeChangeRadio.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(final ActionEvent e) {
                    newInstance.switchTypePanel(type);
                }
            });
        }

        newInstance.saveButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                final TypeView currentTypeElement =
                        newInstance.arcTypesToElements
                                .get(newInstance.currentType);

                currentTypeElement.save(mainArcPanel.getNewName(),
                        mainArcPanel.getPriority(), codePanel.getCode());
            }
        });

        arcController.addView(newInstance);
        arcController.fill(newInstance);

        newInstance.frame.addInternalFrameListener(new InternalFrameAdapter() {
            /*
             * (non-Javadoc)
             * 
             * @see
             * javax.swing.event.InternalFrameAdapter#internalFrameClosed(javax
             * .swing.event.InternalFrameEvent)
             */
            @Override
            public void internalFrameClosed(final InternalFrameEvent e) {
                newInstance.unsubscribe();
                newInstance.clearAll();
            }
        });

        parent.add(newInstance.frame);
        newInstance.frame.offset();

        return newInstance;
    }

    /**
     * Spustí testovací verzi.
     * 
     * @param args
     *            argumenty
     */
    public static void main(final String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager
                            .getSystemLookAndFeelClassName());

                    final JDesktopPane desktopPane = new JDesktopPane();
                    final ArcInternalWindow arcInternalWindow =
                            ArcInternalWindow.create(desktopPane);
                    final JScrollPane contentPane =
                            new JScrollPane(desktopPane);
                    contentPane.setPreferredSize(new Dimension(300, 1000));
                    arcInternalWindow.show();

                    final JFrame frame = new JFrame();
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    frame.setContentPane(contentPane);
                    frame.pack();
                    frame.setVisible(true);
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private ArcInternalWindow(final ReferrableInternalFrame frame,
            final MainPanel mainArcPanel, final CodePanel codePanel,
            final Map<Class<? extends Arc>, AbstractTypePanel> typesToElements,
            final Map<Class<? extends Arc>, String> typesToDescriptions,
            final List<Class<? extends Arc>> orderedSupportedTypes,
            final ArcController arcController) {
        Preconditions.checkNotNull(arcController);
        Preconditions.checkNotNull(frame);
        Preconditions.checkNotNull(mainArcPanel);
        Preconditions.checkNotNull(codePanel);
        Preconditions.checkNotNull(typesToElements);
        Preconditions.checkNotNull(typesToDescriptions);
        Preconditions.checkNotNull(orderedSupportedTypes);

        Preconditions.checkArgument(typesToDescriptions.keySet().equals(
                typesToElements.keySet()));
        Preconditions.checkArgument(Sets.newHashSet(orderedSupportedTypes)
                .equals(typesToDescriptions.keySet()));

        this.arcController = arcController;

        this.frame = frame;
        this.arcTypesToElements = ImmutableMap.copyOf(typesToElements);

        this.mainArcPanel = mainArcPanel;
        this.codePanel = codePanel;

        for (final Class<? extends Arc> type : orderedSupportedTypes) {
            final String description = typesToDescriptions.get(type);
            final JRadioButton typeChangeRadio =
                    new JRadioButton(description, false);

            this.typesToRadios.put(type, typeChangeRadio);
            this.typeChangeButtonGroup.add(typeChangeRadio);
            this.typeChoicePanel.add(typeChangeRadio);

            final AbstractTypePanel panel = typesToElements.get(type);
            this.typesPanel.add(panel, type.getName());
        }

        this.saveButton.setMaximumSize(SAVE_BUTTON_DIMENSION);
        this.saveButton.setMinimumSize(SAVE_BUTTON_DIMENSION);
        this.saveButton.setPreferredSize(SAVE_BUTTON_DIMENSION);

        final JPanel savePane = new JPanel();
        savePane.setLayout(new BoxLayout(savePane, BoxLayout.X_AXIS));
        savePane.add(this.saveButton);

        this.typeChoicePanel.setBorder(BorderFactory
                .createTitledBorder(UiLocalizer.print("ArcTypeChange")));
        this.typesPanel.setBorder(BorderFactory.createTitledBorder(UiLocalizer
                .print("TransitionCondition")));
        this.codePanel.setBorder(BorderFactory.createTitledBorder(UiLocalizer
                .print("ExecutedCode")));

        this.contentPane.setLayout(new BoxLayout(this.contentPane,
                BoxLayout.Y_AXIS));
        this.contentPane.add(this.mainArcPanel);
        this.contentPane.add(Box.createVerticalStrut(STRUT_SIZE));
        this.contentPane.add(this.typeChoicePanel);
        this.contentPane.add(Box.createVerticalStrut(STRUT_SIZE));
        this.contentPane.add(this.typesPanel);
        this.contentPane.add(Box.createVerticalStrut(STRUT_SIZE));
        this.contentPane.add(this.codePanel);
        this.contentPane.add(Box.createVerticalStrut(STRUT_SIZE));
        this.contentPane.add(savePane);

        this.frame.setClosable(true);
        this.frame.setIconifiable(true);
        this.frame.setResizable(true);
        this.frame.setContentPane(this.contentPane);
        this.frame.setSize(250, 630);

        this.icons = loadIcons();
        final ImageIcon botnicekIcon = this.icons.get(BOTNICEK_ICON_NAME);
        if (Presence.isPresent(botnicekIcon)) {
            this.frame.setFrameIcon(botnicekIcon);
        }
    }

    private void clearAll() {
        final Collection<AbstractTypePanel> panels =
                this.arcTypesToElements.values();
        for (final AbstractTypePanel typePanel : panels) {
            typePanel.clear();
        }

        this.codePanel.clear();
        this.mainArcPanel.clear();
    }

    private void clearAllAndResetSwitchedTo(
            final Class<? extends Arc> switchedTo) {
        final Set<Entry<Class<? extends Arc>, AbstractTypePanel>> entries =
                this.arcTypesToElements.entrySet();
        for (final Entry<Class<? extends Arc>, AbstractTypePanel> entry : entries) {
            final AbstractTypePanel typePanel = entry.getValue();

            if (entry.getKey().equals(switchedTo)) {
                typePanel.reset(this.frame);
            } else {
                typePanel.clear();
            }
        }
    }

    private void clearAllButSwitchedTo(final Class<? extends Arc> switchedTo) {
        final Set<Entry<Class<? extends Arc>, AbstractTypePanel>> entries =
                this.arcTypesToElements.entrySet();
        for (final Entry<Class<? extends Arc>, AbstractTypePanel> entry : entries) {
            final AbstractTypePanel typePanel = entry.getValue();

            if (entry.getKey().equals(switchedTo)) {
                continue;
            }

            typePanel.clear();
        }
    }

    private BiMap<String, ImageIcon> loadIcons() {
        final ImmutableBiMap.Builder<String, ImageIcon> iconsBuilder =
                ImmutableBiMap.builder();
        for (final Entry<String, String> iconEntry : iconNamesToPaths
                .entrySet()) {
            final String name = iconEntry.getKey();
            final String path = iconEntry.getValue();

            final URL iconUrl = getClass().getResource(path);
            if (Presence.isPresent(iconUrl)) {
                iconsBuilder.put(name, new ImageIcon(iconUrl));
            } else {
                LOGGER.log(Level.WARNING, "MissingIcon", path);
            }
        }
        return iconsBuilder.build();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.types.ArcViewAdapter
     * #removed()
     */
    @Override
    public void removed() {
        this.frame.dispose();
    }

    /**
     * Zobrazí vnitřní panel.
     */
    public void show() {
        this.frame.show();
    }

    private void switchTypePanel(final Class<? extends Arc> arcClass) {
        clearAllAndResetSwitchedTo(arcClass);

        this.typesPanelLayout.show(this.typesPanel, arcClass.getName());

        this.currentType = arcClass;
    }

    private void unsubscribe() {
        this.arcController.removeView(this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.types.ArcViewAdapter
     * #updatedName(cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord)
     */
    @Override
    public void updateName(final NormalWord name) {
        assert SwingUtilities.isEventDispatchThread();

        Preconditions.checkNotNull(name);

        this.frame.setTitle(name.getText() + VIEW_COPY_IDENTIFIER_SEPARATOR
                + Integer.toHexString(hashCode()));
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.ArcView#updatedType
     * (java.lang.Class)
     */
    @Override
    public void updateType(final Class<? extends Arc> arcClass) {
        assert SwingUtilities.isEventDispatchThread();

        Preconditions.checkNotNull(arcClass);

        final JRadioButton typeButton = this.typesToRadios.get(arcClass);
        Preconditions.checkArgument(Presence.isPresent(typeButton));

        typeButton.setSelected(true);
        updateTypePanel(arcClass);
    }

    private void updateTypePanel(final Class<? extends Arc> arcClass) {
        clearAllButSwitchedTo(arcClass);

        this.typesPanelLayout.show(this.typesPanel, arcClass.getName());

        this.currentType = arcClass;
    }
}
