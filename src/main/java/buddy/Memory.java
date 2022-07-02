package buddy;

import java.nio.ByteBuffer;

public class Memory {
    private final byte[] memory;

    public Memory(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("size should be greater than zero");
        }

        this.memory = new byte[size];
    }

    public void setBool(int address, boolean value) {
        checkAddress(address);

        this.memory[address] = value ? (byte) 1 : (byte) 0;
    }

    public boolean getBool(int address) {
        checkAddress(address);

        return this.memory[address] == (byte) 1;
    }

    public void setInt32(int address, int value) {
        checkAddress(address);

        byte[] bytes = ByteBuffer.allocate(Constant.INT32_SIZE).putInt(value).array();
        setByteArray(address, bytes);
    }

    public int getInt32(int address) {
        checkAddress(address);

        if (address + Constant.INT32_SIZE > this.memory.length) {
            throw new IllegalArgumentException("address overflow");
        }

        byte[] bytes = new byte[Constant.INT32_SIZE];

        System.arraycopy(this.memory, address, bytes, 0, Constant.INT32_SIZE);

        return ByteBuffer.wrap(bytes).getInt();
    }

    private void setByteArray(int address, byte[] bytes) {
        checkAddress(address);

        if (bytes == null || bytes.length == 0) {
            throw new IllegalArgumentException("empty bytes");
        }

        if (address + bytes.length > this.memory.length) {
            throw new IllegalArgumentException("address overflow");
        }

        System.arraycopy(bytes, 0, this.memory, address, bytes.length);
    }

    private void checkAddress(int address) {
        if (address < 0 || address >= this.memory.length) {
            throw new IllegalArgumentException("illegal address");
        }
    }
}
