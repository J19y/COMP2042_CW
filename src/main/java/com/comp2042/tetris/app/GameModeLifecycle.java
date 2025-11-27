package com.comp2042.tetris.app;

/**
 * Optional lifecycle hooks for game modes that need explicit control
 * over start/pause/resume behaviour (e.g. timed modes).
 */
public interface GameModeLifecycle extends CreateNewGame {
    /**
     * Called once the view countdown (3..2..1) has finished and the
     * game should begin mode-specific timers/behaviour.
     */
    default void startMode() {}

    /**
     * Called when the game is paused and the mode should suspend timers/logic.
     */
    default void pauseMode() {}

    /**
     * Called when the game is resumed and the mode should resume timers/logic.
     */
    default void resumeMode() {}
}
