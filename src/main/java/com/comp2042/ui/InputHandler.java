package com.comp2042.ui;

import java.util.function.BooleanSupplier;

import com.comp2042.event.EventSource;
import com.comp2042.event.EventType;
import com.comp2042.event.InputEventListener;
import com.comp2042.event.MoveEvent;
import com.comp2042.model.ShowResult;
import com.comp2042.model.ViewData;

import javafx.scene.Node;
import javafx.scene.input.KeyCode;

// Handles key input mapping and dispatches events to the game controller.

public final class InputHandler {

    public interface InputCallbacks {
        void onViewUpdate(ViewData data);
        void onDownResult(ShowResult result);
    }

    public void attach(Node focusNode,
                       InputEventListener listener,
                       InputCallbacks callbacks,
                       BooleanSupplier canAcceptInput) {
        if (focusNode == null) return;
        focusNode.setOnKeyPressed(keyEvent -> {
            if (canAcceptInput == null || canAcceptInput.getAsBoolean()) {
                KeyCode code = keyEvent.getCode();
                boolean handled = false;
                if (code == KeyCode.LEFT || code == KeyCode.A) {
                    callbacks.onViewUpdate(listener.onLeftEvent(new MoveEvent(EventType.LEFT, EventSource.USER)));
                    handled = true;
                } else if (code == KeyCode.RIGHT || code == KeyCode.D) {
                    callbacks.onViewUpdate(listener.onRightEvent(new MoveEvent(EventType.RIGHT, EventSource.USER)));
                    handled = true;
                } else if (code == KeyCode.UP || code == KeyCode.W) {
                    callbacks.onViewUpdate(listener.onRotateEvent(new MoveEvent(EventType.ROTATE, EventSource.USER)));
                    handled = true;
                } else if (code == KeyCode.DOWN || code == KeyCode.S) {
                    callbacks.onDownResult(listener.onDownEvent(new MoveEvent(EventType.DOWN, EventSource.USER)));
                    handled = true;
                }
                if (handled) {
                    keyEvent.consume();
                }
            }
        });
    }
}
