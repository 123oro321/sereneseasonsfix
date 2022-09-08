package oros.sereneseasonfix.mixin;

import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sereneseasons.api.season.Season;
import sereneseasons.command.CommandSetSeason;
import sereneseasons.config.SeasonsConfig;

@Mixin(CommandSetSeason.class)
public abstract class MixinCommandSetSeason {

    @Inject(method = "setSeason", at= @At("HEAD"), remap = false, cancellable = true)
    private static void setSeason(CommandSource cs, World world, Season.SubSeason season, CallbackInfoReturnable<Integer> cir) throws CommandException {
        if (!SeasonsConfig.isDimensionWhitelisted(world.getDimensionKey())) {
            cs.sendFeedback(new TranslationTextComponent("commands.sereneseasonsfix.setseason.notwhitelisted"), true);
            cir.setReturnValue(1);
        }
    }
}
