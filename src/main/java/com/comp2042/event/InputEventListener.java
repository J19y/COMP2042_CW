package com.comp2042.event;

import com.comp2042.model.ShowResult;

/**
 * Handles player input events in the game.
 * Uses a generic onEvent(MoveEvent) method that delegates to
 * specific handlers, keeping it flexible and easy to extend.
 */
public interface InputEventListener {

    ShowResult onDownEvent(MoveEvent event);
    ShowResult onLeftEvent(MoveEvent event);
    ShowResult onRightEvent(MoveEvent event);
    ShowResult onRotateEvent(MoveEvent event);

    void createNewGame();


    // Unified event handler returning a ShowResult for ALL event types.
    default ShowResult onEvent(MoveEvent event) {
        EventType type = event.getEventType();
        return switch (type) {
            case DOWN -> onDownEvent(event);
            case LEFT -> onLeftEvent(event);
            case RIGHT -> onRightEvent(event);
            case ROTATE -> onRotateEvent(event);
            case HARD_DROP -> onDownEvent(event); // fallback handled by controller override if needed
        };
    }
}
