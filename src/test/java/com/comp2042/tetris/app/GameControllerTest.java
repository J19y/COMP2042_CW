package com.comp2042.tetris.app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.comp2042.tetris.domain.model.RowClearResult;
import com.comp2042.tetris.domain.model.ShowResult;
import com.comp2042.tetris.domain.model.SpawnResult;
import com.comp2042.tetris.domain.model.ViewData;
import com.comp2042.tetris.domain.scoring.ScoreManager;
import com.comp2042.tetris.domain.scoring.ScoringPolicy;
import com.comp2042.tetris.mechanics.board.BoardLifecycle;
import com.comp2042.tetris.mechanics.board.BoardPorts;
import com.comp2042.tetris.mechanics.board.BoardRead;
import com.comp2042.tetris.mechanics.board.GameView;
import com.comp2042.tetris.mechanics.movement.BrickDropActions;
import com.comp2042.tetris.mechanics.movement.BrickMovement;
import com.comp2042.tetris.mechanics.spawn.BrickSpawn;
import com.comp2042.tetris.ui.input.DropInput;
import com.comp2042.tetris.ui.input.EventSource;
import com.comp2042.tetris.ui.input.EventType;
import com.comp2042.tetris.ui.input.InputActionHandler;
import com.comp2042.tetris.ui.input.MoveEvent;

import javafx.beans.property.IntegerProperty;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

final class GameControllerTest {

    private SpyGameView view;
    private TestBoardPorts boardPorts;
    private ScoreManager scoreManager;
    private GameController controller;

    @BeforeEach
    void setUp() {
        view = new SpyGameView();
        boardPorts = new TestBoardPorts();
        scoreManager = new ScoreManager();
        controller = new GameController(view, new FixedScoringPolicy(), scoreManager, boardPorts);
    }

    @Test
    void constructorInitializesViewAndBindings() {
        
        assertTrue(view.initCalled);
        assertSame(boardPorts.reader.getBoardMatrix(), view.initMatrix);
        assertSame(boardPorts.reader.getViewData(), view.initViewData);
        assertSame(scoreManager.scoreProperty(), view.boundScore);
        assertTrue(view.handlersSet);
        assertSame(controller, view.inputHandler);
        assertSame(controller, view.dropInput);
        assertSame(controller, view.newGameHandler);
    }

    @Test
    void onLeftReturnsLatestViewData() {
        
        ViewData expected = new ViewData(new int[][] {{7}}, 1, 2, new int[][] {{4}});
        boardPorts.reader.setViewData(expected);

        ShowResult result = controller.onLeft(new MoveEvent(EventType.LEFT, EventSource.USER));

        assertSame(expected, result.getViewData());
        assertNull(result.getClearRow());
        assertEquals("LEFT", boardPorts.movement.lastAction);
    }

    @Test
    void onDownLandingRefreshesViewAndAwardsScore() {
        
        boardPorts.dropActions.setLandingResult(new RowClearResult(2, new int[][] {{1}}, 0, null));

        ShowResult result = controller.onDown(new MoveEvent(EventType.DOWN, EventSource.USER));

        assertNotNull(result.getClearRow());
        assertEquals(2, result.getClearRow().getLinesRemoved());
        assertEquals(20, result.getClearRow().getScoreBonus());
        assertEquals(20, scoreManager.getValue());
        assertEquals(1, view.refreshCount);
        assertSame(boardPorts.reader.boardMatrix, view.lastRefreshedMatrix);
    }

    @Test
    void handleFallsBackToReaderWhenNoCommandRegistered() {
        
        ViewData expected = new ViewData(new int[][] {{3}}, 0, 0, new int[][] {{2}});
        boardPorts.reader.setViewData(expected);

        ShowResult result = controller.handle(new MoveEvent(EventType.PAUSE, EventSource.USER));

        assertSame(expected, result.getViewData());
        assertNull(result.getClearRow());
    }

    @Test
    void registerCommandAllowsCustomHandler() {
        
        ViewData customView = new ViewData(new int[][] {{5}}, 4, 3, new int[][] {{1}});
        ShowResult expected = new ShowResult(null, customView);
        controller.registerCommand(EventType.PAUSE, event -> expected);

        ShowResult result = controller.handle(new MoveEvent(EventType.PAUSE, EventSource.USER));

        assertSame(expected, result);
    }

    @Test
    void createNewGameResetsBoardAndScore() {
        
        scoreManager.add(99);

        controller.createNewGame();

        assertEquals(0, scoreManager.getValue());
        assertEquals(1, boardPorts.lifecycle.newGameCalls);
        assertEquals(1, view.refreshCount);
        assertSame(boardPorts.reader.boardMatrix, view.lastRefreshedMatrix);
    }

