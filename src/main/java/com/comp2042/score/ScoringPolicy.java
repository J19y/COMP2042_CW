package com.comp2042.score;

import com.comp2042.event.EventSource;

/**
 * Strategy interface to compute scoring for various game actions.
 * Implementations can provide different scoring rules without
 * modifying game logic.
 */
public interface ScoringPolicy {

    int scoreForLineClear(int linesCleared);

    /**
     * Score awarded for a single downward tick.
     * movedDown indicates whether the brick actually moved (no collision).
     */
    int scoreForDrop(EventSource source, boolean movedDown);
}
