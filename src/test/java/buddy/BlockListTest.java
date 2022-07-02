package buddy;

import org.junit.Assert;
import org.junit.Test;

public class BlockListTest {
    @Test
    public void shouldCheckInvalidConstructorArguments() {
        Assert.assertThrows(IllegalArgumentException.class, () -> new BlockList(-1, new Memory(1), 1));
        Assert.assertThrows(NullPointerException.class, () -> new BlockList(1, null, 1));
        Assert.assertThrows(IllegalArgumentException.class, () -> new BlockList(1, new Memory(1), -1));
    }

    @Test
    public void shouldThrowExceptionOnPopEmptyList() {
        Assert.assertThrows(RuntimeException.class, () -> new BlockList(1, new Memory(1), 1).getFirst());
    }
}
