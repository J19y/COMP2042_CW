package com.comp2042.tetris.ui.view;

import com.comp2042.tetris.domain.model.ViewData;
import com.comp2042.tetris.engine.board.GameView;
import com.comp2042.tetris.util.MatrixOperations;


public final class BufferedGameView extends GameViewDecorator {

    private int[][] lastMatrix;

    public BufferedGameView(GameView delegate) {
        super(delegate);
    }

    @Override
    public void initGameView(int[][] boardMatrix, ViewData activeBrickData) {
        cache(boardMatrix);
        super.initGameView(boardMatrix, activeBrickData);
    }

    @Override
    public void refreshGameBackground(int[][] boardMatrix) {
        if (!isSameMatrix(boardMatrix)) {
            cache(boardMatrix);
            super.refreshGameBackground(boardMatrix);
        }
    }

    private boolean isSameMatrix(int[][] boardMatrix) {
        if (lastMatrix == null || boardMatrix == null) {
            return false;
        }
        if (lastMatrix.length != boardMatrix.length) {
            return false;
        }
        for (int i = 0; i < boardMatrix.length; i++) {
            if (lastMatrix[i].length != boardMatrix[i].length) {
                return false;
            }
            for (int j = 0; j < boardMatrix[i].length; j++) {
                if (lastMatrix[i][j] != boardMatrix[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    private void cache(int[][] boardMatrix) {
        if (boardMatrix == null) {
            lastMatrix = null;
        } else {
            lastMatrix = MatrixOperations.copy(boardMatrix);
        }
    }
}

