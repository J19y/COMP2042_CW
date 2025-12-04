package com.comp2042.tetris.application.session;

import java.util.Objects;

import com.comp2042.tetris.application.command.GameCommand;
import com.comp2042.tetris.application.port.GameModeLifecycle;
import com.comp2042.tetris.application.port.GameplayPort;
import com.comp2042.tetris.domain.model.RowClearResult;
import com.comp2042.tetris.domain.model.ShowResult;
import com.comp2042.tetris.domain.model.ViewData;
import com.comp2042.tetris.domain.scoring.ClassicScoringPolicy;
import com.comp2042.tetris.domain.scoring.ScoreManager;
import com.comp2042.tetris.domain.scoring.ScoringPolicy;
import com.comp2042.tetris.engine.board.BoardFactory;
import com.comp2042.tetris.engine.board.BoardLifecycle;
import com.comp2042.tetris.engine.board.BoardPorts;
import com.comp2042.tetris.engine.board.BoardRead;
import com.comp2042.tetris.engine.board.GameView;
import com.comp2042.tetris.engine.board.SimpleBoardFactory;
import com.comp2042.tetris.engine.board.SimpleBoardPorts;
import com.comp2042.tetris.engine.movement.BrickDrop;
import com.comp2042.tetris.engine.movement.BrickDropActions;
import com.comp2042.tetris.engine.movement.BrickMove;
import com.comp2042.tetris.engine.movement.BrickMovement;
import com.comp2042.tetris.engine.spawn.BrickSpawn;
import com.comp2042.tetris.engine.spawn.SpawnManager;
import com.comp2042.tetris.services.audio.MusicManager;
import com.comp2042.tetris.ui.input.EventSource;
import com.comp2042.tetris.ui.input.EventType;
import com.comp2042.tetris.ui.input.MoveEvent;

import javafx.beans.property.IntegerProperty;


public class BaseGameController implements GameplayPort, GameModeLifecycle {
    protected final BrickMovement movement;
    protected final BrickDropActions dropActions;
    protected final BoardRead reader;
    protected final BrickSpawn spawner;
    protected final BoardLifecycle boardLifecycle;
    protected final GameView view; 
    protected final SpawnManager spawnManager;
    protected final ScoreManager scoreService;
    protected final BrickMove moveHandler;
    protected final BrickDrop dropHandler;
    protected final ScoringPolicy scoringPolicy;
    
    protected final java.util.Map<EventType, GameCommand> commands
        = new java.util.EnumMap<>(EventType.class);

    
    protected volatile boolean active = true;
    protected int totalLinesCleared = 0;
    protected long gameStartTime = 0;

    public BaseGameController(GameView view) {
        this(view, new ClassicScoringPolicy(), new ScoreManager());
    }

    public BaseGameController(GameView view, ScoringPolicy policy, ScoreManager scoreManager) {
        this(view, policy, scoreManager, new SimpleBoardFactory());
    }

    public BaseGameController(GameView view, ScoringPolicy policy, ScoreManager scoreManager, BoardFactory boardFactory) {
        this(view, policy, scoreManager,
            new SimpleBoardPorts(Objects.requireNonNull(boardFactory, "boardFactory must not be null").create(22, 10)));
    }

    public BaseGameController(GameView view, ScoringPolicy policy, ScoreManager scoreManager, BoardPorts boardPorts) {
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
        
        
        spawnManager.addGameOverObserver(this::gameOver);
        spawnManager.spawn();
        registerDefaultCommands();
        setupView();
        
    }

    
    protected void onStart() {
        
    }

    @Override
    public void startMode() {
        gameStartTime = System.currentTimeMillis();
        onStart();
    }

    @Override
    public void pauseMode() {
        
    }

    @Override
    public void resumeMode() {
        
    }

    
    protected void registerDefaultCommands() {
        commands.put(EventType.LEFT, new MoveLeftCommand(movement, reader));
        commands.put(EventType.RIGHT, new MoveRightCommand(movement, reader));
        commands.put(EventType.ROTATE, new RotateCommand(movement, reader));
        commands.put(EventType.DOWN, new SoftDropCommand(dropHandler, view, reader));
        commands.put(EventType.HARD_DROP,
            new HardDropCommand(dropActions, scoringPolicy, scoreService, spawnManager, reader, view));
    }

