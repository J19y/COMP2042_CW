package com.comp2042.game;

import java.awt.Point;

import com.comp2042.logic.bricks.Brick;
import com.comp2042.logic.bricks.BrickGenerator;
import com.comp2042.logic.bricks.RandomBrickGenerator;
import com.comp2042.model.RowClearResult;
import com.comp2042.model.SpawnResult;
import com.comp2042.model.ViewData;
import com.comp2042.util.CollisionDetector;
import com.comp2042.util.MatrixOperations;

/**
 * Implementation of the Board interface that handles the core game mechanics.
 * This class manages the game matrix, brick movement, collision detection, and score tracking.
 */
public class SimpleBoard implements Board {

    private final int rows;
    private final int cols;
    private final BrickGenerator brickGenerator;
    private final BrickRotator brickRotator;
    private final BrickPositionManager positionManager;
    // Renamed from `currentGameMatrix` -> `boardMatrix` to make it easier to understand the board's state.
    private int[][] boardMatrix;

    public SimpleBoard(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        // Constructor parameters renamed to `rows`/`cols` to avoid confusion
        // about matrix orientation (matrix[row][col]). Initialize the board accordingly.
        boardMatrix = new int[rows][cols];
        brickGenerator = new RandomBrickGenerator();
        brickRotator = new BrickRotator();
        positionManager = new BrickPositionManager(4, 10);
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
    com.comp2042.model.RotationInfo nextShape = brickRotator.getNextShape();
    boolean conflict = CollisionDetector.isCollision(currentMatrix, nextShape.getShape(), positionManager.getX(), positionManager.getY());
        if (conflict) {
            return false;
        } else {
            brickRotator.setCurrentShape(nextShape.getPosition());
            return true;
        }
    }

    @Override
    /**
     * Spawns a new brick and places it at the initial active position.
     * Renamed from createNewBrick() -> spawnBrick() to clarify this method
     * both creates and places the brick (it 'spawns' it into the game world).
     */
    public SpawnResult spawnBrick() {
        Brick currentBrick = brickGenerator.getBrick();
        brickRotator.setBrick(currentBrick);
        positionManager.reset(4, 10);
        boolean gameOver = CollisionDetector.isCollision(boardMatrix, brickRotator.getCurrentShape(), positionManager.getX(), positionManager.getY());
        return new SpawnResult(gameOver);
    }

    @Override
    public int[][] getBoardMatrix() {
    return boardMatrix;
    }

    @Override
    public ViewData getViewData() {
    return new ViewData(brickRotator.getCurrentShape(), positionManager.getX(), positionManager.getY(), brickGenerator.peekNextBrick().getRotationMatrix().get(0));
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
    // Assigned to ScoreService for score management.

    @Override
    public void newGame() {
        boardMatrix = new int[rows][cols];
        // Ignore gameOver flag here; a fresh board should not be game over.
        spawnBrick();
    }
}
