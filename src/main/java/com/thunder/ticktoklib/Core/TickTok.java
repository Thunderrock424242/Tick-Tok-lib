package com.thunder.ticktoklib.Core;

import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.thunder.ticktoklib.TickTokConfig;
import com.thunder.ticktoklib.api.TickTokAPI;
import com.thunder.ticktoklib.util.TickTokPhaseTracker;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
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
import net.neoforged.neoforge.event.tick.LevelTickEvent;

/**
 * Main mod class for Tick Time Lib.
 */
@Mod(ModConstants.MOD_ID)

public class TickTok {

    private static final int TICK_OPTIMIZATION_INTERVAL = 20;

    private final TickTokPhaseTracker phaseTracker = new TickTokPhaseTracker();

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

        if (TickTokConfig.isDebugLoggingEnabled() && ModConstants.LOGGER.isDebugEnabled()) {
            ModConstants.LOGGER.debug("TickTok constructor - registered listeners for FMLCommonSetupEvent and BuildCreativeModeTabContentsEvent");
        }

        // Register global events
        NeoForge.EVENT_BUS.register(this);

        if (TickTokConfig.isDebugLoggingEnabled() && ModConstants.LOGGER.isDebugEnabled()) {
            ModConstants.LOGGER.debug("TickTok constructor - subscribed to NeoForge.EVENT_BUS with {}", this.getClass().getSimpleName());
        }

        container.registerConfig(ModConfig.Type.COMMON, TickTokConfig.SPEC);

        if (TickTokConfig.isDebugLoggingEnabled() && ModConstants.LOGGER.isDebugEnabled()) {
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
        if (TickTokConfig.isDebugLoggingEnabled() && ModConstants.LOGGER.isDebugEnabled()) {
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
        LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal("ticktok")
                .requires(source -> source.hasPermission(2))
                .then(Commands.literal("now")
                        .executes(ctx -> {
                            long dayTime = ctx.getSource().getLevel().getDayTime();
                            ctx.getSource().sendSuccess(() -> TickTokAPI.buildPhaseReport(dayTime), true);
                            return 1;
                        }))
                .then(Commands.literal("convert")
                        .then(Commands.argument("ticks", LongArgumentType.longArg(0))
                                .executes(ctx -> {
                                    long ticks = LongArgumentType.getLong(ctx, "ticks");
                                    ctx.getSource().sendSuccess(() -> TickTokAPI.buildConversionReport(ticks), false);
                                    return 1;
                                })));

        event.getDispatcher().register(builder);

        if (ModConstants.LOGGER.isTraceEnabled()) {
            ModConstants.LOGGER.trace("TickTok.onRegisterCommands registered /ticktok helpers");
        }
    }

    @SubscribeEvent
    public void onLevelTick(LevelTickEvent.Post event) {
        long dayTime = event.getLevel().getDayTime();

        if (TickTokConfig.isTickOptimizationEnabled()) {
            if (Math.floorMod(dayTime, TICK_OPTIMIZATION_INTERVAL) != 0) {
                return;
            }
        }

        phaseTracker.handle(dayTime, event.getLevel().dimension());
    }
}
