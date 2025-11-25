package com.comp2042.tetris.app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.comp2042.tetris.mechanics.board.GameView;
import com.comp2042.tetris.ui.view.BufferedGameView;
import com.comp2042.tetris.ui.view.GuiController;

import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.Glow;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MenuController {

    @FXML
    private Pane backgroundPane;
    @FXML
    private javafx.scene.layout.StackPane rootPane;
    @FXML
    private BorderPane mainMenuUI;

    @FXML
    private HBox titleContainer;
    @FXML
    private Text yearText;

    @FXML
    private Button playButton;
    @FXML
    private Button quitButton;
    @FXML
    private javafx.scene.layout.StackPane exitConfirmationOverlay;

    private final List<FallingShape> fallingShapes = new ArrayList<>();
    private final List<Particle> particles = new ArrayList<>();
    private final Random random = new Random();
    private AnimationTimer timer;

    private static final Color[] NEON_COLORS = {
            Color.CYAN, Color.YELLOW, Color.LIME, Color.RED, Color.MAGENTA, Color.ORANGE, Color.DODGERBLUE
    };

    @FXML
    public void initialize() {
        setupTitle();
        setupButtons();
        startAnimation();
        

        Rectangle fadeOverlay = new Rectangle(700, 600, Color.BLACK);
        fadeOverlay.setMouseTransparent(true); // Allow clicks to pass through if needed, though it fades out
        rootPane.getChildren().add(fadeOverlay); // Add on top of everything

        FadeTransition fade = new FadeTransition(Duration.seconds(3), fadeOverlay);
        fade.setFromValue(1);
        fade.setToValue(0);
        fade.setOnFinished(e -> rootPane.getChildren().remove(fadeOverlay)); // Remove after fade
        fade.play();
    }

    private void setupTitle() {
        String title = "TETRIS";
        Color[] titleColors = {Color.BLUE, Color.RED, Color.YELLOW, Color.LIME, Color.MAGENTA, Color.ORANGE};

        javafx.scene.text.Font customFont = null;
        try {
            customFont = javafx.scene.text.Font.loadFont(getClass().getResourceAsStream("/PressStart2P-vaV7.ttf"), 60);
            if (customFont != null) {
                System.out.println("Loaded custom font: " + customFont.getName());
            } else {
                System.err.println("Failed to load PressStart2P-vaV7.ttf");
            }
        } catch (Exception e) { 
            System.err.println("Exception loading font: " + e.getMessage());
        }
        
        if (customFont == null) {
            try {
                customFont = javafx.scene.text.Font.loadFont(getClass().getResourceAsStream("/digital.ttf"), 80);
            } catch (Exception e) { /* Ignore */ }
        }
        if (customFont == null) {
            customFont = javafx.scene.text.Font.font("Impact", 90);
        }

        for (int i = 0; i < title.length(); i++) {
            Text letter = new Text(String.valueOf(title.charAt(i)));
            letter.getStyleClass().add("title-text");
            letter.setFont(customFont);
            letter.setFill(titleColors[i % titleColors.length]);
            
            // Add glow effect - Reduced as requested
            DropShadow glow = new DropShadow();
            glow.setColor(titleColors[i % titleColors.length]);
            glow.setRadius(10);
            glow.setSpread(0.2);
            letter.setEffect(glow);

            titleContainer.getChildren().add(letter);
        }
        

        javafx.scene.text.Font yearFont = null;
        try {
            yearFont = javafx.scene.text.Font.loadFont(getClass().getResourceAsStream("/NineByFiveNbp-MypB.ttf"), 40);
        } catch (Exception e) { /* Ignore */ }
        
        if (yearFont == null) {
             try {
                yearFont = javafx.scene.text.Font.loadFont(getClass().getResourceAsStream("/digital.ttf"), 40);
            } catch (Exception e) { /* Ignore */ }
        }
        if (yearFont == null) yearFont = javafx.scene.text.Font.font("Arial", 40);
        
        yearText.setFont(yearFont);
        yearText.setFill(Color.WHITE);
        yearText.setEffect(new DropShadow(10, Color.CYAN));
    }

    private void setupButtons() {
        javafx.scene.text.Font buttonFont = null;
        try {
            buttonFont = javafx.scene.text.Font.loadFont(getClass().getResourceAsStream("/PressStart2P-vaV7.ttf"), 16);
        } catch (Exception e) { /* Ignore */ }
        
        if (buttonFont != null) {
            playButton.setFont(buttonFont);
            quitButton.setFont(buttonFont);
        }

        setupButtonAnimation(playButton);
        setupButtonAnimation(quitButton);
    }

    private void setupButtonAnimation(Button button) {
    }

    private void startAnimation() {
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                updateBackground();
            }
        };
        timer.start();
    }

    private void updateBackground() {
        // Spawn new shapes
        if (random.nextDouble() < 0.02) { // 2% chance per frame
            spawnFallingShape();
        }

        // Spawn particles
        if (random.nextDouble() < 0.1) {
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
        double size = 8 + random.nextDouble() * 12; // Smaller size
        Color color = NEON_COLORS[random.nextInt(NEON_COLORS.length)];
        
        Node shape = createRandomTetromino(size, color);
        
        shape.setTranslateX(x);
        shape.setTranslateY(-100);
        
        // More blur for all shapes
        shape.setOpacity(0.4);
        shape.setEffect(new javafx.scene.effect.GaussianBlur(4 + random.nextDouble() * 6));

        backgroundPane.getChildren().add(shape);
        fallingShapes.add(new FallingShape(shape, 0.5 + random.nextDouble() * 1.5));
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
        
        Rectangle p = new Rectangle(2, 2, Color.WHITE);
        p.setOpacity(random.nextDouble() * 0.5);
        p.setTranslateX(x);
        p.setTranslateY(y);
        
        backgroundPane.getChildren().add(p);
        particles.add(new Particle(p));
    }

    @FXML
    private void onPlay() {
        // Fade out transition
        FadeTransition fadeOut = new FadeTransition(Duration.millis(500), rootPane);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(e -> loadGameScene());
        fadeOut.play();
    }

    private void loadGameScene() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ui/layout/game.fxml"));
            Parent root = fxmlLoader.load();
            GuiController controller = fxmlLoader.getController();

            Scene scene = new Scene(root, 700, 600);
            scene.getStylesheets().add(getClass().getResource("/ui/styles/window.css").toExternalForm());
            
            Stage stage = (Stage) playButton.getScene().getWindow();
            root.setOpacity(0);
            stage.setScene(scene);
            FadeTransition fadeIn = new FadeTransition(Duration.millis(600), root);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();
            
            GameView decoratedView = new BufferedGameView(controller);
            // Keep reference to prevent GC or just suppress warning if logic is self-sustaining
            @SuppressWarnings("unused")
            GameController gameController = new GameController(decoratedView);
            
        } catch (IOException e) {
            System.err.println("Failed to load game scene: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void onQuit() {
        backgroundPane.setEffect(new GaussianBlur(10));
        mainMenuUI.setEffect(new GaussianBlur(10));
        exitConfirmationOverlay.setVisible(true);
    }

    @FXML
    private void onConfirmQuit() {
        Stage stage = (Stage) quitButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onCancelQuit() {
        backgroundPane.setEffect(null);
        mainMenuUI.setEffect(null);
        exitConfirmationOverlay.setVisible(false);
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
}
