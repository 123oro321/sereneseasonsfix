package oros.sereneseasonfix.core;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import oros.sereneseasonfix.init.ModConfig;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Sereneseasonfix.MOD_ID)
public class Sereneseasonfix {
    public static final String MOD_ID = "sereneseasonfix";

    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public Sereneseasonfix() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::commonSetup);
        bus.addListener(this::clientSetup);
        bus.addListener(this::loadComplete);

        ModConfig.init();
    }

    private void clientSetup(final FMLClientSetupEvent event)
    {
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
    }

    private void loadComplete(final FMLLoadCompleteEvent event)
    {
    }
}
