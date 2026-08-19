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
 * Main mod class for Tick Tok Lib.
 */
@Mod(ModConstants.MOD_ID)
public class TickTok {

    private final TickTokPhaseTracker phaseTracker = new TickTokPhaseTracker();

    /**
     * Initializes Tick Tok Lib.
     *
     * @param modEventBus the mod event bus
     * @param container   the mod container
     */
    public TickTok(IEventBus modEventBus, ModContainer container) {

        /*
         * Register the config before any TickTok systems attempt to read it.
         *
         * This prevents early lifecycle accesses from querying an unregistered
         * ModConfigSpec and producing "Config unavailable" warnings.
         */
        container.registerConfig(ModConfig.Type.COMMON, TickTokConfig.SPEC);

        ModConstants.LOGGER.info("Initializing TickTok core module");

        // Register mod lifecycle listeners.
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::addCreative);

        /*
         * Do not query TickTokConfig here.
         *
         * The config has been registered, but NeoForge may not have finished
         * loading its actual values yet during the mod constructor.
         *
         * Normal Log4j debug logging is safe here because it does not depend
         * on TickTok's config lifecycle.
         */
        if (ModConstants.LOGGER.isDebugEnabled()) {
            ModConstants.LOGGER.debug(
                    "TickTok constructor - registered listeners for FMLCommonSetupEvent and BuildCreativeModeTabContentsEvent"
            );
        }

        // Register NeoForge gameplay/server events.
        NeoForge.EVENT_BUS.register(this);

        if (ModConstants.LOGGER.isDebugEnabled()) {
            ModConstants.LOGGER.debug(
                    "TickTok constructor - subscribed to NeoForge.EVENT_BUS with {}",
                    this.getClass().getSimpleName()
            );

            ModConstants.LOGGER.debug(
                    "TickTok constructor - registered TickTokConfig.SPEC with ModConfig"
            );
        }
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() ->
                ModConstants.LOGGER.info(
                        "Tick Tok Lib setup complete via FMLCommonSetupEvent"
                )
        );
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        // Reserved for future creative tab registration.
    }

    /**
     * Called when the server starts.
     *
     * @param event server starting event
     */
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

        /*
         * Check the logger first.
         *
         * Because Java short-circuits && from left to right, TickTokConfig
         * will not be queried at all unless debug logging is actually enabled.
         */
        if (ModConstants.LOGGER.isDebugEnabled()
                && TickTokConfig.isDebugLoggingEnabled()) {

            ModConstants.LOGGER.debug(
                    "TickTok.onServerStarting triggered for server {}",
                    event.getServer().getServerVersion()
            );
        }
    }

    /**
     * Registers TickTok commands.
     *
     * @param event command registration event
     */
    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {

        LiteralArgumentBuilder<CommandSourceStack> builder =
                Commands.literal("ticktok")
                        .requires(source -> source.hasPermission(2))

                        .then(Commands.literal("now")
                                .executes(ctx -> {

                                    long dayTime =
                                            ctx.getSource()
                                                    .getLevel()
                                                    .getDayTime();

                                    ctx.getSource().sendSuccess(
                                            () -> TickTokAPI.buildPhaseReport(dayTime),
                                            true
                                    );

                                    return 1;
                                }))

                        .then(Commands.literal("convert")
                                .then(Commands.argument(
                                                "ticks",
                                                LongArgumentType.longArg(0)
                                        )
                                        .executes(ctx -> {

                                            long ticks =
                                                    LongArgumentType.getLong(
                                                            ctx,
                                                            "ticks"
                                                    );

                                            ctx.getSource().sendSuccess(
                                                    () -> TickTokAPI.buildConversionReport(ticks),
                                                    false
                                            );

                                            return 1;
                                        })));

        event.getDispatcher().register(builder);

        if (ModConstants.LOGGER.isTraceEnabled()) {
            ModConstants.LOGGER.trace(
                    "TickTok.onRegisterCommands registered /ticktok helpers"
            );
        }
    }

    /**
     * Handles TickTok's per-level tick tracking.
     *
     * @param event level tick event
     */
    @SubscribeEvent
    public void onLevelTick(LevelTickEvent.Post event) {

        long dayTime = event.getLevel().getDayTime();

        phaseTracker.handle(
                dayTime,
                event.getLevel().dimension()
        );
    }
}