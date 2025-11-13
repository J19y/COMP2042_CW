package com.comp2042.event;

import com.comp2042.model.ShowResult;
import com.comp2042.model.ViewData;

/**
 * Handles player input events in the game.
 * Uses a generic onEvent(MoveEvent) method that delegates to
 * specific handlers, keeping it flexible and easy to extend.
 */
public interface InputEventListener {

    ShowResult onDownEvent(MoveEvent event);

    ViewData onLeftEvent(MoveEvent event);
    ViewData onRightEvent(MoveEvent event);
    ViewData onRotateEvent(MoveEvent event);

    void createNewGame();

    /**
     * Main event handler. Returns ShowResult for DOWN moves,
     * and ViewData for all other moves.
     * By default, it calls the specific handler methods. New events fall back
     * to DOWN handling unless overridden by the listener implementation.
     */
    default Object onEvent(MoveEvent event) {
        EventType type = event.getEventType();
        return switch (type) {
            case DOWN -> onDownEvent(event);
            case LEFT -> onLeftEvent(event);
            case RIGHT -> onRightEvent(event);
            case ROTATE -> onRotateEvent(event);
            default -> onDownEvent(event);
        };
    }
}
