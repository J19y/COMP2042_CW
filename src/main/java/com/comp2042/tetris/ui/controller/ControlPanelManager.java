package com.comp2042.tetris.ui.controller;

import com.comp2042.tetris.ui.animation.NeonFlickerEffect;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

/**
 * Manages the control panel overlay displaying keyboard controls.
 * <p>
 * Creates and animates a neon-styled control scheme display
 * showing arrow keys, spacebar and other game controls.
 * </p>
 *
 * @version 1.0
 */
public class ControlPanelManager {
    private final StackPane rootPane;
    private final javafx.scene.layout.Pane backgroundPane;
    private final javafx.scene.layout.VBox levelSelectionContainer;

    private StackPane controllerPanel;
    private javafx.scene.layout.Pane controllerDim;
    private Rectangle controllerInnerDim;
    private Timeline controllerFlicker;
   
    private javafx.scene.Node[] nodesToBlur;

    public ControlPanelManager(
            StackPane rootPane,
            javafx.scene.layout.Pane backgroundPane,
            javafx.scene.layout.VBox levelSelectionContainer) {
        this.rootPane = rootPane;
        this.backgroundPane = backgroundPane;
        this.levelSelectionContainer = levelSelectionContainer;
    }

    public void setNodesToBlur(javafx.scene.Node... nodes) {
        this.nodesToBlur = nodes == null ? new javafx.scene.Node[0] : nodes;
    }

    
    public void openControllerPanel() {
        if (controllerPanel == null || !controllerPanel.isVisible()) {
            toggleControllerPanel();
        }
    }

    
    public void toggleControllerPanel() {
        if (controllerPanel == null) {
            controllerPanel = createControllerPanel();

            controllerDim = new javafx.scene.layout.Pane();
            controllerDim.getStyleClass().add("pause-dim");
            controllerDim.setOpacity(0);
            controllerDim.setVisible(false);
            controllerDim.setMouseTransparent(false);
            controllerDim.prefWidthProperty().bind(rootPane.widthProperty());
            controllerDim.prefHeightProperty().bind(rootPane.heightProperty());
            controllerDim.setOnMouseClicked(ev -> toggleControllerPanel());

            rootPane.getChildren().add(controllerDim);
            rootPane.getChildren().add(controllerPanel);

            StackPane.setAlignment(controllerPanel, Pos.CENTER);
            controllerPanel.setTranslateX(0);
            controllerPanel.setTranslateY(0);
        }

        boolean visible = controllerPanel.isVisible();
        if (visible) {
            hidePanel();
        } else {
            showPanel();
        }
    }

    
    private void hidePanel() {
        backgroundPane.setOpacity(1.0);
        backgroundPane.setEffect(null);

        if (levelSelectionContainer != null && levelSelectionContainer.isVisible()) {
            levelSelectionContainer.setEffect(null);
            levelSelectionContainer.setOpacity(1.0);
        }

        if (nodesToBlur != null) {
            for (javafx.scene.Node n : nodesToBlur) {
                if (n != null) {
                    n.setEffect(null);
                    n.setOpacity(1.0);
                }
            }
        }

        ParallelTransition hide = new ParallelTransition();

        TranslateTransition slide = new TranslateTransition(Duration.millis(380), controllerPanel);
        slide.setByY(20);
        slide.setInterpolator(javafx.animation.Interpolator.EASE_IN);

        FadeTransition fade = new FadeTransition(Duration.millis(320), controllerPanel);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);

        ScaleTransition scale = new ScaleTransition(Duration.millis(380), controllerPanel);
        scale.setToX(0.9);
        scale.setToY(0.9);
        scale.setInterpolator(javafx.animation.Interpolator.EASE_IN);

        hide.getChildren().addAll(slide, fade, scale);

        if (controllerDim != null) {
            FadeTransition dimFade = new FadeTransition(Duration.millis(300), controllerDim);
            dimFade.setFromValue(controllerDim.getOpacity());
            dimFade.setToValue(0.0);
            dimFade.setOnFinished(ev -> controllerDim.setVisible(false));
            dimFade.play();
        }

