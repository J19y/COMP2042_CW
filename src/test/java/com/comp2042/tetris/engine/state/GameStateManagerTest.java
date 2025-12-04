package com.comp2042.tetris.engine.state;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameStateManagerTest {

    private GameStateManager manager;

    @BeforeEach
    void setUp() {
        manager = new GameStateManager();
    }

    @Test
    void startsInMenuState() {
        
        assertEquals(GameStateManager.GameState.MENU, manager.getCurrentState());
        assertSame(manager.getCurrentState(), manager.stateProperty().get());
        assertFalse(manager.canAcceptInput());
        assertFalse(manager.canUpdateGame());
    }

    @Test
    void startGameMovesFromMenuToPlaying() {
        
        manager.startGame();

        assertEquals(GameStateManager.GameState.PLAYING, manager.getCurrentState());
        assertTrue(manager.canAcceptInput());
        assertTrue(manager.canUpdateGame());
        assertFalse(manager.isPaused());
        assertFalse(manager.isGameOver());
    }

    @Test
    void pauseAndResumeOnlyAffectPlayingState() {
        
        manager.startGame();
        manager.pauseGame();

        assertEquals(GameStateManager.GameState.PAUSED, manager.getCurrentState());
        assertTrue(manager.isPaused());
        assertFalse(manager.canAcceptInput());

        manager.resumeGame();
        assertEquals(GameStateManager.GameState.PLAYING, manager.getCurrentState());
        assertFalse(manager.isPaused());
    }

    @Test
    void gameOverFromPlayingDisablesInput() {
        
        manager.startGame();
        manager.gameOver();

        assertEquals(GameStateManager.GameState.GAME_OVER, manager.getCurrentState());
        assertTrue(manager.isGameOver());
        assertFalse(manager.canAcceptInput());
        assertFalse(manager.canUpdateGame());
    }

    @Test
    void startGameFromGameOverRestartsPlay() {
        
        manager.startGame();
        manager.gameOver();
        manager.startGame();

        assertEquals(GameStateManager.GameState.PLAYING, manager.getCurrentState());
        assertFalse(manager.isGameOver());
    }

    @Test
    void illegalTransitionsLeaveStateUnchanged() {
        
        manager.pauseGame();
        manager.resumeGame();
        manager.gameOver();

        assertEquals(GameStateManager.GameState.MENU, manager.getCurrentState());

        
        manager.startGame();
        manager.gameOver();
        manager.pauseGame();
        manager.resumeGame();

        assertEquals(GameStateManager.GameState.GAME_OVER, manager.getCurrentState());
    }
}

