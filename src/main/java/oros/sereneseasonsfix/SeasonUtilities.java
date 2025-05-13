package oros.sereneseasonsfix;

import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import oros.sereneseasonsfix.data.SeasonExtraSavedData;
import sereneseasons.config.ServerConfig;
import sereneseasons.season.SeasonSavedData;
import sereneseasons.season.SeasonTime;


public class SeasonUtilities {

    public static int calculateCycleTicks(long seasonCycleTicks) {
        return (int) Mth.positiveModulo(seasonCycleTicks ,SeasonTime.ZERO.getCycleDuration());
    }

    public static void setSeasonCycleTicks(SeasonSavedData seasonSavedData, long seasonCycleTicks) {
        seasonSavedData.seasonCycleTicks = calculateCycleTicks(seasonCycleTicks);
        seasonSavedData.setDirty();
    }

    public static void setSeasonStartCycleTicks(SeasonExtraSavedData seasonExtraSavedData, long seasonCycleTicks) {
        seasonExtraSavedData.startingSeasonCycleTicks = calculateCycleTicks(seasonCycleTicks);
        seasonExtraSavedData.setDirty();
    }

    public static boolean isWorldWhitelisted(Level world) {
        return !oros.sereneseasonsfix.config.ServerConfig.block_blacklisted_dimensions.get() || ServerConfig.isDimensionWhitelisted(world.dimension());
    }
}
