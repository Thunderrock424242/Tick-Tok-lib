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
    private final Map<ResourceKey<Level>, TickTokPhase> lastPhaseByLevel = new HashMap<>();

    public void handle(long dayTime, ResourceKey<Level> levelKey) {
        long clamped = Math.floorMod(dayTime, TickTokTimeRange.FULL_DAY);
        TickTokPhase current = TickTokTimeRange.phaseFor(clamped);
        TickTokPhase last = lastPhaseByLevel.get(levelKey);

        if (last == null) {
            lastPhaseByLevel.put(levelKey, current);
            if (TickTokConfig.isEventTracingEnabled() && ModConstants.LOGGER.isTraceEnabled()) {
                ModConstants.LOGGER.trace("TickTokPhaseTracker initialized phase {} for {}", current, levelKey.location());
            }
            NeoForge.EVENT_BUS.post(new TickTokPhaseEvent.Start(current, clamped));
            return;
        }

        if (last != current) {
            NeoForge.EVENT_BUS.post(new TickTokPhaseEvent.End(last, clamped));
            NeoForge.EVENT_BUS.post(new TickTokPhaseEvent.Start(current, clamped));
            lastPhaseByLevel.put(levelKey, current);
            if (TickTokConfig.isEventTracingEnabled() && ModConstants.LOGGER.isTraceEnabled()) {
                ModConstants.LOGGER.trace("TickTokPhaseTracker transitioned {} -> {} for {} at {}", last, current, levelKey.location(), clamped);
            }
        }
    }
}
