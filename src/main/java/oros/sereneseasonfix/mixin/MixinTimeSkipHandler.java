package oros.sereneseasonfix.mixin;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import sereneseasons.handler.season.TimeSkipHandler;

@Mixin(TimeSkipHandler.class)
public abstract class MixinTimeSkipHandler {
    /**
     * @author Or_OS
     * @reason Moved to SeasonHandler cause why keep the SAME LOGIC SEPARATED
     */
    @Overwrite(remap = false)
    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event) {
    }
}
