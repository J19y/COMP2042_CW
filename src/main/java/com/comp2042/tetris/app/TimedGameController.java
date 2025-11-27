package com.comp2042.tetris.app;

import com.comp2042.tetris.mechanics.board.GameView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

/**
 * Timed game mode: players must finish before the countdown expires.
 */
public class TimedGameController extends BaseGameController {
    private static final int DEFAULT_SECONDS = 120; // 2 minutes
    private int remainingSeconds = DEFAULT_SECONDS;
    private Timeline countdown;

    public TimedGameController(GameView view) {
        super(view);
    }

    @Override
    protected void onStart() {
        // reset timer
        stopCountdownIfRunning();
        remainingSeconds = DEFAULT_SECONDS;
        countdown = new Timeline(new KeyFrame(Duration.seconds(1), ev -> {
            remainingSeconds--;
            if (remainingSeconds <= 0) {
                stopCountdownIfRunning();
                // trigger game over on FX thread
                gameOver();
            }
        }));
        countdown.setCycleCount(Timeline.INDEFINITE);
        countdown.play();
    }

    @Override
    public void createNewGame() {
        super.createNewGame();
        // restart timer when a new game is requested
        onStart();
    }

    private void stopCountdownIfRunning() {
        if (countdown != null) {
            countdown.stop();
            countdown = null;
        }
    }

    @Override
    protected void gameOver() {
        stopCountdownIfRunning();
        super.gameOver();
    }
}
