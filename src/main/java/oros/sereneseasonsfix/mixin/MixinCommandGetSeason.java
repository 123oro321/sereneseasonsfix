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
import sereneseasons.command.CommandGetSeason;


@Mixin(CommandGetSeason.class)
public abstract class MixinCommandGetSeason {

    @Inject(method = "getSeason", at = @At("HEAD"), remap = false, cancellable = true)
    private static void getSeason(CommandSource cs, World world, CallbackInfoReturnable<Integer> cir) throws CommandException {
        if (!SeasonUtilities.isWorldWhitelisted(world)) {
            cs.sendSuccess(new TranslationTextComponent("commands.sereneseasonsfix.get_season.not_whitelisted"), true);
            cir.setReturnValue(1);
        }
    }
}
