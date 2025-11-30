package com.comp2042.tetris.mechanics.bricks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import org.junit.jupiter.api.Test;

class BrickRegistryTest {

    @Test
    void getInstanceAlwaysReturnsSameObject() {
        BrickRegistry first = BrickRegistry.getInstance();
        BrickRegistry second = BrickRegistry.getInstance();

        
        assertSame(first, second);
    }

    @Test
    void registerAddsSupplierToRegistry() {
        Supplier<Brick> supplier = TestBrick::new;

        BrickRegistry.register(supplier);
        List<Supplier<Brick>> suppliers = BrickRegistry.getInstance().suppliers();

        
        assertTrue(suppliers.contains(supplier));
        assertNotNull(supplier.get().getRotationMatrix());
    }

    @Test
    void suppliersListIsUnmodifiable() {
        List<Supplier<Brick>> suppliers = BrickRegistry.getInstance().suppliers();

        
        assertThrows(UnsupportedOperationException.class, () -> suppliers.add(TestBrick::new));
    }

    @Test
    void staticGetSuppliersMatchesInstanceView() {
        List<Supplier<Brick>> fromInstance = BrickRegistry.getInstance().suppliers();
        List<Supplier<Brick>> fromStatic = BrickRegistry.getSuppliers();

        
        assertEquals(fromInstance, fromStatic);
    }

    private static final class TestBrick implements Brick {

        @Override
        public List<int[][]> getRotationMatrix() {
            return Collections.singletonList(new int[][]{{1}});
        }
    }
}

