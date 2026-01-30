package com.thunder.ticktoklib.util;

import com.thunder.ticktoklib.TickTokFormatter;

/**
 * Utility for tracking a reusable cooldown window in ticks.
 */
public class TickTokCooldown {
    private final long durationTicks;
    private long startTick;

    public TickTokCooldown(long durationTicks, long startTick) {
        this.durationTicks = durationTicks;
        this.startTick = startTick;
    }

    public long durationTicks() {
        return durationTicks;
    }

    public void reset(long currentTick) {
        this.startTick = currentTick;
    }

    public boolean isReady(long currentTick) {
        return elapsedTicks(currentTick) >= durationTicks;
    }

    public long elapsedTicks(long currentTick) {
        return Math.max(0, currentTick - startTick);
    }

    public long remainingTicks(long currentTick) {
        return Math.max(0, durationTicks - elapsedTicks(currentTick));
    }

    public double remainingFraction(long currentTick) {
        if (durationTicks <= 0) {
            return 0;
        }
        return remainingTicks(currentTick) / (double) durationTicks;
    }

    public String formatRemainingShort(long currentTick) {
        return TickTokFormatter.formatDurationShort(remainingTicks(currentTick));
    }

    public String formatRemainingLong(long currentTick) {
        return TickTokFormatter.formatDurationLong(remainingTicks(currentTick));
    }
}
