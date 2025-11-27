package com.comp2042.tetris.app;

import com.comp2042.tetris.mechanics.board.GameView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
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
        // push initial remaining time to view
        try {
            Platform.runLater(() -> view.setRemainingTime(remainingSeconds));
        } catch (Exception ignored) {}
        countdown = new Timeline(new KeyFrame(Duration.seconds(1), ev -> {
            remainingSeconds--;
            try {
                Platform.runLater(() -> view.setRemainingTime(remainingSeconds));
            } catch (Exception ignored) {}
            if (remainingSeconds <= 0) {
                stopCountdownIfRunning();
                // ensure view shows 00:00 then trigger game over on FX thread
                try {
                    Platform.runLater(() -> view.setRemainingTime(0));
                } catch (Exception ignored) {}
                gameOver();
            }
        }));
        countdown.setCycleCount(Timeline.INDEFINITE);
        countdown.play();
    }

    @Override
    public void createNewGame() {
        super.createNewGame();
        // timer will be started when the UI countdown finishes (startMode is invoked)
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

    @Override
    public void pauseMode() {
        if (countdown != null) {
            countdown.pause();
        }
    }

    @Override
    public void resumeMode() {
        if (countdown != null) {
            countdown.play();
        }
    }
}