    @Test
    void scorePropertyExposesUnderlyingScoreManager() {
        
        IntegerProperty exposed = controller.scoreProperty();

        assertSame(scoreManager.scoreProperty(), exposed);
    }

    private static final class TestBoardPorts implements BoardPorts {
        final StubBrickMovement movement = new StubBrickMovement();
        final StubDropActions dropActions = new StubDropActions();
        final StubBoardRead reader = new StubBoardRead();
        final StubBrickSpawn spawner = new StubBrickSpawn();
        final StubBoardLifecycle lifecycle = new StubBoardLifecycle();

        @Override
        public BrickMovement movement() {
            return movement;
        }

        @Override
        public BrickDropActions dropActions() {
            return dropActions;
        }

        @Override
        public BoardRead reader() {
            return reader;
        }

        @Override
        public BrickSpawn spawner() {
            return spawner;
        }

        @Override
        public BoardLifecycle lifecycle() {
            return lifecycle;
        }
    }

    private static final class StubBrickMovement implements BrickMovement {
        String lastAction;

        @Override
        public boolean moveBrickLeft() {
            lastAction = "LEFT";
            return true;
        }

        @Override
        public boolean moveBrickRight() {
            lastAction = "RIGHT";
            return true;
        }

        @Override
        public boolean rotateLeftBrick() {
            lastAction = "ROTATE";
            return true;
        }
    }

    private static final class StubDropActions implements BrickDropActions {
        private boolean moveBrickDownReturn = true;
        private RowClearResult clearResult = new RowClearResult(0, new int[][] {{0}}, 0, null);

        @Override
        public boolean moveBrickDown() {
            return moveBrickDownReturn;
        }

        @Override
        public void mergeBrickToBackground() {
            
        }

        @Override
        public RowClearResult clearRows() {
            return clearResult;
        }

        void setLandingResult(RowClearResult result) {
            this.moveBrickDownReturn = false;
            this.clearResult = result;
        }
    }

    private static final class StubBoardRead implements BoardRead {
        private final int[][] boardMatrix = {
            {1, 0},
            {0, 1}
        };
        private ViewData viewData = new ViewData(new int[][] {{1}}, 0, 0, new int[][] {{0}});

        @Override
        public int[][] getBoardMatrix() {
            return boardMatrix;
        }

        @Override
        public ViewData getViewData() {
            return viewData;
        }

        void setViewData(ViewData viewData) {
            this.viewData = viewData;
        }
    }

    private static final class StubBrickSpawn implements BrickSpawn {
        private boolean gameOver;

        @Override
        public SpawnResult spawnBrick() {
            return new SpawnResult(gameOver);
        }
    }

    private static final class StubBoardLifecycle implements BoardLifecycle {
        int newGameCalls;

        @Override
        public void newGame() {
            newGameCalls++;
        }
    }

    private static final class SpyGameView implements GameView {
        boolean initCalled;
        int[][] initMatrix;
        ViewData initViewData;
        IntegerProperty boundScore;
        boolean handlersSet;
        InputActionHandler inputHandler;
        DropInput dropInput;
        CreateNewGame newGameHandler;
        int refreshCount;
        int[][] lastRefreshedMatrix;

        @Override
        public void initGameView(int[][] boardMatrix, ViewData activeBrickData) {
            initCalled = true;
            initMatrix = boardMatrix;
            initViewData = activeBrickData;
        }

        @Override
        public void refreshGameBackground(int[][] boardMatrix) {
            refreshCount++;
            lastRefreshedMatrix = boardMatrix;
        }

        @Override
        public void bindScore(IntegerProperty scoreProperty) {
            boundScore = scoreProperty;
        }

        @Override
        public void setInputHandlers(InputActionHandler inputActionHandler, DropInput dropInput, CreateNewGame gameLifecycle) {
            handlersSet = true;
            this.inputHandler = inputActionHandler;
            this.dropInput = dropInput;
            this.newGameHandler = gameLifecycle;
        }

        @Override
        public void gameOver() {
            
        }

        @Override
        public void acceptShowResult(com.comp2042.tetris.domain.model.ShowResult result) {
            
        }

        @Override
        public void settleActiveBrick(Runnable onFinished) {
            if (onFinished != null) onFinished.run();
        }

        @Override
        public void setRemainingTime(int seconds) {
            
        }
    }

    private static final class FixedScoringPolicy implements ScoringPolicy {
        @Override
        public int scoreForLineClear(int linesCleared) {
            return linesCleared * 10;
        }

        @Override
        public int scoreForDrop(EventSource source, boolean movedDown) {
            return movedDown ? 1 : 0;
        }
    }
}

