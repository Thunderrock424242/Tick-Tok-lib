package com.thunder.ticktoklib;

import com.thunder.ticktoklib.Core.ModConstants;
import net.minecraft.network.chat.Component;

/**
 * Helpers for turning "server is X ticks behind" messages into human-friendly time.
 */
public class TickTokLagFormatter {

    private TickTokLagFormatter() {
    }

    public static String formatLagBehind(long ticksBehind, long millisecondsBehind) {
        double seconds = ticksBehind / (double) TickTokHelper.TICKS_PER_SECOND;
        String formatted = String.format(
                "Server is %d ticks behind (%.2f seconds / %d ms)",
                ticksBehind,
                seconds,
                millisecondsBehind
        );
        logFormatting("formatLagBehind", ticksBehind, formatted);
        return formatted;
    }

    /**
     * Format a lagging tick count into a chat-friendly {@link Component}.
     *
     * @param ticksBehind how many ticks the server is behind
     * @return formatted component, e.g. {@code "Server is 120 ticks behind (6.00 seconds / 6000 ms)"}
     */
    public static Component buildLagBehindReport(long ticksBehind) {
        String formatted = formatLagBehind(ticksBehind, TickTokHelper.toMillisecondsLong(ticksBehind));
        logFormatting("buildLagBehindReport", ticksBehind, formatted);
        return Component.literal(formatted);
    }

    private static void logFormatting(String methodName, long ticks, String formatted) {
        if (TickTokConfig.isFormattingTracingEnabled() && ModConstants.LOGGER.isTraceEnabled()) {
            ModConstants.LOGGER.trace("TickTokLagFormatter.{}(ticks={}) -> {}", methodName, ticks, formatted);
        }
    }
}
