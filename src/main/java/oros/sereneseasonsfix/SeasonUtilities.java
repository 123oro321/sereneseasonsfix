package oros.sereneseasonsfix;

import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import sereneseasons.init.ModConfig;
import sereneseasons.season.SeasonSavedData;
import sereneseasons.season.SeasonTime;


public class SeasonUtilities {

    public static int calculateCycleTicks(long seasonCycleTicks) {
        int cycleDuration = SeasonTime.ZERO.getCycleDuration();
        return (int) Mth.positiveModulo(seasonCycleTicks, cycleDuration);
    }

    public static void setSeasonCycleTicks(SeasonSavedData seasonSavedData, long seasonCycleTicks) {
        seasonSavedData.seasonCycleTicks = calculateCycleTicks(seasonCycleTicks);
        seasonSavedData.setDirty();
    }

    public static boolean isWorldWhitelisted(Level level) {
        return !oros.sereneseasonsfix.config.ServerConfig.block_blacklisted_dimensions.get() || ModConfig.seasons.isDimensionWhitelisted(level.dimension());
    }
}
