package com.thunder.ticktoklib.client;

import com.thunder.ticktoklib.TickTokConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.PauseScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;


import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static com.thunder.ticktoklib.Core.ModConstants.MOD_ID;

/**
        * Renders game time and local time on the screen using the HUD.
        */
@EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT)
public class TickTokPauseMenuRenderer {

    private static final DateTimeFormatter LOCAL_TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss");

    @SubscribeEvent
    public static void onPauseScreenRender(RenderGuiEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (!(mc.screen instanceof PauseScreen)) return;

        GuiGraphics graphics = event.getGuiGraphics();
        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();

        int y = screenHeight / 2 + 100; // adjust for spacing

        if (TickTokConfig.SHOW_GAME_TIME.get()) {
            String gameTime = formatMinecraftTime(mc.level.getDayTime());
            drawText(graphics, "Game Time: " + gameTime, 10, y, 0xFFFFFF);
            y += 12;
        }

        if (TickTokConfig.SHOW_LOCAL_TIME.get()) {
            String localTime = LocalTime.now().format(LOCAL_TIME_FORMAT);
            drawText(graphics, "Local Time: " + localTime, 10, y, 0xAAAAFF);
        }
    }

    private static void drawText(GuiGraphics graphics, String text, int x, int y, int color) {
        var font = Minecraft.getInstance().font;
        graphics.drawString(font, text, x, y, color, false);
    }

    private static String formatMinecraftTime(long dayTime) {
        long timeOfDay = dayTime % 24000;
        int hours = (int)((timeOfDay / 1000 + 6) % 24);
        int minutes = (int)((timeOfDay % 1000) * 60 / 1000);
        return String.format("%02d:%02d", hours, minutes);
    }
}