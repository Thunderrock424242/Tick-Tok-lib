package com.thunder.ticktoklib;

import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.function.Supplier;

public class TickTokConfig {

    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;

    public static final ModConfigSpec.BooleanValue SHOW_GAME_TIME;
    public static final ModConfigSpec.BooleanValue SHOW_LOCAL_TIME;
    public static final ModConfigSpec.BooleanValue ENABLE_TICK_OPTIMIZATION;
    public static final ModConfigSpec.BooleanValue ENABLE_DEBUG_LOGGING;
    public static final ModConfigSpec.BooleanValue ENABLE_TRACE_CONVERSIONS;
    public static final ModConfigSpec.BooleanValue ENABLE_TRACE_FORMATTING;
    public static final ModConfigSpec.BooleanValue ENABLE_TRACE_EVENTS;
    public static final ModConfigSpec.BooleanValue ENABLE_OVERLOAD_DIAGNOSTICS;

    static {
        BUILDER.push("Tick Time Display Options");

        SHOW_GAME_TIME = BUILDER
                .comment("Show in-game time (HH:mm) on screen")
                .define("show_game_time", true);

        SHOW_LOCAL_TIME = BUILDER
                .comment("Show local system time (HH:mm:ss) on screen")
                .define("show_local_time", true);

        BUILDER.pop();

        BUILDER.push("Performance");

        ENABLE_TICK_OPTIMIZATION = BUILDER
                .comment("Reduce TickTok's per-tick workload by sampling world time once per second (20 ticks)")
                .define("enable_tick_optimization", false);

        BUILDER.pop();

        BUILDER.push("Debugging");

        ENABLE_DEBUG_LOGGING = BUILDER
                .comment("Enable verbose debug-level logging output in the console")
                .define("enable_debug_logging", false);

        ENABLE_TRACE_CONVERSIONS = BUILDER
                .comment("Emit TRACE logs for tick/time conversion helpers")
                .define("enable_trace_conversions", false);

        ENABLE_TRACE_FORMATTING = BUILDER
                .comment("Emit TRACE logs for formatting helpers")
                .define("enable_trace_formatting", false);

        ENABLE_TRACE_EVENTS = BUILDER
                .comment("Emit TRACE logs for time-of-day phase events")
                .define("enable_trace_events", false);

        ENABLE_OVERLOAD_DIAGNOSTICS = BUILDER
                .comment("Append suspected cause hints to server overload warnings")
                .define("enable_overload_diagnostics", true);

        BUILDER.pop();

        SPEC = BUILDER.build();
    }

    /**
     * Returns whether TickTok's tick optimization is enabled.
     * <p>
     * During early NeoForge startup the config may be registered but not yet
     * fully loaded. In that case the safe default is returned.
     */
    public static boolean isTickOptimizationEnabled() {
        return safeGet(ENABLE_TICK_OPTIMIZATION, false);
    }

    /**
     * Safely reads a generic config value.
     * <p>
     * NeoForge can legitimately throw IllegalStateException when a value is
     * accessed before its config has finished loading. This is an expected
     * lifecycle state, so TickTok uses the supplied fallback without logging
     * a warning.
     */
    private static <T> T safeGet(
            ModConfigSpec.ConfigValue<T> configValue,
            Supplier<T> fallbackSupplier
    ) {
        try {
            return configValue.get();
        } catch (IllegalStateException exception) {
            return fallbackSupplier.get();
        }
    }

    /**
     * Safely reads a boolean config value.
     * <p>
     * If NeoForge has not finished loading the config yet, return the supplied
     * fallback instead of producing misleading startup warning spam.
     */
    private static boolean safeGet(
            ModConfigSpec.BooleanValue configValue,
            boolean fallback
    ) {
        try {
            return configValue.get();
        } catch (IllegalStateException exception) {
            return fallback;
        }
    }

    public static boolean isDebugLoggingEnabled() {
        return safeGet(ENABLE_DEBUG_LOGGING, false);
    }

    public static boolean isConversionTracingEnabled() {
        return safeGet(ENABLE_TRACE_CONVERSIONS, false);
    }

    public static boolean isFormattingTracingEnabled() {
        return safeGet(ENABLE_TRACE_FORMATTING, false);
    }

    public static boolean isEventTracingEnabled() {
        return safeGet(ENABLE_TRACE_EVENTS, false);
    }

    public static boolean isOverloadDiagnosticsEnabled() {
        return safeGet(ENABLE_OVERLOAD_DIAGNOSTICS, true);
    }

    public static boolean showGameTime() {
        return safeGet(SHOW_GAME_TIME, true);
    }

    public static boolean showLocalTime() {
        return safeGet(SHOW_LOCAL_TIME, true);
    }
}