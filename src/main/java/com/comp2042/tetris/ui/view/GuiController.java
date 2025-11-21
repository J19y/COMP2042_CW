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

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.Glow;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GuiController implements Initializable, GameView {

    private static final Logger LOGGER = Logger.getLogger(GuiController.class.getName());

    private static final int BRICK_SIZE = 20;

    @FXML
    private Pane backgroundPane;

    @FXML
    private GridPane gamePanel;

    @FXML
    private Group groupNotification;

    @FXML
    private GridPane brickPanel;

    @FXML
    private javafx.scene.layout.VBox nextBrickPanel;

    @FXML
    private Text scoreText;
    
    @FXML
    private Text timerText;

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

    private final transient BoardRenderer boardRenderer = new BoardRenderer(BRICK_SIZE);
    private final transient InputHandler inputHandler = new InputHandler();
    private final transient ViewInitializer viewInitializer = new ViewInitializer();
    private final transient GameStateManager stateManager = new GameStateManager();
    private transient GameMediator mediator;

    private InputActionHandler inputActionHandler;
    private DropInput dropInput;
    private CreateNewGame gameLifecycle;

    // Background Animation Fields
    private final List<FallingShape> fallingShapes = new ArrayList<>();
    private final List<Particle> particles = new ArrayList<>();
    private final Random random = new Random();
    private AnimationTimer animationTimer;
    private long startTime;
    private long accumulatedNanos;
    private int volume = 100;
    private boolean timerRunning;
    
    private static final Color[] NEON_COLORS = {
            Color.CYAN, Color.YELLOW, Color.LIME, Color.RED, Color.MAGENTA, Color.ORANGE, Color.DODGERBLUE
    };

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        viewInitializer.loadFonts(getClass());
        viewInitializer.setupGamePanel(gamePanel);
        viewInitializer.setupGameOverPanel(gameOverPanel);
        mediator = new GameMediator(boardRenderer, viewInitializer, stateManager, gamePanel, gameOverPanel);

        startAnimation();

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
            mediator.togglePause();
        }
        pauseTimerTracking();
    }
    
    @FXML
    public void resumeGame() {
        if (settingsOverlay == null) {
            return;
        }
        settingsOverlay.setVisible(false);
        if (stateManager.isPaused()) {
            mediator.togglePause();
        }
        if (!countdownOverlay.isVisible()) {
            resumeTimerTracking();
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
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                updateBackground();
                updateTimer(now);
            }
        };
        animationTimer.start();
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

    private void updateBackground() {
        if (backgroundPane == null) return;

        // Spawn new shapes
        if (random.nextDouble() < 0.01) { // Softer frequency
            spawnFallingShape();
        }

        // Spawn particles
        if (random.nextDouble() < 0.05) {
            spawnParticle();
        }

        // Update shapes
        Iterator<FallingShape> shapeIt = fallingShapes.iterator();
        while (shapeIt.hasNext()) {
            FallingShape shape = shapeIt.next();
            shape.update();
            if (shape.isOffScreen()) {
                backgroundPane.getChildren().remove(shape.node);
                shapeIt.remove();
            }
        }

        // Update particles
        Iterator<Particle> particleIt = particles.iterator();
        while (particleIt.hasNext()) {
            Particle p = particleIt.next();
            p.update();
            if (p.isDead()) {
                backgroundPane.getChildren().remove(p.node);
                particleIt.remove();
            }
        }
    }

    private void spawnFallingShape() {
        double x = random.nextDouble() * backgroundPane.getWidth();
        double size = 8 + random.nextDouble() * 10;
        Color color = NEON_COLORS[random.nextInt(NEON_COLORS.length)];
        
        Node shape = createRandomTetromino(size, color);
        
        shape.setTranslateX(x);
        shape.setTranslateY(-100);
        
        shape.setOpacity(0.18);
        shape.setEffect(new GaussianBlur(2 + random.nextDouble() * 3));

        backgroundPane.getChildren().add(shape);
        fallingShapes.add(new FallingShape(shape, 0.3 + random.nextDouble() * 0.8));
    }

    private Node createRandomTetromino(double size, Color color) {
        javafx.scene.Group group = new javafx.scene.Group();
        // 0: I, 1: J, 2: L, 3: O, 4: S, 5: T, 6: Z
        int type = random.nextInt(7);
        
        int[][] coords = getTetrominoCoords(type);
        
        for (int[] coord : coords) {
            Rectangle rect = new Rectangle(size, size, Color.TRANSPARENT);
            rect.setStroke(color);
            rect.setStrokeWidth(2);
            rect.setEffect(new Glow(0.5));
            rect.setX(coord[0] * size);
            rect.setY(coord[1] * size);
            group.getChildren().add(rect);
        }
        
        return group;
    }

    private int[][] getTetrominoCoords(int type) {
        return switch (type) {
            case 0 -> new int[][]{{0, 0}, {1, 0}, {2, 0}, {3, 0}}; // I
            case 1 -> new int[][]{{0, 0}, {0, 1}, {1, 1}, {2, 1}}; // J
            case 2 -> new int[][]{{2, 0}, {0, 1}, {1, 1}, {2, 1}}; // L
            case 3 -> new int[][]{{0, 0}, {1, 0}, {0, 1}, {1, 1}}; // O
            case 4 -> new int[][]{{1, 0}, {2, 0}, {0, 1}, {1, 1}}; // S
            case 5 -> new int[][]{{1, 0}, {0, 1}, {1, 1}, {2, 1}}; // T
            case 6 -> new int[][]{{0, 0}, {1, 0}, {1, 1}, {2, 1}}; // Z
            default -> new int[][]{{0, 0}};
        };
    }

    private void spawnParticle() {
        double x = random.nextDouble() * backgroundPane.getWidth();
        double y = random.nextDouble() * backgroundPane.getHeight();
        
        Rectangle p = new Rectangle(1.5, 1.5, Color.WHITE);
        p.setOpacity(random.nextDouble() * 0.35);
        p.setTranslateX(x);
        p.setTranslateY(y);
        
        backgroundPane.getChildren().add(p);
        particles.add(new Particle(p));
    }

    // Inner classes for animation
    private static class FallingShape {
        Node node;
        double speed;
        double rotationSpeed;

        FallingShape(Node node, double speed) {
            this.node = node;
            this.speed = speed;
            this.rotationSpeed = Math.random() * 2 - 1;
        }

        void update() {
            node.setTranslateY(node.getTranslateY() + speed);
            node.setRotate(node.getRotate() + rotationSpeed);
        }

        boolean isOffScreen() {
            return node.getTranslateY() > 800; // Assuming height is 600
        }
    }

    private static class Particle {
        Node node;
        double life = 1.0;
        double decay;

        Particle(Node node) {
            this.node = node;
            this.decay = 0.005 + Math.random() * 0.01;
        }

        void update() {
            life -= decay;
            node.setOpacity(life);
            node.setTranslateY(node.getTranslateY() + 0.5);
        }

        boolean isDead() {
            return life <= 0;
        }
    }

    @FXML
    @Override
    public void initGameView(int[][] boardMatrix, ViewData brick) {
        Rectangle[][] displayMatrix = boardRenderer.initBoard(gamePanel, boardMatrix);

        ActiveBrickRenderer activeBrickRenderer = new ActiveBrickRenderer(BRICK_SIZE, brickPanel, gamePanel);
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
        scoreText.textProperty().bind(integerProperty.asString());
    }

    private void renderNextBrick(List<int[][]> nextBricks) {
        nextBrickPanel.getChildren().clear();
        for (int[][] nextBrickData : nextBricks) {
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
                        // Add glow effect
                        if (paint instanceof Color color) {
                            rectangle.setEffect(new javafx.scene.effect.DropShadow(10, color));
                        }
                        brickGrid.add(rectangle, j, i);
                    }
                }
            }
            nextBrickPanel.getChildren().add(brickGrid);
        }
    }

    @Override
    public void gameOver() {
        pauseTimerTracking();
        mediator.handleGameOver();
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
        togglePause();
    }

    private void togglePause() {
        mediator.togglePause();
        if (stateManager.isPaused()) {
            pauseTimerTracking();
        } else if (!countdownOverlay.isVisible()) {
            resumeTimerTracking();
        }
    }
}
