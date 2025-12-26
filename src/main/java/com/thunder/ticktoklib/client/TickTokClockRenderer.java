package com.thunder.ticktoklib.client;

import com.thunder.ticktoklib.Core.ModConstants;
import com.thunder.ticktoklib.TickTokConfig;
import com.thunder.ticktoklib.TickTokFormatter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

import java.time.LocalTime;
import java.util.Locale;
import java.util.Objects;

import static com.thunder.ticktoklib.Core.ModConstants.MOD_ID;

/**
        * Renders game time and local time on the screen using the HUD.
        */
@EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT)
public class TickTokClockRenderer {

    private static final Locale LOCALE = Locale.getDefault();
    private static final java.time.format.DateTimeFormatter LOCAL_TIME_FORMATTER = java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss");

    private static String cachedGameTime;
    private static String cachedLocalTime;
    private static long lastGameTick = Long.MIN_VALUE;
    private static long lastLocalSecond = Long.MIN_VALUE;

    @SubscribeEvent
    public static void onRender(RenderGuiEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();

        if (mc.player == null || mc.level == null) return;

        if (ModConstants.LOGGER.isTraceEnabled()) {
            ModConstants.LOGGER.trace("TickTokClockRenderer.onRender - player={}, level={}",
                    mc.player.getName().getString(), mc.level.dimension().location());
        }

        // Only show if holding a clock
        if (!isHoldingClock(mc)) return;

        // Ensure caches are populated once per frame; updates only happen when values change.
        updateGameTimeCache(mc);
        updateLocalTimeCache();

        if (TickTokConfig.isDebugLoggingEnabled() && ModConstants.LOGGER.isDebugEnabled()) {
            ModConstants.LOGGER.debug("TickTokClockRenderer.onRender - rendering HUD overlay using TickTokConfig settings");
        }

        GuiGraphics graphics = event.getGuiGraphics();
        var font = mc.font;

        int x = 10;
        int y = 10;

        graphics.pose().pushPose();
        graphics.pose().translate(0, 0, 1000); // Ensure it's drawn above everything

        if (TickTokConfig.showGameTime()) {
            String text = "Game Time: " + Objects.requireNonNullElse(cachedGameTime, "--:--:--");
            graphics.drawString(font, text, x, y, 0xFFFFFF, true);
            y += font.lineHeight + 2;

            if (ModConstants.LOGGER.isTraceEnabled()) {
                ModConstants.LOGGER.trace("TickTokClockRenderer - displayed game time via TickTokConfig.SHOW_GAME_TIME");
            }
        }

        if (TickTokConfig.showLocalTime()) {
            String text = "Local Time: " + Objects.requireNonNullElse(cachedLocalTime, "--:--:--");
            graphics.drawString(font, text, x, y, 0xAAAAFF, true);

            if (ModConstants.LOGGER.isTraceEnabled()) {
                ModConstants.LOGGER.trace("TickTokClockRenderer - displayed local time via TickTokConfig.SHOW_LOCAL_TIME");
            }
        }

        graphics.pose().popPose();
    }

    private static boolean isHoldingClock(Minecraft mc) {
        ItemStack mainHand = mc.player.getMainHandItem();
        ItemStack offHand = mc.player.getOffhandItem();
        return mainHand.is(Items.CLOCK) || offHand.is(Items.CLOCK);
    }

    private static void updateGameTimeCache(Minecraft mc) {
        if (!TickTokConfig.showGameTime()) {
            return;
        }

        long dayTime = Math.floorMod(mc.level.getDayTime(), 24000L);
        if (dayTime != lastGameTick) {
            cachedGameTime = TickTokFormatter.formatClock(dayTime, true, false, true, LOCALE);
            lastGameTick = dayTime;
        }
    }

    private static void updateLocalTimeCache() {
        if (!TickTokConfig.showLocalTime()) {
            return;
        }

        long currentSecond = System.currentTimeMillis() / 1000L;
        if (currentSecond != lastLocalSecond) {
            cachedLocalTime = LocalTime.now().format(LOCAL_TIME_FORMATTER);
            lastLocalSecond = currentSecond;
        }
    }
}
