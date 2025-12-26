package com.thunder.ticktoklib.mixin;

import com.thunder.ticktoklib.TickTokLagFormatter;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {

    private static final String BEHIND_PATTERN = "Can't keep up! Is the server overloaded? Running {}ms or {} ticks behind";

    @Redirect(
            method = "tickServer",
            at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;warn(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V")
    )
    private void ticktoklib$rewriteLagWarning(Logger logger, String pattern, Object millisBehindObj, Object ticksBehindObj) {
        if (BEHIND_PATTERN.equals(pattern)) {
            long ticksBehind = ((Number) ticksBehindObj).longValue();
            long millisBehind = ((Number) millisBehindObj).longValue();
            logger.warn(TickTokLagFormatter.formatLagBehind(ticksBehind, millisBehind));
            return;
        }

        logger.warn(pattern, millisBehindObj, ticksBehindObj);
    }
}
