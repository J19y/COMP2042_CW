package com.comp2042.tetris.application.port;

import com.comp2042.tetris.ui.input.DropInput;
import com.comp2042.tetris.ui.input.InputActionHandler;
import com.comp2042.tetris.ui.input.MovementInput;

import javafx.beans.property.IntegerProperty;

/**
 * Aggregates all input-handling interfaces into a single port.
 * <p>
 * Defines the contract between UI and game logic, combining movement,
 * drop, input action handling, and game lifecycle capabilities.
 * </p>
 *
 * @author Youssif Mahmoud Gomaa Sayed
 * @version 1.0
 */
public interface GameplayPort extends InputActionHandler, MovementInput, DropInput, CreateNewGame {
    
    /**
     * Returns the score property for UI binding.
     *
     * @return the score property
     */
    IntegerProperty scoreProperty();
}

