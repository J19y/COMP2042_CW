package com.comp2042.tetris.ui.view;

import com.comp2042.tetris.app.CreateNewGame;
import com.comp2042.tetris.app.GameLoopController;
import com.comp2042.tetris.mechanics.state.GameStateManager;
import com.comp2042.tetris.ui.input.InputHandler;
import com.comp2042.tetris.ui.render.ActiveBrickRenderer;
import com.comp2042.tetris.ui.render.BoardRenderer;

import java.net.URL;
import java.util.ResourceBundle;

import com.comp2042.tetris.ui.input.EventSource;
import com.comp2042.tetris.ui.input.EventType;
import com.comp2042.tetris.ui.input.DropInput;
import com.comp2042.tetris.ui.input.InputActionHandler;
import com.comp2042.tetris.ui.input.MoveEvent;
import com.comp2042.tetris.services.notify.NotificationManager;
import com.comp2042.tetris.domain.model.ShowResult;
import com.comp2042.tetris.domain.model.ViewData;

import javafx.beans.property.IntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import com.comp2042.tetris.mechanics.board.GameView;
import javafx.scene.Group;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class GuiController implements Initializable, GameView {

    private static final int BRICK_SIZE = 20;

    @FXML
    private GridPane gamePanel;

    @FXML
    private Group groupNotification;

    @FXML
    private GridPane brickPanel;

    @FXML
    private GameOverPanel gameOverPanel;

    private final transient BoardRenderer boardRenderer = new BoardRenderer(BRICK_SIZE);
    private final transient InputHandler inputHandler = new InputHandler();
    private final transient ViewInitializer viewInitializer = new ViewInitializer();
    private final transient GameStateManager stateManager = new GameStateManager();
    private transient GameMediator mediator;

    private InputActionHandler inputActionHandler;
    private DropInput dropInput;
    private CreateNewGame gameLifecycle;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        viewInitializer.loadFonts(getClass());
        viewInitializer.setupGamePanel(gamePanel);
        viewInitializer.setupGameOverPanel(gameOverPanel);
        mediator = new GameMediator(boardRenderer, viewInitializer, stateManager, gamePanel, gameOverPanel);
    }

    @FXML
    @Override
    public void initGameView(int[][] boardMatrix, ViewData brick) {
        Rectangle[][] displayMatrix = boardRenderer.initBoard(gamePanel, boardMatrix);

        ActiveBrickRenderer activeBrickRenderer = new ActiveBrickRenderer(BRICK_SIZE, brickPanel, gamePanel);
        activeBrickRenderer.initialize(brick);

        NotificationManager notificationService = new NotificationManager(groupNotification);

        GameLoopController gameLoopController = new GameLoopController(Duration.millis(400),
            () -> moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD)));
        gameLoopController.start();
        if (mediator != null) {
            mediator.configureVisuals(displayMatrix, activeBrickRenderer, notificationService);
            mediator.setGameLoop(gameLoopController);
        }
        stateManager.startGame();
        mediator.focusGamePanel();
    }

    @FXML
    @Override
    public void refreshGameBackground(int[][] board) {
        mediator.refreshGameBackground(board);
    }

    private void moveDown(MoveEvent event) {
        if (stateManager.canUpdateGame() && dropInput != null) {
            ShowResult result = dropInput.onDown(event);
            handleResult(result);
        }
        mediator.focusGamePanel();
    }

    private void handleResult(ShowResult data) {
        mediator.handleResult(data);
    }

    @Override
    public void setInputHandlers(InputActionHandler inputActionHandler, DropInput dropInput, CreateNewGame gameLifecycle) {
        this.inputActionHandler = inputActionHandler;
        this.dropInput = dropInput;
        this.gameLifecycle = gameLifecycle;
        inputHandler.setPauseAction(() -> mediator.togglePause());
        if (gamePanel != null && inputHandler != null && inputActionHandler != null) {
            inputHandler.attach(gamePanel, this.inputActionHandler,
                result -> handleResult(result), () -> stateManager.canAcceptInput());
        }
    }

    @Override
    public void bindScore(IntegerProperty integerProperty) {
            // Score binding logic can be implemented here
    }

    @Override
    public void gameOver() {
        mediator.handleGameOver();
    }

    @FXML
    public void newGame(ActionEvent actionEvent) {
        mediator.prepareNewGame();
        if (gameLifecycle != null) {
            gameLifecycle.createNewGame();
        }
        mediator.focusGamePanel();
        mediator.ensureLoopRunning();
        stateManager.startGame();
    }

    @FXML
    public void pauseGame(ActionEvent actionEvent) {
        togglePause();
    }

    private void togglePause() {
        mediator.togglePause();
    }
}
