package com.thunder.ticktoklib.event;

import com.thunder.ticktoklib.api.TickTokPhase;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.Event;

public abstract class TickTokPhaseEvent extends Event {
    public final TickTokPhase phase;
    public final long dayTime;
    public final ResourceKey<Level> levelKey;

    public TickTokPhaseEvent(TickTokPhase phase, long dayTime, ResourceKey<Level> levelKey) {
        this.phase = phase;
        this.dayTime = dayTime;
        this.levelKey = levelKey;
    }

    public static class Start extends TickTokPhaseEvent {
        public Start(TickTokPhase phase, long dayTime, ResourceKey<Level> levelKey) { super(phase, dayTime, levelKey); }
    }

    public static class End extends TickTokPhaseEvent {
        public End(TickTokPhase phase, long dayTime, ResourceKey<Level> levelKey) { super(phase, dayTime, levelKey); }
    }
}
