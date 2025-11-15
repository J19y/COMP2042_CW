package com.comp2042.tetris.mechanics.piece;

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

    private BrickRegistry() {}

    private static final List<Supplier<Brick>> SUPPLIERS = new ArrayList<>();

    static {
        // Default registrations for built-in bricks
        register(IBrick::new);
        register(JBrick::new);
        register(LBrick::new);
        register(OBrick::new);
        register(SBrick::new);
        register(TBrick::new);
        register(ZBrick::new);
    }


    // Register a new brick type.
    public static void register(Supplier<Brick> supplier) {
        if (supplier != null) {
            SUPPLIERS.add(supplier);
        }
    }


    // Returns an unmodifiable view of the registered brick suppliers.
    public static List<Supplier<Brick>> getSuppliers() {
        return Collections.unmodifiableList(SUPPLIERS);
    }
}
