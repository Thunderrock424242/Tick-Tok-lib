package com.thunder.ticktoklib.event;

import com.thunder.ticktoklib.api.TickTokPhase;
import net.neoforged.bus.api.Event;

public abstract class TickTokPhaseEvent extends Event {
    public final TickTokPhase phase;

    public TickTokPhaseEvent(TickTokPhase phase) {
        this.phase = phase;
    }

    public static class Start extends TickTokPhaseEvent {
        public Start(TickTokPhase phase) { super(phase); }
    }

    public static class End extends TickTokPhaseEvent {
        public End(TickTokPhase phase) { super(phase); }
    }
}