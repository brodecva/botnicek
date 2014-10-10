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
package cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Intended;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.test.UnitTest;

/**
 * Test výchozí implementace správce událostí.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see DefaultEventManager
 */
@Category(UnitTest.class)
public class DefaultEventManagerTest {

    private static final class DummyEvent implements Event<DummyListener> {

        @Override
        public void accept(final Visitor visitor) {
            visitor.visit(this);
        }

        @Override
        public void dispatchTo(final DummyListener listener) {
            listener.doSomething();
        }
    }

    private interface DummyListener {
        void doSomething();
    }

    private static final class DummyMappedEvent implements
            MappedEvent<String, DummyListener> {

        private final String key;

        public DummyMappedEvent(final String key) {
            this.key = key;
        }

        @Override
        public void accept(final Visitor visitor) {
            visitor.visit(this);
        }

        @Override
        public void dispatchTo(final DummyListener listener) {
            listener.doSomething();
        }

        @Override
        public String getKey() {
            return this.key;
        }
    }

    private DefaultEventManager tested = Intended.nullReference();

    /**
     * Vytvoří instanci správce událostí pro testování.
     * 
     * @throws java.lang.Exception
     *             pokud dojde k vyhození výjimky
     */
    @Before
    public void setUp() throws Exception {
        this.tested = DefaultEventManager.create();
    }

