package com.comp2042.tetris.ui.controller;

import com.comp2042.tetris.services.audio.MusicManager;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;


public class LevelSelectionManager {
    private final StackPane rootPane;
    private final javafx.scene.layout.HBox titleContainer;
    private final javafx.scene.text.Text yearText;

    private VBox levelSelectionContainer;
    private String selectedGameMode = null;
    private Runnable onModeSelected;
    
    private final javafx.scene.Node[] nodesToToggle;

    public LevelSelectionManager(
            StackPane rootPane,
            HBox titleContainer,
            Text yearText,
            javafx.scene.Node... nodesToToggle) {
        this.rootPane = rootPane;
        this.titleContainer = titleContainer;
        this.yearText = yearText;
        this.nodesToToggle = nodesToToggle == null ? new javafx.scene.Node[0] : nodesToToggle;
    }

    
    public void setOnModeSelected(Runnable callback) {
        this.onModeSelected = callback;
    }

    
    public String getSelectedGameMode() {
        return selectedGameMode;
    }

    
    public void showLevelSelection() {
        
        titleContainer.setVisible(false);
        yearText.setVisible(false);

        
        if (levelSelectionContainer == null) {
            levelSelectionContainer = createLevelSelectionPanel();
            levelSelectionContainer.setMaxWidth(480);
            levelSelectionContainer.setMaxHeight(520);
            levelSelectionContainer.setOpacity(0);
            levelSelectionContainer.setVisible(false);

            rootPane.getChildren().add(levelSelectionContainer);
            StackPane.setAlignment(levelSelectionContainer, Pos.CENTER);
        }

        
        if (nodesToToggle != null) {
            for (javafx.scene.Node n : nodesToToggle) {
                if (n != null) {
                    n.setVisible(false);
                    n.setDisable(true);
                    n.setMouseTransparent(true);
                }
            }
        }

        
        levelSelectionContainer.setVisible(true);
        levelSelectionContainer.setOpacity(1.0);

        
        int delay = 0;
        for (Node child : levelSelectionContainer.getChildren()) {
            child.setOpacity(0);
            child.setTranslateY(20);

            FadeTransition ft = new FadeTransition(Duration.millis(400), child);
            ft.setToValue(1.0);

            TranslateTransition tt = new TranslateTransition(Duration.millis(400), child);
            tt.setToY(0);
            tt.setInterpolator(javafx.animation.Interpolator.EASE_OUT);

            ParallelTransition entry = new ParallelTransition(ft, tt);
            entry.setDelay(Duration.millis(delay));
            entry.play();

            delay += 100;
        }
    }

    
    public void hideLevelSelection() {
        if (levelSelectionContainer != null) {
            FadeTransition fadeOut = new FadeTransition(Duration.millis(300), levelSelectionContainer);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.setOnFinished(e -> {
                levelSelectionContainer.setVisible(false);

                if (rootPane.getChildren().contains(levelSelectionContainer)) {
                    rootPane.getChildren().remove(levelSelectionContainer);
                    levelSelectionContainer = null;
                }

                titleContainer.setVisible(true);
                yearText.setVisible(true);

                
                if (nodesToToggle != null) {
                    for (javafx.scene.Node n : nodesToToggle) {
                        if (n != null) {
                            n.setVisible(true);
                            n.setDisable(false);
                            n.setMouseTransparent(false);
                        }
                    }
                }
            });
            fadeOut.play();
        }
    }

    
    private VBox createLevelSelectionPanel() {
        VBox container = new VBox();
        container.setAlignment(Pos.CENTER);
        container.setSpacing(20);

        
        Text levelTitle = new Text("SELECT MODE");
        levelTitle.setFill(Color.web("#00ff99"));
        levelTitle.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");
        levelTitle.setEffect(new DropShadow(12, Color.web("#00ff99", 0.5)));

        
        Button classicButton = createLevelButton("CLASSIC", "CLASSIC");
        Button rushButton = createLevelButton("RUSH", "RUSH");
        Button mysteryButton = createLevelButton("MYSTERY", "MYSTERY");

        
        Button backButton = new Button("BACK");
        backButton.setStyle(
                "-fx-font-family: 'Press Start 2P', Arial; " +
                "-fx-font-size: 11px; " +
                "-fx-padding: 8 30 8 30; " +
                "-fx-min-width: 130px; " +
                "-fx-border-color: rgba(180,180,180,0.6); " +
                "-fx-border-width: 2px; " +
                "-fx-background-color: linear-gradient(to bottom right, rgba(120,120,120,0.08), rgba(80,80,80,0.04)); " +
                "-fx-text-fill: #e0e0e0; " +
                "-fx-background-radius: 20; " +
                "-fx-border-radius: 20; " +
                "-fx-effect: dropshadow(gaussian, rgba(120,120,120,0.2), 10, 0.28, 0, 0);"
        );
        
        DropShadow backHoverGlow = new DropShadow();
        backHoverGlow.setColor(Color.web("rgba(180,180,180,0.4)"));
        backHoverGlow.setRadius(8);
        backHoverGlow.setSpread(0.28);

        ScaleTransition backScaleUp = new ScaleTransition(Duration.millis(160), backButton);
        backScaleUp.setToX(1.05);
        backScaleUp.setToY(1.05);

        ScaleTransition backScaleDown = new ScaleTransition(Duration.millis(120), backButton);
        backScaleDown.setToX(1.0);
        backScaleDown.setToY(1.0);

        backButton.setOnMouseEntered(e -> {
            backButton.setEffect(backHoverGlow);
            backScaleUp.playFromStart();
        });

        backButton.setOnMouseExited(e -> {
            backButton.setEffect(null);
            backScaleDown.playFromStart();
        });

        backButton.setOnAction(e -> hideLevelSelection());

        container.getChildren().addAll(levelTitle, classicButton, rushButton, mysteryButton);

        HBox backButtonBox = new HBox();
        backButtonBox.setAlignment(Pos.CENTER);
        backButtonBox.setPadding(new Insets(30, 0, 0, 0));
        backButtonBox.getChildren().add(backButton);
        container.getChildren().add(backButtonBox);

        return container;
    }

    
    private Button createLevelButton(String text, String mode) {
        Button button = new Button(text);

        
        String borderColor;
        String textColor;
        String gradientColor1;
        String gradientColor2;
        String shadowColor;

        switch (mode) {
            case "CLASSIC" -> {
                borderColor = "rgba(100,180,255,0.95)";
                textColor = "#e3f2ff";
                gradientColor1 = "rgba(100,180,255,0.12)";
                gradientColor2 = "rgba(70,140,220,0.06)";
                shadowColor = "rgba(100,180,255,0.25)";
            }
            case "RUSH" -> {
                borderColor = "rgba(100,230,150,0.95)";
                textColor = "#e7ffde";
                gradientColor1 = "rgba(100,230,150,0.12)";
                gradientColor2 = "rgba(70,190,120,0.06)";
                shadowColor = "rgba(100,230,150,0.25)";
            }
            case "MYSTERY" -> {
                borderColor = "rgba(200,120,255,0.95)";
                textColor = "#f3e5ff";
                gradientColor1 = "rgba(200,120,255,0.12)";
                gradientColor2 = "rgba(170,90,220,0.06)";
                shadowColor = "rgba(200,120,255,0.25)";
            }
            default -> {
                borderColor = "rgba(100,180,255,0.95)";
                textColor = "#e3f2ff";
                gradientColor1 = "rgba(100,180,255,0.12)";
                gradientColor2 = "rgba(70,140,220,0.06)";
                shadowColor = "rgba(100,180,255,0.25)";
            }
        }

        button.setStyle(
                "-fx-font-family: 'Press Start 2P', Arial; " +
                "-fx-font-size: 16px; " +
                "-fx-padding: 12 44 12 44; " +
                "-fx-min-width: 200px; " +
                "-fx-border-color: " + borderColor + "; " +
                "-fx-border-width: 2px; " +
                "-fx-background-color: linear-gradient(to bottom right, " + gradientColor1 + ", " + gradientColor2 + "); " +
                "-fx-text-fill: " + textColor + "; " +
                "-fx-background-radius: 20; " +
                "-fx-border-radius: 20; " +
                "-fx-effect: dropshadow(gaussian, " + shadowColor + ", 12, 0.28, 0, 0);"
        );

        button.setAlignment(Pos.CENTER);
        button.setWrapText(true);
        button.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        
        String tipText = switch (mode) {
            case "CLASSIC" -> "Standard rules with steady progression and traditional scoring – focus on precise stacking to build high scores.";
            case "RUSH" -> "A timed challenge: clear as many lines as you can before the clock runs out. Faster gravity increases the pressure.";
            case "MYSTERY" -> "Unpredictable piece sequences and increased speed create a chaotic, high-intensity experience for skilled players.";
            default -> "Standard rules with steady progression and traditional scoring.";
        };

        Tooltip tooltip = new Tooltip(tipText);
        tooltip.setWrapText(true);
        tooltip.setMaxWidth(260);
        tooltip.setShowDelay(javafx.util.Duration.millis(100));
        tooltip.setHideDelay(javafx.util.Duration.millis(0));
        tooltip.setShowDuration(javafx.util.Duration.millis(10000));
        tooltip.setStyle(
                "-fx-background-color: rgba(6,6,8,0.86); " +
                "-fx-text-fill: #e6ffff; " +
                "-fx-font-family: 'Press Start 2P', Arial; " +
                "-fx-font-size: 10px; " +
                "-fx-padding: 8px; " +
                "-fx-border-color: rgba(0,255,220,0.16); " +
                "-fx-border-width: 1px; " +
                "-fx-border-radius: 8px; " +
                "-fx-background-radius: 8px; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,255,220,0.12), 10, 0.2, 0, 0);"
        );
        button.setTooltip(tooltip);

        
        Color effectColor = Color.web(shadowColor);

        DropShadow hoverGlow = new DropShadow();
        hoverGlow.setColor(effectColor);
        hoverGlow.setRadius(8);
        hoverGlow.setSpread(0.28);

        ScaleTransition scaleUp = new ScaleTransition(Duration.millis(160), button);
        scaleUp.setToX(1.06);
        scaleUp.setToY(1.06);

        ScaleTransition scaleDown = new ScaleTransition(Duration.millis(120), button);
        scaleDown.setToX(1.0);
        scaleDown.setToY(1.0);

        Timeline pulse = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(hoverGlow.radiusProperty(), 8.0)),
                new KeyFrame(Duration.millis(180), new KeyValue(hoverGlow.radiusProperty(), 20.0)),
                new KeyFrame(Duration.millis(360), new KeyValue(hoverGlow.radiusProperty(), 10.0))
        );
        pulse.setCycleCount(javafx.animation.Animation.INDEFINITE);

        button.setOnMouseEntered(e -> {
            try { MusicManager.getInstance().playSfx("/audio/RotationSoundEffect.mp3", 1.4); } catch (Exception ignored) {}
            button.setEffect(hoverGlow);
            scaleUp.playFromStart();
            pulse.play();
        });

        button.setOnMouseExited(e -> {
            pulse.stop();
            Timeline fadeOut = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(hoverGlow.radiusProperty(), hoverGlow.getRadius())),
                    new KeyFrame(Duration.millis(220), new KeyValue(hoverGlow.radiusProperty(), 0.0))
            );
            fadeOut.setOnFinished(ev -> button.setEffect(null));
            fadeOut.play();
            scaleDown.playFromStart();
        });

        button.setOnMousePressed(e -> {
            try { MusicManager.getInstance().playSfx("/audio/ButtonClickingEffect.mp3"); } catch (Exception ignored) {}
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

        
        button.setOnAction(e -> {
            selectedGameMode = mode;
            if (onModeSelected != null) {
                onModeSelected.run();
            }
        });

        return button;
    }
}



