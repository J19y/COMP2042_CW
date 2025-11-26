package com.comp2042.tetris.mechanics.board;

import com.comp2042.tetris.app.CreateNewGame;

import com.comp2042.tetris.ui.input.DropInput;
import com.comp2042.tetris.ui.input.InputActionHandler;
import com.comp2042.tetris.domain.model.ViewData;
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
}
