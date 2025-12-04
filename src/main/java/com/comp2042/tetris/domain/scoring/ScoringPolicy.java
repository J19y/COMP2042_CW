package com.comp2042.tetris.domain.scoring;

import com.comp2042.tetris.ui.input.EventSource;


public interface ScoringPolicy {

    int scoreForLineClear(int linesCleared);

    
    int scoreForDrop(EventSource source, boolean movedDown);
}

