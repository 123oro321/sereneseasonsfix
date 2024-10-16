package oros.sereneseasonsfix.mixin;

import net.minecraft.commands.CommandRuntimeException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import oros.sereneseasonsfix.SeasonUtilities;
import sereneseasons.command.CommandGetSeason;


@Mixin(CommandGetSeason.class)
public abstract class MixinCommandGetSeason {

    @Inject(method = "getSeason", at = @At("HEAD"), remap = false, cancellable = true)
    private static void getSeason(CommandSourceStack cs, Level world, CallbackInfoReturnable<Integer> cir) throws CommandRuntimeException {
        if (!SeasonUtilities.isWorldWhitelisted(world)) {
            cs.sendSuccess(() -> Component.translatable("commands.sereneseasonsfix.get_season.not_whitelisted"), true);
            cir.setReturnValue(1);
        }
    }
}
