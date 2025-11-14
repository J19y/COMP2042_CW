package com.comp2042.event;

import com.comp2042.model.ShowResult;

public interface MovementInput {
    ShowResult onLeft(MoveEvent event);
    ShowResult onRight(MoveEvent event);
    ShowResult onRotate(MoveEvent event);
}
