package com.comp2042.tetris.ui.input;

import com.comp2042.tetris.domain.model.ShowResult;

/**
 * Interface for handling brick drop inputs.
 * <p>
 * Defines the method for soft drop (moving brick down).
 * </p>
 *
 * @version 1.0
 */
public interface DropInput {
    
    /**
     * Handles down (soft drop) input.
     *
     * @param event the move event
     * @return the result of the drop action
     */
    ShowResult onDown(MoveEvent event);
}

