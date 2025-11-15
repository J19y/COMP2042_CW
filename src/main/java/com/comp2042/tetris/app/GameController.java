package com.comp2042.tetris.app;

import com.comp2042.tetris.mechanics.board.BoardLifecycle;
import com.comp2042.tetris.mechanics.board.BoardRead;
import com.comp2042.tetris.mechanics.board.GameView;
import com.comp2042.tetris.mechanics.board.SimpleBoard;
import com.comp2042.tetris.mechanics.movement.BrickDrop;
import com.comp2042.tetris.mechanics.movement.BrickDropActions;
import com.comp2042.tetris.mechanics.movement.BrickMove;
import com.comp2042.tetris.mechanics.movement.BrickMovement;
import com.comp2042.tetris.mechanics.spawn.BrickSpawn;
import com.comp2042.tetris.mechanics.spawn.SpawnManager;

import com.comp2042.tetris.ui.input.DropInput;
import com.comp2042.tetris.ui.input.EventSource;
import com.comp2042.tetris.ui.input.EventType;
import com.comp2042.tetris.ui.input.InputActionHandler;
import com.comp2042.tetris.ui.input.MoveEvent;
import com.comp2042.tetris.ui.input.MovementInput;
import com.comp2042.tetris.domain.model.RowClearResult;
import com.comp2042.tetris.domain.model.ShowResult;
import com.comp2042.tetris.domain.model.ViewData;
import com.comp2042.tetris.domain.scoring.ClassicScoringPolicy;
import com.comp2042.tetris.domain.scoring.ScoreManager;
import com.comp2042.tetris.domain.scoring.ScoringPolicy;

/**
 * Main game controller that handles game logic and user input.
 * Acts as a bridge between the GUI controller and the game board,
 * processing game events and updating the view accordingly.
 */
public class GameController implements InputActionHandler, MovementInput, DropInput, CreateNewGame {

    private final BrickMovement movement;
    private final BrickDropActions dropActions;
    private final BoardRead reader;
    private final BrickSpawn spawner;
    private final BoardLifecycle boardLifecycle;
    private final GameView view; // Depends on abstraction, not concrete UI
    private final SpawnManager spawnManager;
    private final ScoreManager scoreService;
    private final BrickMove moveHandler;
    private final BrickDrop dropHandler;
    private final ScoringPolicy scoringPolicy;
    // mapping for event handling.
    private final java.util.Map<EventType, java.util.function.Function<MoveEvent, Object>> commands
        = new java.util.EnumMap<>(EventType.class);

    public GameController(GameView view) {
        this(view, new ClassicScoringPolicy(), new ScoreManager());
    }

    // Overloaded constructor to inject policy and score service (OCP-friendly)
    public GameController(GameView view, ScoringPolicy policy, ScoreManager scoreManager) {
        this.view = view;
        SimpleBoard board = new SimpleBoard(25, 10);
        this.movement = board;
        this.dropActions = board;
        this.reader = board;
        this.spawner = board;
        this.boardLifecycle = board;
        this.spawnManager = new SpawnManager(spawner);
        this.scoreService = scoreManager;
        this.moveHandler = new BrickMove(movement, reader);
        this.scoringPolicy = policy;
        this.dropHandler = new BrickDrop(dropActions, reader, scoreService, spawnManager, scoringPolicy);
        spawnManager.spawn(() -> view.gameOver());
        registerDefaultCommands();
        setupView();
    }

    // Registers default command handlers for each event type.
    private void registerDefaultCommands() {
        commands.put(EventType.LEFT, e -> moveHandler.handleLeftMove());
        commands.put(EventType.RIGHT, e -> moveHandler.handleRightMove());
        commands.put(EventType.ROTATE, e -> moveHandler.handleRotation());
        commands.put(EventType.DOWN, e -> {
            ShowResult result = dropHandler.handleDrop(e.getEventSource(), () -> view.gameOver());
            if (result.getClearRow() != null) {
                view.refreshGameBackground(reader.getBoardMatrix());
            }
            return result;
        });
        commands.put(EventType.HARD_DROP, e -> {
            // Hard drop: move down until collision; score per move; then land and clear/spawn
            while (dropActions.moveBrickDown()) {
                int softScore = scoringPolicy.scoreForDrop(EventSource.USER, true);
                if (softScore > 0) {
                    scoreService.add(softScore);
                }
            }
            // Land and clear rows
            dropActions.mergeBrickToBackground();
            RowClearResult clear = dropActions.clearRows();
            if (clear.getLinesRemoved() > 0) {
                int bonus = scoringPolicy.scoreForLineClear(clear.getLinesRemoved());
                if (bonus > 0) {
                    scoreService.add(bonus);
                }
                clear = new RowClearResult(clear.getLinesRemoved(), clear.getNewMatrix(), bonus);
            }
            // Spawn next piece and refresh background
            spawnManager.spawn(() -> view.gameOver());
            view.refreshGameBackground(reader.getBoardMatrix());
            return new ShowResult(clear, reader.getViewData());
        });
    }

    private void setupView() {
        view.initGameView(reader.getBoardMatrix(), reader.getViewData());
        view.bindScore(scoreService.scoreProperty());
        view.setInputHandlers(this, this, this);
    }

    @Override
    public ShowResult onDown(MoveEvent event) {
        ShowResult result = dropHandler.handleDrop(event.getEventSource(), () -> view.gameOver());
        if (result.getClearRow() != null) {
            view.refreshGameBackground(reader.getBoardMatrix());
        }
        return result;
    }

    @Override
    public ShowResult onLeft(MoveEvent event) {
        ViewData vd = moveHandler.handleLeftMove();
        return new ShowResult(null, vd);
    }

    @Override
    public ShowResult onRight(MoveEvent event) {
        ViewData vd = moveHandler.handleRightMove();
        return new ShowResult(null, vd);
    }

    @Override
    public ShowResult onRotate(MoveEvent event) {
        ViewData vd = moveHandler.handleRotation();
        return new ShowResult(null, vd);
    }

    @Override
    public ShowResult handle(MoveEvent event) {
        java.util.function.Function<MoveEvent, Object> handler = commands.get(event.getEventType());
        if (handler == null) {
            return new ShowResult(null, reader.getViewData());
        }
        Object result = handler.apply(event);
        if (result instanceof ShowResult sr) {
            return sr;
        } else if (result instanceof ViewData vd) { // legacy handlers
            return new ShowResult(null, vd);
        }
        // Fallback should never happen, but ensures non-null postcondition
        return new ShowResult(null, reader.getViewData());
    }

    // Allow external registration of new commands without modifying this class
    public void registerCommand(EventType type, java.util.function.Function<MoveEvent, Object> handler) {
        if (type != null && handler != null) {
            commands.put(type, handler);
        }
    }

    @Override
    public void createNewGame() {
        boardLifecycle.newGame();
        scoreService.reset();
        view.refreshGameBackground(reader.getBoardMatrix());
    }
}
