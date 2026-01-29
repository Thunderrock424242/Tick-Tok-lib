package com.thunder.ticktoklib.util;

import com.thunder.ticktoklib.Core.ModConstants;
import com.thunder.ticktoklib.TickTokConfig;
import com.thunder.ticktoklib.TickTokHelper;
import net.minecraft.server.MinecraftServer;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * Utility for scheduling tasks at fractions of a Minecraft tick using real time delays.
 */
public final class SubTickScheduler {
    private SubTickScheduler() {
    }

    public static CompletableFuture<Void> scheduleHalfTick(Runnable task) {
        return schedule(0.5, task);
    }

    public static CompletableFuture<Void> scheduleQuarterTick(Runnable task) {
        return schedule(0.25, task);
    }

    public static CompletableFuture<Void> schedule(double ticks, Runnable task) {
        Objects.requireNonNull(task, "task");
        if (ticks < 0) {
            throw new IllegalArgumentException("ticks must be >= 0");
        }
        long delayMillis = Math.round(ticks * TickTokHelper.MS_PER_TICK);
        debug("schedule", ticks, delayMillis);
        return CompletableFuture.runAsync(
                task,
                CompletableFuture.delayedExecutor(delayMillis, TimeUnit.MILLISECONDS)
        );
    }

    public static CompletableFuture<Void> scheduleOnServer(double ticks, MinecraftServer server, Runnable task) {
        Objects.requireNonNull(server, "server");
        Objects.requireNonNull(task, "task");
        return schedule(ticks, () -> server.execute(task));
    }

    private static void debug(String method, double ticks, long delayMillis) {
        if (TickTokConfig.isDebugLoggingEnabled() && ModConstants.LOGGER.isDebugEnabled()) {
            ModConstants.LOGGER.debug("SubTickScheduler.{} -> {} ticks ({} ms)", method, ticks, delayMillis);
        }
    }
}
