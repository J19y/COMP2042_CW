package com.comp2042.tetris.application.port;

import com.comp2042.tetris.ui.input.DropInput;
import com.comp2042.tetris.ui.input.InputActionHandler;
import com.comp2042.tetris.ui.input.MovementInput;

import javafx.beans.property.IntegerProperty;

public interface GameplayPort extends InputActionHandler, MovementInput, DropInput, CreateNewGame {
    IntegerProperty scoreProperty();
}

