package com.thunder.ticktoklib;

import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.function.Supplier;

public class TickTokConfig {
    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;

    public static final ModConfigSpec.BooleanValue SHOW_GAME_TIME;
    public static final ModConfigSpec.BooleanValue SHOW_LOCAL_TIME;
    public static final ModConfigSpec.ConfigValue<String> GAME_TIME_POSITION;
    public static final ModConfigSpec.ConfigValue<String> LOCAL_TIME_POSITION;
    public static final ModConfigSpec.BooleanValue ENABLE_DEBUG_LOGGING;

    static {
        BUILDER.push("Tick Time Display Options");

        SHOW_GAME_TIME = BUILDER
                .comment("Show in-game time (HH:mm) on screen")
                .define("show_game_time", true);

        SHOW_LOCAL_TIME = BUILDER
                .comment("Show local system time (HH:mm:ss) on screen")
                .define("show_local_time", true);

        GAME_TIME_POSITION = BUILDER
                .comment("Game time position: top_left, top_right, bottom_left, bottom_right")
                .define("game_time_position", "top_left");

        LOCAL_TIME_POSITION = BUILDER
                .comment("Local time position: top_left, top_right, bottom_left, bottom_right")
                .define("local_time_position", "top_right");

        BUILDER.pop();

        BUILDER.push("Debugging");

        ENABLE_DEBUG_LOGGING = BUILDER
                .comment("Enable verbose debug-level logging output in the console")
                .define("enable_debug_logging", false);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }

    private static <T> T safeGet(ModConfigSpec.ConfigValue<T> configValue, Supplier<T> fallbackSupplier) {
        try {
            return configValue.get();
        } catch (IllegalStateException exception) {
            return fallbackSupplier.get();
        }
    }

    private static boolean safeGet(ModConfigSpec.BooleanValue configValue, boolean fallback) {
        try {
            return configValue.get();
        } catch (IllegalStateException exception) {
            return fallback;
        }
    }

    public static boolean isDebugLoggingEnabled() {
        return safeGet(ENABLE_DEBUG_LOGGING, false);
    }

    public static boolean showGameTime() {
        return safeGet(SHOW_GAME_TIME, true);
    }

    public static boolean showLocalTime() {
        return safeGet(SHOW_LOCAL_TIME, true);
    }

    public static String gameTimePosition() {
        return safeGet(GAME_TIME_POSITION, () -> "top_left");
    }

    public static String localTimePosition() {
        return safeGet(LOCAL_TIME_POSITION, () -> "top_right");
    }
}
