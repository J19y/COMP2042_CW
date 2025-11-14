package com.comp2042.event;

import com.comp2042.model.ShowResult;


public interface InputActionHandler {
    ShowResult handle(MoveEvent event);
}
