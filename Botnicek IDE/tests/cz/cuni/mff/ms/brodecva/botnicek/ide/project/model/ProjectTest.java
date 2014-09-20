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

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.easymock.Capture;
import org.easymock.EasyMock;
import org.junit.Test;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.types.SystemName;
import cz.cuni.mff.ms.brodecva.botnicek.ide.project.events.ProjectOpenedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.project.events.ProjectOpenedListener;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.Dispatcher;

/**
 * Testuje projektovou třídu.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see Project
 */
public class ProjectTest {

    private static final SystemName PROJECT_NAME = SystemName.of("My_Project");

    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.project.model.Project#createAndOpen(SystemName, cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.Dispatcher)}.
     */
    @Test
    public void testCreateAndOpenExpectAnnouncedInstanceTheSameAsReturned() {
        final Capture<ProjectOpenedEvent> capturedEvent = new Capture<>();
        
        final Dispatcher dispatcherMock = EasyMock.createStrictMock(Dispatcher.class);
        dispatcherMock.fire(EasyMock.capture(capturedEvent));
        EasyMock.replay(dispatcherMock);
        
        final Project newInstance = Project.createAndOpen(PROJECT_NAME, dispatcherMock);
        
        final ProjectOpenedListener listener = new ProjectOpenedListener() {
            
            
            
            @Override
            public void opened(Project opened) {
                assertEquals(newInstance, opened);
            }
            
        };
        capturedEvent.getValue().dispatchTo(listener);
    }

    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.project.model.Project#open(java.io.InputStream, Dispatcher)}.
     * 
     * @throws IOException pokud dojde k chybě při otevírání či načítání
     * @throws ClassNotFoundException pokud je otevřená definice projektu nekompatibilní
     */
    @Test
    public void testSaveAndOpenLoopback() throws IOException, ClassNotFoundException {
        final Dispatcher dispatcherMock = EasyMock.createStrictMock(Dispatcher.class);
        dispatcherMock.fire(EasyMock.notNull(ProjectOpenedEvent.class));
        EasyMock.expectLastCall().times(2);
        EasyMock.replay(dispatcherMock);
        
        final Project original = Project.createAndOpen(PROJECT_NAME, dispatcherMock);
        
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        original.save(outputStream);
        
        final byte[] pickled = outputStream.toByteArray();
        
        final InputStream inputStream = new ByteArrayInputStream(pickled);
        Project.open(inputStream, dispatcherMock);
    }
}
