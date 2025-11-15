package com.comp2042.tetris.mechanics.state;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * Manages the game state transitions using the State pattern.
 * Extracted from GuiController to follow Single Responsibility Principle.
 * Replaces boolean flags (isPause, isGameOver) with a clear state machine.
 */
public final class GameStateManager {
    
    private final ObjectProperty<GameState> currentState = 
        new SimpleObjectProperty<>(GameState.MENU);

    
    // Enum representing possible game states.
     
    public enum GameState {
        MENU,
        PLAYING,
        PAUSED,
        GAME_OVER;
        
        // Check if input should be accepted in this state.
        public boolean canAcceptInput() {
            return this == PLAYING;
        }
        
        
        // Check if the game should update in this state.
         
        public boolean canUpdateGame() {
            return this == PLAYING;
        }
        
        
        // Check if the game is in an active state (not over).
        
        public boolean isActive() {
            return this == PLAYING || this == PAUSED;
        }
    }

    
    // Get the current game state.
    public GameState getCurrentState() {
        return currentState.get();
    }

    
    // Get the state property for binding.
    public ReadOnlyObjectProperty<GameState> stateProperty() {
        return currentState;
    }

    /**
     * Transition to the PLAYING state.
     * Only allowed from MENU or GAME_OVER states.
     */
    public void startGame() {
        if (currentState.get() == GameState.MENU || 
            currentState.get() == GameState.GAME_OVER) {
            currentState.set(GameState.PLAYING);
        }
    }

    /**
     * Transition to the PAUSED state.
     * Only allowed from PLAYING state.
     */
    public void pauseGame() {
        if (currentState.get() == GameState.PLAYING) {
            currentState.set(GameState.PAUSED);
        }
    }

    /**
     * Resume the game (transition to PLAYING state).
     * Only allowed from PAUSED state.
     */
    public void resumeGame() {
        if (currentState.get() == GameState.PAUSED) {
            currentState.set(GameState.PLAYING);
        }
    }

    
    // Transition to the GAME_OVER state.
    // Only allowed from PLAYING state.
    public void gameOver() {
        if (currentState.get() == GameState.PLAYING) {
            currentState.set(GameState.GAME_OVER);
        }
    }

    
    // Check if the game can accept user input in the current state.
    public boolean canAcceptInput() {
        return currentState.get().canAcceptInput();
    }

    
    // Check if the game should update (run game loop) in the current state.
    public boolean canUpdateGame() {
        return currentState.get().canUpdateGame();
    }

    
    // Check if the current state is GAME_OVER.
    public boolean isGameOver() {
        return currentState.get() == GameState.GAME_OVER;
    }


    // Check if the current state is PAUSED.
    public boolean isPaused() {
        return currentState.get() == GameState.PAUSED;
    }
}
