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
    /**
     * Logical level number shown to the player. This is a monotonic counter incremented
     * every 30 seconds and is separate from the speed multiplier (which may have
     * temporary spikes for events).
     */
    private int levelNumber;
    private final javafx.beans.property.IntegerProperty levelProperty = new javafx.beans.property.SimpleIntegerProperty(1);
    private int eventCountdown;
    private final Random rnd = new Random();
    // Last event id to avoid immediate repeats
    private int lastEvent = -1;
    private boolean controlsInverted = false;
    private int originalSpeedMultiplier;
    private boolean paused = false;

    public MysteryGameController(GameView view) {
        super(view);
        // Bind after construction so subclass fields are initialized (super() may call setupView earlier)
        try { view.bindLevel(levelProperty); } catch (Exception ignored) {}
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
        levelNumber = 1;
        try { levelProperty.set(levelNumber); } catch (Exception ignored) {}

        // Speed-up tick (1s) handles periodic difficulty increases
        speedUpTimer = new Timeline(new KeyFrame(Duration.seconds(1), ev -> {
            elapsedSeconds++;
            if (elapsedSeconds > 0 && elapsedSeconds % 30 == 0) {
                // Increase both the speed used by mechanics and the logical player-visible level.
                speedMultiplier++;
                levelNumber++;
                try { levelProperty.set(levelNumber); } catch (Exception ignored) {}
                try { view.animateLevelIncrement(); } catch (Exception ignored) {}
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

    /**
     * Player-visible level number (monotonic) used by the UI.
     */
    public int getLevel() {
        return levelNumber;
    }

    public javafx.beans.property.IntegerProperty getLevelProperty() {
        return levelProperty;
    }

    private void scheduleNextEvent() {
        // Random interval between 6 and 14 seconds to make timing less predictable
        eventCountdown = 6 + rnd.nextInt(9); // 6..14
    }

    private void triggerRandomEvent() {
        // Weighted random selection so events feel varied but balanced.
        // We'll avoid repeating the same event immediately.
        int pick = -1;
        int attempts = 0;
        while (attempts < 4) {
            attempts++;
            int r = rnd.nextInt(100);
            if (r < 22) {
                pick = 0; // speed boost (22%)
            } else if (r < 44) {
                pick = 1; // controls invert (22%)
            } else if (r < 66) {
                pick = 2; // earthquake (22%)
            } else if (r < 88) {
                pick = 3; // fog (22%)
            } else {
                pick = 4; // heavy gravity (12%) rarer
            }
            if (pick != lastEvent) break; // accept if different
        }

        lastEvent = pick;

        if (pick == 0) {
            // Speed-up (minor)
            speedMultiplier++;
            System.out.println("Mystery Event: Speed Boost! multiplier=" + speedMultiplier);
            try { view.showEventMessage("Speed Boost!"); } catch (Exception ignored) {}
        } else if (pick == 1) {
            toggleControls();
        } else if (pick == 2) {
            triggerEarthquake();
        } else if (pick == 3) {
            // Show fog visual and let controller manage restoring board visibility
            try { view.showFogEffect(3); } catch (Exception ignored) {}
            triggerFog();
        } else if (pick == 4) {
                // Show heavy-gravity visual and apply mechanics spike (short burst)
                try { view.showHeavyGravityEffect(3); } catch (Exception ignored) {}
            triggerHeavyGravity();
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
        // Make heavy gravity far more punishing: much larger multiplier for stronger effect
        speedMultiplier = Math.max(30, speedMultiplier * 12);
        System.out.println("Mystery Event: HEAVY GRAVITY! multiplier=" + speedMultiplier);
        try { view.showEventMessage("Heavy Gravity!"); } catch (Exception ignored) {}
        // Heavy gravity is a temporary multiplier spike for mechanics only; keep levelNumber unchanged.
        // Keep the spike for a short but noticeable time (3s)
        gravityRestoreTimeline = new Timeline(new KeyFrame(Duration.seconds(3), ev -> {
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

