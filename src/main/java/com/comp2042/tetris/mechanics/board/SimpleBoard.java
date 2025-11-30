package com.comp2042.tetris.mechanics.board;

import java.awt.Point;
import java.util.Objects;

import com.comp2042.tetris.domain.model.RowClearResult;
import com.comp2042.tetris.domain.model.SpawnResult;
import com.comp2042.tetris.domain.model.ViewData;
import com.comp2042.tetris.mechanics.bricks.Brick;
import com.comp2042.tetris.mechanics.bricks.BrickGenerator;
import com.comp2042.tetris.mechanics.bricks.BrickGeneratorFactory;
import com.comp2042.tetris.mechanics.bricks.RandomBrickGeneratorFactory;
import com.comp2042.tetris.mechanics.movement.BrickDropActions;
import com.comp2042.tetris.mechanics.movement.BrickMovement;
import com.comp2042.tetris.mechanics.movement.BrickPositionManager;
import com.comp2042.tetris.mechanics.rotation.BrickRotator;
import com.comp2042.tetris.mechanics.spawn.BrickSpawn;
import com.comp2042.tetris.util.CollisionDetector;
import com.comp2042.tetris.util.MatrixOperations;


public class SimpleBoard implements BrickMovement, BrickDropActions, BoardRead, BrickSpawn, BoardLifecycle {

    private final int rows;
    private final int cols;
    private final BrickGenerator brickGenerator;
    private final BrickRotator brickRotator;
    private final BrickPositionManager positionManager;
    
    private int[][] boardMatrix;

    public SimpleBoard(int rows, int cols) {
        this(rows, cols, new RandomBrickGeneratorFactory());
    }

    public SimpleBoard(int rows, int cols, BrickGeneratorFactory generatorFactory) {
        this(rows, cols, new DefaultBoardComponentsFactory(Objects.requireNonNull(generatorFactory, "generatorFactory must not be null")));
    }

    public SimpleBoard(int rows, int cols, BoardComponentsFactory componentsFactory) {
        Objects.requireNonNull(componentsFactory, "componentsFactory must not be null");
        this.rows = rows;
        this.cols = cols;
        
        
        boardMatrix = new int[rows][cols];
        brickGenerator = componentsFactory.createGenerator();
        brickRotator = componentsFactory.createRotator();
        positionManager = componentsFactory.createPositionManager();
    }

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

    @Override
    public int[][] getBoardMatrix() {
    return boardMatrix;
    }

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

    @Override
    public void mergeBrickToBackground() {
        boardMatrix = MatrixOperations.merge(boardMatrix, brickRotator.getCurrentShape(), positionManager.getX(), positionManager.getY());
    }    @Override
    public RowClearResult clearRows() {
    RowClearResult result = MatrixOperations.clearRows(boardMatrix);
    boardMatrix = result.getNewMatrix();
        return result;
    }

    @Override
    public void newGame() {
        boardMatrix = new int[rows][cols];
        
        spawnBrick();
    }

    
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

