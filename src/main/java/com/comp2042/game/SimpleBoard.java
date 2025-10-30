package com.comp2042.game;

import java.awt.Point;

import com.comp2042.logic.bricks.Brick;
import com.comp2042.logic.bricks.BrickGenerator;
import com.comp2042.logic.bricks.RandomBrickGenerator;
import com.comp2042.model.RowClearResult;
import com.comp2042.model.Score;
import com.comp2042.model.ViewData;
import com.comp2042.util.MatrixOperations;

/**
 * Implementation of the Board interface that handles the core game mechanics.
 * This class manages the game matrix, brick movement, collision detection,
 * and score tracking.
 */
public class SimpleBoard implements Board {

    private final int rows;
    private final int cols;
    private final BrickGenerator brickGenerator;
    private final BrickRotator brickRotator;
    // Renamed from `currentGameMatrix` -> `boardMatrix` to make it easier to understand the board's state.
    private int[][] boardMatrix;

    // Renamed from `currentOffset` -> `activePiece` to show that
    // this Point stores the current active piece's position (x=col, y=row).
    private Point activePiece;
    private final Score score;

    public SimpleBoard(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        // Constructor parameters renamed to `rows`/`cols` to avoid confusion
        // about matrix orientation (matrix[row][col]). Initialize the board accordingly.
        boardMatrix = new int[rows][cols];
        brickGenerator = new RandomBrickGenerator();
        brickRotator = new BrickRotator();
        score = new Score();
    }

    @Override
    public boolean moveBrickDown() {
    int[][] currentMatrix = MatrixOperations.copy(boardMatrix);
    Point p = new Point(activePiece);
        p.translate(0, 1);
        boolean conflict = MatrixOperations.isCollision(currentMatrix, brickRotator.getCurrentShape(), (int) p.getX(), (int) p.getY());
        if (conflict) {
            return false;
        } else {
            activePiece = p;
            return true;
        }
    }


    @Override
    public boolean moveBrickLeft() {
    int[][] currentMatrix = MatrixOperations.copy(boardMatrix);
    Point p = new Point(activePiece);
        p.translate(-1, 0);
        boolean conflict = MatrixOperations.isCollision(currentMatrix, brickRotator.getCurrentShape(), (int) p.getX(), (int) p.getY());
        if (conflict) {
            return false;
        } else {
            activePiece = p;
            return true;
        }
    }

    @Override
    public boolean moveBrickRight() {
    int[][] currentMatrix = MatrixOperations.copy(boardMatrix);
    Point p = new Point(activePiece);
        p.translate(1, 0);
        boolean conflict = MatrixOperations.isCollision(currentMatrix, brickRotator.getCurrentShape(), (int) p.getX(), (int) p.getY());
        if (conflict) {
            return false;
        } else {
            activePiece = p;
            return true;
        }
    }

    @Override
    public boolean rotateLeftBrick() {
    int[][] currentMatrix = MatrixOperations.copy(boardMatrix);
    com.comp2042.model.RotationInfo nextShape = brickRotator.getNextShape();
    boolean conflict = MatrixOperations.isCollision(currentMatrix, nextShape.getShape(), (int) activePiece.getX(), (int) activePiece.getY());
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
    public boolean spawnBrick() {
        Brick currentBrick = brickGenerator.getBrick();
        brickRotator.setBrick(currentBrick);
    activePiece = new Point(4, 10);
    return MatrixOperations.isCollision(boardMatrix, brickRotator.getCurrentShape(), (int) activePiece.getX(), (int) activePiece.getY());
    }

    @Override
    public int[][] getBoardMatrix() {
    return boardMatrix;
    }

    @Override
    public ViewData getViewData() {
    return new ViewData(brickRotator.getCurrentShape(), (int) activePiece.getX(), (int) activePiece.getY(), brickGenerator.peekNextBrick().getRotationMatrix().get(0));
    }

    @Override
    public void mergeBrickToBackground() {
    boardMatrix = MatrixOperations.merge(boardMatrix, brickRotator.getCurrentShape(), (int) activePiece.getX(), (int) activePiece.getY());
    }

    @Override
    public RowClearResult clearRows() {
    RowClearResult result = MatrixOperations.clearRows(boardMatrix);
    boardMatrix = result.getNewMatrix();
        return result;
    }

    @Override
    public Score getScore() {
        return score;
    }


    @Override
    public void newGame() {
    boardMatrix = new int[rows][cols];
        score.reset();
        spawnBrick();
    }
}
