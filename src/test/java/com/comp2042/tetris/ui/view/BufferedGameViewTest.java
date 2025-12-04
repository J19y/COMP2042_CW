package com.comp2042.tetris.ui.view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

import com.comp2042.tetris.application.port.CreateNewGame;
import com.comp2042.tetris.domain.model.ViewData;
import com.comp2042.tetris.engine.board.GameView;
import com.comp2042.tetris.ui.input.DropInput;
import com.comp2042.tetris.ui.input.InputActionHandler;

import javafx.beans.property.IntegerProperty;

class BufferedGameViewTest {

    private static final ViewData DUMMY_VIEW_DATA =
            new ViewData(new int[][]{{1}}, 0, 0, new int[][]{{0}});

    @Test
    void initCachesMatrixAndDelegates() {
        RecordingGameView delegate = new RecordingGameView();
        BufferedGameView view = new BufferedGameView(delegate);
        int[][] board = new int[][]{{1, 0}, {0, 1}};

        view.initGameView(board, DUMMY_VIEW_DATA);

        
        assertEquals(1, delegate.initCalls);
        assertSame(board, delegate.lastInitMatrix);
    }

    @Test
    void refreshSkipsWhenMatrixUnchanged() {
        RecordingGameView delegate = new RecordingGameView();
        BufferedGameView view = new BufferedGameView(delegate);
        int[][] first = new int[][]{{1, 0}, {0, 1}};
        int[][] identical = new int[][]{{1, 0}, {0, 1}};

        view.initGameView(first, DUMMY_VIEW_DATA);
        view.refreshGameBackground(identical);

        
        assertEquals(0, delegate.refreshCalls);
    }

    @Test
    void refreshDelegatesWhenMatrixChanges() {
        RecordingGameView delegate = new RecordingGameView();
        BufferedGameView view = new BufferedGameView(delegate);
        int[][] first = new int[][]{{1, 0}, {0, 1}};
        int[][] updated = new int[][]{{1, 1}, {0, 0}};

        view.initGameView(first, DUMMY_VIEW_DATA);
        view.refreshGameBackground(updated);

        
        assertEquals(1, delegate.refreshCalls);
        assertSame(updated, delegate.lastRefreshMatrix);
    }

    @Test
    void refreshDelegatesWhenMatrixBecomesNull() {
        RecordingGameView delegate = new RecordingGameView();
        BufferedGameView view = new BufferedGameView(delegate);

        view.initGameView(new int[][]{{1}}, DUMMY_VIEW_DATA);
        view.refreshGameBackground(null);

        
        assertEquals(1, delegate.refreshCalls);
        assertSame(null, delegate.lastRefreshMatrix);
    }

    private static final class RecordingGameView implements GameView {
        int initCalls;
        int refreshCalls;
        int[][] lastInitMatrix;
        int[][] lastRefreshMatrix;

        @Override
        public void initGameView(int[][] boardMatrix, ViewData activeBrickData) {
            initCalls++;
            lastInitMatrix = boardMatrix;
        }

        @Override
        public void refreshGameBackground(int[][] boardMatrix) {
            refreshCalls++;
            lastRefreshMatrix = boardMatrix;
        }

        @Override
        public void bindScore(IntegerProperty scoreProperty) {
            
        }

        @Override
        public void setInputHandlers(InputActionHandler inputActionHandler, DropInput dropInput,
            CreateNewGame gameLifecycle) {
            
        }

        @Override
        public void gameOver() {
            
        }

        @Override
        public void setRemainingTime(int seconds) {
            
        }

        @Override
        public void acceptShowResult(com.comp2042.tetris.domain.model.ShowResult result) {
            
        }

        @Override
        public void settleActiveBrick(Runnable onFinished) {
            if (onFinished != null) onFinished.run();
        }
    }
}

