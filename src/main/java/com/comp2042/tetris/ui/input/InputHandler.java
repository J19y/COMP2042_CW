package com.comp2042.tetris.ui.input;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.BooleanSupplier;

import com.comp2042.tetris.domain.model.ShowResult;

import javafx.scene.Node;
import javafx.scene.input.KeyCode;

/**
 * Processes keyboard input and routes to appropriate handlers.
 * <p>
 * Supports custom key bindings and can be attached to any JavaFX node
 * to receive keyboard events. Manages the mapping between key codes
 * and game event types.
 * </p>
 *
 * @version 1.0
 */
public final class InputHandler {

    /**
     * Callback interface for receiving input results.
     */
    public interface InputCallbacks {
        /**
         * Called when an input produces a result.
         *
         * @param result the result of the input action
         */
        void onResult(ShowResult result);
    }

    
    private static final Map<KeyCode, EventType> KEYMAP = new EnumMap<>(KeyCode.class);
    static {
        KEYMAP.put(KeyCode.LEFT, EventType.LEFT);
        KEYMAP.put(KeyCode.A, EventType.LEFT);
        KEYMAP.put(KeyCode.RIGHT, EventType.RIGHT);
        KEYMAP.put(KeyCode.D, EventType.RIGHT);
        KEYMAP.put(KeyCode.UP, EventType.ROTATE);
        KEYMAP.put(KeyCode.W, EventType.ROTATE);
        KEYMAP.put(KeyCode.DOWN, EventType.DOWN);
        KEYMAP.put(KeyCode.S, EventType.DOWN);
        KEYMAP.put(KeyCode.SPACE, EventType.HARD_DROP);
        KEYMAP.put(KeyCode.P, EventType.PAUSE);
    }

    private Runnable pauseAction;

    /**
     * Registers a custom key binding.
     *
     * @param key the key code to bind
     * @param type the event type to associate with the key
     */
    public static void registerKeyBinding(KeyCode key, EventType type) {
        if (key != null && type != null) {
            KEYMAP.put(key, type);
        }
    }

    /**
     * Attaches this input handler to a JavaFX node.
     *
     * @param focusNode the node to receive keyboard events
     * @param handler the handler to process input actions
     * @param callbacks the callbacks for input results
     * @param canAcceptInput supplier returning true if input should be processed
     */
    public void attach(Node focusNode,
                       InputActionHandler handler,
                       InputCallbacks callbacks,
                       BooleanSupplier canAcceptInput) {
        if (focusNode == null) return;
        focusNode.setOnKeyPressed(keyEvent -> {
            EventType type = KEYMAP.get(keyEvent.getCode());
            if (type == EventType.PAUSE) {
                if (pauseAction != null) {
                    pauseAction.run();
                }
                keyEvent.consume();
                return;
            }
            if (type != null && (canAcceptInput == null || canAcceptInput.getAsBoolean())) {
                    ShowResult result = handler.handle(new MoveEvent(type, EventSource.USER));
                    callbacks.onResult(result);
                    keyEvent.consume();
            }
        });
    }

    /**
     * Sets the action to execute when pause is triggered.
     *
     * @param pauseAction the pause action runnable
     */
    public void setPauseAction(Runnable pauseAction) {
        this.pauseAction = pauseAction;
    }
}

