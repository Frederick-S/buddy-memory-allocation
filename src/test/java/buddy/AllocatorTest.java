package buddy;

import org.junit.Assert;
import org.junit.Test;

public class AllocatorTest {
    @Test
    public void shouldAllocateMemory() {
        Allocator allocator = new Allocator();
        int[] initFreeBlocks = new int[allocator.getMaxSizeClass()];
        initFreeBlocks[allocator.getMaxSizeClass() - 1] = 1;

        Assert.assertArrayEquals(initFreeBlocks, allocator.getFreeBlocks());

        int[] freeBlocks1 = new int[allocator.getMaxSizeClass()];

        for (int i = 6; i < 15; i++) {
            freeBlocks1[i] = 1;
        }

        int address1 = allocator.alloc(100);
        Assert.assertArrayEquals(freeBlocks1, allocator.getFreeBlocks());

        int[] freeBlocks2 = new int[allocator.getMaxSizeClass()];

        for (int i = 7; i < 15; i++) {
            freeBlocks2[i] = 1;
        }

        int address2 = allocator.alloc(100);
        Assert.assertArrayEquals(freeBlocks2, allocator.getFreeBlocks());

        int[] freeBlocks3 = new int[allocator.getMaxSizeClass()];

        for (int i = 7; i < 15; i++) {
            freeBlocks3[i] = 1;
        }

        freeBlocks3[11] = 0;

        allocator.alloc(4000);
        Assert.assertArrayEquals(freeBlocks3, allocator.getFreeBlocks());

        Assert.assertTrue(address1 > 0);
        Assert.assertNotEquals(address1, address2);
    }

    @Test
    public void shouldFreeMemory() {
        Allocator allocator = new Allocator();
        int address1 = allocator.alloc(100);
        allocator.free(address1);

        int[] freeBlocks1 = new int[allocator.getMaxSizeClass()];
        freeBlocks1[allocator.getMaxSizeClass() - 1] = 1;

        Assert.assertArrayEquals(freeBlocks1, allocator.getFreeBlocks());
    }

    @Test
    public void shouldCheckIfBuddyBlockCanBeMerged() {
        Allocator allocator = new Allocator();
        int address1 = allocator.alloc(100);
        int address2 = allocator.alloc(100);

        int[] freeBlocks1 = new int[allocator.getMaxSizeClass()];

        for (int i = 6; i < 15; i++) {
            freeBlocks1[i] = 1;
        }

        allocator.free(address2);
        Assert.assertArrayEquals(freeBlocks1, allocator.getFreeBlocks());
    }

    @Test
    public void shouldThrowMemoryFullException() {
        Allocator allocator = new Allocator();
        allocator.alloc(1 << (allocator.getMaxSizeClass() - 1));

        Assert.assertThrows("memory is full", RuntimeException.class, () -> allocator.alloc(1 << (allocator.getMaxSizeClass() - 1)));
    }
}
