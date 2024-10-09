package oros.sereneseasonsfix.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.TimeArgument;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import oros.sereneseasonsfix.SeasonUtilities;
import sereneseasons.config.SeasonsConfig;
import sereneseasons.handler.season.SeasonHandler;
import sereneseasons.season.SeasonSavedData;


@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SeasonTimeCommands {
    @SubscribeEvent
    public static void onCommandsRegistered(RegisterCommandsEvent event) {
        event.getDispatcher().register(
                LiteralArgumentBuilder.<CommandSource>literal("season")
                        .then(LiteralArgumentBuilder.<CommandSource>literal("time")
                                .requires(cs -> cs.hasPermission(2))
                                .then(Commands.literal("info").executes((ctx) -> {
                                    World world = (ctx.getSource()).getLevel();
                                    return infoSeasonTime(ctx.getSource(), world);
                                }))
                                .then(Commands.literal("sync").executes((ctx) -> {
                                    World world = (ctx.getSource()).getLevel();
                                    return syncSeasonTime(ctx.getSource(), world);
                                }))
                                .then(Commands.literal("set").then(Commands.argument("time", TimeArgument.time()).executes((ctx) -> {
                                    World world = (ctx.getSource()).getLevel();
                                    return setSeasonTime(ctx.getSource(), world, IntegerArgumentType.getInteger(ctx, "time"));
                                })))
                                .then(Commands.literal("add").then(Commands.argument("time", TimeArgument.time()).executes((ctx) -> {
                                    World world = (ctx.getSource()).getLevel();
                                    return addSeasonTime(ctx.getSource(), world, IntegerArgumentType.getInteger(ctx, "time"));
                                })))
                        )
        );
    }

    private static int infoSeasonTime(CommandSource cs, World world) throws CommandException {
        SeasonSavedData seasonData = SeasonHandler.getSeasonSavedData(world);
        int seasonTime = seasonData.seasonCycleTicks;
        long dayTime = world.getLevelData().getDayTime();
        long delta = seasonTime - SeasonUtilities.calculateCycleTicks(dayTime);
        boolean whitelisted = SeasonsConfig.isDimensionWhitelisted(world.dimension());
        cs.sendSuccess(new TranslationTextComponent("commands.sereneseasonsfix.time.info", seasonTime, dayTime, delta, whitelisted), true);
        return (int) delta;
    }

    private static int syncSeasonTime(CommandSource cs, World world) throws CommandException {
        if (SeasonUtilities.isWorldWhitelisted(world)) {
            SeasonSavedData seasonData = SeasonHandler.getSeasonSavedData(world);
            SeasonUtilities.setSeasonCycleTicks(seasonData, world.getLevelData().getDayTime());
            SeasonHandler.sendSeasonUpdate(world);
            cs.sendSuccess(new TranslationTextComponent("commands.sereneseasonsfix.time.sync_season.success"), true);
            return seasonData.seasonCycleTicks;
        } else {
            cs.sendSuccess(new TranslationTextComponent("commands.sereneseasonsfix.time.sync_season.not_whitelisted"), true);
            return -1;
        }
    }

    private static int setSeasonTime(CommandSource cs, World world, int time) throws CommandException {
        if (SeasonUtilities.isWorldWhitelisted(world)) {
            SeasonSavedData seasonData = SeasonHandler.getSeasonSavedData(world);
            SeasonUtilities.setSeasonCycleTicks(seasonData, time);
            SeasonHandler.sendSeasonUpdate(world);
            cs.sendSuccess(new TranslationTextComponent("commands.sereneseasonsfix.time.set_season.success", seasonData.seasonCycleTicks), true);
            return seasonData.seasonCycleTicks;
        } else {
            cs.sendSuccess(new TranslationTextComponent("commands.sereneseasonsfix.time.set_season.not_whitelisted"), true);
            return -1;
        }
    }

    private static int addSeasonTime(CommandSource cs, World world, int time) throws CommandException {
        if (SeasonUtilities.isWorldWhitelisted(world)) {
            SeasonSavedData seasonData = SeasonHandler.getSeasonSavedData(world);
            SeasonUtilities.setSeasonCycleTicks(seasonData, seasonData.seasonCycleTicks + time);
            SeasonHandler.sendSeasonUpdate(world);
            cs.sendSuccess(new TranslationTextComponent("commands.sereneseasonsfix.time.set_season.success", seasonData.seasonCycleTicks), true);
            return seasonData.seasonCycleTicks;
        } else {
            cs.sendSuccess(new TranslationTextComponent("commands.sereneseasonsfix.time.set_season.not_whitelisted"), true);
            return -1;
        }
    }
}