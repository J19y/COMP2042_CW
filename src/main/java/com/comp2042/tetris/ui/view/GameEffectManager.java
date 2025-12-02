package com.comp2042.tetris.ui.view;

import com.comp2042.tetris.application.session.GameLoopController;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.Glow;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.logging.Level;
import java.util.logging.Logger;

public class GameEffectManager {

    private static final Logger LOGGER = Logger.getLogger(GameEffectManager.class.getName());

    private final Pane gamePanel;
    private final Pane boardClipContainer;
    private final Text levelText;
    private final Text scoreText;
    private final Duration baseTickInterval;

    private GameLoopController gameLoopController;

    private Rectangle fogOverlay;
    private Rectangle gravityOverlay;
    private boolean gravityIntervalActive;
    private Duration savedInterval;
    private Timeline scoreGlowTimeline;

    public GameEffectManager(Pane gamePanel, Pane boardClipContainer, Text levelText, Text scoreText, Duration baseTickInterval) {
        this.gamePanel = gamePanel;
        this.boardClipContainer = boardClipContainer;
        this.levelText = levelText;
        this.scoreText = scoreText;
        this.baseTickInterval = baseTickInterval;
    }

    public void setGameLoopController(GameLoopController gameLoopController) {
        this.gameLoopController = gameLoopController;
    }

    public void playEarthquakeAnimation() {
        if (gamePanel == null) {
            return;
        }
        try {
            Timeline shake = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(gamePanel.translateXProperty(), 0)),
                new KeyFrame(Duration.millis(40), new KeyValue(gamePanel.translateXProperty(), -10)),
                new KeyFrame(Duration.millis(80), new KeyValue(gamePanel.translateXProperty(), 10)),
                new KeyFrame(Duration.millis(120), new KeyValue(gamePanel.translateXProperty(), -6)),
                new KeyFrame(Duration.millis(160), new KeyValue(gamePanel.translateXProperty(), 6)),
                new KeyFrame(Duration.millis(200), new KeyValue(gamePanel.translateXProperty(), 0))
            );
            shake.play();

