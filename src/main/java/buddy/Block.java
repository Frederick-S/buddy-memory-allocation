package buddy;

import java.util.Objects;

public class Block {
    private final int address;
    private final Memory memory;

    public Block(int address, Memory memory) {
        if (address < 0 || address >= memory.getSize()) {
            throw new IllegalArgumentException("invalid address");
        }

        Objects.requireNonNull(memory, "memory should not be null");

        this.address = address;
        this.memory = memory;
    }

    public static Block fromUserAddress(int userAddress, Memory memory) {
        int address = userAddress - Constant.OFFSET_ACTUAL_MEMORY;

        if (address < 0 || address >= memory.getSize()) {
            throw new IllegalArgumentException("invalid address");
        }

        return new Block(address, memory);
    }

    public static int getActualSize(int sizeClass) {
        return (1 << sizeClass) - Constant.OFFSET_ACTUAL_MEMORY;
    }

    public int getUserAddress() {
        return this.address + Constant.OFFSET_ACTUAL_MEMORY;
    }

    public void setUsed() {
        this.memory.setBool(this.address, true);
    }

    public boolean isUsed() {
        return this.memory.getBool(this.address);
    }

    public void setFree() {
        this.memory.setBool(this.address, false);
    }

    public void setSizeClass(int sizeClass) {
        this.memory.setInt32(this.address + Constant.OFFSET_SIZE_CLASS, sizeClass);
    }

    public int getSizeClass() {
        return this.memory.getInt32(this.address + Constant.OFFSET_SIZE_CLASS);
    }

    public void setPrev(Block block) {
        this.memory.setInt32(this.address + Constant.OFFSET_PREV, block.getAddress());
    }

    public Block getPrev() {
        int address = this.memory.getInt32(this.address + Constant.OFFSET_PREV);

        return new Block(address, this.memory);
    }

    public void setNext(Block block) {
        this.memory.setInt32(this.address + Constant.OFFSET_NEXT, block.address);
    }

    public Block getNext() {
        int address = this.memory.getInt32(this.address + Constant.OFFSET_NEXT);

        return new Block(address, this.memory);
    }

    public void insertAfter(Block block) {
        Block nextBlock = getNext();
        setNext(block);
        block.setPrev(this);
        nextBlock.setPrev(block);
        block.setNext(nextBlock);
    }

    public void removeFromList() {
        Block nextBlock = getNext();
        Block prevBlock = getPrev();
        prevBlock.setNext(nextBlock);
        nextBlock.setPrev(prevBlock);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Block block = (Block) o;

        return address == block.address;
    }

    @Override
    public int hashCode() {
        return Objects.hash(address);
    }

    public int getAddress() {
        return address;
    }
}
