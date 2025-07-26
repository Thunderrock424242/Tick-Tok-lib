package com.thunder.ticktoklib;

public class TickTokTimeBuilder {
    private int hours = 0, minutes = 0, seconds = 0;

    public TickTokTimeBuilder hours(int h) {
        this.hours = h; return this;
    }

    public TickTokTimeBuilder minutes(int m) {
        this.minutes = m; return this;
    }

    public TickTokTimeBuilder seconds(int s) {
        this.seconds = s; return this;
    }

    public long toTicks() {
        return TickTokHelper.duration(hours, minutes, seconds);
    }
}