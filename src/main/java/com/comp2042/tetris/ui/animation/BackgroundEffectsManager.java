package com.comp2042.tetris.ui.animation;

import javafx.animation.AnimationTimer;
import javafx.scene.Node;
import javafx.scene.effect.Glow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Manages animated background effects for the menu and game screens.
 * Creates falling tetromino shapes and particle effects with neon colors.
 * Uses AnimationTimer for smooth, frame-based animation updates.
 *
 */
public class BackgroundEffectsManager {
    private final Pane backgroundPane;
    private final List<FallingShape> fallingShapes = new ArrayList<>();
    private final List<Particle> particles = new ArrayList<>();
    private final Random random = new Random();
    private AnimationTimer timer;

    private static final Color[] NEON_COLORS = {
            Color.CYAN, Color.YELLOW, Color.LIME, Color.RED, Color.MAGENTA, Color.ORANGE, Color.DODGERBLUE
    };

    public BackgroundEffectsManager(Pane backgroundPane) {
        this.backgroundPane = backgroundPane;
    }

    
    public void startAnimation() {
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                updateBackground();
            }
        };
        timer.start();
    }

    
    public void stopAnimation() {
        if (timer != null) {
            timer.stop();
        }
    }

    
    private void updateBackground() {
        
        if (random.nextDouble() < 0.03) {
            spawnFallingShape();
        }

        
        if (random.nextDouble() < 0.18) {
            spawnParticle();
        }

        
        Iterator<FallingShape> shapeIt = fallingShapes.iterator();
        while (shapeIt.hasNext()) {
            FallingShape shape = shapeIt.next();
            shape.update();
            if (shape.isOffScreen()) {
                backgroundPane.getChildren().remove(shape.node);
                shapeIt.remove();
            }
        }

        
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
        double size = 8 + random.nextDouble() * 12;
        Color color = NEON_COLORS[random.nextInt(NEON_COLORS.length)];

        
        Node shape = createRandomTetromino(size, color);

        shape.setTranslateX(x);
        shape.setTranslateY(-100);

        
        shape.setOpacity(0.4);
        shape.setEffect(new GaussianBlur(4 + random.nextDouble() * 6));

        backgroundPane.getChildren().add(shape);
        fallingShapes.add(new FallingShape(shape, 0.5 + random.nextDouble() * 1.5));

        
        for (int i = 0; i < 4; i++) {
            double px = x + (random.nextDouble() - 0.5) * 50;
            double py = -80 + random.nextDouble() * 30;
            Rectangle p = new Rectangle(2 + random.nextDouble() * 2, 2 + random.nextDouble() * 2, color);
            p.setOpacity(0.8);
            p.setTranslateX(px);
            p.setTranslateY(py);
            p.setEffect(new Glow(0.7));
            backgroundPane.getChildren().add(p);
            particles.add(new Particle(p));
        }
    }

    
    private void spawnParticle() {
        double x = random.nextDouble() * backgroundPane.getWidth();
        double y = random.nextDouble() * backgroundPane.getHeight();

        Color particleColor = random.nextDouble() < 0.3 ? Color.WHITE : NEON_COLORS[random.nextInt(NEON_COLORS.length)];
        Rectangle p = new Rectangle(2, 2, particleColor);
        p.setOpacity(random.nextDouble() * 0.7 + 0.2);
        p.setTranslateX(x);
        p.setTranslateY(y);

        if (random.nextDouble() < 0.4) {
            p.setEffect(new Glow(0.3));
        }

        backgroundPane.getChildren().add(p);
        particles.add(new Particle(p));
    }

    
    private Node createRandomTetromino(double size, Color color) {
        javafx.scene.Group group = new javafx.scene.Group();

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
            case 0 -> new int[][]{{0, 0}, {1, 0}, {2, 0}, {3, 0}}; 
            case 1 -> new int[][]{{0, 0}, {0, 1}, {1, 1}, {2, 1}}; 
            case 2 -> new int[][]{{2, 0}, {0, 1}, {1, 1}, {2, 1}}; 
            case 3 -> new int[][]{{0, 0}, {1, 0}, {0, 1}, {1, 1}}; 
            case 4 -> new int[][]{{1, 0}, {2, 0}, {0, 1}, {1, 1}}; 
            case 5 -> new int[][]{{1, 0}, {0, 1}, {1, 1}, {2, 1}}; 
            case 6 -> new int[][]{{0, 0}, {1, 0}, {1, 1}, {2, 1}}; 
            default -> new int[][]{{0, 0}};
        };
    }

    
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
            return node.getTranslateY() > 800;
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



