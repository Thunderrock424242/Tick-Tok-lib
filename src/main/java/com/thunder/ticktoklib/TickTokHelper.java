package com.thunder.ticktoklib;

/**
 * Core utility class for converting between Minecraft ticks and real-world time.
 */
public class TickTokHelper {

    public static final int TICKS_PER_SECOND   = 20;
    public static final int TICKS_PER_MINUTE   = TICKS_PER_SECOND * 60;
    public static final int TICKS_PER_HOUR     = TICKS_PER_MINUTE * 60;
    public static final int MS_PER_TICK        = 1000 / TICKS_PER_SECOND; // 50 ms per tick

    // Core conversions
    public static int toTicksSeconds(int seconds) {
        return seconds * TICKS_PER_SECOND;
    }

    public static int toTicksMinutes(int minutes) {
        return minutes * TICKS_PER_MINUTE;
    }

    public static int toTicksHours(int hours) {
        return hours * TICKS_PER_HOUR;
    }

    /**
     * Converts milliseconds to ticks, rounding to nearest tick.
     */
    public static int toTicksMilliseconds(int milliseconds) {
        return Math.round(milliseconds * (TICKS_PER_SECOND / 1000f));
    }

    public static float toSeconds(int ticks) {
        return ticks / (float) TICKS_PER_SECOND;
    }

    public static float toMinutes(int ticks) {
        return ticks / (float) TICKS_PER_MINUTE;
    }

    public static float toHours(int ticks) {
        return ticks / (float) TICKS_PER_HOUR;
    }

    /**
     * Converts ticks back into total milliseconds.
     */
    public static int toMilliseconds(int ticks) {
        return ticks * MS_PER_TICK;
    }

    /**
     * Converts the given time to total ticks.
     *
     * @param hours       number of hours
     * @param minutes     number of minutes
     * @param seconds     number of seconds
     * @param milliseconds number of milliseconds
     * @return total ticks
     */
    public static int duration(int hours, int minutes, int seconds, int milliseconds) {
        int base =      hours   * TICKS_PER_HOUR
                + minutes * TICKS_PER_MINUTE
                + seconds * TICKS_PER_SECOND;
        return base + toTicksMilliseconds(milliseconds);
    }

    /**
     * Creates a fluent time builder for constructing durations.
     *
     * @return TickTokTimeBuilder
     */
    public static TickTokTimeBuilder time() {
        return new TickTokTimeBuilder();
    }
}