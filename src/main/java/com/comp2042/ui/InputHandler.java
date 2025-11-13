package com.comp2042.ui;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.BooleanSupplier;

import com.comp2042.event.EventSource;
import com.comp2042.event.EventType;
import com.comp2042.event.InputEventListener;
import com.comp2042.event.MoveEvent;
import com.comp2042.model.ShowResult;
import com.comp2042.model.ViewData;

import javafx.scene.Node;
import javafx.scene.input.KeyCode;

// Handles key input mapping and dispatches events via a generic listener method.

public final class InputHandler {

    public interface InputCallbacks {
        void onViewUpdate(ViewData data);
        void onDownResult(ShowResult result);
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
                       InputEventListener listener,
                       InputCallbacks callbacks,
                       BooleanSupplier canAcceptInput) {
        if (focusNode == null) return;
        focusNode.setOnKeyPressed(keyEvent -> {
            if (canAcceptInput == null || canAcceptInput.getAsBoolean()) {
                EventType type = KEYMAP.get(keyEvent.getCode());
                if (type != null) {
                    Object result = listener.onEvent(new MoveEvent(type, EventSource.USER));
                    if (result instanceof ViewData vd) {
                        callbacks.onViewUpdate(vd);
                    } else if (result instanceof ShowResult sr) {
                        callbacks.onDownResult(sr);
                    }
                    keyEvent.consume();
                }
            }
        });
    }
}
