package com.comp2042.game;

import com.comp2042.event.EventSource;
import com.comp2042.model.RowClearResult;
import com.comp2042.model.ShowResult;
import com.comp2042.service.ScoreManager;

/**
 * Handles brick dropping logic including landing, row clearing, and scoring.
 * Extracted from GameController to follow Single Responsibility Principle.
 */
public final class BrickDrop {
    
    private final Board board;
    private final ScoreManager scoreService;
    private final SpawnManager spawnManager;


    public BrickDrop(Board board, ScoreManager scoreService, SpawnManager spawnManager) {
        this.board = board;
        this.scoreService = scoreService;
        this.spawnManager = spawnManager;
    }


    // Handle a brick drop attempt.
    public ShowResult handleDrop(EventSource source, Runnable onGameOver) {
        boolean canMove = board.moveBrickDown();
        RowClearResult result = null;
        
        if (!canMove) {
            // Brick has landed
            board.mergeBrickToBackground();
            result = board.clearRows();
            
            if (result.getLinesRemoved() > 0) {
                // Calculate score bonus using classic Tetris formula
                int scoreBonus = calculateClearBonus(result.getLinesRemoved());
                scoreService.add(scoreBonus);
                // Update result with calculated score for display
                result = new RowClearResult(result.getLinesRemoved(), result.getNewMatrix(), scoreBonus);
            }
            
            // Spawn new brick (may trigger game over)
            spawnManager.spawn(() -> onGameOver.run());
        } else {
            // Brick moved down successfully
            if (source == EventSource.USER) {
                // Award points for manual drop
                scoreService.add(1);
            }
        }
        
        return new ShowResult(result, board.getViewData());
    }

    
    // Calculate score bonus for clearing lines.
    // Uses classic Tetris scoring: 50 * lines^2
    private int calculateClearBonus(int linesCleared) {
        return 50 * linesCleared * linesCleared;
    }
}
