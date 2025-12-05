package com.comp2042.tetris.domain.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Value object encapsulating the game score as a JavaFX {@link IntegerProperty}.
 * <p>
 * Provides reactive UI binding capabilities through JavaFX property observers.
 * The score can only be increased with positive values and can be reset to zero.
 * </p>
 *
 * @author Youssif Mahmoud Gomaa Sayed
 * @version 1.0
 */
public final class Score {
    private final IntegerProperty score = new SimpleIntegerProperty(0);

    /**
     * Returns the score property for JavaFX binding.
     *
     * @return the {@link IntegerProperty} representing the score
     */
    public IntegerProperty scoreProperty() {
        return score;
    }

    /**
     * Gets the current score value.
     *
     * @return the current score as an integer
     */
    public int getValue() {
        return score.get();
    }

    /**
     * Adds points to the current score.
     *
     * @param points the number of points to add (must be non-negative)
     * @throws IllegalArgumentException if points is negative
     */
    public void add(int points) {
        if (points < 0) {
            
            throw new IllegalArgumentException("Cannot add negative points");
        }
        score.setValue(score.getValue() + points);
    }

    /**
     * Resets the score to zero.
     */
    public void reset() {
        score.setValue(0);
    }
}
