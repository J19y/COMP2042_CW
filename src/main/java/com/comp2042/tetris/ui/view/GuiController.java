package com.comp2042.tetris.ui.view;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.comp2042.tetris.app.BaseGameController;
import com.comp2042.tetris.app.CreateNewGame;
import com.comp2042.tetris.app.GameLoopController;
import com.comp2042.tetris.app.GameModeLifecycle;
import com.comp2042.tetris.domain.model.ShowResult;
import com.comp2042.tetris.domain.model.ViewData;
import com.comp2042.tetris.mechanics.board.GameView;
import com.comp2042.tetris.mechanics.state.GameStateManager;
import com.comp2042.tetris.services.audio.MusicManager;
import com.comp2042.tetris.services.notify.NotificationManager;
import com.comp2042.tetris.ui.input.DropInput;
import com.comp2042.tetris.ui.input.EventSource;
import com.comp2042.tetris.ui.input.EventType;
import com.comp2042.tetris.ui.input.InputActionHandler;
import com.comp2042.tetris.ui.input.InputHandler;
import com.comp2042.tetris.ui.input.MoveEvent;
import com.comp2042.tetris.ui.render.ActiveBrickRenderer;
import com.comp2042.tetris.ui.render.BoardRenderer;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GuiController implements Initializable, GameView {

    private static final Logger LOGGER = Logger.getLogger(GuiController.class.getName());
    private static final int BRICK_SIZE = 20;

    @FXML
    private Pane backgroundPane;

    @FXML
    private Pane boardClipContainer;

    @FXML
    private StackPane rootPane;

    @FXML
    private Pane pauseDim;

    @FXML
    private GridPane gamePanel;

    @FXML
    private Group groupNotification;

    private NotificationManager notificationService;

    @FXML
    private GridPane ghostPanel;

    @FXML
    private GridPane brickPanel;

    @FXML
    private javafx.scene.layout.VBox nextBrickPanel;

    @FXML
    private Text scoreText;

    @FXML
    private Text timerText;

    @FXML
    private Text levelText;

    @FXML
    private javafx.scene.layout.VBox levelContainer;

    private IntegerProperty scoreProperty;
    private javafx.beans.property.IntegerProperty boundLevelProperty;

    @FXML
    private GameOverPanel gameOverPanel;

    @FXML
    private StackPane countdownOverlay;

    @FXML
    private Text countdownText;

    @FXML
    private StackPane settingsOverlay;

    @FXML
    private Text volumeText;

    @FXML
    private Slider volumeSlider;

    @FXML
    private Button musicToggleButton;

    @FXML
    private HBox helpContainer;

    private final transient BoardRenderer boardRenderer = new BoardRenderer(BRICK_SIZE);
    private final transient InputHandler inputHandler = new InputHandler();
    private final transient ViewInitializer viewInitializer = new ViewInitializer();
    private final transient GameStateManager stateManager = new GameStateManager();
    private transient GameMediator mediator;

    private GameLoopController gameLoopController;
    private Duration baseTickInterval = Duration.millis(400);

    private InputActionHandler inputActionHandler;
    private DropInput dropInput;
    private CreateNewGame gameLifecycle;

    private GameTimer gameTimer;
    private AudioSettingsController audioSettingsController;
    private PauseOverlayController pauseOverlayController;
    private CountdownManager countdownManager;
    private GameEffectManager effectManager;
    private NextBrickRenderer nextBrickRenderer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        viewInitializer.loadFonts(getClass());
        String digitalFamily = viewInitializer.getDigitalFontFamily();
        if (digitalFamily != null && timerText != null) {
            timerText.setFont(javafx.scene.text.Font.font(digitalFamily, 28));
        }
        viewInitializer.setupGamePanel(gamePanel);
        viewInitializer.setupGameOverPanel(gameOverPanel);
        if (boundLevelProperty != null && levelContainer != null && levelText != null) {
            try {
                levelText.textProperty().bind(boundLevelProperty.asString());
                levelContainer.setVisible(true);
            } catch (Exception ignored) {
                levelContainer.setVisible(false);
            }
        }
        if (gameOverPanel != null) {
            gameOverPanel.setOnRetry(() -> newGame(new ActionEvent()));
            gameOverPanel.setOnReturnToMenu(this::returnToMenu);
        }
        applyBoardClip();
        pauseOverlayController = new PauseOverlayController(pauseDim, rootPane);
        pauseOverlayController.bindHelpContainer(helpContainer, stateManager);
        mediator = new GameMediator(boardRenderer, viewInitializer, stateManager, gamePanel, gameOverPanel);

        gameTimer = new GameTimer(timerText, backgroundPane);
        gameTimer.startAnimation();

        effectManager = new GameEffectManager(gamePanel, boardClipContainer, levelText, scoreText, baseTickInterval);
        nextBrickRenderer = new NextBrickRenderer(nextBrickPanel, BRICK_SIZE);
        audioSettingsController = new AudioSettingsController(volumeSlider, volumeText, musicToggleButton);
        audioSettingsController.initialize();
        countdownManager = new CountdownManager(countdownOverlay, countdownText, stateManager, mediator, pauseOverlayController, gameTimer);

        Platform.runLater(() -> {
            if (backgroundPane != null && backgroundPane.getScene() != null) {
                Node root = backgroundPane.getScene().getRoot();
                root.setOpacity(0);
                FadeTransition ft = new FadeTransition(Duration.millis(600), root);
                ft.setFromValue(0.0);
                ft.setToValue(1.0);
                ft.play();
            }
        });
    }

    private void startCountdown() {
        if (countdownManager != null) {
            countdownManager.setGameLifecycle(gameLifecycle);
            countdownManager.startCountdown();
        }
    }

    @FXML
    public void openSettings() {
        if (settingsOverlay == null) {
            return;
        }
        settingsOverlay.setVisible(true);
        if (!stateManager.isPaused()) {
            togglePause();
        } else if (pauseOverlayController != null) {
            pauseOverlayController.setManualPauseActive(true);
        }
    }

    @FXML
    public void resumeGame() {
        if (settingsOverlay == null) {
            return;
        }
        settingsOverlay.setVisible(false);
        if (stateManager.isPaused()) {
            togglePause();
        }
        if (mediator != null) {
            mediator.ensureLoopRunning();
            mediator.focusGamePanel();
        }
    }

    @FXML
    public void returnToMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/layout/menu.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) gamePanel.getScene().getWindow();
            if (gameTimer != null) {
                gameTimer.pauseTimerTracking();
            }
            FadeTransition ft = new FadeTransition(Duration.millis(500), gamePanel.getScene().getRoot());
            ft.setFromValue(1.0);
            ft.setToValue(0.0);
            ft.setOnFinished(e -> {
                root.setOpacity(0);
                stage.setScene(scene);
                FadeTransition fadeIn = new FadeTransition(Duration.millis(600), root);
                fadeIn.setFromValue(0.0);
                fadeIn.setToValue(1.0);
                fadeIn.play();
            });
            ft.play();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Unable to load menu view", e);
        }
    }

    @FXML
    public void increaseVolume() {
        if (audioSettingsController != null) {
            audioSettingsController.increaseVolume();
        }
    }

    @FXML
    public void decreaseVolume() {
        if (audioSettingsController != null) {
            audioSettingsController.decreaseVolume();
        }
    }

    @FXML
    public void toggleMusic(ActionEvent event) {
        if (audioSettingsController != null) {
            audioSettingsController.toggleMusic();
        }
    }

    @Override
    public void setRemainingTime(int seconds) {
        if (gameTimer != null) {
            gameTimer.setRemainingTime(seconds);
        }
    }

    @Override
    public void setBoardVisibility(boolean visible) {
        Platform.runLater(() -> {
            try {
                if (gamePanel != null) {
                    gamePanel.setOpacity(visible ? 1.0 : 0.0);
                }
                if (brickPanel != null) {
                    brickPanel.setVisible(visible);
                }
                if (ghostPanel != null) {
                    ghostPanel.setVisible(visible);
                }
                if (nextBrickPanel != null) {
                    nextBrickPanel.setOpacity(visible ? 1.0 : 0.0);
                }
            } catch (Exception ignored) {}
        });
    }

    @Override
    public void showMessage(String message) {
        try {
            if (notificationService != null && message != null && !message.isEmpty()) {
                notificationService.showMessage(message);
            }
        } catch (Exception ignored) {}
    }

    @Override
    public void showEventMessage(String message) {
        try {
            if (notificationService != null && message != null && !message.isEmpty()) {
                notificationService.showEventMessage(message);
            }
        } catch (Exception ignored) {}
    }

    @Override
    public void playEarthquakeAnimation() {
        if (effectManager != null) {
            effectManager.playEarthquakeAnimation();
        }
    }

    @Override
    public void animateLevelIncrement() {
        if (effectManager != null) {
            effectManager.animateLevelIncrement();
        }
    }

    @Override
    public void showFogEffect(int seconds) {
        if (effectManager != null) {
            effectManager.showFogEffect(seconds);
        }
    }

    @Override
    public void showHeavyGravityEffect(int seconds) {
        if (effectManager != null) {
            effectManager.showHeavyGravityEffect(seconds);
        }
    }

    @FXML
    @Override
    public void initGameView(int[][] boardMatrix, ViewData brick) {
        Rectangle[][] displayMatrix = boardRenderer.initBoard(gamePanel, boardMatrix);
        ActiveBrickRenderer activeBrickRenderer = new ActiveBrickRenderer(BRICK_SIZE, ghostPanel, brickPanel);
        activeBrickRenderer.initialize(brick);
        if (nextBrickRenderer != null) {
            nextBrickRenderer.render(brick.getNextBrickData());
        }
        notificationService = new NotificationManager(groupNotification);

        this.gameLoopController = new GameLoopController(baseTickInterval,
            () -> moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD)));

        if (mediator != null) {
            mediator.configureVisuals(displayMatrix, activeBrickRenderer, notificationService);
            mediator.setGameLoop(gameLoopController);
        }
        if (effectManager != null) {
            effectManager.setGameLoopController(gameLoopController);
        }
        stateManager.startGame();
        if (mediator != null) {
            mediator.focusGamePanel();
        }
        startCountdown();
    }

    @FXML
    @Override
    public void refreshGameBackground(int[][] board) {
        if (mediator != null) {
            mediator.refreshGameBackground(board);
        }
    }

    private void moveDown(MoveEvent event) {
        if (stateManager.canUpdateGame() && dropInput != null) {
            ShowResult result = dropInput.onDown(event);
            handleResult(result);
        }
        if (mediator != null) {
            mediator.focusGamePanel();
        }
    }

    private void handleResult(ShowResult data) {
        if (mediator != null) {
            mediator.handleResult(data);
        }
        if (data != null && data.getViewData() != null && nextBrickRenderer != null) {
            nextBrickRenderer.render(data.getViewData().getNextBrickData());
        }
    }

    @Override
    public void acceptShowResult(ShowResult result) {
        handleResult(result);
    }

    @Override
    public void settleActiveBrick(Runnable onFinished) {
        if (mediator != null) {
            mediator.settleActiveBrick(onFinished);
        } else if (onFinished != null) {
            onFinished.run();
        }
    }

    @Override
    public void setInputHandlers(InputActionHandler inputActionHandler, DropInput dropInput, CreateNewGame gameLifecycle) {
        this.inputActionHandler = inputActionHandler;
        this.dropInput = dropInput;
        this.gameLifecycle = gameLifecycle;
        if (countdownManager != null) {
            countdownManager.setGameLifecycle(gameLifecycle);
        }
        inputHandler.setPauseAction(() -> {
            boolean canAccept = stateManager == null || stateManager.canAcceptInput();
            boolean gameOverVisible = gameOverPanel != null && gameOverPanel.isVisible();
            if (canAccept && !gameOverVisible) {
                openSettings();
            }
        });
        if (gamePanel != null && inputHandler != null && inputActionHandler != null) {
            inputHandler.attach(gamePanel, this.inputActionHandler,
                result -> handleResult(result), () -> stateManager.canAcceptInput());
        }
    }

    @Override
    public void bindScore(IntegerProperty integerProperty) {
        this.scoreProperty = integerProperty;
        if (scoreText != null && integerProperty != null) {
            scoreText.textProperty().bind(integerProperty.asString());
            integerProperty.addListener((obs, oldV, newV) -> {
                if (newV == null) return;
                int oldHundred = (oldV == null) ? 0 : oldV.intValue() / 100;
                int newHundred = newV.intValue() / 100;
                if (newHundred > oldHundred && effectManager != null) {
                    effectManager.playScoreGlow();
                }
            });
        }
    }

    @Override
    public void bindLevel(IntegerProperty levelProperty) {
        boundLevelProperty = levelProperty;
        Platform.runLater(() -> {
            if (levelContainer == null || levelText == null) {
                return;
            }
            if (levelProperty == null) {
                try {
                    levelText.textProperty().unbind();
                } catch (Exception ignored) {}
                levelContainer.setVisible(false);
                return;
            }
            try {
                levelText.textProperty().bind(levelProperty.asString());
                levelContainer.setVisible(true);
            } catch (Exception ignored) {
                levelContainer.setVisible(false);
            }
        });
    }

    @Override
    public void gameOver() {
        int finalScore = getCurrentScore();
        int totalLines = 0;
        long gameTime = 0;
        if (gameLifecycle instanceof BaseGameController) {
            BaseGameController controller = (BaseGameController) gameLifecycle;
            totalLines = controller.getTotalLinesCleared();
            gameTime = System.currentTimeMillis() - controller.getGameStartTime();
        }
        if (pauseOverlayController != null) {
            pauseOverlayController.setManualPauseActive(false);
        }
        if (gameTimer != null) {
            gameTimer.pauseTimerTracking();
            gameTimer.clearLowTimeEffects();
        }
        try {
            MusicManager.getInstance().stopSfx("/audio/10SecondsTimer.mp3");
        } catch (Exception ignored) {}
        try {
            MusicManager.getInstance().playTrack(MusicManager.Track.GAME_OVER, 900, 1);
        } catch (Exception ignored) {}

        if (gameOverPanel != null) {
            if (gameLifecycle instanceof com.comp2042.tetris.app.MysteryGameController) {
                try {
                    int lvl = ((com.comp2042.tetris.app.MysteryGameController) gameLifecycle).getLevel();
                    gameOverPanel.setMysteryLevel(lvl);
                } catch (Exception ignored) {
                    gameOverPanel.setMysteryLevel(0);
                }
            } else {
                gameOverPanel.setMysteryLevel(0);
            }
        }

        if (mediator != null) {
            mediator.handleGameOver(finalScore, totalLines, gameTime);
        }
    }

    @FXML
    public void newGame(ActionEvent actionEvent) {
        if (mediator != null) {
            mediator.prepareNewGame();
            mediator.focusGamePanel();
        }
        if (gameLifecycle != null) {
            gameLifecycle.createNewGame();
        }
        stateManager.startGame();
        if (gameTimer != null) {
            gameTimer.resetTimerTracking();
        }
        startCountdown();
    }

    @FXML
    public void tryAgain(ActionEvent actionEvent) {
        newGame(actionEvent);
        if (settingsOverlay != null) {
            settingsOverlay.setVisible(false);
        }
        try {
            MusicManager.getInstance().stopSfx("/audio/10SecondsTimer.mp3");
        } catch (Exception ignored) {}
    }

    @FXML
    public void pauseGame(ActionEvent actionEvent) {
        boolean canAccept = stateManager == null || stateManager.canAcceptInput();
        boolean gameOverVisible = gameOverPanel != null && gameOverPanel.isVisible();
        if (canAccept && !gameOverVisible) {
            openSettings();
        }
    }

    private void togglePause() {
        if (mediator == null) {
            return;
        }
        mediator.togglePause();
        boolean paused = stateManager.isPaused();
        if (pauseOverlayController != null) {
            pauseOverlayController.setManualPauseActive(paused);
        }
        if (paused) {
            if (gameTimer != null) {
                gameTimer.pauseTimerTracking();
            }
            if (gameLifecycle instanceof GameModeLifecycle) {
                try {
                    ((GameModeLifecycle) gameLifecycle).pauseMode();
                } catch (Exception ignored) {}
            }
        } else if (countdownOverlay == null || !countdownOverlay.isVisible()) {
            if (gameTimer != null) {
                gameTimer.resumeTimerTracking();
            }
            if (gameLifecycle instanceof GameModeLifecycle) {
                try {
                    ((GameModeLifecycle) gameLifecycle).resumeMode();
                } catch (Exception ignored) {}
            }
        }
    }

    private int getCurrentScore() {
        return scoreProperty == null ? 0 : scoreProperty.get();
    }

    private void applyBoardClip() {
        if (boardClipContainer != null) {
            Rectangle clip = new Rectangle();
            clip.widthProperty().bind(boardClipContainer.widthProperty());
            clip.heightProperty().bind(boardClipContainer.heightProperty());
            boardClipContainer.setClip(clip);
        }
    }
}

