package com.comp2042.game;

import com.comp2042.event.DropInput;
import com.comp2042.event.InputActionHandler;
import com.comp2042.model.ViewData;
import javafx.beans.property.IntegerProperty;

// Abstraction for the game view layer. High-level game logic depends on this interface
public interface GameView {
    void initGameView(int[][] boardMatrix, ViewData activeBrickData);

    void refreshGameBackground(int[][] boardMatrix);

    void bindScore(IntegerProperty scoreProperty);

    void setInputHandlers(InputActionHandler inputActionHandler, DropInput dropInput, CreateNewGame gameLifecycle);

    void gameOver();
}
