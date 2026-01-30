package com.thunder.ticktoklib.api;


import com.thunder.ticktoklib.Core.ModConstants;
import com.thunder.ticktoklib.TickTokConfig;
import com.thunder.ticktoklib.TickTokFormatter;
import com.thunder.ticktoklib.TickTokHelper;
import com.thunder.ticktoklib.TickTokTimeBuilder;
import com.thunder.ticktoklib.util.TickTokCountdown;
import com.thunder.ticktoklib.util.TickTokPhaseScheduler;
import com.thunder.ticktoklib.util.TickTokTimerScheduler;
import com.thunder.ticktoklib.util.TickTokTimeUtils;

import java.time.ZoneId;
import java.time.Duration;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Public API façade for TickTokLib.
 */
public class TickTokAPI {
    private static final TickTokPhaseScheduler PHASE_SCHEDULER = new TickTokPhaseScheduler();
    private static final TickTokTimerScheduler TIMER_SCHEDULER = TickTokTimerScheduler.getDefault();

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

    public static long toTicksFromSeconds(long seconds) {
        logDelegation("toTicksFromSeconds(long)", "TickTokHelper.toTicksSeconds(long)", "seconds=" + seconds);
        return TickTokHelper.toTicksSeconds(seconds);
    }

    public static long toTicksFromSeconds(double seconds) {
        logDelegation("toTicksFromSeconds(double)", "TickTokHelper.toTicksSeconds(double)", "seconds=" + seconds);
        return TickTokHelper.toTicksSeconds(seconds);
    }

    /** Convert minutes → ticks. */
    public static int toTicksFromMinutes(int minutes) {
        logDelegation("toTicksFromMinutes", "TickTokHelper.toTicksMinutes", "minutes=" + minutes);
        return TickTokHelper.toTicksMinutes(minutes);
    }

    public static long toTicksFromMinutes(long minutes) {
        logDelegation("toTicksFromMinutes(long)", "TickTokHelper.toTicksMinutes(long)", "minutes=" + minutes);
        return TickTokHelper.toTicksMinutes(minutes);
    }

    public static long toTicksFromMinutes(double minutes) {
        logDelegation("toTicksFromMinutes(double)", "TickTokHelper.toTicksMinutes(double)", "minutes=" + minutes);
        return TickTokHelper.toTicksMinutes(minutes);
    }

    /** Convert hours → ticks. */
    public static int toTicksFromHours(int hours) {
        logDelegation("toTicksFromHours", "TickTokHelper.toTicksHours", "hours=" + hours);
        return TickTokHelper.toTicksHours(hours);
    }

    public static long toTicksFromHours(long hours) {
        logDelegation("toTicksFromHours(long)", "TickTokHelper.toTicksHours(long)", "hours=" + hours);
        return TickTokHelper.toTicksHours(hours);
    }

    public static long toTicksFromHours(double hours) {
        logDelegation("toTicksFromHours(double)", "TickTokHelper.toTicksHours(double)", "hours=" + hours);
        return TickTokHelper.toTicksHours(hours);
    }

    /** Convert milliseconds → ticks (rounded). */
    public static int toTicksFromMilliseconds(int milliseconds) {
        logDelegation("toTicksFromMilliseconds", "TickTokHelper.toTicksMilliseconds", "milliseconds=" + milliseconds);
        return TickTokHelper.toTicksMilliseconds(milliseconds);
    }

