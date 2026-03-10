package com.thunder.ticktoklib.util;

import com.thunder.ticktoklib.Core.ModConstants;
import com.thunder.ticktoklib.TickTokConfig;
import com.thunder.ticktoklib.api.TickTokPhase;
import com.thunder.ticktoklib.event.TickTokPhaseEvent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.NeoForge;

import java.util.HashMap;
import java.util.Map;

public class TickTokPhaseTracker {
    private final Map<ResourceKey<Level>, PhaseState> stateByLevel = new HashMap<>();

    public void handle(long dayTime, ResourceKey<Level> levelKey) {
        long clamped = Math.floorMod(dayTime, TickTokTimeRange.FULL_DAY);
        PhaseState state = stateByLevel.get(levelKey);

        if (state == null) {
            TickTokPhase current = TickTokTimeRange.phaseFor(clamped);
            stateByLevel.put(levelKey, new PhaseState(current, nextBoundaryFor(current), clamped));
            if (TickTokConfig.isEventTracingEnabled() && ModConstants.LOGGER.isTraceEnabled()) {
                ModConstants.LOGGER.trace("TickTokPhaseTracker initialized phase {} for {}", current, levelKey.location());
            }
            NeoForge.EVENT_BUS.post(new TickTokPhaseEvent.Start(current, clamped, levelKey));
            return;
        }

        if (!crossedBoundary(state.lastClampedTick, clamped, state.nextBoundaryTick)) {
            state.lastClampedTick = clamped;
            return;
        }

        TickTokPhase current = TickTokTimeRange.phaseFor(clamped);
        if (state.phase != current) {
            NeoForge.EVENT_BUS.post(new TickTokPhaseEvent.End(state.phase, clamped, levelKey));
            NeoForge.EVENT_BUS.post(new TickTokPhaseEvent.Start(current, clamped, levelKey));
            if (TickTokConfig.isEventTracingEnabled() && ModConstants.LOGGER.isTraceEnabled()) {
                ModConstants.LOGGER.trace("TickTokPhaseTracker transitioned {} -> {} for {} at {}", state.phase, current, levelKey.location(), clamped);
            }
            state.phase = current;
        }

        state.nextBoundaryTick = nextBoundaryFor(state.phase);
        state.lastClampedTick = clamped;
    }

    private static long nextBoundaryFor(TickTokPhase phase) {
        return switch (phase) {
            case DAY -> TickTokTimeRange.SUNSET_START;
            case SUNSET -> TickTokTimeRange.NIGHT_START;
            case NIGHT -> TickTokTimeRange.SUNRISE_START;
            case SUNRISE -> TickTokTimeRange.DAY_START;
        };
    }

    private static boolean crossedBoundary(long previousTick, long currentTick, long boundaryTick) {
        if (previousTick == currentTick) {
            return false;
        }

        if (previousTick < currentTick) {
            return previousTick < boundaryTick && boundaryTick <= currentTick;
        }

        return previousTick < boundaryTick || boundaryTick <= currentTick;
    }

    private static final class PhaseState {
        private TickTokPhase phase;
        private long nextBoundaryTick;
        private long lastClampedTick;

        private PhaseState(TickTokPhase phase, long nextBoundaryTick, long lastClampedTick) {
            this.phase = phase;
            this.nextBoundaryTick = nextBoundaryTick;
            this.lastClampedTick = lastClampedTick;
        }
    }
}
