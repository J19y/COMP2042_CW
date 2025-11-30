package com.comp2042.tetris.app;

import com.comp2042.tetris.mechanics.board.GameView;
import com.comp2042.tetris.services.audio.MusicManager;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.util.Duration;


public class TimedGameController extends BaseGameController {
    private static final int DEFAULT_SECONDS = 120; 
    private int remainingSeconds = DEFAULT_SECONDS;
    private Timeline countdown;

    public TimedGameController(GameView view) {
        super(view);
    }

    @Override
    protected void onStart() {
        try {
            MusicManager.getInstance().playTrack(MusicManager.Track.RUSH, 900);
        } catch (Exception ignored) {}
        
        stopCountdownIfRunning();
        remainingSeconds = DEFAULT_SECONDS;
        
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

