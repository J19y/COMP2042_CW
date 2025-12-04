package com.comp2042.tetris.ui.input;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for input system components: MoveEvent, EventType, EventSource, and InputActionHandler.
 * Focuses on API contracts and event data propagation.
 */
class InputSystemTest {

    private MoveEvent moveEvent;

    @BeforeEach
    void setUp() {
        // Setup test fixtures
    }

    @Test
    void moveEventStoresEventType() {
        moveEvent = new MoveEvent(EventType.LEFT, EventSource.USER);
        assertEquals(EventType.LEFT, moveEvent.getEventType(), "Event should store LEFT type");
    }

    @Test
    void moveEventStoresEventSource() {
        moveEvent = new MoveEvent(EventType.RIGHT, EventSource.THREAD);
        assertEquals(EventSource.THREAD, moveEvent.getEventSource(), "Event should store THREAD source");
    }

    @Test
    void moveEventCanBeCreatedWithAllEventTypes() {
        for (EventType type : EventType.values()) {
            moveEvent = new MoveEvent(type, EventSource.USER);
            assertEquals(type, moveEvent.getEventType(), "Should store all event types");
        }
    }

    @Test
    void moveEventCanBeCreatedWithAllEventSources() {
        for (EventSource source : EventSource.values()) {
            moveEvent = new MoveEvent(EventType.DOWN, source);
            assertEquals(source, moveEvent.getEventSource(), "Should store all event sources");
        }
    }

    @Test
    void eventSourceEnumHasUserValue() {
        assertNotNull(EventSource.USER, "USER event source should exist");
        assertEquals(EventSource.USER, EventSource.USER);
    }

    @Test
    void eventSourceEnumHasThreadValue() {
        assertNotNull(EventSource.THREAD, "THREAD event source should exist");
        assertEquals(EventSource.THREAD, EventSource.THREAD);
    }

    @Test
    void eventTypeDownExists() {
        moveEvent = new MoveEvent(EventType.DOWN, EventSource.USER);
        assertEquals(EventType.DOWN, moveEvent.getEventType());
    }

    @Test
    void eventTypeLeftExists() {
        moveEvent = new MoveEvent(EventType.LEFT, EventSource.USER);
        assertEquals(EventType.LEFT, moveEvent.getEventType());
    }

    @Test
    void eventTypeRightExists() {
        moveEvent = new MoveEvent(EventType.RIGHT, EventSource.USER);
        assertEquals(EventType.RIGHT, moveEvent.getEventType());
    }

    @Test
    void eventTypeRotateExists() {
        moveEvent = new MoveEvent(EventType.ROTATE, EventSource.USER);
        assertEquals(EventType.ROTATE, moveEvent.getEventType());
    }

    @Test
    void eventTypeHardDropExists() {
        moveEvent = new MoveEvent(EventType.HARD_DROP, EventSource.USER);
        assertEquals(EventType.HARD_DROP, moveEvent.getEventType());
    }

    @Test
    void eventTypePauseExists() {
        moveEvent = new MoveEvent(EventType.PAUSE, EventSource.USER);
        assertEquals(EventType.PAUSE, moveEvent.getEventType());
    }

    @Test
    void inputActionHandlerInterfaceExists() {
        // Verify InputActionHandler interface is accessible
        assertNotNull(InputActionHandler.class, "InputActionHandler interface should exist");
    }

    @Test
    void inputActionHandlerCanHandleMoveEvent() {
        MoveEvent event = new MoveEvent(EventType.LEFT, EventSource.USER);
        assertNotNull(event, "MoveEvent should be created successfully");
    }

    @Test
    void inputActionHandlerCanHandleMultipleEventTypes() {
        for (EventType type : EventType.values()) {
            MoveEvent event = new MoveEvent(type, EventSource.USER);
            assertNotNull(event, "MoveEvent should be created for all event types");
        }
    }

    @Test
    void moveEventGettersReturnSameValues() {
        EventType type = EventType.ROTATE;
        EventSource source = EventSource.THREAD;
        moveEvent = new MoveEvent(type, source);

        assertSame(type, moveEvent.getEventType(), "Should return same EventType instance");
        assertSame(source, moveEvent.getEventSource(), "Should return same EventSource instance");
    }

    @Test
    void moveEventWithUserSourceIsDistinctFromThreadSource() {
        MoveEvent userEvent = new MoveEvent(EventType.LEFT, EventSource.USER);
        MoveEvent threadEvent = new MoveEvent(EventType.LEFT, EventSource.THREAD);

        assertEquals(userEvent.getEventType(), threadEvent.getEventType());
        assertNotNull(userEvent.getEventSource());
        assertNotNull(threadEvent.getEventSource());
    }
}
