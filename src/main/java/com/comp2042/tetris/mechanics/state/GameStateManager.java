package com.comp2042.tetris.mechanics.state;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

// Manages the game state transitions using the State pattern.
public final class GameStateManager {
    
    private final ObjectProperty<GameState> currentState = 
        new SimpleObjectProperty<>(GameState.MENU);

    
    // Enum representing possible game states.
     
    public enum GameState {
        MENU {
            @Override
            GameState startGame(GameStateManager manager) {
                return PLAYING;
            }
        },
        PLAYING {
            @Override
            GameState pauseGame(GameStateManager manager) {
                return PAUSED;
            }

            @Override
            GameState gameOver(GameStateManager manager) {
                return GAME_OVER;
            }
        },
        PAUSED {
            @Override
            GameState resumeGame(GameStateManager manager) {
                return PLAYING;
            }
        },
        GAME_OVER {
            @Override
            GameState startGame(GameStateManager manager) {
                return PLAYING;
            }
        };

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

        GameState startGame(GameStateManager manager) {
            return this;
        }

        GameState pauseGame(GameStateManager manager) {
            return this;
        }

        GameState resumeGame(GameStateManager manager) {
            return this;
        }

        GameState gameOver(GameStateManager manager) {
            return this;
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
        transitionTo(currentState.get().startGame(this));
    }

    /**
     * Transition to the PAUSED state.
     * Only allowed from PLAYING state.
     */
    public void pauseGame() {
        transitionTo(currentState.get().pauseGame(this));
    }

    /**
     * Resume the game (transition to PLAYING state).
     * Only allowed from PAUSED state.
     */
    public void resumeGame() {
        transitionTo(currentState.get().resumeGame(this));
    }

    
    // Transition to the GAME_OVER state.
    // Only allowed from PLAYING state.
    public void gameOver() {
        transitionTo(currentState.get().gameOver(this));
    }

    private void transitionTo(GameState nextState) {
        if (nextState != null && nextState != currentState.get()) {
            currentState.set(nextState);
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
