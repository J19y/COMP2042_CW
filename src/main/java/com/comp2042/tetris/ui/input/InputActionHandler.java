package com.comp2042.tetris.ui.input;

import com.comp2042.tetris.domain.model.ShowResult;

/**
 * Interface for handling game input actions.
 * <p>
 * Implementations process input events and return the resulting game state.
 * </p>
 *
 * @version 1.0
 */
public interface InputActionHandler {
    
    /**
     * Handles a game input event.
     *
     * @param event the input event to handle
     * @return the result of handling the event
     */
    ShowResult handle(MoveEvent event);
}

