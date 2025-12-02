package com.comp2042.tetris.engine.board;

import java.util.Objects;

import com.comp2042.tetris.engine.bricks.RandomBrickGeneratorFactory;




public class SimpleBoardFactory implements BoardFactory {

    private final BoardComponentsFactory componentsFactory;

    public SimpleBoardFactory() {
        this(new DefaultBoardComponentsFactory(new RandomBrickGeneratorFactory()));
    }

    public SimpleBoardFactory(BoardComponentsFactory componentsFactory) {
        this.componentsFactory = Objects.requireNonNull(componentsFactory, "componentsFactory must not be null");
    }

    @Override
    public SimpleBoard create(int rows, int cols) {
        return new SimpleBoard(rows, cols, componentsFactory);
    }
}

