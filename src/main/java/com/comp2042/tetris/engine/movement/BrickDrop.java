package com.comp2042.tetris.engine.movement;

import com.comp2042.tetris.engine.board.BoardRead;
import com.comp2042.tetris.engine.spawn.SpawnManager;

import com.comp2042.tetris.ui.input.EventSource;
import com.comp2042.tetris.domain.model.RowClearResult;
import com.comp2042.tetris.domain.model.ShowResult;
import com.comp2042.tetris.domain.scoring.ScoreManager;
import com.comp2042.tetris.domain.scoring.ScoringPolicy;


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

    
    public ShowResult handleDrop(EventSource source, Runnable onGameOver) {
        boolean canMove = dropActions.moveBrickDown();
        RowClearResult result = null;

        if (!canMove) {
            
            dropActions.mergeBrickToBackground();
            result = dropActions.clearRows();

            if (result.getLinesRemoved() > 0) {
                int scoreBonus = scoringPolicy.scoreForLineClear(result.getLinesRemoved());
                if (scoreBonus > 0) {
                    scoreService.add(scoreBonus);
                }
                
                result = new RowClearResult(result.getLinesRemoved(), result.getNewMatrix(), scoreBonus, result.getClearedRows());
            }

            
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

