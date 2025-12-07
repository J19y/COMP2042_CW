package com.comp2042.tetris.ui.input;

import com.comp2042.tetris.domain.model.ShowResult;

/**
 * Interface for handling brick movement inputs.
 * <p>
 * Defines methods for left, right, and rotation movements.
 * </p>
 *
 * @version 1.0
 */
public interface MovementInput {
    
    /**
     * Handles left movement input.
     *
     * @param event the move event
     * @return the result of the movement
     */
    ShowResult onLeft(MoveEvent event);
    
    /**
     * Handles right movement input.
     *
     * @param event the move event
     * @return the result of the movement
     */
    ShowResult onRight(MoveEvent event);
    
    /**
     * Handles rotation input.
     *
     * @param event the move event
     * @return the result of the rotation
     */
    ShowResult onRotate(MoveEvent event);
}

