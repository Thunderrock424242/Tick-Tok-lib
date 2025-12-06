package com.thunder.ticktoklib;

import com.thunder.ticktoklib.Core.ModConstants;
import com.thunder.ticktoklib.config.HudPosition;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.function.Supplier;

public class TickTokConfig {
    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;

    public static final ModConfigSpec.BooleanValue SHOW_GAME_TIME;
    public static final ModConfigSpec.BooleanValue SHOW_LOCAL_TIME;
    public static final ModConfigSpec.EnumValue<HudPosition> GAME_TIME_POSITION;
    public static final ModConfigSpec.EnumValue<HudPosition> LOCAL_TIME_POSITION;
    public static final ModConfigSpec.BooleanValue ENABLE_TICK_OPTIMIZATION;
    public static final ModConfigSpec.BooleanValue ENABLE_DEBUG_LOGGING;
    public static final ModConfigSpec.BooleanValue ENABLE_TRACE_CONVERSIONS;
    public static final ModConfigSpec.BooleanValue ENABLE_TRACE_FORMATTING;
    public static final ModConfigSpec.BooleanValue ENABLE_TRACE_EVENTS;

    static {
        BUILDER.push("Tick Time Display Options");

        SHOW_GAME_TIME = BUILDER
                .comment("Show in-game time (HH:mm) on screen")
                .define("show_game_time", true);

        SHOW_LOCAL_TIME = BUILDER
                .comment("Show local system time (HH:mm:ss) on screen")
                .define("show_local_time", true);

        GAME_TIME_POSITION = BUILDER
                .comment("Game time position: TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT")
                .defineEnum("game_time_position", HudPosition.TOP_LEFT);

        LOCAL_TIME_POSITION = BUILDER
                .comment("Local time position: TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT")
                .defineEnum("local_time_position", HudPosition.TOP_RIGHT);

        BUILDER.pop();

        BUILDER.push("Performance");

        ENABLE_TICK_OPTIMIZATION = BUILDER
                .comment("Reduce TickTok's per-tick workload by sampling world time once per second (20 ticks)")
                .define("enable_tick_optimization", true);

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

        BUILDER.pop();
        SPEC = BUILDER.build();
    }

    public static boolean isTickOptimizationEnabled() {
        return safeGet(ENABLE_TICK_OPTIMIZATION, true);
    }

    private static <T> T safeGet(ModConfigSpec.ConfigValue<T> configValue, Supplier<T> fallbackSupplier) {
        try {
            return configValue.get();
        } catch (IllegalStateException exception) {
            T fallback = fallbackSupplier.get();
            ModConstants.LOGGER.warn("Config {} unavailable, using fallback {}", configValue.getPath(), fallback);
            return fallback;
        }
    }

    private static boolean safeGet(ModConfigSpec.BooleanValue configValue, boolean fallback) {
        try {
            return configValue.get();
        } catch (IllegalStateException exception) {
            ModConstants.LOGGER.warn("Config {} unavailable, using fallback {}", configValue.getPath(), fallback);
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

    public static boolean showGameTime() {
        return safeGet(SHOW_GAME_TIME, true);
    }

    public static boolean showLocalTime() {
        return safeGet(SHOW_LOCAL_TIME, true);
    }

    public static HudPosition gameTimePosition() {
        return safeGet(GAME_TIME_POSITION, () -> HudPosition.TOP_LEFT);
    }

    public static HudPosition localTimePosition() {
        return safeGet(LOCAL_TIME_POSITION, () -> HudPosition.TOP_RIGHT);
    }
}
