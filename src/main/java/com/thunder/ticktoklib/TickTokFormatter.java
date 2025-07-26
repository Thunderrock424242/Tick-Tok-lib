package com.thunder.ticktoklib;

import static com.thunder.ticktoklib.TickTokHelper.MS_PER_TICK;

public class TickTokFormatter {

    /** Format as "HH:mm" */
    public static String formatHHMM(long ticks) {
        int totalSeconds = (int)(ticks / TickTokHelper.TICKS_PER_SECOND);
        int hours   = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        return String.format("%02d:%02d", hours, minutes);
    }

    /** Format as "HH:mm:ss" */
    public static String formatHHMMSS(long ticks) {
        int totalSeconds = (int)(ticks / TickTokHelper.TICKS_PER_SECOND);
        int hours   = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    /** Format as "HH:mm:ss.SSS" */
    public static String formatHMSms(long ticks) {
        int totalMs    = (int)(ticks * MS_PER_TICK);
        int hours      = totalMs / 3_600_000;
        int minutes    = (totalMs % 3_600_000) / 60_000;
        int seconds    = (totalMs % 60_000) / 1000;
        int millis     = totalMs % 1000;
        return String.format("%02d:%02d:%02d.%03d", hours, minutes, seconds, millis);
    }
}