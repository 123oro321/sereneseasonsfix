package oros.sereneseasonfix.mixin;

import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sereneseasons.command.CommandGetSeason;
import sereneseasons.config.SeasonsConfig;

@Mixin(CommandGetSeason.class)
public abstract class MixinCommandGetSeason {

    @Inject(method = "getSeason", at= @At("HEAD"), remap = false, cancellable = true)
    private static void getSeason(CommandSource cs, World world, CallbackInfoReturnable<Integer> cir) throws CommandException {
        if (!SeasonsConfig.isDimensionWhitelisted(world.getDimensionKey())) {
            cs.sendFeedback(new TranslationTextComponent("commands.sereneseasonsfix.getseason.notwhitelisted"), true);
            cir.setReturnValue(1);
        }
    }
}
