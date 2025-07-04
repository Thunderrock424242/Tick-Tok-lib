package com.thunder.ticktoklib;

import net.neoforged.neoforge.common.ModConfigSpec;

public class TickTokConfig {
    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;

    public static final ModConfigSpec.BooleanValue SHOW_GAME_TIME;
    public static final ModConfigSpec.BooleanValue SHOW_LOCAL_TIME;
    public static final ModConfigSpec.ConfigValue<String> GAME_TIME_POSITION;
    public static final ModConfigSpec.ConfigValue<String> LOCAL_TIME_POSITION;

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
        SPEC = BUILDER.build();
    }
}