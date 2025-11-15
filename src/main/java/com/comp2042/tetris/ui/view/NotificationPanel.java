package com.comp2042.tetris.ui.view;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class NotificationPanel extends BorderPane {
    // Added private static final constants for magic numbers
    private static final double MIN_HEIGHT = 200;
    private static final double MIN_WIDTH = 220;
    private static final double GLOW_INTENSITY = 0.6;
    
    private final Label scoreLabel;

    // Now the constructor does only the main setup
    public NotificationPanel(String text) {
        setMinHeight(MIN_HEIGHT);
        setMinWidth(MIN_WIDTH);
        
        scoreLabel = createScoreLabel(text);
        setCenter(scoreLabel);
    }
    
    // Created another method to create and configure the score label
    private Label createScoreLabel(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("bonusStyle");
        
        Effect glow = new Glow(GLOW_INTENSITY);
        label.setEffect(glow);
        label.setTextFill(Color.WHITE);
        
        return label;
    }

    public void showScore(ObservableList<Node> list) {
        FadeTransition ft = new FadeTransition(Duration.millis(2000), this);
        TranslateTransition tt = new TranslateTransition(Duration.millis(2500), this);
        tt.setToY(this.getLayoutY() - 40);
        ft.setFromValue(1);
        ft.setToValue(0);
        ParallelTransition transition = new ParallelTransition(tt, ft);
        transition.setOnFinished(_ -> list.remove(NotificationPanel.this));
        transition.play();
    }
}
