package com.comp2042.tetris.domain.scoring;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

import javafx.beans.property.IntegerProperty;

class ScoreManagerTest {

    @Test
    void scorePropertyReflectsAdds() {
        ScoreManager manager = new ScoreManager();
        IntegerProperty scoreProperty = manager.scoreProperty();

        manager.add(7);
        manager.add(3);

        // UI bindings should see the same total as the manager exposes
        assertEquals(10, scoreProperty.get());
        assertEquals(10, manager.getValue());
    }

    @Test
    void resetClearsValueAndProperty() {
        ScoreManager manager = new ScoreManager();
        manager.add(12);

        manager.reset();

        // reset() should zero both the manager API and the bound property
        assertEquals(0, manager.getValue());
        assertEquals(0, manager.scoreProperty().get());
    }

    @Test
    void scorePropertyIsStableInstance() {
        ScoreManager manager = new ScoreManager();

        // Consumers expect the property reference to stay the same
        assertSame(manager.scoreProperty(), manager.scoreProperty());
    }
}
