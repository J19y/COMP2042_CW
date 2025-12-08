package com.comp2042.tetris.application.session;

import java.util.Random;

import com.comp2042.tetris.application.command.GameCommand;
import com.comp2042.tetris.engine.board.GameView;
import com.comp2042.tetris.ui.input.EventType;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.util.Duration;

/**
 * Controller for Mystery game mode with random events.
 * Features progressive difficulty with speed increases every 30 seconds,
 * random mystery events (control inversion, fog, gravity changes),
 * and level progression. Extends BaseGameController.
 *
 */
public class MysteryGameController extends BaseGameController {
    private Timeline speedUpTimer;
    private Timeline eventTimer;
    private Timeline controlsRevertTimeline;
    private Timeline fogRestoreTimeline;
    private Timeline gravityRestoreTimeline;
    private int elapsedSeconds;
    private int speedMultiplier;
    
    private int levelNumber;
    private final javafx.beans.property.IntegerProperty levelProperty = new javafx.beans.property.SimpleIntegerProperty(1);
    private int eventCountdown;
    private final Random rnd = new Random();
    
    private int lastEvent = -1;
    private boolean controlsInverted = false;
    private int originalSpeedMultiplier;
    private boolean paused = false;

    public MysteryGameController(GameView view) {
        super(view);
        
        try { view.bindLevel(levelProperty); } catch (Exception ignored) {}
    }

    @Override
    protected void onStart() {
        try {
            com.comp2042.tetris.services.audio.MusicManager.getInstance().playTrack(com.comp2042.tetris.services.audio.MusicManager.Track.MYSTERY, 900);
        } catch (Exception ignored) {}
        
        stopSpeedUpTimerIfRunning();
        elapsedSeconds = 0;
        speedMultiplier = 1;
        levelNumber = 1;
        try { levelProperty.set(levelNumber); } catch (Exception ignored) {}

        
        speedUpTimer = new Timeline(new KeyFrame(Duration.seconds(1), ev -> {
            elapsedSeconds++;
            if (elapsedSeconds > 0 && elapsedSeconds % 30 == 0) {
                
                speedMultiplier++;
                levelNumber++;
                try { levelProperty.set(levelNumber); } catch (Exception ignored) {}
                try { view.animateLevelIncrement(); } catch (Exception ignored) {}
            }
            
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

    
    public int getSpeedMultiplier() {
        return speedMultiplier;
    }

    
    public int getLevel() {
        return levelNumber;
    }

    public javafx.beans.property.IntegerProperty getLevelProperty() {
        return levelProperty;
    }

    private void scheduleNextEvent() {
        
        eventCountdown = 6 + rnd.nextInt(9); 
    }

    private void triggerRandomEvent() {
        
        
        int pick = -1;
        int attempts = 0;
        while (attempts < 4) {
            attempts++;
            int r = rnd.nextInt(100);
            if (r < 22) {
                pick = 0; 
            } else if (r < 44) {
                pick = 1; 
            } else if (r < 66) {
                pick = 2; 
            } else if (r < 88) {
                pick = 3; 
            } else {
                pick = 4; 
            }
            if (pick != lastEvent) break; 
        }

        lastEvent = pick;

        switch (pick) {
            case 0 -> {
                speedMultiplier++;
                try { view.showEventMessage("Speed Boost!"); } catch (Exception ignored) {}
            }
            case 1 -> toggleControls();
            case 2 -> triggerEarthquake();
            case 3 -> {
                try { view.showFogEffect(3); } catch (Exception ignored) {}
                triggerFog();
            }
            case 4 -> {
                try { view.showHeavyGravityEffect(3); } catch (Exception ignored) {}
                triggerHeavyGravity();
            }
        }
    }

    private void toggleControls() {
        controlsInverted = !controlsInverted;
        if (controlsInverted) {
            
            GameCommand leftCmd = commands.get(EventType.LEFT);
            GameCommand rightCmd = commands.get(EventType.RIGHT);
            if (leftCmd != null && rightCmd != null) {
                commands.put(EventType.LEFT, rightCmd);
                commands.put(EventType.RIGHT, leftCmd);
            }
            try { view.showEventMessage("Controls Inverted!"); } catch (Exception ignored) {}
            
            controlsRevertTimeline = new Timeline(new KeyFrame(Duration.seconds(8), ev -> {
                controlsInverted = false;
                registerDefaultCommands();
                try { view.showEventMessage("Controls Normal"); } catch (Exception ignored) {}
            }));
            controlsRevertTimeline.setCycleCount(1);
            if (!paused) controlsRevertTimeline.play();
        } else {
            registerDefaultCommands();
            try { view.showEventMessage("Controls Normal"); } catch (Exception ignored) {}
        }
    }

    private void triggerEarthquake() {
        
        try {
            if (boardLifecycle != null) {
                
                
                Platform.runLater(() -> {
                    try {
                        
                            ((com.comp2042.tetris.engine.board.SimpleBoard) boardLifecycle).addGarbageLine();
                            view.refreshGameBackground(reader.getBoardMatrix());
                            
                            try { view.playEarthquakeAnimation(); } catch (Exception ignored) {}
                    } catch (ClassCastException ex) {
                        
                    }
                });
            }
            try { view.showEventMessage("Earthquake!"); } catch (Exception ignored) {}
        } catch (Exception ignored) {}
    }

    private void triggerFog() {
        
        try {
            Platform.runLater(() -> view.setBoardVisibility(false));
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
        
        originalSpeedMultiplier = speedMultiplier;
        speedMultiplier = Math.max(30, speedMultiplier * 12);
        try { view.showEventMessage("Heavy Gravity!"); } catch (Exception ignored) {}
        gravityRestoreTimeline = new Timeline(new KeyFrame(Duration.seconds(3), ev -> {
            speedMultiplier = originalSpeedMultiplier;
            try { view.showEventMessage("Gravity Normalized"); } catch (Exception ignored) {}
            gravityRestoreTimeline = null;
        }));
        gravityRestoreTimeline.setCycleCount(1);
        if (!paused) gravityRestoreTimeline.play();
    }

    @Override
    public void createNewGame() {
        super.createNewGame();
        onStart(); 
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


