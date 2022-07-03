package buddy;

import java.util.Objects;

public class Block {
    private final int address;
    private final Memory memory;

    public Block(int address, Memory memory) {
        Objects.requireNonNull(memory, "memory should not be null");

        if (address < 0 || address >= memory.getSize()) {
            throw new IllegalArgumentException("invalid address");
        }

        this.address = address;
        this.memory = memory;
    }

    /**
     * Get a block from userAddress.
     */
    public static Block fromUserAddress(int userAddress, Memory memory) {
        Objects.requireNonNull(memory, "memory should not be null");

        int address = userAddress - Constant.OFFSET_ACTUAL_MEMORY;

        if (address < 0 || address >= memory.getSize()) {
            throw new IllegalArgumentException("invalid address");
        }

        return new Block(address, memory);
    }

    /**
     * Get actual size that user can use within a block.
     */
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

    public void resetPrev() {
        this.memory.setInt32(this.address + Constant.OFFSET_PREV, -1);
    }

    public Block getPrev() {
        int address = this.memory.getInt32(this.address + Constant.OFFSET_PREV);

        return address == -1 ? null : new Block(address, this.memory);
    }

    public void setNext(Block block) {
        this.memory.setInt32(this.address + Constant.OFFSET_NEXT, block.address);
    }

    public void resetNext() {
        this.memory.setInt32(this.address + Constant.OFFSET_NEXT, -1);
    }

    public Block getNext() {
        int address = this.memory.getInt32(this.address + Constant.OFFSET_NEXT);

        return address == -1 ? null : new Block(address, this.memory);
    }

    /**
     * Insert block after current block.
     */
    public void insertAfter(Block block) {
        Block nextBlock = getNext();
        setNext(block);
        block.setPrev(this);
        nextBlock.setPrev(block);
        block.setNext(nextBlock);
    }

    /**
     * Remove current block from the doubly linked list.
     */
    public void removeFromList() {
        Block nextBlock = getNext();
        Block prevBlock = getPrev();
        prevBlock.setNext(nextBlock);
        nextBlock.setPrev(prevBlock);
        resetPrev();
        resetNext();
    }

    public int getAddress() {
        return address;
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

    @Override
    public String toString() {
        boolean used = this.isUsed();
        int sizeClass = this.getSizeClass();
        int prevAddress = this.getPrev() == null ? -1 : this.getPrev().getAddress();
        int nextAddress = this.getNext() == null ? -1 : this.getNext().getAddress();

        return String.format("address: %d, isUsed: %b, sizeClass: %d, prevAddress: %d, nextAddress: %d", this.address, used, sizeClass, prevAddress, nextAddress);
    }
}