            Glow glow = new Glow(0.0);
            gamePanel.setEffect(glow);
            Timeline glowTimeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(glow.levelProperty(), 0.0)),
                new KeyFrame(Duration.millis(60), new KeyValue(glow.levelProperty(), 0.9)),
                new KeyFrame(Duration.millis(260), new KeyValue(glow.levelProperty(), 0.0))
            );
            glowTimeline.setOnFinished(e -> gamePanel.setEffect(null));
            glowTimeline.play();
        } catch (Exception ignored) {
            LOGGER.log(Level.FINE, "Unable to run earthquake animation", ignored);
        }
    }

    public void animateLevelIncrement() {
        if (levelText == null) {
            return;
        }
        try {
            Glow glow = new Glow(0.0);
            javafx.scene.effect.Effect prevEffect = levelText.getEffect();
            levelText.setEffect(glow);
            Timeline pulse = new Timeline(
                new KeyFrame(Duration.ZERO,
                    new KeyValue(levelText.scaleXProperty(), 1.0),
                    new KeyValue(levelText.scaleYProperty(), 1.0),
                    new KeyValue(glow.levelProperty(), 0.0)
                ),
                new KeyFrame(Duration.millis(150),
                    new KeyValue(levelText.scaleXProperty(), 1.2),
                    new KeyValue(levelText.scaleYProperty(), 1.2),
                    new KeyValue(glow.levelProperty(), 0.8)
                ),
                new KeyFrame(Duration.millis(300),
                    new KeyValue(levelText.scaleXProperty(), 1.0),
                    new KeyValue(levelText.scaleYProperty(), 1.0),
                    new KeyValue(glow.levelProperty(), 0.0)
                )
            );
            pulse.setOnFinished(e -> levelText.setEffect(prevEffect));
            pulse.play();
        } catch (Exception ignored) {
            LOGGER.log(Level.FINE, "Unable to animate level increment", ignored);
        }
    }

    public void showFogEffect(int seconds) {
        if (boardClipContainer == null) {
            return;
        }
        try {
            if (fogOverlay == null) {
                fogOverlay = new Rectangle();
                fogOverlay.setManaged(false);
                fogOverlay.setMouseTransparent(true);
                fogOverlay.setFill(Color.web("#ffffff", 0.85));
                GaussianBlur blur = new GaussianBlur(0);
                fogOverlay.setEffect(blur);
            }
            fogOverlay.setWidth(boardClipContainer.getWidth());
            fogOverlay.setHeight(boardClipContainer.getHeight());
            fogOverlay.setTranslateX(boardClipContainer.getLayoutX());
            fogOverlay.setTranslateY(boardClipContainer.getLayoutY());
            if (!boardClipContainer.getChildren().contains(fogOverlay)) {
                boardClipContainer.getChildren().add(fogOverlay);
            }
            Timeline in = new Timeline(
                new KeyFrame(Duration.ZERO,
                    new KeyValue(fogOverlay.opacityProperty(), 0.0),
                    new KeyValue(((GaussianBlur) fogOverlay.getEffect()).radiusProperty(), 0)
                ),
                new KeyFrame(Duration.millis(300),
                    new KeyValue(fogOverlay.opacityProperty(), 1.0),
                    new KeyValue(((GaussianBlur) fogOverlay.getEffect()).radiusProperty(), 12)
                )
            );
            in.play();
            PauseTransition wait = new PauseTransition(Duration.seconds(seconds));
            wait.setOnFinished(ev -> {
                Timeline out = new Timeline(
                    new KeyFrame(Duration.ZERO,
                        new KeyValue(fogOverlay.opacityProperty(), fogOverlay.getOpacity())
                    ),
                    new KeyFrame(Duration.millis(400),
                        new KeyValue(fogOverlay.opacityProperty(), 0.0),
                        new KeyValue(((GaussianBlur) fogOverlay.getEffect()).radiusProperty(), 0)
                    )
                );
                out.setOnFinished(e -> boardClipContainer.getChildren().remove(fogOverlay));
                out.play();
            });
            wait.play();
        } catch (Exception ignored) {
            LOGGER.log(Level.FINE, "Unable to show fog effect", ignored);
        }
    }

    public void showHeavyGravityEffect(int seconds) {
        if (boardClipContainer == null || gameLoopController == null) {
            return;
        }
        try {
            if (gravityOverlay == null) {
                gravityOverlay = new Rectangle();
                gravityOverlay.setManaged(false);
                gravityOverlay.setMouseTransparent(true);
                gravityOverlay.setFill(Color.web("#ff1a1a", 0.0));
            }
            gravityOverlay.setWidth(boardClipContainer.getWidth());
            gravityOverlay.setHeight(boardClipContainer.getHeight());
            gravityOverlay.setTranslateX(boardClipContainer.getLayoutX());
            gravityOverlay.setTranslateY(boardClipContainer.getLayoutY());
            if (!boardClipContainer.getChildren().contains(gravityOverlay)) {
                boardClipContainer.getChildren().add(gravityOverlay);
            }
            Timeline pulse = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(gravityOverlay.opacityProperty(), 0.0)),
                new KeyFrame(Duration.millis(60), new KeyValue(gravityOverlay.opacityProperty(), 0.85)),
                new KeyFrame(Duration.millis(200), new KeyValue(gravityOverlay.opacityProperty(), 0.6)),
                new KeyFrame(Duration.millis(360), new KeyValue(gravityOverlay.opacityProperty(), 0.95))
            );
            pulse.setCycleCount(Animation.INDEFINITE);
            pulse.play();
            try {
                if (!gravityIntervalActive) {
                    savedInterval = gameLoopController.getInterval();
                    double baseMs = (savedInterval == null) ? baseTickInterval.toMillis() : savedInterval.toMillis();
                    double newMs = Math.max(40.0, baseMs / 4.0);
                    gameLoopController.setInterval(Duration.millis(newMs));
                    gravityIntervalActive = true;
                }
            } catch (Exception ignored) {
                LOGGER.log(Level.FINE, "Unable to tweak loop interval", ignored);
            }
            Timeline heavyShake = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(gamePanel.translateYProperty(), 0)),
                new KeyFrame(Duration.millis(80), new KeyValue(gamePanel.translateYProperty(), 6)),
                new KeyFrame(Duration.millis(160), new KeyValue(gamePanel.translateYProperty(), -6)),
                new KeyFrame(Duration.millis(240), new KeyValue(gamePanel.translateYProperty(), 3)),
                new KeyFrame(Duration.millis(320), new KeyValue(gamePanel.translateYProperty(), 0))
            );
            heavyShake.setCycleCount(Animation.INDEFINITE);
            heavyShake.play();
            PauseTransition wait = new PauseTransition(Duration.seconds(seconds));
            wait.setOnFinished(ev -> {
                pulse.stop();
                heavyShake.stop();
                if (gamePanel != null) {
                    gamePanel.setTranslateY(0);
                }
                if (gravityIntervalActive) {
                    try {
                        if (savedInterval != null) {
                            gameLoopController.setInterval(savedInterval);
                        }
                    } catch (Exception ignored) {
                        LOGGER.log(Level.FINE, "Unable to restore loop interval", ignored);
                    }
                    gravityIntervalActive = false;
                    savedInterval = null;
                }
                Timeline fade = new Timeline(
                    new KeyFrame(Duration.ZERO,
                        new KeyValue(gravityOverlay.opacityProperty(), gravityOverlay.getOpacity())
                    ),
                    new KeyFrame(Duration.millis(360), new KeyValue(gravityOverlay.opacityProperty(), 0.0))
                );
                fade.setOnFinished(e -> boardClipContainer.getChildren().remove(gravityOverlay));
                fade.play();
            });
            wait.play();
        } catch (Exception ignored) {
            LOGGER.log(Level.FINE, "Unable to show heavy gravity effect", ignored);
        }
    }

    public void playScoreGlow() {
        if (scoreText == null) {
            return;
        }
        if (scoreGlowTimeline != null) {
            scoreGlowTimeline.stop();
        }
        DropShadow ds = new DropShadow();
        ds.setColor(Color.CYAN);
        ds.setRadius(0);
        ds.setSpread(0.6);
        scoreText.setEffect(ds);
        scoreGlowTimeline = new Timeline(
            new KeyFrame(Duration.ZERO,
                new KeyValue(ds.radiusProperty(), 0),
                new KeyValue(scoreText.scaleXProperty(), 1.0),
                new KeyValue(scoreText.scaleYProperty(), 1.0)
            ),
            new KeyFrame(Duration.millis(120),
                new KeyValue(ds.radiusProperty(), 18),
                new KeyValue(scoreText.scaleXProperty(), 1.12),
                new KeyValue(scoreText.scaleYProperty(), 1.12)
            ),
            new KeyFrame(Duration.millis(360),
                new KeyValue(ds.radiusProperty(), 0),
                new KeyValue(scoreText.scaleXProperty(), 1.0),
                new KeyValue(scoreText.scaleYProperty(), 1.0)
            )
        );
        scoreGlowTimeline.setOnFinished(e -> scoreText.setEffect(null));
        scoreGlowTimeline.play();
    }
}
