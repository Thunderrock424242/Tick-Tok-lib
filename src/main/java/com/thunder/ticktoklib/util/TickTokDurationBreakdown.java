package com.thunder.ticktoklib.util;

import com.thunder.ticktoklib.TickTokHelper;

/**
 * Represents a tick duration split into hour/minute/second/millisecond components.
 */
public record TickTokDurationBreakdown(
        long hours,
        long minutes,
        long seconds,
        long milliseconds,
        long totalMilliseconds
) {

    public static TickTokDurationBreakdown fromTicks(long ticks) {
        long totalMs = TickTokHelper.toMillisecondsLong(ticks);
        long hours = totalMs / 3_600_000;
        long minutes = (totalMs % 3_600_000) / 60_000;
        long seconds = (totalMs % 60_000) / 1000;
        long millis = totalMs % 1000;
        return new TickTokDurationBreakdown(hours, minutes, seconds, millis, totalMs);
    }
}
