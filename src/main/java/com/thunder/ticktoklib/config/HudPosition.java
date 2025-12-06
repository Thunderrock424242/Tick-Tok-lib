package com.thunder.ticktoklib.config;

/**
 * Supported HUD corners for time overlays.
 */
public enum HudPosition {
    TOP_LEFT,
    TOP_RIGHT,
    BOTTOM_LEFT,
    BOTTOM_RIGHT;

    public static HudPosition fromString(String raw, HudPosition fallback) {
        for (HudPosition position : values()) {
            if (position.name().equalsIgnoreCase(raw)) {
                return position;
            }
        }
        return fallback;
    }
}
