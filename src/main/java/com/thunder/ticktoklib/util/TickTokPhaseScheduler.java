package com.thunder.ticktoklib.util;

import com.thunder.ticktoklib.Core.ModConstants;
import com.thunder.ticktoklib.TickTokConfig;
import com.thunder.ticktoklib.api.TickTokPhase;
import com.thunder.ticktoklib.event.TickTokPhaseEvent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Allows callers to schedule callbacks that fire when a specific phase begins.
 */
public class TickTokPhaseScheduler {
    private final Map<ResourceKey<Level>, Map<TickTokPhase, Queue<Runnable>>> scheduled = new ConcurrentHashMap<>();
    private final Map<TickTokPhase, Queue<Runnable>> anyLevelScheduled = new ConcurrentHashMap<>();

    public TickTokPhaseScheduler() {
        NeoForge.EVENT_BUS.register(this);
    }

    /**
     * Schedule a task that runs when the next occurrence of the phase begins on any level.
     */
    public void scheduleAtPhase(TickTokPhase phase, Runnable task) {
        scheduleAtPhaseInternal(null, phase, task);
    }

    /**
     * Schedule a task that runs when the next occurrence of the phase begins in a specific level.
     */
    public void scheduleAtPhase(ResourceKey<Level> levelKey, TickTokPhase phase, Runnable task) {
        Objects.requireNonNull(levelKey, "levelKey");
        scheduleAtPhaseInternal(levelKey, phase, task);
    }

    /**
     * Create a barrier that completes when the target phase starts on the given level (or any level if null).
     */
    public PhaseBarrier barrier(ResourceKey<Level> levelKey, TickTokPhase phase) {
        CompletableFuture<Void> gate = new CompletableFuture<>();
        scheduleAtPhaseInternal(levelKey, phase, () -> gate.complete(null));
        return new PhaseBarrier(gate);
    }

    @SubscribeEvent
    public void onPhaseStart(TickTokPhaseEvent.Start event) {
        fireFor(event.levelKey, event.phase);
        fireFor(null, event.phase);
    }

    private void fireFor(ResourceKey<Level> levelKey, TickTokPhase phase) {
        Map<TickTokPhase, Queue<Runnable>> perPhase = levelKey == null ? anyLevelScheduled : scheduled.get(levelKey);
        if (perPhase == null) {
            return;
        }

        Queue<Runnable> tasks = perPhase.get(phase);
        if (tasks == null || tasks.isEmpty()) {
            return;
        }

        int taskCount = tasks.size();
        if (TickTokConfig.isEventTracingEnabled() && ModConstants.LOGGER.isTraceEnabled()) {
            ModConstants.LOGGER.trace("TickTokPhaseScheduler firing {} task(s) for {} in {}", taskCount, phase, levelKey == null ? "any level" : levelKey.location());
        }

        // Run tasks and clear one-shot schedule
        Runnable runnable;
        while ((runnable = tasks.poll()) != null) {
            runnable.run();
        }

        if (tasks.isEmpty()) {
            perPhase.remove(phase, tasks);

            if (perPhase.isEmpty() && levelKey != null) {
                scheduled.remove(levelKey);
            }
        }
    }

    private void scheduleAtPhaseInternal(ResourceKey<Level> levelKey, TickTokPhase phase, Runnable task) {
        Objects.requireNonNull(phase, "phase");
        Objects.requireNonNull(task, "task");
        Map<TickTokPhase, Queue<Runnable>> perPhase = levelKey == null
                ? anyLevelScheduled
                : scheduled.computeIfAbsent(levelKey, key -> new ConcurrentHashMap<>());

        perPhase
                .computeIfAbsent(phase, __ -> new ConcurrentLinkedQueue<>())
                .add(task);

        if (TickTokConfig.isDebugLoggingEnabled() && ModConstants.LOGGER.isDebugEnabled()) {
            ModConstants.LOGGER.debug("TickTokPhaseScheduler scheduled task for phase {} in {}", phase, Optional.ofNullable(levelKey).map(key -> key.location().toString()).orElse(ModConstants.MOD_ID + ":any"));
        }
    }

    public record PhaseBarrier(CompletableFuture<Void> gate) {
        public CompletableFuture<Void> asFuture() {
            return gate;
        }

        public boolean cancel() {
            return gate.cancel(false);
        }
    }
}
