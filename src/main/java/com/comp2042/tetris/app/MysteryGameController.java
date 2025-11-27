package com.comp2042.tetris.app;

import com.comp2042.tetris.mechanics.board.GameView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

/**
 * Mystery game mode: increased difficulty with accelerated gravity.
 * Pieces fall faster, and the game difficulty increases progressively.
 * Every 30 seconds, the game becomes slightly harder.
 */
public class MysteryGameController extends BaseGameController {
    private Timeline speedUpTimer;
    private int elapsedSeconds;
    private int speedMultiplier;

    public MysteryGameController(GameView view) {
        super(view);
    }

    @Override
    protected void onStart() {
        // Start the speed-up timer (every 30 seconds, increase difficulty)
        stopSpeedUpTimerIfRunning();
        elapsedSeconds = 0;
        speedMultiplier = 1;
        
        speedUpTimer = new Timeline(new KeyFrame(Duration.seconds(1), ev -> {
            elapsedSeconds++;
            // Every 30 seconds, the game becomes harder
            if (elapsedSeconds > 0 && elapsedSeconds % 30 == 0) {
                speedMultiplier++;
                // Visual/audio feedback could go here
                System.out.println("Mystery Mode: Difficulty increased! Speed multiplier: " + speedMultiplier);
            }
        }));
        speedUpTimer.setCycleCount(Timeline.INDEFINITE);
        speedUpTimer.play();
    }

    /**
     * Get the current speed multiplier for this mystery mode.
     * Can be used by game loop to accelerate piece dropping.
     */
    public int getSpeedMultiplier() {
        return speedMultiplier;
    }

    @Override
    public void createNewGame() {
        super.createNewGame();
        onStart(); // restart speed-up timer
    }

    private void stopSpeedUpTimerIfRunning() {
        if (speedUpTimer != null) {
            speedUpTimer.stop();
            speedUpTimer = null;
        }
    }

    @Override
    protected void gameOver() {
        stopSpeedUpTimerIfRunning();
        super.gameOver();
    }
}

