package com.comp2042.tetris.domain.scoring;

import com.comp2042.tetris.ui.input.EventSource;

/**
 * Strategy interface for calculating game scores.
 * <p>
 * Implements the Strategy pattern to allow different scoring algorithms
 * for various game modes. Each implementation defines how points are
 * awarded for line clears and drops.
 * </p>
 *
 * @version 1.0
 */
public interface ScoringPolicy {

    /**
     * Calculates the score for clearing lines.
     *
     * @param linesCleared the number of lines cleared simultaneously
     * @return the score points awarded
     */
    int scoreForLineClear(int linesCleared);

    /**
     * Calculates the score for a drop action.
     *
     * @param source the event source (USER for manual drop, THREAD for auto-drop)
     * @param movedDown whether the brick actually moved down
     * @return the score points awarded
     */
    int scoreForDrop(EventSource source, boolean movedDown);
}

