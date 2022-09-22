package oros.sereneseasonfix.mixin;

import net.minecraft.commands.CommandRuntimeException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
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
    private static void setSeason(CommandSourceStack cs, Level world, Season.SubSeason season, CallbackInfoReturnable<Integer> cir) throws CommandRuntimeException {
        if (!SeasonsConfig.isDimensionWhitelisted(world.dimension())) {
            cs.sendSuccess(new TranslatableComponent("commands.sereneseasonsfix.setseason.notwhitelisted"), true);
            cir.setReturnValue(1);
        }
    }
}
