package com.comp2042.ui;

import java.net.URL;
import java.util.ResourceBundle;

import com.comp2042.event.EventSource;
import com.comp2042.event.EventType;
import com.comp2042.event.InputEventListener;
import com.comp2042.event.MoveEvent;
import com.comp2042.model.ShowResult;
import com.comp2042.model.ViewData;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class GuiController implements Initializable {

    private static final int BRICK_SIZE = 20;

    @FXML
    private GridPane gamePanel;

    @FXML
    private Group groupNotification;

    @FXML
    private GridPane brickPanel;

    @FXML
    private GameOverPanel gameOverPanel;

    private transient Rectangle[][] displayMatrix;

    private InputEventListener eventListener;

    private transient Rectangle[][] rectangles;

    private transient Timeline timeLine;

    private final transient BooleanProperty isPause = new SimpleBooleanProperty(false);

    private final transient BooleanProperty isGameOver = new SimpleBooleanProperty(false);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Font.loadFont(getClass().getClassLoader().getResource("digital.ttf").toExternalForm(), 38);
        
        if (gamePanel != null) {
            gamePanel.setFocusTraversable(true);
            gamePanel.requestFocus();
            gamePanel.setOnKeyPressed(keyEvent -> {
                if (!isPause.getValue() && !isGameOver.getValue()) {
                    if (keyEvent.getCode() == KeyCode.LEFT || keyEvent.getCode() == KeyCode.A) {
                        refreshBrick(eventListener.onLeftEvent(new MoveEvent(EventType.LEFT, EventSource.USER)));
                        keyEvent.consume();
                    }
                    if (keyEvent.getCode() == KeyCode.RIGHT || keyEvent.getCode() == KeyCode.D) {
                        refreshBrick(eventListener.onRightEvent(new MoveEvent(EventType.RIGHT, EventSource.USER)));
                        keyEvent.consume();
                    }
                    if (keyEvent.getCode() == KeyCode.UP || keyEvent.getCode() == KeyCode.W) {
                        refreshBrick(eventListener.onRotateEvent(new MoveEvent(EventType.ROTATE, EventSource.USER)));
                        keyEvent.consume();
                    }
                    if (keyEvent.getCode() == KeyCode.DOWN || keyEvent.getCode() == KeyCode.S) {
                        moveDown(new MoveEvent(EventType.DOWN, EventSource.USER));
                        keyEvent.consume();
                    }
                }
                if (keyEvent.getCode() == KeyCode.N) {
                    newGame(null);
                }
            });
        }
        
        if (gameOverPanel != null) {
            gameOverPanel.setVisible(false);
        }
    }

    @FXML
    public void initGameView(int[][] boardMatrix, ViewData brick) {
        displayMatrix = new Rectangle[boardMatrix.length][boardMatrix[0].length];
        for (int i = 2; i < boardMatrix.length; i++) {
            for (int j = 0; j < boardMatrix[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(Color.TRANSPARENT);
                displayMatrix[i][j] = rectangle;
                if (gamePanel != null) {
                    gamePanel.add(rectangle, j, i - 2);
                }
            }
        }

        rectangles = new Rectangle[brick.getBrickData().length][brick.getBrickData()[0].length];
        for (int i = 0; i < brick.getBrickData().length; i++) {
            for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(getFillColor(brick.getBrickData()[i][j]));
                rectangles[i][j] = rectangle;
                if (brickPanel != null) {
                    brickPanel.add(rectangle, j, i);
                }
            }
        }
        if (brickPanel != null && gamePanel != null) {
            brickPanel.setLayoutX(gamePanel.getLayoutX() + brick.getxPosition() * brickPanel.getVgap() + brick.getxPosition() * BRICK_SIZE);
            brickPanel.setLayoutY(-42 + gamePanel.getLayoutY() + brick.getyPosition() * brickPanel.getHgap() + brick.getyPosition() * BRICK_SIZE);
        }

        timeLine = new Timeline(new KeyFrame(
                Duration.millis(400),
                _ -> moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD))
        ));
        timeLine.setCycleCount(Timeline.INDEFINITE);
        timeLine.play();
    }

    private Paint getFillColor(int i) {
            return switch (i) {
                case 0 -> Color.TRANSPARENT;
                case 1 -> Color.AQUA;
                case 2 -> Color.BLUEVIOLET;
                case 3 -> Color.DARKGREEN;
                case 4 -> Color.YELLOW;
                case 5 -> Color.RED;
                case 6 -> Color.BEIGE;
                case 7 -> Color.BURLYWOOD;
                default -> Color.WHITE;
            };
    }

    private void refreshBrick(ViewData brick) {
        if (isPause == null || !isPause.getValue()) {
            if (brickPanel != null && gamePanel != null) {
                brickPanel.setLayoutX(gamePanel.getLayoutX() + brick.getxPosition() * brickPanel.getVgap() + brick.getxPosition() * BRICK_SIZE);
                brickPanel.setLayoutY(-42 + gamePanel.getLayoutY() + brick.getyPosition() * brickPanel.getHgap() + brick.getyPosition() * BRICK_SIZE);
                for (int i = 0; i < brick.getBrickData().length; i++) {
                    for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                        setRectangleData(brick.getBrickData()[i][j], rectangles[i][j]);
                    }
                }
            }
        }
    }

    @FXML
    public void refreshGameBackground(int[][] board) {
        for (int i = 2; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                setRectangleData(board[i][j], displayMatrix[i][j]);
            }
        }
    }

    private void setRectangleData(int color, Rectangle rectangle) {
        if (rectangle != null) {
            rectangle.setFill(getFillColor(color));
            rectangle.setArcHeight(9);
            rectangle.setArcWidth(9);
        }
    }

    private void moveDown(MoveEvent event) {
            if (isPause == null || !isPause.getValue()) {
                if (eventListener != null) {
                    ShowResult downData = eventListener.onDownEvent(event);
                    if (downData.getClearRow() != null && downData.getClearRow().getLinesRemoved() > 0) {
                        NotificationPanel notificationPanel = new NotificationPanel("+" + downData.getClearRow().getScoreBonus());
                        if (groupNotification != null) {
                            groupNotification.getChildren().add(notificationPanel);
                            notificationPanel.showScore(groupNotification.getChildren());
                        }
                    }
                    refreshBrick(downData.getViewData());
                }
            }
            if (gamePanel != null) {
                gamePanel.requestFocus();
            }
        }

    public void setEventListener(InputEventListener eventListener) {
        this.eventListener = eventListener;
    }

    public void bindScore(IntegerProperty integerProperty) {
            // Score binding logic can be implemented here
    }

    public void gameOver() {
            if (timeLine != null) {
                timeLine.stop();
            }
            if (gameOverPanel != null) {
                gameOverPanel.setVisible(true);
            }
            if (isGameOver != null) {
                isGameOver.setValue(Boolean.TRUE);
            }
    }

        @FXML
    public void newGame(ActionEvent actionEvent) {
            if (timeLine != null) {
                timeLine.stop();
            }
            if (gameOverPanel != null) {
                gameOverPanel.setVisible(false);
            }
            if (eventListener != null) {
                eventListener.createNewGame();
            }
            if (gamePanel != null) {
                gamePanel.requestFocus();
            }
            if (timeLine != null) {
                timeLine.play();
            }
            if (isPause != null) {
                isPause.setValue(Boolean.FALSE);
            }
            if (isGameOver != null) {
                isGameOver.setValue(Boolean.FALSE);
            }
    }

        @FXML
    public void pauseGame(ActionEvent actionEvent) {
            if (gamePanel != null) {
                gamePanel.requestFocus();
            }
    }
}
