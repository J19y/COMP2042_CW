package com.comp2042.game;

import com.comp2042.Manager.ScoreManager;
import com.comp2042.event.EventType;
import com.comp2042.event.InputEventListener;
import com.comp2042.event.MoveEvent;
import com.comp2042.model.ShowResult;
import com.comp2042.model.ViewData;
import com.comp2042.ui.GuiController;

/**
 * Main game controller that handles game logic and user input.
 * Acts as a bridge between the GUI controller and the game board,
 * processing game events and updating the view accordingly.
 */
public class GameController implements InputEventListener {

    private final Board board;
    private final GuiController viewGuiController;
    private final SpawnManager spawnManager;
    private final ScoreManager scoreService;
    private final BrickMove moveHandler;
    private final BrickDrop dropHandler;
    // mapping for event handling.
    private final java.util.Map<EventType, java.util.function.Function<MoveEvent, Object>> commands
        = new java.util.EnumMap<>(EventType.class);

    public GameController(GuiController c) {
        this.viewGuiController = c;
        this.board = new SimpleBoard(25, 10);
        this.spawnManager = new SpawnManager(board);
        this.scoreService = new ScoreManager();
        this.moveHandler = new BrickMove(board);
        this.dropHandler = new BrickDrop(board, scoreService, spawnManager);
        spawnManager.spawn(() -> viewGuiController.gameOver());
        registerDefaultCommands();
        setupView();
    }

    // Registers default command handlers for each event type.
    private void registerDefaultCommands() {
        commands.put(EventType.LEFT, e -> moveHandler.handleLeftMove());
        commands.put(EventType.RIGHT, e -> moveHandler.handleRightMove());
        commands.put(EventType.ROTATE, e -> moveHandler.handleRotation());
        commands.put(EventType.DOWN, e -> {
            ShowResult result = dropHandler.handleDrop(e.getEventSource(), () -> viewGuiController.gameOver());
            if (result.getClearRow() != null) {
                viewGuiController.refreshGameBackground(board.getBoardMatrix());
            }
            return result;
        });
    }

    private void setupView() {
        viewGuiController.initGameView(board.getBoardMatrix(), board.getViewData());
        viewGuiController.bindScore(scoreService.scoreProperty());
        viewGuiController.setEventListener(this);
    }

    @Override
    public ShowResult onDownEvent(MoveEvent event) {
        ShowResult result = dropHandler.handleDrop(event.getEventSource(), 
            () -> viewGuiController.gameOver());
        
        // Refresh background if brick landed (rows were cleared indicates landing)
        if (result.getClearRow() != null) {
            viewGuiController.refreshGameBackground(board.getBoardMatrix());
        }
        
        return result;
    }

    @Override
    public ViewData onLeftEvent(MoveEvent event) {
        return moveHandler.handleLeftMove();
    }

    @Override
    public ViewData onRightEvent(MoveEvent event) {
        return moveHandler.handleRightMove();
    }

    @Override
    public ViewData onRotateEvent(MoveEvent event) {
        return moveHandler.handleRotation();
    }

    @Override
    public Object onEvent(MoveEvent event) {
        java.util.function.Function<MoveEvent, Object> handler = commands.get(event.getEventType());
        if (handler == null) {
            // Fallback: return current view data
            return board.getViewData();
        }
        return handler.apply(event);
    }


    @Override
    public void createNewGame() {
        board.newGame();
        scoreService.reset();
        viewGuiController.refreshGameBackground(board.getBoardMatrix());
    }
}
