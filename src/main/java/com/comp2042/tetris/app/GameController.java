package com.comp2042.tetris.app;

import com.comp2042.tetris.mechanics.board.BoardFactory;
import com.comp2042.tetris.mechanics.board.BoardLifecycle;
import com.comp2042.tetris.mechanics.board.BoardPorts;
import com.comp2042.tetris.mechanics.board.BoardRead;
import com.comp2042.tetris.mechanics.board.GameView;
import com.comp2042.tetris.mechanics.board.SimpleBoardFactory;
import com.comp2042.tetris.mechanics.board.SimpleBoardPorts;
import com.comp2042.tetris.mechanics.movement.BrickDrop;
import com.comp2042.tetris.mechanics.movement.BrickDropActions;
import com.comp2042.tetris.mechanics.movement.BrickMove;
import com.comp2042.tetris.mechanics.movement.BrickMovement;
import com.comp2042.tetris.mechanics.spawn.BrickSpawn;
import com.comp2042.tetris.mechanics.spawn.SpawnManager;

import javafx.beans.property.IntegerProperty;
import com.comp2042.tetris.ui.input.EventSource;
import com.comp2042.tetris.ui.input.EventType;
import com.comp2042.tetris.ui.input.MoveEvent;
import java.util.Objects;
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
public class GameController implements GameplayFacade {
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
    private final java.util.Map<EventType, GameCommand> commands
        = new java.util.EnumMap<>(EventType.class);

    public GameController(GameView view) {
        this(view, new ClassicScoringPolicy(), new ScoreManager());
    }

    // Overloaded constructor to inject policy and score service (OCP-friendly)
    public GameController(GameView view, ScoringPolicy policy, ScoreManager scoreManager) {
        this(view, policy, scoreManager, new SimpleBoardFactory());
    }

    public GameController(GameView view, ScoringPolicy policy, ScoreManager scoreManager, BoardFactory boardFactory) {
        this(view, policy, scoreManager,
            new SimpleBoardPorts(Objects.requireNonNull(boardFactory, "boardFactory must not be null").create(25, 10)));
    }

    public GameController(GameView view, ScoringPolicy policy, ScoreManager scoreManager, BoardPorts boardPorts) {
        this.view = view;
        BoardPorts ports = Objects.requireNonNull(boardPorts, "boardPorts must not be null");
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
        // Register observer for game-over events (Observer pattern minimal integration)
        spawnManager.addGameOverObserver(view::gameOver);
        spawnManager.spawn();
        registerDefaultCommands();
        setupView();
    }

    // Registers default command handlers for each event type.
    private void registerDefaultCommands() {
        commands.put(EventType.LEFT, new MoveCommand(moveHandler::handleLeftMove));
        commands.put(EventType.RIGHT, new MoveCommand(moveHandler::handleRightMove));
        commands.put(EventType.ROTATE, new MoveCommand(moveHandler::handleRotation));
        commands.put(EventType.DOWN, new SoftDropCommand(dropHandler, view, reader));
        commands.put(EventType.HARD_DROP,
            new HardDropCommand(dropActions, scoringPolicy, scoreService, spawnManager, reader, view));
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
        GameCommand handler = commands.get(event.getEventType());
        if (handler == null) {
            return new ShowResult(null, reader.getViewData());
        }
        return handler.execute(event);
    }

    // Allow external registration of new commands without modifying this class
    public void registerCommand(EventType type, GameCommand handler) {
        if (type != null && handler != null) {
            commands.put(type, handler);
        }
    }

    private static final class MoveCommand implements GameCommand {
        private final java.util.function.Supplier<ViewData> movement;

        private MoveCommand(java.util.function.Supplier<ViewData> movement) {
            this.movement = movement;
        }

        @Override
        public ShowResult execute(MoveEvent event) {
            return new ShowResult(null, movement.get());
        }
    }

    private static final class SoftDropCommand implements GameCommand {
        private final BrickDrop dropHandler;
        private final GameView view;
        private final BoardRead reader;

        private SoftDropCommand(BrickDrop dropHandler, GameView view, BoardRead reader) {
            this.dropHandler = dropHandler;
            this.view = view;
            this.reader = reader;
        }

        @Override
        public ShowResult execute(MoveEvent event) {
            ShowResult result = dropHandler.handleDrop(event.getEventSource(), () -> view.gameOver());
            if (result.getClearRow() != null) {
                view.refreshGameBackground(reader.getBoardMatrix());
            }
            return result;
        }
    }

    private static final class HardDropCommand implements GameCommand {
        private final BrickDropActions dropActions;
        private final ScoringPolicy scoringPolicy;
        private final ScoreManager scoreService;
        private final SpawnManager spawnManager;
        private final BoardRead reader;
        private final GameView view;

        private HardDropCommand(BrickDropActions dropActions,
                                ScoringPolicy scoringPolicy,
                                ScoreManager scoreService,
                                SpawnManager spawnManager,
                                BoardRead reader,
                                GameView view) {
            this.dropActions = dropActions;
            this.scoringPolicy = scoringPolicy;
            this.scoreService = scoreService;
            this.spawnManager = spawnManager;
            this.reader = reader;
            this.view = view;
        }

        @Override
        public ShowResult execute(MoveEvent event) {
            while (dropActions.moveBrickDown()) {
                int softScore = scoringPolicy.scoreForDrop(EventSource.USER, true);
                if (softScore > 0) {
                    scoreService.add(softScore);
                }
            }
            dropActions.mergeBrickToBackground();
            RowClearResult clear = dropActions.clearRows();
            if (clear.getLinesRemoved() > 0) {
                int bonus = scoringPolicy.scoreForLineClear(clear.getLinesRemoved());
                if (bonus > 0) {
                    scoreService.add(bonus);
                }
                clear = new RowClearResult(clear.getLinesRemoved(), clear.getNewMatrix(), bonus);
            }
            spawnManager.spawn();
            view.refreshGameBackground(reader.getBoardMatrix());
            return new ShowResult(clear, reader.getViewData());
        }
    }

    @Override
    public void createNewGame() {
        boardLifecycle.newGame();
        scoreService.reset();
        view.refreshGameBackground(reader.getBoardMatrix());
    }
    
    @Override
    public IntegerProperty scoreProperty() {
        return scoreService.scoreProperty();
    }
}
