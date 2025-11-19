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

/**
 * Implementation of the Board interface that handles the core game mechanics.
 * This class manages the game matrix, brick movement, collision detection, and score tracking.
 */
public class SimpleBoard implements BrickMovement, BrickDropActions, BoardRead, BrickSpawn, BoardLifecycle {

    private final int rows;
    private final int cols;
    private final BrickGenerator brickGenerator;
    private final BrickRotator brickRotator;
    private final BrickPositionManager positionManager;
    // Renamed from `currentGameMatrix` -> `boardMatrix` to make it easier to understand the board's state.
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
        // Constructor parameters renamed to `rows`/`cols` to avoid confusion
        // about matrix orientation (matrix[row][col]). Initialize the board accordingly.
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

        // Try normal rotation
        if (!CollisionDetector.isCollision(currentMatrix, shape, currentX, currentY)) {
            brickRotator.setCurrentShape(nextShape.getPosition());
            return true;
        }

        // Wall Kick: Try moving right (if hitting left wall)
        if (!CollisionDetector.isCollision(currentMatrix, shape, currentX + 1, currentY)) {
            positionManager.updatePosition(new Point(currentX + 1, currentY));
            brickRotator.setCurrentShape(nextShape.getPosition());
            return true;
        }

        // Wall Kick: Try moving left (if hitting right wall)
        if (!CollisionDetector.isCollision(currentMatrix, shape, currentX - 1, currentY)) {
            positionManager.updatePosition(new Point(currentX - 1, currentY));
            brickRotator.setCurrentShape(nextShape.getPosition());
            return true;
        }

        return false;
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

    @Override
    public void newGame() {
        boardMatrix = new int[rows][cols];
        // Ignore gameOver flag here; a fresh board should not be game over.
        spawnBrick();
    }
}
