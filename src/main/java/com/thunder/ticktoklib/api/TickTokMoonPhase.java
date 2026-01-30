package com.thunder.ticktoklib.api;

public enum TickTokMoonPhase {
    FULL_MOON(0),
    WANING_GIBBOUS(1),
    LAST_QUARTER(2),
    WANING_CRESCENT(3),
    NEW_MOON(4),
    WAXING_CRESCENT(5),
    FIRST_QUARTER(6),
    WAXING_GIBBOUS(7);

    private static final TickTokMoonPhase[] VALUES = values();
    private final int index;

    TickTokMoonPhase(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public static TickTokMoonPhase fromIndex(int index) {
        int normalized = Math.floorMod(index, VALUES.length);
        return VALUES[normalized];
    }
}
