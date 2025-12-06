package com.thunder.ticktoklib;

import com.thunder.ticktoklib.Core.ModConstants;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static com.thunder.ticktoklib.TickTokHelper.MS_PER_TICK;

public class TickTokFormatter {

    private static void logFormatting(String methodName, long ticks, String formatted) {
        if (TickTokConfig.isFormattingTracingEnabled() && ModConstants.LOGGER.isTraceEnabled()) {
            ModConstants.LOGGER.trace("TickTokFormatter.{}(ticks={}) -> {}", methodName, ticks, formatted);
        }
    }

    /** Format as "HH:mm" */
    public static String formatHHMM(long ticks) {
        int totalSeconds = (int)(ticks / TickTokHelper.TICKS_PER_SECOND);
        int hours   = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        String formatted = String.format("%02d:%02d", hours, minutes);
        logFormatting("formatHHMM", ticks, formatted);
        return formatted;
    }

    /** Format as "HH:mm:ss" */
    public static String formatHHMMSS(long ticks) {
        int totalSeconds = (int)(ticks / TickTokHelper.TICKS_PER_SECOND);
        int hours   = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;
        String formatted = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        logFormatting("formatHHMMSS", ticks, formatted);
        return formatted;
    }

    /** Format as "HH:mm:ss.SSS" */
    public static String formatHMSms(long ticks) {
        int totalMs    = (int)(ticks * MS_PER_TICK);
        int hours      = totalMs / 3_600_000;
        int minutes    = (totalMs % 3_600_000) / 60_000;
        int seconds    = (totalMs % 60_000) / 1000;
        int millis     = totalMs % 1000;
        String formatted = String.format("%02d:%02d:%02d.%03d", hours, minutes, seconds, millis);
        logFormatting("formatHMSms", ticks, formatted);
        return formatted;
    }

    /**
     * Format ticks using a locale-aware clock pattern (12h/24h depending on pattern).
     */
    public static String formatLocalized(long ticks, String pattern, Locale locale, ZoneId zoneId) {
        LocalTime time = LocalTime.ofNanoOfDay(TickTokHelper.toMillisecondsLong(ticks) * 1_000_000L)
                .atDate(java.time.LocalDate.EPOCH)
                .atZone(zoneId)
                .toLocalTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern, locale);
        String formatted = formatter.format(time);
        logFormatting("formatLocalized", ticks, formatted);
        return formatted;
    }

    public static String formatClock(long ticks, boolean includeSeconds, boolean includeMillis, boolean twentyFourHour, Locale locale) {
        String pattern = twentyFourHour ? "HH:mm" : "hh:mm a";
        if (includeSeconds) {
            pattern = twentyFourHour ? "HH:mm:ss" : "hh:mm:ss a";
        }
        if (includeMillis) {
            pattern += ".SSS";
        }
        return formatLocalized(ticks, pattern, locale, ZoneId.systemDefault());
    }
}
