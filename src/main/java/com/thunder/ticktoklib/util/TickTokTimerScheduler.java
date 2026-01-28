package com.thunder.ticktoklib.util;

import com.thunder.ticktoklib.Core.ModConstants;
import com.thunder.ticktoklib.TickTokConfig;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Scheduler for running repeating tasks on a tick interval.
 */
public class TickTokTimerScheduler {
    private static final TickTokTimerScheduler DEFAULT = new TickTokTimerScheduler(true);

    private final List<TimerTask> serverTasks = new CopyOnWriteArrayList<>();
    private final Map<ResourceKey<Level>, List<TimerTask>> levelTasks = new ConcurrentHashMap<>();
    private final Map<ResourceKey<Level>, Long> lastLevelTick = new ConcurrentHashMap<>();
    private final AtomicLong serverTickCounter = new AtomicLong();

    public TickTokTimerScheduler() {
        this(true);
    }

    private TickTokTimerScheduler(boolean registerEvents) {
        if (registerEvents) {
            NeoForge.EVENT_BUS.register(this);
        }
    }

    public static TickTokTimerScheduler getDefault() {
        return DEFAULT;
    }

    /**
     * Schedule a repeating task that runs every {@code intervalTicks} on the server tick.
     */
    public TimerHandle scheduleEvery(long intervalTicks, Runnable task) {
        return scheduleEveryInternal(null, intervalTicks, task);
    }

    /**
     * Schedule a repeating task that runs every {@code intervalTicks} on the specified level.
     */
    public TimerHandle scheduleEvery(ResourceKey<Level> levelKey, long intervalTicks, Runnable task) {
        Objects.requireNonNull(levelKey, "levelKey");
        return scheduleEveryInternal(levelKey, intervalTicks, task);
    }

    @SubscribeEvent
    public void onServerTick(ServerTickEvent.Post event) {
        long currentTick = serverTickCounter.incrementAndGet();
        tickTasks(serverTasks, currentTick, null);
    }

    @SubscribeEvent
    public void onLevelTick(LevelTickEvent.Post event) {
        ResourceKey<Level> levelKey = event.getLevel().dimension();
        long currentTick = event.getLevel().getGameTime();
        lastLevelTick.put(levelKey, currentTick);
        List<TimerTask> tasks = levelTasks.get(levelKey);
        if (tasks == null || tasks.isEmpty()) {
            return;
        }
        tickTasks(tasks, currentTick, levelKey);
    }

    private TimerHandle scheduleEveryInternal(ResourceKey<Level> levelKey, long intervalTicks, Runnable task) {
        Objects.requireNonNull(task, "task");
        if (intervalTicks <= 0) {
            throw new IllegalArgumentException("intervalTicks must be > 0");
        }

        long baseTick = levelKey == null
                ? serverTickCounter.get()
                : Optional.ofNullable(lastLevelTick.get(levelKey)).orElse(0L);
        long nextTick = baseTick + intervalTicks;

        TimerTask timerTask = new TimerTask(intervalTicks, nextTick, task);
        if (levelKey == null) {
            serverTasks.add(timerTask);
        } else {
            levelTasks
                    .computeIfAbsent(levelKey, __ -> new CopyOnWriteArrayList<>())
                    .add(timerTask);
        }

        if (TickTokConfig.isDebugLoggingEnabled() && ModConstants.LOGGER.isDebugEnabled()) {
            ModConstants.LOGGER.debug("TickTokTimerScheduler scheduled {} tick interval in {}", intervalTicks,
                    levelKey == null ? "server" : levelKey.location());
        }

        return timerTask;
    }

    private void tickTasks(List<TimerTask> tasks, long currentTick, ResourceKey<Level> levelKey) {
        for (TimerTask task : tasks) {
            if (task.isCancelled()) {
                tasks.remove(task);
                continue;
            }
            if (currentTick >= task.nextTick) {
                runTask(task, currentTick, levelKey);
            }
        }
    }

    private void runTask(TimerTask task, long currentTick, ResourceKey<Level> levelKey) {
        task.task.run();
        long nextTick = task.nextTick;
        while (nextTick <= currentTick) {
            nextTick += task.intervalTicks;
        }
        task.nextTick = nextTick;

        if (TickTokConfig.isDebugLoggingEnabled() && ModConstants.LOGGER.isDebugEnabled()) {
            ModConstants.LOGGER.debug("TickTokTimerScheduler fired task (next in {}t) for {}",
                    task.intervalTicks,
                    levelKey == null ? "server" : levelKey.location());
        }
    }

    public interface TimerHandle {
        boolean cancel();

        boolean isCancelled();
    }

    private static final class TimerTask implements TimerHandle {
        private final long intervalTicks;
        private final Runnable task;
        private volatile long nextTick;
        private volatile boolean cancelled;

        private TimerTask(long intervalTicks, long nextTick, Runnable task) {
            this.intervalTicks = intervalTicks;
            this.nextTick = nextTick;
            this.task = task;
        }

        @Override
        public boolean cancel() {
            cancelled = true;
            return true;
        }

        @Override
        public boolean isCancelled() {
            return cancelled;
        }
    }
}
