package com.comp2042.tetris.domain.scoring;

import com.comp2042.tetris.ui.input.EventSource;


public final class ClassicScoringPolicy implements ScoringPolicy {
    @Override
    public int scoreForLineClear(int linesCleared) {
        if (linesCleared <= 0) return 0;
        return 50 * linesCleared * linesCleared;
    }

    @Override
    public int scoreForDrop(EventSource source, boolean movedDown) {
        if (!movedDown) return 0;
        return source == EventSource.USER ? 1 : 0;
    }
}

