package com.comp2042.tetris.ui.input;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.BooleanSupplier;
import com.comp2042.tetris.domain.model.ShowResult;

import javafx.scene.Node;
import javafx.scene.input.KeyCode;



public final class InputHandler {

    public interface InputCallbacks {
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

    public void setPauseAction(Runnable pauseAction) {
        this.pauseAction = pauseAction;
    }
}

