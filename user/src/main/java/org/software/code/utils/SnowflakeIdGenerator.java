package org.software.code.utils;

public class SnowflakeIdGenerator {

    // 起始的时间戳，2021-07-02 00:00:00 UTC
    private static final long START_TIMESTAMP = 1625222400000L;

    // 每部分占用的位数
    private static final long SEQUENCE_BITS = 12L; // 序列号占用的位数
    private static final long WORKER_ID_BITS = 5L; // 机器标识占用的位数
    private static final long MAX_WORKER_ID = -1L ^ (-1L << WORKER_ID_BITS); // 最大机器ID

    // 每一部分的偏移量
    private static final long WORKER_ID_SHIFT = SEQUENCE_BITS;
    private static final long TIMESTAMP_LEFT_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;

    // 雪花算法核心参数
    private final long workerId;
    private long sequence = 0L;
    private long lastTimestamp = -1L;

    public SnowflakeIdGenerator(long workerId) {
        if (workerId > MAX_WORKER_ID || workerId < 0) {
            throw new IllegalArgumentException(String.format("Worker ID must be between 0 and %d", MAX_WORKER_ID));
        }
        this.workerId = workerId;
    }

    public synchronized long nextId() {
        long timestamp = System.currentTimeMillis();

        // 如果当前时间小于上一次生成ID的时间戳，说明系统时钟回退过，抛出异常
        if (timestamp < lastTimestamp) {
            throw new RuntimeException("Clock moved backwards. Refusing to generate ID");
        }

        // 如果是同一时间生成的，则进行毫秒内序列
        if (timestamp == lastTimestamp) {
            sequence = (sequence + 1) & ((1 << SEQUENCE_BITS) - 1);
            // 毫秒内序列溢出
            if (sequence == 0) {
                // 阻塞到下一个毫秒，获得新的时间戳
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0L; // 时间戳改变，毫秒内序列重置
        }

        // 上次生成ID的时间截
        lastTimestamp = timestamp;

        // 移位并通过或运算拼到一起组成64位的ID
        return ((timestamp - START_TIMESTAMP) << TIMESTAMP_LEFT_SHIFT)
                | (workerId << WORKER_ID_SHIFT)
                | sequence;
    }

    private long tilNextMillis(long lastTimestamp) {
        long timestamp = System.currentTimeMillis();
        while (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }

    public static void main(String[] args) {
        // 示例：创建雪花算法生成器，传入 workerId
        SnowflakeIdGenerator idGenerator = new SnowflakeIdGenerator(1);
        // 生成 ID
        long id = idGenerator.nextId();
        System.out.println("Generated ID: " + id);
    }
}
