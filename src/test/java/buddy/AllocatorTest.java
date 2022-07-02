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
        Assert.assertTrue(address1 > 0);
        Assert.assertNotEquals(address1, address2);
    }
}
