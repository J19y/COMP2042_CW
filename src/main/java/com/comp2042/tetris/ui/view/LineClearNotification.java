package com.comp2042.tetris.ui.view;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * Animated notification for line clear achievements.
 * Shows title and subtitle with color accent and effects.
 */
public class LineClearNotification extends StackPane {

    private final Text title;
    private final Text subtitle;
    private final Rectangle backdrop;

    public LineClearNotification(String titleText, String subtitleText, Color accent) {
        setPickOnBounds(false);
        setAlignment(Pos.CENTER);

        this.backdrop = new Rectangle(180, 56);
        this.backdrop.setArcWidth(12);
        this.backdrop.setArcHeight(12);
        this.backdrop.setFill(new javafx.scene.paint.LinearGradient(0, 0, 1, 0, true, javafx.scene.paint.CycleMethod.NO_CYCLE,
                new javafx.scene.paint.Stop(0, Color.web("#041126", 0.94)),
                new javafx.scene.paint.Stop(1, Color.web("#07131b", 0.94))));
        this.backdrop.setStroke(accent.deriveColor(0, 1, 1, 1.0));
        this.backdrop.setStrokeWidth(3);
        DropShadow glow = new DropShadow(18, accent.deriveColor(0, 1, 1, 0.6));
        glow.setSpread(0.18);
        this.backdrop.setEffect(glow);

        title = new Text(titleText);
        title.getStyleClass().add("line-reward-title");
        title.setFill(Color.WHITE);
        title.setFont(Font.font("Segoe UI Semibold", FontWeight.BOLD, 18));

        subtitle = new Text(subtitleText != null ? subtitleText : "");
        subtitle.getStyleClass().add("line-reward-subtitle");
        subtitle.setFill(accent);
        subtitle.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 10));

        javafx.scene.layout.VBox content = new javafx.scene.layout.VBox(4);
        content.setAlignment(Pos.CENTER);
        content.getChildren().addAll(title, subtitle);

        
        this.backdrop.widthProperty().bind(content.widthProperty().add(32));
        this.backdrop.heightProperty().bind(content.heightProperty().add(18));
        this.backdrop.setMouseTransparent(true);

        getChildren().addAll(this.backdrop, content);
        StackPane.setAlignment(content, Pos.CENTER);
        setOpacity(0);
    }

    public void show(Group containerChildren) {
        if (containerChildren == null) return;

        
        javafx.scene.Node target = null;
        for (javafx.scene.Node n : containerChildren.getChildren()) {
            if (n instanceof javafx.scene.layout.StackPane) {
                target = n;
                break;
            }
        }

        if (target instanceof javafx.scene.layout.StackPane parentStack) {
            parentStack.getChildren().add(this);
            
            javafx.scene.layout.StackPane.setAlignment(this, javafx.geometry.Pos.TOP_CENTER);
            
            setTranslateY(48);
        } else {
            containerChildren.getChildren().add(this);
        }

        
        ScaleTransition pop = new ScaleTransition(Duration.millis(120), this);
        pop.setFromX(0.7);
        pop.setFromY(0.7);
        pop.setToX(1.08);
        pop.setToY(1.08);
        pop.setInterpolator(Interpolator.EASE_OUT);

        ScaleTransition settle = new ScaleTransition(Duration.millis(80), this);
        settle.setFromX(1.08);
        settle.setFromY(1.08);
        settle.setToX(1.0);
        settle.setToY(1.0);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(100), this);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);

        
        DropShadow glowEffect = (DropShadow) backdrop.getEffect();
        Timeline pulseGlow = new Timeline(
            new KeyFrame(Duration.ZERO, new KeyValue(glowEffect.radiusProperty(), 18)),
            new KeyFrame(Duration.millis(120), new KeyValue(glowEffect.radiusProperty(), 32)),
            new KeyFrame(Duration.millis(240), new KeyValue(glowEffect.radiusProperty(), 18))
        );
        pulseGlow.setCycleCount(2);
        pulseGlow.setAutoReverse(true);

        PauseTransition hold = new PauseTransition(Duration.millis(240));

        
        TranslateTransition floatUp = new TranslateTransition(Duration.millis(500), this);
        floatUp.setByY(-50);
        FadeTransition fadeOut = new FadeTransition(Duration.millis(500), this);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);

        SequentialTransition seq = new SequentialTransition(new ParallelTransition(pop, fadeIn), settle,
                new ParallelTransition(hold, pulseGlow), new ParallelTransition(floatUp, fadeOut));

        seq.setOnFinished(e -> {
            
            switch (getParent()) {
                case Group group -> group.getChildren().remove(this);
                case javafx.scene.layout.StackPane stackPane -> stackPane.getChildren().remove(this);
                default -> {}
            }
        });

        seq.play();
    }

    public static void spawnConfetti(Group container) {
        if (container == null) return;
        for (int i = 0; i < 14; i++) {
            Rectangle r = new Rectangle(6, 10);
            r.setArcWidth(2);
            r.setArcHeight(2);
            r.setFill(Color.hsb((i * 32) % 360, 0.85, 0.95));
            r.setTranslateX((Math.random() - 0.5) * 220);
            r.setTranslateY(0);
            container.getChildren().add(r);

            TranslateTransition tt = new TranslateTransition(Duration.millis(800 + (long)(Math.random()*400)), r);
            tt.setByY(140 + Math.random()*80);
            tt.setByX((Math.random()-0.5)*160);
            tt.setInterpolator(Interpolator.EASE_IN);

            RotateTransition rt = new RotateTransition(Duration.millis(700 + (long)(Math.random()*600)), r);
            rt.setByAngle(360 * (Math.random() > 0.5 ? 1 : -1));

            FadeTransition fd = new FadeTransition(Duration.millis(300), r);
            fd.setFromValue(1.0);
            fd.setToValue(0.0);
            fd.setDelay(Duration.millis(700 + Math.random()*300));

            ParallelTransition p = new ParallelTransition(tt, rt, fd);
            final Node node = r;
            p.setOnFinished(ev -> container.getChildren().remove(node));
            p.play();
        }
    }
}

