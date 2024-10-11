package oros.sereneseasonsfix.mixin;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import oros.sereneseasonsfix.SeasonUtilities;
import sereneseasons.api.season.Season;
import sereneseasons.command.CommandSetSeason;


@Mixin(CommandSetSeason.class)
public abstract class MixinCommandSetSeason {

    @Inject(method = "setSeason", at = @At("HEAD"), remap = false, cancellable = true)
    private static void setSeason(CommandSourceStack cs, Level world, Season.SubSeason season, CallbackInfoReturnable<Integer> cir) {
        if (!SeasonUtilities.isWorldWhitelisted(world)) {
            cs.sendSuccess(() -> Component.translatable("commands.sereneseasonsfix.get_season.not_whitelisted"), true);
            cir.setReturnValue(1);
        }
    }
}
