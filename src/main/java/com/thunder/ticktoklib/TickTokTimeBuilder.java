package com.thunder.ticktoklib;

import com.thunder.ticktoklib.Core.ModConstants;
import com.thunder.ticktoklib.TickTokConfig;

public class TickTokTimeBuilder {
    private long hours        = 0;
    private long minutes      = 0;
    private long seconds      = 0;
    private long milliseconds = 0;

    public TickTokTimeBuilder hours(long h) {
        this.hours = h; return this;
    }

    public TickTokTimeBuilder minutes(long m) {
        this.minutes = m; return this;
    }

    public TickTokTimeBuilder seconds(long s) {
        this.seconds = s; return this;
    }

    public TickTokTimeBuilder milliseconds(long ms) {
        this.milliseconds = ms; return this;
    }

    /**
     * @return total ticks for the specified hours/minutes/seconds/milliseconds
     */
    public int toTicks() {
        if (TickTokConfig.isDebugLoggingEnabled() && ModConstants.LOGGER.isDebugEnabled()) {
            ModConstants.LOGGER.debug(
                    "TickTokTimeBuilder.toTicks -> TickTokHelper.duration(h={}, m={}, s={}, ms={})",
                    hours, minutes, seconds, milliseconds
            );
        }
        return Math.toIntExact(TickTokHelper.durationLong(hours, minutes, seconds, milliseconds));
    }

    public long toTicksLong() {
        if (TickTokConfig.isDebugLoggingEnabled() && ModConstants.LOGGER.isDebugEnabled()) {
            ModConstants.LOGGER.debug(
                    "TickTokTimeBuilder.toTicksLong -> TickTokHelper.durationLong(h={}, m={}, s={}, ms={})",
                    hours, minutes, seconds, milliseconds
            );
        }
        return TickTokHelper.durationLong(hours, minutes, seconds, milliseconds);
    }
}
