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

        int address1 = allocator.alloc(100);
        int address2 = allocator.alloc(100);

        Assert.assertTrue(address1 > 0);
        Assert.assertNotEquals(address1, address2);
    }
}
