package com.comp2042.tetris.engine.state;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * Manages the game state transitions using the State pattern.
 * Controls transitions between MENU, PLAYING, PAUSED, and GAME_OVER states.
 * Provides observable state property for UI binding and state-specific behavior.
 *
 */
public final class GameStateManager {
    
    private final ObjectProperty<GameState> currentState = 
        new SimpleObjectProperty<>(GameState.MENU);

    
    
     
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

        
        public boolean canAcceptInput() {
            return this == PLAYING;
        }

        
        public boolean canUpdateGame() {
            return this == PLAYING;
        }

        
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

    
    
    public GameState getCurrentState() {
        return currentState.get();
    }

    
    
    public ReadOnlyObjectProperty<GameState> stateProperty() {
        return currentState;
    }

    
    public void startGame() {
        transitionTo(currentState.get().startGame(this));
    }

    
    public void pauseGame() {
        transitionTo(currentState.get().pauseGame(this));
    }

    
    public void resumeGame() {
        transitionTo(currentState.get().resumeGame(this));
    }

    
    
    
    public void gameOver() {
        transitionTo(currentState.get().gameOver(this));
    }

    private void transitionTo(GameState nextState) {
        if (nextState != null && nextState != currentState.get()) {
            currentState.set(nextState);
        }
    }

    
    
    public boolean canAcceptInput() {
        return currentState.get().canAcceptInput();
    }

    
    
    public boolean canUpdateGame() {
        return currentState.get().canUpdateGame();
    }

    
    
    public boolean isGameOver() {
        return currentState.get() == GameState.GAME_OVER;
    }


    
    public boolean isPaused() {
        return currentState.get() == GameState.PAUSED;
    }
}

