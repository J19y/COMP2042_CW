package com.comp2042.tetris.ui.view;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * RowClearMessage - simplified neon floating text variant.
 * Replaces the previous boxed backdrop and pulsing bars with a single
 * floating, glowing text that uses the `.floating-score` / `.floating-score-high`
 * CSS rules defined in `window.css`.
 */
public final class RowClearMessage extends StackPane {

    private static final Font TITLE_FONT = Font.font("AXR ArcadeMachine", 22);
    // Total duration increased so the message remains readable and aligned with effects
    private static final Duration TOTAL_DURATION = Duration.millis(1000);

    private final Text titleText;

    private RowClearMessage(int lines) {
        setPickOnBounds(false);
        setMouseTransparent(true);
        setOpacity(0.0);
        setAlignment(Pos.CENTER);

        String title = titleForLines(lines);
        titleText = new Text(title);
        titleText.setFont(TITLE_FONT);
        // Apply CSS class for neon styling; large combos get the high variant
        titleText.getStyleClass().add("floating-score");
        String upper = title.toUpperCase();
        if (upper.contains("SWEEP") || upper.contains("TETRA") || upper.contains("TRIPLE") || title.length() > 14) {
            titleText.getStyleClass().add("floating-score-high");
        }

        // Ensure the base text color uses black for readability
        titleText.setFill(Color.BLACK);

        getChildren().add(titleText);
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

        // Start immediately visible, then float and fade
        setScaleX(1.0);
        setScaleY(1.0);
        setOpacity(1.0);

        // Float up by -50px over TOTAL_DURATION (1000ms)
        TranslateTransition floatUp = new TranslateTransition(TOTAL_DURATION, this);
        floatUp.setByY(-50);

        // Fade: start at halfway (delay 500ms) and last 500ms so text is readable
        FadeTransition fade = new FadeTransition(Duration.millis(500), this);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);
        fade.setDelay(Duration.millis(500));

        // Play float and fade immediately
        floatUp.play();
        fade.setOnFinished(e -> cleanup());
        fade.play();
    }

    private void cleanup() {
        Parent parent = getParent();
        if (parent instanceof Group groupParent) {
            groupParent.getChildren().remove(this);
        } else if (parent instanceof StackPane stack) {
            stack.getChildren().remove(this);
        } else if (parent instanceof Pane pane) {
            pane.getChildren().remove(this);
        }
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

}
