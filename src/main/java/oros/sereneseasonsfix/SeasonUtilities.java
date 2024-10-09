package oros.sereneseasonsfix;

import net.minecraft.world.level.Level;
import sereneseasons.config.SeasonsConfig;
import sereneseasons.season.SeasonSavedData;
import sereneseasons.season.SeasonTime;


public class SeasonUtilities {

    public static long calculateCycleTicks(long seasonCycleTicks) {
        int cycleDuration = SeasonTime.ZERO.getCycleDuration();
        return (seasonCycleTicks % cycleDuration + cycleDuration) % cycleDuration;
    }

    public static void setSeasonCycleTicks(SeasonSavedData seasonSavedData, long seasonCycleTicks) {
        seasonSavedData.seasonCycleTicks = (int) calculateCycleTicks(seasonCycleTicks);
        seasonSavedData.setDirty();
    }

    public static boolean isWorldWhitelisted(Level world) {
        return !oros.sereneseasonsfix.config.ServerConfig.block_blacklisted_dimensions.get() || SeasonsConfig.isDimensionWhitelisted(world.dimension());
    }
}
