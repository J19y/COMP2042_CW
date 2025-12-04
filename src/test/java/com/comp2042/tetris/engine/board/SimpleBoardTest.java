package com.comp2042.tetris.engine.board;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.comp2042.tetris.domain.model.RowClearResult;
import com.comp2042.tetris.domain.model.SpawnResult;
import com.comp2042.tetris.domain.model.ViewData;
import com.comp2042.tetris.engine.bricks.Brick;
import com.comp2042.tetris.engine.bricks.BrickGenerator;
import com.comp2042.tetris.engine.movement.BrickPositionManager;
import com.comp2042.tetris.engine.rotation.BrickRotator;
import com.comp2042.tetris.util.MatrixOperations;

class SimpleBoardTest {

    private static final int[][] BLOCK_SHAPE = {
        {0, 0, 0, 0},
        {0, 1, 1, 0},
        {0, 1, 1, 0},
        {0, 0, 0, 0}
    };

    private static final int[][] LINE_SHAPE = {
        {0, 0, 0, 0},
        {0, 0, 0, 0},
        {1, 1, 1, 1},
        {0, 0, 0, 0}
    };

    private BrickPositionManager positionManager;
    private SimpleBoard board;

    @SuppressWarnings("unused")
    @BeforeEach
    void setUp() {
        
        positionManager = new BrickPositionManager(0, 0);
        FixedBrick fixedBrick = new FixedBrick(List.of(BLOCK_SHAPE, LINE_SHAPE));
        TestBoardComponentsFactory factory = new TestBoardComponentsFactory(
            new StubBrickGenerator(fixedBrick),
            new BrickRotator(),
            positionManager
        );
        board = new SimpleBoard(20, 10, factory);
    }

    @Test
    void moveBrickDown_advancesPositionWhenSpaceIsClear() {
        board.spawnBrick();
        positionManager.reset(2, 1);

        
        boolean moved = board.moveBrickDown();

        assertTrue(moved);
        assertEquals(2, positionManager.getX());
        assertEquals(2, positionManager.getY());
    }

    @Test
    void moveBrickLeft_stopsWhenBoardCellIsFilled() {
        board.spawnBrick();
        positionManager.reset(0, 1);
        int[][] matrix = board.getBoardMatrix();
        
        matrix[2][1] = 9;

        boolean moved = board.moveBrickLeft();

        
        assertFalse(moved);
        assertEquals(0, positionManager.getX());
    }

    @Test
    void rotateLeftBrick_returnsFalseWhenRotationWouldCollide() {
        board.spawnBrick();
        
        positionManager.reset(8, 1);

        boolean rotated = board.rotateLeftBrick();

        assertFalse(rotated, "Rotation should fail when the new shape collides");
    }

    
    @Test
    void rotateLeftBrick_updatesShapeWhenSpaceIsClear() {
        board.spawnBrick();
        positionManager.reset(3, 2);

        boolean rotated = board.rotateLeftBrick();

        assertTrue(rotated);
        ViewData viewData = board.getViewData();
        assertMatrixEquals(LINE_SHAPE, viewData.getBrickData());
    }

    @Test
    void mergeBrickToBackground_writesCurrentShapeIntoMatrix() {
        board.spawnBrick();
        positionManager.reset(1, 1);
        int before = countFilledCells(board.getBoardMatrix());

        
        board.mergeBrickToBackground();
        int[][] matrix = board.getBoardMatrix();

        assertEquals(before + countFilledCells(BLOCK_SHAPE), countFilledCells(matrix));
    }

    @Test
    void clearRows_removesCompleteRowsAndReturnsResult() {
        int[][] matrix = board.getBoardMatrix();
        int lastRow = matrix.length - 1;
        for (int col = 0; col < matrix[0].length; col++) {
            matrix[lastRow][col] = 7; 
        }

        
        RowClearResult result = board.clearRows();

        assertEquals(1, result.getLinesRemoved());
        assertArrayEquals(result.getNewMatrix(), board.getBoardMatrix());
    }

