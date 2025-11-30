package com.comp2042.tetris.app;

import com.comp2042.tetris.mechanics.board.BoardRead;
import com.comp2042.tetris.mechanics.board.GameView;
import com.comp2042.tetris.domain.scoring.ScoreManager;

public class ViewSetup {
    public void setupView(GameView view, BoardRead reader, ScoreManager scoreService, GameplayFacade controller) {
        view.initGameView(reader.getBoardMatrix(), reader.getViewData());
        view.bindScore(scoreService.scoreProperty());
        view.setInputHandlers(controller, controller, controller);
    }
}
