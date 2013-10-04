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
package cz.cuni.mff.ms.brodecva.botnicek.library.demo;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import org.xhtmlrenderer.extend.NamespaceHandler;
import org.xhtmlrenderer.simple.FSScrollPane;
import org.xhtmlrenderer.simple.XHTMLPanel;
import org.xhtmlrenderer.simple.extend.XhtmlNamespaceHandler;

/**
 * GUI demonstrační aplikace využívající zobrazující konverzaci jako XHTML
 * dokument.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class GuiXhtml extends AbstractGui {

    /**
     * Konce prvku SPAN.
     */
    private static final String SPAN_END = "</span>";

    /**
     * Začátek označení jména robota.
     */
    private static final String SPAN_CLASS_ROBOT = "<span class=\"robot\">";

    /**
     * Začátek označení uvození uživatele.
     */
    private static final String SPAN_CLASS_USER = "<span class=\"user\">";

    /**
     * Konec prvku BODY a HTML.
     */
    private static final String PAGE_END = "</body></html>";

    /**
     * Začátek odstavce - promluvy.
     */
    private static final String PARAGRAPH_START = "<p class=\"speech\">";

    /**
     * Konec odstavce.
     */
    private static final String PARAGRAPH_END = "</p>";

    /**
     * Znak nové řádky.
     */
    private static final char NEW_LINE = '\n';

    /**
     * Uvozující dvojtečka (za jménem autora).
     */
    private static final String LEADING_COLON = ": ";

    /**
     * URL dokumentu.
     */
    private static final String DOCUMENT_URL =
            "http://ms.mff.cuni.cz/~brodecva/botnicek/library/demo/xhtmlview?render=";

    /**
     * Hlavička dokumentu.
     */
    private static final String DOCUMENT_INTRO =
            "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>"
                    + "<html xmlns=\"http://www.w3.org/1999/xhtml\">";

    /**
     * Definice stylů dokumentu.
     */
    private static final String STYLES =
            "<head><style type=\"text/css\">"
                    + "body{ background-color: #222222; font-size: 10pt; font-family: Arial, Helvetica, \"Nimbus Sans L\", \"Liberation Sans\", FreeSans, Sans-serif; } "
                    + ".speech{ margin: 2px; color: #dddddd; } .user{ font-weight: bold; color: #ccffcc; } .robot{ font-weight: bold; color: #ccccff; }"
                    + "</style></head><body>";

    /**
     * Panel zobrazující XHTML.
     */
    private XHTMLPanel xhtmlView;

    /**
     * Posuvník panelu.
     */
    private JScrollPane scroll;

    /**
     * Specifikace nakládání s dokumentem.
     */
    private NamespaceHandler documentHandling;

    /**
     * Builder řetězce dokumentu.
     */
    private StringBuilder documentBuilder;

    /**
     * Počet vykreslení.
     */
    private int renderCount = 0;

    /**
     * Vytvoří implementaci GUI pro konverzaci.
     * 
     * @param robotName
     *            jméno robota
     */
    public GuiXhtml(final String robotName) {
        super(robotName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.library.demo.alice.AbstractGui#registerView
     * (java.awt.Container)
     */
    @Override
    protected void registerView(final Container holder) {
        xhtmlView = new XHTMLPanel();
        xhtmlView.getSharedContext().getTextRenderer()
                .setSmoothingThreshold(0f);
        documentHandling = new XhtmlNamespaceHandler();
        documentBuilder = new StringBuilder(DOCUMENT_INTRO + STYLES);

        final GridBagConstraints viewConstraint = new GridBagConstraints();

        viewConstraint.fill = GridBagConstraints.BOTH;
        viewConstraint.gridheight = VIEW_GRID_HEIGHT;
        viewConstraint.gridwidth = VIEW_GRID_WIDTH;
        viewConstraint.gridx = VIEW_GRID_X;
        viewConstraint.gridy = VIEW_GRID_Y;
        viewConstraint.insets =
                new Insets(VIEW_INSET_DEFAULT_VALUE, VIEW_INSET_DEFAULT_VALUE,
                        VIEW_INSET_DEFAULT_VALUE, VIEW_INSET_DEFAULT_VALUE);
        viewConstraint.ipadx = VIEW_PADDING_DEFAULT_VALUE;
        viewConstraint.ipady = VIEW_PADDING_DEFAULT_VALUE;
        viewConstraint.weightx = VIEW_WEIGHT_X;
        viewConstraint.weighty = VIEW_WEIGHT_Y;

        scroll = new FSScrollPane(xhtmlView);
        final JScrollBar bar = scroll.getVerticalScrollBar();
        xhtmlView.addComponentListener(new ComponentListener() {
            /**
             * Zajistí posun na konec po přidání textu.
             * 
             * @param e
             *            událost komponenty (nevyužito)
             * 
             * @see java.awt.event.ComponentListener#componentResized(java.awt.event.ComponentEvent)
             */
            @Override
            public void componentResized(final ComponentEvent e) {
                bar.setValue(bar.getMaximum());
            }

            /*
             * (non-Javadoc)
             * 
             * @see
             * java.awt.event.ComponentListener#componentHidden(java.awt.event
             * .ComponentEvent)
             */
            @Override
            public void componentHidden(final ComponentEvent e) {
            }

            /*
             * (non-Javadoc)
             * 
             * @see
             * java.awt.event.ComponentListener#componentMoved(java.awt.event
             * .ComponentEvent)
             */
            @Override
            public void componentMoved(final ComponentEvent e) {
            }

            /*
             * (non-Javadoc)
             * 
             * @see
             * java.awt.event.ComponentListener#componentShown(java.awt.event
             * .ComponentEvent)
             */
            @Override
            public void componentShown(final ComponentEvent e) {
            }
        });
        holder.add(scroll, viewConstraint);
        
        xhtmlView.setDocumentFromString(documentBuilder.toString() + PAGE_END, DOCUMENT_URL
                + (++renderCount), documentHandling);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.library.demo.alice.AbstractGui#appendInput
     * (java.lang.String, java.lang.String)
     */
    @Override
    protected void appendInput(final String author, final String input) {
        appendText(SPAN_CLASS_USER + author + LEADING_COLON + SPAN_END + input);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.library.demo.alice.AbstractGui#appendOutput
     * (java.lang.String, java.lang.String)
     */
    @Override
    protected void appendOutput(final String author, final String output) {
        appendText(SPAN_CLASS_ROBOT + author + LEADING_COLON + SPAN_END
                + output);
    }

    /**
     * Přidá text tak, že jej obalí do značek pro odstavec.
     * 
     * @param text
     *            text k přidání do dokumentu na konec
     */
    private void appendText(final String text) {
        documentBuilder.append(PARAGRAPH_START + text + PARAGRAPH_END
                + NEW_LINE);
        final String documentSnapshot = documentBuilder.toString() + PAGE_END;

        xhtmlView.setDocumentFromString(documentSnapshot, DOCUMENT_URL
                + (++renderCount), documentHandling);
    }
}
