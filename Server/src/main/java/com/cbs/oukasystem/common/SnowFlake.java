package com.cbs.oukasystem.common;

/**
 * @author wx
 */
public final class SnowFlake {
    private final long START_STMP = 1577808000000L; // 2020-01-01
    private final long SEQUENCE_BIT = 12; // 序列号占用的位数
    private final long MACHINE_BIT = 5; // 机器标识占用的位数
    private final long DATACENTER_BIT = 5; // 数据中心占用的位数
    private final long MAX_DATACENTER_NUM = ~(-1L << DATACENTER_BIT);
    private final long MAX_MACHINE_NUM = ~(-1L << MACHINE_BIT);
    private final long MAX_SEQUENCE = ~(-1L << SEQUENCE_BIT);
    private final long MACHINE_LEFT = SEQUENCE_BIT;
    private final long DATACENTER_LEFT = SEQUENCE_BIT + MACHINE_BIT;
    private final long TIMESTMP_LEFT = DATACENTER_LEFT + DATACENTER_BIT;
    private long datacenterId;
    private long workerId;
    private long sequence = 0L;
    private long lastStmp = -1L;

    public SnowFlake(long datacenterId, long machineId) {
        if (datacenterId > MAX_DATACENTER_NUM || datacenterId < 0) {
            throw new IllegalArgumentException("datacenterId can't be greater than MAX_DATACENTER_NUM or less than 0");
        }
        if (machineId > MAX_MACHINE_NUM || machineId < 0) {
            throw new IllegalArgumentException("machineId can't be greater than MAX_MACHINE_NUM or less than 0");
        }
        this.datacenterId = datacenterId;
        this.workerId = machineId;
    }

    public synchronized long nextId() {
        long currStmp = timeGen();
        if (currStmp < lastStmp) {
            throw new RuntimeException("Clock moved backwards.  Refusing to generate id");
        }

        if (currStmp == lastStmp) {
            sequence = (sequence + 1) & MAX_SEQUENCE;
            if (sequence == 0L) {
                currStmp = getNextMill();
            }
        } else {
            sequence = 0L;
        }

        lastStmp = currStmp;
        return (currStmp - START_STMP) << TIMESTMP_LEFT
                | datacenterId << DATACENTER_LEFT
                | workerId << MACHINE_LEFT
                | sequence;
    }

    private long getNextMill() {
        long mill = timeGen();
        while (mill <= lastStmp) {
            mill = timeGen();
        }
        return mill;
    }

    private long timeGen() {
        return System.currentTimeMillis();
    }

}