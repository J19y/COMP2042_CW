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
 * Animated message shown when rows are cleared.
 * Displays title text with fade and translate animations.
 */
public final class RowClearMessage extends StackPane {

    private static final Font TITLE_FONT = Font.font("AXR ArcadeMachine", 22);
    
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
        
        titleText.getStyleClass().add("floating-score");
        String upper = title.toUpperCase();
        if (upper.contains("SWEEP") || upper.contains("TETRA") || upper.contains("TRIPLE") || title.length() > 14) {
            titleText.getStyleClass().add("floating-score-high");
        }

        
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

    
    public static void showCustom(Group container, String message) {
        if (container == null || message == null || message.isEmpty()) return;
        RowClearMessage msg = new RowClearMessage(message);
        msg.play(container);
    }

    private RowClearMessage(String message) {
        setPickOnBounds(false);
        setMouseTransparent(true);
        setOpacity(0.0);
        setAlignment(Pos.CENTER);

        titleText = new Text(message);

        
        

        
        titleText.getStyleClass().add("floating-score-event");
        String upper = message.toUpperCase();
        if (upper.contains("SWEEP") || upper.contains("TETRA") || upper.contains("TRIPLE") || message.length() > 14) {
            titleText.getStyleClass().add("floating-score-event-high");
        }

        
        
        try {
            javafx.scene.text.Font press = javafx.scene.text.Font.loadFont(getClass().getResourceAsStream("/fonts/PressStart2P-vaV7.ttf"), 26);
            if (press != null) {
                titleText.setFont(press);
            }
        } catch (Exception ignored) {}

        
        if (titleText.getStyleClass().contains("floating-score-event-high")) {
            titleText.setFill(Color.web("#FF6BE0"));
        } else {
            titleText.setFill(Color.web("#FFE082"));
        }
        getChildren().add(titleText);
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

        
        setScaleX(1.0);
        setScaleY(1.0);
        setOpacity(1.0);

        
        TranslateTransition floatUp = new TranslateTransition(TOTAL_DURATION, this);
        floatUp.setByY(-50);

        
        FadeTransition fade = new FadeTransition(Duration.millis(500), this);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);
        fade.setDelay(Duration.millis(500));

        
        floatUp.play();
        fade.setOnFinished(e -> cleanup());
        fade.play();
    }

    private void cleanup() {
        Parent parent = getParent();
        switch (parent) {
            case Group groupParent -> groupParent.getChildren().remove(this);
            case StackPane stack -> stack.getChildren().remove(this);
            case Pane pane -> pane.getChildren().remove(this);
            default -> {}
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
        return switch (lines) {
            case 4 -> "TETRA-CORE";
            case 3 -> "TRIPLE CASCADE";
            case 2 -> "DOUBLE PULSE";
            default -> "SINGLE SWEEP";
        };
    }

}

