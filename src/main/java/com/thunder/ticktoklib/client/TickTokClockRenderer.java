package com.thunder.ticktoklib.client;

import com.thunder.ticktoklib.TickTokConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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
public class TickTokClockRenderer {

    private static final DateTimeFormatter LOCAL_TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss");

    @SubscribeEvent
    public static void onRender(RenderGuiEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();

        if (mc.player == null || mc.level == null) return;

        ItemStack mainHand = mc.player.getMainHandItem();
        ItemStack offHand = mc.player.getOffhandItem();

        // Only show if holding a clock
        if (!mainHand.is(Items.CLOCK) && !offHand.is(Items.CLOCK)) return;

        GuiGraphics graphics = event.getGuiGraphics();
        var font = mc.font;

        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();
        int x = 10;
        int y = 10;

        graphics.pose().pushPose();
        graphics.pose().translate(0, 0, 1000); // Ensure it's drawn above everything

        if (TickTokConfig.SHOW_GAME_TIME.get()) {
            String gameTime = formatMinecraftTime(mc.level.getDayTime());
            graphics.drawString(font, "Game Time: " + gameTime, x, y, 0xFFFFFF, true);
            y += 12;
        }

        if (TickTokConfig.SHOW_LOCAL_TIME.get()) {
            String localTime = LocalTime.now().format(LOCAL_TIME_FORMAT);
            graphics.drawString(font, "Local Time: " + localTime, x, y, 0xAAAAFF, true);
        }

        graphics.pose().popPose();
    }

    private static String formatMinecraftTime(long dayTime) {
        long timeOfDay = dayTime % 24000;
        int hours = (int)((timeOfDay / 1000 + 6) % 24);
        int minutes = (int)((timeOfDay % 1000) * 60 / 1000);
        return String.format("%02d:%02d", hours, minutes);
    }
}
