package com.comp2042.tetris.ui.input;

/**
 * Immutable event object representing a game input action.
 * <p>
 * Combines the type of event with its source to provide complete
 * context for input handling.
 * </p>
 *
 * @author Youssif Mahmoud Gomaa Sayed
 * @version 1.0
 */
public final class MoveEvent {
    private final EventType eventType;
    private final EventSource eventSource;

    /**
     * Constructs a MoveEvent with the specified type and source.
     *
     * @param eventType the type of input event
     * @param eventSource the source of the event
     */
    public MoveEvent(EventType eventType, EventSource eventSource) {
        this.eventType = eventType;
        this.eventSource = eventSource;
    }

    /**
     * Gets the type of this event.
     *
     * @return the event type
     */
    public EventType getEventType() {
        return eventType;
    }

    /**
     * Gets the source of this event.
     *
     * @return the event source
     */
    public EventSource getEventSource() {
        return eventSource;
    }
}

