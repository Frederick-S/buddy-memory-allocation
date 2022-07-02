package buddy;

public class Constant {
    public static final int BOOL_SIZE = 1;
    public static final int INT32_SIZE = 32;
    public static final int FLAG_SIZE = BOOL_SIZE;
    public static final int SIZE_SIZE = INT32_SIZE;
    public static final int PREV_SIZE = INT32_SIZE;
    public static final int NEXT_SIZE = INT32_SIZE;
    public static final int OFFSET_SIZE = FLAG_SIZE;
    public static final int OFFSET_PREV = OFFSET_SIZE + SIZE_SIZE;
    public static final int OFFSET_NEXT = OFFSET_PREV + PREV_SIZE;
    public static final int OFFSET_ACTUAL_MEMORY = OFFSET_NEXT + NEXT_SIZE;
}
