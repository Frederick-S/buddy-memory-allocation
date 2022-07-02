package buddy;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AllocatorTest {
    private Allocator allocator;

    @Before
    public void setup() {
        allocator = new Allocator();
    }

    @Test
    public void shouldAllocMemory() {
        int address1 = allocator.alloc(100);
        int address2 = allocator.alloc(100);

        Assert.assertTrue(address1 > 0);
        Assert.assertNotEquals(address1, address2);
    }
}
