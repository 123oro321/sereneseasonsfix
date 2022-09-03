package oros.sereneseasonfix.mixin;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import sereneseasons.api.SSGameRules;
import sereneseasons.api.config.SeasonsOption;
import sereneseasons.api.config.SyncedConfig;
import sereneseasons.api.season.SeasonHelper;
import sereneseasons.handler.season.SeasonHandler;
import sereneseasons.season.SeasonSavedData;
import sereneseasons.season.SeasonTime;

import static oros.sereneseasonfix.Sereneseasonfix.LOGGER;

@Mixin(SeasonHandler.class)
public abstract class MixinSeasonHandler implements SeasonHelper.ISeasonDataProvider {
    private static Long lastDayTime;
    private static Integer tickSinceLastUpdate;

    /**
     * @author Or_OS
     * @reason It's broken m8
     */
    @Overwrite(remap = false)
    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event) {
        World world = event.world;

        if (event.phase == TickEvent.Phase.END && !world.isRemote() && World.OVERWORLD.equals(world.getDimensionKey())) {
            long dayTime = world.getWorldInfo().getDayTime();

            long tmpLastDayTime = lastDayTime;
            lastDayTime = dayTime;

            if (!SyncedConfig.getBooleanValue(SeasonsOption.PROGRESS_SEASON_WHILE_OFFLINE)) {
                MinecraftServer server = world.getServer();
                if (server != null && server.getPlayerList().getCurrentPlayerCount() == 0)
                    return;
            }

            // Only tick seasons if the game rule is enabled
            if (!world.getGameRules().getBoolean(SSGameRules.RULE_DOSEASONCYCLE))
                return;

            SeasonSavedData savedData = SeasonHandler.getSeasonSavedData(world);

            long difference = dayTime - tmpLastDayTime;

            // Skip if there is no difference
            if (difference == 0) {
                return;
            }
            int cycle_duration = SeasonTime.ZERO.getCycleDuration();
            savedData.seasonCycleTicks = (int) (((savedData.seasonCycleTicks + difference) % cycle_duration + cycle_duration) % cycle_duration);

            if (tickSinceLastUpdate++ >= 20) {
                SeasonHandler.sendSeasonUpdate(world);
                tickSinceLastUpdate %= 20;
            }
            savedData.markDirty();
        }
    }
    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event)
    {
        World world = (World) event.getWorld();
        if (!world.isRemote() && World.OVERWORLD.equals(world.getDimensionKey())) {
            LOGGER.info("Setting cached parameters");
            lastDayTime = world.getWorldInfo().getDayTime();
            tickSinceLastUpdate = 0;
        }
    }
}
