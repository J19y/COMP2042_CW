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
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
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
    // Dim overlay used when settings are opened over the level-selection overlay
    private javafx.scene.shape.Rectangle settingsDim;
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
    private List<Timeline> titleFlickers = new ArrayList<>();
    // Ensure launch animation plays only once per app session
    private static boolean launchPlayed = false;

    // Level Selection state
    private javafx.scene.layout.VBox levelSelectionContainer;
    private String selectedGameMode = null; // "CLASSIC", "RUSH", "MYSTERY"

    private static final Color[] NEON_COLORS = {
            Color.CYAN, Color.YELLOW, Color.LIME, Color.RED, Color.MAGENTA, Color.ORANGE, Color.DODGERBLUE
    };

    @FXML
    public void initialize() {
        setupTitle();
        setupButtons();
        // Prepare initial hidden state for launch animation if this is the first time
        prepareLaunchForAnimation();
        if (!launchPlayed) {
            playLaunchAnimation();
            launchPlayed = true;
        } else {
            // Ensure UI is visible and interactive if we already played the launch animation
            finalizeLaunchState();
        }
        // background/particle animation
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

    /**
     * Prepare the UI nodes for the launch animation: hide title and buttons and disable interactivity.
     */
    private void prepareLaunchForAnimation() {
        // Hide title and year until logo animation plays
        if (titleContainer != null) {
            titleContainer.setOpacity(0.0);
            titleContainer.setScaleX(0.96);
            titleContainer.setScaleY(0.96);
        }
        if (yearText != null) {
            yearText.setOpacity(0.0);
        }

        // Ensure main interactive buttons are invisible & non-interactive until the animation completes
        Button[] uiButtons = new Button[]{playButton, quitButton, controlsButton, settingsButton};
        for (Button b : uiButtons) {
            if (b != null) {
                b.setOpacity(0.0);
                b.setDisable(true);
                b.setMouseTransparent(true);
            }
        }

        // Also prepare individual title letters for pre-appearance animation
        if (titleContainer != null) {
            for (Node n : titleContainer.getChildren()) {
                // start letters tiny and slightly lower so staggered pop-in feels tactile
                n.setOpacity(0.0);
                n.setScaleX(0.65);
                n.setScaleY(0.65);
                n.setTranslateY(18);
            }
        }
    }

    /**
     * Called after the launch animation is finished or when it should be skipped. Restores interactivity.
     */
    private void finalizeLaunchState() {
        if (titleContainer != null) {
            titleContainer.setOpacity(1.0);
            titleContainer.setScaleX(1.0);
            titleContainer.setScaleY(1.0);
            // Reset each letter to ensure it's visible and not transformed (fix: logo disappearing on return)
            for (Node n : titleContainer.getChildren()) {
                n.setVisible(true);
                n.setOpacity(1.0);
                n.setScaleX(1.0);
                n.setScaleY(1.0);
                n.setTranslateX(0.0);
                n.setTranslateY(0.0);
                n.setRotate(0.0);
            }
        }
        if (yearText != null) yearText.setOpacity(1.0);

        Button[] uiButtons = new Button[]{playButton, quitButton, controlsButton, settingsButton};
        for (Button b : uiButtons) {
            if (b != null) {
                b.setOpacity(1.0);
                b.setDisable(false);
                b.setMouseTransparent(false);
            }
        }
    }

    /**
     * Runs the launch animation sequence: CRT/glitch on the title, scanline sweep, then reveal buttons sequentially.
     */
    private void playLaunchAnimation() {
        // Disable any UI interaction while animation runs
        rootPane.setDisable(true);

        // Fade/scale the title into view
        javafx.animation.Timeline showTitle = new javafx.animation.Timeline(
            new javafx.animation.KeyFrame(Duration.ZERO,
                new KeyValue(titleContainer.opacityProperty(), 0.0),
                new KeyValue(titleContainer.scaleXProperty(), 0.96),
                new KeyValue(titleContainer.scaleYProperty(), 0.96)),
            new javafx.animation.KeyFrame(Duration.millis(520),
                new KeyValue(titleContainer.opacityProperty(), 1.0, javafx.animation.Interpolator.EASE_OUT),
                new KeyValue(titleContainer.scaleXProperty(), 1.02, javafx.animation.Interpolator.EASE_OUT),
                new KeyValue(titleContainer.scaleYProperty(), 1.02, javafx.animation.Interpolator.EASE_OUT))
        );

        // Brief settling back to 1.0 scale
        javafx.animation.Timeline settleTitle = new javafx.animation.Timeline(
            new javafx.animation.KeyFrame(Duration.millis(0), new KeyValue(titleContainer.scaleXProperty(), 1.02), new KeyValue(titleContainer.scaleYProperty(), 1.02)),
            new javafx.animation.KeyFrame(Duration.millis(160), new KeyValue(titleContainer.scaleXProperty(), 1.0), new KeyValue(titleContainer.scaleYProperty(), 1.0))
        );

        // Create a subtle CRT scanline overlay that sweeps across the title during the glitch
        Rectangle scanline = new Rectangle();
        scanline.setWidth(rootPane.getWidth());
        scanline.setHeight(10);
        scanline.setFill(javafx.scene.paint.Color.web("#ffffff", 0.06));
        scanline.setBlendMode(javafx.scene.effect.BlendMode.SRC_OVER);
        scanline.setOpacity(0);
        // place over the title roughly at its center
        double titleY = titleContainer.getLayoutY() + titleContainer.getTranslateY();
        scanline.setTranslateY(titleY - 12);
        // Make sure it is above background but below overlays like settings; add near top of root
        rootPane.getChildren().add(scanline);

        // Create several quick glitch bursts that shift letters, flicker alpha and flash a stronger glow
        javafx.animation.Timeline glitchTimeline = new javafx.animation.Timeline();
        int bursts = 4;
        double baseTime = 600; // ms offset after showTitle
        for (int i = 0; i < bursts; i++) {
            double t0 = baseTime + i * 180;

            // Add KeyFrame to trigger a quick random letter displacement
            javafx.animation.KeyFrame kf = new javafx.animation.KeyFrame(Duration.millis(t0), e -> {
                // small random subset of letters get an instantaneous offset and alpha jitter
                int letters = titleContainer.getChildren().size();
                for (int li = 0; li < letters; li++) {
                    if (Math.random() < 0.45) {
                        Node n = titleContainer.getChildren().get(li);
                        double xOff = (Math.random() - 0.5) * 18.0; // left/right
                        double yOff = (Math.random() - 0.5) * 6.0;
                        // quick translate and opacity flicker
                        javafx.animation.TranslateTransition tt = new javafx.animation.TranslateTransition(javafx.util.Duration.millis(90), n);
                        tt.setByX(xOff);
                        tt.setByY(yOff);
                        tt.setAutoReverse(true);
                        tt.setCycleCount(2);
                        tt.setInterpolator(javafx.animation.Interpolator.EASE_BOTH);
                        tt.play();

                        javafx.animation.Timeline alpha = new javafx.animation.Timeline(
                            new javafx.animation.KeyFrame(javafx.util.Duration.ZERO, new KeyValue(n.opacityProperty(), n.getOpacity())),
                            new javafx.animation.KeyFrame(javafx.util.Duration.millis(60), new KeyValue(n.opacityProperty(), Math.max(0.2, Math.random()))),
                            new javafx.animation.KeyFrame(javafx.util.Duration.millis(120), new KeyValue(n.opacityProperty(), 1.0))
                        );
                        alpha.play();

                        // briefly increase the DropShadow radius to create a white-hot CRT pop
                        if (n instanceof Text && ((Text) n).getEffect() instanceof DropShadow) {
                            DropShadow ds = (DropShadow) ((Text) n).getEffect();
                            javafx.animation.Timeline dsPulse = new javafx.animation.Timeline(
                                new javafx.animation.KeyFrame(javafx.util.Duration.ZERO, new KeyValue(ds.radiusProperty(), ds.getRadius())),
                                new javafx.animation.KeyFrame(javafx.util.Duration.millis(100), new KeyValue(ds.radiusProperty(), ds.getRadius() + 30)),
                                new javafx.animation.KeyFrame(javafx.util.Duration.millis(160), new KeyValue(ds.radiusProperty(), ds.getRadius()))
                            );
                            dsPulse.play();
                        }
                    }
                }

                // brief scanline flicker effect
                javafx.animation.Timeline scan = new javafx.animation.Timeline(
                    new javafx.animation.KeyFrame(javafx.util.Duration.ZERO, new KeyValue(scanline.opacityProperty(), 0.0), new KeyValue(scanline.translateYProperty(), titleContainer.getLayoutY())),
                    new javafx.animation.KeyFrame(javafx.util.Duration.millis(40), new KeyValue(scanline.opacityProperty(), 1.0)),
                    new javafx.animation.KeyFrame(javafx.util.Duration.millis(160), new KeyValue(scanline.translateYProperty(), titleContainer.getLayoutY() + 32)),
                    new javafx.animation.KeyFrame(javafx.util.Duration.millis(220), new KeyValue(scanline.opacityProperty(), 0.0))
                );
                scan.play();

            });

            glitchTimeline.getKeyFrames().add(kf);
        }

        // After glitches, bring the year text and proceed to reveal buttons
        double revealTime = baseTime + bursts * 180 + 220;
        javafx.animation.KeyFrame revealKF = new javafx.animation.KeyFrame(Duration.millis(revealTime), e -> {
            // show year
            if (yearText != null) {
                FadeTransition fy = new FadeTransition(Duration.millis(260), yearText);
                yearText.setOpacity(0);
                fy.setToValue(1.0);
                fy.play();
            }

            // remove scanline after a small delay
            javafx.animation.Timeline removeScan = new javafx.animation.Timeline(new javafx.animation.KeyFrame(javafx.util.Duration.millis(420), ev -> rootPane.getChildren().remove(scanline)));
            removeScan.play();

            // Reveal main menu buttons one by one with neon glow fade-in
            List<Node> revealButtons = new ArrayList<>();
            if (playButton != null) revealButtons.add(playButton);
            if (quitButton != null) revealButtons.add(quitButton);
            if (controlsButton != null) revealButtons.add(controlsButton);
            if (settingsButton != null) revealButtons.add(settingsButton);

            double startDelay = 0;
            for (Node b : revealButtons) {
                b.setOpacity(0.0);
                b.setVisible(true);
                javafx.animation.FadeTransition ft = new javafx.animation.FadeTransition(Duration.millis(360), b);
                ft.setToValue(1.0);
                ft.setDelay(javafx.util.Duration.millis(startDelay));

                // Slight pop and translate
                javafx.animation.TranslateTransition tt = new javafx.animation.TranslateTransition(Duration.millis(360), b);
                tt.setFromY(10);
                tt.setToY(0);
                tt.setDelay(javafx.util.Duration.millis(startDelay));

                // Do not add an extra reveal glow here — keep the original hover/responsive effects
                // Finalize to enable interactivity after last button revealed
                ft.play();
                tt.play();
                // no extra glow Pulse

                startDelay += 120; // stagger
            }

            // Re-enable interactivity after full reveal
            javafx.animation.Timeline reenable = new javafx.animation.Timeline(new javafx.animation.KeyFrame(javafx.util.Duration.millis(startDelay + 420), ev2 -> {
                rootPane.setDisable(false);
                finalizeLaunchState();
            }));
            reenable.play();

        });

        // Chain the sequences
        // First: per-letter pre-appearance pop-in so the logo "assembles" politely before the CRT glitches.
        // Each letter gets a short fade+overshoot scale, a tiny rotation settle and a neon DropShadow power-up pulse.
        javafx.animation.ParallelTransition preTitle = new javafx.animation.ParallelTransition();
        // Also bring the overall container to alpha 1 so letter-level fades are visible.
        if (titleContainer != null) {
            javafx.animation.FadeTransition containerFade = new javafx.animation.FadeTransition(javafx.util.Duration.millis(90), titleContainer);
            containerFade.setFromValue(titleContainer.getOpacity());
            containerFade.setToValue(1.0);
            preTitle.getChildren().add(containerFade);
        }
        if (titleContainer != null) {
            int idx = 0;
            for (Node n : titleContainer.getChildren()) {
                // ensure starting values
                n.setOpacity(0.0);
                n.setScaleX(0.65);
                n.setScaleY(0.65);
                n.setTranslateY(18);

                // Slightly different durations and an overshoot make the letters pop in more satisfyingly
                javafx.animation.FadeTransition f = new javafx.animation.FadeTransition(javafx.util.Duration.millis(220), n);
                f.setToValue(1.0);
                f.setDelay(javafx.util.Duration.millis(idx * 68));
                f.setInterpolator(javafx.animation.Interpolator.EASE_OUT);

                javafx.animation.ScaleTransition s = new javafx.animation.ScaleTransition(javafx.util.Duration.millis(300), n);
                s.setFromX(0.65);
                s.setFromY(0.65);
                s.setToX(1.08); // slight overshoot
                s.setToY(1.08);
                s.setDelay(javafx.util.Duration.millis(idx * 68));
                s.setInterpolator(javafx.animation.Interpolator.SPLINE(0.2, 0.85, 0.25, 1.0));

                // settle back gently after the overshoot
                javafx.animation.ScaleTransition settle = new javafx.animation.ScaleTransition(javafx.util.Duration.millis(140), n);
                settle.setFromX(1.08);
                settle.setFromY(1.08);
                settle.setToX(1.0);
                settle.setToY(1.0);
                settle.setDelay(javafx.util.Duration.millis(idx * 68 + 260));
                settle.setInterpolator(javafx.animation.Interpolator.EASE_OUT);

                // small translate pop
                javafx.animation.TranslateTransition t = new javafx.animation.TranslateTransition(javafx.util.Duration.millis(300), n);
                t.setFromY(18);
                t.setToY(0);
                t.setDelay(javafx.util.Duration.millis(idx * 68));
                t.setInterpolator(javafx.animation.Interpolator.EASE_OUT);

                // tiny rotation jitter then settle to 0 to feel like pieces snapping together
                double rotStart = (Math.random() - 0.5) * 12.0; // random -6..6
                n.setRotate(rotStart);
                javafx.animation.RotateTransition rot = new javafx.animation.RotateTransition(javafx.util.Duration.millis(360), n);
                rot.setToAngle(0);
                rot.setDelay(javafx.util.Duration.millis(idx * 68 + 20));
                rot.setInterpolator(javafx.animation.Interpolator.EASE_OUT);

                // Neon power-up pulse on the letter's DropShadow effect (if present)
                if (n instanceof Text && ((Text) n).getEffect() instanceof DropShadow) {
                    DropShadow ds = (DropShadow) ((Text) n).getEffect();
                    // ensure starting radius is small so pulse is noticeable
                    double originalRadius = ds.getRadius();
                    ds.setRadius(Math.max(2.0, originalRadius * 0.12));
                    javafx.animation.Timeline dsPulse = new javafx.animation.Timeline(
                        new javafx.animation.KeyFrame(javafx.util.Duration.ZERO, new javafx.animation.KeyValue(ds.radiusProperty(), ds.getRadius())),
                        new javafx.animation.KeyFrame(javafx.util.Duration.millis(120 + idx * 24), new javafx.animation.KeyValue(ds.radiusProperty(), Math.max(20, originalRadius * 1.5))),
                        new javafx.animation.KeyFrame(javafx.util.Duration.millis(320 + idx * 24), new javafx.animation.KeyValue(ds.radiusProperty(), Math.max(10, originalRadius)))
                    );
                    dsPulse.setDelay(javafx.util.Duration.millis(0));
                    preTitle.getChildren().add(dsPulse);
                }

                preTitle.getChildren().addAll(f, s, settle, t, rot);
                idx++;
            }
        }

        preTitle.setOnFinished(ev -> showTitle.play());
        showTitle.setOnFinished(ev -> settleTitle.play());
        settleTitle.setOnFinished(ev -> glitchTimeline.play());
        preTitle.play();
        glitchTimeline.getKeyFrames().add(revealKF);
    }

    private void setupControlLight() {
        if (controlLight == null) return;
        controlLight.setOnMouseClicked(e -> toggleControllerPanel());
        controlLight.setCursor(javafx.scene.Cursor.HAND);
        // Hover pulse/glow for the image if available
        if (controlLightImage != null) {
            controlLightImage.setOnMouseEntered(e -> {
                javafx.animation.ScaleTransition st = new javafx.animation.ScaleTransition(javafx.util.Duration.millis(220), controlLightImage);
                st.setToX(1.25);
                st.setToY(1.25);
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
                st.setToX(1.15);
                st.setToY(1.15);
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
            javafx.scene.image.ImageView bulbImage = new javafx.scene.image.ImageView(new javafx.scene.image.Image(getClass().getResourceAsStream("/originallight-bulb-png.png")));
            bulbImage.setFitWidth(65);
            bulbImage.setFitHeight(65);
            bulbImage.setPreserveRatio(true);
            // Combine Glow and DropShadow for higher intensity
            DropShadow dropShadow = new DropShadow();
            dropShadow.setColor(Color.YELLOW);
            dropShadow.setRadius(5);
            dropShadow.setSpread(0.2);
            Glow glow = new Glow(0.2);
            dropShadow.setInput(glow);
            bulbImage.setEffect(dropShadow);
            
            // Flicker timeline for hover effect
            javafx.animation.Timeline flickerTimeline = new javafx.animation.Timeline(
                new javafx.animation.KeyFrame(javafx.util.Duration.millis(0), 
                    new javafx.animation.KeyValue(glow.levelProperty(), 1.0),
                    new javafx.animation.KeyValue(dropShadow.radiusProperty(), 15)),
                new javafx.animation.KeyFrame(javafx.util.Duration.millis(100), 
                    new javafx.animation.KeyValue(glow.levelProperty(), 0.8),
                    new javafx.animation.KeyValue(dropShadow.radiusProperty(), 12)),
                new javafx.animation.KeyFrame(javafx.util.Duration.millis(200), 
                    new javafx.animation.KeyValue(glow.levelProperty(), 1.0),
                    new javafx.animation.KeyValue(dropShadow.radiusProperty(), 15)),
                new javafx.animation.KeyFrame(javafx.util.Duration.millis(300), 
                    new javafx.animation.KeyValue(glow.levelProperty(), 0.9),
                    new javafx.animation.KeyValue(dropShadow.radiusProperty(), 13)),
                new javafx.animation.KeyFrame(javafx.util.Duration.millis(400), 
                    new javafx.animation.KeyValue(glow.levelProperty(), 1.0),
                    new javafx.animation.KeyValue(dropShadow.radiusProperty(), 15))
            );
            flickerTimeline.setCycleCount(javafx.animation.Animation.INDEFINITE);
            
            // Add smooth hover effect for enhanced glow
            controlsButton.setOnMouseEntered(event -> {
                // Set to max glow and start flickering
                glow.setLevel(1.0);
                dropShadow.setRadius(15);
                flickerTimeline.play();
            });
            controlsButton.setOnMouseExited(event -> {
                // Stop flickering and fade out to dim glow
                flickerTimeline.stop();
                javafx.animation.Timeline glowOut = new javafx.animation.Timeline(
                    new javafx.animation.KeyFrame(javafx.util.Duration.ZERO, 
                        new javafx.animation.KeyValue(glow.levelProperty(), glow.getLevel()),
                        new javafx.animation.KeyValue(dropShadow.radiusProperty(), dropShadow.getRadius())),
                    new javafx.animation.KeyFrame(javafx.util.Duration.millis(250), 
                        new javafx.animation.KeyValue(glow.levelProperty(), 0.2),
                        new javafx.animation.KeyValue(dropShadow.radiusProperty(), 5))
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
            // Combine Glow and DropShadow for higher intensity
            DropShadow dropShadow = new DropShadow();
            dropShadow.setColor(Color.YELLOW);
            dropShadow.setRadius(5);
            dropShadow.setSpread(0.2);
            Glow glow = new Glow(0.2);
            dropShadow.setInput(glow);
            bulb.setEffect(dropShadow);
            bulb.setScaleX(1.3);
            bulb.setScaleY(1.3);
            
            // Flicker timeline for hover effect
            javafx.animation.Timeline flickerTimeline = new javafx.animation.Timeline(
                new javafx.animation.KeyFrame(javafx.util.Duration.millis(0), 
                    new javafx.animation.KeyValue(glow.levelProperty(), 1.0),
                    new javafx.animation.KeyValue(dropShadow.radiusProperty(), 15)),
                new javafx.animation.KeyFrame(javafx.util.Duration.millis(100), 
                    new javafx.animation.KeyValue(glow.levelProperty(), 0.8),
                    new javafx.animation.KeyValue(dropShadow.radiusProperty(), 12)),
                new javafx.animation.KeyFrame(javafx.util.Duration.millis(200), 
                    new javafx.animation.KeyValue(glow.levelProperty(), 1.0),
                    new javafx.animation.KeyValue(dropShadow.radiusProperty(), 15)),
                new javafx.animation.KeyFrame(javafx.util.Duration.millis(300), 
                    new javafx.animation.KeyValue(glow.levelProperty(), 0.9),
                    new javafx.animation.KeyValue(dropShadow.radiusProperty(), 13)),
                new javafx.animation.KeyFrame(javafx.util.Duration.millis(400), 
                    new javafx.animation.KeyValue(glow.levelProperty(), 1.0),
                    new javafx.animation.KeyValue(dropShadow.radiusProperty(), 15))
            );
            flickerTimeline.setCycleCount(javafx.animation.Animation.INDEFINITE);
            
            // Add smooth hover effect for SVG fallback too
            controlsButton.setOnMouseEntered(event -> {
                // Set to max glow and start flickering
                glow.setLevel(1.0);
                dropShadow.setRadius(15);
                flickerTimeline.play();
            });
            controlsButton.setOnMouseExited(event -> {
                // Stop flickering and fade out to dim glow
                flickerTimeline.stop();
                javafx.animation.Timeline glowOut = new javafx.animation.Timeline(
                    new javafx.animation.KeyFrame(javafx.util.Duration.ZERO, 
                        new javafx.animation.KeyValue(glow.levelProperty(), glow.getLevel()),
                        new javafx.animation.KeyValue(dropShadow.radiusProperty(), dropShadow.getRadius())),
                    new javafx.animation.KeyFrame(javafx.util.Duration.millis(250), 
                        new javafx.animation.KeyValue(glow.levelProperty(), 0.2),
                        new javafx.animation.KeyValue(dropShadow.radiusProperty(), 5))
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
            javafx.scene.image.ImageView speakerImage = new javafx.scene.image.ImageView(new javafx.scene.image.Image(getClass().getResourceAsStream("/newWhiteSpeaker.png")));
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
            // If level selection overlay is visible, remove its blur and restore opacity
            if (levelSelectionContainer != null && levelSelectionContainer.isVisible()) {
                levelSelectionContainer.setEffect(null);
                levelSelectionContainer.setOpacity(1.0);
            }
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
            // If the level selection overlay is present, blur it as well so the guide stands out
            if (levelSelectionContainer != null && levelSelectionContainer.isVisible()) {
                levelSelectionContainer.setOpacity(0.3);
                levelSelectionContainer.setEffect(new GaussianBlur(15));
            }
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
            neonFont = javafx.scene.text.Font.loadFont(getClass().getResourceAsStream("/fonts/PressStart2P-vaV7.ttf"), 10);
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
                // If we added a dim over the level selection, remove it
                if (settingsDim != null && rootPane.getChildren().contains(settingsDim)) {
                    rootPane.getChildren().remove(settingsDim);
                }
            } else {
                // Show overlay with Elastic Pop-up effect
                // If level selection overlay is visible, add a dim behind the settings overlay
                if (levelSelectionContainer != null && levelSelectionContainer.isVisible()) {
                    if (settingsDim == null) {
                        settingsDim = new javafx.scene.shape.Rectangle();
                        // make the dim lighter for less visual obstruction
                        settingsDim.setFill(javafx.scene.paint.Color.web("#000000", 0.35));
                        settingsDim.widthProperty().bind(rootPane.widthProperty());
                        settingsDim.heightProperty().bind(rootPane.heightProperty());
                        settingsDim.setOnMouseClicked(ev -> closeSettings());
                    }
                    // Ensure dim is placed above levelSelectionContainer and below settingsOverlay
                    if (!rootPane.getChildren().contains(settingsDim)) {
                        rootPane.getChildren().add(settingsDim);
                    }
                    // Bring settingsOverlay to front so it sits above the dim
                    if (rootPane.getChildren().contains(settingsOverlay)) {
                        rootPane.getChildren().remove(settingsOverlay);
                    }
                    rootPane.getChildren().add(settingsOverlay);
                    // Also softly dim the level selection container for better contrast
                    levelSelectionContainer.setEffect(new javafx.scene.effect.GaussianBlur(8));
                    levelSelectionContainer.setOpacity(0.6);
                }

                settingsOverlay.setVisible(true);
                settingsOverlay.setOpacity(0);
                settingsOverlay.setScaleX(0.5); // Start small
                settingsOverlay.setScaleY(0.5);

                // Fade In
                javafx.animation.FadeTransition fadeIn = new javafx.animation.FadeTransition(Duration.millis(300), settingsOverlay);
                fadeIn.setFromValue(0.0);
                fadeIn.setToValue(1.0);

                // Elastic Scale (using a Timeline to simulate a spring bounce)
                javafx.animation.Timeline bounce = new javafx.animation.Timeline(
                    new javafx.animation.KeyFrame(Duration.millis(0), 
                        new javafx.animation.KeyValue(settingsOverlay.scaleXProperty(), 0.5), 
                        new javafx.animation.KeyValue(settingsOverlay.scaleYProperty(), 0.5)
                    ),
                    new javafx.animation.KeyFrame(Duration.millis(200), 
                        new javafx.animation.KeyValue(settingsOverlay.scaleXProperty(), 1.1) // Overshoot to 110%
                    ), 
                    new javafx.animation.KeyFrame(Duration.millis(200), 
                        new javafx.animation.KeyValue(settingsOverlay.scaleYProperty(), 1.1)
                    ),
                    new javafx.animation.KeyFrame(Duration.millis(400), 
                        new javafx.animation.KeyValue(settingsOverlay.scaleXProperty(), 1.0, javafx.animation.Interpolator.EASE_OUT) // Settle at 100%
                    ),
                    new javafx.animation.KeyFrame(Duration.millis(400), 
                        new javafx.animation.KeyValue(settingsOverlay.scaleYProperty(), 1.0, javafx.animation.Interpolator.EASE_OUT)
                    )
                );

                javafx.animation.ParallelTransition popUp = new javafx.animation.ParallelTransition(fadeIn, bounce);
                popUp.play();
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
            // Remove dim overlay if present
            if (settingsDim != null && rootPane.getChildren().contains(settingsDim)) {
                rootPane.getChildren().remove(settingsDim);
            }
            // Restore level selection appearance if present
            if (levelSelectionContainer != null && levelSelectionContainer.isVisible()) {
                levelSelectionContainer.setEffect(null);
                levelSelectionContainer.setOpacity(1.0);
            }
        }
    }

    private void setupTitle() {
        String title = "TETRIS";
        Color[] titleColors = {Color.BLUE, Color.RED, Color.YELLOW, Color.LIME, Color.MAGENTA, Color.ORANGE};

        javafx.scene.text.Font customFont = null;
        try {
            // Force the logo font size to 130 so it visibly enlarges regardless of metrics
            customFont = javafx.scene.text.Font.loadFont(getClass().getResourceAsStream("/fonts/AXR ArcadeMachine.ttf"), 130);
            if (customFont != null) {
                System.out.println("Loaded custom font: " + customFont.getName());
            } else {
                System.err.println("Failed to load AXR ArcadeMachine.ttf");
            }
        } catch (Exception e) { 
            System.err.println("Exception loading font: " + e.getMessage());
        }
        
        if (customFont == null) {
            try {
                customFont = javafx.scene.text.Font.loadFont(getClass().getResourceAsStream("/fonts/Arcadia-SVG.ttf"), 130);
            } catch (Exception e) { /* Ignore */ }
        }
        if (customFont == null) {
            try {
                customFont = javafx.scene.text.Font.loadFont(getClass().getResourceAsStream("/Retro Pixel.otf"), 130);
            } catch (Exception e) { /* Ignore */ }
        }
        if (customFont == null) {
            try {
                customFont = javafx.scene.text.Font.loadFont(getClass().getResourceAsStream("/fonts/PressStart2P-vaV7.ttf"), 130);
            } catch (Exception e) { /* Ignore */ }
        }
        if (customFont == null) {
            try {
                customFont = javafx.scene.text.Font.loadFont(getClass().getResourceAsStream("/fonts/digital.ttf"), 80);
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
            // Force explicit CSS font-size and family to avoid font-metric mismatches
            try {
                String family = customFont.getName();
                letter.setStyle("-fx-font-size: 130px; -fx-font-family: '" + family + "';");
            } catch (Exception ignored) {
                letter.setStyle("-fx-font-size: 130px;");
            }

            // Add glow effect; increase radius to complement bigger font
            DropShadow glow = new DropShadow();
            glow.setColor(titleColors[i % titleColors.length]);
            glow.setRadius(16);
            glow.setSpread(0.25);
            letter.setEffect(glow);

            titleContainer.getChildren().add(letter);
        }
        

        for (Node node : titleContainer.getChildren()) {
            if (node instanceof Text) {
                Text letter = (Text) node;
                Timeline flicker = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(((DropShadow)letter.getEffect()).radiusProperty(), 10.0)),
                    new KeyFrame(Duration.millis(100), new KeyValue(((DropShadow)letter.getEffect()).radiusProperty(), 20.0)),
                    new KeyFrame(Duration.millis(200), new KeyValue(((DropShadow)letter.getEffect()).radiusProperty(), 10.0)),
                    new KeyFrame(Duration.millis(300), new KeyValue(((DropShadow)letter.getEffect()).radiusProperty(), 15.0)),
                    new KeyFrame(Duration.millis(400), new KeyValue(((DropShadow)letter.getEffect()).radiusProperty(), 10.0))
                );
                flicker.setCycleCount(Timeline.INDEFINITE);
                titleFlickers.add(flicker);
                letter.setOnMouseEntered(e -> flicker.play());
                letter.setOnMouseExited(e -> {
                    flicker.stop();
                    ((DropShadow)letter.getEffect()).setRadius(10.0);
                });
            }
        }

        // Tight spacing between large letters per request
        titleContainer.setSpacing(3);
        // Move the title up slightly so it sits higher on the menu
        titleContainer.setTranslateY(-12);

        javafx.scene.text.Font yearFont = null;
        try {
            // Prefer a slightly larger PressStart2P font for the year text
            yearFont = javafx.scene.text.Font.loadFont(getClass().getResourceAsStream("/fonts/PressStart2P-vaV7.ttf"), 15);
        } catch (Exception e) { /* Ignore */ }
        
        if (yearFont == null) {
             try {
                yearFont = javafx.scene.text.Font.loadFont(getClass().getResourceAsStream("/fonts/AXR ArcadeMachine.ttf"), 40);
            } catch (Exception e) { /* Ignore */ }
        }
        if (yearFont == null) {
             try {
                yearFont = javafx.scene.text.Font.loadFont(getClass().getResourceAsStream("/fonts/Arcadia-SVG.ttf"), 40);
            } catch (Exception e) { /* Ignore */ }
        }
        if (yearFont == null) {
             try {
                yearFont = javafx.scene.text.Font.loadFont(getClass().getResourceAsStream("/fonts/NineByFiveNbp-MypB.ttf"), 40);
            } catch (Exception e) { /* Ignore */ }
        }
        if (yearFont == null) {
             try {
                yearFont = javafx.scene.text.Font.loadFont(getClass().getResourceAsStream("/fonts/digital.ttf"), 40);
            } catch (Exception e) { /* Ignore */ }
        }
        if (yearFont == null) yearFont = javafx.scene.text.Font.font("Arial", 40);
        
        yearText.setFont(yearFont);
        yearText.setFill(Color.WHITE);
        // remove glow for a flatter arcade look
        yearText.setEffect(null);
        // force a CSS size as well in case font metrics differ
        try {
            yearText.setStyle("-fx-font-size: 20px; -fx-font-family: '" + yearFont.getName() + "';");
        } catch (Exception ignored) {
            yearText.setStyle("-fx-font-size: 20px;");
        }

        // Position the year text directly beneath the TETRIS title and keep it centered.
        Runnable positionYear = () -> {
            Platform.runLater(() -> {
                try {
                    double titleX = titleContainer.getLayoutX() + titleContainer.getTranslateX();
                    double titleY = titleContainer.getLayoutY() + titleContainer.getTranslateY();
                    double titleW = titleContainer.getBoundsInParent().getWidth();
                    double titleH = titleContainer.getBoundsInParent().getHeight();
                    double yearW = yearText.getBoundsInParent().getWidth();
                    // center year under title
                    yearText.setLayoutX(titleX + Math.max(0, (titleW - yearW) / 2.0));
                    yearText.setLayoutY(titleY + titleH + 6); // small gap
                } catch (Exception ignored) {
                    // ignore layout timing issues
                }
            });
        };

        // Run once after initial layout
        positionYear.run();

        // Update when title or rootPane change size/position
        titleContainer.boundsInParentProperty().addListener((obs, oldVal, newVal) -> positionYear.run());
        rootPane.widthProperty().addListener((obs, oldVal, newVal) -> positionYear.run());
    }

    private void setupButtons() {
        javafx.scene.text.Font buttonFont = null;
        try {
            buttonFont = javafx.scene.text.Font.loadFont(getClass().getResourceAsStream("/fonts/PressStart2P-vaV7.ttf"), 16);
        } catch (Exception e) { /* Ignore */ }
        
        if (buttonFont != null) {
            playButton.setFont(buttonFont);
            quitButton.setFont(buttonFont);
        }

        // Move the play and quit buttons down slightly for better spacing under the title
        if (playButton != null) playButton.setTranslateY(10);
        if (quitButton != null) quitButton.setTranslateY(10);

        setupButtonAnimation(playButton);
        setupButtonAnimation(quitButton);
    }

    private void setupButtonAnimation(Button button) {
        if (button == null) return;
        button.setCursor(javafx.scene.Cursor.HAND);

        // Neon glow drop shadow that will be pulsed
        DropShadow glow = new DropShadow();
        Color effectColor = (button == playButton) ? Color.web("#00ff99", 0.95) : Color.web("#ff6b6b", 0.95);
        glow.setColor(effectColor);
        glow.setRadius(8);
        glow.setSpread(0.28);
        glow.setOffsetX(0);
        glow.setOffsetY(0);

        // Scale transitions for enter/exit
        javafx.animation.ScaleTransition scaleUp = new javafx.animation.ScaleTransition(javafx.util.Duration.millis(160), button);
        scaleUp.setToX(1.06);
        scaleUp.setToY(1.06);
        scaleUp.setInterpolator(javafx.animation.Interpolator.EASE_OUT);

        javafx.animation.ScaleTransition scaleDown = new javafx.animation.ScaleTransition(javafx.util.Duration.millis(120), button);
        scaleDown.setToX(1.0);
        scaleDown.setToY(1.0);
        scaleDown.setInterpolator(javafx.animation.Interpolator.EASE_IN);

        // Pulse timeline to animate glow radius for a subtle neon breathing effect
        Timeline pulse = new Timeline(
            new KeyFrame(javafx.util.Duration.ZERO, new KeyValue(glow.radiusProperty(), 8.0)),
            new KeyFrame(javafx.util.Duration.millis(180), new KeyValue(glow.radiusProperty(), 20.0)),
            new KeyFrame(javafx.util.Duration.millis(360), new KeyValue(glow.radiusProperty(), 10.0))
        );
        pulse.setCycleCount(javafx.animation.Animation.INDEFINITE);

        // Mouse handlers
        button.setOnMouseEntered(e -> {
            button.setEffect(glow);
            scaleUp.playFromStart();
            pulse.play();
        });

        button.setOnMouseExited(e -> {
            // stop pulsing then fade the glow out smoothly
            pulse.stop();
            Timeline fadeOut = new Timeline(
                new KeyFrame(javafx.util.Duration.ZERO, new KeyValue(glow.radiusProperty(), glow.getRadius())),
                new KeyFrame(javafx.util.Duration.millis(220), new KeyValue(glow.radiusProperty(), 0.0))
            );
            fadeOut.setOnFinished(ev -> button.setEffect(null));
            fadeOut.play();
            scaleDown.playFromStart();
        });

        // Press feedback
        button.setOnMousePressed(e -> {
            button.setScaleX(button.getScaleX() * 0.98);
            button.setScaleY(button.getScaleY() * 0.98);
        });
        button.setOnMouseReleased(e -> {
            // restore to hovered scale if still hovering, otherwise to normal
            if (button.isHover()) {
                button.setScaleX(1.06);
                button.setScaleY(1.06);
            } else {
                button.setScaleX(1.0);
                button.setScaleY(1.0);
            }
        });

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
        if (random.nextDouble() < 0.03) { // 3% chance per frame as requested
            spawnFallingShape();
        }

        // Spawn particles
        if (random.nextDouble() < 0.18) { // 18% chance - more frequent particles
            spawnParticle();
        }

        // (Rotating ring effect removed per request)

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

        // (No parallax stars)
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

        // Spawn burst of colored particles for arcade effect
        for (int i = 0; i < 4; i++) {
            double px = x + (random.nextDouble() - 0.5) * 50;
            double py = -80 + random.nextDouble() * 30;
            Rectangle p = new Rectangle(2 + random.nextDouble() * 2, 2 + random.nextDouble() * 2, color);
            p.setOpacity(0.8);
            p.setTranslateX(px);
            p.setTranslateY(py);
            p.setEffect(new Glow(0.7));
            backgroundPane.getChildren().add(p);
            particles.add(new Particle(p));
        }
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
        
        // Use neon colors for arcade theme instead of just white
        Color particleColor = random.nextDouble() < 0.3 ? Color.WHITE : NEON_COLORS[random.nextInt(NEON_COLORS.length)];
        Rectangle p = new Rectangle(2, 2, particleColor);
        p.setOpacity(random.nextDouble() * 0.7 + 0.2); // Slightly more opaque for visibility
        p.setTranslateX(x);
        p.setTranslateY(y);
        // Add subtle glow to ambient particles
        if (random.nextDouble() < 0.4) {
            p.setEffect(new Glow(0.3));
        }
        
        backgroundPane.getChildren().add(p);
        particles.add(new Particle(p));
    }

    // Rotating ring effect removed — other visual features retained

    @FXML
    private void onPlay() {
        // Show level selection instead of directly loading the game
        showLevelSelection();
    }

    /**
    * Display the Level Selection view with three mode buttons: Classic, Rush, Mystery.
     * Hide the title and main buttons during this state.
     */
    private void showLevelSelection() {
        // Hide the title container and year text
        titleContainer.setVisible(false);
        yearText.setVisible(false);
        
        // Hide the main menu buttons
        playButton.setVisible(false);
        quitButton.setVisible(false);
        
        // Create or show the level selection container as an overlay so bottom controls are not affected
        if (levelSelectionContainer == null) {
            levelSelectionContainer = createLevelSelectionPanel();
            // set a preferred size so it doesn't stretch the layout
            levelSelectionContainer.setMaxWidth(480);
            levelSelectionContainer.setMaxHeight(520);
            levelSelectionContainer.setOpacity(0);
            levelSelectionContainer.setVisible(false);
            // place on rootPane as overlay
            rootPane.getChildren().add(levelSelectionContainer);
            javafx.scene.layout.StackPane.setAlignment(levelSelectionContainer, javafx.geometry.Pos.CENTER);
        }

        // Show container
        levelSelectionContainer.setVisible(true);
        levelSelectionContainer.setOpacity(1.0);

        // ANIMATE CHILDREN (Title + Buttons) ONE BY ONE
        int delay = 0;
        for (javafx.scene.Node child : levelSelectionContainer.getChildren()) {
            child.setOpacity(0);
            child.setTranslateY(20); // Start lower

            javafx.animation.FadeTransition ft = new javafx.animation.FadeTransition(Duration.millis(400), child);
            ft.setToValue(1.0);
            
            javafx.animation.TranslateTransition tt = new javafx.animation.TranslateTransition(Duration.millis(400), child);
            tt.setToY(0);
            tt.setInterpolator(javafx.animation.Interpolator.EASE_OUT);

            javafx.animation.ParallelTransition entry = new javafx.animation.ParallelTransition(ft, tt);
            entry.setDelay(Duration.millis(delay)); // Stagger delays
            entry.play();
            
            delay += 100; // Next item appears 100ms later
        }
    }

    /**
     * Create the level selection panel with three buttons and a back button.
     */
    private javafx.scene.layout.VBox createLevelSelectionPanel() {
        javafx.scene.layout.VBox container = new javafx.scene.layout.VBox();
        container.setAlignment(javafx.geometry.Pos.CENTER);
        container.setSpacing(20);
        
        // Title for level selection
        javafx.scene.text.Text levelTitle = new javafx.scene.text.Text("SELECT MODE");
        levelTitle.setFill(Color.web("#00ff99"));
        levelTitle.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");
        levelTitle.setEffect(new DropShadow(12, Color.web("#00ff99", 0.5)));
        
        // Classic button
        Button classicButton = createLevelButton("CLASSIC", "CLASSIC");
        
        // Rush button
        Button timedButton = createLevelButton("RUSH", "RUSH");
        
        // Mystery button
        Button mysteryButton = createLevelButton("MYSTERY", "MYSTERY");
        
        // Back button (styled like Play/Quit buttons)
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
        setupButtonAnimation(backButton);
        backButton.setOnAction(e -> hideLevelSelection());
        
        container.getChildren().addAll(levelTitle, classicButton, timedButton, mysteryButton);
        
        // Add back button in its own HBox at the bottom with some spacing
        javafx.scene.layout.HBox backButtonBox = new javafx.scene.layout.HBox();
        backButtonBox.setAlignment(javafx.geometry.Pos.CENTER);
        backButtonBox.setPadding(new javafx.geometry.Insets(30, 0, 0, 0));
        backButtonBox.getChildren().add(backButton);
        container.getChildren().add(backButtonBox);
        
        return container;
    }

    /**
     * Create a level selection button with animation and styling matching Play/Quit buttons.
    * Colors: Blue for Classic, Green for Rush, Purple for Mystery.
     */
    private Button createLevelButton(String text, String mode) {
        Button button = new Button(text);
        
        // Set styling based on mode with different colors
        String borderColor;
        String textColor;
        String gradientColor1;
        String gradientColor2;
        String shadowColor;
        
        switch(mode) {
            case "CLASSIC":
                // Blue for Classic
                borderColor = "rgba(100,180,255,0.95)";
                textColor = "#e3f2ff";
                gradientColor1 = "rgba(100,180,255,0.12)";
                gradientColor2 = "rgba(70,140,220,0.06)";
                shadowColor = "rgba(100,180,255,0.25)";
                break;
            case "RUSH":
                // Green for Rush
                borderColor = "rgba(100,230,150,0.95)";
                textColor = "#e7ffde";
                gradientColor1 = "rgba(100,230,150,0.12)";
                gradientColor2 = "rgba(70,190,120,0.06)";
                shadowColor = "rgba(100,230,150,0.25)";
                break;
            case "MYSTERY":
                // Purple for Mystery
                borderColor = "rgba(200,120,255,0.95)";
                textColor = "#f3e5ff";
                gradientColor1 = "rgba(200,120,255,0.12)";
                gradientColor2 = "rgba(170,90,220,0.06)";
                shadowColor = "rgba(200,120,255,0.25)";
                break;
            default:
                borderColor = "rgba(100,180,255,0.95)";
                textColor = "#e3f2ff";
                gradientColor1 = "rgba(100,180,255,0.12)";
                gradientColor2 = "rgba(70,140,220,0.06)";
                shadowColor = "rgba(100,180,255,0.25)";
        }
        
        // Apply consistent styling with Play/Quit buttons but different colors
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
        // Ensure text is centered even for multiline
        button.setAlignment(javafx.geometry.Pos.CENTER);
        button.setWrapText(true);
        button.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        setupButtonAnimation(button);
        // Attach a descriptive tooltip matching the neon arcade theme
        String tipText;
        switch (mode) {
            case "CLASSIC":
                tipText = "Standard rules with steady progression and traditional scoring — focus on precise stacking to build high scores.";
                break;
            case "RUSH":
                tipText = "A timed challenge: clear as many lines as you can before the clock runs out. Faster gravity increases the pressure.";
                break;
            case "MYSTERY":
                tipText = "Unpredictable piece sequences and increased speed create a chaotic, high-intensity experience for skilled players.";
                break;
            default:
                tipText = "Standard rules with steady progression and traditional scoring.";
        }

        javafx.scene.control.Tooltip tooltip = new javafx.scene.control.Tooltip(tipText);
        // Neat neon styling to fit the game's theme
        tooltip.setWrapText(true);
        tooltip.setMaxWidth(260);
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

        button.setOnAction(e -> loadGameSceneWithMode(mode));
        return button;
    }

    /**
     * Hide the level selection and restore the main menu.
     */
    private void hideLevelSelection() {
        if (levelSelectionContainer != null) {
            FadeTransition fadeOut = new FadeTransition(Duration.millis(300), levelSelectionContainer);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.setOnFinished(e -> {
                levelSelectionContainer.setVisible(false);
                // Remove overlay from rootPane so it no longer affects layout
                if (rootPane.getChildren().contains(levelSelectionContainer)) {
                    rootPane.getChildren().remove(levelSelectionContainer);
                    // recreate so next open will re-add cleanly
                    levelSelectionContainer = null;
                }
                // Restore visibility of main menu elements and make sure title letters are restored
                titleContainer.setVisible(true);
                // Ensure any per-letter properties are reset (covers case where launch animation had been used)
                finalizeLaunchState();
                yearText.setVisible(true);
                playButton.setVisible(true);
                quitButton.setVisible(true);
            });
            fadeOut.play();
        }
    }

    /**
     * Load the game scene with the specified mode.
     * Instantiates the appropriate game controller based on mode.
     */
    private void loadGameSceneWithMode(String mode) {
        selectedGameMode = mode;
    
        // Create a "Warp" effect: Scale up + Fade out
        javafx.animation.ParallelTransition warpTransition = new javafx.animation.ParallelTransition();

        // 1. Zoom into the screen (Scale X/Y from 1.0 to 3.0)
        javafx.animation.ScaleTransition zoom = new javafx.animation.ScaleTransition(Duration.millis(500), rootPane);
        zoom.setFromX(1.0);
        zoom.setFromY(1.0);
        zoom.setToX(3.0);
        zoom.setToY(3.0);
        zoom.setInterpolator(javafx.animation.Interpolator.EASE_IN); // Accelerate into the warp

        // 2. Fade out
        javafx.animation.FadeTransition fade = new javafx.animation.FadeTransition(Duration.millis(500), rootPane);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);
        fade.setInterpolator(javafx.animation.Interpolator.EASE_IN);

        warpTransition.getChildren().addAll(zoom, fade);
        warpTransition.setOnFinished(e -> loadGameScene());
        warpTransition.play();
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
            
            // Instantiate the appropriate game controller based on selected mode
            BaseGameController gameController;
            if ("RUSH".equals(selectedGameMode)) {
                gameController = new TimedGameController(decoratedView);
            } else if ("MYSTERY".equals(selectedGameMode)) {
                gameController = new MysteryGameController(decoratedView);
            } else {
                // Default to Classic
                gameController = new ClassicGameController(decoratedView);
            }
            
            // Keep reference to prevent GC
            @SuppressWarnings("unused")
            BaseGameController activeController = gameController;
            
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
