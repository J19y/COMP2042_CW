package com.comp2042.tetris.application.command;

import com.comp2042.tetris.domain.model.RowClearResult;
import com.comp2042.tetris.domain.model.ShowResult;
import com.comp2042.tetris.domain.model.ViewData;
import com.comp2042.tetris.domain.scoring.ScoreManager;
import com.comp2042.tetris.domain.scoring.ScoringPolicy;
import com.comp2042.tetris.engine.board.BoardRead;
import com.comp2042.tetris.engine.board.GameView;
import com.comp2042.tetris.engine.movement.BrickDrop;
import com.comp2042.tetris.engine.movement.BrickDropActions;
import com.comp2042.tetris.engine.movement.BrickMove;
import com.comp2042.tetris.engine.spawn.SpawnManager;
import com.comp2042.tetris.ui.input.EventSource;
import com.comp2042.tetris.ui.input.EventType;
import com.comp2042.tetris.ui.input.MoveEvent;

import java.util.EnumMap;
import java.util.Map;

public class CommandRegistrar {
    private final Map<EventType, GameCommand> commands = new EnumMap<>(EventType.class);

    public void registerDefaultCommands(BrickMove moveHandler,
                                        BrickDrop dropHandler,
                                        BrickDropActions dropActions,
                                        ScoringPolicy scoringPolicy,
                                        ScoreManager scoreService,
                                        SpawnManager spawnManager,
                                        BoardRead reader,
                                        GameView view) {
        commands.put(EventType.LEFT, new MoveCommand(moveHandler::handleLeftMove));
        commands.put(EventType.RIGHT, new MoveCommand(moveHandler::handleRightMove));
        commands.put(EventType.ROTATE, new MoveCommand(moveHandler::handleRotation));
        commands.put(EventType.DOWN, new SoftDropCommand(dropHandler, view, reader));
        commands.put(EventType.HARD_DROP,
            new HardDropCommand(dropActions, scoringPolicy, scoreService, spawnManager, reader, view));
    }

    public void registerCommand(EventType type, GameCommand handler) {
        if (type != null && handler != null) {
            commands.put(type, handler);
        }
    }

    public GameCommand getCommand(EventType type) {
        return commands.get(type);
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
            ViewData current = reader.getViewData();
            boolean wouldCollide = com.comp2042.tetris.util.CollisionDetector.isCollision(
                reader.getBoardMatrix(), current.getBrickData(), current.getxPosition(), current.getyPosition() + 1);

            if (wouldCollide) {
                view.settleActiveBrick(() -> {
                    ShowResult asyncResult = dropHandler.handleDrop(event.getEventSource(), () -> view.gameOver());
                    view.acceptShowResult(asyncResult);
                    if (asyncResult.getClearRow() != null) {
                        view.refreshGameBackground(reader.getBoardMatrix());
                    }
                });
                return new ShowResult(null, reader.getViewData());
            } else {
                ShowResult result = dropHandler.handleDrop(event.getEventSource(), () -> view.gameOver());
                if (result.getClearRow() != null) {
                    view.refreshGameBackground(reader.getBoardMatrix());
                }
                return result;
            }
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
                clear = new RowClearResult(clear.getLinesRemoved(), clear.getNewMatrix(), bonus, clear.getClearedRows());
            }
            spawnManager.spawn();
            view.refreshGameBackground(reader.getBoardMatrix());
            return new ShowResult(clear, reader.getViewData());
        }
    }
}
