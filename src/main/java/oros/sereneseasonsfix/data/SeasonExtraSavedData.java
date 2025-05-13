package oros.sereneseasonsfix.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import org.jetbrains.annotations.NotNull;
import sereneseasons.season.SeasonTime;

import java.util.function.Supplier;

public class SeasonExtraSavedData extends SavedData {
    public static final String DATA_IDENTIFIER = "seasons_ex";
    public static final int VERSION = 0;
    public int startingSeasonCycleTicks;

    public @NotNull CompoundTag save(CompoundTag nbt) {
        nbt.putInt("StartingSeasonCycleTicks", this.startingSeasonCycleTicks);
        return nbt;
    }

    public static SeasonExtraSavedData load(CompoundTag nbt) {
        SeasonExtraSavedData data = new SeasonExtraSavedData();
        data.startingSeasonCycleTicks = Mth.clamp(nbt.getInt("StartingSeasonCycleTicks"), 0, SeasonTime.ZERO.getCycleDuration());
        return data;
    }

    public static SeasonExtraSavedData getExtraSeasonSavedData(Level w) {
        if (!w.isClientSide() && w instanceof ServerLevel world) {
            DimensionDataStorage saveDataManager = world.getChunkSource().getDataStorage();
            Supplier<SeasonExtraSavedData> defaultSaveDataSupplier = () -> {
                SeasonExtraSavedData savedData = new SeasonExtraSavedData();
                savedData.startingSeasonCycleTicks = 0;
                savedData.setDirty();
                return savedData;
            };
            return (SeasonExtraSavedData)saveDataManager.computeIfAbsent(new SavedData.Factory<>(defaultSaveDataSupplier, SeasonExtraSavedData::load, DataFixTypes.LEVEL),"seasons_ex");        } else {
            return null;
        }
    }
}
