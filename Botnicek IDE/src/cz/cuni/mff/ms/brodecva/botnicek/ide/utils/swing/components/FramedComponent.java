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
package cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.components;

import java.awt.Rectangle;

import javax.swing.JComponent;

/**
 * Komponenta, která si udržuje pro svoje potřeby vnitřní rámeček (odlišný od
 * standardního rámečku běžných komponent).
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public abstract class FramedComponent extends JComponent {

    private static final long serialVersionUID = 1L;

    private static final int FRAME_SIZE = 100;

    private static Rectangle frame(final Rectangle bounds) {
        return new Rectangle(bounds.x - FRAME_SIZE, bounds.y - FRAME_SIZE,
                bounds.width + 2 * FRAME_SIZE, bounds.height + 2 * FRAME_SIZE);
    }

    /**
     * Vytvoří zarámovanou komponentu.
     */
    protected FramedComponent() {
        super();
    }

    /**
     * Vrátí výšku komponenty bez rámu.
     * 
     * @return výška bez rámu
     */
    public final int getContentHeight() {
        return getHeight() - 2 * FRAME_SIZE;
    }

    /**
     * Vrátí šířku komponenty bez rámu.
     * 
     * @return šířka bez rámu
     */
    public final int getContentWidth() {
        return getWidth() - 2 * FRAME_SIZE;
    }

    /**
     * Vrátí umístění oblasti bez rámu podle osy x.
     * 
     * @return umístění oblasti bez rámu
     */
    public final int getContentX() {
        return getX() + getFrameWidth();
    }

    /**
     * Vrátí umístění oblasti bez rámu podle osy y.
     * 
     * @return umístění oblasti bez rámu podle y
     */
    public final int getContentY() {
        return getY() + getFrameHeight();
    }

    /**
     * Vrátí výšku rámu.
     * 
     * @return výška rámu
     */
    public final int getFrameHeight() {
        return FRAME_SIZE;
    }

    /**
     * Vrátí šířku rámu.
     * 
     * @return šířka rámu
     */
    public final int getFrameWidth() {
        return FRAME_SIZE;
    }

    /**
     * Nastaví umístění oblasti komponenty uvnitř rámu.
     * 
     * @param x
     *            souřadnice umístění podle osy x
     * @param y
     *            souřadnice umístění podle osy y
     */
    public final void setContentLocation(final int x, final int y) {
        super.setLocation(x - FRAME_SIZE, y - FRAME_SIZE);
    }

    /**
     * Nastaví hranice komponenty včetně rámu.
     * 
     * @param bounds
     *            hranice komponenty bez rámu
     */
    public final void setFramedBounds(final Rectangle bounds) {
        setBounds(frame(bounds));
    }
}