        if (controllerFlicker != null) {
            controllerFlicker.stop();
        }

        if (controllerInnerDim != null) {
            FadeTransition innerFade = new FadeTransition(Duration.millis(220), controllerInnerDim);
            innerFade.setFromValue(controllerInnerDim.getOpacity());
            innerFade.setToValue(0.0);
            innerFade.setOnFinished(ev -> controllerInnerDim.setVisible(false));
            innerFade.play();
        }

        hide.setOnFinished(ev -> controllerPanel.setVisible(false));
        hide.play();
    }

    
    private void showPanel() {
        controllerPanel.setOpacity(0);
        controllerPanel.setVisible(true);
        controllerPanel.setTranslateY(0);

        TranslateTransition slide = new TranslateTransition(Duration.millis(420), controllerPanel);
        slide.setFromY(0);
        slide.setToY(0);
        slide.setInterpolator(javafx.animation.Interpolator.EASE_OUT);

        FadeTransition fade = new FadeTransition(Duration.millis(420), controllerPanel);
        fade.setFromValue(0.0);
        fade.setToValue(1.0);

        ScaleTransition pop = new ScaleTransition(Duration.millis(360), controllerPanel);
        pop.setFromX(0.92);
        pop.setFromY(0.92);
        pop.setToX(1.0);
        pop.setToY(1.0);
        pop.setInterpolator(javafx.animation.Interpolator.EASE_OUT);

        ParallelTransition show = new ParallelTransition(slide, fade, pop);

        if (controllerDim != null) {
            controllerDim.setOpacity(0);
            controllerDim.setVisible(true);
            FadeTransition dimFade = new FadeTransition(Duration.millis(300), controllerDim);
            dimFade.setFromValue(0.0);
            dimFade.setToValue(0.65);
            dimFade.play();
        }

        if (controllerInnerDim != null) {
            controllerInnerDim.setOpacity(0);
            controllerInnerDim.setVisible(true);
            FadeTransition innerFade = new FadeTransition(Duration.millis(260), controllerInnerDim);
            innerFade.setFromValue(0.0);
            innerFade.setToValue(0.5);
            innerFade.play();
        }

        if (controllerFlicker != null) {
            controllerFlicker.play();
        }

        show.play();
        backgroundPane.setOpacity(0.3);
        backgroundPane.setEffect(new javafx.scene.effect.GaussianBlur(15));

        if (levelSelectionContainer != null && levelSelectionContainer.isVisible()) {
            levelSelectionContainer.setOpacity(0.3);
            levelSelectionContainer.setEffect(new javafx.scene.effect.GaussianBlur(15));
        }

        if (nodesToBlur != null) {
            for (javafx.scene.Node n : nodesToBlur) {
                if (n != null) {
                    n.setOpacity(0.3);
                    n.setEffect(new javafx.scene.effect.GaussianBlur(12));
                }
            }
        }
    }

    
    private StackPane createControllerPanel() {
        StackPane panel = new StackPane();
        panel.getStyleClass().add("controller-panel");
        panel.setVisible(false);
        panel.setOpacity(0);
        panel.setOnMouseClicked(e -> toggleControllerPanel());

        VBox controlsContainer = new VBox();
        controlsContainer.setSpacing(14);
        controlsContainer.setPadding(new Insets(16, 20, 16, 20));
        controlsContainer.setPrefWidth(300);

        
        Text header = new Text("HOW TO PLAY");
        header.setFill(Color.web("#bdf8ee"));
        header.setStyle("-fx-font-weight: bold;");
        header.setFont(Font.font("PressStart2P", 20));
        header.setEffect(new DropShadow(8, Color.web("#00ffdd", 0.45)));
        header.setTextAlignment(TextAlignment.CENTER);

        
        Text controlsTitle = new Text("WASD / MOUSE");
        controlsTitle.setFill(Color.web("#00ffdd"));
        controlsTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 12px;");
        controlsTitle.setTextAlignment(TextAlignment.CENTER);

        controlsContainer.getChildren().addAll(header, controlsTitle);

        
        Font neonFont = null;
        try {
            neonFont = Font.loadFont(getClass().getResourceAsStream("/fonts/PressStart2P-vaV7.ttf"), 10);
        } catch (Exception ignored) {
        }
        if (neonFont == null) {
            neonFont = Font.font("Consolas", 10);
        }

        
        String[][] controls = new String[][]{
            {"W/\u2191", "Move Up"},    // ↑
            {"A/\u2190", "Move Left"},  // ←
            {"S/\u2193", "Move Down"},  // ↓
            {"D/\u2192", "Move Right"}, // →
                {"Space", "Hard Drop"},
                {"P", "Pause"},
                {"Mouse", "Alternative Movement"}
        };

        
        for (String[] control : controls) {
            HBox controlRow = new HBox();
            controlRow.setSpacing(12);
            controlRow.setAlignment(Pos.CENTER);
            controlRow.setPrefWidth(300);

            Text keyText = new Text(control[0]);
            keyText.setFont(neonFont);
            keyText.setFill(Color.web("#ffaa00"));
            keyText.setStyle("-fx-font-weight: bold;");
            keyText.setEffect(new DropShadow(6, Color.web("#ffaa00", 0.6)));
            keyText.setTextAlignment(TextAlignment.CENTER);

            Text descText = new Text(control[1]);
            descText.setFont(neonFont);
            descText.setFill(Color.web("#00ffdd"));
            descText.setEffect(new DropShadow(4, Color.web("#00ffdd", 0.4)));
            descText.setTextAlignment(TextAlignment.CENTER);

            controlRow.getChildren().addAll(keyText, descText);
            controlsContainer.getChildren().add(controlRow);
        }

        
        Rectangle bg = new Rectangle(350, 320);
        bg.setArcWidth(12);
        bg.setArcHeight(12);
        bg.setFill(Color.web("#000000", 0.65));
        bg.setStroke(Color.web("#00ffdd", 0.4));
        bg.setStrokeWidth(2);
        bg.setEffect(new DropShadow(20, Color.web("#00ffdd", 0.7)));

        
        controllerInnerDim = new Rectangle();
        controllerInnerDim.setArcWidth(bg.getArcWidth());
        controllerInnerDim.setArcHeight(bg.getArcHeight());
        controllerInnerDim.setFill(Color.web("#000000", 0.5));
        controllerInnerDim.widthProperty().bind(bg.widthProperty());
        controllerInnerDim.heightProperty().bind(bg.heightProperty());
        controllerInnerDim.setMouseTransparent(true);
        controllerInnerDim.setOpacity(0);
        controllerInnerDim.setVisible(false);

        
        try {
            controllerFlicker = NeonFlickerEffect.createIntenseFlicker(controlsContainer);
            if (controllerFlicker != null) {
                controllerFlicker.setCycleCount(javafx.animation.Animation.INDEFINITE);
            }
        } catch (Exception ignored) {
            controllerFlicker = null;
        }

        
        panel.setPrefSize(bg.getWidth(), bg.getHeight());
        controlsContainer.prefWidthProperty().bind(bg.widthProperty().subtract(40));
        controlsContainer.prefHeightProperty().bind(bg.heightProperty().subtract(40));

        controlsContainer.setAlignment(Pos.CENTER);
        StackPane.setAlignment(controlsContainer, Pos.CENTER);
        StackPane.setMargin(controlsContainer, new Insets(0));

        
        for (Node child : controlsContainer.getChildren()) {
            if (child instanceof HBox row) {
                row.prefWidthProperty().bind(controlsContainer.widthProperty());
                HBox.setHgrow(row, Priority.ALWAYS);
            }
        }

        panel.getChildren().addAll(bg, controllerInnerDim, controlsContainer);
        return panel;
    }
}