    protected void setupView() {
        view.initGameView(reader.getBoardMatrix(), reader.getViewData());
        view.bindScore(scoreService.scoreProperty());
        
        try { view.bindLevel(null); } catch (Exception ignored) {}
        view.setInputHandlers(this, this, this);
    }

    @Override
    public ShowResult onDown(MoveEvent event) {
        
        if (!active) return new ShowResult(null, reader.getViewData());
        ShowResult result = dropHandler.handleDrop(event.getEventSource(), () -> view.gameOver());
        if (result.getClearRow() != null) {
            view.refreshGameBackground(reader.getBoardMatrix());
        }
        return result;
    }

    @Override
    public ShowResult onLeft(MoveEvent event) {
        if (!active) return new ShowResult(null, reader.getViewData());
        ViewData vd = moveHandler.handleLeftMove();
        try {
            com.comp2042.tetris.services.audio.MusicManager mm = com.comp2042.tetris.services.audio.MusicManager.getInstance();
            double original = mm.getMusicVolume();
            
            try { mm.fadeMusicTo(Math.max(0.01, original * 0.45), 40); } catch (Exception ignored) {}
            mm.playSfxAtVolume("/audio/RotationSoundEffect.mp3", 0.95);
            
            try { mm.fadeMusicTo(original, 200); } catch (Exception ignored) {}
        } catch (Exception ignored) {}
        return new ShowResult(null, vd);
    }

    @Override
    public ShowResult onRight(MoveEvent event) {
        if (!active) return new ShowResult(null, reader.getViewData());
        ViewData vd = moveHandler.handleRightMove();
        try {
            com.comp2042.tetris.services.audio.MusicManager mm = com.comp2042.tetris.services.audio.MusicManager.getInstance();
            double original = mm.getMusicVolume();
            try { mm.fadeMusicTo(Math.max(0.01, original * 0.45), 40); } catch (Exception ignored) {}
            mm.playSfxAtVolume("/audio/RotationSoundEffect.mp3", 0.95);
            try { mm.fadeMusicTo(original, 200); } catch (Exception ignored) {}
        } catch (Exception ignored) {}
        return new ShowResult(null, vd);
    }

    @Override
    public ShowResult onRotate(MoveEvent event) {
        if (!active) return new ShowResult(null, reader.getViewData());
        ViewData vd = moveHandler.handleRotation();
        try {
            com.comp2042.tetris.services.audio.MusicManager mm = com.comp2042.tetris.services.audio.MusicManager.getInstance();
            double original = mm.getMusicVolume();
            try { mm.fadeMusicTo(Math.max(0.01, original * 0.35), 30); } catch (Exception ignored) {}
            mm.playSfxAtVolume("/audio/RotationSoundEffect.mp3", 1.0);
            try { mm.fadeMusicTo(original, 200); } catch (Exception ignored) {}
        } catch (Exception ignored) {}
        return new ShowResult(null, vd);
    }

    @Override
    public ShowResult handle(MoveEvent event) {
        if (!active) return new ShowResult(null, reader.getViewData());
        GameCommand handler = commands.get(event.getEventType());
        if (handler == null) {
            return new ShowResult(null, reader.getViewData());
        }
        return handler.execute(event);
    }

    
    public void registerCommand(EventType type, GameCommand handler) {
        if (type != null && handler != null) {
            commands.put(type, handler);
        }
    }

    private static final class MoveLeftCommand implements GameCommand {
        private final BrickMovement movement;
        private final BoardRead reader;

        private MoveLeftCommand(BrickMovement movement, BoardRead reader) {
            this.movement = movement;
            this.reader = reader;
        }

        @Override
        public ShowResult execute(MoveEvent event) {
            boolean moved = movement.moveBrickLeft();
            ViewData vd = reader.getViewData();
            if (moved) {
                try {
                    com.comp2042.tetris.services.audio.MusicManager mm = com.comp2042.tetris.services.audio.MusicManager.getInstance();
                    double original = mm.getMusicVolume();
                    try { mm.fadeMusicTo(Math.max(0.01, original * 0.45), 40); } catch (Exception ignored) {}
                    mm.playSfxAtVolume("/audio/RotationSoundEffect.mp3", 0.95);
                    try { mm.fadeMusicTo(original, 200); } catch (Exception ignored) {}
                } catch (Exception ignored) {}
            }
            return new ShowResult(null, vd);
        }
    }

    private static final class MoveRightCommand implements GameCommand {
        private final BrickMovement movement;
        private final BoardRead reader;

