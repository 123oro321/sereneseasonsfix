package oros.sereneseasonfix.mixin;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
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
import sereneseasons.config.SeasonsConfig;

import java.util.HashMap;

import static oros.sereneseasonfix.Sereneseasonfix.LOGGER;

@Mixin(SeasonHandler.class)
public abstract class MixinSeasonHandler implements SeasonHelper.ISeasonDataProvider {
    private static final HashMap<Level, Long> lastDayTimes = new HashMap<>();
    private static final HashMap<Level, Integer> tickSinceLastUpdate = new HashMap<>();

    /**
     * @author Or_OS
     * @reason Merged with TimeSkipHandler and modified the logic to be more consistent
     */
    @Overwrite(remap = false)
    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event) {
        Level world = event.world;

        // Tick only for world server within which is whitelisted
        if (event.phase == TickEvent.Phase.END && !world.isClientSide() && SeasonsConfig.isDimensionWhitelisted(world.dimension())) {

            long dayTime = world.getLevelData().getDayTime();

            long lastDayTime = lastDayTimes.get(world);
            lastDayTimes.put(world, dayTime);

            if (!SyncedConfig.getBooleanValue(SeasonsOption.PROGRESS_SEASON_WHILE_OFFLINE)) {
                MinecraftServer server = world.getServer();
                if (server != null && server.getPlayerList().getPlayerCount() == 0)
                    return;
            }

            // Only tick seasons if the game rule is enabled
            if (!world.getGameRules().getBoolean(SSGameRules.RULE_DOSEASONCYCLE))
                return;

            SeasonSavedData savedData = SeasonHandler.getSeasonSavedData(world);

            long difference = dayTime - lastDayTime;

            // Skip if there is no difference
            if (difference == 0) {
                return;
            }
            int cycle_duration = SeasonTime.ZERO.getCycleDuration();
            savedData.seasonCycleTicks = (int) (((savedData.seasonCycleTicks + difference) % cycle_duration + cycle_duration) % cycle_duration);

            Integer tick = tickSinceLastUpdate.get(world);
            if (tick >= 20) {
                SeasonHandler.sendSeasonUpdate(world);
                tick %= 20;
            }
            tickSinceLastUpdate.put(world,tick + 1);
            savedData.setDirty();
        }
    }
    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event)
    {
        Level world = (Level) event.getWorld();
        if (!world.isClientSide() && SeasonsConfig.isDimensionWhitelisted(world.dimension())) {
            LOGGER.info("Setting cached parameters");
            lastDayTimes.put(world, world.getLevelData().getDayTime());
            tickSinceLastUpdate.put(world, 0);
        }
    }
}
