package com.comp2042.tetris.ui.view;

import javafx.animation.FadeTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class GameOverPanel extends BorderPane {

    private final Label gameOverLabel;
    private final Label scoreLabel;
    private final Button retryButton;
    private final Button menuButton;
    private final FadeTransition fadeTransition;
    private Runnable retryAction = () -> {};
    private Runnable returnAction = () -> {};

    public GameOverPanel() {
        gameOverLabel = new Label("GAME OVER");
        gameOverLabel.getStyleClass().add("game-over-text");
        scoreLabel = new Label("Final Score: 0");
        scoreLabel.getStyleClass().add("game-over-score");

        retryButton = new Button("↺");
        menuButton = new Button("⌂");
        retryButton.getStyleClass().add("menu-button");
        menuButton.getStyleClass().add("menu-button");
        retryButton.getStyleClass().add("icon-button");
        menuButton.getStyleClass().add("icon-button");
        retryButton.setDisable(true);
        menuButton.setDisable(true);

        retryButton.setOnAction(e -> {
            hide();
            retryAction.run();
        });
        menuButton.setOnAction(e -> {
            hide();
            returnAction.run();
        });

        HBox actions = new HBox(12, retryButton, menuButton);
        actions.setAlignment(Pos.CENTER);
        actions.getStyleClass().add("game-over-actions");

        VBox content = new VBox(18, gameOverLabel, scoreLabel, actions);
        content.setAlignment(Pos.CENTER);
        setCenter(content);
        getStyleClass().add("game-over-panel");
        setOpacity(0);
        setVisible(false);

        fadeTransition = new FadeTransition(Duration.millis(250), this);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);

        resetLabelState();
    }
    
    public void show(int finalScore) {
        scoreLabel.setText("Final Score: " + finalScore);
        fadeTransition.stop();
        setOpacity(0);
        super.setVisible(true);
        retryButton.setDisable(false);
        menuButton.setDisable(false);
        fadeTransition.playFromStart();
    }
    
    public void hide() {
        fadeTransition.stop();
        super.setVisible(false);
        resetLabelState();
        retryButton.setDisable(true);
        menuButton.setDisable(true);
    }
    private void resetLabelState() {
        gameOverLabel.setScaleX(1);
        gameOverLabel.setScaleY(1);
        gameOverLabel.setOpacity(1);
        gameOverLabel.setTranslateY(0);
        gameOverLabel.setTranslateX(0);
        gameOverLabel.setText("GAME OVER");
        scoreLabel.setText("Final Score: 0");
    }

    public void setOnRetry(Runnable retryAction) {
        this.retryAction = retryAction != null ? retryAction : () -> {};
    }

    public void setOnReturnToMenu(Runnable returnAction) {
        this.returnAction = returnAction != null ? returnAction : () -> {};
    }

}
