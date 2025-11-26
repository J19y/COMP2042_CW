package com.comp2042.tetris.ui.view;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PauseTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.List;

public final class RowClearMessage extends StackPane {

    private static final Font TITLE_FONT = Font.font("Orbitron", FontWeight.BOLD, 20);
    private static final Font SUBTITLE_FONT = Font.font("Segoe UI", FontWeight.SEMI_BOLD, 10);
    private static final Duration ENTRANCE = Duration.millis(220);
    private static final Duration HOLD = Duration.millis(920);
    private static final Duration EXIT = Duration.millis(260);

    private final Timeline barPulse;
    private final List<Rectangle> pulseBars = new ArrayList<>();

    private RowClearMessage(int lines) {
        setPickOnBounds(false);
        setOpacity(0);
        setAlignment(Pos.CENTER);

        Color accent = accentForLines(lines);

        Text title = new Text(titleForLines(lines));
        title.setFill(Color.WHITE);
        title.setFont(TITLE_FONT);

        Text subtitle = new Text(subtitleForLines(lines));
        subtitle.setFill(accent.deriveColor(0, 1, 1, 0.8));
        subtitle.setFont(SUBTITLE_FONT);

        VBox textHolder = new VBox(4, title, subtitle);
        textHolder.setAlignment(Pos.CENTER);

        HBox barRow = new HBox(6);
        barRow.setAlignment(Pos.CENTER);
        for (int i = 0; i < 5; i++) {
            Rectangle bar = new Rectangle(12, 24);
            bar.setArcWidth(6);
            bar.setArcHeight(6);
            bar.setFill(accent.deriveColor(0, 1, 1, 0.7));
            bar.setStroke(Color.TRANSPARENT);
            bar.setScaleY(0.45);
            pulseBars.add(bar);
            barRow.getChildren().add(bar);
        }

        Rectangle backdrop = new Rectangle(260, 98);
        backdrop.setArcWidth(28);
        backdrop.setArcHeight(28);
        backdrop.setFill(new LinearGradient(0, 0, 1, 1, true, javafx.scene.paint.CycleMethod.NO_CYCLE,
            new Stop(0, Color.web("#03060f", 0.95)),
            new Stop(1, Color.web("#0b1420", 0.92))));
        backdrop.setStroke(accent.deriveColor(0, 1, 1, 0.9));
        backdrop.setStrokeWidth(2.5);
        DropShadow glow = new DropShadow(24, accent.deriveColor(0, 1, 1, 0.7));
        glow.setSpread(0.24);
        backdrop.setEffect(glow);

        VBox content = new VBox(10, textHolder, barRow);
        content.setAlignment(Pos.CENTER);

        getChildren().addAll(backdrop, content);

        barPulse = createBarPulse();
    }

    public static void show(Group container, int lines) {
        if (container == null || lines <= 0) {
            return;
        }
        RowClearMessage message = new RowClearMessage(lines);
        message.play(container);
    }

    private void play(Group container) {
        Node target = findNotificationStack(container);
        if (target instanceof StackPane stack) {
            stack.getChildren().add(this);
            StackPane.setAlignment(this, Pos.TOP_CENTER);
            setTranslateY(52);
        } else {
            container.getChildren().add(this);
            setTranslateY(-100);
        }

        barPulse.play();

        FadeTransition fadeIn = new FadeTransition(ENTRANCE, this);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        ScaleTransition pop = new ScaleTransition(ENTRANCE, this);
        pop.setFromX(0.76);
        pop.setFromY(0.76);
        pop.setToX(1.04);
        pop.setToY(1.04);

        ScaleTransition settle = new ScaleTransition(Duration.millis(120), this);
        settle.setFromX(1.04);
        settle.setFromY(1.04);
        settle.setToX(1.0);
        settle.setToY(1.0);

        PauseTransition hold = new PauseTransition(HOLD);

        FadeTransition fadeOut = new FadeTransition(EXIT, this);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);

        ParallelTransition entrance = new ParallelTransition(fadeIn, pop);
        SequentialTransition sequence = new SequentialTransition(entrance, settle, hold, fadeOut);
        sequence.setOnFinished(e -> cleanup());
        sequence.play();
    }

    private void cleanup() {
        barPulse.stop();
        Parent parent = getParent();
        if (parent instanceof Group groupParent) {
            groupParent.getChildren().remove(this);
        } else if (parent instanceof StackPane stack) {
            stack.getChildren().remove(this);
        } else if (parent instanceof Pane pane) {
            pane.getChildren().remove(this);
        }
    }

    private Timeline createBarPulse() {
        Timeline timeline = new Timeline();
        for (int i = 0; i < pulseBars.size(); i++) {
            Rectangle bar = pulseBars.get(i);
            Duration offset = Duration.millis(i * 70);
            timeline.getKeyFrames().addAll(
                new KeyFrame(offset, new KeyValue(bar.scaleYProperty(), 0.45), new KeyValue(bar.opacityProperty(), 0.5)),
                new KeyFrame(offset.add(Duration.millis(180)), new KeyValue(bar.scaleYProperty(), 1.25), new KeyValue(bar.opacityProperty(), 1.0)),
                new KeyFrame(offset.add(Duration.millis(320)), new KeyValue(bar.scaleYProperty(), 0.55), new KeyValue(bar.opacityProperty(), 0.6))
            );
        }
        timeline.setCycleCount(Animation.INDEFINITE);
        return timeline;
    }

    private static Node findNotificationStack(Group container) {
        for (Node child : container.getChildren()) {
            if (child instanceof StackPane) {
                return child;
            }
        }
        return null;
    }

    private static String titleForLines(int lines) {
        switch (lines) {
            case 4:
                return "TETRA-CORE";
            case 3:
                return "TRIPLE CASCADE";
            case 2:
                return "DOUBLE PULSE";
            default:
                return "SINGLE SWEEP";
        }
    }

    private static String subtitleForLines(int lines) {
        return "Matrix sync: " + lines + (lines == 1 ? " row" : " rows");
    }

    private static Color accentForLines(int lines) {
        switch (lines) {
            case 4:
                return Color.web("#ff5f3c");
            case 3:
                return Color.web("#f97316");
            case 2:
                return Color.web("#38bdf8");
            default:
                return Color.web("#a5b4fc");
        }
    }
}
