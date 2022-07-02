package buddy;

public class Allocator {
    private final Memory memory;
    private final BlockList[] blockLists;

    private static final int MIN_SIZE_CLASS = 4;
    private static final int MAX_SIZE_CLASS = 16;

    public Allocator() {
        int allHeadSentinelSize = this.getMemoryOffset();
        int maxMemorySize = (1 << MAX_SIZE_CLASS) + allHeadSentinelSize;
        this.memory = new Memory(maxMemorySize);
        this.blockLists = new BlockList[MAX_SIZE_CLASS];

        for (int i = 0; i < MAX_SIZE_CLASS; i++) {
            int sizeClass = i + 1;
            int headSentinelAddress = Constant.HEAD_SENTINEL_SIZE * i;
            this.blockLists[i] = new BlockList(headSentinelAddress, this.memory, sizeClass);
            this.blockLists[i].clear();
        }

        // The single full block
        Block block = new Block(allHeadSentinelSize, this.memory);
        block.setSizeClass(MAX_SIZE_CLASS);
        block.setFree();
        this.blockLists[MAX_SIZE_CLASS - 1].insertFront(block);
    }

    public int alloc(int size) {
        Block block = null;

        for (int i = 0; i < MAX_SIZE_CLASS; i++) {
            BlockList blockList = this.blockLists[i];

            if (!blockList.hasAvailableBlock(size)) {
                continue;
            }

            block = blockList.getFirst();
            block = this.split(block, size);
            block.setUsed();

            break;
        }

        if (block == null) {
            throw new RuntimeException("memory is full");
        }

        return block.getUserAddress();
    }

    public void free(int userAddress) {
        Block block = Block.fromUserAddress(userAddress, this.memory);
        block.setFree();

        this.merge(block);
    }

    public void merge(Block block) {
        int sizeClass = block.getSizeClass();

        while (sizeClass < MAX_SIZE_CLASS) {
            Block buddy = this.getBuddy(block, sizeClass);

            if (buddy.isUsed() || buddy.getSizeClass() != sizeClass) {
                break;
            }

            buddy.removeFromList();

            if (block.getAddress() > buddy.getAddress()) {
                block = buddy;
            }

            sizeClass += 1;
        }

        block.setSizeClass(sizeClass);
        this.blockLists[sizeClass - 1].insertFront(block);
    }

    public int[] getFreeBlocks() {
        int[] freeBlocks = new int[MAX_SIZE_CLASS];

        for (int i = 0; i < MAX_SIZE_CLASS; i++) {
            freeBlocks[i] = this.blockLists[i].length();
        }

        return freeBlocks;
    }

    public int getMaxSizeClass() {
        return MAX_SIZE_CLASS;
    }

    private Block split(Block block, int size) {
        int sizeClass = block.getSizeClass();

        while (sizeClass > MIN_SIZE_CLASS && Block.getActualSize(sizeClass - 1) >= size) {
            int newSizeClass = sizeClass - 1;
            Block[] buddies = this.splitToBuddies(block, newSizeClass);
            block = buddies[0];
            sizeClass = newSizeClass;
        }

        block.removeFromList();

        return block;
    }

    private Block[] splitToBuddies(Block block, int sizeClass) {
        block.removeFromList();
        Block[] buddies = new Block[2];

        for (int i = 0; i < 2; i++) {
            int address = block.getAddress() + (1 << sizeClass) * i;
            buddies[i] = new Block(address, this.memory);
            buddies[i].setFree();
            buddies[i].setSizeClass(sizeClass);
        }

        for (int i = 1; i >= 0; i--) {
            this.blockLists[sizeClass - 1].insertFront(buddies[i]);
        }

        return buddies;
    }

    private Block getBuddy(Block block, int sizeClass) {
        int virtualAddress = block.getAddress() - this.getMemoryOffset();
        int buddyVirtualAddress = virtualAddress ^ (1 << sizeClass);
        int buddyAddress = buddyVirtualAddress + this.getMemoryOffset();

        return new Block(buddyAddress, this.memory);
    }

    private int getMemoryOffset() {
        return Constant.HEAD_SENTINEL_SIZE * MAX_SIZE_CLASS;
    }
}
