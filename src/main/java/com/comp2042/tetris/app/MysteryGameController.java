package com.comp2042.tetris.app;

import java.util.Random;

import com.comp2042.tetris.mechanics.board.GameView;
import com.comp2042.tetris.ui.input.EventType;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.util.Duration;

/**
 * Mystery game mode: increased difficulty with accelerated gravity.
 * Pieces fall faster, and the game difficulty increases progressively.
 * Every 30 seconds, the game becomes slightly harder.
 */
public class MysteryGameController extends BaseGameController {
    private Timeline speedUpTimer;
    private Timeline eventTimer;
    private Timeline controlsRevertTimeline;
    private Timeline fogRestoreTimeline;
    private Timeline gravityRestoreTimeline;
    private int elapsedSeconds;
    private int speedMultiplier;
    private int eventCountdown;
    private final Random rnd = new Random();
    private boolean controlsInverted = false;
    private int originalSpeedMultiplier;
    private boolean paused = false;

    public MysteryGameController(GameView view) {
        super(view);
    }

    @Override
    protected void onStart() {
        try {
            com.comp2042.tetris.services.audio.MusicManager.getInstance().playTrack(com.comp2042.tetris.services.audio.MusicManager.Track.MYSTERY, 900);
        } catch (Exception ignored) {}
        // Start timers: speed-up every 30s, and random events every 15-20s
        stopSpeedUpTimerIfRunning();
        elapsedSeconds = 0;
        speedMultiplier = 1;

        // Speed-up tick (1s) handles periodic difficulty increases
        speedUpTimer = new Timeline(new KeyFrame(Duration.seconds(1), ev -> {
            elapsedSeconds++;
            if (elapsedSeconds > 0 && elapsedSeconds % 30 == 0) {
                speedMultiplier++;
                System.out.println("Mystery Mode: Difficulty increased! Speed multiplier: " + speedMultiplier);
            }
            // ticking event timer
            if (eventCountdown > 0) {
                eventCountdown--;
            } else {
                triggerRandomEvent();
                scheduleNextEvent();
            }
        }));
        speedUpTimer.setCycleCount(Timeline.INDEFINITE);
        speedUpTimer.play();

        scheduleNextEvent();
    }

    /**
     * Get the current speed multiplier for this mystery mode.
     * Can be used by game loop to accelerate piece dropping.
     */
    public int getSpeedMultiplier() {
        return speedMultiplier;
    }

    private void scheduleNextEvent() {
        // Random interval between 8 and 12 seconds (more frequent)
        eventCountdown = 8 + rnd.nextInt(5); // 8..12
    }

    private void triggerRandomEvent() {
        int pick = rnd.nextInt(5);
        switch (pick) {
            case 0:
                // Speed-up (minor)
                speedMultiplier++;
                System.out.println("Mystery Event: Speed Boost! multiplier=" + speedMultiplier);
                try { view.showEventMessage("Speed Boost!"); } catch (Exception ignored) {}
                break;
            case 1:
                toggleControls();
                break;
            case 2:
                triggerEarthquake();
                break;
            case 3:
                triggerFog();
                break;
            case 4:
                triggerHeavyGravity();
                break;
            default:
                break;
        }
    }

    private void toggleControls() {
        controlsInverted = !controlsInverted;
        if (controlsInverted) {
            // Swap LEFT and RIGHT handlers
            GameCommand leftCmd = commands.get(EventType.LEFT);
            GameCommand rightCmd = commands.get(EventType.RIGHT);
            if (leftCmd != null && rightCmd != null) {
                commands.put(EventType.LEFT, rightCmd);
                commands.put(EventType.RIGHT, leftCmd);
            }
            System.out.println("Mystery Event: Controls Inverted!");
            try { view.showEventMessage("Controls Inverted!"); } catch (Exception ignored) {}
            // Revert after 8 seconds
            controlsRevertTimeline = new Timeline(new KeyFrame(Duration.seconds(8), ev -> {
                controlsInverted = false;
                registerDefaultCommands();
                System.out.println("Mystery Event: Controls Normal.");
                try { view.showEventMessage("Controls Normal"); } catch (Exception ignored) {}
            }));
            controlsRevertTimeline.setCycleCount(1);
            if (!paused) controlsRevertTimeline.play();
        } else {
            registerDefaultCommands();
            System.out.println("Mystery Event: Controls Normal.");
            try { view.showEventMessage("Controls Normal"); } catch (Exception ignored) {}
        }
    }

