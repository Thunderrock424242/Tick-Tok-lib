package com.thunder.ticktoklib.util;

import com.thunder.ticktoklib.api.TickTokPhase;

public class TickTokTimeRange {
    public static boolean isWithin(long tick, long start, long end) {
        return tick >= start && tick < end;
    }

    public static boolean isWithinPhase(long tick, TickTokPhase phase, long dayStart, long sunsetStart, long nightStart, long sunriseStart) {
        return switch (phase) {
            case DAY -> isWithin(tick, dayStart, sunsetStart);
            case SUNSET -> isWithin(tick, sunsetStart, nightStart);
            case NIGHT -> isWithin(tick, nightStart, sunriseStart);
            case SUNRISE -> tick >= sunriseStart;
        };
    }
}