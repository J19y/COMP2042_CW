package com.comp2042.tetris.app;


public interface GameModeLifecycle extends CreateNewGame {
    
    default void startMode() {}

    
    default void pauseMode() {}

    
    default void resumeMode() {}
}

