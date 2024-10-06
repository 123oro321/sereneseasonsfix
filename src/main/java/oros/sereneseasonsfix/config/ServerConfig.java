package oros.sereneseasonsfix.config;

import net.minecraftforge.common.ForgeConfigSpec;


public class ServerConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static ForgeConfigSpec.BooleanValue enable_override;
    public static ForgeConfigSpec.BooleanValue block_blacklisted_dimensions;


    static {
        BUILDER.push("general_settings");
        enable_override = BUILDER.comment("If Serene Season Fix alternate season logic should be used.").define("enable_season_time_override", true);
        block_blacklisted_dimensions = BUILDER.comment("If season ticking and commads on dimensions outside the whitelist should be disabled.").define("block_blacklisted", true);
        BUILDER.pop();

        SPEC = BUILDER.build();
    }
}
