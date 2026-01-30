package com.thunder.ticktoklib;

import com.thunder.ticktoklib.Core.ModConstants;
import com.thunder.ticktoklib.util.TickTokDurationBreakdown;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class TickTokFormatter {

    private static void logFormatting(String methodName, long ticks, String formatted) {
        if (TickTokConfig.isFormattingTracingEnabled() && ModConstants.LOGGER.isTraceEnabled()) {
            ModConstants.LOGGER.trace("TickTokFormatter.{}(ticks={}) -> {}", methodName, ticks, formatted);
        }
    }

    /** Format as "HH:mm" */
    public static String formatHHMM(long ticks) {
        long totalSeconds = ticks / TickTokHelper.TICKS_PER_SECOND;
        long hours   = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        String formatted = String.format("%02d:%02d", hours, minutes);
        logFormatting("formatHHMM", ticks, formatted);
        return formatted;
    }

    /** Format as "HH:mm:ss" */
    public static String formatHHMMSS(long ticks) {
        long totalSeconds = ticks / TickTokHelper.TICKS_PER_SECOND;
        long hours   = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;
        String formatted = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        logFormatting("formatHHMMSS", ticks, formatted);
        return formatted;
    }

    /** Format as "HH:mm:ss.SSS" */
    public static String formatHMSms(long ticks) {
        long totalMs    = TickTokHelper.toMillisecondsLong(ticks);
        long hours      = totalMs / 3_600_000;
        long minutes    = (totalMs % 3_600_000) / 60_000;
        long seconds    = (totalMs % 60_000) / 1000;
        long millis     = totalMs % 1000;
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

    /**
     * Format a tick duration as a short label (ex: "1h 2m 3s 450ms").
     */
    public static String formatDurationShort(long ticks) {
        String formatted = formatDuration(ticks, true);
        logFormatting("formatDurationShort", ticks, formatted);
        return formatted;
    }

    /**
     * Format a tick duration as a long label (ex: "1 hour 2 minutes 3 seconds 450 milliseconds").
     */
    public static String formatDurationLong(long ticks) {
        String formatted = formatDuration(ticks, false);
        logFormatting("formatDurationLong", ticks, formatted);
        return formatted;
    }

    private static String formatDuration(long ticks, boolean shortUnits) {
        TickTokDurationBreakdown breakdown = TickTokDurationBreakdown.fromTicks(ticks);
        long hours = breakdown.hours();
        long minutes = breakdown.minutes();
        long seconds = breakdown.seconds();
        long millis = breakdown.milliseconds();

        StringBuilder builder = new StringBuilder();
        appendDurationUnit(builder, hours, shortUnits ? "h" : "hour", shortUnits);
        appendDurationUnit(builder, minutes, shortUnits ? "m" : "minute", shortUnits);
        appendDurationUnit(builder, seconds, shortUnits ? "s" : "second", shortUnits);
        if (millis > 0 || builder.length() == 0) {
            appendDurationUnit(builder, millis, shortUnits ? "ms" : "millisecond", shortUnits);
        }
        return builder.toString();
    }

    private static void appendDurationUnit(StringBuilder builder, long value, String unit, boolean shortUnits) {
        if (value == 0) {
            return;
        }
        if (builder.length() > 0) {
            builder.append(' ');
        }
        if (shortUnits) {
            builder.append(value).append(unit);
            return;
        }
        builder.append(value).append(' ').append(unit);
        if (value != 1) {
            builder.append('s');
        }
    }
}
