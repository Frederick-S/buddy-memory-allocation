package buddy;

import java.util.Objects;

public class BlockList {
    private final Block head;
    private final int sizeClass;

    public BlockList(int address, Memory memory, int sizeClass) {
        if (address < 0 || address >= memory.getSize()) {
            throw new IllegalArgumentException("invalid address");
        }

        Objects.requireNonNull(memory, "memory cannot be null");

        if (sizeClass <= 0) {
            throw new IllegalArgumentException("invalid sizeClass");
        }

        this.head = new Block(address, memory);
        this.sizeClass = sizeClass;
    }

    public void clear() {
        this.head.setNext(this.head);
        this.head.setPrev(this.head);
    }

    public boolean isEmpty() {
        return this.head.getNext().equals(this.head);
    }

    public Block getFirst() {
        if (this.isEmpty()) {
            throw new RuntimeException("list must not be empty");
        }

        return this.head.getNext();
    }

    public void insertFront(Block block) {
        this.head.insertAfter(block);
    }

    public boolean hasAvailableBlock(int size) {
        return !this.isEmpty() && Block.getActualSize(this.sizeClass) >= size;
    }

    public int length() {
        int length = 0;
        Block block = this.head.getNext();

        while (!block.equals(this.head)) {
            length += 1;
            block = block.getNext();
        }

        return length;
    }
}
