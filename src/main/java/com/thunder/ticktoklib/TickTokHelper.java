package com.thunder.ticktoklib;

import com.thunder.ticktoklib.Core.ModConstants;
import com.thunder.ticktoklib.api.TickTokPhase;
import com.thunder.ticktoklib.util.TickTokTimerScheduler;
import com.thunder.ticktoklib.util.TickTokTimeRange;

/**
 * Core utility class for converting between Minecraft ticks and real-world time.
 */
public class TickTokHelper {

    public static final int TICKS_PER_SECOND   = 20;
    public static final int TICKS_PER_MINUTE   = TICKS_PER_SECOND * 60;
    public static final int TICKS_PER_HOUR     = TICKS_PER_MINUTE * 60;
    public static final int MS_PER_TICK        = 1000 / TICKS_PER_SECOND; // 50 ms per tick

    private static void logConversion(String methodName, String input, Object result) {
        if (TickTokConfig.isConversionTracingEnabled() && ModConstants.LOGGER.isTraceEnabled()) {
            ModConstants.LOGGER.trace("TickTokHelper.{}({}) -> {}", methodName, input, result);
        }
    }

    // Core conversions
    public static int toTicksSeconds(int seconds) {
        return Math.toIntExact(toTicksSeconds((long) seconds));
    }

    public static long toTicksSeconds(long seconds) {
        long ticks = seconds * TICKS_PER_SECOND;
        logConversion("toTicksSeconds", "seconds=" + seconds, ticks);
        return ticks;
    }

    public static long toTicksSeconds(double seconds) {
        long ticks = Math.round(seconds * TICKS_PER_SECOND);
        logConversion("toTicksSeconds", "seconds=" + seconds, ticks);
        return ticks;
    }

    public static int toTicksMinutes(int minutes) {
        return Math.toIntExact(toTicksMinutes((long) minutes));
    }

    public static long toTicksMinutes(long minutes) {
        long ticks = minutes * TICKS_PER_MINUTE;
        logConversion("toTicksMinutes", "minutes=" + minutes, ticks);
        return ticks;
    }

    public static long toTicksMinutes(double minutes) {
        long ticks = Math.round(minutes * TICKS_PER_MINUTE);
        logConversion("toTicksMinutes", "minutes=" + minutes, ticks);
        return ticks;
    }

    public static int toTicksHours(int hours) {
        return Math.toIntExact(toTicksHours((long) hours));
    }

    public static long toTicksHours(long hours) {
        long ticks = hours * TICKS_PER_HOUR;
        logConversion("toTicksHours", "hours=" + hours, ticks);
        return ticks;
    }

    public static long toTicksHours(double hours) {
        long ticks = Math.round(hours * TICKS_PER_HOUR);
        logConversion("toTicksHours", "hours=" + hours, ticks);
        return ticks;
    }

    /**
     * Converts milliseconds to ticks, rounding to nearest tick.
     */
    public static int toTicksMilliseconds(int milliseconds) {
        return Math.toIntExact(toTicksMilliseconds((long) milliseconds));
    }

    public static long toTicksMilliseconds(long milliseconds) {
        long ticks = Math.round(milliseconds * (TICKS_PER_SECOND / 1000f));
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
        return Math.toIntExact(toMillisecondsLong(ticks));
    }

    public static long toMillisecondsLong(long ticks) {
        long milliseconds = ticks * MS_PER_TICK;
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
        long ticks = durationLong(hours, minutes, seconds, milliseconds);
        return Math.toIntExact(ticks);
    }

    public static long durationLong(long hours, long minutes, long seconds, long milliseconds) {
        long base =      hours   * TICKS_PER_HOUR
                + minutes * TICKS_PER_MINUTE
                + seconds * TICKS_PER_SECOND;
        long ticks = base + toTicksMilliseconds(milliseconds);
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
        if (TickTokConfig.isDebugLoggingEnabled() && ModConstants.LOGGER.isDebugEnabled()) {
            ModConstants.LOGGER.debug("TickTokHelper.time() -> new TickTokTimeBuilder");
        }
        return new TickTokTimeBuilder();
    }

    /**
     * Provides access to the default tick-based timer scheduler.
     */
    public static TickTokTimerScheduler timer() {
        if (TickTokConfig.isDebugLoggingEnabled() && ModConstants.LOGGER.isDebugEnabled()) {
            ModConstants.LOGGER.debug("TickTokHelper.timer() -> default TickTokTimerScheduler");
        }
        return TickTokTimerScheduler.getDefault();
    }

    /**
     * Normalizes an absolute world tick value into the current day cycle (0-23999).
     */
    public static long clampToDay(long dayTime) {
        long clamped = Math.floorMod(dayTime, TickTokTimeRange.FULL_DAY);
        logConversion("clampToDay", "dayTime=" + dayTime, clamped);
        return clamped;
    }

    public static TickTokPhase resolvePhase(long dayTime) {
        TickTokPhase phase = TickTokTimeRange.phaseFor(clampToDay(dayTime));
        logConversion("resolvePhase", "dayTime=" + dayTime, phase);
        return phase;
    }

    public static long ticksUntilPhase(long dayTime, TickTokPhase phase) {
        long clamped = clampToDay(dayTime);
        long ticks = TickTokTimeRange.ticksUntilPhase(clamped, phase);
        logConversion("ticksUntilPhase", "dayTime=" + dayTime + ", target=" + phase, ticks);
        return ticks;
    }

    public static long ticksSincePhaseStart(long dayTime, TickTokPhase phase) {
        long clamped = clampToDay(dayTime);
        long ticks = TickTokTimeRange.ticksSincePhaseStart(clamped, phase);
        logConversion("ticksSincePhaseStart", "dayTime=" + dayTime + ", target=" + phase, ticks);
        return ticks;
    }
}
