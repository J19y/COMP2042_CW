package com.comp2042.tetris.ui.view;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.comp2042.tetris.app.CreateNewGame;
import com.comp2042.tetris.app.GameLoopController;
import com.comp2042.tetris.domain.model.ShowResult;
import com.comp2042.tetris.domain.model.ViewData;
import com.comp2042.tetris.mechanics.board.GameView;
import com.comp2042.tetris.mechanics.state.GameStateManager;
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
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    
    private IntegerProperty scoreProperty;
    
    private javafx.animation.Timeline scoreGlowTimeline;

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
    private javafx.scene.control.Slider volumeSlider;
    
    @FXML
    private javafx.scene.control.Button musicToggleButton;

    @FXML
    private javafx.scene.layout.HBox helpContainer;

    private boolean musicOn = true;

    private final transient BoardRenderer boardRenderer = new BoardRenderer(BRICK_SIZE);
    private final transient InputHandler inputHandler = new InputHandler();
    private final transient ViewInitializer viewInitializer = new ViewInitializer();
    private final transient GameStateManager stateManager = new GameStateManager();
    private transient GameMediator mediator;

    private InputActionHandler inputActionHandler;
    private DropInput dropInput;
    private CreateNewGame gameLifecycle;

    // Background Animation Fields
    private BackgroundAnimator backgroundAnimator;
    private long startTime;
    private long accumulatedNanos;
    private int volume = 100;
    private boolean timerRunning;
    private boolean manualPauseActive;
    private FadeTransition pauseDimTransition;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        viewInitializer.loadFonts(getClass());
        // If the ViewInitializer loaded the digital font, apply it directly to timerText to ensure usage
        String digitalFamily = viewInitializer.getDigitalFontFamily();
        if (digitalFamily != null && timerText != null) {
            timerText.setFont(javafx.scene.text.Font.font(digitalFamily, 28));
        }
        viewInitializer.setupGamePanel(gamePanel);
        viewInitializer.setupGameOverPanel(gameOverPanel);
        if (gameOverPanel != null) {
            gameOverPanel.setOnRetry(() -> newGame(new ActionEvent()));
            gameOverPanel.setOnReturnToMenu(this::returnToMenu);
        }
        applyBoardClip();
        bindPauseDim();
        updatePauseDimVisibility();
        bindHelpContainer();
        mediator = new GameMediator(boardRenderer, viewInitializer, stateManager, gamePanel, gameOverPanel);

        startAnimation();

        // Initialize volume slider (if present in FXML)
        if (volumeSlider != null) {
            volumeSlider.setMin(0);
            volumeSlider.setMax(100);
            volumeSlider.setValue(volume);
            volumeSlider.setBlockIncrement(1);
            volumeSlider.valueProperty().addListener((obs, oldV, newV) -> {
                volume = newV.intValue();
                updateVolumeText();
            });
        }

        // Initialize music toggle (if present)
        if (musicToggleButton != null) {
            musicToggleButton.setText(musicOn ? "ON" : "OFF");
            if (volumeSlider != null) {
                volumeSlider.setDisable(!musicOn);
            }
        }

        // Fade-in will be triggered once the scene is ready
        Platform.runLater(() -> {
            if (backgroundPane != null && backgroundPane.getScene() != null) {
                javafx.scene.Node root = backgroundPane.getScene().getRoot();
                root.setOpacity(0);
                javafx.animation.FadeTransition ft = new javafx.animation.FadeTransition(Duration.millis(600), root);
                ft.setFromValue(0.0);
                ft.setToValue(1.0);
                ft.play();
            }
        });
    }
    
    private void startCountdown() {
        if (countdownOverlay == null) return;
        manualPauseActive = false;
        updatePauseDimVisibility();
        pauseTimerTracking();
        resetTimerTracking();
        countdownOverlay.setVisible(true);
        countdownText.setText("3");
        if (!stateManager.isPaused()) {
            stateManager.pauseGame();
        }
        
        javafx.animation.Timeline timeline = new javafx.animation.Timeline(
            new javafx.animation.KeyFrame(Duration.seconds(0), e -> countdownText.setText("3")),
            new javafx.animation.KeyFrame(Duration.seconds(1), e -> countdownText.setText("2")),
            new javafx.animation.KeyFrame(Duration.seconds(2), e -> countdownText.setText("1")),
            new javafx.animation.KeyFrame(Duration.seconds(3), e -> {
                countdownOverlay.setVisible(false);
                stateManager.resumeGame();
                mediator.ensureLoopRunning();
                resumeTimerTracking();
            })
        );
        timeline.play();
    }
    
    @FXML
    public void openSettings() {
        if (settingsOverlay == null) {
            return;
        }
        settingsOverlay.setVisible(true);
        if (!stateManager.isPaused()) {
            togglePause();
        } else {
            manualPauseActive = true;
            updatePauseDimVisibility();
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
        mediator.ensureLoopRunning();
        mediator.focusGamePanel();
    }
    
    @FXML
    public void returnToMenu() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/ui/layout/menu.fxml"));
            javafx.scene.Parent root = loader.load();
            javafx.scene.Scene scene = new javafx.scene.Scene(root);
            
            javafx.stage.Stage stage = (javafx.stage.Stage) gamePanel.getScene().getWindow();
            pauseTimerTracking();

            // Fade out
            javafx.animation.FadeTransition ft = new javafx.animation.FadeTransition(Duration.millis(500), gamePanel.getScene().getRoot());
            ft.setFromValue(1.0);
            ft.setToValue(0.0);
            ft.setOnFinished(e -> {
                root.setOpacity(0);
                stage.setScene(scene);
                javafx.animation.FadeTransition fadeIn = new javafx.animation.FadeTransition(Duration.millis(600), root);
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
        if (volume < 100) {
            volume += 10;
            updateVolumeText();
        }
    }
    
    @FXML
    public void decreaseVolume() {
        if (volume > 0) {
            volume -= 10;
            updateVolumeText();
        }
    }
    
    private void updateVolumeText() {
        if (volumeText != null) {
            volumeText.setText(volume + "%");
        }
        // TODO: Connect to actual audio service
    }
    
    private void startAnimation() {
        resetTimerTracking();
        backgroundAnimator = new BackgroundAnimator(backgroundPane, () -> updateTimer(System.nanoTime()));
        backgroundAnimator.start();
    }
    
    private void updateTimer(long now) {
        if (timerText == null) {
            return;
        }
        long totalNanos = accumulatedNanos;
        if (timerRunning) {
            totalNanos += now - startTime;
        }
        updateTimerDisplay(totalNanos);
    }

    private void updateTimerDisplay(long totalNanos) {
        long elapsedSeconds = totalNanos / 1_000_000_000L;
        long minutes = elapsedSeconds / 60;
        long seconds = elapsedSeconds % 60;
        timerText.setText(String.format("%02d:%02d", minutes, seconds));
    }

    @FXML
    public void toggleMusic(ActionEvent event) {
        musicOn = !musicOn;
        if (musicToggleButton != null) {
            musicToggleButton.setText(musicOn ? "ON" : "OFF");
        }
        if (volumeSlider != null) {
            volumeSlider.setDisable(!musicOn);
        }
        if (!musicOn && volumeText != null) {
            volumeText.setText("OFF");
        } else {
            updateVolumeText();
        }
    }

    private void resetTimerTracking() {
        accumulatedNanos = 0L;
        startTime = System.nanoTime();
        timerRunning = false;
        if (timerText != null) {
            updateTimerDisplay(0L);
        }
    }

    private void pauseTimerTracking() {
        if (timerRunning) {
            accumulatedNanos += System.nanoTime() - startTime;
            timerRunning = false;
        }
    }

    private void resumeTimerTracking() {
        startTime = System.nanoTime();
        timerRunning = true;
    }
    
    @FXML
    @Override
    public void initGameView(int[][] boardMatrix, ViewData brick) {
        Rectangle[][] displayMatrix = boardRenderer.initBoard(gamePanel, boardMatrix);

        ActiveBrickRenderer activeBrickRenderer = new ActiveBrickRenderer(BRICK_SIZE, ghostPanel, brickPanel);
        activeBrickRenderer.initialize(brick);
        renderNextBrick(brick.getNextBrickData());

        NotificationManager notificationService = new NotificationManager(groupNotification);

        GameLoopController gameLoopController = new GameLoopController(Duration.millis(400),
            () -> moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD)));
        
        // Do NOT start the loop immediately. Wait for countdown.
        // gameLoopController.start(); 
        
        if (mediator != null) {
            mediator.configureVisuals(displayMatrix, activeBrickRenderer, notificationService);
            mediator.setGameLoop(gameLoopController);
        }
        stateManager.startGame();
        mediator.focusGamePanel();
        startCountdown();
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
        if (data != null && data.getViewData() != null) {
            renderNextBrick(data.getViewData().getNextBrickData());
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
        // Bind the Pause key to opening settings (so pressing P shows the settings overlay)
        inputHandler.setPauseAction(this::openSettings);
        if (gamePanel != null && inputHandler != null && inputActionHandler != null) {
            inputHandler.attach(gamePanel, this.inputActionHandler,
                result -> handleResult(result), () -> stateManager.canAcceptInput());
        }
    }

    @Override
    public void bindScore(IntegerProperty integerProperty) {
        this.scoreProperty = integerProperty;
        if (scoreText != null) {
            scoreText.textProperty().bind(integerProperty.asString());
            // initialize last hundred bucket (not used directly)

            // listen for score changes and trigger glow every time we cross a 100-point boundary
            integerProperty.addListener((obs, oldV, newV) -> {
                if (newV == null) return;
                int oldHundred = (oldV == null) ? 0 : oldV.intValue() / 100;
                int newHundred = newV.intValue() / 100;
                if (newHundred > oldHundred) {
                    playScoreGlow();
                }
            });
        }
    }

    private void playScoreGlow() {
        if (scoreText == null) return;

        // stop previous animation if still running
        if (scoreGlowTimeline != null) {
            scoreGlowTimeline.stop();
        }

        javafx.scene.effect.DropShadow ds = new javafx.scene.effect.DropShadow();
        ds.setColor(javafx.scene.paint.Color.CYAN);
        ds.setRadius(0);
        ds.setSpread(0.6);
        scoreText.setEffect(ds);

        scoreGlowTimeline = new javafx.animation.Timeline(
            new javafx.animation.KeyFrame(javafx.util.Duration.ZERO,
                new javafx.animation.KeyValue(ds.radiusProperty(), 0),
                new javafx.animation.KeyValue(scoreText.scaleXProperty(), 1.0),
                new javafx.animation.KeyValue(scoreText.scaleYProperty(), 1.0)
            ),
            new javafx.animation.KeyFrame(javafx.util.Duration.millis(120),
                new javafx.animation.KeyValue(ds.radiusProperty(), 18),
                new javafx.animation.KeyValue(scoreText.scaleXProperty(), 1.12),
                new javafx.animation.KeyValue(scoreText.scaleYProperty(), 1.12)
            ),
            new javafx.animation.KeyFrame(javafx.util.Duration.millis(360),
                new javafx.animation.KeyValue(ds.radiusProperty(), 0),
                new javafx.animation.KeyValue(scoreText.scaleXProperty(), 1.0),
                new javafx.animation.KeyValue(scoreText.scaleYProperty(), 1.0)
            )
        );

        scoreGlowTimeline.setOnFinished(e -> scoreText.setEffect(null));
        scoreGlowTimeline.play();
    }

    private void renderNextBrick(List<int[][]> nextBricks) {
        nextBrickPanel.getChildren().clear();
        if (nextBricks == null || nextBricks.isEmpty()) return;
        int limit = Math.min(2, nextBricks.size());
        for (int idx = 0; idx < limit; idx++) {
            int[][] nextBrickData = nextBricks.get(idx);
            GridPane brickGrid = new GridPane();
            brickGrid.setHgap(1);
            brickGrid.setVgap(1);
            brickGrid.setAlignment(javafx.geometry.Pos.CENTER);
            
            for (int i = 0; i < nextBrickData.length; i++) {
                for (int j = 0; j < nextBrickData[i].length; j++) {
                    if (nextBrickData[i][j] != 0) {
                        Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                        javafx.scene.paint.Paint paint = com.comp2042.tetris.ui.theme.CellColor.fromValue(nextBrickData[i][j]);
                        rectangle.setFill(paint);
                        rectangle.setArcHeight(9);
                        rectangle.setArcWidth(9);

                        // Make next-brick preview follow the inside-black placed style
                        javafx.scene.paint.Paint p = paint;
                        javafx.scene.paint.Color baseColor = javafx.scene.paint.Color.WHITE;
                        if (p instanceof javafx.scene.paint.Color) {
                            baseColor = (javafx.scene.paint.Color) p;
                        }
                        com.comp2042.tetris.ui.theme.NeonGlowStyle.applyPlacedStyle(rectangle, baseColor);
                        brickGrid.add(rectangle, j, i);
                    }
                }
            }
            nextBrickPanel.getChildren().add(brickGrid);
        }
    }

    @Override
    public void gameOver() {
        int finalScore = getCurrentScore();
        manualPauseActive = false;
        updatePauseDimVisibility();
        pauseTimerTracking();
        mediator.handleGameOver(finalScore);
    }

    @FXML
    public void newGame(ActionEvent actionEvent) {
        mediator.prepareNewGame();
        if (gameLifecycle != null) {
            gameLifecycle.createNewGame();
        }
        mediator.focusGamePanel();
        stateManager.startGame();
        resetTimerTracking();
        startCountdown();
    }

    @FXML
    public void pauseGame(ActionEvent actionEvent) {
        // Make the pause button behave like the P key: open the settings overlay
        openSettings();
    }

    private void togglePause() {
        mediator.togglePause();
        manualPauseActive = stateManager.isPaused();
        updatePauseDimVisibility();
        if (manualPauseActive) {
            pauseTimerTracking();
        } else if (countdownOverlay == null || !countdownOverlay.isVisible()) {
            resumeTimerTracking();
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

    private void bindPauseDim() {
        if (pauseDim == null || rootPane == null) {
            return;
        }
        pauseDim.prefWidthProperty().bind(rootPane.widthProperty());
        pauseDim.prefHeightProperty().bind(rootPane.heightProperty());
        pauseDim.setOpacity(0);
        pauseDim.setVisible(false);
    }

    private void updatePauseDimVisibility() {
        if (pauseDim == null) {
            return;
        }
        if (pauseDimTransition != null) {
            pauseDimTransition.stop();
        }
        pauseDimTransition = new FadeTransition(Duration.millis(220), pauseDim);
        pauseDimTransition.setFromValue(pauseDim.getOpacity());
        if (manualPauseActive) {
            pauseDim.setVisible(true);
            pauseDimTransition.setToValue(0.55);
            pauseDimTransition.setOnFinished(null);
        } else {
            if (!pauseDim.isVisible()) {
                pauseDim.setVisible(true);
            }
            pauseDimTransition.setToValue(0);
            pauseDimTransition.setOnFinished(e -> pauseDim.setVisible(false));
        }
        pauseDimTransition.play();
    }

    private void bindHelpContainer() {
        if (helpContainer == null) {
            return;
        }
        // Set initial visibility based on current state
        helpContainer.setVisible(stateManager.getCurrentState() == GameStateManager.GameState.MENU);
        
        // Listen for state changes and update visibility
        stateManager.stateProperty().addListener((obs, oldVal, newVal) -> {
            helpContainer.setVisible(newVal == GameStateManager.GameState.MENU);
        });
    }
}
