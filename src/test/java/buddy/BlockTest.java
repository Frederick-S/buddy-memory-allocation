package buddy;

import org.junit.Assert;
import org.junit.Test;

public class BlockTest {
    @Test
    public void shouldCheckInvalidConstructorArguments() {
        Assert.assertThrows(IllegalArgumentException.class, () -> new Block(-1, new Memory(1)));
        Assert.assertThrows(NullPointerException.class, () -> new Block(1, null));
    }

    @Test
    public void shouldCheckInvalidAddressAndMemory() {
        Assert.assertThrows(NullPointerException.class, () -> Block.fromUserAddress(1, null));
        Assert.assertThrows(IllegalArgumentException.class, () -> Block.fromUserAddress(-1, new Memory(1)));
    }
}
