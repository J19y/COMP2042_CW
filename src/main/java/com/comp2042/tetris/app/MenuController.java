package com.comp2042.tetris.app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.comp2042.tetris.mechanics.board.GameView;
import com.comp2042.tetris.ui.view.BufferedGameView;
import com.comp2042.tetris.ui.view.GuiController;

import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.Glow;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MenuController {

    @FXML
    private Pane backgroundPane;
    @FXML
    private javafx.scene.layout.StackPane rootPane;
    @FXML
    private BorderPane mainMenuUI;

    @FXML
    private HBox titleContainer;
    @FXML
    private Text yearText;

    @FXML
    private Button playButton;
    @FXML
    private Button quitButton;
    @FXML
    private javafx.scene.layout.StackPane exitConfirmationOverlay;
    @FXML
    private javafx.scene.layout.StackPane controlLight;
    @FXML
    private javafx.scene.image.ImageView controlLightImage;
    @FXML
    private javafx.scene.Group controlLightVector;
    @FXML
    private javafx.scene.control.Button controlsButton;
    @FXML
    private javafx.scene.layout.StackPane settingsOverlay;
    @FXML
    private javafx.scene.control.Slider volumeSlider;
    @FXML
    private javafx.scene.control.Button musicToggleButton;
    @FXML
    private javafx.scene.text.Text volumeText;
    @FXML
    private javafx.scene.control.Button settingsButton;

    // Panel that will show controller mappings; created lazily
    private javafx.scene.layout.StackPane controllerPanel;
    // Dim overlay to match the game's pause format
    private javafx.scene.layout.Pane controllerDim;
    // Inner dim for the controller panel (subtle black overlay)
    private Rectangle controllerInnerDim;
    // Flicker timeline for neon effect inside the controller panel
    private javafx.animation.Timeline controllerFlicker;

    private final List<FallingShape> fallingShapes = new ArrayList<>();
    private final List<Particle> particles = new ArrayList<>();
    private final Random random = new Random();
    private AnimationTimer timer;

    private static final Color[] NEON_COLORS = {
            Color.CYAN, Color.YELLOW, Color.LIME, Color.RED, Color.MAGENTA, Color.ORANGE, Color.DODGERBLUE
    };

    @FXML
    public void initialize() {
        setupTitle();
        setupButtons();
        startAnimation();

        setupControlLight();
        setupControlsButton();
        setupSettingsButton();
        

        Rectangle fadeOverlay = new Rectangle(700, 600, Color.BLACK);
        fadeOverlay.setMouseTransparent(true); // Allow clicks to pass through if needed, though it fades out
        rootPane.getChildren().add(fadeOverlay); // Add on top of everything

        FadeTransition fade = new FadeTransition(Duration.seconds(3), fadeOverlay);
        fade.setFromValue(1);
        fade.setToValue(0);
        fade.setOnFinished(e -> rootPane.getChildren().remove(fadeOverlay)); // Remove after fade
        fade.play();
    }

    private void setupControlLight() {
        if (controlLight == null) return;
        controlLight.setOnMouseClicked(e -> toggleControllerPanel());
        controlLight.setCursor(javafx.scene.Cursor.HAND);
        // Hover pulse/glow for the image if available
        if (controlLightImage != null) {
            controlLightImage.setOnMouseEntered(e -> {
                javafx.animation.ScaleTransition st = new javafx.animation.ScaleTransition(javafx.util.Duration.millis(220), controlLightImage);
                st.setToX(1.16);
                st.setToY(1.16);
                st.setInterpolator(javafx.animation.Interpolator.EASE_OUT);
                st.play();
                controlLightImage.setEffect(new Glow(0.65));
            });
            controlLightImage.setOnMouseExited(e -> {
                javafx.animation.ScaleTransition st = new javafx.animation.ScaleTransition(javafx.util.Duration.millis(160), controlLightImage);
                st.setToX(1.0);
                st.setToY(1.0);
                st.setInterpolator(javafx.animation.Interpolator.EASE_IN);
                st.play();
                controlLightImage.setEffect(null);
            });
            // forward clicks from image to parent stackpane handler
            controlLightImage.setOnMouseClicked(e -> controlLight.fireEvent(e));

            // If image resource failed to load, show the vector fallback instead
            javafx.scene.image.Image img = controlLightImage.getImage();
            if (img == null || img.isError()) {
                controlLightImage.setVisible(false);
                if (controlLightVector != null) controlLightVector.setVisible(true);
            }
        }

        // Hover behavior for vector fallback
        if (controlLightVector != null) {
            controlLightVector.setOnMouseEntered(e -> {
                javafx.animation.ScaleTransition st = new javafx.animation.ScaleTransition(javafx.util.Duration.millis(220), controlLightVector);
                st.setToX(1.08);
                st.setToY(1.08);
                st.setInterpolator(javafx.animation.Interpolator.EASE_OUT);
                st.play();
                controlLightVector.setEffect(new DropShadow(14, javafx.scene.paint.Color.web("#00f0d8", 0.6)));
            });
            controlLightVector.setOnMouseExited(e -> {
                javafx.animation.ScaleTransition st = new javafx.animation.ScaleTransition(javafx.util.Duration.millis(160), controlLightVector);
                st.setToX(1.0);
                st.setToY(1.0);
                st.setInterpolator(javafx.animation.Interpolator.EASE_IN);
                st.play();
                controlLightVector.setEffect(null);
            });
            controlLightVector.setOnMouseClicked(e -> controlLight.fireEvent(e));
        }
    }

    private void setupControlsButton() {
        if (controlsButton == null) return;
        controlsButton.setOnAction(e -> toggleControllerPanel());
        controlsButton.setCursor(javafx.scene.Cursor.HAND);
        
        // Replace with the light bulb image from resources
        try {
            javafx.scene.image.ImageView bulbImage = new javafx.scene.image.ImageView(new javafx.scene.image.Image(getClass().getResourceAsStream("/light-bulb3.png")));
            bulbImage.setFitWidth(65);
            bulbImage.setFitHeight(65);
            bulbImage.setPreserveRatio(true);
            bulbImage.setEffect(new Glow(0.3));
            
            // Add smooth hover effect for enhanced glow
            controlsButton.setOnMouseEntered(event -> {
                // Smooth fade in to bright glow
                javafx.animation.Timeline glowIn = new javafx.animation.Timeline(
                    new javafx.animation.KeyFrame(javafx.util.Duration.ZERO, 
                        new javafx.animation.KeyValue(((Glow)bulbImage.getEffect()).levelProperty(), 0.3)),
                    new javafx.animation.KeyFrame(javafx.util.Duration.millis(300), 
                        new javafx.animation.KeyValue(((Glow)bulbImage.getEffect()).levelProperty(), 4.0))
                );
                glowIn.play();
            });
            controlsButton.setOnMouseExited(event -> {
                // Smooth fade out to dim glow
                javafx.animation.Timeline glowOut = new javafx.animation.Timeline(
                    new javafx.animation.KeyFrame(javafx.util.Duration.ZERO, 
                        new javafx.animation.KeyValue(((Glow)bulbImage.getEffect()).levelProperty(), 4.0)),
                    new javafx.animation.KeyFrame(javafx.util.Duration.millis(300), 
                        new javafx.animation.KeyValue(((Glow)bulbImage.getEffect()).levelProperty(), 0.3))
                );
                glowOut.play();
            });
            
            controlsButton.setGraphic(bulbImage);
        } catch (Exception e) {
            // Fallback to SVG if image fails
            javafx.scene.shape.SVGPath bulb = new javafx.scene.shape.SVGPath();
            bulb.setContent("M12 2C13.1 2 14 2.9 14 4V5H16V7H13V19C13 20.1 12.1 21 11 21C9.9 21 9 20.1 9 19V7H6V5H8V4C8 2.9 8.9 2 10 2H12Z M10 4V5H14V4H10Z");
            bulb.setFill(javafx.scene.paint.Color.TRANSPARENT);
            bulb.setStroke(javafx.scene.paint.Color.web("#ffff00"));
            bulb.setStrokeWidth(2);
            bulb.setEffect(new Glow(0.3));
            bulb.setScaleX(1.3);
            bulb.setScaleY(1.3);
            
            // Add smooth hover effect for SVG fallback too
            controlsButton.setOnMouseEntered(event -> {
                // Smooth fade in to bright glow
                javafx.animation.Timeline glowIn = new javafx.animation.Timeline(
                    new javafx.animation.KeyFrame(javafx.util.Duration.ZERO, 
                        new javafx.animation.KeyValue(((Glow)bulb.getEffect()).levelProperty(), 0.3)),
                    new javafx.animation.KeyFrame(javafx.util.Duration.millis(300), 
                        new javafx.animation.KeyValue(((Glow)bulb.getEffect()).levelProperty(), 4.0))
                );
                glowIn.play();
            });
            controlsButton.setOnMouseExited(event -> {
                // Smooth fade out to dim glow
                javafx.animation.Timeline glowOut = new javafx.animation.Timeline(
                    new javafx.animation.KeyFrame(javafx.util.Duration.ZERO, 
                        new javafx.animation.KeyValue(((Glow)bulb.getEffect()).levelProperty(), 4.0)),
                    new javafx.animation.KeyFrame(javafx.util.Duration.millis(300), 
                        new javafx.animation.KeyValue(((Glow)bulb.getEffect()).levelProperty(), 0.3))
                );
                glowOut.play();
            });
            
            controlsButton.setGraphic(bulb);
        }
        controlsButton.setText("");
        controlsButton.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
    }

    private void setupSettingsButton() {
        if (settingsButton == null) return;
        settingsButton.setOnAction(e -> toggleSettings());
        settingsButton.setCursor(javafx.scene.Cursor.HAND);
        
        // Set the speaker icon as the button graphic
        try {
            javafx.scene.image.ImageView speakerImage = new javafx.scene.image.ImageView(new javafx.scene.image.Image(getClass().getResourceAsStream("/whitespeaker.png")));
            speakerImage.setFitWidth(45);
            speakerImage.setFitHeight(45);
            speakerImage.setPreserveRatio(true);
            speakerImage.setTranslateX(-8); // Move further to the left
            // Darker cyan glow effect
            DropShadow cyanGlow = new DropShadow();
            cyanGlow.setColor(Color.web("#008B8B")); // Dark cyan
            cyanGlow.setRadius(8);
            cyanGlow.setSpread(0.15);
            speakerImage.setEffect(cyanGlow);
            settingsButton.setGraphic(speakerImage);
        } catch (Exception e) {
            // Fallback to text if image fails
            settingsButton.setText("AUDIO");
            settingsButton.setStyle("-fx-font-size: 12px; -fx-text-fill: #00ffff; -fx-background-color: transparent; -fx-border-color: transparent; -fx-font-weight: bold;");
        }
        settingsButton.setText("");
        settingsButton.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");

        // Volume slider listener
        if (volumeSlider != null && volumeText != null) {
            volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
                volumeText.setText((int) newVal.doubleValue() + "%");
            });
        }
    }

    /**
     * Sets up the Controls Guide display in the bottom-left of the main menu.
     * Displays keyboard/mouse control hints with retro neon styling and optional flicker animation.
     */

    public void openControllerPanel() {
        if (controllerPanel == null || !controllerPanel.isVisible()) {
            toggleControllerPanel();
        }
    }

    public void toggleControllerPanel() {
        if (controllerPanel == null) {
            controllerPanel = createControllerPanel();
            // place it above mainMenuUI
            // create and insert dim behind the panel to match pause format
            controllerDim = new javafx.scene.layout.Pane();
            controllerDim.getStyleClass().add("pause-dim");
            controllerDim.setOpacity(0);
            controllerDim.setVisible(false);
            controllerDim.setMouseTransparent(false);
            controllerDim.prefWidthProperty().bind(rootPane.widthProperty());
            controllerDim.prefHeightProperty().bind(rootPane.heightProperty());
            controllerDim.setOnMouseClicked(ev -> toggleControllerPanel()); // clicking anywhere closes panel

            rootPane.getChildren().add(controllerDim);
            rootPane.getChildren().add(controllerPanel);
            // Place the controller panel in the centre of the screen so background and text align
            javafx.scene.layout.StackPane.setAlignment(controllerPanel, javafx.geometry.Pos.CENTER);
            controllerPanel.setTranslateX(0);
            controllerPanel.setTranslateY(0);
        }
        boolean visible = controllerPanel.isVisible();
        if (visible) {
            // hide panel + fade out dim (matching pause format)
            backgroundPane.setOpacity(1.0);
            mainMenuUI.setOpacity(1.0);
            backgroundPane.setEffect(null);
            mainMenuUI.setEffect(null);
            javafx.animation.ParallelTransition hide = new javafx.animation.ParallelTransition();
            
            // Slide down with opacity fade
            javafx.animation.TranslateTransition slide = new javafx.animation.TranslateTransition(javafx.util.Duration.millis(380), controllerPanel);
            slide.setByY(20);
            slide.setInterpolator(javafx.animation.Interpolator.EASE_IN);
            
            javafx.animation.FadeTransition fade = new javafx.animation.FadeTransition(javafx.util.Duration.millis(320), controllerPanel);
            fade.setFromValue(1.0);
            fade.setToValue(0.0);

            javafx.animation.ScaleTransition scale = new javafx.animation.ScaleTransition(javafx.util.Duration.millis(380), controllerPanel);
            scale.setToX(0.9);
            scale.setToY(0.9);
            scale.setInterpolator(javafx.animation.Interpolator.EASE_IN);
            
            hide.getChildren().addAll(slide, fade, scale);

            // fade out dim
            if (controllerDim != null) {
                javafx.animation.FadeTransition dimFade = new javafx.animation.FadeTransition(javafx.util.Duration.millis(300), controllerDim);
                dimFade.setFromValue(controllerDim.getOpacity());
                dimFade.setToValue(0.0);
                dimFade.setOnFinished(ev -> controllerDim.setVisible(false));
                dimFade.play();
            }

            // stop and fade out inner controller dim and flicker effect
            if (controllerFlicker != null) {
                controllerFlicker.stop();
            }
            if (controllerInnerDim != null) {
                javafx.animation.FadeTransition innerFade = new javafx.animation.FadeTransition(javafx.util.Duration.millis(220), controllerInnerDim);
                innerFade.setFromValue(controllerInnerDim.getOpacity());
                innerFade.setToValue(0.0);
                innerFade.setOnFinished(ev -> controllerInnerDim.setVisible(false));
                innerFade.play();
            }

            hide.setOnFinished(ev -> controllerPanel.setVisible(false));
            hide.play();
        } else {
            controllerPanel.setOpacity(0);
            controllerPanel.setVisible(true);
            controllerPanel.setTranslateY(0);
            
            // Entrance: slide up + fade + scale with glow effect
            javafx.animation.TranslateTransition slide = new javafx.animation.TranslateTransition(javafx.util.Duration.millis(420), controllerPanel);
            slide.setFromY(0);
            slide.setToY(0);
            slide.setInterpolator(javafx.animation.Interpolator.EASE_OUT);

            javafx.animation.FadeTransition fade = new javafx.animation.FadeTransition(javafx.util.Duration.millis(420), controllerPanel);
            fade.setFromValue(0.0);
            fade.setToValue(1.0);

            javafx.animation.ScaleTransition pop = new javafx.animation.ScaleTransition(javafx.util.Duration.millis(360), controllerPanel);
            pop.setFromX(0.92);
            pop.setFromY(0.92);
            pop.setToX(1.0);
            pop.setToY(1.0);
            pop.setInterpolator(javafx.animation.Interpolator.EASE_OUT);

            javafx.animation.ParallelTransition show = new javafx.animation.ParallelTransition(slide, fade, pop);
            
            // show dim with pause-style fade then show panel
            if (controllerDim != null) {
                controllerDim.setOpacity(0);
                controllerDim.setVisible(true);
                javafx.animation.FadeTransition dimFade = new javafx.animation.FadeTransition(javafx.util.Duration.millis(300), controllerDim);
                dimFade.setFromValue(0.0);
                dimFade.setToValue(0.65);
                dimFade.play();
            }

            // fade in inner panel dim and start flicker
            if (controllerInnerDim != null) {
                controllerInnerDim.setOpacity(0);
                controllerInnerDim.setVisible(true);
                javafx.animation.FadeTransition innerFade = new javafx.animation.FadeTransition(javafx.util.Duration.millis(260), controllerInnerDim);
                innerFade.setFromValue(0.0);
                innerFade.setToValue(0.5);
                innerFade.play();
            }

            if (controllerFlicker != null) {
                controllerFlicker.play();
            }

            show.play();
            backgroundPane.setOpacity(0.3);
            mainMenuUI.setOpacity(0.3);
            backgroundPane.setEffect(new GaussianBlur(15));
            mainMenuUI.setEffect(new GaussianBlur(15));
        }
    }

    private javafx.scene.layout.StackPane createControllerPanel() {
        javafx.scene.layout.StackPane panel = new javafx.scene.layout.StackPane();
        panel.getStyleClass().add("controller-panel");
        panel.setVisible(false);
        panel.setOpacity(0);
        panel.setOnMouseClicked(e -> toggleControllerPanel()); // clicking on panel closes it

        // Create VBox container for neon controls guide
        javafx.scene.layout.VBox controlsContainer = new javafx.scene.layout.VBox();
        controlsContainer.setSpacing(14);
        // Use explicit Insets so layout/pref sizes are calculated reliably
        controlsContainer.setPadding(new javafx.geometry.Insets(16, 20, 16, 20));
        controlsContainer.setPrefWidth(300);

        // Header: prominent title for the guide
        Text header = new Text("HOW TO PLAY");
        header.setFill(javafx.scene.paint.Color.web("#bdf8ee"));
        header.setStyle("-fx-font-weight: bold;");
        header.setFont(javafx.scene.text.Font.font("PressStart2P", 20));
        header.setEffect(new DropShadow(8, javafx.scene.paint.Color.web("#00ffdd", 0.45)));
        header.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        // Subtitle / short descriptor
        Text controlsTitle = new Text("WASD / MOUSE");
        controlsTitle.setFill(javafx.scene.paint.Color.web("#00ffdd"));
        controlsTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 12px;");
        controlsTitle.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        controlsContainer.getChildren().addAll(header, controlsTitle);

        // Load or fallback to default font
        javafx.scene.text.Font neonFont = null;
        try {
            neonFont = javafx.scene.text.Font.loadFont(getClass().getResourceAsStream("/PressStart2P-vaV7.ttf"), 10);
        } catch (Exception ignored) {}
        if (neonFont == null) {
            neonFont = javafx.scene.text.Font.font("Consolas", 10);
        }

        String[][] controls = new String[][]{
                {"W/↑", "Move Up"},
                {"A/←", "Move Left"},
                {"S/↓", "Move Down"},
                {"D/→", "Move Right"},
                {"Space", "Hard Drop"},
                {"P", "Pause"},
                {"Mouse", "Alternative Movement"}
        };

        for (String[] control : controls) {
            javafx.scene.layout.HBox controlRow = new javafx.scene.layout.HBox();
            controlRow.setSpacing(12);
            // center the key + description horizontally within the row
            controlRow.setAlignment(javafx.geometry.Pos.CENTER);
            controlRow.setPrefWidth(300);

            // Key cap
            Text keyText = new Text(control[0]);
            keyText.setFont(neonFont);
            keyText.setFill(javafx.scene.paint.Color.web("#ffaa00"));
            keyText.setStyle("-fx-font-weight: bold;");
            keyText.setEffect(new DropShadow(6, javafx.scene.paint.Color.web("#ffaa00", 0.6)));
            keyText.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

            // Description
            Text descText = new Text(control[1]);
            descText.setFont(neonFont);
            descText.setFill(javafx.scene.paint.Color.web("#00ffdd"));
            descText.setEffect(new DropShadow(4, javafx.scene.paint.Color.web("#00ffdd", 0.4)));
            descText.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

            controlRow.getChildren().addAll(keyText, descText);
            controlsContainer.getChildren().add(controlRow);
        }

        // Backdrop with neon styling
        Rectangle bg = new Rectangle(350, 320);
        bg.setArcWidth(12);
        bg.setArcHeight(12);
        bg.setFill(javafx.scene.paint.Color.web("#000000", 0.65));
        bg.setStroke(javafx.scene.paint.Color.web("#00ffdd", 0.4));
        bg.setStrokeWidth(2);
        bg.setEffect(new DropShadow(20, javafx.scene.paint.Color.web("#00ffdd", 0.7)));

        // Inner dim to slightly darken the panel backdrop and help text pop
        controllerInnerDim = new Rectangle();
        controllerInnerDim.setArcWidth(bg.getArcWidth());
        controllerInnerDim.setArcHeight(bg.getArcHeight());
        // Darken inner panel so controls are easier to read (pause-like darker interior)
        controllerInnerDim.setFill(javafx.scene.paint.Color.web("#000000", 0.5));
        controllerInnerDim.widthProperty().bind(bg.widthProperty());
        controllerInnerDim.heightProperty().bind(bg.heightProperty());
        controllerInnerDim.setMouseTransparent(true);
        controllerInnerDim.setOpacity(0);
        controllerInnerDim.setVisible(false);

        // Prepare subtle flicker timeline but do not start it yet. Apply to the controls container so the whole block subtly flickers.
        try {
            // Use the more pronounced flicker preset for better visual emphasis
            controllerFlicker = com.comp2042.tetris.ui.animation.NeonFlickerEffect.createIntenseFlicker(controlsContainer);
            if (controllerFlicker != null) {
                controllerFlicker.setCycleCount(javafx.animation.Animation.INDEFINITE);
            }
        } catch (Exception ignored) {
            controllerFlicker = null;
        }

        // Ensure the panel itself matches the backdrop size so children are positioned inside it
        panel.setPrefSize(bg.getWidth(), bg.getHeight());

        // Bind controls container width/height to bg so padding aligns precisely and content can be centered
        controlsContainer.prefWidthProperty().bind(bg.widthProperty().subtract(40));
        controlsContainer.prefHeightProperty().bind(bg.heightProperty().subtract(40));

        // Align the controls content to the center inside the neon backdrop (both horizontally & vertically)
        controlsContainer.setAlignment(javafx.geometry.Pos.CENTER);
        javafx.scene.layout.StackPane.setAlignment(controlsContainer, javafx.geometry.Pos.CENTER);
        // Remove extra margins so the control block centers exactly inside the backdrop
        javafx.scene.layout.StackPane.setMargin(controlsContainer, new javafx.geometry.Insets(0));

        // Ensure each HBox row fills the container width so key+description center properly
        for (javafx.scene.Node child : controlsContainer.getChildren()) {
            if (child instanceof javafx.scene.layout.HBox) {
                javafx.scene.layout.HBox row = (javafx.scene.layout.HBox) child;
                row.prefWidthProperty().bind(controlsContainer.widthProperty());
                // let the description expand if needed
                javafx.scene.layout.HBox.setHgrow(row, javafx.scene.layout.Priority.ALWAYS);
            }
        }

        // Add in order: background, inner dim, then content
        panel.getChildren().addAll(bg, controllerInnerDim, controlsContainer);
        return panel;
    }

    @FXML
    private void toggleSettings() {
        if (settingsOverlay != null) {
            boolean visible = settingsOverlay.isVisible();
            if (visible) {
                // Hide overlay with fade out
                FadeTransition fadeOut = new FadeTransition(Duration.millis(300), settingsOverlay);
                fadeOut.setFromValue(1.0);
                fadeOut.setToValue(0.0);
                fadeOut.setOnFinished(e -> settingsOverlay.setVisible(false));
                fadeOut.play();
            } else {
                // Show overlay with fade in
                settingsOverlay.setOpacity(0);
                settingsOverlay.setVisible(true);
                FadeTransition fadeIn = new FadeTransition(Duration.millis(300), settingsOverlay);
                fadeIn.setFromValue(0.0);
                fadeIn.setToValue(1.0);
                fadeIn.play();
            }
        }
    }

    @FXML
    private void toggleMusic() {
        if (musicToggleButton != null) {
            String currentText = musicToggleButton.getText();
            if ("ON".equals(currentText)) {
                musicToggleButton.setText("OFF");
                // Change icon to muted
                Text mutedIcon = new Text("🔇");
                mutedIcon.setFill(Color.WHITE);
                musicToggleButton.setGraphic(mutedIcon);
            } else {
                musicToggleButton.setText("ON");
                // Change icon to unmuted
                Text unmutedIcon = new Text("🔊");
                unmutedIcon.setFill(Color.WHITE);
                musicToggleButton.setGraphic(unmutedIcon);
            }
        }
    }

    @FXML
    private void closeSettings() {
        if (settingsOverlay != null && settingsOverlay.isVisible()) {
            // Hide overlay with fade out
            FadeTransition fadeOut = new FadeTransition(Duration.millis(300), settingsOverlay);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.setOnFinished(e -> settingsOverlay.setVisible(false));
            fadeOut.play();
        }
    }

    private void setupTitle() {
        String title = "TETRIS";
        Color[] titleColors = {Color.BLUE, Color.RED, Color.YELLOW, Color.LIME, Color.MAGENTA, Color.ORANGE};

        javafx.scene.text.Font customFont = null;
        try {
            customFont = javafx.scene.text.Font.loadFont(getClass().getResourceAsStream("/PressStart2P-vaV7.ttf"), 60);
            if (customFont != null) {
                System.out.println("Loaded custom font: " + customFont.getName());
            } else {
                System.err.println("Failed to load PressStart2P-vaV7.ttf");
            }
        } catch (Exception e) { 
            System.err.println("Exception loading font: " + e.getMessage());
        }
        
        if (customFont == null) {
            try {
                customFont = javafx.scene.text.Font.loadFont(getClass().getResourceAsStream("/digital.ttf"), 80);
            } catch (Exception e) { /* Ignore */ }
        }
        if (customFont == null) {
            customFont = javafx.scene.text.Font.font("Impact", 90);
        }

        for (int i = 0; i < title.length(); i++) {
            Text letter = new Text(String.valueOf(title.charAt(i)));
            letter.getStyleClass().add("title-text");
            letter.setFont(customFont);
            letter.setFill(titleColors[i % titleColors.length]);
            
            // Add glow effect - Reduced as requested
            DropShadow glow = new DropShadow();
            glow.setColor(titleColors[i % titleColors.length]);
            glow.setRadius(10);
            glow.setSpread(0.2);
            letter.setEffect(glow);

            titleContainer.getChildren().add(letter);
        }
        

        javafx.scene.text.Font yearFont = null;
        try {
            yearFont = javafx.scene.text.Font.loadFont(getClass().getResourceAsStream("/NineByFiveNbp-MypB.ttf"), 40);
        } catch (Exception e) { /* Ignore */ }
        
        if (yearFont == null) {
             try {
                yearFont = javafx.scene.text.Font.loadFont(getClass().getResourceAsStream("/digital.ttf"), 40);
            } catch (Exception e) { /* Ignore */ }
        }
        if (yearFont == null) yearFont = javafx.scene.text.Font.font("Arial", 40);
        
        yearText.setFont(yearFont);
        yearText.setFill(Color.WHITE);
        yearText.setEffect(new DropShadow(10, Color.CYAN));
    }

    private void setupButtons() {
        javafx.scene.text.Font buttonFont = null;
        try {
            buttonFont = javafx.scene.text.Font.loadFont(getClass().getResourceAsStream("/PressStart2P-vaV7.ttf"), 16);
        } catch (Exception e) { /* Ignore */ }
        
        if (buttonFont != null) {
            playButton.setFont(buttonFont);
            quitButton.setFont(buttonFont);
        }

        setupButtonAnimation(playButton);
        setupButtonAnimation(quitButton);
    }

    private void setupButtonAnimation(Button button) {
    }

    private void startAnimation() {
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                updateBackground();
            }
        };
        timer.start();
    }

    private void updateBackground() {
        // Spawn new shapes
        if (random.nextDouble() < 0.02) { // 2% chance per frame
            spawnFallingShape();
        }

        // Spawn particles
        if (random.nextDouble() < 0.1) {
            spawnParticle();
        }

        // Update shapes
        Iterator<FallingShape> shapeIt = fallingShapes.iterator();
        while (shapeIt.hasNext()) {
            FallingShape shape = shapeIt.next();
            shape.update();
            if (shape.isOffScreen()) {
                backgroundPane.getChildren().remove(shape.node);
                shapeIt.remove();
            }
        }

        // Update particles
        Iterator<Particle> particleIt = particles.iterator();
        while (particleIt.hasNext()) {
            Particle p = particleIt.next();
            p.update();
            if (p.isDead()) {
                backgroundPane.getChildren().remove(p.node);
                particleIt.remove();
            }
        }
    }

    private void spawnFallingShape() {
        double x = random.nextDouble() * backgroundPane.getWidth();
        double size = 8 + random.nextDouble() * 12; // Smaller size
        Color color = NEON_COLORS[random.nextInt(NEON_COLORS.length)];
        
        Node shape = createRandomTetromino(size, color);
        
        shape.setTranslateX(x);
        shape.setTranslateY(-100);
        
        // More blur for all shapes
        shape.setOpacity(0.4);
        shape.setEffect(new javafx.scene.effect.GaussianBlur(4 + random.nextDouble() * 6));

        backgroundPane.getChildren().add(shape);
        fallingShapes.add(new FallingShape(shape, 0.5 + random.nextDouble() * 1.5));
    }

    private Node createRandomTetromino(double size, Color color) {
        javafx.scene.Group group = new javafx.scene.Group();
        // 0: I, 1: J, 2: L, 3: O, 4: S, 5: T, 6: Z
        int type = random.nextInt(7);
        
        int[][] coords = getTetrominoCoords(type);
        
        for (int[] coord : coords) {
            Rectangle rect = new Rectangle(size, size, Color.TRANSPARENT);
            rect.setStroke(color);
            rect.setStrokeWidth(2);
            rect.setEffect(new Glow(0.5));
            rect.setX(coord[0] * size);
            rect.setY(coord[1] * size);
            group.getChildren().add(rect);
        }
        
        return group;
    }

    private int[][] getTetrominoCoords(int type) {
        return switch (type) {
            case 0 -> new int[][]{{0, 0}, {1, 0}, {2, 0}, {3, 0}}; // I
            case 1 -> new int[][]{{0, 0}, {0, 1}, {1, 1}, {2, 1}}; // J
            case 2 -> new int[][]{{2, 0}, {0, 1}, {1, 1}, {2, 1}}; // L
            case 3 -> new int[][]{{0, 0}, {1, 0}, {0, 1}, {1, 1}}; // O
            case 4 -> new int[][]{{1, 0}, {2, 0}, {0, 1}, {1, 1}}; // S
            case 5 -> new int[][]{{1, 0}, {0, 1}, {1, 1}, {2, 1}}; // T
            case 6 -> new int[][]{{0, 0}, {1, 0}, {1, 1}, {2, 1}}; // Z
            default -> new int[][]{{0, 0}};
        };
    }

    private void spawnParticle() {
        double x = random.nextDouble() * backgroundPane.getWidth();
        double y = random.nextDouble() * backgroundPane.getHeight();
        
        Rectangle p = new Rectangle(2, 2, Color.WHITE);
        p.setOpacity(random.nextDouble() * 0.5);
        p.setTranslateX(x);
        p.setTranslateY(y);
        
        backgroundPane.getChildren().add(p);
        particles.add(new Particle(p));
    }

    @FXML
    private void onPlay() {
        // Fade out transition
        FadeTransition fadeOut = new FadeTransition(Duration.millis(500), rootPane);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(e -> loadGameScene());
        fadeOut.play();
    }

    private void loadGameScene() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ui/layout/game.fxml"));
            Parent root = fxmlLoader.load();
            GuiController controller = fxmlLoader.getController();

            Scene scene = new Scene(root, 700, 600);
            scene.getStylesheets().add(getClass().getResource("/ui/styles/window.css").toExternalForm());
            
            Stage stage = (Stage) playButton.getScene().getWindow();
            root.setOpacity(0);
            stage.setScene(scene);
            FadeTransition fadeIn = new FadeTransition(Duration.millis(600), root);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();
            
            GameView decoratedView = new BufferedGameView(controller);
            // Keep reference to prevent GC or just suppress warning if logic is self-sustaining
            @SuppressWarnings("unused")
            GameController gameController = new GameController(decoratedView);
            
        } catch (IOException e) {
            System.err.println("Failed to load game scene: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void onQuit() {
        backgroundPane.setEffect(new GaussianBlur(10));
        mainMenuUI.setEffect(new GaussianBlur(10));
        exitConfirmationOverlay.setVisible(true);
    }

    @FXML
    private void onConfirmQuit() {
        Stage stage = (Stage) quitButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onCancelQuit() {
        backgroundPane.setEffect(null);
        mainMenuUI.setEffect(null);
        exitConfirmationOverlay.setVisible(false);
    }

    // Inner classes for animation
    private static class FallingShape {
        Node node;
        double speed;
        double rotationSpeed;

        FallingShape(Node node, double speed) {
            this.node = node;
            this.speed = speed;
            this.rotationSpeed = Math.random() * 2 - 1;
        }

        void update() {
            node.setTranslateY(node.getTranslateY() + speed);
            node.setRotate(node.getRotate() + rotationSpeed);
        }

        boolean isOffScreen() {
            return node.getTranslateY() > 800; // Assuming height is 600
        }
    }

    private static class Particle {
        Node node;
        double life = 1.0;
        double decay;

        Particle(Node node) {
            this.node = node;
            this.decay = 0.005 + Math.random() * 0.01;
        }

        void update() {
            life -= decay;
            node.setOpacity(life);
            node.setTranslateY(node.getTranslateY() + 0.5);
        }

        boolean isDead() {
            return life <= 0;
        }
    }
}
