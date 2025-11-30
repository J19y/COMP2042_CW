package com.comp2042.tetris.mechanics.board;

import com.comp2042.tetris.app.CreateNewGame;
import com.comp2042.tetris.domain.model.ViewData;
import com.comp2042.tetris.ui.input.DropInput;
import com.comp2042.tetris.ui.input.InputActionHandler;

import javafx.beans.property.IntegerProperty;


public interface GameView {
    void initGameView(int[][] boardMatrix, ViewData activeBrickData);

    void refreshGameBackground(int[][] boardMatrix);

    
    void acceptShowResult(com.comp2042.tetris.domain.model.ShowResult result);

    
    void settleActiveBrick(Runnable onFinished);

    void bindScore(IntegerProperty scoreProperty);

    
    default void bindLevel(IntegerProperty levelProperty) {
        
    }

    void setInputHandlers(InputActionHandler inputActionHandler, DropInput dropInput, CreateNewGame gameLifecycle);

    void gameOver();

    
    void setRemainingTime(int seconds);

    
    default void setBoardVisibility(boolean visible) {
        
    }

    
    default void showMessage(String message) {
        
    }

    
    default void showEventMessage(String message) {
        
    }

    
    default void playEarthquakeAnimation() {
        
    }

    
    default void animateLevelIncrement() {
        
    }

    
    default void showFogEffect(int seconds) {
        
    }

    
    default void showHeavyGravityEffect(int seconds) {
        
    }
}

