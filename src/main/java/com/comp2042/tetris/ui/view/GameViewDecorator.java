package com.comp2042.tetris.ui.view;

import java.util.Objects;

import com.comp2042.tetris.app.CreateNewGame;
import com.comp2042.tetris.domain.model.ShowResult;
import com.comp2042.tetris.domain.model.ViewData;
import com.comp2042.tetris.mechanics.board.GameView;
import com.comp2042.tetris.ui.input.DropInput;
import com.comp2042.tetris.ui.input.InputActionHandler;

import javafx.beans.property.IntegerProperty;

// used as a base decorator for GameView implementations
public class GameViewDecorator implements GameView {

    protected final GameView delegate;

    public GameViewDecorator(GameView delegate) {
        this.delegate = Objects.requireNonNull(delegate, "delegate must not be null");
    }

    @Override
    public void initGameView(int[][] boardMatrix, ViewData activeBrickData) {
        delegate.initGameView(boardMatrix, activeBrickData);
    }

    @Override
    public void refreshGameBackground(int[][] boardMatrix) {
        delegate.refreshGameBackground(boardMatrix);
    }

    @Override
    public void bindScore(IntegerProperty scoreProperty) {
        delegate.bindScore(scoreProperty);
    }

    @Override
    public void bindLevel(IntegerProperty levelProperty) {
        delegate.bindLevel(levelProperty);
    }

    @Override
    public void setInputHandlers(InputActionHandler inputActionHandler, DropInput dropInput,
        CreateNewGame gameLifecycle) {
        delegate.setInputHandlers(inputActionHandler, dropInput, gameLifecycle);
    }
    @Override
    public void gameOver() {
        delegate.gameOver();
    }

    @Override
    public void setRemainingTime(int seconds) {
        delegate.setRemainingTime(seconds);
    }

    @Override
    public void settleActiveBrick(Runnable onSettled) {
        delegate.settleActiveBrick(onSettled);
    }

    @Override
    public void acceptShowResult(ShowResult showResult) {
        delegate.acceptShowResult(showResult);
    }

    @Override
    public void showMessage(String message) {
        delegate.showMessage(message);
    }

    @Override
    public void showEventMessage(String message) {
        delegate.showEventMessage(message);
    }

    @Override
    public void playEarthquakeAnimation() {
        delegate.playEarthquakeAnimation();
    }

    @Override
    public void animateLevelIncrement() {
        delegate.animateLevelIncrement();
    }

    @Override
    public void showFogEffect(int seconds) {
        delegate.showFogEffect(seconds);
    }

    @Override
    public void showHeavyGravityEffect(int seconds) {
        delegate.showHeavyGravityEffect(seconds);
    }
}
