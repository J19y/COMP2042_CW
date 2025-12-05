package com.comp2042.tetris.domain.scoring;

import com.comp2042.tetris.ui.input.EventSource;

/**
 * Classic Tetris scoring policy implementation.
 * <p>
 * Calculates scores using the classic formula:
 * </p>
 * <ul>
 *   <li>Line clear: 50 × linesCleared²</li>
 *   <li>Manual soft drop: 1 point per cell dropped</li>
 *   <li>Auto-drop: 0 points</li>
 * </ul>
 *
 * @author Youssif Mahmoud Gomaa Sayed
 * @version 1.0
 */
public final class ClassicScoringPolicy implements ScoringPolicy {
    
    /**
     * {@inheritDoc}
     * Uses formula: 50 × linesCleared² (e.g., 1 line = 50, 2 lines = 200, 4 lines = 800)
     */
    @Override
    public int scoreForLineClear(int linesCleared) {
        if (linesCleared <= 0) return 0;
        return 50 * linesCleared * linesCleared;
    }

    /**
     * {@inheritDoc}
     * Awards 1 point for user-initiated drops, 0 for automatic drops.
     */
    @Override
    public int scoreForDrop(EventSource source, boolean movedDown) {
        if (!movedDown) return 0;
        return source == EventSource.USER ? 1 : 0;
    }
}

