package com.comp2042.event;

import com.comp2042.model.ShowResult;
import com.comp2042.model.ViewData;

public interface InputEventListener {

    ShowResult onDownEvent(MoveEvent event);

    ViewData onLeftEvent(MoveEvent event);

    ViewData onRightEvent(MoveEvent event);

    ViewData onRotateEvent(MoveEvent event);

    void createNewGame();
}
