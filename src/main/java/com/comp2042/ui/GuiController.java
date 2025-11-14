package com.comp2042.ui;

import java.net.URL;
import java.util.ResourceBundle;

import com.comp2042.event.EventSource;
import com.comp2042.event.EventType;
import com.comp2042.event.DropInput;
import com.comp2042.event.InputActionHandler;
import com.comp2042.event.MoveEvent;
import com.comp2042.game.CreateNewGame;
import com.comp2042.game.state.GameStateManager;
import com.comp2042.manager.NotificationManager;
import com.comp2042.model.ShowResult;
import com.comp2042.model.ViewData;

import javafx.beans.property.IntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
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
    private final transient BoardRenderer boardRenderer = new BoardRenderer(BRICK_SIZE);
    private final transient InputHandler inputHandler = new InputHandler();
    private final transient ViewInitializer viewInitializer = new ViewInitializer();
    private transient ActiveBrickRenderer activeBrickRenderer;
    private transient NotificationManager notificationService;
    private final transient GameStateManager stateManager = new GameStateManager();

    private InputActionHandler inputActionHandler;
    private DropInput dropInput;
    private CreateNewGame gameLifecycle;

    // Extracted to GameLoopController for SRP
    private transient GameLoopController gameLoopController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        viewInitializer.loadFonts(getClass());
        viewInitializer.setupGamePanel(gamePanel);
        viewInitializer.setupGameOverPanel(gameOverPanel);
    }

    @FXML
    public void initGameView(int[][] boardMatrix, ViewData brick) {
        displayMatrix = boardRenderer.initBoard(gamePanel, boardMatrix);

        // Initialize ActiveBrickRenderer
        activeBrickRenderer = new ActiveBrickRenderer(BRICK_SIZE, brickPanel, gamePanel);
        activeBrickRenderer.initialize(brick);

        // Initialize NotificationManager
        notificationService = new NotificationManager(groupNotification);

        gameLoopController = new GameLoopController(Duration.millis(400), 
            () -> moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD)));
        gameLoopController.start();
    }

    private void refreshBrick(ViewData brick) {
        if (stateManager.canUpdateGame()) {
            if (activeBrickRenderer != null) {
                activeBrickRenderer.refresh(brick);
            }
        }
    }

    @FXML
    public void refreshGameBackground(int[][] board) {
        boardRenderer.refreshBoard(board, displayMatrix);
    }

    private void moveDown(MoveEvent event) {
        if (stateManager.canUpdateGame() && dropInput != null) {
            ShowResult result = dropInput.onDown(event);
            handleResult(result);
        }
        viewInitializer.requestFocus(gamePanel);
    }

    private void handleResult(ShowResult data) {
        if (data.getClearRow() != null && data.getClearRow().getLinesRemoved() > 0) {
            if (notificationService != null) {
                notificationService.showScoreBonus(data.getClearRow().getScoreBonus());
            }
            refreshGameBackground(data.getClearRow().getNewMatrix());
        }
        refreshBrick(data.getViewData());
    }

    public void setInputHandlers(InputActionHandler inputActionHandler, DropInput dropInput, CreateNewGame gameLifecycle) {
        this.inputActionHandler = inputActionHandler;
        this.dropInput = dropInput;
        this.gameLifecycle = gameLifecycle;
        if (gamePanel != null && inputHandler != null && inputActionHandler != null) {
            inputHandler.attach(gamePanel, this.inputActionHandler,
                result -> handleResult(result), () -> stateManager.canAcceptInput());
        }
    }

    public void bindScore(IntegerProperty integerProperty) {
            // Score binding logic can be implemented here
    }

    public void gameOver() {
        if (gameLoopController != null) {
            gameLoopController.stop();
        }
        if (gameOverPanel != null) {
            gameOverPanel.setVisible(true);
        }
        stateManager.gameOver();
    }

    @FXML
    public void newGame(ActionEvent actionEvent) {
        if (gameLoopController != null) {
            gameLoopController.stop();
        }
        if (gameOverPanel != null) {
            gameOverPanel.setVisible(false);
        }
        if (gameLifecycle != null) {
            gameLifecycle.createNewGame();
        }
        viewInitializer.requestFocus(gamePanel);
        if (gameLoopController != null && !gameLoopController.isRunning()) {
            gameLoopController.start();
        }
        stateManager.startGame();
    }

        @FXML
    public void pauseGame(ActionEvent actionEvent) {
        viewInitializer.requestFocus(gamePanel);
    }
}
