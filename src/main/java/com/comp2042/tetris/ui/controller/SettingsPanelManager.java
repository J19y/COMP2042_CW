package com.comp2042.tetris.ui.controller;

import com.comp2042.tetris.services.audio.MusicManager;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * Manages the settings panel overlay.
 * Handles volume slider and music toggle with animations.
 */
public class SettingsPanelManager {
    private final StackPane rootPane;
    private final StackPane settingsOverlay;
    private final Slider volumeSlider;
    private final Button musicToggleButton;
    private final Text volumeText;
    private final VBox levelSelectionContainer;

    private Rectangle settingsDim;

    public SettingsPanelManager(
            StackPane rootPane,
            StackPane settingsOverlay,
            Slider volumeSlider,
            Button musicToggleButton,
            Text volumeText,
            VBox levelSelectionContainer) {
        this.rootPane = rootPane;
        this.settingsOverlay = settingsOverlay;
        this.volumeSlider = volumeSlider;
        this.musicToggleButton = musicToggleButton;
        this.volumeText = volumeText;
        this.levelSelectionContainer = levelSelectionContainer;
    }

    
    public void setupVolumeControl() {
        if (volumeSlider != null && volumeText != null) {
            volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
                double pct = newVal.doubleValue();
                volumeText.setText((int) pct + "%");
                try {
                    MusicManager.getInstance().setMusicVolume(pct / 100.0);
                } catch (Exception ignored) {
                }
            });
        }
    }

    
    public void toggleSettings() {
        if (settingsOverlay != null) {
            boolean visible = settingsOverlay.isVisible();
            if (visible) {
                closeSettings();
            } else {
                openSettings();
            }
        }
    }

    
    public void closeSettings() {
        if (settingsOverlay != null && settingsOverlay.isVisible()) {
            FadeTransition fadeOut = new FadeTransition(Duration.millis(300), settingsOverlay);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.setOnFinished(e -> settingsOverlay.setVisible(false));
            fadeOut.play();

            if (settingsDim != null && rootPane.getChildren().contains(settingsDim)) {
                rootPane.getChildren().remove(settingsDim);
            }

            if (levelSelectionContainer != null && levelSelectionContainer.isVisible()) {
                levelSelectionContainer.setEffect(null);
                levelSelectionContainer.setOpacity(1.0);
            }
        }
    }

    
    private void openSettings() {
        
        if (levelSelectionContainer != null && levelSelectionContainer.isVisible()) {
            if (settingsDim == null) {
                settingsDim = new Rectangle();
                settingsDim.setFill(javafx.scene.paint.Color.web("#000000", 0.35));
                settingsDim.widthProperty().bind(rootPane.widthProperty());
                settingsDim.heightProperty().bind(rootPane.heightProperty());
                settingsDim.setOnMouseClicked(ev -> closeSettings());
            }

            if (!rootPane.getChildren().contains(settingsDim)) {
                rootPane.getChildren().add(settingsDim);
            }

            if (rootPane.getChildren().contains(settingsOverlay)) {
                rootPane.getChildren().remove(settingsOverlay);
            }
            rootPane.getChildren().add(settingsOverlay);

            levelSelectionContainer.setEffect(new GaussianBlur(8));
            levelSelectionContainer.setOpacity(0.6);
        }

        
        settingsOverlay.setVisible(true);
        settingsOverlay.setOpacity(0);
        settingsOverlay.setScaleX(0.5);
        settingsOverlay.setScaleY(0.5);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), settingsOverlay);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);

        Timeline bounce = new Timeline(
                new KeyFrame(Duration.millis(0),
                        new KeyValue(settingsOverlay.scaleXProperty(), 0.5),
                        new KeyValue(settingsOverlay.scaleYProperty(), 0.5)
                ),
                new KeyFrame(Duration.millis(200),
                        new KeyValue(settingsOverlay.scaleXProperty(), 1.1)
                ),
                new KeyFrame(Duration.millis(200),
                        new KeyValue(settingsOverlay.scaleYProperty(), 1.1)
                ),
                new KeyFrame(Duration.millis(400),
                        new KeyValue(settingsOverlay.scaleXProperty(), 1.0, javafx.animation.Interpolator.EASE_OUT)
                ),
                new KeyFrame(Duration.millis(400),
                        new KeyValue(settingsOverlay.scaleYProperty(), 1.0, javafx.animation.Interpolator.EASE_OUT)
                )
        );

        javafx.animation.ParallelTransition popUp = new javafx.animation.ParallelTransition(fadeIn, bounce);
        popUp.play();
    }

    
    public void toggleMusic() {
        if (musicToggleButton != null) {
            String currentText = musicToggleButton.getText();
            if ("ON".equals(currentText)) {
                musicToggleButton.setText("OFF");
                musicToggleButton.setGraphic(null);

                try {
                    MusicManager.getInstance().setMusicEnabled(false);
                } catch (Exception ignored) {
                }
                try {
                    MusicManager.getInstance().playSfx("/audio/ButtonClickingEffect.mp3");
                } catch (Exception ignored) {
                }
            } else {
                musicToggleButton.setText("ON");
                musicToggleButton.setGraphic(null);

                try {
                    MusicManager.getInstance().setMusicEnabled(true);
                } catch (Exception ignored) {
                }
                try {
                    MusicManager.getInstance().playSfx("/audio/ButtonClickingEffect.mp3");
                } catch (Exception ignored) {
                }
            }
        }
    }
}



