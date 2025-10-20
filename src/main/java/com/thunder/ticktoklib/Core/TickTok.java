package com.thunder.ticktoklib.Core;

import com.thunder.ticktoklib.TickTokConfig;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

/**
 * Main mod class for Tick Time Lib.
 */
@Mod(ModConstants.MOD_ID)

public class TickTok {

    /**
     * Instantiates a new Wilderness odyssey api main mod class.
     *
     * @param modEventBus the mod event bus
     * @param container   the container
     */
    public TickTok(IEventBus modEventBus, ModContainer container) {

        ModConstants.LOGGER.info("Initializing TickTok core module");

        // Register mod setup and creative tabs
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::addCreative);

        if (TickTokConfig.ENABLE_DEBUG_LOGGING.get() && ModConstants.LOGGER.isDebugEnabled()) {
            ModConstants.LOGGER.debug("TickTok constructor - registered listeners for FMLCommonSetupEvent and BuildCreativeModeTabContentsEvent");
        }

        // Register global events
        NeoForge.EVENT_BUS.register(this);

        if (TickTokConfig.ENABLE_DEBUG_LOGGING.get() && ModConstants.LOGGER.isDebugEnabled()) {
            ModConstants.LOGGER.debug("TickTok constructor - subscribed to NeoForge.EVENT_BUS with {}", this.getClass().getSimpleName());
        }

        container.registerConfig(ModConfig.Type.COMMON, TickTokConfig.SPEC);

        if (TickTokConfig.ENABLE_DEBUG_LOGGING.get() && ModConstants.LOGGER.isDebugEnabled()) {
            ModConstants.LOGGER.debug("TickTok constructor - registered TickTokConfig.SPEC with ModConfig");
        }


    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> ModConstants.LOGGER.info("Tick Tok Lib setup complete via FMLCommonSetupEvent"));
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {


    }

    /**
     * On server starting.
     *
     * @param event the event
     */
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        if (TickTokConfig.ENABLE_DEBUG_LOGGING.get() && ModConstants.LOGGER.isDebugEnabled()) {
            ModConstants.LOGGER.debug("TickTok.onServerStarting triggered for server {}", event.getServer().getServerVersion());
        }
    }

    /**
     * On register commands.
     *
     * @param event the event
     */
    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        if (ModConstants.LOGGER.isTraceEnabled()) {
            ModConstants.LOGGER.trace("TickTok.onRegisterCommands invoked - delegating to command registration (none configured)");
        }
    }
}

