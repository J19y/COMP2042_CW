package com.comp2042.tetris.ui.view;

import com.comp2042.tetris.engine.state.GameStateManager;

import javafx.animation.FadeTransition;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

/**
 * Controls the pause overlay UI with fade animations.
 * Manages pause dim effect and help container visibility.
 */
public class PauseOverlayController {

    private final Pane pauseDim;
    private final StackPane rootPane;
    private FadeTransition pauseDimTransition;
    private boolean manualPauseActive;

    public PauseOverlayController(Pane pauseDim, StackPane rootPane) {
        this.pauseDim = pauseDim;
        this.rootPane = rootPane;
        bindPauseDim();
    }

    public void bindHelpContainer(HBox helpContainer, GameStateManager stateManager) {
        if (helpContainer == null || stateManager == null) {
            return;
        }
        helpContainer.setVisible(stateManager.getCurrentState() == GameStateManager.GameState.MENU);
        stateManager.stateProperty().addListener((obs, oldVal, newVal) ->
            helpContainer.setVisible(newVal == GameStateManager.GameState.MENU)
        );
    }

    public void setManualPauseActive(boolean manualPauseActive) {
        this.manualPauseActive = manualPauseActive;
        updatePauseDimVisibility();
    }

    public boolean isManualPauseActive() {
        return manualPauseActive;
    }

    public void updatePauseDimVisibility() {
        if (pauseDim == null) {
            return;
        }
        if (pauseDimTransition != null) {
            pauseDimTransition.stop();
        }
        pauseDimTransition = new FadeTransition(Duration.millis(220), pauseDim);
        pauseDimTransition.setFromValue(pauseDim.getOpacity());
        if (manualPauseActive) {
            pauseDim.setVisible(true);
            pauseDimTransition.setToValue(0.55);
            pauseDimTransition.setOnFinished(null);
        } else {
            if (!pauseDim.isVisible()) {
                pauseDim.setVisible(true);
            }
            pauseDimTransition.setToValue(0);
            pauseDimTransition.setOnFinished(e -> pauseDim.setVisible(false));
        }
        pauseDimTransition.play();
    }

    private void bindPauseDim() {
        if (pauseDim == null || rootPane == null) {
            return;
        }
        pauseDim.prefWidthProperty().bind(rootPane.widthProperty());
        pauseDim.prefHeightProperty().bind(rootPane.heightProperty());
        pauseDim.setOpacity(0);
        pauseDim.setVisible(false);
    }
}
