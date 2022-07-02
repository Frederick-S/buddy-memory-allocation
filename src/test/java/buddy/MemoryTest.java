package buddy;

import org.junit.Assert;
import org.junit.Test;

public class MemoryTest {
    @Test
    public void shouldCheckInvalidMemorySize() {
        Assert.assertThrows(IllegalArgumentException.class, () -> new Memory(-1));
    }

    @Test
    public void shouldSetGetBool() {
        Memory memory = new Memory(100);
        memory.setBool(10, true);
        memory.setBool(20, false);

        Assert.assertTrue(memory.getBool(10));
        Assert.assertFalse(memory.getBool(20));
    }

    @Test
    public void shouldSetGetInt32() {
        Memory memory = new Memory(100);
        memory.setInt32(10, 256);
        memory.setInt32(50, 999);

        Assert.assertEquals(256, memory.getInt32(10));
        Assert.assertEquals(999, memory.getInt32(50));
    }

    @Test
    public void shouldCheckInvalidMemoryAddress() {
        Memory memory = new Memory(2);

        Assert.assertThrows(IllegalArgumentException.class, () -> memory.getInt32(100));
        Assert.assertThrows(IllegalArgumentException.class, () -> memory.setInt32(1, 10));
        Assert.assertThrows(IllegalArgumentException.class, () -> memory.getInt32(1));
    }
}
