package oros.sereneseasonfix.command;

import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.commands.CommandRuntimeException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import sereneseasons.handler.season.SeasonHandler;
import sereneseasons.season.SeasonSavedData;
import sereneseasons.season.SeasonTime;

public class CommandDebugSeason {

    public CommandDebugSeason() {
    }

    static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("debug").executes((ctx) -> {
            Level world = (ctx.getSource()).getLevel();
            return debugSeason(ctx.getSource(), world);
        });
    }

    private static int debugSeason(CommandSourceStack cs, Level world) throws CommandRuntimeException {
        SeasonSavedData seasonData = SeasonHandler.getSeasonSavedData(world);
        int seasonTime = new SeasonTime(seasonData.seasonCycleTicks).getSeasonCycleTicks();
        long dayTime = world.getLevelData().getDayTime();
        cs.sendSuccess(() -> Component.translatable("commands.sereneseasonsfix.debugseason.info", seasonTime, dayTime, seasonTime - dayTime), true);
        return 1;
    }
}
