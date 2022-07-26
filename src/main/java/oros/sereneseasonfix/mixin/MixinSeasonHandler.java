package oros.sereneseasonfix.mixin;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import oros.sereneseasonfix.Sereneseasonfix;
import sereneseasons.api.SSGameRules;
import sereneseasons.api.config.SeasonsOption;
import sereneseasons.api.config.SyncedConfig;
import sereneseasons.api.season.SeasonHelper;
import sereneseasons.handler.season.SeasonHandler;
import sereneseasons.season.SeasonSavedData;
import sereneseasons.season.SeasonTime;

import java.util.HashMap;

@Mixin(SeasonHandler.class)
public abstract class MixinSeasonHandler implements SeasonHelper.ISeasonDataProvider {
    private static final HashMap<World, Long> lastDayTimes = new HashMap<>();

    /**
     * @author Or_OS
     * @reason It's broken m8
     */
    @Overwrite(remap = false)
    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event) {
        World world = event.world;

        if (event.phase == TickEvent.Phase.END && !world.isRemote()) {
            long dayTime = world.getWorldInfo().getDayTime();

            long lastDayTime = lastDayTimes.get(world);
            lastDayTimes.put(world, dayTime);

            if (!SyncedConfig.getBooleanValue(SeasonsOption.PROGRESS_SEASON_WHILE_OFFLINE)) {
                MinecraftServer server = world.getServer();
                if (server != null && server.getPlayerList().getCurrentPlayerCount() == 0)
                    return;
            }

            // Only tick seasons if the game rule is enabled
            if (!world.getGameRules().getBoolean(SSGameRules.RULE_DOSEASONCYCLE))
                return;

            SeasonSavedData savedData = SeasonHandler.getSeasonSavedData(world);

            long difference = dayTime - lastDayTime; // Make sure seasons change correctly at the right time, you can add to the time 1d without a problem,
            // World rejoin add few ticks worth of desync! Might have something to do with lastDayTimes. Need to find a way to purge when world closed.
            // Skip if there is no difference
            if (difference == 0) {
                return;
            }

            savedData.seasonCycleTicks += difference;
            savedData.seasonCycleTicks %= SeasonTime.ZERO.getCycleDuration();

            if (world.getGameTime() % 20 == 0) {
                SeasonHandler.sendSeasonUpdate(world);
            }
            savedData.markDirty();
        }
    }
    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event)
    {
        World world = (World) event.getWorld();
        lastDayTimes.put(world, world.getWorldInfo().getDayTime());
    }
}
