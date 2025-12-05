package com.comp2042.tetris.engine.board;

import java.awt.Point;
import java.util.Objects;

import com.comp2042.tetris.domain.model.RowClearResult;
import com.comp2042.tetris.domain.model.SpawnResult;
import com.comp2042.tetris.domain.model.ViewData;
import com.comp2042.tetris.engine.bricks.Brick;
import com.comp2042.tetris.engine.bricks.BrickGenerator;
import com.comp2042.tetris.engine.bricks.BrickGeneratorFactory;
import com.comp2042.tetris.engine.bricks.RandomBrickGeneratorFactory;
import com.comp2042.tetris.engine.movement.BrickDropActions;
import com.comp2042.tetris.engine.movement.BrickMovement;
import com.comp2042.tetris.engine.movement.BrickPositionManager;
import com.comp2042.tetris.engine.rotation.BrickRotator;
import com.comp2042.tetris.engine.spawn.BrickSpawn;
import com.comp2042.tetris.util.CollisionDetector;
import com.comp2042.tetris.util.MatrixOperations;

/**
 * Core game board implementation managing brick placement and movement.
 * <p>
 * Implements multiple interfaces to provide movement, drop actions,
 * board reading, spawning, and lifecycle management capabilities.
 * Uses composition to delegate to specialized components.
 * </p>
 *
 * @author Youssif Mahmoud Gomaa Sayed
 * @version 1.0
 */
public class SimpleBoard implements BrickMovement, BrickDropActions, BoardRead, BrickSpawn, BoardLifecycle {

    private final int rows;
    private final int cols;
    private final BrickGenerator brickGenerator;
    private final BrickRotator brickRotator;
    private final BrickPositionManager positionManager;
    
    private int[][] boardMatrix;

    /**
     * Constructs a SimpleBoard with default random brick generator.
     *
     * @param rows the number of rows
     * @param cols the number of columns
     */
    public SimpleBoard(int rows, int cols) {
        this(rows, cols, new RandomBrickGeneratorFactory());
    }

    /**
     * Constructs a SimpleBoard with a custom brick generator factory.
     *
     * @param rows the number of rows
     * @param cols the number of columns
     * @param generatorFactory the factory to create brick generators
     */
    public SimpleBoard(int rows, int cols, BrickGeneratorFactory generatorFactory) {
        this(rows, cols, new DefaultBoardComponentsFactory(Objects.requireNonNull(generatorFactory, "generatorFactory must not be null")));
    }

