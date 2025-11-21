package com.comp2042.tetris.mechanics.movement;

import com.comp2042.tetris.mechanics.board.BoardRead;
import com.comp2042.tetris.mechanics.spawn.SpawnManager;

import com.comp2042.tetris.ui.input.EventSource;
import com.comp2042.tetris.domain.model.RowClearResult;
import com.comp2042.tetris.domain.model.ShowResult;
import com.comp2042.tetris.domain.scoring.ScoreManager;
import com.comp2042.tetris.domain.scoring.ScoringPolicy;

/**
 * Handles brick dropping logic including landing, row clearing, and scoring.
 * Depends only on minimal drop actions and read-only board access.
 */
public final class BrickDrop {

    private final BrickDropActions dropActions;
    private final BoardRead reader;
    private final ScoreManager scoreService;
    private final SpawnManager spawnManager;
    private final ScoringPolicy scoringPolicy;

    public BrickDrop(BrickDropActions dropActions, BoardRead reader, ScoreManager scoreService, SpawnManager spawnManager, ScoringPolicy scoringPolicy) {
        this.dropActions = dropActions;
        this.reader = reader;
        this.scoreService = scoreService;
        this.spawnManager = spawnManager;
        this.scoringPolicy = scoringPolicy;
    }

    // Handle a brick drop attempt.
    public ShowResult handleDrop(EventSource source, Runnable onGameOver) {
        boolean canMove = dropActions.moveBrickDown();
        RowClearResult result = null;

        if (!canMove) {
            // Brick has landed
            dropActions.mergeBrickToBackground();
            result = dropActions.clearRows();

            if (result.getLinesRemoved() > 0) {
                int scoreBonus = scoringPolicy.scoreForLineClear(result.getLinesRemoved());
                if (scoreBonus > 0) {
                    scoreService.add(scoreBonus);
                }
                // Update result with calculated score for display
                result = new RowClearResult(result.getLinesRemoved(), result.getNewMatrix(), scoreBonus);
            }

            // Notify via SpawnManager observers rather than direct callback
            spawnManager.spawn();
        } else {
            int dropScore = scoringPolicy.scoreForDrop(source, true);
            if (dropScore > 0) {
                scoreService.add(dropScore);
            }
        }

        return new ShowResult(result, reader.getViewData());
    }
}
