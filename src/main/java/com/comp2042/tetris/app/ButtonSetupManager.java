package com.comp2042.tetris.app;

import com.comp2042.tetris.services.audio.MusicManager;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.util.Duration;


public class ButtonSetupManager {
    private final Button playButton;
    private final Button quitButton;
    private final Button controlsButton;
    private final Button settingsButton;
    private final StackPane controlLight;
    private final ImageView controlLightImage;
    private final javafx.scene.Group controlLightVector;

    public ButtonSetupManager(
            Button playButton,
            Button quitButton,
            Button controlsButton,
            Button settingsButton,
            StackPane controlLight,
            ImageView controlLightImage,
            javafx.scene.Group controlLightVector) {
        this.playButton = playButton;
        this.quitButton = quitButton;
        this.controlsButton = controlsButton;
        this.settingsButton = settingsButton;
        this.controlLight = controlLight;
        this.controlLightImage = controlLightImage;
        this.controlLightVector = controlLightVector;
    }

    
    public void setupButtons() {
        Font buttonFont = loadButtonFont();

        if (buttonFont != null) {
            if (playButton != null) playButton.setFont(buttonFont);
            if (quitButton != null) quitButton.setFont(buttonFont);
        }

        if (playButton != null) playButton.setTranslateY(10);
        if (quitButton != null) quitButton.setTranslateY(10);

        setupButtonAnimation(playButton);
        setupButtonAnimation(quitButton);
    }

    
    public void setupButtonAnimation(Button button) {
        if (button == null) return;
        button.setCursor(Cursor.HAND);

        
        Color effectColor = (button == playButton) ? Color.web("#00ff99", 0.95) : Color.web("#ff6b6b", 0.95);
        
        DropShadow glow = new DropShadow();
        glow.setColor(effectColor);
        glow.setRadius(8);
        glow.setSpread(0.28);
        glow.setOffsetX(0);
        glow.setOffsetY(0);

        
        ScaleTransition scaleUp = new ScaleTransition(Duration.millis(160), button);
        scaleUp.setToX(1.06);
        scaleUp.setToY(1.06);
        scaleUp.setInterpolator(javafx.animation.Interpolator.EASE_OUT);

        ScaleTransition scaleDown = new ScaleTransition(Duration.millis(120), button);
        scaleDown.setToX(1.0);
        scaleDown.setToY(1.0);
        scaleDown.setInterpolator(javafx.animation.Interpolator.EASE_IN);

        
        Timeline pulse = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(glow.radiusProperty(), 8.0)),
                new KeyFrame(Duration.millis(180), new KeyValue(glow.radiusProperty(), 20.0)),
                new KeyFrame(Duration.millis(360), new KeyValue(glow.radiusProperty(), 10.0))
        );
        pulse.setCycleCount(javafx.animation.Animation.INDEFINITE);

        
        button.setOnMouseEntered(e -> {
            try {
                MusicManager.getInstance().playSfx("/audio/RotationSoundEffect.mp3", 1.4);
            } catch (Exception ignored) {
            }
            button.setEffect(glow);
            scaleUp.playFromStart();
            pulse.play();
        });

        button.setOnMouseExited(e -> {
            pulse.stop();
            Timeline fadeOut = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(glow.radiusProperty(), glow.getRadius())),
                    new KeyFrame(Duration.millis(220), new KeyValue(glow.radiusProperty(), 0.0))
            );
            fadeOut.setOnFinished(ev -> button.setEffect(null));
            fadeOut.play();
            scaleDown.playFromStart();
        });

        
        button.setOnMousePressed(e -> {
            try {
                MusicManager.getInstance().playSfx("/audio/ButtonClickingEffect.mp3");
            } catch (Exception ignored) {
            }
            button.setScaleX(button.getScaleX() * 0.98);
            button.setScaleY(button.getScaleY() * 0.98);
        });

        button.setOnMouseReleased(e -> {
            if (button.isHover()) {
                button.setScaleX(1.06);
                button.setScaleY(1.06);
            } else {
                button.setScaleX(1.0);
                button.setScaleY(1.0);
            }
        });
    }

    
    public void setupControlLight() {
        if (controlLight == null) return;
        controlLight.setCursor(Cursor.HAND);

        if (controlLightImage != null) {
            setupControlLightImage();
        }

        if (controlLightVector != null) {
            setupControlLightVector();
        }
    }

    
    private void setupControlLightImage() {
        controlLightImage.setOnMouseEntered(e -> {
            try {
                MusicManager.getInstance().playSfx("/audio/RotationSoundEffect.mp3", 1.4);
            } catch (Exception ignored) {
            }
            
            ScaleTransition st = new ScaleTransition(Duration.millis(220), controlLightImage);
            st.setToX(1.25);
            st.setToY(1.25);
            st.setInterpolator(javafx.animation.Interpolator.EASE_OUT);
            st.play();
            controlLightImage.setEffect(new Glow(0.65));
        });

        controlLightImage.setOnMouseExited(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(160), controlLightImage);
            st.setToX(1.0);
            st.setToY(1.0);
            st.setInterpolator(javafx.animation.Interpolator.EASE_IN);
            st.play();
            controlLightImage.setEffect(null);
        });

        controlLightImage.setOnMouseClicked(e -> controlLight.fireEvent(e));

        
        Image img = controlLightImage.getImage();
        if (img == null || img.isError()) {
            controlLightImage.setVisible(false);
            if (controlLightVector != null) controlLightVector.setVisible(true);
        }
    }

    
    private void setupControlLightVector() {
        controlLightVector.setOnMouseEntered(e -> {
            try {
                MusicManager.getInstance().playSfx("/audio/RotationSoundEffect.mp3", 1.4);
            } catch (Exception ignored) {
            }
            
            ScaleTransition st = new ScaleTransition(Duration.millis(220), controlLightVector);
            st.setToX(1.15);
            st.setToY(1.15);
            st.setInterpolator(javafx.animation.Interpolator.EASE_OUT);
            st.play();
            controlLightVector.setEffect(new DropShadow(14, Color.web("#00f0d8", 0.6)));
        });

        controlLightVector.setOnMouseExited(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(160), controlLightVector);
            st.setToX(1.0);
            st.setToY(1.0);
            st.setInterpolator(javafx.animation.Interpolator.EASE_IN);
            st.play();
            controlLightVector.setEffect(null);
        });

        controlLightVector.setOnMouseClicked(e -> controlLight.fireEvent(e));
    }

    
    public void setupControlsButton() {
        if (controlsButton == null) return;
        controlsButton.setCursor(Cursor.HAND);

        try {
            
            ImageView bulbImage = new ImageView(
                    new Image(getClass().getResourceAsStream("/originallight-bulb-png.png")));
            bulbImage.setFitWidth(65);
            bulbImage.setFitHeight(65);
            bulbImage.setPreserveRatio(true);

            setupControlsButtonEffects(bulbImage);
            controlsButton.setGraphic(bulbImage);
        } catch (Exception e) {
            
            SVGPath bulb = new SVGPath();
            bulb.setContent("M12 2C13.1 2 14 2.9 14 4V5H16V7H13V19C13 20.1 12.1 21 11 21C9.9 21 9 20.1 9 19V7H6V5H8V4C8 2.9 8.9 2 10 2H12Z M10 4V5H14V4H10Z");
            bulb.setFill(javafx.scene.paint.Color.TRANSPARENT);
            bulb.setStroke(Color.web("#ffff00"));
            bulb.setStrokeWidth(2);
            bulb.setScaleX(1.3);
            bulb.setScaleY(1.3);

            setupControlsButtonEffects(bulb);
            controlsButton.setGraphic(bulb);
        }

        controlsButton.setText("");
        controlsButton.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
    }

    
    private void setupControlsButtonEffects(javafx.scene.Node graphic) {
        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.YELLOW);
        dropShadow.setRadius(5);
        dropShadow.setSpread(0.2);
        Glow glow = new Glow(0.2);
        dropShadow.setInput(glow);
        graphic.setEffect(dropShadow);

        Timeline flickerTimeline = new Timeline(
                new KeyFrame(Duration.millis(0),
                        new KeyValue(glow.levelProperty(), 1.0),
                        new KeyValue(dropShadow.radiusProperty(), 15)),
                new KeyFrame(Duration.millis(100),
                        new KeyValue(glow.levelProperty(), 0.8),
                        new KeyValue(dropShadow.radiusProperty(), 12)),
                new KeyFrame(Duration.millis(200),
                        new KeyValue(glow.levelProperty(), 1.0),
                        new KeyValue(dropShadow.radiusProperty(), 15)),
                new KeyFrame(Duration.millis(300),
                        new KeyValue(glow.levelProperty(), 0.9),
                        new KeyValue(dropShadow.radiusProperty(), 13)),
                new KeyFrame(Duration.millis(400),
                        new KeyValue(glow.levelProperty(), 1.0),
                        new KeyValue(dropShadow.radiusProperty(), 15))
        );
        flickerTimeline.setCycleCount(javafx.animation.Animation.INDEFINITE);

        controlsButton.setOnMouseEntered(event -> {
            try {
                MusicManager.getInstance().playSfx("/audio/RotationSoundEffect.mp3", 1.4);
            } catch (Exception ignored) {
            }
            glow.setLevel(1.0);
            dropShadow.setRadius(15);
            flickerTimeline.play();
        });

        controlsButton.setOnMouseExited(event -> {
            flickerTimeline.stop();
            Timeline glowOut = new Timeline(
                    new KeyFrame(Duration.ZERO,
                            new KeyValue(glow.levelProperty(), glow.getLevel()),
                            new KeyValue(dropShadow.radiusProperty(), dropShadow.getRadius())),
                    new KeyFrame(Duration.millis(250),
                            new KeyValue(glow.levelProperty(), 0.2),
                            new KeyValue(dropShadow.radiusProperty(), 5))
            );
            glowOut.play();
        });

        controlsButton.setOnMousePressed(ev -> {
            try {
                MusicManager.getInstance().playSfx("/audio/ButtonClickingEffect.mp3");
            } catch (Exception ignored) {
            }
        });
    }

    
    public void setupSettingsButton() {
        if (settingsButton == null) return;
        settingsButton.setCursor(Cursor.HAND);

        try {
            ImageView speakerImage = new ImageView(
                    new Image(getClass().getResourceAsStream("/newWhiteSpeaker.png")));
            speakerImage.setFitWidth(45);
            speakerImage.setFitHeight(45);
            speakerImage.setPreserveRatio(true);
            speakerImage.setTranslateX(-8);

            DropShadow cyanGlow = new DropShadow();
            cyanGlow.setColor(Color.web("#008B8B"));
            cyanGlow.setRadius(8);
            cyanGlow.setSpread(0.15);
            speakerImage.setEffect(cyanGlow);
            settingsButton.setGraphic(speakerImage);
        } catch (Exception e) {
            
            settingsButton.setText("AUDIO");
            settingsButton.setStyle(
                    "-fx-font-size: 12px; -fx-text-fill: #00ffff; -fx-background-color: transparent; " +
                    "-fx-border-color: transparent; -fx-font-weight: bold;");
        }

        settingsButton.setText("");
        settingsButton.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
    }

    
    private Font loadButtonFont() {
        try {
            return Font.loadFont(getClass().getResourceAsStream("/fonts/PressStart2P-vaV7.ttf"), 16);
        } catch (Exception e) {
            return null;
        }
    }
}



