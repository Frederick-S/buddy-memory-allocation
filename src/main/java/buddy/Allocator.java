package buddy;

public class Allocator {
    private final Memory memory;
    private final BlockList[] blockLists;

    private static final int MIN_SIZE_CLASS = 4;
    private static final int MAX_SIZE_CLASS = 16;

    public Allocator() {
        int allHeadSentinelSize = Constant.HEAD_SENTINEL_SIZE * MAX_SIZE_CLASS;
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
            int sizeClass = i + 1;

            if (blockList.isEmpty() || Block.getActualSize(sizeClass) < size) {
                continue;
            }

            block = blockList.getFirst();
            block.removeFromList();
            block = this.split(block, size);
            block.setUsed();

            break;
        }

        if (block == null) {
            throw new RuntimeException("memory is full");
        }

        return block.getUserAddress();
    }

    private Block split(Block block, int size) {
        int oldSizeClass = block.getSizeClass();
        int sizeClass = block.getSizeClass();

        while (sizeClass > MIN_SIZE_CLASS && Block.getActualSize(sizeClass - 1) >= size) {
            int newSizeClass = sizeClass - 1;
            Block[] buddies = this.splitToBuddies(block, newSizeClass);
            block = buddies[0];
            sizeClass = newSizeClass;
        }

        // block is splitted
        if (sizeClass != oldSizeClass) {
            block.removeFromList();
        }

        return block;
    }

    private Block[] splitToBuddies(Block block, int sizeClass) {
        Block[] buddies = new Block[2];

        for (int i = 0; i < 2; i++) {
            int address = block.getAddress() + (1 << sizeClass) * i;
            buddies[i] = new Block(address, this.memory);
            buddies[i].setFree();
            buddies[i].setSizeClass(sizeClass);
            this.blockLists[sizeClass - 1].insertFront(buddies[i]);
        }

        return buddies;
    }
}
