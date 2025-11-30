package com.comp2042.tetris.app;

import com.comp2042.tetris.mechanics.board.BoardFactory;
import com.comp2042.tetris.mechanics.board.BoardLifecycle;
import com.comp2042.tetris.mechanics.board.BoardPorts;
import com.comp2042.tetris.mechanics.board.BoardRead;
import com.comp2042.tetris.mechanics.board.GameView;
import com.comp2042.tetris.mechanics.board.SimpleBoardPorts;
import com.comp2042.tetris.mechanics.movement.BrickDrop;
import com.comp2042.tetris.mechanics.movement.BrickDropActions;
import com.comp2042.tetris.mechanics.movement.BrickMove;
import com.comp2042.tetris.mechanics.movement.BrickMovement;
import com.comp2042.tetris.mechanics.spawn.BrickSpawn;
import com.comp2042.tetris.mechanics.spawn.SpawnManager;
import com.comp2042.tetris.domain.scoring.ScoreManager;
import com.comp2042.tetris.domain.scoring.ScoringPolicy;

public class GameInitializer {
    private final BrickMovement movement;
    private final BrickDropActions dropActions;
    private final BoardRead reader;
    private final BrickSpawn spawner;
    private final BoardLifecycle boardLifecycle;
    private final GameView view;
    private final SpawnManager spawnManager;
    private final ScoreManager scoreService;
    private final BrickMove moveHandler;
    private final BrickDrop dropHandler;
    private final ScoringPolicy scoringPolicy;

    public GameInitializer(GameView view, ScoringPolicy policy, ScoreManager scoreManager, BoardFactory boardFactory) {
        BoardPorts ports = new SimpleBoardPorts(boardFactory.create(22, 10));
        this.movement = ports.movement();
        this.dropActions = ports.dropActions();
        this.reader = ports.reader();
        this.spawner = ports.spawner();
        this.boardLifecycle = ports.lifecycle();
        this.spawnManager = new SpawnManager(spawner);
        this.scoreService = scoreManager;
        this.moveHandler = new BrickMove(movement, reader);
        this.scoringPolicy = policy;
        this.dropHandler = new BrickDrop(dropActions, reader, scoreService, spawnManager, scoringPolicy);
        this.view = view;

        spawnManager.addGameOverObserver(view::gameOver);
        spawnManager.spawn();
    }

    public BrickMovement getMovement() {
        return movement;
    }

    public BrickDropActions getDropActions() {
        return dropActions;
    }

    public BoardRead getReader() {
        return reader;
    }

    public BrickSpawn getSpawner() {
        return spawner;
    }

    public BoardLifecycle getBoardLifecycle() {
        return boardLifecycle;
    }

    public GameView getView() {
        return view;
    }

    public SpawnManager getSpawnManager() {
        return spawnManager;
    }

    public ScoreManager getScoreService() {
        return scoreService;
    }

    public BrickMove getMoveHandler() {
        return moveHandler;
    }

    public BrickDrop getDropHandler() {
        return dropHandler;
    }

    public ScoringPolicy getScoringPolicy() {
        return scoringPolicy;
    }
}
