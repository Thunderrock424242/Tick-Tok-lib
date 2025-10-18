package com.thunder.ticktoklib;

import com.thunder.ticktoklib.Core.ModConstants;

/**
 * Core utility class for converting between Minecraft ticks and real-world time.
 */
public class TickTokHelper {

    public static final int TICKS_PER_SECOND   = 20;
    public static final int TICKS_PER_MINUTE   = TICKS_PER_SECOND * 60;
    public static final int TICKS_PER_HOUR     = TICKS_PER_MINUTE * 60;
    public static final int MS_PER_TICK        = 1000 / TICKS_PER_SECOND; // 50 ms per tick

    private static void logConversion(String methodName, String input, Object result) {
        if (ModConstants.LOGGER.isTraceEnabled()) {
            ModConstants.LOGGER.trace("TickTokHelper.{}({}) -> {}", methodName, input, result);
        }
    }

    // Core conversions
    public static int toTicksSeconds(int seconds) {
        int ticks = seconds * TICKS_PER_SECOND;
        logConversion("toTicksSeconds", "seconds=" + seconds, ticks);
        return ticks;
    }

    public static int toTicksMinutes(int minutes) {
        int ticks = minutes * TICKS_PER_MINUTE;
        logConversion("toTicksMinutes", "minutes=" + minutes, ticks);
        return ticks;
    }

    public static int toTicksHours(int hours) {
        int ticks = hours * TICKS_PER_HOUR;
        logConversion("toTicksHours", "hours=" + hours, ticks);
        return ticks;
    }

    /**
     * Converts milliseconds to ticks, rounding to nearest tick.
     */
    public static int toTicksMilliseconds(int milliseconds) {
        int ticks = Math.round(milliseconds * (TICKS_PER_SECOND / 1000f));
        logConversion("toTicksMilliseconds", "milliseconds=" + milliseconds, ticks);
        return ticks;
    }

    public static float toSeconds(int ticks) {
        float seconds = ticks / (float) TICKS_PER_SECOND;
        logConversion("toSeconds", "ticks=" + ticks, seconds);
        return seconds;
    }

    public static float toMinutes(int ticks) {
        float minutes = ticks / (float) TICKS_PER_MINUTE;
        logConversion("toMinutes", "ticks=" + ticks, minutes);
        return minutes;
    }

    public static float toHours(int ticks) {
        float hours = ticks / (float) TICKS_PER_HOUR;
        logConversion("toHours", "ticks=" + ticks, hours);
        return hours;
    }

    /**
     * Converts ticks back into total milliseconds.
     */
    public static int toMilliseconds(int ticks) {
        int milliseconds = ticks * MS_PER_TICK;
        logConversion("toMilliseconds", "ticks=" + ticks, milliseconds);
        return milliseconds;
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
        int ticks = base + toTicksMilliseconds(milliseconds);
        logConversion(
                "duration",
                String.format("h=%d, m=%d, s=%d, ms=%d", hours, minutes, seconds, milliseconds),
                ticks
        );
        return ticks;
    }

    /**
     * Creates a fluent time builder for constructing durations.
     *
     * @return TickTokTimeBuilder
     */
    public static TickTokTimeBuilder time() {
        if (ModConstants.LOGGER.isDebugEnabled()) {
            ModConstants.LOGGER.debug("TickTokHelper.time() -> new TickTokTimeBuilder");
        }
        return new TickTokTimeBuilder();
    }
}
