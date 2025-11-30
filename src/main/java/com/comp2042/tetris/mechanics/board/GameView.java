package com.comp2042.tetris.mechanics.board;

import com.comp2042.tetris.app.CreateNewGame;
import com.comp2042.tetris.domain.model.ViewData;
import com.comp2042.tetris.ui.input.DropInput;
import com.comp2042.tetris.ui.input.InputActionHandler;

import javafx.beans.property.IntegerProperty;

// Abstraction for the game view layer. High-level game logic depends on this interface
public interface GameView {
    void initGameView(int[][] boardMatrix, ViewData activeBrickData);

    void refreshGameBackground(int[][] boardMatrix);

    /**
     * Accepts a ShowResult asynchronously from the game engine so the view
     * can update visuals (next-brick, active brick, etc.).
     */
    void acceptShowResult(com.comp2042.tetris.domain.model.ShowResult result);

    /**
     * Ask the view to animate a short settle of the active brick, then invoke
     * the provided callback when the animation finishes.
     */
    void settleActiveBrick(Runnable onFinished);

    void bindScore(IntegerProperty scoreProperty);

    /**
     * Bind an integer property representing a visible "level" for modes that expose it
     * (e.g. Mystery mode). Default implementation is no-op to keep non-GUI / legacy
     * implementations unaffected.
     */
    default void bindLevel(IntegerProperty levelProperty) {
        // default no-op
    }

    void setInputHandlers(InputActionHandler inputActionHandler, DropInput dropInput, CreateNewGame gameLifecycle);

    void gameOver();

    /**
     * Update the view with remaining seconds for timed game modes.
     * Implementations may ignore this call if they do not display a countdown.
     * @param seconds remaining seconds in the countdown
     */
    void setRemainingTime(int seconds);

    /**
     * Toggle visibility of the main board and active-piece layers. Default no-op
     * so tests and non-GUI implementations are unaffected.
     * @param visible true to show board, false to hide
     */
    default void setBoardVisibility(boolean visible) {
        // default: do nothing. GUI implementations may override.
    }

    /**
     * Show a short message to the player (e.g. "FOG", "EARTHQUAKE"). Default no-op.
     * @param message text to display
     */
    default void showMessage(String message) {
        // default: do nothing. GUI implementations may override.
    }

    /**
     * Show a large event notification (uses RowClearMessage style). Default no-op.
     */
    default void showEventMessage(String message) {
        // default no-op for non-GUI implementations
    }

    /**
     * Play an earthquake / shake animation on the game board (visual effect for garbage line).
     * Default no-op for non-GUI implementations.
     */
    default void playEarthquakeAnimation() {
        // default implementation does nothing
    }

    /**
     * Animate the level display when it increments (e.g. pulse effect).
     * Default no-op for non-GUI implementations.
     */
    default void animateLevelIncrement() {
        // default implementation does nothing
    }

    /**
     * Show a fog effect over the board for a number of seconds. GUI implementations
     * may choose to overlay a semi-opaque blurred rectangle or temporarily hide the board.
     * @param seconds duration of the fog effect
     */
    default void showFogEffect(int seconds) {
        // default no-op
    }

    /**
     * Show a heavy-gravity visual effect (e.g. red flash/pulse) for a number of seconds.
     * @param seconds duration of the heavy gravity effect
     */
    default void showHeavyGravityEffect(int seconds) {
        // default no-op
    }
}
