package com.comp2042.tetris.mechanics.state;

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
        // Fresh manager should expose MENU state via getter and property.
        assertEquals(GameStateManager.GameState.MENU, manager.getCurrentState());
        assertSame(manager.getCurrentState(), manager.stateProperty().get());
        assertFalse(manager.canAcceptInput());
        assertFalse(manager.canUpdateGame());
    }

    @Test
    void startGameMovesFromMenuToPlaying() {
        // Starting from MENU should enter PLAYING and enable input/updates.
        manager.startGame();

        assertEquals(GameStateManager.GameState.PLAYING, manager.getCurrentState());
        assertTrue(manager.canAcceptInput());
        assertTrue(manager.canUpdateGame());
        assertFalse(manager.isPaused());
        assertFalse(manager.isGameOver());
    }

    @Test
    void pauseAndResumeOnlyAffectPlayingState() {
        // Pause from PLAYING should switch to PAUSED and resume back to PLAYING.
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
        // GAME_OVER should only be reachable from PLAYING and block gameplay.
        manager.startGame();
        manager.gameOver();

        assertEquals(GameStateManager.GameState.GAME_OVER, manager.getCurrentState());
        assertTrue(manager.isGameOver());
        assertFalse(manager.canAcceptInput());
        assertFalse(manager.canUpdateGame());
    }

    @Test
    void startGameFromGameOverRestartsPlay() {
        // After GAME_OVER, starting again should go back to PLAYING.
        manager.startGame();
        manager.gameOver();
        manager.startGame();

        assertEquals(GameStateManager.GameState.PLAYING, manager.getCurrentState());
        assertFalse(manager.isGameOver());
    }

    @Test
    void illegalTransitionsLeaveStateUnchanged() {
        // Calling pause/resume/gameOver in MENU should be ignored.
        manager.pauseGame();
        manager.resumeGame();
        manager.gameOver();

        assertEquals(GameStateManager.GameState.MENU, manager.getCurrentState());

        // From GAME_OVER, pause/resume should also be ignored.
        manager.startGame();
        manager.gameOver();
        manager.pauseGame();
        manager.resumeGame();

        assertEquals(GameStateManager.GameState.GAME_OVER, manager.getCurrentState());
    }
}
