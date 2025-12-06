package com.thunder.ticktoklib.event;

import com.thunder.ticktoklib.api.TickTokPhase;
import net.neoforged.bus.api.Event;

public abstract class TickTokPhaseEvent extends Event {
    public final TickTokPhase phase;
    public final long dayTime;

    public TickTokPhaseEvent(TickTokPhase phase, long dayTime) {
        this.phase = phase;
        this.dayTime = dayTime;
    }

    public static class Start extends TickTokPhaseEvent {
        public Start(TickTokPhase phase, long dayTime) { super(phase, dayTime); }
    }

    public static class End extends TickTokPhaseEvent {
        public End(TickTokPhase phase, long dayTime) { super(phase, dayTime); }
    }
}
