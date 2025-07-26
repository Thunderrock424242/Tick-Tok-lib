package com.thunder.ticktoklib.api;


import com.thunder.ticktoklib.TickTokFormatter;
import com.thunder.ticktoklib.TickTokHelper;
import com.thunder.ticktoklib.TickTokTimeBuilder;

/**
 * Public API façade for TickTokLib.
 */
public class TickTokAPI {

    // ── Conversion from real‐world units to ticks ─────────────────────

    /** Convert seconds → ticks. */
    public static int toTicksFromSeconds(int seconds) {
        return TickTokHelper.toTicksSeconds(seconds);
    }

    /** Convert minutes → ticks. */
    public static int toTicksFromMinutes(int minutes) {
        return TickTokHelper.toTicksMinutes(minutes);
    }

    /** Convert hours → ticks. */
    public static int toTicksFromHours(int hours) {
        return TickTokHelper.toTicksHours(hours);
    }

    /** Convert milliseconds → ticks (rounded). */
    public static int toTicksFromMilliseconds(int milliseconds) {
        return TickTokHelper.toTicksMilliseconds(milliseconds);
    }

    /** Backwards‐compatible alias (assumes seconds). */
    public static int toTicks(int seconds) {
        return toTicksFromSeconds(seconds);
    }

    // ── Duration builders ────────────────────────────────────────────

    /** Build a complex duration (h, m, s, ms) fluently. */
    public static TickTokTimeBuilder timeBuilder() {
        return TickTokHelper.time();
    }

    /** Full duration (h, m, s, ms) → ticks. */
    public static int duration(int hours, int minutes, int seconds, int milliseconds) {
        return TickTokHelper.duration(hours, minutes, seconds, milliseconds);
    }

    // ── Conversion from ticks back to real‐world units ───────────────

    /** Convert ticks → seconds (may be fractional). */
    public static float toSeconds(int ticks) {
        return TickTokHelper.toSeconds(ticks);
    }

    /** Convert ticks → minutes. */
    public static float toMinutes(int ticks) {
        return TickTokHelper.toMinutes(ticks);
    }

    /** Convert ticks → hours. */
    public static float toHours(int ticks) {
        return TickTokHelper.toHours(ticks);
    }

    /** Convert ticks → total milliseconds. */
    public static int toMilliseconds(int ticks) {
        return TickTokHelper.toMilliseconds(ticks);
    }

    // ── Formatting utilities ─────────────────────────────────────────

    /** Format ticks as "HH:mm". */
    public static String formatHHMM(long ticks) {
        return TickTokFormatter.formatHHMM(ticks);
    }

    /** Format ticks as "HH:mm:ss". */
    public static String formatHHMMSS(long ticks) {
        return TickTokFormatter.formatHHMMSS(ticks);
    }

    /** Format ticks as "HH:mm:ss.SSS". */
    public static String formatHMSms(long ticks) {
        return TickTokFormatter.formatHMSms(ticks);
    }
}