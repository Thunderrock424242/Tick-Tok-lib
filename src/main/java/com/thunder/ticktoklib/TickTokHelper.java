package com.thunder.ticktoklib;

/**
 * Core utility class for converting between Minecraft ticks and real-world time.
 */
public class TickTokHelper {

    public static final int TICKS_PER_SECOND = 20;
    public static final int TICKS_PER_MINUTE = TICKS_PER_SECOND * 60;
    public static final int TICKS_PER_HOUR = TICKS_PER_MINUTE * 60;

    // Core conversions
    public static int toTicks(int seconds) {
        return seconds * TICKS_PER_SECOND;
    }

    public static int toTicksMinutes(int minutes) {
        return minutes * TICKS_PER_MINUTE;
    }

    public static int toTicksHours(int hours) {
        return hours * TICKS_PER_HOUR;
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

    public static int duration(int hours, int minutes, int seconds) {
        return (hours * TICKS_PER_HOUR) + (minutes * TICKS_PER_MINUTE) + (seconds * TICKS_PER_SECOND);
    }

    public static String formatTicksToMinSec(int ticks) {
        int totalSeconds = ticks / TICKS_PER_SECOND;
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%d:%02d", minutes, seconds);
    }

    public static String formatTicksToHMS(int ticks) {
        int totalSeconds = ticks / TICKS_PER_SECOND;
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;
        return String.format("%d:%02d:%02d", hours, minutes, seconds);
    }
}