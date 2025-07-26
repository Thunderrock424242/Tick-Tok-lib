package com.thunder.ticktoklib.util;

import com.thunder.ticktoklib.TickTokHelper;
import net.minecraft.server.MinecraftServer;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Simple scheduler for executing tasks at sub-tick precision.
 * <p>
 * This scheduler uses a single daemon thread to schedule runnables
 * with millisecond delays corresponding to fractions of a Minecraft tick.
 * It is intended for lightweight tasks that do not interact directly with
 * the game world from the worker thread.
 * </p>
 */
public class SubTickScheduler {
    /**
     * Number of sub-ticks per full tick. Quarter tick precision by default.
     */
    private static final int SUBTICKS_PER_TICK = 4;

    /**
     * Milliseconds in one sub-tick (50ms / 4 = 12.5ms).
     */
    private static final long SUBTICK_MS = TickTokHelper.MS_PER_TICK / SUBTICKS_PER_TICK;

    private static final ScheduledExecutorService EXECUTOR = Executors.newSingleThreadScheduledExecutor(r -> {
        Thread t = new Thread(r, "subtick-scheduler");
        t.setDaemon(true);
        return t;
    });

    /**
     * Schedule a task after the specified delay in ticks. Fractions are allowed.
     *
     * @param tickDelay delay in ticks (may be fractional)
     * @param task      runnable to execute
     */
    public static void schedule(double tickDelay, Runnable task) {
        long delayMs = (long) Math.floor(tickDelay * TickTokHelper.MS_PER_TICK);
        EXECUTOR.schedule(task, delayMs, TimeUnit.MILLISECONDS);
    }

    /** Schedule a task to run after half a tick (~25ms). */
    public static void scheduleHalfTick(Runnable task) {
        EXECUTOR.schedule(task, TickTokHelper.MS_PER_TICK / 2, TimeUnit.MILLISECONDS);
    }

    /** Schedule a task to run after a quarter tick (~12.5ms). */
    public static void scheduleQuarterTick(Runnable task) {
        EXECUTOR.schedule(task, SUBTICK_MS, TimeUnit.MILLISECONDS);
    }

    /**
     * Schedule a task to run on the server thread after the specified delay.
     *
     * @param tickDelay delay in ticks (may be fractional)
     * @param server    MinecraftServer instance
     * @param task      runnable to execute on the server thread
     */
    public static void scheduleOnServer(double tickDelay, MinecraftServer server, Runnable task) {
        long delayMs = (long) Math.floor(tickDelay * TickTokHelper.MS_PER_TICK);
        EXECUTOR.schedule(() -> server.execute(task), delayMs, TimeUnit.MILLISECONDS);
    }

    /** Schedule a half tick task on the server thread. */
    public static void scheduleHalfTickOnServer(MinecraftServer server, Runnable task) {
        EXECUTOR.schedule(() -> server.execute(task), TickTokHelper.MS_PER_TICK / 2, TimeUnit.MILLISECONDS);
    }

    /** Schedule a quarter tick task on the server thread. */
    public static void scheduleQuarterTickOnServer(MinecraftServer server, Runnable task) {
        EXECUTOR.schedule(() -> server.execute(task), SUBTICK_MS, TimeUnit.MILLISECONDS);
    }

    /**
     * Shut down the scheduler. This should typically be called when the mod
     * or game is stopping.
     */
    public static void shutdown() {
        EXECUTOR.shutdownNow();
    }
}
