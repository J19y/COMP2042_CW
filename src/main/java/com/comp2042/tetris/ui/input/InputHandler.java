package com.comp2042.tetris.ui.input;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.BooleanSupplier;

import com.comp2042.tetris.ui.input.EventSource;
import com.comp2042.tetris.ui.input.EventType;
import com.comp2042.tetris.ui.input.InputActionHandler;
import com.comp2042.tetris.ui.input.MoveEvent;
import com.comp2042.tetris.domain.model.ShowResult;

import javafx.scene.Node;
import javafx.scene.input.KeyCode;

// Handles key input mapping and dispatches events via a generic handler.

public final class InputHandler {

    public interface InputCallbacks {
        void onResult(ShowResult result);
    }

    // Key mapping to EventType to avoid if/else chains.
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
    }

    // Allow external modules to register additional key bindings (OCP-friendly)
    public static void registerKeyBinding(KeyCode key, EventType type) {
        if (key != null && type != null) {
            KEYMAP.put(key, type);
        }
    }

    public void attach(Node focusNode,
                       InputActionHandler handler,
                       InputCallbacks callbacks,
                       BooleanSupplier canAcceptInput) {
        if (focusNode == null) return;
        focusNode.setOnKeyPressed(keyEvent -> {
            if (canAcceptInput == null || canAcceptInput.getAsBoolean()) {
                EventType type = KEYMAP.get(keyEvent.getCode());
                if (type != null) {
                    ShowResult result = handler.handle(new MoveEvent(type, EventSource.USER));
                    callbacks.onResult(result);
                    keyEvent.consume();
                }
            }
        });
    }
}
