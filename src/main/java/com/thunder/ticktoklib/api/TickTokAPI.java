package com.thunder.ticktoklib.api;


import com.thunder.ticktoklib.TickTokHelper;

/**
 * Public API for other mods to access tick-time conversions.
 */
public class TickTokAPI {
    public static int toTicks(float seconds) {
        return TickTokHelper.toTicks((int) seconds);
    }

    public static float toSeconds(int ticks) {
        return TickTokHelper.toSeconds(ticks);
    }

    public static int duration(int hours, int minutes, int seconds) {
        return TickTokHelper.duration(hours, minutes, seconds);
    }

    public static String format(int ticks) {
        return TickTokHelper.formatTicksToHMS(ticks);
    }
}