    /**
     * Constructs a SimpleBoard with a custom components factory.
     *
     * @param rows the number of rows
     * @param cols the number of columns
     * @param componentsFactory the factory to create board components
     */
    public SimpleBoard(int rows, int cols, BoardComponentsFactory componentsFactory) {
        Objects.requireNonNull(componentsFactory, "componentsFactory must not be null");
        this.rows = rows;
        this.cols = cols;
        
        
        boardMatrix = new int[rows][cols];
        brickGenerator = componentsFactory.createGenerator();
        brickRotator = componentsFactory.createRotator();
        positionManager = componentsFactory.createPositionManager();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean moveBrickDown() {
    int[][] currentMatrix = MatrixOperations.copy(boardMatrix);
    Point p = positionManager.calculateMoveDown();
        boolean conflict = CollisionDetector.isCollision(currentMatrix, brickRotator.getCurrentShape(), (int) p.getX(), (int) p.getY());
        if (conflict) {
            return false;
        } else {
            positionManager.updatePosition(p);
            return true;
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean moveBrickLeft() {
    int[][] currentMatrix = MatrixOperations.copy(boardMatrix);
    Point p = positionManager.calculateMoveLeft();
        boolean conflict = CollisionDetector.isCollision(currentMatrix, brickRotator.getCurrentShape(), (int) p.getX(), (int) p.getY());
        if (conflict) {
            return false;
        } else {
            positionManager.updatePosition(p);
            return true;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean moveBrickRight() {
    int[][] currentMatrix = MatrixOperations.copy(boardMatrix);
    Point p = positionManager.calculateMoveRight();
        boolean conflict = CollisionDetector.isCollision(currentMatrix, brickRotator.getCurrentShape(), (int) p.getX(), (int) p.getY());
        if (conflict) {
            return false;
        } else {
            positionManager.updatePosition(p);
            return true;
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implements wall kick logic: if the rotation would cause collision,
     * attempts to shift the brick left or right to allow the rotation.
     * </p>
     */
    @Override
    public boolean rotateLeftBrick() {
        int[][] currentMatrix = MatrixOperations.copy(boardMatrix);
        com.comp2042.tetris.domain.model.RotationInfo nextShape = brickRotator.getNextShape();
        int[][] shape = nextShape.getShape();
        int currentX = positionManager.getX();
        int currentY = positionManager.getY();

        
        if (!CollisionDetector.isCollision(currentMatrix, shape, currentX, currentY)) {
            brickRotator.setCurrentShape(nextShape.getPosition());
            return true;
        }

        
        if (!CollisionDetector.isCollision(currentMatrix, shape, currentX + 1, currentY)) {
            positionManager.updatePosition(new Point(currentX + 1, currentY));
            brickRotator.setCurrentShape(nextShape.getPosition());
            return true;
        }

        
        if (!CollisionDetector.isCollision(currentMatrix, shape, currentX - 1, currentY)) {
            positionManager.updatePosition(new Point(currentX - 1, currentY));
            brickRotator.setCurrentShape(nextShape.getPosition());
            return true;
        }

        return false;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Spawns the brick at the center-top of the board.
     * </p>
     */
    @Override
    
    public SpawnResult spawnBrick() {
    Brick currentBrick = brickGenerator.getBrick();
    brickRotator.setBrick(currentBrick);
    int[][] shape = brickRotator.getCurrentShape();
    int spawnX = Math.max(0, (cols - shape[0].length) / 2);
    positionManager.reset(spawnX, 0);
        boolean gameOver = CollisionDetector.isCollision(boardMatrix, brickRotator.getCurrentShape(), positionManager.getX(), positionManager.getY());
        return new SpawnResult(gameOver);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int[][] getBoardMatrix() {
    return boardMatrix;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Also calculates the ghost piece position for preview.
     * </p>
     */
    @Override
    public ViewData getViewData() {
        java.util.List<Brick> nextBricks = brickGenerator.peekNextBricks(3);
        java.util.List<int[][]> nextBrickMatrices = new java.util.ArrayList<>();
        for (Brick b : nextBricks) {
            nextBrickMatrices.add(b.getRotationMatrix().get(0));
        }
        
        
        int ghostY = positionManager.getY();
        int[][] currentShape = brickRotator.getCurrentShape();
        int currentX = positionManager.getX();
        
        while (!CollisionDetector.isCollision(boardMatrix, currentShape, currentX, ghostY + 1)) {
            ghostY++;
        }
        
        return new ViewData(currentShape, currentX, positionManager.getY(), nextBrickMatrices, ghostY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void mergeBrickToBackground() {
        boardMatrix = MatrixOperations.merge(boardMatrix, brickRotator.getCurrentShape(), positionManager.getX(), positionManager.getY());
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public RowClearResult clearRows() {
    RowClearResult result = MatrixOperations.clearRows(boardMatrix);
    boardMatrix = result.getNewMatrix();
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void newGame() {
        boardMatrix = new int[rows][cols];
        
        spawnBrick();
    }

    /**
     * Adds a garbage line at the bottom of the board.
     * <p>
     * Used in mystery mode events. Creates a partial row with random blocks
     * and one guaranteed hole.
     * </p>
     */
    public void addGarbageLine() {
        int[][] newMatrix = new int[rows][cols];
        
        for (int r = 0; r < rows - 1; r++) {
            System.arraycopy(boardMatrix[r + 1], 0, newMatrix[r], 0, cols);
        }
        
        java.util.Random rnd = new java.util.Random();
        int[] bottom = new int[cols];
        int holeIndex = rnd.nextInt(cols);
        for (int c = 0; c < cols; c++) {
            if (c == holeIndex) {
                bottom[c] = 0;
            } else {
                bottom[c] = rnd.nextBoolean() ? 1 : 0;
            }
        }
        
        boolean anyBlock = false;
        for (int c = 0; c < cols; c++) {
            if (bottom[c] != 0) { anyBlock = true; break; }
        }
        if (!anyBlock) {
            
            for (int c = 0; c < cols; c++) {
                if (c != holeIndex) { bottom[c] = 1; break; }
            }
        }
        newMatrix[rows - 1] = bottom;
        boardMatrix = newMatrix;
    }
}

