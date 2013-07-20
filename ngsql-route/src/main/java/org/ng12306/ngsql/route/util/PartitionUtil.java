package org.ng12306.ngsql.route.util;

public final class PartitionUtil {
	private static final int PARTITION_LENGTH = 1024;
	private static final long AND_VALUE = PARTITION_LENGTH - 1;
	private final int[] segment = new int[PARTITION_LENGTH];
	
    public PartitionUtil(int count, int length) {
    	return;
    }
	
    public int partition(long hash) {
        return segment[(int) (hash & AND_VALUE)];
    }

    public int partition(String key, int start, int end) {
        //return partition(StringUtil.hash(key, start, end));
    	return 0;
    }
}
