package com.thunder.ticktoklib.api;


import com.thunder.ticktoklib.Core.ModConstants;
import com.thunder.ticktoklib.TickTokConfig;
import com.thunder.ticktoklib.TickTokFormatter;
import com.thunder.ticktoklib.TickTokHelper;
import com.thunder.ticktoklib.TickTokTimeBuilder;

/**
 * Public API façade for TickTokLib.
 */
public class TickTokAPI {
    private static void logDelegation(String methodName, String target, String details) {
        if (TickTokConfig.isDebugLoggingEnabled() && ModConstants.LOGGER.isDebugEnabled()) {
            ModConstants.LOGGER.debug("TickTokAPI.{} -> {} ({})", methodName, target, details);
        }
    }

    // ── Conversion from real‐world units to ticks ─────────────────────

    /** Convert seconds → ticks. */
    public static int toTicksFromSeconds(int seconds) {
        logDelegation("toTicksFromSeconds", "TickTokHelper.toTicksSeconds", "seconds=" + seconds);
        return TickTokHelper.toTicksSeconds(seconds);
    }

    /** Convert minutes → ticks. */
    public static int toTicksFromMinutes(int minutes) {
        logDelegation("toTicksFromMinutes", "TickTokHelper.toTicksMinutes", "minutes=" + minutes);
        return TickTokHelper.toTicksMinutes(minutes);
    }

    /** Convert hours → ticks. */
    public static int toTicksFromHours(int hours) {
        logDelegation("toTicksFromHours", "TickTokHelper.toTicksHours", "hours=" + hours);
        return TickTokHelper.toTicksHours(hours);
    }

    /** Convert milliseconds → ticks (rounded). */
    public static int toTicksFromMilliseconds(int milliseconds) {
        logDelegation("toTicksFromMilliseconds", "TickTokHelper.toTicksMilliseconds", "milliseconds=" + milliseconds);
        return TickTokHelper.toTicksMilliseconds(milliseconds);
    }

    /** Backwards‐compatible alias (assumes seconds). */
    public static int toTicks(int seconds) {
        logDelegation("toTicks", "TickTokAPI.toTicksFromSeconds", "seconds=" + seconds);
        return toTicksFromSeconds(seconds);
    }

    // ── Duration builders ────────────────────────────────────────────

    /** Build a complex duration (h, m, s, ms) fluently. */
    public static TickTokTimeBuilder timeBuilder() {
        logDelegation("timeBuilder", "TickTokHelper.time", "constructing TickTokTimeBuilder");
        return TickTokHelper.time();
    }

    /** Full duration (h, m, s, ms) → ticks. */
    public static int duration(int hours, int minutes, int seconds, int milliseconds) {
        logDelegation("duration", "TickTokHelper.duration", String.format("h=%d, m=%d, s=%d, ms=%d", hours, minutes, seconds, milliseconds));
        return TickTokHelper.duration(hours, minutes, seconds, milliseconds);
    }

    // ── Conversion from ticks back to real‐world units ───────────────

    /** Convert ticks → seconds (may be fractional). */
    public static float toSeconds(int ticks) {
        logDelegation("toSeconds", "TickTokHelper.toSeconds", "ticks=" + ticks);
        return TickTokHelper.toSeconds(ticks);
    }

    /** Convert ticks → minutes. */
    public static float toMinutes(int ticks) {
        logDelegation("toMinutes", "TickTokHelper.toMinutes", "ticks=" + ticks);
        return TickTokHelper.toMinutes(ticks);
    }

    /** Convert ticks → hours. */
    public static float toHours(int ticks) {
        logDelegation("toHours", "TickTokHelper.toHours", "ticks=" + ticks);
        return TickTokHelper.toHours(ticks);
    }

    /** Convert ticks → total milliseconds. */
    public static int toMilliseconds(int ticks) {
        logDelegation("toMilliseconds", "TickTokHelper.toMilliseconds", "ticks=" + ticks);
        return TickTokHelper.toMilliseconds(ticks);
    }

    // ── Formatting utilities ─────────────────────────────────────────

    /** Format ticks as "HH:mm". */
    public static String formatHHMM(long ticks) {
        logDelegation("formatHHMM", "TickTokFormatter.formatHHMM", "ticks=" + ticks);
        return TickTokFormatter.formatHHMM(ticks);
    }

    /** Format ticks as "HH:mm:ss". */
    public static String formatHHMMSS(long ticks) {
        logDelegation("formatHHMMSS", "TickTokFormatter.formatHHMMSS", "ticks=" + ticks);
        return TickTokFormatter.formatHHMMSS(ticks);
    }

    /** Format ticks as "HH:mm:ss.SSS". */
    public static String formatHMSms(long ticks) {
        logDelegation("formatHMSms", "TickTokFormatter.formatHMSms", "ticks=" + ticks);
        return TickTokFormatter.formatHMSms(ticks);
    }
}
