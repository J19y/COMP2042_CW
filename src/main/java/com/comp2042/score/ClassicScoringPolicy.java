package com.comp2042.score;

import com.comp2042.event.EventSource;

/**
 * Classic Tetris-like scoring:
 * - Line clear: 50 * linesCleared^2
 * - Manual soft drop: +1 per downward move by user
 * - Gravity (thread) drop: 0
 */
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
