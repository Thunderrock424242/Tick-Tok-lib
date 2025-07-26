package com.thunder.ticktoklib.util;

public class TickTokCountdown {
    private final long duration;
    private final long startTick;

    public TickTokCountdown(long duration, long startTick) {
        this.duration = duration;
        this.startTick = startTick;
    }

    public boolean isFinished(long currentTick) {
        return (currentTick - startTick) >= duration;
    }

    public long remaining(long currentTick) {
        return Math.max(0, duration - (currentTick - startTick));
    }
}