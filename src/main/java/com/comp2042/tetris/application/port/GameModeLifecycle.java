package com.comp2042.tetris.application.port;

/**
 * Interface for game mode lifecycle events.
 * <p>
 * Enables polymorphic handling of different game modes by providing
 * hooks for start, pause, and resume operations.
 * </p>
 *
 * @author Youssif Mahmoud Gomaa Sayed
 * @version 1.0
 */
public interface GameModeLifecycle extends CreateNewGame {
    
    /**
     * Called when the game mode starts.
     * Default implementation does nothing.
     */
    default void startMode() {}

    /**
     * Called when the game is paused.
     * Default implementation does nothing.
     */
    default void pauseMode() {}

    /**
     * Called when the game resumes from pause.
     * Default implementation does nothing.
     */
    default void resumeMode() {}
}

