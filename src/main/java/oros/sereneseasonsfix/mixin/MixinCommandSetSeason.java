package oros.sereneseasonsfix.mixin;

import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
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
    private static void setSeason(CommandSource cs, World world, Season.SubSeason season, CallbackInfoReturnable<Integer> cir) throws CommandException {
        if (!SeasonUtilities.isWorldWhitelisted(world)) {
            cs.sendSuccess(new TranslationTextComponent("commands.sereneseasonsfix.get_season.not_whitelisted"), true);
            cir.setReturnValue(1);
        }
    }
}
