package buddy;

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

    private void checkAddress(int address) {
        if (address < 0 || address >= this.memory.length) {
            throw new IllegalArgumentException("illegal address");
        }
    }
}
