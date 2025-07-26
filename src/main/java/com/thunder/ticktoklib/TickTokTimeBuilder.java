package com.thunder.ticktoklib;

public class TickTokTimeBuilder {
    private int hours        = 0;
    private int minutes      = 0;
    private int seconds      = 0;
    private int milliseconds = 0;

    public TickTokTimeBuilder hours(int h) {
        this.hours = h; return this;
    }

    public TickTokTimeBuilder minutes(int m) {
        this.minutes = m; return this;
    }

    public TickTokTimeBuilder seconds(int s) {
        this.seconds = s; return this;
    }

    public TickTokTimeBuilder milliseconds(int ms) {
        this.milliseconds = ms; return this;
    }

    /**
     * @return total ticks for the specified hours/minutes/seconds/milliseconds
     */
    public int toTicks() {
        return TickTokHelper.duration(hours, minutes, seconds, milliseconds);
    }
}