package com.comp2042.tetris.ui.controller;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;


public class MenuAnimationController {
    private final StackPane rootPane;
    private final HBox titleContainer;
    private final Text yearText;
    private final Button playButton;
    private final Button quitButton;
    private final Button controlsButton;
    private final Button settingsButton;
    private final BorderPane mainMenuUI;

    private static boolean launchPlayed = false;

    public MenuAnimationController(
            StackPane rootPane,
            HBox titleContainer,
            Text yearText,
            Button playButton,
            Button quitButton,
            Button controlsButton,
            Button settingsButton,
            BorderPane mainMenuUI) {
        this.rootPane = rootPane;
        this.titleContainer = titleContainer;
        this.yearText = yearText;
        this.playButton = playButton;
        this.quitButton = quitButton;
        this.controlsButton = controlsButton;
        this.settingsButton = settingsButton;
        this.mainMenuUI = mainMenuUI;
    }

    
    public void prepareLaunchForAnimation() {
        if (titleContainer != null) {
            titleContainer.setOpacity(0.0);
            titleContainer.setScaleX(0.96);
            titleContainer.setScaleY(0.96);
        }
        if (yearText != null) {
            yearText.setOpacity(0.0);
        }

        Button[] uiButtons = new Button[]{playButton, quitButton, controlsButton, settingsButton};
        for (Button b : uiButtons) {
            if (b != null) {
                b.setOpacity(0.0);
                b.setDisable(true);
                b.setMouseTransparent(true);
            }
        }

        if (titleContainer != null) {
            for (Node n : titleContainer.getChildren()) {
                n.setOpacity(0.0);
                n.setScaleX(0.65);
                n.setScaleY(0.65);
                n.setTranslateY(18);
            }
        }
    }

    
    public void finalizeLaunchState() {
        if (titleContainer != null) {
            titleContainer.setOpacity(1.0);
            titleContainer.setScaleX(1.0);
            titleContainer.setScaleY(1.0);

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

    
    public boolean shouldPlayLaunchAnimation() {
        if (!launchPlayed) {
            launchPlayed = true;
            return true;
        }
        return false;
    }

    
    public void playLaunchAnimation() {
        rootPane.setDisable(true);

        
        Timeline showTitle = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(titleContainer.opacityProperty(), 0.0),
                        new KeyValue(titleContainer.scaleXProperty(), 0.96),
                        new KeyValue(titleContainer.scaleYProperty(), 0.96)),
                new KeyFrame(Duration.millis(520),
                        new KeyValue(titleContainer.opacityProperty(), 1.0, javafx.animation.Interpolator.EASE_OUT),
                        new KeyValue(titleContainer.scaleXProperty(), 1.02, javafx.animation.Interpolator.EASE_OUT),
                        new KeyValue(titleContainer.scaleYProperty(), 1.02, javafx.animation.Interpolator.EASE_OUT))
        );

        Timeline settleTitle = new Timeline(
                new KeyFrame(Duration.millis(0), new KeyValue(titleContainer.scaleXProperty(), 1.02), new KeyValue(titleContainer.scaleYProperty(), 1.02)),
                new KeyFrame(Duration.millis(160), new KeyValue(titleContainer.scaleXProperty(), 1.0), new KeyValue(titleContainer.scaleYProperty(), 1.0))
        );

        
        Rectangle scanline = new Rectangle();
        scanline.setWidth(rootPane.getWidth());
        scanline.setHeight(10);
        scanline.setFill(Color.web("#ffffff", 0.06));
        scanline.setBlendMode(javafx.scene.effect.BlendMode.SRC_OVER);
        scanline.setOpacity(0);
        double titleY = titleContainer.getLayoutY() + titleContainer.getTranslateY();
        scanline.setTranslateY(titleY - 12);

        rootPane.getChildren().add(scanline);

        Timeline glitchTimeline = new Timeline();
        int bursts = 4;
        double baseTime = 600;
        for (int i = 0; i < bursts; i++) {
            double t0 = baseTime + i * 180;
            KeyFrame kf = new KeyFrame(Duration.millis(t0), e -> playGlitchBurst(scanline));
            glitchTimeline.getKeyFrames().add(kf);
        }

        
        double revealTime = baseTime + bursts * 180 + 220;
        KeyFrame revealKF = new KeyFrame(Duration.millis(revealTime), e -> revealElements(scanline));
        glitchTimeline.getKeyFrames().add(revealKF);

        
        ParallelTransition preTitle = createLetterAnimations();

        preTitle.setOnFinished(ev -> showTitle.play());
        showTitle.setOnFinished(ev -> settleTitle.play());
        settleTitle.setOnFinished(ev -> glitchTimeline.play());
        preTitle.play();
    }

    
    private void playGlitchBurst(Rectangle scanline) {
        int letters = titleContainer.getChildren().size();
        for (int li = 0; li < letters; li++) {
            if (Math.random() < 0.45) {
                Node n = titleContainer.getChildren().get(li);
                double xOff = (Math.random() - 0.5) * 18.0;
                double yOff = (Math.random() - 0.5) * 6.0;

                TranslateTransition tt = new TranslateTransition(Duration.millis(90), n);
                tt.setByX(xOff);
                tt.setByY(yOff);
                tt.setAutoReverse(true);
                tt.setCycleCount(2);
                tt.setInterpolator(javafx.animation.Interpolator.EASE_BOTH);
                tt.play();

                Timeline alpha = new Timeline(
                        new KeyFrame(Duration.ZERO, new KeyValue(n.opacityProperty(), n.getOpacity())),
                        new KeyFrame(Duration.millis(60), new KeyValue(n.opacityProperty(), Math.max(0.2, Math.random()))),
                        new KeyFrame(Duration.millis(120), new KeyValue(n.opacityProperty(), 1.0))
                );
                alpha.play();

                if (n instanceof Text && ((Text) n).getEffect() instanceof DropShadow) {
                    DropShadow ds = (DropShadow) ((Text) n).getEffect();
                    Timeline dsPulse = new Timeline(
                            new KeyFrame(Duration.ZERO, new KeyValue(ds.radiusProperty(), ds.getRadius())),
                            new KeyFrame(Duration.millis(100), new KeyValue(ds.radiusProperty(), ds.getRadius() + 30)),
                            new KeyFrame(Duration.millis(160), new KeyValue(ds.radiusProperty(), ds.getRadius()))
                    );
                    dsPulse.play();
                }
            }
        }

        Timeline scan = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(scanline.opacityProperty(), 0.0), new KeyValue(scanline.translateYProperty(), titleContainer.getLayoutY())),
                new KeyFrame(Duration.millis(40), new KeyValue(scanline.opacityProperty(), 1.0)),
                new KeyFrame(Duration.millis(160), new KeyValue(scanline.translateYProperty(), titleContainer.getLayoutY() + 32)),
                new KeyFrame(Duration.millis(220), new KeyValue(scanline.opacityProperty(), 0.0))
        );
        scan.play();
    }

    
    private void revealElements(Rectangle scanline) {
        if (yearText != null) {
            FadeTransition fy = new FadeTransition(Duration.millis(260), yearText);
            yearText.setOpacity(0);
            fy.setToValue(1.0);
            fy.play();
        }

        Timeline removeScan = new Timeline(new KeyFrame(Duration.millis(420), ev -> rootPane.getChildren().remove(scanline)));
        removeScan.play();

        List<Node> revealButtons = new ArrayList<>();
        if (playButton != null) revealButtons.add(playButton);
        if (quitButton != null) revealButtons.add(quitButton);
        if (controlsButton != null) revealButtons.add(controlsButton);
        if (settingsButton != null) revealButtons.add(settingsButton);

        double startDelay = 0;
        for (Node b : revealButtons) {
            b.setOpacity(0.0);
            b.setVisible(true);

            FadeTransition ft = new FadeTransition(Duration.millis(360), b);
            ft.setToValue(1.0);
            ft.setDelay(Duration.millis(startDelay));

            TranslateTransition tt = new TranslateTransition(Duration.millis(360), b);
            tt.setFromY(10);
            tt.setToY(0);
            tt.setDelay(Duration.millis(startDelay));

            ft.play();
            tt.play();

            startDelay += 120;
        }

        Timeline reenable = new Timeline(new KeyFrame(Duration.millis(startDelay + 420), ev2 -> {
            rootPane.setDisable(false);
            finalizeLaunchState();
        }));
        reenable.play();
    }

    
    private ParallelTransition createLetterAnimations() {
        ParallelTransition preTitle = new ParallelTransition();

        if (titleContainer != null) {
            FadeTransition containerFade = new FadeTransition(Duration.millis(90), titleContainer);
            containerFade.setFromValue(titleContainer.getOpacity());
            containerFade.setToValue(1.0);
            preTitle.getChildren().add(containerFade);
        }

        if (titleContainer != null) {
            int idx = 0;
            for (Node n : titleContainer.getChildren()) {
                n.setOpacity(0.0);
                n.setScaleX(0.65);
                n.setScaleY(0.65);
                n.setTranslateY(18);

                FadeTransition f = new FadeTransition(Duration.millis(220), n);
                f.setToValue(1.0);
                f.setDelay(Duration.millis(idx * 68));
                f.setInterpolator(javafx.animation.Interpolator.EASE_OUT);

                ScaleTransition s = new ScaleTransition(Duration.millis(300), n);
                s.setFromX(0.65);
                s.setFromY(0.65);
                s.setToX(1.08);
                s.setToY(1.08);
                s.setDelay(Duration.millis(idx * 68));
                s.setInterpolator(javafx.animation.Interpolator.SPLINE(0.2, 0.85, 0.25, 1.0));

                ScaleTransition settle = new ScaleTransition(Duration.millis(140), n);
                settle.setFromX(1.08);
                settle.setFromY(1.08);
                settle.setToX(1.0);
                settle.setToY(1.0);
                settle.setDelay(Duration.millis(idx * 68 + 260));
                settle.setInterpolator(javafx.animation.Interpolator.EASE_OUT);

                TranslateTransition t = new TranslateTransition(Duration.millis(300), n);
                t.setFromY(18);
                t.setToY(0);
                t.setDelay(Duration.millis(idx * 68));
                t.setInterpolator(javafx.animation.Interpolator.EASE_OUT);

                double rotStart = (Math.random() - 0.5) * 12.0;
                n.setRotate(rotStart);
                RotateTransition rot = new RotateTransition(Duration.millis(360), n);
                rot.setToAngle(0);
                rot.setDelay(Duration.millis(idx * 68 + 20));
                rot.setInterpolator(javafx.animation.Interpolator.EASE_OUT);

                if (n instanceof Text && ((Text) n).getEffect() instanceof DropShadow) {
                    DropShadow ds = (DropShadow) ((Text) n).getEffect();
                    double originalRadius = ds.getRadius();
                    ds.setRadius(Math.max(2.0, originalRadius * 0.12));

                    Timeline dsPulse = new Timeline(
                            new KeyFrame(Duration.ZERO, new KeyValue(ds.radiusProperty(), ds.getRadius())),
                            new KeyFrame(Duration.millis(120 + idx * 24), new KeyValue(ds.radiusProperty(), Math.max(20, originalRadius * 1.5))),
                            new KeyFrame(Duration.millis(320 + idx * 24), new KeyValue(ds.radiusProperty(), Math.max(10, originalRadius)))
                    );
                    dsPulse.setDelay(Duration.ZERO);
                    preTitle.getChildren().add(dsPulse);
                }

                preTitle.getChildren().addAll(f, s, settle, t, rot);
                idx++;
            }
        }

        return preTitle;
    }

    
    public void playGameSceneWarpTransition(Runnable onComplete) {
        ScaleTransition zoom = new ScaleTransition(Duration.millis(500), rootPane);
        zoom.setFromX(1.0);
        zoom.setFromY(1.0);
        zoom.setToX(3.0);
        zoom.setToY(3.0);
        zoom.setInterpolator(javafx.animation.Interpolator.EASE_IN);

        FadeTransition fade = new FadeTransition(Duration.millis(500), rootPane);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);
        fade.setInterpolator(javafx.animation.Interpolator.EASE_IN);

        ParallelTransition warpTransition = new ParallelTransition(zoom, fade);
        warpTransition.setOnFinished(e -> onComplete.run());
        warpTransition.play();
    }

    
    public void blurMenuUI() {
        mainMenuUI.setEffect(new javafx.scene.effect.GaussianBlur(10));
    }

    
    public void clearMenuBlur() {
        mainMenuUI.setEffect(null);
    }
}



