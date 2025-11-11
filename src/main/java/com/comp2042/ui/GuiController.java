package com.comp2042.ui;

import java.net.URL;
import java.util.ResourceBundle;

import com.comp2042.event.EventSource;
import com.comp2042.event.EventType;
import com.comp2042.event.InputEventListener;
import com.comp2042.event.MoveEvent;
import com.comp2042.model.ShowResult;
import com.comp2042.model.ViewData;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.layout.GridPane;
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
    private transient BoardRenderer boardRenderer = new BoardRenderer(BRICK_SIZE);
    private transient InputHandler inputHandler = new InputHandler();

    private InputEventListener eventListener;

    private transient Rectangle[][] rectangles;

    // Replaced raw Timeline with extracted GameLoop for SRP.
    private transient GameLoop gameLoop;

    private final transient BooleanProperty isPause = new SimpleBooleanProperty(false);

    private final transient BooleanProperty isGameOver = new SimpleBooleanProperty(false);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Font.loadFont(getClass().getClassLoader().getResource("digital.ttf").toExternalForm(), 38);
        
        if (gamePanel != null) {
            gamePanel.setFocusTraversable(true);
            gamePanel.requestFocus();
        }
        
        if (gameOverPanel != null) {
            gameOverPanel.setVisible(false);
        }
    }

    @FXML
    public void initGameView(int[][] boardMatrix, ViewData brick) {
        displayMatrix = boardRenderer.initBoard(gamePanel, boardMatrix);

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

    gameLoop = new GameLoop(Duration.millis(400), () -> moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD)));
    gameLoop.start();
    }

    // assigned to CellColor utility.
    private Paint getFillColor(int i) {
        return CellColor.fromValue(i);
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
        boardRenderer.refreshBoard(board, displayMatrix);
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
                    handleDownResult(downData);
                }
            }
            if (gamePanel != null) {
                gamePanel.requestFocus();
            }
        }

    private void handleDownResult(ShowResult downData) {
        if (downData.getClearRow() != null && downData.getClearRow().getLinesRemoved() > 0) {
            NotificationPanel notificationPanel = new NotificationPanel("+" + downData.getClearRow().getScoreBonus());
            if (groupNotification != null) {
                groupNotification.getChildren().add(notificationPanel);
                notificationPanel.showScore(groupNotification.getChildren());
            }
        }
        refreshBrick(downData.getViewData());
    }

    public void setEventListener(InputEventListener eventListener) {
        this.eventListener = eventListener;
        if (gamePanel != null && inputHandler != null) {
            inputHandler.attach(gamePanel, eventListener,
                    new InputHandler.InputCallbacks() {
                        @Override
                        public void onViewUpdate(ViewData data) {
                            refreshBrick(data);
                        }
                        @Override
                        public void onDownResult(ShowResult result) {
                            handleDownResult(result);
                        }
                    }, () -> !isPause.getValue() && !isGameOver.getValue());
        }
    }

    public void bindScore(IntegerProperty integerProperty) {
            // Score binding logic can be implemented here
    }

    public void gameOver() {
            if (gameLoop != null) {
                gameLoop.stop();
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
            if (gameLoop != null) {
                gameLoop.stop();
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
            if (gameLoop != null && !gameLoop.isRunning()) {
                gameLoop.start();
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
