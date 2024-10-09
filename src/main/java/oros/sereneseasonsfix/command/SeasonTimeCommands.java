package oros.sereneseasonsfix.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandRuntimeException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.TimeArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import oros.sereneseasonsfix.SeasonUtilities;
import sereneseasons.config.ServerConfig;
import sereneseasons.handler.season.SeasonHandler;
import sereneseasons.season.SeasonSavedData;


@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SeasonTimeCommands {
    @SubscribeEvent
    public static void onCommandsRegistered(RegisterCommandsEvent event) {
        event.getDispatcher().register(
                LiteralArgumentBuilder.<CommandSourceStack>literal("season")
                        .then(LiteralArgumentBuilder.<CommandSourceStack>literal("time")
                                .requires(cs -> cs.hasPermission(2))
                                .then(Commands.literal("info").executes((ctx) -> {
                                    Level world = (ctx.getSource()).getLevel();
                                    return infoSeasonTime(ctx.getSource(), world);
                                }))
                                .then(Commands.literal("sync").executes((ctx) -> {
                                    Level world = (ctx.getSource()).getLevel();
                                    return syncSeasonTime(ctx.getSource(), world);
                                }))
                                .then(Commands.literal("set").then(Commands.argument("time", TimeArgument.time()).executes((ctx) -> {
                                    Level world = (ctx.getSource()).getLevel();
                                    return setSeasonTime(ctx.getSource(), world, IntegerArgumentType.getInteger(ctx, "time"));
                                })))
                                .then(Commands.literal("add").then(Commands.argument("time", TimeArgument.time()).executes((ctx) -> {
                                    Level world = (ctx.getSource()).getLevel();
                                    return addSeasonTime(ctx.getSource(), world, IntegerArgumentType.getInteger(ctx, "time"));
                                })))
                        )
        );
    }

    private static int infoSeasonTime(CommandSourceStack cs, Level world) throws CommandRuntimeException {
        SeasonSavedData seasonData = SeasonHandler.getSeasonSavedData(world);
        int seasonTime = seasonData.seasonCycleTicks;
        long dayTime = world.getLevelData().getDayTime();
        long delta = seasonTime - SeasonUtilities.calculateCycleTicks(dayTime);
        boolean whitelisted = ServerConfig.isDimensionWhitelisted(world.dimension());
        cs.sendSuccess(Component.translatable("commands.sereneseasonsfix.time.info", seasonTime, dayTime, delta, whitelisted), true);
        return (int) delta;
    }

    private static int syncSeasonTime(CommandSourceStack cs, Level world) throws CommandRuntimeException {
        if (SeasonUtilities.isWorldWhitelisted(world)) {
            SeasonSavedData seasonData = SeasonHandler.getSeasonSavedData(world);
            SeasonUtilities.setSeasonCycleTicks(seasonData, world.getLevelData().getDayTime());
            SeasonHandler.sendSeasonUpdate(world);
            cs.sendSuccess(Component.translatable("commands.sereneseasonsfix.time.sync_season.success"), true);
            return seasonData.seasonCycleTicks;
        } else {
            cs.sendSuccess(Component.translatable("commands.sereneseasonsfix.time.sync_season.not_whitelisted"), true);
            return -1;
        }
    }

    private static int setSeasonTime(CommandSourceStack cs, Level world, int time) throws CommandRuntimeException {
        if (SeasonUtilities.isWorldWhitelisted(world)) {
            SeasonSavedData seasonData = SeasonHandler.getSeasonSavedData(world);
            SeasonUtilities.setSeasonCycleTicks(seasonData, time);
            SeasonHandler.sendSeasonUpdate(world);
            cs.sendSuccess(Component.translatable("commands.sereneseasonsfix.time.set_season.success", seasonData.seasonCycleTicks), true);
            return seasonData.seasonCycleTicks;
        } else {
            cs.sendSuccess(Component.translatable("commands.sereneseasonsfix.time.set_season.not_whitelisted"), true);
            return -1;
        }
    }

    private static int addSeasonTime(CommandSourceStack cs, Level world, int time) throws CommandRuntimeException {
        if (SeasonUtilities.isWorldWhitelisted(world)) {
            SeasonSavedData seasonData = SeasonHandler.getSeasonSavedData(world);
            SeasonUtilities.setSeasonCycleTicks(seasonData, seasonData.seasonCycleTicks + time);
            SeasonHandler.sendSeasonUpdate(world);
            cs.sendSuccess(Component.translatable("commands.sereneseasonsfix.time.set_season.success", seasonData.seasonCycleTicks), true);
            return seasonData.seasonCycleTicks;
        } else {
            cs.sendSuccess(Component.translatable("commands.sereneseasonsfix.time.set_season.not_whitelisted"), true);
            return -1;
        }
    }
}