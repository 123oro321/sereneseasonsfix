package oros.sereneseasonsfix.data;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.WorldSavedData;

import java.util.function.Supplier;

public class SeasonExtraSavedData extends WorldSavedData {
    public static final String DATA_IDENTIFIER = "seasons_ex";
    public static final int VERSION = 0;
    public int startingSeasonCycleTicks;

    public SeasonExtraSavedData() {
        this("ex_seasons");
    }

    public SeasonExtraSavedData(String identifier) {
        super(identifier);
    }

    public void load(CompoundNBT nbt) {
        this.startingSeasonCycleTicks = nbt.getInt("StartingSeasonCycleTicks");
    }

    public CompoundNBT save(CompoundNBT nbt) {
        nbt.putInt("StartingSeasonCycleTicks", this.startingSeasonCycleTicks);
        return nbt;
    }


    public static SeasonExtraSavedData getExtraSeasonSavedData(World w) {
        if (!w.isClientSide() && w instanceof ServerWorld) {
            ServerWorld world = (ServerWorld)w;
            DimensionSavedDataManager saveDataManager = world.getChunkSource().getDataStorage();
            Supplier<SeasonExtraSavedData> defaultSaveDataSupplier = () -> {
                SeasonExtraSavedData savedData = new SeasonExtraSavedData();
                savedData.startingSeasonCycleTicks = 0;
                savedData.setDirty();
                return savedData;
            };
            return (SeasonExtraSavedData)saveDataManager.computeIfAbsent(defaultSaveDataSupplier, "ex_seasons");
        } else {
            return null;
        }
    }
}