package com.comp2042.tetris.domain.scoring;

import com.comp2042.tetris.domain.model.Score;

import javafx.beans.property.IntegerProperty;

/**
 * Manages the game score and provides a facade over the {@link Score} value object.
 * <p>
 * Provides methods for adding points, resetting the score, and accessing
 * the score property for UI binding.
 * </p>
 *
 * @author Youssif Mahmoud Gomaa Sayed
 * @version 1.0
 */
public final class ScoreManager {
    private final Score score = new Score();

    /**
     * Returns the score property for JavaFX binding.
     *
     * @return the {@link IntegerProperty} representing the score
     */
    public IntegerProperty scoreProperty() {
        return score.scoreProperty();
    }

    /**
     * Gets the current score value.
     *
     * @return the current score
     */
    public int getValue() {
        return score.getValue();
    }

    /**
     * Adds points to the current score.
     *
     * @param points the points to add
     */
    public void add(int points) {
        score.add(points);
    }

    /**
     * Resets the score to zero.
     */
    public void reset() {
        score.reset();
    }
}

