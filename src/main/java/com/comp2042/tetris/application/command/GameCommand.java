package com.comp2042.tetris.application.command;

import com.comp2042.tetris.domain.model.ShowResult;
import com.comp2042.tetris.ui.input.MoveEvent;

/**
 * Command pattern interface for game actions.
 * <p>
 * Each input type (left, right, rotate, drop) is encapsulated as a command object,
 * enabling flexible input handling and action replay capabilities.
 * </p>
 *
 * @version 1.0
 */
public interface GameCommand {
    
    /**
     * Executes the game command.
     *
     * @param event the move event triggering this command
     * @return the result of executing the command
     */
    ShowResult execute(MoveEvent event);
}

