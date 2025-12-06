package com.thunder.ticktoklib.util;

import com.thunder.ticktoklib.api.TickTokPhase;

public class TickTokTimeRange {
    public static final long FULL_DAY = 24_000L;
    public static final long DAY_START = 0L;
    public static final long SUNSET_START = 12_000L;
    public static final long NIGHT_START = 13_000L;
    public static final long SUNRISE_START = 23_000L;

    public static boolean isWithin(long tick, long start, long end) {
        return tick >= start && tick < end;
    }

    public static boolean isWithinPhase(long tick, TickTokPhase phase) {
        return switch (phase) {
            case DAY -> isWithin(tick, DAY_START, SUNSET_START);
            case SUNSET -> isWithin(tick, SUNSET_START, NIGHT_START);
            case NIGHT -> isWithin(tick, NIGHT_START, SUNRISE_START);
            case SUNRISE -> tick >= SUNRISE_START || tick < DAY_START; // wraparound into next day
        };
    }

    public static TickTokPhase phaseFor(long tick) {
        if (isWithin(tick, DAY_START, SUNSET_START)) return TickTokPhase.DAY;
        if (isWithin(tick, SUNSET_START, NIGHT_START)) return TickTokPhase.SUNSET;
        if (isWithin(tick, NIGHT_START, SUNRISE_START)) return TickTokPhase.NIGHT;
        return TickTokPhase.SUNRISE;
    }

    public static long phaseStart(TickTokPhase phase) {
        return switch (phase) {
            case DAY -> DAY_START;
            case SUNSET -> SUNSET_START;
            case NIGHT -> NIGHT_START;
            case SUNRISE -> SUNRISE_START;
        };
    }

    public static long ticksUntilPhase(long tick, TickTokPhase phase) {
        long start = phaseStart(phase);
        if (tick <= start) {
            return start - tick;
        }
        return FULL_DAY - tick + start;
    }

    public static long ticksSincePhaseStart(long tick, TickTokPhase phase) {
        long start = phaseStart(phase);
        if (tick >= start) {
            return tick - start;
        }
        return FULL_DAY - start + tick;
    }
}
