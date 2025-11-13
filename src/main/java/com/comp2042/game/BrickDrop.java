package com.comp2042.game;

import com.comp2042.event.EventSource;
import com.comp2042.model.RowClearResult;
import com.comp2042.model.ShowResult;
import com.comp2042.score.ScoreManager;
import com.comp2042.score.ScoringPolicy;

/**
 * Handles brick dropping logic including landing, row clearing, and scoring.
 * Scoring is delegated to a ScoringPolicy.
 */
public final class BrickDrop {
    
    private final Board board;
    private final ScoreManager scoreService;
    private final SpawnManager spawnManager;
    private final ScoringPolicy scoringPolicy;

    public BrickDrop(Board board, ScoreManager scoreService, SpawnManager spawnManager, ScoringPolicy scoringPolicy) {
        this.board = board;
        this.scoreService = scoreService;
        this.spawnManager = spawnManager;
        this.scoringPolicy = scoringPolicy;
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
                int scoreBonus = scoringPolicy.scoreForLineClear(result.getLinesRemoved());
                if (scoreBonus > 0) {
                    scoreService.add(scoreBonus);
                }
                // Update result with calculated score for display
                result = new RowClearResult(result.getLinesRemoved(), result.getNewMatrix(), scoreBonus);
            }
            
            spawnManager.spawn(onGameOver::run);
        } else {
            int dropScore = scoringPolicy.scoreForDrop(source, true);
            if (dropScore > 0) {
                scoreService.add(dropScore);
            }
        }
        
        return new ShowResult(result, board.getViewData());
    }
}
