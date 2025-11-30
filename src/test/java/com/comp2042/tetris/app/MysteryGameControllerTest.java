package com.comp2042.tetris.app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.comp2042.tetris.mechanics.board.GameView;

import javafx.beans.property.IntegerProperty;
import org.junit.jupiter.api.Test;

final class MysteryGameControllerTest {

    private static final class SpyGameView implements GameView {
        IntegerProperty boundLevel;

        @Override
        public void initGameView(int[][] boardMatrix, com.comp2042.tetris.domain.model.ViewData activeBrickData) {
            
        }

        @Override
        public void refreshGameBackground(int[][] boardMatrix) {
            
        }

        @Override
        public void bindScore(IntegerProperty scoreProperty) {
            
        }

        @Override
        public void setInputHandlers(com.comp2042.tetris.ui.input.InputActionHandler inputActionHandler, com.comp2042.tetris.ui.input.DropInput dropInput, com.comp2042.tetris.app.CreateNewGame gameLifecycle) {
            
        }

        @Override
        public void gameOver() {
            
        }

        @Override
        public void setRemainingTime(int seconds) { }

        @Override
        public void acceptShowResult(com.comp2042.tetris.domain.model.ShowResult result) { }

        @Override
        public void settleActiveBrick(Runnable onFinished) { if (onFinished != null) onFinished.run(); }

        @Override
        public void bindLevel(IntegerProperty levelProperty) {
            this.boundLevel = levelProperty;
        }
    }

    @Test
    void constructorBindsLevelProperty() {
        SpyGameView view = new SpyGameView();
        MysteryGameController c = new MysteryGameController(view);
        assertNotNull(view.boundLevel, "Mystery mode should bind a level property to the view");
        assertEquals(1, view.boundLevel.get(), "Initial level should be 1 when Mystery mode starts");
    }

    @Test
    void speedMultiplierDoesNotAffectDisplayedLevel() throws Exception {
        SpyGameView view = new SpyGameView();
        MysteryGameController controller = new MysteryGameController(view);

        
        assertEquals(1, view.boundLevel.get());

        
        java.lang.reflect.Field sm = MysteryGameController.class.getDeclaredField("speedMultiplier");
        sm.setAccessible(true);
        sm.setInt(controller, 20);
        assertEquals(1, view.boundLevel.get(), "Displayed level should remain unchanged when speed multiplier spikes");
    }
}

