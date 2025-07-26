package com.thunder.ticktoklib;

public class TickTokFormatter {
    public static String formatHHMM(long ticks) {
        int hours = (int)((ticks / 1000 + 6) % 24);
        int minutes = (int)((ticks % 1000) * 60 / 1000);
        return String.format("%02d:%02d", hours, minutes);
    }

    public static String formatHHMMSS(long ticks) {
        int hours = (int)((ticks / 1000 + 6) % 24);
        int minutes = (int)((ticks % 1000) * 60 / 1000);
        int seconds = (int)((ticks % 20) * 3);
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}