    private void triggerEarthquake() {
        // Add a garbage line; this will push rows up and change board state
        try {
            if (boardLifecycle != null) {
                // boardLifecycle is SimpleBoard which now implements addGarbageLine()
                // run on FX thread just in case view updates are required
                Platform.runLater(() -> {
                    try {
                        // use reflection-safe cast by calling method if present
                            ((com.comp2042.tetris.mechanics.board.SimpleBoard) boardLifecycle).addGarbageLine();
                            view.refreshGameBackground(reader.getBoardMatrix());
                            // Ask view to play a short earthquake animation so the event is obvious
                            try { view.playEarthquakeAnimation(); } catch (Exception ignored) {}
                    } catch (ClassCastException ex) {
                        // If not SimpleBoard, ignore
                    }
                });
            }
            System.out.println("Mystery Event: Earthquake! Garbage line added.");
            try { view.showEventMessage("Earthquake!"); } catch (Exception ignored) {}
        } catch (Exception ignored) {}
    }

    private void triggerFog() {
        // Hide board for 3 seconds
        try {
            Platform.runLater(() -> view.setBoardVisibility(false));
            System.out.println("Mystery Event: Fog! Board hidden.");
            try { view.showEventMessage("The Fog"); } catch (Exception ignored) {}
            fogRestoreTimeline = new Timeline(new KeyFrame(Duration.seconds(3), ev -> {
                Platform.runLater(() -> view.setBoardVisibility(true));
                fogRestoreTimeline = null;
            }));
            fogRestoreTimeline.setCycleCount(1);
            if (!paused) fogRestoreTimeline.play();
        } catch (Exception ignored) {}
    }

    private void triggerHeavyGravity() {
        // Temporarily increase gravity (speed multiplier) for 5 seconds
        originalSpeedMultiplier = speedMultiplier;
        speedMultiplier = Math.max(10, speedMultiplier * 5);
        System.out.println("Mystery Event: HEAVY GRAVITY! multiplier=" + speedMultiplier);
        try { view.showEventMessage("Heavy Gravity!"); } catch (Exception ignored) {}
        gravityRestoreTimeline = new Timeline(new KeyFrame(Duration.seconds(5), ev -> {
            speedMultiplier = originalSpeedMultiplier;
            System.out.println("Mystery Event: Gravity Normalized. multiplier=" + speedMultiplier);
            try { view.showEventMessage("Gravity Normalized"); } catch (Exception ignored) {}
            gravityRestoreTimeline = null;
        }));
        gravityRestoreTimeline.setCycleCount(1);
        if (!paused) gravityRestoreTimeline.play();
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
        if (eventTimer != null) {
            eventTimer.stop();
            eventTimer = null;
        }
        if (controlsRevertTimeline != null) {
            controlsRevertTimeline.stop();
            controlsRevertTimeline = null;
        }
        if (fogRestoreTimeline != null) {
            fogRestoreTimeline.stop();
            fogRestoreTimeline = null;
        }
        if (gravityRestoreTimeline != null) {
            gravityRestoreTimeline.stop();
            gravityRestoreTimeline = null;
        }
    }

    @Override
    public void pauseMode() {
        paused = true;
        if (speedUpTimer != null && speedUpTimer.getStatus() == javafx.animation.Animation.Status.RUNNING) {
            speedUpTimer.pause();
        }
        if (controlsRevertTimeline != null && controlsRevertTimeline.getStatus() == javafx.animation.Animation.Status.RUNNING) {
            controlsRevertTimeline.pause();
        }
        if (fogRestoreTimeline != null && fogRestoreTimeline.getStatus() == javafx.animation.Animation.Status.RUNNING) {
            fogRestoreTimeline.pause();
        }
        if (gravityRestoreTimeline != null && gravityRestoreTimeline.getStatus() == javafx.animation.Animation.Status.RUNNING) {
            gravityRestoreTimeline.pause();
        }
    }

    @Override
    public void resumeMode() {
        paused = false;
        if (speedUpTimer != null) {
            speedUpTimer.play();
        }
        if (controlsRevertTimeline != null) {
            controlsRevertTimeline.play();
        }
        if (fogRestoreTimeline != null) {
            fogRestoreTimeline.play();
        }
        if (gravityRestoreTimeline != null) {
            gravityRestoreTimeline.play();
        }
    }

    @Override
    protected void gameOver() {
        stopSpeedUpTimerIfRunning();
        super.gameOver();
    }
}

