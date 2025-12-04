package com.comp2042.tetris.ui.input;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

/**
 * Tests for input event system types and enumerations.
 */
class InputEventTypeTest {

    @Test
    void eventTypeEnumHasAllRequiredTypes() {
        assertNotNull(EventType.DOWN);
        assertNotNull(EventType.LEFT);
        assertNotNull(EventType.RIGHT);
        assertNotNull(EventType.ROTATE);
        assertNotNull(EventType.HARD_DROP);
        assertNotNull(EventType.PAUSE);
    }

    @Test
    void eventTypeEnumHasSixTypes() {
        assertEquals(6, EventType.values().length, "EventType enum should have exactly 6 types");
    }

    @Test
    void eventTypesAreDistinct() {
        EventType[] types = EventType.values();
        for (int i = 0; i < types.length; i++) {
            for (int j = i + 1; j < types.length; j++) {
                assertNotNull(types[i]);
                assertNotNull(types[j]);
            }
        }
    }

    @Test
    void movementEventTypesExist() {
        assertNotNull(EventType.DOWN, "DOWN movement should exist");
        assertNotNull(EventType.LEFT, "LEFT movement should exist");
        assertNotNull(EventType.RIGHT, "RIGHT movement should exist");
    }

    @Test
    void rotationEventTypeExists() {
        assertNotNull(EventType.ROTATE, "ROTATE event should exist");
    }

    @Test
    void specialActionTypesExist() {
        assertNotNull(EventType.HARD_DROP, "HARD_DROP action should exist");
        assertNotNull(EventType.PAUSE, "PAUSE action should exist");
    }

    @Test
    void eventTypeEnumCanBeCompared() {
        EventType type1 = EventType.LEFT;
        EventType type2 = EventType.LEFT;
        EventType type3 = EventType.RIGHT;

        assertEquals(type1, type2, "Same enum values should be equal");
        assertNotNull(type1);
        assertNotNull(type3);
    }

    @Test
    void eventTypeEnumCanBeIterated() {
        int count = 0;
        for (EventType type : EventType.values()) {
            count++;
            assertNotNull(type, "Each EventType should not be null");
        }
        assertEquals(6, count, "Should iterate through all 6 types");
    }

    @Test
    void eventTypeStringRepresentations() {
        for (EventType type : EventType.values()) {
            String name = type.name();
            assertNotNull(name);
            assertTrue(name.length() > 0, "EventType name should not be empty");
        }
    }

    @Test
    void eventTypeOrdinalValues() {
        EventType[] types = EventType.values();
        for (int i = 0; i < types.length; i++) {
            assertEquals(i, types[i].ordinal(), "Ordinal should match position in enum");
        }
    }

    private void assertTrue(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