        private MoveRightCommand(BrickMovement movement, BoardRead reader) {
            this.movement = movement;
            this.reader = reader;
        }

        @Override
        public ShowResult execute(MoveEvent event) {
            boolean moved = movement.moveBrickRight();
            ViewData vd = reader.getViewData();
            if (moved) {
                try {
                    com.comp2042.tetris.services.audio.MusicManager mm = com.comp2042.tetris.services.audio.MusicManager.getInstance();
                    double original = mm.getMusicVolume();
                    try { mm.fadeMusicTo(Math.max(0.01, original * 0.45), 40); } catch (Exception ignored) {}
                    mm.playSfxAtVolume("/audio/RotationSoundEffect.mp3", 0.95);
                    try { mm.fadeMusicTo(original, 200); } catch (Exception ignored) {}
                } catch (Exception ignored) {}
            }
            return new ShowResult(null, vd);
        }
    }

    private static final class RotateCommand implements GameCommand {
        private final BrickMovement movement;
        private final BoardRead reader;

        private RotateCommand(BrickMovement movement, BoardRead reader) {
            this.movement = movement;
            this.reader = reader;
        }

        @Override
        public ShowResult execute(MoveEvent event) {
            boolean rotated = movement.rotateLeftBrick();
            ViewData vd = reader.getViewData();
            if (rotated) {
                try {
                    com.comp2042.tetris.services.audio.MusicManager mm = com.comp2042.tetris.services.audio.MusicManager.getInstance();
                    double original = mm.getMusicVolume();
                    try { mm.fadeMusicTo(Math.max(0.01, original * 0.5), 30); } catch (Exception ignored) {}
                    mm.playSfxAtVolume("/audio/RotationSoundEffect.mp3", 1.0);
                    try { mm.fadeMusicTo(original, 200); } catch (Exception ignored) {}
                } catch (Exception ignored) {}
            }
            return new ShowResult(null, vd);
        }
    }

    private final class SoftDropCommand implements GameCommand {
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
            
            com.comp2042.tetris.domain.model.ViewData current = reader.getViewData();
            boolean wouldCollide = com.comp2042.tetris.util.CollisionDetector.isCollision(
                reader.getBoardMatrix(), current.getBrickData(), current.getxPosition(), current.getyPosition() + 1);

            if (wouldCollide) {
                
                view.settleActiveBrick(() -> {
                    ShowResult asyncResult = dropHandler.handleDrop(event.getEventSource(), () -> view.gameOver());
                    
                    view.acceptShowResult(asyncResult);
                    if (asyncResult.getClearRow() != null) {
                        view.refreshGameBackground(reader.getBoardMatrix());
                        totalLinesCleared += asyncResult.getClearRow().getLinesRemoved();
                    }
                });
                try { MusicManager.getInstance().playSfx("/audio/BricksCollisionEffect.mp3"); } catch (Exception ignored) {}
                
                return new ShowResult(null, reader.getViewData());
            } else {
                ShowResult result = dropHandler.handleDrop(event.getEventSource(), () -> view.gameOver());
                if (result.getClearRow() != null) {
                    view.refreshGameBackground(reader.getBoardMatrix());
                    totalLinesCleared += result.getClearRow().getLinesRemoved();
                }
                return result;
            }
        }
    }

    private final class HardDropCommand implements GameCommand {
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
                clear = new RowClearResult(clear.getLinesRemoved(), clear.getNewMatrix(), bonus, clear.getClearedRows());
                totalLinesCleared += clear.getLinesRemoved();
            }
            spawnManager.spawn();
            view.refreshGameBackground(reader.getBoardMatrix());
            try { MusicManager.getInstance().playSfx("/audio/BricksCollisionEffect.mp3"); } catch (Exception ignored) {}
            return new ShowResult(clear, reader.getViewData());
        }
    }

    @Override
    public void createNewGame() {
        
        active = true;
        totalLinesCleared = 0;
        boardLifecycle.newGame();
        scoreService.reset();
        view.refreshGameBackground(reader.getBoardMatrix());
        
        
    }
    
    @Override
    public IntegerProperty scoreProperty() {
        return scoreService.scoreProperty();
    }

    
    protected void gameOver() {
        
        active = false;
        view.gameOver();
    }

    public int getTotalLinesCleared() {
        return totalLinesCleared;
    }

    public long getGameStartTime() {
        return gameStartTime;
    }
}

