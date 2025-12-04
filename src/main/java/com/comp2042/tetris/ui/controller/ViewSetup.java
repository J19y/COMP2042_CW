package com.comp2042.tetris.ui.controller;

import com.comp2042.tetris.engine.board.BoardRead;
import com.comp2042.tetris.engine.board.GameView;
import com.comp2042.tetris.domain.scoring.ScoreManager;
import com.comp2042.tetris.application.port.GameplayPort;

public class ViewSetup {
    public void setupView(GameView view, BoardRead reader, ScoreManager scoreService, GameplayPort controller) {
        view.initGameView(reader.getBoardMatrix(), reader.getViewData());
        view.bindScore(scoreService.scoreProperty());
        view.setInputHandlers(controller, controller, controller);
    }
}