    public static long toTicksFromMilliseconds(long milliseconds) {
        logDelegation("toTicksFromMilliseconds(long)", "TickTokHelper.toTicksMilliseconds(long)", "milliseconds=" + milliseconds);
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

    public static long durationLong(long hours, long minutes, long seconds, long milliseconds) {
        logDelegation("durationLong", "TickTokHelper.durationLong", String.format("h=%d, m=%d, s=%d, ms=%d", hours, minutes, seconds, milliseconds));
        return TickTokHelper.durationLong(hours, minutes, seconds, milliseconds);
    }

    // ── Duration <-> ticks helpers using java.time ──────────────────
    public static java.time.Duration ticksToDuration(long ticks) {
        logDelegation("ticksToDuration", "TickTokTimeUtils.ticksToDuration", "ticks=" + ticks);
        return TickTokTimeUtils.ticksToDuration(ticks);
    }

    public static java.time.Duration ticksToDuration(net.minecraft.world.level.Level level, long ticks) {
        logDelegation("ticksToDuration(level)", "TickTokTimeUtils.ticksToDuration(Level, long)", "ticks=" + ticks + ", level=" + level.dimension().location());
        return TickTokTimeUtils.ticksToDuration(level, ticks);
    }

    public static long durationToTicks(java.time.Duration duration) {
        logDelegation("durationToTicks", "TickTokTimeUtils.durationToTicks", "duration=" + duration);
        return TickTokTimeUtils.durationToTicks(duration);
    }

    public static long timeUnitToTicks(long time, java.util.concurrent.TimeUnit unit) {
        logDelegation("timeUnitToTicks", "TickTokTimeUtils.timeUnitToTicks", time + " " + unit);
        return TickTokTimeUtils.timeUnitToTicks(time, unit);
    }

    public static long ticksToTimeUnit(long ticks, java.util.concurrent.TimeUnit unit) {
        logDelegation("ticksToTimeUnit", "TickTokTimeUtils.ticksToTimeUnit", "ticks=" + ticks + ", unit=" + unit);
        return TickTokTimeUtils.ticksToTimeUnit(ticks, unit);
    }

    public static java.util.concurrent.CompletableFuture<Void> sleepTicksAsync(long ticks) {
        logDelegation("sleepTicksAsync", "TickTokTimeUtils.sleepTicksAsync", "ticks=" + ticks);
        return TickTokTimeUtils.sleepTicksAsync(ticks);
    }

    public static java.util.concurrent.CompletableFuture<Void> sleepTicksOnServer(net.minecraft.server.MinecraftServer server, long ticks, Runnable task) {
        logDelegation("sleepTicksOnServer", "TickTokTimeUtils.sleepTicksOnServer", "ticks=" + ticks + ", server=" + server);
        return TickTokTimeUtils.sleepTicksOnServer(server, ticks, task);
    }

    // ── Timer helpers ───────────────────────────────────────────────
    public static TickTokTimerScheduler.TimerHandle scheduleEveryTicks(long intervalTicks, Runnable task) {
        logDelegation("scheduleEveryTicks(server)", "TickTokTimerScheduler.scheduleEvery", "intervalTicks=" + intervalTicks);
        return TIMER_SCHEDULER.scheduleEvery(intervalTicks, task);
    }

    public static TickTokTimerScheduler.TimerHandle scheduleEveryTicks(net.minecraft.resources.ResourceKey<net.minecraft.world.level.Level> level,
                                                                       long intervalTicks,
                                                                       Runnable task) {
        logDelegation("scheduleEveryTicks(level)", "TickTokTimerScheduler.scheduleEvery", "intervalTicks=" + intervalTicks + ", level=" + level.location());
        return TIMER_SCHEDULER.scheduleEvery(level, intervalTicks, task);
    }

    public static TickTokTimerScheduler.TimerHandle scheduleEvery(Duration duration, Runnable task) {
        long intervalTicks = TickTokTimeUtils.durationToTicks(duration);
        logDelegation("scheduleEvery(duration)", "TickTokTimerScheduler.scheduleEvery", "duration=" + duration + ", intervalTicks=" + intervalTicks);
        return TIMER_SCHEDULER.scheduleEvery(intervalTicks, task);
    }

    public static TickTokTimerScheduler.TimerHandle scheduleEvery(net.minecraft.resources.ResourceKey<net.minecraft.world.level.Level> level,
                                                                  Duration duration,
                                                                  Runnable task) {
        long intervalTicks = TickTokTimeUtils.durationToTicks(duration);
        logDelegation("scheduleEvery(level, duration)", "TickTokTimerScheduler.scheduleEvery", "duration=" + duration + ", intervalTicks=" + intervalTicks + ", level=" + level.location());
        return TIMER_SCHEDULER.scheduleEvery(level, intervalTicks, task);
    }

    public static TickTokTimerScheduler.TimerHandle scheduleEvery(long time, TimeUnit unit, Runnable task) {
        long intervalTicks = TickTokTimeUtils.timeUnitToTicks(time, unit);
        logDelegation("scheduleEvery(timeUnit)", "TickTokTimerScheduler.scheduleEvery", "time=" + time + " " + unit + ", intervalTicks=" + intervalTicks);
        return TIMER_SCHEDULER.scheduleEvery(intervalTicks, task);
    }

    public static TickTokTimerScheduler.TimerHandle scheduleEvery(net.minecraft.resources.ResourceKey<net.minecraft.world.level.Level> level,
                                                                  long time,
                                                                  TimeUnit unit,
                                                                  Runnable task) {
        long intervalTicks = TickTokTimeUtils.timeUnitToTicks(time, unit);
        logDelegation("scheduleEvery(level, timeUnit)", "TickTokTimerScheduler.scheduleEvery", "time=" + time + " " + unit + ", intervalTicks=" + intervalTicks + ", level=" + level.location());
        return TIMER_SCHEDULER.scheduleEvery(level, intervalTicks, task);
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

    public static long toMillisecondsLong(long ticks) {
        logDelegation("toMillisecondsLong", "TickTokHelper.toMillisecondsLong", "ticks=" + ticks);
        return TickTokHelper.toMillisecondsLong(ticks);
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

    public static String formatClock(long ticks, boolean includeSeconds, boolean includeMillis, boolean twentyFourHour, Locale locale) {
        logDelegation("formatClock", "TickTokFormatter.formatClock", "ticks=" + ticks);
        return TickTokFormatter.formatClock(ticks, includeSeconds, includeMillis, twentyFourHour, locale);
    }

    public static String formatLocalized(long ticks, String pattern, Locale locale, ZoneId zoneId) {
        logDelegation("formatLocalized", "TickTokFormatter.formatLocalized", "ticks=" + ticks + ", pattern=" + pattern);
        return TickTokFormatter.formatLocalized(ticks, pattern, locale, zoneId);
    }

    // ── Moon phase helpers ───────────────────────────────────────────
    public static int moonPhaseIndex(long dayTime) {
        logDelegation("moonPhaseIndex", "TickTokHelper.moonPhaseIndex", "dayTime=" + dayTime);
        return TickTokHelper.moonPhaseIndex(dayTime);
    }

    public static int moonPhaseIndex(net.minecraft.world.level.Level level) {
        logDelegation("moonPhaseIndex(level)", "TickTokHelper.moonPhaseIndex", "level=" + level.dimension().location());
        return TickTokHelper.moonPhaseIndex(level.getDayTime());
    }

    public static TickTokMoonPhase currentMoonPhase(long dayTime) {
        logDelegation("currentMoonPhase", "TickTokHelper.resolveMoonPhase", "dayTime=" + dayTime);
        return TickTokHelper.resolveMoonPhase(dayTime);
    }

    public static TickTokMoonPhase currentMoonPhase(net.minecraft.world.level.Level level) {
        logDelegation("currentMoonPhase(level)", "TickTokHelper.resolveMoonPhase", "level=" + level.dimension().location());
        return TickTokHelper.resolveMoonPhase(level.getDayTime());
    }

    // ── Phase helpers ────────────────────────────────────────────────
    public static TickTokPhase currentPhase(long dayTime) {
        logDelegation("currentPhase", "TickTokHelper.resolvePhase", "dayTime=" + dayTime);
        return TickTokHelper.resolvePhase(dayTime);
    }

    public static long ticksUntilPhase(long dayTime, TickTokPhase phase) {
        logDelegation("ticksUntilPhase", "TickTokHelper.ticksUntilPhase", "dayTime=" + dayTime + ", target=" + phase);
        return TickTokHelper.ticksUntilPhase(dayTime, phase);
    }

    public static long ticksSincePhaseStart(long dayTime, TickTokPhase phase) {
        logDelegation("ticksSincePhaseStart", "TickTokHelper.ticksSincePhaseStart", "dayTime=" + dayTime + ", target=" + phase);
        return TickTokHelper.ticksSincePhaseStart(dayTime, phase);
    }

    public static void scheduleAtPhase(TickTokPhase phase, Runnable task) {
        logDelegation("scheduleAtPhase(any)", "TickTokPhaseScheduler.scheduleAtPhase", "phase=" + phase);
        PHASE_SCHEDULER.scheduleAtPhase(phase, task);
    }

    public static void scheduleAtPhase(net.minecraft.resources.ResourceKey<net.minecraft.world.level.Level> level, TickTokPhase phase, Runnable task) {
        logDelegation("scheduleAtPhase(level)", "TickTokPhaseScheduler.scheduleAtPhase", "phase=" + phase + ", level=" + level.location());
        PHASE_SCHEDULER.scheduleAtPhase(level, phase, task);
    }

    public static TickTokPhaseScheduler.PhaseBarrier phaseBarrier(net.minecraft.resources.ResourceKey<net.minecraft.world.level.Level> level, TickTokPhase phase) {
        logDelegation("phaseBarrier", "TickTokPhaseScheduler.barrier", "phase=" + phase + ", level=" + (level == null ? "any" : level.location()));
        return PHASE_SCHEDULER.barrier(level, phase);
    }

    // ── Countdown helpers ───────────────────────────────────────────
    public static TickTokCountdown countdown(long duration, long startTick) {
        logDelegation("countdown", "TickTokCountdown", "duration=" + duration + ", startTick=" + startTick);
        return new TickTokCountdown(duration, startTick, (Runnable) null);
    }

    public static TickTokCountdown countdown(long duration, long startTick, Runnable onComplete) {
        logDelegation("countdown(callback)", "TickTokCountdown", "duration=" + duration + ", startTick=" + startTick);
        return new TickTokCountdown(duration, startTick, onComplete);
    }

    public static TickTokCountdown countdown(long duration, long startTick, Consumer<TickTokCountdown> onComplete) {
        logDelegation("countdown(consumer)", "TickTokCountdown", "duration=" + duration + ", startTick=" + startTick);
        return new TickTokCountdown(duration, startTick, onComplete);
    }

    // ── Command-style formatting helpers ────────────────────────────
    public static net.minecraft.network.chat.Component buildPhaseReport(long dayTime) {
        TickTokPhase phase = currentPhase(dayTime);
        return net.minecraft.network.chat.Component.literal(String.format("Day time: %d (%s)", dayTime, phase));
    }

    public static net.minecraft.network.chat.Component buildConversionReport(long ticks) {
        String formatted = TickTokFormatter.formatHHMMSS(ticks);
        float seconds = TickTokHelper.toSeconds((int) ticks);
        return net.minecraft.network.chat.Component.literal(String.format("%d ticks -> %s (%.2f seconds)", ticks, formatted, seconds));
    }

    public static net.minecraft.network.chat.Component buildLagBehindReport(long ticksBehind) {
        logDelegation("buildLagBehindReport", "TickTokLagFormatter.buildLagBehindReport", "ticksBehind=" + ticksBehind);
        return com.thunder.ticktoklib.TickTokLagFormatter.buildLagBehindReport(ticksBehind);
    }
}
