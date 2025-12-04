package com.comp2042.tetris.engine.spawn;

import java.util.ArrayList;
import java.util.List;


public final class SpawnManager {
    private final BrickSpawn spawner;

    public interface GameOverCallback {
        void onGameOver();
    }

    
    private final List<GameOverCallback> observers = new ArrayList<>();

    public SpawnManager(BrickSpawn spawner) {
        this.spawner = spawner;
    }

    
    
    public void addGameOverObserver(GameOverCallback listener) {
        if (listener != null && !observers.contains(listener)) {
            observers.add(listener);
        }
    }

    
    
    public void removeGameOverObserver(GameOverCallback listener) {
        observers.remove(listener);
    }

    
    public boolean spawn(GameOverCallback callback) {
        boolean gameOver = spawner.spawnBrick().isGameOver();
        if (gameOver) {
            if (callback != null) {
                callback.onGameOver();
            }
            
            if (!observers.isEmpty()) {
                
                List<GameOverCallback> snapshot = new ArrayList<>(observers);
                for (GameOverCallback l : snapshot) {
                    try {
                        l.onGameOver();
                    } catch (RuntimeException ignored) {
                        
                    }
                }
            }
        }
        return gameOver;
    }

    
    
     
    public boolean spawn() {
        return spawn(null);
    }
}

