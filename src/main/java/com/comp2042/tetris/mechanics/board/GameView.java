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
}
