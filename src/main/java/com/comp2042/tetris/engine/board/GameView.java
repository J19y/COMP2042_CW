package com.comp2042.tetris.engine.board;

import com.comp2042.tetris.application.port.CreateNewGame;
import com.comp2042.tetris.domain.model.ViewData;
import com.comp2042.tetris.ui.input.DropInput;
import com.comp2042.tetris.ui.input.InputActionHandler;

import javafx.beans.property.IntegerProperty;

/**
 * Interface defining the contract between game logic and the visual representation.
 * <p>
 * Provides methods for initializing the view, refreshing the display,
 * handling game events, and binding properties for reactive updates.
 * </p>
 *
 * @version 1.0
 */
public interface GameView {
    
    /**
     * Initializes the game view with the board matrix and active brick data.
     *
     * @param boardMatrix the initial board state
     * @param activeBrickData the initial active brick information
     */
    void initGameView(int[][] boardMatrix, ViewData activeBrickData);

    /**
     * Refreshes the game background with the updated board matrix.
     *
     * @param boardMatrix the current board state
     */
    void refreshGameBackground(int[][] boardMatrix);

    /**
     * Accepts and processes a show result from game actions.
     *
     * @param result the result to process
     */
    void acceptShowResult(com.comp2042.tetris.domain.model.ShowResult result);

    /**
     * Triggers the settle animation for the active brick.
     *
     * @param onFinished callback to invoke when animation completes
     */
    void settleActiveBrick(Runnable onFinished);

    /**
     * Binds the score property for reactive UI updates.
     *
     * @param scoreProperty the score property to bind
     */
    void bindScore(IntegerProperty scoreProperty);

    /**
     * Binds the level property for reactive UI updates.
     * Default implementation does nothing.
     *
     * @param levelProperty the level property to bind
     */
    default void bindLevel(IntegerProperty levelProperty) {
        
    }

    /**
     * Sets the input handlers for user interaction.
     *
     * @param inputActionHandler the handler for input actions
     * @param dropInput the handler for drop input
     * @param gameLifecycle the handler for game lifecycle events
     */
    void setInputHandlers(InputActionHandler inputActionHandler, DropInput dropInput, CreateNewGame gameLifecycle);

    /**
     * Triggers the game over state in the view.
     */
    void gameOver();

    /**
     * Sets the remaining time display for timed modes.
     *
     * @param seconds the remaining time in seconds
     */
    void setRemainingTime(int seconds);

    /**
     * Sets the visibility of the game board.
     * Default implementation does nothing.
     *
     * @param visible true to show, false to hide
     */
    default void setBoardVisibility(boolean visible) {
        
    }

    /**
     * Shows a message to the player.
     * Default implementation does nothing.
     *
     * @param message the message to display
     */
    default void showMessage(String message) {
        
    }

    /**
     * Shows an event message (for mystery mode events).
     * Default implementation does nothing.
     *
     * @param message the event message to display
     */
    default void showEventMessage(String message) {
        
    }

    /**
     * Plays the earthquake visual effect.
     * Default implementation does nothing.
     */
    default void playEarthquakeAnimation() {
        
    }

    /**
     * Plays the level increment animation.
     * Default implementation does nothing.
     */
    default void animateLevelIncrement() {
        
    }

    /**
     * Shows the fog effect for the specified duration.
     * Default implementation does nothing.
     *
     * @param seconds duration of the fog effect
     */
    default void showFogEffect(int seconds) {
        
    }

    /**
     * Shows the heavy gravity effect indicator.
     * Default implementation does nothing.
     *
     * @param seconds duration of the effect
     */
    default void showHeavyGravityEffect(int seconds) {
        
    }
}

