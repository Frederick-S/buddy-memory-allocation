package buddy;

public class Constant {
    public static final int BOOL_SIZE = 1;
    public static final int INT32_SIZE = 4;
    public static final int FLAG_SIZE = BOOL_SIZE;
    public static final int SIZE_CLASS_SIZE = INT32_SIZE;
    public static final int PREV_SIZE = INT32_SIZE;
    public static final int NEXT_SIZE = INT32_SIZE;
    public static final int OFFSET_SIZE_CLASS = FLAG_SIZE;
    public static final int OFFSET_PREV = OFFSET_SIZE_CLASS + SIZE_CLASS_SIZE;
    public static final int OFFSET_NEXT = OFFSET_PREV + PREV_SIZE;
    public static final int OFFSET_ACTUAL_MEMORY = OFFSET_NEXT + NEXT_SIZE;
    public static final int MIN_SIZE_CLASS = 8;
    public static final int SIZE_SENTINEL = 1 << MIN_SIZE_CLASS;
    public static final int META_DATA_SIZE = FLAG_SIZE + SIZE_CLASS_SIZE + PREV_SIZE + NEXT_SIZE;
}