    @Test
    void spawnBrick_reportsGameOverWhenSpawnAreaBlocked() {
        int[][] matrix = board.getBoardMatrix();
        int spawnX = Math.max(0, (matrix[0].length - BLOCK_SHAPE[0].length) / 2);
        
        for (int dx = 0; dx < BLOCK_SHAPE[0].length; dx++) {
            for (int dy = 0; dy < BLOCK_SHAPE.length; dy++) {
                matrix[dy][spawnX + dx] = 5;
            }
        }

        SpawnResult result = board.spawnBrick();

        assertTrue(result.isGameOver());
    }

    
    @Test
    void spawnBrick_placesBrickAtTopCenterWhenSpaceIsClear() {
        SpawnResult result = board.spawnBrick();

        assertFalse(result.isGameOver());
        int expectedX = Math.max(0, (board.getBoardMatrix()[0].length - BLOCK_SHAPE[0].length) / 2);
        assertEquals(expectedX, positionManager.getX());
        assertEquals(0, positionManager.getY());
    }

    
    @Test
    void moveBrickRight_advancesPositionWhenSpaceIsClear() {
        board.spawnBrick();
        positionManager.reset(1, 1);

        boolean moved = board.moveBrickRight();

        assertTrue(moved);
        assertEquals(2, positionManager.getX());
        assertEquals(1, positionManager.getY());
    }

    
    @Test
    void moveBrickRight_stopsWhenBoardCellIsFilled() {
        board.spawnBrick();
        positionManager.reset(1, 1);
        int[][] matrix = board.getBoardMatrix();
        matrix[2][3] = 8;

        boolean moved = board.moveBrickRight();

        assertFalse(moved);
        assertEquals(1, positionManager.getX());
    }

    
    @Test
    void moveBrickDown_stopsWhenSpaceBelowIsOccupied() {
        board.spawnBrick();
        positionManager.reset(1, 1);
        int[][] matrix = board.getBoardMatrix();
        matrix[3][2] = 4;

        boolean moved = board.moveBrickDown();

        assertFalse(moved);
        assertEquals(1, positionManager.getY());
    }

    
    @Test
    void newGame_resetsBoardAndSpawnsFreshBrick() {
        board.spawnBrick();
        int[][] matrix = board.getBoardMatrix();
        for (int[] row : matrix) {
            Arrays.fill(row, 1);
        }

        board.newGame();

        assertEquals(0, countFilledCells(board.getBoardMatrix()));
        assertEquals(0, positionManager.getY());
    }

    
    @Test
    void getViewData_reflectsLatestPositionAndProvidesCopies() {
        board.spawnBrick();
        positionManager.reset(2, 3);
        board.moveBrickDown();
        board.moveBrickRight();

        ViewData viewData = board.getViewData();
        assertEquals(positionManager.getX(), viewData.getxPosition());
        assertEquals(positionManager.getY(), viewData.getyPosition());

        int[][] first = viewData.getBrickData();
        first[0][0] = 99;
        int[][] second = viewData.getBrickData();
        assertEquals(0, second[0][0]);
        assertNotEquals(first[0][0], second[0][0]);
    }

    private static int countFilledCells(int[][] matrix) {
        int count = 0;
        for (int[] row : matrix) {
            for (int cell : row) {
                if (cell != 0) {
                    count++;
                }
            }
        }
        return count;
    }

    private static void assertMatrixEquals(int[][] expected, int[][] actual) {
        assertEquals(expected.length, actual.length, "Row count differs");
        for (int i = 0; i < expected.length; i++) {
            assertArrayEquals(expected[i], actual[i], "Row " + i + " differs");
        }
    }

    private static final class FixedBrick implements Brick {
        private final List<int[][]> rotations;

        FixedBrick(List<int[][]> rotations) {
            this.rotations = rotations;
        }

        @Override
        public List<int[][]> getRotationMatrix() {
            return MatrixOperations.deepCopyList(rotations);
        }
    }

    private static final class StubBrickGenerator implements BrickGenerator {
        private final Brick brick;

        StubBrickGenerator(Brick brick) {
            this.brick = brick;
        }

        @Override
        public Brick getBrick() {
            return brick;
        }

        @Override
        public Brick peekNextBrick() {
            return brick;
        }

        @Override
        public java.util.List<Brick> peekNextBricks(int count) {
            if (count <= 0) {
                return Collections.emptyList();
            }
            return Collections.nCopies(count, brick);
        }
    }

    private static final class TestBoardComponentsFactory implements BoardComponentsFactory {
        private final BrickGenerator generator;
        private final BrickRotator rotator;
        private final BrickPositionManager positionManager;

        TestBoardComponentsFactory(BrickGenerator generator, BrickRotator rotator, BrickPositionManager positionManager) {
            this.generator = generator;
            this.rotator = rotator;
            this.positionManager = positionManager;
        }

        @Override
        public BrickGenerator createGenerator() {
            return generator;
        }

        @Override
        public BrickRotator createRotator() {
            return rotator;
        }

        @Override
        public BrickPositionManager createPositionManager() {
            return positionManager;
        }
    }
}

