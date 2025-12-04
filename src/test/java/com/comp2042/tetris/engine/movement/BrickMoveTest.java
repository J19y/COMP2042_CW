package com.comp2042.tetris.engine.movement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.comp2042.tetris.domain.model.ViewData;
import com.comp2042.tetris.engine.board.BoardRead;

class BrickMoveTest {

    private SpyBrickMovement movement;
    private StubBoardRead reader;
    private BrickMove brickMove;

    @BeforeEach
    void setUp() {
        movement = new SpyBrickMovement();
        reader = new StubBoardRead();
        brickMove = new BrickMove(movement, reader);
    }

    @Test
    void handleLeftMoveDelegatesAndReturnsViewData() {
        ViewData expected = new ViewData(new int[][]{{1}}, 0, 0, new int[][]{{0}});
        reader.viewData = expected;

        ViewData result = brickMove.handleLeftMove();

        assertEquals(1, movement.leftMoveCalls);
        assertSame(expected, result);
    }

    @Test
    void handleRightMoveDelegatesAndReturnsViewData() {
        ViewData expected = new ViewData(new int[][]{{1}}, 0, 0, new int[][]{{0}});
        reader.viewData = expected;

        ViewData result = brickMove.handleRightMove();

        assertEquals(1, movement.rightMoveCalls);
        assertSame(expected, result);
    }

    @Test
    void handleRotationDelegatesAndReturnsViewData() {
        ViewData expected = new ViewData(new int[][]{{1}}, 0, 0, new int[][]{{0}});
        reader.viewData = expected;

        ViewData result = brickMove.handleRotation();

        assertEquals(1, movement.rotationCalls);
        assertSame(expected, result);
    }

    @Test
    void multipleLeftMovesAccumulate() {
        ViewData viewData = new ViewData(new int[][]{{1}}, 0, 0, new int[][]{{0}});
        reader.viewData = viewData;

        brickMove.handleLeftMove();
        brickMove.handleLeftMove();
        brickMove.handleLeftMove();

        assertEquals(3, movement.leftMoveCalls);
    }

    @Test
    void multipleRightMovesAccumulate() {
        ViewData viewData = new ViewData(new int[][]{{1}}, 0, 0, new int[][]{{0}});
        reader.viewData = viewData;

        brickMove.handleRightMove();
        brickMove.handleRightMove();

        assertEquals(2, movement.rightMoveCalls);
    }

    @Test
    void multipleRotationsAccumulate() {
        ViewData viewData = new ViewData(new int[][]{{1}}, 0, 0, new int[][]{{0}});
        reader.viewData = viewData;

        brickMove.handleRotation();
        brickMove.handleRotation();
        brickMove.handleRotation();
        brickMove.handleRotation();

        assertEquals(4, movement.rotationCalls);
    }

    @Test
    void leftRightRotationCombinations() {
        ViewData viewData = new ViewData(new int[][]{{1}}, 0, 0, new int[][]{{0}});
        reader.viewData = viewData;

        brickMove.handleLeftMove();
        brickMove.handleRightMove();
        brickMove.handleRotation();
        brickMove.handleLeftMove();

        assertEquals(2, movement.leftMoveCalls);
        assertEquals(1, movement.rightMoveCalls);
        assertEquals(1, movement.rotationCalls);
    }

    @Test
    void viewDataReflectsLatestBoardState() {
        ViewData first = new ViewData(new int[][]{{1}}, 0, 0, new int[][]{{0}});
        ViewData second = new ViewData(new int[][]{{2}}, 5, 5, new int[][]{{0}});

        reader.viewData = first;
        ViewData result1 = brickMove.handleLeftMove();
        assertSame(first, result1);

        reader.viewData = second;
        ViewData result2 = brickMove.handleLeftMove();
        assertSame(second, result2);
    }

    @Test
    void movementReturnValueIgnoredButReaderCalled() {
        // Movement returns true/false, but we only care about reader output
        movement.shouldReturnTrue = true;
        ViewData viewData = new ViewData(new int[][]{{1}}, 0, 0, new int[][]{{0}});
        reader.viewData = viewData;

        ViewData result = brickMove.handleLeftMove();

        assertNotNull(result);
        assertSame(viewData, result);
    }

    // ==================== Test Stubs and Spies ====================

    private static final class SpyBrickMovement implements BrickMovement {
        int leftMoveCalls;
        int rightMoveCalls;
        int rotationCalls;
        boolean shouldReturnTrue = true;

        @Override
        public boolean moveBrickLeft() {
            leftMoveCalls++;
            return shouldReturnTrue;
        }

        @Override
        public boolean moveBrickRight() {
            rightMoveCalls++;
            return shouldReturnTrue;
        }

        @Override
        public boolean rotateLeftBrick() {
            rotationCalls++;
            return shouldReturnTrue;
        }
    }

    private static final class StubBoardRead implements BoardRead {
        ViewData viewData;

        @Override
        public int[][] getBoardMatrix() {
            return new int[][]{{1}};
        }

        @Override
        public ViewData getViewData() {
            return viewData;
        }
    }
}
