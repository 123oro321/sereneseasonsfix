package oros.sereneseasonsfix.mixin;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import oros.sereneseasonsfix.SeasonUtilities;
import sereneseasons.api.SSGameRules;
import sereneseasons.api.season.SeasonHelper;
import sereneseasons.config.ServerConfig;
import sereneseasons.handler.season.SeasonHandler;
import sereneseasons.season.SeasonSavedData;

import java.util.HashMap;

import oros.sereneseasonsfix.core.Sereneseasonsfix;


@Mixin(SeasonHandler.class)
public abstract class MixinSeasonHandler implements SeasonHelper.ISeasonDataProvider {
    @Unique
    private static final HashMap<Level, Long> sereneseasonsfix$lastDayTimes = new HashMap<>();
    @Unique
    private static final HashMap<Level, Integer> sereneseasonsfix$tickSinceLastUpdate = new HashMap<>();

    @Inject(method = "onWorldTick", at = @At("HEAD"), remap = false, cancellable = true)
    public void onWorldTick(TickEvent.WorldTickEvent event, CallbackInfo ci) {
        if (oros.sereneseasonsfix.config.ServerConfig.enable_override.get()) {
            ci.cancel();

            Level world = event.world;

            // Tick only for whitelisted worlds
            if (event.phase == TickEvent.Phase.END && !world.isClientSide() && SeasonUtilities.isWorldWhitelisted(world)) {

                long dayTime = world.getLevelData().getDayTime();

                long lastDayTime = sereneseasonsfix$lastDayTimes.get(world);
                sereneseasonsfix$lastDayTimes.put(world, dayTime);

                if (!(Boolean) ServerConfig.progressSeasonWhileOffline.get()) {
                    MinecraftServer server = world.getServer();
                    if (server != null && server.getPlayerList().getPlayerCount() == 0)
                        return;
                }

                // Only tick seasons if the game rule is enabled
                if (!world.getGameRules().getBoolean(SSGameRules.RULE_DOSEASONCYCLE))
                    return;

                long difference = dayTime - lastDayTime;

                // Skip if there is no difference
                if (difference == 0) {
                    return;
                }

                SeasonSavedData savedData = SeasonHandler.getSeasonSavedData(world);
                SeasonUtilities.setSeasonCycleTicks(savedData, savedData.seasonCycleTicks + difference);

                Integer tick = sereneseasonsfix$tickSinceLastUpdate.get(world);
                if (tick >= 20) {
                    SeasonHandler.sendSeasonUpdate(world);
                    tick %= 20;
                }
                sereneseasonsfix$tickSinceLastUpdate.put(world, tick + 1);
            }
        }
    }

    @Unique
    @SubscribeEvent
    public void sereneseasonsfix$onWorldLoad(WorldEvent.Load event) {
        Level world = (Level) event.getWorld();
        if (SeasonUtilities.isWorldWhitelisted(world)) {
            Sereneseasonsfix.LOGGER.info("Setting cached parameters");
            sereneseasonsfix$lastDayTimes.put(world, world.getLevelData().getDayTime());
            sereneseasonsfix$tickSinceLastUpdate.put(world, 0);
        }
    }

    @Unique
    @SubscribeEvent
    public void sereneseasonsfix$onWorldUnload(WorldEvent.Unload event) {
        Level world = (Level) event.getWorld();
        if (SeasonUtilities.isWorldWhitelisted(world)) {
            Sereneseasonsfix.LOGGER.info("Clearing cached parameters");
            sereneseasonsfix$lastDayTimes.remove(world);
            sereneseasonsfix$tickSinceLastUpdate.remove(world);
        }
    }
}