    /**
     * Zahodí instanci správce událostí pro testování.
     * 
     * @throws java.lang.Exception
     *             pokud dojde k vyhození výjimky
     */
    @After
    public void tearDown() throws Exception {
        this.tested = Intended.nullReference();
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.DefaultEventManager#addListener(java.lang.Class, java.lang.Object)}
     * .
     */
    @Test
    public void testAddAndRemoveEventListener() {
        final DummyListener listener =
                EasyMock.createNiceMock(DummyListener.class);

        this.tested.addListener(DummyEvent.class, listener);
        this.tested.removeListener(DummyEvent.class, listener);
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.DefaultEventManager#addListener(java.lang.Class, java.lang.Object)}
     * .
     */
    @Test
    public void testAddAndRemoveEventListenerWhenMappedEventRegisteredAccept() {
        final DummyListener listener =
                EasyMock.createNiceMock(DummyListener.class);

        this.tested.addListener(DummyMappedEvent.class, listener);
        this.tested.removeListener(DummyMappedEvent.class, listener);
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.DefaultEventManager#addListener(java.lang.Class, java.lang.Object, java.lang.Object)}
     * .
     */
    @Test
    public void testAddAndRemoveMappedEventListener() {
        final String key = "dummyKey";
        final DummyListener listener =
                EasyMock.createNiceMock(DummyListener.class);

        this.tested.addListener(DummyMappedEvent.class, key, listener);
        this.tested.removeListener(DummyMappedEvent.class, key, listener);
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.DefaultEventManager#removeAllListeners(java.lang.Class, java.lang.Object)}
     * .
     */
    @Test
    public
            void
            testAddEventAndMappedEventListenerAndRemoveAllMappedListenersAndFireExpectNotMappedDispatched() {
        final String key = "dummyKey";

        final DummyListener listener =
                EasyMock.createStrictMock(DummyListener.class);
        listener.doSomething();
        EasyMock.replay(listener);

        this.tested.addListener(DummyMappedEvent.class, key, listener);
        this.tested.addListener(DummyMappedEvent.class, listener);
        this.tested.removeAllListeners(DummyMappedEvent.class, key);
        this.tested.fire(new DummyMappedEvent(key));

        EasyMock.verify(listener);
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.DefaultEventManager#removeAllListeners(java.lang.Class)}
     * .
     */
    @Test
    public
            void
            testAddEventAndMappedEventListenerAndRemoveAllNotMappedListenersAndFireExpectMappedDispatched() {
        final String key = "dummyKey";

        final DummyListener listener =
                EasyMock.createStrictMock(DummyListener.class);
        listener.doSomething();
        EasyMock.replay(listener);

        this.tested.addListener(DummyMappedEvent.class, key, listener);
        this.tested.addListener(DummyMappedEvent.class, listener);
        this.tested.removeAllListeners(DummyMappedEvent.class);
        this.tested.fire(new DummyMappedEvent(key));

        EasyMock.verify(listener);
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.DefaultEventManager#fire(cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.Event)}
     * .
     */
    @Test
    public void testAddEventListenerAndFire() {
        final DummyListener listener =
                EasyMock.createStrictMock(DummyListener.class);
        listener.doSomething();
        EasyMock.replay(listener);

        this.tested.addListener(DummyEvent.class, listener);
        this.tested.fire(new DummyEvent());

        EasyMock.verify(listener);
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.DefaultEventManager#fire(cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.Event)}
     * .
     */
    @Test
    public void testAddEventListenerAndFireWhenEventMapped() {
        final String key = "dummyKey";

        final DummyListener listener =
                EasyMock.createStrictMock(DummyListener.class);
        listener.doSomething();
        EasyMock.replay(listener);

        this.tested.addListener(DummyMappedEvent.class, listener);
        this.tested.fire(new DummyMappedEvent(key));

        EasyMock.verify(listener);
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.DefaultEventManager#removeAllListeners(java.lang.Class)}
     * .
     */
    @Test
    public
            void
            testAddEventListenerAndRemoveAllListenersAndFireExpectNotDispatched() {
        final DummyListener listener =
                EasyMock.createStrictMock(DummyListener.class);
        EasyMock.replay(listener);

        this.tested.addListener(DummyEvent.class, listener);
        this.tested.removeAllListeners(DummyEvent.class);
        this.tested.fire(new DummyEvent());

        EasyMock.verify(listener);
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.DefaultEventManager#removeAllListeners(java.lang.Class)}
     * .
     */
    @Test
    public
            void
            testAddEventListenerAndRemoveAllListenersAndFireWhenEventMappedExpectNotDispatched() {
        final String key = "dummyKey";

        final DummyListener listener =
                EasyMock.createStrictMock(DummyListener.class);
        EasyMock.replay(listener);

        this.tested.addListener(DummyMappedEvent.class, listener);
        this.tested.removeAllListeners(DummyMappedEvent.class);
        this.tested.fire(new DummyMappedEvent(key));

        EasyMock.verify(listener);
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.DefaultEventManager#fire(cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.Event)}
     * .
     */
    @Test
    public void
            testAddEventListenerAndRemoveListenerAndFireExpectNotDispatched() {
        final DummyListener listener =
                EasyMock.createStrictMock(DummyListener.class);
        EasyMock.replay(listener);

        this.tested.addListener(DummyEvent.class, listener);
        this.tested.removeListener(DummyEvent.class, listener);
        this.tested.fire(new DummyEvent());

        EasyMock.verify(listener);
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.DefaultEventManager#fire(cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.Event)}
     * .
     */
    @Test
    public void testAddMappedEventListenerAndFire() {
        final String key = "dummyKey";

        final DummyListener listener =
                EasyMock.createStrictMock(DummyListener.class);
        listener.doSomething();
        EasyMock.replay(listener);

        this.tested.addListener(DummyMappedEvent.class, key, listener);
        this.tested.fire(new DummyMappedEvent(key));

        EasyMock.verify(listener);
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.DefaultEventManager#removeAllListeners(java.lang.Class, java.lang.Object)}
     * .
     */
    @Test
    public
            void
            testAddMappedEventListenerAndRemoveAllMappedListenersAndFireExpectNotDispatched() {
        final String key = "dummyKey";

        final DummyListener listener =
                EasyMock.createStrictMock(DummyListener.class);
        EasyMock.replay(listener);

        this.tested.addListener(DummyMappedEvent.class, key, listener);
        this.tested.removeAllListeners(DummyMappedEvent.class, key);
        this.tested.fire(new DummyMappedEvent(key));

        EasyMock.verify(listener);
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.DefaultEventManager#removeAllListeners(java.lang.Class, java.lang.Object)}
     * .
     */
    @Test
    public
            void
            testAddMappedEventListenerAndRemoveAllMappedListenersAndFireWhenKeyDifferentExpectDispatchedThoseWithOtherKey() {
        final String key = "dummyKey";
        final String otherKey = "otherDummyKey";

        final DummyListener listener =
                EasyMock.createStrictMock(DummyListener.class);
        listener.doSomething();
        EasyMock.replay(listener);

        this.tested.addListener(DummyMappedEvent.class, key, listener);
        this.tested.addListener(DummyMappedEvent.class, otherKey, listener);
        this.tested.removeAllListeners(DummyMappedEvent.class, key);
        this.tested.fire(new DummyMappedEvent(otherKey));

        EasyMock.verify(listener);
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.DefaultEventManager#fire(cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.Event)}
     * .
     */
    @Test
    public
            void
            testAddMappedEventListenerAndRemoveListenerAndFireExpectNotDispatched() {
        final String key = "dummyKey";

        final DummyListener listener =
                EasyMock.createStrictMock(DummyListener.class);
        EasyMock.replay(listener);

        this.tested.addListener(DummyMappedEvent.class, key, listener);
        this.tested.removeListener(DummyMappedEvent.class, key, listener);
        this.tested.fire(new DummyMappedEvent(key));

        EasyMock.verify(listener);
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.DefaultEventManager#fire(cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.Event)}
     * .
     */
    @Test
    public
            void
            testAddMappedEventListenerAndRemoveListenerAndFireWhenEventMappedExpectNotDispatched() {
        final String key = "dummyKey";

        final DummyListener listener =
                EasyMock.createStrictMock(DummyListener.class);
        EasyMock.replay(listener);

        this.tested.addListener(DummyMappedEvent.class, listener);
        this.tested.removeListener(DummyMappedEvent.class, listener);
        this.tested.fire(new DummyMappedEvent(key));

        EasyMock.verify(listener);
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.DefaultEventManager#create()}
     * .
     */
    @Test
    public void testCreate() {
        DefaultEventManager.create();
    }
}
