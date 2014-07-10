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
package cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing;

import java.awt.Rectangle;
import java.awt.event.ComponentListener;
import java.util.Arrays;

import javax.swing.JComponent;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public abstract class GraphComponent extends JComponent {

    private static final int FRAME_SIZE = 100;
    
    private static Rectangle frame(final Rectangle bounds) {
        return new Rectangle(bounds.x - FRAME_SIZE, bounds.y - FRAME_SIZE, bounds.width + 2 * FRAME_SIZE, bounds.height + 2 * FRAME_SIZE);
    }

    public GraphComponent() {
        super();
    }
    
    public void removeAllComponentListeners() {
        final ComponentListener[] listeners = getComponentListeners();
        final ComponentListener[] listenersCopy = Arrays.copyOf(listeners, listeners.length);
        
        for (final ComponentListener removed : listenersCopy) {
            this.removeComponentListener(removed);
        }
    }
    
    public void setFramedBounds(Rectangle bounds) {
        setBounds(frame(bounds));
    }
    
    public int getContentWidth() {
        return getWidth() - 2 * FRAME_SIZE;
    }
    
    public int getContentHeight() {
        return getHeight() - 2 * FRAME_SIZE;
    }
    
    public int getFrameWidth() {
        return FRAME_SIZE;
    }
    
    public int getFrameHeight() {
        return FRAME_SIZE;
    }
    
    public void setContentLocation(final int x, final int y) {
        super.setLocation(x - FRAME_SIZE, y - FRAME_SIZE);
    }
    
    public int getContentX() {
        return this.getX() + getFrameWidth();
    }
    
    public int getContentY() {
        return this.getY() + getFrameHeight();
    }
}
