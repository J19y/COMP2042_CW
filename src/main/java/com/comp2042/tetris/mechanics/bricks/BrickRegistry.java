package com.comp2042.tetris.mechanics.bricks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

/**
 * Registry for available Brick types. Generators can query this registry to
 * obtain brick suppliers instead of hardcoding knowledge of all brick classes.
 * New bricks can be added via without modifying existing generator code (OCP).
 */
public final class BrickRegistry {

    private static final BrickRegistry INSTANCE = new BrickRegistry();

    private final List<Supplier<Brick>> suppliers = new ArrayList<>();

    private BrickRegistry() {
        registerInternal(IBrick::new);
        registerInternal(JBrick::new);
        registerInternal(LBrick::new);
        registerInternal(OBrick::new);
        registerInternal(SBrick::new);
        registerInternal(TBrick::new);
        registerInternal(ZBrick::new);
    }

    public static BrickRegistry getInstance() {
        return INSTANCE;
    }

    // Register a new brick type.
    public static void register(Supplier<Brick> supplier) {
        INSTANCE.registerInternal(supplier);
    }

    private void registerInternal(Supplier<Brick> supplier) {
        if (supplier != null) {
            suppliers.add(supplier);
        }
    }

    public List<Supplier<Brick>> suppliers() {
        return Collections.unmodifiableList(suppliers);
    }

    // Returns an unmodifiable view of the registered brick suppliers.
    public static List<Supplier<Brick>> getSuppliers() {
        return INSTANCE.suppliers();
    }
}
