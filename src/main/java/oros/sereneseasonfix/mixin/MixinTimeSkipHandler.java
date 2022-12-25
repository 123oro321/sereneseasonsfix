package oros.sereneseasonfix.mixin;

import net.minecraftforge.event.TickEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sereneseasons.handler.season.TimeSkipHandler;

@Mixin(TimeSkipHandler.class)
public abstract class MixinTimeSkipHandler {
    @Inject(method = "onWorldTick", at = @At("HEAD"), remap = false, cancellable = true)
    private static void onWorldTick(TickEvent.LevelTickEvent event, CallbackInfo ci) {
        ci.cancel();
    }
}
