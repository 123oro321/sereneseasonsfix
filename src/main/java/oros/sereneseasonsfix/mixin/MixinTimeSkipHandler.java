package oros.sereneseasonsfix.mixin;

import net.minecraftforge.event.TickEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sereneseasons.handler.season.TimeSkipHandler;
import oros.sereneseasonsfix.config.ServerConfig;


@Mixin(TimeSkipHandler.class)
public abstract class MixinTimeSkipHandler {
    @Inject(method = "onWorldTick", at = @At("HEAD"), remap = false, cancellable = true)
    private void onWorldTick(TickEvent.WorldTickEvent event, CallbackInfo ci) {
        if (ServerConfig.enable_override.get()) {
            ci.cancel();
        }
    }
}
