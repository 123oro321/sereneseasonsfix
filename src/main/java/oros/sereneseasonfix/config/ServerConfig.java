package oros.sereneseasonfix.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ServerConfig
{
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static ForgeConfigSpec.BooleanValue enable_overwrite;

    static
    {
        BUILDER.push("general_settings");
        enable_overwrite = BUILDER.comment("If Serene Season Fix alternate season logic should be used").define("enable_season_time_overwrite", true);
        BUILDER.pop();

        SPEC = BUILDER.build();
    }
}
