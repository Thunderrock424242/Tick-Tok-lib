package com.thunder.ticktoklib.util;

import com.thunder.ticktoklib.Core.ModConstants;
import com.thunder.ticktoklib.TickTokConfig;
import com.thunder.ticktoklib.TickTokHelper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * Utility helpers that bridge Java time primitives with tick-based time in Minecraft.
 */
public final class TickTokTimeUtils {
    private TickTokTimeUtils() {
    }

    /** Convert ticks to a {@link Duration}. */
    public static Duration ticksToDuration(long ticks) {
        long millis = TickTokHelper.toMillisecondsLong(ticks);
        debug("ticksToDuration", "ticks=" + ticks + " -> " + millis + "ms");
        return Duration.ofMillis(millis);
    }

    /** Break down ticks into hour/minute/second/millisecond components. */
    public static TickTokDurationBreakdown breakdownTicks(long ticks) {
        TickTokDurationBreakdown breakdown = TickTokDurationBreakdown.fromTicks(ticks);
        debug("breakdownTicks", "ticks=" + ticks + " -> " + breakdown.totalMilliseconds() + "ms");
        return breakdown;
    }

    /** Convert ticks to a {@link Duration}, retaining the level for future time-scaling hooks. */
    public static Duration ticksToDuration(Level level, long ticks) {
        Objects.requireNonNull(level, "level");
        return ticksToDuration(ticks);
    }

    /** Convert a {@link Duration} to ticks (rounded). */
    public static long durationToTicks(Duration duration) {
        Objects.requireNonNull(duration, "duration");
        long millis = duration.toMillis();
        long ticks = TickTokHelper.toTicksMilliseconds(millis);
        debug("durationToTicks", "duration=" + millis + "ms -> " + ticks + "t");
        return ticks;
    }

    /** Convert a {@link TimeUnit}-based duration to ticks. */
    public static long timeUnitToTicks(long time, TimeUnit unit) {
        Objects.requireNonNull(unit, "unit");
        long millis = unit.toMillis(time);
        long ticks = TickTokHelper.toTicksMilliseconds(millis);
        debug("timeUnitToTicks", "time=" + time + " " + unit + " -> " + ticks + "t");
        return ticks;
    }

    /** Convert ticks to the requested {@link TimeUnit}. */
    public static long ticksToTimeUnit(long ticks, TimeUnit unit) {
        Objects.requireNonNull(unit, "unit");
        long millis = TickTokHelper.toMillisecondsLong(ticks);
        long converted = unit.convert(millis, TimeUnit.MILLISECONDS);
        debug("ticksToTimeUnit", "ticks=" + ticks + " -> " + converted + " " + unit);
        return converted;
    }

    /**
     * Schedule a task to run after the given number of ticks using the common pool. This is best-effort and not a
     * replacement for server-thread scheduling; callers should hop back to the main thread as needed.
     */
    public static CompletableFuture<Void> sleepTicksAsync(long ticks) {
        Duration delay = ticksToDuration(ticks);
        debug("sleepTicksAsync", "ticks=" + ticks + ", delay=" + delay);
        return CompletableFuture.runAsync(() -> {
        }, CompletableFuture.delayedExecutor(delay.toMillis(), TimeUnit.MILLISECONDS));
    }

    /**
     * Schedule a task to run after the given ticks relative to a server instance. The task itself is run on the main
     * thread using {@link MinecraftServer#execute(Runnable)}.
     */
    public static CompletableFuture<Void> sleepTicksOnServer(MinecraftServer server, long ticks, Runnable task) {
        Objects.requireNonNull(server, "server");
        Objects.requireNonNull(task, "task");
        return sleepTicksAsync(ticks).thenRun(() -> server.execute(task));
    }

    private static void debug(String method, String message) {
        if (TickTokConfig.isDebugLoggingEnabled() && ModConstants.LOGGER.isDebugEnabled()) {
            ModConstants.LOGGER.debug("TickTokTimeUtils.{} -> {}", method, message);
        }
    }
}
